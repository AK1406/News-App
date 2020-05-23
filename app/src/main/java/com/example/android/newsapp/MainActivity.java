package com.example.android.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    private static final String GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/search";
    public static final int NEWS_LOADER_ID = 1;
    private NewsAdapter newsAdapter;
    private TextView errorMessage;
    private ProgressBar loaderIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loaderIndicator = findViewById(R.id.loader_indicator);
        errorMessage = findViewById(R.id.error_message);

        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            getLoaderManager().initLoader(NEWS_LOADER_ID, null, this);

        } else {
            loaderIndicator.setVisibility(View.GONE);
            errorMessage.setText(R.string.no_result_found);
        }

        newsAdapter = new NewsAdapter(this, new ArrayList<News>());
        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(newsAdapter);
        listView.setEmptyView(errorMessage);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                News currentNews = newsAdapter.getItem(position);
                assert currentNews != null;
                Uri newsUri = Uri.parse(currentNews.getWebUrl());
                Intent website = new Intent(Intent.ACTION_VIEW, newsUri);
                startActivity(website);
                Toast.makeText(MainActivity.this,"Move to web browser !",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        Uri.Builder builder = Uri.parse(GUARDIAN_REQUEST_URL).buildUpon();
        builder.appendQueryParameter("api-key","5f80f2cc-803b-4d34-bcd3-c3be01bf9452");
        builder.appendQueryParameter("show-tags","contributors");
        builder.appendQueryParameter("page-size","10");

        return new NewsLoader(this,builder.build().toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        newsAdapter.clear();
        loaderIndicator.setVisibility(View.GONE);
        errorMessage.setText(R.string.no_news_found);
        if (news != null && !news.isEmpty()) {
            newsAdapter.addAll(news);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        newsAdapter.clear();
    }
}



