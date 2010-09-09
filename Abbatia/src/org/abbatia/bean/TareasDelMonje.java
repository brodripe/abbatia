/***********************************************************************
 * Module:  TareasDelMonje.java
 * Author:  Benja & John
 * Purpose: Defines the Class TareasDelMonje
 ***********************************************************************/
package org.abbatia.bean;


public class TareasDelMonje {
    private long idDePeriodo;
    private long idDeMonje;
    private long idDeActividad;

    // --------------------------------------------------------------------------
    // Asociacion A: ID de Periodo
    // --------------------------------------------------------------------------
    public void setIdDePeriodo(long newIdDePeriodo) {
        this.idDePeriodo = newIdDePeriodo;
    }

    public long getIdDePeriodo() {
        return this.idDePeriodo;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Asociacion A: ID de Monje
    // --------------------------------------------------------------------------

    public void setIdDeMonje(long newIdDeMonje) {
        this.idDeMonje = newIdDeMonje;
    }

    public long getIdDeMonje() {
        return this.idDeMonje;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Asociacion A: ID de Actividad
    // --------------------------------------------------------------------------

    public void setIdDeActividad(long newIdDeActividad) {
        this.idDeActividad = newIdDeActividad;
    }

    public long getIdDeActividad() {
        return this.idDeActividad;
    }
    // --------------------------------------------------------------------------


}


