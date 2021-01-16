package handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SearchWorkerHandler implements RequestHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchWorkerHandler.class);
    private static final String ENDPOINT = "/task";

    @Override
    public byte[] handleRequest(byte[] request) throws Exception {
        LOGGER.info("Handling {} request", ENDPOINT);
        return new byte[0];
    }

    @Override
    public String getEndpoint() {
        return ENDPOINT;
    }

    @Override
    public String getMethod() {
        return "post";
    }
}
