package net.oooops.sigma.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProxyPassItem{

	@JsonProperty("plugin")
	@JsonIgnoreProperties(ignoreUnknown = true)
	private Plugin plugin = new Plugin();

	@JsonProperty("id")
	private String id;

	@JsonProperty("upstream-id")
	private String upstreamId;

	public void setPlugin(Plugin plugin){
		this.plugin = plugin;
	}

	public Plugin getPlugin(){
		return plugin;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setUpstreamId(String upstreamId){
		this.upstreamId = upstreamId;
	}

	public String getUpstreamId(){
		return upstreamId;
	}

	@Override
 	public String toString(){
		return 
			"ProxyPassItem{" + 
			"plugin = '" + plugin + '\'' + 
			",id = '" + id + '\'' + 
			",upstream-id = '" + upstreamId + '\'' + 
			"}";
		}
}