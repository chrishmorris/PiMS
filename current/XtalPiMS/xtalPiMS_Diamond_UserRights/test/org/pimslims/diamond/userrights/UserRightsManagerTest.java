package org.pimslims.diamond.userrights;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.pimslims.access.Access;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.accessControl.Permission;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.reference.ExperimentType;

import static org.junit.Assert.*;
import org.junit.Test;


public class UserRightsManagerTest {
	
	@Test
	public void testGetVisitsForProposal(){
		ProposalBean pb=new ProposalBean();
		pb.setId("26166");
		UserVisitRightsManager.proposals.put("26166",pb);
		UserVisitRightsManager.getVisitsForProposal(pb);
		assertTrue(pb.getVisits().size()>0);
	}
	
	@Test
	public void testGetProteinAcronymsForProposal(){
		ProposalBean pb=new ProposalBean();
		pb.setId("26166");
		UserVisitRightsManager.proposals.put("26166",pb);
		UserVisitRightsManager.getProteinAcronymsForProposal(pb);
		assertTrue(pb.getProteinAcronyms().size()>0);
	}

	@Test
	public void testGetUsersForProposal(){
		ProposalBean pb=new ProposalBean();
		pb.setId("26166");
		UserVisitRightsManager.proposals.put("26166",pb);
		VisitBean vb=new VisitBean();
		vb.setName("mx4025-1");
		pb.addVisit(vb);
		UserVisitRightsManager.getUserIdsForProposal(pb);
		Map<String,String> userIds=pb.getUserIds();
		Iterator<String> i=(Iterator<String>) userIds.keySet().iterator();
		while(i.hasNext()){
			String uid=(String)i.next();
			System.out.println("User ID: "+uid);
		}
		assertTrue(userIds.size()>0);
	}

	@Test
	public void testGetUsersForProposalNoVisits(){
		ProposalBean pb=new ProposalBean();
		pb.setId("26166");
		UserVisitRightsManager.proposals.put("26166",pb);
		Map<String,String> userIds=pb.getUserIds();
		assertTrue(userIds.size()==0);
	}

	@Test
	public void testProcessDiamondUserIdsResponse(){
		String response="<ns1:getUsersForVisitResponse xmlns:ns1=\"http://www.diamond.ac.uk/services/genericws\"><ns1:numberColumns>2</ns1:numberColumns><ns1:columnNames>FULLNAME</ns1:columnNames><ns1:columnNames>NAME</ns1:columnNames><ns1:numberRows>4</ns1:numberRows><ns1:row>Mr Karl Levik#SEP#cnp64921#SEP#</ns1:row><ns1:row>Dr Ed Daniel#SEP#ejd53#SEP#</ns1:row><ns1:row>Dr Alun Ashton#SEP#awa25#SEP#</ns1:row><ns1:row>Mr Chris Morris#SEP#cm65#SEP#</ns1:row><ns1:status>OK</ns1:status><ns1:message>success</ns1:message></ns1:getUsersForVisitResponse>";
		ProposalBean pb=new ProposalBean();
		UserVisitRightsManager.processDiamondUserIdsResponse(pb, response);
		Map<String,String> userIds=pb.getUserIds();
		System.out.println("Users found in testProcessDiamondUserIdsResponse: "+userIds.size());
		Iterator i=userIds.keySet().iterator();
		while(i.hasNext()){
			String fedId=(String)i.next();
			String fullName=(String)userIds.get(fedId);
			System.out.println(fedId+": ["+fullName+"]");
		}
		assertTrue(userIds.keySet().size()==4);
	}
	
