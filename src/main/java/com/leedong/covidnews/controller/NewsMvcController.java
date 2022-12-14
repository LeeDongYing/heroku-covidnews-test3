package com.leedong.covidnews.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.leedong.covidnews.model.Data;
import com.leedong.covidnews.model.News;
import com.leedong.covidnews.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class NewsMvcController {

    @Autowired
    private NewsService newsService;

    @GetMapping("/")
    public String homepage() {
        return "redirect:/index";
    }

    @GetMapping(value = {"/index", "/index.html"})
    public String showNews(Model model, @RequestParam(required = false) String search) throws ParseException, JsonProcessingException {
        model.addAttribute("boderStyle", "border: 1px  black solid");

        model.addAttribute("search", search);
        model.addAttribute("result", search);


        List<News> newsList = newsService.getNews(search);

        model.addAttribute("newsList", newsList);
        return "index";
    }

    @GetMapping("/news/edit/{newsId}")
    public String EditNewsPage(@PathVariable("newsId") Integer newsId, Model model) {
        News news = newsService.getNewsById(newsId);
        model.addAttribute("news", news);

        return "edit_news";
    }

    @PostMapping("/news/save")
    public String saveNews(News news) {
        newsService.editNews(news);

        return "redirect:/index.html";
    }

    @PostMapping("/news/create")
    public String create(Model model, News news, Data data) {


        model.addAttribute("news", new News());
        model.addAttribute("data", new Data());

        List<Data> dataList = new ArrayList<>();
        dataList.add(data);

        news.setDataList(dataList);
        newsService.createNews(news);

        return "redirect:/index.html";
    }

    @GetMapping("/updateLatestNews")
    public String updateLatestNews(RedirectAttributes ra) throws ParseException, JsonProcessingException {

        newsService.saveNews(requestNews());
        return "redirect:/index.html";
    }


    @GetMapping("/news/delete/{newsId}")
    public String deleteNews(@PathVariable("newsId") Integer newsId) {
        newsService.deleteById(newsId);
        return "redirect:/index.html";
    }


    private ResponseEntity<String> requestNews() {
        String url = "https://www.hpa.gov.tw/wf/newsapi.ashx";

        RestTemplate restTemplate = new RestTemplate();

        //header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity httpEntity = new HttpEntity(headers);

        //????????????
        //keyword??????????????????
        //startdate???????????????????????????
        //enddate???????????????????????????
        url += "?startdate=2022/07/25";

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                httpEntity,
                String.class
        );
        return response;
    }

}
