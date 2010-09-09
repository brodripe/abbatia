package org.abbatia.core;

import org.abbatia.bean.Tiempo;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.AbadiaConfiguracion;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.Utilidades;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.util.MessageResources;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.StringTokenizer;

public class CoreTiempo {
    private static Random r = new Random(System.currentTimeMillis());
    public static String sPathPelo = null;

    public CoreTiempo() {
    }

    //este método tiene que devolver la hora actual de la abadía
    //calculando el paso del tiempo desde el año 0 de la abadía.
    public static GregorianCalendar getTiempoAbadia() throws AbadiaException {
        //recuperamos el año 0 de la abbatia

        //AbadiaConfiguracion abadiaconf = new AbadiaConfiguracion();
        //AbadiaConfiguracion.Init(sPathPelo);
        /**
        any_cero_abadia=1100
        mes_cero_abadia=0
        dia_cero_abadia=1
        any_cero_real=2004
        mes_cero_real=0
        dia_cero_real=1
        ratio_tiempo_abadia=12
         */
        //int iAnyCeroAbadia = 1100; //Integer.valueOf(AbadiaConfiguracion.getPropiedad("any_cero_abadia"));
        int iAnyCeroAbadia = Integer.valueOf(AbadiaConfiguracion.getPropiedad("any_cero_abadia"));
        int iMesCeroAbadia = Integer.valueOf(AbadiaConfiguracion.getPropiedad("mes_cero_abadia"));
        int iDiaCeroAbadia = Integer.valueOf(AbadiaConfiguracion.getPropiedad("dia_cero_abadia"));
        int iAnyCeroReal = Integer.valueOf(AbadiaConfiguracion.getPropiedad("any_cero_real"));
        int iMesCeroReal = Integer.valueOf(AbadiaConfiguracion.getPropiedad("mes_cero_real"));
        int iDiaCeroReal = Integer.valueOf(AbadiaConfiguracion.getPropiedad("dia_cero_real"));
        int iEquivalencia = Integer.valueOf(AbadiaConfiguracion.getPropiedad("ratio_tiempo_abadia"));

        GregorianCalendar tiempoCeroAbadia = new GregorianCalendar(iAnyCeroAbadia, iMesCeroAbadia, iDiaCeroAbadia);
        GregorianCalendar tiempoCeroReal = new GregorianCalendar(iAnyCeroReal, iMesCeroReal, iDiaCeroReal);
        GregorianCalendar tiempoActualReal = new GregorianCalendar();

        long lDiferencia = tiempoActualReal.getTimeInMillis() - tiempoCeroReal.getTimeInMillis();

        lDiferencia = lDiferencia * iEquivalencia;
        int iDiferenciaM = (int) (lDiferencia / 60000);

        tiempoCeroAbadia.add(GregorianCalendar.MINUTE, iDiferenciaM);

        return tiempoCeroAbadia;
    }

    public static String getTiempoAbadiaString() throws AbadiaException {

        GregorianCalendar tiempoCeroAbadia = getTiempoAbadia();

        return tiempoCeroAbadia.get(GregorianCalendar.YEAR) + "-" +
                Utilidades.completarLongitudEnteros(2, tiempoCeroAbadia.get(GregorianCalendar.MONTH) + 1) + "-" +
                Utilidades.completarLongitudEnteros(2, tiempoCeroAbadia.get(GregorianCalendar.DATE));
    }

    /**
     * Devuelve el año en curso de abbatia
     *
     * @return
     * @throws AbadiaException
     */
    public static int getAnoEnCurso() throws AbadiaException {
        GregorianCalendar tiempoCeroAbadia = getTiempoAbadia();
        return tiempoCeroAbadia.get(GregorianCalendar.YEAR);
    }

    // Pasado un gregorian devuelve el string
    public static String getTiempoAbadiaString(GregorianCalendar tiempoCeroAbadia) throws AbadiaException {

        return tiempoCeroAbadia.get(GregorianCalendar.YEAR) + "-" +
                Utilidades.completarLongitudEnteros(2, tiempoCeroAbadia.get(GregorianCalendar.MONTH) + 1) + "-" +
                Utilidades.completarLongitudEnteros(2, tiempoCeroAbadia.get(GregorianCalendar.DATE));
    }


