package io.techwings.kafka.kafkalearning;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import io.techwings.kafka.kafkalearning.controllers.KafkaProducerController;
import io.techwings.kafka.kafkalearning.dto.Party;

/**
 * Assumes a local Kafka instance running on localhost:9092 with an existing
 * topic of first_topic.
 * 
 * You can run the test by using Postman and providing the following JSON as
 * payload for a POST:
 * <code>
 * {
 * "firstName": "Marco",
 * "lastName": "Tedone"
 * }
 * </code>
 * 
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class KafkaLearningApplicationTestsIntegrationTest {

	private static final Logger LOG = LoggerFactory.getLogger(KafkaLearningApplicationTestsIntegrationTest.class);

	@Autowired
	private KafkaProducerController controller;

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
	}

	/**
	 * @throws Exception
	 */
	@Test
	void ensureSendMessageToKafkaWorks() throws Exception {
		Party party = new Party("Unit Test", "Spring Boot");
		final ResponseEntity<String> response = this.restTemplate.postForEntity("/api/v1/kafka/publish", controller,
				String.class, party);
		assertNotNull(response);
		LOG.info("Response status code is: {}", response.getStatusCode());
		LOG.info("Response body is: {}", response.getBody().toString());
	}

}
