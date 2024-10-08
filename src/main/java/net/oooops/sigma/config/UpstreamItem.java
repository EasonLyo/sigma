package net.oooops.sigma.config;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UpstreamItem{

	@JsonProperty("rule-strategy")
	private String ruleStrategy;

	@JsonProperty("nodes")
	private List<String> nodes;

	@JsonProperty("plugin")
	@JsonIgnoreProperties(ignoreUnknown = true)
	private Plugin plugin;

	@JsonProperty("discovery")
	private String discovery;

	@JsonProperty("id")
	private String id;

	public void setRuleStrategy(String ruleStrategy){
		this.ruleStrategy = ruleStrategy;
	}

	public String getRuleStrategy(){
		return ruleStrategy;
	}

	public void setNodes(List<String> nodes){
		this.nodes = nodes;
	}

	public List<String> getNodes(){
		return nodes;
	}

	public void setPlugin(Plugin plugin){
		this.plugin = plugin;
	}

	public Plugin getPlugin(){
		return plugin;
	}

	public void setDiscovery(String discovery){
		this.discovery = discovery;
	}

	public String getDiscovery(){
		return discovery;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	@Override
 	public String toString(){
		return 
			"UpstreamItem{" + 
			"rule-strategy = '" + ruleStrategy + '\'' + 
			",nodes = '" + nodes + '\'' + 
			",plugin = '" + plugin + '\'' + 
			",discovery = '" + discovery + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}