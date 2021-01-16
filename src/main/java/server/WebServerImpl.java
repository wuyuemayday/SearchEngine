package server;

import com.sun.net.httpserver.*;
import handler.RequestHandler;
import handler.ResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class WebServerImpl implements WebServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebServerImpl.class);
    private static final String STATUS_ENDPOINT = "/health";
    private static final int DEFAULT_THREADPOOL = 8;

    private final int port;
    private final RequestHandler[] requestHandlers;
    private final ResponseHandler responseHandler;
    private HttpServer server;

    public WebServerImpl(
            final int port,
            final RequestHandler[] requestHandlers,
            final ResponseHandler responseHandler) {
        this.port = port;
        this.requestHandlers = requestHandlers;
        this.responseHandler = responseHandler;

        try {
            this.server = HttpServer.create(new InetSocketAddress(port), 0);
            this.server.setExecutor(Executors.newFixedThreadPool(DEFAULT_THREADPOOL));
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        this.initialize();

        LOGGER.info("Starting server on port {}", this.port);
        this.server.start();
    }

    @Override
    public void stop() {
        server.stop(30);
    }

    private void initialize() {
        server.createContext(STATUS_ENDPOINT).setHandler(this::handleHealthRequest);

        for (RequestHandler handler : this.requestHandlers) {
            server.createContext(handler.getEndpoint()).setHandler(createHttpHandler(handler, this.responseHandler));
        }
    }

    private HttpHandler createHttpHandler(final RequestHandler handler, final ResponseHandler responseHandler) {
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if (!exchange.getRequestMethod().equalsIgnoreCase(handler.getMethod())) {
                    exchange.close();
                    return;
                }

                try {
                    final byte[] resp = handler.handleRequest(exchange.getRequestBody().readAllBytes());
                    responseHandler.sendResponse(200, resp, exchange);
                } catch (final Exception e) {
                    final String internalServerError = "Internal server error";
                    responseHandler.sendResponse(500, internalServerError.getBytes(), exchange);
                }
            }
        };
    }

    private void handleHealthRequest(final HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("get")) {
            exchange.close();
            return;
        }

        final String message = "Server is alive\n";
        this.responseHandler.sendResponse(200, message.getBytes(), exchange);
    }
}
