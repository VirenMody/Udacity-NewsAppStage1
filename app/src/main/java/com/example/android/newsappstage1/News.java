package com.example.android.newsappstage1;

import java.util.Date;

public class News {

    private String mHeadline;
    private String mDate;
    private String mAuthor;
    private String mSection;
    private String mUrl;

    // TODO Fix date to proper format
    public News(String mHeadline, String mDate, String mAuthor, String mSection,
                String url) {
        this.mHeadline = mHeadline;
        this.mDate = mDate;
        this.mAuthor = mAuthor;
        this.mSection = mSection;
        this.mUrl = url;
    }

    public String getHeadline() {
        return mHeadline;
    }

    public String getDate() {
        return mDate;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getSection() {
        return mSection;
    }

    public String getUrl() {
        return mUrl;
    }
}
