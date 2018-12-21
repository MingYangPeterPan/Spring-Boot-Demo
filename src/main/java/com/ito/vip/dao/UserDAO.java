package com.ito.vip.dao;

import java.util.List;

import com.ito.vip.common.dao.BaseDAO;
import com.ito.vip.model.User;

public interface UserDAO extends BaseDAO<User, Long> {

    public User getUserByName(String username);
    
    public List<String> getUserAuthorities(Long userId);
   
    public List<User> getUserList(Long userId);
}
