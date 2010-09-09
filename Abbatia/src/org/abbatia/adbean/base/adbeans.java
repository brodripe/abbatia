package org.abbatia.adbean.base;

import org.abbatia.ad.poolJDBC;
import org.abbatia.exception.AbadiaDBConnectionException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.apache.log4j.Logger;

import javax.sql.RowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class adbeans {
    private static Logger log = Logger.getLogger(adbeans.class.getName());
    protected Connection con;

    //este constructor informara el objeto conexión
    //para que pueda ser utilizado por todas las clases
    //que la extiendan.
    public static void main(String argv[]) {
        //adbeans ad = new adbeans();

    }


    public adbeans(Connection conexion) throws AbadiaDBConnectionException {
        con = conexion;
        try {
            if (con == null || con.isClosed()) {

                //si la conexion esta cerrada, decrementamos el contador...
//          if (con != null && con.isClosed())
//          {
//              log.info("Conexión cerrada, volvemos a abrirla....");
//              poolJDBC.decrementarContador();
//          }
                con = poolJDBC.obtenerConex(Constantes.DB_CONEXION_ABADIAS, Constantes.DB_TIMEOUT);
            } else {
                log.debug("LOGBRP - La conexión ya está establecida, no se obtiene");
            }
        } catch (Exception e) {
            //log.error("LOGBRP - No se ha podido conectar con la base de datos ", e);
            throw new AbadiaDBConnectionException("No se ha podido conectar con la base de datos", e, log);
        }

    }

/*  public adbeans(int timeOut) throws AbadiaDBConnectionException {

try
{
  if (con == null || con.isClosed())
  {
    //si la conexion esta cerrada, decrementamos el contador...
//           if (con != null && con.isClosed())
//           {
//               log.info("Conexión cerrada, volvemos a abrirla....");
//               poolJDBC.decrementarContador();
//           }

    con = poolJDBC.obtenerConex(Constantes.DB_CONEXION_ABADIAS, timeOut);
  }else
  {
    log.debug("LOGBRP - La conexión ya está establecida, no se obtiene");
  }
}catch(Exception e)
{
   e.printStackTrace();
  //log.error("LOGBRP - No se ha podido conectar con la base de datos ", e);
  throw new AbadiaDBConnectionException("No se ha podido conectar con la base de datos", e, log);
}
}
public adbeans(int timeOut, String sConexion) throws AbadiaDBConnectionException {
  try
  {
    if (con == null || con.isClosed())
    {
      //si la conexion esta cerrada, decrementamos el contador...
//             if (con != null && con.isClosed())
//             {
//                 log.info("Conexión cerrada, volvemos a abrirla....");
//                 poolJDBC.decrementarContador();
//             }


      con = poolJDBC.obtenerConex(sConexion, timeOut);
    }else
    {
      log.debug("LOGBRP - La conexión ya está establecida, no se obtiene");
    }
  }catch(Exception e)
  {
    throw new AbadiaDBConnectionException("No se ha podido conectar con la base de datos", e, log);
  }
}*/

    public Connection getConexion() throws AbadiaException {
        try {
            if (con == null || con.isClosed()) {
                con = poolJDBC.obtenerConex(Constantes.DB_CONEXION_ABADIAS, Constantes.DB_TIMEOUT);
            } else {
                log.debug("LOGBRP - La conexión ya está establecida, no se obtiene");
            }
        } catch (Exception e) {
            //log.error("LOGBRP - No se ha podido conectar con la base de datos ", e);
            throw new AbadiaDBConnectionException("No se ha podido conectar con la base de datos", e, log);
        }
        return con;
    }

    //
    /*  No se llama nunca... almenos cuando se debuga desde consola o log
    */
/*
  public void finalize()
  {
    try
    {
      poolJDBC.liberarConex(con);
    } catch(Exception e)
    {}

  }
*/

    public static void cerrarStatement(PreparedStatement ps) {
        poolJDBC.cerrarStatement(ps);
    }

    public static void cerrarStatement(Statement st) {
        poolJDBC.cerrarStatement(st);
    }

    public static void cerrarResultSet(ResultSet rs) {
        poolJDBC.cerrarResultSet(rs);
    }

    public static void cerrarRowSet(RowSet rs) {
        poolJDBC.cerrarRowSet(rs);
    }

}
