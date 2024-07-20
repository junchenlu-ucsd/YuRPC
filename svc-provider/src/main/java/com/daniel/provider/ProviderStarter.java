package com.daniel.provider;

import com.daniel.shared.service.UserService;
import com.daniel.yurpc.registry.LocalRegistry;
import com.daniel.yurpc.server.HttpServer;
import com.daniel.yurpc.server.VertxHttpServer;

public class ProviderStarter {

    public static void main(String[] args) {

        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        HttpServer server = new VertxHttpServer();
        server.doStart(8080);
    }
}
