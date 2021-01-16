package module;

import cluster.serviceregistry.ServiceRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import controller.SearchCoordinatorController;
import controller.SearchCoordinatorControllerImpl;
import handler.SearchCoordinatorHandler;
import handler.SearchWorkerHandler;
import repository.DocumentsImpl;

public final class SearchProvider {
    private final ObjectMapper objectMapper;
    private final ServiceRegistry workerRegistry;

    public SearchProvider(final ObjectMapper objectMapper,
                          final ServiceRegistry workerRegistry) {
        this.objectMapper = objectMapper;
        this.workerRegistry = workerRegistry;
    }

    public SearchCoordinatorHandler provideCoordinatorHandler() {
        final SearchCoordinatorController controller = new SearchCoordinatorControllerImpl(this.workerRegistry, new DocumentsImpl());

        return new SearchCoordinatorHandler(controller, this.objectMapper);
    }

    public SearchWorkerHandler provideWorkerHandler() {
        return new SearchWorkerHandler();
    }
}
