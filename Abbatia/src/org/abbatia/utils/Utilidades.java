package org.abbatia.utils;

import org.abbatia.bbean.GestionDblClickBBean;
import org.abbatia.bean.Table;
import org.abbatia.core.CoreTiempo;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.exception.pwdCortaException;
import org.abbatia.exception.pwdSinNumerosException;
import static org.abbatia.utils.Constantes.*;
import org.apache.log4j.Logger;

import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.*;

public class Utilidades {
    private static Logger log = Logger.getLogger(Utilidades.class.getName());
    public static Random r = new Random();

    public static void main(String[] argv) {

        System.out.println(formatStringToDouble("1000"));


    }

    public static double redondear(double nD, int nDec)
    {
      return Math.round(nD*Math.pow(10,nDec))/Math.pow(10,nDec);
    }

    public static String redondear(double valor) {
        DecimalFormat nf = new DecimalFormat();

        nf.setMaximumFractionDigits(2);
        String svalor = nf.format(valor);
        return svalor;
    }

    public static String redondear(long valor) {
        DecimalFormat nf = new DecimalFormat();

        nf.setMaximumFractionDigits(2);
        String svalor = nf.format(valor);
        return svalor;
    }

    public static double formatStringToDouble(String valor) {

        DecimalFormat nf = new DecimalFormat("#,###.##");
        ParsePosition pp = new ParsePosition(0);

        Number n = nf.parse(valor, pp);
        if (n == null) {
            return 0;
        } else return n.doubleValue();

    }

    public static String formatStringToDB(String strDate) {
        //Pasar strDate a formato "yyyy-MM-dd"
        String strDateBD = "";
        String year = null;
        String month = null;
        String day = null;


        if (CoreTiempo.bFormatoFechaCorrecto(strDate, Constantes.FORMATO_FECHA_PANTALLA)) {
            StringTokenizer stToken = new StringTokenizer(strDate, "-");
            //strDate tiene formato "dd-MM-yyyy"

            if (stToken.hasMoreTokens()) {
                //Coger las particiones de una a una
                day = stToken.nextToken();
                month = stToken.nextToken();
                year = stToken.nextToken();

                strDateBD = year + "-" + month + "-" + day;
            }

            return strDateBD;
        } else if (CoreTiempo.bFormatoFechaCorrecto(strDate, Constantes.FORMATO_FECHA_BD)) {
            //strDate tiene formato "yyyy-MM-dd"

            return strDate;
        } else {
            return Constantes.FEC_DEFECTO;
        }

    }

    public static String formatStringFromDB(String strDateDB) {
        //Paso strDateDB a formato "dd-MM-yyyy"
        String strDate = "";
        String year = null;
        String month = null;
        String day = null;
        String sHoraMin = "";
        int iSeparator;

        if (strDateDB == null) return "00-00-0000";
        if (!strDateDB.equals(Constantes.FEC_DEFECTO)) {

            if (strDateDB.indexOf(" ") > 0) {
                iSeparator = strDateDB.indexOf(" ");
                sHoraMin = strDateDB.substring(strDateDB.indexOf(" "), strDateDB.indexOf(" ") + 6);
                strDateDB = strDateDB.substring(0, iSeparator);
            }

            StringTokenizer stToken = new StringTokenizer(strDateDB, "-");
            //strDateDB tiene formato "yyyy-MM-dd"

            if (stToken.hasMoreTokens()) {
                //Coger las particiones de una a una
                year = stToken.nextToken();
                month = stToken.nextToken();
                day = stToken.nextToken();

                strDate = day + "-" + month + "-" + year;
            }
            //si la fecha contiene también hora y minutos...
            strDate = strDate.concat(sHoraMin);
            return strDate;
        } else {
            return "";
        }
    }

    // Format a string
    public static String Format(String texto, String replace) {
        int pos = texto.indexOf("%s");
        if (pos > -1) {
            texto = texto.substring(0, pos) + replace + texto.substring(pos + 2);
        }
        return texto;
    }


    // Format a int
    public static String Format(String texto, int replace) {
        int pos = texto.indexOf("%d");
        if (pos > -1) {
            texto = texto.substring(0, pos) + replace + texto.substring(pos + 2);
        }
        return texto;
    }

    // Format a float
    public static String Format(String texto, float replace) {
        int pos = texto.indexOf("%f");
        if (pos > -1) {
            texto = texto.substring(0, pos) + Utilidades.redondear(replace) + texto.substring(pos + 2);
        }
        return texto;
    }

    // Format a double
    public static String Format(String texto, double replace) {
        int pos = texto.indexOf("%f");
        if (pos > -1) {
            texto = texto.substring(0, pos) + Utilidades.redondear(replace) + texto.substring(pos + 2);
        }
        return texto;
    }

