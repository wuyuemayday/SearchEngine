package cluster.manager;

import entity.cluster.Constant;
import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cluster.entity.Znode;
import cluster.entity.ZnodeImpl;

import java.util.Collections;
import java.util.List;

public final class ClusterManagerImpl implements ClusterManager, Watcher {
    private static final Logger logger = LoggerFactory.getLogger(ClusterManagerImpl.class);
    private static final String ZNODE_PREFIX = "c_";

    private final ZooKeeper zookeeper;
    private final ElectionObserver electionObserver;
    private Znode currentZnode;

    public ClusterManagerImpl(final ZooKeeper zookeeper, final ElectionObserver electionObserver) {
        this.zookeeper = zookeeper;
        this.electionObserver = electionObserver;
    }

    @Override
    public void connectToCluster() {
        if (currentZnode != null) {
            return;
        }

        try {
            final String znodePrefix = String.format("%s/%s", Constant.SUB_NODE, ZNODE_PREFIX);
            final String znodeFullPath = zookeeper.create(
                    znodePrefix,
                    new byte[]{},
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.EPHEMERAL_SEQUENTIAL);

            logger.info(String.format("znode name: %s", znodeFullPath));
            this.currentZnode = new ZnodeImpl(znodeFullPath.replace(Constant.SUB_NODE + "/", ""));
        } catch (final KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void electLeader() {
        try {
            final List<String> children = zookeeper.getChildren(Constant.SUB_NODE, false);
            Collections.sort(children);
            if (children.isEmpty()) {
                logger.error("no server in the cluster");
                return;
            }

            final String smallestNode = children.iterator().next();
            if (smallestNode.equals(this.currentZnode.getName())) {
                logger.info(String.format("I'm the leader: %s", this.currentZnode.getName()));
                electionObserver.onLeader();
            } else {
                logger.info(String.format("I'm NOT the leader: %s", this.currentZnode.getName()));
                final int currentPosition = Collections.binarySearch(children, this.currentZnode.getName());
                if (currentPosition <= 0) {
                    logger.error(String.format("Invalid Znode index. ZnodeName: %s, ZnodeIndex: %d", this.currentZnode.getName(), currentPosition));
                    return;
                }

                final String predecessorZnode = children.get(currentPosition - 1);
                zookeeper.exists(String.format("%s/%s", Constant.SUB_NODE, predecessorZnode), this);
                logger.info(String.format("Watching znode %s", predecessorZnode));

                electionObserver.onWorker();
            }
        } catch (final KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getType() == Event.EventType.NodeDeleted) {
            electLeader();
        }
    }
}
