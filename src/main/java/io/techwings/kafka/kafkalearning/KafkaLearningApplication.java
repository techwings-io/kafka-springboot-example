package io.techwings.kafka.kafkalearning;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@ComponentScan(basePackages = "io.techwings.kafka.kafkalearning")
@EnableKafka
public class KafkaLearningApplication {

	private static final Logger LOG = LoggerFactory.getLogger(KafkaLearningApplication.class);

	public static final String TOPIC_NAME = "first_topic";

	public static void main(String[] args) {
		SpringApplication.run(KafkaLearningApplication.class, args);

	}

}
