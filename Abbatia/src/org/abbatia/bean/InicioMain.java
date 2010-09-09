package org.abbatia.bean;

import java.io.Serializable;
import java.util.ArrayList;

/*
     Página de Incio - Bean
*/
public class InicioMain implements Serializable {
    private ArrayList ultimasConexiones;
    private ArrayList ultimosMensForos;
    private ArrayList ultimosAcontecimientos;
    private ArrayList ultimosMejoresAbadias;
    private ArrayList ultimasNovedades;
    private int countMejoresAbadias;


    public void setUltimasConexiones(ArrayList ultimasConexiones) {
        this.ultimasConexiones = ultimasConexiones;
    }

    public ArrayList getUltimasConexiones() {
        return ultimasConexiones;
    }


    public void setUltimosMensForos(ArrayList ultimosMensForos) {
        this.ultimosMensForos = ultimosMensForos;
    }

    public ArrayList getUltimosMensForos() {
        return ultimosMensForos;
    }

    public void setUltimosAcontecimientos(ArrayList ultimosAcontecimientos) {
        this.ultimosAcontecimientos = ultimosAcontecimientos;
    }

    public ArrayList getUltimosAcontecimientos() {
        return ultimosAcontecimientos;
    }

    public void setUltimosMejoresAbadias(ArrayList ultimosMejoresAbadias) {
        this.ultimosMejoresAbadias = ultimosMejoresAbadias;
    }

    public ArrayList getUltimosMejoresAbadias() {
        return ultimosMejoresAbadias;
    }


    public void setUltimasNovedades(ArrayList ultimasNovedades) {
        this.ultimasNovedades = ultimasNovedades;
    }

    public void setCountMejoresAbadias(int countMejoresAbadias) {
        this.countMejoresAbadias = countMejoresAbadias;
    }

    public ArrayList getUltimasNovedades() {
        return ultimasNovedades;
    }

    public int getCountMejoresAbadias() {
        return countMejoresAbadias;
    }

}
