/***********************************************************************
 * Module:  JerarquiaEclesiastica.java
 * Author:  Benja & John
 * Purpose: Defines the Class JerarquiaEclesiastica
 ***********************************************************************/
package org.abbatia.bean;

import java.util.ArrayList;
import java.util.Hashtable;

public class JerarquiaEclesiastica {
    private int idDeJerarquia;
    private String descripcion;
    private ArrayList nivel;

    private Hashtable nivelHT;

    // --------------------------------------------------------------------------
    // ID de Jerarquía
    // --------------------------------------------------------------------------
    public void setIdDeJerarquia(int newIdDeJerarquia) {
        this.idDeJerarquia = newIdDeJerarquia;
    }

    public int getIdDeJerarquia() {
        return this.idDeJerarquia;
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

    // --------------------------------------------------------------------------
    // Asociacion Link B: nivel - Nivel Según la Jerarquía Eclesiástica en cada Momento
    // --------------------------------------------------------------------------

    public ArrayList getNivel() {
        return nivel;
    }

    public void setNivel(ArrayList nivel) {
        this.nivel = nivel;
    }

    public Hashtable getNivelHT() {
        return nivelHT;
    }

    public void setNivelHT(Hashtable nivelHT) {
        this.nivelHT = nivelHT;
    }

    public void addMonje(Monje monje) {
        if (this.nivel == null)
            this.nivel = new ArrayList();
        this.nivel.add(monje);
    }

    public boolean removeMonje(Monje monje) {
        if (this.nivel != null)
            return this.nivel.remove(monje);
        return false;
    }
    // --------------------------------------------------------------------------


}


