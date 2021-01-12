package cluster.manager;

public interface ClusterManager {
    void connectToCluster();
    void electLeader();
}
