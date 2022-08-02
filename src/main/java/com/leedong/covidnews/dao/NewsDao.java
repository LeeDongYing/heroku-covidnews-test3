package com.leedong.covidnews.dao;

import com.leedong.covidnews.model.Data;
import com.leedong.covidnews.model.News;

import java.util.List;

public interface NewsDao {
    void saveNews(List<News> newsList);

    boolean getNewsByUrl(News news);

    List<News> getNews(String search);

    List<Data> getDataByUrl(String connectionUrl);

    News getNewsById(Integer newsId);

    void deleteByTitle(Integer newsId);

    void editNews(News news);
}
