package net.oooops.sigma.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Plugins{

	@JsonProperty("http-redirect-https")
	private boolean httpRedirectHttps;

	public void setHttpRedirectHttps(boolean httpRedirectHttps){
		this.httpRedirectHttps = httpRedirectHttps;
	}

	public boolean isHttpRedirectHttps(){
		return httpRedirectHttps;
	}

}