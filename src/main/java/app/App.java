package app;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.log4j.Logger;
import zookeeper.leaderelection.ElectionObserver;
import zookeeper.leaderelection.LeaderElection;
import zookeeper.leaderelection.ElectionObserverImpl;

import java.io.IOException;

public final class App implements Watcher {
    private static final Logger LOGGER = Logger.getLogger(App.class);
    private static final String ZOOKEEPER_ADDRESS = "localhost:2181";
    private static final int SESSION_TIMEOUT = 3000;

    private final ZooKeeper zookeeper;
    private final LeaderElection leaderElection;

    public App() throws IOException {
        this.zookeeper = new ZooKeeper(ZOOKEEPER_ADDRESS, SESSION_TIMEOUT, this);

        final ElectionObserver observer = new ElectionObserverImpl();
        this.leaderElection = new LeaderElection(this.zookeeper, observer);
    }

    public static void main(String[] args) {
        try {
            final App app = new App();

            app.run();
            app.shutdown();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void run() throws KeeperException, InterruptedException {
        LOGGER.info("Starting the application...");

        this.leaderElection.initializeZnode();
        this.leaderElection.electLeader();
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
