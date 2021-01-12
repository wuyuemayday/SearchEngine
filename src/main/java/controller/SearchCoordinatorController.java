package controller;

import entity.handler.CoordinateRequest;
import entity.handler.CoordinateResponse;

public interface SearchCoordinatorController {
    CoordinateResponse coordinateSearches(CoordinateRequest request);
}
