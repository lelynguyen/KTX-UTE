package com.example.ktx_ute.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ktx_ute.model.ChatModel;
import com.example.ktx_ute.model.DateModel;
import com.example.ktx_ute.model.Message;
import com.example.ktx_ute.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_DATE = 0;
    public static final int TYPE_LEFT_START = 1;
    public static final int TYPE_LEFT = 2;
    public static final int TYPE_RIGHT = 3;

    private Context context;
    private List<ChatModel> chatModels;
    private String fakeUser = "2";

    private int userID;

    public MessageAdapter(Context context, int userID, List<ChatModel> chatModels) {
        this.context = context;
        this.chatModels = chatModels;
        this.userID = userID;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType)
        {
            case TYPE_LEFT_START:
                view = LayoutInflater.from(context).inflate(R.layout.item_message_start_left, parent, false);
                return new MessageViewHolder(view, true);
            case TYPE_LEFT:
                view = LayoutInflater.from(context).inflate(R.layout.item_message_left, parent, false);
                return new MessageViewHolder(view, false);
            case TYPE_RIGHT:
                view = LayoutInflater.from(context).inflate(R.layout.item_message_right, parent, false);
                return new MessageViewHolder(view, false);
            case TYPE_DATE:
                view = LayoutInflater.from(context).inflate(R.layout.item_date, parent, false);
                return new DateViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int type = holder.getItemViewType();
        switch (type) {
            case TYPE_DATE:
                DateModel dateModel = (DateModel) chatModels.get(position);
                ((DateViewHolder)holder).bind(dateModel);
                break;
            default:
                Message message = (Message) chatModels.get(position);
                ((MessageViewHolder)holder).bind(message);
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (chatModels != null)
            return chatModels.size();
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return chatModels.get(position).getType();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewUsername;
        private TextView textViewMessage;
        private TextView textViewTime;
        private boolean hasUsername;

        public MessageViewHolder(@NonNull View itemView, boolean hasUsername) {
            super(itemView);
            this.hasUsername = hasUsername;
            if (hasUsername) {
                this.textViewUsername = itemView.findViewById(R.id.textView_User);
            }
            this.textViewMessage = itemView.findViewById(R.id.textView_Message);
            this.textViewTime = itemView.findViewById(R.id.textView_Time);
        }

        public void setUsername(String username) {
            if (hasUsername) {
                textViewUsername.setText(username);
            }
        }

        public void bind(Message message) {
            setUsername(message.getUsername());
            textViewMessage.setText(message.getMessage());
            textViewTime.setText(message.getTime());
        }
    }

    public class DateViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewDate;

        public DateViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewDate = itemView.findViewById(R.id.textView_Date);
        }

        public void bind(DateModel dateModel) {
            textViewDate.setText(dateModel.getDatetime());
        }
    }
}
