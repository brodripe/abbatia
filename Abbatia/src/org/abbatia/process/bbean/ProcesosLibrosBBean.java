package org.abbatia.process.bbean;

import org.abbatia.adbean.*;
import org.abbatia.bean.Libro;
import org.abbatia.bean.LibroProceso;
import org.abbatia.bean.Mensajes;
import org.abbatia.bean.MonjeProceso;
import org.abbatia.core.CoreTiempo;
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
import java.util.Iterator;

public class ProcesosLibrosBBean extends ProcesosUtils {
    private static Logger log = Logger.getLogger(ProcesosLibrosBBean.class.getName());

    public ProcesosLibrosBBean() throws AbadiaDBConnectionException {
        super();
    }

    public void copiar(int idPeriodo) throws AbadiaException {
        String sTrace = this.getClass() + ".copiar()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adHabilidades oHabilidadesAD;
        adRecurso oRecursosAD;
        adActividad oActividadAD;
        adLibros oLibroAD;
        adProcesos oProcesosAD;
        adLiterales oLiteralesAD;
        adMonje oMonjeAD;
        adViajar oViajarAD;
        adMensajes oMensajesAD;

        ArrayList<Libro> alLibros;
        ArrayList<Mensajes> alMensajes;

        int DESTREZA = 0;
        int FE = 3;
        int TALENTO = 2;
        int IDIOMA = 1;

        int last_idioma = 0;
        int idioma;
        double resto;
        double deterioro;
        short estadoLibro;
        int recurso_pergamino;
        int recurso_pluma;
        int recurso_tinta;
        String desc_pergamino = "";
        String desc_tinta = "";
        String desc_pluma = "";
        String nombre_libro;

        String msg_10120 = "";
        String msg_10125 = "";
        String msg_10126 = "";
        String msg_10127 = "";
        String msg_10128 = "";
        String msg_10129 = "";
        String msg;

        int habilidades[];
        double habilidad;
        int destreza_plumas;
        double paginasCopiadas;
        HashMap<Integer, Double> recurso;


        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);

            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando Proceso copiar_libros", 0);

            oLiteralesAD = new adLiterales(con);
            oLibroAD = new adLibros(con);
            oRecursosAD = new adRecurso(con);
            oHabilidadesAD = new adHabilidades(con);
            oActividadAD = new adActividad(con);
            oMonjeAD = new adMonje(con);
            oViajarAD = new adViajar(con);

            //recuperamos el array de libros a procesar
            alLibros = oLibroAD.recuperarLibrosProceso(idPeriodo);

            alMensajes = new ArrayList<Mensajes>();

            //      Libro libroOrigen = null;

