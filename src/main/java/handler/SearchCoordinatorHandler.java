package handler;

import controller.SearchCoordinatorController;
import entity.handler.CoordinateRequest;
import entity.handler.CoordinateResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SearchCoordinatorHandler implements RequestHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchCoordinatorHandler.class);
    private static final String ENDPOINT = "/search";

    private final ObjectMapper objectMapper;
    private final SearchCoordinatorController controller;

    public SearchCoordinatorHandler(
            final SearchCoordinatorController controller,
            final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.controller = controller;
    }

    @Override
    public byte[] handleRequest(final byte[] request) throws Exception {
        LOGGER.info("Handling {} request", ENDPOINT);
        final CoordinateRequest coordinateRequest = new CoordinateRequest(new String(request));
        final CoordinateResponse coordinateResponse = this.controller.coordinateSearches(coordinateRequest);
        return objectMapper.writeValueAsBytes(coordinateResponse);
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
