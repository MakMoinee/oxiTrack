package com.oxitrack.client.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users {

    private String userID;
    private String firstName;
    private String middleName;
    private String lastName;
    private String address;
    private String userName;
    private String password;
    private int userType;

    public Users(UserBuilder builder) {
        this.userID = builder.userID;
        this.firstName = builder.firstName;
        this.middleName = builder.middleName;
        this.lastName = builder.lastName;
        this.address = builder.address;
        this.userName = builder.userName;
        this.password = builder.password;
        this.userType = builder.userType;
    }

    public static class UserBuilder {

        private String userID;
        private String firstName;
        private String middleName;
        private String lastName;
        private String address;
        private String userName;
        private String password;

        private int userType;

        public UserBuilder setUserID(String userID) {
            this.userID = userID;
            return this;
        }

        public UserBuilder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserBuilder setMiddleName(String middleName) {
            this.middleName = middleName;
            return this;
        }

        public UserBuilder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserBuilder setAddress(String address) {
            this.address = address;
            return this;
        }

        public UserBuilder setUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public UserBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder setUserType(int userType) {
            this.userType = userType;
            return this;
        }

        public Users build() {
            return new Users(this);
        }

    }
}
