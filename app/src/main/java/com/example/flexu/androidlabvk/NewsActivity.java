package com.example.flexu.androidlabvk;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.flexu.androidlabvk.models.VkNewsItem;
import com.example.flexu.androidlabvk.network.RxVk;
import com.flexus.androidlab_2.R;

import java.text.SimpleDateFormat;
import java.util.LinkedList;

public class NewsActivity extends AppCompatActivity {

    ProgressBar mProgress;
    RecyclerView mNewsList;
    RecyclerNewsAdapter mNewsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        mProgress = (ProgressBar) findViewById(R.id.loading_view);
        mNewsList = (RecyclerView) findViewById(R.id.news_view);
        mNewsAdapter = new RecyclerNewsAdapter(this);
        mNewsList.setAdapter(mNewsAdapter);
        mNewsList.setLayoutManager(new LinearLayoutManager(this));
        getNewsAndShowThem();
    }

    private void getNewsAndShowThem() {
        showLoading();
        RxVk api = new RxVk();
        api.getNews(new RxVk.RxVkListener<LinkedList<VkNewsItem>>() {


            @Override
            public void requestFinished(LinkedList<VkNewsItem> requestResult) {
                mNewsAdapter.setNewsList(requestResult);
                showMessage();
            }
        });
    }

    private void showLoading() {
        mNewsList.setVisibility(View.GONE);
        mProgress.setVisibility(View.VISIBLE);
    }

    private void showMessage() {
        mNewsList.setVisibility(View.VISIBLE);
        mProgress.setVisibility(View.GONE);
    }


    private class RecyclerNewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private LinkedList<VkNewsItem> VkNewsItemList;
        private Context mContext;

        public void setNewsList(LinkedList<VkNewsItem> mNewsList) {
            this.VkNewsItemList = mNewsList;
            notifyDataSetChanged();
        }

        public RecyclerNewsAdapter(@NonNull Context context) {
            mContext = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_item, parent, false);
            return new NewsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof NewsViewHolder) {
                VkNewsItem news = VkNewsItemList.get(position);
                ((NewsViewHolder) holder).bind(news);
                Glide.with(mContext).load(news.getPublisher().getPhoto_50()).fitCenter().into(((NewsViewHolder) holder).publisherImage);
            }
        }

        @Override
        public int getItemCount() {
            return VkNewsItemList != null ? VkNewsItemList.size() : 0;
        }

        private class NewsViewHolder extends RecyclerView.ViewHolder {

            ImageView publisherImage;
            TextView publisherName;
            TextView text;
            ImageView attachmentImage;
            TextView likes;
            TextView date;

            public NewsViewHolder(View itemView) {
                super(itemView);
                publisherImage = (ImageView) itemView.findViewById(R.id.group_image);
                publisherName = (TextView) itemView.findViewById(R.id.group_name);
                text = (TextView) itemView.findViewById(R.id.news_text);
                attachmentImage = (ImageView) itemView.findViewById(R.id.attach_image);
                likes = (TextView) itemView.findViewById(R.id.likes);
                date = (TextView) itemView.findViewById(R.id.date);
            }

            public void bind(VkNewsItem news) {
                publisherName.setText(news.getPublisher().getName());
                text.setText(news.getText());
                Integer n;
                if (news.getLikes() != null) {
                    n = news.getLikes().getCount();
                } else n = 0;
                likes.setText(n.toString());

                Long d = news.getDate() * 1000;
                SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                String formatted = formatDate.format(d);
                date.setText(formatted);
                if (news.getAttachments() != null) {
                    for (int i = 0; i < news.getAttachments().size(); i++) {
                        if (news.getAttachments().get(i).getPhoto() != null) {
                            Glide.with(mContext).load(news.getAttachments().get(i).getPhoto().getPhoto_1280()).fitCenter().into(attachmentImage);
                        }
                    }
                }
            }
        }
    }
}
