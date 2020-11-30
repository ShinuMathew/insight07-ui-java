package com.insightui.actions;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.ss.formula.functions.Match;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.interactions.Actions;
import com.insightui.browsers.BrowserSetUp;
import com.insightui.logger.Logger;
import com.insightui.utils.UtilityManager;
import com.insightui.variables.CommonVariables;
import bsh.util.Util;

public class CommonActions {

	// Open URL
	public static void open() {
		// BrowserSetUp.waitUntilDOMLoad();
		try {
			if (CommonVariables.currentStepvalue.contains("$")) {
				String url = UtilityManager.getValueFromInsightHashMap(CommonVariables.currentStepvalue);
				Logger.logMessage("Launching " + url);
				CommonVariables.insightDriver.get(url);
			} else {
				Logger.logMessage("Launching " + CommonVariables.currentStepvalue);
				CommonVariables.insightDriver.get(CommonVariables.currentStepvalue);
			}
			BrowserSetUp.waitUntilDOMLoad();
		} catch (Exception ex) {
			Logger.logError("Unable to launch the url. Follfowing exception occured: " + ex.getClass());
		}
	}

	// Verify page title
	public static void verifyPageTitle() {
		try {
			String pageTitle = CommonVariables.insightDriver.getTitle();
			if (pageTitle.equals(CommonVariables.currentStepvalue)) {
				Logger.logMessage("Page title matched. ");
				Logger.logMessage("Expected value: " + CommonVariables.currentStepvalue);
				Logger.logMessage("Actual value: " + pageTitle);
			} else {
				Logger.logWarning("Page title not matched. ");
				Logger.logWarning("Expected value: " + CommonVariables.currentStepvalue);
				Logger.logWarning("Actual value: " + pageTitle);
			}
		} catch (Exception ex) {
			Logger.logError(
					"Unable to get the title of the current page. Following exception occured: " + ex.getClass());
		}

	}

	// Verify text
	public static void verifyText() {
		try {
			BrowserSetUp.waitForElement();
			if (CommonVariables.currentStepvalue.contains("$")) {
				ElementActions.getWebElement();
				String text = CommonVariables.insightElement.getText();
				String expectedText = UtilityManager.getValueFromInsightHashMap(CommonVariables.currentStepvalue);
				if (expectedText.contains(text)) {
					Logger.logMessage("Text matched. ");
					Logger.logMessage("Expected value: " + expectedText);
					Logger.logMessage("Actual value: " + text);
				} else {
					Logger.logWarning("Text not matched. ");
					Logger.logWarning("Expected value: " + expectedText);
					Logger.logWarning("Actual value: " + text);
				}
			} else {
				ElementActions.getWebElement();
				String text = CommonVariables.insightElement.getText();
				if (CommonVariables.currentStepvalue.contains(text)) {
					Logger.logMessage("Text matched. ");
					Logger.logMessage("Expected value: " + CommonVariables.currentStepvalue);
					Logger.logMessage("Actual value: " + text);
				} else {
					Logger.logWarning("Text not matched. ");
					Logger.logWarning("Expected value: " + CommonVariables.currentStepvalue);
					Logger.logWarning("Actual value: " + text);
				}
			}

		} catch (Exception ex) {
			Logger.logError("Unable to click the element. Following exception occured: " + ex.getClass());
		}
	}

	// Store text
	public static void storeText() {
		try {
			BrowserSetUp.waitForElement();
			ElementActions.getWebElement();
			String text = CommonVariables.insightElement.getText();
			UtilityManager.storeValueToInsightHashMap(CommonVariables.currentStepvalue, text);
			Logger.logMessage(text + " stored in hashmap at: " + CommonVariables.currentStepvalue);
		} catch (Exception ex) {
			Logger.logError("Unable to click the element. Following exception occured: " + ex.getClass());
		}
	}

	// Click element and wait
	public static void clickAndWait() {
		try {
			BrowserSetUp.waitUntilDOMLoad();
			ElementActions.getWebElement();
			scrollToElement();
			CommonVariables.insightElement.click();
			Logger.logMessage("Element clicked successfully...");
		} catch (Exception ex) {
			Logger.logError("Unable to click the element. Following exception occured: " + ex.getClass());
		}
	}

	// Click if exists
	public static void clickIfExists() {
		try {
			BrowserSetUp.waitUntilDOMLoad();
			CommonVariables.insightElement = CommonVariables.insightDriver.findElement(ElementActions.getLocator());
			scrollToElement();
			CommonVariables.insightElement.click();
		} catch (Exception ex) {
			Logger.logMessage("Unable to click the element. Following exception occured: " + ex.getClass());
		}
	}

