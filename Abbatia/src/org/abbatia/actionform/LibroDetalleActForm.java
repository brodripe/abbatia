package org.abbatia.actionform;

import org.apache.struts.action.ActionForm;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Benjamin
 * Date: 05-may-2005
 * Time: 23:37:01
 * To change this template use File | Settings | File Templates.
 */
public class LibroDetalleActForm extends ActionForm {
    private String accion = "inicio";
    private int idLibro;
    private int idLibro_origen;
    private int idLibroTipo;
    private int idEdificio;
    private int Abadiaid;
    private int idMonje;
    private int idIdioma;
    private String idioma_desc;
    private String abadiaDesc_copia;
    private String nombreLibro;
    private String descLibro;
    private String descEstado;
    private short nivel;
    private String fecha_adquisicion;
    private String fecha_creacion;
    private int numPaginas;
    private double desgaste;
    private String deterioro;
    private String grafico;
    private ArrayList detalles;
    private double precioCopia;
    private String precioCopiaS;
    private String precioMax;
    private String precioMin;


    public double getDesgaste() {
        return desgaste;
    }

    public void setDesgaste(double desgaste) {
        this.desgaste = desgaste;
    }

    public String getDeterioro() {
        return deterioro;
    }

    public void setDeterioro(String deterioro) {
        this.deterioro = deterioro;
    }

    public String getPrecioCopiaS() {
        return precioCopiaS;
    }

    public void setPrecioCopiaS(String precioCopiaS) {
        this.precioCopiaS = precioCopiaS;
    }


    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public double getPrecioCopia() {
        return precioCopia;
    }

    public void setPrecioCopia(double precioCopia) {
        this.precioCopia = precioCopia;
    }

    public String getPrecioMax() {
        return precioMax;
    }

    public void setPrecioMax(String precioMax) {
        this.precioMax = precioMax;
    }

    public String getPrecioMin() {
        return precioMin;
    }

    public void setPrecioMin(String precioMin) {
        this.precioMin = precioMin;
    }

    public String getGrafico() {
        return grafico;
    }

    public void setGrafico(String grafico) {
        this.grafico = grafico;
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

    public int getAbadiaid() {
        return Abadiaid;
    }

    public void setAbadiaid(int abadiaid) {
        Abadiaid = abadiaid;
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

    public String getAbadiaDesc_copia() {
        return abadiaDesc_copia;
    }

    public void setAbadiaDesc_copia(String abadiaDesc_copia) {
        this.abadiaDesc_copia = abadiaDesc_copia;
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

    public ArrayList getDetalles() {
        return detalles;
    }

    public void setDetalles(ArrayList detalles) {
        this.detalles = detalles;
    }

}
