/***********************************************************************
 * Module:  Periodo.java
 * Author:  Benja & John
 * Purpose: Defines the Class Periodo
 ***********************************************************************/
package org.abbatia.bean;

import java.util.ArrayList;
import java.util.Hashtable;

public class Periodo {
    private String descripcion;
    private long idDePeriodo;
    private long asignable;
    private ArrayList periodicidadDeLasTareas;

    private Hashtable periodicidadDeLasTareasHT;

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
    // ID de Periodo
    // --------------------------------------------------------------------------

    public void setIdDePeriodo(long newIdDePeriodo) {
        this.idDePeriodo = newIdDePeriodo;
    }

    public long getIdDePeriodo() {
        return this.idDePeriodo;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Asignable - Determina si este periodo es asignable para tareas...
    // --------------------------------------------------------------------------

    public void setAsignable(long newAsignable) {
        this.asignable = newAsignable;
    }

    public long getAsignable() {
        return this.asignable;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Asociacion Link B: Periodicidad de las Tareas
    // --------------------------------------------------------------------------

    public ArrayList getPeriodicidadDeLasTareas() {
        return periodicidadDeLasTareas;
    }

    public void setPeriodicidadDeLasTareas(ArrayList periodicidadDeLasTareas) {
        this.periodicidadDeLasTareas = periodicidadDeLasTareas;
    }

    public Hashtable getPeriodicidadDeLasTareasHT() {
        return periodicidadDeLasTareasHT;
    }

    public void setPeriodicidadDeLasTareasHT(Hashtable periodicidadDeLasTareasHT) {
        this.periodicidadDeLasTareasHT = periodicidadDeLasTareasHT;
    }

    public void addTareasDelMonje(TareasDelMonje tareasDelMonje) {
        if (this.periodicidadDeLasTareas == null)
            this.periodicidadDeLasTareas = new ArrayList();
        this.periodicidadDeLasTareas.add(tareasDelMonje);
    }

    public boolean removeTareasDelMonje(TareasDelMonje tareasDelMonje) {
        if (this.periodicidadDeLasTareas != null)
            return this.periodicidadDeLasTareas.remove(tareasDelMonje);
        return false;
    }
    // --------------------------------------------------------------------------


}


