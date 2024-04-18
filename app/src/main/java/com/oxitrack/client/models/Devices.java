package com.oxitrack.client.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Devices {
    private String deviceID;

    private  String userID;
    private String deviceIP;

    private String status;

    public Devices(DeviceBuilder builder) {
        this.deviceID = builder.deviceID;
        this.deviceIP = builder.deviceIP;
        this.status = builder.status;
        this.userID = builder.userID;
    }

    public static class DeviceBuilder {
        private String deviceID;
        private String deviceIP;
        private  String userID;

        private String status;

        public DeviceBuilder setUserID(String userID) {
            this.userID = userID;
            return this;
        }

        public DeviceBuilder setDeviceID(String deviceID) {
            this.deviceID = deviceID;
            return this;
        }

        public DeviceBuilder setDeviceIP(String deviceIP) {
            this.deviceIP = deviceIP;
            return this;
        }

        public DeviceBuilder setStatus(String status) {
            this.status = status;
            return this;
        }

        public Devices build() {
            return new Devices(this);
        }
    }
}