	@Test
	public void testMatchProposalUserIdsToProposalGroupNoChanges(){
		Calendar now=new GregorianCalendar();
		String unique="_"+now.getTimeInMillis();
		ProposalBean pb=new ProposalBean();
		pb.setName("TESTPROP"+unique);
		pb.addUserId("TESTabc123"+unique, "Mr Firstname Lastname");
		pb.addUserId("TESTxyz789"+unique, "Prof Firstname2 Lastname2");
		WritableVersion version = ModelImpl.getModel().getWritableVersion(Access.ADMINISTRATOR);
		try {
			UserGroup ug=new UserGroup(version, "TESTPROP"+unique);
			User abc123=new User(version, "TESTabc123"+unique);
			User xyz789=new User(version, "TESTxyz789"+unique);
			ug.addMemberUser(abc123);
			ug.addMemberUser(xyz789);
			UserVisitRightsManager.matchProposalUserIdsToProposalGroup(version, pb);
			assertTrue(ug.getMemberUsers().size()==2);
			User user=version.findFirst(User.class, User.PROP_NAME, "TESTabc123"+unique);
			assertTrue(ug.getMemberUsers().contains(user));
			user=version.findFirst(User.class, User.PROP_NAME, "TESTxyz789"+unique);
			assertTrue(ug.getMemberUsers().contains(xyz789));
		} catch (NullPointerException e) {
			fail();
			e.printStackTrace();
		} catch (ConstraintException e) {
			fail();
			e.printStackTrace();
		} finally {
			version.abort();
		}
	}
	
	@Test
	public void testMatchProposalUserIdsToVisitGroupsUserAdded(){
		Calendar now=new GregorianCalendar();
		String unique="_"+now.getTimeInMillis();
		ProposalBean pb=new ProposalBean();
		pb.setName("TESTPROP"+unique);
		pb.addUserId("TESTabc123"+unique, "Mr Firstname Lastname");
		pb.addUserId("TESTxyz789"+unique, "Prof Firstname2 Lastname2");
		WritableVersion version = ModelImpl.getModel().getWritableVersion(Access.ADMINISTRATOR);	
		try {
			UserGroup ug=new UserGroup(version, "TESTPROP"+unique);
			User abc123=new User(version, "TESTabc123"+unique);
			ug.addMemberUser(abc123);
			User xyz789=new User(version, "TESTxyz789"+unique); //don't add to group
			UserVisitRightsManager.matchProposalUserIdsToProposalGroup(version, pb);
			assertTrue(ug.getMemberUsers().size()==2);
			User user=version.findFirst(User.class, User.PROP_NAME, "TESTabc123"+unique);
			assertTrue(ug.getMemberUsers().contains(user));
			user=version.findFirst(User.class, User.PROP_NAME, "TESTxyz789"+unique);
			assertTrue(ug.getMemberUsers().contains(user));
		} catch (NullPointerException e) {
			fail();
			e.printStackTrace();
		} catch (ConstraintException e) {
			fail();
			e.printStackTrace();
		} finally {
			version.abort();
		}
	}

