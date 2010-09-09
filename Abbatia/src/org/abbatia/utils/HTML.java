package org.abbatia.utils;
import java.text.*;
import java.util.Random;
import org.apache.log4j.Logger;

public class HTML {
  private static Logger log = Logger.getLogger(Utilidades.class.getName());
  private static Random r = new Random();
  public static int ALIGN_NONE = 0;
  public static int ALIGN_LEFT = 1;
  public static int ALIGN_RIGHT = 2;
  public static int ALIGN_CENTER = 3;

  //Contents.setNavigate( cHTML.getNavigateBar("mercado_compra.do", "action=inicio", Contents.getPage_actual(), Contents.getPages()) );
  public String getNavigateBar(String url, String parametros, int pagina, int pagina_total)
  {
    String sHTML = "", sURL = url + "?" + parametros + "&pagina=", sA1 = "<a href=\"", sA2 = "\">", sA3 = "</a>";
    String sFont1 = "<font size=2>", sFont2 = "</font>";
    if (pagina_total != 0) {
      // Adelante y atrás
      if (pagina != 0) sHTML = sHTML + sFont1 + sA1 + sURL + (pagina - 1) + sA2 + "<< " +
          sA3+ sFont2;
      for (int p = 0; p < pagina_total + 1; p++) {
        if (p == pagina)
          sHTML = sHTML + " " + sFont1 + "<b>"+(p + 1)+ "</b>" + sFont2 + " ";
        else
          sHTML = sHTML + sFont1 + sA1 + sURL + (p) + sA2 + " " + (p + 1) + " " + sA3+ sFont2;
      }
      if (pagina != pagina_total) sHTML = sHTML + sFont1+ sA1 + sURL + (pagina + 1) +
          sA2 + " >>" + sA3+sFont2;
    }
    return sHTML;
  }

  /**
   * Genera una barra de navegación basada en la llamada a una función javascript definida desde el
   * código.
   * @param funcion Nombre de la función javascript, que será invocada desde el navegador y declarada
   * en la página cliente.
   * @param pagina Número de página que se quiere mostrar.
   * @param pagina_total Número de páginas totales a mostrar.
   * @return El código HTML que representa la barra de navegación.
   */
  public static String getNavigateBarJavaScript(String funcion, int pagina, int pagina_total) {
    String sHTML = "";
    String patronSinLink = "<font size=2><b> {0} </b></font>";
    String patronLink = "<a href=\"javascript:{0}(''{1}'');\"><font size=2> {2} </font></a>";

    if (pagina_total != 0) {
      // Adelante y atrás
      if (pagina != 0) {
        sHTML += MessageFormat.format(patronLink,new Object[]{funcion,String.valueOf(pagina-1),"<<"});
      }
      for (int i = 0; i < pagina_total + 1; i++) {
        if (i == pagina) sHTML += MessageFormat.format(patronSinLink,new Object[]{String.valueOf(i+1)});
        else
          sHTML += MessageFormat.format(patronLink,new Object[]{funcion,String.valueOf(i),String.valueOf(i+1)});
      }
      if (pagina != pagina_total) {
        sHTML += MessageFormat.format(patronLink,new Object[]{funcion,String.valueOf(pagina+1),">>"});
      }
    }
    return sHTML;
  }

  /*   Muestra una barra pequeñina ;-)
      */
  public static String smallBarra( int Posicion, String tooltip ) 
  {
    int n = Posicion;
    if (Posicion < 0)  n = 0;
    if (Posicion > 10) n = 10;
    return "<img src=\"images/iconos/barras/mini_estado_"+n+".gif\" alt=\""+tooltip+"\" title=\""+tooltip+"\" >";
  }

  public static String smallBarraRed( int Posicion, String tooltip )
  {
    int n = Posicion;
    if (Posicion < 0)  n = 0;
    if (Posicion > 10) n = 10;
    return "<img src=\"images/iconos/barras/mini_estado_red_"+n+".gif\" alt=\""+tooltip+"\" title=\""+tooltip+"\" >";
  }

  /*
     Devuelve el titulo para una columna formateada en html
  */
  public String getTD_TitleTable(String titulo, String action, int ordenid ) {
    String shtml;

    if (ordenid == 0)
    {
      shtml = "<td bgcolor=\"#E1C08B\" align=\"center\"><b><font color=\"#FFFFFF\">" + titulo + "</font></b></td>";
    } else {
      shtml = "<td bgcolor=\"#E1C08B\" align=\"center\">"+
                  "<table width=\"100%\" border=0>"+
                  "<td align=\"left\"><a href=\""+action+"&orden=1&ordenid="+ordenid+"\" ><img border=0 src=\"images/iconos/16/arrowdown.gif\"></a></td>" +
                  "<td align=\"center\"><b><font color=\"#FFFFFF\">" + titulo + "</font></b>" +
                  "<td align=\"right\"><a href=\""+action+"&orden=2&ordenid="+ordenid+"\"><img border=0 src=\"images/iconos/16/arrowup.gif\"></a></td><tr></table>"+
                  "</td>";

    }

    return shtml;
  }

  //
  public String getTD(int align, String Color, String contenido ) {
    String shtml;

    shtml = "<td";
    switch(align) {
      case 0:
        break;
      case 1:
        shtml = shtml + " align=\"left\"";
        break;
      case 2:
        shtml = shtml + " align=\"right\"";
        break;
      case 3:
        shtml = shtml + " align=\"center\"";
        break;
    }
    shtml = shtml + ">";
    if (Color != null)
    {
      shtml = shtml + "<font color=\""+Color+"\">" + contenido + "</font>";
    } else {
      shtml = shtml = shtml + contenido;
    }
    return shtml + "</td>";
  }

  // Devuelve el TR
  public String getTR(int contador, String contenido ) {
    if (contador % 2 == 0)
      return "<tr>"+contenido+"</tr>";
    else return "<tr bgcolor=\"#EDE0C5\">"+contenido+"</tr>";
  }

  /*   Muestra una barra
      */
  public String Barra( int Posicion, String tooltip ) {
    int n = Posicion;
    if (Posicion < 0)  n = 0;
    if (Posicion > 10) n = 10;
    return "<img src=\"images/iconos/barras/estado_"+n+".gif\" alt=\""+tooltip+"\" title=\""+tooltip+"\" >";
  }

  public static int getBarras_Value( int valor )
  {
    int option = 10;
    if(valor > 25 && valor < 35)   option = 10;
    if(valor > 35 || valor < 30)   option = 8;
    if(valor > 30 || valor < 25)   option = 6;
    if(valor > 40 || valor < 20)   option = 5;
    if(valor > 45 || valor < 15)   option = 3;
    if(valor > 50 || valor < 10)   option = 2;
    if(valor > 55 || valor < 5)    option = 1;
    if(valor >= 60 || valor <= 0)  option = 0;
    return option;
  }
  public static int getBarrasEstado( int valor )
  {
      int option = 10;
      if(valor > 90 && valor <= 100)   option = 10;
      if(valor > 80 && valor <= 90 )   option = 9;
      if(valor > 70 && valor <= 80)   option = 8;
      if(valor > 60 && valor <= 70)   option = 7;
      if(valor > 50 && valor <= 60)   option = 6;
      if(valor > 40 && valor <= 50)   option = 5;
      if(valor > 30 && valor <= 40)    option = 4;
      if(valor > 20 && valor <= 30)  option = 3;
      if(valor > 10 && valor <= 20)    option = 2;
      if(valor > 0 && valor <= 10)  option = 1;

      return option;
    }

}
