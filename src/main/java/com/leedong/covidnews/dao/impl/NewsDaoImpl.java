package com.leedong.covidnews.dao.impl;

import com.leedong.covidnews.dao.NewsDao;
import com.leedong.covidnews.model.Data;
import com.leedong.covidnews.model.News;
import com.leedong.covidnews.rowmapper.DataRowMapper;
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
        String sql = "INSERT INTO news (title, content, connectUrl, created_date, modified_date,status)\n" +
                "VALUES (:title , :content ,:connectUrl  ,:createdDate ,:modifiedDate,:status)";

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
            mapSqlParameterSources[i].addValue("status","1");
        }
        namedParameterJdbcTemplate.batchUpdate(sql,mapSqlParameterSources);
    }

    @Override
    public News getNewsById(Integer newsId) {
        String sql = "SELECT newsId,title,content,connectUrl,created_date,modified_date,status \n" +
                "FROM news WHERE newsId =:newsId;";

        Map<String,Object> map =new HashMap<>();
        map.put("newsId",newsId);

        List<News> newsList = namedParameterJdbcTemplate.query(sql,map,new NewsRowMapper());

        if (newsList.size()>0){
            return newsList.get(0);
        }else {
            return null;
        }
    }

    @Override
    public void editNews(News news) {
        String sql = "UPDATE news SET title = :title ,content = :content WHERE newsId = :newsId;";

        Map<String,Object> map = new HashMap<>();
        map.put("newsId",news.getNewsId());
        map.put("title",news.getTitle());
        map.put("content",news.getContent());

        namedParameterJdbcTemplate.update(sql,map);
    }

    @Override
    public boolean getNewsByUrl(News news) {
        String sql = "SELECT title,content,connectUrl,created_date,modified_date,status FROM news WHERE connectUrl = :connectionUrl";

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
    public List<News> getNews(String search) {
        String sql = "SELECT newsId,title,content,connectUrl,created_date,modified_date,status FROM news WHERE 1=1";

        Map<String,Object> map = new HashMap<>();

        if(search != null){
            sql += " AND title LIKE :search OR content LIKE :search";
            map.put("search","%" + search + "%");
        }
        //排序
        sql = sql + " ORDER BY created_date desc";

        List<News> newsList = namedParameterJdbcTemplate.query(sql,map,new NewsRowMapper());

        if (newsList.size()>0){
            return newsList;
        }else {
            return null;
        }
    }

    @Override
    public List<Data> getDataByUrl(String connectionUrl) {
        String sql = "SELECT explanation,name,connection FROM data WHERE connectionUrl = :connecturl";

        Map<String,Object> map = new HashMap<>();
        map.put("connecturl",connectionUrl);

        List<Data> dataList = namedParameterJdbcTemplate.query(sql,map,new DataRowMapper());
        if(dataList != null)
            return dataList;
        else
            return null;
    }

    @Override
    public void deleteByTitle(Integer newsId){
        String sql ="UPDATE news SET status = '0' WHERE newsId = :newsId;";

        Map<String,Object> map = new HashMap<>();
        map.put("newsId",newsId);

        namedParameterJdbcTemplate.update(sql, map);

    }
}
