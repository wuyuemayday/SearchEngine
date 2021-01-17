package util;

import entity.document.Document;
import strategy.document.DocumentSplitor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocumentUtil {
    private final DocumentSplitor docSplitor;

    public DocumentUtil(final DocumentSplitor splitor) {
        this.docSplitor = splitor;
    }

    public Map<String, Integer> getFrequencyMap(final Document doc) {
        final List<String> words = this.docSplitor.splitDocumentToWords(doc);
        final Map<String, Integer> res = new HashMap<>();
        for (final String word : words) {
            res.put(word, res.getOrDefault(word, 0) + 1);
        }

        return res;
    }

    public int getTotalWords(final Document doc) {
        return this.docSplitor.splitDocumentToWords(doc).size();
    }
}
