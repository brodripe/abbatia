package org.abbatia.process.bbean;

import org.abbatia.adbean.adLiterales;
import org.abbatia.adbean.adMensajes;
import org.abbatia.adbean.adUtils;
import org.abbatia.bean.*;
import org.abbatia.core.CoreTiempo;
import org.abbatia.dbms.ConnectionFactory;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaDBConnectionException;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.process.adbean.adProcesos;
import org.abbatia.process.utils.ProcesosUtils;
import org.abbatia.utils.Constantes;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

public class ProcesosAlimentosBBean extends ProcesosUtils {

    private static Logger log = Logger.getLogger(ProcesosAlimentosBBean.class.getName());

    public ProcesosAlimentosBBean() throws AbadiaDBConnectionException {
        super();
    }

    /* Alimentar los monjes
    */
    public void alimentar_monjes(int estado) throws AbadiaException {
        String sTrace = this.getClass() + ".alimentar_monjes(" + estado + ")";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        ArrayList<Mensajes> alMensajes = new ArrayList<Mensajes>();

        adUtils oUtilsAD;
        adProcesos oProcesosAD;
        adLiterales oLiteralesAD;
        adMensajes oMensajesAD;

        int last_abadiaid = 0, last_idiomaid = 0, tmp_idiomaid;
        int iAbadiaProceso;
        int bReturn;
        boolean alimentos_en_cocina = true;
        String msg1 = "", msg2 = "", msg6 = "";
        HashMap<Short, ArrayList<AlimentoFamiliaProceso>> hmAlimentosPorFamilia = new HashMap<Short, ArrayList<AlimentoFamiliaProceso>>();
        ArrayList<AlimentoFamiliaProceso> alAlimentosPorAbadia = new ArrayList<AlimentoFamiliaProceso>();

        ArrayList<Monje> alListaMonjes = new ArrayList<Monje>();
        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS, Constantes.AUTOCOMIT_OF);
            // Recuperar los monjes
            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando Proceso alimentar_monjes: " + estado, 0);
            if (estado == Constantes.MONJE_VIVO) {
                alListaMonjes = oProcesosAD.getMonjesPrAlimentacion();
            } else if (estado == Constantes.MONJE_VISITA) {
                alListaMonjes = oProcesosAD.getMonjesPrAlimentacionVisita();
            }
            oUtilsAD = new adUtils(con);
            oLiteralesAD = new adLiterales(con);
            oMensajesAD = new adMensajes(con);

