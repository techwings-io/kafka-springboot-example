package io.techwings.kafka.kafkalearning.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.lang.Nullable;

@Configuration
@EnableKafka
public class KafkaConfig {

    private static final int THIRTY_TWO_MB = 32 * 1024;

    private static final int PRODUCER_CONFIG_LINGER_IN_MILLISECONDS = 20;

    private static final Logger LOG = LoggerFactory.getLogger(KafkaConfig.class);

    public static final String KSTREAM_PROPERTIES_QUALIFIER = "kStreamProperties";

    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapServers;
    @Value("${spring.kafka.producer.key-serializer}")
    private String producerKeySerializerClass;
    @Value("${spring.kafka.producer.value-serializer}")
    private String producerValueSerializerClass;
    @Value("${app.topic.name}")
    private String topicName;

    @Bean
    public Map<String, Object> producerConfiguration() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, producerKeySerializerClass);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, producerValueSerializerClass);
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        props.put(ProducerConfig.LINGER_MS_CONFIG, PRODUCER_CONFIG_LINGER_IN_MILLISECONDS);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(THIRTY_TWO_MB));

        return props;
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfiguration());
    }

    @Bean(name = "customTemplate")
    public KafkaTemplate<String, String> kafkaTemplate() {
        KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(producerFactory());
        kafkaTemplate.setProducerListener(new ProducerListener<String, String>() {
            @Override
            public void onSuccess(
                    ProducerRecord<String, String> producerRecord,
                    RecordMetadata recordMetadata) {

                logEventData(producerRecord, recordMetadata);

            }

            public void onError(ProducerRecord<String, String> producerRecord, @Nullable RecordMetadata recordMetadata,
                    Exception exception) {
                LOG.error("Error occurred while sending data {} to topic {}. Exception: {}", producerRecord,
                        recordMetadata != null ? recordMetadata.topic() : "", exception);
            }

            private void logEventData(ProducerRecord<String, String> producerRecord, RecordMetadata recordMetadata) {
                LOG.info("ACK from ProducerListener. Message: {}", producerRecord.value());
                LOG.info("Topic: {}", recordMetadata.topic());
                LOG.info("Key: {}", producerRecord.key());
                LOG.info("Partition: {}", recordMetadata.partition());
                LOG.info("Offset: {}", recordMetadata.offset());
                LOG.info("Timestamp: {}", recordMetadata.timestamp());
            }
        });
        return kafkaTemplate;
    }

    @Bean(name = KSTREAM_PROPERTIES_QUALIFIER)
    public Properties kStreamProperties() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        return props;
    }

}
