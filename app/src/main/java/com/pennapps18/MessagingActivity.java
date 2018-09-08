package com.pennapps18;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ListView;
import android.widget.EditText;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MessagingActivity extends AppCompatActivity {
    private ListView mMessageView;
    private MessageListAdapter mMessageAdapter;
    private MessageListener mMessageListener;
    private Gson gson;
    Message currMessage;
    Calendar cal;
    public String usrNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        List<BaseMessage> msgs = new ArrayList<>();
        gson = new Gson();
        currMessage = null;

        mMessageView = (ListView) findViewById(R.id.messages_view);
        mMessageAdapter = new MessageListAdapter(this, msgs);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mMessageView.setAdapter(mMessageAdapter);

        mMessageListener = new MessageListener() {
            @Override
            public void onFound(Message message) {
                byte[] bytes = message.getContent();
                String msgJSON = new String(bytes);
                BaseMessage msg = gson.fromJson(msgJSON, BaseMessage.class);
                mMessageAdapter.add(msg);
            }

            @Override
            public void onLost(Message message) { }
        };

        Intent i = getIntent();
        usrNumber = i.getStringExtra("Phone#");
        mMessageAdapter.setNumber(usrNumber);
    }

    @Override
    public void onStart() {
        super.onStart();
        Nearby.getMessagesClient(this).subscribe(mMessageListener);
    }

    @Override
    public void onStop() {
        if (currMessage != null) {
            Nearby.getMessagesClient(this).unpublish(currMessage);
        }
        Nearby.getMessagesClient(this).unsubscribe(mMessageListener);
        super.onStop();
    }

    public void sendMessage(View view) {
        EditText typeBox = (EditText) findViewById(R.id.editText);
        String message = typeBox.getText().toString();
        if (message.length() > 0) {
            // some message to send this out over Nearby
            BaseMessage newMsg = new BaseMessage(message, usrNumber, getCurrTime(), 0, null);
            String msgJSON = gson.toJson(newMsg);
            Message mMessage = new Message(msgJSON.getBytes(StandardCharsets.UTF_8));
            if (currMessage != null) {
                Nearby.getMessagesClient(this).unpublish(currMessage);
            }
            Nearby.getMessagesClient(this).publish(mMessage);
            mMessageAdapter.add(newMsg);
            typeBox.getText().clear();
        }

    }

    public String getCurrTime() {
        cal = Calendar.getInstance();
        return cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE);
    }
}
