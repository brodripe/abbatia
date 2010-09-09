package org.abbatia.process.bbean;

import org.abbatia.adbean.adLiterales;
import org.abbatia.adbean.adMensajes;
import org.abbatia.adbean.adMonje;
import org.abbatia.adbean.adUtils;
import org.abbatia.bean.Mensajes;
import org.abbatia.bean.MonjeBase;
import org.abbatia.dbms.ConnectionFactory;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaDBConnectionException;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.process.adbean.adProcesos;
import org.abbatia.process.utils.ProcesosUtils;
import org.abbatia.utils.Constantes;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.util.ArrayList;

public class ProcesosMonjesBBean extends ProcesosUtils {

    private static Logger log = Logger.getLogger(ProcesosMonjesBBean.class.getName());

    public ProcesosMonjesBBean() throws AbadiaDBConnectionException {
        super();
    }

    public void pasarMonjesOsario(String sDias) throws AbadiaException {
        String sTrace = this.getClass() + ".pasarMonjesOsario()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adUtils oUtilsAD;
        adMonje oMonjeAD;
        adProcesos oProcesosAD;
        adLiterales oLiteralesAD;
        adMensajes oMensajesAD;

        ArrayList<Mensajes> mensajes = new ArrayList<Mensajes>();
        ArrayList<MonjeBase> alMonjes;


        int last_idiomaid = -1;
        String msg, msg1, msg_Osario = "";

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);

            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando Proceso Pasar Monjes Muertos al Osario", 0);

            oLiteralesAD = new adLiterales(con);
            oMonjeAD = new adMonje(con);
            oUtilsAD = new adUtils(con);
            alMonjes = oMonjeAD.recuperarMonjesParaOsario(Integer.valueOf(sDias));

            for (MonjeBase monje : alMonjes) {
                if (last_idiomaid != monje.getIdIdioma()) {
                    last_idiomaid = monje.getIdIdioma();
                    msg_Osario = oLiteralesAD.getLiteralStatic(10300, last_idiomaid); // Los restos del hermano %s de %s han sido trasladados al Osario
                }
                msg = Format(msg_Osario, monje.getNombre());
                msg1 = Format(msg, monje.getApellido1());
                mensajes.add(new Mensajes(monje.getIdAbadia(), -1, msg1, 0));
                oUtilsAD.execSQL("UPDATE monje_cementerio set estado = " + Constantes.MONJE_OSARIO + " WHERE monjeid = " + monje.getIdMonje());
            }

            oMensajesAD = new adMensajes(con);
            oMensajesAD.crearMensajes(mensajes);

            oProcesosAD.addLog("- Finalizando Proceso Pasar Monjes Muertos al Osario", 0);
        } catch (Exception e) {
            throw new AbadiaSQLException(sTrace + " ERROR: Proceso Pasar Monjes Muertos al Osario", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    /**
     * Genera mensajes de advertencia avisando de que determinados monjes tienes tareas sin asignar a ningua actividad
     *
     * @throws AbadiaException excepción general
     */
    public void notificarMonjesOciosos() throws AbadiaException {
        String sTrace = this.getClass() + ".notificarMonjesOciosos()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adMonje oMonjeAD;
        adProcesos oProcesosAD;
        adLiterales oLiteralesAD;
        adMensajes oMensajesAD;

        ArrayList<Mensajes> mensajes = new ArrayList<Mensajes>();
        ArrayList<MonjeBase> alMonjes;


        int last_idiomaid = -1;
        String msg, msg1, msg_Ocioso = "";

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);

            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando Proceso Notificar monjes Ociosos", 0);

            oLiteralesAD = new adLiterales(con);
            oMonjeAD = new adMonje(con);
            alMonjes = oMonjeAD.recuperarMonjesOciosos();


            for (MonjeBase monje : alMonjes) {
                if (last_idiomaid != monje.getIdIdioma()) {
                    last_idiomaid = monje.getIdIdioma();
                    msg_Ocioso = oLiteralesAD.getLiteralStatic(10301, last_idiomaid); // Los restos del hermano %s de %s han sido trasladados al Osario
                }
                msg = Format(msg_Ocioso, monje.getNombre());
                msg1 = Format(msg, monje.getApellido1());
                mensajes.add(new Mensajes(monje.getIdAbadia(), monje.getIdMonje(), msg1, 0));
            }

            oMensajesAD = new adMensajes(con);
            oMensajesAD.crearMensajes(mensajes);

            oProcesosAD.addLog("- Finalizando Proceso Notificar monjes Ociosos", 0);
        } catch (Exception e) {
            throw new AbadiaSQLException(sTrace + " ERROR: Proceso Notificar monjes Ociosos", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

}
