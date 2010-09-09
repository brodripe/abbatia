package org.abbatia.actionform;

import org.apache.struts.action.ActionForm;

public class BuscarMercadoCompraForm extends ActionForm {
    private String accion;
    private int mercancia = 1;
    private int familia;
    private int tipo = 0;
    private int mercado = 0;
    private int filtro = 0;
    private int orden = 0;
    private int ordenid = 0;
    private int pagina = 0;
    private String filtrocontents = "";

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

    public void setOrdenid(int ordenid) {
        this.ordenid = ordenid;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public int getOrdenid() {
        return ordenid;
    }

    public int getOrden() {
        return orden;
    }

    public void setPagina(int pagina) {
        this.pagina = pagina;
    }

    public int getPagina() {
        return pagina;
    }
}
