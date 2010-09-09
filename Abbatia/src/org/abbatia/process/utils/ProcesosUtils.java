package org.abbatia.process.utils;
import org.abbatia.utils.*;
import org.apache.log4j.Logger;

import java.util.Random;

public class ProcesosUtils
{
  private static Logger log = Logger.getLogger(ProcesosUtils.class.getName());
  private static Random r = new Random();

  //este constructor informara el objeto conexión
  //para que pueda ser utilizado por todas las clases
  //que la extiendan.

  // Format a string
  public static String Format(String texto, String replace)
  {
    //int pos = texto.indexOf("%s");
    //if (pos > 0) {
    //  texto = texto.substring(0,pos) + replace + texto.substring(pos+2);
    //}
    //return texto;
    return texto.replaceFirst("%s", replace);
  }
  // Format a string + string
  public static String Format(String texto, String replace1, String replace2)
  {
    texto = texto.replaceFirst("%s", replace1);
    texto = texto.replaceFirst("%s", replace2);
    return texto;
  }
  // Format a int
  public static String Format(String texto, int replace)
  {
    int pos = texto.indexOf("%d");
    if (pos > 0) {
      texto = texto.substring(0,pos) + replace + texto.substring(pos+2);
    }
    return texto;
  }

  // Format a float
   public static String Format(String texto, float replace)
     {
       int pos = texto.indexOf("%f");
       if (pos > 0) {
         texto = texto.substring(0,pos) + Utilidades.redondear(replace) + texto.substring(pos+2);
       }
       return texto;
     }
   // Format a double
   public static String Format(String texto, double replace)
        {
          int pos = texto.indexOf("%f");
          if (pos > 0) {
            texto = texto.substring(0,pos) + Utilidades.redondear(replace) + texto.substring(pos+2);
          }
          return texto;
        }

  /* Devuelve un numero entre Min y Max */

}
