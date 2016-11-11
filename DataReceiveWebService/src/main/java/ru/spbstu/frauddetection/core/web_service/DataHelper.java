package ru.spbstu.frauddetection.core.web_service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

public class DataHelper {
	private static ConstantName constN = new ConstantName();
	private static Logger log = Logger.getLogger(ConfigServiceController.class.getName());

	public Document stringToDocument(String xmlStr) {
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;

		try {
			docBuilder = builderFactory.newDocumentBuilder();
			return docBuilder.parse(new InputSource(new StringReader(xmlStr)));
		} catch (Exception e) {
			Logger.getLogger(e.getClass()).log(Level.ERROR, "Catch Exception!!!", e);
		}
		return null;
	}

	public String documentToString(Document doc) {
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transfObject;
		try {
			transfObject = tFactory.newTransformer();
			StringWriter writer = new StringWriter();
			transfObject.transform(new DOMSource(doc), new StreamResult(writer));

			return writer.getBuffer().toString();
		} catch (TransformerException e) {
			Logger.getLogger(e.getClass()).log(Level.ERROR, "Catch Exception!!!", e);
		}

		return null;
	}

	public void saveDocumentToFile(Document doc) {
		try {
			DOMSource source = new DOMSource(doc);
			StreamResult streamResult = new StreamResult(new File(constN.pathToConfigXML));
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();

			transformer.transform(source, streamResult);
		} catch (Exception e) {
			Logger.getLogger(e.getClass()).log(Level.ERROR, "Catch Exception!!!", e);
		}

	}

	public void csvToJson() throws Exception {
		File input = new File(constN.pathToCsvReport);
		// import "tmp/Reports/report.csv"
		File output = new File(constN.pathToJsonReport);
		// export "tmp/Reports/report.json"

		List<Map<?, ?>> data = readObjectsFromCsv(input);
		writeAsJson(data, output);

	}

	public static List<Map<?, ?>> readObjectsFromCsv(File file) throws IOException {
		CsvSchema bootstrap = CsvSchema.emptySchema().withHeader();
		CsvMapper csvMapper = new CsvMapper();
		@SuppressWarnings("deprecation")
		MappingIterator<Map<?, ?>> mappingIterator = csvMapper.reader(Map.class).with(bootstrap).readValues(file);

		return mappingIterator.readAll();
	}

	public static void writeAsJson(List<Map<?, ?>> data, File file) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(file, data);
	}

	public static String jsonFromFileToString(File jsonFile) throws FileNotFoundException {
		String str = "";
		Scanner input = new Scanner(jsonFile);
		while (input.hasNext())
			str += input.nextLine() + "\r\n";
		input.close();

		return str;
	}

	public static String parsingMethodName(String Message) throws NullPointerException {
		// Format of Message: with <method_name> for data <data> fraud detected
		try {
			int start = Message.indexOf("with") + 5;
			int end = Message.indexOf("for", start);
			char buf[] = new char[end - start];
			Message.getChars(start, end, buf, 0);
			String method_name = new String(buf);
			return method_name;

		} catch (NullPointerException e) {
			log.info("NullPointerException caught");
			return null;
		} catch (NegativeArraySizeException e) {
			log.info("NegativeArraySizeException caught. FraudMessage is incorrect");
			return null;
		}
	}

	public static String parsingXMLName(String xml) {
		// Format of XML: ... <first_name> ... <firs_name>...
		try {
			if (xml != "") {
				String result = "";
				int start = xml.indexOf("<first_name>") + 12;
				int end = xml.indexOf("</first_name>", start);
				result = writingName(result, xml, start, end);

				start = xml.indexOf("<middle_name>") + 13;
				end = xml.indexOf("</middle_name>", start);
				result = writingName(result, xml, start, end);

				start = xml.indexOf("<last_name>") + 11;
				end = xml.indexOf("</last_name>", start);
				result = writingName(result, xml, start, end);

				return result;
			} else {
				log.info("Name is absent");
				return null;
			}
		} catch (NullPointerException e) {
			log.info("NullPointerException caught");
			return null;
		} catch (NegativeArraySizeException e) {
			log.info("NegativeArraySizeException caught. XML is incorrect");
			return null;
		}

	}

	public static String writingName(String result, String xml, int start, int end) {
		if (start * end > 0) {
			char[] buf = new char[end - start];
			xml.getChars(start, end, buf, 0);
			result += new String(buf) + " ";
		}
		return result;
	}
}
