package ru.spbstu.frauddetection.core.web_service;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigServiceController {
	private static Logger log = Logger.getLogger(ConfigServiceController.class.getName());

	@RequestMapping(value = "/receive_config_xml", method = RequestMethod.POST)

	public ResponseEntity<String> configXML(@RequestBody String data) throws Exception {
		DataHelper helper = new DataHelper();
		helper.saveDocumentToFile(helper.stringToDocument(data));
    
		log.info("ConfigService controller got");
		log.info(data);

		return new ResponseEntity<String>(HttpStatus.CREATED);
	}
}
/**/
