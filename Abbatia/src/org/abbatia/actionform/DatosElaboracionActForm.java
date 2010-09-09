package org.abbatia.actionform;

import org.apache.struts.action.ActionForm;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Benjamin
 * Date: 21-nov-2004
 * Time: 14:28:12
 * To change this template use File | Settings | File Templates.
 */
public class DatosElaboracionActForm extends ActionForm {
    private int idProducto;
    private String descProducto;
    private int idAbadia;
    private int cantidad = 0;
    private String fecha_inicio;
    private int estado;
    private String unidadMedida;

    private ArrayList requisitos;

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public ArrayList getRequisitos() {
        return requisitos;
    }

    public void setRequisitos(ArrayList requisitos) {
        this.requisitos = requisitos;
    }

    public String getDescProducto() {
        return descProducto;
    }

    public void setDescProducto(String descProducto) {
        this.descProducto = descProducto;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getIdAbadia() {
        return idAbadia;
    }

    public void setIdAbadia(int idAbadia) {
        this.idAbadia = idAbadia;
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

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

}
