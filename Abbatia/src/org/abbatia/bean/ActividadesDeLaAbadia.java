/***********************************************************************
 * Module:  ActividadesDeLaAbadia.java
 * Author:  Benja & John
 * Purpose: Defines the Class ActividadesDeLaAbadia
 ***********************************************************************/
package org.abbatia.bean;


public class ActividadesDeLaAbadia {
    private long idDeAbadia;
    private long idDeActividad;
    private double nivel;

    // --------------------------------------------------------------------------
    // Asociacion Link A: ID de Abadia
    // --------------------------------------------------------------------------
    public void setIdDeAbadia(long newIdDeAbadia) {
        this.idDeAbadia = newIdDeAbadia;
    }

    public long getIdDeAbadia() {
        return this.idDeAbadia;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Asociacion Link B: ID de Actividad
    // --------------------------------------------------------------------------

    public void setIdDeActividad(long newIdDeActividad) {
        this.idDeActividad = newIdDeActividad;
    }

    public long getIdDeActividad() {
        return this.idDeActividad;
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


