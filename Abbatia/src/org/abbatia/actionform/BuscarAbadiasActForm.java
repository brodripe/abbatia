package org.abbatia.actionform;

import org.apache.struts.action.ActionForm;

import java.util.ArrayList;

public class BuscarAbadiasActForm extends ActionForm {
    private String accion = "inicio";
    private int region;
    private int orden;
    private String nombre;
    private ArrayList listado;
    private long[] seleccion;
    private boolean administradores = false;


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setRegion(int region) {
        this.region = region;
    }

    public int getRegion() {
        return region;
    }


    public void setOrden(int orden) {
        this.orden = orden;
    }


    public int getOrden() {
        return orden;
    }


    public void setListado(ArrayList listado) {
        this.listado = listado;
    }


    public ArrayList getListado() {
        return listado;
    }


    public void setSeleccion(long[] seleccion) {
        this.seleccion = seleccion;
    }


    public long[] getSeleccion() {
        return seleccion;
    }


    public void setAccion(String accion) {
        this.accion = accion;
    }


    public String getAccion() {
        return accion;
    }


    public void setAdministradores(boolean administradores) {
        this.administradores = administradores;
    }


    public boolean isAdministradores() {
        return administradores;
    }

}