package org.abbatia.bean;

/**
 * Created by IntelliJ IDEA.
 * User: Benjamin
 * Date: 21-nov-2004
 * Time: 14:36:27
 * To change this template use File | Settings | File Templates.
 */
public class requisitoElaboracion {
    private int idProducto;
    private String producto;
    private double cantidadD;
    private String cantidad;
    private String tipo;

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public double getCantidadD() {
        return cantidadD;
    }

    public void setCantidadD(double cantidadD) {
        this.cantidadD = cantidadD;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }
}
