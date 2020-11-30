package com.insightui.plugins;

import java.io.File;
import java.io.FileReader;
import java.util.List;

import com.insightui.logger.Logger;
import com.insightui.pojo.TestCaseResult;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

public class CSVHandler {

	public String csvLocation;
	public TestCaseResult output;
	public List<String[]> allData;

	public CSVHandler(String csvLocation) {

		this.csvLocation = csvLocation;
		// this.output = output;
	}

	public List<String[]> ReadCSV() {
		try {

			File fname = new File(csvLocation);
			FileReader filereader = new FileReader(fname);

			CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
			allData = csvReader.readAll();

		} catch (Exception ex) {

			Logger.logFatalError(
					"Unable to read the CSV doc " + csvLocation + ". Following exception occured\n" + ex.getMessage());
		} finally {
			return allData;
		}

	}

}
