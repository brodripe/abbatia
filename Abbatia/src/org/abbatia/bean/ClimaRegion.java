package org.abbatia.bean;

/**
 * Created by IntelliJ IDEA.
 * User: Benjamin
 * Date: 05-may-2006
 * Time: 23:26:52
 * To change this template use File | Settings | File Templates.
 */
public class ClimaRegion {
    private int idRegion;
    private int temperatura;
    private int clima;
    private int nieve;
    private int temperaturaMinima;
    private int temperaturaMaxima;
    private int tiempoMaximo;
    private int tiempoMinimo;


    public ClimaRegion() {
    }

    public ClimaRegion(int idRegion, int temperaturaMinima, int temperaturaMaxima, int tiempoMaximo, int tiempoMinimo) {
        this.idRegion = idRegion;
        this.temperaturaMinima = temperaturaMinima;
        this.temperaturaMaxima = temperaturaMaxima;
        this.tiempoMaximo = tiempoMaximo;
        this.tiempoMinimo = tiempoMinimo;
    }

    public int getTemperaturaMinima() {
        return temperaturaMinima;
    }

    public void setTemperaturaMinima(int temperaturaMinima) {
        this.temperaturaMinima = temperaturaMinima;
    }

    public int getTemperaturaMaxima() {
        return temperaturaMaxima;
    }

    public void setTemperaturaMaxima(int temperaturaMaxima) {
        this.temperaturaMaxima = temperaturaMaxima;
    }

    public int getTiempoMaximo() {
        return tiempoMaximo;
    }

    public void setTiempoMaximo(int tiempoMaximo) {
        this.tiempoMaximo = tiempoMaximo;
    }

    public int getTiempoMinimo() {
        return tiempoMinimo;
    }

    public void setTiempoMinimo(int tiempoMinimo) {
        this.tiempoMinimo = tiempoMinimo;
    }

    public int getIdRegion() {
        return idRegion;
    }

    public void setIdRegion(int idRegion) {
        this.idRegion = idRegion;
    }

    public int getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(int temperatura) {
        this.temperatura = temperatura;
    }

    public int getClima() {
        return clima;
    }

    public void setClima(int clima) {
        this.clima = clima;
    }

    public int getNieve() {
        return nieve;
    }

    public void setNieve(int nieve) {
        this.nieve = nieve;
    }
}
