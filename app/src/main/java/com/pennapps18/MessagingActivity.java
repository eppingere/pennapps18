package com.pennapps18;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        List<BaseMessage> msgs = dummyMsg();

        mMessageRecycler = (ListView) findViewById(R.id.messages_view);
        mMessageAdapter = new MessageListAdapter(this, msgs);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mMessageRecycler.setLayoutManager(linearLayoutManager);
        mMessageRecycler.setAdapter(mMessageAdapter);
    }

    List<BaseMessage> dummyMsg() {
        List<BaseMessage> res = new ArrayList<>(50);
        for (int i = 0; i < 5; i++) {
            res.add(new BaseMessage("Msg #" + i, "User" + i, (i+1) % 5 == 0 ? 5 : 0));
        }
        return res;
    }
}
