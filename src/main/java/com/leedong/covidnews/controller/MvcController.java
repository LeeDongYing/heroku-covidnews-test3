package com.leedong.covidnews.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.leedong.covidnews.model.News;
import com.leedong.covidnews.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.util.List;



@Controller
public class MvcController {

    @Autowired
    private NewsService newsService;

    @RequestMapping("/")
    public String news(Model model ,@RequestParam(required = false) String title) throws ParseException, JsonProcessingException {
        List<News> newsList = newsService.getNews(title);
        model.addAttribute("newsList",newsList);

        return "index";
    }

}
