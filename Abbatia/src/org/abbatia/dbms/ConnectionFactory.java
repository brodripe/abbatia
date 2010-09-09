package org.abbatia.dbms;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.abbatia.exception.AbadiaDBConnectionException;
import org.abbatia.exception.AbadiaNamingException;
import org.abbatia.exception.DBMSException;
import org.abbatia.exception.base.SystemException;

/**
 * <p>Title: ConnectionFactory </p>
 * <p>Description: Gestion de conexiones</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Abbatia</p>
 *
 * @author Benjamín Rodríguez
 * @version 1.0
 */

public class ConnectionFactory
{

private static Logger log = Logger.getLogger(DataSourceFactory.class.getName());

    /**
	 * Contador de conexiones
	 */
	private static long connectionCounter = 0;

	/**
	 * Constructor vacio
	 */
	public ConnectionFactory() {
	}

	/**
	 * Obtiene una conexion para un nombre de datasource (creando la conexion directamente o buscando el datasource,
	 * dependiendo de los properties connecionfactory.properties y datasourcefactory.properties)
	 *
	 * @param dataSourceName Datasource
	 * @return  Connection
	 * @throws org.abbatia.exception.AbadiaDBConnectionException Excepción de sistema
     */
	public static Connection getConnection(String dataSourceName) throws AbadiaDBConnectionException {
		return getConnection(dataSourceName, true);
	}

	/**
	 * Obtiene una conexion para un nombre de datasource (creando la conexion directamente o buscando el datasource,
	 * dependiendo de los properties connecionfactory.properties y datasourcefactory.properties)
	 *
	 * @param dataSourceName DataSource
	 * @param autoCommit Autocommit
	 * @return Connection
	 * @throws AbadiaDBConnectionException Exceptión de conexión
	 */
	public static Connection getConnection(String dataSourceName, boolean autoCommit) throws AbadiaDBConnectionException {

		Connection conn;
    	try
        {
			conn = DataSourceFactory.getDataSource(dataSourceName).getConnection();
			conn.setAutoCommit(autoCommit);
		} catch (SQLException sqle)
        {
			throw new AbadiaDBConnectionException("Error al obtener la conexión \"" + dataSourceName + "\"", sqle, log);
        } catch (AbadiaNamingException e)
        {
            throw new AbadiaDBConnectionException("Error al obtener la conexión \"" + dataSourceName + "\"", e, log);
        }
        // incrementamos el contador de conexiones, no llega hasta aqui si no se obtiene conexion
		connectionCounter++;

		return conn;
	}

	/**
	 * Cierra la conexion al origen de datos recibido como parametro
	 *
	 * @param conn Objeto Connection
	 * @return true si se ha cerrado la conexion
	 * @deprecated
	 */
	public static boolean closeConnexion(Connection conn) {
		return closeConnection(conn);
	}

	/**
	 * Cierra la conexion al origen de datos recibido como parametro
	 *
	 * @param conn Objeto Connection
	 * @return true si se ha cerrado la conexion
	 */
	public static boolean closeConnection(Connection conn) {
		boolean closed = false;

		if (conn != null)
        {
			try
            {
                conn.close();
                //Sólo se debe decrementar el contador cuando se ha cerrado la conexión
                connectionCounter--;
                closed = true;
			} catch (SQLException e)
            {
				//No se lanza ninguna excepcion;
			}
		}

		return closed;
	}

	/**
	 * Metodo usado para confirmar las transacciones realizadas a traves de un Connection.
	 *
	 * @param conn conexión
	 * @throws org.abbatia.exception.base.SystemException Excepción de sistema
     */
	public static void commitTransaction(Connection conn) throws SystemException {
		if (conn != null) {
			try {
				// verificamos que la conexion tenga las transacciones activas
				if (!conn.getAutoCommit()) {
					//cancelar la transaccion
					conn.commit();
				}
			} catch (SQLException sqle) {
				throw new DBMSException("Error al confirmar la transacción", sqle, log);
			}
		}
	}

	/**
	 * Cancela una transaccion a base de datos
	 *
	 * @param conn connexion que lleva asociados la transaccion a aborar
     * @throws org.abbatia.exception.base.SystemException  Excepción de sistema
	 */
	public static void rollbackTransaction(Connection conn) throws SystemException {
		if (conn != null) {
			try {
				// verificamos que la conexion tenga las transacciones activas
				if (!conn.getAutoCommit()) {
					// cancelar la transaccion
					conn.rollback();
				}
			} catch (SQLException sqle) {
				throw new DBMSException("Error al hacer rollback de la transacción", sqle, log);
			}
		}
	}

	/**
	 * Devuelve el numero de conexiones abiertas
	 *
	 * @return long
	 */
	public static long getConnectionCounter() {
		return connectionCounter;
	}
}