            for (Libro libro : alLibros) {

                //si se ha cambiado de idioma, preinformamos los mensajes en el idioma correspondiente
                if (last_idioma != libro.getIdIdioma_usuario()) {
                    last_idioma = libro.getIdIdioma_usuario();

                    msg_10120 = oLiteralesAD.getLiteralStatic(10120, last_idioma);
                    msg_10125 = oLiteralesAD.getLiteralStatic(10125, last_idioma);
                    msg_10126 = oLiteralesAD.getLiteralStatic(10126, last_idioma);
                    msg_10127 = oLiteralesAD.getLiteralStatic(10127, last_idioma);
                    msg_10128 = oLiteralesAD.getLiteralStatic(10128, last_idioma);
                    msg_10129 = oLiteralesAD.getLiteralStatic(10129, last_idioma); //Alguno de los libros que estás copiando se encuentra "En restauración", la copia está parada.

                    desc_pergamino = oRecursosAD.recuperarDescripcionRecurso(Constantes.RECURSOS_PERGAMINOS, last_idioma);
                    desc_pluma = oRecursosAD.recuperarDescripcionRecurso(Constantes.RECURSOS_PLUMAS, last_idioma);
                    desc_tinta = oRecursosAD.recuperarDescripcionRecurso(Constantes.RECURSOS_TINTA, last_idioma);
                }

                //recuperamos el nombre del libro para el idioma en curso...
                //oLibroAD = new adLibros(Constantes.DB_CONEXION_PROCESS);
                nombre_libro = oLibroAD.recuperarNombreLibro(libro.getIdLibroTipo(), libro.getIdIdioma_usuario());
                //oLibroAD.finalize();

                //recuperamos las habilidades del monje en cuestión

                habilidades = oHabilidadesAD.recuperarHabilidadesCopiar(libro.getIdMonje());

                recurso = oRecursosAD.recuperarRecursos(libro.getIdAbadia_copia());


                if (recurso.get(Constantes.RECURSOS_PERGAMINOS) != null && recurso.get(Constantes.RECURSOS_PERGAMINOS) > 1) {
                    recurso_pergamino = (recurso.get(Constantes.RECURSOS_PERGAMINOS)).intValue();
                } else {
                    //mensaje de que falta el recurso Pergamino.
                    msg = Format(msg_10120, nombre_libro, desc_pergamino);
                    alMensajes.add(new Mensajes(libro.getIdAbadia(), libro.getIdMonje(), msg, 1));
                    recurso_pergamino = 0;
                }
                if ((recurso.get(Constantes.RECURSOS_PLUMAS) != null) && (recurso.get(Constantes.RECURSOS_PLUMAS)) > 1) {
                    recurso_pluma = (recurso.get(Constantes.RECURSOS_PLUMAS)).intValue();
                } else {
                    //mensaje de que falta el recurso Pluma.
                    msg = Format(msg_10120, nombre_libro, desc_pluma);
                    alMensajes.add(new Mensajes(libro.getIdAbadia(), libro.getIdMonje(), msg, 1));
                    recurso_pluma = 0;
                }
                if (recurso.get(Constantes.RECURSOS_TINTA) != null && (recurso.get(Constantes.RECURSOS_TINTA)) > 1) {
                    recurso_tinta = (recurso.get(Constantes.RECURSOS_TINTA)).intValue();
                } else {
                    //mensaje de que falta el recurso Tinta.
                    msg = Format(msg_10120, nombre_libro, desc_tinta);
                    alMensajes.add(new Mensajes(libro.getIdAbadia(), libro.getIdMonje(), msg, 1));
                    recurso_tinta = 0;
                }

                //si dispone de todos los recursos necesarios..
                if (recurso_tinta > 0 && recurso_pluma > 0 && recurso_pergamino > 0) {
                    //si el idioma de la copia coincide con el del libro original...
                    //si es en latin es como si fuera su propio idioma.
                    if (libro.getIdIdioma() == libro.getIdIdioma_origen() | libro.getIdIdioma_origen() == Constantes.IDIOMA_LATIN) {
                        idioma = 100;
                        //de lo contrario
                    } else {
                        idioma = habilidades[IDIOMA];
                    }

                    paginasCopiadas = (((((double) habilidades[DESTREZA] / 300) + ((double) habilidades[TALENTO] / 300) + ((double) idioma / 300)) / 100) * (double) habilidades[FE]) / 2;
                    if (paginasCopiadas < 0) paginasCopiadas = 0;
                    //en este punto debemos recuperar el estado en el que se encuentra el libro que estamos copiando.
                    estadoLibro = oLibroAD.recuperarEstadoLibro(libro.getIdLibro_origen());
                    //si está en estado En restauración
                    if (estadoLibro == Constantes.ESTADO_LIBRO_RESTAURANDO) {
                        // No se copia y se genera mensaje avisando de la situación.
                        msg = Format(msg_10129, nombre_libro);
                        alMensajes.add(new Mensajes(libro.getIdAbadia(), -1, msg, 1));
                    } else {
                        //si está en estado completo => seguimos con la copia e incrementamos el deterioro...
                        //si está en estado deteriorado => seguimos con la copia e incrementamos el deterioro...
                        //llamada al metodo que gestiona el incremento del deterioro a partir del número de páginas copiadas.
                        //recuperamos los puntos de deterioro correspondientes al libro_origen
                        deterioro = oLibroAD.recuperarDeterioroCorrespondientePorLibro(libro.getIdLibro_origen());
                        //al deterioro correspondiente al libro le restamos los puntos de habilidad del monje
                        //contando con que un monje con muchas habilidades deteriora menos un libro al hacer la copia.
                        habilidad = ((((habilidades[DESTREZA] * habilidades[FE]) / 100) + ((habilidades[TALENTO] * habilidades[FE]) / 100) / 2) * 0.0001);
                        oLibroAD.incrementarDeterioroLibroPorCopia(libro.getIdLibro_origen(), deterioro - habilidad);

                        //Controlar que si es una nueva pagina, restar un pergamino.
                        //si la suma de las paginas copiadas, hace una pagina nueva, restaremos un pergamino y
                        //si el resto de la division entre 2, es diferente de 0! (cada dos paginas un pergamino).
                        //si es la primera pagina tambien restamos un pergamino
                        resto = ((int) (libro.getNumPaginasCopiadas() + paginasCopiadas)) % 2;
                        if (((int) libro.getNumPaginasCopiadas() < (int) (libro.getNumPaginasCopiadas() + paginasCopiadas) && resto == 0) || ((libro.getNumPaginasCopiadas() == 0 && paginasCopiadas > 0))) {
                            oRecursosAD.restarRecurso(Constantes.RECURSOS_PERGAMINOS, libro.getIdAbadia_copia(), 1);
                        }

                        //dependiendo de la destreza del monje, perdera mas plumas o no.
                        destreza_plumas = habilidades[DESTREZA] / 10;
                        if (Utilidades.Random(1, destreza_plumas) == 1) {
                            oRecursosAD.restarRecurso(Constantes.RECURSOS_PLUMAS, libro.getIdAbadia_copia(), 1);
                        }
                        //aqui tenemos que restar paginasCopiadas a la tinta. (se pierde una unidad de tinta por pagina copiada).
                        if ((int) libro.getNumPaginasCopiadas() < (int) (libro.getNumPaginasCopiadas() + paginasCopiadas)) {
                            oRecursosAD.restarRecurso(Constantes.RECURSOS_TINTA, libro.getIdAbadia_copia(), 1);
                        }

                        libro.setNumPaginasCopiadas(libro.getNumPaginasCopiadas() + paginasCopiadas);
                        libro.setEstado(Constantes.ESTADO_LIBRO_COPIANDOSE);

                        // Acumular
                        oActividadAD.incrementaAcumulado(libro.getIdMonje(), idPeriodo, Constantes.TAREA_COPIAR, paginasCopiadas);

                        //literal 10127 con el numero de paginas copiadas, he puesto %s porque seran decimales....
                        msg = Format(msg_10127, Utilidades.redondear(paginasCopiadas), nombre_libro);

                        //crear mensaje por actividad
                        oProcesosAD.setActividadMens(libro.getIdMonje(), Constantes.TAREA_COPIAR, idPeriodo, msg, paginasCopiadas);

                        //si hemos terminado el libro...
                        if (libro.getNumPaginasCopiadas() >= libro.getNumPaginas()) {
                            //oLibroAD = new adLibros(Constantes.DB_CONEXION_PROCESS);
                            double precioCopia = oLibroAD.recuperarPrecioCopiaLibro(libro.getIdLibro_origen());
                            //oLibroAD.finalize();
                            //si el la copia se esta haciendo en la abbatia local..
                            if (libro.getIdAbadia() == libro.getIdAbadia_copia()) {
                                //actualizamos el estado del libro a "sin encuadernar"
                                libro.setEstado(Constantes.ESTADO_LIBRO_SIN_ENCUADERNAR);

                                //por último, eliminamos los bloqueos de registros libro_tarea del monje
                                oLibroAD.eliminarTareasCopiaPorMonje(libro.getIdMonje());

                                //desbloqueamos las actividades de copia...
                                oMonjeAD.desbloquarActividadesCopia(libro.getIdMonje());
                                //literal mensaje 10125
                                msg = Format(msg_10125, nombre_libro);
                                alMensajes.add(new Mensajes(libro.getIdAbadia(), libro.getIdMonje(), msg, 1));

                            } else //si la copia se ha completado en otra abbatia
                            {
                                //se paga lo acordado por la copia del libro...
                                //recuperamos el precio de copia del libro

                                //le restamos lo pagado anteriormente
                                oRecursosAD.sumarRecurso(Constantes.RECURSOS_ORO, libro.getIdAbadia_copia(), precioCopia - libro.getPrecioCopia());

                                //actualizamos el estado a viajando para iniciar con el viaje de vuelta a la abbatia
                                libro.setEstado(Constantes.ESTADO_LIBRO_VIAJANDO);

                                oViajarAD.actualizarFechaPartidaOrigen(libro.getIdMonje());
                                //si el monje esta deplazado..
                                //literal mensaje 10126
                                msg = Format(msg_10126, nombre_libro);
                                alMensajes.add(new Mensajes(libro.getIdAbadia(), libro.getIdMonje(), msg, 1));

                                //tenemos que actualizar el campo dummy del viaje con el identificador del libro
                                oViajarAD.actualizarDummy(libro.getIdMonje(), String.valueOf(libro.getIdLibro()));

                                //por último, eliminamos los bloqueos de registros libro_tarea del monje
                                oLibroAD.eliminarTareasCopiaPorMonje(libro.getIdMonje());

                                //desbloqueamos las actividades de copia...
                                oMonjeAD.desbloquarActividadesCopia(libro.getIdMonje());
                            }

                            libro.setPrecioCopia(precioCopia);
                            libro.setNumPaginasCopiadas(libro.getNumPaginas());
                            libro.setFecha_creacion(CoreTiempo.getTiempoAbadiaString());

                            //si el monje está desplazado, debemos iniciar el viaje de vuelta a su abbatia
                        }
                        //actualizamos el registro en base de datos.
                        //log.info(libro.toString());
                        oLibroAD.actualizarLibro(libro);
                    }
                } else//fin validación de recursos
                {
                    alMensajes.add(new Mensajes(libro.getIdAbadia_copia(), -1, msg_10128, 1));
                }
            }

