package io.techwings.kafka.kafkalearning.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.MessageEvent;

@Service
public class WikimediaEventHandler implements EventHandler {

    @Autowired
    private WikimediaChangesProducer producerService;

    @Override
    public void onOpen() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void onClosed() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMessage(String event, MessageEvent messageEvent) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void onComment(String comment) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void onError(Throwable t) {
        // TODO Auto-generated method stub

    }

}
