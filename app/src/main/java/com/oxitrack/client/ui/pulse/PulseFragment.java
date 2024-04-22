package com.oxitrack.client.ui.pulse;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.MakMoinee.library.dialogs.MyDialog;
import com.github.MakMoinee.library.services.Demail;
import com.github.MakMoinee.library.services.Utils;
import com.oxitrack.client.adapters.DeviceSpinAdapter;
import com.oxitrack.client.adapters.PulseAdapter;
import com.oxitrack.client.configs.EmailConfigs;
import com.oxitrack.client.databinding.DialogAddPulseBinding;
import com.oxitrack.client.databinding.FragmentPulseBinding;
import com.oxitrack.client.interfaces.DeviceAdapterListener;
import com.oxitrack.client.interfaces.DeviceFSListener;
import com.oxitrack.client.interfaces.DoctorRequestListener;
import com.oxitrack.client.interfaces.PulseAdapterListener;
import com.oxitrack.client.interfaces.PulseFSListener;
import com.oxitrack.client.interfaces.PulseListener;
import com.oxitrack.client.models.Devices;
import com.oxitrack.client.models.Doctor;
import com.oxitrack.client.models.Pulse;
import com.oxitrack.client.models.Users;
import com.oxitrack.client.preference.UserPref;
import com.oxitrack.client.services.FSRequest;
import com.oxitrack.client.services.VRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class PulseFragment extends Fragment {

    private final float NORMAL_BPM = 100;
    private final float HIGH_BPM = 121; //GREATER
    private final float LOW_BPM = 99; //BELOW


    FragmentPulseBinding binding;
    DialogAddPulseBinding dialogAddPulseBinding;
    AlertDialog addDialog;
    FSRequest request;
    DeviceSpinAdapter adapter;

    List<Devices> devicesList = new ArrayList<>();
    Devices currentDevice;
    VRequest vRequest;
    MyDialog myDialog;

    PulseAdapter pulseAdapter;
    Demail demail;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPulseBinding.inflate(inflater, container, false);
        setListeners();
        request = new FSRequest();
        vRequest = new VRequest(requireContext());
        myDialog = new MyDialog(requireContext());
        demail = new Demail(
                requireContext(),
                EmailConfigs.emailHost,
                EmailConfigs.emailPort,
                EmailConfigs.emailClass,
                EmailConfigs.emailAuth,
                EmailConfigs.emailAdd,
                EmailConfigs.emailPass
        );
        loadAllDevices();
        loadAllPulses();
        return binding.getRoot();
    }

    private void loadAllPulses() {
        String userID = new UserPref(requireContext()).getStringItem("userID");
        request.getPulses(userID, new PulseFSListener() {
            @Override
            public <T> void onSuccess(T any) {
                // NOT IMPLEMENTED
            }

            @Override
            public void onSucessPulseList(List<Pulse> pulseList) {
                if (pulseList.size() > 0) {

                    Collections.sort(pulseList, (p1, p2) -> {
                        // Parse the date strings to compare them
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
                        try {
                            Date date1 = format.parse(p1.getDate());
                            Date date2 = format.parse(p2.getDate());
                            return date1.compareTo(date2);
                        } catch (ParseException e) {
                            if (e != null && e.getLocalizedMessage() != null) {
                                Log.e("pulse_error", e.getLocalizedMessage());
                            }
                        }
                        return 0;
                    });

                    pulseAdapter = new PulseAdapter(requireContext(), pulseList, pulse -> {

                    });
                    binding.recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
                    binding.recycler.setAdapter(pulseAdapter);
                }
            }

            @Override
            public void onError(Error error) {
                Toast.makeText(requireContext(), "There are no pulse record yet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAllDevices() {
        String userID = new UserPref(requireContext()).getStringItem("userID");
        if (userID != null) {
            request.getDevices(userID, new DeviceFSListener() {
                @Override
                public void onSuccessDevices(List<Devices> devices) {
                    devicesList = devices;
                }

                @Override
                public <T> void onSuccess(T any) {

                }

                @Override
                public void onError(Error error) {

                }
            });
        }
    }

    private void setListeners() {
        binding.btnAddPulseRecord.setOnClickListener(v -> {
            if (devicesList.size() > 0) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(requireContext());
                dialogAddPulseBinding = DialogAddPulseBinding.inflate(LayoutInflater.from(requireContext()), null, false);
                mBuilder.setView(dialogAddPulseBinding.getRoot());
                setDialogListeners();
                loadDialogSpin();
                addDialog = mBuilder.create();
                addDialog.show();
            }

        });

    }

    private void setDialogListeners() {
        dialogAddPulseBinding.btnStartRecord.setOnClickListener(v -> {
            if (currentDevice != null) {
                addDialog.setCancelable(false);
                dialogAddPulseBinding.btnStartRecord.setEnabled(false);
                Toast.makeText(requireContext(), "Starting to record, Please Don't Close App", Toast.LENGTH_SHORT).show();
                int count = 1;
                int count2 = 10;
                for (int i = 10000; i >= 0; i--) {
                    if (i > 1000) {
                        if (count == 1000) {
                            count2--;
                            dialogAddPulseBinding.txtCountDown.setText(Integer.toString(count2));
                            count = 0;
                        }
                        count++;
                    }
                    if (i == 10000) {
                        vRequest.getPulseData(currentDevice.getDeviceIP(), new PulseListener() {

                            @Override
                            public void onSuccessPulse(Pulse pulse) {
                                if (pulse != null) {
                                    String userID = new UserPref(requireContext()).getStringItem("userID");
                                    myDialog.setCustomMessage("Saving Pulse Data ...");
                                    myDialog.show();
                                    pulse.setUserID(userID);
                                    pulse.setIp(currentDevice.getDeviceIP());
                                    String date = Utils.getCurrentDate("yyyy-MM-dd hh:mm a");
                                    pulse.setDate(date);
                                    request.savePulse(pulse, new PulseFSListener() {
                                        @Override
                                        public <T> void onSuccess(T any) {
                                            myDialog.dismiss();
                                            Toast.makeText(requireContext(), "Successfully Saved Pulse Data", Toast.LENGTH_SHORT).show();
                                            addDialog.dismiss();
                                            notifyDoctor(pulse.getBPM());
                                            loadAllPulses();
                                        }

                                        @Override
                                        public void onError(Error error) {
                                            myDialog.dismiss();
                                            Toast.makeText(requireContext(), "Failed To Save Pulse Data, Please Try Again Later", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onError(Error error) {
                                Toast.makeText(requireContext(), "Failed To Retrieve Pulse Data", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
            }
        });
    }

    private void notifyDoctor(float bp) {
        String message = "";
        if (bp >= HIGH_BPM) {
            message = "HIGH BP";
        } else if (bp <= LOW_BPM) {
            message = "LOW BP";
        } else {
            message = "";
        }

        if (message != "") {
            Users users = new UserPref(requireContext()).getUsers();
            String subject = String.format("%s %s %s - Pulse Rate Update", users.getFirstName(), users.getMiddleName(), users.getLastName());
            String emailMessage = String.format("Good Day, \n You are registered as one of the trusted doctors of %s %s %s thus we are notifying you that he or she has taken pulse rate via OxiTrack and has been recorded that he or she has %s: %1.2f\n\n Please act accordingly, Thanks \n\n From: OxiTrack \n\n----DO NOT REPLY----", users.getFirstName(), users.getMiddleName(), users.getLastName(), message, bp);
            request.getAllDoctors(users.getUserID(), new DoctorRequestListener() {
                @Override
                public void onSuccess(List<Doctor> doctorList) {
                    for (Doctor doctor : doctorList) {
                        demail.sendEmail(doctor.getEmail(), subject, emailMessage);
                    }
                }

                @Override
                public void onError(Error error) {

                }
            });
        }

    }

    private void loadDialogSpin() {
        if (devicesList.size() > 0) {


            adapter = new DeviceSpinAdapter(requireContext(), android.R.layout.simple_spinner_item, devicesList, new DeviceAdapterListener() {
                @Override
                public void onClick(Devices devices) {
                    currentDevice = devices;
                    if (devices != null) {
                        dialogAddPulseBinding.btnStartRecord.setEnabled(true);
                    }
                }

                @Override
                public void onDelete(Devices devices) {

                }
            });
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dialogAddPulseBinding.spinner.setAdapter(adapter);
            dialogAddPulseBinding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    currentDevice = devicesList.get(position);
                    if (currentDevice != null) {
                        dialogAddPulseBinding.btnStartRecord.setEnabled(true);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }
}
