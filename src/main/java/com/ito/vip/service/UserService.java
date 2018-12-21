package com.ito.vip.service;

import java.util.List;

import com.ito.vip.model.User;

public interface UserService {

    public User findUserByUserName(String userName);

    public List<String> getUserAuthorities(Long userId);

    public User createUser(User users);

    public User update(User users);

    public void inactiveUser(Long userId);

    public List<User> getUserList(Long userId);
}
