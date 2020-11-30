package com.insightui.testinitializer;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.Row;

import com.insightui.variables.CommonVariables;
import com.insightui.variables.CommonVariables.DBMS;
import com.insightui.variables.CommonVariables.EXECUTIONSTATUS;
import com.insightui.variables.CommonVariables.LOGTYPE;
import com.insightui.variables.CommonVariables.TCRESULT;
//import com.mysql.jdbc.log.Log;
import com.insightui.actions.CommonActions;
import com.insightui.actions.ElementActions;
import com.insightui.actions.ListBoxActions;
import com.insightui.actions.PopUpActions;
import com.insightui.actions.SpecificActions;
import com.insightui.browsers.BrowserSetUp;
import com.insightui.dbconnect.DBHandler;
import com.insightui.ignitor.Ignitor;
import com.insightui.logger.Logger;
import com.insightui.plugins.CSVHandler;
import com.insightui.plugins.ExcelHandler;
import com.insightui.pojo.LoggerInfo;
import com.insightui.pojo.TCInfo;
import com.insightui.pojo.TestCaseDesign;
import com.insightui.utils.UtilityManager;
import com.insightui.variables.*;

public class TestInitializer {

	// Get test from
	public static void readGlobalConfig() {
		InputStream file;
		if (CommonVariables.osName.toLowerCase().contains("mac")
				|| CommonVariables.osName.toLowerCase().contains("window")) {
			try {
				Properties prop = new Properties();
				file = new FileInputStream(CommonVariables.propFilePath);
				prop.load(file);
				for (Entry<Object, Object> entry : prop.entrySet())
					CommonVariables.globalData.put((String) entry.getKey(), (String) entry.getValue());
			} catch (Exception ex) {
				System.out.println(
						"Following exception occured when trying to read the config files. " + ex.getMessage());
			}
		} else {
			try {
				Properties prop = new Properties();
				file = TestInitializer.class.getClassLoader()
						.getResourceAsStream("/InsightUI/GlobalProperties.properties");
				prop.load(file);
				for (Entry<Object, Object> entry : prop.entrySet()) {
					CommonVariables.globalData.put((String) entry.getKey(), (String) entry.getValue());
				}
			} catch (Exception ex) {
				System.out.println(
						"Following exception occured when trying to read the config files. " + ex.getMessage());
			}
		}
	}

	public static void getTCInfo() {
		try {
			CommonVariables.tcinfo = new TCInfo(CommonVariables.TCName.split("_"));
		} catch (Exception ex) {
			Logger.logFatalError("Unable to get test case info. Following exception occured" + ex.getMessage());
		}
	}

	public static void getTestStepsFromDB() {
		try {
			String query = "Select STEP_ID, TEST_STEP_DESC, KEYWORD, LOCATOR_TYPE, TARGET, VALUE_S, COMMENTS from TC_Design where TC_NAME= '"
					+ CommonVariables.TCName + "' and STEP_STATUS='Active' order by STEP_ID;";
			DBHandler.openDBConnection(DBMS.MYSQL);
			ResultSet result = DBHandler.executeQuery(query);
			while (result.next()) {
				CommonVariables.testCaseDesign.add(new TestCaseDesign(
						Integer.parseInt(result.getString("STEP_ID").toString()), result.getString("TEST_STEP_DESC"),
						result.getString("KEYWORD"), result.getString("LOCATOR_TYPE"), result.getString("TARGET"),
						result.getString("VALUE_S"), result.getString("COMMENTS")));
			}
			Logger.logMessage("Test case steps read and loaded successfully...");
			Logger.logMessage("-----------------------------------------------");
			DBHandler.closeDBConnection();
		} catch (Exception ex) {
			Logger.logFatalError("Unable to read test steps from DB. Following exception occured" + ex.getMessage());
		}
	}

