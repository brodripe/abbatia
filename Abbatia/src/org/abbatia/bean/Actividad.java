/***********************************************************************
 * Module:  Actividad.java
 * Author:  Benja & John
 * Purpose: Defines the Class Actividad
 ***********************************************************************/
package org.abbatia.bean;

import java.util.ArrayList;
import java.util.Hashtable;

public class Actividad {
    private long idDeActividad;
    private String descripcion;
    private short consumoProteinas;
    private short consumoLipidos;
    private short consumoHidratosCarbono;
    private short consumoVitaminas;
    private long cantidad;
    private int idDeRecurso;
    private ArrayList actividadesDeLaAbadia;

    private Hashtable actividadesDeLaAbadiaHT;
    private ArrayList actividadDeLosMonjes;

    private Hashtable actividadDeLosMonjesHT;
    private ArrayList actividadMejorada;

    private Hashtable actividadMejoradaHT;

    // --------------------------------------------------------------------------
    // ID de Actividad
    // --------------------------------------------------------------------------
    public void setIdDeActividad(long newIdDeActividad) {
        this.idDeActividad = newIdDeActividad;
    }

    public long getIdDeActividad() {
        return this.idDeActividad;
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
    // Consumo Proteínas
    // --------------------------------------------------------------------------

    public void setConsumoProteinas(short newConsumoProteinas) {
        this.consumoProteinas = newConsumoProteinas;
    }

    public short getConsumoProteinas() {
        return this.consumoProteinas;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Consumo Lípidos
    // --------------------------------------------------------------------------

    public void setConsumoLipidos(short newConsumoLipidos) {
        this.consumoLipidos = newConsumoLipidos;
    }

    public short getConsumoLipidos() {
        return this.consumoLipidos;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Consumo Hidratos Carbono
    // --------------------------------------------------------------------------

    public void setConsumoHidratosCarbono(short newConsumoHidratosCarbono) {
        this.consumoHidratosCarbono = newConsumoHidratosCarbono;
    }

    public short getConsumoHidratosCarbono() {
        return this.consumoHidratosCarbono;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Consumo Vitaminas
    // --------------------------------------------------------------------------

    public void setConsumoVitaminas(short newConsumoVitaminas) {
        this.consumoVitaminas = newConsumoVitaminas;
    }

    public short getConsumoVitaminas() {
        return this.consumoVitaminas;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Cantidad
    // --------------------------------------------------------------------------

    public void setCantidad(long newCantidad) {
        this.cantidad = newCantidad;
    }

    public long getCantidad() {
        return this.cantidad;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Asociacion A: ID de Recurso
    // --------------------------------------------------------------------------

    public void setIdDeRecurso(int newIdDeRecurso) {
        this.idDeRecurso = newIdDeRecurso;
    }

    public int getIdDeRecurso() {
        return this.idDeRecurso;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Asociacion Link A: Actividades de la Abadia - Actividades de la Abadia
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

    public void addAbadia(Abadia abadia) {
        if (this.actividadesDeLaAbadia == null)
            this.actividadesDeLaAbadia = new ArrayList();
        this.actividadesDeLaAbadia.add(abadia);
    }

    public boolean removeAbadia(Abadia abadia) {
        if (this.actividadesDeLaAbadia != null)
            return this.actividadesDeLaAbadia.remove(abadia);
        return false;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Asociacion Link B: Actividad de los Monjes
    // --------------------------------------------------------------------------

    public ArrayList getActividadDeLosMonjes() {
        return actividadDeLosMonjes;
    }

    public void setActividadDeLosMonjes(ArrayList actividadDeLosMonjes) {
        this.actividadDeLosMonjes = actividadDeLosMonjes;
    }

    public Hashtable getActividadDeLosMonjesHT() {
        return actividadDeLosMonjesHT;
    }

    public void setActividadDeLosMonjesHT(Hashtable actividadDeLosMonjesHT) {
        this.actividadDeLosMonjesHT = actividadDeLosMonjesHT;
    }

    public void addTareasDelMonje(TareasDelMonje tareasDelMonje) {
        if (this.actividadDeLosMonjes == null)
            this.actividadDeLosMonjes = new ArrayList();
        this.actividadDeLosMonjes.add(tareasDelMonje);
    }

    public boolean removeTareasDelMonje(TareasDelMonje tareasDelMonje) {
        if (this.actividadDeLosMonjes != null)
            return this.actividadDeLosMonjes.remove(tareasDelMonje);
        return false;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Asociacion Link B: Actividad Mejorada
    // --------------------------------------------------------------------------

    public ArrayList getActividadMejorada() {
        return actividadMejorada;
    }

    public void setActividadMejorada(ArrayList actividadMejorada) {
        this.actividadMejorada = actividadMejorada;
    }

    public Hashtable getActividadMejoradaHT() {
        return actividadMejoradaHT;
    }

    public void setActividadMejoradaHT(Hashtable actividadMejoradaHT) {
        this.actividadMejoradaHT = actividadMejoradaHT;
    }

    public void addHabilidad(Habilidad habilidad) {
        if (this.actividadMejorada == null)
            this.actividadMejorada = new ArrayList();
        this.actividadMejorada.add(habilidad);
    }

    public boolean removeHabilidad(Habilidad habilidad) {
        if (this.actividadMejorada != null)
            return this.actividadMejorada.remove(habilidad);
        return false;
    }
    // --------------------------------------------------------------------------


}


