package com.estifatech.newsapp.service;


import com.estifatech.newsapp.model.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsService {
    @GET("top-headlines")
    Call<News> getTopHeadlines(
            @Query("country") String country,
            @Query("category") String category,
            @Query("apiKey") String apiKey
    );
}
