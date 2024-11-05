package com.sistema_energia.controller.tda.stack;

public class Stack<E> extends StackOperation<E> {
    public Stack(Integer top) {
        super(top);
    }

    public void push(E data) {
        try {
            push(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public E pop() {
        try {
            return pop();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public E peek() {
        try {
            return peek();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
}
