package com.example.android.newsapp;

public class News {

    private String mNewsTitle;
    private String mSectionName;
    private String mWebUrl;
    private String mPublishDate;
    private String mNewsType;
    private String mAuthorName;

    public News(String newsTitle, String webUrl, String sectionName, String newsType,String authorName, String publishDate) {
        mWebUrl = webUrl;
        mNewsTitle = newsTitle;
        mSectionName = sectionName;
        mNewsType = newsType;
        mAuthorName=authorName;
        mPublishDate = publishDate;
    }

    public String getNewsTitle() { return mNewsTitle; }

    public String getSectionName() {
        return mSectionName;
    }

    public String getWebUrl() {
        return mWebUrl;
    }

    public String getPublishDate() {
        return mPublishDate;
    }

    public String getType() { return mNewsType; }

    public String getAuthorName(){return mAuthorName;}
}
