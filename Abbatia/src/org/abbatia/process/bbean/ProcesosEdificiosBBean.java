package org.abbatia.process.bbean;

import org.abbatia.adbean.*;
import org.abbatia.bean.*;
import org.abbatia.dbms.ConnectionFactory;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaDBConnectionException;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.process.adbean.adProcesos;
import org.abbatia.process.utils.ProcesosUtils;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.Utilidades;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

public class ProcesosEdificiosBBean extends ProcesosUtils {

    private static Logger log = Logger.getLogger(ProcesosEdificiosBBean.class.getName());

    public ProcesosEdificiosBBean() throws AbadiaDBConnectionException {
        super();
    }

    public void nivel_edificios() throws AbadiaException {

        String sTrace = this.getClass() + ".nivel_edificios()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adProcesos oProcesosAD;
        adUtils oUtilsAD;
        adAbadia oAbadiaAD;
        adLiterales oLiteralesAD;
        adMensajes oMensajesAD;

        ArrayList<Mensajes> mensajes = new ArrayList<Mensajes>();

        String msg = "";
        int last_abadiaid = 0, last_idiomaid = 0, tmp_idiomaid;
        ArrayList<EdificioNivel> alAbadias;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);

            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando Proceso nivel_edificios", 0);

            oUtilsAD = new adUtils(con);
            oLiteralesAD = new adLiterales(con);
            oMensajesAD = new adMensajes(con);
            oAbadiaAD = new adAbadia(con);
            alAbadias = oAbadiaAD.recuperarAbadiasParaNivelEdificios();


            for (EdificioNivel oEdificioNivel : alAbadias) {
                // Cargar los mensajes en el idioma correcto
                if (last_abadiaid != oEdificioNivel.getAbadiaId()) {
                    last_abadiaid = oEdificioNivel.getAbadiaId();

                    tmp_idiomaid = oUtilsAD.getIdiomaID(last_abadiaid);
                    if (last_idiomaid != tmp_idiomaid) {
                        last_idiomaid = tmp_idiomaid;
                        msg = oLiteralesAD.getLiteralStatic(10040, last_idiomaid);  // Se ha finalizado la ampliación del edificio
                    }

                }
                // Comprobar los niveles
                oUtilsAD.execSQL("UPDATE edificio SET nivel = " + oEdificioNivel.getNivel() + " WHERE ABADIAID = " + oEdificioNivel.getAbadiaId() +
                        " AND TipoEdificioID = " + oEdificioNivel.getTipoEdificioId());
                oUtilsAD.execSQL("DELETE FROM `edificio_construccion` WHERE ABADIAID = " + oEdificioNivel.getAbadiaId() +
                        " AND TipoEdificioID = " + oEdificioNivel.getTipoEdificioId());
                mensajes.add(new Mensajes(last_abadiaid, -1, msg, 0));
            }
            oMensajesAD.crearMensajes(mensajes);

