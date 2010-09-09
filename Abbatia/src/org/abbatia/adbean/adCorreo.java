package org.abbatia.adbean;

import org.abbatia.actionform.BuscarAbadiasActForm;
import org.abbatia.adbean.base.adbeans;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Correo;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.CorreoNoEncontradoException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.Utilidades;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class adCorreo extends adbeans {
    private static Logger log = Logger.getLogger(adCorreo.class.getName());

    /**
     * Instancia la clase sin obtener una nueva conexión
     *
     * @param con
     * @throws AbadiaException
     */
    public adCorreo(Connection con) throws AbadiaException {
        super(con);
    }

    public void crearCorreo(Correo correo) throws AbadiaException {
        String sSQL = "";
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        int iCount = 0;
        adRecurso recursoAD;
        try {
            stmt = con.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_UPDATABLE);

            correo.setTexto(Utilidades.normalizarTexto(correo.getTexto()));

            sSQL = "Insert Into correo (ABADIAID, MENSAJE, FECHA_ABADIA, FECHA_REAL) " +
                    "Values (" + correo.getIdAbadiaOrigen() + ", '" + correo.getTexto() + "', '" + correo.getFecha_abadia() + "', '" + correo.getFecha_real() + "')";

            //asigno los valores
            // Ejecutarlo

            stmt.executeUpdate(sSQL, Statement.RETURN_GENERATED_KEYS);
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                correo.setIdCorreo(rs.getLong(1));
            }
            rs.close();
            stmt.close();
            //crear destinatarios..

            StringTokenizer st = new StringTokenizer(correo.getIdAbadiasDestino(), ";");
            sSQL = "Insert Into correo_destinatario (CORREOID, ABADIAID, LEIDO) values(?, ?, ?) ";
            ps = con.prepareStatement(sSQL);
            while (st.hasMoreElements()) {
                ps.setLong(1, correo.getIdCorreo());
                ps.setObject(2, st.nextElement());
                ps.setShort(3, (short) 0);
                ps.execute();
                iCount++;
            }
            recursoAD = new adRecurso(con);
            recursoAD.restarRecurso(Constantes.RECURSOS_ORO, correo.getIdAbadiaOrigen(), (double) iCount * Constantes.VARIOS_COSTE_ENVIO_MENSAJE);


        } catch (SQLException e) {
            throw new AbadiaSQLException("adCorreo. crearCorreo. SQLException ", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(stmt);
        }
    }

    public String recuperarNombresSeleccion(BuscarAbadiasActForm datos) throws AbadiaException {
        Abadia abadia;
        String nombresSeleccion = "";
        adAbadia abadias = new adAbadia(con);

        for (int iCount = 0; datos.getSeleccion().length > iCount; iCount++) {
            abadia = abadias.recuperarAbadia(datos.getSeleccion()[iCount]);

            nombresSeleccion = nombresSeleccion + abadia.getNombre() + ";";
        }
        log.debug("adCorreo. recuperarNombresSeleccion. nombresSeleccion antes: " + nombresSeleccion);
        if (!nombresSeleccion.equals("")) {
            nombresSeleccion = nombresSeleccion.substring(0, nombresSeleccion.length() - 1);
        }
        log.debug("adCorreo. recuperarNombresSeleccion. nombresSeleccion despues: " + nombresSeleccion);
        return nombresSeleccion;
    }

    public ArrayList<Correo> recuperarEnviados(Abadia abadia) throws AbadiaException {
        //Definición de cadena sql de consulta
        String sSQL = "select a.nombre as destinatario, c.fecha_abadia, c.fecha_real, c.mensaje, cd.leido as estado, c.correoid " +
                "from correo c, correo_destinatario cd, abadia a " +
                "where c.correoid=cd.correoid and c.abadiaid=? and cd.abadiaid=a.abadiaid order by c.fecha_abadia desc";

        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        Correo correo;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave.
            int parNo = 1;
            ps.setLong(parNo, abadia.getIdDeAbadia());
            //Lanzo la consulta y cargo el resultado en un resultset
            rs = ps.executeQuery();
            ArrayList<Correo> correos = new ArrayList<Correo>();
            //si la consulta encuentra la mensajes....
            while (rs.next()) {
                //iniciamos el volcado de datos sobre
                //el objeto mensajes.
                correo = new Correo();
                correo.setIdCorreo(rs.getLong("CORREOID"));
                correo.setFecha_abadia(Utilidades.formatStringFromDB(rs.getString("FECHA_ABADIA")));
                correo.setFecha_real(Utilidades.formatStringFromDB(rs.getString("FECHA_REAL")));
                if (rs.getString("MENSAJE").length() > 50) {
                    correo.setTexto(rs.getString("MENSAJE").substring(0, 50).concat("..."));
                } else {
                    correo.setTexto(rs.getString("MENSAJE").concat("..."));
                }

                correo.setAbadiaDestino(rs.getString("DESTINATARIO"));
                if (rs.getShort("ESTADO") == 0) {
                    correo.setEstado("NO");
                } else {
                    correo.setEstado("SI");
                }
                correos.add(correo);
            }
            return correos;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adCorreo. recuperarEnviados. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public Correo recuperarDatosCorreo(String correoid, Abadia abadia) throws AbadiaException {
        //Definición de cadena sql de consulta
        String sSQL = "select c.mensaje, c.abadiaid, a.nombre " +
                "from correo c, abadia a " +
                "where a.abadiaid = c.abadiaid and c.correoid=? ";

        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        Correo correo;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave.
            int parNo = 1;
            ps.setLong(parNo, Long.parseLong(correoid));
            // ps.setLong(parNo++, abbatia.getIdDeAbadia());
            //Lanzo la consulta y cargo el resultado en un resultset
            rs = ps.executeQuery();
            //si la consulta encuentra la mensajes....
            if (rs.next()) {
                //iniciamos el volcado de datos sobre
                //el objeto mensajes.
                correo = new Correo();
                correo.setIdCorreo(Long.parseLong(correoid));
                correo.setTexto(rs.getString("MENSAJE"));
                correo.setIdAbadiaOrigen(rs.getLong("ABADIAID"));
                correo.setAbadiaOrigen(rs.getString("NOMBRE"));
                correo.setAbadiaDestino(recuperarDestinatarios(correoid));
            } else throw new CorreoNoEncontradoException("adCorreo.recuperarDatosCorreo.no encontrado", log);
            return correo;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adCorreo. recuperarDatosCorreo. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public void marcarLeido(String correoid, Abadia abadia) throws AbadiaException {
        //Definición de cadena sql de consulta
        String sSQL = "update correo_destinatario set leido = 1 where correoid = ? and abadiaid = ?";

        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave.
            int parNo = 1;
            ps.setLong(parNo++, Long.parseLong(correoid));
            ps.setLong(parNo, abadia.getIdDeAbadia());
            //Lanzo la consulta y cargo el resultado en un resultset
            ps.executeUpdate();
            //si la consulta encuentra la mensajes....
        } catch (SQLException e) {
            throw new AbadiaSQLException("adCorreo. marcarLeido. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public void borrarCorreoStatic(String correoid, Abadia abadia) throws AbadiaException {
        //Definición de cadena sql de consulta
        String sSQL = "DELETE FROM correo_destinatario WHERE correoid = " + correoid + " and abadiaid = " + abadia.getIdDeAbadia();
        adUtils utilsAD;

        utilsAD = new adUtils(con);
        utilsAD.execSQL(sSQL);
    }

    public void borrarCorreo(String correoid, Abadia abadia) throws AbadiaException {
        //Definición de cadena sql de consulta
        String sSQL = "DELETE FROM correo_destinatario WHERE correoid = ? and abadiaid = ?";

        // Hay que borrar el correo si no existen m�s vinculos con el correo_destinatario *********************************

        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave.
            int parNo = 1;
            ps.setLong(parNo++, Long.parseLong(correoid));
            ps.setLong(parNo, abadia.getIdDeAbadia());
            //Lanzo la consulta y cargo el resultado en un resultset
            ps.execute();
            //si la consulta encuentra la mensajes....
        } catch (SQLException e) {
            throw new AbadiaSQLException("adCorreo. borrarCorreo. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public void borrarCorreoDestinatario(int correoid, Abadia abadia) throws AbadiaException {
        //Definición de cadena sql de consulta
        String sSQL = "DELETE FROM correo_destinatario WHERE correoid = " + correoid + " and abadiaid = " + abadia.getIdDeAbadia();
        adUtils utils = new adUtils(con);
        utils.execSQL(sSQL);
    }

    public void borrarCorreoOrigen(int correoid) throws AbadiaException {
        //Definición de cadena sql de consulta
        String sSQL = "DELETE FROM correo WHERE correoid = " + correoid;
        String sSQL_dest = "DELETE FROM correo_destinatario WHERE correoid = " + correoid;

        adUtils utils = new adUtils(con);
        utils.execSQL(sSQL);
        utils.execSQL(sSQL_dest);
    }

    public void borrarCorreoAbadia(int idAbadia) throws AbadiaException {
        //Definición de cadena sql de consulta

        String sSQL_dest = "DELETE FROM correo_destinatario WHERE correoid in (select correoid from correo where abadiaid = " + idAbadia + ")";
        String sSQL = "DELETE FROM correo WHERE abadiaid = " + idAbadia;

        adUtils utils = new adUtils(con);
        utils.execSQL(sSQL);
        utils.execSQL(sSQL_dest);
        utils.execSQL(sSQL_dest);
        //utils.finalize();
    }


    public String recuperarDestinatarios(String correoid) throws AbadiaException {
        //Definición de cadena sql de consulta
        String sSQL = "select c.abadiaid, a.nombre " +
                "from correo_destinatario c, abadia a " +
                "where c.correoid=? and c.abadiaid=a.abadiaid";

        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave.
            int parNo = 1;
            ps.setLong(parNo, Long.parseLong(correoid));
            //Lanzo la consulta y cargo el resultado en un resultset
            rs = ps.executeQuery();
            String dest = "";
            //si la consulta encuentra la mensajes....
            while (rs.next()) {
                dest = dest + rs.getString("nombre") + ";";
            }
            if (!dest.equals("")) {
                dest = dest.substring(0, dest.length() - 1);
            }
            return dest;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adCorreo. recuperarDestinatarios. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public ArrayList<Correo> recuperarRecibidos(Abadia abadia) throws AbadiaException {
        //Definición de cadena sql de consulta
        String sSQL = "select a.nombre as desde, c.fecha_abadia, c.fecha_real, c.mensaje, cd.leido as estado, c.correoid " +
                "from correo c, correo_destinatario cd, abadia a " +
                "where c.correoid=cd.correoid and cd.abadiaid=? and c.abadiaid=a.abadiaid order by c.fecha_abadia desc";
        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Correo> correos = new ArrayList<Correo>();
        Correo correo;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setLong(1, abadia.getIdDeAbadia());
            //Lanzo la consulta y cargo el resultado en un resultset
            rs = ps.executeQuery();
            //si la consulta encuentra la mensajes....
            while (rs.next()) {
                //iniciamos el volcado de datos sobre
                //el objeto mensajes.
                correo = new Correo();
                correo.setIdCorreo(rs.getLong("CORREOID"));
                correo.setFecha_abadia(Utilidades.formatStringFromDB(rs.getString("FECHA_ABADIA")));
                correo.setFecha_real(Utilidades.formatStringFromDB(rs.getString("FECHA_REAL")));
                correo.setTexto(rs.getString("MENSAJE"));
/*
                if (rs.getString("MENSAJE").length() > 50) {
                    correo.setTexto(rs.getString("MENSAJE").substring(0, 50).concat("..."));
                } else {
                    correo.setTexto(rs.getString("MENSAJE").concat("..."));
                }
*/

                correo.setAbadiaDestino(rs.getString("DESDE"));
                if (rs.getShort("ESTADO") == 0) {
                    correo.setEstado("NO");
                } else {
                    correo.setEstado("SI");
                }
                correos.add(correo);
            }

            return correos;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adCorreo. recuperarEnviados. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public int recuperarTotal(Abadia abadia) throws AbadiaException {
        String sSQLCount = "select Count(*) " +
                "from correo c, correo_destinatario cd, abadia a " +
                "where c.correoid=cd.correoid and cd.abadiaid=? and c.abadiaid=a.abadiaid";

        //creo un objeto de tipo PreparedStatement sobre el que se
        int total = 0;
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQLCount);
            ps.setLong(1, abadia.getIdDeAbadia());
            rs = ps.executeQuery();
            if (rs.next())
                total = rs.getInt(1);
            return total;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adCorreo. recuperarTotal. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }
}
