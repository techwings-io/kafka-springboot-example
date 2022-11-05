package io.techwings.kafka.kafkalearning.services;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.KafkaConsumer;
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

    public static final String OPEN_SEARCH_INDEX_NAME = "wikimedia";

    @Override
    public void run(String... args) throws Exception {
        LOG.info("Running");
        startWikipediaToKafkaStream();

        GetIndexRequest getIndexRequest = new GetIndexRequest(OPEN_SEARCH_INDEX_NAME);
        if (!restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT)) {
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(OPEN_SEARCH_INDEX_NAME);
            restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            LOG.info("Index {} created", OPEN_SEARCH_INDEX_NAME);
        } else {
            LOG.info("Index {} already exists", OPEN_SEARCH_INDEX_NAME);
        }

    }

    private void startWikipediaToKafkaStream() throws InterruptedException {
        String url = "https://stream.wikimedia.org/v2/stream/recentchange";
        EventHandler handler = this.eventHandler;
        EventSource.Builder builder = new EventSource.Builder(handler, URI.create(url));
        EventSource source = builder.build();
        LOG.info("Starting the Wikimedia Source...");
        source.start();

        TimeUnit.SECONDS.sleep(3);

        source.close();
    }

}
