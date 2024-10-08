package net.oooops.sigma.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

import java.util.List;

public class Csp{

	@JsonProperty("enable")
	private boolean enable;

	@JsonProperty("directive")
	@JsonIgnoreProperties(ignoreUnknown = true)
	private List<Directive> directive = Lists.newArrayList();

	@JsonProperty("report-only")
	private boolean reportOnly;

	public void setEnable(boolean enable){
		this.enable = enable;
	}

	public boolean isEnable(){
		return enable;
	}

	public boolean isReportOnly() {
		return reportOnly;
	}

	public void setReportOnly(boolean reportOnly) {
		this.reportOnly = reportOnly;
	}

	public void setDirective(List<Directive> directive){
		this.directive = directive;
	}

	public List<Directive> getDirective(){
		return directive;
	}
}