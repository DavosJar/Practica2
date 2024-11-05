package com.sistema_energia.controller.dao.implement;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

// Clase AdapterDao
public class AdapterDao<T> implements InterfazDao<T> {
    private final Class<T> clazz;
    private Gson g;
    public String URL = "media" + File.separator;

    public AdapterDao(Class<T> clazz) {
        this.clazz = clazz;
        this.g = new Gson();
    }

    @Override
    public void persist(T object) throws Exception {
        try {
            T[] array = listAll();
            T[] newArray = (T[]) Array.newInstance(clazz, array.length + 1);
            System.arraycopy(array, 0, newArray, 0, array.length);
            newArray[array.length] = object;

            String info = g.toJson(newArray);
            saveFile(info);
        } catch (Exception e) {
            throw new Exception("Error al guardar el objeto: " + e.getMessage(), e);
        }
    }

    @Override
    public void merge(T object, Integer index) throws Exception {
        try {
            T[] array = listAll();
            if (index < 0 || index >= array.length) {
                throw new ArrayIndexOutOfBoundsException(index);
            }
            array[index] = object;

            String info = g.toJson(array);
            saveFile(info);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new Exception("Índice fuera de límites: " + index, e);
        } catch (Exception e) {
            throw new Exception("Error al combinar el objeto: " + e.getMessage(), e);
        }
    }

    @Override
    public T[] listAll(){
        try {
            String data = readFile();
            if (data == null || data.isEmpty()) {
                throw new Exception("Error: No se pudo leer datos del archivo.");
            }
            Type arrayType = Array.newInstance(clazz, 0).getClass();
            T[] arrayObjects = g.fromJson(data, arrayType);

            return arrayObjects != null ? arrayObjects : (T[]) Array.newInstance(clazz, 0);
        } catch (JsonSyntaxException e) {
            System.err.println("Error al convertir el JSON a objeto: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al listar todos los objetos: " + e.getMessage());
        }
        return (T[]) Array.newInstance(clazz, 0);
    }

    @Override
    public T get(Integer id) throws Exception {
        try {
            return listAll()[id - 1];
        } catch (IndexOutOfBoundsException e) {
            throw new Exception("ID fuera de límites: " + id, e);
        } catch (Exception e) {
            throw new Exception("Error al obtener el objeto con ID " + id + ": " + e.getMessage(), e);
        }
    }
    @Override
    public void delete(Integer index)throws Exception{
        T[] array = listAll();
        T[] newArray = (T[]) Array.newInstance(clazz, array.length - 1);
        array[index] = null;
        int count = 0;
        for (T t : array) {
            if (t != null) {
                newArray[count] = t;
                count++;
            }
        }
        String info = g.toJson(newArray);
        saveFile(info);
    }

    private String readFile() throws Exception {
        File file = new File(URL + clazz.getSimpleName() + ".json");
        if (!file.exists()) {
            return "[]";
        }

        try (Scanner in = new Scanner(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            while (in.hasNext()) {
                sb.append(in.next());
            }
            return sb.toString();
        } catch (Exception e) {
            throw new Exception("Error al leer el archivo: " + e.getMessage(), e);
        }
    }

    private void saveFile(String info) throws Exception {
        File dir = new File(URL);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new Exception("Error al crear el directorio: " + URL);
            }
        }

        File file = new File(URL + clazz.getSimpleName() + ".json");
        try (FileWriter f = new FileWriter(file)) {
            f.write(info);
            f.flush();
        } catch (Exception e) {
            throw new Exception("Error al guardar el archivo: " + e.getMessage(), e);
        }
    }
}