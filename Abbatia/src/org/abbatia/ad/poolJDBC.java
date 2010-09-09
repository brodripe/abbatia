package org.abbatia.ad;

//import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.Context;
import javax.sql.DataSource;
import javax.sql.RowSet;
//import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
//import com.mysql.jdbc.Statement;

import org.apache.log4j.Logger;
import org.abbatia.utils.Constantes;
//

import java.sql.*;

public class poolJDBC
{
    private static Logger log = Logger.getLogger(poolJDBC.class.getName());
    //private static MysqlDataSource dsAbadias = null;
    private static DataSource dsAbadias = null;
    //private static DataSource dsAbadias = null;
    //private static MysqlDataSource dsProcesos = null;
    private static DataSource dsProcesos = null;
    private static int iContador = 0;

    public static int getiContador() {
        return iContador;
    }

    public static void decrementarContador()
    {
        iContador --;
        log.debug("Se cierra Conexión: " + iContador );
    }

    public static void incrementarContador(String strDataSource)
    {
        iContador ++;
        log.debug("Se abre Conexión " + strDataSource +  " - " + iContador);
    }
    public static void setiContador(int iContador) {
        poolJDBC.iContador = iContador;
    }

    /**
 * Method obtenerConex.
 * @param strDataSource
 * @return Connection
 */
public static Connection obtenerConex(String strDataSource, int timeOut) throws Exception
{
	Connection conResult = null;

	try
    {
        if (strDataSource.equals(Constantes.DB_CONEXION_ABADIAS))
        {
            if (dsAbadias == null)
            {
/*                  dsAbadias = new MysqlDataSource();

                  dsAbadias.setServerName (server);
                  dsAbadias.setDatabaseName (database);
                  dsAbadias.setUser (username);
                  dsAbadias.setPassword (password);
*/
                  Context ctx = new InitialContext();
                  dsAbadias = (DataSource) ctx.lookup("java:comp/env/jdbc/" + strDataSource);
                  //dsAbadias.
            }
            //conResult = setupConnection (dsAbadias);
            conResult = dsAbadias.getConnection();

            incrementarContador(strDataSource);


        }else if (strDataSource.equals(Constantes.DB_CONEXION_PROCESS))
        {
            if (dsProcesos == null)
            {
/*                dsProcesos = new MysqlDataSource();
                dsProcesos.setServerName (server);
                dsProcesos.setDatabaseName (database);
                dsProcesos.setUser (username);
                dsProcesos.setPassword (password);
 */
                Context ctx = new InitialContext();
                dsProcesos= (DataSource) ctx.lookup("java:comp/env/jdbc/" + strDataSource);
            }

            //return dsProcesos.getConnection();
            //conResult = setupConnection (dsProcesos);
            conResult = dsProcesos.getConnection();
            incrementarContador(strDataSource);

        }
        setTimeOutDB( conResult, timeOut );
        conResult.setAutoCommit(true);
        return (conResult);

	}catch (NamingException ne)
    {
		throw new Exception("Error JNDI al obtener una conexión: " + strDataSource, ne);
	}
	catch (SQLException sqle)
    {
		String message= "Error SQL al obtener una conexión: " + sqle.getMessage();
		throw new Exception(message, sqle);
	}

}

/**
 * Method liberarConex.
 * @param conn
 */
public static void liberarConex(Connection conn) throws Exception
{
   try
   {
		if (conn != null && !conn.isClosed())
        {
			conn.close();
            decrementarContador();
            //log.info("Se cierra Conexión: " + iContador );
		}
   }catch (SQLException sqle)
   {
		String message= "poolJDBC liberarConex: " + sqle.getMessage() ;
        //log.error("LOGBRP - " + message, sqle);
		throw new Exception(message, sqle);
	}
}

/**
 * Method obtenerDataSource.
 * @param strDataSource
 * @return DataSource
 */
public static DataSource obtenerDataSource(String strDataSource) throws Exception
{

	DataSource conDS = null;
	try
        {
          log.debug("LOGBRP - Obtengo el InitialContext.");
          InitialContext ic = new InitialContext();
          log.debug("LOGBRP - InitialContext Obtenido.");
          conDS = (DataSource) ic.lookup(strDataSource);
          log.debug("LOGBRP - Conexión con datasource establecida.");
        }
	catch (NamingException ne)
        {
          String message = "Error JNDI al obtener un DataSource: " + ne.getMessage();
          log.error("LOGBRP - " + message, ne);
          //conDS = null;
          throw new Exception(message, ne);
        }
	return (conDS);
}

// Establecer el timeout de la conexion
public static void setTimeOutDB(Connection conResult, int timeout) throws Exception
{
        Statement ps = (Statement) conResult.createStatement();
        ps.execute("SET SESSION wait_timeout="+timeout);
        ps.execute("SET SESSION interactive_timeout="+timeout);
        ps.close();
}

public static void cerrarResultSet(ResultSet rs)
{
    try
    {
       rs.close();
    }catch(Exception e)
    {}
}
public static void cerrarStatement(PreparedStatement ps)
    {
        try
        {
           ps.close();
        }catch(Exception e)
        {}
    }
    public static void cerrarStatement(Statement st)
        {
            try
            {
               st.close();
            }catch(Exception e)
            {}
        }

public static void cerrarRowSet(RowSet rs)
    {
        try
        {
           rs.close();
        }catch(Exception e)
        {}
    }


/*
   private static com.mysql.jdbc.Connection setupConnection (DataSource ds)  throws SQLException
   {
      Connection conn = ds.getConnection();
      // Set null date option:
      com.mysql.jdbc.Connection  myConn = (com.mysql.jdbc.Connection) conn;
      myConn.setZeroDateTimeBehavior ("convertToNull");
      return myConn;
   }
   */
}
