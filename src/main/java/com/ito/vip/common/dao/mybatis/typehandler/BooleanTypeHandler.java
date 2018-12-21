package com.ito.vip.common.dao.mybatis.typehandler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(value = {Boolean.class})
@MappedJdbcTypes(value = {JdbcType.BOOLEAN})
@SuppressWarnings("unused")
public class BooleanTypeHandler implements TypeHandler<Boolean> {

    @Override
    public Boolean getResult(ResultSet resultSet, String name) throws SQLException {
        Boolean restlt = Boolean.FALSE;
        Boolean content = resultSet.getBoolean(name);
        if(content){
            restlt = Boolean.TRUE;
        }
        return restlt;
    }

    @Override
    public Boolean getResult(ResultSet resultSet, int index) throws SQLException {
        Boolean restlt = Boolean.FALSE;
        Boolean content = resultSet.getBoolean(index);
        if(content){
            restlt = Boolean.TRUE;
        }

        return restlt;
    }

    @Override
    public Boolean getResult(CallableStatement callableStatement, int index) throws SQLException {
         Boolean restlt = Boolean.FALSE;
         Boolean content = callableStatement.getBoolean(index);
         if(content){
             restlt = Boolean.TRUE;
         }
         return restlt;
    }

    @Override
    public void setParameter(PreparedStatement preparedStatement, int index, Boolean value, JdbcType jdbcType) throws SQLException {
         if(value){
             preparedStatement.setBoolean(index, Boolean.TRUE);
         }else{
             preparedStatement.setBoolean(index, Boolean.FALSE);
         }
    }

}
