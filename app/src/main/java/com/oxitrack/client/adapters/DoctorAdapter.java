package com.oxitrack.client.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oxitrack.client.R;
import com.oxitrack.client.interfaces.DoctorAdapterListener;
import com.oxitrack.client.models.Doctor;

import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ViewHolder> {

    Context mContext;
    List<Doctor> doctorList;
    DoctorAdapterListener listener;

    public DoctorAdapter(Context mContext, List<Doctor> doctorList, DoctorAdapterListener listener) {
        this.mContext = mContext;
        this.doctorList = doctorList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DoctorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.item_doctor, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorAdapter.ViewHolder holder, int position) {
        Doctor doctor = doctorList.get(position);
        holder.txtName.setText(String.format("Dr. %s %s %s", doctor.getFirstName(), doctor.getMiddleName(), doctor.getLastName()));
        holder.itemView.setOnClickListener(v -> listener.onClick(doctor));
    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
        }
    }
}
