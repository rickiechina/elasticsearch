package com.rickie.elasticsearch.restclientdemo.service;

import com.rickie.elasticsearch.restclientdemo.domain.vo.BookVO;
import com.rickie.elasticsearch.restclientdemo.domain.vo.BoolQueryVO;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

@Service
public class BookService {
    @Resource
    private RestHighLevelClient client;

    public String addBook(BookVO vo) {
        try {
            XContentBuilder content = XContentFactory.jsonBuilder().startObject()
                    .field("type", vo.getType())
                    .field("word_count", vo.getWordCount())
                    .field("author", vo.getAuthor())
                    .field("title", vo.getTitle())
                    .timeField("publish_date", vo.getPublishDate())
                    .endObject();
            IndexRequest request = new IndexRequest("book").source(content);
            IndexResponse response = client.index(request, RequestOptions.DEFAULT);
            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String findBookById(String id) {
        GetRequest request = new GetRequest("book", id);

        try {
            GetResponse response = client.get(request, RequestOptions.DEFAULT);
            // 通过 response.getSource()方法可以拿到该文档的字段信息
            Map<String, Object> resultMap = response.getSource();

            return resultMap.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String findBookByIdAsync(String id) {
        GetRequest request = new GetRequest("book", id);

        // 异步执行 - 异步方法不会阻塞并立即返回
        ActionListener<GetResponse> listener = new ActionListener<GetResponse>() {
            @Override
            public void onResponse(GetResponse getResponse) {
                // 执行成功时调用，Response 以参数方式提供。
                if (getResponse.isExists()) {
                    System.out.println(getResponse.getSourceAsString());
                } else {
                    System.out.println(getResponse.getSourceAsString());
                    System.out.println("isExists方法返回false");
                }
            }

            @Override
            public void onFailure(Exception e) {
                // 失败的情况下调用，引发的异常以参数方式提供。
                System.out.println(e.toString());
            }
        };

        // 异步执行获取索引请求
        client.getAsync(request, RequestOptions.DEFAULT, listener);

        return null;
    }


    public String update(BookVO vo) {
        try {
            UpdateRequest request = new UpdateRequest("book", vo.getId());
            XContentBuilder content = XContentFactory.jsonBuilder().startObject()
                    .field("type", vo.getType())
                    .field("word_count", vo.getWordCount())
                    .field("author", vo.getAuthor())
                    .field("title", vo.getTitle())
                    .timeField("publish_date", vo.getPublishDate())
                    .endObject();

            request.doc(content);
            UpdateResponse response = client.update(request, RequestOptions.DEFAULT);

            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String delete(String id) {
        try {
            DeleteRequest request = new DeleteRequest("book").id(id);
            DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
            return response.toString();
        } catch (IOException e) {
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
            boolQuery.must(QueryBuilders.termQuery("title", vo.getTitle()));
        }
        if(vo.getGtWordCount() != null && vo.getLtWordCount() != null) {
            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("word_count")
                    .from(vo.getGtWordCount())
                    .to(vo.getLtWordCount());
            boolQuery.filter(rangeQuery);
        }

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().query(boolQuery);
        SearchRequest searchRequest = new SearchRequest("book").source(searchSourceBuilder);

        String result = "";
        try{
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
            // 检索SearchHits
            SearchHits hits = response.getHits();
            String lineSeparator = System.getProperty("line.separator", "\n");
            for (SearchHit hit : hits) {
                // do something with the SearchHit
                result += hit.getSourceAsString() + lineSeparator;
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
