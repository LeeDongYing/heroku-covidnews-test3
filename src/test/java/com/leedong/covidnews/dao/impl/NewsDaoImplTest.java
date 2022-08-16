package com.leedong.covidnews.dao.impl;

import com.leedong.covidnews.dao.NewsDao;
import com.leedong.covidnews.model.Data;
import com.leedong.covidnews.model.News;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class NewsDaoImplTest {

    @Autowired
    private NewsDao newsDao;

    @Test
    public void getNewsByIdTest(){
        News result = newsDao.getNewsById(1);

        assertNotNull(result);
        assertNotNull(result.getTitle());
        assertNotNull(result.getContent());
        assertNotNull(result.getConnectionUrl());
        assertNotNull(result.getCreatedDate());
        assertNotNull(result.getModifiedDate());
        assertNotNull(result.getStatus());
    }

    @Test
    public void createNewsTest(){
        News news = new News();
        news.setTitle("TitleTest");
        news.setContent("ContentTest");
        news.setConnectionUrl("https://test.com");

        Data data = new Data();
        data.setExplanation("【附件】");
        data.setName("File_19489.pdf");
        data.setConnection("https://www.hpa.gov.tw/Pages/ashx/File.ashx?FilePath=~/File/Attach/15942/File_19489.pdf");
        data.setConnectionUrl(news.getConnectionUrl());
        List<Data> dataList = new ArrayList<>();
        dataList.add(data);

        news.setDataList(dataList);

        newsDao.createNews(news);
        News result = newsDao.getNewsById(3);

        assertNotNull(result);
        assertNotNull(result.getTitle());
        assertEquals("TitleTest",result.getTitle());
        assertNotNull(result.getContent());
        assertEquals("ContentTest",result.getContent());
        assertNotNull(result.getConnectionUrl());
        assertEquals("https://test.com",result.getConnectionUrl());
        assertNotNull(result.getCreatedDate());
        assertNotNull(result.getModifiedDate());
        assertNotNull(result.getStatus());
        for(Data data1:result.getDataList()){
            assertNotNull(data1.getName());
            assertNotNull(data1.getExplanation());
            assertNotNull(data1.getConnection());

        }
//        assertNotNull(result.getDataList());



    }

    @Test
    public void editNewsTest(){
        News news = newsDao.getNewsById(1);
        news.setTitle("TitleTest");
        news.setContent("ContentTest");

        newsDao.editNews(news);
        News result = newsDao.getNewsById(1);

        assertNotNull(result);
        assertEquals("TitleTest",result.getTitle());
        assertEquals("ContentTest",result.getContent());

    }

    @Test
    public void deleteById(){
        newsDao.deleteById(1);

        assertEquals("0",newsDao.getNewsById(1).getStatus());
    }



}