/**
 * ProjectService.java
 * 
 * Created on 5 Feb 2008
 *
 * Author seroul
 */
package org.pimslims.crystallization.business;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.pimslims.business.DataStorage;
import org.pimslims.business.core.model.Group;
import org.pimslims.business.core.model.Project;
import org.pimslims.business.core.service.ProjectService;

import junit.framework.TestCase;

/**
 * @author seroul
 *
 */
public class ProjectServiceTest extends TestCase {
	
	// unique string to avoid name clashes
    private static final String UNIQUE = "test" + System.currentTimeMillis();
	
	protected final DataStorage dataStorage;

	/**
	 * @param name
	 */
	public ProjectServiceTest(String methodName, DataStorage dataStorage) {
        super(methodName);
        this.dataStorage = dataStorage;
	}
	
	
	public void testCreate() throws Exception {
        this.dataStorage.openResources("administrator");
        try {
            Project toMake = new Project();
            toMake.setName("organisation" + UNIQUE);
            
            ProjectService service = this.dataStorage.getProjectService();
        	assertNotNull(service);
            service.create(toMake);

            Project made = service.findByName(toMake.getName());
            assertNotNull(made);
            assertEquals(toMake.getName(), made.getName());
            
            Project madeID = service.find(toMake.getId());
            assertNotNull(madeID);
            assertEquals(toMake.getId(), madeID.getId());
        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }
	
	
}
