/***********************************************************************
 * Module:  Especializacion.java
 * Author:  Benja & John
 * Purpose: Defines the Class Especializacion
 ***********************************************************************/
package org.abbatia.bean;

import java.util.ArrayList;
import java.util.Hashtable;

public class Especializacion {
    private long idDeEspecializacion;
    private String descripcion;
    private ArrayList especializacionDeMonjes;

    private Hashtable especializacionDeMonjesHT;

    // --------------------------------------------------------------------------
    // ID de Especialización
    // --------------------------------------------------------------------------
    public void setIdDeEspecializacion(long newIdDeEspecializacion) {
        this.idDeEspecializacion = newIdDeEspecializacion;
    }

    public long getIdDeEspecializacion() {
        return this.idDeEspecializacion;
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
    // Asociacion Link B: Especialización de Monjes - Especializaciones de los Monjes
    // --------------------------------------------------------------------------

    public ArrayList getEspecializacionDeMonjes() {
        return especializacionDeMonjes;
    }

    public void setEspecializacionDeMonjes(ArrayList especializacionDeMonjes) {
        this.especializacionDeMonjes = especializacionDeMonjes;
    }

    public Hashtable getEspecializacionDeMonjesHT() {
        return especializacionDeMonjesHT;
    }

    public void setEspecializacionDeMonjesHT(Hashtable especializacionDeMonjesHT) {
        this.especializacionDeMonjesHT = especializacionDeMonjesHT;
    }

    public void addMonje(Monje monje) {
        if (this.especializacionDeMonjes == null)
            this.especializacionDeMonjes = new ArrayList();
        this.especializacionDeMonjes.add(monje);
    }

    public boolean removeMonje(Monje monje) {
        if (this.especializacionDeMonjes != null)
            return this.especializacionDeMonjes.remove(monje);
        return false;
    }
    // --------------------------------------------------------------------------


}


