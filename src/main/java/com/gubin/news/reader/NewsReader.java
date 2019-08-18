package com.gubin.news.reader;

import com.gubin.news.domain.News;
import com.gubin.news.domain.Site;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public abstract class NewsReader {
    private static final Logger logger = LoggerFactory.getLogger(NewsReader.class);
    protected final Site site;

    protected NewsReader(Site site) {
        this.site = site;
    }

    public List<News> read() {
        InputStream inputStream = null;
        try {
            inputStream = openStream();
            return read(inputStream);
        } finally {
            closeStream(inputStream);
        }
    }

    protected abstract List<News> read(InputStream inputStream);

    private InputStream openStream() {
        InputStream inputStream = null;
        try {
            URLConnection urlConnection = new URL(site.getLink()).openConnection();
            inputStream = urlConnection.getInputStream();
        } catch (IOException e) {
            logger.error(String.format("Connection with url: %s is not established. Exception: %s", site.getLink(), e.getMessage()));
            new RuntimeException(e);
        }
        return inputStream;
    }

    private void closeStream(InputStream inputStream) {
        if (null != inputStream) {
            try {
                inputStream.close();
            } catch (IOException e) {
                logger.warn(String.format("Error when closing connection with site: %s, message: %s", site.getLink(), e.getMessage()));
            }
        }
    }
}
