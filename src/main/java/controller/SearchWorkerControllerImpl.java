package controller;

import entity.document.Document;
import entity.tfidf.TFIDFWordResult;
import entity.tfidf.TFIDFTaskRequest;
import entity.tfidf.TFIDFTaskResponse;
import repository.DocumentsRepo;
import strategy.search.TFIDF;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class SearchWorkerControllerImpl implements SearchWorkerController {
    private final DocumentsRepo repository;
    private final TFIDF strategy;

    public SearchWorkerControllerImpl(final DocumentsRepo repository,
                                      final TFIDF strategy) {
        this.repository = repository;
        this.strategy = strategy;
    }

    @Override
    public TFIDFTaskResponse processTFIDFTask(final TFIDFTaskRequest request) {
        final Map<String, TFIDFWordResult> response = new HashMap<>();
        final List<Document> documents = this.repository.getDocumentsByIDs(request.getDocumentIDs());
        for (final String word : request.getTerms()) {
            final TFIDFWordResult res = this.strategy.getTFIDFResult(word, documents);
            response.putIfAbsent(word, res);
        }

        return new TFIDFTaskResponse(response);
    }
}
