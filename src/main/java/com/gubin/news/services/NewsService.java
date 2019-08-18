package com.gubin.news.services;

import com.gubin.news.domain.News;
import java.util.List;

public interface NewsService {
    void refresh(Integer siteId);
    void refreshAll();
    List<News> findByTitle(String query);
    void clearDatabase();
}
