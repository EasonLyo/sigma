package net.oooops.sigma.core.platform.impl;

import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpVersion;
import io.vertx.core.net.SocketAddress;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.LoggerFormatter;
import io.vertx.ext.web.impl.Utils;

import static com.google.common.net.HttpHeaders.X_FORWARDED_FOR;

public class SigmaHTTPLoggerFormatter implements LoggerFormatter {

    @Override
    public String format(RoutingContext routingContext, long requestTime) {
        long timestamp = System.currentTimeMillis();

        SocketAddress socketAddress = routingContext.request().remoteAddress();

        String remoteAddr = socketAddress == null ? "-" : socketAddress.host();
        String remotePort = socketAddress == null ? "" : STR.":\{socketAddress.port()}";

        HttpMethod method = routingContext.request().method();
        String uri = routingContext.request().uri();
        HttpVersion version = routingContext.request().version();

        // immediate = false
        long contentLength  = routingContext.request().response().bytesWritten();

        String versionFormatted = switch (version) {
            case HTTP_1_0 -> "HTTP/1.0";
            case HTTP_1_1 -> "HTTP/1.1";
            case HTTP_2 -> "HTTP/2.0";
        };

        final MultiMap headers = routingContext.request().headers();
        int status = routingContext.request().response().getStatusCode();

        String referrer = headers.contains("referrer") ? headers.get("referrer") : headers.get(HttpHeaders.REFERER);
        String userAgent = routingContext.request().headers().get(HttpHeaders.USER_AGENT);
        referrer = referrer == null ? "-" : referrer;
        userAgent = userAgent == null ? "-" : userAgent;

        String xForwardFor = routingContext.request().getHeader(X_FORWARDED_FOR) == null ? "-" : routingContext.request().getHeader(X_FORWARDED_FOR);

        String message = STR."""
                \{remoteAddr}\{remotePort} - [\{Utils.formatRFC1123DateTime(timestamp)}] "\{method.name()} \{uri} \{versionFormatted}" \{status} "\{requestTime}ms" \{contentLength} "\{userAgent}" "\{referrer}" "\{xForwardFor}"
                """;
        return message;

    }
}
