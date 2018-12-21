package com.ito.vip.common.dao;

public interface BaseDAO<T,K> {

    public T create(T entity);

    public T update(T entity);

    public T getById(K id);

    public void delete(K id);
}
