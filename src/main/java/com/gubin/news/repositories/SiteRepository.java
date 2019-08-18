package com.gubin.news.repositories;

import com.gubin.news.domain.Site;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteRepository extends CrudRepository<Site, Integer> {
    boolean existsSiteByTitleOrLink(String title, String link);
}
