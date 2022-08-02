package com.leedong.covidnews.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leedong.covidnews.dao.NewsDao;
import com.leedong.covidnews.model.Data;
import com.leedong.covidnews.model.News;
import com.leedong.covidnews.service.NewsService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class NewsServiceImpl implements NewsService {
    @Autowired
    private NewsDao newsDao;

    @Override
    public List<News> getNews(String search) throws JsonProcessingException, ParseException {
        List<News> newsList =newsDao.getNews(search);
        List<News> nList = new ArrayList<>();
        for(News news : newsList){
            if(news.getStatus().equals("1")){
                nList.add(news);
            }else
                continue;
        }
        for (News news : nList) {
                List<Data> dataList = newsDao.getDataByUrl(news.getConnectionUrl());
                news.setDataList(dataList);
            }
        return nList;
    }

    @Override
    public News getNewsById(Integer newsId) {
        return newsDao.getNewsById(newsId);
    }

    @Override
    public void saveNews(ResponseEntity<String> response) throws JsonProcessingException, ParseException {
        List<News> newsList =  transfer(response);
        List<News> nList = new ArrayList<>();
        for (int i = 0;i<newsList.size();i++){
            News news = newsList.get(i);
            if (newsDao.getNewsByUrl(news) != true) {
                nList.add(newsList.get(i));
            }else{
                continue;
            }
        }
        if(nList != null){
            newsDao.saveNews(nList);
        } else {
            return;
        }

    }

    @Override
    public void editNews(News news) {
        newsDao.editNews(news);
    }

    @Override
    public void deleteByTitle(Integer newsId) {

        newsDao.deleteByTitle(newsId);
    }

    private List<News> transfer(ResponseEntity<String> response) throws JsonProcessingException, ParseException {
        String result = response.getBody().toString();

        List<News> newsList = new ArrayList<>();

//      把object轉乘String
//      String json = new String();

        JSONArray jsonArray = new JSONArray(result);
        for (int i = 0; i < jsonArray.length(); i++) {
            News news = new News();

            JSONObject jsonObject = jsonArray.getJSONObject(i);

            news.setTitle(jsonObject.getString("標題"));
            news.setContent(jsonObject.getString("內容"));

//            String[] jArray = objectMapper.readValue(jsonObject.getJSONArray("附加檔案").toString(),String[].class);
//            news.setDataList(jArray);
            JSONArray jArray = jsonObject.getJSONArray("附加檔案");
            List<Data> dataList = new ArrayList<>();
            for (int j = 0 ; j< jArray.length() ;j++){
                JSONObject jObject = jArray.getJSONObject(j);
                Data data = new Data();
                data.setExplanation(jObject.getString("檔案說明"));
                data.setName(jObject.getString("檔案名稱"));
                data.setConnection(jObject.getString("連結位置"));
                dataList.add(data);
            }
            news.setDataList(dataList);

            news.setConnectionUrl(jsonObject.getString("連結網址"));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            Date cDate = sdf.parse(jsonObject.getString("發布日期"));
            news.setCreatedDate(cDate);

            Date mDate = sdf.parse(jsonObject.getString("修改日期"));
            news.setModifiedDate(mDate);

            newsList.add(news);

//            json += objectMapper.writeValueAsString(news);
        }
        return newsList;
    }
}
