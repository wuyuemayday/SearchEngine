package entity.tfidf;

import entity.tfidf.TFIDFWordResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class TFIDFTaskResponse {
    private Map<String, TFIDFWordResult> results;

    public TFIDFTaskResponse(final Map<String, TFIDFWordResult> results) {
        this.results = new HashMap<>(results);
    }

    public Map<String, TFIDFWordResult> getResults() {
        return Collections.unmodifiableMap(results);
    }
}
