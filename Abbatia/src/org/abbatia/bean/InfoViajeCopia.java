package org.abbatia.bean;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Benjamin
 * Date: 26-may-2005
 * Time: 21:37:51
 * To change this template use File | Settings | File Templates.
 */
public class InfoViajeCopia implements Serializable {
    private int idLibro;
    private int idAbadiaOrigen;
    private int idAbadiaDestino;
    private String nombreLibro;
    private int idMonje;
    private String nombreMonje;
    private int[] periodo;
    private double precioCopia;


    public int getIdAbadiaOrigen() {
        return idAbadiaOrigen;
    }

    public void setIdAbadiaOrigen(int idAbadiaOrigen) {
        this.idAbadiaOrigen = idAbadiaOrigen;
    }

    public int getIdAbadiaDestino() {
        return idAbadiaDestino;
    }

    public void setIdAbadiaDestino(int idAbadiaDestino) {
        this.idAbadiaDestino = idAbadiaDestino;
    }

    public double getPrecioCopia() {
        return precioCopia;
    }

    public void setPrecioCopia(double precioCopia) {
        this.precioCopia = precioCopia;
    }

    public int getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public String getNombreLibro() {
        return nombreLibro;
    }

    public void setNombreLibro(String nombreLibro) {
        this.nombreLibro = nombreLibro;
    }

    public int getIdMonje() {
        return idMonje;
    }

    public void setIdMonje(int idMonje) {
        this.idMonje = idMonje;
    }

    public String getNombreMonje() {
        return nombreMonje;
    }

    public void setNombreMonje(String nombreMonje) {
        this.nombreMonje = nombreMonje;
    }

    public int[] getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int[] periodo) {
        this.periodo = periodo;
    }
}
