package com.oxitrack.client;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.MakMoinee.library.common.MapForm;
import com.github.MakMoinee.library.dialogs.MyDialog;
import com.github.MakMoinee.library.interfaces.FirestoreListener;
import com.github.MakMoinee.library.models.FirestoreRequestBody;
import com.google.firebase.firestore.auth.User;
import com.oxitrack.client.databinding.ActivityMainBinding;
import com.oxitrack.client.models.Users;
import com.oxitrack.client.preference.UserPref;
import com.oxitrack.client.services.FSRequest;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FSRequest request;
    MyDialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        request = new FSRequest();
        myDialog = new MyDialog(MainActivity.this);
        try{
            String userID = new UserPref(MainActivity.this).getStringItem("userID");
            if (userID != null) {
                Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        }catch (Exception e){

        }

        setListeners();
    }

    private void setListeners() {
        binding.btnLogin.setOnClickListener(v -> {
            String username = binding.editUsername.getText().toString().trim();
            String password = binding.editPassword.getText().toString().trim();

            if (username.equals("") || password.equals("")) {
                Toast.makeText(MainActivity.this, "Please Don't Leave Empty Fields", Toast.LENGTH_SHORT).show();
            } else {
                myDialog.show();

                FirestoreRequestBody body = new FirestoreRequestBody.FirestoreRequestBodyBuilder()
                        .setCollectionName(FSRequest.USERS_COLLECTION)
                        .setEmail(username) // replace by username
                        .setWhereFromField("userName")
                        .setWhereValueField(username)
                        .build();

                request.login(body, password, new FirestoreListener() {
                    @Override
                    public <T> void onSuccess(T any) {
                        myDialog.dismiss();
                        if (any instanceof Users) {
                            Users u = (Users) any;
                            if (u != null) {
                                new UserPref(MainActivity.this).storeLogin(MapForm.convertObjectToMap(u));
                                Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }

                    }

                    @Override
                    public void onError(Error error) {
                        myDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Wrong Password or Username", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

        binding.txtCreateAccount.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CreateAccountActivity.class);
            startActivity(intent);
        });
    }
}