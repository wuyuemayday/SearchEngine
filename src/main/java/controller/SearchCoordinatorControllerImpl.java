package controller;

import cluster.serviceregistry.ServiceRegistry;
import entity.handler.CoordinateRequest;
import entity.handler.CoordinateResponse;
import repository.DocumentsRepo;

public class SearchCoordinatorControllerImpl implements SearchCoordinatorController {
    private final ServiceRegistry workerRegistry;
    private final DocumentsRepo booksRepo;

    public SearchCoordinatorControllerImpl(
            final ServiceRegistry workerRegistry,
            final DocumentsRepo booksRepo) {
        this.workerRegistry = workerRegistry;
        this.booksRepo = booksRepo;
    }

    @Override
    public CoordinateResponse coordinateSearches(CoordinateRequest request) {
        return null;
    }
}
