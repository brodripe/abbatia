package org.abbatia.actionform;

import org.apache.struts.action.ActionForm;

import java.util.ArrayList;

public class ObispadoImpuestosActForm extends ActionForm {
    private String total;
    private int modificar;
    private int regionid;
    private int obispado;
    private int cancelacion;
    private int venta5;
    private int venta10;
    private int venta15;
    private int transito;

    public int getTransito() {
        return transito;
    }

    public void setTransito(int transito) {
        this.transito = transito;
    }

    public int getCardenales() {
        return cardenales;
    }

    public void setCardenales(int cardenales) {
        this.cardenales = cardenales;
    }

    public int getPapado() {
        return papado;
    }

    public void setPapado(int papado) {
        this.papado = papado;
    }

    private int cardenales;
    private int papado;


    private ArrayList contribuciones;

    public ArrayList getContribuciones() {
        return contribuciones;
    }

    public void setContribuciones(ArrayList contribuciones) {
        this.contribuciones = contribuciones;
    }


    public int getCancelacion() {
        if (cancelacion < 0) return 0;
        else if (cancelacion > 50) return 50;
        else
            return cancelacion;
    }

    public int getObispado() {
        if (obispado < 0) return 0;
        else if (obispado > 50) return 50;
        else
            return obispado;
    }

    public int getVenta15() {
        if (venta15 < 0) return 0;
        else if (venta15 > 50) return 50;
        else
            return venta15;
    }

    public int getVenta5() {
        if (venta5 < 0) return 0;
        else if (venta5 > 50) return 50;
        else
            return venta5;
    }

    public void setVenta10(int venta10) {
        this.venta10 = venta10;
    }

    public void setCancelacion(int cancelacion) {
        this.cancelacion = cancelacion;
    }

    public void setObispado(int obispado) {
        this.obispado = obispado;
    }

    public void setVenta15(int venta15) {
        this.venta15 = venta15;
    }

    public void setVenta5(int venta5) {
        this.venta5 = venta5;
    }

    public void setRegionid(int regionid) {
        this.regionid = regionid;
    }

    public void setModificar(int modificar) {
        this.modificar = modificar;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public int getVenta10() {
        if (venta10 < 0) return 0;
        else if (venta10 > 50) return 50;
        else
            return venta10;
    }

    public int getRegionid() {
        return regionid;
    }

    public int getModificar() {
        return modificar;
    }

    public String getTotal() {
        return total;
    }

}
