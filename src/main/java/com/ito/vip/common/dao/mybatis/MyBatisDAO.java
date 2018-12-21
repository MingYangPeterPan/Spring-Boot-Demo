package com.ito.vip.common.dao.mybatis;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class MyBatisDAO<T, K> extends SqlSessionDaoSupport {

    private static final String SQL_NAME_SPACE_SEPARATOR = ".";
    private static final String SQL_ID_CREATE = "create";
    private static final String SQL_ID_UPDATE = "update";
    private static final String SQL_ID_DELETE = "delete";
    private static final String SQL_ID_GET_BY_ID = "getById";

    @SuppressWarnings("unchecked")
    private Class<T> getActuallClassType(){
        Class<T> classType = null;
        ParameterizedType parameterizedClass = (ParameterizedType)this.getClass().getGenericSuperclass();
        classType = (Class<T>)parameterizedClass.getActualTypeArguments()[0];
        return classType;
    }

    @Autowired
    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        super.setSqlSessionTemplate(sqlSessionTemplate);
    }

    protected String getSqlNameSpace(){
        String sqlNameSpace = StringUtils.EMPTY;
        Class<T> classType = this.getActuallClassType();
        if(classType!=null){
            sqlNameSpace= classType.getName();
        }
        return sqlNameSpace+ SQL_NAME_SPACE_SEPARATOR;
    }

    public T create(T entity){
        String insertStatement = this.getSqlNameSpace() + SQL_ID_CREATE;
        this.getSqlSession().insert(insertStatement, entity);
        return entity;
    }

    public T update(T entity){
        String updateStatement = this.getSqlNameSpace() + SQL_ID_UPDATE;
        this.getSqlSession().update(updateStatement, entity);
        return entity;
    }

    @SuppressWarnings("unchecked")
    public T getById(K id){
        String selectStatement = this.getSqlNameSpace() + SQL_ID_GET_BY_ID;
        T entity = (T)this.getSqlSession().selectOne(selectStatement, id);
        return entity;
    }

    public void delete(K id){
        //delete record by primary key, and if error happens, then throw exception
        String deleteStatement = this.getSqlNameSpace() + SQL_ID_DELETE;
        this.getSqlSession().delete(deleteStatement, id);
    }

    public List<Map<String, Object>> selectResultAsListMap(String queryName, Object parameter){
        List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
        String selectStatement = this.getSqlNameSpace() + queryName;
        result = this.getSqlSession().selectList(selectStatement, parameter);
        return result;
    }
}
