package org.abbatia.bean;

/**
 * Created by IntelliJ IDEA.
 * User: Benjamin
 * Date: 18-sep-2004
 * Time: 9:56:04
 * To change this template use File | Settings | File Templates.
 */
public class Alimento {
    private int idAlimento;
    private double cantidad;
    private String descAli;
    private String descFam;
    private String precioMercado;

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public int getIdAlimento() {
        return idAlimento;
    }

    public void setIdAlimento(int idAlimento) {
        this.idAlimento = idAlimento;
    }

    public String getDescAli() {
        return descAli;
    }

    public void setDescAli(String descAli) {
        this.descAli = descAli;
    }

    public String getDescFam() {
        return descFam;
    }

    public void setDescFam(String descFam) {
        this.descFam = descFam;
    }

    public String getPrecioMercado() {
        return precioMercado;
    }

    public void setPrecioMercado(String precioMercado) {
        this.precioMercado = precioMercado;
    }
}
