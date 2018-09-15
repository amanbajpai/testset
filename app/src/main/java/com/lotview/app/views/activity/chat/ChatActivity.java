package com.lotview.app.views.activity.chat;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lotview.app.R;
import com.lotview.app.databinding.ActivityChatLayoutBinding;
import com.lotview.app.preferences.AppSharedPrefs;
import com.lotview.app.views.base.BaseActivity;
import com.lotview.app.views.custom_view.CustomActionBar;

/**
 * Created by ankurrawal on 15/9/18.
 */

public class ChatActivity extends BaseActivity {
    private Context context;
    private ChatActivityViewModel viewModel;
    private ActivityChatLayoutBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setCustomActionBar();
        initializeViews();
    }

    @Override
    public void initializeViews() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat_layout);
        viewModel = ViewModelProviders.of(this).get(ChatActivityViewModel.class);
        binding.setViewModel(viewModel);
        loadChatUrl();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void setCustomActionBar() {
        CustomActionBar customActionBar = new CustomActionBar(this);
        customActionBar.setActionbar(getString(R.string.chat_header), false, false, this);
    }

    private void loadChatUrl() {
        binding.chatWv.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                binding.chatWvProgressBar.setVisibility(View.GONE);
            }
        });
        binding.chatWvProgressBar.setVisibility(View.VISIBLE);
        binding.chatWv.loadUrl(AppSharedPrefs.getChatUrl());
    }
}
