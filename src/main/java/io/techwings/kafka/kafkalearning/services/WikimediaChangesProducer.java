package io.techwings.kafka.kafkalearning.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class WikimediaChangesProducer {

    @Qualifier("customTemplate")
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final Logger LOG = LoggerFactory.getLogger(WikimediaChangesProducer.class);

    String topic = "wikimedia.recentchange";

    public void sendMessage(String data) {
        LOG.info("Sending data to topic {}", topic);
        kafkaTemplate.send(this.topic, data);

    }
}
