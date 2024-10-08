package net.oooops.sigma.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

import java.util.List;

public class Plugin{

	@JsonProperty("static-resources")
	@JsonIgnoreProperties(ignoreUnknown = true)
	private StaticResources staticResources;

	@JsonProperty("gzip")
	@JsonIgnoreProperties(ignoreUnknown = true)
	private Gzip gzip;

	@JsonProperty("csp")
	@JsonIgnoreProperties(ignoreUnknown = true)
	private Csp csp;

	@JsonProperty("metadata")
	@JsonIgnoreProperties(ignoreUnknown = true)
	private List<MetadataItem> metadata = Lists.newArrayList();

	@JsonProperty("cors")
	@JsonIgnoreProperties(ignoreUnknown = true)
	private Cors cors;

	@JsonProperty("log-config")
	@JsonIgnoreProperties(ignoreUnknown = true)
	private logConfig logConfig;

	@JsonProperty("hsts")
	@JsonIgnoreProperties(ignoreUnknown = true)
	private Hsts hsts;

	@JsonProperty("sub-router")
	@JsonIgnoreProperties(ignoreUnknown = true)
	private List<RouteItem> subRouter;

	@JsonProperty("https-degrade-http")
	private boolean httpsDegradeHttp;

	@JsonProperty("rewrite-path")
	@JsonIgnoreProperties(ignoreUnknown = true)
	private RewritePath rewritePath;

	@JsonProperty("x-frame")
	@JsonIgnoreProperties(ignoreUnknown = true)
	private String XFrame;

	public void setStaticResources(StaticResources staticResources){
		this.staticResources = staticResources;
	}

	public StaticResources getStaticResources(){
		return staticResources;
	}

	public void setGzip(Gzip gzip){
		this.gzip = gzip;
	}

	public Gzip getGzip(){
		return gzip;
	}

	public void setCsp(Csp csp){
		this.csp = csp;
	}

	public Csp getCsp(){
		return csp;
	}

	public void setMetadata(List<MetadataItem> metadata){
		this.metadata = metadata;
	}

	public List<MetadataItem> getMetadata(){
		return metadata;
	}

	public void setCors(Cors cors){
		this.cors = cors;
	}

	public Cors getCors(){
		return cors;
	}

	public void setLogConfig(logConfig logConfig){
		this.logConfig = logConfig;
	}

	public logConfig getLogConfig(){
		return logConfig;
	}

	public void setHsts(Hsts hsts){
		this.hsts = hsts;
	}

	public Hsts getHsts(){
		return hsts;
	}

	public void setSubRouter(List<RouteItem> subRouter){
		this.subRouter = subRouter;
	}

	public List<RouteItem> getSubRouter(){
		return subRouter;
	}

	public void setHttpsDegradeHttp(boolean httpsDegradeHttp){
		this.httpsDegradeHttp = httpsDegradeHttp;
	}

	public boolean isHttpsDegradeHttp(){
		return httpsDegradeHttp;
	}

	public void setRewritePath(RewritePath rewritePath){
		this.rewritePath = rewritePath;
	}

	public RewritePath getRewritePath(){
		return rewritePath;
	}

	public String getXFrame() {
		return XFrame;
	}

	public void setXFrame(String xFrame) {
		this.XFrame = xFrame;
	}
}