    public static String getTiempoAbadiaStringConHoras() throws AbadiaException {

        GregorianCalendar tiempoCeroAbadia = getTiempoAbadia();

        return tiempoCeroAbadia.get(GregorianCalendar.YEAR) + "-" +
                Utilidades.completarLongitudEnteros(2, tiempoCeroAbadia.get(GregorianCalendar.MONTH) + 1) + "-" +
                Utilidades.completarLongitudEnteros(2, tiempoCeroAbadia.get(GregorianCalendar.DATE)) + " " +
                Utilidades.completarLongitudEnteros(2, tiempoCeroAbadia.get(GregorianCalendar.HOUR_OF_DAY)) + ":" +
                Utilidades.completarLongitudEnteros(2, tiempoCeroAbadia.get(GregorianCalendar.MINUTE));
    }

    // Pasado un gregorian devuelve el string
    public static String getTiempoAbadiaStringConHoras(GregorianCalendar tiempoCeroAbadia) throws AbadiaException {

        return tiempoCeroAbadia.get(GregorianCalendar.YEAR) + "-" +
                Utilidades.completarLongitudEnteros(2, tiempoCeroAbadia.get(GregorianCalendar.MONTH) + 1) + "-" +
                Utilidades.completarLongitudEnteros(2, tiempoCeroAbadia.get(GregorianCalendar.DATE)) + " " +
                Utilidades.completarLongitudEnteros(2, tiempoCeroAbadia.get(GregorianCalendar.HOUR_OF_DAY)) + ":" +
                Utilidades.completarLongitudEnteros(2, tiempoCeroAbadia.get(GregorianCalendar.MINUTE));
    }

    public static String getTiempoRealStringConHoras() throws AbadiaException {

        GregorianCalendar tiempoReal = new GregorianCalendar();

        return tiempoReal.get(GregorianCalendar.YEAR) + "-" +
                Utilidades.completarLongitudEnteros(2, tiempoReal.get(GregorianCalendar.MONTH) + 1) + "-" +
                Utilidades.completarLongitudEnteros(2, tiempoReal.get(GregorianCalendar.DATE)) + " " +
                Utilidades.completarLongitudEnteros(2, tiempoReal.get(GregorianCalendar.HOUR_OF_DAY)) + ":" +
                Utilidades.completarLongitudEnteros(2, tiempoReal.get(GregorianCalendar.MINUTE)) + ":" +
                Utilidades.completarLongitudEnteros(2, tiempoReal.get(GregorianCalendar.SECOND));
    }

    public static String getTiempoRealString() throws AbadiaException {
        GregorianCalendar tiempoReal = new GregorianCalendar();

        return tiempoReal.get(GregorianCalendar.YEAR) + "-" +
                Utilidades.completarLongitudEnteros(2, tiempoReal.get(GregorianCalendar.MONTH) + 1) + "-" +
                Utilidades.completarLongitudEnteros(2, tiempoReal.get(GregorianCalendar.DATE));
    }

    public static GregorianCalendar getTiempoReal() throws AbadiaException {
        return new GregorianCalendar();
    }

    public static String getFechaNacimiento(int edad) throws AbadiaException {
        GregorianCalendar tiempoAbadia = getTiempoAbadia();

        int mes = r.nextInt(11) + 1;
        int dia = r.nextInt(29) + 1;
        tiempoAbadia.add(GregorianCalendar.YEAR, -edad);
        return Utilidades.completarLongitudEnteros(2, dia) + "-" +
                Utilidades.completarLongitudEnteros(2, mes) + "-" +
                tiempoAbadia.get(GregorianCalendar.YEAR);
    }

    public String getFechaNacimientoMes(int edad) throws AbadiaException {
        GregorianCalendar tiempoAbadia = getTiempoAbadia();

        int mes = r.nextInt(11) + 1;
        int dia = r.nextInt(29) + 1;
        tiempoAbadia.add(GregorianCalendar.MONTH, -edad);
        return Utilidades.completarLongitudEnteros(2, dia) + "-" +
                Utilidades.completarLongitudEnteros(2, mes) + "-" +
                tiempoAbadia.get(GregorianCalendar.YEAR);
    }

    public static short getEdad(String FechaNacimiento) throws AbadiaException {
        GregorianCalendar tiempoAbadia = getTiempoAbadia();
        GregorianCalendar fechaNacimiento = Utilidades.formatStringToCalendar(FechaNacimiento);

        return (short) (tiempoAbadia.get(Calendar.YEAR) - fechaNacimiento.get(Calendar.YEAR));
    }

