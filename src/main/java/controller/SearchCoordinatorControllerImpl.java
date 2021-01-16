package controller;

import cluster.serviceregistry.ServiceRegistry;
import entity.handler.CoordinateRequest;
import entity.handler.CoordinateResponse;
import repository.Documents;

public class SearchCoordinatorControllerImpl implements SearchCoordinatorController {
    private final ServiceRegistry workerRegistry;
    private final Documents booksRepo;

    public SearchCoordinatorControllerImpl(
            final ServiceRegistry workerRegistry,
            final Documents booksRepo) {
        this.workerRegistry = workerRegistry;
        this.booksRepo = booksRepo;
    }

    @Override
    public CoordinateResponse coordinateSearches(CoordinateRequest request) {
        return null;
    }
}
