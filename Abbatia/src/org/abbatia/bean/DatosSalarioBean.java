package org.abbatia.bean;

import org.apache.struts.action.ActionForm;

/**
 * Created by Benjamín Rodríguez.
 * User: Benjamin
 * Date: 02-mar-2005
 * Time: 13:47:45
 */
public class DatosSalarioBean extends ActionForm {
    private String accion = "inicio";
    private int numGuardias;
    private int salarioGuardia;
    private int salarioTotal;

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public int getNumGuardias() {
        return numGuardias;
    }

    public void setNumGuardias(int numGuardias) {
        this.numGuardias = numGuardias;
    }

    public int getSalarioGuardia() {
        return salarioGuardia;
    }

    public void setSalarioGuardia(int salarioGuardia) {
        this.salarioGuardia = salarioGuardia;
    }

    public int getSalarioTotal() {
        return salarioTotal;
    }

    public void setSalarioTotal(int salarioTotal) {
        this.salarioTotal = salarioTotal;
    }
}
