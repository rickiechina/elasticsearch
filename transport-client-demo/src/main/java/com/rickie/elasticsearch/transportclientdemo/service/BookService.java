package com.rickie.elasticsearch.transportclientdemo.service;

import com.rickie.elasticsearch.transportclientdemo.domain.vo.BookVO;
import com.rickie.elasticsearch.transportclientdemo.domain.vo.BoolQueryVO;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class BookService {
    @Resource
    private TransportClient client;

    public String addBook(BookVO vo) {
        try {
            XContentBuilder content = XContentFactory.jsonBuilder().startObject()
                    .field("type", vo.getType())
                    .field("word_count", vo.getWordCount())
                    .field("author", vo.getAuthor())
                    .field("title", vo.getTitle())
                    .timeField("publish_date", vo.getPublishDate())
                    .endObject();
            // output
            System.out.println(Strings.toString(content));
            IndexResponse response = client.prepareIndex("book2", "_doc")
                    .setSource(content)
                    .get();
            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String findBookById(String id) {
        try {
            GetResponse response = client.prepareGet("book2", "_doc", id).get();
            // 通过 response.getSource()方法可以拿到该文档的字段信息
            Map<String, Object> resultMap = response.getSource();

            return resultMap.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String update(BookVO vo) {
        try {
            UpdateRequest request = new UpdateRequest();
            request.index("book2");
            request.type("_doc");
            request.id(vo.getId());

            XContentBuilder content = XContentFactory.jsonBuilder().startObject()
                    .field("type", vo.getType())
                    .field("title", vo.getTitle())
                    .field("author", vo.getAuthor())
                    .field("word_count", vo.getWordCount())
                    .field("publish_date", vo.getPublishDate())
                    .endObject();

            request.doc(content);
            UpdateResponse response = client.update(request).get();
            return response.toString();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String delete(String id) {
        try {
            DeleteResponse response = client.prepareDelete("book2", "_doc", id).get();

            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String boolQuery(BoolQueryVO vo){
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if(!StringUtils.isEmpty(vo.getAuthor())) {
            boolQuery.must(QueryBuilders.matchQuery("author", vo.getAuthor()));
        }
        if(!StringUtils.isEmpty(vo.getTitle())) {
            boolQuery.must(QueryBuilders.matchQuery("title", vo.getTitle()));
        }
        if(vo.getGtWordCount() != null && vo.getLtWordCount() != null) {
            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("word_count")
                    .from(vo.getGtWordCount())
                    .to(vo.getLtWordCount());
            boolQuery.filter(rangeQuery);
        }

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().query(boolQuery);

        try{
            SearchResponse response = client.prepareSearch("book2")
                    .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                    .setQuery(searchSourceBuilder.query())
                    .get();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
