package handler;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public final class ResponseHandler {
    public void sendResponse(final int statusCode, final byte[] resp, final HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(statusCode, resp.length);
        final OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(resp);
        outputStream.flush();
        outputStream.close();
    }
}
