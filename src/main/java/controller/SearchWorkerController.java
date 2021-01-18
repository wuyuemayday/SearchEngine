package controller;

import entity.tfidf.TFIDFTaskRequest;
import entity.tfidf.TFIDFTaskResponse;

public interface SearchWorkerController {
    TFIDFTaskResponse processTFIDFTask(TFIDFTaskRequest request);
}
