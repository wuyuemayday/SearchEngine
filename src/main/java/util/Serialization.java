package util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public final class Serialization<T> {
    private final ObjectMapper mapper;
    private final Class<T> clz;

    public Serialization(final ObjectMapper mapper, final Class<T> clz) {
        this.mapper = mapper;
        this.clz = clz;
    }

    public byte[] serialize(final T obj) {
        try {
            return mapper.writeValueAsBytes(obj);
        } catch (final JsonProcessingException e) {
            e.printStackTrace();
            return new byte[]{};
        }
    }

    public T deserialize(final byte[] bytes) {
        try {
            return mapper.readValue(bytes, clz);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
