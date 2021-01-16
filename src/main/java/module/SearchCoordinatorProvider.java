package module;

import cluster.serviceregistry.ServiceRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import controller.SearchCoordinatorController;
import controller.SearchCoordinatorControllerImpl;
import handler.SearchCoordinatorHandler;
import repository.DocumentsImpl;

public final class SearchCoordinatorProvider {
    private final ObjectMapper objectMapper;
    private final ServiceRegistry workerRegistry;

    public SearchCoordinatorProvider(final ObjectMapper objectMapper,
                                     final ServiceRegistry workerRegistry) {
        this.objectMapper = objectMapper;
        this.workerRegistry = workerRegistry;
    }

    public SearchCoordinatorHandler provideHandler() {
        final SearchCoordinatorController controller = new SearchCoordinatorControllerImpl(this.workerRegistry, new DocumentsImpl());

        return new SearchCoordinatorHandler(controller, this.objectMapper);
    }
}
