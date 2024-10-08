package net.oooops.sigma.core;

import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.ErrorHandler;

public class SigmaErrorhandler {

    public static void init(Router router, ErrorHandler errorHandler) {
        router.errorHandler(400, errorHandler);
        router.errorHandler(401, errorHandler);
        router.errorHandler(402, errorHandler);
        router.errorHandler(403, errorHandler);
        router.errorHandler(404, errorHandler);
        router.errorHandler(405, errorHandler);
        router.errorHandler(406, errorHandler);
        router.errorHandler(407, errorHandler);
        router.errorHandler(408, errorHandler);
        router.errorHandler(409, errorHandler);
        router.errorHandler(410, errorHandler);
        router.errorHandler(411, errorHandler);
        router.errorHandler(412, errorHandler);
        router.errorHandler(413, errorHandler);
        router.errorHandler(414, errorHandler);
        router.errorHandler(415, errorHandler);
        router.errorHandler(416, errorHandler);
        router.errorHandler(417, errorHandler);
        router.errorHandler(421, errorHandler);
        router.errorHandler(422, errorHandler);
        router.errorHandler(423, errorHandler);
        router.errorHandler(424, errorHandler);
        router.errorHandler(425, errorHandler);
        router.errorHandler(426, errorHandler);
        router.errorHandler(500, errorHandler);
        router.errorHandler(501, errorHandler);
        router.errorHandler(502, errorHandler);
        router.errorHandler(503, errorHandler);
        router.errorHandler(504, errorHandler);
        router.errorHandler(505, errorHandler);
        router.errorHandler(506, errorHandler);
        router.errorHandler(507, errorHandler);
        router.errorHandler(510, errorHandler);
        router.errorHandler(511, errorHandler);
    }

    public static void putErrorHandler(Router router, int statusCode, ErrorHandler errorHandler) {
        router.errorHandler(statusCode, errorHandler);
    }

}
