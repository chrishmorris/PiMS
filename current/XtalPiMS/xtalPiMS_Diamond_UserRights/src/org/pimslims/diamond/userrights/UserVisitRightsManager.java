package org.pimslims.diamond.userrights;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.accessControl.Permission;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.people.Person;

import uk.ac.diamond.ws.ejd.RESTTestGenericClient;
import uk.ac.diamond.ws.ejd.VisitsAndProteinAcronyms;

public class UserVisitRightsManager {

	private static int chunkSize=500;
	
	protected static HashMap<String, ProposalBean> proposals=new HashMap<String, ProposalBean>();
	
	private static HashMap<String, String> proposalNames=new HashMap<String, String>();
	
	private static String[] visitPermissions={ "read" };
	
	public static void main(String[] args){
		System.out.println(" ");
		System.out.println("-------------------------------------");
		System.out.println("xtalPiMS User Rights sync (author:ejd53)");
		System.out.println("Beginning sync of Diamond to xtalPiMS access rights");
	
		Properties properties=new Properties();
    	File file=new File("conf/Properties");
    	try {
			InputStream in = new FileInputStream(file);
			properties.load(in);
			String perms=properties.getProperty("diamond.visitpermissions", "read");
			UserVisitRightsManager.visitPermissions=perms.split(",");
		} catch (IOException e) {
			if (e instanceof FileNotFoundException) {
                System.err.println("Could not find properties file at: " + file.getAbsolutePath());
            }
            throw new RuntimeException(e);
	    }		
		//Hit up Diamond for all the info we need
		getProposals();
		getVisitsForProposals();
		getProteinAcronymsForProposals();
		getUserIdsForProposals();
		//at this point we have a representation of the Diamond rights. Compare it to PiMS and sync.
		WritableVersion version = ModelImpl.getModel().getWritableVersion(Access.ADMINISTRATOR);
		java.util.Iterator<String> i=proposals.keySet().iterator();
		try {
			while(i.hasNext()){
				String proposalName=i.next();
				ProposalBean pb=proposals.get(proposalName);
				System.out.println("Beginning user access sync for proposal "+proposalName);
				matchProposalUserIdsToProposalGroup(version, pb);			
				matchProposalProteinAcronymsToLabNotebooks(version, pb);
				System.out.println("Finished user access sync for proposal "+proposalName);
			}
			version.commit();
		} catch (AbortedException e) {
			e.printStackTrace();
		} catch (ConstraintException e) {
			e.printStackTrace();
		} finally {
			if(!version.isCompleted()){
				version.abort();
			}
		}
		System.out.println("Finished access rights sync");
		System.out.println("-------------------------------------");
	}
	
	private static void getProposals(){
		int chunkStart=1;
		String proposalXml=VisitsAndProteinAcronyms.getProposals(chunkStart, chunkSize);
		DiamondResponseParser parser=new DiamondResponseParser(proposalXml);
		Map<String, Object>[] proposalMap=parser.getMap();
		while(proposalMap.length!=0){
			int numRows=proposalMap.length;
			System.out.println("..."+numRows+" proposals found");
			for(int i=0; i<numRows; i++){
				Map<String, Object> proposal=proposalMap[i];
				String proposalCode=(String)proposal.get("PROPOSALCODE"); //"mx"
				String proposalNumber=(String)proposal.get("PROPOSALNUMBER"); //"4025"
				String proposalId=(String)proposal.get("PROPOSALID"); //database ID
				ProposalBean p=new ProposalBean();
				p.setCode(proposalCode);
				p.setId(proposalId);
				p.setNumber(proposalNumber);
				p.setName(proposalCode+proposalNumber);
				proposals.put(proposalId, p); //cache it
			}
			if(numRows>=chunkSize){
				chunkStart+=chunkSize;
				proposalXml=VisitsAndProteinAcronyms.getProposals(chunkStart, chunkSize);
				parser=new DiamondResponseParser(proposalXml);
				proposalMap=parser.getMap();
			} else {
				proposalMap=new Map[0]; //don't go round again, and don't check Diamond for more.
			}
		}
		return;
	}
	
