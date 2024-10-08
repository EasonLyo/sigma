package net.oooops.sigma.core.platform;

import io.vertx.ext.web.handler.PlatformHandler;
import net.oooops.sigma.core.platform.impl.ServerVersionHandlerImpl;


public interface ServerVersionHandler extends PlatformHandler {

  static ServerVersionHandler create() {
    return new ServerVersionHandlerImpl();
  }

}
