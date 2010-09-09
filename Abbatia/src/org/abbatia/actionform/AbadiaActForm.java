package org.abbatia.actionform;

import org.apache.commons.validator.GenericValidator;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

public class AbadiaActForm extends ActionForm {
    private int abadia;
    private int orden;
    private int region;
    private int actividad;
    private long usuarioid;
    private String nombreAbadia = "";
    private String fechaconstruccion;
    private String arquitecto = "";
    private int capacidad;
    private int niveljerarquico;
    private String historia = "";
    private ArrayList monjes = new ArrayList();
    private static Logger log = Logger.getLogger(AbadiaActForm.class.getName());

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        if (GenericValidator.isBlankOrNull(nombreAbadia)) {
            errors.add("nombreAbadia", new ActionError("error.nombreabadia.required"));
            return errors;
        }
        return errors;
    }


    public void setUsuarioid(long usuarioid) {
        this.usuarioid = usuarioid;
    }


    public long getUsuarioid() {
        return usuarioid;
    }

    public void setFechaconstruccion(String fechaconstruccion) {
        this.fechaconstruccion = fechaconstruccion;
    }


    public String getFechaconstruccion() {
        return fechaconstruccion;
    }


    public void setArquitecto(String arquitecto) {
        this.arquitecto = arquitecto;
    }


    public String getArquitecto() {
        return arquitecto;
    }


    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }


    public int getCapacidad() {
        return capacidad;
    }


    public void setNiveljerarquico(int niveljerarquico) {
        this.niveljerarquico = niveljerarquico;
    }


    public int getNiveljerarquico() {
        return niveljerarquico;
    }


    public void setHistoria(String historia) {
        this.historia = historia;
    }


    public String getHistoria() {
        return historia;
    }


    public void setAbadia(int abadia) {
        this.abadia = abadia;
    }


    public int getAbadia() {
        return abadia;
    }


    public void setOrden(int orden) {
        this.orden = orden;
    }


    public int getOrden() {
        return orden;
    }


    public void setRegion(int region) {
        this.region = region;
    }


    public int getRegion() {
        return region;
    }


    public void setNombreAbadia(String nombreAbadia) {
        this.nombreAbadia = nombreAbadia;
    }


    public String getNombreAbadia() {
        return nombreAbadia;
    }


    public void setActividad(int actividad) {
        this.actividad = actividad;
    }


    public int getActividad() {
        return actividad;
    }


    public void setMonjes(ArrayList monjes) {
        this.monjes = monjes;
    }


    public ArrayList getMonjes() {
        return monjes;
    }
}