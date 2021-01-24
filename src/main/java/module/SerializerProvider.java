package module;

import entity.task.TaskRequest;
import entity.task.TaskResponse;
import util.Serialization;

public final class SerializerProvider {
    private static Serialization<TaskRequest> TFIDF_TASK_REQUEST_SERIALIZER;
    private static Serialization<TaskResponse> TFIDF_TASK_RESPONSE_SERIALIZER;

    private SerializerProvider() {}

    public static Serialization<TaskRequest> provideTFIDFTaskRequestSerializer() {
        if (TFIDF_TASK_REQUEST_SERIALIZER == null) {
            TFIDF_TASK_REQUEST_SERIALIZER = new Serialization<>(ObjectMapperProvider.provideObjectMapper(), TaskRequest.class);
        }

        return TFIDF_TASK_REQUEST_SERIALIZER;
    }

    public static Serialization<TaskResponse> provideTFIDFTaskResponseSerializer() {
        if (TFIDF_TASK_RESPONSE_SERIALIZER == null) {
            TFIDF_TASK_RESPONSE_SERIALIZER = new Serialization<>(ObjectMapperProvider.provideObjectMapper(), TaskResponse.class);
        }

        return TFIDF_TASK_RESPONSE_SERIALIZER;
    }
}
