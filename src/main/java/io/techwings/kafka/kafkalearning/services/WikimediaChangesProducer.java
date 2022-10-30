package io.techwings.kafka.kafkalearning.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import io.techwings.kafka.kafkalearning.dto.Party;

@Service
public class WikimediaChangesProducer {

    @Qualifier("customTemplate")
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    String topic = "wikimedia.recentchange";

    public void sendMessage(Party data) {

    }
}
