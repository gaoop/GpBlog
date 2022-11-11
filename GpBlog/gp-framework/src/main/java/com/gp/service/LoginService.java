package com.gp.service;

import com.gp.domain.ResponseResult;
import com.gp.domain.entity.User;

public interface   LoginService {
    ResponseResult login(User user);

    ResponseResult logout();

}
