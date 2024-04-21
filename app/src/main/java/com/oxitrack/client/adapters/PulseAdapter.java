package com.oxitrack.client.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oxitrack.client.R;
import com.oxitrack.client.interfaces.PulseAdapterListener;
import com.oxitrack.client.models.Pulse;

import java.util.List;

public class PulseAdapter extends RecyclerView.Adapter<PulseAdapter.ViewHolder> {

    Context mContext;
    List<Pulse> pulseList;

    PulseAdapterListener listener;

    public PulseAdapter(Context mContext, List<Pulse> pulseList, PulseAdapterListener listener) {
        this.mContext = mContext;
        this.pulseList = pulseList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PulseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.item_pulse, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull PulseAdapter.ViewHolder holder, int position) {
        Pulse pulse = pulseList.get(position);
        holder.txtBPM.setText(String.format("BPM: %1.2f", pulse.getBPM()));
        holder.txtIP.setText(String.format("IP: %s", pulse.getIp()));
        holder.txtDate.setText(String.format("Date: %s", pulse.getDate()));
    }

    @Override
    public int getItemCount() {
        return pulseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtBPM, txtIP, txtDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtBPM = itemView.findViewById(R.id.txtBPM);
            txtIP = itemView.findViewById(R.id.txtIP);
            txtDate = itemView.findViewById(R.id.txtDate);
        }
    }
}
