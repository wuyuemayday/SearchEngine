package server;

import entity.task.TaskRequest;
import entity.task.TaskResponse;

import java.util.concurrent.CompletableFuture;

public interface WorkerClient {
    CompletableFuture<TaskResponse> doSearch(String url, TaskRequest request);
}
