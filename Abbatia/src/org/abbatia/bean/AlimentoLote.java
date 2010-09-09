package org.abbatia.bean;

import java.io.Serializable;

public class AlimentoLote implements Serializable {
    private int idLote;
    private int idAlimento;
    private int idAlimentoSalado = 0;
    private int idEdificio;
    private String descripcion;
    private int idFamilia;

    public String getFechaCaducidad_hasta() {
        return fechaCaducidad_hasta;
    }

    public void setFechaCaducidad_hasta(String fechaCaducidad_hasta) {
        this.fechaCaducidad_hasta = fechaCaducidad_hasta;
    }

    private int diasVidas;
    private String familiaDescripcion;
    private String fechaEntrada;
    private String fechaCaducidad;
    private String fechaCaducidad_desde;
    private String fechaCaducidad_hasta;
    private int estado;
    private double cantidad;
    private String cantidadS;
    private int proteinas;
    private int lipidos;
    private int hidratos_carbono;
    private float consumo_monje;
    private float volumen_unidad;
    private int unidad_medida;


    public int getIdAlimentoSalado() {
        return idAlimentoSalado;
    }

    public void setIdAlimentoSalado(int idAlimentoSalado) {
        this.idAlimentoSalado = idAlimentoSalado;
    }

    public String getCantidadS() {
        return cantidadS;
    }

    public void setCantidadS(String cantidadS) {
        this.cantidadS = cantidadS;
    }

    public String getFechaEntrada() {
        return fechaEntrada;
    }

    public String getFechaCaducidad_desde() {
        return fechaCaducidad_desde;
    }

    public void setFechaCaducidad_desde(String fechaCaducidad_desde) {
        this.fechaCaducidad_desde = fechaCaducidad_desde;
    }

    public int getIdFamilia() {
        return idFamilia;
    }

    public int getEstado() {
        return estado;
    }

    public int getIdAlimento() {
        return idAlimento;
    }

    public int getIdEdificio() {
        return idEdificio;
    }

    public float getConsumo_monje() {
        return consumo_monje;
    }

    public int getIdLote() {
        return idLote;
    }

    public double getCantidad() {
        return cantidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getFamiliaDescripcion() {
        return familiaDescripcion;
    }

    public int getUnidad_medida() {
        return unidad_medida;
    }

    public int getHidratos_carbono() {
        return hidratos_carbono;
    }

    public int getLipidos() {
        return lipidos;
    }

    public String getFechaCaducidad() {
        return fechaCaducidad;
    }

    public int getProteinas() {
        return proteinas;
    }

    public int getDiasVidas() {
        return diasVidas;
    }

    public float getVolumen_unidad() {
        return volumen_unidad;
    }

    public void setFechaEntrada(String fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public void setIdFamilia(int idFamilia) {
        this.idFamilia = idFamilia;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public void setIdAlimento(int idAlimento) {
        this.idAlimento = idAlimento;
    }

    public void setIdEdificio(int idEdificio) {
        this.idEdificio = idEdificio;
    }

    public void setConsumo_monje(float consumo_monje) {
        this.consumo_monje = consumo_monje;
    }

    public void setIdLote(int idLote) {
        this.idLote = idLote;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public void setDescripcion(String Descripcion) {
        this.descripcion = Descripcion;
    }

    public void setFamiliaDescripcion(String familiaDescripcion) {
        this.familiaDescripcion = familiaDescripcion;
    }

    public void setUnidad_medida(int unidad_medida) {
        this.unidad_medida = unidad_medida;
    }

    public void setHidratos_carbono(int hidratos_carbono) {
        this.hidratos_carbono = hidratos_carbono;
    }

    public void setLipidos(int lipidos) {
        this.lipidos = lipidos;
    }

    public void setFechaCaducidad(String fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public void setProteinas(int proteinas) {
        this.proteinas = proteinas;
    }

    public void setDiasVidas(int DiasVidas) {
        this.diasVidas = DiasVidas;
    }

    public void setVolumen_unidad(float volumen_unidad) {
        this.volumen_unidad = volumen_unidad;
    }

}
