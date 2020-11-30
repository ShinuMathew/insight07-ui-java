package com.insightui.actions;

import org.openqa.selenium.Alert;

import com.insightui.logger.Logger;
import com.insightui.variables.CommonVariables;

public class PopUpActions {
	// Handle Javascript pop up
	public static void handleJSAlert() {
		try {
			Alert jsAlert = CommonVariables.insightDriver.switchTo().alert();
			if (CommonVariables.currentStepvalue.toUpperCase().equals("OK"))
				jsAlert.accept();
			else if (CommonVariables.currentStepvalue.toUpperCase().equals("CANCEL"))
				jsAlert.dismiss();
		}
		catch (Exception ex) {
			Logger.logError("Following exception occured when handling the pop up.\n" + ex.getMessage());
		}
	}
}
