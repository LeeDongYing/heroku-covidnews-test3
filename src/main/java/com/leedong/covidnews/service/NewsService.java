package com.leedong.covidnews.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.leedong.covidnews.model.News;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.List;

public interface NewsService {
    void saveNews(ResponseEntity<String> response) throws JsonProcessingException, ParseException;

    List<News> getNews(String search) throws JsonProcessingException, ParseException;
}
