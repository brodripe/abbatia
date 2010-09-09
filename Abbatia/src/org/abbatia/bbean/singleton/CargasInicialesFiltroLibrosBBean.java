/**
 * Created by Benjamín Rodríguez.
 * User: benjamin
 * Date: 02-may-2009
 * Time: 16:36:28
 */
package org.abbatia.bbean.singleton;

import org.abbatia.adbean.adUtils;
import org.abbatia.bean.Table;
import org.abbatia.dbms.ConnectionFactory;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

public class CargasInicialesFiltroLibrosBBean {
    private static Logger log = Logger.getLogger(CargasInicialesFiltroLibrosBBean.class.getName());
    private static CargasInicialesFiltroLibrosBBean ourInstance = new CargasInicialesFiltroLibrosBBean();

    private HashMap<String, List<Table>> hmTablasList;
    private HashMap<String, HashMap<Integer, List<Table>>> hmTablasHash;

    public static CargasInicialesFiltroLibrosBBean getInstance() {
        return ourInstance;
    }

    private CargasInicialesFiltroLibrosBBean() {
        String sTrace = this.getClass() + ".CargasInicialesFiltroLibrosBBean()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adUtils oUtilsAD;
        hmTablasList = new HashMap<String, List<Table>>();
        hmTablasHash = new HashMap<String, HashMap<Integer, List<Table>>>();
        List<Table> alRegiones;
        HashMap<Integer, List<Table>> hmAbadiasRegion = new HashMap<Integer, List<Table>>();

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oUtilsAD = new adUtils(con);
            alRegiones = oUtilsAD.execProcedureList("call recuperarRegionesConLibros();", true);
            for (Table oTable : alRegiones) {
                hmAbadiasRegion.put(oTable.getId(), oUtilsAD.execProcedureList("call recuperarAbadiasConLibrosPorRegion(" + oTable.getId() + ")", true));
            }
            hmTablasHash.put("AbadiasConLibroPorRegion", hmAbadiasRegion);
            hmTablasList.put("RegionesConLibro", oUtilsAD.execProcedureList("call recuperarRegionesConLibros();", true));
            hmTablasList.put("AbadiasConLibro", oUtilsAD.execProcedureList("call recuperarAbadiasConLibros();", true));
            hmTablasList.put("LibroActivos", oUtilsAD.execProcedureList("call recuperarLibrosActivos();", true));
            hmTablasList.put("IdiomasLibro", oUtilsAD.execProcedureList("call recuperarIdiomasConLibros();", true));
        } catch (AbadiaException e) {
            log.error(sTrace, e);
        } catch (Exception e) {
            log.error(sTrace, e);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public HashMap<String, List<Table>> getHashList() {
        return hmTablasList;
    }

    public HashMap<String, HashMap<Integer, List<Table>>> getHashMap() {
        return hmTablasHash;
    }

    public static List getValueList(String p_szKey) {
        String sTrace = ourInstance.getClass() + ".getValueList(" + p_szKey + ")";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        try {
            return ourInstance.getHashList().get(p_szKey);
        } finally {
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public static List<Table> getValueList(String p_szKey, int p_iKey) {
        String sTrace = ourInstance.getClass() + ".getValueList(" + p_szKey + ", " + p_iKey + ")";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        try {
            return ourInstance.getHashMap().get(p_szKey).get(p_iKey);
        } finally {
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }


    public static void reload() {
        ourInstance = new CargasInicialesFiltroLibrosBBean();
    }
}
