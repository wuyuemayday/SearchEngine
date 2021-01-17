package strategy.document;

import entity.document.Document;

import java.util.List;

public interface DocumentSplitor {
    List<String> splitDocumentToWords(Document doc);
}
