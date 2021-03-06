/**
 * 
 */
package uk.ac.ox.oppf.www.wsplate.util;

import java.net.URL;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.net.SMTPAppender;

//import uk.ac.ox.oppf.plate.Constants;
//import uk.ac.ox.oppf.www.wsplate.lims.PlateInfoFactory;

/**
 * General utility methods
 * 
 * @author Jon Diprose
 */
public class InitUtil {

	/**
	 * Standard application intialization
	 */
	public static void initialize() {
		
    	// Initialize log4j
		URL propFile = InitUtil.class.getResource("/wsplate-log4j.properties");
		if (null == propFile) throw new RuntimeException("wsplate-log4j.properties not found!");
		System.out.println(propFile.getPath());
    	PropertyConfigurator.configure(propFile);
    	
		// Get a logger for this class
    	Logger log = Logger.getLogger(InitUtil.class);
    	
		// Fix SMTPAppenders' Evaluator class
    	// TODO FIXME: Set correct logger
        //SMTPAppender app = (SMTPAppender) Logger.getLogger(PlateInfoFactory.class.getName() + ".Vault").getAppender("MailApp");
        SMTPAppender app = (SMTPAppender) Logger.getLogger("org.pimslims.integration.fmlx" + ".Vault").getAppender("MailApp");
		app.setEvaluatorClass(TrueLoggingEvaluator.class.getName());
		
        // TODO FIXME: Set correct logger
        //app = (SMTPAppender) Logger.getLogger(PlateInfoFactory.class.getName() + ".Vault.LIMS").getAppender("LIMSmailApp");
        app = (SMTPAppender) Logger.getLogger("org.pimslims.integration.fmlx" + ".Vault.LIMS").getAppender("LIMSmailApp");
		app.setEvaluatorClass(TrueLoggingEvaluator.class.getName());
		
    	// Make the hostname available to log4j
    	String hostname = "localhost";
    	try {
    		java.net.InetAddress localMachine = java.net.InetAddress.getLocalHost();
    		hostname = localMachine.toString();
    		//hostname = localMachine.getCanonicalHostName();
    		hostname = localMachine.getHostName();
    	}
    	catch (java.net.UnknownHostException uhe) {
    		// Swallow it
    	}
    	MDC.put("hostname", hostname);
    	
	}
	
}
