package com.daniel.yurpc.proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.daniel.yurpc.model.RpcRequest;
import com.daniel.yurpc.model.RpcResponse;
import com.daniel.yurpc.serializer.JdkSerializer;
import com.daniel.yurpc.serializer.Serializer;
import lombok.extern.java.Log;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Log
public class ServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Serializer serializer = new JdkSerializer();

        RpcRequest rpcRequest = RpcRequest.builder().svcName(method.getDeclaringClass().getName()).methodName(method.getName())
                .paramType(method.getParameterTypes()).args(args).build();

        try {
            byte[] serialized = serializer.serialize(rpcRequest);
            try (HttpResponse httpResponse = HttpRequest.post("http://localhost:8080").body(serialized).execute()) {
                RpcResponse rpcResponse = serializer.deserialize(httpResponse.bodyBytes(), RpcResponse.class);
                return rpcResponse.getData();
            }
        } catch (IOException e) {
            log.severe(e.getMessage());
        }

        return null;
    }
}
