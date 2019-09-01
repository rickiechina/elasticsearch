package com.rickie.elasticsearch.transportclientdemo.domain.vo;

import lombok.Data;

@Data
public class BookVO {
    private String id;
    private String type;
    private Integer wordCount;
    private String author;
    private String title;
    private String publishDate;
}
