package cluster.manager;

import cluster.serviceregistry.ServiceRegistry;
import handler.RequestHandler;
import handler.ResponseHandler;
import handler.SearchCoordinatorHandler;
import handler.SearchWorkerHandler;
import module.SearchProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.WebServer;
import server.WebServerImpl;

public class ElectionObserverImpl implements ElectionObserver {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElectionObserverImpl.class);
    private static final String LOCALHOST = "http://localhost";

    private final ServiceRegistry coordinatorRegistry;
    private final ServiceRegistry workerRegistry;
    private final SearchProvider searchProvider;
    private final int port;

    public ElectionObserverImpl(final ServiceRegistry coordinatorRegistry,
                                final ServiceRegistry workerRegistry,
                                final SearchProvider searchProvider,
                                final int port) {
        this.coordinatorRegistry = coordinatorRegistry;
        this.workerRegistry = workerRegistry;
        this.searchProvider = searchProvider;
        this.port = port;
    }

    @Override
    public void onLeader() {
        final SearchCoordinatorHandler handler = this.searchProvider.provideCoordinatorHandler();
        final WebServer server = new WebServerImpl(
                port, new RequestHandler[]{handler}, new ResponseHandler());

        server.start();
        LOGGER.info("Starting coordinator server on port {}", port);

        this.coordinatorRegistry.register(LOCALHOST + handler.getEndpoint());
    }

    @Override
    public void onWorker() {
        final SearchWorkerHandler handler = this.searchProvider.provideWorkerHandler();
        final WebServer server = new WebServerImpl(
                port, new RequestHandler[]{handler}, new ResponseHandler());

        server.start();
        LOGGER.info("Starting worker server on port {}", port);

        this.workerRegistry.register(LOCALHOST + handler.getEndpoint());
    }
}
