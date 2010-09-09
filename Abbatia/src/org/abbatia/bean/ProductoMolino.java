package org.abbatia.bean;

/**
 * Created by IntelliJ IDEA.
 * User: benjamin.rodriguez
 * Date: 05-nov-2004
 * Time: 20:23:54
 * To change this template use File | Settings | File Templates.
 */
public class ProductoMolino {
    private int abadiaid;
    private int productoid_entrada;
    private String productoid_desc;
    private int recursoid_salida;
    private String recursoid_desc;
    private int cantidad_ini;
    private int cantidad_act;
    private String fecha_inicio;
    private String barra_HTML;

    public String getBarra_HTML() {
        return barra_HTML;
    }

    public void setBarra_HTML(String barra_HTML) {
        this.barra_HTML = barra_HTML;
    }


    public int getCantidad_act() {
        return cantidad_act;
    }

    public void setCantidad_act(int cantidad_act) {
        this.cantidad_act = cantidad_act;
    }

    public String getProductoid_desc() {
        return productoid_desc;
    }

    public void setProductoid_desc(String productoid_desc) {
        this.productoid_desc = productoid_desc;
    }

    public String getRecursoid_desc() {
        return recursoid_desc;
    }

    public void setRecursoid_desc(String recursoid_desc) {
        this.recursoid_desc = recursoid_desc;
    }

    public int getAbadiaid() {
        return abadiaid;
    }

    public void setAbadiaid(int abadiaid) {
        this.abadiaid = abadiaid;
    }

    public int getProductoid_entrada() {
        return productoid_entrada;
    }

    public void setProductoid_entrada(int productoid_entrada) {
        this.productoid_entrada = productoid_entrada;
    }

    public int getRecursoid_salida() {
        return recursoid_salida;
    }

    public void setRecursoid_salida(int recursoid_salida) {
        this.recursoid_salida = recursoid_salida;
    }

    public int getCantidad_ini() {
        return cantidad_ini;
    }

    public void setCantidad_ini(int cantidad_ini) {
        this.cantidad_ini = cantidad_ini;
    }


    public String getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(String fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }
}
