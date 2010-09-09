package org.abbatia.bean;

/**
 * Created by Benjamín Rodríguez.
 * User: benjamin
 * Date: 16-may-2009
 * Time: 0:40:15
 */
public class ConsumoAlimentosFamilia {
    private int abadiaId;
    private int familiaId;
    private int alimentoId;
    private double requerido;
    private String requeridoStr;
    private String alimentoDesc;
    private String familiaDesc;
    private double disponible;
    private String disponibleStr;
    private double diferencia;
    private String diferenciaStr;

    public int getAbadiaId() {
        return abadiaId;
    }

    public void setAbadiaId(int abadiaId) {
        this.abadiaId = abadiaId;
    }

    public int getFamiliaId() {
        return familiaId;
    }

    public void setFamiliaId(int familiaId) {
        this.familiaId = familiaId;
    }

    public int getAlimentoId() {
        return alimentoId;
    }

    public void setAlimentoId(int alimentoId) {
        this.alimentoId = alimentoId;
    }

    public double getRequerido() {
        return requerido;
    }

    public void setRequerido(double requerido) {
        this.requerido = requerido;
    }

    public String getAlimentoDesc() {
        return alimentoDesc;
    }

    public void setAlimentoDesc(String alimentoDesc) {
        this.alimentoDesc = alimentoDesc;
    }

    public String getFamiliaDesc() {
        return familiaDesc;
    }

    public void setFamiliaDesc(String familiaDesc) {
        this.familiaDesc = familiaDesc;
    }

    public double getDisponible() {
        return disponible;
    }

    public void setDisponible(double disponible) {
        this.disponible = disponible;
    }

    public double getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(double diferencia) {
        this.diferencia = diferencia;
    }

    public String getRequeridoStr() {
        return requeridoStr;
    }

    public void setRequeridoStr(String requeridoStr) {
        this.requeridoStr = requeridoStr;
    }

    public String getDisponibleStr() {
        return disponibleStr;
    }

    public void setDisponibleStr(String disponibleStr) {
        this.disponibleStr = disponibleStr;
    }

    public String getDiferenciaStr() {
        return diferenciaStr;
    }

    public void setDiferenciaStr(String diferenciaStr) {
        this.diferenciaStr = diferenciaStr;
    }
}
