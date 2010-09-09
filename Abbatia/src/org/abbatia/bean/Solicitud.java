package org.abbatia.bean;

/**
 * Created by IntelliJ IDEA.
 * User: Benjamin
 * Date: 12-mar-2005
 * Time: 21:26:27
 * To change this template use File | Settings | File Templates.
 */
public class Solicitud {
    private int idSolicitud;
    private int idAbadia;
    private String nombreAbadia;
    private int idMonje;
    private String fechaCreacion;
    private int idTipoSolicitud;
    private short estado;
    private String texto;
    private int votosNecesarios;
    private int diasVigencia;
    private short valorDefecto;
    private int literalSi;
    private int literalNo;
    private int votosSi;
    private int votosNo;
    private int votosPendiente;
    private int idIdioma;
    private int Regionid;
    private String accion;
    private int totalVotos;
    private int[] votantes;
    private String fechaCaduca;
    private short voto;

    public int getRegionid() {
        return Regionid;
    }

    public void setRegionid(int regionid) {
        Regionid = regionid;
    }

    public short getVoto() {
        return voto;
    }

    public void setVoto(short voto) {
        this.voto = voto;
    }

    public String getNombreAbadia() {
        return nombreAbadia;
    }

    public void setNombreAbadia(String nombreAbadia) {
        this.nombreAbadia = nombreAbadia;
    }

    public String getFechaCaduca() {
        return fechaCaduca;
    }

    public void setFechaCaduca(String fecha_caduca) {
        this.fechaCaduca = fecha_caduca;
    }

    public int[] getVotantes() {
        return votantes;
    }

    public void setVotantes(int[] votantes) {
        this.votantes = votantes;
    }

    public int getTotalVotos() {
        return totalVotos;
    }

    public void setTotalVotos(int totalVotos) {
        this.totalVotos = totalVotos;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public int getIdIdioma() {
        return idIdioma;
    }

    public void setIdIdioma(int idIdioma) {
        this.idIdioma = idIdioma;
    }

    public void setRegion(int Regionid) {
        this.Regionid = Regionid;
    }

    public int getRegion() {
        return Regionid;
    }


    public int getVotosSi() {
        return votosSi;
    }

    public void setVotosSi(int votosSi) {
        this.votosSi = votosSi;
    }

    public int getVotosNo() {
        return votosNo;
    }

    public void setVotosNo(int votosNo) {
        this.votosNo = votosNo;
    }

    public int getVotosPendiente() {
        return votosPendiente;
    }

    public void setVotosPendiente(int votosPendiente) {
        this.votosPendiente = votosPendiente;
    }

    public int getLiteralSi() {
        return literalSi;
    }

    public void setLiteralSi(int literalSi) {
        this.literalSi = literalSi;
    }

    public int getLiteralNo() {
        return literalNo;
    }

    public void setLiteralNo(int literalNo) {
        this.literalNo = literalNo;
    }

    public short getValorDefecto() {
        return valorDefecto;
    }

    public void setValorDefecto(short valorDefecto) {
        this.valorDefecto = valorDefecto;
    }

    public int getDiasVigencia() {
        return diasVigencia;
    }

    public void setDiasVigencia(int diasVigencia) {
        this.diasVigencia = diasVigencia;
    }

    public int getVotosNecesarios() {
        return votosNecesarios;
    }

    public void setVotosNecesarios(int votosNecesarios) {
        this.votosNecesarios = votosNecesarios;
    }

    public int getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(int idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public int getIdAbadia() {
        return idAbadia;
    }

    public void setIdAbadia(int idAbadia) {
        this.idAbadia = idAbadia;
    }

    public int getIdMonje() {
        return idMonje;
    }

    public void setIdMonje(int idMonje) {
        this.idMonje = idMonje;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public int getIdTipoSolicitud() {
        return idTipoSolicitud;
    }

    public void setIdTipoSolicitud(int idTipoSolicitud) {
        this.idTipoSolicitud = idTipoSolicitud;
    }

    public short getEstado() {
        return estado;
    }

    public void setEstado(short estado) {
        this.estado = estado;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