	public static void readTCDesign() {
		if (CommonVariables.isStepsStoredInDB == false) {
			if (CommonVariables.osName.toLowerCase().contains("mac")) {
				try {
					String path = CommonVariables.globalPath + CommonVariables.globalData.get("TestCaseCSV") + "/"
							+ CommonVariables.tcinfo.getCountry() + "/" + CommonVariables.tcinfo.getEnvironment() + "/"
							+ CommonVariables.TCName + "/" + CommonVariables.TCName + ".csv";
					CSVHandler csvhandler = new CSVHandler(path);
					for (String[] row : csvhandler.ReadCSV())
						CommonVariables.testCaseDesign.add(new TestCaseDesign(Integer.parseInt(row[0].toString()),
								row[1], row[2], row[3], row[4], row[5], row[6]));
					Logger.logMessage("Test case design steps loaded successfully...");
					Logger.logMessage("-----------------------------------------------");
				} catch (Exception ex) {
					Logger.logFatalError(
							"Unable to read test case design. Following exception occured" + ex.getMessage());
				}
			} else if (CommonVariables.osName.toLowerCase().contains("window")) {
				String path = CommonVariables.globalPath + CommonVariables.globalData.get("TestCaseCSV") + "\\"
						+ CommonVariables.tcinfo.getCountry() + "\\" + CommonVariables.tcinfo.getEnvironment() + "\\"
						+ CommonVariables.TCName + "\\" + CommonVariables.TCName + ".csv";
				CSVHandler csvhandler = new CSVHandler(path);
				for (String[] row : csvhandler.ReadCSV())
					CommonVariables.testCaseDesign.add(new TestCaseDesign(Integer.parseInt(row[0].toString()), row[1],
							row[2], row[3], row[4], row[5], row[6]));
				Logger.logMessage("Test case design steps loaded successfully...");
				Logger.logMessage("-----------------------------------------------");
			} else {
				try {
					String path = "/Users/shinu.mathew/Documents/Insight10/TestCases.xlsx";
					// System.getProperty("user.dir")+
					// CommonVariables.globalData.get("TestCaseExcel");
					ExcelHandler readTCFromExcel = new ExcelHandler(path, CommonVariables.TCName);
					Iterator<Row> iterator = readTCFromExcel.readSheet();
					while (iterator.hasNext()) {
						Row row = iterator.next();
						if (row.getCell(0).toString().toLowerCase().contains("step"))
							continue;
						else
							CommonVariables.testCaseDesign.add(new TestCaseDesign(
									Integer.parseInt(row.getCell(0).toString()), row.getCell(1).toString(),
									row.getCell(2).toString(), row.getCell(3).toString(), row.getCell(4).toString(),
									row.getCell(5).toString(), row.getCell(6).toString()));
					}
				} catch (Exception ex) {
					Logger.logFatalError(
							"Unable to read test case design. Following exception occured" + ex.getMessage());
				}

			}
		}
	}

	public static void storageCleanup() {
		try {
			CommonVariables.globalData.clear();
			CommonVariables.insightHashMap.clear();
			CommonVariables.testCaseDesign.clear();
			CommonVariables.tcFailReason.clear();
			CommonVariables.setupHtmlLogs.clear();
			CommonVariables.cleanupHtmlLogs.clear();
			CommonVariables.loggerInfo.clear();
		} catch (Exception ex) {
			Logger.logFatalError("Unable to cleanup storage data due to the below exception: " + ex.getMessage());
		}
	}

	public static void startExecution() {
		try {
			// CloseExistingBrowsers?? - TBD
			// LaunchBrowser
			BrowserSetUp.launchBrowser();
			Logger.logMessage("---------------------------------------");
			// DeleteBrowserCookie
			BrowserSetUp.deleteBrowserCookies();
			Logger.logMessage("---------------------------------------");
			Logger.logMessage(
					"##########################################################################################\n");
			Logger.logMessage(" Starting execution of Test Case: " + CommonVariables.TCName + "...");
			Logger.logMessage(
					"##########################################################################################\n");
			CommonVariables.executionStatus = EXECUTIONSTATUS.STARTED;
			for (TestCaseDesign tcDesign : CommonVariables.testCaseDesign) {
				ElementActions.unHighlightElement();
				// Logger.logMessage(tcDesign.stepID + " || " + tcDesign.stepDesc + " || " +
				// tcDesign.action + " || "+ tcDesign.locatorType + " || " + tcDesign.target + "
				// || " + tcDesign.value);
				CommonVariables.isStepFailed = false;
				CommonVariables.currentStepTCDesign = tcDesign;
				CommonVariables.currentStepNo = tcDesign.stepID;
				CommonVariables.currentStepDesc = tcDesign.stepDesc;
				CommonVariables.currentStepAction = tcDesign.action;
				CommonVariables.currentStepLocatorType = tcDesign.locatorType;
				CommonVariables.currentStepTarget = tcDesign.target;
				CommonVariables.currentStepvalue = tcDesign.value;
				System.out.println("\n" + tcDesign.stepID + " : " + tcDesign.stepDesc);
				System.out.println("---------------------------------------");
				CommonVariables.currentStepLogs = new StringBuilder();
				// Logger.displayGrowl(LOGTYPE.INFO, CommonVariables.currentStepNo+".
				// "+CommonVariables.currentStepDesc);
				CommonVariables.executionStatus = EXECUTIONSTATUS.INPROGRESS;
				executeAction();
				if (!CommonVariables.isStepFailed) {
					CommonVariables.currentStepResult = CommonVariables.STEPRESULT.PASSED;
					ElementActions.highlightElementPass();
					System.out.println("\nStep result :" + CommonVariables.currentStepResult);
				} else
					System.out.println("\nStep result :" + CommonVariables.currentStepResult);
				CommonVariables.loggerInfo.add(new LoggerInfo(tcDesign, CommonVariables.currentStepResult.toString(),
						CommonVariables.currentStepLogs.toString(), CommonVariables.currentStepScreenshot));
			}
			CommonVariables.executionStatus = EXECUTIONSTATUS.COMPLETED;
			if (!CommonVariables.isTestCaseFailed)
				CommonVariables.tcResult = TCRESULT.PASSED;
			else
				CommonVariables.tcResult = TCRESULT.FAILED;
		} catch (Exception ex) {
			Logger.logFatalError("Unable to start executeAction due to following exception: " + ex.getMessage());
		}
	}

