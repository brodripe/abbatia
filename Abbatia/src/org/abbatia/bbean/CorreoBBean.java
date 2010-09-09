package org.abbatia.bbean;

import org.abbatia.actionform.MensajeActForm;
import org.abbatia.adbean.adCorreo;
import org.abbatia.adbean.adMensajes;
import org.abbatia.adbean.adRecurso;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Correo;
import org.abbatia.core.CoreTiempo;
import org.abbatia.dbms.ConnectionFactory;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AccesoDenegadoException;
import org.abbatia.exception.ExcedidoNumDestinatariosException;
import org.abbatia.exception.OroInsuficienteException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.exception.base.SystemException;
import org.abbatia.utils.Constantes;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.util.StringTokenizer;

/**
 * Created by Benjamín Rodríguez.
 * User: benjamin
 * Date: 24-may-2008
 * Time: 21:06:39
 */
public class CorreoBBean {
    private static Logger log = Logger.getLogger(CorreoBBean.class.getName());


    public Correo recuperarCorreo(String p_szCorreoId, Abadia p_oAbadia) throws AbadiaException {
        String sTrace = this.getClass() + ".recuperarCorreo()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adCorreo oCorreoAD;
        Correo oCorreo;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oCorreoAD = new adCorreo(con);
            oCorreo = oCorreoAD.recuperarDatosCorreo(p_szCorreoId, p_oAbadia);
            return oCorreo;
        } catch (SystemException e) {
            throw e;
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public int aceptarEnvio(MensajeActForm p_afMensaje, Abadia p_oAbadia) throws AbadiaException {
        String sTrace = this.getClass() + ".aceptarEnvio()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adCorreo oCorreoAD;
        adRecurso oRecursoAD;
        adMensajes oMensajesAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);

            //verificamos si la abbatia dispone de oro suficiente para enviar el mensaje.
            oRecursoAD = new adRecurso(con);
            double dOroDisponible = oRecursoAD.recuperarValorRecurso(Constantes.RECURSOS_ORO, p_oAbadia.getIdDeAbadia());
            StringTokenizer stDestinatarios = new StringTokenizer(p_afMensaje.getDestinatarioid(), ";");
            int numDestinatarios = stDestinatarios.countTokens();

            //si el número de destinatarios supera el límite establecido, no dejamos enviar el mensaje...
            if (numDestinatarios > Constantes.VARIOS_MAX_DESTINATARIOS) {
                throw new ExcedidoNumDestinatariosException("Excedido el número de destinatarios", log);
                //mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.correo.superado.limite.destinatarios", String.valueOf(Constantes.VARIOS_MAX_DESTINATARIOS)));
                //saveMessages(request.getSession(), mensajes);
                //return mapping.findForward("mensajes");
            }

            if (dOroDisponible < numDestinatarios * Constantes.VARIOS_COSTE_ENVIO_MENSAJE) {
                throw new OroInsuficienteException("Oro insuficiente", log);
                //mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.correo.oroinsuficiente", String.valueOf(numDestinatarios*Constantes.VARIOS_COSTE_ENVIO_MENSAJE)));
                //saveMessages(request.getSession(), mensajes);
                //return mapping.findForward("mensajes");
            }

            Correo correo = new Correo();
            correo.setIdAbadiaOrigen(p_oAbadia.getIdDeAbadia());
            correo.setIdAbadiasDestino(p_afMensaje.getDestinatarioid());
            correo.setTexto(p_afMensaje.getMsg());
            correo.setFecha_real(CoreTiempo.getTiempoRealStringConHoras());
            correo.setFecha_abadia(CoreTiempo.getTiempoAbadiaStringConHoras());

            oCorreoAD = new adCorreo(con);
            oCorreoAD.crearCorreo(correo);

            p_afMensaje.setCorreos(oCorreoAD.recuperarEnviados(p_oAbadia));

            oMensajesAD = new adMensajes(con);
            oMensajesAD.crearMensajesCorreo(p_afMensaje, p_oAbadia);
            p_afMensaje.setAccion(null);
//            request.setAttribute("MensajeForm", mensaje);
            return numDestinatarios;
        } catch (SystemException e) {
            throw e;
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public void eliminarEnviados(MensajeActForm p_afMensaje, Abadia p_oAbadia) throws AbadiaException {
        String sTrace = this.getClass() + ".eliminarEnviados()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adCorreo oCorreoAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oCorreoAD = new adCorreo(con);
            if (p_afMensaje.getSeleccion().length > 0) {
                for (int iCount = 0; iCount < p_afMensaje.getSeleccion().length; iCount++) {
                    oCorreoAD.borrarCorreoOrigen(p_afMensaje.getSeleccion()[iCount]);
                }
            }
            p_afMensaje.setCorreos(oCorreoAD.recuperarEnviados(p_oAbadia));

        } catch (SystemException e) {
            throw e;
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public void mostrarEnviado(String p_szCorreoId, MensajeActForm p_afMensaje, Abadia p_oAbadia) throws AbadiaException {
        String sTrace = this.getClass() + ".mostrarEnviado()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adCorreo oCorreoAD;

        Correo oCorreo;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oCorreoAD = new adCorreo(con);
            oCorreo = oCorreoAD.recuperarDatosCorreo(p_szCorreoId, p_oAbadia);

            //verificar si el mensaje es nuestro, de lo contrario generar mensaje de tramposo...
            if (oCorreo.getIdAbadiaOrigen() == p_oAbadia.getIdDeAbadia()) {
                oCorreoAD.marcarLeido(p_szCorreoId, p_oAbadia);
            } else {
                throw new AccesoDenegadoException("Acceso denegado a correo", log);
            }
            p_afMensaje.setDesde(oCorreo.getAbadiaOrigen());
            p_afMensaje.setDestinatario(oCorreo.getAbadiaDestino());
            p_afMensaje.setMsg(oCorreo.getTexto());
            p_afMensaje.setTipo("consulta");
            p_afMensaje.setDireccion("salida");

        } catch (SystemException e) {
            throw e;
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public void recuperarEnviados(MensajeActForm p_afMensaje, Abadia p_oAbadia) throws AbadiaException {
        String sTrace = this.getClass() + ".recuperarEnviados()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adCorreo oCorreoAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oCorreoAD = new adCorreo(con);
            p_afMensaje.setCorreos(oCorreoAD.recuperarEnviados(p_oAbadia));
        } catch (SystemException e) {
            throw e;
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public void eliminarRecibidos(MensajeActForm p_afMensaje, String p_szCorreoId, Abadia p_oAbadia) throws AbadiaException {
        String sTrace = this.getClass() + ".eliminarRecibidos()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adCorreo oCorreoAD;

        Correo oCorreo;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oCorreoAD = new adCorreo(con);
            if (p_afMensaje.getSeleccion() != null && p_afMensaje.getSeleccion().length > 0) {
                for (int iCount = 0; iCount < p_afMensaje.getSeleccion().length; iCount++) {
                    oCorreoAD.borrarCorreoDestinatario(p_afMensaje.getSeleccion()[iCount], p_oAbadia);
                }
            } else {
                // Borrar el correo
                if ((p_szCorreoId != null)) {
                    oCorreo = oCorreoAD.recuperarDatosCorreo(p_szCorreoId, p_oAbadia);
                    //verificar si el mensaje es para nosotros, y de lo contrario, mostrar mensaje de tramposo...
                    if (oCorreo.getAbadiaDestino().indexOf(p_oAbadia.getNombre()) > -1) {
                        oCorreoAD.borrarCorreoStatic(p_szCorreoId, p_oAbadia);
                    } else {
                        throw new AccesoDenegadoException("Acceso no permitido a correo", log);
                        //estás intentando ver un correo que no es para ti.
                        //mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.tramposo.borrarcorreo"));
                        //saveMessages(request.getSession(), mensajes);
                        //return mapping.findForward("mensajes");
                    }
                }
            }
            p_afMensaje.setCorreos(oCorreoAD.recuperarRecibidos(p_oAbadia));

        } catch (SystemException e) {
            throw e;
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public void mostrarRecibido(String p_szCorreoId, MensajeActForm p_afMensaje, Abadia p_oAbadia) throws AbadiaException {
        String sTrace = this.getClass() + ".mostrarRecibido()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adCorreo oCorreoAD;

        Correo oCorreo;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oCorreoAD = new adCorreo(con);
            oCorreo = oCorreoAD.recuperarDatosCorreo(p_szCorreoId, p_oAbadia);

            //verificar si el mensaje es para nosotros, y de lo contrario, mostrar mensaje de tramposo...
            if (oCorreo.getAbadiaDestino().indexOf(p_oAbadia.getNombre()) > -1) {
                oCorreoAD.marcarLeido(p_szCorreoId, p_oAbadia);
            } else {
                throw new AccesoDenegadoException(sTrace + " Acceso denegado a correo", log);
                //estás intentando ver un correo que no es para ti.
                //mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.tramposo.accesocorreo"));
                //saveMessages(request.getSession(), mensajes);
                //return mapping.findForward("mensajes");
            }

            p_afMensaje.setIdAbadiaOrigen(oCorreo.getIdAbadiaOrigen());
            p_afMensaje.setDestinatarioid(oCorreo.getIdAbadiasDestino());
            p_afMensaje.setDesde(oCorreo.getAbadiaOrigen());
            p_afMensaje.setDestinatario(oCorreo.getAbadiaDestino());
            p_afMensaje.setMsg(oCorreo.getTexto());
            p_afMensaje.setTipo("consulta");
            p_afMensaje.setDireccion("entrada");

        } catch (SystemException e) {
            throw e;
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public void recuperarRecibidos(MensajeActForm p_afMensaje, Abadia p_oAbadia) throws AbadiaException {
        String sTrace = this.getClass() + ".recuperarRecibidos()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adCorreo oCorreoAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oCorreoAD = new adCorreo(con);
            p_afMensaje.setCorreos(oCorreoAD.recuperarRecibidos(p_oAbadia));
        } catch (SystemException e) {
            throw e;
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

}
