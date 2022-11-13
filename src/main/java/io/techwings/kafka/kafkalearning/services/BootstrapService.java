package io.techwings.kafka.kafkalearning.services;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.TimeUnit;

import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.client.indices.CreateIndexRequest;
import org.opensearch.client.indices.GetIndexRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.EventSource;

@Service
public class BootstrapService implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(BootstrapService.class);

    @Autowired
    private EventHandler eventHandler;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private KafkaStream kafkaStream;

    public static final String OPEN_SEARCH_INDEX_NAME = "wikimedia";

    @Override
    public void run(String... args) throws Exception {

        LOG.info("Running...");

        // Uncomment the method below to read the stream from Wikimedia as opposed to
        // using
        // a connect-standalone Kafka connector. To use the standalone connector
        // you'd use a command like the following:
        // <code>
        // connect-standalone kafka-connectors/config/connect-standalone.properties
        // kafka-connectors/config/wikimedia.properties
        // </code>
        // Please note: the event payload structure from the Event Source below is
        // different from the one
        // returned by the standalone connector. Currently the KafkaStream.pipeline
        // method is coded to support
        // the standalone format

        // startWikipediaToKafkaStream();

        // You must have OpenSearch running in order to execute the method below. There
        // are Docker compose scripts on how to do this. One such example is in the
        // source
        // code for this project.

        // createOpenSearchIndex();

        kafkaStream.activateStreamPipeline();

    }

    private void createOpenSearchIndex() throws IOException {
        GetIndexRequest getIndexRequest = new GetIndexRequest(OPEN_SEARCH_INDEX_NAME);
        if (!restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT)) {
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(OPEN_SEARCH_INDEX_NAME);
            restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            LOG.info("Index {} created", OPEN_SEARCH_INDEX_NAME);
        } else {
            LOG.info("Index {} already exists", OPEN_SEARCH_INDEX_NAME);
        }
    }

    private void startWikimediaToKafkaStream() throws InterruptedException {
        String wikimediaConnectionUrl = "https://stream.wikimedia.org/v2/stream/recentchange";
        EventHandler eventHandler = this.eventHandler;
        EventSource.Builder eventSourceBuilder = new EventSource.Builder(eventHandler,
                URI.create(wikimediaConnectionUrl));
        EventSource eventSource = eventSourceBuilder.build();
        LOG.info("Starting the Wikimedia Source...");
        eventSource.start();

        TimeUnit.MINUTES.sleep(10);

        eventSource.close();
    }

}
