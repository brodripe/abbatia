/***********************************************************************
 * Module:  Parametros.java
 * Author:  Benja & John
 * Purpose: Defines the Class Parametros
 ***********************************************************************/
package org.abbatia.bean;

public class Parametros {
    private long relacionTemporal;
    private short idealProteinas;
    private short idealLipidos;
    private short idealHidratosCarbono;
    private short idealVitaminas;

    // --------------------------------------------------------------------------
    // Relación Temporal
    // --------------------------------------------------------------------------
    public void setRelacionTemporal(long newRelacionTemporal) {
        this.relacionTemporal = newRelacionTemporal;
    }

    public long getRelacionTemporal() {
        return this.relacionTemporal;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Ideal Proteínas
    // --------------------------------------------------------------------------

    public void setIdealProteinas(short newIdealProteinas) {
        this.idealProteinas = newIdealProteinas;
    }

    public short getIdealProteinas() {
        return this.idealProteinas;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Ideal Lípidos
    // --------------------------------------------------------------------------

    public void setIdealLipidos(short newIdealLipidos) {
        this.idealLipidos = newIdealLipidos;
    }

    public short getIdealLipidos() {
        return this.idealLipidos;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Ideal Hidratos Carbono
    // --------------------------------------------------------------------------

    public void setIdealHidratosCarbono(short newIdealHidratosCarbono) {
        this.idealHidratosCarbono = newIdealHidratosCarbono;
    }

    public short getIdealHidratosCarbono() {
        return this.idealHidratosCarbono;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Ideal Vitaminas
    // --------------------------------------------------------------------------

    public void setIdealVitaminas(short newIdealVitaminas) {
        this.idealVitaminas = newIdealVitaminas;
    }

    public short getIdealVitaminas() {
        return this.idealVitaminas;
    }
    // --------------------------------------------------------------------------


}


