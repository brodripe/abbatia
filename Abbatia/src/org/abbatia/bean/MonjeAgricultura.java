package org.abbatia.bean;

/**
 * Created by IntelliJ IDEA.
 * User: Benjamin
 * Date: 09-abr-2006
 * Time: 1:58:47
 * To change this template use File | Settings | File Templates.
 */
public class MonjeAgricultura extends MonjeBase {
    private int idCampo;
    private double fe;
    private double fuerza;
    private double destreza;
    public boolean haTrabajado = false;

    public boolean isHaTrabajado() {
        return haTrabajado;
    }

    public void setHaTrabajado(boolean haTrabajado) {
        this.haTrabajado = haTrabajado;
    }

    public int getIdCampo() {
        return idCampo;
    }

    public void setIdCampo(int idCampo) {
        this.idCampo = idCampo;
    }

    public double getFe() {
        return fe;
    }

    public void setFe(double fe) {
        this.fe = fe;
    }

    public double getFuerza() {
        return fuerza;
    }

    public void setFuerza(double fuerza) {
        this.fuerza = fuerza;
    }

    public double getDestreza() {
        return destreza;
    }

    public void setDestreza(double destreza) {
        this.destreza = destreza;
    }
}
