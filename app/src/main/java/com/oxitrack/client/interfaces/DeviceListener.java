package com.oxitrack.client.interfaces;

import com.github.MakMoinee.library.interfaces.LocalVolleyRequestListener;

public interface DeviceListener extends LocalVolleyRequestListener {
    default void onSuccessDeviceRequest() {
        //ADD OWN IMPLEMENTATION
    }
}