    public static String compararStrDate(String strDate1, String strDate2) {
        //Comparar las 2 fechas.
        //Si strDate2 > strDate1, devolver "02"
        //Si strDate2 < strDate1, devolver "03"
        //Si strDate2 == strDate1, devolver "00"

        String resultado = Constantes.ERROR;
        //loge.debug("LOGJBP Utilidades compararStrDate() strDate1= "+strDate1);
        //loge.debug("LOGJBP Utilidades compararStrDate() strDate2= "+strDate2);
        int year1 = 0;
        int month1 = 0;
        int day1 = 0;

        int year2 = 0;
        int month2 = 0;
        int day2 = 0;


        if ((CoreTiempo.bFormatoFechaCorrecto(strDate1, Constantes.FORMATO_FECHA_PANTALLA)) &&
                (CoreTiempo.bFormatoFechaCorrecto(strDate2, Constantes.FORMATO_FECHA_PANTALLA))) {
            //Pasar la primera fecha de tipo String a tipo Date para luego comparar con la segunda
            //strDate1 tiene formato "dd-MM-yyyy"
            StringTokenizer stToken1 = new StringTokenizer(strDate1, "-");

            if (stToken1.hasMoreTokens()) {
                //Coger las particiones de una a una
                day1 = Integer.parseInt(stToken1.nextToken());
                month1 = Integer.parseInt(stToken1.nextToken());
                year1 = Integer.parseInt(stToken1.nextToken());
            }

            Calendar calendar1 = Calendar.getInstance();
            calendar1.set(year1, month1 - 1, day1);
            Date date1 = calendar1.getTime();

            //Pasar la segunda fecha de tipo String a tipo Date para luego comparar con la primera
            //strDate2 tiene formato "dd-MM-yyyy"
            StringTokenizer stToken2 = new StringTokenizer(strDate2, "-");

            if (stToken2.hasMoreTokens()) {
                //Coger las particiones de una a una
                day2 = Integer.parseInt(stToken2.nextToken());
                month2 = Integer.parseInt(stToken2.nextToken());
                year2 = Integer.parseInt(stToken2.nextToken());
            }

            Calendar calendar2 = Calendar.getInstance();
            calendar1.set(year2, month2 - 1, day2);
            Date date2 = calendar1.getTime();

            //Comparar las fechas de tipo Date
            int iResultado = date1.compareTo(date2);

            if (iResultado > 0) {
                resultado = Constantes.FECHA_MENOR;
            } else if (iResultado < 0) {
                resultado = Constantes.FECHA_MAYOR;
            } else {
                resultado = Constantes.MISMA_FECHA;
            }

            /*if (date2.after(date1)) {
            resultado = Constantes.OK;
            }
            else {
            resultado = Constantes.FALSO;
            }*/
        } else {
            resultado = Constantes.ERROR;
        }
        //loge.debug("LOGJBP Utilidades compararStrDate() resultado= "+resultado);
        return resultado;
    }

    public static String compararStrDateTime(String strDateTime1, String strDateTime2) {
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

        String sFecha1 = strDateTime1.substring(0, strDateTime1.indexOf(" "));
        String sHora1 = strDateTime1.substring(strDateTime1.indexOf(" ") + 1);
        int iHor1 = Integer.valueOf(sHora1.substring(0, 2)).intValue();
        int iMin1 = Integer.valueOf(sHora1.substring(3, 5)).intValue();
        int iSec1 = Integer.valueOf(sHora1.substring(6, 8)).intValue();


        String sFecha2 = strDateTime2.substring(0, strDateTime2.indexOf(" "));
        String sHora2 = strDateTime2.substring(strDateTime2.indexOf(" ") + 1);
        int iHor2 = Integer.valueOf(sHora2.substring(0, 2)).intValue();
        int iMin2 = Integer.valueOf(sHora2.substring(3, 5)).intValue();
        int iSec2 = Integer.valueOf(sHora2.substring(6, 8)).intValue();

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
        calendar1.set(year1, month1 - 1, day1, iHor1, iMin1, iSec1);
        Date date1 = calendar1.getTime();

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
        calendar1.set(year2, month2 - 1, day2, iHor2, iMin2, iSec2);
        Date date2 = calendar1.getTime();

        //Comparar las fechas de tipo Date
        int iResultado = date1.compareTo(date2);

        if (iResultado > 0) {
            resultado = Constantes.FECHA_MENOR;
        } else if (iResultado < 0) {
            resultado = Constantes.FECHA_MAYOR;
        } else {
            resultado = Constantes.MISMA_FECHA;
        }

        /*if (date2.after(date1)) {
        resultado = Constantes.OK;
        }
        else {
        resultado = Constantes.FALSO;
        }*/
        //loge.debug("LOGJBP Utilidades compararStrDate() resultado= "+resultado);
        return resultado;

    }

