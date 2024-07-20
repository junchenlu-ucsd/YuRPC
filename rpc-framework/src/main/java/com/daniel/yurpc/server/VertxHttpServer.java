package com.daniel.yurpc.server;

import io.vertx.core.Vertx;

public class VertxHttpServer implements HttpServer {

    @Override
    public void doStart(int port) {
        Vertx vertx = Vertx.vertx();
        io.vertx.core.http.HttpServer server = vertx.createHttpServer();

        server.requestHandler(new HttpServerHandler());
//        server.requestHandler(request -> {
//            System.out.println("Request: " + request.method() + " " + request.uri());
//            request.response().putHeader("content-type", "text/plain").end("Hello from Vert.x HTTP Server!");
//        });

        server.listen(port, status -> {
            if (status.succeeded()) {
                System.out.println("Vert.x HTTP Server on port " + port + " ...");
            } else {
                System.err.println("Fail: " + status.cause());
            }
        });
    }
}
