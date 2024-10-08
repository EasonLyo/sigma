package net.oooops.sigma.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MetadataItem{

	@JsonProperty("name")
	private String name;

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	@Override
 	public String toString(){
		return 
			"MetadataItem{" + 
			"name = '" + name + '\'' + 
			"}";
		}
}