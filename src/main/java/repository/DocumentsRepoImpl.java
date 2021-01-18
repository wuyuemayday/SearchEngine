package repository;

import entity.document.Document;

import java.util.ArrayList;
import java.util.List;

public class DocumentsRepoImpl implements DocumentsRepo {
    @Override
    public List<Document> scan() {
        return new ArrayList<>();
    }

    @Override
    public int count() {
        return 0;
    }

    @Override
    public Document getDocumentByID(String id) {
        return null;
    }

    @Override
    public List<Document> getDocumentsByIDs(List<String> ids) {
        return null;
    }
}
