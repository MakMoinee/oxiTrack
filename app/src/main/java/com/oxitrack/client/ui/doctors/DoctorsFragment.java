package com.oxitrack.client.ui.doctors;

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

import com.github.MakMoinee.library.dialogs.MyDialog;
import com.github.MakMoinee.library.interfaces.FirestoreListener;
import com.github.MakMoinee.library.services.Utils;
import com.oxitrack.client.adapters.DoctorAdapter;
import com.oxitrack.client.databinding.DialogAddDoctorBinding;
import com.oxitrack.client.databinding.FragmentDoctorsBinding;
import com.oxitrack.client.interfaces.DoctorRequestListener;
import com.oxitrack.client.models.Doctor;
import com.oxitrack.client.preference.UserPref;
import com.oxitrack.client.services.FSRequest;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class DoctorsFragment extends Fragment {

    FragmentDoctorsBinding binding;
    DialogAddDoctorBinding dialogAddDoctorBinding;
    AlertDialog addDoctorAlertDialog;
    MyDialog myDialog;
    FSRequest fsRequest;
    List<Doctor> doctorList = new ArrayList<>();

    DoctorAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDoctorsBinding.inflate(inflater, container, false);
        myDialog = new MyDialog(requireContext());
        fsRequest = new FSRequest();
        setListeners();
        loadDoctors();
        return binding.getRoot();
    }

    private void loadDoctors() {
        doctorList = new ArrayList<>();
        binding.recycler.setAdapter(null);
        String userID = new UserPref(requireContext()).getStringItem("userID");
        fsRequest.getAllDoctors(userID, new DoctorRequestListener() {

            @Override
            public void onSuccess(List<Doctor> doctorList) {
                adapter = new DoctorAdapter(requireContext(), doctorList, doctor -> {
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(requireContext());
                    DialogInterface.OnClickListener dListener = (dialog, which) -> {
                        switch (which) {
                            case DialogInterface.BUTTON_NEGATIVE:
                                myDialog.show();
                                fsRequest.deleteDoctor(doctor.getDoctorID(), new FirestoreListener() {
                                    @Override
                                    public <T> void onSuccess(T any) {
                                        myDialog.dismiss();
                                        Toast.makeText(requireContext(), "Successfully Deleted Doctor", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        loadDoctors();
                                    }

                                    @Override
                                    public void onError(Error error) {
                                        myDialog.dismiss();
                                        Toast.makeText(requireContext(), "Failed To Delete Doctor, Please Try Again Later", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                break;
                            default:
                                dialog.dismiss();
                                break;
                        }
                    };

                    mBuilder.setMessage("Are You Sure You Want To Delete This?")
                            .setNegativeButton("Yes", dListener)
                            .setPositiveButton("No", dListener)
                            .setCancelable(false)
                            .show();

                });
                binding.recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
                binding.recycler.setAdapter(adapter);
            }

            @Override
            public void onError(Error error) {
                Toast.makeText(requireContext(), "There are no doctors added yet, please try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setListeners() {
        binding.btnAddDoctor.setOnClickListener(v -> {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(requireContext());
            dialogAddDoctorBinding = DialogAddDoctorBinding.inflate(LayoutInflater.from(requireContext()), null, false);
            mBuilder.setView(dialogAddDoctorBinding.getRoot());
            setDialogListeners();
            addDoctorAlertDialog = mBuilder.create();
            addDoctorAlertDialog.show();
        });
    }

    private void setDialogListeners() {
        dialogAddDoctorBinding.btnProceedAdding.setOnClickListener(v -> {
            String firstName = dialogAddDoctorBinding.editFirstName.getText().toString().trim();
            String middleName = dialogAddDoctorBinding.editMiddleName.getText().toString().trim();
            String lastName = dialogAddDoctorBinding.editLastName.getText().toString().trim();
            String email = dialogAddDoctorBinding.editEmail.getText().toString().trim();

            if (firstName.equals("") || lastName.equals("") || email.equals("")) {
                Toast.makeText(requireContext(), "Please Don't Leave Empty Fields", Toast.LENGTH_SHORT).show();
            } else {
                myDialog.show();
                String userID = new UserPref(requireContext()).getStringItem("userID");
                Doctor doctor = new Doctor.DoctorBuilder()
                        .setFirstName(firstName)
                        .setMiddleName(middleName)
                        .setLastName(lastName)
                        .setUserID(userID)
                        .setEmail(email)
                        .setRegisteredDate(Utils.getCurrentDate("yyyy-MM-dd hh:mm a"))
                        .build();

                fsRequest.insertDoctor(doctor, new FirestoreListener() {
                    @Override
                    public <T> void onSuccess(T any) {
                        myDialog.dismiss();
                        Toast.makeText(requireContext(), "Successfully Added Doctor", Toast.LENGTH_SHORT).show();
                        loadDoctors();
                        addDoctorAlertDialog.dismiss();
                    }

                    @Override
                    public void onError(Error error) {
                        myDialog.dismiss();
                        Toast.makeText(requireContext(), "Failed To Add Doctor, Please Try Again Later", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
