package com.gubin.news.web.controllers;

import com.gubin.news.domain.News;
import com.gubin.news.domain.Site;
import com.gubin.news.forms.IntValueHolder;
import com.gubin.news.services.NewsService;
import com.gubin.news.services.SiteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class NewsController {
    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);

    @Autowired
    private SiteService siteService;

    @Autowired
    private NewsService newsService;

    @Autowired
    @Qualifier("addSiteValidator")
    private Validator addSiteValidator;

    @Value("${app.autoupdate-news.cron}")
    private String cronAutoupdate;

    @GetMapping("/")
    public String home(Model model) {
        logger.debug("Get start page: /");
        model.addAttribute("newSite", new Site());
        return "index";
    }

    @PostMapping("/refresh")
    public String refresh(@ModelAttribute("selectedSite") IntValueHolder selectedSite) {
        logger.debug(String.format("Post refresh [/refresh]: (selected site id: %d)", selectedSite.getValue()));
        newsService.refresh(selectedSite.getValue());
        return "redirect:news";
    }

    @PostMapping("/refreshAll")
    public String refreshAll() {
        logger.debug("Post refresh all [/refreshAll]");
        newsService.refreshAll();
        return "redirect:news";
    }

    @PostMapping("/clearDB")
    public String clearDB() {
        logger.debug("Post clear database [/clearDB]");
        newsService.clearDatabase();
        return "redirect:/";
    }

    @GetMapping("/news")
    public String filter(@RequestParam(name = "q", required = false) String query, Model model) {
        logger.debug(String.format("Get news [/news]: (query: %s)", query));
        model.addAttribute("sites", siteService.findAll());
        model.addAttribute("selectedSite", new IntValueHolder());
        model.addAttribute("q", query);
        List<News> news = newsService.findByTitle(query);
        model.addAttribute("news", news);
        model.addAttribute("newsCount", news.size());
        model.addAttribute("autoupdateCron", cronAutoupdate);
        return "news";
    }

    @Validated
    @PostMapping("/site/add")
    public String add(@ModelAttribute("newSite") @Valid Site site, BindingResult bindingResult) {
        logger.debug(String.format("Post add site [/site/add]: (site = %s)", site));
        addSiteValidator.validate(site, bindingResult);
        if (bindingResult.hasErrors()) {
            logger.warn(String.format("Add site errors: %s",
                    bindingResult.getAllErrors().stream().map(e -> e.getDefaultMessage()).collect(Collectors.joining("; "))));
            return "index";
        }
        siteService.addSite(site);
        return "redirect:/";
    }
}
