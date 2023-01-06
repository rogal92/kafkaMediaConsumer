package org.example;

import org.example.opensearch.OpenSearchService;

public class Main {
    public static void main(String[] args) {
        OpenSearchService openSearchService = new OpenSearchService();
        openSearchService.createIndex();
    }
}