	private static void getVisitsForProposals(){
		java.util.Iterator<String> i=proposals.keySet().iterator();
		while(i.hasNext()){
			String proposalId=i.next();
			ProposalBean p=proposals.get(proposalId);
			System.out.println("Getting visits for proposal ID "+proposalId);
			getVisitsForProposal(p);
		}
	}
	protected static void getVisitsForProposal(ProposalBean proposal){
		int chunkStart=0;
		String proposalId=proposal.getId();
		String visitsXml=VisitsAndProteinAcronyms.getVisitsByProposalId(proposalId, chunkStart, chunkSize);
		DiamondResponseParser parser=new DiamondResponseParser(visitsXml);
		Map <String,Object>[] visitsMap=parser.getMap();
		while(visitsMap.length>0){
			int numRows=visitsMap.length;
			System.out.println("Found "+numRows+" visits for proposal "+proposal.getName());
			for(int i=0; i<numRows; i++){
				VisitBean vb=new VisitBean();
				Map<String, Object> visit=visitsMap[i];
				String visitId=(String)visit.get("SESSIONID"); //ISPyB DB ID
				String visitNumber=(String)visit.get("VISIT_NUMBER");
				String vproposalId=(String)visit.get("PROPOSALID");
				assert(vproposalId.equals(proposalId));
				vb.setProposalId(vproposalId);
				vb.setVisitId(visitId);
				vb.setVisitNumber(visitNumber);
				vb.setName(proposal.getName()+"-"+visitNumber); // mx4025-1
				proposal.addVisit(vb);
			}
			if(numRows==chunkSize){
				//might be more
				chunkStart+=chunkSize;
				visitsXml=VisitsAndProteinAcronyms.getVisitsByProposalId(proposalId, chunkStart, chunkSize);
				parser=new DiamondResponseParser(visitsXml);
				visitsMap=parser.getMap();
			} else {
				//no more, so make a dummy empty map, don't request one from Diamond
				visitsMap=new Map[0];
			}
		}
	}
	
	protected static void getProteinAcronymsForProposals(){
		java.util.Iterator<String> i=proposals.keySet().iterator();
		while(i.hasNext()){
			String proposalId=i.next();
			ProposalBean p=proposals.get(proposalId);
			System.out.println("Getting protein acronyms for proposal ID "+proposalId);
			getProteinAcronymsForProposal(p);
		}
	}
	protected static void getProteinAcronymsForProposal(ProposalBean proposal){
		int chunkStart=0;
		String proposalId=proposal.getId();
		String proteinsXml=VisitsAndProteinAcronyms.getProteinAcronymsForProposal(proposalId, chunkStart, chunkSize);
		DiamondResponseParser parser=new DiamondResponseParser(proteinsXml);
		Map <String,Object>[] proteinsMap=parser.getMap();
		while(proteinsMap.length>0){
			int numRows=proteinsMap.length;
			System.out.println("Found "+numRows+" proteins for proposal "+proposal.getName());
			for(int i=0; i<numRows; i++){
				Map<String, Object> visit=proteinsMap[i];
				String acronym=(String)visit.get("ACRONYM");
				proposal.addProteinAcronym(acronym);
			}
			if(numRows==chunkSize){
				//might be more
				chunkStart+=chunkSize;
				proteinsXml=VisitsAndProteinAcronyms.getVisitsByProposalId(proposalId, chunkStart, chunkSize);
				parser=new DiamondResponseParser(proteinsXml);
				proteinsMap=parser.getMap();
			} else {
				//no more, so make a dummy empty map, don't request one from Diamond
				proteinsMap=new Map[0];
			}
		}
	}
	
	protected static void getUserIdsForProposals(){
		java.util.Iterator<String> i=proposals.keySet().iterator();
		while(i.hasNext()){
			String proposalId=i.next();
			ProposalBean p=proposals.get(proposalId);
			System.out.println("Getting user FedIDs for proposal ID "+proposalId);
			getUserIdsForProposal(p);
		}
	}
	protected static void getUserIdsForProposal(ProposalBean pb){
		Set<VisitBean> visits=pb.getVisits();
		if(visits.isEmpty()){
			return;
		}
		//Diamond has "get users for visit" but this is actually at the proposal level, 
		//so just get them for the first visit; all the rest will be the same.
		
		VisitBean vb=visits.iterator().next(); 
		String visitName=vb.getName();
		String diamondResponse=VisitsAndProteinAcronyms.getUsersForVisit(visitName);
		processDiamondUserIdsResponse(pb, diamondResponse);
	}

	protected static void processDiamondUserIdsResponse(ProposalBean pb, String response){
		DiamondResponseParser parser=new DiamondResponseParser(response);
		Map<String, Object>[] users=parser.getMap();
		int numUsers=users.length;
		if(0==numUsers){
			return;
		}
		for(int i=0; i<numUsers; i++){
			Map<String, Object> user=users[i];
			String fedId=(String)user.get("NAME");// fedId
			String fullName=(String)user.get("FULLNAME");// "Mr Ed Daniel"
			if(null==fedId){ 
				System.out.println("FedID was null for a user in proposal "+pb.getCode()+", full name was '"+fullName+"'. Ignoring user.");
				continue;
			}
			if(fedId.matches("^[a-zA-Z]{1,4}[0-9]{1,6}$")){
				System.out.println("Proposal "+pb.getName()+": Found user "+fedId+" ("+fullName+")");
				pb.addUserId(fedId, fullName);
			} else {
				System.out.println("Proposal "+pb.getName()+": Ignoring '"+fedId+"' - not a valid fedID");
			}
		}
	}

