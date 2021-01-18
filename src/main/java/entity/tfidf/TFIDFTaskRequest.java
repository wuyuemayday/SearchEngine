package entity.tfidf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class TFIDFTaskRequest {
    private List<String> documentIDs;
    private List<String> terms;

    public TFIDFTaskRequest(final List<String> documentIDs,
                            final List<String> terms) {
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
