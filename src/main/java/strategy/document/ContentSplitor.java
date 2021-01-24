package strategy.document;

import entity.document.Document;

import java.util.List;

public interface ContentSplitor {
    List<String> splitDocumentToWords(String content);
}
