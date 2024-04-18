package com.oxitrack.client.services;

import android.content.Context;
import android.util.Log;

import com.github.MakMoinee.library.interfaces.LocalVolleyRequestListener;
import com.github.MakMoinee.library.models.LocalVolleyRequestBody;
import com.github.MakMoinee.library.services.LocalVolleyRequest;
import com.oxitrack.client.interfaces.DeviceListener;
import com.oxitrack.client.models.Devices;

import org.json.JSONObject;

public class VRequest extends LocalVolleyRequest {
    public VRequest(Context mContext) {
        super(mContext);
    }

    public void pingDevice(Devices devices, DeviceListener listener) {
        LocalVolleyRequestBody body = new LocalVolleyRequestBody.LocalVolleyRequestBodyBuilder()
                .setUrl(String.format("http://%s/ping", devices.getDeviceIP()))
                .build();
        this.sendJSONGetRequest(body, new LocalVolleyRequestListener() {

            @Override
            public void onSuccessJSON(JSONObject object) {
                listener.onSuccessDeviceRequest();
            }

            @Override
            public void onError(Error error) {
                if (error != null && error.getLocalizedMessage() != null) {
                    Log.e("error_ping", error.getLocalizedMessage());
                }
                listener.onError(error);
            }
        });
    }
}