            oMensajesAD = new adMensajes(con);
            oMensajesAD.crearMensajes(alMensajes);

            oProcesosAD.addLog("+ Finalizado Proceso copiar_libros", 0);

        } catch (Exception e) {
            throw new AbadiaSQLException(sTrace + "ERROR: ProcesosLibrosBBean.copiar", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }


    }

    /**
     * Gestión de desgaste de los libros por el paso del tiempo....
     *
     * @throws AbadiaException Excepción general
     */
    public void desgastarLibros() throws AbadiaException {

        String sTrace = this.getClass() + ".desgastarLibros()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adProcesos oProcesosAD;
        adUtils oUtilsAD;
        adLibros oLibrosAD;
        adLiterales oLiteralesAD;
        adViajar oViajarAD;
        adMonje oMonjeAD;
        adMensajes oMensajesAD;

        String sSQLIncDesgaste = "update libro, libro_nivel set libro.desgaste = libro.desgaste + libro_nivel.desgaste where libro.nivel = libro_nivel.nivel and libro.TIPO_LIBROID and libro.ESTADO in (" + Constantes.ESTADO_LIBRO_COMPLETO + "," + Constantes.ESTADO_LIBRO_DETERIORADO + ", " + Constantes.ESTADO_LIBRO_RESTAURANDO + ") and libro.ABADIAID not in (select a.abadiaid from abadia a, usuario u where a.usuarioid = u.usuarioid and u.abadia_congelada = 1)";
        String sSQLCambioEstado = "update libro set estado = " + Constantes.ESTADO_LIBRO_DETERIORADO + " where desgaste > 50 and estado = " + Constantes.ESTADO_LIBRO_COMPLETO;
        String sSQLEliminarPorDeterioro = "delete from libro where desgaste >= 100";

        ArrayList<Mensajes> alMensajes = new ArrayList<Mensajes>();
        ArrayList<MonjeProceso> alMonjes;
        Iterator<MonjeProceso> itMonjes;
        ArrayList<LibroProceso> alLibros;
        String sMsgLibroDeteriorado = ""; //12202
        String sMsgAvisoDeterioro = "";   //12203
        String sMsgAvisoEliminacion = ""; //12204
        String sMsgTmp;
        int idIdioma = -1;

        MonjeProceso monje;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);

            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando Proceso Deterioro Libros", 0);

            //actualiza el deterioro de los libros en función de su nivel y tipo
            oUtilsAD = new adUtils(con);
            oUtilsAD.execSQL(sSQLIncDesgaste);
            oLiteralesAD = new adLiterales(con);
            oViajarAD = new adViajar(con);
            oMonjeAD = new adMonje(con);
            //recuperar los libros que están Completos (2) y tienen un desgaste superior a 50 para generar mensaje indicando que pasa al estado: Deteriorado
            oLibrosAD = new adLibros(con);
            alLibros = oLibrosAD.recuperarLibrosCompletosDeteriorados();

            for (LibroProceso libro : alLibros) {
                //si hay cambio de idioma...
                if (libro.getIdIdioma() != idIdioma) {
                    //inicializamos el indicador de idioma
                    idIdioma = libro.getIdIdioma();
                    //recuperamos el literal del mensaje a mostrar.
                    sMsgLibroDeteriorado = oLiteralesAD.getLiteralStatic(12202, idIdioma); // tu libro %s de nivel %s ha pasado a estado: deteriorado
                }
                //montamos el mensaje con los datos propios del libro / abbatia
                sMsgTmp = Format(sMsgLibroDeteriorado, libro.getNombre(), String.valueOf(libro.getNivel()));
                alMensajes.add(new Mensajes(libro.getIdAbadia(), -1, sMsgTmp, 1));
            }

            //actualizar el estado de los libros completos con desgaste superior a 50 con el estado: Deteriorado (para que no puedan copiarse)
            oUtilsAD.execSQL(sSQLCambioEstado);

            //recuperar libros con deterioro 20, 30 y 40
            idIdioma = -1;
            alLibros = oLibrosAD.recuperarLibrosPorDeterioro(20, 30, 40);
            for (LibroProceso libro : alLibros) {
                //si hay cambio de idioma...
                if (libro.getIdIdioma() != idIdioma) {
                    //inicializamos el indicador de idioma
                    idIdioma = libro.getIdIdioma();
                    //recuperamos el literal del mensaje a mostrar.
                    sMsgAvisoDeterioro = oLiteralesAD.getLiteralStatic(12203, idIdioma); // Los libros de tu biblioteca se están deteriorando...
                }
                //montamos el mensaje con los datos propios del libro / abbatia
                alMensajes.add(new Mensajes(libro.getIdAbadia(), -1, sMsgAvisoDeterioro, 1));
            }

            //recuperar los libros que serán eliminados por alcanzar un deterioro de 100 puntos
            alLibros = oLibrosAD.recuperarLibrosPorDeterioroMayorOIgualQue(100);
            //sobre los libros recuperados debemos hacer las siguientes operaciones..
            for (LibroProceso libro : alLibros) {
                //si hay cambio de idioma...
                if (libro.getIdIdioma() != idIdioma) {
                    //inicializamos el indicador de idioma
                    idIdioma = libro.getIdIdioma();
                    //recuperamos el literal del mensaje a mostrar.
                    sMsgAvisoEliminacion = oLiteralesAD.getLiteralStatic(12204, idIdioma); // El libro %s de nivel %s ha desaparecido como causa de su tederioro
                }
                //recuperamos los posibles monjes copistas
                alMonjes = oLibrosAD.recuperarMonjesCopistasPorIDLibro(libro.getIdLibro());
                //verificamos si existen monjes copiando ese libro o subiendo su nivel...
                if (!alMonjes.isEmpty()) {
                    itMonjes = alMonjes.iterator();
                    //en caso de existir monjes copiando o subiendo nivel
                    while (itMonjes.hasNext()) {
                        monje = itMonjes.next();
                        //verificamos el los copistas pertenecen a la propia abadía o están de viaje
                        //si el monje copista pertenece a otra abadía...
                        if (monje.getIdAbadia() != libro.getIdAbadia()) {
                            oViajarAD.forzarVueltaProceso(monje, Constantes.FORZAR_RETORNO_LIBRO_DETERIORADO);
                        } else //representa que el monje copista pertenece a la misma abadía.
                        {
                            //eliminamos los bloqueos de las tareas
                            oLibrosAD.eliminarTareasCopiaPorMonje(monje.getIdMonje());
                            //desbloqueamos las actividades de copia...
                            oMonjeAD.desbloquarActividadesCopia(monje.getIdMonje());
                        }
                        //
                    }
                }
                //montamos el mensaje con los datos propios del libro / abbatia
                sMsgTmp = Format(sMsgAvisoEliminacion, libro.getNombre(), String.valueOf(libro.getNivel()));
                alMensajes.add(new Mensajes(libro.getIdAbadia(), -1, sMsgTmp, 1));
            }

            //generar mensajes
            oMensajesAD = new adMensajes(con);
            oMensajesAD.crearMensajes(alMensajes);

            //eliminar libros con deterioro superior a 100
            oUtilsAD.execSQL(sSQLEliminarPorDeterioro);

            oProcesosAD.addLog("+ Finalizado Proceso Deterioro Libros", 0);

        } catch (Exception e) {
            throw new AbadiaSQLException(sTrace + " ERROR: Proceso Deterioro Libros", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    /**
     * Gesciona la restauración de los libros por parte de los monjes en un periodo determinado.
     *
     * @param periodo periodo procesado
     * @throws AbadiaException Exception general
     */
    public void restaurarLibros(int periodo) throws AbadiaException {

        String sTrace = this.getClass() + ".restaurarLibros()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);


        adUtils oUtilsAD;
        adHabilidades oHabilidadesAD;
        adActividad oActividadAD;
        adProcesos oProcesosAD;
        adLiterales oLiteralesAD;
        adLibros oLibrosAD;

        int DESTREZA = 0;
        int FE = 3;
        int TALENTO = 2;

        double habilidades[];
        ArrayList<Integer> idLibros;

        int last_idiomaid = 0;
        double valor;
        double valorPorLibro;
        String msg;
        String msg1 = "";
        String msg2 = "";
        ArrayList<MonjeProceso> alMonjes;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);

            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando SUBProceso Restaurar Libros", 0);
            alMonjes = oProcesosAD.recuperarMonjesActividadPeriodo(periodo, Constantes.TAREA_RESTAURAR_LIBROS);

            oLiteralesAD = new adLiterales(con);
            oHabilidadesAD = new adHabilidades(con);
            oLibrosAD = new adLibros(con);
            oActividadAD = new adActividad(con);
            oUtilsAD = new adUtils(con);

            for (MonjeProceso monje : alMonjes) {
                if (last_idiomaid != monje.getIdIdioma()) {
                    last_idiomaid = monje.getIdIdioma();
                    msg1 = oLiteralesAD.getLiteralStatic(10019, last_idiomaid);  // Ha restaurado %d puntos de los libros deteriorados
                    msg2 = oLiteralesAD.getLiteralStatic(10022, last_idiomaid);  // No hay libros En Restauración
                }

                //recuperamos las habilidades del monje en cuestión
                habilidades = oHabilidadesAD.recuperarHabilidadesRestaurar(monje.getIdMonje());

                //calculamos los puntos de restauración de monje.
                valor = (((habilidades[DESTREZA] * habilidades[FE]) / 100 + (habilidades[TALENTO] * habilidades[FE]) / 100) / 2) * 0.007;

                //recuperamos un vector con los id de libros a restaurar en la abadía en la que está el monje.
                idLibros = oLibrosAD.recupearIDLibrosEnRestauracion(monje.getIdAbadia());

                if (idLibros.size() == 0) {
                    msg = msg2;
                } else {
                    //dividimos el valor por el número de libros a restaurar...
                    valorPorLibro = valor / idLibros.size();

                    //actualizamos los puntos de deterioro de los libros en restauración
                    oLibrosAD.reducirDeterioroLibro(idLibros, valorPorLibro);
                    oActividadAD.incrementaAcumulado(monje.getIdMonje(), periodo, Constantes.TAREA_RESTAURAR_LIBROS, valor);
                    msg = Format(msg1, valor);
                }
                oProcesosAD.setActividadMens(monje.getIdMonje(), Constantes.TAREA_RESTAURAR_LIBROS, periodo, msg, valor);
            }

            //ejecutamos un update masivo sobre todos los libros con un deterioro igual o inferior a 0 para cambiar su estado de "en restauración" a "completos"
            oUtilsAD.execSQL("update libro set estado = " + Constantes.ESTADO_LIBRO_COMPLETO + " where estado = " + Constantes.ESTADO_LIBRO_RESTAURANDO + " and desgaste <= 0");
            //fijamos a 0 los libros con desgaste por debajo de 0
            oUtilsAD.execSQL("update libro set desgaste = 0 where desgaste < 0");
            oProcesosAD.addLog("- Finalizando SUBProceso Restaurar Libros", 0);

        } catch (Exception e) {
            throw new AbadiaSQLException(sTrace + " ERROR: Proceso Restaurar Libros", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }
}

