package com.oxitrack.client;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.MakMoinee.library.common.MapForm;
import com.github.MakMoinee.library.dialogs.MyDialog;
import com.github.MakMoinee.library.interfaces.FirestoreListener;
import com.github.MakMoinee.library.models.FirestoreRequestBody;
import com.github.MakMoinee.library.services.HashPass;
import com.oxitrack.client.databinding.ActivityCreateAccountBinding;
import com.oxitrack.client.models.Users;
import com.oxitrack.client.services.FSRequest;

public class CreateAccountActivity extends AppCompatActivity {

    ActivityCreateAccountBinding binding;
    FSRequest request;
    MyDialog myDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        request = new FSRequest();
        myDialog = new MyDialog(CreateAccountActivity.this);
        setListeners();
    }

    private void setListeners() {
        binding.btnSignup.setOnClickListener(v -> {
            String firstName = binding.editFirstName.getText().toString().trim();
            String middleName = binding.editMiddleName.getText().toString().trim();
            String lastName = binding.editLastName.getText().toString().trim();
            String address = binding.editAddress.getText().toString().trim();
            String username = binding.editUsername.getText().toString().trim();
            String password = binding.editPassword.getText().toString().trim();
            String confirm = binding.editConfirmPass.getText().toString().trim();


            if (
                    firstName.equals("") ||
                            middleName.equals("") ||
                            lastName.equals("") ||
                            address.equals("") ||
                            username.equals("") ||
                            password.equals("") ||
                            confirm.equals("")
            ) {
                Toast.makeText(CreateAccountActivity.this, "Please Don't Leave Empty Fields", Toast.LENGTH_SHORT).show();
            } else {
                if (password.equals(confirm)) {
                    myDialog.show();
                    Users users = new Users.UserBuilder()
                            .setFirstName(firstName)
                            .setMiddleName(middleName)
                            .setLastName(lastName)
                            .setAddress(address)
                            .setUserName(username)
                            .setPassword(new HashPass().makeHashPassword(password))
                            .setUserType(2)
                            .build();

                    FirestoreRequestBody body = new FirestoreRequestBody.FirestoreRequestBodyBuilder()
                            .setCollectionName(FSRequest.USERS_COLLECTION)
                            .setEmail(username) // replace with username
                            .setWhereFromField("userName")
                            .setWhereValueField(username)
                            .setParams(MapForm.convertObjectToMap(users))
                            .build();

                    request.insertUniqueData(body, new FirestoreListener() {
                        @Override
                        public <T> void onSuccess(T any) {
                            myDialog.dismiss();
                            Toast.makeText(CreateAccountActivity.this, "Successfully Created Account", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onError(Error error) {
                            myDialog.dismiss();
                            if (error != null && error.getLocalizedMessage() != null) {
                                Log.e("error_create", error.getLocalizedMessage());
                            }
                            Toast.makeText(CreateAccountActivity.this, "Failed To Create Account, Please Try Again Later", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(CreateAccountActivity.this, "Password Don't Match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
