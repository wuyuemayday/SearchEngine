package repository;

import entity.repository.Document;

import java.util.List;

public interface Books {
    List<Document> scan();
    int count();
}
