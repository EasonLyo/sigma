package net.oooops.sigma.config;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.http.HttpMethod;

public class RouteItem {

    @JsonProperty("path")
    private String path;

    @JsonProperty("proxy-pass-id")
    private String proxyPassId;

    @JsonProperty("enable")
    private boolean enable;
    @JsonProperty("allow-method")
    @JsonIgnoreProperties(ignoreUnknown = true)
    private List<String> allowMethod = Lists.newArrayList();

    @JsonProperty("produces")
    @JsonIgnoreProperties(ignoreUnknown = true)
    private List<String> produces = Lists.newArrayList();

    @JsonProperty("consumes")
    @JsonIgnoreProperties(ignoreUnknown = true)
    private List<String> consumes = Lists.newArrayList();

    @JsonProperty("id")
    private String id;

    @JsonProperty("desc")
    @JsonIgnoreProperties(ignoreUnknown = true)
    private String desc;

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setProxyPassId(String proxyPassId) {
        this.proxyPassId = proxyPassId;
    }

    public String getProxyPassId() {
        return proxyPassId;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setAllowMethod(List<String> allowMethod) {
        this.allowMethod = allowMethod;
    }

    public List<String> getAllowMethod() {
        return allowMethod;
    }

    public void setProduces(List<String> produces) {
        this.produces = produces;
    }

    public List<String> getProduces() {
        return produces;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setConsumes(List<String> consumes) {
        this.consumes = consumes;
    }

    public List<String> getConsumes() {
        return consumes;
    }

}