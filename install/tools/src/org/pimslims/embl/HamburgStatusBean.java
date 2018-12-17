package org.pimslims.embl;

public class HamburgStatusBean {

	private final String task;
	private final String pi;
	private final String labID;
	private final String lab;
	private final String contact;
	private final String remarks;

	public HamburgStatusBean(String task, String pi, String labID, String lab,
			String contact, String remarks) {
		super();
		this.task = task;
		this.pi = pi;
		this.labID = labID;
        this.lab = lab;
        this.contact = contact;
        this.remarks = remarks;
    }

    public String getTask() {
        return task;
    }

    public String getPi() {
        return pi;
    }

    public String getLabID() {
        return labID;
    }

    public String getLab() {
        return lab;
    }

    public String getContact() {
        return contact;
    }

    public String getRemarks() {
        return remarks;
    }

}
