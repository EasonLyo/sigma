package net.oooops.sigma.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Https {

	@JsonProperty("plugin")
	@JsonIgnoreProperties(ignoreUnknown = true)
	private Plugin plugin;

	@JsonProperty("port")
	private int port = 443;

	@JsonProperty("enable")
	private boolean enable;

	@JsonProperty("id")
	private String id;

	@JsonProperty("ssl")
	private Ssl ssl;

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

	public void setEnable(boolean enable){
		this.enable = enable;
	}

	public boolean isEnable(){
		return enable;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setSsl(Ssl ssl){
		this.ssl = ssl;
	}

	public Ssl getSsl(){
		return ssl;
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
			"Https{" + 
			"plugin = '" + plugin + '\'' + 
			",port = '" + port + '\'' + 
			",enable = '" + enable + '\'' + 
			",id = '" + id + '\'' + 
			",ssl = '" + ssl + '\'' + 
			",server-id = '" + serverId + '\'' + 
			",desc = '" + desc + '\'' + 
			"}";
		}
}