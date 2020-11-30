package com.insightui.browsers;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.insightui.actions.ElementActions;
import com.insightui.logger.Logger;
import com.insightui.variables.CommonVariables;

public class BrowserSetUp {

	public static void launchBrowser() {
		if (CommonVariables.insightuiBrowser.equalsIgnoreCase("Chrome")) {
			ChromeOptions options = new ChromeOptions();
			options.addArguments("start-maximized");
			options.addArguments("disable-infobars");
			if (CommonVariables.isheadless)
				options.addArguments("--headless");
			if (CommonVariables.osName.toLowerCase().contains("mac")) {
				String path = CommonVariables.globalPath + CommonVariables.globalData.get("ChromeDriver")
						+ "/chromedriver";
				System.setProperty("webdriver.chrome.driver", path);
			} else if (CommonVariables.osName.toLowerCase().contains("window"))
				System.setProperty("webdriver.chrome.driver", CommonVariables.globalPath
						+ CommonVariables.globalData.get("ChromeDriver") + "\\chromedriver.exe");			
			// Launch Chrome
			CommonVariables.insightDriver = new ChromeDriver(options);
			CommonVariables.insightDriver.manage().window().maximize();
			// Implicit Wait
			CommonVariables.insightDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			CommonVariables.insightDriver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
			CommonVariables.insightDriver.manage().timeouts().setScriptTimeout(100, TimeUnit.SECONDS);
			Logger.logMessage("Chrome browser launched successfully");
		} else if (CommonVariables.insightuiBrowser.equalsIgnoreCase("IE")
				|| CommonVariables.insightuiBrowser.equalsIgnoreCase("IExplorer")) {

		} else if (CommonVariables.insightuiBrowser.equalsIgnoreCase("Firefox")) {

		}

		else if (CommonVariables.insightuiBrowser.equalsIgnoreCase("Safari")) {
			CommonVariables.insightDriver = new SafariDriver();
			CommonVariables.insightDriver.manage().window().maximize();
			// Implicit Wait
			CommonVariables.insightDriver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
			CommonVariables.insightDriver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
			CommonVariables.insightDriver.manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
		} else if (CommonVariables.insightuiBrowser.equalsIgnoreCase("Edge")) {

		} else {
			System.out.println("Please specify a browser name");
		}
	}

	public static void deleteBrowserCookies() {
		try {
			CommonVariables.insightDriver.manage().deleteAllCookies();
			for (Cookie cookie : CommonVariables.insightDriver.manage().getCookies()) {
				Logger.logMessage("Deleting cookie: " + cookie + "/n");
				CommonVariables.insightDriver.manage().deleteCookie(cookie);
			}
			Logger.logMessage("Browser cookies deleted successfully");
		} catch (Exception ex) {
			Logger.logFatalError("Following Exception occured while deleting browser cookies. /n" + ex.getMessage());
		}
	}

	/* Implemewnt LogInfor for message that are not to be displayed in main log */

	public static void waitUntilDOMLoad() {
		try {
			String domLoadScript = "if(document != undefined && document.readyState) {return document.readyState;} else {return undefined;}";
			while (true) {
				String domLoad = ((JavascriptExecutor) CommonVariables.insightDriver).executeScript(domLoadScript)
						.toString();
				if (domLoad.toUpperCase().contains("COMPLETE") || domLoad.toUpperCase().contains("LOADED")) {
					System.out.println("DOM readystate: \"" + domLoad + "\"");
					break;
				} else
					continue;
			}
			try {
				boolean ajaxCallsComplete = false;
				int count = 0;
				do {
					ajaxCallsComplete = (boolean) ((JavascriptExecutor) CommonVariables.insightDriver)
							.executeScript("return jQuery.active==0");
					System.out.println("ajaxcomplete status: " + ajaxCallsComplete);
					count++;
				} while (!ajaxCallsComplete);
			} catch (Exception ex) {
				if (ex.getMessage().contains("not defined")) {
					System.out.println("Skipping ajaxCallsComplete as the page does not contains JQuery");
				}
			}
		} catch (Exception ex) {
			Logger.logFatalError(
					"Unable to execute waitUntilDOMLoad() due to the following exception. " + ex.getClass());
		}
	}

	public static void waitForSpecificElement() {
		try {
			int waitCount = 0;
			while (waitCount < 5) {			
				if (!CommonVariables.insightDriver.findElements(ElementActions.getLocator()).isEmpty()) {
					if (!CommonVariables.insightuiBrowser.toUpperCase().equals("SAFARI")) {
						Point loc = CommonVariables.insightDriver.findElement(ElementActions.getLocator())
								.getLocation();
						System.out.println("Element cordinate:\n" + loc.toString());
					}
					break;
				} else {
					waitCount++;
					continue;
				}
			}
		} catch (Exception ex) {
			System.out.println("Interuption occured when executing wait. Following excpetion occured: " + ex.getClass());
		}
	}

	public static void waitForElement() {
		try {
			WebDriverWait wait = new WebDriverWait(CommonVariables.insightDriver, 15);
			CommonVariables.insightElement = wait
					.until(ExpectedConditions.visibilityOf(CommonVariables.insightElement));
		} catch (Exception ex) {
			System.out.println(
					"Interuption occured when executing waitForElement. Following excpetion occured: " + ex.getClass());
		}
	}

	public static void webdriverWait(Function<WebDriver, ExpectedCondition<WebElement>> expectedCondition) {
		try {
			WebDriverWait wait = new WebDriverWait(CommonVariables.insightDriver, 15);
			wait.until(expectedCondition.apply(CommonVariables.insightDriver));
		} catch (Exception ex) {
			System.out.println(
					"Interuption occured when executing webdriverWait. Following excpetion occured: " + ex.getClass());
		}
	}

	public static void closeBrowser() {

		try {
			if (CommonVariables.osName.toLowerCase().contains("mac")) {
				if (CommonVariables.closeAllBrowser == true) {
					try {
						CommonVariables.insightDriver.quit();
						Logger.logMessage("Closed browser successfully..");
						// Kill task from shell script
					} catch (Exception ex) {
						Logger.logFatalError("Unable to quit all browsers. Exception occured: " + ex.getMessage());
					}
				}
			}
		} catch (Exception ex) {
			Logger.logFatalError("Unable to quit browser. Exception occured: " + ex.getMessage());
		}
	}
}
