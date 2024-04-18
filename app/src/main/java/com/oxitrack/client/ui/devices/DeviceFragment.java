package com.oxitrack.client.ui.devices;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.MakMoinee.library.common.MapForm;
import com.github.MakMoinee.library.dialogs.MyDialog;
import com.github.MakMoinee.library.interfaces.FirestoreListener;
import com.github.MakMoinee.library.models.FirestoreRequestBody;
import com.oxitrack.client.adapters.DeviceAdapter;
import com.oxitrack.client.databinding.DialogAddDeviceBinding;
import com.oxitrack.client.databinding.FragmentDevicesBinding;
import com.oxitrack.client.interfaces.DeviceAdapterListener;
import com.oxitrack.client.interfaces.DeviceFSListener;
import com.oxitrack.client.interfaces.DeviceListener;
import com.oxitrack.client.models.Devices;
import com.oxitrack.client.preference.UserPref;
import com.oxitrack.client.services.FSRequest;
import com.oxitrack.client.services.VRequest;

import java.util.ArrayList;
import java.util.List;

public class DeviceFragment extends Fragment {

    FragmentDevicesBinding binding;
    DialogAddDeviceBinding dialogAddDeviceBinding;
    AlertDialog addDialog;
    MyDialog myDialog;

    FSRequest request;

    VRequest vRequest;

    Devices processDevice;

    DeviceAdapter deviceAdapter;

    List<Devices> devicesList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDevicesBinding.inflate(inflater, container, false);
        myDialog = new MyDialog(requireContext());
        vRequest = new VRequest(requireContext());
        request = new FSRequest();
        setListeners();
        loadDevices();
        return binding.getRoot();
    }

    private void loadDevices() {
        devicesList = new ArrayList<>();
        binding.recycler.setAdapter(null);
        String userID = new UserPref(requireContext()).getStringItem("userID");
        request.getDevices(userID, new DeviceFSListener() {
            @Override
            public void onSuccessDevices(List<Devices> d) {
                if (d.size() > 0) {
                    devicesList = d;
                    deviceAdapter = new DeviceAdapter(requireContext(), devicesList, new DeviceAdapterListener() {
                        @Override
                        public void onClick(Devices devices) {

                        }

                        @Override
                        public void onDelete(Devices devices) {
                            AlertDialog.Builder dBuilder = new AlertDialog.Builder(requireContext());
                            DialogInterface.OnClickListener dListener = (dialog, which) -> {
                                switch (which) {
                                    case DialogInterface.BUTTON_NEGATIVE:
                                        deleteDevice(devices.getDeviceID());
                                        break;
                                    case DialogInterface.BUTTON_POSITIVE:
                                        break;
                                }
                            };
                            dBuilder.setMessage("Are You Sure You Want To Delete This Device?")
                                    .setNegativeButton("Yes", dListener)
                                    .setPositiveButton("No", dListener)
                                    .setCancelable(false)
                                    .show();
                        }
                    });
                    binding.recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
                    binding.recycler.setAdapter(deviceAdapter);
                }
            }

            @Override
            public <T> void onSuccess(T any) {
                //NOT IMPLEMENTED
            }

            @Override
            public void onError(Error error) {
                Toast.makeText(requireContext(), "There are no added devices yet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteDevice(String deviceID) {
        myDialog.show();

        request.deleteDevice(deviceID, new DeviceFSListener() {
            @Override
            public void onSuccessDevices(List<Devices> devices) {

            }

            @Override
            public <T> void onSuccess(T any) {
                myDialog.dismiss();
                loadDevices();
                Toast.makeText(requireContext(), "Successfully Deleted Device", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Error error) {
                myDialog.dismiss();
                Toast.makeText(requireContext(), "Failed To Delete Device, Please Try Again Later", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setListeners() {
        binding.btnAdd.setOnClickListener(v -> {
            dialogAddDeviceBinding = DialogAddDeviceBinding.inflate(LayoutInflater.from(requireContext()), null, false);
            AlertDialog.Builder tBuilder = new AlertDialog.Builder(requireContext());
            tBuilder.setView(dialogAddDeviceBinding.getRoot());
            setDialogListener();
            addDialog = tBuilder.create();
            addDialog.show();
        });
    }

    private void setDialogListener() {
        dialogAddDeviceBinding.btnPingDevice.setOnClickListener(v -> {
            String deviceIP = dialogAddDeviceBinding.editIP.getText().toString().trim();
            if (deviceIP.isEmpty()) {
                Toast.makeText(requireContext(), "Please Don't Leave IP Field Empty", Toast.LENGTH_SHORT).show();
            } else {
                myDialog.show();
                processDevice = new Devices.DeviceBuilder()
                        .setDeviceIP(deviceIP)
                        .build();
                vRequest.pingDevice(processDevice, new DeviceListener() {

                    @Override
                    public void onSuccessDeviceRequest() {
                        myDialog.dismiss();
                        processDevice.setStatus("active");
                        Toast.makeText(requireContext(), "Device is pingable, device addition enabled", Toast.LENGTH_SHORT).show();
                        dialogAddDeviceBinding.btnAddDevice.setEnabled(true);
                    }

                    @Override
                    public void onError(Error error) {
                        myDialog.dismiss();
                        Toast.makeText(requireContext(), "Device Is Not Accessible, Please Double Check IP", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        dialogAddDeviceBinding.btnAddDevice.setOnClickListener(v -> {

            String oldIP = processDevice.getDeviceIP();
            String deviceIP = dialogAddDeviceBinding.editIP.getText().toString().trim();

            if (oldIP.equals(deviceIP)) {
                myDialog.show();
                String userID = new UserPref(requireContext()).getStringItem("userID");
                processDevice.setUserID(userID);
                FirestoreRequestBody body = new FirestoreRequestBody.FirestoreRequestBodyBuilder()
                        .setCollectionName(FSRequest.DEVICE_COLLECTION)
                        .setParams(MapForm.convertObjectToMap(processDevice))
                        .setEmail(processDevice.getDeviceIP())
                        .setWhereFromField("deviceIP")
                        .setWhereValueField(processDevice.getDeviceID())
                        .build();

                request.insertUniqueData(body, new FirestoreListener() {
                    @Override
                    public <T> void onSuccess(T any) {
                        myDialog.dismiss();
                        Toast.makeText(requireContext(), "Successfully Added Device", Toast.LENGTH_SHORT).show();
                        addDialog.dismiss();
                        binding.recycler.setAdapter(null);
                        loadDevices();
                    }

                    @Override
                    public void onError(Error error) {
                        myDialog.dismiss();
                        Toast.makeText(requireContext(), "Failed To Add Device, Please Try Again Later", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(requireContext(), "You Have Inputted New IP, Device Ping Is Required ...", Toast.LENGTH_SHORT).show();
                dialogAddDeviceBinding.btnPingDevice.setEnabled(false);
            }
        });
    }
}
