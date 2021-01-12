package cluster.serviceregistry;

import java.util.List;

public interface ServiceRegistry {
    void register(String host);
    void unregister();
    List<String> getHosts();
    void updateHosts();
}
