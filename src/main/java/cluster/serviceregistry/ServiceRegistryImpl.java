package cluster.serviceregistry;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ServiceRegistryImpl implements ServiceRegistry, Watcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRegistryImpl.class);
    private static final String ZNODE_PREFIX = "n_";

    private final ZooKeeper zooKeeper;
    private final String registry;
    private String currentZnode;
    private List<String> hosts;

    public ServiceRegistryImpl(final ZooKeeper zooKeeper, final String registry) throws KeeperException, InterruptedException {
        this.zooKeeper = zooKeeper;
        this.registry = registry;

        if (this.zooKeeper.exists(this.registry, false) == null) {
            this.zooKeeper.create(this.registry, new byte[]{}, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
    }

    @Override
    public void register(final String host) {
        if (this.currentZnode != null) {
            return;
        }

        try {
            this.currentZnode = this.zooKeeper.create(
                    String.format("%s/%s", this.registry, ZNODE_PREFIX),
                    host.getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.EPHEMERAL_SEQUENTIAL);

            LOGGER.info("Registered znode {} to registry {}", this.currentZnode, this.registry);
        } catch (final KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unregister() {
        if (this.registry == null || this.currentZnode == null) {
            return;
        }

        try {
            if (this.zooKeeper.exists(this.currentZnode, false) == null) {
                return;
            }

            this.zooKeeper.delete(this.currentZnode, -1);
        } catch (final KeeperException | InterruptedException e) {
            e.printStackTrace();
            return;
        }

        LOGGER.info("Unregistered znode {} from registry {}", this.currentZnode, this.registry);
    }

    @Override
    public synchronized List<String> getHosts() {
        if (this.hosts == null) {
            updateHosts();
        }

        return this.hosts;
    }

    @Override
    public synchronized void updateHosts() {
        try {
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

            this.hosts = Collections.unmodifiableList(res);
        } catch (final KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void process(WatchedEvent watchedEvent) {
        updateHosts();
    }
}
