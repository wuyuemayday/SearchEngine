package entity.coordinator;

import entity.document.DocumentScore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class CoordinateResponse {
    private final List<DocumentScore> result;

    public CoordinateResponse(final List<DocumentScore> result) {
        this.result = new ArrayList<>(result);
    }

    public List<DocumentScore> getResult() {
        return Collections.unmodifiableList(result);
    }
}
