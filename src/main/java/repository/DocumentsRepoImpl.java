package repository;

import entity.repository.DocumentRecord;

import java.util.ArrayList;
import java.util.List;

public class DocumentsRepoImpl implements DocumentsRepo {
    @Override
    public List<DocumentRecord> scan() {
        return new ArrayList<>();
    }

    @Override
    public int count() {
        return 0;
    }
}
