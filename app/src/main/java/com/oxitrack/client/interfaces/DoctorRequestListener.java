package com.oxitrack.client.interfaces;

import com.oxitrack.client.models.Doctor;

import java.util.List;

public interface DoctorRequestListener {
    default void onSuccess(List<Doctor> doctorList) {

    }

    void onError(Error error);

}
