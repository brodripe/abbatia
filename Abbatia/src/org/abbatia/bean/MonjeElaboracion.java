package org.abbatia.bean;

/**
 * Created by IntelliJ IDEA.
 * User: Benjamin
 * Date: 30-sep-2005
 * Time: 10:49:48
 * To change this template use File | Settings | File Templates.
 */
public class MonjeElaboracion extends MonjeBase {
    private int nivel;
    private int idEdificio;


    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }


    public int getIdEdificio() {
        return idEdificio;
    }

    public void setIdEdificio(int idEdificio) {
        this.idEdificio = idEdificio;
    }
}
