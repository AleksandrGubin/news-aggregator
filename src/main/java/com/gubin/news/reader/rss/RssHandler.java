package com.gubin.news.reader.rss;

import com.gubin.news.domain.News;
import com.gubin.news.domain.Site;
import com.gubin.news.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;

class RssHandler extends DefaultHandler {
    private static final Logger logger = LoggerFactory.getLogger(RssHandler.class);

    private static final String GUID = "guid";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String LINK = "link";
    private static final String ITEM = "item";
    private static final String PUB_DATE = "pubDate";
    private final Site site;
    private final List<News> messages = new ArrayList<>();
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();


    private News message;
    private String currentElementName;

    public RssHandler(Site site) {
        this.site = site;
    }

    List<News> getMessages() {
        return messages;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (ITEM.equals(qName)) {
            message = new News();
            message.setSource(site);
        }

        if (null != message) {
            currentElementName = qName;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (ITEM.equals(qName) && null != message) {
            if (validate(message)) {
                messages.add(message);
            }
            message = null;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (null == currentElementName) {
            return;
        }

        String value = stripValue(ch, start, length);
        logger.debug(String.format("Parser: element = %s, value = %s", currentElementName, value));
        if (null == value) {
            return;
        }

        switch (currentElementName) {
            case GUID:
                message.setGuid(value);
                break;
            case TITLE:
                message.setTitle(value);
                break;
            case DESCRIPTION:
                message.setDescription(value);
                break;
            case LINK:
                message.setLink(value);
                break;
            case PUB_DATE:
                message.setPubDate(DateUtils.INSTANCE.parseRssDate(value));
                break;
        }
    }

    private String stripValue(char[] ch, int start, int length) {
        String rawValue = new String(ch, start, length);
        return null == rawValue ? null : StringUtils.trimToNull(rawValue.replaceAll("[\r\n]", StringUtils.EMPTY));
    }

    private boolean validate(News message) {
        return validator.validate(message).isEmpty();
    }
}
