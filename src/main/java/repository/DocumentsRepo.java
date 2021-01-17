package repository;

import entity.repository.DocumentRecord;

import java.util.List;

public interface DocumentsRepo {
    List<DocumentRecord> scan();
    int count();
}
