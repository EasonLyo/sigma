package net.oooops.sigma.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Hsts {

    @JsonProperty("enable")
    private boolean enable;

	@JsonProperty("include-sub-domains")
    private boolean includeSubDomains;

    @JsonProperty("max-age")
	@JsonIgnoreProperties(ignoreUnknown = true)
    private long maxAge = 15768000L;

    public void setIncludeSubDomains(boolean includeSubDomains) {
        this.includeSubDomains = includeSubDomains;
    }

    public boolean isIncludeSubDomains() {
        return includeSubDomains;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setMaxAge(long maxAge) {
        this.maxAge = maxAge;
    }

    public long getMaxAge() {
        return maxAge;
    }

    @Override
    public String toString() {
        return
                "Hsts{" +
                        "include-sub-domains = '" + includeSubDomains + '\'' +
                        ",enable = '" + enable + '\'' +
                        ",max-age = '" + maxAge + '\'' +
                        "}";
    }
}