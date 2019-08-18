package com.gubin.news.validators;

import com.gubin.news.domain.Site;
import com.gubin.news.repositories.SiteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Locale;

@Service
public class AddSiteValidator implements Validator {
    private static final Logger logger = LoggerFactory.getLogger(AddSiteValidator.class);
    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private MessageSource messageSource;

    @Override
    public boolean supports(Class<?> clazz) {
        return Site.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Site site = (Site) target;
        if (siteRepository.existsSiteByTitleOrLink(site.getTitle(), site.getLink())) {
            logger.warn(String.format("Site: %s - already exists", site));
            errors.reject("NotUnique", getMessage("validation.not-unique-site"));
        }
    }

    private String getMessage(String code) {
        return messageSource.getMessage(code, new Object[0], Locale.getDefault());
    }
}
