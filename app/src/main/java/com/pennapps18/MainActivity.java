package com.pennapps18;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fasterxml.jackson.databind.ser.Serializers;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMessageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);
        mMessageAdapter = new MessageListAdapter(this, dummyMsg());
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    List<BaseMessage> dummyMsg() {
        List<BaseMessage> res = new ArrayList<BaseMessage>(50);
        for (int i = 0; i < 50; i++) {
            res.add(new BaseMessage("Msg #" + i, "User" + i, i % 5 == 0 ? 5 : 0));
        }
        return res;
    }
}
