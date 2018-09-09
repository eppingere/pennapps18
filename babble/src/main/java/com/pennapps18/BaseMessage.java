package com.pennapps18;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import org.bson.Document;
// Base Stitch Packages

// Packages needed to interact with MongoDB and Stitch

// Necessary component for working with MongoDB Mobile


public class BaseMessage {

    static String TAG = "BaseMessage";

    private String body;
    private String sender;
    private String timeStamp;
    private int urgency;
    private Location coords;

    public BaseMessage(String msg, String usr, String time, int urg, Location place) {
        this.body = msg;
        this.sender = usr;
        this.timeStamp = time;
        this.urgency = urg;
        this.coords = place;
    }

    public String getBody() {
        return this.body;
    }

    public String getSender() {
        return this.sender;
    }

    public String getTimeStamp() {
        return this.timeStamp;
    }

    public int getUrgency() {
        return this.urgency;
    }

    public Location getCoords() { return this.coords; }

    public static void save(Context context, Object message_gson){
        //convert to mongoDb object
        //save to local
        try {
//            MongoCursor<String> dbsCursor = mongoClient.listDatabaseNames().iterator();
//            while(dbsCursor.hasNext()) {
//                System.out.println(dbsCursor.next());
//            }
            //context.getResources().getString(R.string.mdb_database_id)
            Log.d(TAG, context.getResources().getString(R.string.mdb_database_id));
            Log.d(TAG, context.getResources().getString(R.string.mdb_collection_id));
            StitchHandler.localClient.getDatabase("messages").getCollection("messages").insertOne(new Document("message_gson", message_gson));
            Log.d(TAG, "Saved message locally.");
        }
        catch(Exception e){
            Log.d(TAG, "Failed to save message locally.");
            Log.d(TAG, e.getMessage());
        }
        try {
            Log.d(TAG, context.getResources().getString(R.string.mdb_database_id));
            Log.d(TAG, context.getResources().getString(R.string.mdb_collection_id));
            StitchHandler.atlasClient.getDatabase("messages").getCollection("messages").insertOne(new Document("message_gson", message_gson));
            Log.d(TAG, "Saved message remotely.");
        }
        catch(Exception e){//JsonProcessingException jpe) {
            Log.d(TAG, "Failed to save message remotely.");
            Log.d(TAG, e.getMessage());
        }
        //try
            //save to remote db
        //catch
            //save to sync db
    }
}
