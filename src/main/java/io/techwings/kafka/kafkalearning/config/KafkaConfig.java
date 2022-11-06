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

@Configuration
public class KafkaConfig {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaConfig.class);

    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapServers;
    @Value("${spring.kafka.producer.key-serializer}")
    private String producerKeySerializerClass;
    @Value("${spring.kafka.producer.value-serializer}")
    private String producerValueSerializerClass;
    @Value("${app.topic.name}")
    private String topic;

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, producerKeySerializerClass);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, producerValueSerializerClass);
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        props.put(ProducerConfig.LINGER_MS_CONFIG, 20);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(32 * 1024));

        return props;
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean(name = "customTemplate")
    public KafkaTemplate<String, String> kafkaTemplate() {
        KafkaTemplate<String, String> template = new KafkaTemplate<>(producerFactory());
        template.setProducerListener(new ProducerListener<String, String>() {
            @Override
            public void onSuccess(
                    ProducerRecord<String, String> producerRecord,
                    RecordMetadata recordMetadata) {

                LOG.info("ACK from ProducerListener. Message: {}", producerRecord.value());
                LOG.info("Topic: {}", recordMetadata.topic());
                LOG.info("Key: {}", producerRecord.key());
                LOG.info("Partition: {}", recordMetadata.partition());
                LOG.info("Offset: {}", recordMetadata.offset());
                LOG.info("Timestamp: {}", recordMetadata.timestamp());

            }

            public void onError(ProducerRecord<String, String> producerRecord, RecordMetadata recordMetadata,
                    Exception exception) {
                LOG.error("Error occurred while sending data {} to topic {}. Exception: {}", producerRecord,
                        recordMetadata.topic(), exception);
            }
        });
        return template;
    }

}
