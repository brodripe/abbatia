package org.abbatia.bbean;

import org.abbatia.ListenerAbadia;
import org.abbatia.adbean.adInicioContents;
import org.abbatia.adbean.adPublicidad;
import org.abbatia.adbean.adUsuario;
import org.abbatia.adbean.adUtils;
import org.abbatia.bean.InicioMain;
import org.abbatia.bean.Publicidad;
import org.abbatia.core.CoreTiempo;
import org.abbatia.dbms.ConnectionFactory;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.exception.base.SystemException;
import org.abbatia.utils.Constantes;
import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;

import java.sql.Connection;
import java.util.HashMap;

/**
 * Created by Benjamín Rodríguez.
 * User: benjamin
 * Date: 24-may-2008
 * Time: 21:06:39
 */
public class InicioBBean {
    private static Logger log = Logger.getLogger(InicioBBean.class.getName());


    public InicioMain getTopUsuarios(int p_iPagina, int p_iRegion) throws AbadiaException {
        String sTrace = this.getClass() + ".getTopUsuarios()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adUtils oUtilsAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oUtilsAD = new adUtils(con);
            return oUtilsAD.getTopUsers(p_iPagina, p_iRegion);

        } catch (AbadiaException e) {
            throw new SystemException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public HashMap getDatosIniciales(String p_szParam, MessageResources p_oRecursos) throws AbadiaException {
        String sTrace = this.getClass() + ".getDatosIniciales()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adUtils oUtilsAD;
        adInicioContents oInicioContentsAD;
        adUsuario oUsuarioAD;
        adPublicidad oPublicidadAD;

        HashMap<String, Object> hmRequest = new HashMap<String, Object>();
        InicioMain oInicioMain;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);

            oUtilsAD = new adUtils(con);
            oInicioMain = oUtilsAD.getInicioMain(p_szParam);

            oInicioContentsAD = new adInicioContents(con);
            oInicioMain.setUltimasNovedades(oInicioContentsAD.getNovedades(p_szParam));

            hmRequest.put("DatosInicio", oInicioMain); // <--- nuevo???
            hmRequest.put("Tiempo", CoreTiempo.getTiempoAbadiaStringConHorasObj(p_oRecursos));
            hmRequest.put("UsuariosConectados", ListenerAbadia.getContador());
            oUsuarioAD = new adUsuario(con);
            hmRequest.put("UsuariosRegistrados", oUsuarioAD.getUsuariosRegistrados(0));
            hmRequest.put("UsuariosActivos", oUsuarioAD.getUsuariosRegistrados(1));
            hmRequest.put("AltasPendientes", oUsuarioAD.getUsuariosRegistrados(2));

            // Publicidad
            oPublicidadAD = new adPublicidad(con);
            Publicidad ban125 = oPublicidadAD.visualizar(0);
            Publicidad ban468 = oPublicidadAD.visualizar(1);
            hmRequest.put("Banner125", ban125);
            hmRequest.put("Banner468", ban468);

            return hmRequest;

        } catch (AbadiaException e) {
            throw new SystemException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }
}
