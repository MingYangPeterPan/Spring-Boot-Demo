package com.ito.vip.dao.impl;

import com.ito.vip.common.dao.mybatis.MyBatisDAO;
import com.ito.vip.dao.UserDAO;
import com.ito.vip.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "userDAO")
public class UserDAOImpl extends MyBatisDAO<User, Long> implements UserDAO {

    @Override
    public User getUserByName(String username) {
        User user = this.getSqlSession().selectOne(this.getSqlNameSpace()+ "getUserByName", username);
        return user;
    }

    @Override
    public List<String> getUserAuthorities(Long userId) {
        List<String> list = this.getSqlSession().selectList(this.getSqlNameSpace() + "getUserAuthorities", userId);

        if (list.size() == 0) {
            list.add("ALL");
        }
        // 用户权限列表
        return list;
    }

    @Override
    public List<User> getUserList(Long userId) {
        List<User> list = this.getSqlSession().selectList(this.getSqlNameSpace()+ "getUserListByUserId", userId);
        return list;
    }

}
