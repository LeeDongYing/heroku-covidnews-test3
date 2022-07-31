package com.leedong.covidnews.rowmapper;

import com.leedong.covidnews.model.Data;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DataRowMapper implements RowMapper<Data> {
    @Override
    public Data mapRow(ResultSet rs, int rowNum) throws SQLException {
        Data data = new Data();
        data.setExplanation(rs.getString("explanation"));
        data.setName(rs.getString("name"));
        data.setConnection(rs.getString("connection"));
        return data;
    }
}
