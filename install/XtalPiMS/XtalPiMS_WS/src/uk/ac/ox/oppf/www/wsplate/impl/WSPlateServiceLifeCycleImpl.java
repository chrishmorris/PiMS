/**
 * 
 */
package uk.ac.ox.oppf.www.wsplate.impl;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.engine.ServiceLifeCycle;
import org.apache.log4j.Logger;

import uk.ac.ox.oppf.www.wsplate.util.InitUtil;

/**
 * @author Jon Diprose
 */
public class WSPlateServiceLifeCycleImpl implements ServiceLifeCycle {

    /**
     * The logger for this class
     */
    private static final Logger LOG = Logger.getLogger(WSPlateServiceLifeCycleImpl.class);
    
	/* (non-Javadoc)
	 * @see org.apache.axis2.engine.ServiceLifeCycle#shutDown(org.apache.axis2.context.ConfigurationContext, org.apache.axis2.description.AxisService)
	 */
	public void shutDown(ConfigurationContext cxt, AxisService service) {
		
		LOG.debug("in shutDown(cxt, service)");

	}

	/* (non-Javadoc)
	 * @see org.apache.axis2.engine.ServiceLifeCycle#startUp(org.apache.axis2.context.ConfigurationContext, org.apache.axis2.description.AxisService)
	 */
	public void startUp(ConfigurationContext cxt, AxisService service) {
		
		LOG.debug("in startUp(cxt, service)");
		
    	// Initialize the application
    	InitUtil.initialize();
        
	}

}
