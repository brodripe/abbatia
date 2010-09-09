/***********************************************************************
 * Module:  HabilidadesDeLosMonjes.java
 * Author:  Benja & John
 * Purpose: Defines the Class HabilidadesDeLosMonjes
 ***********************************************************************/
package org.abbatia.bean;


public class HabilidadesDeLosMonjes {
    private long idDeMonje;
    private int idDeHabilidad;
    private int nivel;
    private String descripcion;

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
    // Asociacion Link B: ID de Habilidad
    // --------------------------------------------------------------------------

    public void setIdDeHabilidad(int newIdDeHabilidad) {
        this.idDeHabilidad = newIdDeHabilidad;
    }

    public int getIdDeHabilidad() {
        return this.idDeHabilidad;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Nivel
    // --------------------------------------------------------------------------

    public void setNivel(int newNivel) {
        if (newNivel < 0) this.nivel = 0;
        else if (newNivel > 100) this.nivel = 100;
        else
            this.nivel = newNivel;
    }

    public int getNivel() {
        return this.nivel;
    }


    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


    public String getDescripcion() {
        return descripcion;
    }
    // --------------------------------------------------------------------------


}


