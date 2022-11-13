package io.techwings.kafka.processors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class BotCountStreamBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(BotCountStreamBuilder.class);

    private static final String BOT_COUNT_STORE = "bot-count-store";
    private static final String BOT_COUNT_TOPIC = "wikimedia_stats_bots";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final KStream<String, String> inputStream;

    public BotCountStreamBuilder(KStream<String, String> inputStream) {
        this.inputStream = inputStream;
    }

    public void setup() {
        this.inputStream
                .mapValues(changeJson -> {
                    try {
                        return returnBotNonBot(changeJson);
                    } catch (IOException e) {
                        return "parse-error";
                    }
                })
                .groupBy((key, botOrNot) -> botOrNot)
                .count(Materialized.as(BOT_COUNT_STORE))
                .toStream()
                .mapValues((key, value) -> {
                    try {
                        return mapValues(key, value);
                    } catch (JsonProcessingException e) {
                        return "";
                    }
                })
                .to(BOT_COUNT_TOPIC);
    }

    private String mapValues(String key, Long value) throws JsonProcessingException {
        final Map<String, Long> kvMap = Map.of(String.valueOf(key), value);
        return OBJECT_MAPPER.writeValueAsString(kvMap);
    }

    private String returnBotNonBot(String changeJson) throws JsonProcessingException, JsonMappingException {
        JsonNode payloadNode = getPayloadNode(changeJson);
        if (payloadNode == null || !payloadNode.get("bot").asBoolean())
            return "non-bot";
        return "bot";
    }

    private JsonNode getPayloadNode(String changeJson) throws JsonProcessingException, JsonMappingException {
        final JsonNode jsonNode = OBJECT_MAPPER.readTree(changeJson);
        return OBJECT_MAPPER.readTree(jsonNode.get("payload").asText());
    }
}
