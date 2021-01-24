package module;

import entity.task.TaskRequest;
import entity.task.TaskResponse;
import util.Serialization;

public final class SerializerProvider {
    private static Serialization<TaskRequest> TASK_REQUEST_SERIALIZER;
    private static Serialization<TaskResponse> TASK_RESPONSE_SERIALIZER;

    private SerializerProvider() {}

    public static Serialization<TaskRequest> provideTaskRequestSerializer() {
        if (TASK_REQUEST_SERIALIZER == null) {
            TASK_REQUEST_SERIALIZER = new Serialization<>(ObjectMapperProvider.provideObjectMapper(), TaskRequest.class);
        }

        return TASK_REQUEST_SERIALIZER;
    }

    public static Serialization<TaskResponse> provideTaskResponseSerializer() {
        if (TASK_RESPONSE_SERIALIZER == null) {
            TASK_RESPONSE_SERIALIZER = new Serialization<>(ObjectMapperProvider.provideObjectMapper(), TaskResponse.class);
        }

        return TASK_RESPONSE_SERIALIZER;
    }
}
