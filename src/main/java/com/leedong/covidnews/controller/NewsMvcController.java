package com.leedong.covidnews.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.leedong.covidnews.model.News;
import com.leedong.covidnews.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.text.ParseException;
import java.util.List;

@Controller
public class NewsMvcController {

    @Autowired
    private NewsService newsService;

    @GetMapping("/news")
    public String showNews(Model model,String search) throws ParseException, JsonProcessingException {
        List<News> newsList = newsService.getNews(search);
        model.addAttribute("newsList",newsList);
        return "news";
    }

    @GetMapping("/news/delete/{title}")
    public String deleteNews(@PathVariable("title") String title){
        newsService.deleteByUrl(title);
        return "index";
    }


}
