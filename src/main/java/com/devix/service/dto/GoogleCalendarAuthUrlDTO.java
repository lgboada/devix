package com.devix.service.dto;

import java.io.Serializable;

public class GoogleCalendarAuthUrlDTO implements Serializable {

    private String url;

    public GoogleCalendarAuthUrlDTO() {}

    public GoogleCalendarAuthUrlDTO(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
