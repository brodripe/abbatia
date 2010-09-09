package org.abbatia.bean;

public class MercadoMovima {
    private int idMovima;
    private int idProducto;
    private long idAbadia;
    private String fecha;
    private String tipo;
    private int cantidad;
    private double precio_unidad;

    public int getIdProducto() {
        return idProducto;
    }

    public String getTipo() {
        return tipo;
    }

    public long getIdAbadia() {
        return idAbadia;
    }

    public int getIdMovima() {
        return idMovima;
    }

    public double getPrecio_unidad() {
        return precio_unidad;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setIdAbadia(long idAbadia) {
        this.idAbadia = idAbadia;
    }

    public void setIdMovima(int idMovima) {
        this.idMovima = idMovima;
    }

    public void setPrecio_unidad(double precio_unidad) {
        this.precio_unidad = precio_unidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getFecha() {
        return fecha;
    }
}
