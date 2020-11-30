package com.insightui.utils;

import com.insightui.variables.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.impl.DefaultBHttpClientConnection;
import org.json.JSONObject;
import org.json.JSONPropertyName;
import org.omg.CORBA.COMM_FAILURE;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.http.HttpMethod;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.insightui.dbconnect.DBHandler;
import com.insightui.logger.*;
import com.insightui.plugins.RestClient;
import com.insightui.pojo.LoggerInfo;
import com.insightui.pojo.TestCaseResult;
import com.insightui.variables.CommonVariables.DBMS;
import com.insightui.variables.CommonVariables.EXECUTIONSTATUS;
import com.insightui.variables.CommonVariables.HttpMethods;
import com.jayway.jsonpath.JsonPath;

public class UtilityManager {

	public static void getOsName() {
		try {
			CommonVariables.osName = System.getProperty("os.name");
			Logger.logMessage("Test cases is being executed in :" + CommonVariables.osName.toUpperCase());
		} catch (Exception ex) {
			Logger.logError("Unable to get the osName");
		}
	}

	public static void getGlobalPath() {
		try {
			if (CommonVariables.osName.toLowerCase().contains("mac")) {
				String path = "/Users/{{username}}/Documents/Insight10/";
				path = path.replace("{{username}}", System.getProperty("user.name"));
				CommonVariables.globalPath = path;
			} else if (CommonVariables.osName.toLowerCase().contains("window")) {
				CommonVariables.globalPath = "C:\\Windows\\Insight10\\";
			}

			File report = new File(CommonVariables.globalPath);
			if (!report.exists()) {
				report.mkdir();
			}
		} catch (Exception ex) {
			System.out.println("Unable to create global path due to following exception: \n" + ex.getMessage());
		}
	}

	public static void getLocalDirectory() {
		try {
			String path = ".";
			CommonVariables.localPath = Paths.get(path.toString()).toString();
		} catch (Exception ex) {
			System.out.println("Unable to create local path due to following exception: \n" + ex.getMessage());
		}
	}

	public static String getTestRunConfig(String tcrunID) {
		try {
			String query = "select testrunid, config from Test_Run where testrunid='" + tcrunID + "';";
			String config = "";

			DBHandler.openDBConnection(DBMS.POSTGRES);
			ResultSet result = DBHandler.executeQuery(query);

			while (result.next()) {
				config = result.getString("config");
			}
			return config;
		} catch (Exception ex) {
			Logger.logFatalError(
					"Unable to get the testrun config. Following exception occured.../n" + ex.getMessage());
			return null;
		}
	}

	public static void getTestRunParams() {
		try {
			CommonVariables.TCName = JsonPath.read(CommonVariables.testRunConfig, "$.tc_name");
			CommonVariables.insightuiBrowser = JsonPath.read(CommonVariables.testRunConfig, "$.browsers[0]");
		} catch (Exception ex) {
			Logger.logFatalError(
					"Unable to get the testrun params. Following exception occured.../n" + ex.getMessage());
		}
	}

	// Get value from Insight Hash map
	public static String getValueFromInsightHashMap(String key) {
		String value = "";
		try {
			if (key.contains("$") || key.contains("{") || key.contains("}")) {
				key = key.replace("$", "");
				key = key.replace("{", "");
				key = key.replace("}", "");

				value = CommonVariables.insightHashMap.get(key);
			} else {
				value = CommonVariables.insightHashMap.get(key);
			}
			return value;
		} catch (Exception ex) {
			Logger.logFatalError("Unable to get the value from map. Following exception occured " + ex.getMessage());
			return value;
		}
	}

	// Store value in Hash Map
	public static void storeValueToInsightHashMap(String key, String value) {
		try {
			if (key.contains("$") || key.contains("{") || key.contains("}")) {
				key = key.replace("$", "");
				key = key.replace("{", "");
				key = key.replace("}", "");

				CommonVariables.insightHashMap.put(key, value);
			} else {
				CommonVariables.insightHashMap.put(key, value);
			}

		} catch (Exception ex) {
			Logger.logFatalError("Unable to get the value from map. Following exception occured " + ex.getMessage());
		}
	}

