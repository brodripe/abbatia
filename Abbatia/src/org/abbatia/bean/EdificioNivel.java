package org.abbatia.bean;

/**
 * Created by IntelliJ IDEA.
 * User: Benjamin
 * Date: 16-sep-2005
 * Time: 16:26:33
 * To change this template use File | Settings | File Templates.
 */
public class EdificioNivel {
    private int abadiaId;
    private int nivel;
    private int tipoEdificioId;

    public EdificioNivel(int abadiaId, int nivel, int tipoEdificioId) {
        this.abadiaId = abadiaId;
        this.nivel = nivel;
        this.tipoEdificioId = tipoEdificioId;
    }

    public int getAbadiaId() {
        return abadiaId;
    }

    public void setAbadiaId(int abadiaId) {
        this.abadiaId = abadiaId;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public int getTipoEdificioId() {
        return tipoEdificioId;
    }

    public void setTipoEdificioId(int tipoEdificioId) {
        this.tipoEdificioId = tipoEdificioId;
    }
}