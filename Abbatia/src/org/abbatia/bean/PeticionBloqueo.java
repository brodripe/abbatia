package org.abbatia.bean;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Benjamin
 * Date: 08-sep-2007
 * Time: 17:20:52
 * To change this template use File | Settings | File Templates.
 */
public class PeticionBloqueo implements Serializable {
    private int peticionId;
    private String nombreAbadia;
    private String nombreRegion;
    private String nombreUsuario;
    private String fechaCreacion;
    private String fechaCierre;
    private String motivo;
    private int diasBloqueo;
    private String estado;


    public String getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(String fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public String getNombreRegion() {
        return nombreRegion;
    }

    public void setNombreRegion(String nombreRegion) {
        this.nombreRegion = nombreRegion;
    }

    public int getPeticionId() {
        return peticionId;
    }

    public void setPeticionId(int peticionId) {
        this.peticionId = peticionId;
    }

    public String getNombreAbadia() {
        return nombreAbadia;
    }

    public void setNombreAbadia(String nombreAbadia) {
        this.nombreAbadia = nombreAbadia;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public int getDiasBloqueo() {
        return diasBloqueo;
    }

    public void setDiasBloqueo(int diasBloqueo) {
        this.diasBloqueo = diasBloqueo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
