package org.abbatia.bean;

public class LibroCopia {
    private int idLibro;
    private int idLibro_origen;
    private int idAbadia;
    private int idAbadiaCopia;
    private int idMonje;
    private String idioma_desc;
    private String abadia_Desc;
    private String nombreMonje;
    private String apellidoMonje;
    private short estado;
    private String descEstado;
    private short nivel;
    private int numPaginas;
    private double numPaginasCopiadas;
    private String progreso;
    private int idPeriodo;
    private String periodo_Desc;
    private String claveLibroPeriodo;
    public int eliminable = 0;
    public String deterioro;
    public double desgaste;


    public String getDeterioro() {
        return deterioro;
    }

    public void setDeterioro(String deterioro) {
        this.deterioro = deterioro;
    }

    public double getDesgaste() {
        return desgaste;
    }

    public void setDesgaste(double desgaste) {
        this.desgaste = desgaste;
    }

    public int getEliminable() {
        return eliminable;
    }

    public void setEliminable(int eliminable) {
        this.eliminable = eliminable;
    }


    public int getIdAbadiaCopia() {
        return idAbadiaCopia;
    }

    public void setIdAbadiaCopia(int idAbadiaCopia) {
        this.idAbadiaCopia = idAbadiaCopia;
    }

    public String getClaveLibroPeriodo() {
        return claveLibroPeriodo;
    }

    public void setClaveLibroPeriodo(String claveLibroPeriodo) {
        this.claveLibroPeriodo = claveLibroPeriodo;
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

    public String getIdioma_desc() {
        return idioma_desc;
    }

    public void setIdioma_desc(String idioma_desc) {
        this.idioma_desc = idioma_desc;
    }

    public String getAbadia_Desc() {
        return abadia_Desc;
    }

    public void setAbadia_Desc(String abadia_Desc) {
        this.abadia_Desc = abadia_Desc;
    }

    public String getNombreMonje() {
        return nombreMonje;
    }

    public void setNombreMonje(String nombreMonje) {
        this.nombreMonje = nombreMonje;
    }

    public String getApellidoMonje() {
        return apellidoMonje;
    }

    public void setApellidoMonje(String apellidoMonje) {
        this.apellidoMonje = apellidoMonje;
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

    public String getProgreso() {
        return progreso;
    }

    public void setProgreso(String progreso) {
        this.progreso = progreso;
    }

    public int getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(int idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public String getPeriodo_Desc() {
        return periodo_Desc;
    }

    public void setPeriodo_Desc(String periodo_Desc) {
        this.periodo_Desc = periodo_Desc;
    }
}

