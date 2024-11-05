package com.sistema_energia.controller.tda.stack;

import com.sistema_energia.controller.excepction.ListEmptyException;
import com.sistema_energia.controller.tda.list.LinkedList;

public class StackOperation<E> extends LinkedList<E> {
    private Integer top;

    public StackOperation(Integer top) {
        this.top = top;
    }

    public Integer getTop() {
        return top;
    }

    public void setTop(Integer top) {
        this.top = top;
    }

    public Boolean verify() {
        return getSize().intValue() <= top.intValue();
    }
    public void push(E data) throws IndexOutOfBoundsException, ListEmptyException {
        if (verify()) {
            add(data, 0);
        }
    }
    public E pop() throws ListEmptyException {
        if (isEmpty()) {
            throw new ListEmptyException("Pila vacia");
        } else {
            return deleteHeader();
        }
    }
}