package com.rickie.elasticsearch.demo.dao;

import com.rickie.elasticsearch.demo.model.Article;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ArticleSearchRepository extends ElasticsearchRepository<Article, Long> {
}
