package com.example.android.newsappstage1;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.LoaderManager.*;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;


public class NewsActivity extends AppCompatActivity implements LoaderCallbacks<List<News>> {

    private static final String LOG_TAG = "In NewsActivity - ";
    private static final String GUARDIAN_API_URL = "content.guardianapis.com";
    private static final String GUARDIAN_API_KEY = "07f07fb1-de71-4fd2-98a1-353afb43fb6d";
    private TextView mEmptyStateTextView;
    private NewsAdapter mNewsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);

        ListView newsListView = findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        newsListView.setEmptyView(mEmptyStateTextView);

        mNewsAdapter = new NewsAdapter(this, new ArrayList<News>());
        newsListView.setAdapter(mNewsAdapter);

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                News currentNews = (News) mNewsAdapter.getItem(position);

                Uri newsUri = Uri.parse(currentNews.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                startActivity(websiteIntent);
            }
        });

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(1, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }


    @NonNull
    @Override
    public Loader<List<News>> onCreateLoader(int i, @Nullable Bundle bundle) {

        String section = "sport|technology|education|film|environment|us-news|science|world";

        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https")
                .authority(GUARDIAN_API_URL)
                .appendPath("search")
                .appendQueryParameter("api-key", GUARDIAN_API_KEY)
                .appendQueryParameter("show-tags", "contributor")
                .appendQueryParameter("section", section)
                .appendQueryParameter("production-office", "us");

        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<News>> loader, List<News> newsList) {
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        mEmptyStateTextView.setText(R.string.no_data);

        if(newsList != null && !newsList.isEmpty()) {
            mNewsAdapter.addAll(newsList);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<News>> loader) {
        mNewsAdapter.clear();
    }
}
