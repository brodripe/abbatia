package org.abbatia.process;

import org.abbatia.adbean.adAbadia;
import org.abbatia.adbean.adPlanificador;
import org.abbatia.bbean.PlanificadorBBean;
import org.abbatia.bean.datosProceso;
import org.abbatia.core.CoreMail;
import org.abbatia.dbms.ConnectionFactory;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.process.adbean.adProcesos;
import org.abbatia.utils.Constantes;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.TimerTask;

/**
 * Created by Benjamín Rodríguez.
 * User: Benjamin
 * Date: 12-sep-2004
 * Time: 2:22:23
 */
public class prPlanificador extends TimerTask {
    private static Logger log = Logger.getLogger(prPlanificador.class.getName());

    public void run() {
        prMain main;
        datosProceso proceso = null;
        adPlanificador planif;
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);
            planif = new adPlanificador(con);
            int status = planif.recuperarProcesoStatus();
            proceso = planif.recuperarProcesoPendiente();
            if (status == 0) {
                log.info("Planificador inactivo por base de datos");
                //this.interrupt();
                return;
            }
            if (proceso != null) {
                log.info("Lanzo prMain con idProceso: " + proceso.getIdProceso());
                PlanificadorBBean.errorRecuperadoPlanificador();
                main = new prMain();
                main.lanzar(proceso, con);
                log.info("Fin prMain con idProceso: " + proceso.getIdProceso());
            }

        } catch (Exception e) {
            try {
                log.error("prPlanificador. run.", e);
                //si un proceso peta, debemos desprogramarlo y enviar un mensaje a los administradores..
                planif = new adPlanificador(con);
                planif.bloquearProcesosStatus();
                PlanificadorBBean.errorPlanificador();
                if (proceso == null) {
                    adAbadia abadiaAD = new adAbadia(con);
                    ArrayList listaCorreo = abadiaAD.getMailAdministradores();

                    String asunto = "Se ha generado una excepción en el proceso: " + proceso.getIdProceso() + " - " + proceso.getName() + ", se paran todos los procesos... y precisa de la intervención de un Administrador";
                    CoreMail.enviarNotificacionAlta(listaCorreo, asunto);
                }
                //dejamos traza en base de datos
                adProcesos procesos = new adProcesos(con);
                procesos.addLog("ERROR en el planificador: = " + e, 1);
            } catch (AbadiaException e1) {
                log.error("prPlanificador. AbadiaException", e1);
            } catch (Exception e1) {
                log.error("prPlanificador. AbadiaException", e1);
            }
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
        }
    }

}
