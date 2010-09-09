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
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class CargasInicialesDietasBBean {
    private static Logger log = Logger.getLogger(CargasInicialesDietasBBean.class.getName());
    private static CargasInicialesDietasBBean ourInstance = new CargasInicialesDietasBBean();

    private HashMap<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, List<Table>>>>>> hmDietasNivel;


    public static CargasInicialesDietasBBean getInstance() {
        return ourInstance;
    }

    private CargasInicialesDietasBBean() {
        String sTrace = this.getClass() + ".CargasInicialesDietasBBean()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adUtils oUtilsAD;

        hmDietasNivel = new HashMap<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, List<Table>>>>>>();

        List<Integer> alNivelesAbadia;
        List<Integer> alFamilias;
        List<Integer> alIdiomas;
        List<Integer> alJerarquias;
        List<Integer> alActividades;
        List<Table> alListaAlimentos;
        HashMap<Integer, List<Table>> hmAlimentosIdioma;


        HashMap<Integer, HashMap<Integer, List<Table>>> hmAlimentosFamilia;
        HashMap<Integer, HashMap<Integer, HashMap<Integer, List<Table>>>> hmAlimentosActividad;
        HashMap<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, List<Table>>>>> hmAlimentosJerarquia;
        //HashMap<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, List<Table>>>>> hmDietas;

/*
        String szSQL_ori = "SELECT alimentos_tipo.ALIMENTOID, literales.LITERAL " +
                " FROM alimentos_tipo Inner Join alimentos_familia_jerarquia ON alimentos_familia_jerarquia.ALIMENTOID = alimentos_tipo.ALIMENTOID " +
                " Inner Join literales ON alimentos_tipo.LITERALID = literales.LITERALID " +
                " WHERE literales.IDIOMAID = {0} and alimentos_familia_jerarquia.JERARQUIAID = {1} and alimentos_familia_jerarquia.FAMILIAID = {2}";
*/

        String szSQL = "SELECT alimentos_tipo.ALIMENTOID, literales.LITERAL " +
                " FROM alimentos_tipo Inner Join dieta ON dieta.ALIMENTOID = alimentos_tipo.ALIMENTOID " +
                " Inner Join literales ON alimentos_tipo.LITERALID = literales.LITERALID " +
                " WHERE literales.IDIOMAID = {0} and dieta.JERARQUIAID = {1} and dieta.FAMILIAID = {2} and dieta.NIVEL_ABADIA = {3} and dieta.ACTIVIDADID = {4}";

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oUtilsAD = new adUtils(con);
            //recuperación de los niveles de abadía existentes
            alNivelesAbadia = oUtilsAD.execSQLListInt("SELECT DISTINCT(nivel_abadia) FROM dieta");
            //recuperación de las jerarquias de monjes existentes
            alJerarquias = oUtilsAD.execSQLListInt("SELECT DISTINCT(jerarquiaid) FROM dieta");
            //recuperacion de las actividades / tareas
            alActividades = oUtilsAD.execSQLListInt("SELECT DISTINCT(actividadid) FROM dieta");
            //Recuperación de las familias de alimentos
            alFamilias = oUtilsAD.execSQLListInt("SELECT DISTINCT(familiaid) FROM dieta");
            //recuperación de idiomas
            alIdiomas = oUtilsAD.execSQLListInt("SELECT idiomaid FROM idioma");

            //bucle para cargar las tablas de alimentos vinculados a cada familia, jerarquia y nivel.
            //hmDietas = new HashMap<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, List<Table>>>>>();
            for (Integer iNivel : alNivelesAbadia) {
                hmAlimentosJerarquia = new HashMap<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, List<Table>>>>>();
                for (Integer iJerarquia : alJerarquias) {
                    hmAlimentosActividad = new HashMap<Integer, HashMap<Integer, HashMap<Integer, List<Table>>>>();
                    for (Integer iActividad : alActividades) {
                        hmAlimentosFamilia = new HashMap<Integer, HashMap<Integer, List<Table>>>();
                        for (Integer iFamilia : alFamilias) {
                            hmAlimentosIdioma = new HashMap<Integer, List<Table>>();
                            for (Integer iIdioma : alIdiomas) {
                                alListaAlimentos = oUtilsAD.execSQLList(MessageFormat.format(szSQL,
                                        new Integer[]{iIdioma, iJerarquia, iFamilia, iNivel, iActividad}));
                                if (!alListaAlimentos.isEmpty())
                                    hmAlimentosIdioma.put(iIdioma, oUtilsAD.execSQLList(MessageFormat.format(szSQL,
                                            new Integer[]{iIdioma, iJerarquia, iFamilia, iNivel, iActividad})));
                            }
                            if (!hmAlimentosIdioma.isEmpty())
                                hmAlimentosFamilia.put(iFamilia, hmAlimentosIdioma);
                        }
                        if (!hmAlimentosFamilia.isEmpty())
                            hmAlimentosActividad.put(iActividad, hmAlimentosFamilia);
                    }
                    if (!hmAlimentosActividad.isEmpty())
                        hmAlimentosJerarquia.put(iJerarquia, hmAlimentosActividad);
                }
                if (!hmAlimentosJerarquia.isEmpty())
                    hmDietasNivel.put(iNivel, hmAlimentosJerarquia);
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


/*
    public HashMap<Integer, HashMap<Integer, HashMap<Integer, List<Table>>>> getHashMap() {
        return hmDietas;
    }
*/

    /**
     * Recupera lista de alimentos para un nivel de abadía, familia, jerarquía e idioma determinado
     *
     * @param p_iNivelAbadia Nivel de Abadia
     * @param p_iFamilia     Familia de alimentos
     * @param p_iJerarquia   Jerarquia de monje
     * @param p_iIdioma      Idioma de usuario
     * @return
     * @throws AbadiaException Exception general
     */
    public static List<Table> getValueList(int p_iNivelAbadia, int p_iFamilia, int p_iJerarquia, int p_iIdioma, int p_iActividad) throws AbadiaException {
        String sTrace = ourInstance.getClass() + ".getValueList(" + p_iNivelAbadia + ", " + p_iFamilia + ", " + p_iJerarquia + ", " + p_iIdioma + ", " + p_iActividad + ")";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        try {
            if (p_iFamilia == 0) {
                return new ArrayList<Table>();
            } else
                return ourInstance.hmDietasNivel.get(p_iNivelAbadia).get(p_iJerarquia).get(p_iActividad).get(p_iFamilia).get(p_iIdioma);
        } catch (NullPointerException e) {
            log.error(sTrace, e);
            return new ArrayList<Table>();
        } finally {
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    /**
     * Recuperar una la familia que corresonde para una alimentación determinada.
     *
     * @param p_iNivelAbadia
     * @param p_iJerarquia
     * @param p_iActividad
     * @return
     * @throws AbadiaException
     */
    public static Integer getFamiliaAlimentosActividad(int p_iNivelAbadia, int p_iJerarquia, int p_iActividad) throws AbadiaException {
        String sTrace = ourInstance.getClass() + ".getFamiliaAlimentosActividad(" + p_iNivelAbadia + ", " + p_iJerarquia + " ," + p_iActividad + ")";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);
        HashMap<Integer, HashMap<Integer, List<Table>>> hmFamiliaAlimento;
        Iterator itFamilias;
        Integer iFamilia = 0;
        try {
            hmFamiliaAlimento = ourInstance.hmDietasNivel.get(p_iNivelAbadia).get(p_iJerarquia).get(p_iActividad);
            itFamilias = hmFamiliaAlimento.keySet().iterator();
            while (itFamilias.hasNext()) {
                iFamilia = (Integer) itFamilias.next();
                break;
            }
            return iFamilia;
        } catch (NullPointerException e) {
            log.error(sTrace, e);
            return 0;
        } finally {
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }


    }

    /**
     * Recupera lista de alimentos que corresponden para un nivel determinado de abadía, una familia y una jerarquía
     *
     * @param p_iNivelAbadia Nivel de la abadía
     * @param p_iFamilia     Familia de alimentos
     * @param p_iJerarquia   Jerarquia del monje
     * @return
     * @throws AbadiaException Excepción general
     */
    public static List<Table> getAlimentosFamiliaJerarquiaNivelActividad(int p_iNivelAbadia, int p_iFamilia, int p_iJerarquia, int p_iActividad) throws AbadiaException {
        String sTrace = ourInstance.getClass() + ".getAlimentosFamiliaJerarquiaNivelActividad(" + p_iNivelAbadia + ", " + p_iFamilia + ", " + p_iJerarquia + ", " + p_iActividad + ")";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        try {
            if (p_iFamilia == 0) {
                return new ArrayList<Table>();
            } else
                //return ourInstance.hmDietasNivel.get(p_iNivelAbadia).get(p_iFamilia).get(p_iJerarquia).get(1).get(p_iActividad);
                return ourInstance.hmDietasNivel.get(p_iNivelAbadia).get(p_iJerarquia).get(p_iActividad).get(p_iFamilia).get(1);
        } catch (NullPointerException e) {
            log.error(sTrace, e);
            return new ArrayList<Table>();
        } finally {
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }


    }


    public static void reload() {
        ourInstance = new CargasInicialesDietasBBean();
    }
}