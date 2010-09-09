/***********************************************************************
 * Module:  Propiedad.java
 * Author:  Benja & John
 * Purpose: Defines the Class Propiedad
 ***********************************************************************/
package org.abbatia.bean;

import java.util.ArrayList;
import java.util.Hashtable;

public class Propiedad {
    private long idDePropiedad;
    private String descripcion;
    private String nombre;
    private long historico;
    private short maximoTamanoHistorico;
    private ArrayList propiedadesDeLosMonjes;

    private Hashtable propiedadesDeLosMonjesHT;
    private ArrayList valoresDePropiedades;

    private Hashtable valoresDePropiedadesHT;
    private ArrayList historicoDeValores;

    private Hashtable historicoDeValoresHT;

    // --------------------------------------------------------------------------
    // ID de Propiedad
    // --------------------------------------------------------------------------
    public void setIdDePropiedad(long newIdDePropiedad) {
        this.idDePropiedad = newIdDePropiedad;
    }

    public long getIdDePropiedad() {
        return this.idDePropiedad;
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
    // Nombre
    // --------------------------------------------------------------------------

    public void setNombre(String newNombre) {
        this.nombre = newNombre;
    }

    public String getNombre() {
        return this.nombre;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Histórico
    // --------------------------------------------------------------------------

    public void setHistorico(long newHistorico) {
        this.historico = newHistorico;
    }

    public long getHistorico() {
        return this.historico;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Máximo Tamaño Histórico
    // --------------------------------------------------------------------------

    public void setMaximoTamanoHistorico(short newMaximoTamanoHistorico) {
        this.maximoTamanoHistorico = newMaximoTamanoHistorico;
    }

    public short getMaximoTamanoHistorico() {
        return this.maximoTamanoHistorico;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Asociacion Link A: Propiedades de los Monjes
    // --------------------------------------------------------------------------

    public ArrayList getPropiedadesDeLosMonjes() {
        return propiedadesDeLosMonjes;
    }

    public void setPropiedadesDeLosMonjes(ArrayList propiedadesDeLosMonjes) {
        this.propiedadesDeLosMonjes = propiedadesDeLosMonjes;
    }

    public Hashtable getPropiedadesDeLosMonjesHT() {
        return propiedadesDeLosMonjesHT;
    }

    public void setPropiedadesDeLosMonjesHT(Hashtable propiedadesDeLosMonjesHT) {
        this.propiedadesDeLosMonjesHT = propiedadesDeLosMonjesHT;
    }

    public void addMonje(Monje monje) {
        if (this.propiedadesDeLosMonjes == null)
            this.propiedadesDeLosMonjes = new ArrayList();
        this.propiedadesDeLosMonjes.add(monje);
    }

    public boolean removeMonje(Monje monje) {
        if (this.propiedadesDeLosMonjes != null)
            return this.propiedadesDeLosMonjes.remove(monje);
        return false;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Asociacion Link B: Valores de Propiedades
    // --------------------------------------------------------------------------

    public ArrayList getValoresDePropiedades() {
        return valoresDePropiedades;
    }

    public void setValoresDePropiedades(ArrayList valoresDePropiedades) {
        this.valoresDePropiedades = valoresDePropiedades;
    }

    public Hashtable getValoresDePropiedadesHT() {
        return valoresDePropiedadesHT;
    }

    public void setValoresDePropiedadesHT(Hashtable valoresDePropiedadesHT) {
        this.valoresDePropiedadesHT = valoresDePropiedadesHT;
    }

    public void addPropiedadValor(PropiedadValor propiedadValor) {
        if (this.valoresDePropiedades == null)
            this.valoresDePropiedades = new ArrayList();
        this.valoresDePropiedades.add(propiedadValor);
    }

    public boolean removePropiedadValor(PropiedadValor propiedadValor) {
        if (this.valoresDePropiedades != null)
            return this.valoresDePropiedades.remove(propiedadValor);
        return false;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Asociacion Link B: Historico de Valores
    // --------------------------------------------------------------------------

    public ArrayList getHistoricoDeValores() {
        return historicoDeValores;
    }

    public void setHistoricoDeValores(ArrayList historicoDeValores) {
        this.historicoDeValores = historicoDeValores;
    }

    public Hashtable getHistoricoDeValoresHT() {
        return historicoDeValoresHT;
    }

    public void setHistoricoDeValoresHT(Hashtable historicoDeValoresHT) {
        this.historicoDeValoresHT = historicoDeValoresHT;
    }

    public void addPropiedadHistorico(PropiedadHistorico propiedadHistorico) {
        if (this.historicoDeValores == null)
            this.historicoDeValores = new ArrayList();
        this.historicoDeValores.add(propiedadHistorico);
    }

    public boolean removePropiedadHistorico(PropiedadHistorico propiedadHistorico) {
        if (this.historicoDeValores != null)
            return this.historicoDeValores.remove(propiedadHistorico);
        return false;
    }
    // --------------------------------------------------------------------------


}


