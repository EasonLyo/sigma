package net.oooops.sigma.core.platform.impl;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.MIMEHeader;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.ErrorHandler;
import io.vertx.ext.web.handler.impl.ErrorHandlerImpl;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

public class SigmaErrorHandlerImpl implements ErrorHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ErrorHandlerImpl.class);

    /**
     * Flag to enable/disable printing the full stack trace of exceptions.
     */
    private final boolean displayExceptionDetails;

    /**
     * Cached template for rendering the html errors
     */
    private final String errorTemplate;

    public SigmaErrorHandlerImpl(Vertx vertx, String errorTemplateName, boolean displayExceptionDetails) {
        Objects.requireNonNull(errorTemplateName);
        this.errorTemplate = vertx.fileSystem()
                .readFileBlocking(errorTemplateName)
                .toString(StandardCharsets.UTF_8);
        this.displayExceptionDetails = displayExceptionDetails;
    }

    @Override
    public void handle(RoutingContext context) {

        HttpServerResponse response = context.response();

        Throwable failure = context.failure();

        if (response.headWritten()) {
            // response is already being processed, so we can't really
            // format the error as a "pretty print" message
            if (LOG.isWarnEnabled()) {
                LOG.warn("Response headers are already written", failure);
            }

            try {
                // force a close of the socket to
                // avoid dangling connections
                response.close();
            } catch (RuntimeException e) {
                // ignore
            }
            return;
        }

        int errorCode = context.statusCode();

        // force default error code
        if (errorCode == -1) {
            errorCode = 500;
        }

        response
                .setStatusCode(errorCode);

        answerWithError(context, errorCode);
    }

    private void answerWithError(RoutingContext context, int errorCode) {
        if (!sendErrorResponseMIME(context, errorCode) && !sendErrorAcceptMIME(context, errorCode)) {
            // fallback plain/text
            sendError(context, "text/plain", errorCode);
        }
    }

    private boolean sendErrorResponseMIME(RoutingContext context, int errorCode) {
        // does the response already set the mime type?
        String mime = context.response().headers().get(HttpHeaders.CONTENT_TYPE);

        if (mime == null) {
            // does the route have an acceptable content type?
            mime = context.getAcceptableContentType();
        }

        return mime != null && sendError(context, mime, errorCode);
    }

    private boolean sendErrorAcceptMIME(RoutingContext context, int errorCode) {
        // respect the client accept order
        List<MIMEHeader> acceptableMimes = context.parsedHeaders().accept();

        for (MIMEHeader accept : acceptableMimes) {
            if (sendError(context, accept.value(), errorCode)) {
                return true;
            }
        }
        return false;
    }

    private boolean sendError(RoutingContext context, String mime, int errorCode) {

        final String title = "An unexpected error occurred";

        final HttpServerResponse response = context.response();
        final Throwable exception = context.failure();

        final String errorMessage;

        if (displayExceptionDetails) {
            if (exception == null) {
                errorMessage = response.getStatusMessage();
            } else {
                errorMessage = exception.getMessage();
            }
        } else {
            errorMessage = response.getStatusMessage();
        }

        if (mime.startsWith("text/html")) {
            StringBuilder stack = null;
            if (exception != null && displayExceptionDetails) {
                stack = new StringBuilder();
                for (StackTraceElement elem : exception.getStackTrace()) {
                    stack
                            .append("<li>")
                            .append(escapeHTML(elem.toString()))
                            .append("</li>");
                }
            }
            response.putHeader(HttpHeaders.CONTENT_TYPE, "text/html");
            response.end(
                    errorTemplate
                            .replace("{title}", title)
                            .replace("{errorCode}", Integer.toString(errorCode))
                            .replace("{errorMessage}", htmlFormat(errorMessage))
                            .replace("{stackTrace}", stack == null ? "" : stack.toString())
            );
            return true;
        }

        if (mime.startsWith("application/json")) {
            JsonObject jsonError = new JsonObject();
            jsonError.put("code", errorCode).put("message", errorMessage);
            if (exception != null && displayExceptionDetails) {
                JsonArray stack = new JsonArray();
                for (StackTraceElement elem : exception.getStackTrace()) {
                    stack.add(elem.toString());
                }
                jsonError.put("stack", stack);
            }
            response.putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            response.end(jsonError.encode());
            return true;
        }

        if (mime.startsWith("text/plain")) {
            response.putHeader(HttpHeaders.CONTENT_TYPE, "text/plain");
            StringBuilder sb = new StringBuilder();
            sb.append("Error ");
            sb.append(errorCode);
            sb.append(": ");
            sb.append(errorMessage);
            if (exception != null && displayExceptionDetails) {
                for (StackTraceElement elem : exception.getStackTrace()) {
                    sb.append("\tat ").append(elem).append("\n");
                }
            }
            response.end(sb.toString());
            return true;
        }

        return false;
    }

    /**
     * Very incomplete html escape that will escape the most common characters on error messages.
     * This is to avoid pulling a full dependency to perform a compliant escape. Error messages
     * are created by developers as such that they should not be to complex for logging.
     */
    private static String escapeHTML(String s) {
        StringBuilder out = new StringBuilder(Math.max(16, s.length()));
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c > 127 || c == '"' || c == '\'' || c == '<' || c == '>' || c == '&') {
                out.append("&#");
                out.append((int) c);
                out.append(';');
            } else {
                out.append(c);
            }
        }
        return out.toString();
    }

    private static String htmlFormat(String errorMessage) {
        if (errorMessage == null) {
            return "";
        }

        // step #1 (escape html entities)
        String escaped = escapeHTML(errorMessage);
        // step #2 (replace line endings with breaks)
        return escaped.replaceAll("\\r?\\n", "<br>");
    }
}
