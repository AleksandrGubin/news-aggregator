package com.gubin.news.reader.rss;

import com.gubin.news.domain.News;
import com.gubin.news.domain.Site;
import com.gubin.news.reader.NewsReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class RssNewsReader extends NewsReader {
    private static final Logger logger = LoggerFactory.getLogger(RssNewsReader.class);
    private RssNewsReader(Site site) {
        super(site);
    }

    public static NewsReader create(Site site) {
        return new RssNewsReader(site);
    }

    @Override
    public List<News> read(InputStream inputStream) {
        RssHandler handler = new RssHandler(site);
        parse(inputStream, handler);
        return handler.getMessages();
    }

    private void parse(InputStream inputStream, DefaultHandler handler) {
        try {
            createParser().parse(inputStream, handler);
        } catch (SAXException | IOException e) {
            logger.error(String.format("Exception when parse rss: %s", e.getMessage()));
            throw new RuntimeException(e);
        }
    }

    private SAXParser createParser() {
        SAXParser parser = null;
        SAXParserFactory spf = SAXParserFactory.newInstance();
        if (null != spf) {
            try {
                parser = spf.newSAXParser();
            } catch (ParserConfigurationException | SAXException e) {
                logger.error(String.format("Exception when configuring parser: %s", e.getMessage()));
                throw new RuntimeException(e);
            }
        }
        return parser;
    }
}
