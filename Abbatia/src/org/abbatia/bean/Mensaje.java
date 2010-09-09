package org.abbatia.bean;

import org.abbatia.exception.base.AbadiaException;

import java.io.Serializable;

public class Mensaje implements Serializable {
    private int idDeMonje;
    private String fechaAbadia;
    private String fechaReal;
    private String mensaje;
    private int idAbadia;
    private String nombreAbadia;
    private int tipo;     // 0 - Informativo , 1 - Alerta


    public Mensaje(int idMonje, String mensaje, int tipo, String fechaAbadia) throws AbadiaException {
        this.setIdDeMonje(idMonje);
        this.setFechaAbadia(fechaAbadia);
        this.setMensaje(mensaje);
        this.setTipo(tipo);
    }

    public Mensaje() {
    }

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
    // Asociacion A: ID de Monje
    // --------------------------------------------------------------------------

    public void setIdDeMonje(int newIdDeMonje) {
        this.idDeMonje = newIdDeMonje;
    }

    public int getIdDeMonje() {
        return this.idDeMonje;
    }
    // --------------------------------------------------------------------------


    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }


    public String getFechaReal() {
        return fechaReal;
    }

    public void setFechaReal(String fechaReal) {
        this.fechaReal = fechaReal;
    }

    public int getIdAbadia() {
        return idAbadia;
    }

    public void setIdAbadia(int idAbadia) {
        this.idAbadia = idAbadia;
    }

    public String getNombreAbadia() {
        return nombreAbadia;
    }

    public void setNombreAbadia(String nombreAbadia) {
        this.nombreAbadia = nombreAbadia;
    }
}
