package cluster.entity;

public final class ZnodeImpl implements Znode {
    private static final String NAMESPACE = "/SearchEngine/Node";
    private static final String PREFIX = "c_";

    private final String name;

    public ZnodeImpl(final String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
