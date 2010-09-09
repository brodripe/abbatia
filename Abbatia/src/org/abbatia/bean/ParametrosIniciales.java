package org.abbatia.bean;

import java.util.Hashtable;

public class ParametrosIniciales {
    Hashtable propiedades;
    Hashtable habilidades;


    public void setPropiedades(Hashtable propiedades) {
        this.propiedades = propiedades;
    }


    public Hashtable getPropiedades() {
        return propiedades;
    }


    public void setHabilidades(Hashtable habilidades) {
        this.habilidades = habilidades;
    }


    public Hashtable getHabilidades() {
        return habilidades;
    }

}