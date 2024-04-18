package com.oxitrack.client.preference;

import android.content.Context;

import com.github.MakMoinee.library.preference.LoginPref;
import com.oxitrack.client.models.Users;

import java.util.Map;

public class UserPref extends LoginPref {
    public UserPref(Context mContext) {
        super(mContext);
    }

    public Users getUsers() {
        Users users = new Users();
        Map<String, Object> myMap = this.getLogin();

        for (Map.Entry<String, Object> entry : myMap.entrySet()) {
            if (entry.getKey().equals("userID")) {
                users.setUserID(entry.getValue().toString());
            }
            if (entry.getKey().equals("firstName")) {
                users.setFirstName(entry.getValue().toString());
            }
            if (entry.getKey().equals("middleName")) {
                users.setMiddleName(entry.getValue().toString());
            }
            if (entry.getKey().equals("lastName")) {
                users.setLastName(entry.getValue().toString());
            }
            if (entry.getKey().equals("address")) {
                users.setAddress(entry.getValue().toString());
            }
            if (entry.getKey().equals("userName")) {
                users.setUserName(entry.getValue().toString());
            }

            if (entry.getKey().equals("userType")) {
                users.setUserType((int) entry.getValue());
            }

        }

        return users;
    }
}
