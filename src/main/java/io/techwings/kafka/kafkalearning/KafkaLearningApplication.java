package io.techwings.kafka.kafkalearning;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.EnableKafka;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.EventSource;

import io.techwings.kafka.kafkalearning.services.WikimediaEventHandler;

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
