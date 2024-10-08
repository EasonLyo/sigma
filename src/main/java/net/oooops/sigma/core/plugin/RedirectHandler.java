package net.oooops.sigma.core.plugin;

import com.google.common.base.Strings;
import io.netty.handler.codec.http.HttpScheme;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

public class RedirectHandler implements Handler<HttpServerRequest> {

    @Override
    public void handle(HttpServerRequest req) {
        String url = STR."""
                \{HttpScheme.HTTPS.toString()}://\{req.authority().toString()}\{req.uri()}""";
        HttpServerResponse response = req.response();
        // status
        int status = response.getStatusCode();

        if (status < 300 || status >= 400) {
            // if a custom code is in use that will be
            // respected
            response.setStatusCode(302);
        }

        response.putHeader(HttpHeaders.CONTENT_TYPE, "text/plain; charset=utf-8")
                .putHeader(HttpHeaders.LOCATION, url)
                .end(STR."Redirecting to \{url}.");
    }
}
