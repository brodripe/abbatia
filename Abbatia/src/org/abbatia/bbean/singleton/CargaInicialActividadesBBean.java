/**
 * Created by IntelliJ IDEA.
 * User: benjamin
 * Date: 13-may-2010
 * Time: 22:40:28
 * To change this template use File | Settings | File Templates.
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
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;

public class CargaInicialActividadesBBean {

    private static Logger log = Logger.getLogger(CargaInicialActividadesBBean.class.getName());
    private HashMap<Integer, HashMap<Integer, List<Table>>> hmActividadesIdiomaJerarquia;


    private static CargaInicialActividadesBBean ourInstance = new CargaInicialActividadesBBean();

    public static CargaInicialActividadesBBean getInstance() {
        return ourInstance;
    }

    private CargaInicialActividadesBBean() {
        String sTrace = this.getClass() + ".CargaInicialActividadesBBean()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);
        adUtils oUtilsAD;

        String szSQL = "SELECT at.actividadid, l.literal FROM actividad_tipo at, literales l, actividad_tipo_asignable ata " +
                "WHERE at.literalid = l.literalid AND l.idiomaid = {0} and at.actividadid = ata.actividadid and ata.jerarquiaid = {1} " +
                " ORDER BY literal";

        List<Integer> alIdiomas;
        List<Integer> alJerarquias;
        HashMap<Integer, List<Table>> hmActividadesIdioma;

        hmActividadesIdiomaJerarquia = new HashMap<Integer, HashMap<Integer, List<Table>>>();

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oUtilsAD = new adUtils(con);

            alIdiomas = oUtilsAD.execSQLListInt("SELECT idiomaid FROM idioma");
            alJerarquias = oUtilsAD.execSQLListInt("SELECT jerarquiaid from jerarquia_eclesiastica");

            for (Integer iJerarquia : alJerarquias) {
                hmActividadesIdioma = new HashMap<Integer, List<Table>>();
                for (Integer iIdioma : alIdiomas) {
                    hmActividadesIdioma.put(iIdioma, oUtilsAD.execSQLList(MessageFormat.format(szSQL, new Integer[]{iIdioma, iJerarquia})));
                }
                hmActividadesIdiomaJerarquia.put(iJerarquia, hmActividadesIdioma);
            }

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

    public static List<Table> recuperarActividadesPorJerarquiaIdioma(int p_iJerarquiaId, int p_iIdiomaId) throws AbadiaException {
        return getInstance().hmActividadesIdiomaJerarquia.get(p_iJerarquiaId).get(p_iIdiomaId);
    }

    public static void reload() {
        ourInstance = new CargaInicialActividadesBBean();
    }

}