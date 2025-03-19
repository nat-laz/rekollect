# Elasticsearch Setup Guide

## **Run Elasticsearch using Docker**

1. **Make sure Docker is installed** on your machine. If not, install it from:

   → [Docker Official Website](https://www.docker.com/get-started)


2. **Start Elasticsearch container** using the following command:

    ````shell
    docker run -d --name elasticsearch \
         -p 9200:9200 -p 9300:9300 \
         -e "discovery.type=single-node" \
         -e "xpack.security.enabled=false" \
         docker.elastic.co/elasticsearch/elasticsearch:8.15.0
    ````
3. Verify if Elasticsearch container is running:

   ```shell
   docker ps
   ```

4. Test connection:

   ```shell
   curl -X GET "http://localhost:9200/"
    
   ```

Expected output:

   ```shell
      {
      "name" : "elasticsearch",
      "cluster_name" : "docker-cluster",
      "version" : { "number" : "8.15.0", ... },
      "tagline" : "You Know, for Search"
      }
   ```

___

## **Create an Elasticsearch Index: ```Record```**

1. Checking existing index: ```curl -X GET "http://localhost:9200/_cat/indices?v"```. If index is not listed, create it
   manually.
2. Manually create the Index:

```shell
curl -X PUT "http://localhost:9200/records" -H "Content-Type: application/json" -d '{
  "settings": {
    "analysis": {
      "analyzer": {
        "lowercase_analyzer": {
          "type": "custom",
          "tokenizer": "standard",
          "filter": ["lowercase"]
        }
      }
    }
  },
  "mappings": {
    "properties": {
      "title": { "type": "text" },
      "description": { "type": "text" },
      "category": { "type": "text", "analyzer": "lowercase_analyzer" },
      "releaseDate": { "type": "date", "format": "yyyy-MM-dd" },
      "createdAt": { "type": "date", "format": "date_optional_time" },
      "updatedAt": { "type": "date", "format": "date_optional_time" },
      "tags": { "type": "text", "analyzer": "lowercase_analyzer" },
      "creators": {
        "type": "nested",
        "properties": {
          "firstName": { "type": "text" },
          "lastName": { "type": "text" },
          "role": { "type": "text" }
        }
      }
    }
  }
}'

```

3. Verify: ```curl -X GET "http://localhost:9200/records/_mapping?pretty"```
4. Delete and recreate the Index: ```curl -X DELETE "http://localhost:9200/records"```