package entity.coordinator;

public final class CoordinateRequest {
    private final String query;

    public CoordinateRequest(final String query) {
        this.query = query;
    }

    public String getQuery() {
        return this.query;
    }
}
