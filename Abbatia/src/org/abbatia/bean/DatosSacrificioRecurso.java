package org.abbatia.bean;

public class DatosSacrificioRecurso {
    private int recurso_id;
    private int recurso_max;
    private int recurso_min;
    private String unidad_recurso;
    private String recurso_desc;
    private int numAnimales;

    public int getNumAnimales() {
        return numAnimales;
    }

    public void setNumAnimales(int numAnimales) {
        this.numAnimales = numAnimales;
    }

    public int getRecurso_id() {
        return recurso_id;
    }

    public void setRecurso_id(int recurso_id) {
        this.recurso_id = recurso_id;
    }

    public int getRecurso_max() {
        return recurso_max;
    }

    public void setRecurso_max(int recurso_max) {
        this.recurso_max = recurso_max;
    }

    public int getRecurso_min() {
        return recurso_min;
    }

    public void setRecurso_min(int recurso_min) {
        this.recurso_min = recurso_min;
    }

    public String getUnidad_recurso() {
        return unidad_recurso;
    }

    public void setUnidad_recurso(String unidad_recurso) {
        this.unidad_recurso = unidad_recurso;
    }

    public String getRecurso_desc() {
        return recurso_desc;
    }

    public void setRecurso_desc(String recurso_desc) {
        this.recurso_desc = recurso_desc;
    }
}