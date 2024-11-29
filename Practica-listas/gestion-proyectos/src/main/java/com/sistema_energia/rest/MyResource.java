package com.sistema_energia.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sistema_energia.controller.dao.services.ProyectoServices;
import com.sistema_energia.controller.model.Proyecto;
import com.sistema_energia.controller.tda.list.LinkedList;

@Path("myresource")
public class MyResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() throws Exception {
        /*
         * int tiempoInicio;
         * int tiempoFinal;
         * int tiempoTotal;
         * int positio;
         * 
         * LinkedList<Integer> lista;
         * lista = generarListaRandom(60000);
         * LinkedList<Integer> lista1 = lista.cloneList();
         * LinkedList<Integer> lista2 = lista.cloneList();
         * LinkedList<Integer> lista3 = lista.cloneList();
         * LinkedList<Integer> lista4 = lista.cloneList();
         * LinkedList<Integer> lista5 = lista.cloneList();
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
         * tiempoInicio = (int) System.currentTimeMillis();
         * lista3.mergeSort(1);
         * tiempoFinal = (int) System.currentTimeMillis();
         * tiempoTotal = tiempoFinal - tiempoInicio;
         * System.out.println("Tiempo de ejecucion MergeSort : " + tiempoTotal +
         * " milisegundos");
         * Integer value = (int) (Math.random() * 60000);
         * tiempoInicio = (int) System.nanoTime();
         * positio = lista4.linearSearch(value);
         * tiempoFinal = (int) System.nanoTime();
         * tiempoTotal = tiempoFinal - tiempoInicio;
         * System.out.println("Tiempo de ejecucion LinearSearch : " + tiempoTotal +
         * " ns en posicion: " + positio);
         * lista5.mergeSort(1);
         * 
         * tiempoInicio = (int) System.nanoTime();
         * positio = lista5.binarySarch(value);
         * tiempoFinal = (int) System.nanoTime();
         * tiempoTotal = tiempoFinal - tiempoInicio;
         * System.out.println("Tiempo de ejecucion BinarySearch con mergeSort : " +
         * tiempoTotal + " ns en posicion: " + positio);
         */
        ProyectoServices ps = new ProyectoServices();
        LinkedList<Proyecto> lista = ps.listAll();
        System.out.println(lista);
        System.out.println("Lista de proyectos ordenada por fecha");
        LinkedList<Proyecto> lista3 = lista.cloneList();
        lista3.mergeSort("fechafin", 1);
        System.out.println(lista3);

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