	@Test
	public void testMatchProposalUserIdsToVisitGroupsNonPimsUserAdded(){
		Calendar now=new GregorianCalendar();
		String unique="_"+now.getTimeInMillis();
		ProposalBean pb=new ProposalBean();
		pb.setName("TESTPROP"+unique);
		pb.addUserId("TESTabc123"+unique, "Mr Firstname Lastname");
		pb.addUserId("TESTxyz789"+unique, "Prof Firstname2 Lastname2");
		WritableVersion version = ModelImpl.getModel().getWritableVersion(Access.ADMINISTRATOR);	
		try {
			UserGroup ug=new UserGroup(version, "TESTPROP"+unique);
			User abc123=new User(version, "TESTabc123"+unique);
			ug.addMemberUser(abc123);
			UserVisitRightsManager.matchProposalUserIdsToProposalGroup(version, pb);
			assertTrue(ug.getMemberUsers().size()==2);
			User user=version.findFirst(User.class, User.PROP_NAME, "TESTabc123"+unique);
			assertTrue(ug.getMemberUsers().contains(user));
			User user2=version.findFirst(User.class, User.PROP_NAME, "TESTxyz789"+unique);
			assertTrue(null!=user2); //should have been created and added to group
			assertTrue(ug.getMemberUsers().contains(user2));
		} catch (NullPointerException e) {
			fail();
			e.printStackTrace();
		} catch (ConstraintException e) {
			fail();
			e.printStackTrace();
		} finally {
			version.abort();
		}
	}

	
	@Test
	public void testMatchProposalUserIdsToVisitGroupsUserRemoved(){
		Calendar now=new GregorianCalendar();
		String unique="_"+now.getTimeInMillis();
		ProposalBean pb=new ProposalBean();
		pb.setName("TESTPROP"+unique);
		pb.addUserId("TESTabc123"+unique, "Mr Firstname Lastname");
		WritableVersion version = ModelImpl.getModel().getWritableVersion(Access.ADMINISTRATOR);	
		try {
			UserGroup ug=new UserGroup(version, "TESTPROP"+unique);
			User abc123=new User(version, "TESTabc123"+unique);
			User xyz789=new User(version, "TESTxyz789"+unique);
			ug.addMemberUser(abc123);
			ug.addMemberUser(xyz789);
			UserVisitRightsManager.matchProposalUserIdsToProposalGroup(version, pb);
			assertTrue(ug.getMemberUsers().size()==1);
			User user=version.findFirst(User.class, User.PROP_NAME, "TESTabc123"+unique);
			assertTrue(ug.getMemberUsers().contains(user));
			user=version.findFirst(User.class, User.PROP_NAME, "TESTxyz789"+unique);
			assertFalse(ug.getMemberUsers().contains(user));
		} catch (NullPointerException e) {
			fail();
			e.printStackTrace();
		} catch (ConstraintException e) {
			fail();
			e.printStackTrace();
		} finally {
			version.abort();
		}
	}

	
	@Test
	public void testMatchProposalUserIdsToVisitGroupsUserAddedAndRemoved(){
		Calendar now=new GregorianCalendar();
		String unique="_"+now.getTimeInMillis();
		ProposalBean pb=new ProposalBean();
		pb.setName("TESTPROP"+unique);
		pb.addUserId("TESTabc123"+unique, "Mr Firstname Lastname");
		pb.addUserId("TESTxyz789"+unique, "Prof Firstname2 Lastname2");
		VisitBean vb=new VisitBean();
		vb.setVisitNumber("1");
		vb.setProposalId("9999"+unique);
		vb.setVisitId("8888"+unique);
		vb.setName("test1234-1"+unique);
		pb.addVisit(vb);
		WritableVersion version = ModelImpl.getModel().getWritableVersion(Access.ADMINISTRATOR);	
		try {
			UserGroup ug=new UserGroup(version, "TESTPROP"+unique);
			User abc123=new User(version, "TESTabc123"+unique);
			User def456=new User(version, "TESTdef456"+unique);
			User xyz789=new User(version, "TESTxyz789"+unique);
			ug.addMemberUser(abc123);
			ug.addMemberUser(def456);
			UserVisitRightsManager.matchProposalUserIdsToProposalGroup(version, pb);
			assertTrue(ug.getMemberUsers().size()==2);
			User user=version.findFirst(User.class, User.PROP_NAME, "TESTabc123"+unique);
			assertTrue(ug.getMemberUsers().contains(user));
			user=version.findFirst(User.class, User.PROP_NAME, "TESTdef456"+unique);
			assertFalse(ug.getMemberUsers().contains(user));
			user=version.findFirst(User.class, User.PROP_NAME, "TESTxyz789"+unique);
			assertTrue(ug.getMemberUsers().contains(user));
		} catch (NullPointerException e) {
			fail();
			e.printStackTrace();
		} catch (ConstraintException e) {
			fail();
			e.printStackTrace();
		} finally {
			version.abort();
		}
	}
	


