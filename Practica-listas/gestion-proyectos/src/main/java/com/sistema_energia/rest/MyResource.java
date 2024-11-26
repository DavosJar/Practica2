package com.sistema_energia.rest;

import com.sistema_energia.controller.tda.list.LinkedList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
public class MyResource {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        int tiempoInicio;
        int tiempoFinal;
        int tiempoTotal;
        int positio;

        LinkedList<Integer> lista;
        lista = generarListaRandom(60000);
        LinkedList<Integer> lista1 = lista.cloneList();
        LinkedList<Integer> lista2 = lista.cloneList();
        LinkedList<Integer> lista3 = lista.cloneList();
        LinkedList<Integer> lista4 = lista.cloneList();
        LinkedList<Integer> lista5 = lista.cloneList();
        tiempoInicio = (int) System.currentTimeMillis();
        lista1.shellSort(1);
        tiempoFinal = (int) System.currentTimeMillis();
        tiempoTotal = tiempoFinal - tiempoInicio;
        System.out.println("Tiempo de ejecucion ShellSort : " + tiempoTotal + " milisegundos");
        tiempoInicio = (int) System.currentTimeMillis();
        lista2.quickSort(1);
        tiempoFinal = (int) System.currentTimeMillis();
        tiempoTotal = tiempoFinal - tiempoInicio;
        System.out.println("Tiempo de ejecucion QuickSort : " + tiempoTotal + " milisegundos");
        tiempoInicio = (int) System.currentTimeMillis();
        lista3.mergeSort(1);
        tiempoFinal = (int) System.currentTimeMillis();
        tiempoTotal = tiempoFinal - tiempoInicio;
        System.out.println("Tiempo de ejecucion MergeSort : " + tiempoTotal + " milisegundos");
        Integer value = (int) (Math.random() * 60000);
        tiempoInicio = (int) System.nanoTime();
        positio = lista4.linearSearch(value);
        tiempoFinal = (int) System.nanoTime();
        tiempoTotal = tiempoFinal - tiempoInicio;
        System.out
                .println("Tiempo de ejecucion LinearSearch : " + tiempoTotal + " ns en posicion: " + positio);
        lista5.mergeSort(1);

        tiempoInicio = (int) System.nanoTime();
        positio = lista5.binarySarch(value);
        tiempoFinal = (int) System.nanoTime();
        tiempoTotal = tiempoFinal - tiempoInicio;
        System.out.println("Tiempo de ejecucion BinarySearch con mergeSort : " + tiempoTotal
                + " ns en posicion: " + positio);

        return "Got it!";

    }

    LinkedList<Integer> generarListaRandom(Integer cantidad) {
        LinkedList<Integer> lista = new LinkedList<>();
        for (int i = 0; i < cantidad; i++) {
            lista.add((int) (Math.random() * 60000));
        }
        return lista;
    }

}
