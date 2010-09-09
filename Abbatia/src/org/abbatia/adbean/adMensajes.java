package org.abbatia.adbean;

import org.abbatia.actionform.MensajeActForm;
import org.abbatia.adbean.base.adbeans;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Mensajes;
import org.abbatia.core.CoreTiempo;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.Utilidades;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

public class adMensajes extends adbeans {
    private static Logger log = Logger.getLogger(adMensajes.class.getName());

    /**
     * Instancia la clase sin obtener una nueva conexión
     *
     * @param con
     * @throws AbadiaException
     */
    public adMensajes(Connection con) throws AbadiaException {
        super(con);
    }


    public ArrayList<Mensajes> recuperarMensajesAbadia(Abadia abadia) throws AbadiaException {
        //Definición de cadena sql de consulta
        String sSQL = "Select * from mensajes where (ABADIAID = ? AND REGIONID = ?) OR REGIONID = -1";
        //creo un objeto de tipo Mensajes
        Mensajes mensaje = null;
        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave.
            log.debug("IdAbadia" + abadia.getIdDeAbadia());
            log.debug("IdRegion" + abadia.getIdDeRegion());
            int parNo = 1;
            ps.setLong(parNo++, abadia.getIdDeAbadia());
            ps.setLong(parNo, abadia.getIdDeRegion());
            //Lanzo la consulta y cargo el resultado en un resultset
            rs = ps.executeQuery();
            ArrayList<Mensajes> mensajesR = new ArrayList<Mensajes>();
            //si la consulta encuentra la mensajes....
            while (rs.next()) {
                //iniciamos el volcado de datos sobre
                //el objeto mensajes.
                mensaje = new Mensajes();
                mensaje.setIdDeMensaje(rs.getLong("MENSAJEID"));
                mensaje.setFechaAbadia(Utilidades.formatStringFromDB(rs.getString("FECHAABADIA")));
                mensaje.setFechaReal(Utilidades.formatStringFromDB(rs.getString("FECHAREAL")));
                mensaje.setIdDeMonje(rs.getLong("MONJEID"));
                mensaje.setMensaje(rs.getString("MENSAJE"));
                mensaje.setTipo(rs.getInt("TIPO"));
                //devolvemos el objeto Mensajes informado.
                mensajesR.add(mensaje);
            }
            return mensajesR;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMensaje. getMensajes. Error accediento a la base de datos.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /* Añadir un mensajes de procesos
    */
    public void crearMensajeActividadMonje(int idmonje, int idactividad, int idperiodo, String mensaje, float cantidad) throws AbadiaException {

        // Comprobar si existe un usuario con el mismo nick
        String sSQL = "INSERT into actividad_mensajes  ( monjeid, actividadid, periodoid, fechaabadia, fechareal, mensaje, cantidad ) " +
                "VALUES ( ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idmonje);
            ps.setInt(2, idactividad);
            ps.setInt(3, idperiodo);
            ps.setString(4, CoreTiempo.getTiempoAbadiaStringConHoras());
            ps.setString(5, CoreTiempo.getTiempoRealStringConHoras());
            ps.setString(6, mensaje);
            ps.setFloat(7, cantidad);
            // Ejecutarlo
            ps.execute();

        } catch (SQLException e) {
            throw new AbadiaSQLException("ProcesosUtils. crearMensajeActividadMonje. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public void crearMensaje(Mensajes mensaje) throws AbadiaException {
        String sSQL = "Insert Into mensajes (REGIONID, IDIOMAID, ABADIAID, MONJEID, FECHAABADIA, FECHAREAL, TIPO, MENSAJE) Values (?,?,?,?,?,?,?,?)";

        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        try {
            ps = (PreparedStatement) getConexion().prepareStatement(sSQL);
            //asigno los valores
            int parNo = 1;
            ps.setInt(parNo++, mensaje.getIdDeRegion());
            ps.setInt(parNo++, mensaje.getIdDeIdioma());
            ps.setLong(parNo++, mensaje.getIdDeAbadia());
            ps.setLong(parNo++, mensaje.getIdDeMonje());
            ps.setString(parNo++, mensaje.getFechaAbadia());
            ps.setString(parNo++, mensaje.getFechaReal());
            ps.setInt(parNo++, mensaje.getTipo());
            ps.setString(parNo, mensaje.getMensaje());
            // Ejecutarlo
            ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMensaje. crearMensaje. Error accediento a la base de datos.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }


    }


    public void crearMensajesCorreo(MensajeActForm correo, Abadia abadia) throws AbadiaException {
        //StringTokenizer stDest = new StringTokenizer(correo.getDestinatario(), ";");
        StringTokenizer stDestId = new StringTokenizer(correo.getDestinatarioid(), ";");
        //creamos los mensajes para notificar a los destinatarios el correo de entrada.
        log.debug("adMensajes. crearMensajesCorreo. Inicio");
        Mensajes mensaje = null;
        while (stDestId.hasMoreTokens()) {
            log.debug("adMensajes. crearMensajesCorreo. mensaje bucle");
            mensaje = new Mensajes();
            mensaje.setMensaje("Has recibido un correo de la abbatia " + abadia.getNombre());
            mensaje.setIdDeAbadia(Long.parseLong(stDestId.nextToken()));
            mensaje.setFechaReal(CoreTiempo.getTiempoRealStringConHoras());
            mensaje.setFechaAbadia(CoreTiempo.getTiempoAbadiaStringConHoras());
            mensaje.setIdDeRegion(-1);
            mensaje.setIdDeMonje(-1);
            mensaje.setTipo(0);    // Informativo
            //insert en base de datos.
            crearMensaje(mensaje);
        }

        log.debug("adMensajes. crearMensajesCorreo. medio");
        //creamos el mensaje para el emisor del correo
        mensaje = new Mensajes();
        mensaje.setIdDeAbadia(abadia.getIdDeAbadia());
        mensaje.setIdDeMonje(-1);
        mensaje.setIdDeRegion(-1);
        mensaje.setMensaje("Has enviado un correo a los siguientes destinatarios: " + correo.getDestinatario());
        mensaje.setFechaAbadia(CoreTiempo.getTiempoAbadiaStringConHoras());
        mensaje.setFechaReal(CoreTiempo.getTiempoRealStringConHoras());
        mensaje.setTipo(0);
        crearMensaje(mensaje);
        log.debug("adMensajes. crearMensajesCorreo. Fin");
    }

    public void crearMensaje(int abadiaid, int monjeid, int regionid, int tipo, String msg) throws AbadiaException {
        Mensajes mensaje = null;

        mensaje = new Mensajes();
        mensaje.setMensaje(msg);
        mensaje.setIdDeAbadia(abadiaid);
        mensaje.setFechaReal(CoreTiempo.getTiempoRealStringConHoras());
        mensaje.setFechaAbadia(CoreTiempo.getTiempoAbadiaStringConHoras());
        mensaje.setIdDeRegion(regionid);
        mensaje.setIdDeMonje(monjeid);
        mensaje.setIdDeIdioma(1);
        mensaje.setTipo(tipo);
        //insert en base de datos.
        crearMensaje(mensaje);
    }

    public void crearMensaje(int abadiaid, int monjeid, int regionid, int idiomaid, int tipo, String msg) throws AbadiaException {
        Mensajes mensaje = null;

        mensaje = new Mensajes();
        mensaje.setMensaje(msg);
        mensaje.setIdDeAbadia(abadiaid);
        mensaje.setFechaReal(CoreTiempo.getTiempoRealStringConHoras());
        mensaje.setFechaAbadia(CoreTiempo.getTiempoAbadiaStringConHoras());
        mensaje.setIdDeRegion(regionid);
        mensaje.setIdDeMonje(monjeid);
        mensaje.setIdDeIdioma(idiomaid);
        mensaje.setTipo(tipo);
        //insert en base de datos.
        crearMensaje(mensaje);
    }

    //Elimina un objeto Mensajes de la base de datos
    //a partir de la clave del Mensajes
    public boolean eliminarMensajesDeAbadia(long idAbadia) throws AbadiaSQLException {
        String sSQL = "Delete From `mensajes` Where `ABADIAID` = ?";
        PreparedStatement ps = null;
        try {
            ps = (PreparedStatement) con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave del monje
            int parNo = 1;
            ps.setLong(parNo, idAbadia);
            // Ejecutarlo
            return ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMensaje. eliminarMensajesDeAbadia. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    //Elimina un objeto Mensajes de la base de datos
    //a partir de la clave de abadias y tipo
    public boolean eliminarMensajesDeAbadia(long idAbadia, int tipo) throws AbadiaSQLException {
        String sSQL = "Delete From `mensajes` Where `ABADIAID` = ? AND `TIPO` = ?";
        PreparedStatement ps = null;
        try {
            ps = (PreparedStatement) con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave del monje
            int parNo = 1;
            ps.setLong(parNo++, idAbadia);
            ps.setInt(parNo, tipo);
            // Ejecutarlo
            return ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMensaje. eliminarMensajesDeAbadia. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    //Elimina un objeto Mensajes de la base de datos
    //a partir de la clave de abadias borra los mensajes antiguos
    public boolean eliminarMensajesDeAbadiaAntiguos(long idAbadia) throws AbadiaSQLException {
        String sSQL = "Delete From `mensajes` where abadiaid = ? and fechareal < Date_sub(Now(), Interval 15 day)";
        PreparedStatement ps = null;
        try {
            ps = (PreparedStatement) con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave del monje
            int parNo = 1;
            ps.setLong(parNo, idAbadia);
            // Ejecutarlo
            return ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMensaje. eliminarMensajesDeAbadiaAntiguos. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    // Crear mensajes basados en una lista de mensajes
    public void crearMensajes(ArrayList listaMensajes) throws AbadiaException {
        Iterator lista = listaMensajes.iterator();
        Mensajes mensaje = null;

        while (lista.hasNext()) {
            mensaje = (Mensajes) lista.next();
            mensaje.setFechaAbadia(CoreTiempo.getTiempoAbadiaStringConHoras());
            mensaje.setFechaReal(CoreTiempo.getTiempoRealStringConHoras());
            mensaje.setIdDeRegion(-1);
            crearMensaje(mensaje);
        }

    }

    // Crear mensajes basados en una lista de mensajes
    public void crearMensajesActividad(int idmonje, int idactividad, int idperiodo, String mensaje, float cantidad) throws AbadiaException {
        crearMensajeActividadMonje(idmonje, idactividad, idperiodo, mensaje, cantidad);
    }


    // Crear mensajes para todos los usuarios de una regi�n... se basa
    public void crearMensajesParaRegion(int regionid, int tipo, int literalid, String Param1, String Param2, String Param3, String Param4) throws AbadiaException {
        Mensajes mensaje = new Mensajes();
        String msg = "";
        // valores por defecto del mensaje
        mensaje.setIdDeRegion(regionid);
        mensaje.setIdDeAbadia(-1);
        mensaje.setIdDeMonje(-1);
        mensaje.setTipo(tipo);
        mensaje.setFechaReal(CoreTiempo.getTiempoRealStringConHoras());
        mensaje.setFechaAbadia(CoreTiempo.getTiempoAbadiaStringConHoras());
        // Para cada idioma
        // Para cada idioma
        adLiterales literalesAD = new adLiterales(con);
        for (int idioma = 1; idioma <= Constantes.IDIOMAS; idioma++) {
            msg = literalesAD.getLiteral(literalid, idioma);
            if (Param1 != null) msg = Utilidades.Format(msg, Param1);
            if (Param2 != null) msg = Utilidades.Format(msg, Param2);
            if (Param3 != null) msg = Utilidades.Format(msg, Param3);
            if (Param4 != null) msg = Utilidades.Format(msg, Param4);

            mensaje.setMensaje(msg);
            mensaje.setIdDeIdioma(idioma);
            crearMensaje(mensaje);
        }
    }


    // Crear mensajes basados en una lista de mensajes
    public void crearMensajesParaTodos(int tipo, int literalid, String Param1, String Param2, String Param3, String Param4) throws AbadiaException {
        // Crear mensajes basados en una lista de mensajes
        crearMensajesParaRegion(-1, tipo, literalid, Param1, Param2, Param3, Param4);
    }
}
