package com.htdwps.grateful;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.htdwps.grateful.Util.StringConstantsUtil;

public class PrivacyTermsActivity extends AppCompatActivity {

    private Intent intentExtra;

    private String statementType;

    private TextView statementHolderTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_terms);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        statementHolderTextView = findViewById(R.id.tv_blank_statement_holder);

        intentExtra = getIntent();

        if (intentExtra.hasExtra(StringConstantsUtil.STATEMENT_TYPE)) {

            statementType = intentExtra.getStringExtra(StringConstantsUtil.STATEMENT_TYPE);

            switch (statementType) {

                case StringConstantsUtil.TERMS_LABEL:

                    setTitle(R.string.terms_headline);
                    statementHolderTextView.setText(getResources().getString(R.string.terms_condition_full_statement));

                    break;

                case StringConstantsUtil.PRIVACY_LABEL:

                    setTitle(R.string.privacy_policy_headline);
                    statementHolderTextView.setText(getResources().getString(R.string.privacy_policy_full_statement));

                    break;

            }

        }

    }
}
