/***********************************************************************
 * Module:  RecursosDeLaAbadia.java
 * Author:  Benja & John
 * Purpose: Defines the Class RecursosDeLaAbadia
 ***********************************************************************/
package org.abbatia.bean;

public class RecursosDeLaAbadia {
    private long idDeAbadia;
    private int idDeRecurso;
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
    // Asociacion Link B: ID de Recurso
    // --------------------------------------------------------------------------

    public void setIdDeRecurso(int newIdDeRecurso) {
        this.idDeRecurso = newIdDeRecurso;
    }

    public int getIdDeRecurso() {
        return this.idDeRecurso;
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


