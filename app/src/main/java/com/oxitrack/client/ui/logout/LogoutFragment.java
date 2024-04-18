package com.oxitrack.client.ui.logout;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.oxitrack.client.databinding.FragmentHomeBinding;
import com.oxitrack.client.interfaces.LogoutListener;

public class LogoutFragment extends Fragment {

    FragmentHomeBinding binding;
    LogoutListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        popUp();
        return binding.getRoot();
    }

    private void popUp() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(requireContext());
        DialogInterface.OnClickListener dListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_NEGATIVE:
                    listener.finishCurrentActivity();
                    break;
                default:
                    dialog.dismiss();
                    listener.cancelLogout();
            }
        };
        mBuilder.setMessage("Are You Sure You Want To Logout?")
                .setNegativeButton("Yes", dListener)
                .setPositiveButton("No", dListener)
                .setCancelable(false)
                .show();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        // Ensure the parent activity implements the LogoutListener interface
        if (context instanceof LogoutListener) {
            listener = (LogoutListener) context;

        } else {
            throw new ClassCastException(context.toString() + " must implement LogoutListener");
        }
    }
}
