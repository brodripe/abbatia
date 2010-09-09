/***********************************************************************
 * Module:  Mensajes.java
 * Author:  Benja & John
 * Purpose: Defines the Class Mensajes
 ***********************************************************************/
package org.abbatia.bean;

import org.abbatia.core.CoreTiempo;
import org.abbatia.exception.base.AbadiaException;

import java.io.Serializable;

public class Mensajes implements Serializable {
    private long idDeMensaje;
    private String fechaAbadia;
    private String fechaReal;
    private String mensaje;
    private long idDeAbadia;
    private long idDeMonje;
    private int idDeRegion;
    private int idDeIdioma;
    private int tipo;     // 0 - Informativo , 1 - Alerta
    private Object[] claves;


    public Mensajes(long idAbadia, long idMonje, String mensaje, int tipo) throws AbadiaException {
        this.setIdDeAbadia(idAbadia);
        this.setIdDeMonje(idMonje);
        this.setFechaAbadia(CoreTiempo.getTiempoAbadiaStringConHoras());
        this.setFechaReal(CoreTiempo.getTiempoRealStringConHoras());
        this.setMensaje(mensaje);
        this.setTipo(tipo);
    }

    public Mensajes() {
    }

    // --------------------------------------------------------------------------
    // ID de Mensaje
    // --------------------------------------------------------------------------
    public void setIdDeMensaje(long newIdDeMensaje) {
        this.idDeMensaje = newIdDeMensaje;
    }

    public long getIdDeMensaje() {
        return this.idDeMensaje;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Fecha Abadia
    // --------------------------------------------------------------------------

    public void setFechaAbadia(String newFechaAbadia) {
        this.fechaAbadia = newFechaAbadia;
    }

    public String getFechaAbadia() {
        return this.fechaAbadia;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Fecha Real
    // --------------------------------------------------------------------------

    public void setFechaReal(String newFechaReal) {
        this.fechaReal = newFechaReal;
    }

    public String getFechaReal() {
        return this.fechaReal;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Mensaje
    // --------------------------------------------------------------------------

    public void setMensaje(String newMensaje) {
        this.mensaje = newMensaje;
    }

    public String getMensaje() {
        return this.mensaje;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Asociacion A: ID de Abadia
    // --------------------------------------------------------------------------

    public void setIdDeAbadia(long newIdDeAbadia) {
        this.idDeAbadia = newIdDeAbadia;
    }

    public long getIdDeAbadia() {
        return this.idDeAbadia;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Asociacion A: ID de Monje
    // --------------------------------------------------------------------------

    public void setIdDeMonje(long newIdDeMonje) {
        this.idDeMonje = newIdDeMonje;
    }

    public long getIdDeMonje() {
        return this.idDeMonje;
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


    public void setClaves(Object[] claves) {
        this.claves = claves;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public void setIdDeIdioma(int idDeIdioma) {
        this.idDeIdioma = idDeIdioma;
    }

    public Object[] getClaves() {
        return claves;
    }

    public int getTipo() {
        return tipo;
    }

    public int getIdDeIdioma() {
        return idDeIdioma;
    }

    // --------------------------------------------------------------------------


}


