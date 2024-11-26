package com.sistema_energia.controller.tda.queue;

import com.sistema_energia.controller.excepction.ListEmptyException;

public class Queue<E> {
    private QueueOperation<E> queueOperation;

    public Queue(Integer cant) {
        queueOperation = new QueueOperation<>(cant);
    }

    public Queue() {
        queueOperation = new QueueOperation<>();
    }

    public void queue(E data) throws Exception {
        this.queueOperation.enqueue(data);
    }

    public E dequeue() throws Exception {
        return this.queueOperation.dequeue();
    }

    public Integer getSize() {
        return this.queueOperation.getSize();
    }

    public void clear() {
        this.queueOperation.reset();
    }

    public E peek() throws ListEmptyException {
        return this.queueOperation.peek();
    }

    public boolean isEmpty() {
        return queueOperation.isEmpty();
    }

    public void print() {
        System.out.println("COLA");
        System.out.println(this.queueOperation.toString());
        System.out.println("********");
    }

    @Override
    public String toString() {
        return this.queueOperation.toString();
    }
}