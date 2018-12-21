package com.ito.vip.dao;

import com.ito.vip.common.dao.BaseDAO;
import com.ito.vip.model.Token;

public interface TokenDAO extends BaseDAO<Token, Long> {

    public Token getTokenByValue(String valueField);

    public void deleteTokenByValue(String valueField);
}
