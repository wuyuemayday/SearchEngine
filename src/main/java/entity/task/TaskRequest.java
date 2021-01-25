package entity.task;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class TaskRequest {
    private List<String> documentIDs;
    private List<String> terms;

    public TaskRequest(@JsonProperty("documentIDs") final List<String> documentIDs,
                       @JsonProperty("terms") final List<String> terms) {
        this.documentIDs = new ArrayList<>(documentIDs);
        this.terms = new ArrayList<>(terms);
    }

    public List<String> getDocumentIDs() {
        return Collections.unmodifiableList(documentIDs);
    }

    public List<String> getTerms() {
        return Collections.unmodifiableList(terms);
    }
}
