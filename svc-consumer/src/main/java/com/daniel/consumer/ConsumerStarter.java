package com.daniel.consumer;

import com.daniel.shared.model.User;
import com.daniel.shared.service.UserService;
import com.daniel.yurpc.proxy.ServiceProxyFactory;

public class ConsumerStarter {

    public static void main(String[] args) {
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("daniel");

        assert userService != null;
        user = userService.getUser(user);  // invoke RPC
        if (user != null) {
            System.out.println("After RPC: " + user.getName());
        } else {
            System.out.println("user == null");
        }
    }
}
