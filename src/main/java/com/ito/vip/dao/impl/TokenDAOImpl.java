package com.ito.vip.dao.impl;

import org.springframework.stereotype.Repository;

import com.ito.vip.common.dao.mybatis.MyBatisDAO;
import com.ito.vip.dao.TokenDAO;
import com.ito.vip.model.Token;

@Repository(value = "tokenDAO")
public class TokenDAOImpl extends MyBatisDAO<Token, Long> implements TokenDAO {

    @Override
    public Token getTokenByValue(String valueField) {
        Token token = this.getSqlSession().selectOne(this.getSqlNameSpace()+ "getTokenByValue", valueField);
        return token;
    }

    @Override
    public void deleteTokenByValue(String valueField) {
        this.getSqlSession().delete(this.getSqlNameSpace()+ "deleteTokenByValue", valueField);
    }
}
