package net.oooops.sigma.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Gzip{

	@JsonProperty("enable")
	private boolean enable;

	@JsonProperty("compression-level")
	@JsonIgnoreProperties(ignoreUnknown = true)
	private int compressionLevel = 6;

	@JsonProperty("window-bits")
	@JsonIgnoreProperties(ignoreUnknown = true)
	private int windowBits = 15;

	@JsonProperty("mem-level")
	@JsonIgnoreProperties(ignoreUnknown = true)
	private int memLevel = 8;

	public void setMemLevel(Integer memLevel){
		this.memLevel = memLevel;
	}

	public Integer getMemLevel(){
		return memLevel;
	}

	public void setEnable(boolean enable){
		this.enable = enable;
	}

	public boolean isEnable(){
		return enable;
	}

	public void setCompressionLevel(Integer compressionLevel){
		this.compressionLevel = compressionLevel;
	}

	public Integer getCompressionLevel(){
		return compressionLevel;
	}

	public void setWindowBits(Integer windowBits){
		this.windowBits = windowBits;
	}

	public Integer getWindowBits(){
		return windowBits;
	}

	@Override
 	public String toString(){
		return 
			"Gzip{" + 
			"mem-level = '" + memLevel + '\'' + 
			",enable = '" + enable + '\'' + 
			",compression-level = '" + compressionLevel + '\'' + 
			",window-bits = '" + windowBits + '\'' + 
			"}";
		}
}