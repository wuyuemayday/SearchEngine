package strategy.search;

import entity.document.DocumentData;
import entity.tfidf.TFIDFWordResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TFIDF {
    public static TFIDFWordResult calculateWordTFIDF(final String word, final List<DocumentData> documentDataList) {
        final Map<String, Double> tfs = new HashMap<>();
        int docsContainWord = 0;
        for (final DocumentData data : documentDataList) {
            if (data.getFrequency().containsKey(word)) {
                final double tf = (double) data.getFrequency().get(word) / data.getTotalWords();
                tfs.put(data.getDocID(), tf);
                docsContainWord++;
            }
        }

        final double idf = Math.log10((double) documentDataList.size() / docsContainWord);

        return new TFIDFWordResult(word, tfs, idf);
    }
}
