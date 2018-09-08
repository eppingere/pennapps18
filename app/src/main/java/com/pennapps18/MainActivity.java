package com.pennapps18;

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
import android.util.Log;

import com.fasterxml.jackson.databind.ser.Serializers;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements NfcAdapter.CreateBeamUrisCallback {

    private static final String TAG = "BeamLargeFilesFragment";
    /**
     * Filename that is to be sent for this activity. Relative to /assets.
     */
    private static final String FILENAME = "stargazer_droid.jpg";
    /**
     * Content provider URI.
     */
    private static final String CONTENT_BASE_URI =
            "content://com.example.android.beamlargefiles.files/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        NfcAdapter nfc = NfcAdapter.getDefaultAdapter(this);
        if (nfc != null) {
            Log.w(TAG, "NFC available. Setting Beam Push URI callback");
            nfc.setBeamPushUrisCallback(this, this);
        } else {
            Log.w(TAG, "NFC is not available");
        }
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

        String fileName = "NAME.apk";

        // Retrieve the path to the user's public pictures directory
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

