/***********************************************************************
 * Module:  Usuario.java
 * Author:  Benja & John
 * Purpose: Defines the Class Usuario
 ***********************************************************************/
package org.abbatia.bean;

import java.io.Serializable;

public class Usuario implements Serializable {
    private long idDeUsuario;
    private String nick;
    private String contrasena;
    private String nombre;
    private String primerApellido;
    private String segundoApellido;
    private String email;
    private long conectado;
    private String paginaWeb;
    private long icq;
    private long bloqueado;
    private long registrado;
    private long administrador;
    private short congelado;
    private String ultimaConexion;
    private short idDeIdioma;
    private long idDeAbadia;
    private int edad;
    private String edadDesc;
    private int pais;
    private int sexo;
    private String fecha_alta;
    private String fecha_bloqueo;
    private int dias_bloqueo;
    private int reintentos;
    private String ipActual;
    private String motivo;
    private boolean aceptaNormas;

    public boolean isAceptaNormas() {
        return aceptaNormas;
    }

    public void setAceptaNormas(boolean aceptaNormas) {
        this.aceptaNormas = aceptaNormas;
    }

    public String getEdadDesc() {
        return edadDesc;
    }

    public void setEdadDesc(String edadDesc) {
        this.edadDesc = edadDesc;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getIpActual() {
        return ipActual;
    }

    public void setIpActual(String ipActual) {
        this.ipActual = ipActual;
    }

    public int getReintentos() {
        return reintentos;
    }

    public void setReintentos(int reintentos) {
        this.reintentos = reintentos;
    }

    public int getDias_bloqueo() {
        return dias_bloqueo;
    }

    public void setDias_bloqueo(int dias_bloqueo) {
        this.dias_bloqueo = dias_bloqueo;
    }

    public String getFecha_bloqueo() {
        return fecha_bloqueo;
    }

    public void setFecha_bloqueo(String fecha_bloqueo) {
        this.fecha_bloqueo = fecha_bloqueo;
    }

    public int getSexo() {
        return sexo;
    }

    public void setSexo(int sexo) {
        this.sexo = sexo;
    }

    public String getFecha_alta() {
        return fecha_alta;
    }

    public void setFecha_alta(String fecha_alta) {
        this.fecha_alta = fecha_alta;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public int getPais() {
        return pais;
    }

    public void setPais(int pais) {
        this.pais = pais;
    }


    // --------------------------------------------------------------------------
    // ID de Usuario
    // --------------------------------------------------------------------------
    public void setIdDeUsuario(long newIdDeUsuario) {
        this.idDeUsuario = newIdDeUsuario;
    }

    public long getIdDeUsuario() {
        return this.idDeUsuario;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Nick
    // --------------------------------------------------------------------------

    public void setNick(String newNick) {
        this.nick = newNick;
    }

    public String getNick() {
        return this.nick;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Contraseña
    // --------------------------------------------------------------------------

    public void setContrasena(String newContrasena) {
        this.contrasena = newContrasena;
    }

    public String getContrasena() {
        return this.contrasena;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Nombre
    // --------------------------------------------------------------------------

    public void setNombre(String newNombre) {
        this.nombre = newNombre;
    }

    public String getNombre() {
        return this.nombre;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Primer Apellido
    // --------------------------------------------------------------------------

    public void setPrimerApellido(String newPrimerApellido) {
        this.primerApellido = newPrimerApellido;
    }

    public String getPrimerApellido() {
        return this.primerApellido;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Segundo Apellido
    // --------------------------------------------------------------------------

    public void setSegundoApellido(String newSegundoApellido) {
        this.segundoApellido = newSegundoApellido;
    }

    public String getSegundoApellido() {
        return this.segundoApellido;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // email
    // --------------------------------------------------------------------------

    public void setEmail(String newEmail) {
        this.email = newEmail;
    }

    public String getEmail() {
        return this.email;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Conectado
    // --------------------------------------------------------------------------

    public void setConectado(long newConectado) {
        this.conectado = newConectado;
    }

    public long getConectado() {
        return this.conectado;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Administrador
    // --------------------------------------------------------------------------

    public void setAdministrador(long newAdministrador) {
        this.administrador = newAdministrador;
    }

    public long getAdministrador() {
        return this.administrador;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Registrado
    // --------------------------------------------------------------------------

    public void setRegistrado(long newRegistrado) {
        this.registrado = newRegistrado;
    }

    public long getRegistrado() {
        return this.registrado;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Pagina Web
    // --------------------------------------------------------------------------

    public void setPaginaWeb(String newPaginaWeb) {
        this.paginaWeb = newPaginaWeb;
    }

    public String getPaginaWeb() {
        return this.paginaWeb;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // ICQ
    // --------------------------------------------------------------------------

    public void setIcq(long newIcq) {
        this.icq = newIcq;
    }

    public long getIcq() {
        return this.icq;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Bloqueado
    // --------------------------------------------------------------------------

    public void setBloqueado(long newBloqueado) {
        this.bloqueado = newBloqueado;
    }

    public long getBloqueado() {
        return this.bloqueado;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Ultima Conexion
    // --------------------------------------------------------------------------

    public void setUltimaConexion(String newUltimaConexion) {
        this.ultimaConexion = newUltimaConexion;
    }

    public String getUltimaConexion() {
        return this.ultimaConexion;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Asociacion A: ID de Idioma
    // --------------------------------------------------------------------------

    public void setIdDeIdioma(short newIdDeIdioma) {
        this.idDeIdioma = newIdDeIdioma;
    }

    public short getIdDeIdioma() {
        return this.idDeIdioma;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Asociacion B: ID de Abadia
    // --------------------------------------------------------------------------

    public void setIdDeAbadia(long newIdDeAbadia) {
        this.idDeAbadia = newIdDeAbadia;
    }

    public long getIdDeAbadia() {
        return this.idDeAbadia;
    }

    // --------------------------------------------------------------------------


    public void setCongelado(short congelado) {
        this.congelado = congelado;
    }

    public short getCongelado() {
        return congelado;
    }
}


