package org.abbatia.actionform;

import org.apache.struts.action.ActionForm;

import java.util.ArrayList;

public class SiembraActForm extends ActionForm {
    private String accion;
    private int idCampo;
    private int seleccion;
    private ArrayList semillas;


    public void setIdCampo(int idCampo) {
        this.idCampo = idCampo;
    }


    public int getIdCampo() {
        return idCampo;
    }


    public void setSeleccion(int seleccion) {
        this.seleccion = seleccion;
    }


    public int getSeleccion() {
        return seleccion;
    }


    public void setAccion(String accion) {
        this.accion = accion;
    }


    public String getAccion() {
        return accion;
    }


    public void setSemillas(ArrayList semillas) {
        this.semillas = semillas;
    }


    public ArrayList getSemillas() {
        return semillas;
    }

}