package org.abbatia.process.bbean;

import org.abbatia.adbean.*;
import org.abbatia.bean.*;
import org.abbatia.core.CoreTiempo;
import org.abbatia.dbms.ConnectionFactory;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.exception.base.SystemException;
import org.abbatia.process.adbean.adProcesos;
import org.abbatia.process.prTrabajos;
import org.abbatia.process.utils.ProcesosUtils;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.Utilidades;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Benjamín Rodríguez.
 * User: benjamin
 * Date: 24-may-2008
 * Time: 21:06:39
 */
public class ProcesosBBean {
    private static Logger log = Logger.getLogger(ProcesosBBean.class.getName());

    public void ganaderia_producirAlimentos(Hashtable htStress) throws AbadiaException {

        String sTrace = this.getClass() + ".ganaderia_producirAlimentos()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);


        Hashtable<Integer, AnimalProduccion> htProduccion = null;
        AnimalProduccion produccionAlimento;
        adUtils oUtilsAD;
        adProcesos oProcesosAD;

        int ctd1;
        int edificioid = 0;
        EdificioProceso edificio = null;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);
            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando SUBProceso ganaderia_producirAlimentos", 0);

            ArrayList<AnimalProceso> animalesAL = oProcesosAD.recuperarAnimalesProduccionAlimentos();

            Iterator<AnimalProceso> animales = animalesAL.iterator();
            AnimalProceso animal;

            // poner todo a 0
            oUtilsAD = new adUtils(con);
            oUtilsAD.execSQL("DELETE FROM animales_produccion_alimento");

            // Calcular para cada animal el número de unidades que se pueden recoger
            while (animales.hasNext()) {
                animal = animales.next();

                if (animal.getIdEdificio() != edificioid) {
                    //cambio de edificio, actualizamos registro.
                    if (edificioid != 0) {
                        oProcesosAD.generarProduccionGanaderiaAlimentos(htProduccion);
                    }
                    edificioid = animal.getIdEdificio();
                    //debemos calcular la capacidad del edificio
                    edificio = (EdificioProceso) htStress.get(new Integer(edificioid));
                    htProduccion = new Hashtable<Integer, AnimalProduccion>();
                }
                if (edificio != null) {
                    //si el edificio excede su capacidad de almacenamiento
                    //los animales o deberian producir nada.
                    if (edificio.getExtresado() == 0) {
                        // Obtenemos un random entre la produccion max y la min
                        ctd1 = Utilidades.Random(animal.getMin(), animal.getMax());
                        if (ctd1 != 0) {
                            //obtengo el objeto correspondiente al producto que se procesa
                            produccionAlimento = htProduccion.get(new Integer(animal.getIdAlimento()));
                            if (produccionAlimento != null) {
                                //incremento la cantidad.
                                produccionAlimento.setCantidad(produccionAlimento.getCantidad() + ctd1);
                            } else {
                                //si no existe entrada para ese producto...
                                //creo el producto, lo informo y lo añado a la lista.
                                produccionAlimento = new AnimalProduccion();
                                produccionAlimento.setCantidad(ctd1);
                                produccionAlimento.setIdAlimento(animal.getIdAlimento());
                                produccionAlimento.setIdEdificio(animal.getIdEdificio());
                                produccionAlimento.setRecoge_monje(animal.getRecoge_monje());
                                htProduccion.put(animal.getIdAlimento(), produccionAlimento);
                            }

                        }
                    }
                }
            }
            oProcesosAD.addLog("- Fin SUBProceso ganaderia_producirAlimentos", 0);

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public void ganaderia_producirRecursos(Hashtable htStress) throws AbadiaException {

        String sTrace = this.getClass() + ".ganaderia_producirRecursos()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        Hashtable<Integer, AnimalProduccion> htProduccion = null;
        AnimalProduccion produccionRecurso;
        adUtils oUtilsAD;
        adProcesos oProcesosAD;

        double ctd1;
        int edificioid = 0;
        EdificioProceso edificio = null;
        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);
            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando SUBProceso ganaderia_producirRecursos", 0);

            ArrayList<AnimalProceso> animalesAL = oProcesosAD.recuperarAnimalesProduccionRecursos();

            Iterator<AnimalProceso> animales = animalesAL.iterator();
            AnimalProceso animal;

            // poner todo a 0
            oUtilsAD = new adUtils(con);
            oUtilsAD.execSQL("DELETE FROM animales_produccion_recurso");

            // Calcular para cada animal el número de unidades que se pueden recoger
            while (animales.hasNext()) {
                animal = animales.next();

                if (animal.getIdEdificio() != edificioid) {
                    //cambio de edificio, actualizamos registro.
                    if (edificioid != 0) {
                        oProcesosAD.generarProduccionGanaderiaRecursos(htProduccion);
                    }
                    edificioid = animal.getIdEdificio();
                    //debemos calcular la capacidad del edificio
                    edificio = (EdificioProceso) htStress.get(new Integer(edificioid));
                    htProduccion = new Hashtable<Integer, AnimalProduccion>();
                }
                if (edificio != null) {
                    //si el edificio excede su capacidad de almacenamiento
                    //los animales o deberian producir nada.
                    if (edificio.getExtresado() == 0) {
                        // Obtenemos un random entre la produccion max y la min
                        ctd1 = Utilidades.RandomDouble(animal.getMin(), animal.getMax());
                        if (ctd1 != 0) {
                            //obtengo el objeto correspondiente al producto que se procesa
                            produccionRecurso = htProduccion.get(new Integer(animal.getIdRecurso()));
                            if (produccionRecurso != null) {
                                //incremento la cantidad.
                                produccionRecurso.setCantidad(produccionRecurso.getCantidad() + ctd1);
                            } else {
                                //si no existe entrada para ese producto...
                                //creo el producto, lo informo y lo añado a la lista.
                                produccionRecurso = new AnimalProduccion();
                                produccionRecurso.setCantidad(ctd1);
                                produccionRecurso.setIdRecurso(animal.getIdRecurso());
                                produccionRecurso.setIdEdificio(animal.getIdEdificio());
                                produccionRecurso.setRecoge_monje(animal.getRecoge_monje());
                                htProduccion.put(animal.getIdRecurso(), produccionRecurso);
                            }

                        }
                    }
                }
            }
            oProcesosAD.addLog("- Fin SUBProceso ganaderia_producirRecursos", 0);

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    /**
     * Ejecuta el cálculo de la fe de los monjes en función de los siguiente parametros
     * - Número de rezos al día
     * - Edificios relacionados con la fe y sus niveles
     * - Fe del abad, los obispos, los cardenales y el papa
     *
     * @param estado Estado de los monjes que queremos procesar (vivos y de viaje)
     * @throws org.abbatia.exception.base.AbadiaException
     *          Excepción base de abbatia
     */
    public void control_habilidades_fe(int estado) throws AbadiaException {

        String sTrace = this.getClass() + ".control_habilidades_fe(" + estado + ")";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        String sSQLFe = "";

        //esta select sólo afecta a los monjes que se encuentran en su abbatia..
        if (estado == Constantes.MONJE_VIVO) {
            sSQLFe = "Select ab.abadiaid, m.monjeid, sum(acumulado_dia) acumulado_dia, ab.regionid, m.jerarquiaid, m.santo, hm.valor_actual as fe_monje, ab.usuarioid, m.estado" +
                    "          FROM actividad a, monje m, abadia ab, usuario u, habilidad_monje hm " +
                    "          where a.monjeid = m.monjeid and m.estado = 0 AND m.abadiaid = ab.abadiaid AND ab.usuarioid = u.usuarioid AND u.abadia_congelada = 0  and " +
                    "                a.actividadid in (" + Constantes.TAREA_REZAR + "," + Constantes.TAREA_ABAD + ") " +
                    "                AND hm.habilidadid = " + Constantes.HABILIDAD_FE + " AND hm.monjeid = m.monjeid" +
                    "         group by ab.abadiaid, m.monjeid ";
        } else if (estado == Constantes.MONJE_VISITA) {
            sSQLFe = "Select ab.abadiaid, m.monjeid, sum(acumulado_dia) acumulado_dia, ab.regionid, m.jerarquiaid, m.santo, hm.valor_actual as fe_monje, ab.usuarioid, m.estado,mv.abadiaid_destino" +
                    "          FROM actividad a, monje m, abadia ab, usuario u, habilidad_monje hm, monje_visita mv " +
                    "          where m.estado = 4 and  mv.monjeid = m.monjeid and mv.abadiaid_destino = ab.abadiaid AND ab.usuarioid = u.usuarioid AND u.abadia_congelada = 0  and " +
                    "                a.actividadid in (" + Constantes.TAREA_REZAR + "," + Constantes.TAREA_ABAD + ") AND a.monjeid = m.monjeid" +
                    "                AND hm.habilidadid = " + Constantes.HABILIDAD_FE + " AND hm.monjeid = m.monjeid" +
                    "         group by mv.abadiaid_destino, m.monjeid ";
        }
        String sSQLHab2 = "UPDATE `monje` set santo = 1, nombre = CONCAT('San ', nombre) WHERE monjeid=?";

        adUtils oUtilsAD;
        adHabilidades oHabilidadesAD;
        adEdificio oEdificioAD;
        adProcesos oProcesosAD;

        double val, val1, val2, val3, val4, val5, fe_obispo = 0, fe_abad = 0, iglesia_nivel = 0, oratorio_nivel = 0, claustro_nivel = 0, coro_nivel = 0;
        double carisma_abad = 0, cardenal_abadia = 0, papa_abadia = 0, obispo_abadia = 0, santo = 0, libros = 0;
        int last_abadiaid = 0, mes_alta = 0, anyo_alta = 0, mes_hoy, anyo_hoy;

        // Pillar la fecha de hoy
        GregorianCalendar tiempoReal;
        tiempoReal = CoreTiempo.getTiempoReal();
        mes_hoy = tiempoReal.get(GregorianCalendar.MONTH) + 1;
        anyo_hoy = tiempoReal.get(GregorianCalendar.YEAR);


        PreparedStatement ps = null;
        PreparedStatement psHab = null;
        PreparedStatement psHab2 = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);
            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando SUBProceso aumentar_habilidades_fe: " + estado, 0);

            // recuperamos la fe del papa y la media de fe de los cardenales
            oHabilidadesAD = new adHabilidades(con);
            double fe_papa = oHabilidadesAD.recuperaHabilidadPapa(Constantes.HABILIDAD_FE);
            double fe_cardenal = oHabilidadesAD.recuperaHabilidadCardenales(Constantes.HABILIDAD_FE);

            oUtilsAD = new adUtils(con);
            oEdificioAD = new adEdificio(con);

            ps = con.prepareStatement(sSQLFe);
            rs = ps.executeQuery();

