package io.techwings.kafka.kafkalearning.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class WikimediaChangesProducer {

    @Qualifier("customTemplate")
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final Logger LOG = LoggerFactory.getLogger(WikimediaChangesProducer.class);

    @Value("${app.topic.name}")
    private String topic;

    public void sendMessage(String data) {
        LOG.info("Sending data to topic {}", topic);
        kafkaTemplate.send(this.topic, data);
        LOG.info("Wikimedia data sent to Kafka", data);

    }
}
