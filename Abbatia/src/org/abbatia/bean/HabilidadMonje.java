/***********************************************************************
 * Module:  Habilidad.java
 * Author:  Benja & John
 * Purpose: Defines the Class HabilidadMonje
 ***********************************************************************/
package org.abbatia.bean;

public class HabilidadMonje {
    private int idMonje;
    private int idHabilidad;
    private String descripcion;
    private double valorInicial;
    private double valorActual;

    public int getIdMonje() {
        return idMonje;
    }

    public void setIdMonje(int idMonje) {
        this.idMonje = idMonje;
    }

    public int getIdHabilidad() {
        return idHabilidad;
    }

    public void setIdHabilidad(int idHabilidad) {
        this.idHabilidad = idHabilidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getValorInicial() {
        return valorInicial;
    }

    public void setValorInicial(double valorInicial) {
        this.valorInicial = valorInicial;
    }

    public double getValorActual() {
        return valorActual;
    }

    public void setValorActual(double valorActual) {
        this.valorActual = valorActual;
    }


}


