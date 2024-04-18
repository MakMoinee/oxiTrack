package com.oxitrack.client.interfaces;

import com.github.MakMoinee.library.interfaces.FirestoreListener;
import com.oxitrack.client.models.Devices;

import java.util.List;

public interface DeviceFSListener extends FirestoreListener {
    void onSuccessDevices(List<Devices> devices);
}
