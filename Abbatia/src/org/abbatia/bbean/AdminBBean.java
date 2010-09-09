package org.abbatia.bbean;

import org.abbatia.actionform.AdminActForm;
import org.abbatia.actionform.MercadoAdminForm;
import org.abbatia.adbean.*;
import org.abbatia.bean.*;
import org.abbatia.core.CoreMail;
import org.abbatia.core.CoreTiempo;
import org.abbatia.dbms.ConnectionFactory;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.CardenalSinFuncionException;
import org.abbatia.exception.NoEsCardenalException;
import org.abbatia.exception.UsuarioNoEncontradoException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.exception.base.ApplicationException;
import org.abbatia.exception.base.SystemException;
import org.abbatia.process.adbean.adProcesos;
import org.abbatia.process.bbean.ProcesosDesastresBBean;
import org.abbatia.process.prMain;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.HTML;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Benjamín Rodríguez.
 * User: benjamin
 * Date: 24-may-2008
 * Time: 21:06:39
 */
public class AdminBBean {
    private static Logger log = Logger.getLogger(AdminBBean.class.getName());


    public void accesoIlegal(Abadia p_oAbadiaOri, Usuario p_oUsuario) throws AbadiaException {
        String sTrace = this.getClass() + ".accesoIlegal()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adAbadia oAbadiaAD;
        adCorreo oCorreoAD;
        adUsuario oUsuarioAD;

        Correo oCorreo;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS, Constantes.AUTOCOMIT_OF);
            oCorreo = new Correo();
            oCorreo.setIdAbadiaOrigen(209);
            oAbadiaAD = new adAbadia(con);
            oCorreo.setIdAbadiasDestino(oAbadiaAD.getIDAbadiasAdministradores());

            oCorreo.setTexto("la Abadia: " + p_oAbadiaOri.getNombre() + " ha tratado de entrar en la pantalla de administración (no se lo he permitido)");
            oCorreo.setFecha_real(CoreTiempo.getTiempoRealString());
            oCorreo.setFecha_abadia(CoreTiempo.getTiempoAbadiaString());
            oCorreoAD = new adCorreo(con);
            oCorreoAD.crearCorreo(oCorreo);

