package com.wavebigfish.service;

import com.wavebigfish.common.RestResult;
import com.wavebigfish.model.User;

public interface UserService {

    RestResult<String> registerUser(User user);

    RestResult<User> login(User user);

    User getUserByToken(String token);

}
