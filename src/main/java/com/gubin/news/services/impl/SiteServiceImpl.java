package com.gubin.news.services.impl;

import com.gubin.news.domain.Site;
import com.gubin.news.repositories.SiteRepository;
import com.gubin.news.services.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SiteServiceImpl implements SiteService {
    @Autowired
    private SiteRepository siteRepository;

    @Override
    public void addSite(Site site) {
        siteRepository.save(site);
    }

    @Override
    public Iterable<Site> findAll() {
        return siteRepository.findAll();
    }
}
