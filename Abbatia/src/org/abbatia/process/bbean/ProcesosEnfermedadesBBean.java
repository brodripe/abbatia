package org.abbatia.process.bbean;

import org.abbatia.adbean.*;
import org.abbatia.bbean.MonjeBBean;
import org.abbatia.bean.Enfermedad;
import org.abbatia.bean.Mensajes;
import org.abbatia.bean.Monje;
import org.abbatia.bean.MonjeSalud;
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

/**
 * Created by Benjamín Rodríguez.
 * User: Benjamin
 * Date: 27-feb-2006
 * Time: 12:29:17
 */
public class ProcesosEnfermedadesBBean extends ProcesosUtils {

    private static Logger log = Logger.getLogger(ProcesosEnfermedadesBBean.class.getName());
    private ArrayList<Mensajes> mensajes = new ArrayList<Mensajes>();
    private HashMap htEnfermedades = null;

    public ProcesosEnfermedadesBBean() throws AbadiaDBConnectionException {
        super();
    }

    public void enfermar() throws AbadiaException {
        String sTrace = this.getClass() + ".enfermar()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);


        adProcesos oProcesosAD;
        adUtils oUtilsAD;
        adEnfermedad oEnfermedadAD;
        adLiterales oLiteralesAD;
        adMensajes oMensajesAD;

        ArrayList<Monje> alMonjes;

        int posibilidades;
        String msg1 = "";
        int last_abadiaid = 0, last_idiomaid = 0, tmp_idiomaid;

        boolean bEstaEnfermo = false;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);
            // Recuperar los monjes
            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando Proceso enfermedad. ", 0);
            alMonjes = oProcesosAD.recuperarMonjesParaEnfermar();
            oUtilsAD = new adUtils(con);
            oLiteralesAD = new adLiterales(con);
            //recuperar una tabla con las enfermedades de nivel 1 por carencia / exceso / propiedad - tiempo
            //para agilizar la inserción de las enfermedades de los monjes
            //hashtable indexada por propiedad y con valores exceso y defecto
            oEnfermedadAD = new adEnfermedad(con);
            htEnfermedades = oEnfermedadAD.recuperarTablaEnfermedades(1);

