/***********************************************************************
 * Module:  Idioma.java
 * Author:  Benja & John
 * Purpose: Defines the Class Idioma
 ***********************************************************************/
package org.abbatia.bean;

import java.util.ArrayList;
import java.util.Hashtable;

public class Idioma {
    private int idDeIdioma;
    private String descripcion;
    private ArrayList idiomaDelUsuario;

    private Hashtable idiomaDelUsuarioHT;

    // --------------------------------------------------------------------------
    // ID de Idioma
    // --------------------------------------------------------------------------
    public void setIdDeIdioma(int newIdDeIdioma) {
        this.idDeIdioma = newIdDeIdioma;
    }

    public int getIdDeIdioma() {
        return this.idDeIdioma;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Descripción
    // --------------------------------------------------------------------------

    public void setDescripcion(String newDescripcion) {
        this.descripcion = newDescripcion;
    }

    public String getDescripcion() {
        return this.descripcion;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Asociacion Link B: Idioma del Usuario - Idioma Utilizado por el Usuario
    // --------------------------------------------------------------------------

    public ArrayList getIdiomaDelUsuario() {
        return idiomaDelUsuario;
    }

    public void setIdiomaDelUsuario(ArrayList idiomaDelUsuario) {
        this.idiomaDelUsuario = idiomaDelUsuario;
    }

    public Hashtable getIdiomaDelUsuarioHT() {
        return idiomaDelUsuarioHT;
    }

    public void setIdiomaDelUsuarioHT(Hashtable idiomaDelUsuarioHT) {
        this.idiomaDelUsuarioHT = idiomaDelUsuarioHT;
    }

    public void addUsuario(Usuario usuario) {
        if (this.idiomaDelUsuario == null)
            this.idiomaDelUsuario = new ArrayList();
        this.idiomaDelUsuario.add(usuario);
    }

    public boolean removeUsuario(Usuario usuario) {
        if (this.idiomaDelUsuario != null)
            return this.idiomaDelUsuario.remove(usuario);
        return false;
    }
    // --------------------------------------------------------------------------


}