            while (rs.next()) {

                if ((last_abadiaid != rs.getInt("abadiaid") && estado == Constantes.MONJE_VIVO) || (estado == Constantes.MONJE_VISITA && last_abadiaid != rs.getInt("abadiaid_destino"))) {

                    //recuperamos la fe dle obispo por cada región
                    fe_obispo = oHabilidadesAD.recuperaHabilidadObispo(Constantes.HABILIDAD_FE, rs.getInt("regionid"));
                    if (estado == Constantes.MONJE_VIVO) {
                        //recuperamos la fe del abad por cada abbatia
                        fe_abad = oHabilidadesAD.recuperaHabilidadAbad(Constantes.HABILIDAD_FE, rs.getInt("abadiaid"));
                        //recuperamos el carisma del abad por cada abbatia
                        carisma_abad = oHabilidadesAD.recuperaHabilidadAbad(Constantes.HABILIDAD_CARISMA, rs.getInt("abadiaid"));
                    } else {
                        //recuperamos la fe del abad por cada abbatia
                        fe_abad = oHabilidadesAD.recuperaHabilidadAbad(Constantes.HABILIDAD_FE, rs.getInt("abadiaid_destino"));
                        //recuperamos el carisma del abad por cada abbatia
                        carisma_abad = oHabilidadesAD.recuperaHabilidadAbad(Constantes.HABILIDAD_CARISMA, rs.getInt("abadiaid_destino"));
                    }


                    mes_alta = oUtilsAD.getSQL("SELECT month(fechaalta) FROM usuario WHERE usuarioid = " + rs.getInt("usuarioid"), 0);
                    anyo_alta = oUtilsAD.getSQL("SELECT year(fechaalta) FROM usuario WHERE usuarioid = " + rs.getInt("usuarioid"), 0);

                    // Obispo
                    if (estado == Constantes.MONJE_VIVO) {
                        obispo_abadia = oUtilsAD.getSQL("SELECT monjeid FROM monje WHERE  " +
                                " jerarquiaid = " + Constantes.JERARQUIA_OBISPO + " AND estado=" + Constantes.MONJE_VIVO + " AND abadiaid = " + rs.getInt("abadiaid"), 0);
                    } else {
                        obispo_abadia = oUtilsAD.getSQL("SELECT monjeid FROM monje WHERE  " +
                                " jerarquiaid = " + Constantes.JERARQUIA_OBISPO + " AND estado=" + Constantes.MONJE_VIVO + " AND abadiaid = " + rs.getInt("abadiaid_destino"), 0);
                    }
                    if (estado == Constantes.MONJE_VIVO) {
                        if (obispo_abadia == 0)  // Lo tenemos de visita en nuestra abadía?
                            obispo_abadia = oUtilsAD.getSQL("SELECT m.monjeid FROM monje m, monje_visita mv WHERE  " +
                                    "m.jerarquiaid = " + Constantes.JERARQUIA_OBISPO + " AND m.estado=" + Constantes.MONJE_VISITA + " AND m.monjeid = mv.monjeid AND mv.abadiaid_destino = " + rs.getInt("abadiaid"), 0);
                    } else {
                        if (obispo_abadia == 0)  // Lo tenemos de visita en nuestra abadía?
                            obispo_abadia = oUtilsAD.getSQL("SELECT m.monjeid FROM monje m, monje_visita mv WHERE  " +
                                    "m.jerarquiaid = " + Constantes.JERARQUIA_OBISPO + " AND m.estado=" + Constantes.MONJE_VISITA + " AND m.monjeid = mv.monjeid AND mv.abadiaid_destino = " + rs.getInt("abadiaid_destino"), 0);
                    }
                    // Cardenal
                    if (estado == Constantes.MONJE_VIVO) {
                        cardenal_abadia = oUtilsAD.getSQL("SELECT monjeid FROM monje WHERE  " +
                                " jerarquiaid = " + Constantes.JERARQUIA_CARDENAL + " AND estado=" + Constantes.MONJE_VIVO + " AND abadiaid = " + rs.getInt("abadiaid"), 0);
                    } else {
                        cardenal_abadia = oUtilsAD.getSQL("SELECT monjeid FROM monje WHERE  " +
                                " jerarquiaid = " + Constantes.JERARQUIA_CARDENAL + " AND estado=" + Constantes.MONJE_VIVO + " AND abadiaid = " + rs.getInt("abadiaid_destino"), 0);
                    }
                    if (rs.getInt("estado") == Constantes.MONJE_VIVO) {
                        if (cardenal_abadia == 0)    // Lo tenemos de visita en nuestra abadía?
                            cardenal_abadia = oUtilsAD.getSQL("SELECT m.monjeid FROM monje m, monje_visita mv WHERE  " +
                                    "m.jerarquiaid = " + Constantes.JERARQUIA_CARDENAL + " AND m.estado=" + Constantes.MONJE_VISITA + " AND m.monjeid = mv.monjeid AND mv.abadiaid_destino = " + rs.getInt("abadiaid"), 0);
                    } else {
                        if (cardenal_abadia == 0)    // Lo tenemos de visita en nuestra abadía?
                            cardenal_abadia = oUtilsAD.getSQL("SELECT m.monjeid FROM monje m, monje_visita mv WHERE  " +
                                    "m.jerarquiaid = " + Constantes.JERARQUIA_CARDENAL + " AND m.estado=" + Constantes.MONJE_VISITA + " AND m.monjeid = mv.monjeid AND mv.abadiaid_destino = " + rs.getInt("abadiaid_destino"), 0);
                    }
                    // Papa
                    if (estado == Constantes.MONJE_VIVO) {
                        papa_abadia = oUtilsAD.getSQL("SELECT monjeid FROM monje WHERE  " +
                                " jerarquiaid = " + Constantes.JERARQUIA_PAPA + " AND estado=" + Constantes.MONJE_VIVO + " AND abadiaid = " + rs.getInt("abadiaid"), 0);
                    } else {
                        papa_abadia = oUtilsAD.getSQL("SELECT monjeid FROM monje WHERE  " +
                                " jerarquiaid = " + Constantes.JERARQUIA_PAPA + " AND estado=" + Constantes.MONJE_VIVO + " AND abadiaid = " + rs.getInt("abadiaid_destino"), 0);
                    }
                    if (estado == Constantes.MONJE_VIVO) {
                        if (papa_abadia == 0)    // Lo tenemos de visita en nuestra abadía?
                            papa_abadia = oUtilsAD.getSQL("SELECT m.monjeid FROM monje m, monje_visita mv WHERE  " +
                                    "m.jerarquiaid = " + Constantes.JERARQUIA_PAPA + " AND m.estado=" + Constantes.MONJE_VISITA + " AND m.monjeid = mv.monjeid AND mv.abadiaid_destino = " + rs.getInt("abadiaid"), 0);
                    } else {
                        if (papa_abadia == 0)    // Lo tenemos de visita en nuestra abadía?
                            papa_abadia = oUtilsAD.getSQL("SELECT m.monjeid FROM monje m, monje_visita mv WHERE  " +
                                    "m.jerarquiaid = " + Constantes.JERARQUIA_PAPA + " AND m.estado=" + Constantes.MONJE_VISITA + " AND m.monjeid = mv.monjeid AND mv.abadiaid_destino = " + rs.getInt("abadiaid_destino"), 0);
                    }

                    //Santo
                    if (estado == Constantes.MONJE_VIVO) {
                        santo = oUtilsAD.getSQL("SELECT count(*) FROM monje WHERE abadiaid = " + rs.getInt("abadiaid") + " AND santo = 1" +
                                " AND estado = " + Constantes.MONJE_VIVO, 0);
                    } else {
                        santo = oUtilsAD.getSQL("SELECT count(*) FROM monje WHERE abadiaid = " + rs.getInt("abadiaid_destino") + " AND santo = 1" +
                                " AND estado = " + Constantes.MONJE_VIVO, 0);
                    }


                    if (santo == 0) {
                        if (estado == Constantes.MONJE_VIVO) {
                            santo = oUtilsAD.getSQL("SELECT count(*) FROM monje m,monje_visita mv WHERE mv.abadiaid_destino = " + rs.getInt("abadiaid") + " AND m.santo = 1" +
                                    " AND m.estado = " + Constantes.MONJE_VISITA + " AND mv.monjeid = m.monjeid", 0);
                        } else {
                            santo = oUtilsAD.getSQL("SELECT count(*) FROM monje m,monje_visita mv WHERE mv.abadiaid_destino = " + rs.getInt("abadiaid_destino") + " AND m.santo = 1" +
                                    " AND m.estado = " + Constantes.MONJE_VISITA + " AND mv.monjeid = m.monjeid", 0);
                        }
                    }
                    if (estado == Constantes.MONJE_VIVO) {
                        libros = oUtilsAD.getSQL("SELECT impacto FROM libro l, libro_habilidad lh WHERE l.abadiaid = " + rs.getInt("abadiaid") + " AND l.tipo_libroid = lh.tipo_libroid " +
                                " AND lh.habilidadid = " + Constantes.HABILIDAD_FE + " AND l.nivel = lh.nivel AND l.estado = " + Constantes.ESTADO_LIBRO_COMPLETO + " GROUP BY l.tipo_libroid", 0);
                    } else {
                        libros = oUtilsAD.getSQL("SELECT impacto FROM libro l, libro_habilidad lh WHERE l.abadiaid = " + rs.getInt("abadiaid_destino") + " AND l.tipo_libroid = lh.tipo_libroid " +
                                " AND lh.habilidadid = " + Constantes.HABILIDAD_FE + " AND l.nivel = lh.nivel AND l.estado = " + Constantes.ESTADO_LIBRO_COMPLETO + " GROUP BY l.tipo_libroid", 0);
                    }

                    /************************************************************/
                    /*        Control de edificios                              */
                    /************************************************************/

                    //recupero nivel de iglesia
                    if (estado == Constantes.MONJE_VIVO) {
                        iglesia_nivel = oEdificioAD.recuperarNivelEdificio(rs.getInt("abadiaid"), Constantes.EDIFICIO_IGLESIA);

                        //recupero nivel de claustro
                        claustro_nivel = oEdificioAD.recuperarNivelEdificio(rs.getInt("abadiaid"), Constantes.EDIFICIO_CLAUSTRO);
                        //recupero nivel de coro
                        coro_nivel = oEdificioAD.recuperarNivelEdificio(rs.getInt("abadiaid"), Constantes.EDIFICIO_CORO);
                        //recupero nivel de oratorio
                        oratorio_nivel = oEdificioAD.recuperarNivelEdificio(rs.getInt("abadiaid"), Constantes.EDIFICIO_ORATORIO);
                    } else {
                        iglesia_nivel = oEdificioAD.recuperarNivelEdificio(rs.getInt("abadiaid_destino"), Constantes.EDIFICIO_IGLESIA);

                        //recupero nivel de claustro
                        claustro_nivel = oEdificioAD.recuperarNivelEdificio(rs.getInt("abadiaid_destino"), Constantes.EDIFICIO_CLAUSTRO);
                        //recupero nivel de coro
                        coro_nivel = oEdificioAD.recuperarNivelEdificio(rs.getInt("abadiaid_destino"), Constantes.EDIFICIO_CORO);
                        //recupero nivel de oratorio
                        oratorio_nivel = oEdificioAD.recuperarNivelEdificio(rs.getInt("abadiaid_destino"), Constantes.EDIFICIO_ORATORIO);
                    }

                    carisma_abad = (carisma_abad / 100);
                    if (estado == Constantes.MONJE_VIVO) {
                        last_abadiaid = rs.getInt("abadiaid");
                    } else {
                        last_abadiaid = rs.getInt("abadiaid_destino");
                    }
                }


                val1 = 0;
                val4 = 0;

                //calculo de la fe de Maitines.
                //si estamos calculando la fe de un monje superior a un obispo, no cuenta la fe de los inferiores.
                if (rs.getInt("jerarquiaid") <= Constantes.JERARQUIA_ABAD)
                    val1 = (float) (Utilidades.Random(1, 50) / 100) + (float) ((fe_obispo / 100) / 2);
                else {
                    //Si es un obispo, calculamos la fe a partir de los cardenales.
                    if (rs.getInt("jerarquiaid") <= Constantes.JERARQUIA_OBISPO) {
                        val1 = (float) (Utilidades.Random(1, 50) / 100) +
                                (float) ((fe_cardenal / 100) / 2);
                    } else if (rs.getInt("jerarquiaid") >= Constantes.JERARQUIA_CARDENAL) {
                        //Si es un cardenal calculamos la fe a partir de el Papa.
                        val1 = (float) (Utilidades.Random(1, 50) / 100) +
                                (float) ((fe_papa / 100) / 2);
                    }

                }

                //calculo de la fe de Laudes
                if (rs.getInt("jerarquiaid") <= Constantes.JERARQUIA_OBISPO) {
                    val2 = (float) ((2 / (5 - iglesia_nivel)) +
                            (float) (1 / (5 - coro_nivel)) +
                            (float) (1 / (5 - claustro_nivel)) +
                            (float) (1 / (5 - oratorio_nivel)) +
                            ((fe_cardenal / 100) / 2));
                } else {
                    //si es un cardenal calculamos la fe a partir del Papa.
                    val2 = (float) ((2 / (5 - iglesia_nivel)) +
                            (float) (1 / (5 - coro_nivel)) +
                            (float) (1 / (5 - claustro_nivel)) +
                            (float) (1 / (5 - oratorio_nivel)) +
                            ((fe_papa / 100) / 2));

                }

                //calculo de la fe de Angelus
                val3 = (Utilidades.Random(1, 50) / 100) + ((fe_papa / 100) / 2);

                //calculo de los rezos de prima, tercia, nona y Visperas
                if (rs.getInt("jerarquiaid") <= Constantes.JERARQUIA_MONJE) {
                    val4 = (float) ((Utilidades.Random(1, (51 - (50 * carisma_abad))) / 100) +
                            (((fe_abad / 100) / (2 - carisma_abad)))) * (float) (rs.getInt("acumulado_dia") - 3);
                } else {
                    if (rs.getInt("jerarquiaid") == Constantes.JERARQUIA_ABAD) {
                        val4 = ((Utilidades.Random(1, 50) / 100) +
                                (float) ((fe_obispo / 100) / 2)) * (float) (rs.getInt("acumulado_dia") - 3);
                    } else if (rs.getInt("jerarquiaid") == Constantes.JERARQUIA_OBISPO) {
                        val4 = ((Utilidades.Random(1, 50) / 100) +
                                (float) ((fe_cardenal / 100) / 2)) * (float) (rs.getInt("acumulado_dia") - 3);
                    } else if (rs.getInt("jerarquiaid") >= Constantes.JERARQUIA_CARDENAL) {
                        val4 = ((Utilidades.Random(1, 50) / 100) +
                                (float) ((fe_papa / 100) / 2)) * (float) (rs.getInt("acumulado_dia") - 3);

                    }

                }
                val5 = 0;

                if (obispo_abadia != 0)
                    val5 = (float) 0.5;

                if (cardenal_abadia != 0)
                    val5 = (float) (val5 + 1);

                if (papa_abadia != 0)
                    val5 = (float) (val5 + 2);

                //if (santo == 1)
                //   val5 = (float) (val5 + 1);
                val5 = (float) (val5 + santo);

                //acumulado de todos los rezos del dia.
                val = (float) ((val1 + val2 + val3 + val4 + val5) - 4) + libros;

                if (val < 0)
                    val = -1;
                if (rs.getInt("fe_monje") + val <= 50) {
                    if (rs.getInt("acumulado_dia") >= 4) {
                        if (rs.getInt("fe_monje") < 50)
                            val = 1;
                        else
                            val = 0;

                    }
                }

                //el Papa puede llegar a Santo
                if (rs.getInt("jerarquiaid") != Constantes.JERARQUIA_PAPA) {
                    //en el caso de que la fe llegue hasta mas de 95.
                    if (rs.getInt("fe_monje") + val > 95) {
                        //tiene un 5% de posibilidades de subir por encima de 95.
                        //tienen que llevar mas de un mes real jugando para llegar a ser Santos.
                        if (Utilidades.Random(1, 100) <= 10 && (((mes_alta >= mes_hoy + 1) && (anyo_alta == anyo_hoy)) || (anyo_hoy > anyo_alta)))
                            val = 1; //subira de uno en uno hasta 100.
                        else
                            val = 95 - rs.getInt("fe_monje"); //lo dejamos para que llegue a 95 justo.
                    }
                }
                //se puede producir un santo aleatoriamente a cualquier monje. Una oportunidad entre 2000
                //los santos solo pueden aparecer aleatoriamente si llevan mas de un mes real jugando.
                if (Utilidades.Random(1, 1000) == 1 && (((mes_alta >= mes_hoy + 1) && (anyo_alta == anyo_hoy)) || (anyo_hoy > anyo_alta))) {
                    if (Utilidades.Random(1, 500) == 1) {
                        val = 100 - rs.getInt("fe_monje");
                    }
                }
                val = Math.round(val);

                if (val != 0) {
                    //incrementamos la fe
                    oHabilidadesAD.incrementarHabilidad(rs.getInt("monjeid"), Constantes.HABILIDAD_FE, val);
                }
                //Hacer Santos.
                if (val + rs.getInt("fe_monje") >= 100 & rs.getInt("santo") == 0 & rs.getInt("jerarquiaid") > 0) {
                    psHab2 = con.prepareStatement(sSQLHab2);
                    psHab2.setInt(1, rs.getInt("monjeid"));
                    psHab2.execute();
                    psHab2.close();
                }
            }

            // FE! - Por si aca ;-)
            oHabilidadesAD.resetearHabilidad(Constantes.HABILIDAD_FE);

