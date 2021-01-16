package repository;

import entity.repository.Document;

import java.util.List;

public interface Documents {
    List<Document> scan();
    int count();
}
