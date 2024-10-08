package net.oooops.sigma.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProxyServer{

	@JsonProperty("http")
	private Http http;

	@JsonProperty("https")
	@JsonIgnoreProperties(ignoreUnknown = true)
	private Https https;

	public void setHttp(Http http){
		this.http = http;
	}

	public Http getHttp(){
		return http;
	}

	public void setHttps(Https https){
		this.https = https;
	}

	public Https getHttps(){
		return https;
	}

	@Override
 	public String toString(){
		return 
			"ProxyServer{" + 
			"http = '" + http + '\'' +
			",https = '" + https + '\'' +
			"}";
		}
}