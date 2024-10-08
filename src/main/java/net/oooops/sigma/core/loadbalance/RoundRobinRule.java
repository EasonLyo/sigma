package net.oooops.sigma.core.loadbalance;


import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

import java.util.List;

public class RoundRobinRule implements IRule {

  private static final Logger LOG = LoggerFactory.getLogger(RoundRobinRule.class);
  private ILoadBalancer loadBalancer;


  public RoundRobinRule() {
  }

  public RoundRobinRule(ILoadBalancer loadBalancer) {
    this();
    setLoadBalancer(loadBalancer);
  }

  @Override
  public Server choose(Object key) {
    return choose(getLoadBalancer(), key);
  }

  private Server choose(ILoadBalancer lb, Object key) {
    if (!(key instanceof Long)) {
      LOG.error("param is not correct, it is null or not type of long");
      return null;
    }

    long count = (Long) key;
    if (lb == null) {
      LOG.warn("no load balancer");
      return null;
    }
    Server server; // define a return value but is null

    List<Server> reachableServers = lb.getReachableServers();
    List<Server> allServers = lb.getAllServers();

    int upCount = reachableServers.size();
    int serverCount = allServers.size();

    if ((upCount == 0) || (serverCount == 0)) {
      LOG.warn("No up servers available from load balancer: " + lb);
      return null;
    }
    // this value can be signed, before use it, we should unsigned it.
    int SignedCurrentIndex = (int) (count % serverCount);
    server = allServers.get(Math.abs(SignedCurrentIndex));
    return server;
  }

  @Override
  public void setLoadBalancer(ILoadBalancer lb) {
    this.loadBalancer = lb;
  }

  @Override
  public ILoadBalancer getLoadBalancer() {
    return this.loadBalancer;
  }
}
