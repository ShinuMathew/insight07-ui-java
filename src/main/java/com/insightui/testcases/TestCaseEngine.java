package com.insightui.testcases;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import com.insightui.utils.*;
import com.insightui.variables.CommonVariables;
import com.insightui.variables.CommonVariables.EXECUTIONSTATUS;

import org.omg.CORBA.COMM_FAILURE;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import com.google.common.base.Stopwatch;
import com.insightui.browsers.BrowserSetUp;
import com.insightui.dbconnect.DBHandler;
import com.insightui.logger.Logger;
import com.insightui.testinitializer.TestInitializer;

@Test
public class TestCaseEngine {

	// Global Test Setups
	@BeforeMethod
	public void TestSetup() {
		UtilityManager.getOsName();
		CommonVariables.currentStepLogs = new StringBuilder();
		CommonVariables.setupLogs = new StringBuilder();
		CommonVariables.cleanupLogs = new StringBuilder();
		UtilityManager.getGlobalPath();
		UtilityManager.getLocalDirectory();
		CommonVariables.propFilePath = CommonVariables.globalPath + "/GlobalProperties.properties";
		CommonVariables.tcStartTime = Instant.now().toString(); // UTC time
		// CommonVariables.tcStartTime = LocalDateTime.now().toString(); //Local time
		TestInitializer.storageCleanup();
		TestInitializer.readGlobalConfig();
		CommonVariables.executionStatus = EXECUTIONSTATUS.YETTOSTART;
		Logger.logMessage("Initializing test setup");
		Logger.logMessage("*********************************************");
		CommonVariables.isLocalExecution = false;
		CommonVariables.isRunIDExecution = true;
		CommonVariables.isStepsStoredInDB = true;
		CommonVariables.isDataStoredInDB = true;
	}

	@Test
	@Parameters("tc_runID")
	public void TestCaseEngine(String tc_runID) {
		try {
			if (!tc_runID.contains("packagetest")) {
				if (!CommonVariables.isRunIDExecution) {
					CommonVariables.TCName = tc_runID;
					CommonVariables.insightuiBrowser = CommonVariables.globalData.get("BrowserName");
				} else {
					CommonVariables.tcRunID = tc_runID;
					CommonVariables.testRunConfig = UtilityManager.getTestRunConfig(tc_runID);
					UtilityManager.getTestRunParams();
				}
				TestInitializer.getTCInfo();
				UtilityManager.createReportLocation();
				Logger.logMessage("Test Case details: ");
				Logger.logMessage("-----------------------------------------------");
				Logger.logMessage("\nProject: " + CommonVariables.tcinfo.getProject());
				Logger.logMessage("\nCountry: " + CommonVariables.tcinfo.getCountry());
				Logger.logMessage("\nEnvironment: " + CommonVariables.tcinfo.getEnvironment());
				Logger.logMessage("\nTestCase type: " + CommonVariables.tcinfo.getTcType());
				Logger.logMessage("\nTestCase name: " + CommonVariables.tcinfo.getTcDesc());
				Logger.logMessage("-----------------------------------------------");
				if (!CommonVariables.isStepsStoredInDB)
					TestInitializer.readTCDesign();
				else
					// Read data from DB. Configure processors to integrate TC data to MySql.
					TestInitializer.getTestStepsFromDB();
				CommonVariables.watch = Stopwatch.createStarted();
				TestInitializer.startExecution();
				CommonVariables.watch.stop();
				CommonVariables.testDuration = CommonVariables.watch.toString();
			} else {
				CommonVariables.TCName = tc_runID;
				System.out.println("Dummy test for package creation executed....");
			}
		} catch (Exception ex) {
			Logger.logFatalError("Unable to start execution. Following exception occured: " + ex.getMessage());
		}
	}

	@AfterMethod
	public void TestCleanup() {
		if (!CommonVariables.TCName.contains("packagetest")) {
			Logger.logMessage("Started Cleanup activity");
			// CommonVariables.tcEndTime = LocalDateTime.now().toString(); //Local time
			if (CommonVariables.executionStatus.equals(EXECUTIONSTATUS.INTERRUPTED)) {
				CommonVariables.watch.stop();
				CommonVariables.testDuration = CommonVariables.watch.toString();
			}
			CommonVariables.tcEndTime = Instant.now().toString(); // UTC time
			UtilityManager.takeScreenshot("EndOfExecution");
			CommonVariables.closeAllBrowser = true;
			BrowserSetUp.closeBrowser();
			DBHandler.closeAllDBConnections();
			Logger.logMessage("Initializing notepad generation");
			UtilityManager.generateTextReport();
			UtilityManager.generateExtentReport();
			UtilityManager.updateTestResultInES();
			if (CommonVariables.executionStatus != EXECUTIONSTATUS.INTERRUPTED)
				if (CommonVariables.isTestCaseFailed)
					Assert.fail();
		}
	}
}

class InitTestCase {

	// TBD
	/**
	 * "ROM_QA_Demo_Smoke_CODTest"; "ROM_QA_Demo_Smoke_BTTest";
	 * "ROM_QA_Demo_Smoke_CCTest"; "ROM_QA_Demo_Sanity_AEMTest";
	 * "ROM_QA_Demo_Smoke_BTTest_Deprived";
	 * "ROM_QA_Demo_Smoke_CODTest_Deprived";
	 * "ROM_QA_Demo_Smoke_AdyenBTGuest"; "ROM_QA_Demo_Smoke_AdyenCCGuest";
	 * "ROM_QA_Demo_Smoke_PPCGuest"; "ROM_QA_Demo_Smoke_PPCOGuest";
	 *
	 */
}
