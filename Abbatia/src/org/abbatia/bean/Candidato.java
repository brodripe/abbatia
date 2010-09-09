package org.abbatia.bean;

/**
 * Created by IntelliJ IDEA.
 * User: Benjamin
 * Date: 25-oct-2004
 * Time: 16:14:34
 * To change this template use File | Settings | File Templates.
 */
public class Candidato {
    public int idMonje;
    public int idAbadia;
    public int idOrden;
    public String nombreMonje;
    public String nombreAbadia;
    public String nombreOrden;
    public String descripcion;
    public int votos;
    public int idRegion;
    public int votable;

    public int getVotable() {
        return votable;
    }

    public void setVotable(int votable) {
        this.votable = votable;
    }

    public int getIdRegion() {
        return idRegion;
    }

    public void setIdRegion(int idRegion) {
        this.idRegion = idRegion;
    }

    public int getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(int idOrden) {
        this.idOrden = idOrden;
    }

    public String getNombreOrden() {
        return nombreOrden;
    }

    public void setNombreOrden(String nombreOrden) {
        this.nombreOrden = nombreOrden;
    }

    public int getIdMonje() {
        return idMonje;
    }

    public void setIdMonje(int idMonje) {
        this.idMonje = idMonje;
    }

    public String getNombreMonje() {
        return nombreMonje;
    }

    public void setNombreMonje(String nombreMonje) {
        this.nombreMonje = nombreMonje;
    }

    public int getIdAbadia() {
        return idAbadia;
    }

    public void setIdAbadia(int idAbadia) {
        this.idAbadia = idAbadia;
    }

    public String getNombreAbadia() {
        return nombreAbadia;
    }

    public void setNombreAbadia(String nombreAbadia) {
        this.nombreAbadia = nombreAbadia;
    }

    public int getVotos() {
        return votos;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setVotos(int votos) {
        this.votos = votos;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
