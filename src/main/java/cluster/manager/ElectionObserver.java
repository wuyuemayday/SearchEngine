package cluster.manager;

public interface ElectionObserver {
    void onLeader();
    void onWorker();
}