    public static short getDiasTranscurridos(String fecha) throws AbadiaException {
        GregorianCalendar tiempoAbadia = getTiempoAbadia();
        GregorianCalendar fechaNacimiento = Utilidades.formatStringToCalendar(fecha);

        return (short) (tiempoAbadia.get(Calendar.DAY_OF_YEAR) - fechaNacimiento.get(Calendar.DAY_OF_YEAR));
    }

    public static Tiempo getTiempoAbadiaStringConHorasObj(MessageResources resource) throws AbadiaException {

        GregorianCalendar tiempoCeroAbadia = getTiempoAbadia();
        Tiempo time = new Tiempo();
        time.setYear(String.valueOf(tiempoCeroAbadia.get(GregorianCalendar.YEAR)));
        time.setMes(Utilidades.completarLongitudEnteros(2, tiempoCeroAbadia.get(GregorianCalendar.MONTH)));
        time.setDia(Utilidades.completarLongitudEnteros(2, tiempoCeroAbadia.get(GregorianCalendar.DATE)));
        time.setHora(Utilidades.completarLongitudEnteros(2, tiempoCeroAbadia.get(GregorianCalendar.HOUR_OF_DAY)));
        time.setMinuto(Utilidades.completarLongitudEnteros(2, tiempoCeroAbadia.get(GregorianCalendar.MINUTE)));
        int day = tiempoCeroAbadia.get(GregorianCalendar.DAY_OF_WEEK);

        //faltan los ajustes de multiidioma...
//    if (day == Calendar.MONDAY) time.setDiaSemana(resource.getMessage("general.lunes"));
//    if (day == Calendar.TUESDAY) time.setDiaSemana(resource.getMessage("general.martes"));
//    if (day == Calendar.WEDNESDAY) time.setDiaSemana(resource.getMessage("general.miercoles"));
//    if (day == Calendar.THURSDAY) time.setDiaSemana(resource.getMessage("general.jueves"));
//    if (day == Calendar.FRIDAY) time.setDiaSemana(resource.getMessage("general.viernes"));
//    if (day == Calendar.SATURDAY) time.setDiaSemana(resource.getMessage("general.sabado"));
//    if (day == Calendar.SUNDAY) time.setDiaSemana(resource.getMessage("general.domingo"));
        if (day == Calendar.MONDAY) time.setDiaSemana("general.lunes");
        if (day == Calendar.TUESDAY) time.setDiaSemana("general.martes");
        if (day == Calendar.WEDNESDAY) time.setDiaSemana("general.miercoles");
        if (day == Calendar.THURSDAY) time.setDiaSemana("general.jueves");
        if (day == Calendar.FRIDAY) time.setDiaSemana("general.viernes");
        if (day == Calendar.SATURDAY) time.setDiaSemana("general.sabado");
        if (day == Calendar.SUNDAY) time.setDiaSemana("general.domingo");

        return time;
    }

    public static String getDiferenciaMesString(int iMes) throws AbadiaException {
        int dia = 0;
        int diahoy = 0;
        GregorianCalendar tiempoAbadia = getTiempoAbadia();

        //si el mes es 0, se calcula como maximo el dia de hoy.
        if (iMes == 0) {
            diahoy = tiempoAbadia.get(GregorianCalendar.DATE);
            dia = r.nextInt(diahoy);
        } else {
            dia = r.nextInt(29) + 1;
        }

        tiempoAbadia.add(GregorianCalendar.MONTH, iMes);
        return tiempoAbadia.get(GregorianCalendar.YEAR) + "-" +
                Utilidades.completarLongitudEnteros(2, tiempoAbadia.get(GregorianCalendar.MONTH) + 1) + "-" +
                Utilidades.completarLongitudEnteros(2, dia);


    }

    public static String getDiferenciaString(int iDias) throws AbadiaException {
        GregorianCalendar tiempoAbadia = getTiempoAbadia();
        tiempoAbadia.add(GregorianCalendar.DATE, iDias);
        return tiempoAbadia.get(GregorianCalendar.YEAR) + "-" +
                Utilidades.completarLongitudEnteros(2, tiempoAbadia.get(GregorianCalendar.MONTH) + 1) + "-" +
                Utilidades.completarLongitudEnteros(2, tiempoAbadia.get(GregorianCalendar.DATE));


    }

