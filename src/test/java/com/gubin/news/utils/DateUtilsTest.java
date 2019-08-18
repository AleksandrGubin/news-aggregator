package com.gubin.news.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;

public class DateUtilsTest {
    @Test
    public void parseRssDate() {
        Calendar date = DateUtils.INSTANCE.parseRssDate("Fri, 16 Aug 2019 17:22:00 +0300");
        Assert.assertNotNull(date);
        Assert.assertEquals(16, date.get(Calendar.DAY_OF_MONTH));
        Assert.assertEquals(Calendar.AUGUST, date.get(Calendar.MONTH));
        Assert.assertEquals(2019, date.get(Calendar.YEAR));
        Assert.assertEquals(Calendar.FRIDAY, date.get(Calendar.DAY_OF_WEEK));
        Assert.assertEquals(22, date.get(Calendar.MINUTE));
        Assert.assertEquals(0, date.get(Calendar.SECOND));
    }
}