	// Scroll to element
	public static void scrollToElement() {
		try {
			Actions builder = new Actions(CommonVariables.insightDriver);
			builder.moveToElement(CommonVariables.insightElement);
			builder.perform();
			((JavascriptExecutor) CommonVariables.insightDriver).executeScript(
					"arguments[0].scrollIntoView({\n" + "            behavior: 'auto',\n"
							+ "            block: 'center',\n" + "            inline: 'center'\n" + "        });",
					CommonVariables.insightElement);
			ElementActions.highlightElement();
			// System.out.println("Success");
		} catch (Exception ex) {
			Logger.logError("Unable to scroll to element. Following exception occured: " + ex.getClass());
		}
	}

	// Type
	public static void type() {
		try {
			if (CommonVariables.currentStepvalue.contains("$") || CommonVariables.currentStepvalue.contains("{")
					|| CommonVariables.currentStepvalue.contains("}")) {
				String text = UtilityManager.getValueFromInsightHashMap(CommonVariables.currentStepvalue);
				ElementActions.getWebElement();
				scrollToElement();
				CommonVariables.insightElement.sendKeys(text);
				BrowserSetUp.waitUntilDOMLoad();
				Logger.logMessage("Text was typed successfully...");
			} else {
				ElementActions.getWebElement();
				scrollToElement();
				CommonVariables.insightElement.sendKeys(CommonVariables.currentStepvalue);
				BrowserSetUp.waitUntilDOMLoad();
				Logger.logMessage("Text was typed successfully...");
			}

		} catch (Exception ex) {
			Logger.logError("Unable to click the element. Following exception occured: " + ex.getClass());
		}
	}

	// Type if exists
	public static void typeIfExists() {
		try {
			if (CommonVariables.currentStepvalue.contains("$") || CommonVariables.currentStepvalue.contains("{")
					|| CommonVariables.currentStepvalue.contains("}")) {
				String text = UtilityManager.getValueFromInsightHashMap(CommonVariables.currentStepvalue);
				BrowserSetUp.waitUntilDOMLoad();
				CommonVariables.insightElement = CommonVariables.insightDriver.findElement(ElementActions.getLocator());
				scrollToElement();			
				CommonVariables.insightElement.sendKeys(text);
				BrowserSetUp.waitUntilDOMLoad();			
				Logger.logMessage("Text was typed successfully...");
			} else {
				BrowserSetUp.waitUntilDOMLoad();
				CommonVariables.insightElement = CommonVariables.insightDriver.findElement(ElementActions.getLocator());
				scrollToElement();			
				CommonVariables.insightElement.sendKeys(CommonVariables.currentStepvalue);
				BrowserSetUp.waitUntilDOMLoad();			
				Logger.logMessage("Text was typed successfully...");
			}
		} catch (Exception ex) {
			Logger.logMessage("Unable to click the element. Following exception occured: " + ex.getClass());
		}
	}

	// Verify if element present
	public static void verifyElementPresent() {
		try {
			ElementActions.getWebElement();
			if (CommonVariables.insightElement.isDisplayed())
				Logger.logMessage("The current element is present in page. ");
			else
				Logger.logWarning("The current element is present in page. ");		
		} catch (Exception ex) {
			Logger.logWarning("Unable to click the element. Following exception occured: " + ex.getClass());
		}
	}

	// Wait in seconds
	public static void waitInSeconds() {
		try {
			int seconds = Integer.parseInt(CommonVariables.currentStepvalue);
			for (int iter = 0; iter < seconds; iter++) {
				
			}
			Logger.logMessage("Script waited successfully for " + seconds + " seconds...");
		} catch (Exception ex) {
			Logger.logWarning("Unable to induce wait in seconds due to the following exception: " + ex.getClass());
		}
	}

	// Switch to tab
	public static void switchToTab() {
		try {
			int windowId = Integer.parseInt(CommonVariables.currentStepvalue);
			ArrayList windowHandles = new ArrayList(CommonVariables.insightDriver.getWindowHandles());
			CommonVariables.insightDriver.switchTo().window((String) windowHandles.get(windowId));
		} catch (Exception ex) {
			Logger.logError("Unable to switch to the requested tab. Following exception occured: " + ex.getClass());
		}
	}

