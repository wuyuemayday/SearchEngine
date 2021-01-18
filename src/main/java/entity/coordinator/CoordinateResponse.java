package entity.coordinator;

import entity.repository.DocumentRecord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class CoordinateResponse {
    private final List<DocumentRecord> result;

    public CoordinateResponse(final List<DocumentRecord> result) {
        this.result = new ArrayList<>(result);
    }

    public List<DocumentRecord> getResult() {
        return Collections.unmodifiableList(result);
    }
}
