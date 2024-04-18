package com.oxitrack.client.services;

import android.util.Log;

import com.github.MakMoinee.library.common.MapForm;
import com.github.MakMoinee.library.interfaces.FirestoreListener;
import com.github.MakMoinee.library.models.FirestoreRequestBody;
import com.github.MakMoinee.library.services.FirestoreRequest;
import com.github.MakMoinee.library.services.HashPass;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.oxitrack.client.interfaces.DeviceFSListener;
import com.oxitrack.client.interfaces.PulseFSListener;
import com.oxitrack.client.interfaces.PulseListener;
import com.oxitrack.client.models.Devices;
import com.oxitrack.client.models.Pulse;
import com.oxitrack.client.models.Users;

import java.util.ArrayList;
import java.util.List;

public class FSRequest extends FirestoreRequest {
    public static final String DEVICE_COLLECTION = "devices";
    public static final String PULSE_COLLECTION = "pulses";

    public FSRequest() {
        super();
    }

    public void login(FirestoreRequestBody body, String password, FirestoreListener listener) {
        this.findAll(body, new FirestoreListener() {
            @Override
            public <T> void onSuccess(T any) {
                boolean isFound = false;
                int count = 0;
                if (any instanceof QuerySnapshot) {
                    QuerySnapshot snapshots = (QuerySnapshot) any;
                    for (DocumentSnapshot documentSnapshot : snapshots) {
                        count++;
                        if (documentSnapshot.exists()) {
                            Users users = documentSnapshot.toObject(Users.class);
                            if (users != null) {
                                boolean isValid = new HashPass().verifyPassword(password, users.getPassword());
                                if (isValid) {
                                    isFound = true;
                                    users.setUserID(documentSnapshot.getId());
                                    listener.onSuccess(users);
                                }

                            }
                        }
                    }

                    if (count == snapshots.size()) {
                        if (!isFound) {
                            listener.onError(new Error("wrong username or password"));
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

    public void getDevices(String userID, DeviceFSListener listener) {
        FirestoreRequestBody body = new FirestoreRequestBody.FirestoreRequestBodyBuilder()
                .setCollectionName(DEVICE_COLLECTION)
                .setWhereFromField("userID")
                .setWhereValueField(userID)
                .build();
        this.findAll(body, new FirestoreListener() {
            @Override
            public <T> void onSuccess(T any) {
                if (any instanceof QuerySnapshot) {
                    QuerySnapshot snapshots = (QuerySnapshot) any;
                    if (!snapshots.isEmpty()) {
                        List<Devices> devicesList = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot : snapshots) {
                            if (documentSnapshot.exists()) {
                                Devices devices = documentSnapshot.toObject(Devices.class);
                                if (devices != null) {
                                    devices.setDeviceID(documentSnapshot.getId());
                                    devicesList.add(devices);
                                }
                            }
                        }

                        if (devicesList.size() == snapshots.size()) {
                            if (devicesList.size() > 0) {
                                listener.onSuccessDevices(devicesList);
                            } else {
                                listener.onError(new Error("empty"));
                            }
                        } else {
                            listener.onError(new Error("empty"));
                        }
                    }
                }
            }

            @Override
            public void onError(Error error) {
                if (error != null && error.getLocalizedMessage() != null) {
                    Log.e("error_devices", error.getLocalizedMessage());
                }
                listener.onError(error);
            }
        });
    }

    public void deleteDevice(String deviceID, DeviceFSListener listener) {
        FirestoreRequestBody body = new FirestoreRequestBody.FirestoreRequestBodyBuilder()
                .setCollectionName(DEVICE_COLLECTION)
                .setDocumentID(deviceID)
                .build();
        this.delete(body, new FirestoreListener() {
            @Override
            public <T> void onSuccess(T any) {
                listener.onSuccess("success");
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


    public void savePulse(Pulse pulse, PulseFSListener listener) {
        FirestoreRequestBody body = new FirestoreRequestBody.FirestoreRequestBodyBuilder()
                .setCollectionName(PULSE_COLLECTION)
                .setParams(MapForm.convertObjectToMap(pulse))
                .build();
        this.insertOnly(body, new FirestoreListener() {
            @Override
            public <T> void onSuccess(T any) {
                listener.onSuccess("success");
            }

            @Override
            public void onError(Error error) {
                if (error != null && error.getLocalizedMessage() != null) {
                    Log.e("error_pulse", error.getLocalizedMessage());
                }
                listener.onError(error);
            }
        });
    }

    public void getPulses(String userID, PulseFSListener listener) {
        FirestoreRequestBody body = new FirestoreRequestBody.FirestoreRequestBodyBuilder()
                .setCollectionName(FSRequest.PULSE_COLLECTION)
                .setWhereFromField("userID")
                .setWhereValueField(userID)
                .build();

        this.findAll(body, new FirestoreListener() {
            @Override
            public <T> void onSuccess(T any) {
                if (any instanceof QuerySnapshot) {
                    QuerySnapshot snapshots = (QuerySnapshot) any;
                    if (!snapshots.isEmpty()) {
                        List<Pulse> pulseList = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot : snapshots) {
                            Pulse pulse = documentSnapshot.toObject(Pulse.class);
                            if (pulse != null) {
                                pulse.setPulseID(documentSnapshot.getId());
                                pulseList.add(pulse);
                            }
                        }

                        if (pulseList.size() == snapshots.size()) {
                            if (pulseList.size() > 0) {
                                listener.onSucessPulseList(pulseList);
                            } else {
                                listener.onError(new Error("empty"));
                            }
                        }
                    }
                }
            }

            @Override
            public void onError(Error error) {
                if (error != null && error.getLocalizedMessage() != null) {
                    Log.e("error_pulse", error.getLocalizedMessage());
                }
                listener.onError(error);
            }
        });
    }
}
