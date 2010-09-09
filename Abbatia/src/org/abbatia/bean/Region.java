/***********************************************************************
 * Module:  Region.java
 * Author:  Benja & John
 * Purpose: Defines the Class Region
 ***********************************************************************/
package org.abbatia.bean;

import java.util.ArrayList;
import java.util.Hashtable;

public class Region {
    private int idDeRegion;
    private String descripcion;
    private short x;
    private short y;
    private ArrayList regionAbadia;

    private Hashtable regionAbadiaHT;
    private ArrayList mensajesRegion;

    private Hashtable mensajesRegionHT;

    private double comisionTransito;

    public double getComisionTransito() {
        return comisionTransito;
    }

    public void setComisionTransito(double comisionTransito) {
        this.comisionTransito = comisionTransito;
    }

    public Region() {
    }

    public Region(int idDeRegion, String descripcion, double comisionTransito) {
        this.idDeRegion = idDeRegion;
        this.descripcion = descripcion;
        this.comisionTransito = comisionTransito;
    }

    public Region(int idDeRegion, String descripcion) {
        this.idDeRegion = idDeRegion;
        this.descripcion = descripcion;
    }// --------------------------------------------------------------------------

    // ID de Región
    // --------------------------------------------------------------------------
    public void setIdDeRegion(int newIdDeRegion) {
        this.idDeRegion = newIdDeRegion;
    }

    public int getIdDeRegion() {
        return this.idDeRegion;
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
    // X
    // --------------------------------------------------------------------------

    public void setX(short newX) {
        this.x = newX;
    }

    public short getX() {
        return this.x;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Y
    // --------------------------------------------------------------------------

    public void setY(short newY) {
        this.y = newY;
    }

    public short getY() {
        return this.y;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Asociacion Link B: region abbatia - Región en la que el usuario gestiona su abadía
    // --------------------------------------------------------------------------

    public ArrayList getRegionAbadia() {
        return regionAbadia;
    }

    public void setRegionAbadia(ArrayList regionAbadia) {
        this.regionAbadia = regionAbadia;
    }

    public Hashtable getRegionAbadiaHT() {
        return regionAbadiaHT;
    }

    public void setRegionAbadiaHT(Hashtable regionAbadiaHT) {
        this.regionAbadiaHT = regionAbadiaHT;
    }

    public void addAbadia(Abadia abadia) {
        if (this.regionAbadia == null)
            this.regionAbadia = new ArrayList();
        this.regionAbadia.add(abadia);
    }

    public boolean removeAbadia(Abadia abadia) {
        if (this.regionAbadia != null)
            return this.regionAbadia.remove(abadia);
        return false;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Asociacion Link B: Mensajes Región
    // --------------------------------------------------------------------------

    public ArrayList getMensajesRegion() {
        return mensajesRegion;
    }

    public void setMensajesRegion(ArrayList mensajesRegion) {
        this.mensajesRegion = mensajesRegion;
    }

    public Hashtable getMensajesRegionHT() {
        return mensajesRegionHT;
    }

    public void setMensajesRegionHT(Hashtable mensajesRegionHT) {
        this.mensajesRegionHT = mensajesRegionHT;
    }

    public void addMensajes(Mensajes mensajes) {
        if (this.mensajesRegion == null)
            this.mensajesRegion = new ArrayList();
        this.mensajesRegion.add(mensajes);
    }

    public boolean removeMensajes(Mensajes mensajes) {
        if (this.mensajesRegion != null)
            return this.mensajesRegion.remove(mensajes);
        return false;
    }
    // --------------------------------------------------------------------------


}


