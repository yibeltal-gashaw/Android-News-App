package com.estifatech.newsapp;

import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;

public class NewsActivity extends AppCompatActivity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_news);
        webView = findViewById(R.id.webView);
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get the URL from Intent
        String url = getIntent().getStringExtra("url");
        String title = getIntent().getStringExtra("title");
        // Enable the Up button (back arrow)
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 🔙 show back arrow
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(title); // or toolbar.setTitle("Your Title");
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        if (url == null || url.isEmpty()) {
            Toast.makeText(this, "Invalid URL", Toast.LENGTH_SHORT).show();
            finish(); // Exit if no URL is provided
            return;
        }

        // Configure WebView settings
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // Enable JS if needed
        webSettings.setDomStorageEnabled(true); // Optional for advanced sites

        // Ensure links open inside WebView
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }
        });

        // Load the initial URL
        webView.loadUrl(url);
    }
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack(); // Navigate back within the WebView
        } else {
            super.onBackPressed(); // Exit activity
        }
    }
}