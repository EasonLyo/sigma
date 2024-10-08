package net.oooops.sigma.config;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import io.vertx.core.http.HttpMethod;

public class Cors{
	@JsonProperty("enable")
	private boolean enable;

	@JsonProperty("allow-credentials")
	@JsonIgnoreProperties(ignoreUnknown = true)
	private boolean allowCredentials;

	@JsonProperty("allow-origins")
	@JsonIgnoreProperties(ignoreUnknown = true)
	private List<String> allowOrigins = Lists.newArrayList();

	@JsonProperty("allow-methods")
	@JsonIgnoreProperties(ignoreUnknown = true)
	private List<HttpMethod> allowMethods = Lists.newArrayList();

	public void setEnable(boolean enable){
		this.enable = enable;
	}

	public boolean isEnable(){
		return enable;
	}

	public void setAllowOrigins(List<String> allowOrigins){
		this.allowOrigins = allowOrigins;
	}

	public List<String> getAllowOrigins(){
		return allowOrigins;
	}

	public void setAllowMethods(List<HttpMethod> allowMethods){
		this.allowMethods = allowMethods;
	}

	public List<HttpMethod> getAllowMethods(){
		return allowMethods;
	}

	public boolean isAllowCredentials() {
		return allowCredentials;
	}

	public void setAllowCredentials(boolean allowCredentials) {
		this.allowCredentials = allowCredentials;
	}
}