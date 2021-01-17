package entity.strategy;

import entity.document.Document;

import java.util.Collections;
import java.util.Map;

public final class TFIDFResult {
    private String word;
    private Map<Document, Double> tfs;
    private int docWithWord;
    private int totalDocs;

    public TFIDFResult(final String word,
                       final Map<Document, Double> tfs,
                       final int docWithWord,
                       final int totalDocs) {
        this.word = word;
        this.tfs = tfs;
        this.docWithWord = docWithWord;
        this.totalDocs = totalDocs;
    }

    public Map<Document, Double> getTFs() {
        return Collections.unmodifiableMap(this.tfs);
    }

    public String getWord() {
        return this.word;
    }

    public int getDocWithWord() {
        return this.docWithWord;
    }

    public double getTotalDocs() {
        return this.totalDocs;
    }
}
