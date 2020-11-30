package com.insightui.variables;

import java.sql.Connection;

import com.google.common.base.Stopwatch;
import com.insightui.pojo.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.insightui.pojo.TCInfo;

public class CommonVariables {

	public static String tcRunID;
	public static String TCName;
	public static String testRunConfig;
	public static List<TestCaseDesign> testCaseDesign = new ArrayList<TestCaseDesign>();

	// Utils
	public static String osName;
	public static String propFilePath;
	public static String globalDataPath;
	public static String globalPath;
	public static String localPath;
	public static boolean isLocalExecution;
	public static boolean isRunIDExecution;

	// DB connection
	public static String url;
	public static String user;
	public static String password;
	public static Connection con;
	public static Statement stmt;
	public static ResultSet rs;
	public static PreparedStatement prepStmt;

	public enum DBMS {
		MYSQL, POSTGRES
	};

	// Browser
	public static String insightuiBrowser;
	public static String driverPath;
	public static boolean closeAllBrowser = true;
	public static boolean isheadless = false;

	// Global Data
	public static HashMap<String, String> globalData = new HashMap<>();

	// Driver
	public static WebDriver insightDriver;
	public static WebElement insightElement;
	public static List<WebElement> insightElementsList;

	// TestInit
	public static boolean isStepsStoredInDB;
	public static TCInfo tcinfo;
	public static boolean isDataStoredInDB;

	// TCoutput
	public static Map<String, String> tcOutput = new HashMap<String, String>();

	// CurrentTC
	public static int currentStepNo;
	public static String currentStepDesc;
	public static String currentStepAction;
	public static String currentStepLocatorType;
	public static String currentStepTarget;
	public static String currentStepvalue;
	public static String currentStepScreenshot;
	public static TestCaseDesign currentStepTCDesign;

	// Storage
	public static Map<String, String> insightHashMap = new HashMap<String, String>();

	// Logger
	public static Stopwatch watch;
	public static String testDuration;
	public static boolean isStepFailed;
	public static int stepNo;
	public static String eoeStepScreenshot;
	public static boolean isTestCaseFailed = false;

	public enum STEPRESULT {
		PASSED, FAILED, WARNING
	};

	public enum TCRESULT {
		PASSED, FAILED
	};

	public enum LOGTYPE {
		INFO, MESSAGE, WARNING, ERROR
	};

	public static Map<String, String> tcFailReason = new HashMap<String, String>();
	public static STEPRESULT currentStepResult;
	public static TCRESULT tcResult;
	public static LOGTYPE logType;
	public static StringBuilder currentStepLogs;
	public static StringBuilder setupLogs;
	public static List<String> setupHtmlLogs = new ArrayList<String>();
	public static StringBuilder cleanupLogs;
	public static List<String> cleanupHtmlLogs = new ArrayList<String>();
	public static List<LoggerInfo> loggerInfo = new ArrayList<LoggerInfo>();
	public static String CurrentTCLogLocation;
	public static String CurrentTCNotPadLocation;
	public static String tcStartTime;
	public static String tcEndTime;

	public enum EXECUTIONSTATUS {
		YETTOSTART, STARTED, INPROGRESS, INTERRUPTED, COMPLETED
	}

	public static EXECUTIONSTATUS executionStatus;
	public static TestCaseResult tcResultES;
	public static String htmlReportLink;

	public enum HttpMethods {
		GET, POST, PUT, DELETE, PATCH
	}
}