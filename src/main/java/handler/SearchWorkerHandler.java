package handler;

public final class SearchWorkerHandler implements RequestHandler {
    private static final String ENDPOINT = "/task";

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
