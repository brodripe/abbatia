package org.abbatia.bean;

import java.util.ArrayList;

public class Novedades {
    private int novedadID;
    private String fecha;
    private ArrayList msg;


    public void setFecha(String fecha) {
        this.fecha = fecha;
    }


    public String getFecha() {
        return fecha;
    }


    public void setMsg(ArrayList msg) {
        this.msg = msg;
    }


    public ArrayList getMsg() {
        return msg;
    }


    public void setNovedadID(int novedadID) {
        this.novedadID = novedadID;
    }


    public int getNovedadID() {
        return novedadID;
    }
}