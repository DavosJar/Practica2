package com.sistema_energia.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sistema_energia.controller.tda.list.LinkedList;

@Path("myresource")
public class MyResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() throws Exception {

        int tiempoInicio;
        int tiempoFinal;
        int tiempoTotal;
        int positio;
        Integer cant = 10000;

        LinkedList<Integer> lista;
        lista = generarListaRandom(cant);
        LinkedList<Integer> lista1 = lista.cloneList();
        LinkedList<Integer> lista2 = lista.cloneList();
        LinkedList<Integer> lista3 = lista.cloneList();
        LinkedList<Integer> lista4 = lista.cloneList();
        LinkedList<Integer> lista7 = lista.cloneList();

        LinkedList<Integer> lista5;
        LinkedList<Integer> lista6;

        System.out.println("Lista generada con " + cant + " elementos");

        /*
         * System.out.println("-----------------------------------------");
         * tiempoInicio = (int) System.currentTimeMillis();
         * lista1.shellSort(1);
         * tiempoFinal = (int) System.currentTimeMillis();
         * tiempoTotal = tiempoFinal - tiempoInicio;
         * System.out.println("Tiempo de ejecucion ShellSort : " + tiempoTotal +
         * " milisegundos");
         * tiempoInicio = (int) System.currentTimeMillis();
         * lista2.quickSort(1);
         * tiempoFinal = (int) System.currentTimeMillis();
         * tiempoTotal = tiempoFinal - tiempoInicio;
         * System.out.println("Tiempo de ejecucion QuickSort : " + tiempoTotal +
         * " milisegundos");
         * 
         * tiempoInicio = (int) System.currentTimeMillis();
         * lista3.mergeSort(1);
         * tiempoFinal = (int) System.currentTimeMillis();
         * tiempoTotal = tiempoFinal - tiempoInicio;
         * System.out.println("Tiempo de ejecucion MergeSort : " + tiempoTotal +
         * " milisegundos");
         */
        Integer value = (int) (Math.random() * 6500);

        tiempoInicio = (int) System.nanoTime();
        lista5 = lista4.secuentialSearch(value);
        tiempoFinal = (int) System.nanoTime();
        tiempoTotal = tiempoFinal - tiempoInicio;
        System.out.println("Tiempo de ejecucion sequentialSearch : " +
                tiempoTotal + " ns: " + lista5.getSize() + " elementos  con valor: " + value);

        tiempoInicio = (int) System.nanoTime();
        lista6 = lista7.linearBinarySearch(value);
        tiempoFinal = (int) System.nanoTime();
        tiempoTotal = tiempoFinal - tiempoInicio;
        System.out.println("Tiempo de ejecucion LinearBinarySearch : " +
                tiempoTotal + " ns: " + lista6.getSize() + " elementos  con valor: " + value);
        return "Got it!";

    }

    LinkedList<Integer> generarListaRandom(Integer cantidad) {
        LinkedList<Integer> lista = new LinkedList<>();
        for (int i = 0; i < cantidad; i++) {
            lista.add((int) (Math.random() * 5000));
        }
        return lista;
    }

}
