package org.abbatia.bean;

/**
 * Created by IntelliJ IDEA.
 * User: Benjamin
 * Date: 25-sep-2004
 * Time: 22:16:34
 * To change this template use File | Settings | File Templates.
 */
public class AlimentoProceso {
    private int idAbadia;
    private int idAalimento;
    private int idLote;
    private int ididioma;
    private double cantidad;
    private String descripcion;
    private String medida;

    public int getIdAalimento() {
        return idAalimento;
    }

    public void setIdAalimento(int idAalimento) {
        this.idAalimento = idAalimento;
    }

    public int getIdLote() {
        return idLote;
    }

    public void setIdLote(int idLote) {
        this.idLote = idLote;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getIdidioma() {
        return ididioma;
    }

    public String getMedida() {
        return medida;
    }

    public int getIdAbadia() {
        return idAbadia;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setIdidioma(int ididioma) {
        this.ididioma = ididioma;
    }

    public void setMedida(String medida) {
        this.medida = medida;
    }

    public void setIdAbadia(int idAbadia) {
        this.idAbadia = idAbadia;
    }
}
