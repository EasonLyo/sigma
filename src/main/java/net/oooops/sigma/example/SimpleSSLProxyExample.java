package net.oooops.sigma.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.net.PemKeyCertOptions;

public class SimpleSSLProxyExample extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    HttpServerOptions serverOptions = new HttpServerOptions()
      .setSsl(true).setKeyCertOptions(
        new PemKeyCertOptions().
          setKeyPath("cart/localhost+2-key.pem").
          setCertPath("cart/localhost+2.pem")
      );
    vertx.createHttpServer(serverOptions).requestHandler(request -> {
      HttpServerResponse response = request.response();
      response.putHeader("content-type", "text/plain");
      // Write to the response and end it
      response.end("Hello World!");
    }).listen(443);
  }
}
