package net.oooops.sigma.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RewritePath{

	@JsonProperty("regexp")
	@JsonIgnoreProperties(ignoreUnknown = true)
	private String regexp;

	@JsonProperty("enable")
	private boolean enable;

	@JsonProperty("replacement")
	@JsonIgnoreProperties(ignoreUnknown = true)
	private String replacement;

	public void setRegexp(String regexp){
		this.regexp = regexp;
	}

	public String getRegexp(){
		return regexp;
	}

	public void setEnable(boolean enable){
		this.enable = enable;
	}

	public boolean isEnable(){
		return enable;
	}

	public void setReplacement(String replacement){
		this.replacement = replacement;
	}

	public String getReplacement(){
		return replacement;
	}

	@Override
 	public String toString(){
		return 
			"RewritePath{" + 
			"regexp = '" + regexp + '\'' + 
			",enable = '" + enable + '\'' + 
			",replacement = '" + replacement + '\'' + 
			"}";
		}
}