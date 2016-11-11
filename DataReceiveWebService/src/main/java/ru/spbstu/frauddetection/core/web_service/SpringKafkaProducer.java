package ru.spbstu.frauddetection.core.web_service;

import com.google.common.io.Resources;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
public class SpringKafkaProducer {
	private final String topic = "xml_data";

	private Producer<String, String> producer;

	@PostConstruct
	public void initProducer() throws IOException {
		try (InputStream props = Resources.getResource("webservice.properties").openStream()) {
			Properties kafkaProperties = new Properties();
			kafkaProperties.load(props);
			producer = new KafkaProducer<>(kafkaProperties);
		}
	}

	public void send(String message) {
		ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, message);
		producer.send(record);

		System.out.println("SpringKafkaProducer sent:");
		System.out.println(message);
	}

}
