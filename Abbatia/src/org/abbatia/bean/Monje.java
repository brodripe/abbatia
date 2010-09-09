/***********************************************************************
 * Module:  Monje.java
 * Author:  Benja & John
 * Purpose: Defines the Class Monje
 ***********************************************************************/
package org.abbatia.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

public class Monje extends MonjeBase implements Serializable {
    private String fechaDeNacimiento;
    private String fechaDeFallecimiento;
    private String fechaDeEntradaEnAbadia;
    private int siguienteID;
    private int anteriorID;
    private int estado;   // 0 - vivo, 1 - muerto ( velatorio ), 2 - enfermo, 3 - viaje
    private String jerarquia;
    private int idDeAbadia_Visita;
    private int idDeEspecializacion;
    private short santo;
    private short nivelAbadia;
    // Descripcion de abbatia y region solo para ense�arlo desde otra abbatia
    private String abadia;
    private String region;
    // Alimentaci�n del monje
    private short comeFamiliaID1;
    private short comeFamiliaID2;
    private short comeFamiliaID3;
    private short comeFamiliaID4;
    private short comeFamiliaID5;
    private short ha_comidoFamiliaID1;
    private short ha_comidoFamiliaID2;
    private short ha_comidoFamiliaID3;
    private short ha_comidoFamiliaID4;
    private short ha_comidoFamiliaID5;
    //Datos de Actividades
    // Actividades del monje
    private short actMaitines;
    private short actLaudes;
    private short actPrima;
    private short actTercia;
    private short actSexta;
    private short actNona;
    private short actVispera;
    private short actAngelus;

    private short actMaitines_completado;
    private short actMaitines_bloqueado;
    private String actMaitines_link;

    private short actLaudes_completado;
    private short actLaudes_bloqueado;
    private String actLaudes_link;

    private short actPrima_completado;
    private short actPrima_bloqueado;
    private String actPrima_link;

    private short actTercia_completado;
    private short actTercia_bloqueado;
    private String actTercia_link;

    private short actSexta_completado;
    private short actSexta_bloqueado;
    private String actSexta_link;

    private short actNona_completado;
    private short actNona_bloqueado;
    private String actNona_link;

    private short actVispera_completado;
    private short actVispera_bloqueado;
    private String actVispera_link;

    private short actAngelus_completado;
    private short actAngelus_bloqueado;
    private String actAngelus_link;

    private Enfermedad enfermedad;
    private short proteinas;
    private short lipidos;
    private short hidratosCarbono;
    private short vitaminas;
    private short ant_proteinas;
    private short ant_lipidos;
    private short ant_hidratosCarbono;
    private short ant_vitaminas;
    private short salud;
    private String barra_proteinas;
    private String barra_lipidos;
    private String barra_hidratosCarbono;
    private String barra_vitaminas;
    private String barra_salud;
    // Habilidades
    private String barra_idioma;
    private String barra_fe;
    private String barra_fuerza;
    private String barra_destreza;
    private String barra_talento;
    private String barra_sabiduria;
    private String barra_carisma;
    private String barra_popularidad;
    private int fe = 0;
    private int idioma = 0;
    private int talento = 0;
    private int sabiduria = 0;
    private int fuerza = 0;
    private int carisma = 0;
    private int destreza = 0;
    private int popularidad = 0;   // Propiedad id:27


    private String fechaDeUltimaComida;
    private String fechallegada;
    private String HTMLEnfermedad = "";
    private int nivelEnfermedad = 0;
    private short edad;

    // Listas
    private ArrayList<Habilidad> habilidadesDeLosMonjes;

    private Hashtable habilidadesDeLosMonjesHT;
    private ArrayList<Propiedad> propiedadesDeLosMonjes;

    private Hashtable propiedadesDeLosMonjesHT;
    private ArrayList<TareasDelMonje> tareasDelMonje;

    private Hashtable tareasDelMonjeHT;
    private ArrayList mensajesMonje;

    private Hashtable mensajesMonjeHT;


    private String imgProteinas_eval;
    private String imgLipidos_eval;
    private String imgHidratos_eval;
    private String imgVitaminas_eval;
    private String imgSalud_eval;

