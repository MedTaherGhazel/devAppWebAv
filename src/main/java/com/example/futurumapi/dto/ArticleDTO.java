package com.example.futurumapi.dto;

import lombok.Data;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
public class ArticleDTO {
    private Long id;
    private String title;
    private String content;
    private String description;
    private Date pubDate;
    private String pdf;
    private List<String> tags;
    private String domainName;
    private Set<Long> contributorIds;
    private List<String> contributorNames;
}