	@Test
	public void testMatchAcronymsToPermissionsNoChanges(){
		Calendar now=new GregorianCalendar();
		String unique="_"+now.getTimeInMillis();
		ProposalBean pb=new ProposalBean();
		pb.setName("TESTPROP"+unique);
		VisitBean vb=new VisitBean();
		vb.setVisitNumber("1");
		vb.setProposalId("9999"+unique);
		vb.setVisitId("8888"+unique);
		vb.setName("test1234-1"+unique);
		pb.addVisit(vb);
		pb.addProteinAcronym("TESTAC1"+unique);
		pb.addProteinAcronym("TESTAC2"+unique);
		
		//Set up test DB objects as administrator
		WritableVersion version = ModelImpl.getModel().getWritableVersion(Access.ADMINISTRATOR);	
		try {
			setUpProteinAcronymTestObjects(version, now, unique);
			//call my code here
			UserVisitRightsManager.matchProposalProteinAcronymsToLabNotebooks(version, pb);
			version.commit();
		} catch (ConstraintException e) {
			fail("Could not set up test objects - ConstraintException");
			e.printStackTrace();
		} catch (AbortedException e) {
			fail("Could not set up test objects - AbortedException");
			e.printStackTrace();
		} finally {
			if(!version.isCompleted()){
				version.abort();
			}
		}
		
		//See whether user can read the relevant LNs
		ReadableVersion rv=ModelImpl.getModel().getReadableVersion("bob"+unique);
		try {
			assertTrue(null!=rv.findFirst(Experiment.class, Experiment.PROP_NAME, "ex1"+unique));
			assertTrue(null!=rv.findFirst(Experiment.class, Experiment.PROP_NAME, "ex2"+unique));
			assertFalse(null!=rv.findFirst(Experiment.class, Experiment.PROP_NAME, "ex3"+unique));
			assertFalse(null!=rv.findFirst(Experiment.class, Experiment.PROP_NAME, "ex4"+unique));
		} finally { 
			rv.abort();
		}
		
		//and back to administrator, to delete the test objects
		version = ModelImpl.getModel().getWritableVersion(Access.ADMINISTRATOR);	
		try {
			tearDownProteinAcronymTestObjects(version, unique);
			version.commit();
		} catch (AccessException e) {
			fail("Could not delete test objects - AccessException");
			e.printStackTrace();
		} catch (ConstraintException e) {
			fail("Could not delete test objects - ConstraintException");
			e.printStackTrace();
		} catch (AbortedException e) {
			fail("Could not delete test objects - AbortedException");
			e.printStackTrace();
		} finally {
			if(!version.isCompleted()){
				version.abort();
			}
		}
	}
	@Test

	public void testMatchAcronymsToPermissionsAcronymAdded(){
		Calendar now=new GregorianCalendar();
		String unique="_"+now.getTimeInMillis();
		ProposalBean pb=new ProposalBean();
		pb.setName("TESTPROP"+unique);
		pb.addProteinAcronym("TESTAC1"+unique);
		pb.addProteinAcronym("TESTAC2"+unique);
		pb.addProteinAcronym("TESTAC3"+unique);
		
		//Set up test DB objects as administrator
		WritableVersion version = ModelImpl.getModel().getWritableVersion(Access.ADMINISTRATOR);	
		try {
			setUpProteinAcronymTestObjects(version, now, unique);
			//call my code here
			UserVisitRightsManager.matchProposalProteinAcronymsToLabNotebooks(version, pb);
			version.commit();
		} catch (ConstraintException e) {
			fail("Could not set up test objects - ConstraintException");
			e.printStackTrace();
		} catch (AbortedException e) {
			fail("Could not set up test objects - AbortedException");
			e.printStackTrace();
		} finally {
			if(!version.isCompleted()){
				version.abort();
			}
		}
		
		//See whether user can read the relevant LNs
		ReadableVersion rv=ModelImpl.getModel().getReadableVersion("bob"+unique);
		try {
			assertTrue(null!=rv.findFirst(Experiment.class, Experiment.PROP_NAME, "ex1"+unique));
			assertTrue(null!=rv.findFirst(Experiment.class, Experiment.PROP_NAME, "ex2"+unique));
			assertTrue(null!=rv.findFirst(Experiment.class, Experiment.PROP_NAME, "ex3"+unique));
			assertFalse(null!=rv.findFirst(Experiment.class, Experiment.PROP_NAME, "ex4"+unique));
		} finally { 
			rv.abort();
		}
		
		//and back to administrator, to delete the test objects
		version = ModelImpl.getModel().getWritableVersion(Access.ADMINISTRATOR);	
		try {
			tearDownProteinAcronymTestObjects(version, unique);
			version.commit();
		} catch (AccessException e) {
			fail("Could not delete test objects - AccessException");
			e.printStackTrace();
		} catch (ConstraintException e) {
			fail("Could not delete test objects - ConstraintException");
			e.printStackTrace();
		} catch (AbortedException e) {
			fail("Could not delete test objects - AbortedException");
			e.printStackTrace();
		} finally {
			if(!version.isCompleted()){
				version.abort();
			}
		}
	}
	
