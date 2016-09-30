package com.example.flexu.androidlabvk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.flexu.androidlabvk.network.RxVk;
import com.flexus.androidlab_2.R;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView mUserName;
    RelativeLayout mMainContent;
    ProgressBar mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUserName = (TextView) findViewById(R.id.user_name);
        mMainContent = (RelativeLayout) findViewById(R.id.main_content);
        mProgress = (ProgressBar) findViewById(R.id.loading_view);
        Button toMessagesButton = (Button) findViewById(R.id.messages);
        toMessagesButton.setOnClickListener(this);

        Button toFriendsOptimisedBtn = (Button) findViewById(R.id.to_friends_activity_optimised);
        toFriendsOptimisedBtn.setOnClickListener(this);

        Button toNewsButton = (Button) findViewById(R.id.to_news_btn);
        toNewsButton.setOnClickListener(this);

        setUserNameAndShowButtons();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.messages:
                toMessages();
                break;
            case R.id.to_friends_activity_optimised:
                toFriendsOptimised();
                break;
            case R.id.to_news_btn:
                toNews();
                break;
        }
    }

    private void toNews() {
        Intent newsIntent = new Intent(this, NewsActivity.class);
        startActivity(newsIntent);
    }

    private void setUserNameAndShowButtons() {
        showLoading();
        RxVk vk = new RxVk();
        vk.getUsers(new RxVk.RxVkListener<VKList<VKApiUser>>() {
            @Override
            public void requestFinished(VKList<VKApiUser> requestResult) {
                showMainLayout();
                mUserName.setText(getString(R.string.main_activity_user_name,
                        requestResult.get(0).first_name, requestResult.get(0).last_name));
            }
        }, 0);
    }

    private void showMainLayout() {
        mMainContent.setVisibility(View.VISIBLE);
        mProgress.setVisibility(View.GONE);
    }

    private void showLoading() {
        mProgress.setVisibility(View.VISIBLE);
        mMainContent.setVisibility(View.GONE);
    }

    private void toMessages() {
        Intent messageIntent = new Intent(this, MessageActivity.class);
        startActivity(messageIntent);
    }

    private void toFriendsOptimised() {
        Intent friendsIntent = new Intent(this, FriendsActivityOptimised.class);
        startActivity(friendsIntent);
    }
}
