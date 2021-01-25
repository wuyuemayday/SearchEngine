package entity.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import entity.document.DocumentData;

import java.util.*;

public final class TaskResponse {
    private List<DocumentData> results;

    public TaskResponse(@JsonProperty("results") final List<DocumentData> results) {
        this.results = new ArrayList<>(results);
    }

    public List<DocumentData> getResults() {
        return Collections.unmodifiableList(results);
    }
}