            //bloqueamos al usuario de por vida.
            oUsuarioAD = new adUsuario(con);
            oUsuarioAD.bloquearUsuario(p_oUsuario.getIdDeUsuario(), 0, Constantes.BLOQUEO_ACCESO_ADMINISTRACION);
            ConnectionFactory.commitTransaction(con);
        } catch (AbadiaException e) {
            ConnectionFactory.rollbackTransaction(con);
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public Abadia restaurarAbadia(String p_sAbadiaId) throws AbadiaException {
        String sTrace = this.getClass() + ".restaurarAbadia()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adMonje oMonjeAD;
        adMensajes oMensajesAD;
        adAbadia oAbadiaAD;

        Abadia oAbadia;
        Mensajes oMensaje;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS, Constantes.AUTOCOMIT_OF);

            oMonjeAD = new adMonje(con);
            oMensajesAD = new adMensajes(con);
            oAbadiaAD = new adAbadia(con);
            oAbadia = oAbadiaAD.recuperarAbadia(p_sAbadiaId);

            oMonjeAD.restaurarMonjes(oAbadia.getIdDeAbadia());
            oMensaje = new Mensajes();
            oMensaje.setFechaAbadia(CoreTiempo.getTiempoAbadiaStringConHoras());
            oMensaje.setFechaReal(CoreTiempo.getTiempoRealStringConHoras());
            oMensaje.setIdDeRegion(oAbadia.getIdDeRegion());
            oMensaje.setIdDeAbadia(oAbadia.getIdDeAbadia());
            oMensaje.setIdDeMonje(-1);
            oMensaje.setMensaje("Se ha obrado el milagro sobre tu abadía, tus monjes y recursos han sido reestablecidos.");
            //Crear un mensaje de aviso
            oMensajesAD.crearMensaje(oMensaje);

            ConnectionFactory.commitTransaction(con);

            return oAbadia;
        } catch (AbadiaException e) {
            ConnectionFactory.rollbackTransaction(con);
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public HashMap<String, Serializable> suplantarAbadia(AdminActForm p_afAdmin) throws AbadiaException {
        String sTrace = this.getClass() + ".suplantarAbadia()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adAbadia oAbadiaAD;
        adUsuario oUsuarioAD;

        Abadia oAbadiaSuplantada;
        Usuario oUsuarioSuplantado;
        String sNombreAbadia;
        HashMap<String, Serializable> hmReturn = new HashMap<String, Serializable>();

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oAbadiaAD = new adAbadia(con);
            oUsuarioAD = new adUsuario(con);
            if (oAbadiaAD.existeAbadia(p_afAdmin.getNombreAbadia())) {
                oAbadiaSuplantada = oAbadiaAD.recuperarAbadia(p_afAdmin.getNombreAbadia());
            } else {
                sNombreAbadia = oUsuarioAD.recuperarUsuarioAbadia(p_afAdmin.getNombreAbadia());
                oAbadiaSuplantada = oAbadiaAD.recuperarAbadia(sNombreAbadia);
            }

            oUsuarioSuplantado = oUsuarioAD.recuperarUsuario(oAbadiaSuplantada.getIdDeUsuario());
            if (oUsuarioSuplantado == null)
                throw new UsuarioNoEncontradoException("", null, log);

            hmReturn.put("usuario", oUsuarioSuplantado);
            hmReturn.put("abbatia", oAbadiaSuplantada);

            return hmReturn;


        } catch (AbadiaException e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public void eliminarAbadia(String p_sAbadia, ActionMessages p_alActionMessages) throws AbadiaException {
        String sTrace = this.getClass() + ".eliminarAbadia()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adAbadia oAbadiaAD;
        adUsuario oUsuarioAD;

        Abadia oAbadia;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS, Constantes.AUTOCOMIT_OF);
            oAbadiaAD = new adAbadia(con);
            oAbadia = oAbadiaAD.recuperarAbadia(p_sAbadia);
            oAbadiaAD.eliminarTodoAbadia(oAbadia.getIdDeAbadia());

            oUsuarioAD = new adUsuario(con);
            oUsuarioAD.eliminar(oAbadia.getIdDeUsuario());
            p_alActionMessages.add("msg", new ActionMessage("mensajes.info.eliminarusuario", oAbadia.getNombre()));

            ConnectionFactory.commitTransaction(con);
        } catch (AbadiaException e) {
            ConnectionFactory.rollbackTransaction(con);
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public void restaurarHabilidades(ActionMessages p_alActionMessages) throws AbadiaException {
        String sTrace = this.getClass() + ".restaurarHabilidades()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adMonje oMonjeAD;
        adHabilidades oHabilidadesAD;
        ArrayList<Monje> alMonjes;
        Iterator<Monje> itMonjes;
        Monje oMonje;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS, Constantes.AUTOCOMIT_OF);
            oMonjeAD = new adMonje(con);
            oHabilidadesAD = new adHabilidades(con);
            alMonjes = oMonjeAD.recuperarMonjesDefectuosos();
            itMonjes = alMonjes.iterator();

            while (itMonjes.hasNext()) {
                oMonje = itMonjes.next();
                oMonjeAD.restaurarMonje_alimentacion(oMonje);
                oHabilidadesAD.crearHabilidadesInicialesAdmin(oMonje);
            }

            p_alActionMessages.add("msg", new ActionMessage("mensajes.info.habilidadestodas", alMonjes.size()));

            ConnectionFactory.commitTransaction(con);
        } catch (AbadiaException e) {
            ConnectionFactory.rollbackTransaction(con);
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public void restaurarHabilidadesAbadia(AdminActForm p_afAdmin, ActionMessages p_alActionMessages) throws AbadiaException {
        String sTrace = this.getClass() + ".restaurarHabilidadesAbadia()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adAbadia oAbadiaAD;
        adUtils oUtilsAD;

        Abadia oAbadia;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS, Constantes.AUTOCOMIT_OF);
            oAbadiaAD = new adAbadia(con);
            oAbadia = oAbadiaAD.recuperarAbadia(p_afAdmin.getNombreAbadia());

            oUtilsAD = new adUtils(con);
            oUtilsAD.restablecerHabilidades(oAbadia.getIdDeAbadia());

            p_alActionMessages.add("msg", new ActionMessage("mensajes.info.habilidadesabadia", oAbadia.getNombre()));

            ConnectionFactory.commitTransaction(con);

        } catch (AbadiaException e) {
            ConnectionFactory.rollbackTransaction(con);
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public void restaurarMonje(AdminActForm p_afAdmin, ActionMessages p_alActionMessages) throws AbadiaException {
        String sTrace = this.getClass() + ".restaurarMonje()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adMonje oMonjeAD;
        adHabilidades oHabilidadesAD;

        Monje oMonje;
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS, Constantes.AUTOCOMIT_OF);
            oMonje = new Monje(p_afAdmin.getIdMonje());
            oMonjeAD = new adMonje(con);
            oMonje.setNombre(oMonjeAD.getNomMonje(oMonje.getIdDeMonje(), "de"));
            //Monje monje = monjeAD.recuperarDatosMonje(admin.getIdMonje());
            //monjeAD.actualizarActividades(monje);
            oMonjeAD.restaurarMonje_alimentacion(oMonje);

            oHabilidadesAD = new adHabilidades(con);
            oHabilidadesAD.crearHabilidadesInicialesAdmin(oMonje);
            p_alActionMessages.add("msg", new ActionMessage("mensajes.info.monjerestaurado", oMonje.getNombre()));

            ConnectionFactory.commitTransaction(con);
        } catch (AbadiaException e) {
            ConnectionFactory.rollbackTransaction(con);
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public void lanzarCatastrofe(AdminActForm p_afAdmin, ActionMessages p_alActionMessages) throws AbadiaException {
        String sTrace = this.getClass() + ".lanzarCatastrofe()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adAbadia oAbadiaAD;
        ProcesosDesastresBBean oDesastresPR;

        Abadia oAbadia;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS, Constantes.AUTOCOMIT_OF);

            oAbadiaAD = new adAbadia(con);
            oAbadia = oAbadiaAD.recuperarAbadia(p_afAdmin.getNombreAbadia());

            oDesastresPR = new ProcesosDesastresBBean();
            oDesastresPR.realizar_desastre(oAbadia.getIdDeAbadia(), true);

            p_alActionMessages.add("msg", new ActionMessage("mensajes.info.catastrofelanzada", oAbadia.getNombre()));

            ConnectionFactory.commitTransaction(con);
        } catch (AbadiaException e) {
            ConnectionFactory.rollbackTransaction(con);
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public void generarLibro(AdminActForm p_afAdmin) throws AbadiaException {
        String sTrace = this.getClass() + ".generarLibro()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adLibros oLibrosAD;


        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS, Constantes.AUTOCOMIT_OF);
            oLibrosAD = new adLibros(con);
            oLibrosAD.generarLibroPorRegion(p_afAdmin.getIdLibro());
            ConnectionFactory.commitTransaction(con);
        } catch (AbadiaException e) {
            ConnectionFactory.rollbackTransaction(con);
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public HashMap<String, Serializable> administrarLiterales(Usuario p_oUsuario, HttpServletRequest p_hmRequest) throws AbadiaException {
        String sTrace = this.getClass() + ".administrarLiterales()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adLiterales oLiteralesAD;
        HashMap<String, Serializable> hmRequest = new HashMap<String, Serializable>();

        String sPost = p_hmRequest.getParameter("post");
        String sLiteral;
        String sLiteralId;
        int iLiteralId = 0;
        ArrayList<Table> alTable1;
        int iPagina = 0;
        int iIdioma1 = 1;
        int iIdioma2 = 1;

        String sPagina = p_hmRequest.getParameter("pagina");
        if (sPagina != null)
            iPagina = Integer.parseInt(sPagina);
        String sIdioma1 = p_hmRequest.getParameter("Idioma1");
        if (sIdioma1 != null)
            iIdioma1 = Integer.parseInt(sIdioma1);
        String sIdioma2 = p_hmRequest.getParameter("Idioma2");
        if (sIdioma2 != null)
            iIdioma2 = Integer.parseInt(sIdioma2);
        String sNuevos = p_hmRequest.getParameter("Nuevos");
        if (sNuevos == null)
            sNuevos = "N";

        String sNavega;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS, Constantes.AUTOCOMIT_OF);
            oLiteralesAD = new adLiterales(con);
            if ((p_oUsuario.getAdministrador() == 1) || (p_oUsuario.getAdministrador() == 2) || (p_oUsuario.getAdministrador() == 3)) {
                // Se envian todos los datos
                if (sPost == null) sPost = "0";
                if (sPost.equals("1")) {
                    // Recuperar!
                    for (int n = 0; n < Constantes.REGISTROS_PAGINA + 1; n++) {
                        sPost = p_hmRequest.getParameter("C_" + n);
                        if (sPost == null) sPost = "OFF";
                        if ((sPost.equals("ON")) && (iIdioma2 != 1)) {
                            sLiteral = p_hmRequest.getParameter("T_" + n);
                            if (sLiteral == null)
                                sLiteral = "";
                            sLiteralId = p_hmRequest.getParameter("L_" + n);
                            if (sLiteralId != null)
                                iLiteralId = Integer.parseInt(sLiteralId);
                            oLiteralesAD.setLiteral(iLiteralId, iIdioma2, sLiteral);
                            log.info("AdminLiteralesAction. El usuario: " + p_oUsuario.getNick() + " ha modificado el literal: " + iLiteralId + " en el idioma " + iIdioma2 + " con el valor: " + sLiteral);
                        }
                    }
                }

                alTable1 = oLiteralesAD.getLiterales(iIdioma1, iIdioma2, iPagina, sNuevos);
                int total = oLiteralesAD.getTotalRecords(iIdioma1, iIdioma2, sNuevos);
                sIdioma1 = oLiteralesAD.getIdiomaDesc(iIdioma1);
                sIdioma2 = oLiteralesAD.getIdiomaDesc(iIdioma2);

                HTML cHTML = new HTML();
                sNavega = cHTML.getNavigateBar("AdminLiterales.do", "Idioma1=" + iIdioma1 + "&Idioma2=" + iIdioma2 + "&Nuevos=" + sNuevos, iPagina, total / Constantes.REGISTROS_PAGINA);

                hmRequest.put("literales", alTable1);
                hmRequest.put("Navega", sNavega);
                hmRequest.put("sIdioma1", sIdioma1);
                hmRequest.put("sIdioma2", sIdioma2);
                hmRequest.put("Idioma1", Integer.toString(iIdioma1));
                hmRequest.put("Idioma2", Integer.toString(iIdioma2));
                hmRequest.put("Pagina", String.valueOf(iPagina));
                hmRequest.put("Nuevos", sNuevos);
            }
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

    public HashMap<String, ArrayList<Table>> administrarProcesos(Usuario p_oUsuario) throws AbadiaException {
        String sTrace = this.getClass() + ".administrarProcesos()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        HashMap<String, ArrayList<Table>> hmRequest = new HashMap<String, ArrayList<Table>>();

        Connection con = null;
        try {
            ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);
            adPlanificador planificador = new adPlanificador(con);
            hmRequest.put("runs", planificador.getProcesosEjecutar(p_oUsuario.getAdministrador()));
            hmRequest.put("logs", planificador.getProcesosLogs());
            return hmRequest;
        } catch (AbadiaException e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public ArrayList<Usuario> controlAltas(String p_sUsuarioId, ActionMapping p_oMapping, MessageResources p_oResource) throws AbadiaException {
        String sTrace = this.getClass() + ".controlAltas()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adUsuario oUsuarioAD;
        adUtils oUtilsAD;
        ArrayList<Usuario> alUsuario;

        Usuario oUsuarioAlta = null;
        Email oEmail;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oUsuarioAD = new adUsuario(con);
            if (p_sUsuarioId != null) {
                oUsuarioAlta = oUsuarioAD.recuperarUsuario(Integer.parseInt(p_sUsuarioId));
            }
/*
            if (oUsuarioAlta == null) {
                throw new UsuarioNoEncontradoException(sTrace + "=> No se ha encontrado el usuario", null, log);
            }
*/
            if (p_oMapping.getParameter() != null && p_oMapping.getParameter().equals("aceptar")) {
                //enviar correo
                oEmail = new Email();
                oEmail.setAsunto(p_oResource.getMessage("mensajes.mail.asunto"));
                String sMensaje = p_oResource.getMessage("mensajes.mail.body");
                sMensaje = sMensaje + p_oResource.getMessage("mensajes.mail.body.usuario", oUsuarioAlta.getNick());
                sMensaje = sMensaje + p_oResource.getMessage("mensajes.mail.body.password", oUsuarioAlta.getContrasena());
                sMensaje = sMensaje + p_oResource.getMessage("mensajes.mail.body.link");
                sMensaje = sMensaje + p_oResource.getMessage("mensajes.mail.body.fin");
                oEmail.setTo(oUsuarioAlta.getEmail());
                oEmail.setMsg(sMensaje);
                CoreMail.enviarEmail(oEmail);
                //Actualizar estado del registro a 0
                oUsuarioAD.actualizarTipoUsuario(oUsuarioAlta.getIdDeUsuario(), Constantes.USUARIO_BASICO);


            } else if (p_oMapping.getParameter() != null && p_oMapping.getParameter().equals("denegar")) {
                //enviar correo
                oEmail = new Email();
                oEmail.setAsunto(p_oResource.getMessage("mensajes.mail.asunto.nook"));
                String sMensaje = p_oResource.getMessage("mensajes.mail.body.nook");
                sMensaje = sMensaje + p_oResource.getMessage("mensajes.mail.body.nook.opt1");
                sMensaje = sMensaje + p_oResource.getMessage("mensajes.mail.body.nook.opt2");
                sMensaje = sMensaje + p_oResource.getMessage("mensajes.mail.body.nook.opt3");
                sMensaje = sMensaje + p_oResource.getMessage("mensajes.mail.body.nook.opt4", oUsuarioAlta.getNombre(), oUsuarioAlta.getPrimerApellido(), oUsuarioAlta.getSegundoApellido());
                sMensaje = sMensaje + p_oResource.getMessage("mensajes.mail.body.nook.saludos");
                oEmail.setTo(oUsuarioAlta.getEmail());
                oEmail.setMsg(sMensaje);
                CoreMail.enviarEmail(oEmail);
                //eliminar registro de usuario
                oUsuarioAD.eliminarUsuario(oUsuarioAlta.getIdDeUsuario());
            }

            //recupera la lista de usuarios pendientes de confirmación USUARIO_TIPO = 99
            oUtilsAD = new adUtils(con);
            alUsuario = oUsuarioAD.recuperarUsuariosAltasPendientes();
            for (Usuario oUsuario:alUsuario)
            {
                oUsuario.setEdadDesc(oUtilsAD.getTablaDescripcion("EDAD", String.valueOf(oUsuario.getEdad()), "No informada"));
            }
            return alUsuario;

        } catch (AbadiaException e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public void ejecutarProceso(int p_iProceso) throws AbadiaException {
        String sTrace = this.getClass() + ".ejecutarProceso(" + p_iProceso + ")";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        prMain oMainPR;
        adPlanificador oPlanificadorAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS, Constantes.AUTOCOMIT_OF);
            oMainPR = new prMain();
            log.info("Proceso - ID:" + p_iProceso);
            oPlanificadorAD = new adPlanificador(con);
            datosProceso proceso = oPlanificadorAD.recuperarProceso(p_iProceso);
            oMainPR.lanzar(proceso, con);

            ConnectionFactory.commitTransaction(con);
        } catch (AbadiaException e) {
            ConnectionFactory.rollbackTransaction(con);
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public void arrancarPlanificador() throws AbadiaException {
        String sTrace = this.getClass() + ".arrancarPlanificador()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adProcesos oProcesosAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("**** Arrancado planificador", 0);

        } catch (AbadiaException e) {
            throw e;
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }


    }

    public void pararPlanificador() throws AbadiaException {
        String sTrace = this.getClass() + ".pararPlanificador()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adProcesos oProcesosAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("**** Parando planificador", 0);

        } catch (AbadiaException e) {
            throw new SystemException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public HashMap<String, ArrayList<PeticionBloqueo>> gestionBloqueos(String p_szPeticionId, ActionMapping p_oMapping, Usuario p_oUsuario) throws AbadiaException {
        String sTrace = this.getClass() + ".gestionBloqueos()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adBloqueos oBloqueosAD;
        int iPeticionId = 0;

        HashMap<String, ArrayList<PeticionBloqueo>> hmRequest = new HashMap<String, ArrayList<PeticionBloqueo>>();

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oBloqueosAD = new adBloqueos(con);
            if (p_szPeticionId != null) {
                iPeticionId = Integer.parseInt(p_szPeticionId);
                if (p_oMapping.getParameter() != null && p_oMapping.getParameter().equals("aceptar")) {
                    oBloqueosAD.actualizarVoto(iPeticionId, p_oUsuario.getIdDeUsuario(), Constantes.VOTO_PETICION_BLOQUEO_SI);
                    oBloqueosAD.gestionarPeticionBloqueo(iPeticionId);

                } else if (p_oMapping.getParameter() != null && p_oMapping.getParameter().equals("denegar")) {
                    oBloqueosAD.actualizarVoto(iPeticionId, p_oUsuario.getIdDeUsuario(), Constantes.VOTO_PETICION_BLOQUEO_NO);
                    oBloqueosAD.gestionarPeticionBloqueo(iPeticionId);

                }
            }
            //recupera la lista de usuarios pendientes de confirmación USUARIO_TIPO = 99
            hmRequest.put("BloqueosAbiertos", oBloqueosAD.recuperarPeticionesBloqueo(Constantes.ESTADO_PETICION_BLOQUEO_ABIERTA));
            hmRequest.put("BloqueosCerrados", oBloqueosAD.recuperarPeticionesBloqueo(Constantes.ESTADO_PETICION_BLOQUEO_CERRADA));

            return hmRequest;
        } catch (AbadiaException e) {
            throw new SystemException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }


    }

    public HashMap<String, ArrayList> listarMercadoAdminInicio(int p_iMercancia, Usuario p_oUsuario) throws AbadiaException {
        String sTrace = this.getClass() + ".listarMercadoAdminInicio()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        HashMap<String, ArrayList> hmRequest = new HashMap<String, ArrayList>();
        ArrayList alMercanciaTipo;
        ArrayList<MercadoAdminForm> alDatosMercado;


        adUtils oUtilsAD;
        adMercadoAdministracion oMercadoAdministracionAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            //recuperamos el atributo mercancia del filtro de selección.
            oUtilsAD = new adUtils(con);
            alMercanciaTipo = oUtilsAD.getClaveValor(Constantes.TABLA_MERCANCIA);

            oMercadoAdministracionAD = new adMercadoAdministracion(con);
            alDatosMercado = oMercadoAdministracionAD.getMercadoAdmin(p_oUsuario, p_iMercancia);

            hmRequest.put("lista_mercancias", alDatosMercado);
            hmRequest.put("mercancia_tipo", alMercanciaTipo);

            return hmRequest;
        } catch (AbadiaException e) {
            throw new SystemException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public HashMap<String, ArrayList> listarMercadoAdmin(int p_iMercancia, Abadia p_oAbadia, Usuario p_oUsuario) throws AbadiaException {
        String sTrace = this.getClass() + ".listarMercadoAdmin()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        HashMap<String, ArrayList> hmRequest = new HashMap<String, ArrayList>();
        ArrayList alMercanciaTipo;
        ArrayList<MercadoAdminForm> alDatosMercado;


        adUtils oUtilsAD;
        adMercadoAdministracion oMercadoAdministracionAD;
        adJerarquiaEclesiastica oJerarquiaAD;
        adCardenal oCardenalAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            //aquí, comprobaremos si el usuario tiene un cardenal, y recuperaremos si tiene responsabilidades de mercado
            //en caso de tenerlas, obtendremos el ámbito de mercado que puede controlar e informaremos la variable "mercancia"
            oJerarquiaAD = new adJerarquiaEclesiastica(con);
            boolean existeCardenal = oJerarquiaAD.tieneEminencia(p_oAbadia.getIdDeAbadia(), Constantes.JERARQUIA_CARDENAL);

            if (existeCardenal) {
                //Verificamos si entre las funciones del cardenal esta la de administrar el mercado
                oCardenalAD = new adCardenal(con);
                int funcion = oCardenalAD.getFuncionCardenal(p_oAbadia);

                if (funcion == Constantes.FUNCION_CARDENAL_MERCADO) {
                    oUtilsAD = new adUtils(con);
                    alMercanciaTipo = oUtilsAD.getClaveValor(Constantes.TABLA_MERCANCIA);

                    oMercadoAdministracionAD = new adMercadoAdministracion(con);
                    alDatosMercado = oMercadoAdministracionAD.getMercadoAdmin(p_oUsuario, p_iMercancia);

                    hmRequest.put("mercancia_tipo", alMercanciaTipo);
                    hmRequest.put("lista_mercancias", alDatosMercado);
                    return hmRequest;
                } else {
                    throw new CardenalSinFuncionException("El cardenal no tiene la función indicada", log);
                }
            } else {
                throw new NoEsCardenalException("La abadía no tiene cardenal", log);
            }

        } catch (ApplicationException e) {
            throw e;
        } catch (AbadiaException e) {
            throw new SystemException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public void altaNuevoLiteral(String p_szLiteral) throws AbadiaException
    {
        String sTrace = this.getClass() + ".altaNuevoLiteral(" + p_szLiteral + ")";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        int iLiteralID;
        adUtils oUtilsAD;
        //recuperar el último código utilizado en la tabla de literales

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oUtilsAD = new adUtils(con);
            iLiteralID = oUtilsAD.getSQL("select max(literalid) from literales", 100000);
            oUtilsAD.execSQL("INSERT INTO literales (LITERALID,IDIOMAID,LITERAL) VALUES (" + (iLiteralID + 1) + ",'1','" + p_szLiteral + "')");
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }
}
