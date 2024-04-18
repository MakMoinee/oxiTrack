package com.oxitrack.client.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.oxitrack.client.interfaces.DeviceAdapterListener;
import com.oxitrack.client.models.Devices;

import java.util.List;

public class DeviceSpinAdapter extends ArrayAdapter<Devices> {
    private Context context;
    private List<Devices> devicesList;

    DeviceAdapterListener listener;

    public DeviceSpinAdapter(Context context, int resource, List<Devices> devicesList, DeviceAdapterListener l) {
        super(context, resource, devicesList);
        this.context = context;
        this.devicesList = devicesList;
        this.listener = l;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(android.R.layout.simple_spinner_item, null);
        }

        // Customize here if needed, e.g., set the text color or size
        TextView textView = view.findViewById(android.R.id.text1);
        textView.setText(devicesList.get(position).getDeviceIP());

        return view;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, null);
        }

        // Customize here if needed, e.g., set the text color or size
        TextView textView = view.findViewById(android.R.id.text1);
        textView.setText(devicesList.get(position).getDeviceIP());

        return view;
    }
}
