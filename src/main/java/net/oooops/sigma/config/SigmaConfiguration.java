package net.oooops.sigma.config;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SigmaConfiguration {
	@JsonProperty("proxy-server")
	private ProxyServer proxyServer;

	@JsonProperty("proxy-pass")
	private List<ProxyPassItem> proxyPass;

	@JsonProperty("router")
	private List<RouterItem> router;

	@JsonProperty("upstream")
	private List<UpstreamItem> upstream;

	@JsonProperty("plugins")
	@JsonIgnoreProperties(ignoreUnknown = true)
	private Plugins plugins = new Plugins();

	@JsonProperty("proxy-client")
	@JsonIgnoreProperties(ignoreUnknown = true)
	private ProxyClient proxyClient;

	public void setRouter(List<RouterItem> router){
		this.router = router;
	}

	public List<RouterItem> getRouter(){
		return router;
	}

	public void setProxyPass(List<ProxyPassItem> proxyPass){
		this.proxyPass = proxyPass;
	}

	public List<ProxyPassItem> getProxyPass(){
		return proxyPass;
	}

	public void setUpstream(List<UpstreamItem> upstream){
		this.upstream = upstream;
	}

	public List<UpstreamItem> getUpstream(){
		return upstream;
	}

	public void setProxyServer(ProxyServer proxyServer){
		this.proxyServer = proxyServer;
	}

	public ProxyServer getProxyServer(){
		return proxyServer;
	}

	public void setPlugins(Plugins plugins){
		this.plugins = plugins;
	}

	public Plugins getPlugins(){
		return plugins;
	}

	public void setProxyClient(ProxyClient proxyClient){
		this.proxyClient = proxyClient;
	}

	public ProxyClient getProxyClient(){
		return proxyClient;
	}
}