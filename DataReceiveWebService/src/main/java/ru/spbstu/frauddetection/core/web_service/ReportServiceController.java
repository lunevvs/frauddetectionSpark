package ru.spbstu.frauddetection.core.web_service;

import java.io.File;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportServiceController {
	private static ConstantName constN = new ConstantName();
	private static Logger log = Logger.getLogger(ConfigServiceController.class.getName());
	private static 	DataHelper helper = new DataHelper();

	@RequestMapping(value = "/get_status", method = RequestMethod.GET)

	public static String returnReportFile() throws Exception {
		helper.csvToJson();
		// "report.csv" -> "report.json"; "csvToJson" return JSON-File
		// "report.json"

		log.info("ReportService controller got");
		log.info("JSON-File with Report is returned");

		return DataHelper.jsonFromFileToString(new File(constN.pathToJsonReport));
	}
}
/**/