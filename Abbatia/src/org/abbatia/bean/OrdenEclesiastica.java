/***********************************************************************
 * Module:  OrdenEclesiastica.java
 * Author:  Benja & John
 * Purpose: Defines the Class OrdenEclesiastica
 ***********************************************************************/
package org.abbatia.bean;

import java.util.ArrayList;
import java.util.Hashtable;

public class OrdenEclesiastica {
    private int idDeOrden;
    private String descripcion;
    private ArrayList abadiaOrden;

    private Hashtable abadiaOrdenHT;

    // --------------------------------------------------------------------------
    // ID de Orden
    // --------------------------------------------------------------------------
    public void setIdDeOrden(int newIdDeOrden) {
        this.idDeOrden = newIdDeOrden;
    }

    public int getIdDeOrden() {
        return this.idDeOrden;
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
    // Asociacion Link B: Abadia Orden - Orden a la que Pertenecen las Abadías.
    // --------------------------------------------------------------------------

    public ArrayList getAbadiaOrden() {
        return abadiaOrden;
    }

    public void setAbadiaOrden(ArrayList abadiaOrden) {
        this.abadiaOrden = abadiaOrden;
    }

    public Hashtable getAbadiaOrdenHT() {
        return abadiaOrdenHT;
    }

    public void setAbadiaOrdenHT(Hashtable abadiaOrdenHT) {
        this.abadiaOrdenHT = abadiaOrdenHT;
    }

    public void addAbadia(Abadia abadia) {
        if (this.abadiaOrden == null)
            this.abadiaOrden = new ArrayList();
        this.abadiaOrden.add(abadia);
    }

    public boolean removeAbadia(Abadia abadia) {
        if (this.abadiaOrden != null)
            return this.abadiaOrden.remove(abadia);
        return false;
    }
    // --------------------------------------------------------------------------


}