	// Get global insight data from db
	public static void getGlobalInsightData() {
		InputStream file;
		CommonVariables.globalDataPath = CommonVariables.globalPath + "Repos" + "/"
				+ CommonVariables.tcinfo.getCountry() + "/" + CommonVariables.tcinfo.getEnvironment() + "/"
				+ CommonVariables.tcinfo.getCountry() + "_" + CommonVariables.tcinfo.getEnvironment() + "_"
				+ "GlobalData.properties";

		if (!CommonVariables.isDataStoredInDB) {
			try {
				if (CommonVariables.osName.toLowerCase().contains("mac")
						|| CommonVariables.osName.toLowerCase().contains("window")) {

					try {

						Properties prop = new Properties();
						file = new FileInputStream(CommonVariables.globalDataPath);
						prop.load(file);

						for (Entry<Object, Object> entry : prop.entrySet()) {
							CommonVariables.insightHashMap.put((String) entry.getKey(), (String) entry.getValue());
						}
						Logger.logMessage("Successfully read Global data from Property file");
					} catch (Exception ex) {
						System.out.println(
								"Following exception occured when trying to read the config files. " + ex.getMessage());
					}
				}

			} catch (Exception ex) {
				Logger.logFatalError(
						"Unable to store global data to hashmap. Following exception occured " + ex.getMessage());
			}
		} else {
			try {
				String query = "Select INSIGHT_KEYS, INSIGHT_VALUES from GLOBAL_INSIGHT_DATA where Country ='"
						+ CommonVariables.tcinfo.getCountry() + "' and Environment='"
						+ CommonVariables.tcinfo.getEnvironment() + "'";

				DBHandler.openDBConnection(DBMS.MYSQL);
				ResultSet result = DBHandler.executeQuery(query);

				while (result.next()) {
					CommonVariables.insightHashMap.put(result.getString("INSIGHT_KEYS"),
							result.getString("INSIGHT_VALUES"));
				}

				Logger.logMessage("Successfully fetched Global data from MySQL DB");

			} catch (Exception ex) {
				Logger.logFatalError(
						"Unable to store global data to hashmap. Following exception occured " + ex.getMessage());
			} finally {
				DBHandler.closeDBConnection();
			}
		}
	}

	// Take screenshot
	public static void takeScreenshot(String fileName) {
		try {
			if (!CommonVariables.executionStatus.equals(EXECUTIONSTATUS.YETTOSTART)) {
				TakesScreenshot tkscr = (TakesScreenshot) CommonVariables.insightDriver;
				File scrFile = tkscr.getScreenshotAs(OutputType.FILE);
				File destFile = new File(CommonVariables.CurrentTCLogLocation + "/" + fileName + ".png");
				FileUtils.copyFile(scrFile, destFile);

				if (fileName.toUpperCase().contains("END")) {
					CommonVariables.eoeStepScreenshot = destFile.getPath();
				} else {
					CommonVariables.currentStepScreenshot = destFile.getPath();
				}
			}
		} catch (Exception ex) {
			Logger.logMessage("Unable to take screenshot. Following exception occured " + ex.getMessage());
		}
	}

