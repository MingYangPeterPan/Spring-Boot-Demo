package com.ito.vip.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ito.vip.common.annotation.CommonTransactional;
import com.ito.vip.dao.UserDAO;
import com.ito.vip.model.User;
import com.ito.vip.service.UserService;

@Service(value = "userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public User findUserByUserName(String userName){
        User user = userDAO.getUserByName(userName);
        return user;
    }

    @Override
    @CommonTransactional
    public User createUser(User users) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = users.getPassword();
        users.setPassword(encoder.encode(rawPassword));
        users.setIsActive(true);

        User user = userDAO.create(users);
        return user;
    }

    @CommonTransactional
    public User update(User users) {
        User user = userDAO.update(users);
        return user;
    }

    @Override
    @CommonTransactional
    public void inactiveUser(Long userId) {
        User user = userDAO.getById(userId);
        //Step 1: update as inactive status
        user.setIsActive(Boolean.FALSE);

        this.update(user);
    }

    @Override
    public List<String> getUserAuthorities(Long userId) {
        return userDAO.getUserAuthorities(userId);
    }

    @Override
    public List<User> getUserList(Long userId) {
        return userDAO.getUserList(userId);
    }

}
