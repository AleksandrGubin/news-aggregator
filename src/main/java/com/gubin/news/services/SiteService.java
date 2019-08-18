package com.gubin.news.services;

import com.gubin.news.domain.Site;

public interface SiteService {

    void addSite(Site site);

    Iterable<Site> findAll();
}
