package io.techwings.kafka.kafkalearning.services;

import java.io.IOException;

import org.opensearch.action.index.IndexRequest;
import org.opensearch.action.index.IndexResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OpenSearchConsumer {

    Logger LOG = LoggerFactory.getLogger(OpenSearchConsumer.class);

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @KafkaListener(topics = "wikimedia.recentchange", groupId = "opensearch-consumer-group")
    public void consumeOpenSearchMessages(String message) {
        LOG.info("Consumed Open Search message {}", message);
        IndexRequest indexRequest = new IndexRequest(BootstrapService.OPEN_SEARCH_INDEX_NAME);
        indexRequest.source(message, XContentType.JSON);
        try {
            IndexResponse response = restHighLevelClient.index(
                    indexRequest, RequestOptions.DEFAULT);
            LOG.info("Message id {} sent to Open Search index {}", response.getId(),
                    BootstrapService.OPEN_SEARCH_INDEX_NAME);
        } catch (IOException e) {
            LOG.error("Error while publishing message {}", message, e);
        }

    }

}
