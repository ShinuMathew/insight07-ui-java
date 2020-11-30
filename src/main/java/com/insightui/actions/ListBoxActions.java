package com.insightui.actions;

import org.openqa.selenium.remote.Command;
import org.openqa.selenium.support.ui.Select;

import com.insightui.logger.Logger;
import com.insightui.variables.CommonVariables;

public class ListBoxActions {
	// Select dropdown
	public static void selectFromDropDown() {
		try {
			if (CommonVariables.currentStepTarget.contains("##")) {
				String targets[] = CommonVariables.currentStepTarget.split("##");
				CommonVariables.currentStepTarget = targets[1];// "(//b[@class='button'])[2]";
				ElementActions.getWebElement();
				CommonVariables.insightElement.click();
				CommonVariables.currentStepTarget = targets[0];// "//select[@name=‘state’]";
				ElementActions.getWebElement();
				Select selectDropDown = new Select(CommonVariables.insightElement);
				selectDropDown.selectByVisibleText(CommonVariables.currentStepvalue);
			} else {
				ElementActions.getWebElement();
				Select selectDropDown = new Select(CommonVariables.insightElement);
				selectDropDown.selectByVisibleText(CommonVariables.currentStepvalue);
			}
		} catch (Exception ex) {
			Logger.logError("Following exception occured when selecting " + CommonVariables.currentStepvalue
					+ " from dropdown." + ex.getMessage());
		}
	}

	// Select/Unselect checkbox
	public static void selectCheckbox() {
		try {

		} catch (Exception ex) {

		}
	}

	// Select/Unselect Radio button
	public static void selectRadio() {
		try {

		} catch (Exception ex) {

		}
	}
}
