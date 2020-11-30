package com.insightui.pojo;

import java.util.ArrayList;
import java.util.List;

public class LoggerInfo {
	public TestCaseDesign tcstep;
	public String testStepResult;
	public String testStepLog;
	public String testStepScreenshot;

	public LoggerInfo(TestCaseDesign tcstep, String testStepResult, String testStepLog, String testStepScreenshot) {
		this.tcstep = tcstep;
		this.testStepResult = testStepResult;
		this.testStepLog = testStepLog;
		this.testStepScreenshot = testStepScreenshot;
	}
}
