package com.leedong.covidnews.dao;

import com.leedong.covidnews.model.News;

import java.util.List;

public interface NewsDao {
    void saveNews(List<News> newsList);

    boolean getNewsByUrl(News news);

    List<News> getNews(String title);
}
