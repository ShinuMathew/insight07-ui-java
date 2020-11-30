package com.insightui.pojo;

public class TCInfo {

	private String country;
	private String environment;
	private String project;
	private String tcType;
	private String tcDesc;

	public TCInfo(String tcInfo[]) {

		setCountry(tcInfo[0]);
		setEnvironment(tcInfo[1]);
		setProject(tcInfo[2]);
		setTcType(tcInfo[3]);
		setTcDesc(tcInfo[4]);

	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getTcType() {
		return tcType;
	}

	public void setTcType(String tcType) {
		this.tcType = tcType;
	}

	public String getTcDesc() {
		return tcDesc;
	}

	public void setTcDesc(String tcDesc) {
		this.tcDesc = tcDesc;
	}

}
