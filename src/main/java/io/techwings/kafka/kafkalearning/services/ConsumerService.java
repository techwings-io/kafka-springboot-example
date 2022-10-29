package io.techwings.kafka.kafkalearning.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import io.techwings.kafka.kafkalearning.KafkaLearningApplication;
import io.techwings.kafka.kafkalearning.dto.Party;

@Service

public class ConsumerService {

    private static final Logger LOG = LoggerFactory.getLogger(ConsumerService.class);

    @KafkaListener(id = "ConsumerService-KafkaLearning", topics = KafkaLearningApplication.TOPIC_NAME, groupId = "consumer-group-1")
    public void listen(Party val) {

        LOG.info("Consumer id: {} - Received: {}", "ConsumerService-KafkaLearning", val);
    }

}
