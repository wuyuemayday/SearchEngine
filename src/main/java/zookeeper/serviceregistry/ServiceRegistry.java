package zookeeper.serviceregistry;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ServiceRegistry implements Watcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRegistry.class);
    private static final String ZNODE_PREFIX = "n_";

    private final ZooKeeper zooKeeper;
    private final String registry;
    private String currentZnode;
    private List<String> endpoints;

    public ServiceRegistry(final ZooKeeper zooKeeper, final String registry) throws KeeperException, InterruptedException {
        this.zooKeeper = zooKeeper;
        this.registry = registry;

        if (this.zooKeeper.exists(this.registry, false) == null) {
            this.zooKeeper.create(this.registry, new byte[]{}, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
    }

    public void register(final String endpoint) throws KeeperException, InterruptedException {
        if (this.currentZnode != null) {
            return;
        }

        this.currentZnode = this.zooKeeper.create(
                String.format("%s/%s", this.registry, ZNODE_PREFIX),
                endpoint.getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL_SEQUENTIAL);

        LOGGER.info("Registered znode {} to registry {}", this.currentZnode, this.registry);
    }

    public void unregister() throws KeeperException, InterruptedException {
        if (this.registry == null || this.currentZnode == null) {
            return;
        }
        if (this.zooKeeper.exists(this.currentZnode, false) == null) {
            return;
        }

        this.zooKeeper.delete(this.currentZnode, -1);

        LOGGER.info("Unregistered znode {} from registry {}", this.currentZnode, this.registry);
    }

    public synchronized List<String> getEndpoints() throws KeeperException, InterruptedException {
        if (this.endpoints == null) {
            updateEndpoints();
        }

        return this.endpoints;
    }

    public synchronized void updateEndpoints() throws KeeperException, InterruptedException {
        final List<String> children = this.zooKeeper.getChildren(registry, this);
        final List<String> res = new ArrayList<>(children.size());
        for (final String child : children) {
            final String path = String.format("%s/%s", this.registry, child);
            final Stat stat = this.zooKeeper.exists(path, false);
            if (stat == null) {
                continue;
            }

            final String endpoint = new String(zooKeeper.getData(path, false, stat));
            res.add(endpoint);
        }

         this.endpoints = Collections.unmodifiableList(res);
    }


    @Override
    public void process(WatchedEvent watchedEvent) {
        try {
            updateEndpoints();
        } catch (final KeeperException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}
