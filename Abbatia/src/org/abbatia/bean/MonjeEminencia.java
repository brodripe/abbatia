package org.abbatia.bean;

/**
 * Created by IntelliJ IDEA.
 * User: Benjamin
 * Date: 15-mar-2006
 * Time: 0:15:28
 * To change this template use File | Settings | File Templates.
 */
public class MonjeEminencia extends MonjeBase {
    private String Abadia;
    private String Region;
    private String jerarquia;

    public String getAbadia() {
        return Abadia;
    }

    public void setAbadia(String nombreAbadia) {
        this.Abadia = nombreAbadia;
    }

    public String getRegion() {
        return Region;
    }

    public void setRegion(String nombreRegion) {
        this.Region = nombreRegion;
    }

    public String getJerarquia() {
        return jerarquia;
    }

    public void setJerarquia(String jerarquia) {
        this.jerarquia = jerarquia;
    }
}