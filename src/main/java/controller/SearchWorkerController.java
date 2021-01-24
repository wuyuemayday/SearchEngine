package controller;

import entity.task.TaskRequest;
import entity.task.TaskResponse;

public interface SearchWorkerController {
    TaskResponse processTask(TaskRequest request);
}
