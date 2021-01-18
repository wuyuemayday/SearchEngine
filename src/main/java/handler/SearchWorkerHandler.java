package handler;

import controller.SearchWorkerController;
import entity.tfidf.TFIDFTaskRequest;
import entity.tfidf.TFIDFTaskResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Serialization;

public final class SearchWorkerHandler implements RequestHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchWorkerHandler.class);
    private static final String ENDPOINT = "/task";

    private final SearchWorkerController controller;
    private final Serialization<TFIDFTaskRequest> reqSerializer;
    private final Serialization<TFIDFTaskResponse> respSerializer;

    public SearchWorkerHandler(final SearchWorkerController controller,
                               final Serialization<TFIDFTaskRequest> reqSerializer,
                               final Serialization<TFIDFTaskResponse> respSerializer) {
        this.controller = controller;
        this.reqSerializer = reqSerializer;
        this.respSerializer = respSerializer;
    }

    @Override
    public byte[] handleRequest(byte[] request) throws Exception {
        LOGGER.info("Handling {} request", ENDPOINT);

        final TFIDFTaskRequest task = this.reqSerializer.deserialize(request);
        final TFIDFTaskResponse res = this.controller.processTFIDFTask(task);

        return this.respSerializer.serialize(res);
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
