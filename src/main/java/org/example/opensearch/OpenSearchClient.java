package org.example.opensearch;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestClientBuilder;
import org.opensearch.client.RestHighLevelClient;

import java.net.URI;
import java.util.Optional;
import java.util.function.Supplier;

public class OpenSearchClient {

    public RestHighLevelClient getRestHighLevelClient() {
        String connection = "http://localhost:9200";
        URI uri = URI.create(connection);
        Optional<String> userInfo = Optional.ofNullable(uri.getUserInfo());

        return userInfo.map(userInformation -> getRestHighLevelClient(uri, userInformation))
                       .orElseGet(getDefaultRestHighLevelClientSupplier(uri));
    }

    private RestHighLevelClient getRestHighLevelClient(final URI uri, final String userInformation) {
        String[] auth = userInformation.split(":");
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(auth[0], auth[1]));
        HttpHost httpHost = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
        RestClientBuilder clientBuilder = RestClient.builder(httpHost).setHttpClientConfigCallback(
                httpAsyncClientBuilder -> httpAsyncClientBuilder
                                                  .setDefaultCredentialsProvider(credentialsProvider)
                                                  .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy()));
        return new RestHighLevelClient(clientBuilder);
    }

    private Supplier<RestHighLevelClient> getDefaultRestHighLevelClientSupplier(final URI uri) {
        HttpHost httpHost = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
        return () -> new RestHighLevelClient(RestClient.builder(httpHost));
    }
}
