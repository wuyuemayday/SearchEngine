package handler;

public interface RequestHandler {
    byte[] handleRequest(byte[] request) throws Exception;
    String getEndpoint();
    String getMethod();
}
