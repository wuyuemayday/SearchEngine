package repository;

import entity.document.Document;

import java.util.List;

public interface DocumentsRepo {
    List<Document> scan();
    int count();
    Document getDocumentByID(String id);
    List<Document> getDocumentsByIDs(List<String> ids);
}
