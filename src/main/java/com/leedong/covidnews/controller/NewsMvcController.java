package com.leedong.covidnews.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.leedong.covidnews.model.News;
import com.leedong.covidnews.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.util.List;

@Controller
public class NewsMvcController {

    @Autowired
    private NewsService newsService;

    @GetMapping("/news")
    public String showNews(Model model,@RequestParam(required = false) String search) throws ParseException, JsonProcessingException {
        List<News> newsList = newsService.getNews(search);
        model.addAttribute("newsList",newsList);
        return "news";
    }

    @GetMapping("/updatelatestnews")
    public String updateLatestNews() throws ParseException, JsonProcessingException {
        newsService.saveNews(requestNews());

        return "index";
    }



    @GetMapping("/news/delete/{title}")
    public String deleteNews(@PathVariable("title") String title){
        newsService.deleteByTitle(title);
        return "success";
    }



    private ResponseEntity<String> requestNews() {
        String url = "https://www.hpa.gov.tw/wf/newsapi.ashx";

        RestTemplate restTemplate = new RestTemplate();

        //使用方法和header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity httpEntity = new HttpEntity(headers);

        //搜尋條件
        //keyword：標題關鍵字
        //startdate：發布日期起始時間
        //enddate：發布日期結束時間
        url += "?startdate=2022/07/01";

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                httpEntity,
                String.class
        );
        return response;
    }

}
