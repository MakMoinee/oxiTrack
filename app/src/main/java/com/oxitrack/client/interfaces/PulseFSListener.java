package com.oxitrack.client.interfaces;

import com.github.MakMoinee.library.interfaces.FirestoreListener;
import com.oxitrack.client.models.Pulse;

import java.util.List;

public interface PulseFSListener extends FirestoreListener {
    default void onSucessPulseList(List<Pulse> pulseList) {

    }
}
