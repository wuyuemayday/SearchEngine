package server;

import entity.task.TaskRequest;
import entity.task.TaskResponse;

import java.util.concurrent.CompletableFuture;

public class WorkerClientImpl implements WorkerClient {
    @Override
    public CompletableFuture<TaskResponse> doSearch(String url, TaskRequest request) {
        return null;
    }
}
