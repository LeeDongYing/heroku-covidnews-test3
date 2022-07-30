package com.leedong.covidnews.rowmapper;

import com.leedong.covidnews.model.News;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NewsRowMapper implements RowMapper<News> {
    @Override
    public News mapRow(ResultSet rs, int rowNum) throws SQLException {
        News news = new News();

        news.setTitle(rs.getString("title"));
        news.setContent(rs.getString("content"));
        news.setConnectionUrl(rs.getString("connectUrl"));


        news.setCreatedDate(rs.getTimestamp("created_date"));
        news.setModifiedDate(rs.getTimestamp("modified_date"));

        return news;
    }
}
