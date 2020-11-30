package com.insightui.ignitor;

import java.util.ArrayList;
import java.util.List;

import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

public class Ignitor {

	public static void main(String[] args) {
		try {
			for (String arg : args) {
				System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
				System.out.println("\n\n\"RunID: " + arg + "\"  Execution Started\n\n");
				// Create TestNG instance
				TestNG runner = new TestNG();
				// Create TestNG suite instance
				XmlSuite suite = new XmlSuite();
				suite.setName("InsightTestSuite");
				// Create TestNG test instance
				XmlTest test = new XmlTest(suite);
				test.setName("InsightTest");
				test.addParameter("tc_runID", arg); // "ROM_QA_Demo_Smoke_CODTest"
				List<XmlClass> myClasses = new ArrayList<XmlClass>();
				myClasses.add(new XmlClass("com.insightui.testcases.TestCaseEngine"));
				test.setXmlClasses(myClasses);
				List<XmlTest> myTests = new ArrayList<XmlTest>();
				myTests.add(test);
				suite.setTests(myTests);
				List<XmlSuite> mySuites = new ArrayList<XmlSuite>();
				mySuites.add(suite);
				runner.setXmlSuites(mySuites);
				runner.run();
				System.out.println("\n\n\"RunID: " + arg + "\"  Execution completed\n\n");
				System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
			}
		} catch (Exception ex) {
			System.out.println(
					"Exception occured with ignitor class. Unable to initialize execution due to following exception: \n"
							+ ex.getMessage());
		}
	}

}

/**
 * 
 * List<String> suitefiles=new ArrayList<String>();
 * suitefiles.add(System.getProperty("user.dir")+"/testng.xml");
 * ///Users/shinu.mathew/git/insightui/testng.xml
 *
 **/