package com.example.chenx.musc.tool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

public class TenorTool {

    private static final String API_KEY = "YGPYOYKS4ITJ";
    private static final String LogTag = "TenorTest";

    /**
     * Get anon id
     */
    public static String getAnonId() {
        final String url = String.format("https://api.tenor.com/v1/anonid?key=%s", API_KEY);
        try {
            JSONObject response = get(url);
            return response.getString("anon_id");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Get Search Result GIFs
     */
    public static JSONObject getSearchResults(String anonId, String searchTerm, int limit) {

        // make search request - using default locale of EN_US

        final String url = String.format("https://api.tenor.com/v1/search?q=%1$s&key=%2$s&anon_id=%3$s&limit=%4$s",
                searchTerm, API_KEY, anonId, limit);
        try {
            return get(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Construct and run a GET request
     */
    private static JSONObject get(String url) throws IOException {
        HttpURLConnection connection = null;
        try {
            // Get request
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            // Handle failure
            int statusCode = connection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK && statusCode != HttpURLConnection.HTTP_CREATED) {
                String error = String.format("HTTP Code: '%1$s' from '%2$s'", statusCode, url);
                throw new ConnectException(error);
            }

            // Parse response
            //return parser(connection);
            return JSON.parseObject(getJsonString(connection));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return new JSONObject();
    }

    private static String getJsonString(HttpURLConnection connection)
    {
        BufferedReader reader=null;
        try{
            InputStream in =connection.getInputStream();
            reader=new BufferedReader(new InputStreamReader(in));
            StringBuilder response=new StringBuilder();
            String line;
            while((line=reader.readLine())!=null)
            {
                response.append(line);
            }
            return response.toString();
        }catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (reader != null) { try { reader. close(); } catch (IOException e) { e. printStackTrace(); } } if (connection != null) { connection. disconnect(); }
        }
        return null;
    }


}
