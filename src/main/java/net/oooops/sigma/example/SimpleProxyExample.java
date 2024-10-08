package net.oooops.sigma.example;

import io.vertx.core.*;
import io.vertx.core.http.*;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.proxy.handler.ProxyHandler;
import io.vertx.httpproxy.HttpProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleProxyExample extends AbstractVerticle {

    public static final Logger LOG = LoggerFactory.getLogger(SimpleProxyExample.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        HttpClientOptions proxyClientOptions = new HttpClientOptions()
                .setReusePort(true)
                .setTcpQuickAck(true)
                .setTcpCork(true)
                .setTcpFastOpen(true)
                .setKeepAlive(true)
                // .setLogActivity(true)
                .setMaxPoolSize(2048);
        // 创建代理客户端
        HttpClient proxyClient = vertx.createHttpClient(proxyClientOptions);
        // 初始化单个代理类
        HttpProxy httpProxy = HttpProxy.reverseProxy(proxyClient).origin(8888, "localhost");
        Router router = Router.router(vertx);
        Route rootRoute = router.route();
        rootRoute.handler(ProxyHandler.create(httpProxy));
        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8081, event -> {
                    if (event.succeeded()) {
                        LOG.info("Proxy server is deploy success!");
                        startPromise.complete();
                    }
                });
    }
}