	// Switch to IFrame
	public static void switchToIframe() {
		try {
			ElementActions.getWebElement();
			CommonVariables.insightDriver.switchTo().frame(CommonVariables.insightElement);
		} catch (Exception ex) {
			Logger.logError("Unable to switch to iframe. Following exception occured: " + ex.getClass());
		}
	}

	// Switch to default context
	public static void switchToDefaultContext() {
		try {
			CommonVariables.insightDriver.switchTo().defaultContent();
		} catch (Exception ex) {
			Logger.logError("Unable to switch to the default context. Following exception occured: " + ex.getClass());
		}
	}

	// Store prices
	public static void storePrice() {
		try {
			ElementActions.getWebElement();
			Pattern p = Pattern.compile("[\\d\\.]{1,15}");
			Matcher m = p.matcher(CommonVariables.insightElement.getText());
			while (m.find()) {
				UtilityManager.storeValueToInsightHashMap(CommonVariables.currentStepvalue, m.group());
				Logger.logMessage("Price stored is hashmap to key: " + CommonVariables.currentStepvalue);
			}
		} catch (Exception ex) {
			Logger.logError("Unable to store the price. Following exception occured: " + ex.getClass());
		}
	}

	// Store expected text
	public static void storeExpectedText() {
		try {
			String values[] = CommonVariables.currentStepvalue.split("##");
			String regexPattern = values[0];
			String key = values[1];
			ElementActions.getWebElement();
			Pattern p = Pattern.compile(regexPattern);
			String text = CommonVariables.insightElement.getText();
			Matcher m = p.matcher(text);
			while (m.find()) {
				UtilityManager.storeValueToInsightHashMap(key, m.group());
				Logger.logMessage("Text is stored in hashmap to key: " + key);
			}
		} catch (Exception ex) {
			Logger.logError("Unable to store the expected text. Following exception occured: " + ex.getClass());
		}
	}

	// Get and store test case response
	public static void getAndStoreTCOutput() {
		try {
			String key = CommonVariables.currentStepvalue.replace("$", "");
			key = key.replace("{", "");
			key = key.replace("}", "");
			String value = UtilityManager.getValueFromInsightHashMap(key);
			if (value != null)
				CommonVariables.tcOutput.put(key, value);
			else
				CommonVariables.tcOutput.put(key, "No value found for the key");
		} catch (Exception ex) {
			Logger.logMessage("Unable to store the tcoutput text. Following exception occured: " + ex.getClass());
		}
	}

	public static void verifyPrice() {
		try {
			BrowserSetUp.waitForElement();
			if (CommonVariables.currentStepvalue.contains("$")) {
				ElementActions.getWebElement();
				String text = CommonVariables.insightElement.getText();
				System.out.println(text);
				String expectedPrice = UtilityManager.getValueFromInsightHashMap(CommonVariables.currentStepvalue);
				Pattern p = Pattern.compile("[\\d\\.]{1,10}");
				Matcher m = p.matcher(text);
				String actualPrice = "";
				while (m.find())
					actualPrice = m.group();
				if (expectedPrice.contains(actualPrice)) {
					Logger.logMessage("Price matched. ");
					Logger.logMessage("Expected value: " + expectedPrice);
					Logger.logMessage("Actual value: " + actualPrice);
				} else {
					Logger.logWarning("Price not matched. ");
					Logger.logWarning("Expected value: " + expectedPrice);
					Logger.logWarning("Actual value: " + actualPrice);
				}
			} else {
				ElementActions.getWebElement();
				String actualPrice = CommonVariables.insightElement.getText();
				if (CommonVariables.currentStepvalue.contains(actualPrice)) {
					Logger.logMessage("Price matched. ");
					Logger.logMessage("Expected value: " + CommonVariables.currentStepvalue);
					Logger.logMessage("Actual value: " + actualPrice);
				} else {
					Logger.logWarning("Price not matched. ");
					Logger.logWarning("Expected value: " + CommonVariables.currentStepvalue);
					Logger.logWarning("Actual value: " + actualPrice);
				}
			}
		} catch (NullPointerException npx) {
			Logger.logWarning("Unable to verify the price. Either of the price is empty: ");
		} catch (Exception ex) {
			Logger.logError("Unable to verify the price. Following exception occured: " + ex.getClass());
		}
	}
}

//Actions builder = new Actions(CommonVariables.insightDriver);
//builder.moveToElement(CommonVariables.insightElement);
//builder.perform();
//JavascriptExecutor executor = (JavascriptExecutor)CommonVariables.insightDriver;
//executor.executeScript("arguments[0].click();", CommonVariables.insightElement);
