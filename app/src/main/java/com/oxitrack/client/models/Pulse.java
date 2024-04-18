package com.oxitrack.client.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.MakMoinee.library.common.MapForm;

import org.json.JSONObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pulse {
    private String pulseID;
    private String userID;
    private float BPM;
    private int BMP_AVG;

    public Pulse(PulseBuilder builder) {
        this.pulseID = builder.pulseID;
        this.BPM = builder.BPM;
        this.BMP_AVG = builder.BMP_AVG;
        this.userID = builder.userID;
    }

    public static Pulse fromJson(JSONObject object) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(object.toString(), Pulse.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static class PulseBuilder {
        private String pulseID;
        private String userID;
        private float BPM;
        private int BMP_AVG;

        public PulseBuilder setUserID(String userID) {
            this.userID = userID;
            return this;
        }

        public PulseBuilder setPulseID(String pulseID) {
            this.pulseID = pulseID;
            return this;
        }

        public PulseBuilder setBPM(float BPM) {
            this.BPM = BPM;
            return this;
        }

        public PulseBuilder setBMP_AVG(int BMP_AVG) {
            this.BMP_AVG = BMP_AVG;
            return this;
        }

        public Pulse build() {
            return new Pulse(this);
        }
    }
}
