package controller;

import entity.coordinator.CoordinateRequest;
import entity.coordinator.CoordinateResponse;

public interface SearchCoordinatorController {
    CoordinateResponse coordinateSearches(CoordinateRequest request);
}
