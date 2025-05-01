package com.estifatech.newsapp.PostAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.estifatech.newsapp.NewsActivity;
import com.estifatech.newsapp.R;
import com.estifatech.newsapp.model.NewsModel;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder>{
    List<NewsModel> posts;
    Context context;

    public PostAdapter(List<NewsModel> posts, Context context) {
        this.posts = posts;
        this.context = context;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.posts,parent,false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        NewsModel post = posts.get(position);
        holder.textView.setText(post.getTitle() != null ? post.getTitle() : "No title");
        holder.body.setText(post.getDescription() != null ? post.getDescription() : "No description");
        String url = post.getUrl();
        if (post.getUrlToImage() != null && !post.getUrlToImage().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(post.getUrlToImage())// optional
                    .error(R.drawable.offline)       // optional
                    .into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.post);
        }
        holder.itemView.setOnClickListener(v->{
            if (url != null && !url.isEmpty()) {
                Intent intent = new Intent(context, NewsActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("title", post.getTitle());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
    public static class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView, body;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            textView = itemView.findViewById(R.id.fetch);
            body = itemView.findViewById(R.id.body);
        }
    }

}



