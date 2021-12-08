package com.boshfish.service;

import com.boshfish.common.RestResult;
import com.boshfish.model.User;

public interface UserService {

    RestResult<String> registerUser(User user);

    RestResult<User> login(User user);

    User getUserByToken(String token);

}
