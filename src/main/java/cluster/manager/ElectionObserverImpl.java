package cluster.manager;

import cluster.serviceregistry.ServiceRegistry;

public class ElectionObserverImpl implements ElectionObserver {
    private final ServiceRegistry coordinatorRegistry;
    private final ServiceRegistry workerRegistry;

    public ElectionObserverImpl(final ServiceRegistry coordinatorRegistry,
                                final ServiceRegistry workerRegistry) {
        this.coordinatorRegistry = coordinatorRegistry;
        this.workerRegistry = workerRegistry;
    }

    @Override
    public void onLeader() {
        String host = "http:localhost:8080";
        this.coordinatorRegistry.register(host);
    }

    @Override
    public void onWorker() {
        String host = "dummy";
        this.workerRegistry.register(host);
    }
}
