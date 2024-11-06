package net.oooops.sigma;

import com.google.common.collect.Maps;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.*;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.net.SocketAddress;
import io.vertx.core.shareddata.Counter;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.*;
import io.vertx.ext.web.proxy.handler.ProxyHandler;
import io.vertx.httpproxy.HttpProxy;
import net.oooops.sigma.config.*;
import net.oooops.sigma.core.SigmaErrorhandler;
import net.oooops.sigma.core.loadbalance.ILoadBalancer;
import net.oooops.sigma.core.loadbalance.RoundRobinRule;
import net.oooops.sigma.core.loadbalance.Server;
import net.oooops.sigma.core.loadbalance.StaticUpstreamLoadBalancer;
import net.oooops.sigma.core.option.SigmaOptionHelper;
import net.oooops.sigma.core.platform.ServerVersionHandler;
import net.oooops.sigma.core.platform.impl.SigmaErrorHandlerImpl;
import net.oooops.sigma.core.platform.impl.AccessLoggerFormatter;
import net.oooops.sigma.core.plugin.PathRewriteProxyInterceptor;
import net.oooops.sigma.core.plugin.HttpRedirectHttpsHandler;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentMap;

public class Sigma extends AbstractVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(Sigma.class);

    private static final ConcurrentMap<String, Router> routerList = Maps.newConcurrentMap();

    private static final ConcurrentMap<String, HttpProxy> proxyList = Maps.newConcurrentMap();

    private static final ConcurrentMap<String, ILoadBalancer> upstreamList = Maps.newConcurrentMap();


    @Override
    public void start(Promise<Void> startPromise) {
        // 获取配置文件
        SigmaConfiguration config = config().mapTo(SigmaConfiguration.class);
        // 初始化代理客户端
        HttpClient proxyClient = vertx.createHttpClient(SigmaOptionHelper.createHttpClientOptions(config));
        // 初始化服务器配置
        HttpServerOptions httpServerOptions = SigmaOptionHelper.createHttpServerOptions(config);
        HttpServerOptions httpsServerOptions = SigmaOptionHelper.createHttpServerOptions(config);
        // 初始化后端服务列表
        config.getUpstream().forEach(upstream -> {
            // 获取应用服务列表
            List<Server> list = upstream.getNodes().stream().map(Server::new).toList();
            // 创建负载均衡器
            // TODO 支持多种负载均衡策略
            StaticUpstreamLoadBalancer staticUpstreamLoadBalancer = new StaticUpstreamLoadBalancer(list, new RoundRobinRule(), upstream.getId());
            upstreamList.put(upstream.getId(), staticUpstreamLoadBalancer);
        });

        // 初始化代理规则
        config.getProxyPass().forEach(proxyPassItem -> {
            // 创建代理
            HttpProxy proxy = HttpProxy.reverseProxy(proxyClient);
            // 此处进行请求改写
            if (proxyPassItem.getPlugin().getRewritePath() != null && proxyPassItem.getPlugin().getRewritePath().isEnable()) {
                String regexp = proxyPassItem.getPlugin().getRewritePath().getRegexp();
                String replacement = proxyPassItem.getPlugin().getRewritePath().getReplacement();
                proxy.addInterceptor(new PathRewriteProxyInterceptor(regexp, replacement));
            }
            // 获取负载均衡器
            ILoadBalancer loadBalancer = upstreamList.get(proxyPassItem.getUpstreamId());
            // 负载均衡
            proxy.originSelector(request -> Future.succeededFuture(resolveOriginAddress(request, loadBalancer, vertx.sharedData().getLocalCounter(proxyPassItem.getId()).compose(Counter::getAndIncrement).result())));
            proxyList.put(proxyPassItem.getId(), proxy);
        });

        // 初始化路由
        // todo use radixTree to match uri.
        for (RouterItem routerItem : config.getRouter()) {
            if (routerItem.isEnable()) {
                Router router = Router.router(vertx);
                Route rootRoute = router.route();
                rootRoute.handler(ServerVersionHandler.create()); // 创建版本号信息
                SigmaErrorHandlerImpl errorHandler = new SigmaErrorHandlerImpl(vertx, "templates/web-error.html", false);
                SigmaErrorhandler.init(router, errorHandler);
                // 路由配置
                if (config.getPlugins() != null && config.getPlugins().isLogConfig()) {
                    // todo 配置日志文件位置
                    LoggerHandler loggerHandler = LoggerHandler.create(LoggerFormat.CUSTOM);
                    loggerHandler.customFormatter(new AccessLoggerFormatter());
                    rootRoute.handler(loggerHandler); // 日志打印
                }
                if (routerItem.getAllowForward() != null) {
                    router.allowForward(routerItem.getAllowForward());
                }
                if (routerItem.getVirtualHost() != null) {
                    rootRoute.virtualHost(routerItem.getVirtualHost());
                }
                if (routerItem.getPlugin().getXFrame() != null && !routerItem.getPlugin().getXFrame().isEmpty()) {
                    switch (routerItem.getPlugin().getXFrame().toUpperCase(Locale.ROOT)) {
                        case "SAMEORIGIN" -> rootRoute.handler(XFrameHandler.create(XFrameHandler.SAMEORIGIN));
                        case "DENY" -> rootRoute.handler(XFrameHandler.create(XFrameHandler.DENY));
                        case "NONE" -> LOG.info(STR."The x-frame plugin in router \{routerItem.getId()} is unavailable .");
                        default -> LOG.warn(STR."the plugin config about x-frame is Illegal, router id :\{routerItem.getId()}");
                    }
                }
                if (routerItem.getPlugin().getCors() != null && routerItem.getPlugin().getCors().isEnable()) {
                    Cors cors = routerItem.getPlugin().getCors();
                    CorsHandler corsHandler = CorsHandler.create();
                    corsHandler.addOrigins(cors.getAllowOrigins());
                    cors.getAllowMethods().forEach(corsHandler::allowedMethod);
                    corsHandler.allowCredentials(cors.isAllowCredentials());
                    rootRoute.handler(corsHandler);
                }
                if (routerItem.getPlugin().getHsts() != null && routerItem.getPlugin().getHsts().isEnable()) {
                    Hsts hsts = routerItem.getPlugin().getHsts();
                    HSTSHandler hstsHandler = HSTSHandler.create(hsts.getMaxAge(), hsts.isIncludeSubDomains());
                    rootRoute.handler(hstsHandler);
                }
                if (routerItem.getPlugin().getCsp() != null && routerItem.getPlugin().getCsp().isEnable()) {
                    Csp csp = routerItem.getPlugin().getCsp();
                    CSPHandler cspHandler = CSPHandler.create();
                    csp.getDirective().forEach(directive -> cspHandler.addDirective(directive.getName(), directive.getValue()));
                    cspHandler.setReportOnly(csp.isReportOnly());
                    rootRoute.handler(cspHandler);
                }
                for (RouteItem routeItem : routerItem.getRoute()) {
                    if (routeItem.isEnable()) {
                        Route route = router.route(routeItem.getPath());
                        if (!routeItem.getAllowMethod().isEmpty()) {
                            List<HttpMethod> all = HttpMethod.values();
                            routeItem.getAllowMethod().forEach(item -> {
                                if (all.stream().anyMatch(method -> method.name().equals(item.toUpperCase(Locale.ROOT)))) {
                                    HttpMethod method = HttpMethod.valueOf(item.toUpperCase(Locale.ROOT));
                                    route.method(method);
                                }
                            });
                        }
                        if (!routeItem.getConsumes().isEmpty()) {
                            routeItem.getConsumes().forEach(route::consumes);
                        }
                        if (!routeItem.getProduces().isEmpty()) {
                            routeItem.getProduces().forEach(route::produces);
                        }
                        HttpProxy httpProxy = proxyList.get(routeItem.getProxyPassId());
                        route.handler(ProxyHandler.create(httpProxy));
                    }
                }
                routerList.put(routerItem.getProxyServerId(), router);
            }
        }
        // 创建网关服务器
        HttpServer httpServer = vertx.createHttpServer(httpServerOptions);
        if (config.getPlugins() != null && config.getPlugins().isHttpRedirectHttps()) {
            httpServer.requestHandler(new HttpRedirectHttpsHandler());
            LOG.warn("All http request will redirect to Https, all config of http server is unavailable.");
        } else {
            if (config.getProxyServer().getHttp().getPlugin().getStaticResources().isEnable()) {
                StaticResources staticResources = config.getProxyServer().getHttp().getPlugin().getStaticResources();
                StaticHandler staticHandler = StaticHandler.create(FileSystemAccess.ROOT, staticResources.getRootPath());
                staticHandler.setIncludeHidden(false);
                staticHandler.setAlwaysAsyncFS(true);
                // todo check http If-Modified-Since can be used
                routerList.get(config.getProxyServer().getHttp().getId()).get("/static/*").handler(staticHandler);
            }
            // 启动服务
            httpServer.requestHandler(routerList.get(config.getProxyServer().getHttp().getId()));
            httpServer.exceptionHandler(event -> LOG.error(event.getMessage()));
        }
        httpServer.listen(config.getProxyServer().getHttp().getPort(), server -> {
            if (server.succeeded()) {
                // TODO 合并服务启动Future,统一回调complete
                startPromise.complete();
                LOG.info("HTTP Proxy Server is start");
            } else {
                startPromise.fail(server.cause());
            }
        });

        // 创建HTTPS网关服务器
        if (config.getProxyServer().getHttps().isEnable()) {
            HttpServer httpsServer = vertx.createHttpServer(httpsServerOptions);
            if (config.getProxyServer().getHttps().getPlugin().getStaticResources().isEnable()) {
                StaticResources staticResources = config.getProxyServer().getHttp().getPlugin().getStaticResources();
                StaticHandler staticHandler = StaticHandler.create(FileSystemAccess.ROOT, staticResources.getRootPath());
                staticHandler.setIncludeHidden(false);
                staticHandler.setAlwaysAsyncFS(true);
                // todo check http If-Modified-Since can be used
                routerList.get(config.getProxyServer().getHttps().getId()).route("/static/*").handler(staticHandler);
            }
            // 启动服务
            httpsServer.requestHandler(routerList.get(config.getProxyServer().getHttps().getId()));
            httpsServer.exceptionHandler(event -> LOG.error(event.getMessage()));
            httpsServer.listen(config.getProxyServer().getHttps().getPort(), server -> {
                if (server.succeeded()) {
                    startPromise.complete();
                    LOG.info("HTTPs Proxy Server is start");
                } else {
                    startPromise.fail(server.cause());
                }
            });
        }
    }

    private SocketAddress resolveOriginAddress(HttpServerRequest httpServerRequest, ILoadBalancer loadBalancer, Object key) {
        Server server = loadBalancer.chooseServer(key);
        return SocketAddress.inetSocketAddress(server.getPort(), server.getHost());
    }
}
