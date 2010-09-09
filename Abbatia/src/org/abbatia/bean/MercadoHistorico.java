package org.abbatia.bean;

public class MercadoHistorico {
    private String accion;
    private String tipo;
    private String abadia;
    private String fecha;
    private String descripcion;
    private String cantidad;
    private double cantidadD;
    private String precioUnidad;
    private double precioUnidadD;
    private String precioTotal;
    private double precioTotalD;

    public double getCantidadD() {
        return cantidadD;
    }

    public void setCantidadD(double cantidadD) {
        this.cantidadD = cantidadD;
    }

    public double getPrecioUnidadD() {
        return precioUnidadD;
    }

    public void setPrecioUnidadD(double precioUnidadD) {
        this.precioUnidadD = precioUnidadD;
    }

    public double getPrecioTotalD() {
        return precioTotalD;
    }

    public void setPrecioTotalD(double precioTotalD) {
        this.precioTotalD = precioTotalD;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getAbadia() {
        return abadia;
    }

    public void setAbadia(String abadia) {
        this.abadia = abadia;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getPrecioUnidad() {
        return precioUnidad;
    }

    public void setPrecioUnidad(String precioUnidad) {
        this.precioUnidad = precioUnidad;
    }

    public String getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(String precioTotal) {
        this.precioTotal = precioTotal;
    }

}
