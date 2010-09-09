package org.abbatia.actionform;

import org.apache.struts.action.ActionForm;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Benjamin
 * Date: 04-nov-2004
 * Time: 23:05:22
 * To change this template use File | Settings | File Templates.
 */
public class DatosMolinoActForm extends ActionForm {
    private int seleccion;
    private int cantidad;
    private ArrayList productos;

    public int getSeleccion() {
        return seleccion;
    }

    public void setSeleccion(int seleccion) {
        this.seleccion = seleccion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public ArrayList getProductos() {
        return productos;
    }

    public void setProductos(ArrayList productos) {
        this.productos = productos;
    }
}
