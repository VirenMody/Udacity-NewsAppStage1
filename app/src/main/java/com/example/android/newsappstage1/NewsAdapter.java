package com.example.android.newsappstage1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NewsAdapter extends ArrayAdapter {

    private static final String LOG_TAG = "In NewsAdapter - ";

    public NewsAdapter(Context context, List<News> newsList) {
        super(context, 0, newsList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_list_item,
                    parent, false);
        }

        News currentNews = (News) getItem(position);

        TextView headlineView = convertView.findViewById(R.id.headline);
        headlineView.setText(currentNews.getHeadline());

        String author = currentNews.getAuthor();

        String guardianDatePattern = "yyyy-MM-dd";
        String displayDatePattern = "EEEE, MMMM, dd, yyyy";
        SimpleDateFormat guardianDateFormat = new SimpleDateFormat(guardianDatePattern);
        Date parsedDate = null;
        try {
            parsedDate = guardianDateFormat.parse(currentNews.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat displayDateFormat = new SimpleDateFormat(displayDatePattern);
        String date = displayDateFormat.format(parsedDate);

        String author_date = author == null? date : author + " - " + date;
        TextView authorView = convertView.findViewById(R.id.author_date);
        authorView.setText(author_date);

        TextView sectionView = convertView.findViewById((R.id.section));
        sectionView.setText((currentNews.getSection()));

        return convertView;
    }
}
