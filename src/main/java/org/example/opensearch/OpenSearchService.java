package org.example.opensearch;

import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.client.indices.CreateIndexRequest;
import org.opensearch.client.indices.GetIndexRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class OpenSearchService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenSearchService.class.getName());
    private final OpenSearchClient openSearchClient;

    public OpenSearchService() {
        this.openSearchClient = new OpenSearchClient();
    }

    public void createIndex() {
        RestHighLevelClient restHighLevelClient = openSearchClient.getRestHighLevelClient();

        try(restHighLevelClient) {

            boolean wikimediaExists = restHighLevelClient
                                              .indices()
                                              .exists(new GetIndexRequest("wikimedia"),
                                                      RequestOptions.DEFAULT);
            if (!wikimediaExists) {
                CreateIndexRequest createIndexRequest = new CreateIndexRequest("wikimedia");
                restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            } else {
                LOGGER.info("Wikimendia Index already exists");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
