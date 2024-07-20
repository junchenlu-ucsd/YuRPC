package com.daniel.provider;

import com.daniel.shared.service.UserService;
import com.daniel.shared.model.User;

public class UserServiceImpl implements UserService {

    @Override
    public User getUser(User user) {
        System.out.println("User Name: " + user.getName());
        user.setName(user.getName() + user.getName());
        return user;
    }
}