            for (Monje monje : alListaMonjes) {
                // Cargar los alMensajes en el idioma correcto
                if (last_abadiaid != monje.getIdAbadia()) {
                    last_abadiaid = monje.getIdAbadia();
                    alimentos_en_cocina = true;

                    if (estado == Constantes.MONJE_VISITA)
                        iAbadiaProceso = monje.getIdDeAbadia_Visita();
                    else
                        iAbadiaProceso = monje.getIdAbadia();
                    //cada vez que cambie la abadía, recargamos la hash de alimentos específicos y por familias
                    hmAlimentosPorFamilia = oProcesosAD.recuperarAlimentosPorFamiliaAbadia(iAbadiaProceso);
                    alAlimentosPorAbadia = oProcesosAD.recuperarAlimentosPorAbadia(iAbadiaProceso);
                    tmp_idiomaid = oUtilsAD.getIdiomaID(last_abadiaid);

                    if (last_idiomaid != tmp_idiomaid) {
                        last_idiomaid = tmp_idiomaid;
                        msg1 = oLiteralesAD.getLiteralStatic(10050, last_idiomaid);  // Esta enfadado porque no ha comido
                        msg2 = oLiteralesAD.getLiteralStatic(10051, last_idiomaid);  // Protesta porque no tiene comida suficiente
                        msg6 = oLiteralesAD.getLiteralStatic(10055, last_idiomaid);  // Protesta porque no ha comido lo que le tocaba
                    }

                }

                if (alimentos_en_cocina) {
                    // Alimentarlo! ( 0 - No ha comido, 1 - Ha comido algo, 2 - Ha comido todo
                    if (!alAlimentosPorAbadia.isEmpty()) {
                        bReturn = oProcesosAD.alimentarMonjeFamilia(monje, hmAlimentosPorFamilia, alAlimentosPorAbadia);
                    } else {
                        bReturn = -1;
                    }

                    if (bReturn == -1) {
                        alimentos_en_cocina = false; // En la abbatia no hay alimentos... no volver a calcular la alimentacion de los demás monjes
                        bReturn = 0;
                    }
                } else {
                    bReturn = 0;
                }

                // Se ha alimentado???
                if (bReturn == 0)
                    alMensajes.add(new Mensajes(monje.getIdAbadia(), monje.getIdMonje(), msg1, 1));
                else if (bReturn == 1)
                    alMensajes.add(new Mensajes(monje.getIdAbadia(), monje.getIdMonje(), msg2, 1));
                else if (bReturn == 3)
                    alMensajes.add(new Mensajes(monje.getIdAbadia(), monje.getIdMonje(), msg6, 0));


                if (bReturn == 2) //ha comido todo lo que esperaba..
                {
                    //en este caso inicializamos todos los parámetros de alimentación a 30 (perfect)
                    //e incrementamos en 2 puntitos la salud...
                    //salud = salud + 2;
                    monje.setVitaminas((short) 30);
                    monje.setProteinas((short) 30);
                    monje.setHidratosCarbono((short) 30);
                    monje.setLipidos((short) 30);
                }
                // Guardar los datos del monje
                oUtilsAD.execSQL(new StringBuilder().append("UPDATE monje_alimentacion SET ultima_comida='").append(CoreTiempo.getTiempoAbadiaString()).append("' ").append(",ha_comido_FamiliaID_1 = ").append(monje.getHa_comidoFamiliaID1()).append(",ha_comido_FamiliaID_2 = ").append(monje.getHa_comidoFamiliaID2()).append(",ha_comido_FamiliaID_3 = ").append(monje.getHa_comidoFamiliaID3()).append(",ha_comido_FamiliaID_4 = ").append(monje.getHa_comidoFamiliaID4()).append(",ha_comido_FamiliaID_5 = ").append(monje.getHa_comidoFamiliaID5()).append(",proteinas = ").append(monje.getProteinas()).append(",lipidos = ").append(monje.getLipidos()).append(",hidratos_carbono = ").append(monje.getHidratosCarbono()).append(",vitaminas = ").append(monje.getVitaminas()).append(" WHERE monjeid = ").append(monje.getIdMonje()).toString());
            }
            // Historicos
            oUtilsAD.execSQL("UPDATE `monje_alimentacion` " +
                    "set ant_proteinas = proteinas, ant_lipidos = lipidos, ant_hidratos_carbono = hidratos_carbono, ant_vitaminas = vitaminas");
            // Borrar los alimentos lote vacios
            oUtilsAD.execSQL("DELETE FROM `alimentos` where loteid in (select loteid from alimentos_lote where cantidad <= 0) ");
            oUtilsAD.execSQL("DELETE FROM `alimentos_lote` where cantidad <= 0 ");

            // Pasar todos los alMensajes almacenados a pantalla
            oMensajesAD.crearMensajes(alMensajes);

