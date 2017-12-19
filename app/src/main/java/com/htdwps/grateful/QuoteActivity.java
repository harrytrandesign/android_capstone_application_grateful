package com.htdwps.grateful;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.htdwps.grateful.Adapter.HttpApiGetRequest;

import java.util.concurrent.ExecutionException;

public class QuoteActivity extends AppCompatActivity implements View.OnClickListener {

    static final String API_URL = "http://api.forismatic.com/api/1.0/?";
    String api_method = "getQuote";     // Method of Api call;
    String api_format = "text";         // Format available xml, json, html, text;
    String api_lang = "en";

    TextView quoteText;
    TextView buttonText;
    Typeface scriptFont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote);

        runLayout();
        runQuoteRequest();

    }

    public void runLayout() {

        scriptFont = Typeface.createFromAsset(getAssets(), "fonts/kaushan.ttf");

        quoteText = findViewById(R.id.tv_inspiration_quote);
        buttonText = findViewById(R.id.tv_next_button);
        buttonText.setOnClickListener(this);

        quoteText.setTypeface(scriptFont);
    }

    // TODO: Move this over to the submit new post page
    public void runQuoteRequest() {
        // Point to this web api
        String quoteUrl = API_URL + "method=" + api_method + "&format=" + api_format + "&lang=" + api_lang;

        // Place what we return into this string
        String result;

        // Create new instnce of the AsyncTask
        HttpApiGetRequest getRequest = new HttpApiGetRequest();

        // Perform doInBackground method from AsyncTask getting the results for result string passing in our url
        try {
            result = getRequest.execute(quoteUrl).get();
            quoteText.setText(result);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void nextActivity() {
        Intent mainIntent = new Intent(this, ListActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_next_button:

                nextActivity();
                break;
        }
    }
}