    public static String getDiferenciaStringConHoras(int iDias) throws AbadiaException {
        GregorianCalendar tiempoAbadia = getTiempoAbadia();
        tiempoAbadia.add(GregorianCalendar.DATE, iDias);
        return tiempoAbadia.get(GregorianCalendar.YEAR) + "-" +
                Utilidades.completarLongitudEnteros(2, tiempoAbadia.get(GregorianCalendar.MONTH) + 1) + "-" +
                Utilidades.completarLongitudEnteros(2, tiempoAbadia.get(GregorianCalendar.DATE)) + " " +
                Utilidades.completarLongitudEnteros(2, tiempoAbadia.get(GregorianCalendar.HOUR_OF_DAY)) + ":" +
                Utilidades.completarLongitudEnteros(2, tiempoAbadia.get(GregorianCalendar.MINUTE));


    }

    // Devuelve el periodo actual
    public static int getPeriodoActual() throws AbadiaException {
        int periodoid = Constantes.PERIODO_NADA;
        int hora;
        int minutos;
        // averiguar la hora abbatia
        GregorianCalendar horaAbadia = new GregorianCalendar();
        horaAbadia = CoreTiempo.getTiempoAbadia();
        hora = horaAbadia.get(GregorianCalendar.HOUR_OF_DAY);
        minutos = horaAbadia.get(GregorianCalendar.MINUTE);
        // Maitines
        if (((hora == 0) && (minutos >= 15)) &&
                ((hora == 0) && (minutos <= 45)))
            periodoid = Constantes.PERIODO_MAITINES;
        // Laudes
        if (((hora == 2) && (minutos >= 15)) &&
                ((hora == 2) && (minutos <= 45)))
            periodoid = Constantes.PERIODO_LAUDES;
        // Prima
        if (((hora >= 7) && (minutos >= 00)) &&
                ((hora <= 9) && (minutos <= 59)))
            periodoid = Constantes.PERIODO_PRIMA;
        // Tercia
        if (((hora >= 10) && (minutos >= 00)) &&
                ((hora <= 11) && (minutos <= 59)))
            periodoid = Constantes.PERIODO_TERCIA;
        // Sexta
        if (((hora >= 12) && (minutos >= 00)) &&
                ((hora <= 13) && (minutos <= 59)))
            periodoid = Constantes.PERIODO_SEXTA;
        // Nona
        if (((hora >= 14) && (minutos >= 00)) &&
                ((hora <= 15) && (minutos <= 59)))
            periodoid = Constantes.PERIODO_NONA;
        // Visperas
        if (((hora >= 16) && (minutos >= 00)) &&
                ((hora <= 18) && (minutos <= 59)))
            periodoid = Constantes.PERIODO_VISPERAS;
        // Angelus
        if (((hora == 19) && (minutos >= 45)) &&
                ((hora == 20) && (minutos <= 15)))
            periodoid = Constantes.PERIODO_ANGELUS;
        return periodoid;
    }


    public static String getDiferenciaDias(String strDateIni, String strDateFin) {
        //Comparar las 2 fechas.
        //Si strDate2 > strDate1, devolver "02"
        //Si strDate2 < strDate1, devolver "03"
        //Si strDate2 == strDate1, devolver "00"

        String resultado = Constantes.ERROR;
        int year1 = 0;
        int month1 = 0;
        int day1 = 0;

        int year2 = 0;
        int month2 = 0;
        int day2 = 0;

        String sFecha1 = strDateIni;
        String sFecha2 = strDateFin;

        // Eliminar la hora pa que no moleste
        if (sFecha1.indexOf(":") != -1) sFecha1 = sFecha1.substring(1, sFecha1.indexOf(" "));
        if (sFecha2.indexOf(":") != -1) sFecha2 = sFecha2.substring(1, sFecha2.indexOf(" "));

        //Pasar la primera fecha de tipo String a tipo Date para luego comparar con la segunda
        //strDate1 tiene formato "dd-MM-yyyy"
        StringTokenizer stToken1 = new StringTokenizer(sFecha1, "-");

        if (stToken1.hasMoreTokens()) {
            //Coger las particiones de una a una
            year1 = Integer.parseInt(stToken1.nextToken());
            month1 = Integer.parseInt(stToken1.nextToken());
            day1 = Integer.parseInt(stToken1.nextToken());
        }

        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(year1, month1 - 1, day1);

        //Pasar la segunda fecha de tipo String a tipo Date para luego comparar con la primera
        //strDate2 tiene formato "dd-MM-yyyy"
        StringTokenizer stToken2 = new StringTokenizer(sFecha2, "-");

        if (stToken2.hasMoreTokens()) {
            //Coger las particiones de una a una
            year2 = Integer.parseInt(stToken2.nextToken());
            month2 = Integer.parseInt(stToken2.nextToken());
            day2 = Integer.parseInt(stToken2.nextToken());
        }

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(year2, month2 - 1, day2);

        long dif = calendar2.getTimeInMillis() - calendar1.getTimeInMillis();
        int difM = (int) (dif / 86400000);

        //Comparar las fechas de tipo Date
        resultado = String.valueOf(difM);
        return resultado;
    }

