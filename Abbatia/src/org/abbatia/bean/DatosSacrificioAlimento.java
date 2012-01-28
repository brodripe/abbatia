package org.abbatia.bean;

public class DatosSacrificioAlimento {
    private int animal_tipo;
    private int animal_nivel;
    private int alimento_id;
    private int alimento_max;
    private int alimento_min;
    private int numAnimales;
    private int cantidad;
    private String animal_fechanacimiento;
    private String alimento_desc;
    private String unidad_alimento;
    private String animal_desc;
    private String precio = "1";
    private double precioMercado;
    private String id;

    public String getUnidad_alimento() {
        return unidad_alimento;
    }

    public void setUnidad_alimento(String unidad_alimento) {
        this.unidad_alimento = unidad_alimento;
    }

    public double getPrecioMercado() {
        return precioMercado;
    }

    public void setPrecioMercado(double precioMercado) {
        this.precioMercado = precioMercado;
    }

    public int getNumAnimales() {
        return numAnimales;
    }

    public void setNumAnimales(int numAnimales) {
        this.numAnimales = numAnimales;
    }

    public void setAnimal_tipo(int animal_tipo) {
        this.animal_tipo = animal_tipo;
    }


    public int getAnimal_tipo() {
        return animal_tipo;
    }


    public void setAnimal_nivel(int animal_nivel) {
        this.animal_nivel = animal_nivel;
    }


    public int getAnimal_nivel() {
        return animal_nivel;
    }


    public void setAlimento_id(int alimento_id) {
        this.alimento_id = alimento_id;
    }


    public int getAlimento_id() {
        return alimento_id;
    }


    public void setAlimento_max(int alimento_max) {
        this.alimento_max = alimento_max;
    }


    public int getAlimento_max() {
        return alimento_max;
    }


    public void setAlimento_min(int alimento_min) {
        this.alimento_min = alimento_min;
    }


    public int getAlimento_min() {
        return alimento_min;
    }


    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }


    public int getCantidad() {
        return cantidad;
    }


    public void setAnimal_fechanacimiento(String animal_fechanacimiento) {
        this.animal_fechanacimiento = animal_fechanacimiento;
    }


    public String getAnimal_fechanacimiento() {
        return animal_fechanacimiento;
    }


    public void setAlimento_desc(String alimento_desc) {
        this.alimento_desc = alimento_desc;
    }


    public String getAlimento_desc() {
        return alimento_desc;
    }


    public void setAnimal_desc(String animal_desc) {
        this.animal_desc = animal_desc;
    }


    public String getAnimal_desc() {
        return animal_desc;
    }


    public void setPrecio(String precio) {
        this.precio = precio;
    }


    public String getPrecio() {
        return precio;
    }


    public void setId(String id) {
        this.id = id;
    }


    public String getId() {
        return id;
    }

}