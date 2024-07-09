package com.example.rasabot;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;



import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private List<ChatMessage> messages;

    public ChatAdapter(List<ChatMessage> messages) {
        this.messages = messages;
    }

    public void addMessage(ChatMessage message) {
        messages.add(message);
        notifyItemInserted(messages.size() - 1);
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessage message = messages.get(position);
        if (message.isUserMessage()) {
            holder.userMessageTextView.setText(message.getMessage());
            holder.userLayout.setVisibility(View.VISIBLE);
            holder.botLayout.setVisibility(View.GONE);
            // Set gravity to end for userLayout
            ((LinearLayout.LayoutParams) holder.userLayout.getLayoutParams()).gravity = Gravity.END;
        } else {
            holder.botMessageTextView.setText(message.getMessage());
            holder.userLayout.setVisibility(View.GONE);
            holder.botLayout.setVisibility(View.VISIBLE);
            // Set gravity to start for botLayout
            ((LinearLayout.LayoutParams) holder.botLayout.getLayoutParams()).gravity = Gravity.START;
        }
    }





    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView userMessageTextView;
        TextView botMessageTextView;
        ImageView userImageView;
        ImageView botImageView;
        LinearLayout userLayout;
        LinearLayout botLayout;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            userMessageTextView = itemView.findViewById(R.id.userMessageTextView);
            botMessageTextView = itemView.findViewById(R.id.botMessageTextView);
            userImageView = itemView.findViewById(R.id.userImageView);
            botImageView = itemView.findViewById(R.id.botImageView);
            userLayout = itemView.findViewById(R.id.userLayout);
            botLayout = itemView.findViewById(R.id.botLayout);
        }
    }


}

