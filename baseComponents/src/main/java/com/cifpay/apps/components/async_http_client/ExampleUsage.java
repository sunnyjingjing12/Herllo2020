package com.cifpay.apps.components.async_http_client;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.apache.http.Header;

//import android.preference.PreferenceActivity.Header;

import java.io.UnsupportedEncodingException;

public class ExampleUsage {

    public static void makeRequest() {

        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://www.google.com", new AsyncHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String response = responseBody != null ? new String(responseBody, getCharset()) : null;
                    System.out.println(response);
                }
                catch (UnsupportedEncodingException ignored) {
                }
            }
        });
    }
}