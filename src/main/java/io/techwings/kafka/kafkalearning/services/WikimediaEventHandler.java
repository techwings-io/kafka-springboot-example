package io.techwings.kafka.kafkalearning.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.MessageEvent;

@Service
public class WikimediaEventHandler implements EventHandler {

    private final Logger LOG = LoggerFactory.getLogger(WikimediaChangesProducer.class);

    @Autowired
    private WikimediaChangesProducer producerService;

    @Override
    public void onOpen() {
        LOG.info("Stream opened...");

    }

    @Override
    public void onClosed() {
        LOG.info("Stream closed...");
    }

    @Override
    public void onMessage(String event, MessageEvent messageEvent) throws Exception {
        producerService.sendMessage(messageEvent.getData());

    }

    @Override
    public void onComment(String comment) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void onError(Throwable t) {
        LOG.error("Error while reading from the stream {}", t);

    }

}
