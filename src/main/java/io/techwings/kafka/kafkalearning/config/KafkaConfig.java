package io.techwings.kafka.kafkalearning.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.ProducerListener;

import io.techwings.kafka.kafkalearning.dto.Party;

@Configuration
public class KafkaConfig {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaConfig.class);

    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapServers;
    @Value("${spring.kafka.producer.key-serializer}")
    private String producerKeySerializerClass;
    @Value("${spring.kafka.producer.value-serializer}")
    private String producerValueSerializerClass;

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, producerKeySerializerClass);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, producerValueSerializerClass);
        return props;
    }

    @Bean
    public ProducerFactory<String, Party> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean(name = "customTemplate")
    public KafkaTemplate<String, Party> kafkaTemplate() {
        KafkaTemplate<String, Party> template = new KafkaTemplate<>(producerFactory());
        template.setProducerListener(new ProducerListener<String, Party>() {
            @Override
            public void onSuccess(
                    ProducerRecord<String, Party> producerRecord,
                    RecordMetadata recordMetadata) {

                LOG.info("ACK from ProducerListener. Message: {}", producerRecord.value());
                LOG.info("Topic: {}", recordMetadata.topic());
                LOG.info("Partition: {}", recordMetadata.partition());
                LOG.info("Offset: {}", recordMetadata.offset());
                LOG.info("Timestamp: {}", recordMetadata.timestamp());

            }
        });
        return template;
    }
}
