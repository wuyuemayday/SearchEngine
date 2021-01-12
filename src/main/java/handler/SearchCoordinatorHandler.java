package handler;

public final class SearchCoordinatorHandler implements RequestHandler {
    private static final String ENDPOINT = "/search";

    @Override
    public byte[] handleRequest(byte[] request) throws Exception {
        return new byte[0];
    }

    @Override
    public String getEndpoint() {
        return ENDPOINT;
    }

    @Override
    public String getMethod() {
        return "post";
    }
}
