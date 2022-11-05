package io.techwings.kafka.kafkalearning.services;

import java.io.IOException;
import java.util.Arrays;

import javax.annotation.PreDestroy;

import org.opensearch.action.bulk.BulkItemResponse;
import org.opensearch.action.bulk.BulkRequest;
import org.opensearch.action.bulk.BulkResponse;
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

import com.google.gson.JsonParser;

@Service
public class OpenSearchConsumer {

    Logger LOG = LoggerFactory.getLogger(OpenSearchConsumer.class);

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    private final BulkRequest bulkRequest = new BulkRequest();

    private final int BULK_SIZE = 50;

    @KafkaListener(topics = "wikimedia.recentchange", groupId = "opensearch-consumer-group")
    public void consumeOpenSearchMessages(String message) {
        // Extraction of the meta/id value from the wikimedia object is used to make the
        // entries into OpenSearch idempotent, assuming an "at-least-once" kafka
        // consumer's strategy
        String id = extractIdFromMessage(message);
        LOG.info("Consumed Open Search message {}", message);
        IndexRequest indexRequest = new IndexRequest(BootstrapService.OPEN_SEARCH_INDEX_NAME);
        indexRequest.source(message, XContentType.JSON).id(id);
        try {
            if (bulkRequest.numberOfActions() >= BULK_SIZE) {
                restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
                LOG.info("Bulk messages sent to OpenSearch");
            } else {
                bulkRequest.add(indexRequest);
            }

        } catch (IOException e) {
            LOG.error("Error while publishing message {}", message, e);
        }

    }

    public BulkRequest getBulkRequest() {
        return bulkRequest;
    }

    @PreDestroy
    public void flushLastBatchOfBulkRequests() {
        try {
            if (bulkRequest.numberOfActions() > 0) {
                BulkResponse response = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
                Arrays.stream(response.getItems()).forEach(br -> {
                    LOG.info("Sent to open search with id {}", br.getId());
                });

                LOG.info("Last batch of {} bulk messages sent to OpenSearch", bulkRequest.numberOfActions());
            }

        } catch (IOException e) {
            LOG.error("Error while sending the last batch", e);
        }

    }

    private String extractIdFromMessage(String message) {
        return JsonParser.parseString(message).getAsJsonObject().get("meta").getAsJsonObject().get("id").getAsString();
    }

}
