package io.techwings.kafka.kafkalearning.services;

import java.net.URI;
import java.util.concurrent.TimeUnit;

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

    @Override
    public void run(String... args) throws Exception {
        LOG.info("Running");
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
