package org.abbatia.bean;

import java.io.Serializable;

public class Correo implements Serializable {
    private long idCorreo;
    private long idAbadiaOrigen;
    private String abadiaOrigen;
    private String idAbadiasDestino;
    private String abadiaDestino;
    private String texto;
    private String fecha_abadia;
    private String fecha_real;
    private String estado;


    public void setIdAbadiaOrigen(long idAbadiaOrigen) {
        this.idAbadiaOrigen = idAbadiaOrigen;
    }


    public long getIdAbadiaOrigen() {
        return idAbadiaOrigen;
    }


    public void setIdAbadiasDestino(String idAbadiasDestino) {
        this.idAbadiasDestino = idAbadiasDestino;
    }


    public String getIdAbadiasDestino() {
        return idAbadiasDestino;
    }


    public void setTexto(String texto) {
        this.texto = texto;
    }


    public String getTexto() {
        return texto;
    }


    public void setFecha_abadia(String fecha_abadia) {
        this.fecha_abadia = fecha_abadia;
    }


    public String getFecha_abadia() {
        return fecha_abadia;
    }


    public void setFecha_real(String fecha_real) {
        this.fecha_real = fecha_real;
    }


    public String getFecha_real() {
        return fecha_real;
    }


    public void setIdCorreo(long idCorreo) {
        this.idCorreo = idCorreo;
    }


    public long getIdCorreo() {
        return idCorreo;
    }


    public void setAbadiaDestino(String abadiaDestino) {
        this.abadiaDestino = abadiaDestino;
    }


    public String getAbadiaDestino() {
        return abadiaDestino;
    }


    public void setEstado(String estado) {
        this.estado = estado;
    }


    public String getEstado() {
        return estado;
    }


    public void setAbadiaOrigen(String abadiaOrigen) {
        this.abadiaOrigen = abadiaOrigen;
    }


    public String getAbadiaOrigen() {
        return abadiaOrigen;
    }

}