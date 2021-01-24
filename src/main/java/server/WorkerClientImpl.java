package server;

import entity.task.TaskRequest;
import entity.task.TaskResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Serialization;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class WorkerClientImpl implements WorkerClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(WorkerClientImpl.class);
    private final HttpClient client;
    private final Serialization<TaskRequest> reqSerializer;
    private final Serialization<TaskResponse> respSerializer;

    public WorkerClientImpl(final Serialization<TaskRequest> reqSerializer,
                            final Serialization<TaskResponse> respSerializer) {
        this.client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        this.reqSerializer = reqSerializer;
        this.respSerializer = respSerializer;
    }

    @Override
    public CompletableFuture<TaskResponse> doSearch(final String url, final TaskRequest request) {
        LOGGER.info("Sending search request to worker {}", url);
        final byte[] payload = this.reqSerializer.serialize(request);
        final HttpRequest httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofByteArray(payload))
                .uri(URI.create(url))
                .build();

        return client.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofByteArray())
                .thenApply(HttpResponse::body)
                .thenApply(this.respSerializer::deserialize);
    }
}
