package net.oooops.sigma.core.loadbalance;

import io.netty.util.internal.StringUtil;

import java.util.List;
import java.util.UUID;

/**
 * A LoadBalancer that has the capabilities to obtain the candidate list of servers using a static source.
 */
public class StaticUpstreamLoadBalancer implements ILoadBalancer {
  private final List<Server> staticServerList;
  private IRule rule;
  private String upstreamId;

  public StaticUpstreamLoadBalancer(List<Server> staticServerList, IRule rule, String serverName) {
    this.staticServerList = staticServerList;
    this.rule = rule;
    this.upstreamId = serverName;
    check();
  }

  private void check() {
    if (this.rule == null) {
      this.rule = new RoundRobinRule();
    }
    if (this.rule.getLoadBalancer() != this) {
      this.rule.setLoadBalancer(this);
    }
    if (StringUtil.isNullOrEmpty(upstreamId)) {
      this.upstreamId = UUID.randomUUID().toString();
    }
  }

  public String getUpstreamId() {
    return upstreamId;
  }

  @Override
  @Deprecated
  public void addServers(List<Server> newServers) {}

  @Override
  @Deprecated
  public Server chooseServer(Object key) {
    return this.rule.choose(key);
  }

  @Override
  @Deprecated
  public void markServerDown(Server server) {}

  @Override
  public List<Server> getReachableServers() {
    return staticServerList;
  }

  @Override
  public List<Server> getAllServers() {
    return staticServerList;
  }
}
