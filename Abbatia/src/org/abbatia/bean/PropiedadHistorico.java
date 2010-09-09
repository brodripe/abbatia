/***********************************************************************
 * Module:  PropiedadHistorico.java
 * Author:  Benja & John
 * Purpose: Defines the Class PropiedadHistorico
 ***********************************************************************/
package org.abbatia.bean;


public class PropiedadHistorico {
    private long idDeClave;
    private byte tipo;
    private String fecha;
    private String valor;
    private long idDePropiedad;

    // --------------------------------------------------------------------------
    // Id de Clave
    // --------------------------------------------------------------------------
    public void setIdDeClave(long newIdDeClave) {
        this.idDeClave = newIdDeClave;
    }

    public long getIdDeClave() {
        return this.idDeClave;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Tipo
    // --------------------------------------------------------------------------

    public void setTipo(byte newTipo) {
        this.tipo = newTipo;
    }

    public byte getTipo() {
        return this.tipo;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Fecha
    // --------------------------------------------------------------------------

    public void setFecha(String newFecha) {
        this.fecha = newFecha;
    }

    public String getFecha() {
        return this.fecha;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Valor
    // --------------------------------------------------------------------------

    public void setValor(String newValor) {
        this.valor = newValor;
    }

    public String getValor() {
        return this.valor;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Asociacion A: ID de Propiedad
    // --------------------------------------------------------------------------

    public void setIdDePropiedad(long newIdDePropiedad) {
        this.idDePropiedad = newIdDePropiedad;
    }

    public long getIdDePropiedad() {
        return this.idDePropiedad;
    }
    // --------------------------------------------------------------------------


}