            for (Monje monje : alMonjes) {
                if (last_abadiaid != monje.getIdDeAbadia()) {
                    last_abadiaid = monje.getIdDeAbadia();
                    tmp_idiomaid = oUtilsAD.getIdiomaID(last_abadiaid);

                    if (last_idiomaid != tmp_idiomaid) {
                        last_idiomaid = tmp_idiomaid;
                        msg1 = oLiteralesAD.getLiteralStatic(10054, last_idiomaid);  // Esta enfermo
                    }
                }

                //evaluar la vejez como una enfermedad a parte

                //por cada propiedad calcularemos la probabilidad en base a la que figure en la enfermedad
                //asociada a la carencia / exceso.
                if (monje.getProteinas() <= Constantes.ENFERMEDAD_MINIMO_ACEPTABLE) {
                    posibilidades = Constantes.ENFERMEDAD_MINIMO_ACEPTABLE + 1 - monje.getProteinas() + 100 - monje.getSalud();
                    bEstaEnfermo = enferma(posibilidades, Constantes.ENFERMEDAD_DEFECTO, Constantes.PROPIEDAD_PROTEINAS, monje, msg1, con);
                }

                if ((monje.getHidratosCarbono() <= Constantes.ENFERMEDAD_MINIMO_ACEPTABLE) && !bEstaEnfermo) {
                    posibilidades = Constantes.ENFERMEDAD_MINIMO_ACEPTABLE + 1 - monje.getHidratosCarbono() + 100 - monje.getSalud();
                    bEstaEnfermo = enferma(posibilidades, Constantes.ENFERMEDAD_DEFECTO, Constantes.PROPIEDAD_HIDRATOS, monje, msg1, con);
                }

                if ((monje.getVitaminas() <= Constantes.ENFERMEDAD_MINIMO_ACEPTABLE) && !bEstaEnfermo) {
                    posibilidades = Constantes.ENFERMEDAD_MINIMO_ACEPTABLE + 1 - monje.getVitaminas() + 100 - monje.getSalud();
                    bEstaEnfermo = enferma(posibilidades, Constantes.ENFERMEDAD_DEFECTO, Constantes.PROPIEDAD_VITAMINAS, monje, msg1, con);
                }

                if ((monje.getLipidos() <= Constantes.ENFERMEDAD_MINIMO_ACEPTABLE) && !bEstaEnfermo) {
                    posibilidades = Constantes.ENFERMEDAD_MINIMO_ACEPTABLE + 1 - monje.getLipidos() + 100 - monje.getSalud();
                    bEstaEnfermo = enferma(posibilidades, Constantes.ENFERMEDAD_DEFECTO, Constantes.PROPIEDAD_LIPIDOS, monje, msg1, con);
                }

                //llegados a este punto, ya debemos saber si hay riesgo de alguna enfermedad por defecto
                //de alguna propiedad...
                if ((monje.getProteinas() > Constantes.ENFERMEDAD_MAXIMO_ACEPTABLE) && !bEstaEnfermo) {
                    posibilidades = Constantes.ENFERMEDAD_MAXIMO_ACEPTABLE + 1 - monje.getProteinas() + 100 - monje.getSalud();
                    bEstaEnfermo = enferma(posibilidades, Constantes.ENFERMEDAD_EXCESO, Constantes.PROPIEDAD_PROTEINAS, monje, msg1, con);
                }
                if ((monje.getHidratosCarbono() > Constantes.ENFERMEDAD_MAXIMO_ACEPTABLE) && !bEstaEnfermo) {
                    posibilidades = Constantes.ENFERMEDAD_MAXIMO_ACEPTABLE + 1 - monje.getHidratosCarbono() + 100 - monje.getSalud();
                    bEstaEnfermo = enferma(posibilidades, Constantes.ENFERMEDAD_EXCESO, Constantes.PROPIEDAD_HIDRATOS, monje, msg1, con);
                }

                if ((monje.getVitaminas() > Constantes.ENFERMEDAD_MAXIMO_ACEPTABLE) && !bEstaEnfermo) {
                    posibilidades = Constantes.ENFERMEDAD_MAXIMO_ACEPTABLE + 1 - monje.getVitaminas() + 100 - monje.getSalud();
                    bEstaEnfermo = enferma(posibilidades, Constantes.ENFERMEDAD_EXCESO, Constantes.PROPIEDAD_VITAMINAS, monje, msg1, con);
                }

                if ((monje.getLipidos() > Constantes.ENFERMEDAD_MAXIMO_ACEPTABLE) && !bEstaEnfermo) {
                    posibilidades = Constantes.ENFERMEDAD_MAXIMO_ACEPTABLE + 1 - monje.getLipidos() + 100 - monje.getSalud();
                    bEstaEnfermo = enferma(posibilidades, Constantes.ENFERMEDAD_EXCESO, Constantes.PROPIEDAD_LIPIDOS, monje, msg1, con);
                }

            }
            //creamos los mensajes para todos los monjes que han enfermado.
            oMensajesAD = new adMensajes(con);
            oMensajesAD.crearMensajes(mensajes);

