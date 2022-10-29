package io.techwings.kafka.kafkalearning.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import io.techwings.kafka.kafkalearning.KafkaLearningApplication;
import io.techwings.kafka.kafkalearning.dto.Party;

@Service
public class ProducerService {

    private static final Logger LOG = LoggerFactory.getLogger(ProducerService.class);

    @Qualifier("customTemplate")
    @Autowired
    private KafkaTemplate<String, Party> kafkaTemplate;

    public void sendMessage(Party data) {
        LOG.info("Sending data: ", data);
        kafkaTemplate.send(KafkaLearningApplication.TOPIC_NAME, data);
        LOG.info("Data Sent");

    }
}
