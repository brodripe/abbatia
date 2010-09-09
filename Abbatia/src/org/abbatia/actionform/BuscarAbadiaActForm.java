package org.abbatia.actionform;

import org.apache.struts.action.ActionForm;

import java.util.ArrayList;

public class BuscarAbadiaActForm extends ActionForm {
    private ArrayList contents;
    private String search;
    private int region = -1;
    private int busqueda;
    private int pagina;
    private int total;
    private int opcion;
    private int orden;
    private int ordenid;

    public int getOpcion() {
        return opcion;
    }

    public String getSearch() {
        return search;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public void setOpcion(int opcion) {
        this.opcion = opcion;
    }

    public void setSearch(String Search) {
        this.search = Search;
    }

    public int getOrden() {
        return orden;
    }

    public void setContents(ArrayList Contents) {
        this.contents = Contents;
    }

    public void setOrdenid(int ordenid) {
        this.ordenid = ordenid;
    }

    public void setPagina(int pagina) {
        this.pagina = pagina;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setBusqueda(int busqueda) {
        this.busqueda = busqueda;
    }

    public ArrayList getContents() {
        return contents;
    }

    public int getOrdenid() {
        return ordenid;
    }

    public int getPagina() {
        return pagina;
    }

    public int getTotal() {
        return total;
    }

    public int getBusqueda() {
        return busqueda;
    }

    public int getRegion() {
        return region;
    }

    public void setRegion(int region) {
        this.region = region;
    }
}
