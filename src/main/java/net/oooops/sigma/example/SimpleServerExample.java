package net.oooops.sigma.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.impl.MimeMapping;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleServerExample extends AbstractVerticle {

    public static final Logger LOG = LoggerFactory.getLogger(SimpleServerExample.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        Long port = config().getLong("port");
        if (port == null) {
            port = 8888L;
        }
        HttpServerOptions serverOptions = new HttpServerOptions();
        serverOptions.setTcpFastOpen(true)
                .setTcpCork(true)
                .setTcpQuickAck(true)
                .setReusePort(true);
        vertx.createHttpServer(serverOptions)
                .requestHandler(req -> {
                    JsonObject json = new JsonObject();
                    json.put("code", 200);
                    json.put("msg", "success");
                    json.put("data", null);
                    req.response()
                            .putHeader("content-type", MimeMapping.getMimeTypeForExtension("json"))
                            .end(json.encode());
                }).listen(port.intValue(), http -> {
                    if (http.succeeded()) {
                        startPromise.complete();
                        LOG.info("HTTP server started on port 8888");
                    } else {
                        startPromise.fail(http.cause());
                    }
                });
    }
}
