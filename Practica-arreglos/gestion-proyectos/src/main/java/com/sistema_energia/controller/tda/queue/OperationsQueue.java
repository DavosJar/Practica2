package com.sistema_energia.controller.tda.queue;

import java.util.NoSuchElementException;

import com.sistema_energia.controller.excepction.ListEmptyException;
import com.sistema_energia.controller.tda.list.LinkedList;
import com.sistema_energia.controller.tda.list.Node;

public class OperationsQueue<E> extends LinkedList<E> {
    private  Node<E> front;
    private  Node<E> rear;
    private Integer size;

    public OperationsQueue() {
        super();
        this.front = null;
        this.rear = null;
    }

    public void insertTail(E data) {
        Node<E> newNode = new Node<>(data); 
        if (rear == null) {
            front = newNode; 
            rear = newNode; 
            size++;

        } else {
            rear.setNext(newNode); 
            rear = newNode; 
            size++;

        }
        
    }
    public E removeFront() throws ListEmptyException {
        if (isEmpty()) {
            throw new NoSuchElementException("La cola esta vacia.");
        } else {
            E data = this.front.getData();
            deleteHeader();
            return data;
        }
    }

    public E readRear() {
        if (isEmpty()) {
            throw new NoSuchElementException("La cola esta vacia.");
        } else {
            return front.getData();
        }
    }

    public Node<E> getFront() {
        return front;
    }

    public void setFront(Node<E> front) {
        this.front = front;
    }

    public Node<E> getRear() {
        return rear;
    }

    public void setRear(Node<E> rear) {
        this.rear = rear;
    }


    @Override
    public Integer getSize() {
        return super.getSize();
    }

    @Override
    public void setSize(Integer size) {
        this.size = size;
    }
    
}
