package net.oooops.sigma.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Directive {

	@JsonProperty("name")
	private String name;

	@JsonProperty("value")
	private String value;

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setValue(String value){
		this.value = value;
	}

	public String getValue(){
		return value;
	}

}