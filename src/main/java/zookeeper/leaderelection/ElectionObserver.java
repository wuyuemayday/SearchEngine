package zookeeper.leaderelection;

public interface ElectionObserver {
    void onLeader();
    void onWorker();
}
