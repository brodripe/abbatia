package org.abbatia.actionform;

import org.apache.struts.action.ActionForm;

public class ListaMonjesActForm extends ActionForm {
    private String codigo = "";
    private String nombre = "";
    private String fnacimiento = "";


    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }


    public String getCodigo() {
        return codigo;
    }


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public String getNombre() {
        return nombre;
    }


    public void setFnacimiento(String fnacimiento) {
        this.fnacimiento = fnacimiento;
    }


    public String getFnacimiento() {
        return fnacimiento;
    }


}