    //Metodo que añade ceros al incio de un String
    public static String completarLongitudEnteros(int iLongMax, int iValor) {


        String cero = "0";
        String strValorRdo = new Integer(iValor).toString();

        while (strValorRdo.length() < iLongMax) {

            strValorRdo = "0" + strValorRdo;

        }
        return strValorRdo;

    }

    public static GregorianCalendar formatStringToCalendar(String strDateDB) {
        String strDate = "";
        String year = null;
        String month = null;
        String day = null;

        StringTokenizer stToken = new StringTokenizer(strDateDB, "-");

        if (stToken.hasMoreTokens()) {
            //Coger las particiones de una a una
            year = stToken.nextToken();
            month = stToken.nextToken();
            day = stToken.nextToken();
        }
        //si la fecha contiene también hora y minutos...
        GregorianCalendar gFecha = new GregorianCalendar(Integer.valueOf(year).intValue(), Integer.valueOf(month).intValue(), Integer.valueOf(day).intValue());
        return gFecha;
    }

    public static synchronized void incrementarContador() {
        Integer contador = null;
        InitialContext ic = null;
        try {
            log.debug("Utilidades. incrementarContador. Incremento el contador de usuarios");
            ic = new InitialContext();
            contador = (Integer) ic.lookup("contador_usuarios");
            ic.rebind("contador_usuarios", new Integer(contador.intValue() + 1));
        } catch (NameNotFoundException e) {
            contador = new Integer(1);
            try {
                ic.rebind("contador_usuarios", contador);
            } catch (NamingException e1) {
            }

        } catch (NamingException e) {
            log.error("Utilidades. incrementarContador. error NamingException.", e);
        }

    }

    public static synchronized void decrementarContador() {
        log.debug("Utilidades. decrementarContador. Decremento el contador de usuarios");
        try {
            InitialContext ic = new InitialContext();
            Integer contador = (Integer) ic.lookup("contador_usuarios");
            if (contador != null) {
                ic.rebind("contador_usuarios", new Integer(contador.intValue() - 1));
            }
        } catch (NamingException e) {
            log.error("Utilidades. decrementarContador. error NamingException.", e);
        }

    }

    public static synchronized Integer getUsuariosConectados() {
        try {
            InitialContext ic = new InitialContext();
            Integer contador = (Integer) ic.lookup("contador_usuarios");
            if (contador.intValue() < 1) {
                contador = new Integer(1);
            }
            return contador;
        } catch (NamingException e) {
            log.debug("Utilidades. getUsuariosConectados. error NamingException.");
            return new Integer(0);
        }

    }

    public static double ObtenerMedia(double min, double max) {
        double diff = max - min;
        if (diff <= 0) diff = 1;
        double media = min + r.nextInt((int) diff);
        return media;
    }

    public static String normalizarTexto(String texto) {
        int posicion = texto.indexOf("'");
        if (posicion > 0) {
            texto = texto.replace('\'', '`');
        }
        return texto;
    }

    public static String generaContrasena() {
        try {
            StringBuffer pswd = new StringBuffer();
            Random rnd = new Random((new Date()).getTime());
            int kk;
            do {
                kk = Math.abs(rnd.nextInt()) % 43 + 48;
                if (kk < 58 || kk > 64) pswd.append((char) kk);
            } while (pswd.length() < 8);

            return pswd.toString();
        } catch (Exception ex) {
            return "XXXXXXXX";
        }

    }

    public static double RandomDouble(double min, double max) {
        double diff = max - min;

        if ((int) diff <= 0) diff = 1;
        double media = min + r.nextDouble();
        return media;
    }

    public static double Random(double min, double max) {
        double diff = max - min;

        if ((int) diff <= 0) diff = 1;
        double media = min + r.nextInt((int) diff);
        return media;
    }

    /* Devuelve un numero entre Min y Max */
    public static int Random(int min, int max) {
        int diff = max - min;
        if (diff <= 0) diff = 1;
        int media = min + r.nextInt((int) diff + 1);
        return media;
    }

