package cluster.entity;

public final class ZnodeImpl implements Znode {
    private final String name;

    public ZnodeImpl(final String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