            oProcesosAD.addLog("- Finalizando Proceso alimentar_monjes: " + estado, 0);
            ConnectionFactory.commitTransaction(con);
        } catch (AbadiaSQLException e) {
            ConnectionFactory.rollbackTransaction(con);
            throw new AbadiaSQLException("ERROR: alimentar_monjes = " + e, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }


    /*#######################################################################################################################
    */
    /* Alimetar los animales
    */
    public void alimentar_animales() throws AbadiaException {

        String sTrace = this.getClass() + ".alimentar_animales()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        ArrayList<Mensajes> alMensajes = new ArrayList<Mensajes>();
        adUtils oUtilsAD;
        adLiterales oLiteralAD;
        adProcesos oProcesosAD;
        adMensajes oMensajesAD;


        EdificioProceso oEdificioProceso;


        int salud, last_abadia = 0, last_edificio = 0, last_idiomaid = 0, numMonjes = 0, estresados = 0;
        String msg;
        ArrayList<String> msg1;
        ArrayList<String> msg2;
        ArrayList<String> msg3;
        ArrayList<String> msg4;
        ArrayList<AnimalProceso> alAnimales;

        boolean han_comido = true;
        Hashtable<Integer, EdificioProceso> htStress;
        Hashtable<Integer, Integer> htNumMonjes;
        HashMap<Integer, Boolean> htHayComida = new HashMap<Integer, Boolean>();

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);
            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando Proceso alimentar_animales ", 0);
            alAnimales = oProcesosAD.recuperarAnimalesAlimentacion();

            oUtilsAD = new adUtils(con);
            oLiteralAD = new adLiterales(con);
            oMensajesAD = new adMensajes(con);

            // si no hay animales para procesar...
            if (!alAnimales.isEmpty()) {


                htStress = oProcesosAD.cargarStressPorEdificio();
                //lanzaremos también una consulta única para obtener una hashtable de numero de monjes por abacia actividad.
                htNumMonjes = oProcesosAD.recuperarNumMonjesTareaPorAbadia(Constantes.TAREA_GANADERIA);
                oProcesosAD.sumarNumMonjesTareaPorAbadiaVisita(Constantes.TAREA_GANADERIA, htNumMonjes);
                // Mensajes
                msg1 = oLiteralAD.getLiteral(10070); // Los animales están nerviosos por falta de comida
                msg2 = oLiteralAD.getLiteral(10071); // No tienes monjes que den de alimentar a los animales
                msg3 = oLiteralAD.getLiteral(10072); // El animal %s se ha muerto por no tener buena salud
                msg4 = oLiteralAD.getLiteral(10073); // Los animales no han comido porque estan estresados por falta de espacio

                alMensajes.add(new Mensajes(206, -1, msg1.get(1), 1));

                for (AnimalProceso oAnimalProceso : alAnimales) {
                    // Cambio de edificio
                    if (last_edificio != oAnimalProceso.getIdEdificio()) {

                        if ((last_edificio != 0) && (!han_comido)) // Han comido?
                            alMensajes.add(new Mensajes(last_abadia, -1, msg1.get(last_idiomaid), 1));
                        if ((last_edificio != 0) && ((estresados == 1) || (numMonjes == 0))) { // Estan estresados?
                            if (numMonjes == 0)
                                alMensajes.add(new Mensajes(last_abadia, -1, msg2.get(last_idiomaid), 1));
                            else
                                alMensajes.add(new Mensajes(last_abadia, -1, msg4.get(last_idiomaid), 1));
                            // Decrementar 8 puntitos la salud

                            oUtilsAD.execSQL("update animales " +
                                    "set salud = salud - 8 " +
                                    "where edificioid = " + last_edificio + " and salud > 1 and estado = 0 ");
                        }
                        han_comido = true;
                        last_abadia = oAnimalProceso.getIdAbadia();
                        last_edificio = oAnimalProceso.getIdEdificio();
                        oEdificioProceso = htStress.get(new Integer(last_edificio));
                        if (oEdificioProceso != null) {
                            estresados = oEdificioProceso.getExtresado();
                        }
                        if (htNumMonjes.get(new Integer(last_abadia)) == null) {
                            numMonjes = 0;
                        } else {
                            numMonjes = htNumMonjes.get(new Integer(last_abadia));
                        }

                        last_idiomaid = oAnimalProceso.getIdIdioma();
                        htHayComida.clear();
                    }

                    // Si no estan estresados!!!

                    //Modificacion 15/11 BRP
                    //si los animales estan estresados comeran igual (de lo contrario sera un chollazo)
                    //y si no tienen comida se les restara la salud como al resto.
                    //adicionalmente si estan estresado se les restara a todos un puntillo de salud al cambiar de abbatia.
                    //Se estaba dando la situacion de que los animales no gastan comida si están estresados.
                    //las 2 consecuencias deben sumarse si no hay espacio: consumen alimentos y se les resta la salud.
/*
                    if (oAnimalProceso.getIdAbadia() == 1000024243)
                    {
                        log.info("test proceso");  
                    }
*/
                    if ((estresados == 0) && (numMonjes > 0)) {
                        //si la familia de alimentos está en la hm
                        if (!htHayComida.containsKey(oAnimalProceso.getConsumoFamilia())) {//significa que ya he verificado que no dispongo de esa familia
                            salud = oProcesosAD.alimentar_animal(oAnimalProceso.getIdAbadia(),
                                    oAnimalProceso.getConsumoFamilia(),
                                    oAnimalProceso.getConsumo());
                            if (salud == -1) {
                                // No hay comida, marcar la familia como inexistente para no volver a cargar los datos
                                htHayComida.put(oAnimalProceso.getConsumoFamilia(), true);
                                han_comido = false;
                            }
                        } else salud = -1;
                        oAnimalProceso.setSalud(oAnimalProceso.getSalud() + (salud));
                        oUtilsAD.execSQL("UPDATE animales SET salud = " + oAnimalProceso.getSalud() + " WHERE animalid = " + oAnimalProceso.getIdAnimal());
                    }

                    // Se muere el animal
                    if (oAnimalProceso.getSalud() <= 0) {
                        msg = Format(msg3.get(last_idiomaid), oAnimalProceso.getDescripcion());
                        alMensajes.add(new Mensajes(oAnimalProceso.getIdAbadia(), -1, msg, 1));
                    }

                    if (!han_comido)
                        alMensajes.add(new Mensajes(oAnimalProceso.getIdAbadia(), -1, msg1.get(last_idiomaid), 1));
                    if (numMonjes == 0)
                        alMensajes.add(new Mensajes(oAnimalProceso.getIdAbadia(), -1, msg2.get(last_idiomaid), 1));
                }
            }

            // Matar a los animales que no tienen salud
            oUtilsAD.execSQL(" UPDATE animales a, edificio e " +
                    " SET a.salud = 0, a.estado = 1, fecha_fallecimiento= '" +
                    CoreTiempo.getTiempoAbadiaString() + "' " +
                    " WHERE a.edificioid = e.edificioid and a.salud <= 0 and a.estado = 0 ");
            oUtilsAD.execSQL("UPDATE animales SET salud = 100 WHERE salud > 100");
            // Borrar los alimentos lote vacios
            oUtilsAD.execSQL("DELETE FROM alimentos where loteid in (select loteid from `alimentos_lote` where cantidad <= 0) ");
            oUtilsAD.execSQL("DELETE FROM `alimentos_lote` where cantidad <= 0 ");

            oMensajesAD.crearMensajes(alMensajes);

            oProcesosAD.addLog("- Finalizando Proceso alimentar_animales ", 0);

        } catch (Exception e) {
            throw new AbadiaSQLException("ERROR: alimentar_animales ", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }


    public void caducaAlimentos() throws AbadiaException {

        String sTrace = this.getClass() + ".caducaAlimentos()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);


        adUtils oUtilsAD;
        adProcesos oProcesosAD;
        adLiterales oLiteralesAD;
        adMensajes oMensajesAD;

        //AlimentoProceso oAlimentoProceso;
        ArrayList<Mensajes> alMensajes = new ArrayList<Mensajes>();
        ArrayList<AlimentoProceso> alAlimentos;

        int last_idiomaid = -1;
        String msg1 = "";

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);
            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando Proceso caducaAlimentos", 0);
            alAlimentos = oProcesosAD.recuperarAlimentosCaduca();

            oUtilsAD = new adUtils(con);
            oLiteralesAD = new adLiterales(con);
            oMensajesAD = new adMensajes(con);

            //itAlimentos = alAlimentos.iterator();

            for (AlimentoProceso oAlimentoProceso : alAlimentos) {
                if (last_idiomaid != oAlimentoProceso.getIdidioma()) {
                    last_idiomaid = oAlimentoProceso.getIdidioma();
                    msg1 = oLiteralesAD.getLiteralStatic(10002, last_idiomaid); // Han caducado %d %s de %s
                }
                msg1 = Format(msg1, oAlimentoProceso.getCantidad());
                msg1 = Format(msg1, oAlimentoProceso.getMedida());
                msg1 = Format(msg1, oAlimentoProceso.getDescripcion());
                alMensajes.add(new Mensajes(oAlimentoProceso.getIdAbadia(), -1, msg1, 0));
                oUtilsAD.execSQL("UPDATE alimentos_lote SET cantidad = 0 WHERE LoteID = " + oAlimentoProceso.getIdLote());
            }
            alAlimentos.clear();

            // Mercados
            alAlimentos = oProcesosAD.recuperarAlimentosCaducaMercado();

            for (AlimentoProceso oAlimentoProceso : alAlimentos) {
                if (last_idiomaid != oAlimentoProceso.getIdidioma()) {
                    last_idiomaid = oAlimentoProceso.getIdidioma();
                    msg1 = oLiteralesAD.getLiteralStatic(10003, last_idiomaid); // Han caducado %d %s de %s que tenías en venta en el mercado
                }
                //msg = Format(msg1, oAlimentoProceso.getCantidad());
                msg1 = Format(msg1, oAlimentoProceso.getMedida());
                msg1 = Format(msg1, oAlimentoProceso.getDescripcion());
                alMensajes.add(new Mensajes(oAlimentoProceso.getIdAbadia(), -1, msg1, 0));

                oUtilsAD.execSQL("UPDATE mercados SET estado = 1 WHERE productoid = " + oAlimentoProceso.getIdLote());
            }

            // Eliminar lotes caducados
            oUtilsAD.execSQL("DELETE FROM `alimentos` where loteid in (select loteid from alimentos_lote where fecha_caducidad < date('" + CoreTiempo.getTiempoAbadiaString() + "'))");
            oUtilsAD.execSQL("DELETE FROM `alimentos_lote` where fecha_caducidad < date('" + CoreTiempo.getTiempoAbadiaString() + "')");

            // Borrar los alimentos lote vacios
            oUtilsAD.execSQL("DELETE FROM `alimentos` where loteid in (select loteid from alimentos_lote where cantidad <= 0) ");
            oUtilsAD.execSQL("DELETE FROM `alimentos_lote` where cantidad <= 0 ");

            // Actualizar los estados de los lotes
            oUtilsAD.execSQL("Update alimentos_lote " +
                    "set estado =  ((to_days(fecha_caducidad) - to_days('" + CoreTiempo.getTiempoAbadiaString() + "'))  / (to_days(fecha_caducidad) - to_days(fecha_entrada))) * 100");
            oUtilsAD.execSQL("UPDATE `mercados_alimentos` ma, mercados m " +
                    "SET ma.estado = ((to_days(fecha_caducidad) - to_days('" + CoreTiempo.getTiempoAbadiaString() + "'))  / (to_days(fecha_caducidad) - to_days(fecha_inicial))) * 100 " +
                    "Where ma.productoid = m.productoid and m.mercancia = 'A' and m.estado = 0 and m.abadiaid <> 0 ");

            // Borrar los mercados alimentos que no tengan relación con mercado
            oUtilsAD.execSQL("DELETE FROM mercados WHERE fecha_final < '" + CoreTiempo.getDiferenciaString(-360) + "' and estado = 1 and mercancia='A'");
            oUtilsAD.execSQL("DELETE mercados_alimentos FROM mercados_alimentos LEFT JOIN mercados USING(productoid) WHERE mercados.productoid IS NULL");

            oMensajesAD.crearMensajes(alMensajes);

            oProcesosAD.addLog("- Finalizando Proceso caducaAlimentos", 0);

        } catch (Exception e) {
            throw new AbadiaSQLException("ERROR: caducaAlimentos ", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

}
