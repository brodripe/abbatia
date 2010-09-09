package org.abbatia.bbean;

import org.abbatia.adbean.adLiterales;
import org.abbatia.adbean.adUtils;
import org.abbatia.bean.ExcepcionMax;
import org.abbatia.dbms.ConnectionFactory;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.util.HashMap;

/**
 * Created by Benjamín Rodríguez.
 * User: benjamin
 * Date: 24-may-2008
 * Time: 21:06:39
 */
public class UtilsBBean {
    private static Logger log = Logger.getLogger(UtilsBBean.class.getName());

    public String recuperarPropiedadValor(short p_sTipoParametro, short p_sParametro, String p_sClave) throws AbadiaException {
        String sTrace = this.getClass() + ".recuperarPropiedadValor()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adUtils oUtilsAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oUtilsAD = new adUtils(con);
            return oUtilsAD.getPropidadValor(p_sTipoParametro, p_sParametro, p_sClave);

        } catch (AbadiaException e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public HashMap<String, String> recuperarParametrosLoginFrm() throws AbadiaException {
        String sTrace = this.getClass() + ".recuperarPropiedadValor()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        HashMap<String, String> hmRequest = new HashMap<String, String>();

        adUtils oUtilsAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oUtilsAD = new adUtils(con);
            hmRequest.put("isbn", oUtilsAD.getSQL("SELECT ISBN   FROM publicidad_libros order by rand() limit 1;", ""));
            hmRequest.put("titulo", oUtilsAD.getSQL("SELECT Titulo FROM publicidad_libros WHERE ISBN='" + hmRequest.get("isbn") + "'", ""));
            hmRequest.put("autor", oUtilsAD.getSQL("SELECT Autor  FROM publicidad_libros WHERE ISBN='" + hmRequest.get("isbn") + "'", ""));
            return hmRequest;
        } catch (AbadiaException e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public boolean ejecutarSQL(String p_szSQL) throws AbadiaException {
        String sTrace = this.getClass() + ".ejecutarSQL(" + p_szSQL + ")";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adUtils oUtilsAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);
            oUtilsAD = new adUtils(con);
            return oUtilsAD.execSQL(p_szSQL);
        } catch (AbadiaException e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public int getSQL(String p_szSQL, int p_iDefecto) throws AbadiaException {
        String sTrace = this.getClass() + ".getSQL(" + p_szSQL + ")";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adUtils oUtilsAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);
            oUtilsAD = new adUtils(con);
            return oUtilsAD.getSQL(p_szSQL, p_iDefecto);
        } catch (AbadiaException e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public void setPropiedad(int PropiedadID, int ClaveID, String tipo, String valor) throws AbadiaException {
        String sTrace = this.getClass() + ".setPropiedad(" + PropiedadID + ", " + ClaveID + ", " + tipo + ", " + valor + ")";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adUtils oUtilsAD;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);

            oUtilsAD = new adUtils(con);
            oUtilsAD.setPropriedad(PropiedadID, ClaveID, tipo, valor);

        } catch (AbadiaException e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public HashMap<String, ExcepcionMax> recuperarTablaExcepciones() throws AbadiaException {
        String sTrace = this.getClass() + ".recuperarTablaExcepciones()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adUtils utilsAD;
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            utilsAD = new adUtils(con);
            return utilsAD.getTablaExcepciones();
        } catch (AbadiaException e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public void cargarTablaLiterales() throws AbadiaException {
        String sTrace = this.getClass() + ".cargarTablaLiterales()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adLiterales oLiteralesAD;
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oLiteralesAD = new adLiterales(con);
            oLiteralesAD.cargarLiterales();
        } catch (AbadiaException e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

}
