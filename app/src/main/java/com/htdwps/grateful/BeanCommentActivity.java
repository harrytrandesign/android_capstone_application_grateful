package com.htdwps.grateful;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.htdwps.grateful.Fragment.PrivateBeansFragment;
import com.htdwps.grateful.Model.Beans;
import com.htdwps.grateful.Model.CustomUser;

public class BeanCommentActivity extends AppCompatActivity {

    Beans bean;
    CustomUser user;
//    TextView tvPostText;
//    TextView tvPostPushKey;
//    TextView tvPostUserDisplayName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bean_comment);

//        tvPostText = findViewById(R.id.tv_post_text);
//        tvPostPushKey = findViewById(R.id.tv_post_pushkey);
//        tvPostUserDisplayName = findViewById(R.id.tv_post_username);

        bean = getIntent().getParcelableExtra(PrivateBeansFragment.BEAN_POST_PARAM);
        user = getIntent().getParcelableExtra(PrivateBeansFragment.CUSTOM_USER_PARAM);

        String displayName = user.getUserDisplayName();

//        tvPostText.setText(bean.getBeanText());
//        tvPostPushKey.setText(bean.getBeanPostKey());
//        if (user != null && displayName != null) {
//
//            tvPostUserDisplayName.setText(displayName);
//
//        }

    }
}
