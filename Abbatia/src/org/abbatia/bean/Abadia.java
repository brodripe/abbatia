/***********************************************************************
 * Module:  Abadia.java
 * Author:  Benja & John
 * Purpose: Defines the Class Abadia
 ***********************************************************************/
package org.abbatia.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

public class Abadia implements Serializable {
    private int idDeAbadia;
    private String nombre;
    private String fechaDeConstruccion;
    private String fechaEliminacion;
    private String arquitecto;
    private short capacidad;
    private short nivelJerarquico;
    private String historia;
    private int idDeOrden;
    private int idDeUsuario;
    private int idDeRegion;
    private int idIdioma;
    private String descIdioma;
    private ArrayList actividadesDeLaAbadia;
    private int actividadPrincipal;
    private double puntuacion;

    private Hashtable actividadesDeLaAbadiaHT;
    private ArrayList recursosDeLaAbadia;

    private Hashtable recursosDeLaAbadiaHT;
    private ArrayList pertenecia;

    private Hashtable perteneciaHT;
    private ArrayList edificiosDeLaAbadia;

    private Hashtable edificiosDeLaAbadiaHT;
    private ArrayList mensajesAbadia;

    private Hashtable mensajesAbadiaHT;


    public String getFechaEliminacion() {
        return fechaEliminacion;
    }

    public void setFechaEliminacion(String fechaEliminacion) {
        this.fechaEliminacion = fechaEliminacion;
    }

