package com.pennapps18;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class MessagingActivity extends AppCompatActivity {
    private ListView mMessageView;
    private MessageListAdapter mMessageAdapter;
    private Gson gson;
    private String usrNumber;
    private Boolean goodPerms = false;
    private ArrayList<String> endpoints;

    private final String SERVICE_ID = "com.penapps18";
    private final String TAG = "MessagingActivity";

    // Callback for discovering endpoints
    private final EndpointDiscoveryCallback mEndpointDiscoveryCallback =
            new EndpointDiscoveryCallback() {
                @Override
                public void onEndpointFound(
                        final String endpointId, DiscoveredEndpointInfo discoveredEndpointInfo) {
                    // An endpoint was found!
                    Log.d(TAG, "Found endpoint:" + endpointId);
                    updatePrompt(-1);
                    Nearby.getConnectionsClient(MessagingActivity.this).requestConnection(
                            usrNumber,
                            endpointId,
                            mConnectionLifecycleCallback)
                            .addOnSuccessListener(
                                    new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unusedResult) {
                                            // We successfully requested a connection. Now both sides
                                            // must accept before the connection is established.
                                            Log.d(TAG, "Successfully requested:" + endpointId);
                                        }
                                    })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Nearby Connections failed to request the connection.
                                            Log.d(TAG, "Failed to request:" + endpointId + "Error:" + e.getMessage());
                                            SystemClock.sleep(new Random().nextInt(201) + 20);
                                            if (!e.getMessage().contains("BLUETOOTH_ERROR")) {
                                                startAdvertising();
                                                startDiscovery();
                                            } else {
                                                Nearby.getConnectionsClient(MessagingActivity.this).requestConnection(
                                                        usrNumber,
                                                        endpointId,
                                                        mConnectionLifecycleCallback);
                                            }
                                        }
                                    });
                }

                @Override
                public void onEndpointLost(String endpointId) {
                    // A previously discovered endpoint has gone away.
                }
            };

    // Callback for receiving payloads
    private final PayloadCallback mPayloadCallback = new PayloadCallback() {
        @Override
        public void onPayloadReceived(String endpointId, Payload payload) {
            Log.d(TAG, "Received payload:" + endpointId);
            byte[] bytes = payload.asBytes();
            String msgJSON = new String(bytes);
            BaseMessage msg = gson.fromJson(msgJSON, BaseMessage.class);
            if (!mMessageAdapter.add(msg)) {
                //forward message onward here.
            }
        }
        @Override
        public void onPayloadTransferUpdate(String endpointId, PayloadTransferUpdate payloadTransferUpdate) {}
    };

    // Callback for locking connections between endpoints
    private final ConnectionLifecycleCallback mConnectionLifecycleCallback =
            new ConnectionLifecycleCallback() {
                @Override
                public void onConnectionInitiated(
                        String endpointId, ConnectionInfo connectionInfo) {
                    // Automatically accept the connection on both sides.
                    Log.d(TAG, "Connection initiated" + endpointId);
                    Nearby.getConnectionsClient(MessagingActivity.this).acceptConnection(endpointId, mPayloadCallback);
                }

                @Override
                public void onConnectionResult(String endpointId, ConnectionResolution result) {
                    switch (result.getStatus().getStatusCode()) {
                        case ConnectionsStatusCodes.STATUS_OK:
                            // We're connected! Can now start sending and receiving data.
                            Log.d(TAG, "Successfully connected:" + endpointId);
                            if (!endpoints.contains(endpointId)) {
                                endpoints.add(endpointId);
                                updatePrompt(endpoints.size());
                            }
                            break;
                        case ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED:
                            // The connection was rejected by one or both sides.
                            Log.d(TAG, "Connection rejected:" + endpointId);
                            break;
                        case ConnectionsStatusCodes.STATUS_ERROR:
                            // The connection broke before it was able to be accepted.
                            Log.d(TAG, "Connection broken:" + endpointId);
                            break;
                    }
                }

                @Override
                public void onDisconnected(String endpointId) {
                    // We've been disconnected from this endpoint. No more data can be
                    // sent or received.
                    Log.d(TAG, "Connection disconnected" + endpointId);
                    if (endpoints.contains(endpointId)) {
                        endpoints.remove(endpointId);
                        updatePrompt(endpoints.size());
                    }
                }
            };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        List<BaseMessage> msgs = new ArrayList<>();
        gson = new Gson();
        endpoints = new ArrayList<>();
        updatePrompt(0);

        if (ContextCompat.checkSelfPermission(MessagingActivity.this, Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MessagingActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        } else {
            goodPerms = true;
        }

        mMessageView = (ListView) findViewById(R.id.messages_view);
        mMessageAdapter = new MessageListAdapter(this, msgs);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mMessageView.setAdapter(mMessageAdapter);

        Intent i = getIntent();
        usrNumber = i.getStringExtra("Phone#");
        mMessageAdapter.setNumber(usrNumber);
    }

    @Override
    public void onStart() {
        super.onStart();
        startAdvertising();
        startDiscovery();
    }

    @Override
    public void onStop() {
        Nearby.getConnectionsClient(this).stopAdvertising();
        Nearby.getConnectionsClient(this).stopDiscovery();
        Nearby.getConnectionsClient(MessagingActivity.this).stopAllEndpoints();
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                } else {
                    MessagingActivity.this.finish();
                }
                break;
            }

            // other 'case' lines to check for other
            // permissions this app might request go here.
        }
    }

    public void sendMessage(View view) {
        EditText typeBox = (EditText) findViewById(R.id.editText);
        String message = typeBox.getText().toString();
        if (message.length() > 0) {
            Log.d(TAG, "Sending message");

            // some message to send out over Nearby
            BaseMessage newMsg = new BaseMessage(message, usrNumber, getCurrTime(), 0, null);
            String msgJSON = gson.toJson(newMsg);
            Nearby.getConnectionsClient(this).sendPayload(endpoints,
                    Payload.fromBytes(msgJSON.getBytes(StandardCharsets.UTF_8)));
            mMessageAdapter.add(newMsg);
            typeBox.getText().clear();
        }

    }

    private void startAdvertising() {
        Nearby.getConnectionsClient(this).startAdvertising(usrNumber,
                SERVICE_ID,
                mConnectionLifecycleCallback,
                new AdvertisingOptions(Strategy.P2P_CLUSTER))
                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unusedResult) {
                                // We're advertising!
                                Log.d(TAG, "Successfully started advertising!");
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // We were unable to start advertising.
                                Log.d(TAG, "Failed to start advertising" + e.getMessage());
                                SystemClock.sleep(new Random().nextInt(201) + 20);
                                if (!e.getMessage().contains("STATUS_ALREADY_ADVERTISING"))
                                    startAdvertising();
                            }
                        });
    }

    private void startDiscovery() {
        Nearby.getConnectionsClient(this).startDiscovery(
                SERVICE_ID,
                mEndpointDiscoveryCallback,
                new DiscoveryOptions(Strategy.P2P_CLUSTER))
                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unusedResult) {
                                // We're discovering!
                                Log.d(TAG, "Successfully started discovery");
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // We were unable to start discovering.
                                Log.d(TAG, "Failed to start discovery" + e.getMessage());
                                SystemClock.sleep(new Random().nextInt(201) + 20);
                                if (!e.getMessage().contains("STATUS_ALREADY_DISCOVERING"))
                                    startDiscovery();
                            }
                        });
    }

    public void updatePrompt(int code) {
        if (code == 0) {
            ((TextView) findViewById(R.id.editText)).setHint("No connections yet!");
        } else if (code == -1) {
            ((TextView) findViewById(R.id.editText)).setHint("Connecting...");
        } else {
            ((TextView) findViewById(R.id.editText)).setHint(code + " recipient(s). Write a message");
        }
    }

    public String getCurrTime() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE);
    }
}
