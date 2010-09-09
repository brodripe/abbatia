package org.abbatia.process.bbean;

import org.abbatia.adbean.adLiterales;
import org.abbatia.adbean.adMensajes;
import org.abbatia.adbean.adSolicitudes;
import org.abbatia.base.AccionesBase;
import org.abbatia.bean.Mensajes;
import org.abbatia.bean.Solicitud;
import org.abbatia.core.CoreTiempo;
import org.abbatia.dbms.ConnectionFactory;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaClassForNameException;
import org.abbatia.exception.AbadiaDBConnectionException;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.process.adbean.adProcesos;
import org.abbatia.process.utils.ProcesosUtils;
import org.abbatia.utils.Constantes;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * Gestiona el proceso de aprobación/denegacion de solicitudes
 */
public class ProcesosSolicitudesBBean extends ProcesosUtils {

    private static Logger log = Logger.getLogger(ProcesosSolicitudesBBean.class.getName());

    public ProcesosSolicitudesBBean() throws AbadiaDBConnectionException {
        super();
    }

    public void gestion_solicitudes() throws AbadiaException {
        String sTrace = this.getClass() + ".gestion_solicitudes()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adProcesos oProcesosAD;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);

            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando Proceso gestion_solicitudes", 0);
            actualizarEstados(con);
            oProcesosAD.addLog("- Finalizando Proceso gestion_solicitudes", 0);
        } catch (Exception e) {
            throw new AbadiaSQLException(sTrace + " ERROR: Proceso gestion_solicitudes", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public ArrayList recuperarSolicitudesAprobadas() throws AbadiaException {
        return null;
    }

    /**
     * Actualiza los estado de las solicitudes en función de sus votos y su configuracion por tipo
     *
     * @param p_cConnection conexión
     * @throws AbadiaException Excepción general
     */
    public void actualizarEstados(Connection p_cConnection) throws AbadiaException {
        adSolicitudes solicitudesAD = new adSolicitudes(p_cConnection);
        adLiterales oLiteralesAD = new adLiterales(p_cConnection);
        adMensajes oMensajesAD = new adMensajes(p_cConnection);

        ArrayList<Solicitud> alSolicitudes = solicitudesAD.recuperarSolicitudes(Constantes.SOLICITUD_ESTADO_PENDIENTE);

        ArrayList<Mensajes> mensajes = new ArrayList<Mensajes>();
        int last_idioma = 0;

        String msgSi = "", msgNo = "";
        int diasDesde;
        AccionesBase accion;
        int resultado;

        for (Solicitud solicitud : alSolicitudes) {
            if (last_idioma != solicitud.getIdIdioma()) {
                last_idioma = solicitud.getIdIdioma();
                //utilsAD = new adUtils(Constantes.DB_CONEXION_PROCESS);
                msgSi = oLiteralesAD.getLiteralStatic(solicitud.getLiteralSi(), solicitud.getIdIdioma());
                msgNo = oLiteralesAD.getLiteralStatic(solicitud.getLiteralNo(), solicitud.getIdIdioma());
                //utilsAD.finalize();
            }

            //calculo los dias transcurridos desde la creacion de la solicitud
            diasDesde = CoreTiempo.getDiferenciaDiasInt(solicitud.getFechaCreacion(), CoreTiempo.getTiempoAbadiaString());
            //si han pasado los dias preestablecidos en la solicitud tipo, utilizamos el valor por defecto
            if (diasDesde > solicitud.getDiasVigencia()) {
                //si la votacion está desierta....
                if (solicitud.getVotosPendiente() == solicitud.getVotosPendiente()) {
                    //si el valor por defecto es OK
                    if (solicitud.getValorDefecto() == Constantes.VOTACIONES_OK) {
                        resultado = Constantes.VOTACIONES_OK;
                    } else {
                        resultado = Constantes.VOTACIONES_NOOK;
                    }
                    //si la votacion no está desierta, evaluamos los resultados obtenidos
                } else {
                    //si el porcetaje de sies iguala o supera lo esperado..
                    if ((solicitud.getVotosSi() * 100 / solicitud.getTotalVotos()) >= solicitud.getVotosNecesarios()) {
                        resultado = Constantes.VOTACIONES_OK;
                    } else {
                        resultado = Constantes.VOTACIONES_NOOK;
                    }
                }
            }
            //si la solicitud no ha caducado, evaluamos los resultados
            else {
                //si se ha alcanzado el porcentaje exigido de Sies
                if ((solicitud.getVotosSi() * 100 / solicitud.getTotalVotos()) >= solicitud.getVotosNecesarios()) {
                    resultado = Constantes.VOTACIONES_OK;
                } else
                    //en caso contrario, verificamos si el procentaje de noes puede dar por cancelada la petición
                    if ((solicitud.getVotosNo() * 100 / solicitud.getTotalVotos()) >= solicitud.getVotosNecesarios()) {
                        resultado = Constantes.VOTACIONES_NOOK;
                    } else resultado = Constantes.VOTACIONES_PENDIENTE;
            }
            try {
                //si el valor por defecto es OK
                //solicitudAD = new adSolicitudes(Constantes.DB_CONEXION_PROCESS);
                if (resultado == Constantes.VOTACIONES_OK) {
                    //Instancio al clase que ha de resolver la acciones asociadas a la solicitud
                    accion = (AccionesBase) Class.forName(solicitud.getAccion()).newInstance();
                    //Invoco el metodo accionSi pasandole la solicitud
                    accion.accionSi(solicitud);
                    //añado mensaje de autorizacion
                    mensajes.add(new Mensajes(solicitud.getIdAbadia(), solicitud.getIdMonje(), msgSi, 0));
                    //actualiza el estado de la solicitud a Aprobada

                    solicitudesAD.actualizarEstado(Constantes.SOLICITUD_ESTADO_APROBADA, solicitud.getIdSolicitud());

                } else if (resultado == Constantes.VOTACIONES_NOOK) {
                    //Instancio al clase que ha de resolver la acciones asociadas a la solicitud
                    accion = (AccionesBase) Class.forName(solicitud.getAccion()).newInstance();
                    //invoco el método accionNo pasandole la solcitud
                    accion.accionNo(solicitud);
                    //añad mensaje de NO autorizacion
                    mensajes.add(new Mensajes(solicitud.getIdAbadia(), solicitud.getIdMonje(), msgNo, 0));
                    solicitudesAD.actualizarEstado(Constantes.SOLICITUD_ESTADO_CANCELADA, solicitud.getIdSolicitud());
                }
                //solicitudAD.finalize();
            } catch (ClassNotFoundException e) {
                throw new AbadiaClassForNameException("ProcesosSolicitudesBBean. actualizarEstado. ClassNotFoundException", e, log);
            } catch (InstantiationException e) {
                throw new AbadiaClassForNameException("ProcesosSolicitudesBBean. actualizarEstado. InstantiationException", e, log);
            } catch (IllegalAccessException e) {
                throw new AbadiaClassForNameException("ProcesosSolicitudesBBean. actualizarEstado. IllegalAccessException", e, log);
            }
        }

        oMensajesAD.crearMensajes(mensajes);
    }

}
