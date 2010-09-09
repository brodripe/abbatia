package org.abbatia.bean;

import java.io.Serializable;

/**
 * Agrupa la información a una entrada del mercado que se muestra en formato agrupado.
 */
public class MercadoAgrupado implements Serializable {
    private String cantidad;
    private String precioMinimo;
    private String precioMaximo;
    private String descripcion;
    private String ventas;
    private String unidadDescripcion;
    private String familia;
    private int mercancia;
    private String nivel;

    public void setVentas(String ventas) {
        this.ventas = ventas;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setPrecioMaximo(String precioMaximo) {
        this.precioMaximo = precioMaximo;
    }

    public void setPrecioMinimo(String precioMinimo) {
        this.precioMinimo = precioMinimo;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getPrecioMaximo() {
        return precioMaximo;
    }

    public String getPrecioMinimo() {
        return precioMinimo;
    }

    public String getCantidad() {
        return cantidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getVentas() {
        return ventas;
    }

    public void setUnidadDescripcion(String unidadDescripcion) {
        this.unidadDescripcion = unidadDescripcion;
    }

    public String getUnidadDescripcion() {
        return unidadDescripcion;
    }

    public void setFamilia(String familia) {
        this.familia = familia;
    }

    public String getFamilia() {
        return familia;
    }

    public void setMercancia(int mercancia) {
        this.mercancia = mercancia;
    }

    public int getMercancia() {
        return mercancia;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getNivel() {
        return nivel;
    }
}
