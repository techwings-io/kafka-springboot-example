package io.techwings.kafka.kafkalearning.config;

import java.net.URI;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenSearchConfig {

    @Bean
    public RestHighLevelClient restHighLevelClient() {

        URI connectionUri = getConnectionUri();

        String userLoginInformation = connectionUri.getUserInfo();

        return userLoginInformation == null ? new RestHighLevelClient(
                RestClient.builder(new HttpHost(connectionUri.getHost(), connectionUri.getPort(), "http")))
                : prepareClientWhenSecurityIsApplied(connectionUri, userLoginInformation);

    }

    private URI getConnectionUri() {
        String connectionString = "http://localhost:9200";
        return URI.create(connectionString);
    }

    private RestHighLevelClient prepareClientWhenSecurityIsApplied(URI connectionUri, String userLoginInformation) {
        RestHighLevelClient restHighLevelClient;
        // REST client with security
        String[] authenticationInformation = userLoginInformation.split(":");

        CredentialsProvider credentialsProvider = getCredentialsProvider(authenticationInformation);

        restHighLevelClient = getRestHighLevelClientWithCallbacks(connectionUri, credentialsProvider);
        return restHighLevelClient;
    }

    private RestHighLevelClient getRestHighLevelClientWithCallbacks(URI connectionUri,
            CredentialsProvider credentialsProvider) {
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(
                RestClient
                        .builder(new HttpHost(connectionUri.getHost(), connectionUri.getPort(),
                                connectionUri.getScheme()))
                        .setHttpClientConfigCallback(
                                httpAsyncClientBuilder -> httpAsyncClientBuilder
                                        .setDefaultCredentialsProvider(credentialsProvider)
                                        .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())));
        return restHighLevelClient;
    }

    private CredentialsProvider getCredentialsProvider(String[] authenticationInformation) {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(authenticationInformation[0], authenticationInformation[1]));
        return credentialsProvider;
    }

}
