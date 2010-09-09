package org.abbatia.actionform;

import org.apache.struts.action.ActionForm;

import java.util.ArrayList;

public class DatosCampoActForm extends ActionForm {
    private String accion = "inicio";
    private int idCampo;
    private ArrayList monjes;
    private int[] seleccion;


    public int getIdCampo() {
        return idCampo;
    }

    public void setIdCampo(int idCampo) {
        this.idCampo = idCampo;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public ArrayList getMonjes() {
        return monjes;
    }

    public void setMonjes(ArrayList monjes) {
        this.monjes = monjes;
    }

    public int[] getSeleccion() {
        return seleccion;
    }

    public void setSeleccion(int[] seleccion) {
        this.seleccion = seleccion;
    }
}