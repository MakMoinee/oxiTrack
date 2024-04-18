package com.oxitrack.client.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oxitrack.client.R;
import com.oxitrack.client.interfaces.DeviceAdapterListener;
import com.oxitrack.client.models.Devices;

import java.util.List;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {

    Context mContext;

    List<Devices> devicesList;
    DeviceAdapterListener listener;

    public DeviceAdapter(Context mContext, List<Devices> devicesList, DeviceAdapterListener listener) {
        this.mContext = mContext;
        this.devicesList = devicesList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DeviceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.item_devices, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceAdapter.ViewHolder holder, int position) {
        Devices devices = devicesList.get(position);
        if (devices.getStatus().equals("active")) {
            holder.txtStatus.setTextColor(Color.GREEN);
        } else {
            holder.txtStatus.setTextColor(Color.RED);
        }

        holder.txtStatus.setText(devices.getStatus());
        holder.txtDeviceIP.setText(String.format("Device IP: %s", devices.getDeviceIP()));
        holder.itemView.setOnClickListener(v -> listener.onClick(devices));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(devices));
    }

    @Override
    public int getItemCount() {
        return devicesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtDeviceIP, txtStatus;
        ImageButton btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDeviceIP = itemView.findViewById(R.id.txtDeviceIP);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
