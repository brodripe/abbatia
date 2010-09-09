package org.abbatia.dbms;

import org.abbatia.exception.DBMSException;
import org.abbatia.dbms.model.vo.LoadFieldVO;
import com.sun.rowset.CachedRowSetImpl;

import java.sql.*;

import org.apache.log4j.Logger;


/**
 * <p>Título: DBMSUtils</p>
 *  <p>Descripción:</p>
 *  <p>Copyright: Copyright (c) 2007</p>
 *  <p>Compañía: Olympus</p>
 *
 * @author Talarn Consulting
 */
public class DBMSUtils {
  private static Logger log = Logger.getLogger(DBMSUtils.class.getName());

  /**
   * Cierra la CallableStatement o la Conexion pasada como parametro
   *
   * @param o Objeto (CallableStatement o Connection) a cerrar
   */
  public static void cerrarObjetoSQL (Object o) {
    if (o != null) {
      try {
        if (o instanceof CallableStatement) {
          (( CallableStatement ) o).close();

        } else if (o instanceof Connection) {
          ConnectionFactory.closeConnection(( Connection ) o);

        } else if (o instanceof ResultSet) {
          (( ResultSet ) o).close();

        } else if (o instanceof Statement) {
          (( Statement ) o).close();

        } else if( o instanceof CachedRowSetImpl)
          ((CachedRowSetImpl) o).close();
          // END OF else

      } catch (SQLException sqle) {
        // No se lanza ninguna excepcion
        log.error("Error cerrando objeto SQL.", sqle);

      }

    }

  } // END OF public static void cerrarObjetoSQL (Object)


  /**
   * Registra un parametro de entrada en un prepared statement.
   *
   * @param p_oPstmt
   * @param p_iIndice
   * @param p_oField
   *
   * @throws org.abbatia.exception.DBMSException
   */
  public static void registrarParametroEntrada (PreparedStatement p_oPstmt,
                                                int               p_iIndice,
                                                LoadFieldVO p_oField)
  throws DBMSException {
    if (p_oPstmt != null) {
      try {
        if (p_oField.getValor() == null) {
          p_oPstmt.setNull(p_iIndice, p_oField.getTipo());

        } else {
          switch (p_oField.getTipo()) {
            case Types.INTEGER:
              p_oPstmt.setInt(p_iIndice, ( (Integer) p_oField.getValor()).intValue());

              break;

            case Types.VARCHAR:
              p_oPstmt.setString(p_iIndice, p_oField.getValor().toString());

              break;

            case Types.DATE:
              p_oPstmt.setDate(p_iIndice, (Date) p_oField.getValor());

              break;

          } // END OF switch

        } // END OF else

      } catch (SQLException sqle) {
        throw new DBMSException("Error registrando parámetro " + p_oField.getNombre(), sqle, log);

      } catch (Exception e) {
        throw new DBMSException("Error registrando parámetro " + p_oField.getNombre(), e, log);

      }

    } // END OF if

  } // END OF public static void informarParametro()


  /**
   * Procesa el array resultado de una ejecucion batch.
   *
   * @param aUpdateCounts
   */
  public static int procesarUpdateCounts (int[] aUpdateCounts) {
    int iFilasActualizadas = 0,
        iSentenciasEjecutadasSinInfo = 0,
        iSentenciasErroneas = 0;

    for (int i = 0; i < aUpdateCounts.length; i++) {
      if (aUpdateCounts[i] >= 0) {
        iFilasActualizadas += aUpdateCounts[i];
        
      } else if (aUpdateCounts[i] == Statement.SUCCESS_NO_INFO) {
        iSentenciasEjecutadasSinInfo++;

      } else if (aUpdateCounts[i] == Statement.EXECUTE_FAILED) {
        iSentenciasErroneas++;

      } // END OF else

    } // END OF for

    log.debug("DBMSUtils. Filas totales actualizadas: " + iFilasActualizadas);
    log.debug("DBMSUtils. Sentencias ejecutadas sin información sobre el resultado: " + iSentenciasEjecutadasSinInfo);
    log.debug("DBMSUtils. Sentencias erróneas: " + iSentenciasErroneas);

    return iFilasActualizadas;
    
  } // END OF public static void procesarUpdateCounts (int[])

} // END OF public class DBMSUtils
