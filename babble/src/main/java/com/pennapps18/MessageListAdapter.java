package com.pennapps18;

import android.app.Activity;
import android.content.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class MessageListAdapter extends BaseAdapter {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private static final int VIEW_TYPE_MESSAGE_HELP = 3;

    private Context mContext;
    private List<BaseMessage> mMessageList;
    private String mUsrNumber = "0000000000";

    public MessageListAdapter(Activity context, List<BaseMessage> messageList) {
        mContext = context;
        mMessageList = messageList;
    }

    public Boolean add(BaseMessage message) {
        if (this.mMessageList.contains(message)) {
            return false;
        }
        this.mMessageList.add(message);
        notifyDataSetChanged(); // to render the list we need to notify
        return true;
    }

    public void setNumber(String num) { mUsrNumber = num; }

    @Override
    public int getCount() {
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

        if (message.getSender().equals(mUsrNumber)) {
            return VIEW_TYPE_MESSAGE_SENT;
        } else if (message.getUrgency() > 0) {
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
        }
        return convertView;
    }
}

class MessageViewHolder {
    public TextView name;
    public TextView messageBody;
    public TextView timeStamp;
}

