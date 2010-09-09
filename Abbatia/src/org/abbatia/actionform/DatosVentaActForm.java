package org.abbatia.actionform;

import org.abbatia.utils.Utilidades;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

public class DatosVentaActForm extends ActionForm {
    private int productoID;
    private int cantidad;
    private String descripcionFamilia;
    private String descripcionProducto;
    private String precio;
    private String mercancia;
    private String precioVentaCiudad;
    private boolean venderACiudad;
    private int comision;
    private String fechaInicio;
    private String fechaFin;

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getComision() {
        return comision;
    }

    public void setComision(int comision) {
        this.comision = comision;
    }

    private static Logger log = Logger.getLogger(DatosVentaActForm.class.getName());

    public void setProductoID(int productoID) {
        this.productoID = productoID;
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

    public int getProductoID() {
        return productoID;
    }


    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }


    public int getCantidad() {
        return cantidad;
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

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (precio != null) {
            double dPrecio = Utilidades.formatStringToDouble(precio);
            if (dPrecio < 0.1) {
                errors.add("precio", new ActionError("error.importe.negativo"));
                return errors;
            }
        }
        return errors;
    }

}