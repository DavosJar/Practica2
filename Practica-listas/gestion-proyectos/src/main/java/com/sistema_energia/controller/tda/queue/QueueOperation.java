package com.sistema_energia.controller.tda.queue;

import com.sistema_energia.controller.excepction.ListEmptyException;
import com.sistema_energia.controller.excepction.OverFlowException;
import com.sistema_energia.controller.tda.list.LinkedList;

public class QueueOperation<E> extends LinkedList<E> {
    private Integer top;

    public QueueOperation(Integer top) {
        this.top = top;
    }

    public QueueOperation() {

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

    public void enqueue(E data) throws OverFlowException, IndexOutOfBoundsException, ListEmptyException {
        if (verify()) {
            add(data, getSize());
        } else {
            throw new OverFlowException("Cola llena");
        }
    }

    public E dequeue() throws ListEmptyException {
        if (isEmpty()) {
            throw new ListEmptyException("Cola vacia");
        } else {
            return deleteHeader();
        }
    }

    public E peek() throws ListEmptyException {
        return this.getTail().getData();
    }

}
