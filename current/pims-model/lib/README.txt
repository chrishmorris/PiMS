pims-model dependencies
=======================

TODO Provide a pims maven repository for dependency management
TODO Investigate Eclipse plugin for Ivy

1) Download ant (currently 1.8.2), install, set ANT_HOME as appropriate and add $ANT_HOME/bin to your path.
2) If using the command line, download ivy (currently 2.2.0), copy ivy.jar into $ANT_HOME\lib.
If running Ant through Eclipse, use Window > Preferences ... Ant > Runtime ... Global Entries and click "Add JAR".
3) Make sure JAVA_HOME is set correctly (points at an installed JDK) and $JAVA_HOME/bin is in your path.
4) Make a new folder and create the following three files:

4.1) build.xml:
<project name="pims-model-ivy" default="resolve" xmlns:ivy="antlib:org.apache.ivy.ant">
    <ivy:settings file="ivysettings.xml"/>
    <!-- ================================= 
          target: resolve              
         ================================= -->
    <target name="resolve" description="--> retrieve dependencies with ivy">
    <ivy:retrieve sync="true"
        pattern="${ivy.lib.dir}/[conf]/[artifact]-[revision].[ext]"/>
    </target>
</project>

4.2) ivy.xml:
<?xml version="1.0" encoding="UTF-8"?>
<ivy-module version="2.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="http://ant.apache.org/ivy/schemas/ivy.xsd">
    <info
      organisation="www.pims-lims.org"
      module="pims-model"/>
    <configurations>
        <conf name="runtime" description="Runtime libs - needed by dependent projects" />
        <conf name="build" description="Buildtime libs - needed by ant tasks in build.xml but not needed by dependent projects" />
    </configurations>
    <dependencies>
        <!-- Probably should exclude cglib from hibernate-core but both it and javassist are required by
          hibernate-entitymanager, so no point. -->
        <dependency org="org.hibernate" name="hibernate-core" rev="3.6.8.Final" conf="runtime->default,optional"/>
        <dependency org="org.hibernate" name="hibernate-entitymanager" rev="3.6.8.Final" conf="runtime->default"/>
        <dependency org="org.hibernate" name="hibernate-c3p0" rev="3.6.8.Final" conf="runtime->default"/>
        <dependency org="org.hibernate" name="hibernate-ehcache" rev="3.6.8.Final" conf="runtime->default"/>
        <!-- Not actually in any public maven repository  - create a local repository?
    <dependency org="ojdbc" name="ojdbc" rev="14"/> -->
    <!-- TODO Update to 9.1-901 when in repository - or create a local repository
         TODO Update to jdbc4 when we drop java 1.5 support
        <dependency org="postgresql" name="postgresql" rev="9.1-901.jdbc3"/> -->
    <!-- Actual logging implementation for slf4j - jdk logging - NB Version should match dependency from hibernate-core -->
    <dependency org="org.slf4j" name="slf4j-jdk14" rev="1.6.1" conf="runtime->default"/>
    <!-- Build time only dependencies - should probably be down as provided -->
        <dependency org="junit" name="junit" rev="3.8.1" conf="build->default"/>
        <dependency org="org.apache.ant" name="ant" rev="1.8.2" conf="build->default"/>
        <dependency org="org.hibernate" name="hibernate-tools" rev="3.2.4.GA" conf="build->default"/>
        <!-- Not in repository?
    <dependency org="net.sf" name="incanto" rev="0.2.4" conf="build->default"/> -->
        <dependency org="net.sourceforge.schemaspy" name="schemaspy" rev="5.0.0" conf="build->default"/>
    </dependencies>
</ivy-module>

4.3) ivysettings.xml:
<?xml version="1.0" encoding="UTF-8"?>
<ivysettings>
  <include url="${ivy.default.settings.dir}/ivysettings.xml"/>
  <!-- Uncomment if you want to keep ivy's cache somewhere else
  <caches defaultCacheDir="D:\.ivy2\cache" />
  -->
  <settings defaultResolver="myChain" checkUpToDate="true" />
  <resolvers>
    <chain name="myChain">
      <ibiblio name="jboss" m2compatible="true" root="https://repository.jboss.org/nexus/content/groups/public-jboss/" />
      <ibiblio name="sonatype" m2compatible="true" root="https://oss.sonatype.org/content/repositories/releases/" />
      <ibiblio name="ibiblio" m2compatible="true" />
    </chain>
  </resolvers>
</ivysettings>

5) Open a shell, cd to the folder and run ant ("ant").

6) This should create a lib dir, with jars under lib/runtime and lib/build, that can be used as pims-model/lib.

7) Unfortunately you need three jars that I have been unable to find in a maven repository:

lib/runtime/postgresql-9.1-901-jdbc4.jar [will be in soon]
lib/runtime/ojdbc14.jar [will never be in]
lib/build/incanto-0.2.4.jar [unlikley to be in any time soon]

8) Open eclispe and put everything in lib/runtime and lib/build/junit-3.8.1 on the classpath.

Jon Diprose, 03/11/2011

