package net.oooops.sigma.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class logConfig {
    @JsonProperty("enable")
    private boolean enable;

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isEnable() {
        return enable;
    }
}