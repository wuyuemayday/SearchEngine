package module;

import cluster.serviceregistry.ServiceRegistry;
import controller.SearchCoordinatorController;
import controller.SearchCoordinatorControllerImpl;
import controller.SearchWorkerControllerImpl;
import handler.SearchCoordinatorHandler;
import handler.SearchWorkerHandler;
import repository.DocumentsRepo;
import repository.DocumentsRepoImpl;
import server.WorkerClient;
import strategy.document.ContentSplitor;
import strategy.document.SimpleSplitor;

public final class SearchProvider {
    private final WorkerClient client;
    private final ServiceRegistry workerRegistry;
    private final ContentSplitor splitor;
    private final DocumentsRepo repo;

    public SearchProvider(final WorkerClient client,
                          final ServiceRegistry workerRegistry,
                          final ContentSplitor splitor,
                          final DocumentsRepo repo) {
        this.client = client;
        this.workerRegistry = workerRegistry;
        this.splitor = splitor;
        this.repo = repo;
    }

    public SearchCoordinatorHandler provideCoordinatorHandler() {
        final SearchCoordinatorController controller = new SearchCoordinatorControllerImpl(
                this.client, this.workerRegistry, this.splitor, this.repo);

        return new SearchCoordinatorHandler(controller);
    }

    public SearchWorkerHandler provideWorkerHandler() {
        final ContentSplitor splitor = new SimpleSplitor();
        final DocumentsRepo repository = new DocumentsRepoImpl();

        return new SearchWorkerHandler(
                new SearchWorkerControllerImpl(repository, splitor),
                SerializerProvider.provideTaskRequestSerializer(),
                SerializerProvider.provideTaskResponseSerializer());
    }
}
