package com.pennapps18;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;

import android.app.Activity;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fasterxml.jackson.databind.ser.Serializers;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements NfcAdapter.CreateBeamUrisCallback {

    private static final String TAG = "BeamLargeFilesFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new StitchHandler(getApplicationContext()).AuthWithAnonymous(new StitchHandler.OnAuthCompleted() {
            @Override
            public void onSuccess() {
                // We put further UI work in here so
                // that the user cannot load data before
                // we authenticate.
                Log.d(TAG,"Connected to MongoDB");
                initializeUI();
            }

            @Override
            public void onfail(Exception e) {
                // Auth failed. Show the exception to the user
                Log.d(TAG,e.toString() + "\n\nPlease fix this error and restart the app.");
                initializeUI();
            }
        });
    }

    private void initializeUI() {
        // Locate the button in activity_main.xml
        final Button msgBtn = (Button) findViewById(R.id.btn_msg);
        final Button helpBtn = (Button) findViewById(R.id.btn_help);

        // Capture button clicks
        msgBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // Start MessagingActivity.class
                Intent myIntent = new Intent(MainActivity.this,
                        MessagingActivity.class);
                myIntent.putExtra ( "Phone#", ((EditText)findViewById(R.id.phoneText)).getText().toString() );
                startActivity(myIntent);
            }
        });
        helpBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {/*
                // Start MessagingActivity.class
                Intent myIntent = new Intent(MainActivity.this,
                        MessagingActivity.class);
                startActivity(myIntent);*/
            }
        });

        NfcAdapter nfc = NfcAdapter.getDefaultAdapter(this);
        if (nfc != null) {
            Log.w(TAG, "NFC available. Setting Beam Push URI callback");
            nfc.setBeamPushUrisCallback(this, this);
        } else {
            Log.w(TAG, "NFC is not available");
        }

        ((EditText)findViewById(R.id.phoneText)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().trim().length()==0){
                    msgBtn.setEnabled(false);
                } else {
                    msgBtn.setEnabled(true);
                }
            }
        });
    }

    /**
     * Callback for Beam events (large file version). The return value here should be an array of
     * content:// or file:// URIs to send.
     * <p>
     * Note that the system must have read access to whatever URIs are provided here.
     *
     * @param nfcEvent NFC event which triggered callback
     * @return URIs to be sent to remote device
     */
    // BEGIN_INCLUDE(createBeamUris)
    @Override
    public Uri[] createBeamUris(NfcEvent nfcEvent) {

        String fileName = "babble.apk";

        // Retrieve the path to the user's public downloads directory
        File fileDirectory = Environment
                .getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS);

        // Create a new file using the specified directory and name
        File fileToTransfer = new File(fileDirectory, fileName);
        fileToTransfer.setReadable(true, false);

        Log.i(TAG, "Beam event in progress; createBeamUris() called.");
        // Images are served using a content:// URI. See AssetProvider for implementation.
        Log.i(TAG, "Sending URI: " + Uri.fromFile(fileToTransfer));
        return new Uri[]{Uri.fromFile(fileToTransfer)};
    }
}

