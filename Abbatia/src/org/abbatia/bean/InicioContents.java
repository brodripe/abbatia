package org.abbatia.bean;

import java.io.Serializable;
import java.util.ArrayList;

/*
     Página de Incio del Juego - Bean
         John Lohmeyer
*/

public class InicioContents implements Serializable {
    private ArrayList ultMensajes;    // Ultimos mensajes en abbatia para el usuario
    private ArrayList ultMonjes;      // Los monjes que tienes
    private ArrayList ultMonjesVisita;
    private ArrayList ultMonjesViaje;
    private ArrayList ultMonjesInvitados;
    private ArrayList edificios;
    private String nomRegion;
    private String tempRegion;
    private String climaRegion;
    // Contadores
    private String contadorAldeanos;
    private String contadorMonjes;
    private String contadorGuardia;
    private int correos;
    private int solicitudes;
    // Recursos
    private String recursoMonedas;
    private String recursoAgua;
    private String recursoPiedra;
    private String recursoMadera;
    private String recursoHierro;
    // Clasificacion
    private String clasificacion;
    private double puntos;
    private String puedeContratar;

    public String getPuedeContratar() {
        return puedeContratar;
    }

    public void setPuedeContratar(String puedeContratar) {
        this.puedeContratar = puedeContratar;
    }

    public double getPuntos() {
        return puntos;
    }

    public void setPuntos(double puntos) {
        this.puntos = puntos;
    }

    public ArrayList getUltMonjesVisita() {
        return ultMonjesVisita;
    }

    public void setUltMonjesVisita(ArrayList ultMonjesVisita) {
        this.ultMonjesVisita = ultMonjesVisita;
    }

    public ArrayList getUltMonjesViaje() {
        return ultMonjesViaje;
    }

    public void setUltMonjesViaje(ArrayList ultMonjesViaje) {
        this.ultMonjesViaje = ultMonjesViaje;
    }

    public ArrayList getUltMonjesInvitados() {
        return ultMonjesInvitados;
    }

    public void setUltMonjesInvitados(ArrayList ultMonjesInvitados) {
        this.ultMonjesInvitados = ultMonjesInvitados;
    }

    public int getSolicitudes() {
        return solicitudes;
    }

    public void setSolicitudes(int solicitudes) {
        this.solicitudes = solicitudes;
    }

    public InicioContents() {
    }

    public ArrayList getUltMensajes() {
        return ultMensajes;
    }

    public ArrayList getUltMonjes() {
        return ultMonjes;
    }

    public String getTempRegion() {
        return tempRegion;
    }

    public String getClimaRegion() {
        return climaRegion;
    }

    public String getNomRegion() {
        return nomRegion;
    }

    public void setUltMensajes(ArrayList ultMensajes) {
        this.ultMensajes = ultMensajes;
    }

    public void setUltMonjes(ArrayList ultMonjes) {
        this.ultMonjes = ultMonjes;
    }

    public void setTempRegion(String TempRegion) {
        this.tempRegion = TempRegion;
    }

    public void setClimaRegion(String ClimaRegion) {
        this.climaRegion = ClimaRegion;
    }

    public void setNomRegion(String nomRegion) {
        this.nomRegion = nomRegion;
    }


    public void setEdificios(ArrayList edificios) {
        this.edificios = edificios;
    }

    public void setContadorGuardia(String contadorGuardia) {
        this.contadorGuardia = contadorGuardia;
    }

    public void setContadorMonjes(String contadorMonjes) {
        this.contadorMonjes = contadorMonjes;
    }

    public void setContadorAldeanos(String contadorAldeanos) {
        this.contadorAldeanos = contadorAldeanos;
    }

    public void setRecursoMadera(String recursoMadera) {
        this.recursoMadera = recursoMadera;
    }

    public void setRecursoAgua(String recursoAgua) {
        this.recursoAgua = recursoAgua;
    }

    public void setRecursoPiedra(String recursoPiedra) {
        this.recursoPiedra = recursoPiedra;
    }

    public void setRecursoMonedas(String recursoMonedas) {
        this.recursoMonedas = recursoMonedas;
    }

    public void setRecursoHierro(String recursoHierro) {
        this.recursoHierro = recursoHierro;
    }

    public ArrayList getEdificios() {
        return edificios;
    }

    public String getContadorGuardia() {
        return contadorGuardia;
    }

    public String getContadorMonjes() {
        return contadorMonjes;
    }

    public String getContadorAldeanos() {
        return contadorAldeanos;
    }

    public String getRecursoMadera() {
        return recursoMadera;
    }

    public String getRecursoAgua() {
        return recursoAgua;
    }

    public String getRecursoPiedra() {
        return recursoPiedra;
    }

    public String getRecursoMonedas() {
        return recursoMonedas;
    }

    public String getRecursoHierro() {
        return recursoHierro;
    }


    public void setCorreos(int correos) {
        this.correos = correos;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }

    public int getCorreos() {
        return correos;
    }

    public String getClasificacion() {
        return clasificacion;
    }

}
