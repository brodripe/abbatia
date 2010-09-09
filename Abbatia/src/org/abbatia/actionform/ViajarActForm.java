package org.abbatia.actionform;

import org.apache.struts.action.ActionForm;

public class ViajarActForm extends ActionForm {
    private String accion;
    private int abadiaid_origen;
    private int abadiaid_destino;
    private String abadia_origen;
    private String abadia_destino;
    private String region_origen;
    private String region_destino;
    private double total;
    private double total_caballo;
    private String caballo;
    private int tiempo;
    private int dias_pie;
    private int dias_caballo;
    private int reliquiaid;
    private String dias_camino_pie;
    private String dias_camino_caballo;
    private String dias_montanya_pie;
    private String dias_montanya_caballo;
    private String dias_barco;
    private short tipoViaje;
    private int monjeid;
    private double coste;
    private int idLibro;
    private String nombreLibro;
    private String precioCopia;
    private String nombreMonje;


    public short getTipoViaje() {
        return tipoViaje;
    }

    public void setTipoViaje(short tipoViaje) {
        this.tipoViaje = tipoViaje;
    }

    public String getNombreMonje() {
        return nombreMonje;
    }

    public void setNombreMonje(String nombreMonje) {
        this.nombreMonje = nombreMonje;
    }

    public String getPrecioCopia() {
        return precioCopia;
    }

    public void setPrecioCopia(String precioCopia) {
        this.precioCopia = precioCopia;
    }

    public int getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public String getNombreLibro() {
        return nombreLibro;
    }

    public void setNombreLibro(String nombreLibro) {
        this.nombreLibro = nombreLibro;
    }

    public double getCoste() {
        return coste;
    }

    public void setCoste(double coste) {
        this.coste = coste;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getAbadiaid_destino() {
        return abadiaid_destino;
    }

    public void setAbadiaid_destino(int abadiaid_destino) {
        this.abadiaid_destino = abadiaid_destino;
    }

    public int getAbadiaid_origen() {
        return abadiaid_origen;
    }

    public void setAbadiaid_origen(int abadiaid_origen) {
        this.abadiaid_origen = abadiaid_origen;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public void setMonjeid(int monjeid) {
        this.monjeid = monjeid;
    }

    public int getMonjeid() {
        return monjeid;
    }

    public void setAbadia_origen(String abadia_origen) {
        this.abadia_origen = abadia_origen;
    }

    public String getAbadia_origen() {
        return abadia_origen;
    }

    public void setAbadia_destino(String abadia_destino) {
        this.abadia_destino = abadia_destino;
    }

    public String getAbadia_destino() {
        return abadia_destino;
    }

    public void setRegion_origen(String region_origen) {
        this.region_origen = region_origen;
    }

    public String getRegion_origen() {
        return region_origen;
    }

    public void setRegion_destino(String region_destino) {
        this.region_destino = region_destino;
    }

    public String getRegion_destino() {
        return region_destino;
    }

    public double getTotal_caballo() {
        return total_caballo;
    }

    public void setTotal_caballo(double total_caballo) {
        this.total_caballo = total_caballo;
    }

    public void setDias_camino_pie(String dias_camino_pie) {
        this.dias_camino_pie = dias_camino_pie;
    }

    public String getDias_camino_pie() {
        return dias_camino_pie;
    }

    public void setDias_camino_caballo(String dias_camino_caballo) {
        this.dias_camino_caballo = dias_camino_caballo;
    }

    public String getDias_camino_caballo() {
        return dias_camino_caballo;
    }

    public void setDias_montanya_pie(String dias_montanya_pie) {
        this.dias_montanya_pie = dias_montanya_pie;
    }

    public String getDias_montanya_pie() {
        return dias_montanya_pie;
    }

    public void setDias_montanya_caballo(String dias_montanya_caballo) {
        this.dias_montanya_caballo = dias_montanya_caballo;
    }

    public String getDias_montanya_caballo() {
        return dias_montanya_caballo;
    }

    public void setDias_barco(String dias_barco) {
        this.dias_barco = dias_barco;
    }

    public String getDias_barco() {
        return dias_barco;
    }

    public void setDias_pie(int dias_pie) {
        this.dias_pie = dias_pie;
    }

    public int getDias_pie() {
        return dias_pie;
    }

    public void setDias_caballo(int dias_caballo) {
        this.dias_caballo = dias_caballo;
    }

    public int getDias_caballo() {
        return dias_caballo;
    }

    public void setCaballo(String caballo) {
        this.caballo = caballo;
    }

    public String getCaballo() {
        return caballo;
    }

    public int getTiempo() {
        return tiempo;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    public int getReliquiaid() {
        return reliquiaid;
    }

    public void setReliquiaid(int reliquiaid) {
        this.reliquiaid = reliquiaid;
    }
}
