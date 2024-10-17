package net.oooops.sigma.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Plugins{

	@JsonProperty("http-redirect-https")
	private boolean httpRedirectHttps;

	@JsonProperty("http-redirect-https")
	private boolean logConfig;

	public void setHttpRedirectHttps(boolean httpRedirectHttps){
		this.httpRedirectHttps = httpRedirectHttps;
	}

	public boolean isHttpRedirectHttps(){
		return httpRedirectHttps;
	}

	public boolean isLogConfig() {
		return logConfig;
	}

	public void setLogConfig(boolean logConfig) {
		this.logConfig = logConfig;
	}
}