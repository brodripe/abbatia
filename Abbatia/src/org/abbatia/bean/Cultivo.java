package org.abbatia.bean;

public class Cultivo {
    private int idCultivo;
    private int idAbadia;
    private int idEstado;
    private String descEstado;
    private int idAlimento;
    private int idRecurso;
    private int idRegion;
    private String descAlimento;
    private String fechaCreacion;
    private String fechaSiembra;
    private String fechaInicioCultivo;
    private String fechaFinCultivo;
    private String fechaInicioRecogida;
    private String fechaFinRecogida;
    private String produccion;
    private String barras_visualiza;
    private double arado_total;
    private double siembra_total;
    private double cultiva_por_dia;
    private double cultiva_total;
    private double cultiva_acumulado_dia;
    private double recogida_por_dia;
    private double recogida_total;
    private int dias_cultiva;
    private int dias_recogida;
    private int numMonjes;
    private int idIdioma;
    private int nivelCampo;
    private int aguaPorDia;
    private int tempMinima;
    private int tempMaxima;


    public int getIdRegion() {
        return idRegion;
    }

    public void setIdRegion(int idRegion) {
        this.idRegion = idRegion;
    }

    public int getAguaPorDia() {
        return aguaPorDia;
    }

    public void setAguaPorDia(int aguaPorDia) {
        this.aguaPorDia = aguaPorDia;
    }

    public int getTempMinima() {
        return tempMinima;
    }

    public void setTempMinima(int tempMinima) {
        this.tempMinima = tempMinima;
    }

    public int getTempMaxima() {
        return tempMaxima;
    }

    public void setTempMaxima(int tempMaxima) {
        this.tempMaxima = tempMaxima;
    }


    public double getCultiva_acumulado_dia() {
        return cultiva_acumulado_dia;
    }

    public void setCultiva_acumulado_dia(double cultiva_acumulado_dia) {
        this.cultiva_acumulado_dia = cultiva_acumulado_dia;
    }

    public int getNivelCampo() {
        return nivelCampo;
    }

    public void setNivelCampo(int nivelCampo) {
        this.nivelCampo = nivelCampo;
    }

    public int getIdIdioma() {
        return idIdioma;
    }

    public void setIdIdioma(int idIdioma) {
        this.idIdioma = idIdioma;
    }

    public String getFechaInicioCultivo() {
        return fechaInicioCultivo;
    }

    public void setFechaInicioCultivo(String fechaInicioCultivo) {
        this.fechaInicioCultivo = fechaInicioCultivo;
    }

    public String getFechaFinCultivo() {
        return fechaFinCultivo;
    }

    public void setFechaFinCultivo(String fechaFinCultivo) {
        this.fechaFinCultivo = fechaFinCultivo;
    }

    public String getFechaSiembra() {
        return fechaSiembra;
    }

    public void setFechaSiembra(String fechaSiembra) {
        this.fechaSiembra = fechaSiembra;
    }

    public double getArado_total() {
        return arado_total;
    }

    public void setArado_total(double arado_total) {
        this.arado_total = arado_total;
    }

    public int getIdAbadia() {
        return idAbadia;
    }

    public void setIdAbadia(int idAbadia) {
        this.idAbadia = idAbadia;
    }

    public int getNumMonjes() {
        return numMonjes;
    }

    public void setNumMonjes(int numMonjes) {
        this.numMonjes = numMonjes;
    }


    public void setIdCultivo(int idCultivo) {
        this.idCultivo = idCultivo;
    }


    public int getIdCultivo() {
        return idCultivo;
    }


    public void setIdEstado(int idEstado) {
        this.idEstado = idEstado;
    }


    public int getIdEstado() {
        return idEstado;
    }


    public void setDescEstado(String descEstado) {
        this.descEstado = descEstado;
    }


    public String getDescEstado() {
        return descEstado;
    }


    public void setIdAlimento(int idAlimento) {
        this.idAlimento = idAlimento;
    }


    public int getIdAlimento() {
        return idAlimento;
    }


    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }


    public String getFechaCreacion() {
        return fechaCreacion;
    }


    public void setFechaInicioRecogida(String fechaInicioRecogida) {
        this.fechaInicioRecogida = fechaInicioRecogida;
    }


    public String getFechaInicioRecogida() {
        return fechaInicioRecogida;
    }


    public void setFechaFinRecogida(String fechaFinRecogida) {
        this.fechaFinRecogida = fechaFinRecogida;
    }


    public String getFechaFinRecogida() {
        return fechaFinRecogida;
    }


    public void setProduccion(String produccion) {
        this.produccion = produccion;
    }


    public String getProduccion() {
        return produccion;
    }


    public void setDescAlimento(String descAlimento) {
        this.descAlimento = descAlimento;
    }

    public void setIdRecurso(int idRecurso) {
        this.idRecurso = idRecurso;
    }

    public void setBarras_visualiza(String barras_visualiza) {
        this.barras_visualiza = barras_visualiza;
    }

    public void setRecogida_por_dia(double recogida_por_dia) {
        this.recogida_por_dia = recogida_por_dia;
    }

    public void setCultiva_total(double cultiva_total) {
        this.cultiva_total = cultiva_total;
    }

    public void setSiembra_total(double siembra_total) {
        this.siembra_total = siembra_total;
    }

    public void setRecogida_total(double recogida_total) {
        this.recogida_total = recogida_total;
    }

    public void setCultiva_por_dia(double cultiva_por_dia) {
        this.cultiva_por_dia = cultiva_por_dia;
    }


    public void setDias_recogida(int dias_recogida) {
        this.dias_recogida = dias_recogida;
    }

    public void setDias_cultiva(int dias_cultiva) {
        this.dias_cultiva = dias_cultiva;
    }

    public String getDescAlimento() {
        return descAlimento;
    }

    public int getIdRecurso() {
        return idRecurso;
    }

    public String getBarras_visualiza() {
        return barras_visualiza;
    }

    public double getRecogida_por_dia() {
        return recogida_por_dia;
    }

    public double getCultiva_total() {
        return cultiva_total;
    }

    public double getSiembra_total() {
        return siembra_total;
    }

    public double getRecogida_total() {
        return recogida_total;
    }

    public double getCultiva_por_dia() {
        return cultiva_por_dia;
    }

    public int getDias_recogida() {
        return dias_recogida;
    }

    public int getDias_cultiva() {
        return dias_cultiva;
    }

    /*
    edificios.abbatia.campo.titulo=Campos de Cultivo
    edificios.abbatia.campo.fechacreacion=Fecha Creación
    edificios.abbatia.campo.estado=estado
    edificios.abbatia.campo.alimento=Producto sembrado
    edificios.abbatia.campo.fechainiciosiembra=Inicio Siembra
    edificios.abbatia.campo.fechafinsiembra=Inicio Siembra
    edificios.abbatia.campo.fechainiciorecogida=inicio recogida
    edificios.abbatia.campo.fechafinrecogida=inicio recogida
    edificios.abbatia.campo.produccion=Producción potencial
    */
}
