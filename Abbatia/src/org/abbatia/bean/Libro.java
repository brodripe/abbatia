package org.abbatia.bean;

import java.io.Serializable;

public class Libro implements Serializable {
    private int idLibro;
    private int idLibro_origen;
    private int idLibroTipo;
    private int idEdificio;
    private int idAbadia;
    private String nombreAbadia;
    private int idMonje;
    private int idIdioma;
    private String idioma_desc;
    private int idAbadia_copia;
    private String nombreAbadia_copia;
    private int idIdioma_origen;
    private int idIdioma_usuario;
    private String nombreLibro;
    private String nombreLibroNivel;
    private String descLibro;
    private String nombreMonje;
    private short estado;
    private String descEstado;
    private short nivel;
    private String fecha_adquisicion;
    private String fecha_creacion;
    private int numPaginas;
    private double numPaginasCopiadas;
    private double desgaste;
    private int copiable;
    private int idIdioma_region;
    private String progreso;
    private String deterioro;
    private String precioCopiaS;
    private double precioCopia;
    private int idRegion;
    private String nombreRegion;
    private double precioMax;
    private double precioMin;
    private int esCopiaLocal;
    private double ctdPielEncuadernar;
    private String ocupacion;
    private int franjasOcupadas;
    private String grafico;

    public String getGrafico() {
        return grafico;
    }

    public void setGrafico(String grafico) {
        this.grafico = grafico;
    }

    public String getDeterioro() {
        return deterioro;
    }

    public void setDeterioro(String deterioro) {
        this.deterioro = deterioro;
    }

    public String getNombreLibroNivel() {
        return nombreLibroNivel;
    }

    public void setNombreLibroNivel(String nombreLibroNivel) {
        this.nombreLibroNivel = nombreLibroNivel;
    }

    public int getFranjasOcupadas() {
        return franjasOcupadas;
    }

    public void setFranjasOcupadas(int franjasOcupadas) {
        this.franjasOcupadas = franjasOcupadas;
    }

    public int getIdRegion() {
        return idRegion;
    }

    public void setIdRegion(int idRegion) {
        this.idRegion = idRegion;
    }

    public String getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    public double getCtdPielEncuadernar() {
        return ctdPielEncuadernar;
    }

    public void setCtdPielEncuadernar(double ctdPielEncuadernar) {
        this.ctdPielEncuadernar = ctdPielEncuadernar;
    }

    public int getEsCopiaLocal() {
        return esCopiaLocal;
    }

    public void setEsCopiaLocal(int esCopiaLocal) {
        this.esCopiaLocal = esCopiaLocal;
    }


    public double getPrecioMax() {
        return precioMax;
    }

    public void setPrecioMax(double precioMax) {
        this.precioMax = precioMax;
    }

    public double getPrecioMin() {
        return precioMin;
    }

    public void setPrecioMin(double precioMin) {
        this.precioMin = precioMin;
    }

    public String getNombreRegion() {
        return nombreRegion;
    }

    public void setNombreRegion(String nombreRegion) {
        this.nombreRegion = nombreRegion;
    }

    public int getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public int getIdLibro_origen() {
        return idLibro_origen;
    }

    public void setIdLibro_origen(int idLibro_origen) {
        this.idLibro_origen = idLibro_origen;
    }

    public int getIdLibroTipo() {
        return idLibroTipo;
    }

    public void setIdLibroTipo(int idLibroTipo) {
        this.idLibroTipo = idLibroTipo;
    }

    public int getIdEdificio() {
        return idEdificio;
    }

