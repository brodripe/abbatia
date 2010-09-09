package org.abbatia.bbean;

import org.abbatia.actionform.SiembraActForm;
import org.abbatia.adbean.*;
import org.abbatia.bean.*;
import org.abbatia.core.CoreTiempo;
import org.abbatia.dbms.ConnectionFactory;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.EspacioInsuficienteEnCampo;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.process.utils.ProcesosUtils;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.Utilidades;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Benjamín Rodríguez.
 * User: benjamin
 * Date: 24-may-2008
 * Time: 21:06:39
 */
public class CultivoBBean {
    private static Logger log = Logger.getLogger(CultivoBBean.class.getName());

    public HashMap<String, Object> ararCampo(Abadia p_oAbadia, Usuario p_oUsuario, String p_sCampoId,
                                             ActionMapping mapping, MessageResources p_oResource) throws AbadiaException {
        String sTrace = this.getClass() + ".ararCampo()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adEdificio oEdificioAD;
        adCampo oCampoAD;
        adMensajes oMensajesAD;

        Edificio oEdificio;
        Cultivo oCultivo;
        Confirmacion oConfirmacion;
        Mensajes oMensaje;

        HashMap<String, Object> hmRequest = new HashMap<String, Object>();

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS, Constantes.AUTOCOMIT_OF);
            oEdificioAD = new adEdificio(con);
            oEdificio = oEdificioAD.recuperarEdificioTipo(Constantes.EDIFICIO_CAMPO, p_oAbadia, p_oUsuario);
            hmRequest.put("Edificio", oEdificio);

            if (!mapping.getParameter().equals("confirmar")) {
                //dado que ahora se podra arar en campoAD en cualquier punto del proceso de cultivo
                //verificaremos el estado en el que se encuentra el campoAD actualmente y mostraremos una alerta
                //indicando que se perdera la cosecha si se dedide arar en mitad del cultivo....

                oCampoAD = new adCampo(con);
                oCultivo = oCampoAD.recuperarDatosCultivo(p_oAbadia.getIdDeAbadia(), Integer.parseInt(p_sCampoId));

                if (oCultivo.getIdEstado() == Constantes.ESTADO_CULTIVO_CULTIVANDO || oCultivo.getIdEstado() == Constantes.ESTADO_CULTIVO_RECOGIENDO) {
                    oConfirmacion = new Confirmacion();
                    //cuidado, si decides arar el campo, perderás la cosecha actual, estás seguro....
                    //mostrar dialogo estándar con opciones si o no.
                    //accion si
                    oConfirmacion.setAccionSi("/confirmarArado.do?clave=" + p_sCampoId);
                    //accion no
                    oConfirmacion.setAccionNo("/mostrarEdificio.do?clave=" + oEdificio.getIdDeEdificio());
                    //texto del mensaje
                    oConfirmacion.setTextoConfirmacion(p_oResource.getMessage("mensajes.aviso.cultivo.arar"));
                    //titulo del diálogo
                    oConfirmacion.setTitulo(p_oResource.getMessage("mensajes.aviso.cultivo.arar.titulo"));

                    hmRequest.put("DatosConfirmacion", oConfirmacion);
                    ConnectionFactory.commitTransaction(con);
                    return hmRequest;
                    //request.setAttribute("DatosConfirmacion", oConfirmacion);

                    //return mapping.findForward("confirmar");
                }
            }
            oCampoAD = new adCampo(con);
            oCampoAD.limpiarCampo(Integer.parseInt(p_sCampoId), p_oAbadia.getIdDeAbadia(), Constantes.ESTADO_CULTIVO_ARANDO);

            oMensajesAD = new adMensajes(con);
            oMensaje = new Mensajes();
            oMensaje.setIdDeAbadia(p_oAbadia.getIdDeAbadia());