            oProcesosAD.addLog("- Finalizando SUBProceso aumentar_habilidades_fe: " + estado, 0);

        } catch (SQLException e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } catch (Exception e2) {
            throw new AbadiaSQLException(sTrace, e2, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(psHab);
            DBMSUtils.cerrarObjetoSQL(psHab2);
            DBMSUtils.cerrarObjetoSQL(ps);
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    /*
    pescar pescaditos en un periodo
    */

    public void pescar(int periodo) throws AbadiaException {
        String sTrace = this.getClass() + ".pescar(" + periodo + ")";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);


        adUtils utils;
        adHabilidades oHabilidadesAD;
        adActividad oActividadAD;
        adAlimentoLotes oAlimentoAD;
        adProcesos oProcesosAD;
        adLiterales oLiteralesAD;

        AlimentoLote alimentoLote;


        Connection con = null;

        int fe, destreza, ctd, edificioid, alimentoid, loteid, last_idiomaid = 0;
        float valor;
        String msg, msg1 = "", msg2 = "", pescao;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);

            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando SUBProceso pescar", 0);
            ArrayList<MonjeProceso> monjesAl = oProcesosAD.recuperarMonjesActividadPeriodo(periodo, Constantes.TAREA_PESCAR);
            Iterator<MonjeProceso> monjes = monjesAl.iterator();

            oLiteralesAD = new adLiterales(con);
            utils = new adUtils(con);
            oHabilidadesAD = new adHabilidades(con);
            oAlimentoAD = new adAlimentoLotes(con);
            oActividadAD = new adActividad(con);

            MonjeProceso monje;
            while (monjes.hasNext()) {
                monje = monjes.next();
                if (last_idiomaid != monje.getIdIdioma()) {
                    last_idiomaid = monje.getIdIdioma();
                    //utils = new adUtils(Constantes.DB_CONEXION_PROCESS);
                    msg1 = oLiteralesAD.getLiteralStatic(10004, last_idiomaid);  // Ha pescado %d unidades de pescado
                    msg2 = oLiteralesAD.getLiteralStatic(10013, last_idiomaid);  // No has recogido nada
                    //utils.finalize();
                }

                alimentoid = utils.getSQL("SELECT alimentoid FROM `alimentos_tipo`  where familiaid = 3 and saladoid is not null order by rand() limit 1;", 2);
                pescao = utils.getSQL("SELECT descripcion FROM `alimentos_tipo` where familiaid = 3 and alimentoid = " + alimentoid, "");


                fe = (int) oHabilidadesAD.recuperarHabilidad(monje.getIdMonje(), Constantes.HABILIDAD_FE);
                destreza = (int) oHabilidadesAD.recuperarHabilidad(monje.getIdMonje(), Constantes.HABILIDAD_DESTREZA);

                valor = (destreza * fe) / 100;

                if (valor != 0)
                    ctd = Math.round((10 * valor) / 100);
                else ctd = 0;
                if (ctd != 0) {
                    msg = ProcesosUtils.Format(msg1, ctd);
                    msg = ProcesosUtils.Format(msg, pescao.toLowerCase());
                    // buscar el loteid para la fecha actual
                    edificioid = utils.getSQL("SELECT edificioid FROM `edificio` where abadiaid = " + monje.getIdAbadia() + " and tipoedificioid = " + Constantes.EDIFICIO_COCINA, 0);
                    loteid = utils.getSQL("SELECT a.loteid FROM alimentos a, alimentos_lote al where a.loteid=al.loteid and a.edificioid=" + edificioid + " and alimentoid = " + alimentoid + " and fecha_entrada ='" + CoreTiempo.getTiempoAbadiaString() + "'  LIMIT 1", -1);

                    if (loteid == -1) {
                        alimentoLote = new AlimentoLote();
                        alimentoLote.setCantidad(ctd);
                        alimentoLote.setIdAlimento(alimentoid);
                        alimentoLote.setIdEdificio(edificioid);
                        alimentoLote.setFechaEntrada(CoreTiempo.getTiempoAbadiaString());
                        alimentoLote.setEstado(100);

                        alimentoLote.setFechaCaducidad(CoreTiempo.getDiferenciaString(oAlimentoAD.recuperarCaducidad(alimentoid)));
                        oAlimentoAD.crearAlimentoLote(alimentoLote);
                    } else {
                        oAlimentoAD.sumarEnLote(loteid, ctd);
                    }

                    // Acumular
                    oActividadAD.incrementaAcumulado(monje.getIdMonje(), periodo, Constantes.TAREA_PESCAR, ctd);
                } else {
                    msg = msg2;
                }

                oProcesosAD.setActividadMens(monje.getIdMonje(), monje.getIdActividad(), periodo, msg, ctd);
            }

            oProcesosAD.addLog("- Finalizando SUBProceso pescar", 0);

        } catch (Exception e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public void moler(int periodo) throws AbadiaException {
        String sTrace = this.getClass() + ".moler(" + periodo + ")";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        String sSQL = "SELECT m.monjeid, u.idiomaid, e.nivel, ab.abadiaid " +
                " FROM `monje` m, `actividad` a, abadia ab, usuario u, edificio e " +
                " WHERE m.monjeid = a.monjeid and ab.abadiaid = m.abadiaid and ab.usuarioid = u.usuarioid and " +
                " m.estado = " + Constantes.MONJE_VIVO + " AND a.actividadid = " + Constantes.TAREA_MOLINERO +
                " and periodoid = ? and u.abadia_congelada = 0 and e.abadiaid=ab.abadiaid and e.tipoedificioid = " +
                Constantes.EDIFICIO_MOLINO + " order by u.idiomaid, ab.abadiaid";


        adUtils utils;
        adActividad oActividadAD;
        adProcesos oProcesosAD;
        adLiterales oLiteralesAD;
        adHabilidades oHabilidadesAD;

        int last_idiomaid = 0;
        int produccion;
        int fe, destreza, potencialProduccion;
        String msg, msg1 = "", msg2 = "";
        PreparedStatement ps = null;
        ResultSet rs = null;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);

            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando SUBProceso molinero", 0);

            oLiteralesAD = new adLiterales(con);
            oActividadAD = new adActividad(con);
            utils = new adUtils(con);
            oHabilidadesAD = new adHabilidades(con);

            ps = con.prepareStatement(sSQL);
            ps.setInt(1, periodo);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (last_idiomaid != rs.getInt("idiomaid")) {
                    last_idiomaid = rs.getInt("idiomaid");
                    //utils = new adUtils(Constantes.DB_CONEXION_PROCESS);
                    msg1 = oLiteralesAD.getLiteralStatic(10100, last_idiomaid);  // Ha trabajado en el molino
                    msg2 = oLiteralesAD.getLiteralStatic(10101, last_idiomaid);  // En el molino no hay trabajo
                    //utils.finalize();
                }

                //para determinar el potencial de producción, utilizar como en la pesca
                //destreza * fe / 100
                fe = (int) oHabilidadesAD.recuperarHabilidad(rs.getInt("MONJEID"), Constantes.HABILIDAD_FE);
                destreza = (int) oHabilidadesAD.recuperarHabilidad(rs.getInt("MONJEID"), Constantes.HABILIDAD_DESTREZA);
                potencialProduccion = fe * destreza / 100;
                if (potencialProduccion > 0) {
                    produccion = oProcesosAD.procesoMolinoPorMonje(rs.getInt("ABADIAID"), rs.getInt("NIVEL"), potencialProduccion);
                } else produccion = 0;

                if (produccion > 0) {
                    msg = msg1;
                } else msg = msg2;

                oProcesosAD.setActividadMens(rs.getInt("monjeid"), Constantes.TAREA_MOLINERO, periodo, msg, produccion);
                //incremento el acumulado diario del monje por monje/actividad con lo producido.
                oActividadAD.incrementaAcumulado(rs.getInt("monjeid"), periodo, Constantes.TAREA_MOLINERO, produccion);
            }

            utils.execSQL("DELETE from molino_produccion where ctd_actual < 1 ");
            oProcesosAD.addLog("- Finalizando SUBProceso molinero", 0);

        } catch (SQLException e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public void elaborarProductosEnReposo(short tipo_elaboracion) throws AbadiaException {
        String sTrace = this.getClass() + ".elaborarProductosEnReposo(" + tipo_elaboracion + ")";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);


        adElaboracion oElaboracionAD;
        adProcesos oProcesosAD;

        ArrayList<datosElaboracion> alProductos;
        Iterator<datosElaboracion> itProductos;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);

            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando SUBProceso elaborarProductosEnReposo: " + tipo_elaboracion, 0);
            alProductos = oProcesosAD.recuperarElaboracionPorTipo(tipo_elaboracion);

            datosElaboracion producto;
            if (!alProductos.isEmpty()) {
                itProductos = alProductos.iterator();
                while (itProductos.hasNext()) {
                    producto = itProductos.next();
                    crearRecursoElaboracion(producto, producto.getCantidad(), con);
                    producto.setElaborado(producto.getCantidad());
                    producto.setEstado(Constantes.ESTADO_ELABORACION_FINALIZADO);
                    producto.setFecha_fin(CoreTiempo.getTiempoAbadiaString());

                }
                oElaboracionAD = new adElaboracion(con);
                oElaboracionAD.actualizarElaboracionAlimentos(alProductos);
            }

            oProcesosAD.addLog("+ Fin SUBProceso elaborarProductosEnReposo: " + tipo_elaboracion, 0);

        } catch (Exception e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public void crearRecursoElaboracion(datosElaboracion producto, double cantidadProduccion, Connection p_cConnection) throws AbadiaException {
        adUtils utils;
        adAlimentoLotes alimentoAD;
        if (producto.getTipoProducto().equals(Constantes.MERCANCIA_ALIMENTOS_STR)) {
            utils = new adUtils(p_cConnection);
            int loteid = utils.getSQL("SELECT a.loteid FROM alimentos a, alimentos_lote al where a.loteid=al.loteid and a.edificioid=" + producto.getIdEdificio() + " and alimentoid = " + producto.getIdProducto() + " and fecha_entrada ='" + CoreTiempo.getTiempoAbadiaString() + "'  LIMIT 1", -1);
            if (loteid == -1) {
                AlimentoLote alimentoLote = new AlimentoLote();
                alimentoLote.setCantidad(cantidadProduccion);
                alimentoLote.setIdAlimento(producto.getIdProducto());
                alimentoLote.setIdEdificio(producto.getIdEdificio());
                alimentoLote.setFechaEntrada(CoreTiempo.getTiempoAbadiaString());
                alimentoLote.setEstado(100);

                alimentoAD = new adAlimentoLotes(p_cConnection);
                alimentoLote.setFechaCaducidad(CoreTiempo.getDiferenciaString(alimentoAD.recuperarCaducidad(producto.getIdProducto())));
                alimentoAD.crearAlimentoLote(alimentoLote);

            } else {
                alimentoAD = new adAlimentoLotes(p_cConnection);
                alimentoAD.sumarEnLote(loteid, cantidadProduccion);
            }
        } else //if (producto.getTipoProducto().equals(Constantes.MERCANCIA_RECURSOS_STR))
        {
            adRecurso recursoAD = new adRecurso(p_cConnection);
            recursoAD.sumarRecurso(producto.getIdProducto(), producto.getIdAbadia(), cantidadProduccion);
        }

    }

    public void elaborar(int periodo, short tipo_elaboracion, int tipo_edificio, int estado) throws AbadiaException {
        String sTrace = this.getClass() + ".elaborar(" + periodo + "," + tipo_elaboracion + "," + tipo_edificio + ", " + estado + ")";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        //String sSQL = "";
        ArrayList<MonjeElaboracion> alMonjes;
        Iterator<MonjeElaboracion> itMonjes;

        int last_idiomaid = 0;
        int last_abadiaid = 0;
        String msg1 = "", msg2 = "", msg3 = "", msg4 = "";
        ArrayList<datosElaboracion> listaElaboracion = new ArrayList<datosElaboracion>();
        HashMap<Integer, ArrayList<requisitoElaboracion>> requisitosHT = new HashMap<Integer, ArrayList<requisitoElaboracion>>();
        ArrayList<requisitoElaboracion> requisitos;
        String nombreEdificio;
        int existeRequisito;
        double cantidadRequisito;
        Iterator<requisitoElaboracion> listaRequisitos;

        adElaboracion oElaboracionAD;
        adEdificio oEdificioAD;
        adActividad oActividadAD;
        adHabilidades oHabilidadesAD;
        adProcesos oProcesosAD;
        adLiterales oLiteralesAD;

        requisitoElaboracion requisito;
        MonjeElaboracion monjeElaboracion;
        Iterator<datosElaboracion> productos;
        datosElaboracion producto;

        int contadorProduccion;
        int produccionPendiente;
        double destreza;
        double fe;
        double produccionMaxPorMonje;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);

            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando SUBProceso elaborar: " + tipo_elaboracion, 0);
            alMonjes = oProcesosAD.cargarMonjesElaboracion(estado, tipo_elaboracion, periodo, tipo_edificio);

            oElaboracionAD = new adElaboracion(con);
            oLiteralesAD = new adLiterales(con);
            oEdificioAD = new adEdificio(con);
            oHabilidadesAD = new adHabilidades(con);
            oActividadAD = new adActividad(con);

            itMonjes = alMonjes.iterator();
            //procesamos bucle por monje
            while (itMonjes.hasNext()) {
                monjeElaboracion = itMonjes.next();
                contadorProduccion = 0;
                //la primera pasada, recuperamos los requisitos de elaboracion de todos los productos.
                if (last_idiomaid == 0) {
                    requisitosHT = oElaboracionAD.recuperarRequisitosGlobales();
                }
                if (last_idiomaid != monjeElaboracion.getIdIdioma()) {
                    last_idiomaid = monjeElaboracion.getIdIdioma();
                    //utils = new adUtils(Constantes.DB_CONEXION_PROCESS);
                    msg1 = oLiteralesAD.getLiteralStatic(10110, last_idiomaid);  // Ha elaborado % unidades de %
                    msg2 = oLiteralesAD.getLiteralStatic(10111, last_idiomaid);  // Ha trabajado en la elaboración de:
                    msg3 = oLiteralesAD.getLiteralStatic(10112, last_idiomaid);  // No hay productos para elaborar en el edificio:
                    msg4 = oLiteralesAD.getLiteralStatic(10113, last_idiomaid);  // No ha podido cocinar por falta de ingredientes
                    //utils.finalize();
                }

                //Cada vez que se cambia de abbatia, se vuelven a recuperar los alimentos que se entan elaborando.
                if (last_abadiaid != monjeElaboracion.getIdAbadia()) {
                    //si se ha cambiado de abbatia y no es la primera vez..
                    if (last_abadiaid != 0) {
                        //actualizamos la base de datos con los valores actualizados sobre elaboracion
                        oElaboracionAD.actualizarElaboracionAlimentos(listaElaboracion);
                    }

                    last_abadiaid = monjeElaboracion.getIdAbadia();
                    //por cada abadía, recuperamos la lista de todos los productos que se están elaborando en el edificio, estados (finalizado, pausado y reposando)
                    listaElaboracion = oProcesosAD.recuperarElaboracionPorEdificio(monjeElaboracion.getIdEdificio(), last_idiomaid, tipo_elaboracion);
                }
                //si en el edificio no hay nada para elaborar
                //generamos un mensaje para notificarlo
                if (listaElaboracion.size() == 0) {
                    nombreEdificio = oEdificioAD.recuperarNombrePorEdificioId(monjeElaboracion.getIdEdificio(), monjeElaboracion.getIdIdioma());
                    //generamos mensaje de monje indicando que no hay nada que elaborar.
                    oProcesosAD.setActividadMens(monjeElaboracion.getIdMonje(), tipo_elaboracion, periodo, msg3 + " " + nombreEdificio, 0);
                }

                //reiniciamos la lista de productos a elaborar
                productos = listaElaboracion.iterator();

                //recuperamos parametros de habilidades del monje

                //recuperar fe
                fe = oHabilidadesAD.recuperarHabilidad(monjeElaboracion.getIdMonje(), Constantes.HABILIDAD_FE);
                //recuperar destreza
                destreza = oHabilidadesAD.recuperarHabilidad(monjeElaboracion.getIdMonje(), Constantes.HABILIDAD_DESTREZA);

                //calculamos la productividad del monje
                produccionMaxPorMonje = (destreza * fe * 3) / 1000;
                //si la producción no alcanza la unidad, fijamos 1 como valor mínimo
                if (produccionMaxPorMonje < 1) produccionMaxPorMonje = 1;
                produccionMaxPorMonje = produccionMaxPorMonje / listaElaboracion.size();
                //Procesamos los productos hasta que no queden más o mientras que el contador de producción no supere la producción
                //potencial de monje
                while (productos.hasNext()) {
                    //recuperamos el producto
                    producto = productos.next();

                    //si la cantidad a elaborar por día del producto supera la producción potencial de monje...
                    if (producto.getCtd_dia() > produccionMaxPorMonje) {
                        //fijamos la cantidad producida por día a valor de producción potencial del monje
                        producto.setCtd_dia((int) produccionMaxPorMonje);
                    }
                    if (producto.getCantidad() - producto.getElaborado() < producto.getCtd_dia()) {
                        produccionPendiente = producto.getCantidad() - (int) producto.getElaborado();
                    } else {
                        produccionPendiente = producto.getCtd_dia();
                    }
                    //si hemos elaborado una cantidad igual o superior a la cantidad total establecida y el producto está en estado elaborando y es de los que se elaboran
                    //de una vez  producto.getDias_total() == 1...
                    if (producto.getElaborado() >= producto.getCantidad() && producto.getEstado() == Constantes.ESTADO_ELABORACION_ELABORANDO && producto.getDias_total() == 1) {
                        //marcamos el producto como finalizado.
                        producto.setEstado(Constantes.ESTADO_ELABORACION_FINALIZADO);
                        //else, si el producto no está finalizado, no está reposando y es de elaboración "reposada"....
                    } else if (producto.getEstado() != Constantes.ESTADO_ELABORACION_FINALIZADO && producto.getEstado() != Constantes.ESTADO_ELABORACION_REPOSANDO) {
                        //recupero de la hash la lista de requisitos para elaborar este producto
                        requisitos = requisitosHT.get(new Integer(producto.getIdProducto()));
                        //si la lista de requisitos está informada...
                        if (!requisitos.isEmpty()) {
                            //si se trata de un producto que no depende de recursos si no de tiempo
                            //y está reposando, se supone que ya se han restado los recursos, por lo tanto
                            // no vuelven a restarse ni validarse
                            existeRequisito = 0;
                            //para cualquier estado distinto de "pendiente" en elaboraciones largas (queso)
                            //de este modo no solo validamos los requerimientos la primera vez
                            if (producto.getEstado() != Constantes.ESTADO_ELABORACION_REPOSANDO) {
                                listaRequisitos = requisitos.iterator();
                                //gestionamos la lista de requerimientos para el producto
                                while (listaRequisitos.hasNext()) {
                                    //recuperamos cada requisito del iterator
                                    requisito = listaRequisitos.next();
                                    //obtenemos la cantidad de el producto necesario disponible en la abbatia
                                    cantidadRequisito = oElaboracionAD.recuperarCantidadProducto(requisito, monjeElaboracion.getIdAbadia());
                                    //si la cantidad disponible no cubre con las necesidades del producto * cantidad elaborada por día..
                                    if (cantidadRequisito < requisito.getCantidadD() * produccionPendiente) {
                                        //se para la elaboración de ese producto por falta de recursos....
                                        existeRequisito++;
                                        break;
                                    }
                                }
                            }
                            //faltan requisitos....
                            if (existeRequisito > 0) {
                                oProcesosAD.setActividadMens(monjeElaboracion.getIdMonje(), tipo_elaboracion, periodo, msg4, 0);

                                //si la falta de recursos coincide con la finalización de la cantidad total de elaboración...
                                if (producto.getElaborado() >= producto.getCantidad()) {
                                    producto.setEstado(Constantes.ESTADO_ELABORACION_FINALIZADO);
                                } else {
                                    //actualizamos el estado del registro a "detenida por falta de recursos"
                                    producto.setEstado(Constantes.ESTADO_ELABORACION_DETENIDA_R);
                                }
                            } else {
                                //el usuario dispone de los requisitos seleccionados

                                //si el producto elaborado está Reposando, no debemos restar los recursos, de lo contrario...
                                if (producto.getEstado() != Constantes.ESTADO_ELABORACION_REPOSANDO) {
                                    //restamos los recursos necesarios para la elaboración total cantidadUnidadxCantidadaElaborar
                                    listaRequisitos = requisitos.iterator();
                                    //bucle para restar los recursos de los requisitos para la elaboración
                                    while (listaRequisitos.hasNext()) {
                                        requisito = listaRequisitos.next();
                                        oElaboracionAD.restarCantidadProducto(requisito, monjeElaboracion.getIdAbadia(), produccionPendiente);
                                    }
                                    //actualizo la cantidad elaborada
                                    //producto.setElaborado(producto.getElaborado() + producto.getCtd_dia());
                                    //Actualiamos el estado del registro de alaboracion a elaborando
                                    producto.setEstado(Constantes.ESTADO_ELABORACION_ELABORANDO);
                                    // el la fecha de inicio no está informada...
                                    if (producto.getFecha_inicio() == null) {
                                        //marcamo la fecha de inicio con la fecha actual abbatia
                                        producto.setFecha_inicio(CoreTiempo.getTiempoAbadiaString());
                                    }
                                }
                                //Una vez restados los recursos analizamos 2 escenarios
                                //1 - Se trata de un producto de elaboracion inmediata (tipo pan...)
                                //    En ese caso creamos los productos correspondientes a la elaboración diaria por monje
                                double cantidadProduccion = 0;
                                //si es un producto de elaboración inmediata (sin reposo)....
                                if (producto.getDias_total() == 1) {
                                    //si lo elaborado supera o iguala la cantidad total...  y el estado no está finalizado...
                                    if ((producto.getCantidad() <= producto.getElaborado() + produccionPendiente) && (producto.getEstado() != Constantes.ESTADO_ELABORACION_FINALIZADO)) {
                                        //actualizamos el estado de elaboracion a finalizado
                                        producto.setEstado(Constantes.ESTADO_ELABORACION_FINALIZADO);
                                        producto.setFecha_fin(CoreTiempo.getTiempoAbadiaString());
                                        //cantidadProduccion = producto.getCantidad() - producto.getElaborado() + produccionPendiente;
                                        //cantidadProduccion = producto.getCantidad() - producto.getElaborado() + produccionPendiente;
                                    } /*else {
                                        //Actualizamos la producción con el total del día o lo que quede si es menor
                                        cantidadProduccion = produccionPendiente;
                                    }*/
                                    cantidadProduccion = produccionPendiente;
                                    //incrementamos el cotador de producción por monje
                                    contadorProduccion += cantidadProduccion;
                                }
                                //sumo lo elaborado con lo que acabo de elaborar...
                                if (producto.getElaborado() + cantidadProduccion > producto.getCantidad()) {
                                    cantidadProduccion = producto.getCantidad() - producto.getElaborado();
                                }

                                //sumamos a lo elaborado la cantidad producida den esta pasada
                                producto.setElaborado(producto.getElaborado() + cantidadProduccion);
                                //creamos los nuevos lotes elaborados

                                //para las elaboraciones a largo plazo, solo se generan productos al final...
                                producto.setIdAbadia(monjeElaboracion.getIdAbadia());
                                if (cantidadProduccion > 0) {
                                    //generamos mensaje de monje con lo que ha elaborado.
                                    msg1 = ProcesosUtils.Format(msg1, (int) cantidadProduccion);
                                    msg1 = ProcesosUtils.Format(msg1, producto.getDescProducto());

                                    oProcesosAD.setActividadMens(monjeElaboracion.getIdMonje(), tipo_elaboracion, periodo, msg1, (float) cantidadProduccion);

                                    //incrementamos acumulado por actividad/periodo
                                    oActividadAD.incrementaAcumulado(monjeElaboracion.getIdMonje(), periodo, tipo_elaboracion, cantidadProduccion);

                                    crearRecursoElaboracion(producto, cantidadProduccion, con);

                                }
/*                                }*/
                            }
                        } else {
                            //no hay producto para elaborar...
                            oProcesosAD.setActividadMens(monjeElaboracion.getIdMonje(), tipo_elaboracion, periodo, msg3, 0);
                        }
                    }
                    //Actualizamos los datos del registro de elaboración en base de datos
                }
            }

            //para actualizar los datos de la última abbatia procesada...
            if (last_abadiaid != 0) {
                //actualizamos la base de datos con los valores actualizados sobre elaboracion
                oElaboracionAD.actualizarElaboracionAlimentos(listaElaboracion);
            }

            oProcesosAD.addLog("- Finalizando SUBProceso elaborar: " + tipo_elaboracion, 0);

        } catch (Exception e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    /*
    El monje va a rezar en un periodo de tiempo
    Acumular en la actividad la tarea... marcar como realizado
    */

    public void rezar(int periodo) throws AbadiaException {

        String sTrace = this.getClass() + ".rezar(" + periodo + ")";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        String sSQL = "UPDATE actividad a, monje m, abadia ab " +
                " set a.acumulado_dia = 1, a.realizado = 1 " +
                " where m.monjeid = a.monjeid and  m.estado in (" + Constantes.MONJE_VIVO + "," + Constantes.MONJE_VISITA + ") AND a.periodoid = " + periodo +
                " AND a.actividadid = " + Constantes.TAREA_REZAR + " AND m.abadiaid = ab.abadiaid ";

        adUtils utilsAD;
        adProcesos oProcesosAD;
        adLiterales oLiteralesAD;

        ArrayList<MonjeProceso> monjesAl;
        Iterator<MonjeProceso> monjes;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);

            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando SUBProceso rezar", 0);
            monjesAl = oProcesosAD.recuperarMonjesActividadPeriodo(periodo, Constantes.TAREA_REZAR);
            monjes = monjesAl.iterator();

            utilsAD = new adUtils(con);
            utilsAD.execSQL(sSQL);

            int last_idiomaid = 0;
            String msg = "";

            oLiteralesAD = new adLiterales(con);
            MonjeProceso monje;
            while (monjes.hasNext()) {
                monje = monjes.next();
                if (last_idiomaid != monje.getIdIdioma()) {
                    last_idiomaid = monje.getIdIdioma();
                    //utilsAD = new adUtils(Constantes.DB_CONEXION_PROCESS);
                    msg = oLiteralesAD.getLiteralStatic(10018, last_idiomaid);  // Ha rezado
                    //utilsAD.finalize();
                }
                oProcesosAD.setActividadMens(monje.getIdMonje(), Constantes.TAREA_REZAR, periodo, msg, 0);
            }
            oProcesosAD.addLog("- Finalizando SUBProceso rezar ", 0);

        } catch (Exception e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public void abad(int periodo) throws AbadiaException {
        String sTrace = this.getClass() + ".abad(" + periodo + ")";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        String sSQL = "UPDATE actividad a, monje m, abadia ab, usuario u " +
                "set a.acumulado_dia = a.acumulado_dia + 1, a.realizado = 1 " +
                "where a.monjeid = m.monjeid and m.estado = 0 and a.periodoid = " + periodo + " AND a.actividadid = " + Constantes.TAREA_ABAD + " and m.abadiaid = ab.abadiaid and ab.usuarioid = u.usuarioid and u.abadia_congelada = 0";

        adProcesos oProcesosAD;
        adUtils utils;
        adLiterales oLiteralesAD;

        ArrayList<MonjeProceso> monjesAl;
        Iterator<MonjeProceso> monjes;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);

            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando SUBProceso abad", 0);
            monjesAl = oProcesosAD.recuperarMonjesActividadPeriodo(periodo, Constantes.TAREA_ABAD);
            monjes = monjesAl.iterator();

            utils = new adUtils(con);
            utils.execSQL(sSQL);

            oLiteralesAD = new adLiterales(con);
            int last_idiomaid = 0;
            String msg = "";

            MonjeProceso monje;
            while (monjes.hasNext()) {
                monje = monjes.next();
                if (last_idiomaid != monje.getIdIdioma()) {
                    last_idiomaid = monje.getIdIdioma();
                    //utils = new adUtils(Constantes.DB_CONEXION_PROCESS);
                    msg = oLiteralesAD.getLiteralStatic(10017, last_idiomaid);  // Ha realizado su tarea de abad
                    //utils.finalize();
                }

                oProcesosAD.setActividadMens(monje.getIdMonje(), Constantes.TAREA_ABAD, periodo, msg, 0);
            }

            oProcesosAD.addLog("- Finalizando SUBProceso abad", 0);

        } catch (Exception e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }


    public void curar(int periodo) throws AbadiaException {

        String sTrace = this.getClass() + ".curar(" + periodo + ")";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        double fe;
        double destreza;
        double sabiduria;
        double talento;
        int nivel_enfermeria;
        double cura;
        int enfermedad;
        int result_salud;
        int iCount;
        String msg;
        String sFechaFin;

        Monje monjeEnfermo;
        Enfermedad eEnfermedad;

        ArrayList<Mensajes> mensajes = new ArrayList<Mensajes>();
        ArrayList<Monje> alMonjesEnfermos;
        HashMap<Integer, Integer> hmPropiedades;
        Iterator<Monje> itMonjesEnfermos;
        HashMap<Integer, HabilidadMonje> hmHabilidades;
        HashMap<Integer, ArrayList<Monje>> hmMonjesEnfermos;
        HashMap<String, Enfermedad> htEnfermedades;


        adActividad oActividadAD;
        adHabilidades oHabilidadesAD;
        adEdificio oEdificioAD;
        adEnfermedad oEnfermedadAD;
        adProcesos oProcesosAD;
        adLiterales oLiteralesAD;
        adMonje oMonjeAD;
        adMensajes oMensajesAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);

            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando SUBProceso curar", 0);
            ArrayList<MonjeProceso> monjesAl = oProcesosAD.recuperarMonjesActividadPeriodo(periodo, Constantes.TAREA_CURAR);
            Iterator<MonjeProceso> monjes = monjesAl.iterator();

            //obtenemos la hashtable de enfermedades por tipo/nivel
            oEnfermedadAD = new adEnfermedad(con);
            htEnfermedades = oEnfermedadAD.recuperarTablaEnfermedadesPorTipo();

            //obtenemos la lista de monjes enfermos por abbatia
            hmMonjesEnfermos = oProcesosAD.recuperarMonjesEnfermosPorAbadia();

            oEdificioAD = new adEdificio(con);
            oHabilidadesAD = new adHabilidades(con);
            oEnfermedadAD = new adEnfermedad(con);
            oActividadAD = new adActividad(con);
            oLiteralesAD = new adLiterales(con);
            oMonjeAD = new adMonje(con);
            oMensajesAD = new adMensajes(con);

            MonjeProceso monje;
            while (monjes.hasNext()) {
                iCount = 0;
                monje = monjes.next();
                // recuperamos nivel enfermeria
                nivel_enfermeria = oEdificioAD.recuperarNivelEdificio(monje.getIdAbadia(), Constantes.EDIFICIO_ENFERMERIA);

                hmHabilidades = oHabilidadesAD.recuperarHabilidadesMonjeHM(monje.getIdMonje());

                //Las habilidades del monje con tarea "CURAR"
                fe = (hmHabilidades.get(new Integer(Constantes.HABILIDAD_FE))).getValorActual();
                destreza = (hmHabilidades.get(new Integer(Constantes.HABILIDAD_DESTREZA))).getValorActual();
                sabiduria = (hmHabilidades.get(new Integer(Constantes.HABILIDAD_SABIDURIA))).getValorActual();
                talento = (hmHabilidades.get(new Integer(Constantes.HABILIDAD_TALENTO))).getValorActual();

                //calculo de la cura del monje con tarea "CURAR"
                cura = (((sabiduria * 0.5) + (talento * 0.3) + (destreza * 0.2)) * fe) / 1000;

                //Todos los monjes enfermos de la abbatia donde esta el monje con tarea "CURAR"

                alMonjesEnfermos = hmMonjesEnfermos.get(new Integer(monje.getIdAbadia()));

                //si se encuentran monjes enfermos en la abbatia....
                if (alMonjesEnfermos != null) {
                    itMonjesEnfermos = alMonjesEnfermos.iterator();

                    while (itMonjesEnfermos.hasNext() && iCount <= Math.pow(nivel_enfermeria, 2)) {
                        iCount++;
                        monjeEnfermo = itMonjesEnfermos.next();

                        hmPropiedades = new HashMap<Integer, Integer>();
                        hmPropiedades.put(Constantes.PROPIEDAD_HIDRATOS, (int) monjeEnfermo.getHidratosCarbono());
                        hmPropiedades.put(Constantes.PROPIEDAD_LIPIDOS, (int) monjeEnfermo.getLipidos());
                        hmPropiedades.put(Constantes.PROPIEDAD_PROTEINAS, (int) monjeEnfermo.getProteinas());
                        hmPropiedades.put(Constantes.PROPIEDAD_VITAMINAS, (int) monjeEnfermo.getVitaminas());

                        enfermedad = Utilidades.Random(1, 8);
                        result_salud = (int) (cura - enfermedad);

                        //restamos de la fecha fin de la enfermedad los dias corresondientes a la curacion
                        sFechaFin = CoreTiempo.sumarDias(monjeEnfermo.getEnfermedad().getFechaFin(), -enfermedad);
                        monjeEnfermo.getEnfermedad().setFechaFin(sFechaFin);

                        //actualizamos la fecha fin de la enfermedad.
                        oEnfermedadAD.actualizarDatosEnfermedad(monjeEnfermo, monjeEnfermo.getEnfermedad());

                        //incrementar el acumulado de la curacion
                        oActividadAD.incrementaAcumulado(monje.getIdMonje(), periodo, Constantes.TAREA_CURAR, 1);

                        //recuperamos la enfermedad que afecta al monje de la hash de tipos....
                        eEnfermedad = htEnfermedades.get(String.valueOf(monjeEnfermo.getEnfermedad().getTipoEnfermedad()).concat(String.valueOf(monjeEnfermo.getEnfermedad().getNivel())));
                        //ha curado....
                        if (result_salud > 0) // para que no este enfermo
                        {
                            monjeEnfermo.setVitaminas((short) 30);
                            monjeEnfermo.setProteinas((short) 30);
                            monjeEnfermo.setHidratosCarbono((short) 30);
                            monjeEnfermo.setLipidos((short) 30);

                            //crear mensaje indicando que ha mejorado...
                            //oUtilsAD = new adUtils(Constantes.DB_CONEXION_PROCESS);
                            msg = oLiteralesAD.getLiteralStatic(eEnfermedad.getLiteralidMejora(), monjeEnfermo.getIdioma());  // Ha sanado %s de vida
                            //oUtilsAD.finalize();

                            mensajes.add(new Mensajes(monjeEnfermo.getIdAbadia(), monjeEnfermo.getIdMonje(), msg, 0));

                        } else //no cura
                        {

                            //si la propiedad que causa la enfermedad está en 30...
                            if (hmPropiedades.get(new Integer(eEnfermedad.getPropiedadCausa())) == 30) {
                                //si la enfermedad es por defecto....
                                if (eEnfermedad.getFaltaExceso() == Constantes.ENFERMEDAD_DEFECTO) {
                                    //debemos restar 11 a la propiedad implicada.
                                    if (eEnfermedad.getPropiedadCausa() == Constantes.PROPIEDAD_HIDRATOS) {
                                        monjeEnfermo.setHidratosCarbono((short) (monjeEnfermo.getHidratosCarbono() - 11));
                                    } else if (eEnfermedad.getPropiedadCausa() == Constantes.PROPIEDAD_LIPIDOS) {
                                        monjeEnfermo.setLipidos((short) (monjeEnfermo.getLipidos() - 11));
                                    } else if (eEnfermedad.getPropiedadCausa() == Constantes.PROPIEDAD_PROTEINAS) {
                                        monjeEnfermo.setProteinas((short) (monjeEnfermo.getProteinas() - 11));
                                    } else if (eEnfermedad.getPropiedadCausa() == Constantes.PROPIEDAD_VITAMINAS) {
                                        monjeEnfermo.setVitaminas((short) (monjeEnfermo.getVitaminas() - 11));
                                    }
                                } else //si la enfermedad es por exceso sumamos 21
                                {
                                    //debemos restar 11 a la propiedad implicada.
                                    if (eEnfermedad.getPropiedadCausa() == Constantes.PROPIEDAD_HIDRATOS) {
                                        monjeEnfermo.setHidratosCarbono((short) (monjeEnfermo.getHidratosCarbono() + 21));
                                    } else if (eEnfermedad.getPropiedadCausa() == Constantes.PROPIEDAD_LIPIDOS) {
                                        monjeEnfermo.setLipidos((short) (monjeEnfermo.getLipidos() + 21));
                                    } else if (eEnfermedad.getPropiedadCausa() == Constantes.PROPIEDAD_PROTEINAS) {
                                        monjeEnfermo.setProteinas((short) (monjeEnfermo.getProteinas() + 21));
                                    } else if (eEnfermedad.getPropiedadCausa() == Constantes.PROPIEDAD_VITAMINAS) {
                                        monjeEnfermo.setVitaminas((short) (monjeEnfermo.getVitaminas() + 21));
                                    }
                                }

                            } else //si no es la primera vez sólo sumaremos uno restaremos 1
                            {
                                //si la enfermedad es por defecto....
                                if (eEnfermedad.getFaltaExceso() == Constantes.ENFERMEDAD_DEFECTO) {
                                    //debemos restar 1 a la propiedad implicada.
                                    if (eEnfermedad.getPropiedadCausa() == Constantes.PROPIEDAD_HIDRATOS) {
                                        monjeEnfermo.setHidratosCarbono((short) (monjeEnfermo.getHidratosCarbono() - 1));
                                    } else if (eEnfermedad.getPropiedadCausa() == Constantes.PROPIEDAD_LIPIDOS) {
                                        monjeEnfermo.setLipidos((short) (monjeEnfermo.getLipidos() - 1));
                                    } else if (eEnfermedad.getPropiedadCausa() == Constantes.PROPIEDAD_PROTEINAS) {
                                        monjeEnfermo.setProteinas((short) (monjeEnfermo.getProteinas() - 1));
                                    } else if (eEnfermedad.getPropiedadCausa() == Constantes.PROPIEDAD_VITAMINAS) {
                                        monjeEnfermo.setVitaminas((short) (monjeEnfermo.getVitaminas() - 1));
                                    }
                                } else //si la enfermedad es por exceso sumamos 1
                                {
                                    //debemos restar 11 a la propiedad implicada.
                                    if (eEnfermedad.getPropiedadCausa() == Constantes.PROPIEDAD_HIDRATOS) {
                                        monjeEnfermo.setHidratosCarbono((short) (monjeEnfermo.getHidratosCarbono() + 1));
                                    } else if (eEnfermedad.getPropiedadCausa() == Constantes.PROPIEDAD_LIPIDOS) {
                                        monjeEnfermo.setLipidos((short) (monjeEnfermo.getLipidos() + 1));
                                    } else if (eEnfermedad.getPropiedadCausa() == Constantes.PROPIEDAD_PROTEINAS) {
                                        monjeEnfermo.setProteinas((short) (monjeEnfermo.getProteinas() + 1));
                                    } else if (eEnfermedad.getPropiedadCausa() == Constantes.PROPIEDAD_VITAMINAS) {
                                        monjeEnfermo.setVitaminas((short) (monjeEnfermo.getVitaminas() + 1));
                                    }
                                }
                            }

                            //crear mensaje indicando que ha empeorado...
                            //oUtilsAD = new adUtils(Constantes.DB_CONEXION_PROCESS);
                            msg = oLiteralesAD.getLiteralStatic(eEnfermedad.getLiteralidEmpeora(), monjeEnfermo.getIdioma());
                            //oUtilsAD.finalize();

                            mensajes.add(new Mensajes(monjeEnfermo.getIdAbadia(), monjeEnfermo.getIdMonje(), msg, 0));

                            //en función de la enfermedad que tiene y de la propiedad que afecta la enfermedad
                            //si la propiedad está a 30, le sumamos 21 o le restamos 11 en funcion de si la enfermedad es por
                            //Exceso o por defecto, si no está a 30 le sumamos o le restamos 1

                        }
                        //actualizamos los parametros de propiedades actualizados...
                        oMonjeAD.actualizarPropiedades(monjeEnfermo);

                        oProcesosAD.setActividadMens(monje.getIdMonje(), Constantes.TAREA_CURAR, periodo, msg, 0);
                    }
                } else {
                    //no hay monjes para curar
                    //crear mensaje indicando que ha empeorado...
                    msg = oLiteralesAD.getLiteralStatic(10500, monje.getIdIdioma()); //no hay monjes para curar
                    oProcesosAD.setActividadMens(monje.getIdMonje(), Constantes.TAREA_CURAR, periodo, msg, 0);
                }
            }
            oMensajesAD.crearMensajes(mensajes);

            oProcesosAD.addLog("- Finalizando SUBProceso curar", 0);

        } catch (Exception e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    /*
    Talar los arboles en un periodo
    */

    public void talar_arboles(int periodo) throws AbadiaException {

        String sTrace = this.getClass() + ".talar_arboles(" + periodo + ")";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adHabilidades oHabilidadesAD;
        adActividad oActividadAD;
        adRecurso oRecursoAD;
        adProcesos oProcesosAD;
        adLiterales oLiteralesAD;

        int fe, fuerza, ctd, last_idiomaid = 0;
        float valor;
        String msg, msg1 = "", msg2 = "";
        ArrayList<MonjeProceso> monjesAl;
        Iterator<MonjeProceso> monjes;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);
            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando SUBProceso talar_arboles", 0);
            monjesAl = oProcesosAD.recuperarMonjesActividadPeriodo(periodo, Constantes.TAREA_TALAR);
            monjes = monjesAl.iterator();

            oLiteralesAD = new adLiterales(con);
            oHabilidadesAD = new adHabilidades(con);
            oRecursoAD = new adRecurso(con);
            oActividadAD = new adActividad(con);

            MonjeProceso monje;
            while (monjes.hasNext()) {
                monje = monjes.next();
                if (last_idiomaid != monje.getIdIdioma()) {
                    last_idiomaid = monje.getIdIdioma();
                    //oUtilsAD = new adUtils(Constantes.DB_CONEXION_PROCESS);
                    msg1 = oLiteralesAD.getLiteralStatic(10000, last_idiomaid);  // Ha recogido %d unidades de madera
                    msg2 = oLiteralesAD.getLiteralStatic(10013, last_idiomaid);  // No has recogido nada
                    //oUtilsAD.finalize();
                }


                fe = (int) oHabilidadesAD.recuperarHabilidad(monje.getIdMonje(), Constantes.HABILIDAD_FE, 25);
                fuerza = (int) oHabilidadesAD.recuperarHabilidad(monje.getIdMonje(), Constantes.HABILIDAD_FUERZA, 25);

                valor = (fuerza * fe) / 100;
                if (valor != 0)
                    ctd = Math.round((10 * valor) / 100);
                else ctd = 0;
                if (ctd != 0) {
                    msg = ProcesosUtils.Format(msg1, ctd);
                    oRecursoAD.sumarRecurso(Constantes.RECURSOS_MADERA, monje.getIdAbadia(), ctd);
                    oActividadAD.incrementaAcumulado(monje.getIdMonje(), periodo, Constantes.TAREA_TALAR, ctd);
                } else {
                    msg = msg2;
                }
                oProcesosAD.setActividadMens(monje.getIdMonje(), Constantes.TAREA_TALAR, periodo, msg, ctd);

            }
            oProcesosAD.addLog("- Finalizando SUBProceso talar_arboles", 0);
        } catch (Exception e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public void teach(int periodo) throws AbadiaException {
        String sTrace = this.getClass() + ".teach(" + periodo + ")";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        double fe;
        double destreza;
        double sabiduria;
        double talento;
        double fuerza;
        double idioma;
        int nivel_escuela;
        int fe_alumno;
        int abadia_ant;

        double valor;
        int last_idiomaid;
        int last_idiomaid2;

        String msg;
        String msg2;
        String nom_profe;
        String nom_alumno;

        ArrayList mensajes = new ArrayList();

        adHabilidades oHabilidadesAD;
        adEdificio oEdificioAD;
        adMonje oMonjeAD;
        adProcesos oProcesosAD;
        adActividad oActividadAD;
        adLiterales oLiteralesAD;
        adMensajes oMensajesAD;

        HashMap<Integer, HabilidadMonje> hmHabilidades;
        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);

            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando SUBProceso teach", 0);
            ArrayList<MonjeProceso> monjesAl = oProcesosAD.recuperarMonjesActividadPeriodo(periodo, Constantes.TAREA_ENSENAR);

            oEdificioAD = new adEdificio(con);
            oHabilidadesAD = new adHabilidades(con);
            oActividadAD = new adActividad(con);
            oMonjeAD = new adMonje(con);
            oLiteralesAD = new adLiterales(con);
            oMensajesAD = new adMensajes(con);

            abadia_ant = 0;

            for (MonjeProceso monje : monjesAl) {
                if (abadia_ant != monje.getIdAbadia()) {

                    nivel_escuela = oEdificioAD.recuperarNivelEdificio(monje.getIdAbadia(), Constantes.EDIFICIO_ESCUELA);

                    //Las habilidades del monje con tarea "ENSEÑAR"
                    hmHabilidades = oHabilidadesAD.recuperarHabilidadesMonjeHM(monje.getIdMonje());

                    fe = (hmHabilidades.get(new Integer(Constantes.HABILIDAD_FE))).getValorActual();
                    destreza = (hmHabilidades.get(new Integer(Constantes.HABILIDAD_DESTREZA))).getValorActual();
                    sabiduria = (hmHabilidades.get(new Integer(Constantes.HABILIDAD_SABIDURIA))).getValorActual();
                    talento = (hmHabilidades.get(new Integer(Constantes.HABILIDAD_TALENTO))).getValorActual();
                    fuerza = (hmHabilidades.get(new Integer(Constantes.HABILIDAD_FUERZA))).getValorActual();
                    idioma = (hmHabilidades.get(new Integer(Constantes.HABILIDAD_IDIOMA))).getValorActual();

                    destreza = (destreza * fe) / 100;
                    sabiduria = (sabiduria * fe) / 100;
                    talento = (talento * fe) / 100;
                    fuerza = (fuerza * fe) / 100;
                    idioma = (idioma * fe) / 100;
                    //Todos los monjes y los enfermos de la abbatia donde esta el monje con tarea "APRENDER" en el mismo periodo.
                    ArrayList<MonjeEnfermo> alMonjesAprendiendo = oProcesosAD.recuperarMonjesAprendiendo(monje.getIdAbadia(), periodo, nivel_escuela);

                    //MonjeEnfermo monjeProceso;
                    for (MonjeEnfermo monjeProceso : alMonjesAprendiendo) {
                        oActividadAD.incrementaAcumulado(monje.getIdMonje(), periodo, Constantes.TAREA_ENSENAR, 1);

                        fe_alumno = (int) oHabilidadesAD.recuperarHabilidad(monjeProceso.getIdMonje(), Constantes.HABILIDAD_FE, 0);
                        valor = 0;
                        if (destreza >= 50) {
                            valor = destreza / 1000;
                            valor = valor * fe_alumno;
                            valor = valor / 1000;
                            oHabilidadesAD.incrementarHabilidad(monjeProceso.getIdMonje(), Constantes.HABILIDAD_DESTREZA, valor);
                        }
                        if (sabiduria >= 50) {
                            valor = sabiduria / 1000;
                            valor = valor * fe_alumno;
                            valor = valor / 1000;

                            oHabilidadesAD.incrementarHabilidad(monjeProceso.getIdMonje(), Constantes.HABILIDAD_SABIDURIA, valor);
                        }
                        if (talento >= 50) {
                            valor = talento / 1000;
                            valor = valor * fe_alumno;
                            valor = valor / 1000;

                            oHabilidadesAD.incrementarHabilidad(monjeProceso.getIdMonje(), Constantes.HABILIDAD_TALENTO, valor);
                        }
                        if (fuerza >= 50) {
                            valor = fuerza / 1000;
                            valor = valor * fe_alumno;
                            valor = valor / 1000;

                            oHabilidadesAD.incrementarHabilidad(monjeProceso.getIdMonje(), Constantes.HABILIDAD_FUERZA, valor);
                        }
                        if (idioma >= 50) {
                            valor = idioma / 1000;
                            valor = valor * fe_alumno;
                            valor = valor / 1000;

                            oHabilidadesAD.incrementarHabilidad(monjeProceso.getIdMonje(), Constantes.HABILIDAD_IDIOMA, valor);
                        }

                        if (valor > 0) {
                            //msg = el nombre del profesor.
                            //msg2 = el nombre del alumno.

                            nom_profe = oMonjeAD.getNomMonje(monje.getIdMonje(), "de");
                            nom_alumno = oMonjeAD.getNomMonje(monjeProceso.getIdMonje(), "de");

                            last_idiomaid = monje.getIdIdioma();

                            //oUtilsAD = new adUtils(Constantes.DB_CONEXION_PROCESS);
                            msg = oLiteralesAD.getLiteralStatic(10130, last_idiomaid);  // Ha enseñado a %s
                            //oUtilsAD.finalize();

                            last_idiomaid2 = monjeProceso.getIdIdioma();
                            //oUtilsAD = new adUtils(Constantes.DB_CONEXION_PROCESS);
                            msg2 = oLiteralesAD.getLiteralStatic(10131, last_idiomaid2);  // Ha aprendido de %s
                            //oUtilsAD.finalize();


                            msg = ProcesosUtils.Format(msg, nom_alumno);
                            oProcesosAD.setActividadMens(monje.getIdMonje(), Constantes.TAREA_ENSENAR, periodo, msg, valor);

                            msg2 = ProcesosUtils.Format(msg2, nom_profe);
                            oProcesosAD.setActividadMens(monjeProceso.getIdMonje(), Constantes.TAREA_ENSENAR, periodo, msg2, 0);

                            //mensajes.add(new Mensajes(monjeProceso.getIdAbadia(), monjeProceso.getIdMonje(), msg2, 0));

                        } else {
                            last_idiomaid = monje.getIdIdioma();
                            //oUtilsAD = new adUtils(Constantes.DB_CONEXION_PROCESS);
                            msg = oLiteralesAD.getLiteralStatic(10132, last_idiomaid);
                            //oUtilsAD.finalize();
                            oProcesosAD.setActividadMens(monje.getIdMonje(), Constantes.TAREA_ENSENAR, periodo, msg, -1);
                        }

                    }
                } else {
                    last_idiomaid = monje.getIdIdioma();
                    //oUtilsAD = new adUtils(Constantes.DB_CONEXION_PROCESS);
                    msg = oLiteralesAD.getLiteralStatic(10140, last_idiomaid);
                    //oUtilsAD.finalize();

                    oProcesosAD.setActividadMens(monje.getIdMonje(), Constantes.TAREA_ENSENAR, periodo, msg, 0);
                }
                abadia_ant = monje.getIdAbadia();
            }

            oMensajesAD.crearMensajes(mensajes);

            oProcesosAD.addLog("- Finalizando SUBProceso teach", 0);

        } catch (Exception e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    /*
    ganaderia en un periodo
    */

    public void ganaderiaAlimentos(int periodo, ArrayList alMonjes) throws AbadiaException {

        String sTrace = this.getClass() + ".ganaderiaAlimentos(" + periodo + ")";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adUtils oUtilsAD;
        adHabilidades oHabilidadesAD;
        adActividad oActividadAD;
        adProcesos oProcesosAD;
        adLiterales oLiteralesAD;
        adAlimentoLotes oAlimentoAD;
        //primero, cargamos una hashtable con la producción por abadías

        Hashtable<Object, ProduccionPorAbadia> alProduccion = null;
        Enumeration<ProduccionPorAbadia> itProduccion;
        ProduccionPorAbadia produccion;

        AlimentoLote alimentoLote;
        Iterator monjes;
        Hashtable<Integer, Hashtable<Object, ProduccionPorAbadia>> htProduccion;

        int fe;
        int destreza;
        double ctd_Alimento;
        double ctd_Monje;
        int ctd_Total_Monje;

        int loteid, last_idiomaid = 0, last_abadia = 0;

        float valor;
        String msg, msg1 = "", msg2 = "", unidades, alimento;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);

            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando SUBProceso ganaderia-Alimentos", 0);
            htProduccion = oProcesosAD.cargarProduccionGanaderiaPorAbadiaAlimentos();
            monjes = alMonjes.iterator();

            oLiteralesAD = new adLiterales(con);
            oHabilidadesAD = new adHabilidades(con);
            oUtilsAD = new adUtils(con);
            oAlimentoAD = new adAlimentoLotes(con);
            oActividadAD = new adActividad(con);

            MonjeProceso monje;
            while (monjes.hasNext()) {
                monje = (MonjeProceso) monjes.next();
                //Inicializar contador por monje
                ctd_Total_Monje = 0;

                if (last_idiomaid != monje.getIdIdioma()) {
                    last_idiomaid = monje.getIdIdioma();
                    //oUtilsAD = new adUtils(Constantes.DB_CONEXION_PROCESS);
                    msg1 = oLiteralesAD.getLiteralStatic(10006, last_idiomaid);  // Ha recogido %d %s de %s
                    msg2 = oLiteralesAD.getLiteralStatic(10013, last_idiomaid);  // No has recogido nada
                    //oUtilsAD.finalize();
                }
                if (last_abadia != monje.getIdAbadia()) {
                    //cambio de abbatia
                    //generamos los alimentos obtenidos...
                    if (last_abadia != 0 && alProduccion != null) {
                        //le pasamos el arrayList correspondiente a la abbatia.
                        oProcesosAD.actualizarProduccionPorAbadiaAlimentos(alProduccion);
                    }
                    last_abadia = monje.getIdAbadia();
                    //recupero de la hash la lista de produccion de la granja
                    alProduccion = htProduccion.get(new Integer(monje.getIdAbadia()));
                }
                if (alProduccion != null) {
                    //inicializamos el iterator....
                    itProduccion = alProduccion.elements();
                    // Formula


                    fe = (int) oHabilidadesAD.recuperarHabilidad(monje.getIdMonje(), Constantes.HABILIDAD_FE, 25);
                    destreza = (int) oHabilidadesAD.recuperarHabilidad(monje.getIdMonje(), Constantes.HABILIDAD_DESTREZA, 25);

                    //while(itProduccion.hasMoreElements() && ctd_Total_Monje <= ctd_Max_Monje)
                    while (itProduccion.hasMoreElements()) {
                        produccion = itProduccion.nextElement();
                        unidades = produccion.getUnidad_medida();
                        alimento = produccion.getDescripcion();
                        ctd_Alimento = produccion.getCantidad();
                        ctd_Monje = produccion.getRecoge_monje();
                        if (ctd_Alimento > 0) {
                            valor = (destreza * fe) / 100;
                            // poner el valor a 1 cuando todo esta a 0 si estamos muy a fe
                            if (valor > 0)
                                ctd_Monje = Math.round((ctd_Monje * valor) / 100);
                            else ctd_Monje = 0;

                            if ((ctd_Monje == 0) && (fe > 80)) ctd_Monje = 1;

                            //si el monje tiene que recoger mas de lo que queda
                            //ajustamos lo que tiene que recoger el monje a lo que queda..
                            if (ctd_Monje > ctd_Alimento) {
                                ctd_Monje = ctd_Alimento;
                            }
                            ctd_Alimento = ctd_Alimento - ctd_Monje;    // Lo que ha recogido el monje
                            //ctd2 = ctd2 - ctd1;  // Lo que ha recogido del animal
                            if (ctd_Alimento < 0) {
                                ctd_Alimento = 0;
                            }


                            if (ctd_Monje > 0) {
                                //Actualizo la cantidad con el actual despues de recoger..
                                produccion.setCantidad(ctd_Alimento);
                                ctd_Total_Monje += ctd_Monje;

                                msg = ProcesosUtils.Format(msg1, ctd_Monje);
                                msg = ProcesosUtils.Format(msg, unidades.toLowerCase());
                                msg = ProcesosUtils.Format(msg, alimento.toLowerCase());

                                loteid = oUtilsAD.getSQL("SELECT a.loteid FROM alimentos a, alimentos_lote al where a.loteid=al.loteid and a.edificioid=" + produccion.getIdEdificio() + " and alimentoid = " + produccion.getIdAlimento() + " and fecha_entrada ='" + CoreTiempo.getTiempoAbadiaString() + "'  LIMIT 1", -1);
                                if (loteid == -1) {
                                    alimentoLote = new AlimentoLote();
                                    alimentoLote.setCantidad(ctd_Monje);
                                    alimentoLote.setIdAlimento(produccion.getIdAlimento());
                                    alimentoLote.setIdEdificio(produccion.getIdEdificio());
                                    alimentoLote.setFechaEntrada(CoreTiempo.getTiempoAbadiaString());
                                    alimentoLote.setEstado(100);
                                    alimentoLote.setFechaCaducidad(CoreTiempo.getDiferenciaString(oAlimentoAD.recuperarCaducidad(produccion.getIdAlimento())));
                                    oAlimentoAD.crearAlimentoLote(alimentoLote);
                                } else {
                                    oAlimentoAD.sumarEnLote(loteid, ctd_Monje);
                                }

                                // Acumular
                                oActividadAD.incrementaAcumulado(monje.getIdMonje(), periodo, Constantes.TAREA_GANADERIA, ctd_Monje);
                                //generar mensaje
                                oProcesosAD.setActividadMens(monje.getIdMonje(), monje.getIdActividad(), periodo, msg, ctd_Monje);
                            }
                        }
                    }
                }
                // No se ha recogido nada!!!
                if (ctd_Total_Monje == 0) {
                    oProcesosAD.setActividadMens(monje.getIdMonje(), monje.getIdActividad(), periodo, msg2, 0);
                }
            }
            oProcesosAD.addLog("- Finalizando SUBProceso ganaderia-Alimentos", 0);

        } catch (Exception e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    /*
    ganaderia en un periodo
    */

    public void ganaderiaRecursos(int periodo, ArrayList alMonjes) throws AbadiaException {

        String sTrace = this.getClass() + ".ganaderiaRecursos(" + periodo + ")";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adHabilidades oHabilidadesAD;
        adActividad oActividadAD;
        adProcesos oProcesosAD;
        adLiterales oLiteralesAD;
        adRecurso oRecursoAD;
        //primero, cargamos una hashtable con la producción por abadías

        Hashtable<Object, ProduccionPorAbadia> alProduccion = null;
        Enumeration<ProduccionPorAbadia> itProduccion;
        ProduccionPorAbadia produccion;

        Iterator monjes;
        Hashtable<Integer, Hashtable<Object, ProduccionPorAbadia>> htProduccion;

        int fe;
        int destreza;
        double ctd_Recurso;
        double ctd_Monje;
        int ctd_Total_Monje;

        int last_idiomaid = 0, last_abadia = 0;

        float valor;
        String msg, msg1 = "", msg2 = "", unidades, recurso;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);

            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando SUBProceso ganaderia-Recursos", 0);
            htProduccion = oProcesosAD.cargarProduccionGanaderiaPorAbadiaRecursos();
            monjes = alMonjes.iterator();

            oLiteralesAD = new adLiterales(con);
            oHabilidadesAD = new adHabilidades(con);
            oRecursoAD = new adRecurso(con);
            oActividadAD = new adActividad(con);

            MonjeProceso monje;
            while (monjes.hasNext()) {
                monje = (MonjeProceso) monjes.next();
                //Inicializar contador por monje
                ctd_Total_Monje = 0;

                if (last_idiomaid != monje.getIdIdioma()) {
                    last_idiomaid = monje.getIdIdioma();
                    //oUtilsAD = new adUtils(Constantes.DB_CONEXION_PROCESS);
                    msg1 = oLiteralesAD.getLiteralStatic(10006, last_idiomaid);  // Ha recogido %d %s de %s
                    msg2 = oLiteralesAD.getLiteralStatic(10013, last_idiomaid);  // No has recogido nada
                    //oUtilsAD.finalize();
                }
                if (last_abadia != monje.getIdAbadia()) {
                    //cambio de abbatia
                    //generamos los alimentos obtenidos...
                    if (last_abadia != 0 && alProduccion != null) {
                        //le pasamos el arrayList correspondiente a la abbatia.
                        oProcesosAD.actualizarProduccionPorAbadiaRecursos(alProduccion);
                    }
                    last_abadia = monje.getIdAbadia();
                    //recupero de la hash la lista de produccion de la granja
                    alProduccion = htProduccion.get(new Integer(monje.getIdAbadia()));
                }
                if (alProduccion != null) {
                    //inicializamos el iterator....
                    itProduccion = alProduccion.elements();
                    // Formula


                    fe = (int) oHabilidadesAD.recuperarHabilidad(monje.getIdMonje(), Constantes.HABILIDAD_FE, 25);
                    destreza = (int) oHabilidadesAD.recuperarHabilidad(monje.getIdMonje(), Constantes.HABILIDAD_DESTREZA, 25);

                    //while(itProduccion.hasMoreElements() && ctd_Total_Monje <= ctd_Max_Monje)
                    while (itProduccion.hasMoreElements()) {
                        produccion = itProduccion.nextElement();
                        unidades = produccion.getUnidad_medida();
                        recurso = produccion.getDescripcion();
                        ctd_Recurso = produccion.getCantidad();
                        ctd_Monje = produccion.getRecoge_monje();
                        if (ctd_Recurso > 0) {
                            valor = (destreza * fe) / 100;
                            // poner el valor a 1 cuando todo esta a 0 si estamos muy a fe
                            if (valor > 0)
                                ctd_Monje = Math.round((ctd_Monje * valor) / 100);
                            else ctd_Monje = 0;

                            if ((ctd_Monje == 0) && (fe > 80)) ctd_Monje = 1;

                            //si el monje tiene que recoger mas de lo que queda
                            //ajustamos lo que tiene que recoger el monje a lo que queda..
                            if (ctd_Monje > ctd_Recurso) {
                                ctd_Monje = ctd_Recurso;
                            }
                            ctd_Recurso = ctd_Recurso - ctd_Monje;    // Lo que ha recogido el monje
                            //ctd2 = ctd2 - ctd1;  // Lo que ha recogido del animal
                            if (ctd_Recurso < 0) {
                                ctd_Recurso = 0;
                            }


                            if (ctd_Monje > 0) {
                                //Actualizo la cantidad con el actual despues de recoger..
                                produccion.setCantidad(ctd_Recurso);
                                ctd_Total_Monje += ctd_Monje;

                                msg = ProcesosUtils.Format(msg1, ctd_Monje);
                                msg = ProcesosUtils.Format(msg, unidades.toLowerCase());
                                msg = ProcesosUtils.Format(msg, recurso.toLowerCase());

                                // sumar el recurso obtenido.
                                oRecursoAD.sumarRecurso(produccion.getIdRecurso(), produccion.getIdAbadia(), ctd_Monje);

                                // Acumular
                                oActividadAD.incrementaAcumulado(monje.getIdMonje(), periodo, Constantes.TAREA_GANADERIA, ctd_Monje);

                                //generar mensaje
                                oProcesosAD.setActividadMens(monje.getIdMonje(), monje.getIdActividad(), periodo, msg, ctd_Monje);
                            }
                        }
                    }
                }
                // No se ha recogido nada!!!
                if (ctd_Total_Monje == 0) {
                    oProcesosAD.setActividadMens(monje.getIdMonje(), monje.getIdActividad(), periodo, msg2, 0);
                }
            }
            oProcesosAD.addLog("- Finalizando SUBProceso ganaderia-Recursos", 0);

        } catch (Exception e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public void gestionAgricultura(int periodo) throws AbadiaException {
        String sTrace = this.getClass() + ".gestionAgricultura(" + periodo + ")";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adProcesos oProcesosAD;
        adCampo oCampoAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);
            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando SUBProceso agricultura", 0);

            HashMap<Integer, ArrayList<MonjeAgricultura>> hmMonjes;
            oCampoAD = new adCampo(con);
            oCampoAD.transitarEstadosCultivo();
            hmMonjes = oCampoAD.cargarTablaMonjesPorCultivo(periodo);

            oCampoAD.transitarEstadosRecogida();

            oCampoAD.arar(periodo, hmMonjes);
            oCampoAD.cultivar(periodo, hmMonjes);
            oCampoAD.recoger(periodo, hmMonjes);

            oProcesosAD.addLog("- Finalizando SUBProceso agricultura", 0);
        } catch (AbadiaException e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public void caducarVentas() throws AbadiaException {
        String sTrace = this.getClass() + ".caducarVentas()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        double comision;
        ArrayList<Mensajes> mensajes = new ArrayList<Mensajes>();
        int last_idiomaid = 0;
        String msg = "";

        adProcesos oProcesosAD;
        adLiterales oLiteralesAD;
        adMensajes oMensajesAD;

        ArrayList alProductos;
        Iterator itProductos;
        ProductoIdioma oProducto;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS, Constantes.AUTOCOMIT_OF);
            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando Proceso Caduca Ventas", 0);
            alProductos = oProcesosAD.recuperarVentasParaCaducar();
            oLiteralesAD = new adLiterales(con);
            oMensajesAD = new adMensajes(con);

            itProductos = alProductos.iterator();
            while (itProductos.hasNext()) {
                oProducto = (ProductoIdioma) itProductos.next();

                comision = cancelarVentaProceso(oProducto.getProductoId(), con);

                if (last_idiomaid != oProducto.getIdiomaId()) {
                    last_idiomaid = oProducto.getIdiomaId();
                    //oUtilsAD = new adUtils(Constantes.DB_CONEXION_PROCESS);
                    msg = oLiteralesAD.getLiteralStatic(10115, last_idiomaid);  // Ha pescado %d unidades de pescado
                    //oUtilsAD.finalize();
                }
                //generamos mensajes para las comisiones del mercado..
                if (comision > 0) {
                    mensajes.add(new Mensajes(oProducto.getAbadiaId(), -1, Utilidades.Format(msg, Utilidades.redondear(comision)), 0));
                }
            }
            oMensajesAD.crearMensajes(mensajes);
            oProcesosAD.addLog("+ Finalizado Proceso Caduca Ventas", 0);
            ConnectionFactory.commitTransaction(con);
        } catch (SystemException e) {
            ConnectionFactory.rollbackTransaction(con);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public double cancelarVentaProceso(int idProducto, Connection p_cConnection) throws AbadiaException {
        String sTrace = this.getClass() + ".cancelarVentaProceso(" + idProducto + ")";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adMercado oMercadoAD;
        adAlimentoLotes oAlimentosLotesAD;
        adAbadia oAbadiaAD;
        adEdificio oEdificioAD;
        adComisiones oComisionesAD;
        adAnimal oAnimalAD;
        adRecurso oRecursoAD;

        Abadia abadia;
        double comision = 0;

        Mercado mercancia;

        try {
            oMercadoAD = new adMercado(p_cConnection);
            oAbadiaAD = new adAbadia(p_cConnection);
            oComisionesAD = new adComisiones(p_cConnection);
            oRecursoAD = new adRecurso(p_cConnection);
            oEdificioAD = new adEdificio(p_cConnection);
            oAlimentosLotesAD = new adAlimentoLotes(p_cConnection);
            oAnimalAD = new adAnimal(p_cConnection);

            mercancia = oMercadoAD.recuperarMercado(idProducto);
            //si la mercancía no tiene asociado registro de venta de ningun tipo...
            if (mercancia == null) {
                oMercadoAD.eliminarMercado(idProducto);
                return 0;
            }

            //gestionamos el pago de comisiones como parte de la retirada del mercado
            abadia = oAbadiaAD.recuperarAbadia(mercancia.getIdAbadia());

            if (abadia != null) {
                //calculamos las comisiones
                String diferencia = CoreTiempo.getDiferenciaDias(mercancia.getFecha_inicial(), mercancia.getFecha_final());
                String comisionTipo;
                if (diferencia.equals("5")) {
                    comisionTipo = Constantes.COMISION_TIPO_VENTA5;
                } else if (diferencia.equals("10")) {
                    comisionTipo = Constantes.COMISION_TIPO_VENTA10;
                } else if (diferencia.equals("15")) {
                    comisionTipo = Constantes.COMISION_TIPO_VENTA15;
                } else comisionTipo = Constantes.COMISION_TIPO_VENTA5;

                //si la abbatia tiene menos de un mes de antiguedad, no paga comisiones
                int libre = CoreTiempo.getDiferenciaDiasInt(abadia.getFechaDeConstruccion(), CoreTiempo.getTiempoAbadiaString());
                if (libre > 30) {

                    comision = oComisionesAD.getComision(mercancia.getIdAbadia(), comisionTipo);
                }
                double valorComision;
                if (comision > 0) {
                    valorComision = ((mercancia.getCtd_actual() * mercancia.getPrecio_actual()) * comision) / 100;
                    oComisionesAD.sumarImporteComision(mercancia.getIdAbadia(), CoreTiempo.getTiempoAbadiaString(), valorComision);
                    //restamos la pasta a la abbatia que tenía el producto a la venta...
                    oRecursoAD.restarRecurso(Constantes.RECURSOS_ORO, abadia.getIdDeAbadia(), valorComision);
                }
            }

            // Alimentos
            if (mercancia.getMercancia().equals(Constantes.MERCANCIA_ALIMENTOS_STR)) {
                AlimentoLote alimento = new AlimentoLote();
                alimento.setIdEdificio(oEdificioAD.recuperarIdEdificioPorFamiliaAlimento(mercancia.getIdAlimento(), mercancia.getIdAbadia()));
                oMercadoAD.restarMercado(mercancia, mercancia.getCtd_actual());

                alimento.setFechaCaducidad(mercancia.getFecha_caduca());
                alimento.setFechaEntrada(mercancia.getFecha_inicial());
                alimento.setEstado(mercancia.getEstado());
                alimento.setCantidad(mercancia.getCtd_actual());
                alimento.setIdAlimento(mercancia.getIdAlimento());

                oAlimentosLotesAD.crearAlimentoLote(alimento);
            } else if (mercancia.getMercancia().equals(Constantes.MERCANCIA_RECURSOS_STR)) {
                //elimino el producto del menrcado
                oMercadoAD.restarMercado(mercancia, mercancia.getCtd_actual());
                //incremento el recurso local
                oRecursoAD.sumarRecurso(mercancia.getIdRecurso(), mercancia.getIdAbadia(), mercancia.getCtd_actual());

            } else if (mercancia.getMercancia().equals(Constantes.MERCANCIA_ANIMALES_STR)) {

                Animal animal;
                int idEdificio = oEdificioAD.recuperarIdEdificioPorTipoAnimal(mercancia.getIdAnimalTipo(), mercancia.getIdAbadia());
                animal = oAnimalAD.recuperarAnimalTipo(mercancia.getIdAnimalTipo(), mercancia.getIdAnimalNivel());
                animal.setEdificioid(idEdificio);
                animal.setTipoAnimalid(mercancia.getIdAnimal());
                animal.setFecha_nacimiento(Utilidades.formatStringFromDB(mercancia.getFecha_nacimiento()));
                animal.setNivel((short) mercancia.getIdAnimalNivel());
                animal.setSalud(mercancia.getAnimalSalud());
                animal.setEstado(0);
                //bucle para crear tantos animales como teníamos a la venta...
                oMercadoAD.restarMercado(mercancia, mercancia.getCtd_actual());

                for (int iCount = 0; iCount < mercancia.getCtd_actual(); iCount++) {
                    oAnimalAD.crearAnimal(animal);
                }

            }
            return comision;

        } catch (Exception e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            //DBMSUtils.cerrarObjetoSQL(p_cConnection);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    /**
     * Elimina del mercado los alimentos caducados.
     *
     * @throws AbadiaException excepcion general
     */
    public void caducarAlimentosEnVenta() throws AbadiaException {
        String sTrace = this.getClass() + ".caducarAlimentosEnVenta()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        ArrayList<Mensajes> mensajes = new ArrayList<Mensajes>();
        int last_idiomaid = 0;
        String msg = "";

        adProcesos oProcesosAD;
        adMercado oMercadoAD;
        adLiterales oLiteralesAD;
        adMensajes oMensajesAD;

        ArrayList alProductos;
        Iterator itProductos;

        ProductoIdioma oProducto;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);

            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando Proceso Elimina Alimentos caducados del mercado", 0);
            oMercadoAD = new adMercado(con);
            oLiteralesAD = new adLiterales(con);
            oMensajesAD = new adMensajes(con);

            alProductos = oProcesosAD.recuperarVentasParaCaducarEnVenta();
            itProductos = alProductos.iterator();

            while (itProductos.hasNext()) {
                oProducto = (ProductoIdioma) itProductos.next();

                //Eliminar el regitro de mercado y el de mercados_alimentos
                oMercadoAD.eliminarAlimentoMercado(oProducto.getProductoId());

                if (last_idiomaid != oProducto.getIdiomaId()) {
                    last_idiomaid = oProducto.getIdiomaId();
                    msg = oLiteralesAD.getLiteralStatic(10116, last_idiomaid);  // Alguno de los alimentos que tenías a la venta han caducado, la venta se ha perdido.
                }
                //generamos mensajes para las comisiones del mercado..
                mensajes.add(new Mensajes(oProducto.getAbadiaId(), -1, msg, 0));
            }

            oMensajesAD.crearMensajes(mensajes);

            oProcesosAD.addLog("+ Finalizado Proceso Elimina Alimentos caducados del mercado", 0);

        } catch (SystemException e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    /**
     * controla las habilidades de los monjes
     *
     * @throws org.abbatia.exception.AbadiaSQLException
     *          Excepción base de abbatia
     */
    public void control_habilidades_resto() throws AbadiaException {
        String sTrace = this.getClass() + ".control_habilidades_resto()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adProcesos oProcesosAD;
        adHabilidades oHabilidadAD;

        Hashtable<Integer, Double> htActividad;
        Hashtable<Integer, Double> htHabilidad;
        Integer iMonje;
        Integer iActividad;
        Integer iHabilidad;
        Double dActividadCantidad;
        Double dImpactoHabilidad;
        Enumeration<Integer> actividades;
        Enumeration<Integer> habilidades;


        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);
            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando SUBProceso control_habilidades_resto", 0);
            Hashtable<Integer, Hashtable<Integer, Double>> htMonje = oProcesosAD.recuperarActividadesProduccion();
            Hashtable<Integer, Hashtable<Integer, Double>> htRelacion = oProcesosAD.recuperarRelacionActHab();

            oHabilidadAD = new adHabilidades(con);

            Enumeration<Integer> monjes = htMonje.keys();

            while (monjes.hasMoreElements()) {
                //recupero el identificador de monje
                iMonje = monjes.nextElement();
                //utilizo el identificador de monje para recuperar las hashtable de actividades asociada
                htActividad = htMonje.get(iMonje);
                //obtengo un enumerator con la lista de actividades
                actividades = htActividad.keys();
                //bucle por actividad
                while (actividades.hasMoreElements()) {
                    //recupero el primer registro de actividad
                    iActividad = actividades.nextElement();
                    //obtengo el valor asociado a esa actividad
                    dActividadCantidad = htActividad.get(iActividad);
                    //si la cantidad es 0 se fija a -1 para forzar un decremento
                    /* if (dActividadCantidad.intValue() == 0)
                    {
                        dActividadCantidad = new Double(-1);
                    }*/

                    //recorro los impactos sobre habilidades de esa actividad
                    htHabilidad = htRelacion.get(iActividad);
                    habilidades = htHabilidad.keys();
                    while (habilidades.hasMoreElements()) {
                        //recsupero clave de habilidad
                        iHabilidad = habilidades.nextElement();
                        //recupero el impacto sobre esa habilidad de la actividad
                        dImpactoHabilidad = htHabilidad.get(iHabilidad);
                        //si la cantidad de actividad es 0..
                        if (dActividadCantidad > 0) {
                            //Actualizo la habilidad
                            oHabilidadAD.incrementarHabilidad(iMonje, iHabilidad, dActividadCantidad * dImpactoHabilidad);
                        }
                    }
                }
            }
            oProcesosAD.addLog("- Finalizando SUBProceso control_habilidades_resto", 0);

        } catch (Exception e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public void gestionClimaAgricultura() throws AbadiaException {
        String sTrace = this.getClass() + ".gestionClimaAgricultura()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        ArrayList<Cultivo> alCampos;
        ArrayList<Mensajes> alMensajes = new ArrayList<Mensajes>();
        Iterator<Cultivo> itCampos;
        HashMap<Integer, ClimaRegion> hmClimas;
        HashMap<Integer, ClimaLluvia> hmPrecipitaciones;

        ClimaRegion climaRegion;
        Cultivo cultivo;
        ClimaLluvia lluvia;

        adCampo oCampoAD;
        adRegion oRegionAD;
        adClima oClimaAD;
        adProcesos oProcesosAD;
        adLiterales oLiteralesAD;
        adRecurso oRecursoAD;
        adMensajes oMensajesAD;

        int iLitrosLluvia;
        int iLitrosDiferencia;
        int iMargenDiferencial = 15;
        int iAgua;
        int iVarianteMin = 0;
        int iVarianteMax = 0;

        int lastIdioma = 0;

        String msgFrio = "";
        String msgCalor = "";
        String msgInundacion = "";
        String msgSinAgua = "";
        String msgGranizada = "";
        String msgNieve = "";

        int ratioPuntosRestar;


        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);

            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando Proceso Gestión Clima Agricultura", 0);

            oCampoAD = new adCampo(con);
            //recuperar ArrayList con los datos de todos los campos en estado cultivando
            //que tengan asignados monjes.
            alCampos = oCampoAD.recuperarCamposPorAbadiaEstado(Constantes.ESTADO_CULTIVO_CULTIVANDO);

            oRegionAD = new adRegion(con);
            hmClimas = oRegionAD.recuperarDatosClima();

            oClimaAD = new adClima(con);
            hmPrecipitaciones = oClimaAD.recuperarLluviasPorClima();

            oLiteralesAD = new adLiterales(con);
            oRecursoAD = new adRecurso(con);
            oMensajesAD = new adMensajes(con);

            itCampos = alCampos.iterator();

            while (itCampos.hasNext()) {
                cultivo = itCampos.next();

                //gestion de literales
                if (cultivo.getIdIdioma() != lastIdioma) {
                    lastIdioma = cultivo.getIdIdioma();
                    //oUtilsAD = new adUtils(Constantes.DB_CONEXION_PROCESS);
                    msgFrio = oLiteralesAD.getLiteralStatic(10611, cultivo.getIdIdioma());      //El frio en la region está perjudicando el cultivo de %s
                    msgCalor = oLiteralesAD.getLiteralStatic(10612, cultivo.getIdIdioma());     //El calor en la región está perjudicando el cultivo de %s
                    msgInundacion = oLiteralesAD.getLiteralStatic(10613, cultivo.getIdIdioma());//El exceso de lluvia está perjudicando el cultivo de %s
                    msgSinAgua = oLiteralesAD.getLiteralStatic(10614, cultivo.getIdIdioma());   //La carencia de agua en tu abbatia está perjudicando el cultivo de %s
                    msgGranizada = oLiteralesAD.getLiteralStatic(10615, cultivo.getIdIdioma()); //El granizo ha perjudicado el cultivo de %s
                    msgNieve = oLiteralesAD.getLiteralStatic(10616, cultivo.getIdIdioma());     //El exceso de nieve ha perjudicado el cultivo de %s
                    //oUtilsAD.finalize();
                }
                //obtenemos la descripción del alimento de cultivo para los mensajes
                //descAlimento = oAlimentosAD.recuperarDescripcionAlimento(cultivo.getIdAlimento(), cultivo.getIdIdioma());

                climaRegion = hmClimas.get(new Integer(cultivo.getIdRegion()));

                //si el clima de la región está por debajo de lo soportado por el cultivo...
                if (climaRegion.getTemperatura() < cultivo.getTempMinima()) {
                    //mensaje indicando la pérdida de puntos de cultivo.
                    //msgFrio = ProcesosUtils.Format(msgFrio, cultivo.getDescAlimento());
                    alMensajes.add(new Mensajes(cultivo.getIdAbadia(), -1, ProcesosUtils.Format(msgFrio, cultivo.getDescAlimento()), 0));
                    //determinar factor a utilizar en base a la direfencia de temperaturas
                    ratioPuntosRestar = +(cultivo.getTempMinima() - climaRegion.getTemperatura()) * 100;
                    if (ratioPuntosRestar < 0) {
                        ratioPuntosRestar = ratioPuntosRestar * (-1);
                    }
                    //restar puntos de cultivo
                    oCampoAD.restarPuntosCultivoStatic(cultivo.getIdCultivo(), ratioPuntosRestar);
                } else {
                    //si la temperatura de la región supera el máximo permitido por el cultivo...
                    if (climaRegion.getTemperatura() > cultivo.getTempMaxima()) {
                        //mensaje indicando la perdida de puntos de cultivo
                        //msgCalor = ProcesosUtils.Format(msgCalor, cultivo.getDescAlimento());
                        alMensajes.add(new Mensajes(cultivo.getIdAbadia(), -1, ProcesosUtils.Format(msgCalor, cultivo.getDescAlimento()), 0));
                        //determinar factor a utilizar en base a la direfencia de temperaturas
                        ratioPuntosRestar = +(climaRegion.getTemperatura() - cultivo.getTempMaxima()) * 100;
                        if (ratioPuntosRestar < 0) {
                            ratioPuntosRestar = ratioPuntosRestar * (-1);
                        }
                        //restar puntos de cultivo...
                        oCampoAD.restarPuntosCultivoStatic(cultivo.getIdCultivo(), ratioPuntosRestar);
                    }
                }
                iLitrosLluvia = 0;
                iLitrosDiferencia = 0;
                //determinamos si llueve o no
                if (climaRegion.getClima() == Constantes.CLIMA_LLOVIZNA ||
                        climaRegion.getClima() == Constantes.CLIMA_LLUVIA ||
                        climaRegion.getClima() == Constantes.CLIMA_LLUVIA_ESPORADICA ||
                        climaRegion.getClima() == Constantes.CLIMA_LLUVIA_FUERTE ||
                        climaRegion.getClima() == Constantes.CLIMA_LLUVIA_MODERADA ||
                        climaRegion.getClima() == Constantes.CLIMA_TORMENTA) {
                    //recuperamos las precipitaciones por clima (litros de agua)
                    lluvia = hmPrecipitaciones.get(new Integer(climaRegion.getClima()));

                    //recuperamos un aleatorio para los litros de lluvia que han caido.
                    if (lluvia != null) {
                        iLitrosLluvia = Utilidades.Random(lluvia.getMinLlubia(), lluvia.getMaxLlubia());
                    } else {
                        iLitrosLluvia = 0;
                    }

                    //aplicamos variaciones para el ramdom
                    switch (climaRegion.getClima()) {
                        case 5: //Constantes.CLIMA_LLOVIZNA
                            iLitrosLluvia -= 10;
                            break;
                        case 8: //Constantes.CLIMA_LLUVIA
                            break;
                        case 6: //Constantes.CLIMA_LLUVIA_ESPORADICA
                            iLitrosLluvia -= 5;
                            break;
                        case 9: //Constantes.CLIMA_LLUVIA_FUERTE
                            iLitrosLluvia += 10;
                            break;
                        case 7: //Constantes.CLIMA_LLUVIA_MODERADA
                            iLitrosLluvia -= 5;
                            break;
                        case 10: //Constantes.CLIMA_TORMENTA
                            iLitrosLluvia += 15;
                            break;
                    }

                    //obtenemos la diferencia entre el agua caida y la necesaria por el campo.
                    iLitrosDiferencia = cultivo.getAguaPorDia() - iLitrosLluvia;
                    if (iLitrosDiferencia < 0) {
                        iLitrosDiferencia = iLitrosDiferencia * (-1);
                    }
                }

                //si la diferencia + el margen diferencial es superior a 0
                if (iLitrosLluvia > cultivo.getAguaPorDia() + iMargenDiferencial) {
                    //ha llovido demasiado
                    //restamos al cultivo los puntos de diferencia de lluvia.
                    oCampoAD.restarPuntosCultivoStatic(cultivo.getIdCultivo(), iLitrosDiferencia * 5);
                    //generamos mensaje
                    //msgInundacion = ProcesosUtils.Format(msgInundacion, cultivo.getDescAlimento());
                    alMensajes.add(new Mensajes(cultivo.getIdAbadia(), -1, ProcesosUtils.Format(msgInundacion, cultivo.getDescAlimento()), 0));
                } else
                    //si la diferencia - el diferencial es inferior a 0
                    if (iLitrosLluvia + iMargenDiferencial < cultivo.getAguaPorDia()) {
                        //ha llovido poco
                        //si no ha llovido suficiente obtenemos la cantidad de agua que tiene la abbatia
                        iAgua = (int) oRecursoAD.recuperarValorRecurso(Constantes.RECURSOS_AGUA, cultivo.getIdAbadia());
                        //si tiene agua suficiente...
                        if (iAgua >= +(iLitrosDiferencia)) {
                            //restamos de recsurso agua los litros necesarios...
                            oRecursoAD.restarRecurso(Constantes.RECURSOS_AGUA, cultivo.getIdAbadia(), iLitrosDiferencia);
                        } else {
                            //no se dispone de agua suficiente
                            //se restan puntos de cultivo
                            oCampoAD.restarPuntosCultivoStatic(cultivo.getIdCultivo(), cultivo.getAguaPorDia() * 5);
                            //crear mensaje 'sin agua en tu abbatia'
                            //msgSinAgua = ProcesosUtils.Format(msgSinAgua, cultivo.getDescAlimento());
                            alMensajes.add(new Mensajes(cultivo.getIdAbadia(), -1, ProcesosUtils.Format(msgSinAgua, cultivo.getDescAlimento()), 0));
                        }
                    }
                //control para el granizo
                if (climaRegion.getClima() == Constantes.CLIMA_GRANIZO) {
                    //restar puntos de cultivo
                    oCampoAD.restarPuntosCultivoStatic(cultivo.getIdCultivo(), 1000);
                    //generar mensaje
                    //msgGranizada = ProcesosUtils.Format(msgGranizada, cultivo.getDescAlimento());
                    alMensajes.add(new Mensajes(cultivo.getIdAbadia(), -1, ProcesosUtils.Format(msgGranizada, cultivo.getDescAlimento()), 0));
                }
                //control para la cantidad de nieve
                if (climaRegion.getNieve() > 8) {
                    //restar puntos de cultivo
                    oCampoAD.restarPuntosCultivoStatic(cultivo.getIdCultivo(), 100);
                    //generar mensaje
                    //msgNieve = ProcesosUtils.Format(msgNieve, cultivo.getDescAlimento());
                    alMensajes.add(new Mensajes(cultivo.getIdAbadia(), -1, ProcesosUtils.Format(msgNieve, cultivo.getDescAlimento()), 0));
                }

            }
            //ponemos a 0 los puntos de cultivo que sean negativos.
            oCampoAD.resetPuntosCultivoCero();

            oMensajesAD.crearMensajes(alMensajes);

            oProcesosAD.addLog("- Finalizado Proceso Gestión Clima Agricultura", 0);


        } catch (AbadiaException e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public void resetearAlimentacionMonjes(int p_iPeriodo) throws AbadiaException {
        String sTrace = this.getClass() + ".resetearAlimentacionMonjes(" + p_iPeriodo + ")";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adUtils oUtilsAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);
            oUtilsAD = new adUtils(con);
            oUtilsAD.execProcedure("call resetearAlimentacionMonjes(" + p_iPeriodo + ")");

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);

        }
    }

    public ArrayList recuperarMonjesActividadPeriodo(int p_iPeriodo, int p_iTarea) throws AbadiaException {
        String sTrace = this.getClass() + ".recuperarMonjesActividadPeriodo(" + p_iPeriodo + ", " + p_iTarea + ")";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adProcesos oProcesosAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);
            oProcesosAD = new adProcesos(con);
            return oProcesosAD.recuperarMonjesActividadPeriodo(p_iPeriodo, Constantes.TAREA_GANADERIA);

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);

        }
    }

    public void lanzar_mercados() throws AbadiaException {
        String sTrace = this.getClass() + ".lanzar_mercados()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adProcesos oProcesosAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);

            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando Proceso lanzar_mercados", 0);

            oProcesosAD.regularizaPrecios();

            oProcesosAD.addLog("- Finalizando Proceso lanzar_mercados", 0);

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);

        }

    }

    public void resetActividades() throws AbadiaException {
        String sTrace = this.getClass() + ".resetActividades()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adProcesos oProcesosAD;
        adActividad oActividadAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);
            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando Proceso Reset de actividades", 0);

            oActividadAD = new adActividad(con);
            oActividadAD.resetActividades();

            oProcesosAD.addLog("- Finalizando Proceso Reset de actividades", 0);

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);

        }

    }

    public void eliminarMensajesActividadesAntiguos(int p_iNumDias) throws AbadiaException {
        String sTrace = this.getClass() + ".eliminarMensajesActividadesAntiguos(" + p_iNumDias + ")";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adProcesos oProcesosAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);
            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando Proceso Eliminar mensajes Actividades Antiguos", 0);
            oProcesosAD.eliminarMensajesActividadesAntiguos(p_iNumDias);
            oProcesosAD.addLog("- Finalizando Proceso Eliminar mensajes Actividades Antiguos", 0);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public Hashtable cargarStressPorEdificio() throws AbadiaException {
        String sTrace = this.getClass() + ".cargarStressPorEdificio()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adProcesos oProcesosAD;

        Hashtable htStress;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);
            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando Proceso cargar Stress Por Edificio", 0);
            htStress = oProcesosAD.cargarStressPorEdificio();
            oProcesosAD.addLog("- Finalizando Proceso cargar Stress Por Edificio", 0);
            return htStress;
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public void trabajosMonje(int p_iFranjaHoraria) throws AbadiaException {
        String sTrace = this.getClass() + ".trabajosMonje()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);
        adProcesos oProcesosAD;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);
            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando Proceso trabajar_monje Periodo " + p_iFranjaHoraria, 0);
            DBMSUtils.cerrarObjetoSQL(con);

            prTrabajos.trabajar_monje(p_iFranjaHoraria);

            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);
            oProcesosAD.addLog("- Finalizando Proceso trabajar_monje Periodo " + p_iFranjaHoraria, 0);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }
}