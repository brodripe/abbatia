package org.abbatia.actionform;

import org.apache.struts.action.ActionForm;

/**
 * Created by IntelliJ IDEA.
 * User: Benjamin
 * Date: 06-feb-2005
 * Time: 11:58:36
 * To change this template use File | Settings | File Templates.
 */
public class MercadoAdminForm extends ActionForm {
    private String accion = "inicio";
    private int productoId;
    private String mercancia;
    private String descripcion;
    private String precio;
    private String precioMaximo;
    private String precioMaximoC;
    private String precioMinimo;
    private String precioMinimoC;
    private int numeroVentas;
    private String familia;
    private int nivel;
    private int cantidad;
    private String ventasHoy;
    private String ventasAyer;
    private int alimentoId;
    private int animalId;
    private int animalNivel;
    private int animalTipo;
    private String animalFechaNacimiento;
    private int reliquiaId;
    private int libroId;
    private int recursoId;
    private String claveAlimento;
    private String claveRecurso;
    private String claveAnimal;
    private String claveReliquia;
    private String claveLibro;

    public String getClaveAlimento() {
        return claveAlimento;
    }

    public void setClaveAlimento(String claveAlimento) {
        this.claveAlimento = claveAlimento;
    }

    public String getClaveRecurso() {
        return claveRecurso;
    }

    public void setClaveRecurso(String claveRecurso) {
        this.claveRecurso = claveRecurso;
    }

    public String getClaveAnimal() {
        return claveAnimal;
    }

    public void setClaveAnimal(String claveAnimal) {
        this.claveAnimal = claveAnimal;
    }

    public String getClaveReliquia() {
        return claveReliquia;
    }

    public void setClaveReliquia(String claveReliquia) {
        this.claveReliquia = claveReliquia;
    }

    public String getClaveLibro() {
        return claveLibro;
    }

    public void setClaveLibro(String claveLibro) {
        this.claveLibro = claveLibro;
    }

    public int getRecursoId() {
        return recursoId;
    }

    public void setRecursoId(int recursoId) {
        this.recursoId = recursoId;
    }

    public int getAlimentoId() {
        return alimentoId;
    }

    public void setAlimentoId(int alimentoId) {
        this.alimentoId = alimentoId;
    }

    public int getAnimalId() {
        return animalId;
    }

    public void setAnimalId(int animalId) {
        this.animalId = animalId;
    }

    public int getAnimalNivel() {
        return animalNivel;
    }

    public void setAnimalNivel(int animalNivel) {
        this.animalNivel = animalNivel;
    }

    public int getAnimalTipo() {
        return animalTipo;
    }

    public void setAnimalTipo(int animalTipo) {
        this.animalTipo = animalTipo;
    }

    public String getAnimalFechaNacimiento() {
        return animalFechaNacimiento;
    }

    public void setAnimalFechaNacimiento(String animalFechaNacimiento) {
        this.animalFechaNacimiento = animalFechaNacimiento;
    }

    public int getReliquiaId() {
        return reliquiaId;
    }

    public void setReliquiaId(int reliquiaId) {
        this.reliquiaId = reliquiaId;
    }

    public int getLibroId() {
        return libroId;
    }

    public void setLibroId(int libroId) {
        this.libroId = libroId;
    }


    public String getVentasHoy() {
        return ventasHoy;
    }

    public void setVentasHoy(String ventasHoy) {
        this.ventasHoy = ventasHoy;
    }

    public String getVentasAyer() {
        return ventasAyer;
    }

    public void setVentasAyer(String ventasAyer) {
        this.ventasAyer = ventasAyer;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }


    public String getFamilia() {
        return familia;
    }

    public void setFamilia(String familia) {
        this.familia = familia;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public int getProductoId() {
        return productoId;
    }

    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }

    public String getMercancia() {
        return mercancia;
    }

    public void setMercancia(String mercancia) {
        this.mercancia = mercancia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


    public int getNumeroVentas() {
        return numeroVentas;
    }

    public void setNumeroVentas(int numeroVentas) {
        this.numeroVentas = numeroVentas;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getPrecioMaximo() {
        return precioMaximo;
    }

    public void setPrecioMaximo(String precioMaximo) {
        this.precioMaximo = precioMaximo;
    }

    public String getPrecioMaximoC() {
        return precioMaximoC;
    }

    public void setPrecioMaximoC(String precioMaximoC) {
        this.precioMaximoC = precioMaximoC;
    }

    public String getPrecioMinimo() {
        return precioMinimo;
    }

    public void setPrecioMinimo(String precioMinimo) {
        this.precioMinimo = precioMinimo;
    }

    public String getPrecioMinimoC() {
        return precioMinimoC;
    }

    public void setPrecioMinimoC(String precioMinimoC) {
        this.precioMinimoC = precioMinimoC;
    }
}