    public static String doMD5(String test) {
        String mdArray = "";
        try {

            byte[] dataBuffer = test.getBytes("UTF-8");

            byte[] theDigest = digestIt(dataBuffer);
            mdArray = displayBase64(theDigest);

            boolean ok = true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return mdArray;
    }

    /*This method generates and returns a digest for an
    incoming array of bytes.*/

    static byte[] digestIt(byte[] dataIn) {
        byte[] theDigest = null;
        try {

            java.security.MessageDigest messageDigest =
                    java.security.MessageDigest.getInstance("MD5");

            messageDigest.update(dataIn);

            theDigest = messageDigest.digest();
        } catch (Exception e) {
            System.out.println(e);
        }

        return theDigest;
    }

    //Method to display an array of bytes in base 64 format
    static String displayBase64(byte[] data) {
        sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
        String encoded = encoder.encodeBuffer(data);
        return encoded;
    }

    public static void check(String pass) throws AbadiaException {
        int idx = 0;
        boolean numeric = false;
        boolean upperCase = false;
        boolean lowerCase = false;
        //char last = 0;
        while (idx < pass.length()) {
            char ch = pass.charAt(idx++);
            upperCase |= Character.isUpperCase(ch);
            lowerCase |= Character.isLowerCase(ch);
            numeric |= Character.isDigit(ch);
            //if (last != 0)
            //{
            //    if (last == ch)
            //        throw new pwdValoresRepetidosException("Utilidades.check. valores repetidos", log);
            //        //System.out.println("repeated " + last);
            //}
            //last = ch;
        }
        /* de momento no contralamos mayúsculas y minúsculas...
            if (! upperCase)
                throw new pwdSinMayusculasException("Utilidades.check. pwd sin Mayusculas", log);
                //System.out.println("need at least one upper case character");
            if (! lowerCase)
                throw new pwdSinMinusculasException("Utilidades.check. pwd sin Minusculas", log);
                //System.out.println("need at least one lower case character");
        */
        if (!numeric)
            throw new pwdSinNumerosException("Utilidades.check. pwd sin Números", log);
        //System.out.println("need at least one numeric character");
        if (pass.length() < 6)
            throw new pwdCortaException("Utilidades.check. pwd Corta", log);
        //System.out.println("need at least 5 characters");
    }

    public static void eliminarRegistroContext(String sNick) throws AbadiaException {
        GestionDblClickBBean.resetUserTime(sNick);
/*
        InitialContext initCtx = null;
        try {
            GestionDblClickBBean.resetUserTime(sNick);
            initCtx = new InitialContext();
            initCtx.rebind(sNick, null);
            initCtx.close();
        } catch (NamingException e) {
            log.error("Utilidades. eliminarRegistroContext", e);
            throw new AbadiaNamingException("Utilidades. eliminarRegistroContext", e, log);
        }
*/
    }

    public static ArrayList<Table> ordenarContenidoHash2List(HashMap<Integer, String> hm, Table tPrimero) {
        List<Integer> mapKeys = new ArrayList<Integer>(hm.keySet());
        List<String> mapValues = new ArrayList<String>(hm.values());
        ArrayList<Table> alReturn = new ArrayList<Table>();

        hm.clear();

        TreeSet<String> sortedSet = new TreeSet<String>(mapValues);

        Object[] sortedArray = sortedSet.toArray();

        int size = sortedArray.length;

        //a) Ascending sort
        alReturn.add(tPrimero);
        for (int i = 0; i < size; i++) {

            //System.out.println(sortedArray[i]);

            alReturn.add(new Table(mapKeys.get(mapValues.indexOf(sortedArray[i])), (String) sortedArray[i]));
            //hm.put(mapKeys.get(mapValues.indexOf(sortedArray[i])), sortedArray[i]);

        }
        return alReturn;
    }

    public static int recuperarIncrementoEspacioEdificioPorLibro(int p_iTipoEdificioId, int p_iNivelLibro,
                                                                 int p_iNivelEdificio) throws AbadiaException {
        int iIncrementoEspacio = 0;

        if (p_iTipoEdificioId == EDIFICIO_GRANERO) {
            iIncrementoEspacio = (p_iNivelLibro * p_iNivelEdificio) * 10;
        }
        if (p_iTipoEdificioId == EDIFICIO_GRANJA) {
            iIncrementoEspacio = (p_iNivelLibro * p_iNivelEdificio) * 10;
        }
        if (p_iTipoEdificioId == EDIFICIO_ESTABLO) {
            iIncrementoEspacio = (p_iNivelLibro * p_iNivelEdificio) * 10;
        }
        if (p_iTipoEdificioId == EDIFICIO_DORMITORIO) {
            iIncrementoEspacio = (p_iNivelLibro * p_iNivelEdificio);
        }
        if (p_iTipoEdificioId == EDIFICIO_ALMACEN) {
            iIncrementoEspacio = (p_iNivelLibro * p_iNivelEdificio) * 15;
        }
        if (p_iTipoEdificioId == EDIFICIO_COCINA) {
            iIncrementoEspacio = (p_iNivelLibro * p_iNivelEdificio) * 10;
        }

        return iIncrementoEspacio;
    }
}
