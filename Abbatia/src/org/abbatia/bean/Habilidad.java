/***********************************************************************
 * Module:  Habilidad.java
 * Author:  Benja & John
 * Purpose: Defines the Class Habilidad
 ***********************************************************************/
package org.abbatia.bean;

import java.util.ArrayList;
import java.util.Hashtable;

public class Habilidad {
    private int idDeHabilidad;
    private String descripcion;
    private short nivelMejora;
    private long idDeActividad;
    private ArrayList habilidadesDeLosMonjes;

    private Hashtable habilidadesDeLosMonjesHT;
    private ArrayList efectoDelLibro;

    private Hashtable efectoDelLibroHT;

    // --------------------------------------------------------------------------
    // ID de Habilidad
    // --------------------------------------------------------------------------
    public void setIdDeHabilidad(int newIdDeHabilidad) {
        this.idDeHabilidad = newIdDeHabilidad;
    }

    public int getIdDeHabilidad() {
        return this.idDeHabilidad;
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
    // Nivel Mejora - Nivel (porcentaje o puntos) en la que la actividad es mejorada.
    // --------------------------------------------------------------------------

    public void setNivelMejora(short newNivelMejora) {
        this.nivelMejora = newNivelMejora;
    }

    public short getNivelMejora() {
        return this.nivelMejora;
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

    // --------------------------------------------------------------------------
    // Asociacion Link A: Habilidades de los Monjes - Habilidades de los Monjes
    // --------------------------------------------------------------------------

    public ArrayList getHabilidadesDeLosMonjes() {
        return habilidadesDeLosMonjes;
    }

    public void setHabilidadesDeLosMonjes(ArrayList habilidadesDeLosMonjes) {
        this.habilidadesDeLosMonjes = habilidadesDeLosMonjes;
    }

    public Hashtable getHabilidadesDeLosMonjesHT() {
        return habilidadesDeLosMonjesHT;
    }

    public void setHabilidadesDeLosMonjesHT(Hashtable habilidadesDeLosMonjesHT) {
        this.habilidadesDeLosMonjesHT = habilidadesDeLosMonjesHT;
    }

    public void addMonje(Monje monje) {
        if (this.habilidadesDeLosMonjes == null)
            this.habilidadesDeLosMonjes = new ArrayList();
        this.habilidadesDeLosMonjes.add(monje);
    }

    public boolean removeMonje(Monje monje) {
        if (this.habilidadesDeLosMonjes != null)
            return this.habilidadesDeLosMonjes.remove(monje);
        return false;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Asociacion Link B: Efecto del Libro
    // --------------------------------------------------------------------------

    public ArrayList getEfectoDelLibro() {
        return efectoDelLibro;
    }

    public void setEfectoDelLibro(ArrayList efectoDelLibro) {
        this.efectoDelLibro = efectoDelLibro;
    }

    public Hashtable getEfectoDelLibroHT() {
        return efectoDelLibroHT;
    }

    public void setEfectoDelLibroHT(Hashtable efectoDelLibroHT) {
        this.efectoDelLibroHT = efectoDelLibroHT;
    }

    public void addRecurso(Recurso recurso) {
        if (this.efectoDelLibro == null)
            this.efectoDelLibro = new ArrayList();
        this.efectoDelLibro.add(recurso);
    }

    public boolean removeRecurso(Recurso recurso) {
        if (this.efectoDelLibro != null)
            return this.efectoDelLibro.remove(recurso);
        return false;
    }
    // --------------------------------------------------------------------------


}


