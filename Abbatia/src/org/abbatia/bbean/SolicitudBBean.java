package org.abbatia.bbean;

import org.abbatia.adbean.adSolicitudes;
import org.abbatia.base.AccionesBase;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Solicitud;
import org.abbatia.bean.Usuario;
import org.abbatia.dbms.ConnectionFactory;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.SolicitudSinProcesarException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.exception.base.SystemException;
import org.abbatia.utils.Constantes;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Benjamín Rodríguez.
 * User: benjamin
 * Date: 24-may-2008
 * Time: 21:06:39
 */
public class SolicitudBBean {
    private static Logger log = Logger.getLogger(SolicitudBBean.class.getName());


    public HashMap<String, ArrayList<Solicitud>> listarSolicitudes(Abadia p_oAbadia, Usuario p_oUsuario) throws AbadiaException {
        String sTrace = this.getClass() + ".listarSolicitudes()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adSolicitudes oSolicitudesAD;

        HashMap<String, ArrayList<Solicitud>> hmRequest = new HashMap<String, ArrayList<Solicitud>>();

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oSolicitudesAD = new adSolicitudes(con);
            hmRequest.put("solicitudes_propias", oSolicitudesAD.recuperarListaPropias(p_oAbadia, p_oUsuario, Constantes.SOLICITUD_TIPO_VIAJE));
            hmRequest.put("solicitudes_propias_copia", oSolicitudesAD.recuperarListaPropias(p_oAbadia, p_oUsuario, Constantes.SOLICITUD_TIPO_VIAJE_COPIA));
            hmRequest.put("solicitudes_terceros", oSolicitudesAD.recuperarListaTerceros(p_oAbadia, p_oUsuario, Constantes.SOLICITUD_TIPO_VIAJE));
            hmRequest.put("solicitudes_terceros_copia", oSolicitudesAD.recuperarListaTerceros(p_oAbadia, p_oUsuario, Constantes.SOLICITUD_TIPO_VIAJE_COPIA));

            return hmRequest;

        } catch (AbadiaException e) {
            throw new SystemException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public HashMap<String, ArrayList<Solicitud>> solicitudCancelacion(int p_iSolicitudId, Abadia p_oAbadia, Usuario p_oUsuario) throws AbadiaException {
        String sTrace = this.getClass() + ".solicitudCancelacion()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adSolicitudes oSolicitudAD;

        Solicitud oSolicitud;
        AccionesBase oAccion;

        HashMap<String, ArrayList<Solicitud>> hmRequest = new HashMap<String, ArrayList<Solicitud>>();

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            //recuperamos los datos de la solicitud
            oSolicitudAD = new adSolicitudes(con);
            oSolicitud = oSolicitudAD.recuperarSolicitud(p_iSolicitudId);

            //si localizamos la solicitud..
            if (oSolicitud != null) {
                //evaluamos el estado de la solicitud para que no se pueda eliminar si aun no se ha procesado...
                if (oSolicitud.getEstado() == Constantes.SOLICITUD_ESTADO_PENDIENTE) {
                    throw new SolicitudSinProcesarException(sTrace, log);
                }

                //Instanciamos la clase correspondiente en función del tipo de solicitud
                oAccion = (AccionesBase) Class.forName(oSolicitud.getAccion()).newInstance();
                //invocamos el metodo de eliminacion de acción
                oAccion.accionEliminar(oSolicitud);
                //eliminamos los registros asociados a la solicitud
                oSolicitudAD.cancelarSolicitud(p_oAbadia, p_iSolicitudId);
            }
            //recargar la lista de solicitudes...
            hmRequest.put("solicitudes_propias", oSolicitudAD.recuperarListaPropias(p_oAbadia, p_oUsuario, Constantes.SOLICITUD_TIPO_VIAJE));
            hmRequest.put("solicitudes_propias_copia", oSolicitudAD.recuperarListaPropias(p_oAbadia, p_oUsuario, Constantes.SOLICITUD_TIPO_VIAJE_COPIA));
            hmRequest.put("solicitudes_terceros", oSolicitudAD.recuperarListaTerceros(p_oAbadia, p_oUsuario, Constantes.SOLICITUD_TIPO_VIAJE));
            hmRequest.put("solicitudes_terceros_copia", oSolicitudAD.recuperarListaTerceros(p_oAbadia, p_oUsuario, Constantes.SOLICITUD_TIPO_VIAJE_COPIA));
            return hmRequest;
        } catch (ClassNotFoundException e) {
            throw new SystemException(sTrace, e, log);
        } catch (IllegalAccessException e) {
            throw new SystemException(sTrace, e, log);
        } catch (InstantiationException e) {
            throw new SystemException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public HashMap<String, ArrayList<Solicitud>> solicitudSiNo(int p_iSolicitudId, Abadia p_oAbadia, Usuario p_oUsuario,
                                                               short p_sValorSolicitud) throws AbadiaException {
        String sTrace = this.getClass() + ".solicitudNo()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adSolicitudes oSolicitudAD;

        HashMap<String, ArrayList<Solicitud>> hmRequest = new HashMap<String, ArrayList<Solicitud>>();

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oSolicitudAD = new adSolicitudes(con);
            oSolicitudAD.actualizarVoto(p_iSolicitudId, p_oAbadia.getIdDeAbadia(), p_sValorSolicitud);
            hmRequest.put("solicitudes_propias", oSolicitudAD.recuperarListaPropias(p_oAbadia, p_oUsuario, Constantes.SOLICITUD_TIPO_VIAJE));
            hmRequest.put("solicitudes_propias_copia", oSolicitudAD.recuperarListaPropias(p_oAbadia, p_oUsuario, Constantes.SOLICITUD_TIPO_VIAJE_COPIA));
            hmRequest.put("solicitudes_terceros", oSolicitudAD.recuperarListaTerceros(p_oAbadia, p_oUsuario, Constantes.SOLICITUD_TIPO_VIAJE));
            hmRequest.put("solicitudes_terceros_copia", oSolicitudAD.recuperarListaTerceros(p_oAbadia, p_oUsuario, Constantes.SOLICITUD_TIPO_VIAJE_COPIA));
            return hmRequest;
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

}
