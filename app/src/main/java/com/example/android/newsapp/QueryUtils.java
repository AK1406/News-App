package com.example.android.newsapp;

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

public class QueryUtils {

    //code form QuakeReport
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    public static List<News> fetchNewsData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making in HTTP request : ", e);
        }
        List<News> news = getNewsDataFromJson(jsonResponse);
        return news;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem is  ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem in retrieving news Json result.", e);
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

    public static ArrayList<News> getNewsDataFromJson(String Json) {
        ArrayList<News> newsList = new ArrayList<>();
        try {
            JSONObject baseJsonResponse = new JSONObject(Json);
            JSONObject response = baseJsonResponse.getJSONObject("response");
            JSONArray newsArray = response.getJSONArray("results");

            for (int i = 0; i < newsArray.length(); i++) {
                JSONObject jsonObject = newsArray.getJSONObject(i);

                String newsSectionName = jsonObject.getString("sectionName");
                String newsTitle = jsonObject.getString("webTitle");
                String webUrl = jsonObject.getString("webUrl");
                String publicationDate = jsonObject.getString("webPublicationDate");
                String newsType = jsonObject.getString("type");
                String authorName="";
//check author
                JSONArray authorArray=jsonObject.getJSONArray("tags");
                if (authorArray != null) {
                    for (int j = 0; j < authorArray.length(); j++) {
                        JSONObject object = authorArray.getJSONObject(j);
                        authorName = object.getString("webTitle");
                    }
                    if (authorName == null) {
                        authorName = "Author NA";
                    }
                }
                News newsData = new News(newsTitle, webUrl, newsSectionName, newsType,authorName, publicationDate);
                newsList.add(newsData);
            }
        } catch (JSONException e) {
            Log.i(LOG_TAG, "Problem during parsing news JSON results" + e.toString());
        }
        return newsList;
    }
}

