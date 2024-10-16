package net.oooops.sigma.core.option;

import io.netty.handler.codec.compression.StandardCompressionOptions;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpServerOptions;
import net.oooops.sigma.config.SigmaConfiguration;
import net.oooops.sigma.config.Ssl;

public class SigmaOptionHelper extends HttpClientOptions {

    public static HttpClientOptions createHttpClientOptions(SigmaConfiguration config) {

        return new HttpClientOptions()
                // this actually creates a lease to the client
                // when the verticle is undeployed, the lease will be released automaticaly
                .setShared(true)
                .setName("__vertx.proxy.client")
                .setReusePort(true)
                .setTcpQuickAck(true)
                .setTcpCork(true)
                .setTcpFastOpen(true)
                .setSsl(config.getProxyClient().isSsl())
                .setPoolCleanerPeriod(1000 * 60)
                // the maximum number of opened per HTTP/1.x server (5 by default), The pool creates up to 5 connections per server.
                .setMaxPoolSize(config.getProxyClient().getMaxPoolSizePerServer());
    }

    /**
     * Native on Linux gives you extra networking options:
     * SO_REUSEPORT
     * TCP_QUICKACK
     * TCP_CORK
     * TCP_FASTOPEN
     * TCP_USER_TIMEOUT
     *
     * @param config sigma config
     * @return HttpServerOptions
     */
    public static HttpServerOptions createHttpServerOptions(SigmaConfiguration config) {
        HttpServerOptions httpServerOptions = new HttpServerOptions()
                .setTcpFastOpen(true)
                .setTcpCork(true)
                .setTcpQuickAck(true)
                .setReusePort(true);

        if (config.getProxyServer().getHttp().getPlugin() != null
                && config.getProxyServer().getHttp().getPlugin().getGzip() != null
                && config.getProxyServer().getHttp().getPlugin().getGzip().isEnable()) {
            httpServerOptions.setCompressionSupported(true);
            Integer compressionLevel = config.getProxyServer().getHttp().getPlugin().getGzip().getCompressionLevel();
            Integer windowBits = config.getProxyServer().getHttp().getPlugin().getGzip().getWindowBits();
            Integer memLevel = config.getProxyServer().getHttp().getPlugin().getGzip().getMemLevel();
            if (compressionLevel != null && windowBits != null && memLevel != null) {
                httpServerOptions
                        .addCompressor(StandardCompressionOptions.gzip(compressionLevel, windowBits, memLevel))
                        .addCompressor(StandardCompressionOptions.deflate(compressionLevel, windowBits, memLevel));
            }
        }
        return httpServerOptions;
    }

    public static HttpServerOptions createHttpsServerOptions(SigmaConfiguration config) {
        HttpServerOptions httpServerOptions = new HttpServerOptions()
                .setTcpFastOpen(true)
                .setTcpCork(true)
                .setTcpQuickAck(true)
                .setReusePort(true);
        Ssl ssl = config.getProxyServer().getHttps().getSsl();
        if (ssl == null || ssl.getType() == null || ssl.getKeyPath() == null || ssl.getCertPath() == null) {
            throw new IllegalArgumentException("SSL config error, must config SSL, SSL type, SSL KeyPath and SSL certPath!");
        }

        if (config.getProxyServer().getHttps().getPlugin().getGzip() !=null && config.getProxyServer().getHttps().getPlugin().getGzip().isEnable()) {
            httpServerOptions.setCompressionSupported(true);
            Integer compressionLevel = config.getProxyServer().getHttps().getPlugin().getGzip().getCompressionLevel();
            Integer windowBits = config.getProxyServer().getHttps().getPlugin().getGzip().getWindowBits();
            Integer memLevel = config.getProxyServer().getHttps().getPlugin().getGzip().getMemLevel();
            if (compressionLevel != null && windowBits != null && memLevel != null) {
                httpServerOptions
                        .addCompressor(StandardCompressionOptions.gzip(compressionLevel, windowBits, memLevel))
                        .addCompressor(StandardCompressionOptions.deflate(compressionLevel, windowBits, memLevel));
            }
        }
        return httpServerOptions;
    }
}
