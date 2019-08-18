package com.gubin.news.services.impl;

import com.gubin.news.domain.News;
import com.gubin.news.domain.Site;
import com.gubin.news.reader.rss.RssNewsReader;
import com.gubin.news.repositories.NewsRepository;
import com.gubin.news.repositories.SiteRepository;
import com.gubin.news.services.NewsService;
import com.gubin.news.utils.ConcurrencyUtils;
import com.gubin.news.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@EnableScheduling
public class NewsServiceImpl implements NewsService {
    private static final Logger logger = LoggerFactory.getLogger(NewsServiceImpl.class);
    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private SiteRepository siteRepository;

    @Value("${app.autoupdate-news.thread-count}")
    private Integer threadCount;

    @Override
    public void refresh(Integer siteId) {
        logger.debug(String.format("Refresh site with id: %d", siteId));
        if (null != siteId) {
            siteRepository.findById(siteId).ifPresent(this::refresh);
        }
    }

    @Override
    public List<News> findByTitle(String query) {
        logger.debug(String.format("Find news by query: [query = %s]", query));
        List<News> news = StringUtils.isEmpty(query) ? newsRepository.findAll()
                : newsRepository.findByTitleContainingIgnoreCase(query);
        Collections.sort(news);
        logger.debug(String.format("%d news found", news.size()));
        return news;
    }

    @Override
    public void clearDatabase() {
        newsRepository.deleteAll();
        siteRepository.deleteAll();
    }

    @Scheduled(cron = "${app.autoupdate-news.cron}")
    @Override
    public void refreshAll() {
        logger.debug("Refresh all sites");
        Collection<Site> sites = new ArrayList<>();
        siteRepository.findAll().forEach(sites::add);
        refresh(sites);
    }

    private void refresh(Collection<Site> sites) {
        logger.debug(String.format("Refresh sites: %s", sites.stream().map(Site::getTitle).collect(Collectors.joining(", "))));
        ConcurrencyUtils.INSTANCE.invokeAll(sites.stream().map(s -> ((Runnable) () -> refresh(s))).collect(Collectors.toList()), threadCount);
    }

    private void refresh(Site site) {
        logger.debug(String.format("Refresh site: %s", site));
        Calendar date = newsRepository.findMaxPubDateBySource(site.getId());
        logger.debug(String.format("Last news date: %s", DateUtils.INSTANCE.format(date)));
        List<News> newItems = RssNewsReader.create(site).read();
        logger.debug(String.format("%d news were downloaded", newItems.size()));
        if (null != date) {
            newItems.removeIf(n -> null == n.getPubDate() || !date.before(n.getPubDate()));
        }
        logger.debug(String.format("%d news will be added", newItems.size()));
        newsRepository.saveAll(newItems);
    }
}
