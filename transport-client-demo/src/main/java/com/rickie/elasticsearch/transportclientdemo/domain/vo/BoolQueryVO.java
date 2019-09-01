package com.rickie.elasticsearch.transportclientdemo.domain.vo;

import lombok.Data;

@Data
public class BoolQueryVO {
    private String author;
    private String title;
    private Integer gtWordCount;
    private Integer ltWordCount;
}
