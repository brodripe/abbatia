package org.abbatia.bbean;

import org.abbatia.actionform.PeticionBloqueoActForm;
import org.abbatia.actionform.RegistroActForm;
import org.abbatia.adbean.*;
import org.abbatia.bean.Table;
import org.abbatia.bean.Usuario;
import org.abbatia.dbms.ConnectionFactory;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.UsuarioBloqueadoException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.exception.base.SystemException;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.Utilidades;
import org.apache.commons.validator.GenericValidator;
import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;

import java.awt.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Benjamín Rodríguez.
 * User: benjamin
 * Date: 24-may-2008
 * Time: 21:06:39
 */
public class UsuarioBBean {
    private static Logger log = Logger.getLogger(UsuarioBBean.class.getName());

    public void actualizarRegistro(Usuario p_oUsuario, RegistroActForm p_afRegistro, String p_sIPAdress) throws AbadiaException {
        String sTrace = this.getClass() + ".actualizarRegistro()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adUsuario oUsuarioAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS, Constantes.AUTOCOMIT_OF);
            oUsuarioAD = new adUsuario(con);
            p_oUsuario = oUsuarioAD.actualizar(p_afRegistro, p_oUsuario.getIdDeUsuario());
            p_oUsuario.setIpActual(p_sIPAdress);
            oUsuarioAD.actualizarFechaConexion(p_oUsuario.getIdDeUsuario());
            oUsuarioAD.actualizarIPUsuario(p_oUsuario.getIdDeUsuario(), p_sIPAdress);
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

    public void bloquearUsuario(long p_lUsuarioId, int p_iNumDias, String p_sMensaje) throws AbadiaException {
        String sTrace = this.getClass() + ".actualizarRegistro()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adUsuario oUsuarioAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oUsuarioAD = new adUsuario(con);
            oUsuarioAD.bloquearUsuario(p_lUsuarioId, p_iNumDias, p_sMensaje);
        } catch (AbadiaException e) {
            throw new SystemException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public void crearPeticionBloqueo(PeticionBloqueoActForm p_afPeticionBloqueo) throws AbadiaException {
        String sTrace = this.getClass() + ".actualizarRegistro()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adBloqueos oBloqueosAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oBloqueosAD = new adBloqueos(con);
            oBloqueosAD.crearPeticionBloqueo(p_afPeticionBloqueo);
        } catch (AbadiaException e) {
            throw new SystemException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public Usuario recuperarUsuarioPorNick(String p_sNick) throws AbadiaException {
        String sTrace = this.getClass() + ".recuperarUsuarioPorNick()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adUsuario oUsuarioAD;

        Usuario oUsuario;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oUsuarioAD = new adUsuario(con);
            oUsuario = oUsuarioAD.recuperarUsuarioPorNick(p_sNick);
            return oUsuario;
        } catch (AbadiaException e) {
            throw new SystemException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public void actualizarEstadoAceptacionNormas(long p_iUsuarioId, boolean p_bEstado) throws AbadiaException {
        String sTrace = this.getClass() + ".actualizarEstadoAceptacionNormas()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adUsuario oUsuarioAD;
        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oUsuarioAD = new adUsuario(con);
            oUsuarioAD.actualizarAceptacionNormas(p_iUsuarioId, p_bEstado);
        } catch (AbadiaException e) {
            throw new SystemException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public HashMap<String, ArrayList<Table>> recuperarDatosUsuario() throws AbadiaException {
        String sTrace = this.getClass() + ".recuperarDatosUsuario()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        HashMap<String, ArrayList<Table>> hmRequest = new HashMap<String, ArrayList<Table>>();
        adUtils oUtilsAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oUtilsAD = new adUtils(con);
            hmRequest.put("languages", oUtilsAD.getTablaIdiomas(Constantes.IDIOMA_TIPO_ASIGNABLE));
            hmRequest.put("paises", oUtilsAD.getClaveValor(Constantes.TABLA_PAIS));
            hmRequest.put("edades", oUtilsAD.getClaveValor(Constantes.TABLA_EDAD));
            hmRequest.put("sexo", oUtilsAD.getClaveValor(Constantes.TABLA_SEXO));

            return hmRequest;

        } catch (AbadiaException e) {
            throw new SystemException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public HashMap login(String p_szUsername, String p_szPassword, String p_szIP, MessageResources p_oResource) throws AbadiaException {
        String sTrace = this.getClass() + ".login()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adUsuario oUsuarioAD;
        adForum oForumAD;
        adAbadia oAbadiaAD;

        Usuario oUsuario;
        HashMap<String, Object> hmReturn = new HashMap<String, Object>();

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oUsuarioAD = new adUsuario(con);
            int bloqueado = oUsuarioAD.getBloqueado(p_szUsername);
            if (bloqueado == 1) {
                throw new UsuarioBloqueadoException(sTrace + " Usuario bloqueado", log);
            }
            oUsuario = oUsuarioAD.validar(p_szUsername, p_szPassword);
            oUsuario.setIpActual(p_szIP);
            oUsuarioAD.actualizarFechaConexion(oUsuario.getIdDeUsuario());
            oUsuarioAD.actualizarIPUsuario(oUsuario.getIdDeUsuario(), p_szIP);

            oUsuarioAD.addRegistroIP(p_szUsername, p_szIP, 1);

            //si el login ha sido correcto, reseteamos el numero de reintentos.
            oUsuarioAD.resetearReintentos(oUsuario.getIdDeUsuario());
            // Comprobar el foro... sino existe darlo de alta
            oForumAD = new adForum(con);
            oForumAD.validar(oUsuario);

            oAbadiaAD = new adAbadia(con);
            hmReturn.put(Constantes.ABADIA, oAbadiaAD.recuperarAbadiaDeUsuario(oUsuario, p_oResource));
            hmReturn.put(Constantes.USER_KEY, oUsuario);
            return hmReturn;
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public int gestionValidacionIncorrecta(String p_szUsername, String p_szIP) throws AbadiaException {
        String sTrace = this.getClass() + ".gestionValidacionIncorrecta()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adUsuario oUsuarioAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);

            oUsuarioAD = new adUsuario(con);
            oUsuarioAD.addRegistroIP(p_szUsername, p_szIP, 0);
            //recuperamos reintentos..
            int reintentos = oUsuarioAD.recuperarReintentos(p_szUsername);
            //si superan
            if (reintentos == Constantes.VARIOS_REINTENTOS_LOGIN) {
                oUsuarioAD.bloquearUsuario(p_szUsername, 1, Constantes.BLOQUEO_REINTENTOS);
            } else {
                oUsuarioAD.incrementarReintentos(p_szUsername);
            }

            return reintentos;

        } catch (AbadiaException e) {
            throw new SystemException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public HashMap<String, ArrayList<Table>> cargarTablasRegistro() throws AbadiaException {
        String sTrace = this.getClass() + ".gestionValidacionIncorrecta()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        HashMap<String, ArrayList<Table>> hmRequest = new HashMap<String, ArrayList<Table>>();

        adUtils oUtilsAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oUtilsAD = new adUtils(con);
            hmRequest.put("regiones", oUtilsAD.getTableRegionMenosUso());
            hmRequest.put("ordenes", oUtilsAD.getTable(Constantes.TABLA_ORDEN));
            hmRequest.put("actividades", oUtilsAD.getClaveValor(Constantes.TABLA_ACTIVIDAD));
            return hmRequest;
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public HashMap<String, ArrayList<Table>> registroUsuario() throws AbadiaException {
        String sTrace = this.getClass() + ".registroUsuario()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adUtils oUtilsAD;

        HashMap<String, ArrayList<Table>> hmRequest = new HashMap<String, ArrayList<Table>>();

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oUtilsAD = new adUtils(con);
            hmRequest.put("languages", oUtilsAD.getTablaIdiomas(Constantes.IDIOMA_TIPO_ASIGNABLE));
            hmRequest.put("paises", oUtilsAD.getClaveValor(Constantes.TABLA_PAIS));
            hmRequest.put("edades", oUtilsAD.getClaveValor(Constantes.TABLA_EDAD));
            hmRequest.put("sexo", oUtilsAD.getClaveValor(Constantes.TABLA_SEXO));

            return hmRequest;
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public Usuario registroUsuarioFin(RegistroActForm p_afUsuario, String p_szRemoteAddr) throws AbadiaException {
        String sTrace = this.getClass() + ".registroUsuarioFin()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adUsuario oUsuarioAD;

        Usuario oUsuario;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oUsuarioAD = new adUsuario(con);
            p_afUsuario.setPwd(Utilidades.generaContrasena());
            oUsuario = oUsuarioAD.registrar(p_afUsuario);
            oUsuarioAD.altaIPUsuario(oUsuario.getIdDeUsuario(), p_szRemoteAddr);

            return oUsuario;
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public ArrayList<Table> recuperarSupporters(int p_iPagina, Point p_pTotal, MessageResources p_oResource) throws AbadiaException {
        String sTrace = this.getClass() + ".recuperarSupporters()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adUsuario oUsuarioAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oUsuarioAD = new adUsuario(con);
            return oUsuarioAD.recuperarSupporters(p_iPagina, p_pTotal, p_oResource);

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public ArrayList<Table> recuperarPosiblesTramposos(int p_iPagina, Point p_pTotal) throws AbadiaException {
        String sTrace = this.getClass() + ".recuperarPosiblesTramposos()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adUsuario oUsuarioAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oUsuarioAD = new adUsuario(con);
            return oUsuarioAD.recuperarPosiblesTramposos(p_iPagina, p_pTotal);

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public ArrayList<Usuario> desbloquearUsuario(String p_szUsuarioId) throws AbadiaException {
        String sTrace = this.getClass() + ".desbloquearUsuario()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adUsuario oUsuarioAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oUsuarioAD = new adUsuario(con);
            if (!GenericValidator.isBlankOrNull(p_szUsuarioId)) {
                oUsuarioAD.desBloquearUsuario(Integer.parseInt(p_szUsuarioId));
            }
            //cargamos la lista de usuario bloqueados.
            return oUsuarioAD.recuperarUsuariosBloqueados();
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }
}
