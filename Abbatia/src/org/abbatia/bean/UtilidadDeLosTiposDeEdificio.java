/***********************************************************************
 * Module:  UtilidadDeLosTiposDeEdificio.java
 * Author:  Benja & John
 * Purpose: Defines the Class UtilidadDeLosTiposDeEdificio
 ***********************************************************************/
package org.abbatia.bean;


public class UtilidadDeLosTiposDeEdificio {
    private long idDeTipoDeEdificio;
    private long idDeUtilidad;
    private double nivel;

    // --------------------------------------------------------------------------
    // Asociacion Link A: ID de Tipo de Edificio
    // --------------------------------------------------------------------------
    public void setIdDeTipoDeEdificio(long newIdDeTipoDeEdificio) {
        this.idDeTipoDeEdificio = newIdDeTipoDeEdificio;
    }

    public long getIdDeTipoDeEdificio() {
        return this.idDeTipoDeEdificio;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Asociacion Link B: ID de Utilidad
    // --------------------------------------------------------------------------

    public void setIdDeUtilidad(long newIdDeUtilidad) {
        this.idDeUtilidad = newIdDeUtilidad;
    }

    public long getIdDeUtilidad() {
        return this.idDeUtilidad;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Nivel
    // --------------------------------------------------------------------------

    public void setNivel(double newNivel) {
        this.nivel = newNivel;
    }

    public double getNivel() {
        return this.nivel;
    }
    // --------------------------------------------------------------------------


}


