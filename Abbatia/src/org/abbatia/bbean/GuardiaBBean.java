package org.abbatia.bbean;

import org.abbatia.adbean.adMensajes;
import org.abbatia.adbean.adRecurso;
import org.abbatia.adbean.adSalarios;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.DatosSalarioBean;
import org.abbatia.bean.Mensajes;
import org.abbatia.core.CoreTiempo;
import org.abbatia.dbms.ConnectionFactory;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.NoDisponesDeGuardiasException;
import org.abbatia.exception.OroInsuficienteException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.exception.base.SystemException;
import org.abbatia.utils.Constantes;
import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;

import java.sql.Connection;

/**
 * Created by Benjamín Rodríguez.
 * User: benjamin
 * Date: 24-may-2008
 * Time: 21:06:39
 */
public class GuardiaBBean {
    private static Logger log = Logger.getLogger(GuardiaBBean.class.getName());


    public void contratarGuardia(Abadia p_oAbadia, MessageResources p_oResources) throws AbadiaException {
        String sTrace = this.getClass() + ".contratarGuardia()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adRecurso oRecursoAD;
        adSalarios oSalarioAD;
        adMensajes oMensajesAD;

        double dMonedas;
        int iSalarioGuardia;

        Mensajes oMensaje;

        Connection con = null;


        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS, Constantes.AUTOCOMIT_OF);
            oRecursoAD = new adRecurso(con);
            dMonedas = oRecursoAD.recuperarValorRecurso(Constantes.RECURSOS_ORO, p_oAbadia.getIdDeAbadia());
            oSalarioAD = new adSalarios(con);
            iSalarioGuardia = (int) oSalarioAD.recuperarSalario(Constantes.EMPLEADO_GUARDIA);
            //recuperamos el salario por guardia
            if (dMonedas < iSalarioGuardia) {
                throw new OroInsuficienteException("No dispones de oro suficiente", log);
            } else {
                //incrementamos en 1 el recurso "guardias"
                oRecursoAD = new adRecurso(con);
                oRecursoAD.sumarRecurso(Constantes.RECURSOS_GUARDIA, p_oAbadia.getIdDeAbadia(), 1);
                //la contratacion implica un coste equivalente a la mensualidad
                oRecursoAD.restarRecurso(Constantes.RECURSOS_ORO, p_oAbadia.getIdDeAbadia(), iSalarioGuardia);

                oMensaje = new Mensajes();
                //creo un mensaje para notificar la contratación
                oMensaje.setIdDeAbadia(p_oAbadia.getIdDeAbadia());
                oMensaje.setFechaReal(CoreTiempo.getTiempoRealStringConHoras());
                oMensaje.setFechaAbadia(CoreTiempo.getTiempoAbadiaStringConHoras());
                oMensaje.setIdDeRegion(p_oAbadia.getIdDeRegion());
                oMensaje.setIdDeMonje(-1);
                oMensaje.setTipo(1);
                oMensaje.setMensaje(p_oResources.getMessage("guardias.contratado"));
                oMensajesAD = new adMensajes(con);
                oMensajesAD.crearMensaje(oMensaje);
            }

            ConnectionFactory.commitTransaction(con);

        } catch (AbadiaException e) {
            ConnectionFactory.rollbackTransaction(con);
            throw new SystemException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public void despedirGuardia(Abadia p_oAbadia, MessageResources p_oResources) throws AbadiaException {
        String sTrace = this.getClass() + ".despedirGuardia()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adRecurso oRecursoAD;
        adSalarios oSalarioAD;
        adMensajes oMensajesAD;

        double dMonedas;
        int iNumGuardias;
        int iSalarioGuardia;
        Mensajes oMensaje;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS, Constantes.AUTOCOMIT_OF);
            oRecursoAD = new adRecurso(con);
            dMonedas = oRecursoAD.recuperarValorRecurso(Constantes.RECURSOS_ORO, p_oAbadia.getIdDeAbadia());
            iNumGuardias = (int) oRecursoAD.recuperarValorRecurso(Constantes.RECURSOS_GUARDIA, p_oAbadia.getIdDeAbadia());
            oSalarioAD = new adSalarios(con);
            iSalarioGuardia = (int) oSalarioAD.recuperarSalario(Constantes.EMPLEADO_GUARDIA);

            if (dMonedas < iSalarioGuardia) {
                throw new OroInsuficienteException("Oro insuficiente", log);
            } else {
                //miramos los guardias que tenemos
                //si no tiene guardias no decrementamos
                if (iNumGuardias <= 0) {
                    throw new NoDisponesDeGuardiasException("No dispones de guardias", log);
                } else {
                    //decrementar en 1 el recurso "guardias"
                    oRecursoAD.restarRecurso(Constantes.RECURSOS_GUARDIA, p_oAbadia.getIdDeAbadia(), 1);
                    //el despido implica un coste equivalente a la mensualidad
                    oRecursoAD.restarRecurso(Constantes.RECURSOS_ORO, p_oAbadia.getIdDeAbadia(), iSalarioGuardia);
                    //creo un mensaje para notificar el despido
                    oMensaje = new Mensajes();
                    oMensaje.setIdDeAbadia(p_oAbadia.getIdDeAbadia());
                    oMensaje.setFechaReal(CoreTiempo.getTiempoRealStringConHoras());
                    oMensaje.setFechaAbadia(CoreTiempo.getTiempoAbadiaStringConHoras());
                    oMensaje.setIdDeRegion(p_oAbadia.getIdDeRegion());
                    oMensaje.setIdDeMonje(-1);
                    oMensaje.setTipo(1);
                    oMensaje.setMensaje(p_oResources.getMessage("guardias.despedido"));
                    oMensajesAD = new adMensajes(con);
                    oMensajesAD.crearMensaje(oMensaje);
                }
            }

            ConnectionFactory.commitTransaction(con);

        } catch (AbadiaException e) {
            ConnectionFactory.rollbackTransaction(con);
            throw e;
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public DatosSalarioBean recuperarDatosGuardias(Abadia p_oAbadia) throws AbadiaException {
        String sTrace = this.getClass() + ".recuperarDatosGuardias()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adRecurso oRecursoAD;
        adSalarios oSalarioAD;
        DatosSalarioBean oDatosGuardia;
        Connection con = null;

        try {
            oDatosGuardia = new DatosSalarioBean();
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oRecursoAD = new adRecurso(con);
            oDatosGuardia.setNumGuardias((int) oRecursoAD.recuperarValorRecurso(Constantes.RECURSOS_GUARDIA, p_oAbadia.getIdDeAbadia()));
            oSalarioAD = new adSalarios(con);
            oDatosGuardia.setSalarioGuardia((int) oSalarioAD.recuperarSalario(Constantes.EMPLEADO_GUARDIA));
            return oDatosGuardia;
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }
}
