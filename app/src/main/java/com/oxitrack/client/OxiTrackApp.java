package com.oxitrack.client;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class OxiTrackApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
