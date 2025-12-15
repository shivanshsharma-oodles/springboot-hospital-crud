package com.yt.jpa.hospitalManagement.service;

import com.yt.jpa.hospitalManagement.entity.User;

public interface UserService {
    String authenticate(User user);

    String registerUser(User user);
}
