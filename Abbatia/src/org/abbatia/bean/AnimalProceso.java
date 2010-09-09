package org.abbatia.bean;

/**
 * Created by IntelliJ IDEA.
 * User: Benjamin
 * Date: 25-sep-2004
 * Time: 16:06:13
 * To change this template use File | Settings | File Templates.
 */
public class AnimalProceso {
    private int idAbadia;
    private int idAnimal;
    private int idAlimento;
    private int idRecurso;
    private int tipoAnimalIdMasc;
    private int tipoAnimalIdFem;
    private String descripcion;
    private int idIdioma;
    private int salud;
    private int min;
    private int max;
    private int criasMax;
    private int almacenamiento;
    private int idEdificio;
    private double consumo;
    private int consumoFamilia;
    private int estresado;
    private String fecha_parir;
    private int recoge_monje;


    public int getRecoge_monje() {
        return recoge_monje;
    }

    public void setRecoge_monje(int recoge_monje) {
        this.recoge_monje = recoge_monje;
    }

    public int getIdAlimento() {
        return idAlimento;
    }

    public void setIdAlimento(int idAlimento) {
        this.idAlimento = idAlimento;
    }

    public double getConsumo() {
        return consumo;
    }

    public void setConsumo(double consumo) {
        this.consumo = consumo;
    }

    public int getConsumoFamilia() {
        return consumoFamilia;
    }

    public void setConsumoFamilia(int consumoFamilia) {
        this.consumoFamilia = consumoFamilia;
    }

    public int getIdIdioma() {
        return idIdioma;
    }

    public void setIdIdioma(int idIdioma) {
        this.idIdioma = idIdioma;
    }

    public int getIdEdificio() {
        return idEdificio;
    }

    public void setIdEdificio(int idEdificio) {
        this.idEdificio = idEdificio;
    }

    public int getTipoAnimalIdFem() {
        return tipoAnimalIdFem;
    }

    public void setTipoAnimalIdFem(int tipoAnimalIdFem) {
        this.tipoAnimalIdFem = tipoAnimalIdFem;
    }

    public int getCriasMax() {
        return criasMax;
    }

    public void setCriasMax(int criasMax) {
        this.criasMax = criasMax;
    }

    public int getIdAbadia() {
        return idAbadia;
    }

    public void setIdAbadia(int idAbadia) {
        this.idAbadia = idAbadia;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getSalud() {
        return salud;
    }

    public void setSalud(int salud) {
        if (salud > 100) salud = 100;
        else if (salud < 0) salud = 0;
        else
            this.salud = salud;
    }

    public int getIdAnimal() {
        return idAnimal;
    }

    public void setIdAnimal(int idAnimal) {
        this.idAnimal = idAnimal;
    }

    public int getTipoAnimalIdMasc() {
        return tipoAnimalIdMasc;
    }

    public void setTipoAnimalIdMasc(int tipoAnimalIdMasc) {
        this.tipoAnimalIdMasc = tipoAnimalIdMasc;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getAlmacenamiento() {
        return almacenamiento;
    }

    public String getFecha_parir() {
        return fecha_parir;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setAlmacenamiento(int almacenamiento) {
        this.almacenamiento = almacenamiento;
    }

    public void setFecha_parir(String fecha_parir) {
        this.fecha_parir = fecha_parir;
    }

    public int getEstresado() {
        return estresado;
    }

    public void setEstresado(int estresado) {
        this.estresado = estresado;
    }

    public int getIdRecurso() {
        return idRecurso;
    }

    public void setIdRecurso(int idRecurso) {
        this.idRecurso = idRecurso;
    }
}
