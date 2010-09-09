package org.abbatia.bean;

import java.util.Collection;
import java.util.Hashtable;

public class Paginacion {
    private String consultaTipo;
    private Hashtable datos;
    private int actual;
    private int totalRegistros;
    private int registrosPorPagina;
    private int numPaginas;


    public Paginacion() {
    }

    public void setConsulta(String consulta) {
        this.consultaTipo = consulta;
    }


    public String getConsultaTipo() {
        return consultaTipo;
    }


    public void setDatos(Hashtable datos) {
        this.datos = datos;
    }


    public Hashtable getDatos() {
        return datos;
    }

    public void setActual(int actual) {
        this.actual = actual;
    }


    public int getActual() {
        return actual;
    }


    public void setTotalRegistros(int totalRegistros) {
        this.totalRegistros = totalRegistros;
    }


    public int getTotalRegistros() {
        return totalRegistros;
    }


    public void setRegistrosPorPagina(int registrosPorPagina) {
        this.registrosPorPagina = registrosPorPagina;
    }


    public int getRegistrosPorPagina() {
        return registrosPorPagina;
    }


    public void setNumPaginas(int numPaginas) {
        this.numPaginas = numPaginas;
    }

    //devuelve el numero de páginas
    public int getNumPaginas() {
        return numPaginas;
    }

    //devuelve los datos de la siguiente página a partir del cursor
    public Hashtable getNext() {
        return null;
    }

    //devuelve la página anterior
    public Hashtable getPrev() {
        return null;
    }

    //devuelve la primera página
    public Hashtable getFirst() {
        return null;
    }

    //devuelve los datos de la última página
    public Hashtable getLast() {
        Hashtable h = new Hashtable();

        Collection c = datos.values();
        return null;
    }

    //resetea el cursor y elimina de memoria
    //el bean vaciando el hashtable.
    public void reset() {
        this.setActual(0);
    }

}