	// Create report location
	public static void createReportLocation() {
		String currentDate = LocalDate.now().toString();
		String currentTime = LocalDateTime.now().toString();
		String src = CommonVariables.globalPath + CommonVariables.globalData.get("LocalReportLocation");
		currentTime = currentTime.replace('/', '_');

		try {
			if (CommonVariables.osName.toLowerCase().contains("mac")) {
				// Create folder with current date
				src = src + currentDate;

				File report = new File(src);
				if (!report.exists()) {
					report.mkdir();
				}

				// Create folder with Project name
				src = src + "/" + CommonVariables.tcinfo.getCountry();

				report = new File(src);
				if (!report.exists()) {
					report.mkdir();
				}

				// Create folder with Environment name
				src = src + "/" + CommonVariables.tcinfo.getEnvironment();

				report = new File(src);
				if (!report.exists()) {
					report.mkdir();
				}

				// Create folder with TC name
				src = src + "/" + CommonVariables.TCName + "_" + currentTime;

				report = new File(src);
				if (!report.exists()) {
					report.mkdir();
				}

				CommonVariables.CurrentTCLogLocation = src;

				// Create notepad with TC name
				src = src + "/" + CommonVariables.TCName + ".txt";

				report = new File(src);
				if (!report.exists()) {
					report.createNewFile();
				}

				CommonVariables.CurrentTCNotPadLocation = src;

			} else if (CommonVariables.osName.toLowerCase().contains("window")) {
				// Create folder with current date
				src = src + currentDate;

				File report = new File(src);
				if (!report.exists()) {
					report.mkdir();
				}

				// Create folder with Project name
				src = src + "\\" + CommonVariables.tcinfo.getCountry();

				report = new File(src);
				if (!report.exists()) {
					report.mkdir();
				}

				// Create folder with Environment name
				src = src + "\\" + CommonVariables.tcinfo.getEnvironment();

				report = new File(src);
				if (!report.exists()) {
					report.mkdir();
				}

				// Create folder with TC name
				src = src + "\\" + CommonVariables.TCName + "_" + currentTime;

				report = new File(src);
				if (!report.exists()) {
					report.mkdir();
				}

				CommonVariables.CurrentTCLogLocation = src;

				// Create notepad with TC name
				src = src + "\\" + CommonVariables.TCName + ".txt";

				report = new File(src);
				if (!report.exists()) {
					report.createNewFile();
				}

				CommonVariables.CurrentTCNotPadLocation = src;
			}
		} catch (Exception ex) {
			Logger.logFatalError("Unable to create log location. Following exception occurred: \n" + ex.getMessage());
		}
	}

	// Generate notepad report for test case
	public static void generateTextReport() {
		try {
			FileWriter fw = new FileWriter(CommonVariables.CurrentTCNotPadLocation);
			BufferedWriter bw = new BufferedWriter(fw);

			bw.write(CommonVariables.setupLogs.toString());

			for (LoggerInfo loggerInfo : CommonVariables.loggerInfo) {
				bw.write(loggerInfo.tcstep.stepID + "-->" + loggerInfo.tcstep.stepDesc);
				bw.write("\n----------------------------------------------------------");
				bw.write("\n");
				bw.write("Action: " + loggerInfo.tcstep.action);
				bw.write("\n");
				bw.write("Target: " + loggerInfo.tcstep.target);
				bw.write("\n");
				bw.write("\n");
				bw.write("LogDetails: \n------------\n" + loggerInfo.testStepLog);
				bw.write("\n");
				bw.write("Step Result: " + loggerInfo.testStepResult);
				bw.write("\n==============================================================================\n");
			}
			bw.write(CommonVariables.cleanupLogs.toString());
			bw.write(
					"\n#######################################################################################################################");
			bw.write("\nTest case run duration: +" + CommonVariables.testDuration);
			bw.write(
					"\n########################################TEST CASE EXECUTION COMPLETED##################################################");
			bw.flush();
			bw.close();
		} catch (Exception ex) {
			Logger.logFatalError("Unable to Generate log for the current test run. Following exception occurred: \n"
					+ ex.getMessage());
		}
	}

