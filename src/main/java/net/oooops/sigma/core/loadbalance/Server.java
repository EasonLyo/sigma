package net.oooops.sigma.core.loadbalance;


import org.graalvm.collections.Pair;

import java.io.Serializable;

public class Server implements Serializable {

  /**
   * Additional meta information of a server, which contains
   * information of the targeting application, as well as server identification
   * specific for a deployment environment, for example, AWS.
   */
  public interface MetaInfo {
    /**
     * @return the name of application that runs on this server, null if not available
     */
    String getAppName();

    /**
     * @return the group of the server, for example, auto scaling group ID in AWS.
     * Null if not available
     */
    String getServerGroup();

    /**
     * @return A virtual address used by the server to register with discovery service.
     * Null if not available
     */
    String getServiceIdForDiscovery();

    /**
     * @return ID of the server
     */
    String getInstanceId();
  }

  public static final String UNKNOWN_ZONE = "UNKNOWN";
  private String host;
  private int port = 80;
  private String scheme;
  private volatile String id;
  private volatile boolean isAliveFlag;
  private String zone = UNKNOWN_ZONE;
  private volatile boolean readyToServe = true;

  private final MetaInfo simpleMetaInfo = new MetaInfo() {
    @Override
    public String getAppName() {
      return null;
    }

    @Override
    public String getServerGroup() {
      return null;
    }

    @Override
    public String getServiceIdForDiscovery() {
      return null;
    }

    @Override
    public String getInstanceId() {
      return id;
    }
  };

  public Server(String host, int port) {
    this(null, host, port);
  }

  public Server(String scheme, String host, int port) {
    this.scheme = scheme;
    this.host = host;
    this.port = port;
    this.id = host + ":" + port;
    isAliveFlag = false;
  }

  /* host:port combination */
  public Server(String id) {
    setId(id);
    isAliveFlag = false;
  }

  // No reason to synchronize this, I believe.
  // The assignment should be atomic, and two setAlive calls
  // with conflicting results will still give nonsense(last one wins)
  // synchronization or no.

  public void setAlive(boolean isAliveFlag) {
    this.isAliveFlag = isAliveFlag;
  }

  public boolean isAlive() {
    return isAliveFlag;
  }

  @Deprecated
  public void setHostPort(String hostPort) {
    setId(hostPort);
  }

  static public String normalizeId(String id) {
    Pair<String, Integer> hostPort = getHostPort(id);
    if (hostPort == null) {
      return null;
    } else {
      return hostPort.getLeft() + ":" + hostPort.getRight();
    }
  }

  private static String getScheme(String id) {
    if (id != null) {
      if (id.toLowerCase().startsWith("http://")) {
        return "http";
      } else if (id.toLowerCase().startsWith("https://")) {
        return "https";
      }
    }
    return null;
  }

  static Pair<String, Integer> getHostPort(String id) {
    if (id != null) {
      String host = null;
      int port = 80;

      if (id.toLowerCase().startsWith("http://")) {
        id = id.substring(7);
      } else if (id.toLowerCase().startsWith("https://")) {
        id = id.substring(8);
        port = 443;
      }

      if (id.contains("/")) {
        int slash_idx = id.indexOf("/");
        id = id.substring(0, slash_idx);
      }

      int colon_idx = id.indexOf(':');

      if (colon_idx == -1) {
        host = id; // default
      } else {
        host = id.substring(0, colon_idx);
        try {
          port = Integer.parseInt(id.substring(colon_idx + 1));
        } catch (NumberFormatException e) {
          throw e;
        }
      }
      return Pair.create(host, port);
    } else {
      return null;
    }

  }

  public void setId(String id) {
    Pair<String, Integer> hostPort = getHostPort(id);
    if (hostPort != null) {
      this.id = hostPort.getLeft() + ":" + hostPort.getRight();
      this.host = hostPort.getLeft();
      this.port = hostPort.getRight();
      this.scheme = getScheme(id);
    } else {
      this.id = null;
    }
  }

  public void setScheme(String scheme) {
    this.scheme = scheme;
  }

  public void setPort(int port) {
    this.port = port;

    if (host != null) {
      id = host + ":" + port;
    }
  }

  public void setHost(String host) {
    if (host != null) {
      this.host = host;
      id = host + ":" + port;
    }
  }

  public String getId() {
    return id;
  }

  public String getHost() {
    return host;
  }

  public int getPort() {
    return port;
  }

  public String getScheme() {
    return scheme;
  }

  public String getHostPort() {
    return host + ":" + port;
  }

  public MetaInfo getMetaInfo() {
    return simpleMetaInfo;
  }

  public String toString() {
    return this.getId();
  }

  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!(obj instanceof Server))
      return false;
    Server svc = (Server) obj;
    return svc.getId().equals(this.getId());

  }

  public int hashCode() {
    int hash = 7;
    hash = 31 * hash + (null == this.getId() ? 0 : this.getId().hashCode());
    return hash;
  }

  public final String getZone() {
    return zone;
  }

  public final void setZone(String zone) {
    this.zone = zone;
  }

  public final boolean isReadyToServe() {
    return readyToServe;
  }

  public final void setReadyToServe(boolean readyToServe) {
    this.readyToServe = readyToServe;
  }
}