    public void setIdEdificio(int idEdificio) {
        this.idEdificio = idEdificio;
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

    public int getIdMonje() {
        return idMonje;
    }

    public void setIdMonje(int idMonje) {
        this.idMonje = idMonje;
    }

    public int getIdIdioma() {
        return idIdioma;
    }

    public void setIdIdioma(int idIdioma) {
        this.idIdioma = idIdioma;
    }

    public String getIdioma_desc() {
        return idioma_desc;
    }

    public void setIdioma_desc(String idioma_desc) {
        this.idioma_desc = idioma_desc;
    }

    public int getIdAbadia_copia() {
        return idAbadia_copia;
    }

    public void setIdAbadia_copia(int idAbadia_copia) {
        this.idAbadia_copia = idAbadia_copia;
    }

    public String getNombreAbadia_copia() {
        return nombreAbadia_copia;
    }

    public void setNombreAbadia_copia(String nombreAbadia_copia) {
        this.nombreAbadia_copia = nombreAbadia_copia;
    }

    public int getIdIdioma_origen() {
        return idIdioma_origen;
    }

    public void setIdIdioma_origen(int idIdioma_origen) {
        this.idIdioma_origen = idIdioma_origen;
    }

    public int getIdIdioma_usuario() {
        return idIdioma_usuario;
    }

    public void setIdIdioma_usuario(int idIdioma_usuario) {
        this.idIdioma_usuario = idIdioma_usuario;
    }

    public int getIdIdioma_region() {
        return idIdioma_usuario;
    }

    public void setIdIdioma_region(int idIdioma_region) {
        this.idIdioma_region = idIdioma_region;
    }

    public String getNombreLibro() {
        return nombreLibro;
    }

    public void setNombreLibro(String nombreLibro) {
        this.nombreLibro = nombreLibro;
    }

    public String getDescLibro() {
        return descLibro;
    }

    public void setDescLibro(String descLibro) {
        this.descLibro = descLibro;
    }

    public String getNombreMonje() {
        return nombreMonje;
    }

    public void setNombreMonje(String nombreMonje) {
        this.nombreMonje = nombreMonje;
    }

    public short getEstado() {
        return estado;
    }

    public void setEstado(short estado) {
        this.estado = estado;
    }

    public String getDescEstado() {
        return descEstado;
    }

    public void setDescEstado(String descEstado) {
        this.descEstado = descEstado;
    }

    public short getNivel() {
        return nivel;
    }

    public void setNivel(short nivel) {
        this.nivel = nivel;
    }

    public String getFecha_adquisicion() {
        return fecha_adquisicion;
    }

    public void setFecha_adquisicion(String fecha_adquisicion) {
        this.fecha_adquisicion = fecha_adquisicion;
    }

    public String getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(String fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public int getNumPaginas() {
        return numPaginas;
    }

    public void setNumPaginas(int numPaginas) {
        this.numPaginas = numPaginas;
    }

    public double getNumPaginasCopiadas() {
        return numPaginasCopiadas;
    }

    public void setNumPaginasCopiadas(double numPaginasCopiadas) {
        this.numPaginasCopiadas = numPaginasCopiadas;
    }

    public double getDesgaste() {
        return desgaste;
    }

    public void setDesgaste(double desgaste) {
        this.desgaste = desgaste;
    }

    public int getCopiable() {
        return copiable;
    }

    public void setCopiable(int copiable) {
        this.copiable = copiable;
    }

    public String getProgreso() {
        return progreso;
    }

    public void setProgreso(String progreso) {
        this.progreso = progreso;
    }

    public String getPrecioCopiaS() {
        return precioCopiaS;
    }

    public void setPrecioCopiaS(String precioCopiaS) {
        this.precioCopiaS = precioCopiaS;
    }

    public double getPrecioCopia() {
        return precioCopia;
    }

    public void setPrecioCopia(double precioCopia) {
        this.precioCopia = precioCopia;
    }

    @Override
    public String toString() {
        return "Libro{" +
                "idLibro=" + idLibro +
                ", idLibro_origen=" + idLibro_origen +
                ", idLibroTipo=" + idLibroTipo +
                ", idEdificio=" + idEdificio +
                ", idAbadia=" + idAbadia +
                ", nombreAbadia='" + nombreAbadia + '\'' +
                ", idMonje=" + idMonje +
                ", idIdioma=" + idIdioma +
                ", idioma_desc='" + idioma_desc + '\'' +
                ", idAbadia_copia=" + idAbadia_copia +
                ", nombreAbadia_copia='" + nombreAbadia_copia + '\'' +
                ", idIdioma_origen=" + idIdioma_origen +
                ", idIdioma_usuario=" + idIdioma_usuario +
                ", nombreLibro='" + nombreLibro + '\'' +
                ", nombreLibroNivel='" + nombreLibroNivel + '\'' +
                ", descLibro='" + descLibro + '\'' +
                ", nombreMonje='" + nombreMonje + '\'' +
                ", estado=" + estado +
                ", descEstado='" + descEstado + '\'' +
                ", nivel=" + nivel +
                ", fecha_adquisicion='" + fecha_adquisicion + '\'' +
                ", fecha_creacion='" + fecha_creacion + '\'' +
                ", numPaginas=" + numPaginas +
                ", numPaginasCopiadas=" + numPaginasCopiadas +
                ", desgaste=" + desgaste +
                ", copiable=" + copiable +
                ", idIdioma_region=" + idIdioma_region +
                ", progreso='" + progreso + '\'' +
                ", deterioro='" + deterioro + '\'' +
                ", precioCopiaS='" + precioCopiaS + '\'' +
                ", precioCopia=" + precioCopia +
                ", idRegion=" + idRegion +
                ", nombreRegion='" + nombreRegion + '\'' +
                ", precioMax='" + precioMax + '\'' +
                ", precioMin='" + precioMin + '\'' +
                ", esCopiaLocal=" + esCopiaLocal +
                ", ctdPielEncuadernar=" + ctdPielEncuadernar +
                ", ocupacion='" + ocupacion + '\'' +
                ", franjasOcupadas=" + franjasOcupadas +
                '}';
    }
}
