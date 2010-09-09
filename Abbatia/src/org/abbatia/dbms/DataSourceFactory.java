package org.abbatia.dbms;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.util.Hashtable;
import org.abbatia.exception.AbadiaNamingException;
import org.apache.log4j.Logger;

/**
 * <p>Title: SGCArq </p>
 * <p>Description: Gestion de conexiones</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Olympus</p>
 * @author unascribed
 * @version 1.0
 */

public class DataSourceFactory
{
  private static Logger log = Logger.getLogger(DataSourceFactory.class.getName());

  /**
   * Constantes que definen los parametros que se recogeran del fichero de properties datasourcefactory.properties
   */

  /**
   * Caché de datasources
   */
  private static Hashtable dataSources = new Hashtable();

  /**
   * Constructor vacío
   */
  public DataSourceFactory() {
  }

  /**
   * Recoge el datasource del contexto definido en el fichero de properties (si esta definido) o del
   * contexto por defecto de la VM (si no)
   * @param dataSourceName
   * @return
   * @throws org.abbatia.exception.AbadiaNamingException
   */
  private static DataSource lookupDataSource (String dataSourceName) throws AbadiaNamingException {
    DataSource ds = null;
    InitialContext initialContext = null;
    try
    {

      initialContext = new InitialContext();

      Object o = initialContext.lookup("java:comp/env/jdbc/".concat(dataSourceName));
      if (o instanceof DataSource) {
        ds = (DataSource) o;
      } else {
        throw new AbadiaNamingException("Error al recuperar el objeto \"" + dataSourceName + "\": No implementa la interfaz DataSource.", null, log);
      }
    } catch (NamingException ne)
    {
      throw new AbadiaNamingException("Error al recuperar el objeto \"" + dataSourceName + "\": No se encuentra en el servidor de nombres.", ne, log);
    } finally {
      if (initialContext != null) {
        try {
          initialContext.close();
        } catch (NamingException e) {
          // Cannot do anything
        }
      }
    }
    return ds;
  }

  /**
   * Método que recupera un datasource, haciendo caché
   * @param dataSourceName
   * @return
   */
  public static DataSource getDataSource(String dataSourceName) throws AbadiaNamingException {
    DataSource ds;

    if (dataSources.get(dataSourceName) == null) {
      ds = lookupDataSource(dataSourceName);
      dataSources.put(dataSourceName, ds);
    } else {
      ds = (DataSource) dataSources.get(dataSourceName);
    }

    return ds;
  }
}