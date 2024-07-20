package com.daniel.yurpc.serializer;

import java.io.IOException;

public interface Serializer {

    <T> byte[] serialize(T obj) throws IOException;

    <T> T deserialize(byte[] bytes, Class<T> cls) throws IOException;
}
