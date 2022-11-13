package io.techwings.kafka.kafkalearning.services;

import java.util.Properties;

import javax.annotation.PreDestroy;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.techwings.kafka.kafkalearning.config.KafkaConfig;
import io.techwings.kafka.processors.BotCountStreamBuilder;
import io.techwings.kafka.processors.EventCountTimeseriesBuilder;
import io.techwings.kafka.processors.WebsiteCountStreamBuilder;

@Service
public class KafkaStream {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaStream.class);

    @Value("${app.topic.connect.name}")
    private String inputTopicName;

    @Autowired
    @Qualifier(KafkaConfig.KSTREAM_PROPERTIES_QUALIFIER)
    private Properties kStreamProperties;

    private KafkaStreams kafkaStreams;

    public void activateStreamPipeline() {
        StreamsBuilder builder = new StreamsBuilder();
        LOG.info("Topology invoked...");
        KStream<String, String> eventStream = builder.stream(inputTopicName);

        BotCountStreamBuilder botCountStreamBuilder = new BotCountStreamBuilder(eventStream);
        botCountStreamBuilder.setup();

        WebsiteCountStreamBuilder websiteCountStreamBuilder = new WebsiteCountStreamBuilder(eventStream);
        websiteCountStreamBuilder.setup();

        EventCountTimeseriesBuilder eventCountTimeseriesBuilder = new EventCountTimeseriesBuilder(eventStream);
        eventCountTimeseriesBuilder.setup();

        final Topology topology = builder.build();
        LOG.info("Topology: {} ", topology.describe());
        this.kafkaStreams = new KafkaStreams(topology, kStreamProperties);
        kafkaStreams.start();

    }

    @PreDestroy
    public void cleanUpOnShutdown() {
        LOG.info("Received Bean shutdown request. Cleaning up resources...");
        this.kafkaStreams.close();
        LOG.info("Stream closed.");
    }
}
