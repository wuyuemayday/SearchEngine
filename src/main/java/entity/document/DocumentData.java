package entity.document;

import java.util.HashMap;
import java.util.Map;

public final class DocumentData {
    private String docID;
    private int totalWords;
    private Map<String, Integer> frequency;

    public DocumentData(final String docID,
                        final int totalWords,
                        final Map<String, Integer> frequency) {
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
