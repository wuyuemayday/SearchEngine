package app;

import cluster.serviceregistry.ServiceRegistry;
import cluster.serviceregistry.ServiceRegistryImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.cluster.Constant;
import entity.task.TaskRequest;
import entity.task.TaskResponse;
import module.ObjectMapperProvider;
import module.SearchProvider;
import module.SerializerProvider;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.log4j.Logger;
import cluster.manager.ClusterManager;
import cluster.manager.ElectionObserver;
import cluster.manager.ClusterManagerImpl;
import cluster.manager.ElectionObserverImpl;
import repository.DocumentsRepo;
import repository.DocumentsRepoImpl;
import server.WorkerClient;
import server.WorkerClientImpl;
import strategy.document.ContentSplitor;
import strategy.document.SimpleSplitor;
import util.Serialization;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public final class App implements Watcher {
    private static final Logger LOGGER = Logger.getLogger(App.class);
    private final ZooKeeper zookeeper;
    private final ClusterManager clusterManager;

    public App(final int port) throws IOException, KeeperException, InterruptedException {
        this.zookeeper = new ZooKeeper(Constant.ZOOKEEPER_ADDRESS, Constant.SESSION_TIMEOUT, this);

        final ServiceRegistry coordinatorRegistry = new ServiceRegistryImpl(this.zookeeper, Constant.SUB_COORDINATOR);
        final ServiceRegistry workerRegistry = new ServiceRegistryImpl(this.zookeeper, Constant.SUB_WORKER);

        final WorkerClient workerClient = new WorkerClientImpl(
                SerializerProvider.provideTaskRequestSerializer(),
                SerializerProvider.provideTaskResponseSerializer());
        final ContentSplitor splitor = new SimpleSplitor();
        final DocumentsRepo repo = new DocumentsRepoImpl();
        final SearchProvider coordinatorProvider = new SearchProvider(workerClient, workerRegistry, splitor, repo);
        final ElectionObserver observer = new ElectionObserverImpl(
                coordinatorRegistry, workerRegistry, coordinatorProvider, port);

        this.clusterManager = new ClusterManagerImpl(this.zookeeper, observer);
    }

    public static void main(String[] args) {
        int port = 8081;
        if (args.length == 1) {
            port = Integer.parseInt(args[0]);
        }

        try {
            final App app = new App(port);

            app.run();
            app.shutdown();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void run() throws InterruptedException {
        LOGGER.info("Starting the application...");

        this.clusterManager.connectToCluster();
        this.clusterManager.electLeader();
        synchronized (this.zookeeper) {
            zookeeper.wait();
        }
    }

    public void shutdown() throws InterruptedException {
        LOGGER.info("Shutting down the application...");
        zookeeper.close();
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getType() == Event.EventType.None) {
            if (watchedEvent.getState() != Event.KeeperState.SyncConnected) {
                zookeeper.notifyAll();
            }
        }
    }
}
