package com.oxitrack.client.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {

    private String doctorID;

    private String userID;

    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String registeredDate;

    public Doctor(DoctorBuilder builder) {
        this.firstName = builder.firstName;
        this.middleName = builder.middleName;
        this.lastName = builder.lastName;
        this.email = builder.email;
        this.registeredDate = builder.registeredDate;
        this.doctorID = builder.doctorID;
        this.userID = builder.userID;
    }


    public static class DoctorBuilder {
        private String doctorID;

        private String userID;
        private String firstName;
        private String middleName;
        private String lastName;
        private String email;
        private String registeredDate;

        public DoctorBuilder setDoctorID(String doctorID) {
            this.doctorID = doctorID;
            return this;
        }

        public DoctorBuilder setUserID(String userID) {
            this.userID = userID;
            return this;
        }

        public DoctorBuilder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public DoctorBuilder setMiddleName(String middleName) {
            this.middleName = middleName;
            return this;
        }

        public DoctorBuilder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public DoctorBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public DoctorBuilder setRegisteredDate(String registeredDate) {
            this.registeredDate = registeredDate;
            return this;
        }

        public Doctor build(){
            return new Doctor(this);
        }
    }
}