	/**
	 * Set up four LabNotebooks, each containing one Experiment.
	 * Set up one User in one UserGroup.
	 * Grant UserGroup read permission on two of the LabNotebooks.
	 */
	private void setUpProteinAcronymTestObjects(WritableVersion version, Calendar now, String unique) throws ConstraintException{
		ExperimentType et=new ExperimentType(version, "extype"+unique);
		UserGroup ug=new UserGroup(version, "TESTPROP"+unique);
		User bob=new User(version,"bob"+unique);
		ug.addMemberUser(bob);
		LabNotebook ln1=new LabNotebook(version, "TESTPROP"+unique+".TESTAC1"+unique);
		LabNotebook ln2=new LabNotebook(version, "TESTPROP"+unique+".TESTAC2"+unique);
		LabNotebook ln3=new LabNotebook(version, "TESTPROP"+unique+".TESTAC3"+unique);
		LabNotebook ln4=new LabNotebook(version, "TESTPROP"+unique+".TESTAC4"+unique);
		Permission p1=new Permission(version, "read", ln1, ug);
		Permission p2=new Permission(version, "read", ln2, ug);
		Experiment e1=new Experiment(version, "ex1"+unique, now, now, et);
		Experiment e2=new Experiment(version, "ex2"+unique, now, now, et);
		Experiment e3=new Experiment(version, "ex3"+unique, now, now, et);
		Experiment e4=new Experiment(version, "ex4"+unique, now, now, et);
		e1.setAccess(ln1);
		e2.setAccess(ln2);
		e3.setAccess(ln3);
		e4.setAccess(ln4);
	}

	private void tearDownProteinAcronymTestObjects(WritableVersion version, String unique) throws AccessException, ConstraintException{
		Experiment e1=version.findFirst(Experiment.class, Experiment.PROP_NAME, "ex1"+unique);
		Experiment e2=version.findFirst(Experiment.class, Experiment.PROP_NAME, "ex2"+unique);
		Experiment e3=version.findFirst(Experiment.class, Experiment.PROP_NAME, "ex3"+unique);
		Experiment e4=version.findFirst(Experiment.class, Experiment.PROP_NAME, "ex4"+unique);
		version.delete(e1);
		version.delete(e2);
		version.delete(e3);
		version.delete(e4);
		ExperimentType et=version.findFirst(ExperimentType.class, ExperimentType.PROP_NAME, "extype"+unique);
		version.delete(et);
		User bob=version.findFirst(User.class, User.PROP_NAME, "bob"+unique);
		version.delete(bob);
		UserGroup ug=version.findFirst(UserGroup.class, UserGroup.PROP_NAME, "TESTPROP"+unique);
		version.delete(ug);
		LabNotebook ln1=version.findFirst(LabNotebook.class, LabNotebook.PROP_NAME, "TESTPROP"+unique+".TESTAC1"+unique);
		LabNotebook ln2=version.findFirst(LabNotebook.class, LabNotebook.PROP_NAME, "TESTPROP"+unique+".TESTAC2"+unique);
		LabNotebook ln3=version.findFirst(LabNotebook.class, LabNotebook.PROP_NAME, "TESTPROP"+unique+".TESTAC3"+unique);
		LabNotebook ln4=version.findFirst(LabNotebook.class, LabNotebook.PROP_NAME, "TESTPROP"+unique+".TESTAC4"+unique);
		version.delete(ln1);
		version.delete(ln2);
		version.delete(ln3);
		version.delete(ln4);
	}

}