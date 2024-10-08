package net.oooops.sigma.core.plugin;

import io.vertx.core.Future;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.httpproxy.ProxyContext;
import io.vertx.httpproxy.ProxyInterceptor;
import io.vertx.httpproxy.ProxyResponse;

/**
 * 在进行请求转发的时候,进行请求改写的拦截器,如果后续有优先级的,需要指定拦截器列表添加顺序,参考:
 * <a href="io.vertx.httpproxy.impl.ReverseProxy#addInterceptor(io.vertx.httpproxy.ProxyInterceptor)">addInterceptor()</a>
 * <a href="io.vertx.httpproxy.impl.ReverseProxy.Proxy#sendRequest()">sendRequest()</a>
 *
 */
public class PathRewriteProxyInterceptor implements ProxyInterceptor {

  private final String regexp;
  private final String replacement;

  public PathRewriteProxyInterceptor(String regexp, String replacement) {
    this.regexp = regexp;
    this.replacement = replacement;
  }

  @Override
  public Future<ProxyResponse> handleProxyRequest(ProxyContext context) {
    HttpServerRequest proxiedRequest = context.request().proxiedRequest();
    String path = proxiedRequest.path();
    String newPath = path.replaceAll(regexp, replacement.replace("$\\", "$"));
    context.request().setURI(newPath);
    return ProxyInterceptor.super.handleProxyRequest(context);
  }

  @Override
  public Future<Void> handleProxyResponse(ProxyContext context) {
    return ProxyInterceptor.super.handleProxyResponse(context);
  }
}
