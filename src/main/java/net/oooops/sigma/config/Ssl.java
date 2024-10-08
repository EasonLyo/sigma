package net.oooops.sigma.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Ssl{

	@JsonProperty("certPath")
	private String certPath;

	@JsonProperty("keyPath")
	private String keyPath;

	@JsonProperty("type")
	private String type;

	public void setCertPath(String certPath){
		this.certPath = certPath;
	}

	public String getCertPath(){
		return certPath;
	}

	public void setKeyPath(String keyPath){
		this.keyPath = keyPath;
	}

	public String getKeyPath(){
		return keyPath;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	@Override
 	public String toString(){
		return 
			"Ssl{" + 
			"certPath = '" + certPath + '\'' + 
			",keyPath = '" + keyPath + '\'' + 
			",type = '" + type + '\'' + 
			"}";
		}
}