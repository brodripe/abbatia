package org.abbatia.bean;

public class Animal {
    private int edificioid;
    private int animalid;
    private int productoid;// Pa el mercado
    private int tipoAnimalid;
    private short nivel;
    private int estado;
    private String fecha_nacimiento;
    private String fecha_fallecimiento;
    private String fecha_embarazo;
    private String fecha_parto;
    private int salud;
    private int actividad;
    private int alimentoid;
    private int cantidad;
    private String nombre;
    private String clave;
    private int carneMin;
    private int carneMax;
    private short aislado;
    private int abadiaid;
    private String descTipo;
    private String barra_HTML;
    private int puedeTrabajar;
    private int trabaja;

    public int getPuedeTrabajar() {
        return puedeTrabajar;
    }

    public void setPuedeTrabajar(int puedeTrabajar) {
        this.puedeTrabajar = puedeTrabajar;
    }

    public int getTrabaja() {
        return trabaja;
    }

    public void setTrabaja(int trabaja) {
        this.trabaja = trabaja;
    }

    public String getBarra_HTML() {
        return barra_HTML;
    }

    public void setBarra_HTML(String barra_HTML) {
        this.barra_HTML = barra_HTML;
    }

    public String getDescTipo() {
        return descTipo;
    }

    public void setDescTipo(String descTipo) {
        this.descTipo = descTipo;
    }

    public int getAbadiaid() {
        return abadiaid;
    }

    public void setAbadiaid(int abadiaid) {
        this.abadiaid = abadiaid;
    }


    public void setEdificioid(int edificioid) {
        this.edificioid = edificioid;
    }


    public int getEdificioid() {
        return edificioid;
    }


    public void setAnimalid(int animalid) {
        this.animalid = animalid;
    }


    public int getAnimalid() {
        return animalid;
    }


    public void setNivel(short nivel) {
        this.nivel = nivel;
    }


    public short getNivel() {
        return nivel;
    }


    public void setEstado(int estado) {
        this.estado = estado;
    }


    public int getEstado() {
        return estado;
    }


    public void setFecha_nacimiento(String fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }


    public String getFecha_nacimiento() {
        return fecha_nacimiento;
    }


    public void setFecha_fallecimiento(String fecha_fallecimiento) {
        this.fecha_fallecimiento = fecha_fallecimiento;
    }


    public String getFecha_fallecimiento() {
        return fecha_fallecimiento;
    }


    public void setSalud(int salud) {
        this.salud = salud;
    }


    public int getSalud() {
        return salud;
    }


    public void setActividad(int actividad) {
        this.actividad = actividad;
    }


    public int getActividad() {
        return actividad;
    }


    public void setAlimentoid(int alimentoid) {
        this.alimentoid = alimentoid;
    }


    public int getAlimentoid() {
        return alimentoid;
    }


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public String getNombre() {
        return nombre;
    }


    public void setTipoAnimalid(int tipoAnimalid) {
        this.tipoAnimalid = tipoAnimalid;
    }


    public int getTipoAnimalid() {
        return tipoAnimalid;
    }


    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }


    public int getCantidad() {
        return cantidad;
    }


    public void setClave(String clave) {
        this.clave = clave;
    }


    public String getClave() {
        return clave;
    }


    public void setCarneMin(int carneMin) {
        this.carneMin = carneMin;
    }


    public int getCarneMin() {
        return carneMin;
    }


    public void setCarneMax(int carneMax) {
        this.carneMax = carneMax;
    }


    public int getCarneMax() {
        return carneMax;
    }


    public void setFecha_embarazo(String fecha_embarazo) {
        this.fecha_embarazo = fecha_embarazo;
    }


    public String getFecha_embarazo() {
        return fecha_embarazo;
    }


    public void setFecha_parto(String fecha_parto) {
        this.fecha_parto = fecha_parto;
    }


    public String getFecha_parto() {
        return fecha_parto;
    }


    public void setAislado(short aislado) {
        this.aislado = aislado;
    }

    public void setProductoid(int productoid) {
        this.productoid = productoid;
    }

    public short getAislado() {
        return aislado;
    }

    public int getProductoid() {
        return productoid;
    }

}
