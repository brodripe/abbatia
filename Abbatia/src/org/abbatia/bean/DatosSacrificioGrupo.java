package org.abbatia.bean;

/**
 * Created by IntelliJ IDEA.
 * User: BenjaminHP
 * Date: 26/01/12
 * Time: 21:50
 * To change this template use File | Settings | File Templates.
 */
public class DatosSacrificioGrupo implements Cloneable {
    private String clave;
    private Animal animal;
    private int numeroAnimales;
    private DatosSacrificioAlimento datosAlimento;
    private DatosSacrificioRecurso datosRecurso;

    public DatosSacrificioGrupo() {
    }

    public DatosSacrificioGrupo(DatosSacrificioGrupo p_oDatos) {
        this.clave = p_oDatos.getClave();
        this.animal = p_oDatos.getAnimal();
        this.numeroAnimales = p_oDatos.getNumeroAnimales();
        this.datosAlimento = p_oDatos.getDatosAlimento();
        this.datosRecurso = p_oDatos.getDatosRecurso();
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public int getNumeroAnimales() {
        return numeroAnimales;
    }

    public void setNumeroAnimales(int numeroAnimales) {
        this.numeroAnimales = numeroAnimales;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public DatosSacrificioAlimento getDatosAlimento() {
        return datosAlimento;
    }

    public void setDatosAlimento(DatosSacrificioAlimento datosAlimento) {
        this.datosAlimento = datosAlimento;
    }

    public DatosSacrificioRecurso getDatosRecurso() {
        return datosRecurso;
    }

    public void setDatosRecurso(DatosSacrificioRecurso datosRecurso) {
        this.datosRecurso = datosRecurso;
    }
}