            oMensaje.setMensaje(p_oResource.getMessage("mensajes.info.arandocampo"));
            oMensaje.setIdDeRegion(p_oAbadia.getIdDeRegion());
            oMensaje.setFechaAbadia(CoreTiempo.getTiempoAbadiaStringConHoras());
            oMensaje.setFechaReal(CoreTiempo.getTiempoRealStringConHoras());
            oMensaje.setIdDeMonje(-1);
            oMensajesAD.crearMensaje(oMensaje);
            ConnectionFactory.commitTransaction(con);
            return hmRequest;

        } catch (AbadiaException e) {
            ConnectionFactory.rollbackTransaction(con);
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public Edificio cancelarCampo(String p_sCampoId, Abadia p_oAbadia, Usuario p_oUsuario, MessageResources p_oResource) throws AbadiaException {
        String sTrace = this.getClass() + ".cancelarCampo()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adCampo oCampoAD;
        adEdificio oEdificioAD;
        adMensajes oMensajeAD;

        Edificio oEdificio;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS, Constantes.AUTOCOMIT_OF);
            oCampoAD = new adCampo(con);
            oCampoAD.cancelarCampo(Integer.valueOf(p_sCampoId), p_oAbadia.getIdDeAbadia());

            oMensajeAD = new adMensajes(con);
            Mensajes mensaje = new Mensajes();
            mensaje.setIdDeAbadia(p_oAbadia.getIdDeAbadia());
            mensaje.setMensaje(p_oResource.getMessage("mensajes.info.cancelarcampo"));
            mensaje.setIdDeRegion(p_oAbadia.getIdDeRegion());
            mensaje.setFechaAbadia(CoreTiempo.getTiempoAbadiaStringConHoras());
            mensaje.setFechaReal(CoreTiempo.getTiempoRealStringConHoras());
            mensaje.setIdDeMonje(-1);
            oMensajeAD.crearMensaje(mensaje);

            oEdificioAD = new adEdificio(con);
            oEdificio = oEdificioAD.recuperarEdificioTipo(Constantes.EDIFICIO_CAMPO, p_oAbadia, p_oUsuario);

            ConnectionFactory.commitTransaction(con);
            return oEdificio;

        } catch (AbadiaException e) {
            ConnectionFactory.rollbackTransaction(con);
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public ActionForward crearCultivo(Abadia p_oAbadia, Usuario p_oUsuario, MessageResources p_oResource) throws AbadiaException {
        String sTrace = this.getClass() + ".crearCultivo()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adCampo oCampoAD;
        adEdificio oEdificioAD;
        adMensajes oMensajeAD;

        Edificio oEdificio;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oEdificioAD = new adEdificio(con);
            oEdificio = oEdificioAD.recuperarEdificioTipo(Constantes.EDIFICIO_CAMPO, p_oAbadia, p_oUsuario);

            oCampoAD = new adCampo(con);
            int actual = oCampoAD.recuperaNumeroDeCampos(p_oAbadia);
            if (actual < oEdificio.getAlmacenamiento()) {
                oCampoAD.crearCampo(p_oAbadia);
            } else {
                throw new EspacioInsuficienteEnCampo(sTrace, oEdificio, log);
            }

            Mensajes mensaje = new Mensajes();
            mensaje.setIdDeAbadia(p_oAbadia.getIdDeAbadia());
            mensaje.setMensaje(p_oResource.getMessage("mensajes.info.campocreado"));
            mensaje.setIdDeRegion(p_oAbadia.getIdDeRegion());
            mensaje.setFechaAbadia(CoreTiempo.getTiempoAbadiaStringConHoras());
            mensaje.setFechaReal(CoreTiempo.getTiempoRealStringConHoras());
            mensaje.setIdDeMonje(-1);
            oMensajeAD = new adMensajes(con);
            oMensajeAD.crearMensaje(mensaje);

            Utilidades.eliminarRegistroContext(p_oUsuario.getNick());
            return new ActionForward("/mostrarEdificio.do?clave=" + oEdificio.getIdDeEdificio());

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public Object sembrarCampoInicio(Abadia p_oAbadia, Usuario p_oUsuario, int p_iCampoId, String p_szParameter,
                                     MessageResources p_oResource) throws AbadiaException {
        String sTrace = this.getClass() + ".sembrarCampoInicio()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adCampo oCampoAD;
        adEdificio oEdificioAD;
        adRecurso oRecursoAD;

        Edificio oEdificio;
        Cultivo oCultivo;
        Confirmacion oConfirmacion;
        SiembraActForm oSiembra;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oCampoAD = new adCampo(con);
            oCultivo = oCampoAD.recuperarDatosCultivo(p_oAbadia.getIdDeAbadia(), p_iCampoId);

            if (oCultivo.getIdEstado() == Constantes.ESTADO_CULTIVO_ARANDO && oCultivo.getArado_total() < Constantes.CULTIVO_TOTAL_ARADO && p_szParameter.equals("prematura")) {
                oEdificioAD = new adEdificio(con);
                oEdificio = oEdificioAD.recuperarEdificioTipo(Constantes.EDIFICIO_CAMPO, p_oAbadia, p_oUsuario);

                oConfirmacion = new Confirmacion();
                //cuidado, si decides arar el campo, perderás la cosecha actual, estás seguro....
                //mostrar dialogo estándar con opciones si o no.
                //accion si
                oConfirmacion.setAccionSi("/sembrar_campo.do?clave=" + p_iCampoId);
                //accion no
                oConfirmacion.setAccionNo("/mostrarEdificio.do?clave=" + oEdificio.getIdDeEdificio());
                //texto del mensaje
                oConfirmacion.setTextoConfirmacion(p_oResource.getMessage("mensajes.aviso.cultivo.sembrar.anticipado"));
                //titulo del diálogo
                oConfirmacion.setTitulo(p_oResource.getMessage("mensajes.aviso.cultivo.sembrar.anticipado.titulo"));

                return oConfirmacion;
            }

            oSiembra = new SiembraActForm();
            oSiembra.setIdCampo(p_iCampoId);
            oRecursoAD = new adRecurso(con);
            oSiembra.setSemillas(oRecursoAD.recuperarSemillas(p_oAbadia, p_oUsuario));
            return oSiembra;
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public Edificio sembrarCampoConfirmacion(Abadia p_oAbadia, Usuario p_oUsuario, SiembraActForm p_afSiembra,
                                             MessageResources p_oResource) throws AbadiaException {
        String sTrace = this.getClass() + ".sembrarCampoConfirmacion()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adCampo oCampoAD;
        adEdificio oEdificioAD;
        adRecurso oRecursoAD;
        adMensajes oMensajesAD;

        Cultivo oCultivo;
        Mensajes oMensaje;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oCampoAD = new adCampo(con);
            oCultivo = oCampoAD.recuperarDatosCultivo(p_oAbadia.getIdDeAbadia(), p_afSiembra.getIdCampo());

            oCampoAD.sembrarCampo(p_afSiembra.getIdCampo(), p_afSiembra.getSeleccion(), p_oAbadia.getIdDeAbadia(), oCultivo.getArado_total());

            oRecursoAD = new adRecurso(con);
            oRecursoAD.restarRecurso(p_afSiembra.getSeleccion(), p_oAbadia.getIdDeAbadia(), 1);

            oMensaje = new Mensajes();
            oMensaje.setIdDeAbadia(p_oAbadia.getIdDeAbadia());
            oMensaje.setMensaje(p_oResource.getMessage("mensajes.info.sembrandocampo"));
            oMensaje.setIdDeRegion(p_oAbadia.getIdDeRegion());
            oMensaje.setFechaAbadia(CoreTiempo.getTiempoAbadiaStringConHoras());
            oMensaje.setFechaReal(CoreTiempo.getTiempoRealStringConHoras());
            oMensaje.setIdDeMonje(-1);
            oMensajesAD = new adMensajes(con);
            oMensajesAD.crearMensaje(oMensaje);


            oEdificioAD = new adEdificio(con);
            return oEdificioAD.recuperarEdificioTipo(Constantes.EDIFICIO_CAMPO, p_oAbadia, p_oUsuario);

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    /**
     * Valida los acumulados diarios de cultivo para determinar si se han alcanzado los
     * mínimos requeridos por cultivo y decrementa los puntos de cultivo que se obtendran y muestra un mensaje
     * si no se alcanzan...
     *
     * @throws AbadiaException Excepción general
     */
    public void gestionarPuntosCultivo() throws AbadiaException {
        String sTrace = this.getClass() + ".gestionarPuntosCultivo()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        ArrayList<Cultivo> alCampos;

        double dPuntosRestar;

        adUtils oUtilsAD;
        adCampo oCampoAD;
        adMensajes oMensajesAD;

        String msg = "";

        ArrayList<Mensajes> alMensajes = new ArrayList<Mensajes>();
        //10600 - Los pájaros han atacado uno de tus campos perjudicando el cultivo (deberías asignar más monjes para controlarlo)
        //10601 - Animales salvajes han entrado en uno de tus campos y han perjudicado el cultivo
        int[] literales = {10600, 10601, 10602, 10603};
        int idLastIdioma = 0;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);
            oCampoAD = new adCampo(con);
            //recuperar ArrayList con los datos de todos los campos en estado cultivando
            //que tengan asignados monjes.
            alCampos = oCampoAD.recuperarCamposPorAbadiaEstado(Constantes.ESTADO_CULTIVO_CULTIVANDO);

            if (!alCampos.isEmpty()) {
                oUtilsAD = new adUtils(con);
                for (Cultivo cultivo : alCampos) {
                    //inicializo los punto de cultivo por cada campo.
                    if (idLastIdioma != cultivo.getIdIdioma()) {
                        idLastIdioma = cultivo.getIdIdioma();
                        msg = oUtilsAD.getLiteral(literales, idLastIdioma); // Problemas en el cultivo...
                    }//if (idLastIdioma != cultivo.getIdIdioma())

                    //si los puntos de cultivo acumulados del dia no alcanzan los requeridos por el campo...
                    if (cultivo.getCultiva_acumulado_dia() < cultivo.getCultiva_por_dia()) {
                        //obtengo un aleatorio entre 0 y la diferencia entre los puntos necesarios de cultivo por dia
                        //y los puntos obtenidos. De modo que cuantos menos puntos se tengan, mas alto podra ser el valor.
                        dPuntosRestar = Utilidades.Random(0, cultivo.getCultiva_por_dia() - cultivo.getCultiva_acumulado_dia());
                        //si se van a restar puntos de cultivo..
                        if (dPuntosRestar > 0) {
                            msg = ProcesosUtils.Format(msg, (int) dPuntosRestar);
                            //generaremos un mensaje indicando que algo ha estropeado el cultivo...
                            alMensajes.add(new Mensajes(cultivo.getIdAbadia(), -1, msg, 0));
                            //actualizo el total de cultivo.
                            cultivo.setCultiva_total(cultivo.getCultiva_total() - dPuntosRestar);
                            oCampoAD = new adCampo(con);
                            oCampoAD.actualizarPuntosCultivo(cultivo);
                        }
                    }//if (cultivo.getCultiva_acumulado_dia() < cultivo.getCultiva_por_dia())
                }//while (itCampos.hasNext())
                oMensajesAD = new adMensajes(con);
                oMensajesAD.crearMensajes(alMensajes);
            }//if (!alCampos.isEmpty())
        } catch (Exception e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    /**
     * Resetea el acumulado de puntos de cultivo diario
     *
     * @throws AbadiaException Excepción general
     */
    public void resetAcumuladoCultivos() throws AbadiaException {
        String sTrace = this.getClass() + ".resetAcumuladoCultivos()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        String sSQL = "update campo set cultiva_dia = 0 where estado=" + Constantes.ESTADO_CULTIVO_CULTIVANDO;
        adUtils oUtilsAD;
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);
            oUtilsAD = new adUtils(con);
            oUtilsAD.execSQL(sSQL);
        } catch (Exception e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    /**
     * Genera mensajes advirtiendo que no hay monjes asignados campos...
     *
     * @throws AbadiaException Excepción general
     */
    public void gestionCamposDesatendidos() throws AbadiaException {
        String sTrace = this.getClass() + ".gestionCamposDesatendidos()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        ArrayList<Mensajes> alMensajes = new ArrayList<Mensajes>();
        ArrayList<Cultivo> alCampos;

        adCampo oCampoAD;
        adUtils oUtilsAD;
        adMensajes oMensajesAD;


        int idLastIdioma = 0;
        String msg = "";

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);

            oCampoAD = new adCampo(con);
            alCampos = oCampoAD.recuperarCamposDesatendidos(Constantes.ESTADO_CULTIVO_ARANDO);

            oUtilsAD = new adUtils(con);
            for (Cultivo cultivo : alCampos) {
                if (idLastIdioma != cultivo.getIdIdioma()) {
                    idLastIdioma = cultivo.getIdIdioma();
                    msg = oUtilsAD.getLiteral(10610, idLastIdioma); // Uno de tus campos no está siendo arado por ningún monje (deberías asignar alguno)
                }
                alMensajes.add(new Mensajes(cultivo.getIdAbadia(), -1, msg, 0));
            }
            oMensajesAD = new adMensajes(con);
            oMensajesAD.crearMensajes(alMensajes);

        } catch (Exception e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

}
