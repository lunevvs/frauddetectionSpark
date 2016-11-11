package ru.spbstu.frauddetection.core.web_service;

import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;
import java.io.File;

public class ReportGenerator {
	private static String csvFileHeader = "Name/method/OK or KO" + System.lineSeparator();
	private static String separator = "/";
	private static Logger log = Logger.getLogger(ConfigServiceController.class.getName());
	private static ConstantName constN = new ConstantName();

	private void writing(FileWriter writer, String xml_name, String method_name, Boolean status) throws IOException {
		String statusStr;
		if (status) {
			statusStr = "OK";
		} else {
			statusStr = "KO";
		}
		writer.write(xml_name + separator + method_name + separator + statusStr + System.lineSeparator());
	}

	public void generate(String xml, String fraudMessage, Boolean check) throws Exception {
		// Class Method method; -> method_name = method.valueOf(name);
		String pathToCsvFile = DirectoryHelper.getPathTo(constN.reportsName) + separator + constN.reportFileName
				+ ".csv";

		String method_name = DataHelper.parsingMethodName(fraudMessage);
		String xml_name = DataHelper.parsingXMLName(xml);
		File f = new File(pathToCsvFile);
		if (!f.exists()) {
			FileWriter writer = new FileWriter(pathToCsvFile, true);
			writer.write(csvFileHeader);
			writing(writer, xml_name, method_name, check);
			writer.close();

		} else {
			FileWriter writer = new FileWriter(pathToCsvFile, true);
			writing(writer, xml_name, method_name, check);
			writer.close();
		}

		log.info("Report is generated.");
	}
}