    private int puntosMonje;
    private String nombreCompuesto;


    public Monje(int p_iIdMonje) {
        super(p_iIdMonje);
    }

    public Monje() {
        super();
    }


    public short getSanto() {
        return santo;
    }

    public void setSanto(short santo) {
        this.santo = santo;
    }


    public int getNivelEnfermedad() {
        return nivelEnfermedad;
    }

    public void setNivelEnfermedad(int nivelEnfermedad) {
        this.nivelEnfermedad = nivelEnfermedad;
    }

    public Enfermedad getEnfermedad() {
        return enfermedad;
    }

    public void setEnfermedad(Enfermedad enfermedad) {
        this.enfermedad = enfermedad;
    }

    public short getActMaitines_bloqueado() {
        return actMaitines_bloqueado;
    }

    public void setActMaitines_bloqueado(short actMaitines_bloqueado) {
        this.actMaitines_bloqueado = actMaitines_bloqueado;
    }

    public short getActLaudes_bloqueado() {
        return actLaudes_bloqueado;
    }

    public void setActLaudes_bloqueado(short actLaudes_bloqueado) {
        this.actLaudes_bloqueado = actLaudes_bloqueado;
    }

    public short getActPrima_bloqueado() {
        return actPrima_bloqueado;
    }

    public void setActPrima_bloqueado(short actPrima_bloqueado) {
        this.actPrima_bloqueado = actPrima_bloqueado;
    }

    public short getActTercia_bloqueado() {
        return actTercia_bloqueado;
    }

    public void setActTercia_bloqueado(short actTercia_bloqueado) {
        this.actTercia_bloqueado = actTercia_bloqueado;
    }

    public short getActSexta_bloqueado() {
        return actSexta_bloqueado;
    }

    public void setActSexta_bloqueado(short actSexta_bloqueado) {
        this.actSexta_bloqueado = actSexta_bloqueado;
    }

    public short getActNona_bloqueado() {
        return actNona_bloqueado;
    }

    public void setActNona_bloqueado(short actNona_bloqueado) {
        this.actNona_bloqueado = actNona_bloqueado;
    }

    public short getActVispera_bloqueado() {
        return actVispera_bloqueado;
    }

    public void setActVispera_bloqueado(short actVispera_bloqueado) {
        this.actVispera_bloqueado = actVispera_bloqueado;
    }

    public short getActAngelus_bloqueado() {
        return actAngelus_bloqueado;
    }

    public void setActAngelus_bloqueado(short actAngelus_bloqueado) {
        this.actAngelus_bloqueado = actAngelus_bloqueado;
    }

    public String getActMaitines_link() {
        return actMaitines_link;
    }

    public void setActMaitines_link(String actMaitines_link) {
        this.actMaitines_link = actMaitines_link;
    }

    public String getActLaudes_link() {
        return actLaudes_link;
    }

    public void setActLaudes_link(String actLaudes_link) {
        this.actLaudes_link = actLaudes_link;
    }

    public String getActPrima_link() {
        return actPrima_link;
    }

    public void setActPrima_link(String actPrima_link) {
        this.actPrima_link = actPrima_link;
    }

    public String getActTercia_link() {
        return actTercia_link;
    }

    public void setActTercia_link(String actTercia_link) {
        this.actTercia_link = actTercia_link;
    }

    public String getActSexta_link() {
        return actSexta_link;
    }

    public void setActSexta_link(String actSexta_link) {
        this.actSexta_link = actSexta_link;
    }

    public String getActNona_link() {
        return actNona_link;
    }

    public void setActNona_link(String actNona_link) {
        this.actNona_link = actNona_link;
    }

    public String getActVispera_link() {
        return actVispera_link;
    }

    public void setActVispera_link(String actVispera_link) {
        this.actVispera_link = actVispera_link;
    }

    public String getActAngelus_link() {
        return actAngelus_link;
    }

    public void setActAngelus_link(String actAngelus_link) {
        this.actAngelus_link = actAngelus_link;
    }

    // --------------------------------------------------------------------------
    // Fecha de Nacimiento
    // --------------------------------------------------------------------------

