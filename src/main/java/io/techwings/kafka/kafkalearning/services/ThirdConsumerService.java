package io.techwings.kafka.kafkalearning.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import io.techwings.kafka.kafkalearning.KafkaLearningApplication;
import io.techwings.kafka.kafkalearning.dto.Party;

@Service

public class ThirdConsumerService {

    private static final Logger LOG = LoggerFactory.getLogger(ThirdConsumerService.class);

    @KafkaListener(id = "ThirdConsumerService=KafkaLearning", topics = KafkaLearningApplication.TOPIC_NAME, groupId = "consumer-group-3")
    public void listen(Party val) {

        LOG.info("Consumer id: {} - Received: {}", "ThirdConsumerService=KafkaLearning", val);
    }

}
