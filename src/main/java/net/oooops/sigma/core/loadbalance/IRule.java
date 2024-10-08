package net.oooops.sigma.core.loadbalance;

/**
 * Interface that defines a "Rule" for a LoadBalancer. A Rule can be thought of
 * as a Strategy for loadbalacing. Well known loadbalancing strategies include
 * Round Robin, Response Time based etc.
 *
 */
public interface IRule {
  /*
   * choose one alive server from lb.allServers or
   * lb.upServers according to key
   *
   * @return choosen Server object. NULL is returned if none
   *  server is available
   */

  Server choose(Object key);

  void setLoadBalancer(ILoadBalancer lb);

  ILoadBalancer getLoadBalancer();
}
