package handler;

import controller.SearchCoordinatorController;
import entity.coordinator.CoordinateRequest;
import entity.coordinator.CoordinateResponse;
import entity.proto.SearchModel;
import mapper.CoordinatorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SearchCoordinatorHandler implements RequestHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchCoordinatorHandler.class);
    private static final String ENDPOINT = "/search";

    private final SearchCoordinatorController controller;

    public SearchCoordinatorHandler(final SearchCoordinatorController controller) {
        this.controller = controller;
    }

    @Override
    public byte[] handleRequest(final byte[] request) throws Exception {
        final String terms = new String(request);
        LOGGER.info("Handling {} request, search terms: {}", ENDPOINT, terms);

        final CoordinateRequest coordinateRequest = new CoordinateRequest(terms);
        final CoordinateResponse coordinateResponse = this.controller.coordinateSearches(coordinateRequest);

        final SearchModel.SearchResponse resp = CoordinatorMapper.CoordinateResponseToProtobufResponse(coordinateResponse);
        return resp.toByteArray();
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
