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
import java.util.HashMap;
import java.util.List;

public class CargaInicialFamiliasBBean {

    private static Logger log = Logger.getLogger(CargaInicialFamiliasBBean.class.getName());
    private HashMap<Integer, List<Table>> hmFamiliasIdioma;

    private static CargaInicialFamiliasBBean ourInstance = new CargaInicialFamiliasBBean();

    public static CargaInicialFamiliasBBean getInstance() {
        return ourInstance;
    }

    private CargaInicialFamiliasBBean() {
        String sTrace = this.getClass() + ".CargaInicialFamiliasBBean()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);
        adUtils oUtilsAD;

        List<Table> alFamilias;
        List<Integer> alIdiomas;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oUtilsAD = new adUtils(con);

            alIdiomas = oUtilsAD.execSQLListInt("SELECT idiomaid FROM idioma");
            hmFamiliasIdioma = new HashMap<Integer, List<Table>>();
            for (Integer iIdioma : alIdiomas) {

                alFamilias = oUtilsAD.execSQLList("SELECT af.familiaid, l.literal from alimentos_familia af, literales l" +
                        " where af.literalid = l.literalid and l.idiomaid = " + iIdioma);

                hmFamiliasIdioma.put(iIdioma, alFamilias);
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

    public static List<Table> recuperarFamiliasPorIdioma(int p_iIdiomaId) throws AbadiaException {
        return getInstance().hmFamiliasIdioma.get(p_iIdiomaId);
    }

    public static void reload() {
        ourInstance = new CargaInicialFamiliasBBean();
    }

}
