package module;

import cluster.serviceregistry.ServiceRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import controller.SearchCoordinatorController;
import controller.SearchCoordinatorControllerImpl;
import controller.SearchWorkerControllerImpl;
import handler.SearchCoordinatorHandler;
import handler.SearchWorkerHandler;
import repository.DocumentsRepo;
import repository.DocumentsRepoImpl;
import strategy.document.DocumentSplitor;
import strategy.document.SimpleSplitor;
import strategy.search.TFIDF;
import util.DocumentUtil;

public final class SearchProvider {
    private final ObjectMapper objectMapper;
    private final ServiceRegistry workerRegistry;

    public SearchProvider(final ObjectMapper objectMapper,
                          final ServiceRegistry workerRegistry) {
        this.objectMapper = objectMapper;
        this.workerRegistry = workerRegistry;
    }

    public SearchCoordinatorHandler provideCoordinatorHandler() {
        final SearchCoordinatorController controller = new SearchCoordinatorControllerImpl(this.workerRegistry, new DocumentsRepoImpl());

        return new SearchCoordinatorHandler(controller, this.objectMapper);
    }

    public SearchWorkerHandler provideWorkerHandler() {
        final DocumentSplitor splitor = new SimpleSplitor();
        final DocumentUtil util = new DocumentUtil(splitor);
        final DocumentsRepo repository = new DocumentsRepoImpl();
        final TFIDF strategy = new TFIDF(util);

        return new SearchWorkerHandler(
                new SearchWorkerControllerImpl(repository, strategy),
                SerializerProvider.provideTFIDFTaskRequestSerializer(),
                SerializerProvider.provideTFIDFTaskResponseSerializer());
    }
}
