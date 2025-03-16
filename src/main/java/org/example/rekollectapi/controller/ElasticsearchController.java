package org.example.rekollectapi.controller;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/elasticsearch")
public class ElasticsearchController {

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    @GetMapping("/test")
    public String testConnection() {
        try {
            boolean isAvailable = elasticsearchClient.ping().value();
            return isAvailable ? "Connected to Elasticsearch!" : "Elasticsearch is DOWN!";
        } catch (Exception e) {
            return "Elasticsearch is DOWN. Please start it before searching.";
        }
    }
}