package net.oooops.sigma.config;

import java.util.List;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.ext.web.AllowForwardHeaders;

public class RouterItem{

	@JsonProperty("allow-forward")
	@JsonIgnoreProperties(ignoreUnknown = true)
	private AllowForwardHeaders allowForward = AllowForwardHeaders.NONE;

	@JsonProperty("route")
	private List<RouteItem> route;

	@JsonProperty("plugin")
	@JsonIgnoreProperties(ignoreUnknown = true)
	private Plugin plugin = new Plugin();

	@JsonProperty("proxy-server-id")
	private String proxyServerId;

	@JsonProperty("enable")
	private boolean enable;

	@JsonProperty("virtual-host")
	@JsonIgnoreProperties(ignoreUnknown = true)
	private String virtualHost;

	@JsonProperty("id")
	private String id;

	@JsonProperty("desc")
	@JsonIgnoreProperties(ignoreUnknown = true)
	private String desc;

	@JsonProperty("sub-router")
	private boolean subRouter;

	public void setAllowForward(AllowForwardHeaders allowForward){
		this.allowForward = allowForward;
	}

	public AllowForwardHeaders getAllowForward(){
		return allowForward;
	}

	public void setRoute(List<RouteItem> route){
		this.route = route;
	}

	public List<RouteItem> getRoute(){
		return route;
	}

	public void setPlugin(Plugin plugin){
		this.plugin = plugin;
	}

	public Plugin getPlugin(){
		return plugin;
	}

	public void setProxyServerId(String proxyServerId){
		this.proxyServerId = proxyServerId;
	}

	public String getProxyServerId(){
		return proxyServerId;
	}

	public void setEnable(boolean enable){
		this.enable = enable;
	}

	public boolean isEnable(){
		return enable;
	}

	public void setVirtualHost(String virtualHost){
		this.virtualHost = virtualHost;
	}

	public String getVirtualHost(){
		return virtualHost;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setDesc(String desc){
		this.desc = desc;
	}

	public String getDesc(){
		return desc;
	}

	public void setSubRouter(boolean subRouter){
		this.subRouter = subRouter;
	}

	public boolean isSubRouter(){
		return subRouter;
	}
}