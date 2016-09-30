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
import com.example.flexu.androidlabvk.models.VkDialog;
import com.example.flexu.androidlabvk.models.VkDialogResponse;
import com.example.flexu.androidlabvk.network.RxVk;
import com.flexus.androidlab_2.R;

import java.util.List;

public class MessageActivity extends AppCompatActivity {

    ProgressBar mProgress;
    RecyclerView mMessageList;
    RecyclerMessageAdapter mMessageAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        mProgress = (ProgressBar) findViewById(R.id.loading_view);
        mMessageList = (RecyclerView) findViewById(R.id.message_view);
        mMessageAdapter = new RecyclerMessageAdapter(this);
        mMessageList.setAdapter(mMessageAdapter);
        mMessageList.setLayoutManager(new LinearLayoutManager(this));
        getMessagesAndShowThem();
    }

    private void getMessagesAndShowThem() {
        showLoading();
        RxVk api = new RxVk();
        api.getDialogs(new RxVk.RxVkListener<VkDialogResponse>(){


            @Override
            public void requestFinished(VkDialogResponse requestResult) {
                mMessageAdapter.setDialogsList(requestResult.getDialogs());
                showMessage();
            }
        });
    }
    private void showLoading() {
        mMessageList.setVisibility(View.GONE);
        mProgress.setVisibility(View.VISIBLE);
    }

    private void showMessage() {
        mMessageList.setVisibility(View.VISIBLE);
        mProgress.setVisibility(View.GONE);
    }


    private class RecyclerMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private List<VkDialog> mDialogsList;
        private Context mContext;

        public void setDialogsList(List<VkDialog> mDialogsList) {
            this.mDialogsList = mDialogsList;
            notifyDataSetChanged();
        }

        public RecyclerMessageAdapter(@NonNull Context context) {mContext=context;}

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent,false);
            return new MessageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(holder instanceof MessageViewHolder){
                VkDialog dialogs = mDialogsList.get(position);
                ((MessageViewHolder) holder).bind(dialogs);
                Glide.with(mContext).load(dialogs.getPhoto()).fitCenter().into(((MessageViewHolder) holder).photo);
            }
        }

        @Override
        public int getItemCount() {
            return mDialogsList != null ? mDialogsList.size() : 0;
        }
        private class MessageViewHolder extends RecyclerView.ViewHolder{

            TextView message;
            ImageView photo;
            View isread;

            public MessageViewHolder(View itemView) {
                super(itemView);
                message = (TextView) itemView.findViewById(R.id.message);
                photo = (ImageView) itemView.findViewById(R.id.photo);
                isread = itemView.findViewById(R.id.is_read);
            }

            public void bind(VkDialog dialog){
                message.setText(dialog.getMessage());
                isread.setVisibility(dialog.is_read() ? View.GONE: View.VISIBLE);
            }
        }
    }
}

