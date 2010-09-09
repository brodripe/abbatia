package org.abbatia.bbean;

import org.abbatia.actionform.*;
import org.abbatia.adbean.*;
import org.abbatia.bbean.singleton.CargaInicialActividadesBBean;
import org.abbatia.bbean.singleton.CargaInicialFamiliasBBean;
import org.abbatia.bbean.singleton.CargasInicialesDietasBBean;
import org.abbatia.bean.*;
import org.abbatia.core.CoreMonje;
import org.abbatia.core.CoreTiempo;
import org.abbatia.dbms.ConnectionFactory;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.*;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.Utilidades;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;

import java.io.Serializable;
import java.sql.Connection;
import java.util.*;

/**
 * Created by Benjamín Rodríguez.
 * User: benjamin
 * Date: 16-may-2008
 * Time: 22:34:27
 */
public class MonjeBBean {
    private static Logger log = Logger.getLogger(MonjeBBean.class.getName());


    /*    Función que controla la muerte del monje

              Los literales controlan de que ha muerto el monje los rangos de literales van del 13.000 al 13.999
                  del 13.000 al 13.499 son los literales que aparecen en el epitafio del cementerio
                  del 13.500 al 13.999 ( +500 ) es la historia del monje...
    */

    public void morirMonje(int AbadiaID, int MonjeID, int LiteralID_Defuncion, Connection p_cConnection) throws AbadiaException {
        String sTrace = this.getClass() + ".morirMonje(" + AbadiaID + "," + MonjeID + "," + LiteralID_Defuncion + ")";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        int jerarquiaid;

        adUtils utils;

        try {
            // Averiguar la jerarquia
            utils = new adUtils(p_cConnection);
            jerarquiaid = utils.getSQL("Select Jerarquiaid from monje where monjeid=" + MonjeID, 0);
            morirMonje(AbadiaID, MonjeID, jerarquiaid, LiteralID_Defuncion, p_cConnection);
        } finally {
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    /**
     * Procesa la muerte de un monje eliminando los datos de todas las trablas implicadas y actualizando el resto
     *
     * @param p_iAbadiaID           Identificador de abadía
     * @param p_iMonjeID            Identificador de monje
     * @param p_iJerarquiaID        Identificador de Jerarquía
     * @param p_iLiteralIDDefuncion Identificador de literal.
     * @throws AbadiaException Excepción general
     */
    public void morirMonje(int p_iAbadiaID, int p_iMonjeID, int p_iJerarquiaID, int p_iLiteralIDDefuncion, Connection p_cConnection) throws AbadiaException {
        // Añadirlo en el cementerio

        String sTrace = this.getClass() + ".morirMonje(" + p_iAbadiaID + "," + p_iMonjeID + "," + p_iJerarquiaID + "," + p_iLiteralIDDefuncion + ")";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        String sSQLEntierro = "INSERT INTO `monje_cementerio` (monjeid, abadiaid, jerarquiaid, especializacionid, estado, nombre, apellido1, " +
                "apellido2, fecha_nacimiento, fecha_fallecimiento, fecha_entrada, santo) " +
                "Select * from monje where monjeid = " + p_iMonjeID;

        adHabilidades oHabilidadesAD;
        adLibros oLibroAD;
        adHistoria oHistoriaAD;
        adUtils oUtilsAD;
        adAbadia oAbadiaAD;
        adPapa oPapaAD;
        adMonje oMonjeAD;
        adViajar oViajarAD;

        ArrayList<MonjeProceso> alMonjes;


        try {
            // Primero matar el monje ;-)

            oUtilsAD = new adUtils(p_cConnection);
            oUtilsAD.execSQL("UPDATE monje SET estado = 1, fecha_fallecimiento='" + CoreTiempo.getTiempoAbadiaString() + "' WHERE monjeid = " + p_iMonjeID);
            oUtilsAD.execSQL("DELETE FROM monje_cementerio WHERE monjeid = " + p_iMonjeID);
            oUtilsAD.execSQL(sSQLEntierro);
            oUtilsAD.execSQL("UPDATE monje_cementerio SET estado = 0, muerte_LiteralID=" + p_iLiteralIDDefuncion + " WHERE monjeid = " + p_iMonjeID);

            // Jerarquia del monje
            switch (p_iJerarquiaID) {
                case 0:   // Constantes.Jerarquia_Novicio
                    oHabilidadesAD = new adHabilidades(p_cConnection);
                    oHabilidadesAD.decrementarHabilidadesAbadia(p_iAbadiaID, Constantes.HABILIDAD_FE, 10);
                    break;
                case 1:   // Constantes.Jerarquia_Monje
                    oHabilidadesAD = new adHabilidades(p_cConnection);
                    oHabilidadesAD.decrementarHabilidadesAbadia(p_iAbadiaID, Constantes.HABILIDAD_FE, 20);
                    break;
                case 2:   // Constantes.Jerarquia_Abad
                    oHabilidadesAD = new adHabilidades(p_cConnection);
                    oHabilidadesAD.decrementarHabilidadesAbadia(p_iAbadiaID, Constantes.HABILIDAD_FE, 30);
                    break;
                case 3:   // Constantes.Jerarquia_Obispo
                    oHabilidadesAD = new adHabilidades(p_cConnection);
                    oAbadiaAD = new adAbadia(p_cConnection);
                    oHabilidadesAD.decrementarHabilidadesRegion(oAbadiaAD.getRegionAbadia(p_iAbadiaID), Constantes.HABILIDAD_FE, 30);
                    break;
                case 5:   // Constantes.Jerarquia_Cardenal
                    oHabilidadesAD = new adHabilidades(p_cConnection);
                    oHabilidadesAD.decrementarHabilidad(Constantes.HABILIDAD_FE, 30);
                    //eliminar registro de la tabla: cardenales
                    break;
                case 6:   // Constantes.Jerarquia_Papa
                    oHabilidadesAD = new adHabilidades(p_cConnection);
                    oHabilidadesAD.decrementarHabilidad(Constantes.HABILIDAD_FE, 50);
                    // Realizar el conclave
                    oPapaAD = new adPapa(p_cConnection);
                    oPapaAD.organizarConclave(p_iAbadiaID, p_iMonjeID);
                    break;
            }


            //sea quien sea el monje muerto (da igual la jerarquia) verificamos si quedan menos de 5 monjes vivos
            //si no queda nadie vivo, verificamos si hay monjes de viaje, si los hay, forzamos su vuelta del primero que pillemos
            oMonjeAD = new adMonje(p_cConnection);
            if (oMonjeAD.getNumMonjes(p_iAbadiaID, Constantes.MONJE_VIVO) < 3) {
                alMonjes = oMonjeAD.recuperarMonjesVisita(p_iAbadiaID);
                if (!alMonjes.isEmpty()) {
                    oViajarAD = new adViajar(p_cConnection);
                    for (MonjeProceso oMonje : alMonjes) {
                        //todo temporalmente reutilizamos este método que estaba pensado para cuando un libro se
                        //todo deterioraba durante la copia
                        oViajarAD.forzarVueltaProceso(oMonje, Constantes.FORZAR_RETORNO_ABADIA_SIN_MONJES);
                    }
                }
            }


            //si el monje estaba copiando algun libro...
            oLibroAD = new adLibros(p_cConnection);
            int idLibroCopia = oLibroAD.recuperarLibroPorTareaMonjeCopia(p_iMonjeID);
            if (idLibroCopia != 0) {
                //Eliminamos posibles registros de copia por tarea del monje..
                oLibroAD.eliminarTareasCopiaPorMonje(p_iMonjeID);
                //actualizamos el estado de la copia del libro a "incompleto"
                oLibroAD.actualizarEstadoLibro(idLibroCopia, Constantes.ESTADO_LIBRO_INCOMPLETO);
            }

            // SI ES IMPORTANTE... Hay que marcarlo para la hitoria!
            if (p_iJerarquiaID > 2) {
                oHistoriaAD = new adHistoria(p_cConnection);
                oHistoriaAD.anyadirHistoria(p_iAbadiaID, p_iMonjeID, p_iJerarquiaID, p_iLiteralIDDefuncion + 500);
            }
        } finally {
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }


    /**
     * Recupera todos los monjes vivos de una abadía incluyendo los que están de visita y pertenecen a otras abadías.
     *
     * @param idAbadia Identificador de abadía
     * @param resource objetos i18n
     * @return ArrayList
     * @throws AbadiaException Excepción general
     */
    public ArrayList<Monje> recuperarMonjesPorAbadia(int idAbadia, MessageResources resource) throws AbadiaException {
        String sTrace = this.getClass() + ".recuperarMonjesPorAbadia()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adMonje oMonjeAD;
        ArrayList<Monje> alMonjesVivos;
        ArrayList<Monje> alMonjesVisita;
        ArrayList<Monje> alMonjes;
        Iterator<Monje> itMonjesVisita;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);

            oMonjeAD = new adMonje(con);
            alMonjesVivos = oMonjeAD.recuperarMonjes(idAbadia, Constantes.MONJES_VIVOS, resource);
            alMonjesVisita = oMonjeAD.recuperarMonjes(idAbadia, Constantes.MONJES_VISITA_MIABADIA, resource);

            alMonjes = alMonjesVivos;
            itMonjesVisita = alMonjesVisita.iterator();
            while (itMonjesVisita.hasNext()) {
                alMonjes.add(itMonjesVisita.next());
            }

            return alMonjes;
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }


    public void confirmarExpulsarMonje(Monje p_oMonje, Abadia p_oAbadia, MessageResources p_oResources, ActionMessages p_oMensajes) throws AbadiaException {
        String sTrace = this.getClass() + ".confirmarExpulsarMonje()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        String sSQLI = "insert into monje_expulsado (monjeid, abadiaid, nombre, apellido1, apellido2, fecha_nacimiento, fecha_entrada, fecha_expulsion) " +
                " select monjeid, abadiaid, nombre, apellido1, apellido2, fecha_nacimiento, fecha_entrada, '" + CoreTiempo.getTiempoAbadiaString() + "' from monje where monjeid = " + p_oMonje.getIdDeMonje();

        adUtils oUtilsAD;
        adMonje oMonjeAD;
        adHabilidades oHabilidadesAD;
        adRecurso oRecursoAD;
        adMensajes oMensajesAD;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS, Constantes.AUTOCOMIT_OF);

            oUtilsAD = new adUtils(con);
            oUtilsAD.execSQL(sSQLI);

            oMonjeAD = new adMonje(con);
            oMonjeAD.eliminarDatosMonje(p_oMonje.getIdDeMonje());

            oHabilidadesAD = new adHabilidades(con);
            //un 50% de posibilidades de que baje el carisma del abad
            if (Utilidades.Random(1, 2) == 2) {
                //decrementamos el carisma en un aleatorio de 1 a 5
                oHabilidadesAD.decrementarHabilidad(p_oMonje.getIdDeMonje(), Constantes.HABILIDAD_CARISMA, Utilidades.Random(1, 5));
            }
            //decrementamos en 20 puntos la fe de todos los monjes
            oHabilidadesAD.decrementarHabilidadesAbadia(p_oAbadia.getIdDeAbadia(), Constantes.HABILIDAD_FE, 20);

            oRecursoAD = new adRecurso(con);
            oRecursoAD.restarRecurso(Constantes.RECURSOS_ORO, p_oMonje.getIdDeAbadia(), p_oMonje.getPuntosMonje());

            oMensajesAD = new adMensajes(con);
            oMensajesAD.crearMensaje(p_oAbadia.getIdDeAbadia(), -1, -1, 0, p_oResources.getMessage("mensajes.expulsion.monje", p_oMonje.getNombreCompuesto()));
            oMensajesAD.crearMensaje(p_oAbadia.getIdDeAbadia(), -1, -1, 0, p_oResources.getMessage("mensajes.expulsion.monje2", Utilidades.redondear(p_oMonje.getPuntosMonje())));

            //ad.finalize();

            p_oMensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.expulsion.ok", p_oMonje.getNombreCompuesto()));

            //commit de la transacción
            ConnectionFactory.commitTransaction(con);

        } catch (AbadiaException e) {
            ConnectionFactory.rollbackTransaction(con);
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public void actualizarDatosMonje(MostrarMonjeActForm p_afMostrarMonje, Abadia p_oAbadia, Usuario p_oUsuario, MessageResources p_oResource) throws AbadiaException {
        String sTrace = this.getClass() + ".actualizarDatosMonje()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adMonje oMonjeAD;
        adCampo oCampoAD;

        Monje oMonje;
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS, Constantes.AUTOCOMIT_OF);
            // Mostrar datos del inicio
            oMonjeAD = new adMonje(con);
            oMonje = oMonjeAD.recuperarMonje(p_afMostrarMonje.getIdMonje(), p_oAbadia.getIdDeAbadia(), p_oResource, p_oUsuario.getIdDeIdioma());

            oMonje.setActMaitines(Constantes.TAREA_REZAR);
            oMonje.setActLaudes(Constantes.TAREA_REZAR);
            if (oMonje.getActNona_bloqueado() == 0)
                oMonje.setActNona(p_afMostrarMonje.getActNona());
            if (oMonje.getActPrima_bloqueado() == 0)
                oMonje.setActPrima(p_afMostrarMonje.getActPrima());
            if (oMonje.getActTercia_bloqueado() == 0)
                oMonje.setActTercia(p_afMostrarMonje.getActTercia());
            oMonje.setActSexta(Constantes.TAREA_COMER);
            if (oMonje.getActVispera_bloqueado() == 0)
                oMonje.setActVispera(p_afMostrarMonje.getActVispera());
            oMonje.setActAngelus(Constantes.TAREA_REZAR);

            oMonjeAD.actualizarActividades(oMonje);

            //debemos llamar a un método que elimine los registros campo_monje en el caso de
            //que ya no exista la tarea agricultura....
            oCampoAD = new adCampo(con);
            oCampoAD.eliminarCampoMonjeHuerfanos();

            //copiamos al objeto monje el nivel de la abadía
            oMonje.setNivelAbadia(p_oAbadia.getNivelJerarquico());
            //a partir de ahora se calculara la dieta a partir de las tareas seleccionadas...
            oMonjeAD.actualizarAlimentacionSmart(oMonje);
            oMonjeAD.actualizarAlimentacion(oMonje);


            ConnectionFactory.commitTransaction(con);

        } catch (AbadiaException e) {
            ConnectionFactory.rollbackTransaction(con);
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public HashMap<String, Serializable> evaluarDieta(MostrarMonjeActForm p_afMonje, Abadia p_oAbadia, Usuario p_oUsuario,
                                                      MessageResources p_oResource) throws AbadiaException {
        String sTrace = this.getClass() + ".evaluarDieta()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        HashMap<String, Serializable> hmRequest = new HashMap<String, Serializable>();
        adMonje oMonjeAD;
        adJerarquiaEclesiastica oJerarquiaAD;
        adViajar oViajarAD;
        adAlimentos oAlimentosAD;
        adActividad oActividadAD;

        String szVsitaMiAbadia = null;
        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oMonjeAD = new adMonje(con);
            Monje monje = oMonjeAD.recuperarMonje(p_afMonje.getIdMonje(), p_oAbadia.getIdDeAbadia(), p_oResource, p_oUsuario.getIdDeIdioma());
            oMonjeAD.actualizarBarrasHTML(monje, p_oResource);

            oJerarquiaAD = new adJerarquiaEclesiastica(con);
            hmRequest.put("hayabad", Integer.toString(oJerarquiaAD.getMonjeIDporJerarquia(p_oAbadia.getIdDeAbadia(), Constantes.JERARQUIA_ABAD)));

            // Para los listos de las URLS ;-)
            if (monje.getIdDeAbadia() != p_oAbadia.getIdDeAbadia()) {
                oViajarAD = new adViajar(con);
                // Comprobar que el monje no esté de viaje en su abadía
                if (!oViajarAD.chkMonjeSePuedeVisualizar(p_oAbadia, monje)) {
                    throw new MonjeNoVisualizableException("El monje no se puede visualizar", log);
                }
                szVsitaMiAbadia = "1";
            }

            hmRequest.put("Visita_miabadia", szVsitaMiAbadia);
            hmRequest.put("Monje", monje);

            // Lista de alimentos
            ArrayList<Table> alTable;

            oAlimentosAD = new adAlimentos(con);
            alTable = oAlimentosAD.getListaTipos(p_oUsuario.getIdDeIdioma());

            alTable.add(0, new Table(0, "-"));      // Insertar elementos vacios
            hmRequest.put("alifam", alTable);

            // Lista de actividades
            oActividadAD = new adActividad(con);
            alTable = oActividadAD.getListaTipos(p_oUsuario.getIdDeIdioma(), monje.getIdDeJerarquia());

            alTable.add(0, new Table(0, "-"));      // Insertar elementos vacios
            hmRequest.put("actividades", alTable);

            //evaluacion de propiedades de la dieta...
            oMonjeAD = new adMonje(con);
            oMonjeAD.evaluarDieta(p_afMonje, monje, p_oResource);

            return hmRequest;
        } catch (AbadiaException e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public Monje inicioExpulsarMonje(int p_iMonjeId, Abadia p_oAbadia, MessageResources p_oResource,
                                     ActionMessages p_oMensajes, ArrayList<Notificacion> p_alNotas) throws AbadiaException {
        String sTrace = this.getClass() + ".inicioExpulsarMonje()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adMonje oMonjeAD;
        adRecurso oRecursoAD;

        Monje oMonje;
        Monje oMonjeExpulsado;
        int iOroAbadia;
        int iPuntosMonje;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);

            oMonjeAD = new adMonje(con);
            oMonje = oMonjeAD.recuperarDatosMonje(p_iMonjeId);
            oMonje.setNombreCompuesto(oMonjeAD.getNomMonje(oMonje.getIdDeMonje(), p_oResource.getMessage("monjes.abadia.de")));
            //si el monje no es de nuestra abbatia...
            if (oMonje.getIdDeAbadia() != p_oAbadia.getIdDeAbadia()) {
                p_oMensajes.add("msg", new ActionMessage("mensajes.aviso.tramposo.expulsarmonje"));
                p_alNotas.add(new Notificacion("/mostrarMonje.do?clave=" + oMonje.getIdDeMonje(), p_oResource.getMessage("mensajes.link.volver")));
                throw new GenericException("Excepción contralada", log);
            }
            if (oMonje.getIdDeJerarquia() != Constantes.JERARQUIA_MONJE) {
                p_oMensajes.add("msg", new ActionMessage("mensajes.aviso.expulsarmonje.jerarquiaincorrecta"));
                p_alNotas.add(new Notificacion("/mostrarMonje.do?clave=" + oMonje.getIdDeMonje(), p_oResource.getMessage("mensajes.link.volver")));
                throw new GenericException("Excepción contralada", log);
            }
            if (oMonje.getSanto() == 1) {
                p_oMensajes.add("msg", new ActionMessage("mensajes.aviso.expulsarmonje.santo"));
                p_alNotas.add(new Notificacion("/mostrarMonje.do?clave=" + oMonje.getIdDeMonje(), p_oResource.getMessage("mensajes.link.volver")));
                throw new GenericException("Excepción contralada", log);
            }

            //si el monje no está en estado vivo
            if (oMonje.getEstado() != Constantes.MONJE_VIVO) {
                p_oMensajes.add("msg", new ActionMessage("mensajes.aviso.expulsarmonje.estadoincorrecto"));
                p_alNotas.add(new Notificacion("/mostrarMonje.do?clave=" + oMonje.getIdDeMonje(), p_oResource.getMessage("mensajes.link.volver")));
                throw new GenericException("Excepción contralada", log);
            }
            //hace poso que expulsaste a otro monje
            if (!oMonjeAD.periodoExpulsionValido(p_oAbadia.getIdDeAbadia())) {
                oMonjeExpulsado = oMonjeAD.recuperarUltimoMonjeExpulsado(p_oAbadia.getIdDeAbadia());
                p_oMensajes.add("msg", new ActionMessage("mensajes.aviso.expulsarmonje.periodoinvalido", oMonjeExpulsado.getNombre() + " " + p_oResource.getMessage("monjes.abadia.de") + " " + oMonjeExpulsado.getPrimerApellido()));
                p_alNotas.add(new Notificacion("/mostrarMonje.do?clave=" + oMonje.getIdDeMonje(), p_oResource.getMessage("mensajes.link.volver")));
                throw new GenericException("Excepción contralada", log);
            }

            //calcular la pasta que debemos restar a la abadía.
            iPuntosMonje = oMonjeAD.calcularPuntosMonje(oMonje.getIdDeMonje());
            oMonje.setPuntosMonje(iPuntosMonje);
            //recuperamos el oro del que dipone la abadía
            oRecursoAD = new adRecurso(con);
            iOroAbadia = (int) oRecursoAD.recuperarValorRecurso(Constantes.RECURSOS_ORO, p_oAbadia.getIdDeAbadia());
            //hace poso que expulsaste a otro monje
            if (iPuntosMonje > iOroAbadia) {
                p_oMensajes.add("msg", new ActionMessage("mensajes.aviso.expulsarmonje.faltaoro", Utilidades.redondear(iPuntosMonje)));
                p_alNotas.add(new Notificacion("/mostrarMonje.do?clave=" + oMonje.getIdDeMonje(), p_oResource.getMessage("mensajes.link.volver")));
                throw new GenericException("Excepción contralada", log);
            }

            return oMonje;

        } catch (AbadiaException e) {
            throw e;
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public HashMap<String, ArrayList<Monje>> listarMonjes(Abadia p_oAbadia, MessageResources p_oResource) throws AbadiaException {
        String sTrace = this.getClass() + ".listarMonjes()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        HashMap<String, ArrayList<Monje>> hmRequest = new HashMap<String, ArrayList<Monje>>();

        adAbadia oAbadiaAD;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            //defino un objeto de tipo iterator para recorrer el arrayList
            oAbadiaAD = new adAbadia(con);
            //log.info("Monjes Vivos");
            hmRequest.put("Monjes", oAbadiaAD.getMonjes(p_oAbadia.getIdDeAbadia(), Constantes.MONJES_VIVOS, p_oResource));
            //log.info("Monjes Enfermos");
            hmRequest.put("Monjes_enfermos", oAbadiaAD.getMonjes(p_oAbadia.getIdDeAbadia(), Constantes.MONJES_ENFERMOS, p_oResource));
            //log.info("Monjes Viajando");
            hmRequest.put("Monjes_viaje", oAbadiaAD.getMonjes(p_oAbadia.getIdDeAbadia(), Constantes.MONJES_VIAJANDO, p_oResource));
            //log.info("Monjes Visita");
            hmRequest.put("Monjes_visita", oAbadiaAD.getMonjes(p_oAbadia.getIdDeAbadia(), Constantes.MONJES_VISITA, p_oResource));
            //log.info("Monjes Visita mi abbatia");
            hmRequest.put("Monjes_visita_miabadia", oAbadiaAD.getMonjes(p_oAbadia.getIdDeAbadia(), Constantes.MONJES_VISITA_MIABADIA, p_oResource));
            hmRequest.put("Monjes_visita_enfermos", oAbadiaAD.getMonjes(p_oAbadia.getIdDeAbadia(), Constantes.MONJES_VISITA_ENFERMOS, p_oResource));

            return hmRequest;

        } catch (AbadiaException e) {
            throw e;
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public ArrayList<Table> listarTrabajos(String p_szAhora, Abadia p_oAbadia, Usuario p_oUsuario, MessageResources p_oResource) throws AbadiaException {
        String sTrace = this.getClass() + ".listarMonjes()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adActividad oActividadAD;

        ArrayList<Table> alTable;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oActividadAD = new adActividad(con);
            if (p_szAhora != null) {
                alTable = oActividadAD.getActividadesMonjesAhoraMismo(p_oAbadia.getIdDeAbadia(), p_oUsuario.getIdDeIdioma(), p_oResource);
            } else {
                alTable = oActividadAD.getActividadesAgrupados(p_oAbadia.getIdDeAbadia(), p_oUsuario.getIdDeIdioma());
            }
            return alTable;

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public void reclutarNovicio(Abadia p_oAbadia, Usuario p_oUsuario, ActionMessages p_oMensajes, ArrayList<Notificacion> p_alNotas, MessageResources p_oResource) throws AbadiaException {
        String sTrace = this.getClass() + ".reclutarNovicio()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adMonje oMonjeAD;
        adEdificio oEdificioAD;
        adLibros oLibrosAD;
        adRecurso oRecursoAD;

        CoreMonje oCoreMonje;

        Edificio oEdificio;
        Libro oLibro;
        Monje oMonje;

        int iNumeroMonjes;
        int iCapacidad;
        int iIncrementoCapacidad = 0;
        int iMonjeId;
        double dMonedas;
        double dAldeanos;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oMonjeAD = new adMonje(con);
            iNumeroMonjes = oMonjeAD.getNumMonjesSinJerarquia(p_oAbadia.getIdDeAbadia());

            oEdificioAD = new adEdificio(con);
            oEdificio = oEdificioAD.recuperarEdificioTipo(Constantes.EDIFICIO_DORMITORIO, p_oAbadia, p_oUsuario);

            oLibrosAD = new adLibros(con);
            oLibro = oLibrosAD.recuperaLibroTipo(300 + Constantes.EDIFICIO_DORMITORIO, p_oAbadia.getIdDeAbadia());

            if (oLibro != null) {
                iIncrementoCapacidad = Utilidades.recuperarIncrementoEspacioEdificioPorLibro(oEdificio.getIdDeTipoDeEdificio(), oLibro.getNivel(), oEdificio.getNivel());
            }

            iCapacidad = oEdificio.getCapacidadVital() + iIncrementoCapacidad;

            if (iCapacidad <= iNumeroMonjes) {
                p_oMensajes.add("msg", new ActionMessage("mensajes.info.nohayespacioendormitorio"));
                throw new EspacioInsuficienteException(sTrace + " Espacio Insuficiente en dormitorio", log);
            }

            Random r = new Random(System.currentTimeMillis());

            log.debug("reclutar_novicio - comprar un aldeano...");

            // Recursos
            oRecursoAD = new adRecurso(con);
            dMonedas = oRecursoAD.recuperarValorRecurso(Constantes.RECURSOS_ORO, p_oAbadia.getIdDeAbadia());
            dAldeanos = oRecursoAD.recuperarValorRecurso(Constantes.RECURSOS_ALDEANOS, p_oAbadia.getIdDeAbadia());

            //deberemos ampliar la probabilidad en función del nivel del edificio "noviciado"
            oEdificio = oEdificioAD.recuperarEdificioTipo(Constantes.EDIFICIO_NOVICIADO, p_oAbadia, p_oUsuario);

            int rValue = 0;
            if (oEdificio == null) {
                rValue = 10;
            } else if (oEdificio.getNivel() == 0) {
                rValue = 10;
            } else if (oEdificio.getNivel() == 1) {
                rValue = 25;
            } else if (oEdificio.getNivel() == 2) {
                rValue = 45;
            } else if (oEdificio.getNivel() == 3) {
                rValue = 60;
            } else if (oEdificio.getNivel() == 4) {
                rValue = 80;
            }

            int probabilidad = r.nextInt(100);
            if (dAldeanos >= 1) {

                if (dMonedas >= 500) {
                    int iNumMonjes = oMonjeAD.getNumMonjes(p_oAbadia.getIdDeAbadia());

                    if ((probabilidad <= rValue) || (iNumMonjes < 5)) {
                        oCoreMonje = new CoreMonje(con);
                        oMonje = oCoreMonje.reclutarNovicioParaAbadia(p_oAbadia);
                        iMonjeId = oMonjeAD.crearMonjeID(oMonje);
                        //admonje.crea Habilidades
                        oRecursoAD.restarRecurso(Constantes.RECURSOS_ALDEANOS, p_oAbadia.getIdDeAbadia(), 1);
                        p_alNotas.add(new Notificacion("mostrarMonje.do?clave=" + iMonjeId, p_oResource.getMessage("mensajes.reclutar.mostrar"), "monje_1.gif", (short) 1));
                        p_oMensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("monje.reclutado"));
                        //request.setAttribute("Mensaje", resource.getMessage("monje.reclutado"));
                    } else {
                        p_oMensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("monje.sinreclutar"));
                        //request.setAttribute("Mensaje", resource.getMessage("monje.sinreclutar"));
                    }
                    oRecursoAD.restarRecurso(Constantes.RECURSOS_ORO, p_oAbadia.getIdDeAbadia(), 500);

                } else {
                    p_oMensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mercado.compra3.sinrecursos"));
                    //request.setAttribute("Mensaje", resource.getMessage("mercado.compra3.sinrecursos"));
                }
            } else {
                p_oMensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("monje.reclutar.sinaldeanos"));
                //request.setAttribute("Mensaje", resource.getMessage("monje.reclutar.sinaldeanos"));
            }
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }


    }

    public void mostrarActividadesMonje(int p_iMonjeId, MonjeActividadActForm p_afDatosFormulario, Abadia p_oAbadia, Usuario p_oUsuario, MessageResources p_oResource) throws AbadiaException {
        String sTrace = this.getClass() + ".mostrarActividadesMonje()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adMonje oMonjeAD;
        adViajar oViajarAD;

        ArrayList<datosMonjeActividad> alActividades;


        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oMonjeAD = new adMonje(con);

            oMonjeAD.recuperarMonjeActividad(p_iMonjeId, p_oAbadia.getIdDeAbadia(), p_afDatosFormulario, p_oUsuario.getIdDeIdioma());
            alActividades = oMonjeAD.recuperarActividadMonje(p_afDatosFormulario.getIdDeMonje());

            if (p_afDatosFormulario.getIdDeAbadia() != p_oAbadia.getIdDeAbadia()) {
                Monje monje = oMonjeAD.recuperarMonje(p_iMonjeId, p_oAbadia.getIdDeAbadia(), p_oResource, p_oUsuario.getIdDeIdioma());
                // Comprobar que el monje no esté de viaje en su abadía
                oViajarAD = new adViajar(con);
                if (!oViajarAD.chkMonjeSePuedeVisualizar(p_oAbadia, monje)) {
                    throw new MonjeNoVisualizableException(sTrace + " No se pueden mostral los datos del monje", log);
                }
            }
            p_afDatosFormulario.setListaActividades(alActividades);

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }


    public HashMap<String, Serializable> recuperarDetalleMonje(int p_iMonjeId, String p_szAccion, Abadia p_oAbadia, Usuario p_oUsuario, MessageResources p_oResource) throws AbadiaException {
        String sTrace = this.getClass() + ".recuperarDetalleMonje()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adMonje oMonjeAD;
        adViajar oViajarAD;
        adJerarquiaEclesiastica oJerarquiaAD;
        //adAlimentos oAlimentosAD;
        //adActividad oActividadAD;

        Monje oMonje;
        String szVisitaMiAbadia = "0";
        List<Table> alTable;
        MostrarMonjeActForm afMonje;

        HashMap<String, Serializable> hmRequest = new HashMap<String, Serializable>();

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oMonjeAD = new adMonje(con);
            oMonje = oMonjeAD.recuperarMonje(p_iMonjeId, p_oAbadia.getIdDeAbadia(), p_oResource, p_oUsuario.getIdDeIdioma());
            //actualizar familias de alimentación en base a actividades del monje
            oMonje.setComeFamiliaID1(CargasInicialesDietasBBean.getFamiliaAlimentosActividad(p_oAbadia.getNivelJerarquico(),
                    oMonje.getIdDeJerarquia(), oMonje.getActMaitines()).shortValue());
            oMonje.setComeFamiliaID2(CargasInicialesDietasBBean.getFamiliaAlimentosActividad(p_oAbadia.getNivelJerarquico(),
                    oMonje.getIdDeJerarquia(), oMonje.getActPrima()).shortValue());
            oMonje.setComeFamiliaID3(CargasInicialesDietasBBean.getFamiliaAlimentosActividad(p_oAbadia.getNivelJerarquico(),
                    oMonje.getIdDeJerarquia(), oMonje.getActTercia()).shortValue());
            oMonje.setComeFamiliaID4(CargasInicialesDietasBBean.getFamiliaAlimentosActividad(p_oAbadia.getNivelJerarquico(),
                    oMonje.getIdDeJerarquia(), oMonje.getActNona()).shortValue());
            oMonje.setComeFamiliaID5(CargasInicialesDietasBBean.getFamiliaAlimentosActividad(p_oAbadia.getNivelJerarquico(),
                    oMonje.getIdDeJerarquia(), oMonje.getActVispera()).shortValue());

            oMonjeAD.actualizarBarrasHTML(oMonje, p_oResource);

            oJerarquiaAD = new adJerarquiaEclesiastica(con);
            hmRequest.put("hayabad", Integer.toString(oJerarquiaAD.getMonjeIDporJerarquia(p_oAbadia.getIdDeAbadia(), Constantes.JERARQUIA_ABAD)));

            // Para los listos de las URLS ;-)
            if (oMonje.getIdDeAbadia() != p_oAbadia.getIdDeAbadia()) {
                // Comprobar que el monje no esté de viaje en su abadía
                oViajarAD = new adViajar(con);
                if (!oViajarAD.chkMonjeSePuedeVisualizar(p_oAbadia, oMonje)) {
                    throw new MonjeNoVisualizableException(sTrace + " monje de visita, no visualizable", log);
                }
                szVisitaMiAbadia = "1";
            }

            hmRequest.put("Visita_miabadia", szVisitaMiAbadia);
            hmRequest.put("Monje", oMonje);

            // Lista de alimentos

            alTable = CargaInicialFamiliasBBean.recuperarFamiliasPorIdioma(p_oUsuario.getIdDeIdioma());
//            oAlimentosAD = new adAlimentos(con);
//            alTable = oAlimentosAD.getListaTipos(p_oUsuario.getIdDeIdioma());
            hmRequest.put("alifam", (Serializable) alTable);

            // Lista de actividades
            alTable = CargaInicialActividadesBBean.recuperarActividadesPorJerarquiaIdioma(oMonje.getIdDeJerarquia(),
                    p_oUsuario.getIdDeIdioma());
//            oActividadAD = new adActividad(con);
//            alTable = oActividadAD.getListaTipos(p_oUsuario.getIdDeIdioma(), oMonje.getIdDeJerarquia());
            //alTable.add(0, new Table(0, "-"));      // Insertar elementos vacios
            hmRequest.put("actividades", (Serializable) alTable);

            //
            if (p_szAccion.equals("inicio")) {
                afMonje = new MostrarMonjeActForm();
                //Cargamos los datos del objeto monje sobre el MostrarMonjeActForm
                adMonje.cargarDatosMonje(oMonje, afMonje);
                hmRequest.put("MonjeForm", afMonje);
            }

            return hmRequest;

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public void recuperarMonjesAgricultura(DatosCampoActForm p_afCampo, Abadia p_oAbadia, MessageResources p_oResource) throws AbadiaException {
        String sTrace = this.getClass() + ".recuperarMonjesAgricultura()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adMonje oMonjeAD;
        adCampo oCampoAD;

        ArrayList<Monje> alMonjes;
        Iterator<Monje> itMonjes;
        Monje oMonje;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oMonjeAD = new adMonje(con);
            //buscamos a los monjes de la abbatia en curso para listar los que están
            //disponibles para copiar...
            alMonjes = oMonjeAD.recuperarMonjesActividadAbadia(Constantes.TAREA_AGRICULTURA, p_oAbadia.getIdDeAbadia(), p_oResource);

            //cargar info para ver si el monje ya esta asignado a un campo...
            itMonjes = alMonjes.iterator();
            oCampoAD = new adCampo(con);

            while (itMonjes.hasNext()) {
                oMonje = itMonjes.next();
                if (oCampoAD.estaTrabajando(oMonje.getIdDeMonje())) {
                    oMonje.setPrimerApellido(oMonje.getPrimerApellido() + " (" + p_oResource.getMessage("edificio.abadia.campo.monje.trabajando") + ")");
                }
            }

            p_afCampo.setSeleccion(oCampoAD.recuperarIdMonjesPorCampo(p_afCampo.getIdCampo(), p_oAbadia.getIdDeAbadia()));
            //cargamos la lista de monjes en el objeto DatosCopiaLibroActForm
            p_afCampo.setMonjes(alMonjes);


        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }


    }

    public Edificio fijarSeleccionMonjesAgricultura(DatosCampoActForm p_afCampo, Abadia p_oAbadia, Usuario p_oUsuario) throws AbadiaException {
        String sTrace = this.getClass() + ".fijarSeleccionMonjesAgricultura()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adEdificio oEdificioAD;
        adCampo oCampoAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            //si se ha seleccionado algún monje...
            if (p_afCampo.getSeleccion() != null) {
                //crear registros de campo_tarea
                oCampoAD = new adCampo(con);
                for (int iCount = 0; iCount < p_afCampo.getSeleccion().length; iCount++) {
                    oCampoAD.crearRegistroCampoMonje(p_afCampo.getIdCampo(), p_oAbadia.getIdDeAbadia(), p_afCampo.getSeleccion()[iCount]);
                }
            }

            oEdificioAD = new adEdificio(con);
            return oEdificioAD.recuperarEdificioTipo(Constantes.EDIFICIO_CAMPO, p_oAbadia, p_oUsuario);

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public void recuperarMonjesParaCopia(String p_szLibroId, DatosCopiaLibroActForm p_afData, Usuario p_oUsuario,
                                         Abadia p_oAbadia, MessageResources p_oResource) throws AbadiaException {
        String sTrace = this.getClass() + ".recuperarMonjesParaCopia()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adLibros oLibroAD;
        adMonje oMonjeAD;

        Libro oLibro;

        int iFranjasOcupadas;
        ArrayList<Monje> alMonjes;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);

            oLibroAD = new adLibros(con);
            iFranjasOcupadas = oLibroAD.getFranjasOcupadas(p_szLibroId);
            //si el libro ya tiene ocupadas 4 franjas horarias, significa que no se pueden poner mas monjes a copiar
            if (iFranjasOcupadas == 4) {
                throw new LibroSaturadoException(sTrace, log);
            } else {
                //recuperamos el objeto libro
                oLibro = oLibroAD.recuperarLibro(Integer.parseInt(p_szLibroId), p_oUsuario.getIdDeIdioma());
                //verificar el estado del libro...
                if (!(oLibro.getEstado() == Constantes.ESTADO_LIBRO_COMPLETO)) {
                    throw new LibroIncompletoException(sTrace, log);
                } else {

                    //invormamos el objeto con el libro recuperado
                    p_afData.setNombreLibro(oLibro.getNombreLibro());
                    //buscamos a los monjes de la abbatia en curso para listar los que están
                    //disponibles para copiar...
                    oMonjeAD = new adMonje(con);
                    alMonjes = oMonjeAD.getMonjesParaCopiar(p_oAbadia.getIdDeAbadia(), p_oResource);
                    //cargamos la lista de monjes en el objeto DatosCopiaLibroActForm
                    p_afData.setMonjes(alMonjes);

                    //recuperamos las disponibilidades del libro
                    p_afData.setEstadoPrima(oLibroAD.getPeriodoLibroOcupado(Integer.parseInt(p_szLibroId), Constantes.PERIODO_PRIMA));
                    p_afData.setEstadoNona(oLibroAD.getPeriodoLibroOcupado(Integer.parseInt(p_szLibroId), Constantes.PERIODO_NONA));
                    p_afData.setEstadoTercia(oLibroAD.getPeriodoLibroOcupado(Integer.parseInt(p_szLibroId), Constantes.PERIODO_TERCIA));
                    p_afData.setEstadoVisperas(oLibroAD.getPeriodoLibroOcupado(Integer.parseInt(p_szLibroId), Constantes.PERIODO_VISPERAS));

                }
            }
            p_afData.setIdLibro(Integer.parseInt(p_szLibroId));

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public HashMap<String, Serializable> seleccionMonjesParaCopia(DatosCopiaLibroActForm p_afData, Usuario p_oUsuario,
                                                                  Abadia p_oAbadia, MessageResources p_oResource, ActionMessages p_amMensajes) throws AbadiaException {
        String sTrace = this.getClass() + ".seleccionMonjesParaCopia()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adLibros oLibroAD;
        adMonje oMonjeAD;
        adEdificio oEdificioAD;
        adViajar oViajarAD;
        adAnimal oAnimalAD;

        Libro oLibro;
        Libro oLibroCopia;
        InfoViajeCopia oInfoViajeCopia;
        ViajarActForm afViaje;
        HashMap<String, Serializable> hmRequest = new HashMap<String, Serializable>();

        String szNombreMonje;
        String szNombrePeriodo = "";
        int iLibroId;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            //evaluar el libro para determinar si se trata de un libro nuestro o un libro de otra abbatia..
            //recuperamos el objeto libro
            oLibroAD = new adLibros(con);
            oLibro = oLibroAD.recuperarLibro(p_afData.getIdLibro(), p_oUsuario.getIdDeIdioma());
            //recupero la lista de monjes del objeto actionform
            oMonjeAD = new adMonje(con);
            szNombreMonje = oMonjeAD.getNomMonje(p_afData.getIdMonje(), p_oResource.getMessage("monjes.abadia.de"));

            //verificar si alguna de las tareas seleccionadas estan bloqueadas
            //para el monje seleccionado mostrando un mensaje de aviso
            for (int iCount = 0; iCount < p_afData.getPeriodo().length; iCount++) {
                if (oMonjeAD.getPeriodoMonjeBloqueado(p_afData.getIdMonje(), p_afData.getPeriodo()[iCount])) {
                    if (p_afData.getPeriodo()[iCount] == Constantes.PERIODO_PRIMA)
                        szNombrePeriodo = p_oResource.getMessage("monjes.trabajos.prima");
                    if (p_afData.getPeriodo()[iCount] == Constantes.PERIODO_TERCIA)
                        szNombrePeriodo = p_oResource.getMessage("monjes.trabajos.tercia");
                    if (p_afData.getPeriodo()[iCount] == Constantes.PERIODO_NONA)
                        szNombrePeriodo = p_oResource.getMessage("monjes.trabajos.nona");
                    if (p_afData.getPeriodo()[iCount] == Constantes.PERIODO_VISPERAS)
                        szNombrePeriodo = p_oResource.getMessage("monjes.trabajos.visperas");

                    p_amMensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.copiar.periodo.bloqueado", szNombreMonje, szNombrePeriodo));
                }
            }

            if (oLibro.getIdAbadia() == p_oAbadia.getIdDeAbadia() && p_amMensajes.isEmpty())
            //si el libro origen está en nuestra abbatia, si inicia la copia directamente
            {
                //evaluamos si el monje ya está copiando ese libro

                iLibroId = oLibroAD.existeCopiaIncompletaTipo(oLibro.getIdLibroTipo(), oLibro.getNivel(), p_oAbadia.getIdDeAbadia(), oLibro.getIdAbadia());
                if (iLibroId == 0) {
                    //crear registro de libro
                    //libroAD = new adLibros();
                    iLibroId = oLibroAD.crearRegistroCopia(oLibro, p_oAbadia);
                    //libroAD.finalize();
                } else {
                    //si ya existe un libro y vamos a continuar con su copia, lo marcamos como que se está copiando para evitar
                    //que otro monje intenten continuar la copia también.
                    //actualizar estado del libro que estamos copiando para evitar que alguien pueda voler a contunucar con esa copia
                    oLibroAD.actualizarEstadoLibro(iLibroId, Constantes.ESTADO_LIBRO_COPIANDOSE);

                }
                //crear registros de libro_tarea
                for (int iCount = 0; iCount < p_afData.getPeriodo().length; iCount++) {
                    oLibroAD.crearRegistroLibroTarea(iLibroId, oLibro.getIdLibro(), p_afData.getIdMonje(), p_afData.getPeriodo()[iCount], Constantes.ESTADO_TAREA_COPIA_ACTIVO);
                }

                //actualizar tareas monje
                for (int iCount = 0; iCount < p_afData.getPeriodo().length; iCount++) {
                    oMonjeAD.actualizarActividad(p_afData.getIdMonje(), p_afData.getPeriodo()[iCount], Constantes.TAREA_COPIAR, 1);
                }

                //request.getSession().removeAttribute("datosCopiaLibro");
                oEdificioAD = new adEdificio(con);
                Edificio edificio = oEdificioAD.recuperarEdificioTipo(Constantes.EDIFICIO_BIBLIOTECA, p_oAbadia, p_oUsuario);

                //debería mostrar una página con el resumen.
                Utilidades.eliminarRegistroContext(p_oUsuario.getNick());
                hmRequest.put("ActionForward", new ActionForward("/mostrarEdificio.do?clave=" + edificio.getIdDeEdificio()));
                //af = new ActionForward("/mostrarEdificio.do?clave=" + edificio.getIdDeEdificio());

            } else if (p_amMensajes.isEmpty()) {
                double importePagar = oLibro.getPrecioCopia();
                //buscamos posibles copias incompletas del libro para ver si ya hemos pagado algo
                // por la copia de ese libro a esa abbatia
                int idLibroCopia = oLibroAD.existeCopiaIncompletaTipo(oLibro.getIdLibroTipo(), oLibro.getNivel(), p_oAbadia.getIdDeAbadia(), oLibro.getIdAbadia());
                //si existe una copia incompleta...
                if (idLibroCopia != 0) {
                    oLibroCopia = oLibroAD.recuperarLibro(idLibroCopia);
                    //recuperamos el importe ya pagado por esa copia para descontarlo del total
                    importePagar = oLibro.getPrecioCopia() - oLibroCopia.getPrecioCopia();
                }

                //creamos un objeto intermedio para guardar los datos del libro y monjes seleccionados en sessión
                oInfoViajeCopia = new InfoViajeCopia();
                oInfoViajeCopia.setIdLibro(oLibro.getIdLibro());
                oInfoViajeCopia.setIdMonje(p_afData.getIdMonje());
                oInfoViajeCopia.setNombreLibro(oLibro.getNombreLibro());
                oInfoViajeCopia.setNombreMonje(szNombreMonje);
                oInfoViajeCopia.setPrecioCopia(importePagar);
                oInfoViajeCopia.setIdAbadiaOrigen(p_oAbadia.getIdDeAbadia());
                oInfoViajeCopia.setIdAbadiaDestino(oLibro.getIdAbadia());
                oInfoViajeCopia.setPeriodo(p_afData.getPeriodo());
                //request.getSession().setAttribute(Constantes.DATOS_SESSION_INFO_VIAJE_COPIA, oInfoViajeCopia);
                hmRequest.put(Constantes.DATOS_SESSION_INFO_VIAJE_COPIA, oInfoViajeCopia);
                //mostrar la pantalla con el resumen de datos de la copia con los costes asociados
                //al viaje y costes de copia para solicitar la confirmacion del usuario.
                afViaje = new ViajarActForm();
                afViaje.setMonjeid(p_afData.getIdMonje());
                afViaje.setAbadiaid_origen(p_oAbadia.getIdDeAbadia());
                afViaje.setAbadiaid_destino(oLibro.getIdAbadia());
                afViaje.setIdLibro(oLibro.getIdLibro());
                afViaje.setNombreLibro(oLibro.getNombreLibro());
                afViaje.setNombreMonje(szNombreMonje);

                oViajarAD = new adViajar(con);
                oViajarAD.recuperarForm(p_oAbadia, afViaje);
                afViaje.setPrecioCopia(Utilidades.redondear(importePagar));


                String optCaballo = "0";
                String msgCaballo = "";
                //debemos verificar si la abbatia destino tiene establo para ofrecer o no la posibilidad de llevar un caballo.
                oEdificioAD = new adEdificio(con);
                int idEdificio = oEdificioAD.recuperarIdEdificioTipo(Constantes.EDIFICIO_ESTABLO, oLibro.getIdAbadia());
                //edificioAD.finalize();
                //si la abbatia destion dipone de establo...
                if (idEdificio != 0) {
                    oAnimalAD = new adAnimal(con);
                    if (oAnimalAD.existeAnimalTipoAdultoLibre(p_oAbadia.getIdDeAbadia(), Constantes.ANIMALES_CABALLO) || oAnimalAD.existeAnimalTipoAdultoLibre(p_oAbadia.getIdDeAbadia(), Constantes.ANIMALES_YEGUA)) {
                        optCaballo = "1";
                    }
                } else {
                    msgCaballo = p_oResource.getMessage("viajar.confirmar.nohayestablo");
                }

                hmRequest.put("msgCaballo", msgCaballo);
                hmRequest.put("Caballo", optCaballo);
                hmRequest.put("ViajarForm", afViaje);

                hmRequest.put("forward", "datosviaje");

            } else if (!p_amMensajes.isEmpty()) {
                //se se han añadido mensajes
                //recargamos la página de lista de monjes para copiar con los mensajes corresondientes.
                //saveMessages(request.getSession(), p_amMensajes);
                hmRequest.put("datosCopiaLibro", p_afData);
                //af = actionMapping.findForward("mostrarmonjes");
                hmRequest.put("forward", "mostrarmonjes");
            }
            return hmRequest;

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public void recuperarMonjesParaSubirNivel(String p_szLibroId, DatosCopiaLibroActForm p_afData, Usuario p_oUsuario,
                                              Abadia p_oAbadia, MessageResources p_oResource) throws AbadiaException {
        String sTrace = this.getClass() + ".recuperarMonjesParaSubirNivel()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adLibros oLibroAD;
        adMonje oMonjeAD;

        Libro oLibro;

        int iFranjasOcupadas;
        ArrayList<Monje> alMonjes;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);

            oLibroAD = new adLibros(con);
            iFranjasOcupadas = oLibroAD.getFranjasOcupadas(p_szLibroId);
            //si el libro ya tiene ocupadas 4 franjas horarias, significa que no se pueden poner mas monjes a copiar
            if (iFranjasOcupadas == 4) {
                throw new LibroSaturadoException(sTrace, log);
            } else {
                //recuperamos el objeto libro
                oLibro = oLibroAD.recuperarLibro(Integer.parseInt(p_szLibroId), p_oUsuario.getIdDeIdioma());
                //verificar el estado del libro...
                if (!(oLibro.getEstado() == Constantes.ESTADO_LIBRO_COMPLETO)) {
                    throw new LibroIncompletoException(sTrace, log);
                }

                if (!oLibroAD.existeSiguienteNivelLibro(oLibro.getIdLibroTipo(), oLibro.getNivel())) {
                    throw new NoExisteSiguienteNivelException(sTrace, log);
                }

                //invormamos el objeto con el libro recuperado
                p_afData.setNombreLibro(oLibro.getNombreLibro());
                //buscamos a los monjes de la abbatia en curso para listar los que están
                //disponibles para copiar...
                oMonjeAD = new adMonje(con);
                alMonjes = oMonjeAD.getMonjesParaCopiar(p_oAbadia.getIdDeAbadia(), p_oResource);
                //cargamos la lista de monjes en el objeto DatosCopiaLibroActForm
                p_afData.setMonjes(alMonjes);

                //recuperamos las disponibilidades del libro
                p_afData.setEstadoPrima(oLibroAD.getPeriodoLibroOcupado(Integer.parseInt(p_szLibroId), Constantes.PERIODO_PRIMA));
                p_afData.setEstadoNona(oLibroAD.getPeriodoLibroOcupado(Integer.parseInt(p_szLibroId), Constantes.PERIODO_NONA));
                p_afData.setEstadoTercia(oLibroAD.getPeriodoLibroOcupado(Integer.parseInt(p_szLibroId), Constantes.PERIODO_TERCIA));
                p_afData.setEstadoVisperas(oLibroAD.getPeriodoLibroOcupado(Integer.parseInt(p_szLibroId), Constantes.PERIODO_VISPERAS));


            }
            p_afData.setIdLibro(Integer.parseInt(p_szLibroId));

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public HashMap<String, Serializable> seleccionMonjesParaSubirNivel(DatosCopiaLibroActForm p_afData, Usuario p_oUsuario,
                                                                       Abadia p_oAbadia, MessageResources p_oResource, ActionMessages p_amMensajes) throws AbadiaException {
        String sTrace = this.getClass() + ".seleccionMonjesParaSubirNivel()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adLibros oLibroAD;
        adMonje oMonjeAD;
        adEdificio oEdificioAD;

        Libro oLibro;
        Edificio oEdificio;
        HashMap<String, Serializable> hmRequest = new HashMap<String, Serializable>();

        String szNombreMonje;
        String szNombrePeriodo = "";
        int iLibroId;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            //evaluar el libro para determinar si se trata de un libro nuestro o un libro de otra abbatia..
            //recuperamos el objeto libro
            oLibroAD = new adLibros(con);
            oLibro = oLibroAD.recuperarLibro(p_afData.getIdLibro(), p_oUsuario.getIdDeIdioma());

            //verificamos si existe la definicion del siguiente nivel para ese libro.
            if (!oLibroAD.existeSiguienteNivelLibro(oLibro.getIdLibroTipo(), oLibro.getNivel())) {
                throw new NoExisteSiguienteNivelException(sTrace, log);
            }

            //recupero la lista de monjes del objeto actionform
            oMonjeAD = new adMonje(con);
            szNombreMonje = oMonjeAD.getNomMonje(p_afData.getIdMonje(), p_oResource.getMessage("monjes.abadia.de"));

            //verificar si alguna de las tareas seleccionadas estan bloqueadas
            //para el monje seleccionado mostrando un mensaje de aviso
            for (int iCount = 0; iCount < p_afData.getPeriodo().length; iCount++) {
                if (oMonjeAD.getPeriodoMonjeBloqueado(p_afData.getIdMonje(), p_afData.getPeriodo()[iCount])) {
                    if (p_afData.getPeriodo()[iCount] == Constantes.PERIODO_PRIMA)
                        szNombrePeriodo = p_oResource.getMessage("monjes.trabajos.prima");
                    if (p_afData.getPeriodo()[iCount] == Constantes.PERIODO_TERCIA)
                        szNombrePeriodo = p_oResource.getMessage("monjes.trabajos.tercia");
                    if (p_afData.getPeriodo()[iCount] == Constantes.PERIODO_NONA)
                        szNombrePeriodo = p_oResource.getMessage("monjes.trabajos.nona");
                    if (p_afData.getPeriodo()[iCount] == Constantes.PERIODO_VISPERAS)
                        szNombrePeriodo = p_oResource.getMessage("monjes.trabajos.visperas");

                    p_amMensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.copiar.periodo.bloqueado", szNombreMonje, szNombrePeriodo));
                }
            }

            if (oLibro.getIdAbadia() == p_oAbadia.getIdDeAbadia() && p_amMensajes.isEmpty())
            //si el libro origen está en nuestra abbatia, si inicia la copia directamente
            {
                //evaluamos si el monje ya está copiando ese libro

                iLibroId = oLibroAD.existeCopiaIncompletaTipo(oLibro.getIdLibroTipo(), oLibro.getNivel() + 1, p_oAbadia.getIdDeAbadia(), oLibro.getIdAbadia());
                if (iLibroId == 0) {
                    //crear registro de libro
                    oLibro.setNivel((short) (oLibro.getNivel() + 1));
                    iLibroId = oLibroAD.crearRegistroCopia(oLibro, p_oAbadia);
                }
                //libroAD.finalize();
                //crear registros de libro_tarea
                for (int iCount = 0; iCount < p_afData.getPeriodo().length; iCount++) {
                    oLibroAD.crearRegistroLibroTarea(iLibroId, oLibro.getIdLibro(), p_afData.getIdMonje(), p_afData.getPeriodo()[iCount], Constantes.ESTADO_TAREA_COPIA_ACTIVO);
                }

                //actualizar tareas monje
                for (int iCount = 0; iCount < p_afData.getPeriodo().length; iCount++) {
                    oMonjeAD.actualizarActividad(p_afData.getIdMonje(), p_afData.getPeriodo()[iCount], Constantes.TAREA_COPIAR, 1);
                }

                //request.getSession().removeAttribute("datosCopiaLibro");
                oEdificioAD = new adEdificio(con);
                oEdificio = oEdificioAD.recuperarEdificioTipo(Constantes.EDIFICIO_BIBLIOTECA, p_oAbadia, p_oUsuario);

                //debería mostrar una página con el resumen.
                Utilidades.eliminarRegistroContext(p_oUsuario.getNick());
                hmRequest.put("ActionForward", new ActionForward("/mostrarEdificio.do?clave=" + oEdificio.getIdDeEdificio()));
            } else if (!p_amMensajes.isEmpty()) {
                //se se han añadido mensajes
                //recargamos la página de lista de monjes para copiar con los mensajes corresondientes.
                hmRequest.put("datosCopiaLibro", p_afData);
                hmRequest.put("forward", "mostrarmonjes");
            }
            return hmRequest;

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public void recuperarTareasCopia(String p_szClaveLibro, DatosCopiaLibroActForm p_afCopiaLibro, Abadia p_oAbadia,
                                     Usuario p_oUsuario, MessageResources p_oResource) throws AbadiaException {
        String sTrace = this.getClass() + ".recuperarTareasCopia()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adLibros oLibroAD;
        adMonje oMonjeAD;
        ArrayList alMonjes;

        Libro oLibro;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oLibroAD = new adLibros(con);
            int franjasOcupadas = oLibroAD.getFranjasOcupadas(p_szClaveLibro);
            //si el libro ya tiene ocupadas 4 franjas horarias, significa que no se pueden poner mas monjes a copiar
            if (franjasOcupadas == 4) {
                throw new LibroSaturadoException(sTrace, log);
            }
            //recuperamos el objeto libro
            oLibro = oLibroAD.recuperarLibro(Integer.parseInt(p_szClaveLibro), p_oUsuario.getIdDeIdioma());
            //invormamos el objeto con el libro recuperado
            p_afCopiaLibro.setNombreLibro(oLibro.getNombreLibro());
            //buscamos a los monjes de la abbatia en curso para listar los que están
            //disponibles para copiar...
            oMonjeAD = new adMonje(con);
            alMonjes = oMonjeAD.getMonjesParaCopiar(p_oAbadia.getIdDeAbadia(), p_oResource);
            //cargamos la lista de monjes en el objeto DatosCopiaLibroActForm
            p_afCopiaLibro.setMonjes(alMonjes);

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public void votarPopularidad(int p_iMonjeId, Abadia p_oAbadia, Usuario p_oUsuario,
                                 String p_szVoto) throws AbadiaException {
        String sTrace = this.getClass() + ".votarPopularidad()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adUtils oUtilsAD;
        adMonje oMonjeAD;
        adUsuario oUsuarioAD;
        adHabilidades oHabilidadesAD;

        Monje oMonje;
        int iSuma = 0;
        double dPopularidad = 0;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);

            // Comprobar si ha votado
            oUtilsAD = new adUtils(con);
            int havotado = oUtilsAD.getSQL("SELECT abadiaid FROM `monje_popularidad_votos` where abadiaid=" + p_oAbadia.getIdDeAbadia() +
                    " and monjeid = " + p_iMonjeId + " and fecha='" + CoreTiempo.getTiempoRealString() + "'", 0);
            // Mensaje de error si ya ha votado
            if (havotado != 0) {
                throw new YaHaVotadoException(sTrace, log);
            } else {
                //control para evitar que se vote la popularidad de un obispo que no es de tu región
                oMonjeAD = new adMonje(con);
                oMonje = oMonjeAD.recuperarDatosMonje(p_iMonjeId);
                //si el monje no es obispo o más..
                if (oMonje.getIdDeJerarquia() < Constantes.JERARQUIA_OBISPO) {
                    //No se puede votar la popularidad de un monje si no es obispo o más...
                    throw new NoEsEminenciaException(sTrace, log);
                }
                //si el monje es obispo, verificamos si es de nuestra región
                if (oMonje.getIdDeJerarquia() == Constantes.JERARQUIA_OBISPO) {
                    //verificamos si es de nuestra región
                    if (oMonje.getIdRegion() != p_oAbadia.getIdDeRegion()) {
                        //dejamos traza con los datos
                        log.info("Bloqueamos el usuario con nick: " + p_oUsuario.getNick() + " querer votar la popularidad de un obispo de otra región.");
                        //bloqueamo la cuenta por 2 días
                        oUsuarioAD = new adUsuario(con);
                        oUsuarioAD.bloquearUsuario(p_oUsuario.getIdDeUsuario(), 2, Constantes.BLOQUEO_VOTO_POPULARIDAD_INCORRECTO);
                        throw new VotoFraudulentoException(sTrace, log);
                    }
                }

                // Controlar los votos por monje
                int existereg = oUtilsAD.getSQL("SELECT abadiaid FROM `monje_popularidad_votos` where abadiaid=" + p_oAbadia.getIdDeAbadia() +
                        " and monjeid = " + p_iMonjeId, 0);
                if (existereg == 0) {
                    oUtilsAD.execSQL("INSERT INTO `monje_popularidad_votos` ( abadiaid, monjeid, fecha ) values (" + p_oAbadia.getIdDeAbadia() +
                            ", " + p_iMonjeId + ",'" + CoreTiempo.getTiempoRealString() + "')");
                } else {
                    oUtilsAD.execSQL("UPDATE `monje_popularidad_votos`  SET fecha = '" + CoreTiempo.getTiempoRealString() + "' " +
                            "WHERE abadiaid=" + p_oAbadia.getIdDeAbadia() + " AND monjeid=" + p_iMonjeId);
                }

                // votaciones
                oHabilidadesAD = new adHabilidades(con);
                if (!oHabilidadesAD.existeHabilidad(p_iMonjeId, Constantes.HABILIDAD_POPULARIDAD)) {
                    oHabilidadesAD.crearHabilidad(p_iMonjeId, Constantes.HABILIDAD_POPULARIDAD, 0);
                }
                dPopularidad = oHabilidadesAD.recuperarHabilidad(p_iMonjeId, Constantes.HABILIDAD_POPULARIDAD);
                if ((p_szVoto.equals("1")) && (dPopularidad <= 99)) {
                    if (dPopularidad == 99) {
                        iSuma = 1;
                    } else {
                        iSuma = 2;
                    }
                    oHabilidadesAD.incrementarHabilidad(p_iMonjeId, Constantes.HABILIDAD_POPULARIDAD, iSuma);
                }
                if ((p_szVoto.equals("0")) && (dPopularidad > 0)) {
                    oHabilidadesAD.decrementarHabilidad(p_iMonjeId, Constantes.HABILIDAD_POPULARIDAD, 1);
                }

            }

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public void reclutarNovicio(Abadia p_oAbadia, Usuario p_oUsuario) throws AbadiaException {
        String sTrace = this.getClass() + ".reclutarNovicio()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adMonje oMonjeAD;
        adEdificio oEdificioAD;
        adLibros oLibroAD;

        int iNumMonjes;
        int iCapacidadEdificio;
        int iDias;
        Edificio oEdificio;
        Libro oLibro;
        Monje oMonje;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oMonjeAD = new adMonje(con);
            iNumMonjes = oMonjeAD.getNumMonjesSinJerarquia(p_oAbadia.getIdDeAbadia());

            oEdificioAD = new adEdificio(con);
            oEdificio = oEdificioAD.recuperarEdificioTipo(Constantes.EDIFICIO_DORMITORIO, p_oAbadia, p_oUsuario);


            oLibroAD = new adLibros(con);
            oLibro = oLibroAD.recuperaLibroTipo(300 + Constantes.EDIFICIO_DORMITORIO, p_oAbadia.getIdDeAbadia());

            if (oLibro != null) {
                iCapacidadEdificio = Utilidades.recuperarIncrementoEspacioEdificioPorLibro(oEdificio.getIdDeTipoDeEdificio(), oLibro.getNivel(), oEdificio.getNivel());
                iCapacidadEdificio = oEdificio.getCapacidadVital() + iCapacidadEdificio;
            } else {
                iCapacidadEdificio = oEdificio.getCapacidadVital();
            }
            //log.info("ReclutarMonjeAction. capacidad: " + edificio.getCapacidadVital());
            //log.info("ReclutarMonjeAction. monjes: " + numMonjes);
            if (iCapacidadEdificio > iNumMonjes) {
                //recuperar datos del último novicio muerto
                oMonje = oMonjeAD.recuperarUltimoNovicioMuerto(p_oAbadia.getIdDeAbadia());

                if (oMonje != null) {
                    iDias = CoreTiempo.getDiferenciaDiasInt(oMonje.getFechaDeFallecimiento(), CoreTiempo.getTiempoAbadiaString());
                    //hace menos de 100 días que murió el último novicio
                    if (iDias < 90) {
                        throw new MuerteMonjeReciente(sTrace, oMonje, log);
                    }
                }
            } else {
                throw new EspacioInsuficienteEnDormitorio(sTrace, log);
            }


        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }
}

