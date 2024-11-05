package com.sistema_energia.controller.tda.queue;

import com.sistema_energia.controller.excepction.ListEmptyException;


public class Queue<E> extends OperationsQueue<E> {
    public Queue() {
        super();
    }

    public void enqueue(E data) {
        insertTail(data);
    }

    public E dequeue() throws ListEmptyException {
        return (E) removeFront();
    }

    public E peek() {
        return (E) readRear();
    }
    
}
