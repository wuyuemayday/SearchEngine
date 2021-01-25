package entity.document;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public final class DocumentData {
    private final String docID;
    private final int totalWords;
    private final Map<String, Integer> frequency;

    public DocumentData(@JsonProperty("docID") final String docID,
                        @JsonProperty("totalWords") final int totalWords,
                        @JsonProperty("frequency") final Map<String, Integer> frequency) {
        this.docID = docID;
        this.totalWords = totalWords;
        this.frequency = new HashMap<>(frequency);
    }

    public String getDocID() {
        return docID;
    }

    public int getTotalWords() {
        return totalWords;
    }

    public Map<String, Integer> getFrequency() {
        return frequency;
    }
}
