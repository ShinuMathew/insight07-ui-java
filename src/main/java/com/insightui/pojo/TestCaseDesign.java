package com.insightui.pojo;

public class TestCaseDesign {

	public int stepID;
	public String stepDesc;
	public String action;
	public String locatorType;
	public String target;
	public String value;
	public String comment;

	public TestCaseDesign(int stepID, String stepDesc, String action, String locatorType, String target, String value,
			String comment) {

		this.stepID = stepID;
		this.stepDesc = stepDesc;
		this.action = action;
		this.locatorType = locatorType;
		this.target = target;
		this.value = value;
		this.comment = comment;
	}
}
