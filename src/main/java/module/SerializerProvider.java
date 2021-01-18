package module;

import entity.tfidf.TFIDFTaskRequest;
import entity.tfidf.TFIDFTaskResponse;
import util.Serialization;

public final class SerializerProvider {
    private static Serialization<TFIDFTaskRequest> TFIDF_TASK_REQUEST_SERIALIZER;
    private static Serialization<TFIDFTaskResponse> TFIDF_TASK_RESPONSE_SERIALIZER;

    private SerializerProvider() {}

    public static Serialization<TFIDFTaskRequest> provideTFIDFTaskRequestSerializer() {
        if (TFIDF_TASK_REQUEST_SERIALIZER == null) {
            TFIDF_TASK_REQUEST_SERIALIZER = new Serialization<>(ObjectMapperProvider.provideObjectMapper(), TFIDFTaskRequest.class);
        }

        return TFIDF_TASK_REQUEST_SERIALIZER;
    }

    public static Serialization<TFIDFTaskResponse> provideTFIDFTaskResponseSerializer() {
        if (TFIDF_TASK_RESPONSE_SERIALIZER == null) {
            TFIDF_TASK_RESPONSE_SERIALIZER = new Serialization<>(ObjectMapperProvider.provideObjectMapper(), TFIDFTaskResponse.class);
        }

        return TFIDF_TASK_RESPONSE_SERIALIZER;
    }
}
