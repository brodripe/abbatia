package org.abbatia.actionform;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;

import java.util.ArrayList;

public class DatosVentaAgrupadaActForm extends ActionForm {
    private static Logger log = Logger.getLogger(DatosVentaAgrupadaActForm.class.getName());
    private int idProducto;
    private String descripcionFamilia;
    private String descripcionProducto;
    private String precioMercado;
    private String precio;
    private String mercancia;
    private ArrayList listaProductos;
    private String accion;
    private String precioVentaCiudad;
    private int[] seleccion;
    private boolean venderACiudad;
    private boolean existeMercado;
    private boolean usuarioNuevo;


    public boolean isUsuarioNuevo() {
        return usuarioNuevo;
    }

    public void setUsuarioNuevo(boolean usuarioNuevo) {
        this.usuarioNuevo = usuarioNuevo;
    }

    public boolean isExisteMercado() {
        return existeMercado;
    }

    public void setExisteMercado(boolean existeMercado) {
        this.existeMercado = existeMercado;
    }

    public boolean isVenderACiudad() {
        return venderACiudad;
    }

    public void setVenderACiudad(boolean venderACiudad) {
        this.venderACiudad = venderACiudad;
    }

    public static Logger getLog() {
        return log;
    }

    public static void setLog(Logger log) {
        DatosVentaAgrupadaActForm.log = log;
    }

    public String getPrecioVentaCiudad() {
        return precioVentaCiudad;
    }

    public void setPrecioVentaCiudad(String precioVentaCiudad) {
        this.precioVentaCiudad = precioVentaCiudad;
    }

    public int[] getSeleccion() {
        return seleccion;
    }

    public void setSeleccion(int[] seleccion) {
        this.seleccion = seleccion;
    }

    public String getPrecioMercado() {
        return precioMercado;
    }

    public void setPrecioMercado(String precioMercado) {
        this.precioMercado = precioMercado;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }


    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public ArrayList getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(ArrayList listaProductos) {
        this.listaProductos = listaProductos;
    }


    public void setPrecio(String precio) {
        this.precio = precio;
    }


    public String getPrecio() {
        return precio;
    }


    public void setMercancia(String mercancia) {
        this.mercancia = mercancia;
    }


    public String getMercancia() {
        return mercancia;
    }


    public void setDescripcionFamilia(String descripcionFamilia) {
        this.descripcionFamilia = descripcionFamilia;
    }


    public String getDescripcionFamilia() {
        return descripcionFamilia;
    }


    public void setDescripcionProducto(String descripcionProducto) {
        this.descripcionProducto = descripcionProducto;
    }


    public String getDescripcionProducto() {
        return descripcionProducto;
    }


}