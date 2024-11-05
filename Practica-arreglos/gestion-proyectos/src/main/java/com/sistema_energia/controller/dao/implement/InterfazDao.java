package com.sistema_energia.controller.dao.implement;



public interface InterfazDao<T> {
    public void persist(T object) throws Exception;
    public void merge(T object, Integer Index) throws Exception;
    @SuppressWarnings("rawtypes")
    public T[] listAll();
    public T get(Integer id) throws Exception;
    public void delete(Integer id) throws Exception;
}
