package com.insightui.actions;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.insightui.browsers.BrowserSetUp;
import com.insightui.logger.Logger;
import com.insightui.utils.UtilityManager;
import com.insightui.variables.CommonVariables;

public class ElementActions {

	public static void highlightElement() {
		try {
			String script = "arguments[0].style.border = '3px solid red'";
			// CommonActions.scrollToElement();
			JavascriptExecutor js = (JavascriptExecutor) CommonVariables.insightDriver;
			js.executeScript(script, CommonVariables.insightElement);
		} catch (Exception ex) {
			System.out.println("Unable to highlight element. Check if the selected element is present in the DOM");
		}
	}

	public static void highlightElementPass() {
		try {
			String script = "arguments[0].style.border = '3px solid green'";
			JavascriptExecutor js = (JavascriptExecutor) CommonVariables.insightDriver;
			js.executeScript(script, CommonVariables.insightElement);
		} catch (Exception ex) {
			System.out.println("Unable to highlight element. Check if the selected element is present in the DOM");
		}
	}

	public static void unHighlightElement() {
		try {
			String script = "arguments[0].style.border = '3px solid transparent'";
			JavascriptExecutor js = (JavascriptExecutor) CommonVariables.insightDriver;
			js.executeScript(script, CommonVariables.insightElement);
		} catch (Exception ex) {
			System.out.println("Unable to highlight element. Check if the selected element is present in the DOM");
		}
	}

	public static void getWebElement() {
		try {
			BrowserSetUp.waitUntilDOMLoad();
			// BrowserSetUp.waitForSpecificElement();
			BrowserSetUp.webdriverWait(ele -> ExpectedConditions.presenceOfElementLocated(getLocator()));
			// BrowserSetUp.waitForElement();
			CommonVariables.insightElement = CommonVariables.insightDriver.findElement(getLocator());
			highlightElement();
		} catch (Exception ex) {
			Logger.logError(
					"Exception occured when trying to find element. Following exception occured: " + ex.getClass());
		}
	}

	public static void getWebElements() {
		try {
			BrowserSetUp.waitUntilDOMLoad();
			BrowserSetUp.waitForSpecificElement();
			BrowserSetUp.webdriverWait(ele -> ExpectedConditions.presenceOfElementLocated(getLocator()));
			CommonVariables.insightElementsList = CommonVariables.insightDriver.findElements(getLocator());
			if (!CommonVariables.insightElementsList.isEmpty()) {
				CommonVariables.insightElementsList.stream()
						.forEach(ele -> ((JavascriptExecutor) CommonVariables.insightDriver)
								.executeScript("arguments[0].style.border = '3px solid green'", ele));
			}
		} catch (Exception ex) {
			Logger.logError(
					"Exception occured when trying to find the list of similar elements. Following exception occured: "
							+ ex.getClass());
		}
	}

	public static By getLocator() {
		try {
			if (CommonVariables.currentStepLocatorType.toUpperCase().contains("ID")) {
				return By.id(CommonVariables.currentStepTarget);
			} else if (CommonVariables.currentStepLocatorType.toUpperCase().contains("NAME")) {
				return By.name(CommonVariables.currentStepTarget);
			} else if (CommonVariables.currentStepLocatorType.toUpperCase().contains("CLASS")) {
				return By.className(CommonVariables.currentStepTarget);
			} else if (CommonVariables.currentStepLocatorType.toUpperCase().contains("TAGNAME")) {
				return By.tagName(CommonVariables.currentStepTarget);
			} else if (CommonVariables.currentStepLocatorType.toUpperCase().contains("LINKTEXT")) {
				return By.linkText(CommonVariables.currentStepTarget);
			} else if (CommonVariables.currentStepLocatorType.toUpperCase().contains("PARTIALLINKTEXT")) {
				return By.partialLinkText(CommonVariables.currentStepTarget);
			} else if (CommonVariables.currentStepLocatorType.toUpperCase().contains("XPATH")) {
				return By.xpath(CommonVariables.currentStepTarget);
			} else if (CommonVariables.currentStepLocatorType.toUpperCase().contains("CSS")) {
				return By.cssSelector(CommonVariables.currentStepTarget);
			} else {
				Logger.logError("Invalid locator type. Locator type should be one among");
				return null;
			}

		} catch (Exception ex) {
			Logger.logError("Unable to get the locator. Following exception occured: " + ex.getMessage());
			return null;
		}
	}

}
