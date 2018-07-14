package com.htdwps.grateful.Util;

import com.htdwps.grateful.Adapter.HttpApiGetRequest;

import java.util.concurrent.ExecutionException;

/**
 * Created by HTDWPS on 7/14/18.
 */
public class QuoteGetRequestHelperUtil {

    private static final String API_URL = "http://api.forismatic.com/api/1.0/?";
    private static final String API_METHOD = "getQuote";                                // Method of Api call;
    private static final String API_FORMAT = "text";                                    // Format available xml, json, html, text;
    private static final String API_LANG = "en";

    // TODO: Run an internet connection check here before calling out this Quote.
    public static String runQuoteRequest() {
        // Point to this web api
        String quoteUrl = API_URL + "method=" + API_METHOD + "&format=" + API_FORMAT + "&lang=" + API_LANG;

        // Place what we return into this string
        String result;

        // Create new instance of the AsyncTask
        HttpApiGetRequest getRequest = new HttpApiGetRequest();

        // Perform doInBackground method from AsyncTask getting the results for result string passing in our url
        try {
            result = getRequest.execute(quoteUrl).get();

            return result;

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return "";
    }

}
