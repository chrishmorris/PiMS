package org.pimslims.diamond.plateimporter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class ProposalBean {

	private String name="";
	private String id="";
	private String code="";
	private String number="";	
	private Map<String,String> userIds=new HashMap<String,String>();
	private Set<String> proteinAcronyms=new HashSet<String>();
	private Set<VisitBean> visits=new HashSet<VisitBean>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public Map<String,String> getUserIds() {
		return userIds;
	}
	public void setUserIds(HashMap<String,String> userIds) {
		this.userIds = userIds;
	}
	public void addUserId(String userId, String fullName){
		this.userIds.put(userId, fullName);
	}
	public Set<String> getProteinAcronyms() {
		return proteinAcronyms;
	}
	public void setProteinAcronyms(Set<String> proteinAcronyms) {
		this.proteinAcronyms = proteinAcronyms;
	}
	public void addProteinAcronym(String proteinAcronym){
		this.proteinAcronyms.add(proteinAcronym); //TODO enforce unique
	}
	public Set<VisitBean> getVisits() {
		return visits;
	}
	public void setVisits(Set<VisitBean> visits) {
		this.visits = visits;
	}
	public void addVisit(VisitBean visit){
		this.visits.add(visit);
	}
}
