package org.abbatia.bean;

public class EdificioBase {
    private int tipo_edificio;
    private int edificioid;
    private String nombre_edificio;
    private String descripcion_edificio;
    private String descripcion_edificio_larga;
    private String coste_oro;
    private String coste_madera;
    private String coste_piedra;
    private String coste_hierro;
    private String grafico_construccion;
    private String grafico_visualizacion;
    private int siguienteNivel;
    private int nivel;
    private int dias_costruccion;
    private short existe;


    public void setTipo_edificio(int tipo_edificio) {
        this.tipo_edificio = tipo_edificio;
    }


    public int getTipo_edificio() {
        return tipo_edificio;
    }


    public void setNombre_edificio(String nombre_edificio) {
        this.nombre_edificio = nombre_edificio;
    }


    public String getNombre_edificio() {
        return nombre_edificio;
    }


    public void setDescripcion_edificio(String descripcion_edificio) {
        this.descripcion_edificio = descripcion_edificio;
    }


    public String getDescripcion_edificio() {
        return descripcion_edificio;
    }


    public void setCoste_oro(String coste_oro) {
        this.coste_oro = coste_oro;
    }


    public String getCoste_oro() {
        return coste_oro;
    }


    public void setCoste_madera(String coste_madera) {
        this.coste_madera = coste_madera;
    }


    public String getCoste_madera() {
        return coste_madera;
    }


    public void setCoste_piedra(String coste_piedra) {
        this.coste_piedra = coste_piedra;
    }


    public String getCoste_piedra() {
        return coste_piedra;
    }


    public void setCoste_hierro(String coste_hierro) {
        this.coste_hierro = coste_hierro;
    }


    public String getCoste_hierro() {
        return coste_hierro;
    }


    public void setDias_costruccion(int dias_costruccion) {
        this.dias_costruccion = dias_costruccion;
    }


    public int getDias_costruccion() {
        return dias_costruccion;
    }


    public void setExiste(short existe) {
        this.existe = existe;
    }


    public short getExiste() {
        return existe;
    }


    public void setDescripcion_edificio_larga(String descripcion_edificio_larga) {
        this.descripcion_edificio_larga = descripcion_edificio_larga;
    }


    public String getDescripcion_edificio_larga() {
        return descripcion_edificio_larga;
    }


    public void setGrafico_construccion(String grafico_construccion) {
        this.grafico_construccion = grafico_construccion;
    }


    public String getGrafico_construccion() {
        return grafico_construccion;
    }


    public void setGrafico_visualizacion(String grafico_visualizacion) {
        this.grafico_visualizacion = grafico_visualizacion;
    }

    public void setEdificioid(int edificioid) {
        this.edificioid = edificioid;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public void setSiguienteNivel(int siguienteNivel) {
        this.siguienteNivel = siguienteNivel;
    }

    public String getGrafico_visualizacion() {
        return grafico_visualizacion;
    }

    public int getEdificioid() {
        return edificioid;
    }

    public int getNivel() {
        return nivel;
    }

    public int getSiguienteNivel() {
        return siguienteNivel;
    }

}
