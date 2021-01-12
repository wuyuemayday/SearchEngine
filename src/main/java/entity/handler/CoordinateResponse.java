package entity.handler;

import entity.repository.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class CoordinateResponse {
    private final List<Document> result;

    public CoordinateResponse(final List<Document> result) {
        this.result = new ArrayList<>(result);
    }

    public List<Document> getResult() {
        return Collections.unmodifiableList(result);
    }
}
