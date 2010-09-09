package org.abbatia.bean;

/**
 * Created by IntelliJ IDEA.
 * User: Benjamin
 * Date: 22-nov-2004
 * Time: 22:54:27
 * To change this template use File | Settings | File Templates.
 */
public class datosElaboracion {
    private int idElaboracion;
    private int idAbadia;
    private int idEdificio;
    private int idProducto;
    private String descProducto;
    private int estado;
    private String descEstado;
    private int cantidad;
    private String fecha_inicio;
    private String fecha_fin = "0000-00-00";
    private double elaborado;
    private String elaboradoS;
    private String unidadMedida;
    private int ctd_dia;
    private int ctd_total;
    private int dias_total;
    private String tipoProducto;

    public String getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(String fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public String getElaboradoS() {
        return elaboradoS;
    }

    public void setElaboradoS(String elaboradoS) {
        this.elaboradoS = elaboradoS;
    }

    public int getIdAbadia() {
        return idAbadia;
    }

    public void setIdAbadia(int idAbadia) {
        this.idAbadia = idAbadia;
    }

    public String getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(String tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    public int getDias_total() {
        return dias_total;
    }

    public void setDias_total(int dias_total) {
        this.dias_total = dias_total;
    }

    public int getIdElaboracion() {
        return idElaboracion;
    }

    public void setIdElaboracion(int idElaboracion) {
        this.idElaboracion = idElaboracion;
    }

    public int getCtd_dia() {
        return ctd_dia;
    }

    public void setCtd_dia(int ctd_dia) {
        this.ctd_dia = ctd_dia;
    }

    public int getCtd_total() {
        return ctd_total;
    }

    public void setCtd_total(int ctd_total) {
        this.ctd_total = ctd_total;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public String getDescProducto() {
        return descProducto;
    }

    public void setDescProducto(String descProducto) {
        this.descProducto = descProducto;
    }

    public String getDescEstado() {
        return descEstado;
    }

    public void setDescEstado(String descEstado) {
        this.descEstado = descEstado;
    }

    public double getElaborado() {
        return elaborado;
    }

    public void setElaborado(double elaborado) {
        this.elaborado = elaborado;
    }

    public int getIdEdificio() {
        return idEdificio;
    }

    public void setIdEdificio(int idEdificio) {
        this.idEdificio = idEdificio;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(String fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }
}
