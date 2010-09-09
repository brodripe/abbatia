package org.abbatia.bean;

/**
 * Created by IntelliJ IDEA.
 * User: Benjamin
 * Date: 15-mar-2006
 * Time: 0:15:28
 * To change this template use File | Settings | File Templates.
 */
public class MonjeVisita extends MonjeBase {
    private String nombreAbadia;
    private String nombreRegion;

    public String getNombreAbadia() {
        return nombreAbadia;
    }

    public void setNombreAbadia(String nombreAbadia) {
        this.nombreAbadia = nombreAbadia;
    }

    public String getNombreRegion() {
        return nombreRegion;
    }

    public void setNombreRegion(String nombreRegion) {
        this.nombreRegion = nombreRegion;
    }

}
