package org.abbatia.actionform;

import org.apache.struts.action.ActionForm;

/**
 * Created by IntelliJ IDEA.
 * User: Benjamin
 * Date: 14-ene-2007
 * Time: 22:03:46
 * To change this template use File | Settings | File Templates.
 */
public class RecursosPorFamilia extends ActionForm {
    private int idFamilia;
    private String descripcion;
    private double cantidadNecesaria;
    private String cantidadNecesariaString;
    private double cantidadDisponible;
    private String cantidadDisponibleString;
    private String deficid;


    public int getIdFamilia() {
        return idFamilia;
    }

    public void setIdFamilia(int idFamilia) {
        this.idFamilia = idFamilia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getCantidadNecesaria() {
        return cantidadNecesaria;
    }

    public void setCantidadNecesaria(double cantidadNecesaria) {
        this.cantidadNecesaria = cantidadNecesaria;
    }

    public String getCantidadNecesariaString() {
        return cantidadNecesariaString;
    }

    public void setCantidadNecesariaString(String cantidadNecesariaString) {
        this.cantidadNecesariaString = cantidadNecesariaString;
    }

    public double getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(double cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    public String getCantidadDisponibleString() {
        return cantidadDisponibleString;
    }

    public void setCantidadDisponibleString(String cantidadDisponibleString) {
        this.cantidadDisponibleString = cantidadDisponibleString;
    }


    public String getDeficid() {
        return deficid;
    }

    public void setDeficid(String deficid) {
        this.deficid = deficid;
    }
}
