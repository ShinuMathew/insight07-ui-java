package com.insightui.actions;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.http.HttpResponse;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.insightui.browsers.BrowserSetUp;
import com.insightui.constants.SKUTYPES;
import com.insightui.logger.Logger;
import com.insightui.plugins.RestClient;
import com.insightui.utils.UtilityManager;
import com.insightui.variables.CommonVariables;
import com.insightui.variables.CommonVariables.HttpMethods;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class SpecificActions {

	// ConstructShopfrontURL
	public static void constructShopfrontURL() {
		try {
			String[] value = CommonVariables.currentStepvalue.split("##");
			String skuType = value[0];
			String quantity = value[1];
			String shopFront_Url = value[2];
			String result_url = value[3];
			String sku = "";
			if (skuType.startsWith("${"))
				sku = UtilityManager.getValueFromInsightHashMap(skuType);
			else
				sku = skuType;
			String shopFrontUrl = UtilityManager.getValueFromInsightHashMap(shopFront_Url);
			// validateSkuInventory();
			String resultUrl = shopFrontUrl + sku + "," + quantity;
			UtilityManager.storeValueToInsightHashMap(result_url, resultUrl);
		} catch (Exception ex) {
			Logger.logError("Unable to construct Shopfront url. Following exception occured: " + ex.getMessage());
		}
	}

	// Update sku managed inventory
	private static void setManagedInventory(String skuID, SKUTYPES skuType) {
		try {
			Map<String, Object> sku_settings = new HashMap<>();
			sku_settings.put("sku", skuID);
			sku_settings.put("max_quantity_back_order", 0);
			sku_settings.put("max_quantity_managed", 10000);
			sku_settings.put("allow_back_order", false);
			sku_settings.put("allow_order", true);
			sku_settings.put("use_managed_quantity", true);
			Map<String, Object> data = new HashMap<>();
			data.put("sku", skuID);
			data.put("sku_settings", sku_settings);
			JSONObject jsonObject = new JSONObject();
			jsonObject.putAll(data);
			JSONArray jsonArray = new JSONArray();
			jsonArray.add(jsonObject);
			RestClient client = new RestClient(CommonVariables.insightHashMap.get("MANAGEDINVENTORY_REQURL"),
					HttpMethods.PUT, jsonArray, "application/json");
			HttpResponse response = client.executeDynamicRequest();
			Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
		} catch (Exception ex) {
			Logger.logError("Unable to setManagedInventory. Following exception occured: " + ex.getMessage());
		}
	}

	public static void selectMultipleDeliveryMode() {
		try {
			BrowserSetUp.waitUntilDOMLoad();
			// CommonVariables.currentStepTarget= "//h2[text()='Delivery
			// options']/../../div[2]/div[1]/label/span[1]";
			ElementActions.getWebElements();
			CommonVariables.insightElementsList.stream().forEach(ele -> ele.click());
		} catch (Exception ex) {
			Logger.logError("Unable to selectMultipleDeliveryMode. Following exception occured: " + ex.getMessage());
		}
	}

	// Clear shopping cart
	public static void clearShoppingCart() {
		try {
			BrowserSetUp.waitUntilDOMLoad();
			CommonVariables.insightElementsList = CommonVariables.insightDriver
					.findElements(ElementActions.getLocator());
			for (WebElement ele : CommonVariables.insightElementsList) {
				ele.click();
				BrowserSetUp.waitUntilDOMLoad();
			}
		} catch (NoSuchElementException noex) {
			Logger.logMessage("No products available in the cart");
		} catch (Exception ex) {

		}
	}

	// Apply offer and verify if offer is applied successfully
	public static void applyOfferAndVerify() {
		try {
			BrowserSetUp.waitUntilDOMLoad();
			String[] offerPaths = CommonVariables.currentStepTarget.split("##");
			String offerInput = offerPaths[0];
			String offerApplyButton = offerPaths[1];
			String offerApplyConfirm = offerPaths[2];
			String offerID = CommonVariables.currentStepvalue;
			CommonVariables.currentStepTarget = offerInput;
			ElementActions.getWebElement();
			CommonActions.type();
			ElementActions.highlightElementPass();
			CommonVariables.currentStepTarget = offerApplyButton;
			ElementActions.getWebElement();
			CommonActions.clickAndWait();
			ElementActions.highlightElementPass();
			if (CommonVariables.tcinfo.getCountry().equals("ROU")) {
				CommonVariables.currentStepTarget = offerApplyConfirm;
				ElementActions.getWebElement();
				ElementActions.highlightElementPass();
				if (CommonVariables.insightElement.isDisplayed())
					Logger.logMessage("Offer applied successfully...");
				else
					Logger.logError("Offer was not applied... Update the OfferID, it might have expired");
			} else {
				CommonVariables.currentStepTarget = offerApplyConfirm;
				ElementActions.getWebElement();
				ElementActions.highlightElementPass();
				if (CommonVariables.insightElement.getText().contains(offerID))
					Logger.logMessage("Offer applied successfully...");
				else
					Logger.logError("Offer was not applied... Update the OfferID, it might have expired");
			}

		} catch (Exception ex) {
			Logger.logError("Unable to apply the Offer. Following exception occured: \n" + ex.getMessage());
		}
	}
}
