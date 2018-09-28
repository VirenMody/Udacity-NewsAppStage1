package com.example.android.newsappstage1;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {

    private static final String LOG_TAG = "In QueryUtils - ";

    private QueryUtils() {}

    public static List<News> fetchNewsData(String apiUrl) {

        URL url = null;
        try {
            url = new URL(apiUrl);
        } catch (MalformedURLException ex) {
            Log.e(LOG_TAG, "Error building the URL ", ex);
        }

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException ex) {
            Log.e(LOG_TAG, "Error making the HTTP Request.", ex);
        }

        return extractFeatureFromJson(jsonResponse);
    }

    // Taken from Udacity QuakeReport App
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(15000 /* milliseconds */);
            urlConnection.setConnectTimeout(20000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException ex) {
            Log.e(LOG_TAG, "Error retrieving the Guardian API JSON results.", ex);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }


    // Taken from Udacity QuakeReport App
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<News> extractFeatureFromJson(String newsJSON) {
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        List<News> newsList = new ArrayList<>();

        try {
            // Turn JSON string into a JSON object
            JSONObject baseJsonResponse = new JSONObject(newsJSON);

            // Guardian API wraps entire JSON in a "response" JSON object, then retrieve results arr
            JSONObject jsonResponse = baseJsonResponse.getJSONObject("response");
            JSONArray newsJSONArr = jsonResponse.getJSONArray("results");

            for (int i = 0; i < newsJSONArr.length(); i++) {

                JSONObject currentNewsObj = newsJSONArr.getJSONObject(i);

                String headline = currentNewsObj.getString("webTitle");
                String date = currentNewsObj.getString("webPublicationDate");
                String section = currentNewsObj.getString("sectionName");
                String url = currentNewsObj.getString("webUrl");

                // Retrieving the author requires digging into contributor tag for each story
                JSONArray contributorTagsArr = currentNewsObj.getJSONArray("tags");
                String author = contributorTagsArr.length() == 0 ? null : ((JSONObject)
                        contributorTagsArr.get(0)).getString("webTitle");

                newsList.add(new News(headline, date, author, section, url));
            }

        } catch (JSONException ex) {
            Log.e(LOG_TAG, "Error parsing the Guardian API JSON results", ex);
        }

        return newsList;
    }
}
