package com.estifatech.newsapp;

import static com.estifatech.newsapp.constants.Constants.API_KEY;
import static com.estifatech.newsapp.constants.Constants.CATEGORY_TECHNOLOGY;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.estifatech.newsapp.PostAdapter.PostAdapter;
import com.estifatech.newsapp.model.News;
import com.estifatech.newsapp.model.NewsModel;
import com.estifatech.newsapp.service.NetworkChangeReceiver;
import com.estifatech.newsapp.service.NewsClient;
import com.estifatech.newsapp.service.NewsService;
import com.google.android.material.appbar.MaterialToolbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NetworkChangeReceiver.NetworkChangeListener {
    NetworkChangeReceiver networkChangeReceiver;
    SwipeRefreshLayout swipeRefreshLayout;
    Toolbar toolbar;
    NewsService newsService;
    RecyclerView recyclerView;
    PostAdapter adapter;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        // Initialize views
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));

        //Initialize broadcast receiver
        networkChangeReceiver = new NetworkChangeReceiver();
        networkChangeReceiver.setNetworkChangeListener(this);

        // Initialize toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize retrofit client
        newsService = NewsClient.getClient().create(NewsService.class);

        // Initialize swipeRefreshLayout
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            getTopHeadLineNews();
            swipeRefreshLayout.setRefreshing(false);
        });
        progressBar = findViewById(R.id.progressBar);


    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkChangeReceiver);
    }

    @Override
    public void onNetworkAvailable() {
        progressBar.setVisibility(View.VISIBLE);
        getTopHeadLineNews();
    }

    private void getTopHeadLineNews() {
        Call<News> topHeadlines =
                newsService.getTopHeadlines("us",CATEGORY_TECHNOLOGY, API_KEY);
        topHeadlines.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<News> call, @NonNull Response<News> response) {
                if (response.isSuccessful()) {
                    List<NewsModel> posts = new ArrayList<>();
                    News news = response.body();
                    // Handle the news data here
                    assert news != null;
                    news.articles.forEach(article -> {
                        NewsModel post = new NewsModel(); // ✅ New object per article
                        post.setTitle(article.title);
                        post.setDescription(article.description);
                        post.setUrlToImage(article.urlToImage);
                        post.setAuthor(article.author);
                        post.setUrl(article.url);
                        posts.add(post);
                    });
                    // Set the data to the RecyclerView adapter
                    adapter = new PostAdapter(posts,MainActivity.this);
                    recyclerView.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);
                }else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "unknown error";
                        Toast.makeText(MainActivity.this, "Error: " + response.code() + "\n" + errorBody, Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<News> call, @NonNull Throwable throwable) {
                Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onNetworkUnavailable() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate Menu
        getMenuInflater().inflate(R.menu.menu,menu);

        //Get Search view from menu
        MenuItem searchItem = menu.findItem(R.id.action_search);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) searchItem.getActionView();

        // Customize the search view if needed
        searchView.setQueryHint("Search news ...");
        return true;
    }
}
