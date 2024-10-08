package net.oooops.sigma.core.platform.impl;

import net.oooops.sigma.SigmaVersion;
import io.vertx.core.MultiMap;
import io.vertx.ext.web.RoutingContext;
import net.oooops.sigma.core.platform.ServerVersionHandler;

import static io.vertx.core.http.HttpHeaders.SERVER;

public class ServerVersionHandlerImpl implements ServerVersionHandler {


  @Override
  public void handle(RoutingContext event) {
    event.addHeadersEndHandler(v -> {
      MultiMap headers = event.response().headers();
      if (headers.contains(SERVER)) {
        return;
      }
      event.response().putHeader(SERVER, "SIGMA/" + SigmaVersion.getVersion());
    });
    event.next();
  }
}
