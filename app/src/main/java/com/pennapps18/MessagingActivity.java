package com.pennapps18;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ListView;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView mMessageView;
    private MessageListAdapter mMessageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        List<BaseMessage> msgs = dummyMsg();

        mMessageView = (ListView) findViewById(R.id.messages_view);
        mMessageAdapter = new MessageListAdapter(this, msgs);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mMessageView.setAdapter(mMessageAdapter);
    }

    List<BaseMessage> dummyMsg() {
        List<BaseMessage> res = new ArrayList<>(50);
        for (int i = 0; i < 5; i++) {
            res.add(new BaseMessage("Msg #" + i, "User" + i, (i+1) % 5 == 0 ? 5 : 0, null));
        }
        return res;
    }

    public void sendMessage(View view) {
        EditText typeBox = (EditText) findViewById(R.id.editText);
        String message = typeBox.getText().toString();
        if (message.length() > 0) {
            // some message to send this out over p2p
            typeBox.getText().clear();
        }

    }
}
