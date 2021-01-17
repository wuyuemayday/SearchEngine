package strategy.search;

import entity.document.Document;
import entity.strategy.TFIDFResult;
import util.DocumentUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TFIDF {
    private final DocumentUtil util;

    public TFIDF(final DocumentUtil util) {
        this.util = util;
    }

    public TFIDFResult getTFIDFResult(final String word, final List<Document> docs) {
        int docCount = 0;
        final Map<Document, Double> tfs = new HashMap<>();
        for (final Document doc : docs) {
            final int wordCount = this.util.getFrequencyMap(doc).getOrDefault(word, 0);
            final double tf = (double) wordCount / this.util.getTotalWords(doc);
            tfs.put(doc, tf);
            if (wordCount > 0) {
                docCount++;
            }
        }

        return new TFIDFResult(word, tfs, docCount, docs.size());
    }
}
