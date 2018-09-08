package com.pennapps18;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.content.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class MessageListAdapter extends BaseAdapter {
public class MessageListAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private static final int VIEW_TYPE_MESSAGE_HELP = 3;

    private Context mContext;
    private List<BaseMessage> mMessageList;

    public MessageListAdapter(Activity context, List<BaseMessage> messageList) {
    public MessageListAdapter(Context context, List<BaseMessage> messageList) {
        mContext = context;
        mMessageList = messageList;
    }

    @Override
    public int getCount() {
    public int getItemCount() {
        return mMessageList.size();
    }

    @Override
    public BaseMessage getItem(int idx) {
        return mMessageList.get(idx);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemViewType(int idx) {
        BaseMessage message = (BaseMessage) mMessageList.get(idx);

        if (message.getSender().equals("1234567890")) {
            return VIEW_TYPE_MESSAGE_SENT;
        } else if (message.getUrgency() > 0) {
        } else if(message.getUrgency() > 0){
            return VIEW_TYPE_MESSAGE_HELP;
        } else {
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        MessageViewHolder holder = new MessageViewHolder();
        LayoutInflater messageInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        BaseMessage message = mMessageList.get(i);
        int mType = getItemViewType(i);

        if (mType == VIEW_TYPE_MESSAGE_SENT) { // Sent message
            convertView = messageInflater.inflate(R.layout.item_msg_sent, null);
            holder.messageBody = (TextView) convertView.findViewById(R.id.message_body);
            convertView.setTag(holder);
            holder.messageBody.setText(message.getBody());
            holder.timeStamp = (TextView) convertView.findViewById(R.id.message_time);
            holder.timeStamp.setText(message.getTimeStamp());
        } else if (mType == VIEW_TYPE_MESSAGE_HELP) { // Help message
            convertView = messageInflater.inflate(R.layout.item_msg_help, null);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.messageBody = (TextView) convertView.findViewById(R.id.message_body);
            holder.timeStamp = (TextView) convertView.findViewById(R.id.message_time);
            convertView.setTag(holder);
            holder.name.setText(message.getSender());
            holder.messageBody.setText(message.getBody());
            holder.timeStamp.setText(message.getTimeStamp());
        } else { // Received message
            convertView = messageInflater.inflate(R.layout.item_msg_rec, null);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.messageBody = (TextView) convertView.findViewById(R.id.message_body);
            holder.timeStamp = (TextView) convertView.findViewById(R.id.message_time);
            convertView.setTag(holder);
            holder.name.setText(message.getSender());
            holder.messageBody.setText(message.getBody());
            holder.timeStamp.setText(message.getTimeStamp());
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_HELP) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_msg_help, parent, false);
            return new ReceivedMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_msg_rec, parent, false);
            return new ReceivedMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_msg_sent, parent, false);
            return new SentMessageHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int idx) {
        BaseMessage message = (BaseMessage) mMessageList.get(idx);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_HELP:
                ((ReceivedMessageHolder) holder).bind(message);
        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;
        ImageView profileImage;

        SentMessageHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
        }

        void bind(BaseMessage message) {
            messageText.setText(message.getBody());

        return convertView;
            // Format the stored timestamp into a readable String using method.
            timeText.setText(message.getTimeStamp());
        }
    }

    private class MessageViewHolder {
        public TextView name;
        public TextView messageBody;
        public TextView timeStamp;
    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText;
        ImageView profileImage;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
            nameText = (TextView) itemView.findViewById(R.id.text_message_name);
        }

        void bind(BaseMessage message) {
            messageText.setText(message.getBody());

            // Format the stored timestamp into a readable String using method.
            timeText.setText(message.getTimeStamp());
            nameText.setText(message.getSender());
        }
    }
}}