	protected static void matchProposalUserIdsToProposalGroup(WritableVersion version, ProposalBean pb) throws ConstraintException{
		Map<String,String> masterIspybUserlist=pb.getUserIds();

			UserGroup ug=version.findFirst(UserGroup.class, UserGroup.PROP_NAME, pb.getName());
			if(null==ug){
				ug=new UserGroup(version, pb.getName());
			}
			Set<String> ispybUserlist=new HashSet<String>(masterIspybUserlist.keySet());
			Set<User> pimsUserlist=ug.getMemberUsers();
			java.util.Iterator<User> j=pimsUserlist.iterator();
			//Now we have a PiMS and an ISPyB userlist for the proposal.
			//First iterate through the PiMS one, removing any users from the group that don't appear in the ISPyB list.
			//Any that appear in both, we remove them from the ISPyB list, so we can use it in the next step.
			while(j.hasNext()){
				User user=j.next();
				String username=user.getName();
				if(ispybUserlist.contains(username)){
					ispybUserlist.remove(username);
				} else {
					ug.removeMemberUser(user);
				}
			}
			//Any users remaining on the ISPyB list were not in the PiMS usergroup, so need to be added to it.
			java.util.Iterator<String> k=ispybUserlist.iterator();
			while(k.hasNext()){
				String fedId=k.next();
				User user=version.findFirst(User.class, User.PROP_NAME, fedId);
				if(null==user){
					System.out.println("User "+fedId+" not found in PiMS database, creating");
					user=new User(version,fedId);
					Person p = new Person(version,fedId);
					String fullName=masterIspybUserlist.get(fedId);
					String[] parts=fullName.split(" ");
					if(3==parts.length){
						//Title, firstname, lastname - "Mr Edward Daniel"
						p.setTitle(parts[0]);
						p.setGivenName(parts[1]);
						p.setFamilyName(parts[2]);
					}
					user.setPerson(p);
				}
				boolean isMember=false; 
				Set<User> members = ug.getMemberUsers();
				Iterator i = members.iterator();
				while(i.hasNext()){
					User u=(User)i.next();
					if(fedId.equals(u.getName())){
						isMember=true;
						break;
					}
				}
				if(!isMember){
					ug.addMemberUser(user);
				}
			}
			
			//if UG doesn't have permissions on the Containers LN, it needs to.
			LabNotebook ln=version.findFirst(LabNotebook.class, LabNotebook.PROP_NAME, "Containers");
			if(null==ln){
				ln= new LabNotebook(version, "Containers");
			}
			String permissionType="read";
			Map<String, Object> criteria=new HashMap<String, Object>();
			criteria.put(Permission.PROP_USERGROUP, ug);
			criteria.put(Permission.PROP_LABNOTEBOOK, ln);
			criteria.put(Permission.PROP_OPTYPE, permissionType);
			Permission existingPermission=version.findFirst(Permission.class, criteria);
			if(null==existingPermission){
				Permission newPermission=new Permission(version, "read", ln, ug);
				ug.addPermission(newPermission);
				Permission newPermission2=new Permission(version, "update", ln, ug);
				ug.addPermission(newPermission2);
			}
			
	}
	
	protected static void matchProposalProteinAcronymsToLabNotebooks(WritableVersion version, ProposalBean pb) throws ConstraintException{
		Set<String> masterIspybAcronymList=pb.getProteinAcronyms();
		UserGroup ug=version.findFirst(UserGroup.class, UserGroup.PROP_NAME, pb.getName());
		if(null==ug){
			return;
		}
		
		java.util.Iterator<String> pa=masterIspybAcronymList.iterator();
		while(pa.hasNext()){
			String acronym=pa.next();
			System.out.println(pb.getName()+" has acronym "+acronym);
			String lnName=pb.getName()+"."+acronym;
			LabNotebook ln=version.findFirst(LabNotebook.class, LabNotebook.PROP_NAME, lnName);
			if(null==ln){
				ln= new LabNotebook(version, lnName);
			}
			for(int j=0; j<UserVisitRightsManager.visitPermissions.length; j++){
				String permissionType=UserVisitRightsManager.visitPermissions[j].toLowerCase().trim();
				Map<String, Object> criteria=new HashMap<String, Object>();
				criteria.put(Permission.PROP_USERGROUP, ug);
				criteria.put(Permission.PROP_LABNOTEBOOK, ln);
				criteria.put(Permission.PROP_OPTYPE, permissionType);
				Permission existingPermission=version.findFirst(Permission.class, criteria);
				if(null==existingPermission){
					Permission newPermission=new Permission(version, permissionType, ln, ug);
					ug.addPermission(newPermission);
				}
			}
		}
	}

	
}