package com.oxitrack.client.services;

import android.util.Log;
import android.widget.Toast;

import com.github.MakMoinee.library.interfaces.FirestoreListener;
import com.github.MakMoinee.library.models.FirestoreRequestBody;
import com.github.MakMoinee.library.services.FirestoreRequest;
import com.github.MakMoinee.library.services.HashPass;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.oxitrack.client.models.Users;

public class FSRequest extends FirestoreRequest {
    public FSRequest() {
        super();
    }

    public void login(FirestoreRequestBody body, String password, FirestoreListener listener) {
        this.findAll(body, new FirestoreListener() {
            @Override
            public <T> void onSuccess(T any) {
                if (any instanceof QuerySnapshot) {
                    QuerySnapshot snapshots = (QuerySnapshot) any;
                    for (DocumentSnapshot documentSnapshot : snapshots) {
                        if (documentSnapshot.exists()) {
                            Users users = documentSnapshot.toObject(Users.class);
                            if (users != null) {
                                boolean isValid = new HashPass().verifyPassword(password, users.getPassword());
                                if (isValid) {
                                    users.setUserID(documentSnapshot.getId());
                                    listener.onSuccess(users);
                                }

                            }
                        }
                    }
                }
            }

            @Override
            public void onError(Error error) {
                if (error != null && error.getLocalizedMessage() != null) {
                    Log.e("error_login", error.getLocalizedMessage());
                }
                listener.onError(error);
            }
        });
    }
}
