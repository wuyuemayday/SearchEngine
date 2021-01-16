package repository;

import entity.repository.Document;

import java.util.ArrayList;
import java.util.List;

public class DocumentsImpl implements Documents {
    @Override
    public List<Document> scan() {
        return new ArrayList<>();
    }

    @Override
    public int count() {
        return 0;
    }
}
