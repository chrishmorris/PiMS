/**
 * xtalPiMS_Web org.pimslims.crystallization.servlet IspybResultsTest.java
 * 
 * @author cm65
 * @date 13 Dec 2011
 * 
 *       Protein Information Management System
 * @version: 4.3
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.crystallization.servlet;

import junit.framework.TestCase;

import org.pimslims.access.Access;
import org.pimslims.crystallization.servlet.NewDiamondProjectServlet.WorkingBean;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.model.core.LabNotebook;

/**
 * IspybResultsTest
 * 
 */
public class NewDiamondProjectTest extends TestCase {

	private NewDiamondProjectServlet servlet = null;
	private WritableVersion version = null;

	/**
	 * Constructor for IspybResultsTest
	 * 
	 * @param name
	 */
	public NewDiamondProjectTest(String name) {
		super(name);
	}

	public void setUp() throws ConstraintException {
		this.servlet = new NewDiamondProjectServlet();
		this.version = ModelImpl.getModel().getWritableVersion(
				Access.ADMINISTRATOR);
		User user = new User(this.version, "tst123");
		UserGroup proposal = new UserGroup(this.version, "tp1234");
		LabNotebook project = new LabNotebook(this.version, "tp1234.prot");
		proposal.addMemberUser(user);
	}

	public void tearDown() {
		this.servlet = null;
		this.version.abort();
		this.version = null;
	}

	public void testCreateProject() {
		WorkingBean wb = this.servlet.getWorkingBean();
		wb.setUsername("tst123");
		wb.setProposalCode("tp1234");
		wb.setProteinAcronym("prot2");
		try {
			this.servlet.processRequest(this.version, wb);
		} catch (ConstraintException e) {
			e.printStackTrace();
			fail();
		}
		assertTrue(null != this.version.findFirst(LabNotebook.class,
				LabNotebook.PROP_NAME, "tp1234.prot2"));
		assertTrue(null != this.version.findFirst(UserGroup.class,
				UserGroup.PROP_NAME, "tst123"));
		assertTrue(wb.getError().equals(""));
	}

	public void testProjectExists() {
		WorkingBean wb = this.servlet.getWorkingBean();
		wb.setUsername("tst123");
		wb.setProposalCode("tp1234");
		wb.setProteinAcronym("prot");
		try {
			this.servlet.processRequest(this.version, wb);
		} catch (ConstraintException e) {
			e.printStackTrace();
			fail();
		}
		assertFalse(wb.getError().equals(""));
	}

	public void testProposalDoesNotExist() {
		WorkingBean wb = this.servlet.getWorkingBean();
		wb.setUsername("tst123");
		wb.setProposalCode("tp9999");
		wb.setProteinAcronym("prot");
		try {
			this.servlet.processRequest(this.version, wb);
		} catch (ConstraintException e) {
			e.printStackTrace();
			fail();
		}
		assertFalse(wb.getError().equals(""));
	}

	public void testUserNotOnProposal() {
		WorkingBean wb = this.servlet.getWorkingBean();
		wb.setUsername("tst123");
		wb.setProposalCode("tp1234");
		wb.setProteinAcronym("prot2");
		try {
			UserGroup grp = version.findFirst(UserGroup.class,
					UserGroup.PROP_NAME, "tp1234");
			User user = version.findFirst(User.class, User.PROP_NAME, "tst123");
			grp.removeMemberUser(user);
			this.servlet.processRequest(this.version, wb);
		} catch (ConstraintException e) {
			e.printStackTrace();
			fail();
		}
		assertFalse(wb.getError().equals(""));
	}

}
