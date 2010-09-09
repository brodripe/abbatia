package org.abbatia.actionform;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

public class MercadoCompraForm extends ActionForm {

    private static Logger log = Logger.getLogger(MercadoCompraForm.class.getName());

    private String accion;
    private int abadiaid;
    private int mercancia = 1;
    private String mercanciaDesc = "";
    private int familia;
    private String descripcionFamilia = "";
    private String descripcionProducto = "";
    private String descripcionUnidad = "";
    private int productoID;
    private String precio;
    private String precioTotal;
    private int cantidad = 1;
    private int cantidadDisp;
    private int tipo = 0;
    private int mercado = 0;
    private int filtro = 0;
    private int agrupado = 0;
    private int orden = 0;
    private int ordenid = 0;
    private String fechaCaducidad = "";
    public String filtrocontents = "*";
    ArrayList impuestoRegion = null;
    private boolean volverAlMercado;


    public boolean isVolverAlMercado() {
        return volverAlMercado;
    }

    public void setVolverAlMercado(boolean volverAlMercado) {
        this.volverAlMercado = volverAlMercado;
    }

    public String getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(String fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public int getAbadiaid() {
        return abadiaid;
    }

    public void setAbadiaid(int abadiaid) {
        this.abadiaid = abadiaid;
    }

    public ArrayList getImpuestoRegion() {
        return impuestoRegion;
    }

    public void setImpuestoRegion(ArrayList impuestoRegion) {
        this.impuestoRegion = impuestoRegion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }


    public String getAccion() {
        return accion;
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


    public void setPrecio(String precio) {
        this.precio = precio;
    }


    public String getPrecio() {
        return precio;
    }


    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }


    public int getCantidad() {
        return cantidad;
    }


    public void setTipo(int tipo) {
        this.tipo = tipo;
    }


    public int getTipo() {
        return tipo;
    }


    public void setMercado(int mercado) {
        this.mercado = mercado;
    }


    public int getMercado() {
        return mercado;
    }


    public void setFiltro(int filtro) {
        this.filtro = filtro;
    }


    public int getFiltro() {
        return filtro;
    }


    public void setFiltrocontents(String filtrocontents) {
        this.filtrocontents = filtrocontents;
    }


    public String getFiltrocontents() {
        return filtrocontents;
    }


    public void setProductoID(int productoID) {
        this.productoID = productoID;
    }


    public int getProductoID() {
        return productoID;
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


    public void setMercanciaDesc(String mercanciaDesc) {
        this.mercanciaDesc = mercanciaDesc;
    }


    public String getMercanciaDesc() {
        return mercanciaDesc;
    }


    public void setDescripcionUnidad(String descripcionUnidad) {
        this.descripcionUnidad = descripcionUnidad;
    }


    public String getDescripcionUnidad() {
        return descripcionUnidad;
    }


    public void setCantidadDisp(int cantidadDisp) {
        this.cantidadDisp = cantidadDisp;
    }


    public int getCantidadDisp() {
        return cantidadDisp;
    }


    public void setPrecioTotal(String precioTotal) {
        this.precioTotal = precioTotal;
    }

    public void setAgrupado(int agrupado) {
        this.agrupado = agrupado;
    }

    public void setOrdenid(int ordenid) {
        this.ordenid = ordenid;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public String getPrecioTotal() {
        return precioTotal;
    }

    public int getAgrupado() {
        return agrupado;
    }

    public int getOrdenid() {
        return ordenid;
    }

    public int getOrden() {
        return orden;
    }

    /**
     * Validate the properties that have been set for this HTTP request,
     * and return an <code>ActionErrors</code> object that encapsulates any
     * validation errors that have been found.  If no errors are found,
     * return <code>null</code> or an <code>ActionErrors</code> object with
     * no recorded error messages.
     * <p/>
     * The default ipmlementation performs no validation and returns
     * <code>null</code>.  Subclasses must override this method to provide
     * any validation they wish to perform.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        String sDestino = request.getRequestURI();
        log.debug("BuscarMercadoCompraForm. validate. sDestino: " + sDestino);

        ActionErrors errors = new ActionErrors();
        if (accion != null && accion.equals("comprar")) {
            if (cantidad < 1) {
                log.debug("validate. cantidad: " + cantidad);
                //request.setAttribute("CompraForm", request.getAttribute("CompraForm"));
                errors.add("precio", new ActionError("error.cantidad.incorrecta"));
                return errors;
            }
        }
        return errors;
    }


}
