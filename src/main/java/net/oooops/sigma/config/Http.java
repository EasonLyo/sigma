package net.oooops.sigma.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Http {

	@JsonProperty("plugin")
	@JsonIgnoreProperties(ignoreUnknown = true)
	private Plugin plugin;

	@JsonProperty("port")
	@JsonIgnoreProperties(ignoreUnknown = true)
	private Integer port;

	@JsonProperty("id")
	private String id;

	@JsonProperty("server-id")
	private String serverId;

	@JsonProperty("desc")
	@JsonIgnoreProperties(ignoreUnknown = true)
	private String desc;

	public void setPlugin(Plugin plugin){
		this.plugin = plugin;
	}

	public Plugin getPlugin(){
		return plugin;
	}

	public void setPort(Integer port){
		this.port = port;
	}

	public Integer getPort(){
		return port;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setServerId(String serverId){
		this.serverId = serverId;
	}

	public String getServerId(){
		return serverId;
	}

	public void setDesc(String desc){
		this.desc = desc;
	}

	public String getDesc(){
		return desc;
	}

	@Override
 	public String toString(){
		return 
			"Http{" + 
			"plugin = '" + plugin + '\'' + 
			",port = '" + port + '\'' + 
			",id = '" + id + '\'' + 
			",server-id = '" + serverId + '\'' + 
			",desc = '" + desc + '\'' + 
			"}";
		}
}