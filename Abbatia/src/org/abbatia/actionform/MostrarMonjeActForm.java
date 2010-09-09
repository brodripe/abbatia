package org.abbatia.actionform;

import org.apache.struts.action.ActionForm;

import java.util.ArrayList;

public class MostrarMonjeActForm extends ActionForm {
    private String accion;
    private int idMonje = 0;
    private String nombre = "";       //nombre del monje
    private String apellido1 = "";
    private String apellido2 = "";
    private String cabadia = "";      //codigo de abbatia
    private String fnacimiento = "";  //fecha de nacimiento
    private String fentrada = "";     //fecha de entrada
    private String ccargo = "";       //código de cargo
    private String cespecialidad = "";//codigo de especializacion
    private int siguienteID;
    private int anteriorID;

    // Actividades del monje
    private short actMaitines;
    private short actMaitines_bloqueado;
    private String actMaitines_link;

    private short actLaudes;
    private short actLaudes_bloqueado;
    private String actLaudes_link;

    private short actPrima;
    private short actPrima_bloqueado;
    private String actPrima_link;

    private short actTercia;
    private short actTercia_bloqueado;
    private String actTercia_link;

    private short actSexta;
    private short actSexta_bloqueado;
    private String actSexta_link;

    private short actNona;
    private short actNona_bloqueado;
    private String actNona_link;

    private short actVispera;
    private short actVispera_bloqueado;
    private String actVispera_link;

    private short actAngelus;
    private short actAngelus_bloqueado;
    private String actAngelus_link;


    // Alimentación del monje
    private short comeFamiliaID1;
    private short comeFamiliaID2;
    private short comeFamiliaID3;
    private short comeFamiliaID4;
    private short comeFamiliaID5;

    private short comeFamiliaID5_eval;

    private short proteinas;
    private short lipidos;
    private short hidratosCarbono;
    private short vitaminas;


    /*
    private short proteinas_eval;
    private short lipidos_eval;
    private short hidratosCarbono_eval;
    private short vitaminas_eval;
    */
    private short salud;
    private short edad;

    private ArrayList habilidades;    //tabla de habilidades

    public int getIdMonje() {
        return idMonje;
    }

    public void setIdMonje(int idMonje) {
        this.idMonje = idMonje;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public String getNombre() {
        return nombre;
    }


    public void setCabadia(String cabadia) {
        this.cabadia = cabadia;
    }


    public String getCabadia() {
        return cabadia;
    }


    public void setFnacimiento(String fnacimiento) {
        this.fnacimiento = fnacimiento;
    }


    public String getFnacimiento() {
        return fnacimiento;
    }


    public void setFentrada(String fentrada) {
        this.fentrada = fentrada;
    }


    public String getFentrada() {
        return fentrada;
    }

    public void setCcargo(String ccargo) {
        this.ccargo = ccargo;
    }


    public String getCcargo() {
        return ccargo;
    }


    public void setCespecialidad(String cespecialidad) {
        this.cespecialidad = cespecialidad;
    }


    public String getCespecialidad() {
        return cespecialidad;
    }


    public void setHabilidades(ArrayList habilidades) {
        this.habilidades = habilidades;
    }


    public ArrayList getHabilidades() {
        return habilidades;
    }


    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }


    public String getApellido1() {
        return apellido1;
    }


    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public void setComeFamiliaID5(short comeFamiliaID5) {
        this.comeFamiliaID5 = comeFamiliaID5;
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

    public void setVitaminas(short vitaminas) {
        this.vitaminas = vitaminas;
    }

    public void setSalud(short salud) {
        this.salud = salud;
    }

    public void setComeFamiliaID1(short comeFamiliaID1) {
        this.comeFamiliaID1 = comeFamiliaID1;
    }

    public void setLipidos(short lipidos) {
        this.lipidos = lipidos;
    }

    public void setHidratosCarbono(short hidratosCarbono) {
        this.hidratosCarbono = hidratosCarbono;
    }

    public void setProteinas(short proteinas) {
        this.proteinas = proteinas;
    }

    public String getApellido2() {
        return apellido2;
    }

    public short getComeFamiliaID5() {
        return comeFamiliaID5;
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

    public short getVitaminas() {
        return vitaminas;
    }

    public short getSalud() {
        return salud;
    }

    public short getComeFamiliaID1() {
        return comeFamiliaID1;
    }

    public short getLipidos() {
        return lipidos;
    }

    public short getHidratosCarbono() {
        return hidratosCarbono;
    }

    public short getProteinas() {
        return proteinas;
    }


    public void setEdad(short edad) {
        this.edad = edad;
    }


    public short getEdad() {
        return edad;
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

    public void setActMaitines(short actMaitines) {
        this.actMaitines = actMaitines;
    }

    public void setActAngelus(short actAngelus) {
        this.actAngelus = actAngelus;
    }

    public void setActLaudes(short actLaudes) {
        this.actLaudes = actLaudes;
    }

    public void setActSexta(short actSexta) {
        this.actSexta = actSexta;
    }

    public short getActVispera() {
        return actVispera;
    }

    public short getActMaitines() {
        return actMaitines;
    }

    public short getActAngelus() {
        return actAngelus;
    }

    public short getActLaudes() {
        return actLaudes;
    }

    public short getActSexta() {
        return actSexta;
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


    public int getAnteriorID() {
        return anteriorID;
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

    public short getComeFamiliaID5_eval() {
        return comeFamiliaID5_eval;
    }

    public void setComeFamiliaID5_eval(short comeFamiliaID5_eval) {
        this.comeFamiliaID5_eval = comeFamiliaID5_eval;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }
}
