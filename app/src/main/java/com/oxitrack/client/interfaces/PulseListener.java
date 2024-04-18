package com.oxitrack.client.interfaces;

import com.github.MakMoinee.library.interfaces.LocalVolleyRequestListener;
import com.oxitrack.client.models.Pulse;

public interface PulseListener extends LocalVolleyRequestListener {
    default void onSuccessPulse(Pulse pulse) {

    }
}
