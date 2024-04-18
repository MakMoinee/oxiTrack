package com.oxitrack.client.interfaces;

import com.oxitrack.client.models.Devices;

public interface DeviceAdapterListener {
    void onClick(Devices devices);
    void onDelete(Devices devices);
}
