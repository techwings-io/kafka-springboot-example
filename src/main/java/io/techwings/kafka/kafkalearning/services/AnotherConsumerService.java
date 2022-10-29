package io.techwings.kafka.kafkalearning.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import io.techwings.kafka.kafkalearning.KafkaLearningApplication;
import io.techwings.kafka.kafkalearning.dto.Party;

@Service

public class AnotherConsumerService {

    private static final Logger LOG = LoggerFactory.getLogger(AnotherConsumerService.class);

    @KafkaListener(id = "AnotherConsumerService=KafkaLearning", topics = KafkaLearningApplication.TOPIC_NAME, groupId = "consumer-group-1")
    public void listen(Party val) {

        LOG.info("Consumer id: {} - Received: {}", "AnotherConsumerService=KafkaLearning", val);
    }

}
