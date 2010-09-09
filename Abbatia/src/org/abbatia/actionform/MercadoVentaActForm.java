package org.abbatia.actionform;

import org.apache.struts.action.ActionForm;

public class MercadoVentaActForm extends ActionForm {
    private int mercancia = 1;
    private int familia;
    private String id = "";
    private String precio;
    private int dias;
    private String cantidad;
    private String cantidadDisponible;
    private int tipoVenta;
    private String mercado;
    private int nivelAnimal;
    private int tipoAnimal;
    private String fechaAnimal;
    private String descripcionProducto;
    private String descripcionFamilia;
    private String descripcionUnidad;
    private int duracionMercado;
    private String precioVentaCiudad;
    private boolean venderACiudad;
    private boolean usuarioNuevo;
    private boolean existeMercado;
    private double cantidadDouble;


    public boolean isExisteMercado() {
        return existeMercado;
    }

    public void setExisteMercado(boolean existeMercado) {
        this.existeMercado = existeMercado;
    }

    public double getCantidadDouble() {
        return cantidadDouble;
    }

    public void setCantidadDouble(double cantidadDouble) {
        this.cantidadDouble = cantidadDouble;
    }

    public boolean isUsuarioNuevo() {
        return usuarioNuevo;
    }

    public void setUsuarioNuevo(boolean usuarioNuevo) {
        this.usuarioNuevo = usuarioNuevo;
    }

    public String getPrecioVentaCiudad() {
        return precioVentaCiudad;
    }

    public void setPrecioVentaCiudad(String precioVentaCiudad) {
        this.precioVentaCiudad = precioVentaCiudad;
    }

    public boolean isVenderACiudad() {
        return venderACiudad;
    }

    public void setVenderACiudad(boolean venderACiudad) {
        this.venderACiudad = venderACiudad;
    }

    public void setMercancia(int mercancia) {
        this.mercancia = mercancia;
    }


    public int getMercancia() {
        return mercancia;
    }


    public void setFamilia(int familia) {
        this.familia = familia;
    }


    public int getFamilia() {
        return familia;
    }


    public void setId(String id) {
        this.id = id;
    }


    public String getId() {
        return id;
    }


    public void setPrecio(String precio) {
        this.precio = precio;
    }


    public String getPrecio() {
        return precio;
    }


    public void setDias(int dias) {
        this.dias = dias;
    }


    public int getDias() {
        return dias;
    }


    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }


    public String getCantidad() {
        return cantidad;
    }


    public void setMercado(String mercado) {
        this.mercado = mercado;
    }


    public String getMercado() {
        return mercado;
    }


    public void setNivelAnimal(int nivelAnimal) {
        this.nivelAnimal = nivelAnimal;
    }


    public int getNivelAnimal() {
        return nivelAnimal;
    }


    public void setTipoAnimal(int tipoAnimal) {
        this.tipoAnimal = tipoAnimal;
    }


    public int getTipoAnimal() {
        return tipoAnimal;
    }


    public void setFechaAnimal(String fechaAnimal) {
        this.fechaAnimal = fechaAnimal;
    }


    public String getFechaAnimal() {
        return fechaAnimal;
    }


    public void setCantidadDisponible(String cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }


    public String getCantidadDisponible() {
        return cantidadDisponible;
    }


    public void setDescripcionProducto(String descripcionProducto) {
        this.descripcionProducto = descripcionProducto;
    }


    public String getDescripcionProducto() {
        return descripcionProducto;
    }


    public void setDescripcionFamilia(String descripcionFamilia) {
        this.descripcionFamilia = descripcionFamilia;
    }


    public String getDescripcionFamilia() {
        return descripcionFamilia;
    }


    public void setDescripcionUnidad(String descripcionUnidad) {
        this.descripcionUnidad = descripcionUnidad;
    }


    public String getDescripcionUnidad() {
        return descripcionUnidad;
    }


    public void setDuracionMercado(int duracionMercado) {
        this.duracionMercado = duracionMercado;
    }


    public int getDuracionMercado() {
        return duracionMercado;
    }


    public void setTipoVenta(int tipoVenta) {
        this.tipoVenta = tipoVenta;
    }


    public int getTipoVenta() {
        return tipoVenta;
    }


}