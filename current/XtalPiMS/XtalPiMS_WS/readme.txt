Notes On Web Service Code Generation
====================================
Jon Diprose, 15 December 2010
Updated 20 May 2011

1. The project's location on disk must be within the Eclipse workspace. If it is located outside the workspace
then the code generation wizard fails to fully populate itself and then fails with an uninformative error. This
is a bug in the Eclipse code generation wizard which I don't have time to track down.

UPDATE: The above explanation is rubbish. Moving the project back under the workspace didn't fix the problem. It
looks very much like it was the creation of a temporary dynamic web project (in the web space) that was the actual
fix. So, create the project, add the Axis facet and the first time you run the code generation(per Eclipse start?),
choose the temporary project on page 1 of the wizard, go to page2 and ensure its populated, go back to page 1 and
select the correct project. Interestingly, subsequent attempts to delete the temporary project indicate that
Eclipse is holding open some jars (including activation-1.1.jar) under the temporary project's WEB-INF/lib. So,
still an Eclipse bug but not as previously thought. My "temporary" project is now a permanent fixture for the
purposes of running the code generation.

2. The wsdl file to edit is XtalPiMS_WS\xtalpims-for-codegen.wsdl.

3. To regenerate the code, right-click on the wsdl file and select Web Services -> Generate Java bean skeleton.
I run the wizard in Top down Java bean Web Service mode with the service generation level set to Assemble service,
Tomcat6, Axis2 runtime, XtalPiMS_WS project, no client generation and Overwrite files without warning ticked. Page
two should populate itself automatically - see point 1 if not. I use WSPlate, WSPlateSOAP12port_http, ADB,
uk.ac.ox.oppf.www.wsplate and Generate an interface for the skeleton ticked.

Note that the code generation will nuke any changes to relevant files in uk.ox.ac.oppf.www.wsplate. All implementation
code should go in a different package - I use uk.ox.ac.oppf.www.wsplate.impl.

Note also that it looks like the code generation attempts to delete and recreate some portion of the axis2 services
folder - probably /XtalPiMS_WS/web/WEB-INF/services/WSPlate/META-INF. This fails due to the presence of read-only files
in the .svn subfolder but the appropriate updates are made anyway. This folder is built into the .war and only created
at code generation time (rather than general build time) so I'm not inclined to omit it from svn.

4. After each rebuild you need to make some changes to /XtalPiMS_WS/web/WEB-INF/services/WSPlate/META-INF/services.xml:

4.1 The service element should have an additional attribute class that specifies the LifeCyle implementation:
  class="uk.ac.ox.oppf.www.wsplate.impl.WSPlateServiceLifeCycleImpl"

4.2 The ServiceClass parameter must be adjusted to point at the actual implementation:
        <parameter name="ServiceClass">uk.ac.ox.oppf.www.wsplate.impl.WSPlateImpl</parameter>

5. ServletContextListeners in the project are not visible to Tomcat, as they get built into the webservice container.
They need to be put in a separate jar - jars go into WEB-INF/lib and so are visible. Very annoying.

