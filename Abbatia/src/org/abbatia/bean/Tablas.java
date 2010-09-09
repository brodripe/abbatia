/***********************************************************************
 * Module:  Tablas.java
 * Author:  Benja & John
 * Purpose: Defines the Class Tablas
 ***********************************************************************/
package org.abbatia.bean;

import java.io.Serializable;

public class Tablas implements Serializable {
    private String codigo;
    private String subcodigo;
    private int idiomaid;
    private long orden;
    private String descripcion;
    private String valor;

    public Tablas() {

    }

    public Tablas(String newCodigo, String newSubcodigo, int newIdiomaid, long newOrden, String newDescription, String newValor) {
        this.codigo = newCodigo;
        this.subcodigo = newSubcodigo;
        this.idiomaid = newIdiomaid;
        this.orden = newOrden;
        this.descripcion = newDescription;
        this.valor = newValor;
    }

    // --------------------------------------------------------------------------
    // Código
    // --------------------------------------------------------------------------
    public void setCodigo(String newCodigo) {
        this.codigo = newCodigo;
    }

    public String getCodigo() {
        return this.codigo;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Subcódigo
    // --------------------------------------------------------------------------

    public void setSubcodigo(String newSubcodigo) {
        this.subcodigo = newSubcodigo;
    }

    public String getSubcodigo() {
        return this.subcodigo;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Orden
    // --------------------------------------------------------------------------

    public void setOrden(long newOrden) {
        this.orden = newOrden;
    }

    public long getOrden() {
        return this.orden;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Descripción
    // --------------------------------------------------------------------------

    public void setDescripcion(String newDescripcion) {
        this.descripcion = newDescripcion;
    }

    public String getDescripcion() {
        return this.descripcion;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Valor
    // --------------------------------------------------------------------------

    public void setValor(String newValor) {
        this.valor = newValor;
    }

    public void setIdiomaid(int idiomaid) {
        this.idiomaid = idiomaid;
    }

    public String getValor() {
        return this.valor;
    }

    public int getIdiomaid() {
        return idiomaid;
    }

    // --------------------------------------------------------------------------


}


