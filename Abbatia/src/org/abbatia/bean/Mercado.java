package org.abbatia.bean;

import java.io.Serializable;

public class Mercado implements Serializable {
    private int idProducto;
    private int idAbadia;
    private int estado;  // Estado del producto
    private String mercancia; // Tipo de mercancia al que pertenece el producto
    private int tipo; // Tipo de compra (directa o subasta)
    private String tipo_desc; // Descripción del tipo de compra (directa o subasta)
    private String fecha_inicial;
    private String fecha_final;
    private int ctd_inicial;
    private int ctd_actual;
    private double precio_inicial;  // Precio inicial del producto
    private double precio_actual; // Precio actual del producto
    private String precio;
    private String precioTotal;
    private double precioTotalD;
    private String cantidad;

    private String unidad_desc; // Descripción de la unidad del producto

    private String familia;  // Familia del producto
    private String descripcion; // Descripción del producto
    private String volumen;
    private double volumenD;

    // Según el tipo de mercancia
    private int idAlimento;          // Mercancia tipo "A"
    private int idAnimal;            // Mercancia tipo "N"
    private int idAnimalTipo;        // Mercancia tipo "N"
    private int idAnimalNivel;       // Mercancia tipo "N"
    private int animalSalud;         // Mercancia tipo "N"
    private String fecha_nacimiento; // Mercancia tipo "N"
    private int idLibro;             // Mercancia tipo "L"
    private int idRecurso;           // Mercancia tipo "R"
    private int idReliquia;          // Mercancia tipo "O"

    private String fecha_caduca;     // La alimentación cuando caduca
    private String barra_HTML;       // Barra gráfica para mostrar el estado

    private String region;   // Información de la región origen
    private String abadia;   // Información de la abadía origen

    public double getPrecioTotalD() {
        return precioTotalD;
    }

    public void setPrecioTotalD(double precioTotalD) {
        this.precioTotalD = precioTotalD;
    }

    public double getVolumenD() {
        return volumenD;
    }

    public void setVolumenD(double volumenD) {
        this.volumenD = volumenD;
    }

    public String getVolumen() {
        return volumen;
    }

    public void setVolumen(String volumen) {
        this.volumen = volumen;
    }

    public String getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(String precioTotal) {
        this.precioTotal = precioTotal;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public int getAnimalSalud() {
        return animalSalud;
    }

    public void setAnimalSalud(int animalSalud) {
        this.animalSalud = animalSalud;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public int getTipo() {
        return tipo;
    }

    public int getCtd_actual() {
        return ctd_actual;
    }

    public String getFecha_inicial() {
        return fecha_inicial;
    }

    public double getPrecio_inicial() {
        return precio_inicial;
    }

    public int getIdAbadia() {
        return idAbadia;
    }

    public int getEstado() {
        return estado;
    }

    public String getMercancia() {
        return mercancia;
    }

    public double getPrecio_actual() {
        return precio_actual;
    }

    public int getCtd_inicial() {
        return ctd_inicial;
    }

    public void setFecha_final(String fecha_final) {
        this.fecha_final = fecha_final;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public void setCtd_actual(int ctd_actual) {
        this.ctd_actual = ctd_actual;
    }

    public void setFecha_inicial(String fecha_inicial) {
        this.fecha_inicial = fecha_inicial;
    }

    public void setPrecio_inicial(double precio_inicial) {
        this.precio_inicial = precio_inicial;
    }

    public void setIdAbadia(int idAbadia) {
        this.idAbadia = idAbadia;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public void setMercancia(String mercancia) {
        this.mercancia = mercancia;
    }

    public void setPrecio_actual(double precio_actual) {
        this.precio_actual = precio_actual;
    }

    public void setCtd_inicial(int ctd_inicial) {
        this.ctd_inicial = ctd_inicial;
    }

    public void setIdAlimento(int idAlimento) {
        this.idAlimento = idAlimento;
    }

    public void setIdRecurso(int idRecurso) {
        this.idRecurso = idRecurso;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public void setIdReliquia(int idReliquia) {
        this.idReliquia = idReliquia;
    }

    public String getFecha_final() {
        return fecha_final;
    }

    public int getIdAlimento() {
        return idAlimento;
    }

    public int getIdRecurso() {
        return idRecurso;
    }

    public int getIdLibro() {
        return idLibro;
    }

    public int getIdReliquia() {
        return idReliquia;
    }

    public Mercado() {
    }


    public void setIdAnimalTipo(int idAnimalTipo) {
        this.idAnimalTipo = idAnimalTipo;
    }


    public int getIdAnimalTipo() {
        return idAnimalTipo;
    }


    public void setIdAnimalNivel(int idAnimalNivel) {
        this.idAnimalNivel = idAnimalNivel;
    }


    public int getIdAnimalNivel() {
        return idAnimalNivel;
    }


    public void setIdAnimal(int idAnimal) {
        this.idAnimal = idAnimal;
    }


    public int getIdAnimal() {
        return idAnimal;
    }


    public void setFecha_nacimiento(String fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public void setFecha_caduca(String fecha_caduca) {
        this.fecha_caduca = fecha_caduca;
    }

    public String getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public String getFecha_caduca() {
        return fecha_caduca;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegion() {
        return region;
    }

    public void setAbadia(String abadia) {
        this.abadia = abadia;
    }

    public String getAbadia() {
        return abadia;
    }

    public void setFamilia(String familia) {
        this.familia = familia;
    }

    public String getFamilia() {
        return familia;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setUnidad_desc(String unidad_desc) {
        this.unidad_desc = unidad_desc;
    }

    public String getUnidad_desc() {
        return unidad_desc;
    }

    public String getTipo_desc() {
        return tipo_desc;
    }

    public void setTipo_desc(String tipo_desc) {
        this.tipo_desc = tipo_desc;
    }

    public void setBarra_HTML(String barra_HTML) {
        this.barra_HTML = barra_HTML;
    }

    public String getBarra_HTML() {
        return barra_HTML;
    }
}
