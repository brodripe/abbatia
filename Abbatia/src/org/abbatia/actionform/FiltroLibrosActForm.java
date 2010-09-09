package org.abbatia.actionform;

import org.apache.struts.action.ActionForm;

public class FiltroLibrosActForm extends ActionForm {
    private int abadia = 0;
    private int region = 0;
    private int idioma = 0;
    private int libro = 0;
    private boolean disponible;

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public int getIdioma() {
        return idioma;
    }

    public void setIdioma(int idioma) {
        this.idioma = idioma;
    }

    public int getAbadia() {
        return abadia;
    }

    public void setAbadia(int abadia) {
        this.abadia = abadia;
    }

    public int getRegion() {
        return region;
    }

    public void setRegion(int region) {
        this.region = region;
    }

    public int getLibro() {
        return libro;
    }

    public void setLibro(int libro) {
        this.libro = libro;
    }


}
