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
        String connectionString = "http://localhost:9200";

        RestHighLevelClient restHighLevelClient;
        URI connectionUri = URI.create(connectionString);

        String userLoginInformation = connectionUri.getUserInfo();

        if (userLoginInformation == null) {
            // REST client without security
            restHighLevelClient = new RestHighLevelClient(
                    RestClient.builder(new HttpHost(connectionUri.getHost(), connectionUri.getPort(), "http")));

        } else {
            // REST client with security
            String[] authenticationInformation = userLoginInformation.split(":");

            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY,
                    new UsernamePasswordCredentials(authenticationInformation[0], authenticationInformation[1]));

            restHighLevelClient = new RestHighLevelClient(
                    RestClient
                            .builder(new HttpHost(connectionUri.getHost(), connectionUri.getPort(),
                                    connectionUri.getScheme()))
                            .setHttpClientConfigCallback(
                                    httpAsyncClientBuilder -> httpAsyncClientBuilder
                                            .setDefaultCredentialsProvider(credentialsProvider)
                                            .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())));

        }

        return restHighLevelClient;

    }

}
