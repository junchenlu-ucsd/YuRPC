package com.daniel.yurpc.server;

import com.daniel.yurpc.model.RpcRequest;
import com.daniel.yurpc.model.RpcResponse;
import com.daniel.yurpc.registry.LocalRegistry;
import com.daniel.yurpc.serializer.JdkSerializer;
import com.daniel.yurpc.serializer.Serializer;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import lombok.extern.java.Log;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Log
public class HttpServerHandler implements Handler<HttpServerRequest> {
    @Override
    public void handle(HttpServerRequest httpServerRequest) {
        final Serializer serializer = new JdkSerializer();
        System.out.println("Request: " + httpServerRequest.method() + " " + httpServerRequest.uri());

        // asynchronous: does not block the main thread while waiting for the request body
        httpServerRequest.bodyHandler(body -> {
            byte[] bytes = body.getBytes();
            RpcRequest rpcRequest = null;
            try {
                rpcRequest = serializer.deserialize(bytes, RpcRequest.class);
            } catch (IOException e) {
                log.severe(e.getMessage());
            }

            // then, construct the rpc response obj
            RpcResponse rpcResponse = new RpcResponse();
            if (rpcRequest == null) {
                rpcResponse.setMsg("rpc request is null");
                this.doResponse(httpServerRequest, rpcResponse, serializer);
                return;
            }

            try {
                Class<?> implClass = LocalRegistry.getSvc(rpcRequest.getSvcName());
                // Using Java Reflection
                Method method = implClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParamType());
                Object result = method.invoke(implClass.newInstance(), rpcRequest.getArgs());

                rpcResponse.setData(result);
                rpcResponse.setDataType(method.getReturnType());
                rpcResponse.setMsg("ok");
            } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
                     InstantiationException e) {
                log.severe(e.getMessage());

                rpcResponse.setMsg(e.getMessage());
                rpcResponse.setException(e);
            }

            this.doResponse(httpServerRequest, rpcResponse, serializer);
        });
    }

    private void doResponse(HttpServerRequest httpServerRequest, RpcResponse rpcResponse, Serializer serializer) {
        HttpServerResponse httpServerResponse = httpServerRequest.response()
                .putHeader("content-type", "application/json");
        try {
            byte[] serialized = serializer.serialize(rpcResponse);
            httpServerResponse.end(Buffer.buffer(serialized));
        } catch (IOException e) {
            log.severe(e.getMessage());
            httpServerResponse.end(Buffer.buffer());
        }
    }
}