    public double getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(double puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getDescIdioma() {
        return descIdioma;
    }

    public void setDescIdioma(String descIdioma) {
        this.descIdioma = descIdioma;
    }

    // --------------------------------------------------------------------------
    // ID de Abadia
    // --------------------------------------------------------------------------
    public void setIdDeAbadia(int newIdDeAbadia) {
        this.idDeAbadia = newIdDeAbadia;
    }

    public int getIdDeAbadia() {
        return this.idDeAbadia;
    }
    // --------------------------------------------------------------------------


    public int getIdIdioma() {
        return idIdioma;
    }

    public void setIdIdioma(int idIdioma) {
        this.idIdioma = idIdioma;
    }

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
    // Fecha de Construcción
    // --------------------------------------------------------------------------

    public void setFechaDeConstruccion(String newFechaDeConstruccion) {
        this.fechaDeConstruccion = newFechaDeConstruccion;
    }

    public String getFechaDeConstruccion() {
        return this.fechaDeConstruccion;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Arquitecto
    // --------------------------------------------------------------------------

    public void setArquitecto(String newArquitecto) {
        this.arquitecto = newArquitecto;
    }

    public String getArquitecto() {
        return this.arquitecto;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Capacidad
    // --------------------------------------------------------------------------

    public void setCapacidad(short newCapacidad) {
        this.capacidad = newCapacidad;
    }

    public short getCapacidad() {
        return this.capacidad;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Nivel Jerárquico
    // --------------------------------------------------------------------------

    public void setNivelJerarquico(short newNivelJerarquico) {
        this.nivelJerarquico = newNivelJerarquico;
    }

    public short getNivelJerarquico() {
        return this.nivelJerarquico;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Historia
    // --------------------------------------------------------------------------

    public void setHistoria(String newHistoria) {
        this.historia = newHistoria;
    }

    public String getHistoria() {
        return this.historia;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Asociacion A: ID de Orden
    // --------------------------------------------------------------------------

    public void setIdDeOrden(int newIdDeOrden) {
        this.idDeOrden = newIdDeOrden;
    }

    public int getIdDeOrden() {
        return this.idDeOrden;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Asociacion A: ID de Usuario
    // --------------------------------------------------------------------------

    public void setIdDeUsuario(int newIdDeUsuario) {
        this.idDeUsuario = newIdDeUsuario;
    }

    public int getIdDeUsuario() {
        return this.idDeUsuario;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Asociacion A: ID de Región
    // --------------------------------------------------------------------------

    public void setIdDeRegion(int newIdDeRegion) {
        this.idDeRegion = newIdDeRegion;
    }

    public int getIdDeRegion() {
        return this.idDeRegion;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Asociacion Link B: Actividades de la Abadia - Actividades de la Abadia
    // --------------------------------------------------------------------------

    public ArrayList getActividadesDeLaAbadia() {
        return actividadesDeLaAbadia;
    }

    public void setActividadesDeLaAbadia(ArrayList actividadesDeLaAbadia) {
        this.actividadesDeLaAbadia = actividadesDeLaAbadia;
    }

    public Hashtable getActividadesDeLaAbadiaHT() {
        return actividadesDeLaAbadiaHT;
    }

    public void setActividadesDeLaAbadiaHT(Hashtable actividadesDeLaAbadiaHT) {
        this.actividadesDeLaAbadiaHT = actividadesDeLaAbadiaHT;
    }

    public void addActividad(Actividad actividad) {
        if (this.actividadesDeLaAbadia == null)
            this.actividadesDeLaAbadia = new ArrayList();
        this.actividadesDeLaAbadia.add(actividad);
    }

    public boolean removeActividad(Actividad actividad) {
        if (this.actividadesDeLaAbadia != null)
            return this.actividadesDeLaAbadia.remove(actividad);
        return false;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Asociacion Link B: Recursos de la Abadía - Recursos de la Abadía
    // --------------------------------------------------------------------------

    public ArrayList getRecursosDeLaAbadia() {
        return recursosDeLaAbadia;
    }

    public void setRecursosDeLaAbadia(ArrayList recursosDeLaAbadia) {
        this.recursosDeLaAbadia = recursosDeLaAbadia;
    }

    public Hashtable getRecursosDeLaAbadiaHT() {
        return recursosDeLaAbadiaHT;
    }

    public void setRecursosDeLaAbadiaHT(Hashtable recursosDeLaAbadiaHT) {
        this.recursosDeLaAbadiaHT = recursosDeLaAbadiaHT;
    }

    public void addRecurso(Recurso recurso) {
        if (this.recursosDeLaAbadia == null)
            this.recursosDeLaAbadia = new ArrayList();
        this.recursosDeLaAbadia.add(recurso);
    }

    public boolean removeRecurso(Recurso recurso) {
        if (this.recursosDeLaAbadia != null)
            return this.recursosDeLaAbadia.remove(recurso);
        return false;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Asociacion Link B: pertenecia - Monjes gestionados por cada usuario
    // --------------------------------------------------------------------------

    public ArrayList getPertenecia() {
        return pertenecia;
    }

    public void setPertenecia(ArrayList pertenecia) {
        this.pertenecia = pertenecia;
    }

    public Hashtable getPerteneciaHT() {
        return perteneciaHT;
    }

    public void setPerteneciaHT(Hashtable perteneciaHT) {
        this.perteneciaHT = perteneciaHT;
    }

    public void addMonje(Monje monje) {
        if (this.pertenecia == null)
            this.pertenecia = new ArrayList();
        this.pertenecia.add(monje);
    }

    public boolean removeMonje(Monje monje) {
        if (this.pertenecia != null)
            return this.pertenecia.remove(monje);
        return false;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Asociacion Link B: Edificios de la Abadia
    // --------------------------------------------------------------------------

    public ArrayList getEdificiosDeLaAbadia() {
        return edificiosDeLaAbadia;
    }

    public void setEdificiosDeLaAbadia(ArrayList edificiosDeLaAbadia) {
        this.edificiosDeLaAbadia = edificiosDeLaAbadia;
    }

    public Hashtable getEdificiosDeLaAbadiaHT() {
        return edificiosDeLaAbadiaHT;
    }

    public void setEdificiosDeLaAbadiaHT(Hashtable edificiosDeLaAbadiaHT) {
        this.edificiosDeLaAbadiaHT = edificiosDeLaAbadiaHT;
    }

    public void addEdificio(Edificio edificio) {
        if (this.edificiosDeLaAbadia == null)
            this.edificiosDeLaAbadia = new ArrayList();
        this.edificiosDeLaAbadia.add(edificio);
    }

    public boolean removeEdificio(Edificio edificio) {
        if (this.edificiosDeLaAbadia != null)
            return this.edificiosDeLaAbadia.remove(edificio);
        return false;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Asociacion Link B: Mensajes Abadia
    // --------------------------------------------------------------------------

    public ArrayList getMensajesAbadia() {
        return mensajesAbadia;
    }

    public void setMensajesAbadia(ArrayList mensajesAbadia) {
        this.mensajesAbadia = mensajesAbadia;
    }

    public Hashtable getMensajesAbadiaHT() {
        return mensajesAbadiaHT;
    }

    public void setMensajesAbadiaHT(Hashtable mensajesAbadiaHT) {
        this.mensajesAbadiaHT = mensajesAbadiaHT;
    }

    public void addMensajes(Mensajes mensajes) {
        if (this.mensajesAbadia == null)
            this.mensajesAbadia = new ArrayList();
        this.mensajesAbadia.add(mensajes);
    }

    public boolean removeMensajes(Mensajes mensajes) {
        if (this.mensajesAbadia != null)
            return this.mensajesAbadia.remove(mensajes);
        return false;
    }


    public void setActividadPrincipal(int actividadPrincipal) {
        this.actividadPrincipal = actividadPrincipal;
    }


    public int getActividadPrincipal() {
        return actividadPrincipal;
    }

    // --------------------------------------------------------------------------


}


