package repository;

import entity.document.Document;

import java.util.List;

public interface DocumentsRepo {
    List<String> scan();
    int count();
    Document getDocumentByID(String id);
    List<Document> getDocumentsByIDs(List<String> ids);
}
