package com.insightui.plugins;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.insightui.logger.Logger;
import com.insightui.variables.*;

public class ExcelHandler {

	public static XSSFWorkbook workbook;
	public static Sheet sheet;
	public static String doc;
	public static String sheetName;
	public static Iterator<Row> rowIterator;

	public ExcelHandler(String doc, String sheetName) {

		this.doc = doc;
		this.sheetName = sheetName;

	}

	public Iterator<Row> readSheet() {

		File file;
		FileInputStream excel;

		try {

			file = new File(doc);
			excel = new FileInputStream(file);

			workbook = new XSSFWorkbook(excel);
			sheet = workbook.getSheet(CommonVariables.TCName);

			rowIterator = sheet.rowIterator();

			return rowIterator;
		}

		catch (Exception ex) {

			Logger.logError("Unable to read from " + sheet + " in " + doc + ". Following exception occured. "
					+ ex.getMessage());
			return null;
		}

		finally {

		}
	}
}
