/***********************************************************************
 * Module:  PropiedadesDeLosMonjes.java
 * Author:  Benja & John
 * Purpose: Defines the Class PropiedadesDeLosMonjes
 ***********************************************************************/
package org.abbatia.bean;


public class PropiedadesDeLosMonjes {
    private long idDeMonje;
    private long idDePropiedad;
    private double nivel;

    // --------------------------------------------------------------------------
    // Asociacion Link A: ID de Monje
    // --------------------------------------------------------------------------
    public void setIdDeMonje(long newIdDeMonje) {
        this.idDeMonje = newIdDeMonje;
    }

    public long getIdDeMonje() {
        return this.idDeMonje;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Asociacion Link B: ID de Propiedad
    // --------------------------------------------------------------------------

    public void setIdDePropiedad(long newIdDePropiedad) {
        this.idDePropiedad = newIdDePropiedad;
    }

    public long getIdDePropiedad() {
        return this.idDePropiedad;
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