	// Generate extent report
	public static void generateExtentReport() {
		try {
			System.out.println("Initializing ExtentReport generation");
			CommonVariables.htmlReportLink = CommonVariables.CurrentTCLogLocation + "/" + CommonVariables.TCName
					+ ".html";
			ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(CommonVariables.htmlReportLink);
			ExtentReports extent = new ExtentReports();
			extent.attachReporter(htmlReporter);

			ExtentTest logger = extent.createTest(CommonVariables.TCName);

			StringBuilder setupLogsDetail = new StringBuilder();
			StringBuilder cleanupLogsDetails = new StringBuilder();

			for (String setupLogs : CommonVariables.setupHtmlLogs) {
				setupLogsDetail.append(setupLogs + "</br>");
				// logger.info(setupLogs);
			}
			logger.info(setupLogsDetail.toString());

			for (LoggerInfo loggerInfo : CommonVariables.loggerInfo) {
				if (loggerInfo.testStepResult.equals("PASSED")) {
					logger.log(Status.PASS,
							loggerInfo.tcstep.stepID + ".) " + loggerInfo.tcstep.stepDesc + "</br>Action: "
									+ loggerInfo.tcstep.action + "</br>Target: " + loggerInfo.tcstep.target
									+ "</br>LogDetails: </br>------------</br>" + loggerInfo.testStepLog);
				} else if (loggerInfo.testStepResult.equals("FAILED")) {
					logger.log(Status.ERROR,
							loggerInfo.tcstep.stepID + ".) " + loggerInfo.tcstep.stepDesc + "</br>Action: "
									+ loggerInfo.tcstep.action + "</br>Target: " + loggerInfo.tcstep.target
									+ "</br>LogDetails: </br>------------</br>" + loggerInfo.testStepLog + "</br>",
							MediaEntityBuilder.createScreenCaptureFromPath(loggerInfo.testStepScreenshot).build());
				} else if (loggerInfo.testStepResult.equals("WARNING")) {
					logger.log(Status.FAIL,
							loggerInfo.tcstep.stepID + ".) " + loggerInfo.tcstep.stepDesc + "</br>Action: "
									+ loggerInfo.tcstep.action + "</br>Target: " + loggerInfo.tcstep.target
									+ "</br>LogDetails: </br>------------</br>" + loggerInfo.testStepLog + "</br>",
							MediaEntityBuilder.createScreenCaptureFromPath(loggerInfo.testStepScreenshot).build());

				} else {
					// logger.
				}
			}

			// logger.info(CommonVariables.cleanupLogs.toString());
			for (String cleanupLogs : CommonVariables.cleanupHtmlLogs) {
				cleanupLogsDetails.append(cleanupLogs + "</br>");
				// logger.info(cleanupLogs);
			}
			logger.info(cleanupLogsDetails.toString());
			logger.info("Test case run duration: " + CommonVariables.testDuration);
			logger.log(Status.INFO, "END OF EXECUTION",
					MediaEntityBuilder.createScreenCaptureFromPath(CommonVariables.eoeStepScreenshot).build());

			extent.flush();
		} catch (Exception ex) {
			Logger.logFatalError(
					"Unable to Generate Html report for the current test run. Following exception occurred: \n"
							+ ex.getMessage());
		}
	}

	// Update test result in ES
	public static void updateTestResultInES() {
		try {
			CommonVariables.tcResultES = new TestCaseResult(CommonVariables.tcRunID, CommonVariables.TCName,
					CommonVariables.osName, CommonVariables.insightuiBrowser, CommonVariables.tcinfo,
					CommonVariables.executionStatus.toString(), CommonVariables.tcResult.toString(),
					CommonVariables.tcFailReason, CommonVariables.tcStartTime, CommonVariables.tcEndTime,
					CommonVariables.tcOutput, "file://" + CommonVariables.htmlReportLink, CommonVariables.testDuration);

			JSONObject jobject = new JSONObject(CommonVariables.tcResultES);
			System.out.println(jobject.toString(4));

			RestClient ESClient = new RestClient("http://localhost:9200/insight-test-result/test", HttpMethods.POST,
					jobject, "application/json");
			HttpResponse response = ESClient.executeRequest();

			BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			JSONObject jsonResponse = new JSONObject(br.readLine());
			System.out.println(jsonResponse);

			if (response.getStatusLine().getStatusCode() == 201) {
				System.out.println(response.getStatusLine().getStatusCode());
			} else {
				Logger.logMessage("Exception occured while updating the test case result in index.\nStatus Code: "
						+ response.getStatusLine().getStatusCode());
			}

		} catch (Exception ex) {
			Logger.logMessage(
					"Unable to update the index with test result. Following exception occurred: \n" + ex.getMessage());
		}
	}
}
