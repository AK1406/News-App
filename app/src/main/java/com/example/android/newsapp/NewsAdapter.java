package com.example.android.newsapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Context context, ArrayList<News> newsList) {
        super(context, 0, newsList);
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View currentNewsList = convertView;
        if (currentNewsList == null) {
            currentNewsList = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        News news = getItem(position);
        TextView newsTitleView = currentNewsList.findViewById(R.id.newsTitle);
        newsTitleView.setText(news.getNewsTitle());

        TextView sectionNameView = currentNewsList.findViewById(R.id.newsSectionName);
        sectionNameView.setText(news.getSectionName());

        TextView publishDateView = currentNewsList.findViewById(R.id.publishDate);
        publishDateView.setText(news.getPublishDate());

        TextView typeView=currentNewsList.findViewById(R.id.newsType);
        typeView.setText(news.getType());

        TextView authorNameView =currentNewsList.findViewById(R.id.authorName);
        if(authorNameView == null){
        authorNameView.setText(news.getAuthorName());}
        else {
            authorNameView.setText(R.string.authorNA);
        }
        return currentNewsList;

    }
}
