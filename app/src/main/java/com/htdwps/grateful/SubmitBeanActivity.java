package com.htdwps.grateful;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.htdwps.grateful.Util.EmojiSelectUtil;

public class SubmitBeanActivity extends AppCompatActivity {

    EditText editText;
    EditText tagText;
    CheckBox checkBox;
    TextView expressionTextLabel;
    Spinner expressionDropdown;
    ArrayAdapter<String> emojiExpressionAdapter;
    String[] emojiList;
    String[] emotionList;

    // TODO Create a menu button to submit the new bean

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_bean);

        emojiExpressionAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, EmojiSelectUtil.emojiForSpinnerDropdown);
        emojiList = EmojiSelectUtil.emojiForSpinnerDropdown;
        emotionList = EmojiSelectUtil.emojiExpressionTextValue;

        editText = findViewById(R.id.et_beans_message_textbox);
        tagText = findViewById(R.id.et_beans_extra_taglist);
        checkBox = findViewById(R.id.checkbox_public_box);
        expressionTextLabel = findViewById(R.id.tv_mood_expression_text);
        expressionDropdown = findViewById(R.id.spinner_emoji_expression_moods_dropdown);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                boolean isPublic = b;
            }
        });

        expressionDropdown.setAdapter(emojiExpressionAdapter);
        expressionDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                int expressionValue = i;

                expressionTextLabel.setText(emotionList[i]);
                Toast.makeText(SubmitBeanActivity.this, emojiList[i] + " " + expressionValue, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
}
