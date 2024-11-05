package com.sistema_energia.controller.excepction;

public class ListEmptyException extends Exception {
    public ListEmptyException() {}
    public ListEmptyException(String msg) {
        super(msg);
    }    
}
