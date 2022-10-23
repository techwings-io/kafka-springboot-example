package io.techwings.kafka.kafkalearning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "io.techwings.kafka.kafkalearning")
public class KafkaLearningApplication {

	public static final String TOPIC_NAME = "first_topic";

	public static void main(String[] args) {
		SpringApplication.run(KafkaLearningApplication.class, args);
	}

}
