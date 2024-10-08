package net.oooops.sigma.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProxyClient{

	@JsonProperty("ssl")
	@JsonIgnoreProperties(ignoreUnknown = true)
	private boolean ssl;

	@JsonProperty("max-pool-size-per-server")
	@JsonIgnoreProperties(ignoreUnknown = true)
	private int maxPoolSizePerServer = 20;

	public void setSsl(boolean ssl){
		this.ssl = ssl;
	}

	public boolean isSsl(){
		return ssl;
	}

	public void setMaxPoolSizePerServer(Integer maxPoolSizePerServer){
		this.maxPoolSizePerServer = maxPoolSizePerServer;
	}

	public Integer getMaxPoolSizePerServer(){
		return maxPoolSizePerServer;
	}
}