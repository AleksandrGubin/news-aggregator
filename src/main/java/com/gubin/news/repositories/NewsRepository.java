package com.gubin.news.repositories;

import com.gubin.news.domain.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {
    List<News> findByTitleContainingIgnoreCase(String title);

    @Query("select max(pubDate) from News where source.id = :site")
    Calendar findMaxPubDateBySource(@Param("site") Integer site);
}
