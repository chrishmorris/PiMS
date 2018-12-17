/*
 * Created on 15-Feb-2005 This opens and closes the database connection for PIMS. Load context properties.
 * These operations are so important that unlike other errors, these are reported to STDOUT as well as the
 * log. @author: Chris Morris
 */
package org.pimslims.servlet;

import java.util.LinkedList;
import java.util.List;

import javax.naming.Context;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.properties.PropertyGetter;
import org.pimslims.util.InstallationProperties;

/**
 * Servlet context listener. Connects and disconnects database.
 */
public class Listener implements javax.servlet.ServletContextListener {

    //private static final String DEFAULT_PERSPECTIVE = View.DEFAULT_PERSPECTIVE;

    static final String CUSTOMIZATION_PERSPECTIVE = "customization.perspective";

    private AbstractModel model = null;

    /**
     * 
     */
    public Listener() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    public void contextInitialized(final ServletContextEvent event) {
        final ServletContext context = event.getServletContext();
        assert null != context;
/*
        final License license = License.getLicense();
        if ("5.0.0".equals(license.get("version"))) {
            context.setAttribute("license", license);
        } else {
            return;
        } */

        // open the database connection
        try {
            final Context envCtx = PropertyGetter.getNamingContext();
            System.out.println("PiMS starting with database: " + envCtx.lookup("db.url"));
            this.model = ModelImpl.getModel(envCtx);
            context.setAttribute("model", this.model);
            System.out.println("PiMS is connected to database");
            PropertyGetter.setProxySetting();
            System.out.println("PiMS set proxy");

            // note this will not work in Tomcat7
            if (null == context.getRealPath("/")) {
                System.out
                    .println("Warning: PiMS does not run correctly unless the WAR is unpacked, see tomcat.txt");
            } else {
                PropertyGetter.setWorkingDirectory(context.getRealPath("/") + "WEB-INF/");
                System.out.println("PiMS set working directory");
            }
            //CHECKSTYLE:OFF  
            // Occasionally it is OK to catch exception
            // and this class cannot report errors direct to the user,
            // but we seem to have gone to extremes here
            context.setAttribute(View.DEFAULT_PERSPECTIVE, this.getDefaultPerspective());
            context.setAttribute(View.ENABLEDPERSPECTIVENAMES, this.getEnabledPerspectives());
            System.out.println("set context attributes");

            // was this.initParamProperties(context);
            System.out.println("PIMS servlet context initialized");
        } catch (final Exception ex) {
            System.out.println("PiMS exception in contextInitialized method: " + this.getClass().getName());
            context.log("PiMS failed to start", ex);
            ex.printStackTrace();
            event.getServletContext().setAttribute(Installation.START_UP_EXCEPTION, ex);
            //this.connectWithPropertiesFile(context, ex);
        }

    }

/*
    @Deprecated
    // remove this
    private void initParamProperties(final ServletContext context) {
        InputStream in_stream = null;
        try {
            final Properties Props = new Properties();
            in_stream = context.getResourceAsStream("/WEB-INF/conf/ParamProperties");
            if (null != in_stream) {
                Props.load(in_stream);
                final Enumeration propKeys = Props.keys();
                if (!Props.elements().hasMoreElements()) {
                    context.log("No properties in ParamProperties");
                    throw new Throwable("Fail to initialize parameters from ParamProperties");
                }
                while (propKeys.hasMoreElements()) {
                    final String name = propKeys.nextElement().toString();
                    final String value = Props.getProperty(name);
                    context.setAttribute(name, value);
                    context.log("Parameter '" + name + "' = '" + value + "' was initialized successfully");
                }
            }
        } catch (final IOException ioe) {
            context.log("FAIL to initialize parameters from ParamProperties", ioe);
        } catch (final Throwable te) {
            context.log("FAIL to initialize parameters from ParamProperties", te);
        } finally {
            if (in_stream != null) {
                try {
                    in_stream.close();
                } catch (final IOException isc) {
                    // do nothing
                }
            }
        }
    } */
/*
    private void connectWithPropertiesFile(final ServletContext context, final Exception ex) {
        assert false : "this code beleived obsolete";
        final java.io.File properties =
            new java.io.File(context.getRealPath("/") + "/WEB-INF/conf/Properties"
            // note that this path has a double "/" in Sun Java
            // This is a workaround for an error in gij
            // however, PiMS is not guaranteed to work in gij
            );
        if (properties.exists()) {
            System.out.println("Tomcat Context error, using property file instead");

            assert null != properties;
            try {
                this.model = ModelImpl.getModel(properties);
                // or new ModelImpl(
                // this.getClass().getResourceAsStream("Properties") )
                context.setAttribute("model", this.model);
                context.log("PIMS servlet context initialized");
            } catch (final Throwable ex2) {
               
                context.log(
                    "PIMS servlet context not initialized from tomcat context and file!\nProperties file was:"
                        + properties.getAbsolutePath(), ex2);
            }
        } else {
            context.log(
                "PIMS could not connect to database. Are the dbname, userid and password set?"
                    + "\nWebapp location: " + context.getRealPath("") + "\nContext name: "
                    + context.getServletContextName(), ex);

        }
    } */

    String getDefaultPerspective() {
        // set the default perspective
        String defaultPerspective;
        try {
            final String[] enabledPerspectives =
                ModelImpl.getInstallationProperties().getProperty(Listener.CUSTOMIZATION_PERSPECTIVE)
                    .split(",");
            defaultPerspective = enabledPerspectives[0];
        } catch (final RuntimeException e) {
            // not set
            defaultPerspective = org.pimslims.servlet.View.STANDARD_PERSPECTIVE.getName();
        }
        return defaultPerspective;
    }

    List<String> getEnabledPerspectives() {
        // set the default perspective
        List<String> perspectives;
        try {
            final String[] enabledPerspectives =
                ModelImpl.getInstallationProperties().getProperty(Listener.CUSTOMIZATION_PERSPECTIVE)
                    .split(",");
            perspectives = java.util.Arrays.asList(enabledPerspectives);
        } catch (final RuntimeException e) {
            // not set
            perspectives = new LinkedList<String>();
            perspectives.addAll(View.PERSPECTIVES.keySet());
        }
        return perspectives;
    }

    public static void setProxySetting(final InstallationProperties properties) {
        PropertyGetter.setProxySetting();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void contextDestroyed(final ServletContextEvent event) {
        if (null != this.model) {
            try {
                this.model.disconnect();
                event.getServletContext().log("PIMS has disconnected from database OK");
            } catch (final RuntimeException ex) {
                event.getServletContext().log("Error disconnecting PIMS from database", ex.getCause());
                ex.printStackTrace(); // it's useful in Eclipse to get this on
                // STDOUT
            }
        }
        // JMD Shut down hibernate properly - this also shuts down EHCache properly
        this.model.disconnect();

        event.getServletContext().removeAttribute("model");
        this.model = null;

    }

}
