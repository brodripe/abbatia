package org.abbatia.actionform;

import org.apache.struts.action.ActionForm;

public class AdminActForm extends ActionForm {
    private String nombreAbadia = "";
    private boolean confirmacion = false;
    private String accion;
    private int idMonje;
    private int idLibro;

    public int getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public AdminActForm() {
    }


    public void setNombreAbadia(String nombreAbadia) {
        this.nombreAbadia = nombreAbadia;
    }


    public String getNombreAbadia() {
        return nombreAbadia;
    }


    public void setAccion(String accion) {
        this.accion = accion;
    }


    public String getAccion() {
        return accion;
    }


    public void setConfirmacion(boolean confirmacion) {
        this.confirmacion = confirmacion;
    }


    public boolean isConfirmacion() {
        return confirmacion;
    }


    public void setIdMonje(int idMonje) {
        this.idMonje = idMonje;
    }


    public int getIdMonje() {
        return idMonje;
    }
}