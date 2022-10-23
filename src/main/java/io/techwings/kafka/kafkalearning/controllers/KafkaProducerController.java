package io.techwings.kafka.kafkalearning.controllers;

import java.net.URI;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaProducerException;
import org.springframework.kafka.core.KafkaSendCallback;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.techwings.kafka.kafkalearning.dto.Party;
import io.techwings.kafka.kafkalearning.services.ProducerService;

@RestController
@RequestMapping("/api/v1/kafka")
public class KafkaProducerController {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaProducerController.class);

    private ProducerService kafkaProducer;

    public KafkaProducerController(ProducerService kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping("/publish")
    public ResponseEntity<String> publish(@RequestBody Party party) {
        kafkaProducer.sendMessage(party);
        return ResponseEntity.ok("Message sent to /kafka/topic/first_topic");
    }
}
