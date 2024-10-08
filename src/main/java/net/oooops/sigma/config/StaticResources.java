package net.oooops.sigma.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StaticResources {

    @JsonProperty("root-path")
    private String rootPath;

    @JsonProperty("url-pattern")
    private String urlPattern;

    @JsonProperty("enable")
    private boolean enable;

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public String getRootPath() {
        return rootPath;
    }

    public String getUrlPattern() {
        return urlPattern;
    }

    public void setUrlPattern(String urlPattern) {
        this.urlPattern = urlPattern;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isEnable() {
        return enable;
    }

}