            oProcesosAD.addLog("- Finalizando Proceso nivel_edificios", 0);

        } catch (Exception e) {
            throw new AbadiaSQLException("ERROR: nivel_edificios = " + e, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    /**
     * GEstiona el desgaste de los edificios en función del tipo y el nivel
     * En caso de que el deterioro del edificio baje de un porentaje determinado
     * el edificio disminuirá de nivel.
     *
     * @throws AbadiaException Excepción básica
     */
    public void gestionDesgasteEdificios() throws AbadiaException {

        String sTrace = this.getClass() + ".gestionDesgasteEdificios()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);


        adEdificio oEdificioAD;
        adProcesos oProcesosAD;
        adLiterales oLiteralesAD;
        adLibros oLibrosAD;
        adMensajes oMensajesAD;

        HashMap<String, Double> hmDatosDesgaste;
        ArrayList<EdificioMantenimiento> alEdificios;
        ArrayList<Mensajes> alMensajes = new ArrayList<Mensajes>();

        Libro libro;
        double iDeterioro;
        int idLastIdioma = 0;
        String msgEdificioBajaNivel = "";
        String msgTmp;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);

            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando SubProceso Gestión Desgaste Edificios", 0);

            oLiteralesAD = new adLiterales(con);
            //cargar hashmap con la configuración de desgaste de los edificios
            oEdificioAD = new adEdificio(con);
            hmDatosDesgaste = oEdificioAD.recuperarParametrosDesgaste();
            //cargar lista arraylista de edificios de nivel > 2 para ser procesados
            alEdificios = oEdificioAD.recuperarEdificiosMantenimiento();

            oLibrosAD = new adLibros(con);

            for (EdificioMantenimiento edificio : alEdificios) {
                //gestión de literales...
                if (idLastIdioma != edificio.getIdIdioma()) {
                    idLastIdioma = edificio.getIdIdioma();
                    msgEdificioBajaNivel = oLiteralesAD.getLiteralStatic(70001, idLastIdioma);  // El deterioro del edificio: %s ha provocado su bajada de nivel
                }

                //recuperamos el desgaste asodiado al edificio..
                try {
                    iDeterioro = hmDatosDesgaste.get(String.valueOf(edificio.getIdTipoEdificio()).concat(String.valueOf(edificio.getNivel())));
                } catch (NullPointerException e) {
                    iDeterioro = 0;
                }

                //aquí debemos incluir el impacto del libro 324 Conservator aedificium del siguiente modo
                //nivel 1 - reduce en un 20 por ciento el desgaste que sufre el edificio
                //nivel 2 - reduce en un 40 por ciento el desgaste que sufre el edificio
                //nivel 3 - reduce en un 60 por ciento el desgaste que sufre el edificio
                //nivel 4 - reduce en un 80 por ciento el desgaste que sufre el edificio

                libro = oLibrosAD.recuperaLibroTipo(Constantes.LIBRO_TIPO_CONSERVATOR, edificio.getIdAbadia());
                if (libro != null) {
                    switch (libro.getNivel()) {
                        case 1:
                            iDeterioro = iDeterioro - iDeterioro * 20 / 100;
                            break;
                        case 2:
                            iDeterioro = iDeterioro - iDeterioro * 40 / 100;
                            break;
                        case 3:
                            iDeterioro = iDeterioro - iDeterioro * 60 / 100;
                            break;
                        case 4:
                            iDeterioro = iDeterioro - iDeterioro * 80 / 100;
                            break;
                    }
                }

                //si el desgaste restado al estado actual del edificio está por debajo de 50 puntos...
                if (edificio.getEstado() - iDeterioro < 50) {
                    //bajamos el nivel del edificio.
                    //solo si el nivel es mayor que 1
                    if (edificio.getNivel() > 1) {
                        oEdificioAD.bajarNivel(edificio.getIdEdificio(), edificio.getIdAbadia(), 0, 0);
                        //reestablecemos los puntos de estado para el edificio una vez descendido el nivel
                        oEdificioAD.asignarEstadoEdificio(edificio.getIdEdificio(), 100);
                        //generamos un mensaje indicando que el edificio ha bajado de nivel....
                        msgTmp = ProcesosUtils.Format(msgEdificioBajaNivel, oEdificioAD.recuperarNombreEdificio(edificio.getIdEdificio(), edificio.getIdIdioma()));
                        alMensajes.add(new Mensajes(edificio.getIdAbadia(), -1, msgTmp, 0));
                    }
                } else {
                    //restamos los puntos de desgaste del edificio
                    oEdificioAD.bajarEstadoEdificio(edificio.getIdEdificio(), iDeterioro);
                }
            }
            //creamos los mensajes
            oMensajesAD = new adMensajes(con);
            oMensajesAD.crearMensajes(alMensajes);
            oProcesosAD.addLog("+ Finalizado SubProceso Gestión Desgaste Edificios", 0);

        } catch (Exception e) {
            throw new AbadiaSQLException("ERROR: gestionDesgasteEdificios = " + e, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    /**
     * gestiona el pago de gastos de mantenimiento
     *
     * @return HashMap<Integer, Integer>
     * @throws AbadiaException Exception general
     */
    public HashMap<Integer, Integer> gestionGastoMantenimientoEdificios() throws AbadiaException {

        String sTrace = this.getClass() + ".gestionGastoMantenimientoEdificios()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        //recuperar abbatia, coste total de mantenimiento, oro de la abbatia, idioma
        adEdificio oEdificioAD;
        adRecurso oRecursoAD;
        adProcesos oProcesosAD;
        adLiterales oLiteralesAD;
        adMensajes oMensajesAD;

        ArrayList<EdificioMantenimiento> alAbadias;
        HashMap<Integer, Integer> hmAbadia = new HashMap<Integer, Integer>();
        int idIdioma = 0;
        String msgSinRecursos = "";
        String msgTmp;
        String msgCosteMantenimiento = "";
        ArrayList<Mensajes> alMensajes = new ArrayList<Mensajes>();

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);

            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando SubProceso Gestión Gasto Mantenimiento Edificios", 0);

            oRecursoAD = new adRecurso(con);
            oLiteralesAD = new adLiterales(con);
            oEdificioAD = new adEdificio(con);
            alAbadias = oEdificioAD.recuperarAbadiasMantenimientoEdificios();


            for (EdificioMantenimiento abadia : alAbadias) {
                //gestion de literales por idioma
                if (abadia.getIdIdioma() != idIdioma) {
                    //utilsAD = new adUtils(Constantes.DB_CONEXION_PROCESS);
                    msgSinRecursos = oLiteralesAD.getLiteralStatic(70002, abadia.getIdIdioma());  //No dispones de oro suficiente (%d) para el mantenimiento de tus edificios
                    msgCosteMantenimiento = oLiteralesAD.getLiteralStatic(70003, abadia.getIdIdioma()); //Has pagado (%d) monedas de oro en concepto de mantenimiento de tus edificios
                    //utilsAD.finalize();
                    idIdioma = abadia.getIdIdioma();
                }
                //si el coste de mantenimiento supera los recursos de la abbatia...
                if (abadia.getPrecioMantenimiento() > abadia.getOroTotal()) {
                    //notificamos que los edificios de la abbatia no han podido ser reparados por falta de recursos
                    msgTmp = ProcesosUtils.Format(msgSinRecursos, abadia.getPrecioMantenimiento());
                    alMensajes.add(new Mensajes(abadia.getIdAbadia(), -1, msgTmp, Constantes.MENSAJE_TIPO_WARNING));
                    //marcamos la abadía indicamos que sus edificio no se han reparado...
                    hmAbadia.put(abadia.getIdAbadia(), 0);

                } else if (abadia.getPrecioMantenimiento() > 0) {
                    //restamos la pasta de la reparación

                    oRecursoAD.restarRecurso(Constantes.RECURSOS_ORO, abadia.getIdAbadia(), abadia.getPrecioMantenimiento());
                    //mensaje indicando el pago por mantenimiento de edificios
                    msgTmp = ProcesosUtils.Format(msgCosteMantenimiento, abadia.getPrecioMantenimiento());
                    alMensajes.add(new Mensajes(abadia.getIdAbadia(), -1, msgTmp, Constantes.MENSAJE_TIPO_INFO));
                    //marcamos la abbatia como que sus edificios se han reparado.
                    hmAbadia.put(abadia.getIdAbadia(), 1);
                }

            }
            oMensajesAD = new adMensajes(con);
            oMensajesAD.crearMensajes(alMensajes);
            oProcesosAD.addLog("+ Finalizado SubProceso Gestión Gasto Mantenimiento Edificios", 0);
            return hmAbadia;

        } catch (Exception e) {
            throw new AbadiaSQLException("ERROR: gestionGastoMantenimientoEdificios = " + e, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }


    }

    /**
     * Gestiona la recuperación de puntos para el estado de los edificios en función del grado
     * de mantenimiento contratado por la abadía
     *
     * @param hmAbadias abadias
     * @throws AbadiaException excepcion general
     */
    public void gestionaReparacionEdificios(HashMap hmAbadias) throws AbadiaException {

        String sTrace = this.getClass() + ".gestionaReparacionEdificios()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adEdificio oEdificioAD;
        adLibros oLibrosAD;
        adProcesos oProcesosAD;

        ArrayList<EdificioMantenimiento> alEdificios;
        HashMap<String, Double> hmDatosDesgaste;


        Libro libro;
        double iDeterioro;

        //si la lista de abadías no está informada, no ejecutamos el proceso
        if (hmAbadias == null) {
            return;
        }
        //Recibimos como parámetro la lista de abadías que han pagado mantenimiento.
        //cargar hashmap con la configuración de desgaste de los edificios
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);

            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando SubProceso Gestión Reparación Edificios", 0);
            oEdificioAD = new adEdificio(con);
            hmDatosDesgaste = oEdificioAD.recuperarParametrosDesgaste();
            //cargar lista arraylista de edificios de nivel > 2 para ser procesados
            alEdificios = oEdificioAD.recuperarParametrosMantenimiento();
            oLibrosAD = new adLibros(con);

            for (EdificioMantenimiento edificio : alEdificios) {
                //si el edificio pertenece a un abadía que ha pagado el mantenimiento...
                if (hmAbadias.get(new Integer(edificio.getIdAbadia())) == null) {
                    //no se ha encontrado la abadía en la hm.
                } else if ((Integer) hmAbadias.get(new Integer(edificio.getIdAbadia())) == 1) {
                    //significa que ha pagado los costes de mantenimiento
                    //por lo tanto aumentamos el estado del edificio en los mismos puntos
                    //en los que se le han restado en el proceso anterior
                    //recuperamos los puntos de deterioro que corresponden al tipo de edificio y nivel para restablecerlos
                    try {
                        iDeterioro = hmDatosDesgaste.get(String.valueOf(edificio.getIdTipoEdificio()).concat(String.valueOf(edificio.getNivel())));
                    } catch (NullPointerException e) {
                        iDeterioro = 0;
                    }
                    //una vez obtenido el deterioro sufrido por el edificio, verificamos la existencia del libro 323 - Renovo Aedificium que servirá para aumentar
                    //los puntos de recuperación de los edificios en los siguientes terminos:
                    //nivel 1 - iDeterioro +10%
                    //nivel 2 - iDeterioro +20%
                    //nivel 3 - iDeterioro +30%
                    //nivel 4 - iDeterioro +40%
                    // en este caso, los puntos de deterioro serán los que utilizamos para rehabilitar / reparar los edificios subiendo los puntos de estado
                    libro = oLibrosAD.recuperaLibroTipo(Constantes.LIBRO_TIPO_RENOVO, edificio.getIdAbadia());
                    if (libro != null) {
                        switch (libro.getNivel()) {
                            case 1:
                                iDeterioro = iDeterioro + iDeterioro * 10 / 100;
                                break;
                            case 2:
                                iDeterioro = iDeterioro + iDeterioro * 20 / 100;
                                break;
                            case 3:
                                iDeterioro = iDeterioro + iDeterioro * 30 / 100;
                                break;
                            case 4:
                                iDeterioro = iDeterioro + iDeterioro * 40 / 100;
                                break;
                        }
                    }

                    //si el edificio tiene contratado un mantenimiento de nivel 3 (maximo)
                    if (edificio.getMantenimiento() == 3) {
                        //restablecemos integramente los puntos de desgaste + 2...
                        oEdificioAD.subirEstadoEdificio(edificio.getIdEdificio(), iDeterioro + 2);
                    } else if (edificio.getMantenimiento() == 2) {
                        //restablecemos integramente los puntos de desgaste
                        oEdificioAD.subirEstadoEdificio(edificio.getIdEdificio(), iDeterioro);
                    } else if (edificio.getMantenimiento() == 1) {
                        //restablecemos la mitad de los puntos de desgaste
                        oEdificioAD.subirEstadoEdificio(edificio.getIdEdificio(), iDeterioro / 2);
                    }
                }
            }

            //regularizar estado de los edificios. si el estado > 100 ponemos 100
            oEdificioAD.regularizarEstadoEdificios();
            oProcesosAD.addLog("+ Finalizado SubProceso Gestión Reparación Edificios", 0);
        } catch (Exception e) {
            throw new AbadiaSQLException("ERROR: gestionaReparacionEdificios = " + e, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public void devolverCosteMantenimiento() throws AbadiaException {

        String sTrace = this.getClass() + ".devolverCosteMantenimiento()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adEdificio oEdificioAD;
        adProcesos oProcesosAD;
        adRecurso oRecursoAD;

        ArrayList<EdificioMantenimiento> alAbadias;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);

            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando SubProceso Devolución Coste de mantenimiento", 0);

            oRecursoAD = new adRecurso(con);
            oEdificioAD = new adEdificio(con);
            alAbadias = oEdificioAD.recuperarAbadiasMantenimientoEdificios();

            for (EdificioMantenimiento abadia : alAbadias) {
                if (abadia.getPrecioMantenimiento() > 0) {
                    oRecursoAD.sumarRecurso(Constantes.RECURSOS_ORO, abadia.getIdAbadia(), abadia.getPrecioMantenimiento());
                }
            }
            oProcesosAD.addLog("+ Finalizado SubProceso Devolución Coste de mantenimiento", 0);

        } catch (Exception e) {
            throw new AbadiaSQLException("ERROR: devolverCosteMantenimiento = " + e, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    /**
     * GEstiona el desgaste de los edificios en función del tipo y el nivel
     * En caso de que el deterioro del edificio baje de un porentaje determinado
     * el edificio disminuirá de nivel.
     *
     * @throws AbadiaException excepción general
     */
    public void gestionDesgasteEdificiosUndo() throws AbadiaException {

        String sTrace = this.getClass() + ".gestionDesgasteEdificiosUndo()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adEdificio oEdificioAD;
        adProcesos oProcesosAD;
        adLibros oLibrosAD;
        adLiterales oLiteralesAD;
        adMensajes oMensajesAD;

        HashMap<String, Double> hmDatosDesgaste;
        ArrayList<EdificioMantenimiento> alEdificios;
        ArrayList<Mensajes> alMensajes = new ArrayList<Mensajes>();

        Libro libro;
        double iDeterioro;
        int idLastIdioma = 0;
        String msgEdificioBajaNivel = "";
        String msgTmp;


        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);

            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando SubProceso Gestión Desgaste Edificios Undo", 0);
            oLiteralesAD = new adLiterales(con);
            //cargar hashmap con la configuración de desgaste de los edificios
            oLibrosAD = new adLibros(con);
            oEdificioAD = new adEdificio(con);
            hmDatosDesgaste = oEdificioAD.recuperarParametrosDesgaste();
            //cargar lista arraylista de edificios de nivel > 2 para ser procesados
            alEdificios = oEdificioAD.recuperarEdificiosMantenimiento();

            for (EdificioMantenimiento edificio : alEdificios) {
                //gestión de literales...
                if (idLastIdioma != edificio.getIdIdioma()) {
                    idLastIdioma = edificio.getIdIdioma();
                    //utilsAD = new adUtils(Constantes.DB_CONEXION_PROCESS);
                    msgEdificioBajaNivel = oLiteralesAD.getLiteralStatic(70004, idLastIdioma);  // El nivel del edificio: %s se ha restablecido a %d
                    //utilsAD.finalize();
                }

                //recuperamos el desgaste asodiado al edificio..
                try {
                    iDeterioro = hmDatosDesgaste.get(String.valueOf(edificio.getIdTipoEdificio()).concat(String.valueOf(edificio.getNivel())));
                } catch (NullPointerException e) {
                    iDeterioro = 0;
                }

                //aquí debemos incluir el impacto del libro 324 Conservator aedificium del siguiente modo
                //nivel 1 - reduce en un 20 por ciento el desgaste que sufre el edificio
                //nivel 2 - reduce en un 40 por ciento el desgaste que sufre el edificio
                //nivel 3 - reduce en un 60 por ciento el desgaste que sufre el edificio
                //nivel 4 - reduce en un 80 por ciento el desgaste que sufre el edificio

                libro = oLibrosAD.recuperaLibroTipo(Constantes.LIBRO_TIPO_CONSERVATOR, edificio.getIdAbadia());
                if (libro != null) {
                    switch (libro.getNivel()) {
                        case 1:
                            iDeterioro = iDeterioro + iDeterioro * 20 / 100;
                            break;
                        case 2:
                            iDeterioro = iDeterioro + iDeterioro * 40 / 100;
                            break;
                        case 3:
                            iDeterioro = iDeterioro + iDeterioro * 60 / 100;
                            break;
                        case 4:
                            iDeterioro = iDeterioro + iDeterioro * 80 / 100;
                            break;
                    }
                }

                //si el edificio alcanza más de 100 puntos de estado
                if (edificio.getEstado() + iDeterioro > 100) {
                    //verificamos si existe un nivel superior para el edificio en cuestión
                    if (oEdificioAD.existeSiguienteNivel(edificio.getIdTipoEdificio(), edificio.getNivel())) {
                        //si el edificio tiene nivel 0 o nivel 1 necesitamos de la existencia del constructio
                        //para subir nivel...
                        if (edificio.getNivel() < 2) {
                            oEdificioAD.subirNivel(edificio.getIdEdificio());
                            //generamos un mensaje indicando que el edificio ha subido de nivel....
                            oEdificioAD.asignarEstadoEdificio(edificio.getIdEdificio(), 80);
                            //eliminar posible registro de la tabla edificio_construcción
                            oEdificioAD.eliminarEdificioConstruccion(edificio.getIdAbadia(), edificio.getIdTipoEdificio());
                            msgTmp = ProcesosUtils.Format(msgEdificioBajaNivel, oEdificioAD.recuperarNombreEdificio(edificio.getIdEdificio(), edificio.getIdIdioma()));
                            msgTmp = ProcesosUtils.Format(msgTmp, edificio.getNivel() + 1);
                            alMensajes.add(new Mensajes(edificio.getIdAbadia(), -1, msgTmp, 0));
                        } else {
                            //veficamos si dispone del Constructio para el nivel al que se dirige el edificio
                            libro = oLibrosAD.recuperaLibroTipo(Constantes.LIBRO_TIPO_CONTRUCCION, edificio.getIdAbadia());

                            //si el libro existe...
                            if (libro != null) {
                                //Si el nivel del libro = 1 y el nivel al que se debe subir el edifico es 3 o el libro es de nivel > 1 y el nivel objetivo
                                //del edificio es >= 3...
                                if (((libro.getNivel() == 1) && (edificio.getNivel() + 1 == 3)) || ((libro.getNivel() > 1) && (edificio.getNivel() + 1 >= 3))) {
                                    oEdificioAD.subirNivel(edificio.getIdEdificio());
                                    oEdificioAD.asignarEstadoEdificio(edificio.getIdEdificio(), 80);
                                    //eliminar posible registro de la tabla edificio_construcción
                                    oEdificioAD.eliminarEdificioConstruccion(edificio.getIdAbadia(), edificio.getIdTipoEdificio());
                                    //generamos un mensaje indicando que el edificio ha subido de nivel....
                                    msgTmp = ProcesosUtils.Format(msgEdificioBajaNivel, oEdificioAD.recuperarNombreEdificio(edificio.getIdEdificio(), edificio.getIdIdioma()));
                                    msgTmp = ProcesosUtils.Format(msgTmp, edificio.getNivel() + 1);
                                    alMensajes.add(new Mensajes(edificio.getIdAbadia(), -1, msgTmp, 0));
                                }
                            }

                        }
                    }

                } else {
                    //Suma los puntos de desgaste del edificio
                    oEdificioAD.subirEstadoEdificio(edificio.getIdEdificio(), iDeterioro);
                }
            }
            //creamos los mensajes
            oMensajesAD = new adMensajes(con);
            oMensajesAD.crearMensajes(alMensajes);
            oProcesosAD.addLog("+ Finalizado SubProceso Gestión Desgaste Edificios Undo", 0);

        } catch (Exception e) {
            throw new AbadiaSQLException("ERROR: gestionDesgasteEdificiosUndo = " + e, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    /**
     * Gestiona el desgaste de los recursos almacenados en edificios por encima de su capacidad
     * Resta puntos de estado a los recursos en función del grado de saturación del edificio
     * Elimina los recursos cuyo estado está por debajo de 0
     *
     * @throws AbadiaException Excepción general
     */
    public void procesarDesgasteRecursosPorSaturacion() throws AbadiaException {
        String sTrace = this.getClass() + ".procesarDesgasteRecursosPorSaturacion()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adEdificio oEdificioAD;
        adLibros oLibroAD;
        adProcesos oProcesosAD;
        adLiterales oLiteralesAD;
        adRecurso oRecursoAD;
        adMensajes oMensajesAD;

        ArrayList<EdificioSaturado> alEdificios;
        ArrayList<Mensajes> alMensajes = new ArrayList<Mensajes>();

        Libro libro;
        int idIdioma = 0;
        int iIncrementoCapacidadEdificio;
        double dDesgaste;
        double dCantidad;
        double dVolumenExcedente;
        String msgEdificioSaturado = "";
        String msgTmp;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);

            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando SubProceso Gestión Desgaste Recursos por saturación de edificios", 0);

            oRecursoAD = new adRecurso(con);
            oLiteralesAD = new adLiterales(con);
            oLibroAD = new adLibros(con);
            oEdificioAD = new adEdificio(con);

            alEdificios = oEdificioAD.recuperarEdificiosSaturadosRecursos();

            for (EdificioSaturado edificio : alEdificios) {

                //si detectamos un cambio de idioma, recargamos los literales....
                if (idIdioma != edificio.getIdIdioma()) {
                    idIdioma = edificio.getIdIdioma();
                    msgEdificioSaturado = oLiteralesAD.getLiteralStatic(70010, edificio.getIdIdioma());  //Tu {0} supera en {1} metros cúbicos su capacidad de almacenamiento, su contenido puede deteriorarse
                }

                //recuperamos un posible libro que incremente la capacidad del edificio

                libro = oLibroAD.recuperaLibroTipo(300 + edificio.getIdTipoEdificio(), edificio.getIdAbadia());
                //si el libro existe
                if (libro != null) {
                    //incrementamos la capacidad de almacenamiento del edificio
                    iIncrementoCapacidadEdificio = Utilidades.recuperarIncrementoEspacioEdificioPorLibro(edificio.getIdTipoEdificio(), libro.getNivel(), edificio.getNivel());
                    edificio.setAlmacenamientoMaximo(edificio.getAlmacenamientoMaximo() + iIncrementoCapacidadEdificio);
                }
                //si el edificio aun supera su capacidad de almacenamiento...
                if (edificio.getAlmacenamientoMaximo() < edificio.getAlmacenamientoActual()) {
                    dVolumenExcedente = edificio.getAlmacenamientoActual() - edificio.getAlmacenamientoMaximo();
                    if (dVolumenExcedente > edificio.getAlmacenamientoMaximo()) {
                        dCantidad = 2;
                    } else if (dVolumenExcedente * 2 > edificio.getAlmacenamientoMaximo()) {
                        dCantidad = 4;
                    } else if (dVolumenExcedente * 4 > edificio.getAlmacenamientoMaximo()) {
                        dCantidad = 6;
                    } else dCantidad = 10;

                    dDesgaste = dVolumenExcedente / edificio.getAlmacenamientoMaximo();
                    //si el desgaste es superior a 5, lo dejamos en 5 para evitar catástrofes
                    if (dDesgaste > 5) dDesgaste = 5;


                    msgTmp = ProcesosUtils.Format(msgEdificioSaturado, edificio.getNombre(), Utilidades.redondear(dVolumenExcedente));
                    //decrementamos el estado de los recursos del edificio
                    oRecursoAD.decrementarEstadoRecursoPorEdificio(edificio.getIdAbadia(), edificio.getIdTipoEdificio(), (int) dDesgaste);
                    oRecursoAD.decrementarCantidadRecursoPorEdificio(edificio.getIdAbadia(), edificio.getIdTipoEdificio(), (int) dCantidad);
                    alMensajes.add(new Mensajes(edificio.getIdAbadia(), -1, msgTmp, 0));
                }
            }
            //eliminar los recursos que tengan el estado igual o menor que 0.
            oRecursoAD.eliminarRecursosMantenimiento();
            //creamos los mensajes
            oMensajesAD = new adMensajes(con);
            oMensajesAD.crearMensajes(alMensajes);
            oProcesosAD.addLog("+ Finalizado Gestión Desgaste Recursos por saturación de edificios", 0);

        } catch (Exception e) {
            throw new AbadiaSQLException("ERROR: procesarDesgasteRecursosPorSaturacion = " + e, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

}
