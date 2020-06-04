package com.service.impl;

import com.service.UserService;
import org.springframework.stereotype.Service;

@Service("UserService")
public class UserServiceImpl implements UserService {
    @Override
    public String get() {
        return "测试service";
    }
}
