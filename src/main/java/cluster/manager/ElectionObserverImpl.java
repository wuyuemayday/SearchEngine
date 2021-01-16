package cluster.manager;

import cluster.serviceregistry.ServiceRegistry;
import handler.RequestHandler;
import handler.ResponseHandler;
import handler.SearchCoordinatorHandler;
import module.SearchCoordinatorProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.WebServer;
import server.WebServerImpl;

public class ElectionObserverImpl implements ElectionObserver {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElectionObserverImpl.class);
    private static final String LOCALHOST = "http://localhost";

    private final ServiceRegistry coordinatorRegistry;
    private final ServiceRegistry workerRegistry;
    private final SearchCoordinatorProvider coordinatorProvider;
    private final int port;

    public ElectionObserverImpl(final ServiceRegistry coordinatorRegistry,
                                final ServiceRegistry workerRegistry,
                                final SearchCoordinatorProvider coordinatorProvider,
                                final int port) {
        this.coordinatorRegistry = coordinatorRegistry;
        this.workerRegistry = workerRegistry;
        this.coordinatorProvider = coordinatorProvider;
        this.port = port;
    }

    @Override
    public void onLeader() {
        final SearchCoordinatorHandler handler = this.coordinatorProvider.provideHandler();
        final WebServer coordinatorServer = new WebServerImpl(
                port, new RequestHandler[]{handler}, new ResponseHandler());
        coordinatorServer.start();
        LOGGER.info("Starting server on port {}", port);

        this.coordinatorRegistry.register(LOCALHOST + handler.getEndpoint());
    }

    @Override
    public void onWorker() {
        String host = "dummy";
        this.workerRegistry.register(host);
    }
}
