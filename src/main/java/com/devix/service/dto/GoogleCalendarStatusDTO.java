package com.devix.service.dto;

import java.io.Serializable;

public class GoogleCalendarStatusDTO implements Serializable {

    private boolean connected;
    private String googleEmail;

    public GoogleCalendarStatusDTO() {}

    public GoogleCalendarStatusDTO(boolean connected, String googleEmail) {
        this.connected = connected;
        this.googleEmail = googleEmail;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public String getGoogleEmail() {
        return googleEmail;
    }

    public void setGoogleEmail(String googleEmail) {
        this.googleEmail = googleEmail;
    }
}
