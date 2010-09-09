package org.abbatia.bean;

/**
 * Created by Benjamín Rodríguez.
 * User: benjamin
 * Date: 02-feb-2009
 * Time: 22:56:02
 */
public class AlimentoFamiliaProceso {
    private int loteId;
    private short familiaId;
    private int alimentoId;
    private float cantidadLote;
    private float consumoMonje;
    private int proteinas;
    private int lipidos;
    private int hidratosCarbono;
    private int vitaminas;

    public int getLoteId() {
        return loteId;
    }

    public short getFamiliaId() {
        return familiaId;
    }

    public void setFamiliaId(short familiaId) {
        this.familiaId = familiaId;
    }

    public float getCantidadLote() {
        return cantidadLote;
    }

    public void setCantidadLote(float cantidadLote) {
        this.cantidadLote = cantidadLote;
    }

    public void setLoteId(int loteId) {
        this.loteId = loteId;
    }

    public float getConsumoMonje() {
        return consumoMonje;
    }

    public void setConsumoMonje(float consumoMonje) {
        this.consumoMonje = consumoMonje;
    }

    public int getProteinas() {
        return proteinas;
    }

    public void setProteinas(int proteinas) {
        this.proteinas = proteinas;
    }

    public int getLipidos() {
        return lipidos;
    }

    public void setLipidos(int lipidos) {
        this.lipidos = lipidos;
    }

    public int getHidratosCarbono() {
        return hidratosCarbono;
    }

    public void setHidratosCarbono(int hidratosCarbono) {
        this.hidratosCarbono = hidratosCarbono;
    }

    public int getVitaminas() {
        return vitaminas;
    }

    public void setVitaminas(int vitaminas) {
        this.vitaminas = vitaminas;
    }

    public int getAlimentoId() {
        return alimentoId;
    }

    public void setAlimentoId(int alimentoId) {
        this.alimentoId = alimentoId;
    }
}
