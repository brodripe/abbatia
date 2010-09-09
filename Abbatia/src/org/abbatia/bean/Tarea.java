/***********************************************************************
 * Module:  Tarea.java
 * Author:  Benja & John
 * Purpose: Defines the Class Tarea
 ***********************************************************************/
package org.abbatia.bean;


public class Tarea {
    private long tareaID;
    private String descripcion;

    // --------------------------------------------------------------------------
    // TareaID
    // --------------------------------------------------------------------------
    public void setTareaID(long newTareaID) {
        this.tareaID = newTareaID;
    }

    public long getTareaID() {
        return this.tareaID;
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


}


