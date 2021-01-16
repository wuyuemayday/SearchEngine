package module;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class ObjectMapperProvider {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private ObjectMapperProvider() {}

    public static ObjectMapper provideObjectMapper() {
        return OBJECT_MAPPER;
    }
}
