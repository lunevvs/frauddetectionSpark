package ru.spbstu.frauddetection.core.web_service;

import static org.junit.Assert.assertNotNull;

import org.apache.log4j.Logger;
import org.junit.Test;

public class TestDataHelper {
	private static Logger log = Logger.getLogger(ConfigServiceController.class.getName());

	@Test
	public void testParsingMethod() {

		String method = DataHelper.parsingMethodName("with KMeans for data...");
		assertNotNull(method);
		log.info("Test 1 of parsingMethod: " + method);
		method = DataHelper.parsingMethodName("");
		log.info("Test 2 of parsingMethod: " + method);

	}

	@Test
	public void parsingXMLName() {
		String name = DataHelper.parsingXMLName(
				"<Item><first_name>aaa</first_name> <middle_name>aaa</middle_name> <last_name>ccc</last_name></Item>");
		assertNotNull(name);
		log.info("Test 1 of parsingXMLName: " + name);
		name = DataHelper.parsingXMLName("");
		log.info("Test 2 of parsingXMLName: " + name);
	}
}
