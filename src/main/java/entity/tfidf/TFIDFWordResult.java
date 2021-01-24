package entity.tfidf;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class TFIDFWordResult {
    private final String word;
    // docID -> tf, for the given word
    private final Map<String, Double> tfs;
    private final double idf;

    public TFIDFWordResult(final String word,
                           final Map<String, Double> tfs,
                           final double idf) {
        this.word = word;
        this.tfs = new HashMap<>(tfs);
        this.idf = idf;
    }

    public String getWord() {
        return word;
    }

    public Map<String, Double> getTfs() {
        return Collections.unmodifiableMap(tfs);
    }

    public double getIdf() {
        return idf;
    }
}