package ru.spbstu.frauddetection.core.web_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ServiceController {
    @Autowired
    SpringKafkaProducer springKafkaProducer;

    @RequestMapping(value = "/receive_input_xml", method = RequestMethod.POST)
    public ResponseEntity<String> postXML(@RequestBody String data) {

        System.out.println("Service controller got");
        System.out.println(data);

        springKafkaProducer.send(data);

        return new ResponseEntity<String>(HttpStatus.CREATED);
    }
}
