package org.abbatia.bean;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Benjamin
 * Date: 27-feb-2006
 * Time: 23:46:03
 * To change this template use File | Settings | File Templates.
 */
public class Enfermedad {
    private int tipoEnfermedad;
    private int variante;
    private int nivel;
    private int propiedadCausa;
    private double salud;
    private int literalid;
    private int duracion;
    private HashMap propiedadImpacto;
    private int probabilidad;
    private String fechaInicio;
    private String fechaFin;
    private short faltaExceso;
    private int literalidMejora;
    private int literalidEmpeora;

    public int getLiteralidMejora() {
        return literalidMejora;
    }

    public void setLiteralidMejora(int literalidMejora) {
        this.literalidMejora = literalidMejora;
    }

    public int getLiteralidEmpeora() {
        return literalidEmpeora;
    }

    public void setLiteralidEmpeora(int literalidEmpeora) {
        this.literalidEmpeora = literalidEmpeora;
    }

    public short getFaltaExceso() {
        return faltaExceso;
    }

    public void setFaltaExceso(short faltaExceso) {
        this.faltaExceso = faltaExceso;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getProbabilidad() {
        return probabilidad;
    }

    public void setProbabilidad(int probabilidad) {
        this.probabilidad = probabilidad;
    }

    public HashMap getPropiedadImpacto() {
        return propiedadImpacto;
    }

    public void setPropiedadImpacto(HashMap propiedadImpacto) {
        this.propiedadImpacto = propiedadImpacto;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public int getLiteralid() {
        return literalid;
    }

    public void setLiteralid(int literalid) {
        this.literalid = literalid;
    }


    public int getTipoEnfermedad() {
        return tipoEnfermedad;
    }

    public void setTipoEnfermedad(int tipoEnfermedad) {
        this.tipoEnfermedad = tipoEnfermedad;
    }

    public int getVariante() {
        return variante;
    }

    public void setVariante(int variante) {
        this.variante = variante;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public int getPropiedadCausa() {
        return propiedadCausa;
    }

    public void setPropiedadCausa(int propiedadCausa) {
        this.propiedadCausa = propiedadCausa;
    }

    public double getSalud() {
        return salud;
    }

    public void setSalud(double salud) {
        this.salud = salud;
    }
}