            oProcesosAD.addLog("- Finalizado Proceso enfermedad. ", 0);

        } catch (Exception e) {
            throw new AbadiaSQLException("ERROR: Proceso enfermedad = " + e, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    /**
     * @param posibilidad    probabilidad
     * @param exceso_defecto 1 - Exceso / 2 - defecto
     * @param propiedad      propiedad a modificar
     * @param monje          monje a procesar
     * @param msg1           Mensaje a mostrar
     * @param p_cConnection  conexión
     * @return boolean
     * @throws AbadiaException Excepcion general
     */
    public boolean enferma(double posibilidad, int exceso_defecto, int propiedad, Monje monje, String msg1,
                           Connection p_cConnection) throws AbadiaException {
        adEnfermedad oEnformedadAD;
        adLiterales oLiteralesAD;
        Enfermedad enfermedad;
        HashMap htEnfermedadTipo;
        String nombreEnfermedad;
        //obtener enfermedades posibles   exceso / defecto.
        htEnfermedadTipo = (HashMap) htEnfermedades.get(exceso_defecto);
        if (htEnfermedadTipo != null) {
            //recuperamos la enfermedad asociada al exceso / defecto de la propiedad correspondiente.
            enfermedad = (Enfermedad) htEnfermedadTipo.get(new Integer(propiedad));
            if (enfermedad != null) {
                //calculamos la posibilidad de enfermar en base a las posibilidades propias de la
                //enfermedad y el valor obtenido de la propiedad (posibilidad)
                if (Utilidades.Random(1, enfermedad.getProbabilidad()) < posibilidad) {
                    //marcar monje como enfermo
                    //adMonje.actualizarEstado(monje.getIdDeMonje(), Constantes.MONJE_ENFERMO);
                    //el estado no se modificara hasta que no tenga un nivel 3 de la enfermedad.
                    oEnformedadAD = new adEnfermedad(p_cConnection);
                    oLiteralesAD = new adLiterales(p_cConnection);
                    //insertar registro en monje_enfermedad
                    oEnformedadAD.crearRegistroEnfermedad(monje.getIdDeMonje(), enfermedad);

                    //modificar el impacto de la enfermedad
                    oEnformedadAD.aplicarImpactoEnfermedadPropiedad(monje.getIdDeMonje(), enfermedad.getPropiedadImpacto());

                    //recuperamos el nombre de la enfermedad en el idioma del monje...
                    //utils = new adUtils (Constantes.DB_CONEXION_PROCESS);
                    nombreEnfermedad = oLiteralesAD.getLiteralStatic(enfermedad.getLiteralid(), monje.getIdioma());
                    //utils.finalize();
                    //sustituimos el nombre de la enfermedad
                    msg1 = ProcesosUtils.Format(msg1, nombreEnfermedad);
                    //generar mensaje
                    mensajes.add(new Mensajes(monje.getIdDeAbadia(), monje.getIdDeMonje(), msg1, 1));
                    //devolvemos que ha enfermado
                    return true;
                }
            }
        }
        //devolvemos que no ha enfermado.
        return false;
    }

    /**
     * Gestiona la evolución de las enfermedades
     * Enfermos de nivel x que llegan alcanzan la fecha fin de la enfermedad
     * En caso de que la propiedades que dieron lugar a la enfermedad esten corregidas, el monje recuperara la salud
     * y se eliminara el registro de la tabla monje_enfermedad
     * Si la enfermedad llega a su fin y el monje continua con las propiedades sin equilibrar, la enfermedad evolucionará al siguiente nivel
     * y en caso de trarse de un nivel 3... morirá.
     *
     * @throws AbadiaException excepción general
     */
    public void evolucionEnfermedad() throws AbadiaException {
        String sTrace = this.getClass() + ".evolucionEnfermedad()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adProcesos oProcesosAD;
        adUtils oUtilsAD;
        adEnfermedad oEnfermedadAD;
        adLiterales oLiteralesAD;
        adMensajes oMensajesAD;


        ArrayList<Monje> alMonjes;
        Enfermedad enfermedadActual;
        Enfermedad enfermedadSiguiente;

        int posibilidades;
        String msg1 = "", msg2 = "", msg3 = "";
        int last_abadiaid = 0, last_idiomaid = 0, tmp_idiomaid;
        HashMap<Integer, Integer> hmPropiedades = new HashMap<Integer, Integer>();


        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);
            //en primer lugar debemos recuperar un ArrayList con los datos de los monjes enfermos.
            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando Proceso Evolución de Enfermedad. ", 0);
            alMonjes = oProcesosAD.recuperarMonjesEnfermos(CoreTiempo.getTiempoAbadiaString());
            oUtilsAD = new adUtils(con);
            oLiteralesAD = new adLiterales(con);
            oEnfermedadAD = new adEnfermedad(con);
            htEnfermedades = oEnfermedadAD.recuperarTablaEnfermedadesPorTipo();

            for (Monje monje : alMonjes) {

                if (last_abadiaid != monje.getIdDeAbadia()) {
                    last_abadiaid = monje.getIdDeAbadia();
                    tmp_idiomaid = oUtilsAD.getIdiomaID(last_abadiaid);
                    if (last_idiomaid != tmp_idiomaid) {
                        last_idiomaid = tmp_idiomaid;
                        msg1 = oLiteralesAD.getLiteralStatic(10074, last_idiomaid);  // La enfermedad ha remitido: %s
                        msg2 = oLiteralesAD.getLiteralStatic(10075, last_idiomaid);  // Ha empeorado de su enfermedad, ahora tiene: %s
                        msg3 = oLiteralesAD.getLiteralStatic(10076, last_idiomaid);  // Sigue estando enfermo de: %s
                    }

                }

                //para cada monje, veficiamos si deberia enfermar del nivel superior al actual
                //si le toda enfermar volvemos a ejecutar la consulta para nivel siguiente y si no la cumple
                //replanificamos la enfermedad con el mismo nivel.
                hmPropiedades.put(Constantes.PROPIEDAD_HIDRATOS, (int) monje.getHidratosCarbono());
                hmPropiedades.put(Constantes.PROPIEDAD_LIPIDOS, (int) monje.getLipidos());
                hmPropiedades.put(Constantes.PROPIEDAD_PROTEINAS, (int) monje.getProteinas());
                hmPropiedades.put(Constantes.PROPIEDAD_VITAMINAS, (int) monje.getVitaminas());

                enfermedadActual = (Enfermedad) htEnfermedades.get(String.valueOf(monje.getEnfermedad().getTipoEnfermedad()).concat(String.valueOf(monje.getEnfermedad().getNivel())));
                //busco en la hash de enfermedades la siguiente a la actual en nivel
                enfermedadSiguiente = (Enfermedad) htEnfermedades.get(String.valueOf(enfermedadActual.getTipoEnfermedad()).concat(String.valueOf(enfermedadActual.getNivel() + 1)));

                if (enfermedadActual.getFaltaExceso() == Constantes.ENFERMEDAD_DEFECTO) {
                    if (hmPropiedades.get(new Integer(enfermedadActual.getPropiedadCausa())) <= Constantes.ENFERMEDAD_MINIMO_ACEPTABLE) {
                        posibilidades = Constantes.ENFERMEDAD_MINIMO_ACEPTABLE + 1 - hmPropiedades.get(new Integer(enfermedadActual.getPropiedadCausa())) + 100 - monje.getSalud();
                        //miramos si existe una enfermedad de siguiente nivel...
                        if (enfermedadSiguiente != null) {
                            //evaluamos si le toca enfermar....
                            if (Utilidades.Random(1, enfermedadSiguiente.getProbabilidad()) < posibilidades) {
                                //estamos seguros de que enferma....
                                if (Utilidades.Random(1, enfermedadSiguiente.getProbabilidad()) < posibilidades) {
                                    //enferma del siguiente nivel.
                                    enfermar(monje, enfermedadSiguiente, msg2, con);
                                } else {
                                    //sigue enfermo al mismo nivel.
                                    enfermar(monje, enfermedadActual, msg2, con);
                                }
                            } else {
                                //se cura
                                curar(monje, enfermedadActual, msg1, con);

                            }
                        } else if (Utilidades.Random(1, enfermedadActual.getProbabilidad()) < posibilidades) {
                            //sigue enfermo al mismo nivel.
                            enfermar(monje, enfermedadActual, msg2, con);
                        } else {
                            //se cura.
                            curar(monje, enfermedadActual, msg1, con);
                        }
                    } else //ha recuperado el valor para la propiedad
                    {
                        //se cura
                        curar(monje, enfermedadActual, msg1, con);
                    }
                } else if (enfermedadActual.getFaltaExceso() == Constantes.ENFERMEDAD_EXCESO) {
                    if (hmPropiedades.get(new Integer(enfermedadActual.getPropiedadCausa())) >= Constantes.ENFERMEDAD_MAXIMO_ACEPTABLE) {
                        posibilidades = Constantes.ENFERMEDAD_MAXIMO_ACEPTABLE + 1 - hmPropiedades.get(new Integer(enfermedadActual.getPropiedadCausa())) + 100 - monje.getSalud();
                        //miramos si existe una enfermedad de siguiente nivel...
                        if (enfermedadSiguiente != null) {
                            //evaluamos si le toca enfermar....
                            if (Utilidades.Random(1, enfermedadSiguiente.getProbabilidad()) < posibilidades) {
                                //estamos seguros de que enferma....
                                if (Utilidades.Random(1, enfermedadSiguiente.getProbabilidad()) < posibilidades) {
                                    //enferma del siguiente nivel.
                                    enfermar(monje, enfermedadSiguiente, msg2, con);
                                } else {
                                    //sigue enfermo al mismo nivel.
                                    enfermar(monje, enfermedadActual, msg3, con);
                                }
                            } else {
                                //se cura
                                curar(monje, enfermedadActual, msg1, con);
                            }
                        } else if (Utilidades.Random(1, enfermedadActual.getProbabilidad()) < posibilidades) {
                            //sigue enfermo al mismo nivel.
                            enfermar(monje, enfermedadActual, msg3, con);
                        } else {
                            //se cura.
                            curar(monje, enfermedadActual, msg1, con);
                        }
                    } else //ha recuperado el valor para la propiedad
                    {
                        //se cura
                        curar(monje, enfermedadActual, msg1, con);
                    }

                }// else if(enfermedadActual.getFaltaExceso() == Constantes.ENFERMEDAD_EXCESO)
            }

            //creamos los mensajes para todos los monjes que han enfermado.
            oMensajesAD = new adMensajes(con);
            oMensajesAD.crearMensajes(mensajes);

            oProcesosAD.addLog("- Finalizado Proceso Evolución de Enfermedad. ", 0);

        } catch (Exception e) {
            throw new AbadiaSQLException("ERROR: Proceso Evolución de Enfermedad = " + e, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    /**
     * Regulariza la salud de los monjes en función de las edades.
     *
     * @throws AbadiaException Excepción general
     */
    public void restarSaludPorEdad() throws AbadiaException {
        String sTrace = this.getClass() + ".restarSaludPorEdad()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adProcesos oProcesosAD;
        adMonje oMonjeAD;

        ArrayList<MonjeSalud> alListaMonjes;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);
            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando Regularizar salud por edad. ", 0);

            oMonjeAD = new adMonje(con);
            alListaMonjes = oMonjeAD.recuperarTablaEdades();

            for (MonjeSalud monje : alListaMonjes) {
                oMonjeAD.restarSaludPorEdad(monje);
            }

            oProcesosAD.addLog("+ Finalizado Regularizar salud por edad. ", 0);

        } catch (Exception e) {
            throw new AbadiaSQLException("adMonje. restarSaludPorEdad. Error SQL", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    /**
     * Regulariza la salud de los monjes en función de las edades.
     *
     * @throws AbadiaException Exceptión general
     */
    public void subirSaludSanos() throws AbadiaException {
        String sTrace = this.getClass() + ".subirSaludSanos()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        String sSQL = "call subirSaludMonjes()";

        adProcesos oProcesosAD;
        adUtils oUtilsAD;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);
            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando Subir salud monjes sanos. ", 0);

            oUtilsAD = new adUtils(con);
            oUtilsAD.execProcedure(sSQL);

            oProcesosAD.addLog("+ Finalizado Subir salud monjes sanos. ", 0);

        } catch (Exception e) {
            throw new AbadiaSQLException(sTrace + " subirSaludSanos. Error SQL", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    /**
     * Resta los puntos de salud de cada monje en función de la enfermedad que tienen.
     *
     * @throws AbadiaException Excepción general
     */
    public void restarSaludEnfermos() throws AbadiaException {
        String sTrace = this.getClass() + ".restarSaludEnfermos()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adUtils oUtilsAD;
        adProcesos oProcesosAD;
        adEnfermedad oEnfermedadAD;
        adLiterales oLiteralesAD;
        adMonje oMonjeAD;
        adMensajes oMensajesAD;

        MonjeBBean oMonjeBBean;

        ArrayList<Monje> alMonjes;
        Enfermedad enfermedad;

        String msg1 = "", msgTmp;
        String nombreEnfermedad;
        int last_abadiaid = 0, last_idiomaid = 0, tmp_idiomaid;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS, Constantes.AUTOCOMIT_OF);
            //recuperar monjes con enfermedad en vigor...
            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando Proceso Restar Salud Enfermedad. ", 0);
            alMonjes = oProcesosAD.recuperarMonjesEnfermos(null);
            oMonjeAD = new adMonje(con);
            oLiteralesAD = new adLiterales(con);
            oUtilsAD = new adUtils(con);
            //recuperamos una hash con la lista de enfermedades indexada por tipo_enfermedad y nivel
            oEnfermedadAD = new adEnfermedad(con);
            htEnfermedades = oEnfermedadAD.recuperarTablaEnfermedadesPorTipo();

            for (Monje monje : alMonjes) {
                if (last_abadiaid != monje.getIdDeAbadia()) {
                    last_abadiaid = monje.getIdDeAbadia();

                    tmp_idiomaid = oUtilsAD.getIdiomaID(last_abadiaid);

                    if (last_idiomaid != tmp_idiomaid) {
                        last_idiomaid = tmp_idiomaid;
                        msg1 = oLiteralesAD.getLiteralStatic(10056, last_idiomaid);  // Ha muerto a causa de su enfermedad: %s
                    }
                }

                //para poder obtener la salud que debemos restar al monje recuperamos los datos de la enfermedad.
                enfermedad = (Enfermedad) htEnfermedades.get(String.valueOf(monje.getEnfermedad().getTipoEnfermedad()).concat(String.valueOf(monje.getEnfermedad().getNivel())));

                if (enfermedad != null) {
                    //restamos la salud.
                    oMonjeAD.restarSalud(monje.getIdDeMonje(), enfermedad.getSalud());

                    //si la salud del monje esta por dbajo de 1 deberia morir.
                    if (monje.getSalud() <= 0) {
                        nombreEnfermedad = oLiteralesAD.getLiteralStatic(enfermedad.getLiteralid(), monje.getIdioma());

                        msgTmp = ProcesosUtils.Format(msg1, nombreEnfermedad);

                        mensajes.add(new Mensajes(monje.getIdDeAbadia(), -1, msgTmp, 1));

                        oMonjeBBean = new MonjeBBean();
                        oMonjeBBean.morirMonje(monje.getIdDeAbadia(), monje.getIdDeMonje(), 13001, con);  // Fallecio por mala alimentación.
                    }
                }
            }
            oMensajesAD = new adMensajes(con);
            //creamos los mensajes para los monjes que han muerto a causa de su enfermedad.
            oMensajesAD.crearMensajes(mensajes);

            oProcesosAD.addLog("- Finalizado Proceso Restar Salud Enfermedad. ", 0);
            ConnectionFactory.commitTransaction(con);
        } catch (Exception e) {
            ConnectionFactory.rollbackTransaction(con);
            throw new AbadiaSQLException(sTrace + " Proceso Restar Salud Enfermedad.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    /**
     * Gestiona el transito de enfermedades
     * Acutaliza el nivel y las fechas de una enfermedad
     * Aplica el impacto generado por la enfermedad sobre las habilidades
     * Genera un mensajes de: "Ha empeorado..." o "Continua enfermo.."
     *
     * @param monje         Monje a enfermar
     * @param enfermedad    Enfermedad contraida
     * @param msg           Mensaje a generar
     * @param p_cConnection Conexión abierta
     * @throws AbadiaException Excepción general
     */
    public void enfermar(Monje monje, Enfermedad enfermedad, String msg, Connection p_cConnection) throws AbadiaException {
        adEnfermedad oEnfermedadAD;
        adLiterales oLiteralesAD;

        String nombreEnfermedad;
        //insertar registro en monje_enfermedad


        oEnfermedadAD = new adEnfermedad(p_cConnection);
        oEnfermedadAD.actualizarEnfermedad(monje, enfermedad);

        //modificar el impacto de la enfermedad
        oEnfermedadAD.aplicarImpactoEnfermedadPropiedad(monje.getIdDeMonje(), enfermedad.getPropiedadImpacto());

        //recuperamos el nombre de la enfermedad en el idioma del monje...
        oLiteralesAD = new adLiterales(p_cConnection);
        nombreEnfermedad = oLiteralesAD.getLiteralStatic(enfermedad.getLiteralid(), monje.getIdioma());
        //sustituimos el nombre de la enfermedad
        msg = ProcesosUtils.Format(msg, nombreEnfermedad);
        //generar mensaje
        mensajes.add(new Mensajes(monje.getIdDeAbadia(), monje.getIdDeMonje(), msg, 1));
    }

    /**
     * Gestiona el fin de una enfermedad.
     * Elimina el registro de la tabla monje_enfermedad
     * Genera un mensaje de: "ha sanado"
     *
     * @param monje         Monje a procesar
     * @param enfermedad    enfermedad
     * @param msg           mensaje
     * @param p_cConnection Conexión
     * @throws AbadiaException Excepción general
     */
    public void curar(Monje monje, Enfermedad enfermedad, String msg, Connection p_cConnection) throws AbadiaException {
        adEnfermedad oEnfermedadAD;
        adLiterales oLiteralesAD;

        String nombreEnfermedad;
        int iNivelEnfermedad;
        Enfermedad enfermedadLocal;

        oEnfermedadAD = new adEnfermedad(p_cConnection);
        oEnfermedadAD.eliminarEnfermedad(monje.getIdDeMonje());

        //aqui deberiamos restablecer los valores de habilidades que se restaron al enfermar.
        //se deben recuperar los valores para el nivel en curso de la enfermedad
        iNivelEnfermedad = enfermedad.getNivel();
        for (; iNivelEnfermedad > 0; iNivelEnfermedad--) {
            enfermedadLocal = (Enfermedad) htEnfermedades.get(String.valueOf(enfermedad.getTipoEnfermedad()).concat(String.valueOf(iNivelEnfermedad)));
            if (enfermedadLocal != null) {
                oEnfermedadAD.restablecerImpactoEnfermedadPropiedad(monje.getIdDeMonje(), enfermedadLocal.getPropiedadImpacto());
            }
        }
        //recuperamos el nombre de la enfermedad en el idioma del monje...
        //utils = new adUtils (Constantes.DB_CONEXION_PROCESS);
        oLiteralesAD = new adLiterales(p_cConnection);
        nombreEnfermedad = oLiteralesAD.getLiteralStatic(enfermedad.getLiteralid(), monje.getIdioma());
        //utils.finalize();
        //sustituimos el nombre de la enfermedad
        msg = ProcesosUtils.Format(msg, nombreEnfermedad);
        //generar mensaje
        mensajes.add(new Mensajes(monje.getIdDeAbadia(), monje.getIdDeMonje(), msg, 1));
    }

}
