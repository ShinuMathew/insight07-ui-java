package com.insightui.pojo;

import java.util.Map;

import com.insightui.variables.CommonVariables;

public class TestCaseResult {
	private String tcRunID;
	private String tcName;
	private String device;
	private String browserName;
	private TCInfo tcInfo;
	private String tcStartTime;
	private String testResult;
	private String executionStatus;
	private String tcEndTime;
	private Map<String, String> tcOutput;
	private Map<String, String> tcFailReason;
	private String htmlReportLink;
	private String tcDuration;

	public TestCaseResult(String tcRunID, String tcName, String device, String browserName, TCInfo tcInfo,
			String executionStatus, String testResult, Map<String, String> tcFailReason, String tcStartTime,
			String tcEndTime, Map<String, String> tcOutput, String htmlReportLink, String tcDuration) {
		this.tcRunID = tcRunID;
		this.tcName = tcName;
		this.device = device;
		this.browserName = browserName;
		this.tcInfo = tcInfo;
		this.executionStatus = executionStatus;
		this.testResult = testResult;
		this.tcFailReason = tcFailReason;
		this.tcStartTime = tcStartTime;
		this.tcEndTime = tcEndTime;
		this.tcOutput = tcOutput;
		this.htmlReportLink = htmlReportLink;
		this.tcDuration = tcDuration;
	}

	public String getTcRunID() {
		return tcRunID;
	}

	public void setTcRunID(String tcRunID) {
		this.tcRunID = tcRunID;
	}

	public String getTcDuration() {
		return tcDuration;
	}

	public void setTcDuration(String tcDuration) {
		this.tcDuration = tcDuration;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getHtmlReportLink() {
		return htmlReportLink;
	}

	public void setHtmlReportLink(String htmlReportLink) {
		this.htmlReportLink = htmlReportLink;
	}

	public String getExecutionStatus() {
		return executionStatus;
	}

	public void setExecutionStatus(String executionStatus) {
		this.executionStatus = executionStatus;
	}

	public Map<String, String> getTcFailReason() {
		return tcFailReason;
	}

	public void setTcFailReason(Map<String, String> tcFailReason) {
		this.tcFailReason = tcFailReason;
	}

	public Map<String, String> getTcOutput() {
		return tcOutput;
	}

	public void setTcOutput(Map<String, String> tcOutput) {
		this.tcOutput = tcOutput;
	}

	public String getBrowserName() {
		return browserName;
	}

	public void setBrowserName(String browserName) {
		this.browserName = browserName;
	}

	public TCInfo getTcInfo() {
		return tcInfo;
	}

	public void setTcInfo(TCInfo tcInfo) {
		this.tcInfo = tcInfo;
	}

	public String getTestResult() {
		return testResult;
	}

	public void setTestResult(String testResult) {
		this.testResult = testResult;
	}

	public String getTcName() {
		return tcName;
	}

	public void setTcName(String tcName) {
		this.tcName = tcName;
	}

	public String getTcStartTime() {
		return tcStartTime;
	}

	public void setTcStartTime(String tcStartTime) {
		this.tcStartTime = tcStartTime;
	}

	public String getTcEndTime() {
		return tcEndTime;
	}

	public void setTcEndTime(String tcEndTime) {
		this.tcEndTime = tcEndTime;
	}

//	private String Project;
//	private String Country;
//	private String Environment;
//	private String TCType;
//	private String TCDesc;
}
