package controller;

import cluster.serviceregistry.ServiceRegistry;
import entity.coordinator.CoordinateRequest;
import entity.coordinator.CoordinateResponse;
import entity.document.Document;
import entity.document.DocumentData;
import entity.document.DocumentScore;
import entity.task.TaskRequest;
import entity.task.TaskResponse;
import entity.tfidf.TFIDFWordResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.DocumentsRepo;
import server.WorkerClient;
import strategy.document.ContentSplitor;
import strategy.search.TFIDF;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class SearchCoordinatorControllerImpl implements SearchCoordinatorController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchCoordinatorControllerImpl.class);

    private final WorkerClient client;
    private final ServiceRegistry workerRegistry;
    private final ContentSplitor splitor;
    private final DocumentsRepo documentsRepo;

    public SearchCoordinatorControllerImpl(
            final WorkerClient client,
            final ServiceRegistry workerRegistry,
            final ContentSplitor splitor,
            final DocumentsRepo documentsRepo) {
        this.client = client;
        this.workerRegistry = workerRegistry;
        this.splitor = splitor;
        this.documentsRepo = documentsRepo;
    }

    @Override
    public CoordinateResponse coordinateSearches(CoordinateRequest request) {
        final List<String> workers = this.workerRegistry.getHosts();
        if (workers.isEmpty()) {
            LOGGER.warn("No worker available to dispatch search request");
            return new CoordinateResponse(new ArrayList<>());
        }

        LOGGER.info("Dispatching search requests to {} workers ...", workers.size());

        final List<String> words = this.splitor.splitDocumentToWords(request.getQuery());
        final List<String> docs = this.documentsRepo.scan();
        Map<String, TaskRequest> workerRequests = this.createWorkerRequests(workers, docs, words);
        final List<TaskResponse> workerResponses = this.dispatchWorkerRequests(workerRequests);

        final List<DocumentScore> docScores = this.aggregateResults(workerResponses, words);

        return new CoordinateResponse(docScores);
    }

    private List<DocumentScore> aggregateResults(final List<TaskResponse> resps, final List<String> words) {
        final List<DocumentData> allDocs = new ArrayList<>();
        for (final TaskResponse resp : resps) {
            allDocs.addAll(resp.getResults());
        }

        final Map<String, TFIDFWordResult> tfidfs = aggregateTFIDFs(words, allDocs);
        final List<DocumentScore> docScores = new ArrayList<>();
        for (final DocumentData docData : allDocs) {
            double score = this.getDocScore(docData, words, tfidfs);
            Document doc = this.documentsRepo.getDocumentByID(docData.getDocID());
            docScores.add(new DocumentScore(doc.getID(), doc.getName(), score));
        }

        docScores.sort((a, b) -> (int) (b.getScore() - a.getScore()));

        return docScores;
    }

    private double getDocScore(final DocumentData docData,
                               final List<String> terms,
                               final Map<String, TFIDFWordResult> tfidfs) {
        double score = 0.0;
        for (final String word : terms) {
            final TFIDFWordResult wordResult = tfidfs.get(word);
            if (wordResult != null) {
                if (wordResult.getTfs().containsKey(docData.getDocID())) {
                    score += wordResult.getTfs().get(docData.getDocID()) * wordResult.getIdf();
                }
            }

        }

        return score;
    }

    private Map<String, TFIDFWordResult> aggregateTFIDFs(final List<String> words, final List<DocumentData> allDocs) {
        final Map<String, TFIDFWordResult> res = new HashMap<>();
        for (final String word : words) {
            final TFIDFWordResult wordResult = TFIDF.calculateWordTFIDF(word, allDocs);
            res.put(word, wordResult);
        }

        return res;
    }

    private List<TaskResponse> dispatchWorkerRequests(final Map<String, TaskRequest> reqs) {
        final List<CompletableFuture<TaskResponse>> futures = new ArrayList<>();
        for (final String worker : reqs.keySet()) {
            LOGGER.info("Dispatch search requests to worker {}", worker);
            futures.add(this.client.doSearch(worker, reqs.get(worker)));
        }

        LOGGER.info("Waiting on workers responses ...");
        final List<TaskResponse> res = new ArrayList<>();
        for (final CompletableFuture<TaskResponse> fut : futures) {
            try {
                res.add(fut.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        return res;
    }

    private Map<String, TaskRequest> createWorkerRequests(final List<String> workers,
                                                          final List<String> docs,
                                                          final List<String> words) {
        final int documentsPerWorker = (docs.size() + workers.size() - 1) / workers.size();

        final Map<String, TaskRequest> out = new HashMap<>();
        int from = 0;
        for (final String worker : workers) {
            final int to = Math.min(from + documentsPerWorker, docs.size());
            out.put(worker, new TaskRequest(docs.subList(from, to), words));
            from += documentsPerWorker;
        }

        return out;
    }
}