    public static int getDiferenciaDiasInt(String strDateIni, String strDateFin) {
        //Comparar las 2 fechas.

        int year1 = 0;
        int month1 = 0;
        int day1 = 0;

        int year2 = 0;
        int month2 = 0;
        int day2 = 0;

        String sFecha1 = strDateIni;
        String sFecha2 = strDateFin;

        // Eliminar la hora pa que no moleste
        if (sFecha1.indexOf(":") != -1) sFecha1 = sFecha1.substring(1, sFecha1.indexOf(" "));
        if (sFecha2.indexOf(":") != -1) sFecha2 = sFecha2.substring(1, sFecha2.indexOf(" "));

        //Pasar la primera fecha de tipo String a tipo Date para luego comparar con la segunda
        //strDate1 tiene formato "dd-MM-yyyy"
        StringTokenizer stToken1 = new StringTokenizer(sFecha1, "-");

        if (stToken1.hasMoreTokens()) {
            //Coger las particiones de una a una
            year1 = Integer.parseInt(stToken1.nextToken());
            month1 = Integer.parseInt(stToken1.nextToken());
            day1 = Integer.parseInt(stToken1.nextToken());
        }

        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(year1, month1 - 1, day1);

        //Pasar la segunda fecha de tipo String a tipo Date para luego comparar con la primera
        //strDate2 tiene formato "dd-MM-yyyy"
        StringTokenizer stToken2 = new StringTokenizer(sFecha2, "-");

        if (stToken2.hasMoreTokens()) {
            //Coger las particiones de una a una
            year2 = Integer.parseInt(stToken2.nextToken());
            month2 = Integer.parseInt(stToken2.nextToken());
            day2 = Integer.parseInt(stToken2.nextToken());
        }

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(year2, month2 - 1, day2);

        long dif = calendar2.getTimeInMillis() - calendar1.getTimeInMillis();

        //Comparar las fechas de tipo Date
        return (int) (dif / 86400000);
    }

    public static String sumarDias(String strDateIni, int iDias) {
        int year1 = 0;
        int month1 = 0;
        int day1 = 0;

        String sFecha1 = strDateIni;

        // Eliminar la hora pa que no moleste
        if (sFecha1.indexOf(":") != -1) sFecha1 = sFecha1.substring(1, sFecha1.indexOf(" "));

        //Pasar la primera fecha de tipo String a tipo Date para luego comparar con la segunda
        //strDate1 tiene formato "dd-MM-yyyy"
        StringTokenizer stToken1 = new StringTokenizer(sFecha1, "-");

        if (stToken1.hasMoreTokens()) {
            //Coger las particiones de una a una
            year1 = Integer.parseInt(stToken1.nextToken());
            month1 = Integer.parseInt(stToken1.nextToken());
            day1 = Integer.parseInt(stToken1.nextToken());
        }

        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(year1, month1 - 1, day1);

        calendar1.add(GregorianCalendar.DATE, iDias);
        return calendar1.get(GregorianCalendar.YEAR) + "-" +
                Utilidades.completarLongitudEnteros(2, calendar1.get(GregorianCalendar.MONTH) + 1) + "-" +
                Utilidades.completarLongitudEnteros(2, calendar1.get(GregorianCalendar.DATE));

    }


    //Método que devuelva si una fecha en un formato dado es correcta
    public static boolean bFormatoFechaCorrecto(String strFecha, String strFormato) {
        boolean bEsFechaCorrecta = false;
        bEsFechaCorrecta = GenericValidator.isDate(strFecha, strFormato, true);
        return bEsFechaCorrecta;
    }

    public static boolean esNoche() throws AbadiaException {

        GregorianCalendar tiempoCeroAbadia = getTiempoAbadia();

        return (tiempoCeroAbadia.get(GregorianCalendar.HOUR_OF_DAY) >= 18) || (tiempoCeroAbadia.get(GregorianCalendar.HOUR_OF_DAY) <= 8);

    }

}
