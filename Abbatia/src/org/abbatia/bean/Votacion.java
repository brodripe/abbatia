package org.abbatia.bean;

import org.apache.struts.action.ActionForm;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Benjamin
 * Date: 25-oct-2004
 * Time: 17:13:22
 * To change this template use File | Settings | File Templates.
 */
public class Votacion extends ActionForm {
    private long idAbadia;
    private int idRegion;
    private String nombreRegion;
    private String fechaInicio;
    private String fechaFin;
    private String descripcion;
    private int puedeVotar;
    private ArrayList candidatos;
    private int totalVotantes;
    private int pendientesVoto;
    private int seleccion;

    public int getTotalVotantes() {
        return totalVotantes;
    }

    public void setTotalVotantes(int totalVotantes) {
        this.totalVotantes = totalVotantes;
    }

    public int getPendientesVoto() {
        return pendientesVoto;
    }

    public void setPendientesVoto(int pendientesVoto) {
        this.pendientesVoto = pendientesVoto;
    }

    public int getPuedeVotar() {
        return puedeVotar;
    }

    public void setPuedeVotar(int puedeVotar) {
        this.puedeVotar = puedeVotar;
    }

    public int getSeleccion() {
        return seleccion;
    }

    public void setSeleccion(int seleccion) {
        this.seleccion = seleccion;
    }

    public int getIdRegion() {
        return idRegion;
    }

    public void setIdRegion(int idRegion) {
        this.idRegion = idRegion;
    }

    public String getNombreRegion() {
        return nombreRegion;
    }

    public void setNombreRegion(String nombreRegion) {
        this.nombreRegion = nombreRegion;
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

    public ArrayList getCandidatos() {
        return candidatos;
    }

    public long getIdAbadia() {
        return idAbadia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setCandidatos(ArrayList candidatos) {
        this.candidatos = candidatos;
    }

    public void setIdAbadia(long idAbadia) {
        this.idAbadia = idAbadia;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setIdAbadia(int idAbadia) {
        this.idAbadia = idAbadia;
    }

}