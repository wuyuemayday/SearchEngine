package zookeeper.leaderelection;

import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

public final class LeaderElection implements Watcher {
    private static final Logger logger = LoggerFactory.getLogger(LeaderElection.class);
    private static final String ZOOKEEPER_NAMESPACE = "/SearchEngine/Node";
    private static final String ZNODE_PREFIX = "c_";

    private final ZooKeeper zookeeper;
    private final ElectionObserver electionObserver;
    private String currentZnodeName;

    public LeaderElection(final ZooKeeper zookeeper, final ElectionObserver electionObserver) {
        this.zookeeper = zookeeper;
        this.electionObserver = electionObserver;
    }

    public void initializeZnode() throws KeeperException, InterruptedException {
        final String znodePrefix = String.format("%s/%s", ZOOKEEPER_NAMESPACE, ZNODE_PREFIX);
        final String znodeFullPath = zookeeper.create(
                znodePrefix,
                new byte[]{},
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL_SEQUENTIAL);

        logger.info(String.format("znode name: %s", znodeFullPath));
        this.currentZnodeName = znodeFullPath.replace(ZOOKEEPER_NAMESPACE + "/", "");
    }

    public void electLeader() throws KeeperException, InterruptedException {
        final List<String> children = zookeeper.getChildren(ZOOKEEPER_NAMESPACE, false);
        Collections.sort(children);
        if (children.isEmpty()) {
            logger.error("no server in the cluster");
            return;
        }

        final String smallestNode = children.iterator().next();
        if (smallestNode.equals(this.currentZnodeName)) {
            logger.info(String.format("I'm the leader: %s", this.currentZnodeName));
            electionObserver.onLeader();
        } else {
            logger.info(String.format("I'm NOT the leader: %s", this.currentZnodeName));
            final int currentPosition = Collections.binarySearch(children, this.currentZnodeName);
            if (currentPosition <= 0) {
                logger.error(String.format("Invalid Znode index. ZnodeName: %s, ZnodeIndex: %d", this.currentZnodeName, currentPosition));
                return;
            }

            final String predecessorZnode = children.get(currentPosition - 1);
            zookeeper.exists(String.format("%s/%s", ZOOKEEPER_NAMESPACE, predecessorZnode), this);
            logger.info(String.format("Watching znode %s", predecessorZnode));

            electionObserver.onWorker();
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getType() == Event.EventType.NodeDeleted) {
            try {
                electLeader();
            } catch (final KeeperException | InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
