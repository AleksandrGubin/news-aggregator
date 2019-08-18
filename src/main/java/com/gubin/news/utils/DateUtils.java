package com.gubin.news.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

public enum DateUtils {
    INSTANCE;

    private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);
    private static final SimpleDateFormat RSS_DATE_FORMAT = getRssDateFormat();

    private static final SimpleDateFormat getRssDateFormat() {
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
        format.setLenient(true);
        return format;
    }

    public Calendar parseRssDate(String date) {
        if (StringUtils.isEmpty(date)) {
            return null;
        }

        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(RSS_DATE_FORMAT.parse(date));
            return calendar;
        } catch (ParseException e) {
            logger.warn(String.format("Date parse exception: parse(\"%s\", \"%s\")", Arrays.toString(date.toCharArray()), RSS_DATE_FORMAT.toPattern()));
            return null;
        }
    }

    public String format(Calendar date) {
        return null == date ? null : RSS_DATE_FORMAT.format(date.getTime());
    }
}
