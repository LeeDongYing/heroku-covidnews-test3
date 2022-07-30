package com.leedong.covidnews.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leedong.covidnews.model.News;
import com.leedong.covidnews.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.util.List;

@RestController
public class NewsController {

    @Autowired
    private NewsService newsService;

    @GetMapping("/allnews")
    public ResponseEntity<String> allnews(){
        return requestNews();
    }


    @GetMapping("/news")
    @Validated
    public ResponseEntity<List<News>> getnews(@RequestParam(required = false) String title) throws JsonProcessingException, ParseException {
//
        List<News> newsList = newsService.getNews(title);
        return ResponseEntity.status(HttpStatus.OK).body(newsList);
//
    }

    @GetMapping("/savenews")
    public String savenews() throws ParseException, JsonProcessingException {
        ResponseEntity<String> response = requestNews();
        newsService.saveNews(response);
        return "saved";
    }

    private ResponseEntity<String> requestNews() {
        String url = "https://www.hpa.gov.tw/wf/newsapi.ashx?fbclid=IwAR11w4I_brMYrgl7iAummGlQV8hKxvdf3NWmUWlp0Cadyy2DHAnPaST6DxM";

        RestTemplate restTemplate = new RestTemplate();

        //使用方法和header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity httpEntity = new HttpEntity(headers);

        //搜尋條件
        //keyword：標題關鍵字
        //startdate：發布日期起始時間
        //enddate：發布日期結束時間
        url +="&startdate=2022/06/20";

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                httpEntity,
                String.class
        );
        return response;
    }
}
