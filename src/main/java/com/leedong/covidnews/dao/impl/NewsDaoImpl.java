package com.leedong.covidnews.dao.impl;

import com.leedong.covidnews.dao.NewsDao;
import com.leedong.covidnews.model.Data;
import com.leedong.covidnews.model.News;
import com.leedong.covidnews.rowmapper.NewsRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class NewsDaoImpl implements NewsDao {

    @Autowired(required = false)
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    @Override
    public void saveNews(List<News> newsList) {
        String sql = "INSERT INTO news (title, content, connectUrl, created_date, modified_date)\n" +
                "VALUES (:title , :content ,:connectUrl  ,:createdDate ,:modifiedDate)";

        String sqlDataList = "INSERT INTO data (explanation, name, connection, connectionUrl) \n" +
                "VALUES (:explanation ,:name , :connection ,:connectionUrl)";

        MapSqlParameterSource[] mapSqlParameterSources = new MapSqlParameterSource[newsList.size()];

        for (int i = 0 ;i < newsList.size() ; i++){
            News news = newsList.get(i);

            for (int j = 0; j<news.getDataList().size();j++) {
                Map<String, Object> map = new HashMap<>();
                Data data = news.getDataList().get(j);
                map.put("explanation", data.getExplanation());
                map.put("name", data.getName());
                map.put("connection", data.getConnection());
                map.put("connectionUrl", news.getConnectionUrl());

                namedParameterJdbcTemplate.update(sqlDataList,map);
            }

            mapSqlParameterSources[i] = new MapSqlParameterSource();
            mapSqlParameterSources[i].addValue("title",news.getTitle());
            mapSqlParameterSources[i].addValue("content",news.getContent());
            mapSqlParameterSources[i].addValue("connectUrl",news.getConnectionUrl());
            mapSqlParameterSources[i].addValue("createdDate",news.getCreatedDate());
            mapSqlParameterSources[i].addValue("modifiedDate",news.getModifiedDate());

        }
        namedParameterJdbcTemplate.batchUpdate(sql,mapSqlParameterSources);
    }

    @Override
    public boolean getNewsByUrl(News news) {
        String sql = "SELECT title,content,connectUrl,created_date,modified_date FROM news WHERE connectUrl = :connectionUrl";

        Map<String,Object> map = new HashMap<>();
        map.put("connectionUrl",news.getConnectionUrl());

        List<News> newsList = namedParameterJdbcTemplate.query(sql,map,new NewsRowMapper());
        if (newsList.size()>0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public List<News> getNews(String title) {
        String sql = "SELECT title,content,connectUrl,created_date,modified_date \n" +
                "FROM news WHERE 1=1 AND title LIKE :title";

        Map<String,Object> map = new HashMap<>();
        map.put("title","%" + title + "%");

        List<News> newsList = namedParameterJdbcTemplate.query(sql,map,new NewsRowMapper());

        if (newsList.size()>0){
            return newsList;
        }else {
            return null;
        }
    }
}
