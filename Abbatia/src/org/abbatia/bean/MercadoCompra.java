package org.abbatia.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class MercadoCompra implements Serializable {

    // Páginador  ( contador, página actual, páginas )
    private int count;
    private int page_actual;
    private int pages;
    private String navigate;
    private ArrayList lstCompra;

    public void setLstCompra(ArrayList lstCompra) {
        this.lstCompra = lstCompra;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public void setPage_actual(int page_actual) {
        this.page_actual = page_actual;
    }

    public void setNavigate(String navigate) {
        this.navigate = navigate;
    }

    public ArrayList getLstCompra() {
        return lstCompra;
    }

    public int getCount() {
        return count;
    }

    public int getPages() {
        return pages;
    }

    public int getPage_actual() {
        return page_actual;
    }

    public String getNavigate() {
        return navigate;
    }
}
