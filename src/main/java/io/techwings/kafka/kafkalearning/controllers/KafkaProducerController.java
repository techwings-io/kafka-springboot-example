package io.techwings.kafka.kafkalearning.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.techwings.kafka.kafkalearning.dto.Party;
import io.techwings.kafka.kafkalearning.services.ProducerService;

@RestController
@RequestMapping("/api/v1/kafka")
public class KafkaProducerController {

    private ProducerService kafkaProducer;

    public KafkaProducerController(ProducerService kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping("/publish")
    public ResponseEntity<String> publish(@RequestBody Party party) {
        kafkaProducer.sendMessage(party);
        return ResponseEntity.ok("Message sent to kafka topic");
    }
}
