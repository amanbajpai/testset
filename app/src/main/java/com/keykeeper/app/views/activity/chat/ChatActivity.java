package com.keykeeper.app.views.activity.chat;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.keykeeper.app.R;
import com.keykeeper.app.databinding.ActivityChatLayoutBinding;
import com.keykeeper.app.preferences.AppSharedPrefs;
import com.keykeeper.app.utils.AppUtils;
import com.keykeeper.app.utils.Utils;
import com.keykeeper.app.views.base.BaseActivity;
import com.keykeeper.app.views.custom_view.CustomActionBar;

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

        binding.chatWv.getSettings().setJavaScriptEnabled(true);
        binding.chatWv.getSettings().setLoadWithOverviewMode(true);
        binding.chatWv.getSettings().setUseWideViewPort(true);
        Utils.hideSoftKeyboard(ChatActivity.this);
        loadChatUrl();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right_iv:
                finish();
                break;
        }
    }


    @Override
    public void setCustomActionBar() {
        CustomActionBar customActionBar = new CustomActionBar(this);
        customActionBar.setActionbar(getString(R.string.chat_header), false, true,false, false, false, this);
    }


    private void loadChatUrl() {

        String chat_url = getIntent().getStringExtra(AppUtils.CHAT_EMP_URL);
        if (chat_url == null || chat_url.equals("")) {
            chat_url = AppSharedPrefs.getChatUrl();
        }
//        String chat_url = AppSharedPrefs.getChatUrl();

        binding.chatWvProgressBar.setVisibility(View.VISIBLE);
        binding.chatWv.setVisibility(View.VISIBLE);

        binding.chatWv.loadUrl(chat_url);
        binding.chatWv.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                binding.chatWv.setVisibility(View.VISIBLE);
//                binding.chatWvProgressBar.setVisibility(View.GONE);
//            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                binding.chatWv.setVisibility(View.GONE);
                binding.noDataFountLayout.setVisibility(View.VISIBLE);
                binding.tvNoRecords.setText(R.string.chat_no_internet);

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                binding.chatWv.setVisibility(View.VISIBLE);
                binding.chatWvProgressBar.setVisibility(View.GONE);
            }
        });
    }
}
