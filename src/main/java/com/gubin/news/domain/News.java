package com.gubin.news.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Calendar;

@Entity(name = "News")
public class News implements Comparable<News> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Column(nullable = false)
    private String guid;

    @NotBlank
    @Column(nullable = false)
    private String title;

    @NotBlank
    @Column(nullable = false)
    private String link;

    @Column(length = 1000)
    private String description;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Calendar pubDate;

    @ManyToOne
    private Site source;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Calendar getPubDate() {
        return pubDate;
    }

    public void setPubDate(Calendar pubDate) {
        this.pubDate = pubDate;
    }

    public Site getSource() {
        return source;
    }

    public void setSource(Site source) {
        this.source = source;
    }

    @Override
    public int compareTo(News o) {
        if (null == o) {
            return -1;
        }

        if (pubDate == o.getPubDate()) {
            return 0;
        }

        if (null == pubDate && null != o.getPubDate()) {
            return 1;
        }

        if (null == o.getPubDate() && null != pubDate) {
            return -1;
        }

        return o.getPubDate().compareTo(pubDate);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
