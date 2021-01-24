package entity.task;

import entity.document.DocumentData;

import java.util.*;

public final class TaskResponse {
    private List<DocumentData> results;

    public TaskResponse(final List<DocumentData> results) {
        this.results = new ArrayList<>(results);
    }

    public List<DocumentData> getResults() {
        return Collections.unmodifiableList(results);
    }
}
