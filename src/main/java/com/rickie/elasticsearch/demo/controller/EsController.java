package com.rickie.elasticsearch.demo.controller;

import com.rickie.elasticsearch.demo.dao.ArticleSearchRepository;
import com.rickie.elasticsearch.demo.model.Article;
import com.rickie.elasticsearch.demo.model.Author;
import com.rickie.elasticsearch.demo.model.Course;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Iterator;

@RestController
public class EsController {
    @Autowired
    private ArticleSearchRepository articleSearchRepository;

    @RequestMapping("/add")
    public void saveArticleIndex(){
        Author author = new Author();
        author.setId(1L);
        author.setName("Rickie Li");
        author.setRemark("Sr. Java Developer");

        Course course = new Course();
        course.setId(1L);
        course.setName("elastic stack v7.x");

        Article article = new Article();
        article.setId(1L);
        article.setTitle("install elasticsearch v7.3");
        article.setAbstracts("welcome to elastic stack v7.x");
        article.setCourse(course);
        article.setAuthor(author);
        article.setContent("content ...");
        article.setPostTime(new Date());
        article.setClickCount(100L);

        articleSearchRepository.save(article);
    }

    @RequestMapping("/query")
    public void queryArticle() {
        String queryString = "elasticsearch";
        QueryStringQueryBuilder builder = new QueryStringQueryBuilder(queryString);
        Iterable<Article> searchResult = articleSearchRepository.search(builder);
        Iterator<Article> iterator = searchResult.iterator();
        while(iterator.hasNext()) {
            System.out.println(iterator.next());
        }
        System.out.println("=== end === " + LocalDateTime.now().toString());
    }
}
