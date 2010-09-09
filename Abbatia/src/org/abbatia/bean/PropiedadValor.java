/***********************************************************************
 * Module:  PropiedadValor.java
 * Author:  Benja & John
 * Purpose: Defines the Class PropiedadValor
 ***********************************************************************/
package org.abbatia.bean;


public class PropiedadValor {
    private long idDeClave;
    private byte tipo;
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


