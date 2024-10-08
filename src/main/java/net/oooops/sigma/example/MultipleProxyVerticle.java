package net.oooops.sigma.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.proxy.handler.ProxyHandler;
import io.vertx.httpproxy.HttpProxy;

public class MultipleProxyVerticle extends AbstractVerticle {

  // 代理6666和6667两个服务,并反向代理
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    // 创建代理客户端
    HttpClient proxyClient = vertx.createHttpClient();
    // 创建代理服务
    HttpServer httpProxyServer = vertx.createHttpServer();
    // 设置客户端为代理
    HttpProxy httpProxy = HttpProxy.reverseProxy(proxyClient).origin(6666,"localhost");
    HttpProxy httpProxy2 = HttpProxy.reverseProxy(proxyClient).origin(6667,"localhost");
    // 创建代理路由规则
    Router router = Router.router(vertx);
    router.route(HttpMethod.GET, "/1").handler(ctx -> {
      HttpServerRequest request = ctx.request();
      request.path();
    }).handler(ProxyHandler.create(httpProxy));
    router.route(HttpMethod.GET, "/2").handler(ProxyHandler.create(httpProxy2));

    httpProxyServer.requestHandler(router).listen(7070);
  }
}
