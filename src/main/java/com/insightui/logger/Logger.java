package com.insightui.logger;

import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;

import com.insightui.pojo.LoggerInfo;
import com.insightui.utils.UtilityManager;
import com.insightui.variables.*;
import com.insightui.variables.CommonVariables.EXECUTIONSTATUS;
import com.insightui.variables.CommonVariables.LOGTYPE;
import com.insightui.variables.CommonVariables.TCRESULT;

public class Logger {

	public static void logMessage(String message) {

		// Log implementation
		System.out.println(message);

		if (CommonVariables.executionStatus == EXECUTIONSTATUS.YETTOSTART) {
			CommonVariables.setupLogs.append("\n" + message);
			CommonVariables.setupHtmlLogs.add("\n" + message);
			// displayGrowl(LOGTYPE.INFO, message);
		} else if (CommonVariables.executionStatus == EXECUTIONSTATUS.STARTED
				|| CommonVariables.executionStatus == EXECUTIONSTATUS.INPROGRESS) {
			CommonVariables.currentStepLogs.append("\n" + message);
			// displayGrowl(LOGTYPE.MESSAGE, message);
		} else if (CommonVariables.executionStatus == EXECUTIONSTATUS.COMPLETED) {
			CommonVariables.cleanupLogs.append("\n" + message);
			CommonVariables.cleanupHtmlLogs.add("\n" + message);
			// displayGrowl(LOGTYPE.INFO, message);
		}

	}

	public static void logWarning(String message) {

		try {
			// Log implementation[Log4J][Custom/Extent report]
			CommonVariables.isStepFailed = true;
			CommonVariables.isTestCaseFailed = true;
			CommonVariables.currentStepResult = CommonVariables.STEPRESULT.WARNING;

			displayGrowl(LOGTYPE.WARNING, message);
			
			UtilityManager
					.takeScreenshot(CommonVariables.tcinfo.getTcDesc() + "_StepNo._" + CommonVariables.currentStepNo);

			System.out.println(message);
			CommonVariables.currentStepLogs.append("\n" + message);
			CommonVariables.tcFailReason.put(CommonVariables.currentStepResult.name(), "StepNo "
					+ CommonVariables.currentStepNo + " :  " + CommonVariables.currentStepDesc + " :  " + message);

		} catch (Exception ex) {
			System.out.println("Exception occured when generating logWarning for Step no.  " + CommonVariables.stepNo);
		}

	}

	public static void logError(String message) {

		try {
			// Log implementation[Log4J][Custom/Extent report]
			CommonVariables.isStepFailed = true;
			CommonVariables.isTestCaseFailed = true;
			CommonVariables.currentStepResult = CommonVariables.STEPRESULT.FAILED;
			CommonVariables.tcResult = TCRESULT.FAILED;

			System.out.println(message);
			CommonVariables.currentStepLogs.append("\n" + message);

			Logger.logMessage("\nStep result :" + CommonVariables.currentStepResult);
			Logger.logMessage("\nTest case result :" + CommonVariables.tcResult);

			CommonVariables.executionStatus = EXECUTIONSTATUS.INTERRUPTED;
			CommonVariables.tcFailReason.put(CommonVariables.currentStepResult.name(), "StepNo "
					+ CommonVariables.currentStepNo + " :  " + CommonVariables.currentStepDesc + " :  " + message);

			displayGrowl(LOGTYPE.ERROR, message);
			
			UtilityManager
					.takeScreenshot(CommonVariables.tcinfo.getTcDesc() + "_StepNo._" + CommonVariables.currentStepNo);

			CommonVariables.loggerInfo.add(
					new LoggerInfo(CommonVariables.currentStepTCDesign, CommonVariables.currentStepResult.toString(),
							CommonVariables.currentStepLogs.toString(), CommonVariables.currentStepScreenshot));

			Assert.fail();
		} catch (Exception ex) {
			System.out.println("Exception occured when generating logError for Step no.  " + CommonVariables.stepNo);
		}
	}

	public static void logFatalError(String message) {

		try {
			// Log implementation[Log4J][Custom/Extent report]

			// CommonVariables.stepLogMessage = message;
			System.out.println(message);
			CommonVariables.isStepFailed = true;
			CommonVariables.isTestCaseFailed = true;
			CommonVariables.tcResult = TCRESULT.FAILED;

			CommonVariables.currentStepLogs.append("\n" + message);

			Logger.logMessage("\nStep result :" + CommonVariables.currentStepResult);
			Logger.logMessage("\nTest case result :" + CommonVariables.tcResult);

			displayGrowl(LOGTYPE.ERROR, message);
			
			UtilityManager
					.takeScreenshot(CommonVariables.tcinfo.getTcDesc() + "_StepNo._" + CommonVariables.currentStepNo);

			CommonVariables.executionStatus = EXECUTIONSTATUS.INTERRUPTED;
			Assert.fail();
		} catch (Exception ex) {
			System.out.println("Exception occured when generating logWarning for Step no.  " + CommonVariables.stepNo);
		}
	}

	public static void displayGrowl(CommonVariables.LOGTYPE logType, String msg) {
		try {
			if (CommonVariables.executionStatus.equals(EXECUTIONSTATUS.STARTED)
					|| CommonVariables.executionStatus.equals(EXECUTIONSTATUS.INPROGRESS)
					|| CommonVariables.executionStatus.equals(EXECUTIONSTATUS.INTERRUPTED)) {
				JavascriptExecutor js = (JavascriptExecutor) CommonVariables.insightDriver;

				js.executeScript("if (!window.jQuery) {"
						+ "var jquery = document.createElement('script'); jquery.type = 'text/javascript';"
						+ "jquery.src = 'https://ajax.googleapis.com/ajax/libs/jquery/2.0.2/jquery.min.js';"
						+ "document.getElementsByTagName('head')[0].appendChild(jquery);" + "}");

				js.executeScript("$.getScript('https://the-internet.herokuapp.com/js/vendor/jquery.growl.js')");

				js.executeScript("$('head').append('<link rel=\"stylesheet\" "
						+ "href=\"https://the-internet.herokuapp.com/css/jquery.growl.css\" "
						+ "type=\"text/css\" />');");

				if (logType.equals(LOGTYPE.MESSAGE)) {
					js.executeScript("$.growl.notice({ title: 'MESSAGE', message: '" + msg + "' });");
				} else if (logType.equals(LOGTYPE.WARNING)) {
					js.executeScript("$.growl({ title: 'INFO', message: '" + CommonVariables.currentStepNo + ". "
							+ CommonVariables.currentStepDesc + "' });");
					js.executeScript("$.growl.warning({ title: 'WARNING!', message: '" + msg + "' });");
					
				} else if (logType.equals(LOGTYPE.ERROR)) {
					js.executeScript("$.growl({ title: 'INFO', message: '" + CommonVariables.currentStepNo + ". "
							+ CommonVariables.currentStepDesc + "' });");
					js.executeScript("$.growl.error({ title: 'ERROR', message: '" + msg + "' });");
					
				} else if (logType.equals(LOGTYPE.INFO)) {
					js.executeScript("$.growl({ title: 'INFO', message: '" + msg + "' });");
				}
			}
		} catch (Exception ex) {
			System.out.println("Following exception occured when displaying growl \n" + ex.getMessage());
		}
	}
}