    public void setFechaDeNacimiento(String newFechaDeNacimiento) {
        this.fechaDeNacimiento = newFechaDeNacimiento;
    }

    public String getFechaDeNacimiento() {
        return this.fechaDeNacimiento;
    }
    // --------------------------------------------------------------------------

// --------------------------------------------------------------------------
// Fecha de Llegada
// --------------------------------------------------------------------------

    public String getFechallegada() {
        return fechallegada;
    }

    public void setFechallegada(String fechallegada) {
        this.fechallegada = fechallegada;
    }

    // --------------------------------------------------------------------------
    // Fecha de Entrada en Abadía
    // --------------------------------------------------------------------------
    public void setFechaDeEntradaEnAbadia(String newFechaDeEntradaEnAbadia) {
        this.fechaDeEntradaEnAbadia = newFechaDeEntradaEnAbadia;
    }

    public String getFechaDeEntradaEnAbadia() {
        return this.fechaDeEntradaEnAbadia;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Proteínas
    // --------------------------------------------------------------------------

    public void setProteinas(short newProteinas) {
        if (newProteinas > 60)
            this.proteinas = 60;
        else if (newProteinas < 0)
            this.proteinas = 0;
        else
            this.proteinas = newProteinas;
    }

    public short getProteinas() {
        return this.proteinas;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Lípidos
    // --------------------------------------------------------------------------

    public void setLipidos(short newLipidos) {
        if (newLipidos > 60)
            this.lipidos = 60;
        else if (newLipidos < 0)
            this.lipidos = 0;
        else
            this.lipidos = newLipidos;
    }

    public short getLipidos() {
        return this.lipidos;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Hidratos Carbono
    // --------------------------------------------------------------------------

    public void setHidratosCarbono(short newHidratosCarbono) {
        if (newHidratosCarbono > 60)
            this.hidratosCarbono = 60;
        else if (newHidratosCarbono < 0)
            this.hidratosCarbono = 0;
        else
            this.hidratosCarbono = newHidratosCarbono;
    }

    public short getHidratosCarbono() {
        return this.hidratosCarbono;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Vitaminas
    // --------------------------------------------------------------------------

    public void setVitaminas(short newVitaminas) {
        if (newVitaminas > 60)
            this.vitaminas = 60;
        else if (newVitaminas < 0)
            this.vitaminas = 0;
        else
            this.vitaminas = newVitaminas;
    }

    public short getVitaminas() {
        return this.vitaminas;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Asociacion A: ID de Especialización
    // --------------------------------------------------------------------------

    public void setIdDeEspecializacion(int newIdDeEspecializacion) {
        this.idDeEspecializacion = newIdDeEspecializacion;
    }

    public int getIdDeEspecializacion() {
        return this.idDeEspecializacion;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Asociacion Link B: Habilidades de los Monjes - Habilidades de los Monjes
    // --------------------------------------------------------------------------

    public ArrayList<Habilidad> getHabilidadesDeLosMonjes() {
        return habilidadesDeLosMonjes;
    }

    public void setHabilidadesDeLosMonjes(ArrayList<Habilidad> habilidadesDeLosMonjes) {
        this.habilidadesDeLosMonjes = habilidadesDeLosMonjes;
    }

    public Hashtable getHabilidadesDeLosMonjesHT() {
        return habilidadesDeLosMonjesHT;
    }

    public void setHabilidadesDeLosMonjesHT(Hashtable habilidadesDeLosMonjesHT) {
        this.habilidadesDeLosMonjesHT = habilidadesDeLosMonjesHT;
    }

    public void addHabilidad(Habilidad habilidad) {
        if (this.habilidadesDeLosMonjes == null)
            this.habilidadesDeLosMonjes = new ArrayList<Habilidad>();
        this.habilidadesDeLosMonjes.add(habilidad);
    }

    public boolean removeHabilidad(Habilidad habilidad) {
        if (this.habilidadesDeLosMonjes != null)
            return this.habilidadesDeLosMonjes.remove(habilidad);
        return false;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Asociacion Link B: Propiedades de los Monjes
    // --------------------------------------------------------------------------

    public ArrayList<Propiedad> getPropiedadesDeLosMonjes() {
        return propiedadesDeLosMonjes;
    }

    public void setPropiedadesDeLosMonjes(ArrayList<Propiedad> propiedadesDeLosMonjes) {
        this.propiedadesDeLosMonjes = propiedadesDeLosMonjes;
    }

    public Hashtable getPropiedadesDeLosMonjesHT() {
        return propiedadesDeLosMonjesHT;
    }

    public void setPropiedadesDeLosMonjesHT(Hashtable propiedadesDeLosMonjesHT) {
        this.propiedadesDeLosMonjesHT = propiedadesDeLosMonjesHT;
    }

    public void addPropiedad(Propiedad propiedad) {
        if (this.propiedadesDeLosMonjes == null)
            this.propiedadesDeLosMonjes = new ArrayList<Propiedad>();
        this.propiedadesDeLosMonjes.add(propiedad);
    }

    public boolean removePropiedad(Propiedad propiedad) {
        return this.propiedadesDeLosMonjes != null && this.propiedadesDeLosMonjes.remove(propiedad);
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Asociacion Link B: Tareas del Monje
    // --------------------------------------------------------------------------

    public ArrayList<TareasDelMonje> getTareasDelMonje() {
        return tareasDelMonje;
    }

    public void setTareasDelMonje(ArrayList<TareasDelMonje> tareasDelMonje) {
        this.tareasDelMonje = tareasDelMonje;
    }

    public Hashtable getTareasDelMonjeHT() {
        return tareasDelMonjeHT;
    }

    public void setTareasDelMonjeHT(Hashtable tareasDelMonjeHT) {
        this.tareasDelMonjeHT = tareasDelMonjeHT;
    }

    public void addTareasDelMonje(TareasDelMonje tareasDelMonje) {
        if (this.tareasDelMonje == null)
            this.tareasDelMonje = new ArrayList<TareasDelMonje>();
        this.tareasDelMonje.add(tareasDelMonje);
    }

    public boolean removeTareasDelMonje(TareasDelMonje tareasDelMonje) {
        return this.tareasDelMonje != null && this.tareasDelMonje.remove(tareasDelMonje);
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Asociacion Link B: Mensajes Monje
    // --------------------------------------------------------------------------

    public ArrayList getMensajesMonje() {
        return mensajesMonje;
    }

    public void setMensajesMonje(ArrayList<Table> mensajesMonje) {
        this.mensajesMonje = mensajesMonje;
    }

    public Hashtable getMensajesMonjeHT() {
        return mensajesMonjeHT;
    }

    public short getComeFamiliaID5() {
        return comeFamiliaID5;
    }

    public short getSalud() {
        return salud;
    }

    public short getComeFamiliaID1() {
        return comeFamiliaID1;
    }

    public short getComeFamiliaID4() {
        return comeFamiliaID4;
    }

    public short getComeFamiliaID3() {
        return comeFamiliaID3;
    }

    public short getComeFamiliaID2() {
        return comeFamiliaID2;
    }

    public String getFechaDeUltimaComida() {
        return fechaDeUltimaComida;
    }

    public void setMensajesMonjeHT(Hashtable mensajesMonjeHT) {
        this.mensajesMonjeHT = mensajesMonjeHT;
    }

    public void setComeFamiliaID5(short comeFamiliaID5) {
        this.comeFamiliaID5 = comeFamiliaID5;
    }

    public void setSalud(short salud) {
        if (salud > 100) this.salud = 100;
        else if (salud < 0) this.salud = 0;
        else
            this.salud = salud;
    }

    public void setComeFamiliaID1(short comeFamiliaID1) {
        this.comeFamiliaID1 = comeFamiliaID1;
    }

    public void setComeFamiliaID4(short comeFamiliaID4) {
        this.comeFamiliaID4 = comeFamiliaID4;
    }

    public void setComeFamiliaID3(short comeFamiliaID3) {
        this.comeFamiliaID3 = comeFamiliaID3;
    }

    public void setComeFamiliaID2(short comeFamiliaID2) {
        this.comeFamiliaID2 = comeFamiliaID2;
    }

    public void setFechaDeUltimaComida(String fechaDeUltimaComida) {
        this.fechaDeUltimaComida = fechaDeUltimaComida;
    }

    public void addMensajes(Mensajes mensajes) {
        if (this.mensajesMonje == null)
            this.mensajesMonje = new ArrayList();
        this.mensajesMonje.add(mensajes);
    }

    public boolean removeMensajes(Mensajes mensajes) {
        if (this.mensajesMonje != null)
            return this.mensajesMonje.remove(mensajes);
        return false;
    }


    public void setEdad(short edad) {
        this.edad = edad;
    }

    public void setBarra_proteinas(String barra_proteinas) {
        this.barra_proteinas = barra_proteinas;
    }

    public void setBarra_lipidos(String barra_lipidos) {
        this.barra_lipidos = barra_lipidos;
    }

    public void setBarra_salud(String barra_salud) {
        this.barra_salud = barra_salud;
    }

    public void setBarra_hidratosCarbono(String barra_hidratosCarbono) {
        this.barra_hidratosCarbono = barra_hidratosCarbono;
    }

    public void setBarra_vitaminas(String barra_vitaminas) {
        this.barra_vitaminas = barra_vitaminas;
    }

    public void setFechaDeFallecimiento(String fechaDeFallecimiento) {
        this.fechaDeFallecimiento = fechaDeFallecimiento;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public short getEdad() {
        return edad;
    }

    public String getBarra_proteinas() {
        return barra_proteinas;
    }

    public String getBarra_lipidos() {
        return barra_lipidos;
    }

    public String getBarra_salud() {
        return barra_salud;
    }

    public String getBarra_hidratosCarbono() {
        return barra_hidratosCarbono;
    }

    public String getBarra_vitaminas() {
        return barra_vitaminas;
    }

    public String getFechaDeFallecimiento() {
        return fechaDeFallecimiento;
    }

    public int getEstado() {
        return estado;
    }


    public void setActPrima(short actPrima) {
        this.actPrima = actPrima;
    }


    public short getActPrima() {
        return actPrima;
    }


    public void setActTercia(short actTercia) {
        this.actTercia = actTercia;
    }


    public short getActTercia() {
        return actTercia;
    }


    public void setActNona(short actNona) {
        this.actNona = actNona;
    }


    public short getActNona() {
        return actNona;
    }


    public void setActVispera(short actVispera) {
        this.actVispera = actVispera;
    }


    public short getActVispera() {
        return actVispera;
    }


    public void setActMaitines(short actMaitines) {
        this.actMaitines = actMaitines;
    }


    public short getActMaitines() {
        return actMaitines;
    }


    public void setActLaudes(short actLaudes) {
        this.actLaudes = actLaudes;
    }


    public short getActLaudes() {
        return actLaudes;
    }


    public void setActSexta(short actSexta) {
        this.actSexta = actSexta;
    }


    public short getActSexta() {
        return actSexta;
    }


    public void setActAngelus(short actAngelus) {
        this.actAngelus = actAngelus;
    }


    public short getActAngelus() {
        return actAngelus;
    }


    public void setSiguienteID(int siguienteID) {
        this.siguienteID = siguienteID;
    }


    public int getSiguienteID() {
        return siguienteID;
    }


    public void setAnteriorID(int anteriorID) {
        this.anteriorID = anteriorID;
    }


    public void setBarra_fe(String barra_fe) {
        this.barra_fe = barra_fe;
    }

    public void setBarra_destreza(String barra_destreza) {
        this.barra_destreza = barra_destreza;
    }

    public void setBarra_talento(String barra_talento) {
        this.barra_talento = barra_talento;
    }

    public void setBarra_sabiduria(String barra_sabiduria) {
        this.barra_sabiduria = barra_sabiduria;
    }

    public void setBarra_idioma(String barra_idioma) {
        this.barra_idioma = barra_idioma;
    }

    public void setBarra_fuerza(String barra_fuerza) {
        this.barra_fuerza = barra_fuerza;
    }

    public int getAnteriorID() {
        return anteriorID;
    }


    public String getBarra_fe() {
        return barra_fe;
    }

    public String getBarra_destreza() {
        return barra_destreza;
    }

    public String getBarra_talento() {
        return barra_talento;
    }

    public String getBarra_sabiduria() {
        return barra_sabiduria;
    }

    public String getBarra_idioma() {
        return barra_idioma;
    }

    public String getBarra_fuerza() {
        return barra_fuerza;
    }


    public void setFe(int fe) {
        this.fe = fe;
    }


    public int getFe() {
        return fe;
    }


    public void setIdioma(int idioma) {
        this.idioma = idioma;
    }


    public int getIdioma() {
        return idioma;
    }


    public void setTalento(int talento) {
        this.talento = talento;
    }


    public int getTalento() {
        return talento;
    }


    public void setSabiduria(int sabiduria) {
        this.sabiduria = sabiduria;
    }


    public int getSabiduria() {
        return sabiduria;
    }


    public void setFuerza(int fuerza) {
        this.fuerza = fuerza;
    }


    public int getFuerza() {
        return fuerza;
    }


    public void setDestreza(int destreza) {
        this.destreza = destreza;
    }

    public void setActNona_completado(short actNona_completado) {
        this.actNona_completado = actNona_completado;
    }

    public void setActAngelus_completado(short actAngelus_completado) {
        this.actAngelus_completado = actAngelus_completado;
    }

    public void setActPrima_completado(short actPrima_completado) {
        this.actPrima_completado = actPrima_completado;
    }

    public void setActMaitines_completado(short actMaitines_completado) {
        this.actMaitines_completado = actMaitines_completado;
    }

    public void setActTercia_completado(short actTercia_completado) {
        this.actTercia_completado = actTercia_completado;
    }

    public void setActLaudes_completado(short actLaudes_completado) {
        this.actLaudes_completado = actLaudes_completado;
    }

    public void setActSexta_completado(short actSexta_completado) {
        this.actSexta_completado = actSexta_completado;
    }

    public void setActVispera_completado(short actVispera_completado) {
        this.actVispera_completado = actVispera_completado;
    }

    public void setBarra_carisma(String barra_carisma) {
        this.barra_carisma = barra_carisma;
    }

    public void setCarisma(int carisma) {
        this.carisma = carisma;
    }

    public void setAnt_hidratosCarbono(short ant_hidratosCarbono) {
        this.ant_hidratosCarbono = ant_hidratosCarbono;
    }

    public void setAnt_lipidos(short ant_lipidos) {
        this.ant_lipidos = ant_lipidos;
    }

    public void setAnt_proteinas(short ant_proteinas) {
        this.ant_proteinas = ant_proteinas;
    }

    public void setAnt_vitaminas(short ant_vitaminas) {
        this.ant_vitaminas = ant_vitaminas;
    }

    public void setHa_comidoFamiliaID3(short ha_comidoFamiliaID3) {
        this.ha_comidoFamiliaID3 = ha_comidoFamiliaID3;
    }

    public void setHa_comidoFamiliaID5(short ha_comidoFamiliaID5) {
        this.ha_comidoFamiliaID5 = ha_comidoFamiliaID5;
    }

    public void setHa_comidoFamiliaID2(short ha_comidoFamiliaID2) {
        this.ha_comidoFamiliaID2 = ha_comidoFamiliaID2;
    }

    public void setHa_comidoFamiliaID4(short ha_comidoFamiliaID4) {
        this.ha_comidoFamiliaID4 = ha_comidoFamiliaID4;
    }

    public void setHa_comidoFamiliaID1(short ha_comidoFamiliaID1) {
        this.ha_comidoFamiliaID1 = ha_comidoFamiliaID1;
    }

    public void setJerarquia(String jerarquia) {
        this.jerarquia = jerarquia;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setAbadia(String abadia) {
        this.abadia = abadia;
    }

    public void setPopularidad(int popularidad) {
        if (popularidad > 100) this.popularidad = 100;
        else if (popularidad < 0) this.popularidad = 0;
        else
            this.popularidad = popularidad;
    }

    public void setBarra_popularidad(String barra_popularidad) {
        this.barra_popularidad = barra_popularidad;
    }

    public int getDestreza() {
        return destreza;
    }

    public short getActNona_completado() {
        return actNona_completado;
    }

    public short getActAngelus_completado() {
        return actAngelus_completado;
    }

    public short getActPrima_completado() {
        return actPrima_completado;
    }

    public short getActMaitines_completado() {
        return actMaitines_completado;
    }

    public short getActTercia_completado() {
        return actTercia_completado;
    }

    public short getActLaudes_completado() {
        return actLaudes_completado;
    }

    public short getActSexta_completado() {
        return actSexta_completado;
    }

    public short getActVispera_completado() {
        return actVispera_completado;
    }

    public String getBarra_carisma() {
        return barra_carisma;
    }

    public int getCarisma() {
        return carisma;
    }

    public short getAnt_hidratosCarbono() {
        if (ant_hidratosCarbono == 0)
            return 1;  // evitar division por cero
        else
            return ant_hidratosCarbono;
    }

    public short getAnt_lipidos() {
        if (ant_lipidos == 0)
            return 1;  // evitar division por cero
        else
            return ant_lipidos;
    }

    public short getAnt_proteinas() {
        if (ant_proteinas == 0)
            return 1;  // evitar division por cero
        else
            return ant_proteinas;
    }

    public short getAnt_vitaminas() {
        if (ant_vitaminas == 0)
            return 1;  // evitar division por cero
        else
            return ant_vitaminas;
    }

    public short getHa_comidoFamiliaID3() {
        return ha_comidoFamiliaID3;
    }

    public short getHa_comidoFamiliaID5() {
        return ha_comidoFamiliaID5;
    }

    public short getHa_comidoFamiliaID2() {
        return ha_comidoFamiliaID2;
    }

    public short getHa_comidoFamiliaID4() {
        return ha_comidoFamiliaID4;
    }

    public short getHa_comidoFamiliaID1() {
        return ha_comidoFamiliaID1;
    }

    public String getJerarquia() {
        return jerarquia;
    }

    public String getRegion() {
        return region;
    }

    public String getAbadia() {
        return abadia;
    }

    public int getPopularidad() {
        return popularidad;
    }

    public String getBarra_popularidad() {
        return barra_popularidad;
    }

    public short isActPrima_completado() {
        return actPrima_completado;
    }


    public int getIdDeAbadia_Visita() {
        return idDeAbadia_Visita;
    }

    public void setIdDeAbadia_Visita(int idDeAbadia_Visita) {
        this.idDeAbadia_Visita = idDeAbadia_Visita;
    }

    public String getImgProteinas_eval() {
        return imgProteinas_eval;
    }

    public void setImgProteinas_eval(String imgProteinas_eval) {
        this.imgProteinas_eval = imgProteinas_eval;
    }

    public String getImgLipidos_eval() {
        return imgLipidos_eval;
    }

    public void setImgLipidos_eval(String imgLipidos_eval) {
        this.imgLipidos_eval = imgLipidos_eval;
    }

    public String getImgHidratos_eval() {
        return imgHidratos_eval;
    }

    public void setImgHidratos_eval(String imgHidratos_eval) {
        this.imgHidratos_eval = imgHidratos_eval;
    }

    public String getImgVitaminas_eval() {
        return imgVitaminas_eval;
    }

    public void setImgVitaminas_eval(String imgVitaminas_eval) {
        this.imgVitaminas_eval = imgVitaminas_eval;
    }

    public String getImgSalud_eval() {
        return imgSalud_eval;
    }

    public void setImgSalud_eval(String imgSalud_eval) {
        this.imgSalud_eval = imgSalud_eval;
    }

    public String getHTMLEnfermedad() {
        return HTMLEnfermedad;
    }

    public void setHTMLEnfermedad(String HTMLEnfermedad) {
        this.HTMLEnfermedad = HTMLEnfermedad;
    }

    public int getPuntosMonje() {
        return puntosMonje;
    }

    public void setPuntosMonje(int puntosMonje) {
        this.puntosMonje = puntosMonje;
    }

    public String getNombreCompuesto() {
        return nombreCompuesto;
    }

    public void setNombreCompuesto(String nombreCompuesto) {
        this.nombreCompuesto = nombreCompuesto;
    }

    public short getNivelAbadia() {
        return nivelAbadia;
    }

    public void setNivelAbadia(short nivelAbadia) {
        this.nivelAbadia = nivelAbadia;
    }
}


