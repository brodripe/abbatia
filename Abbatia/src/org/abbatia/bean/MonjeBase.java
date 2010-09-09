package org.abbatia.bean;

import java.io.Serializable;

public class MonjeBase implements Serializable {
    private int idMonje;
    private int idAbadia;
    private int idRegion;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private int estado;
    private int idJerarquia;
    private int idIdioma;


    public int getIdJerarquia() {
        return idJerarquia;
    }

    public void setIdJerarquia(int idJerarquia) {
        this.idJerarquia = idJerarquia;
    }

    public MonjeBase(int p_iIdMonje) {
        this.idMonje = p_iIdMonje;
    }

    public MonjeBase() {
        //To change body of created methods use File | Settings | File Templates.
    }

    public String getApellido2() {
        return apellido2;
    }

    public String getSegundoApellido() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public void setSegundoApellido(String apellido2) {
        this.apellido2 = apellido2;
    }

    public int getIdMonje() {
        return idMonje;
    }

    public int getIdDeMonje() {
        return idMonje;
    }


    public void setIdMonje(int idMonje) {
        this.idMonje = idMonje;
    }

    public void setIdDeMonje(int idMonje) {
        this.idMonje = idMonje;
    }

    public int getIdAbadia() {
        return idAbadia;
    }

    public int getIdDeAbadia() {
        return idAbadia;
    }

    public void setIdAbadia(int idAbadia) {
        this.idAbadia = idAbadia;
    }

    public void setIdDeAbadia(int idAbadia) {
        this.idAbadia = idAbadia;
    }

    public int getIdRegion() {
        return idRegion;
    }

    public void setIdRegion(int idRegion) {
        this.idRegion = idRegion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public String getPrimerApellido() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public void setPrimerApellido(String apellido1) {
        this.apellido1 = apellido1;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getIdDeJerarquia() {
        return idJerarquia;
    }

    public void setIdDeJerarquia(int idDeJerarquia) {
        this.idJerarquia = idDeJerarquia;
    }

    public int getIdIdioma() {
        return idIdioma;
    }

    public void setIdIdioma(int idIdioma) {
        this.idIdioma = idIdioma;
    }

    public int getIdEstado() {
        return estado;
    }

    public void setIdEstado(int idEstado) {
        this.estado = idEstado;
    }

    public String getNombreMonje() {
        return nombre;
    }

    public void setNombreMonje(String nombreMonje) {
        this.nombre = nombreMonje;
    }


}
