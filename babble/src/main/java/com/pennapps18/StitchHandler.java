package com.pennapps18;

import android.content.Context;

import android.support.annotation.NonNull;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.core.auth.StitchUser;
import com.mongodb.stitch.android.services.mongodb.local.LocalMongoDbService;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential;
import com.mongodb.stitch.core.auth.providers.facebook.FacebookCredential;
import com.mongodb.stitch.core.auth.providers.google.GoogleCredential;
import com.mongodb.stitch.core.auth.providers.userapikey.UserApiKeyCredential;
import com.mongodb.stitch.core.auth.providers.userpassword.UserPasswordCredential;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class StitchHandler {

    private static StitchAppClient stitchClient;
    public static MongoClient localClient;
    public static RemoteMongoClient atlasClient;
    private static String apiKey;
    private static String facebookToken;
    private static String googleAuthKey;
    private Gson gson = new Gson();

    public interface OnAuthCompleted{
        void onSuccess();
        void onfail(Exception e);
    }

    public StitchHandler(final Context context) {

        apiKey = context.getString(R.string.userApiKey);
        facebookToken = context.getString(R.string.facebookToken);
        googleAuthKey = context.getString(R.string.googleAuthCode);

        //initialize the Stitch client
        String stitchAppId = context.getString(R.string.stitch_app_id);
        StitchHandler.stitchClient = Stitch.initializeDefaultAppClient(stitchAppId);
    }

    /**
     * Use this method if your Stitch app is configured to allow
     * anonymous access
     */
    public void AuthWithAnonymous(final OnAuthCompleted listener) {
        StitchHandler.stitchClient.getAuth()
                .loginWithCredential(new AnonymousCredential())
                .addOnCompleteListener(new OnCompleteListener<StitchUser>() {
                    @Override
                    public void onComplete(@NonNull Task<StitchUser> task) {
                        if (task.isSuccessful()) {
                            StitchHandler.localClient = StitchHandler.stitchClient.getServiceClient(LocalMongoDbService.clientFactory);
                            StitchHandler.atlasClient = StitchHandler.stitchClient.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");
                            listener.onSuccess();
                        } else {
                            listener.onfail(task.getException());
                        }

                    }
                });
    }


//    /**
//     * Use this method if your Stitch app is configured with
//     * API Key authentication.
//     * REQUIREMENT: You must store the API Key in your strings.xml file
//     * or modify this method accordingly.
//     */
//    public void AuthWithApiKey(final OnAuthCompleted listener) {
//        StitchHandler.stitchClient.getAuth()
//                .loginWithCredential(new UserApiKeyCredential(apiKey))
//                .addOnCompleteListener(new OnCompleteListener<StitchUser>() {
//                    @Override
//                    public void onComplete(@NonNull Task<StitchUser> task) {
//                        if (task.isSuccessful()) {
//                            StitchHandler.localClient = StitchHandler.stitchClient.getServiceClient(LocalMongoDbService.clientFactory);
//                            StitchHandler.atlasClient = StitchHandler.stitchClient.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");
//                            listener.onSuccess();
//                        } else {
//                            listener.onfail(task.getException());
//                        }
//                    }
//                });
//    }
//
//    /**
//     * Use this method if your Stitch app is configured with
//     * API Key authentication.
//     * REQUIREMENT: You must pass the username and password, which will probably
//     * come from a custom login UI.
//     */
//    public void AuthWithUserPass(String username, String password, final OnAuthCompleted listener) {
//        StitchHandler.stitchClient.getAuth()
//                .loginWithCredential(new UserPasswordCredential(username, password))
//                .addOnCompleteListener(new OnCompleteListener<StitchUser>() {
//                    @Override
//                    public void onComplete(@NonNull Task<StitchUser> task) {
//                        if (task.isSuccessful()) {
//                            StitchHandler.localClient = StitchHandler.stitchClient.getServiceClient(LocalMongoDbService.clientFactory);
//                            StitchHandler.atlasClient = StitchHandler.stitchClient.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");
//                            listener.onSuccess();
//                        } else {
//                            listener.onfail(task.getException());
//                        }
//                    }
//                });
//    }
//
//    /**
//     * Use this method if your Stitch app is configured with
//     * Facebook authentication.
//     * See https://docs.mongodb.com/stitch/authentication/facebook/
//     * to learn more about configuring FB Auth.
//     */
//    public void AuthWithFacebook(final OnAuthCompleted listener) {
//        StitchHandler.stitchClient.getAuth()
//                .loginWithCredential(new FacebookCredential(facebookToken))
//                .addOnCompleteListener(new OnCompleteListener<StitchUser>() {
//                    @Override
//                    public void onComplete(@NonNull Task<StitchUser> task) {
//                        if (task.isSuccessful()) {
//                            StitchHandler.localClient = StitchHandler.stitchClient.getServiceClient(LocalMongoDbService.clientFactory);
//                            StitchHandler.atlasClient = StitchHandler.stitchClient.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");
//                            listener.onSuccess();
//                        } else {
//                            listener.onfail(task.getException());
//                        }
//                    }
//                });
//    }
//
//    /**
//     * Use this method if your Stitch app is configured with
//     * Facebook authentication.
//     * See https://docs.mongodb.com/stitch/authentication/google/
//     * to learn more about configuring Google Auth.
//     */
//    public void AuthWithGoogle(final OnAuthCompleted listener) {
//        StitchHandler.stitchClient.getAuth()
//                .loginWithCredential(new GoogleCredential(googleAuthKey))
//                .addOnCompleteListener(new OnCompleteListener<StitchUser>() {
//                    @Override
//                    public void onComplete(@NonNull Task<StitchUser> task) {
//                        if (task.isSuccessful()) {
//                            StitchHandler.localClient = StitchHandler.stitchClient.getServiceClient(LocalMongoDbService.clientFactory);
//                            StitchHandler.atlasClient = StitchHandler.stitchClient.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");
//                            listener.onSuccess();
//                        } else {
//                            listener.onfail(task.getException());
//                        }
//                    }
//                });
//    }
}