	public static void executeActionRef() {
		try {
			List<Method> commonActionsMethods = Arrays.asList(CommonActions.class.getMethods());
			List<Method> specificActionsMethods = Arrays.asList(SpecificActions.class.getMethods());
			List<Method> utilityManagerMethods = Arrays.asList(UtilityManager.class.getMethods());
			List<Method> listBoxActionsMethods = Arrays.asList(ListBoxActions.class.getMethods());
			List<Method> popUpActionsMethods = Arrays.asList(PopUpActions.class.getMethods());
			List<Method> elementActionsMethods = Arrays.asList(ElementActions.class.getMethods());

			if (!commonActionsMethods.stream().filter(
					_method -> _method.toString().equalsIgnoreCase(CommonVariables.currentStepAction.toUpperCase()))
					.collect(Collectors.toList()).isEmpty()) {
				// CommonActions
			}

		} catch (Exception ex) {
			Logger.logFatalError("Following exception occured when Executing action:\n" + ex.getClass().getName());
		}
	}

	public static void executeAction() {

		try {
			// Implement Action method invoke using reflection- TBD_P2
			switch (CommonVariables.currentStepAction.toUpperCase()) {
			case "OPEN":
				CommonActions.open();
				break;

			case "CONSTRUCTSHOPFRONTURL":
				SpecificActions.constructShopfrontURL();
				break;

			case "GETGLOBALINSIGHTDATA":
				UtilityManager.getGlobalInsightData();
				break;

			case "VERIFYPAGETITLE":
				CommonActions.verifyPageTitle();
				break;

			case "CLICKANDWAIT":
				CommonActions.clickAndWait();
				break;

			case "CLICKIFEXISTS":
				CommonActions.clickIfExists();
				break;

			case "TYPE":
				CommonActions.type();
				break;

			case "TYPEIFEXISTS":
				CommonActions.typeIfExists();
				break;

			case "VERIFYELEMENTPRESENT":
				CommonActions.verifyElementPresent();
				break;

			case "VERIFYTEXT":
				CommonActions.verifyText();
				break;

			case "CLEARSHOPPINGCART":
				SpecificActions.clearShoppingCart();
				break;

			case "STORETEXT":
				CommonActions.storeText();
				break;

			case "WAITINSECONDS":
				CommonActions.waitInSeconds();
				break;

			case "APPLYOFFERANDVERIFY":
				SpecificActions.applyOfferAndVerify();
				break;

			case "SELECTFROMDROPDOWN":
				ListBoxActions.selectFromDropDown();
				break;

			case "HANDLEJSALERT":
				PopUpActions.handleJSAlert();
				break;

			case "STOREPRICE":
				CommonActions.storePrice();
				break;

			case "STOREEXPECTEDTEXT":
				CommonActions.storeExpectedText();
				break;

			case "SWITCHTOTAB":
				CommonActions.switchToTab();
				break;

			case "SWITCHTOIFRAME":
				CommonActions.switchToIframe();
				break;

			case "SWITCHTODEFAULTCONTEXT":
				CommonActions.switchToDefaultContext();
				break;

			case "VERIFYPRICE":
				CommonActions.verifyPrice();
				break;

			case "GETANDSTORETCOUTPUT":
				CommonActions.getAndStoreTCOutput();
				break;

			case "WAITFORSPECIFICELEMENT":
				BrowserSetUp.waitForSpecificElement();
				break;

			case "SELECTMULTIPLEDELIVERYMODE":
				SpecificActions.selectMultipleDeliveryMode();
				break;
			}
		}

		catch (Exception ex) {

		}
	}
}
