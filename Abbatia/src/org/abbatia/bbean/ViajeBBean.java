package org.abbatia.bbean;

import org.abbatia.actionform.ViajarActForm;
import org.abbatia.adbean.*;
import org.abbatia.bean.*;
import org.abbatia.core.CoreTiempo;
import org.abbatia.dbms.ConnectionFactory;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.*;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.exception.base.ApplicationException;
import org.abbatia.utils.Constantes;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;

import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Benjamín Rodríguez.
 * User: benjamin
 * Date: 24-may-2008
 * Time: 21:06:39
 */
public class ViajeBBean {
    private static Logger log = Logger.getLogger(ViajeBBean.class.getName());

    public Edificio salarAlimento(Usuario p_oUsuario, Abadia p_oAbadia, int p_iAlimentoId, String p_szParameter) throws AbadiaException {
        String sTrace = this.getClass() + ".salarAlimento(" + p_oUsuario.getNick() + "," + p_oAbadia.getIdDeAbadia() + "," + p_iAlimentoId + ")";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adEdificio oEdificioAD;
        adAlimentoLotes oAlimentoLotesAD;

        Edificio oEdificio;
        ArrayList<AlimentoLote> alAlimentos;
        Iterator<AlimentoLote> itAlimentos;
        AlimentoLote oAlimentoLote;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            //recuperamos el edificio que alverga el alimento
            oEdificioAD = new adEdificio(con);
            oAlimentoLotesAD = new adAlimentoLotes(con);
            oEdificio = oEdificioAD.recuperarEdificioPorFamiliaAlimento(p_oAbadia, p_iAlimentoId, p_oUsuario);

            //si la opción es "salar" todo...
            if (p_szParameter.equals("todo")) {
                //recuperamos los lotes disponibles de ese mismo alimento
                alAlimentos = oAlimentoLotesAD.recuperarAlimentosPorID(p_iAlimentoId, oEdificio.getIdDeEdificio());

                itAlimentos = alAlimentos.iterator();
                while (itAlimentos.hasNext()) {
                    oAlimentoLote = itAlimentos.next();
                    oAlimentoLotesAD.salarAlimento(oAlimentoLote, oAlimentoLote.getCantidad(), p_oAbadia.getIdDeAbadia());
                }
            } else if (p_szParameter.equals("lote")) {
                oAlimentoLote = oAlimentoLotesAD.recuperarAlimentoLote(p_iAlimentoId);
                //si el elimento no es salable...
                if (oAlimentoLote.getIdAlimentoSalado() == 0) {
                    throw new AlimentoNoSalableException(sTrace, log);
                }
                oAlimentoLotesAD.salarAlimento(oAlimentoLote, oAlimentoLote.getCantidad(), p_oAbadia.getIdDeAbadia());
            }

            return oEdificio;

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public HashMap<String, Serializable> viajarInicio(ViajarActForm p_afViajar, Abadia p_oAbadia, Usuario p_oUsuario,
                                                      MessageResources p_oResource) throws AbadiaException {
        String sTrace = this.getClass() + ".viajarInicio()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adViajar oViajarAD;
        adAnimal oAnimalAD;
        adEdificio oEdificioAD;
        adInicioContents oInicioContentsAD;
        adMonje oMonjeAD;

        int iNumMonjes;
        HashMap<String, Serializable> hmRequest = new HashMap<String, Serializable>();

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oViajarAD = new adViajar(con);
            oAnimalAD = new adAnimal(con);
            oEdificioAD = new adEdificio(con);
            oInicioContentsAD = new adInicioContents(con);
            oMonjeAD = new adMonje(con);

            // Controlar que el tio no haga la pirula!
            if (!oViajarAD.monjesExperiencia(p_oUsuario, p_oAbadia)) {  //
                throw new ExperienciaInsuficienteException(sTrace, log);
            }


            iNumMonjes = oMonjeAD.getNumMonjes(p_oAbadia.getIdDeAbadia(), Constantes.MONJE_VIVO);
            //si la abbatia tiene menos de 6 monjes, no dejamos viajar a mas..
            if (iNumMonjes < 6) {
                throw new MonjesInsuficientesException(sTrace, log);
            }


            oViajarAD.recuperarForm(p_oAbadia, p_afViajar);

            hmRequest.put("monjes", oViajarAD.getMonjesLista(p_oAbadia, p_oUsuario.getIdDeIdioma(), p_oResource));
            //request.setAttribute("monjes", oViajarAD.getMonjesLista(p_oAbadia, p_oUsuario.getIdDeIdioma(), p_oResource));

            // Recursos de la abbatia
            InicioContents datos = new InicioContents();
            oInicioContentsAD.getRecursos(p_oAbadia, datos);

            hmRequest.put("DatosContents", datos);
            //request.setAttribute("DatosContents", datos);

            String optCaballo = "0";
            String msgCaballo = "";
            //debemos verificar si la abbatia destino tiene establo para ofrecer o no la posibilidad de llevar un caballo.
            int idEdificio = oEdificioAD.recuperarIdEdificioTipo(Constantes.EDIFICIO_ESTABLO, p_afViajar.getAbadiaid_destino());
            //si la abbatia destion dipone de establo...
            if (idEdificio != 0) {
                if (oAnimalAD.existeAnimalTipoAdultoLibre(p_oAbadia.getIdDeAbadia(), Constantes.ANIMALES_CABALLO) || oAnimalAD.existeAnimalTipoAdultoLibre(p_oAbadia.getIdDeAbadia(), Constantes.ANIMALES_YEGUA)) {
                    optCaballo = "1";
                }
            } else {
                msgCaballo = p_oResource.getMessage("viajar.confirmar.nohayestablo");
            }
            hmRequest.put("msgCaballo", msgCaballo);
            hmRequest.put("Caballo", optCaballo);
            return hmRequest;

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public void viajarConfirmacion(ViajarActForm p_afViajar, Abadia p_oAbadia, Usuario p_oUsuario,
                                   MessageResources p_oResource) throws AbadiaException {
        String sTrace = this.getClass() + ".viajarConfirmacion()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adViajar oViajarAD;
        adAnimal oAnimalAD;
        adMonje oMonjeAD;
        adRecurso oRecursoAD;
        adSolicitudes oSolicitudesAD;
        adMensajes oMensajesAD;

        int iNumMonjes;
        Recurso oRecurso;


        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oViajarAD = new adViajar(con);
            oAnimalAD = new adAnimal(con);
            oMonjeAD = new adMonje(con);
            oRecursoAD = new adRecurso(con);
            oSolicitudesAD = new adSolicitudes(con);
            oMensajesAD = new adMensajes(con);

            // Controlar que el tio no haga la pirula!
            if (!oViajarAD.monjesExperiencia(p_oUsuario, p_oAbadia)) {  //
                throw new ExperienciaInsuficienteException(sTrace, log);
            }


            iNumMonjes = oMonjeAD.getNumMonjes(p_oAbadia.getIdDeAbadia(), Constantes.MONJE_VIVO);
            //si la abbatia tiene menos de 6 monjes, no dejamos viajar a mas..
            if (iNumMonjes < 6) {
                throw new MonjesInsuficientesException(sTrace, log);
            }
            //verificamos que el monje seleccionado pertenezca a nuestra abadía
            if (!oMonjeAD.existeMonje(p_oAbadia.getIdDeAbadia(), p_afViajar.getMonjeid())) {
                throw new MonjeIncorrectoException(sTrace, log);
            }

            // Recursos.
            oRecurso = oRecursoAD.recuperarRecurso(Constantes.RECURSOS_ORO, p_oAbadia.getIdDeAbadia());
            double precio = p_afViajar.getTotal();
            if (p_afViajar.getCaballo() != null && p_afViajar.getCaballo().equals("on"))
                precio = p_afViajar.getTotal_caballo();
            if (precio > oRecurso.getCantidad()) {
                throw new OroInsuficienteException(sTrace, log);
            }
            // Tenemos dinero... a viajar
            oRecursoAD.restarRecurso(Constantes.RECURSOS_ORO, p_oAbadia.getIdDeAbadia(), precio);        // Restar las pelas
            p_afViajar.setCoste(precio);
            p_afViajar.setTipoViaje(Constantes.VIAJE_TIPO_SIMPLE);
            // No hay errores
            //se genera el registro de viaje....
            oViajarAD.prepararViaje(p_oAbadia, p_afViajar);
            //generamos una solicitud para la abadía a la que vamos....

            Solicitud solicitud = new Solicitud();
            solicitud.setIdMonje(p_afViajar.getMonjeid());
            solicitud.setIdTipoSolicitud(Constantes.SOLICITUD_TIPO_VIAJE);
            solicitud.setIdAbadia(p_oAbadia.getIdDeAbadia());
            int[] votantes = {p_afViajar.getAbadiaid_destino()};
            solicitud.setVotantes(votantes);
            solicitud.setFechaCreacion(CoreTiempo.getTiempoAbadiaString());
            solicitud.setEstado(Constantes.SOLICITUD_ESTADO_PENDIENTE);
            solicitud.setTexto(p_oResource.getMessage("mensajes.solicitud.viaje.texto", p_afViajar.getAbadia_origen()));

            oSolicitudesAD.crearSolicitud(solicitud);

            if (p_afViajar.getCaballo() != null && p_afViajar.getCaballo().equals("on")) {
                //aqui debemos modificar el estado de un caballo / yegua a 5 (viajando)
                int idAnimal = oAnimalAD.getIdAnimalTipoAdultoLibre(p_oAbadia.getIdDeAbadia(), Constantes.ANIMALES_YEGUA);
                if (idAnimal == 0) {
                    idAnimal = oAnimalAD.getIdAnimalTipoAdultoLibre(p_oAbadia.getIdDeAbadia(), Constantes.ANIMALES_CABALLO);
                }
                //actualizamos el estado del animal a "viajando"
                oAnimalAD.actualizarEstado(idAnimal, Constantes.ESTADO_ANIMAL_VIAJANDO);

                //debemos actualizar la tabla monje_visita_caballo con el identificador del monje y del animal.
                oViajarAD.insertarMonjeVisitaAnimal(p_afViajar.getMonjeid(), idAnimal);
            }

            //generamos un mensaje de notificacion de recepcion de solicitud para la abadía destino
            oMensajesAD.crearMensaje(p_afViajar.getAbadiaid_destino(), -1, -1, p_oUsuario.getIdDeIdioma(), 0, p_oResource.getMessage("mensajes.solicitud.recibida", p_afViajar.getAbadia_origen()));
            oMensajesAD.crearMensaje(p_afViajar.getAbadiaid_origen(), -1, -1, p_oUsuario.getIdDeIdioma(), 0, p_oResource.getMessage("mensajes.solicitud.enviada", p_afViajar.getAbadia_destino()));

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public void viajarParaCopiar(ViajarActForm p_afViajar, InfoViajeCopia p_oInfoViajeCopia, Abadia p_oAbadia,
                                 Usuario p_oUsuario, MessageResources p_oResource) throws AbadiaException {
        String sTrace = this.getClass() + ".viajarParaCopiar()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adViajar oViajarAD;
        adAnimal oAnimalAD;
        adMonje oMonjeAD;
        adRecurso oRecursoAD;
        adSolicitudes oSolicitudesAD;
        adMensajes oMensajesAD;
        adLibros oLibrosAD;

        int iNumMonjes;
        Recurso oRecurso;


        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oViajarAD = new adViajar(con);
            oMonjeAD = new adMonje(con);
            // Controlar que el tio no haga la pirula!
            if (!oViajarAD.monjesExperiencia(p_oUsuario, p_oAbadia)) {  //
                throw new ExperienciaInsuficienteException(sTrace, log);
                //mensajes.add("msg", new ActionMessage("viajar.confirmar.error.sinexperiencia"));
                //saveMessages(request.getSession(), mensajes);
                //return mapping.findForward("mensajes");
            }


            iNumMonjes = oMonjeAD.getNumMonjes(p_oAbadia.getIdDeAbadia(), Constantes.MONJE_VIVO);
            //si la abbatia tiene menos de 6 monjes, no dejamos viajar a mas..
            if (iNumMonjes < 6) {
                throw new MonjesInsuficientesException(sTrace, log);
                //mensajes.add("msg", new ActionMessage("viajar.confirmar.error.pocosmonjes"));
                //saveMessages(request.getSession(), mensajes);
                //return mapping.findForward("mensajes");
            }

            // Recursos.
            oRecursoAD = new adRecurso(con);
            oRecurso = oRecursoAD.recuperarRecurso(Constantes.RECURSOS_ORO, p_oAbadia.getIdDeAbadia());

            double precio = p_afViajar.getTotal();
            if (p_afViajar.getCaballo() != null && p_afViajar.getCaballo().equals("on"))
                precio = p_afViajar.getTotal_caballo();

            //sumamos el coste de copia del libro
            precio = precio + p_oInfoViajeCopia.getPrecioCopia();

            if (precio > oRecurso.getCantidad()) {
                throw new OroInsuficienteException(sTrace, log);
                //mensajes.add("msg", new ActionMessage("viajar.confirmar.sindinero"));
                //saveMessages(request.getSession(), mensajes);
                //return mapping.findForward("mensajes");
            }
            // Tenemos dinero... a viajar
            oRecursoAD.restarRecurso(Constantes.RECURSOS_ORO, p_oAbadia.getIdDeAbadia(), precio);        // Restar las pelas
            p_afViajar.setCoste(precio);
            // No hay errores
            //se genera el registro de viaje....
            p_afViajar.setTipoViaje(Constantes.VIAJE_TIPO_COPIA);
            oViajarAD.prepararViaje(p_oAbadia, p_afViajar);
            //generamos una solicitud para la abadía a la que vamos....

            Solicitud solicitud = new Solicitud();
            solicitud.setIdMonje(p_oInfoViajeCopia.getIdMonje());
            solicitud.setIdTipoSolicitud(Constantes.SOLICITUD_TIPO_VIAJE_COPIA);
            solicitud.setIdAbadia(p_oAbadia.getIdDeAbadia());
            int[] votantes = {p_oInfoViajeCopia.getIdAbadiaDestino()};
            solicitud.setVotantes(votantes);
            solicitud.setFechaCreacion(CoreTiempo.getTiempoAbadiaString());
            solicitud.setEstado(Constantes.SOLICITUD_ESTADO_PENDIENTE);

            //recuperamos la descripcion de los periodos en los que solicitamos copiar el libro
            String sPeriodos = "";
            for (int iCount = 0; iCount < p_oInfoViajeCopia.getPeriodo().length; iCount++) {
                if (p_oInfoViajeCopia.getPeriodo()[iCount] == Constantes.PERIODO_PRIMA) {
                    sPeriodos = sPeriodos.concat(Constantes.PERIODO_PRIMA_DESC);
                } else if (p_oInfoViajeCopia.getPeriodo()[iCount] == Constantes.PERIODO_TERCIA) {
                    sPeriodos = sPeriodos.concat(Constantes.PERIODO_TERCIA_DESC);
                } else if (p_oInfoViajeCopia.getPeriodo()[iCount] == Constantes.PERIODO_NONA) {
                    sPeriodos = sPeriodos.concat(Constantes.PERIODO_NONA_DESC);
                } else if (p_oInfoViajeCopia.getPeriodo()[iCount] == Constantes.PERIODO_VISPERAS) {
                    sPeriodos = sPeriodos.concat(Constantes.PERIODO_VISPERAS_DESC);
                }
                if (!sPeriodos.equals("")) sPeriodos = sPeriodos.concat(", ");
            }
            sPeriodos = sPeriodos.substring(0, sPeriodos.lastIndexOf(", "));

            solicitud.setTexto(p_oResource.getMessage("mensajes.solicitud.viaje.copia.texto", new String[]{p_afViajar.getAbadia_origen(), p_oInfoViajeCopia.getNombreMonje(), p_oInfoViajeCopia.getNombreLibro(), sPeriodos}));

            oSolicitudesAD = new adSolicitudes(con);
            oSolicitudesAD.crearSolicitud(solicitud);

            //creamos los registros necesarios para bloquear las actividades del monje y las tareas del libro
            //en espera la autorizacion

            //crear registros de libro_tarea
            oLibrosAD = new adLibros(con);
            for (int iCount = 0; iCount < p_oInfoViajeCopia.getPeriodo().length; iCount++) {
                //temporalmente, pasamos como identificador de libro/copia el id del monje
                oLibrosAD.crearRegistroLibroTarea(0, p_oInfoViajeCopia.getIdLibro(), p_oInfoViajeCopia.getIdMonje(), p_oInfoViajeCopia.getPeriodo()[iCount], Constantes.ESTADO_TAREA_COPIA_PENDIENTE);
            }

            //actualizar tareas monje
            for (int iCount = 0; iCount < p_oInfoViajeCopia.getPeriodo().length; iCount++) {
                oMonjeAD.actualizarActividad(p_oInfoViajeCopia.getIdMonje(), p_oInfoViajeCopia.getPeriodo()[iCount], Constantes.TAREA_COPIAR, 1);
            }

            if (p_afViajar.getCaballo() != null && p_afViajar.getCaballo().equals("on")) {
                //aqui debemos modificar el estado de un caballo / yegua a 5 (viajando)
                oAnimalAD = new adAnimal(con);
                int idAnimal = oAnimalAD.getIdAnimalTipoAdultoLibre(p_oAbadia.getIdDeAbadia(), Constantes.ANIMALES_YEGUA);
                if (idAnimal == 0) {
                    idAnimal = oAnimalAD.getIdAnimalTipoAdultoLibre(p_oAbadia.getIdDeAbadia(), Constantes.ANIMALES_CABALLO);
                }
                //actualizamos el estado del animal a "viajando"
                oAnimalAD.actualizarEstado(idAnimal, Constantes.ESTADO_ANIMAL_VIAJANDO);

                //debemos actualizar la tabla monje_visita_caballo con el identificador del monje y del animal.
                oViajarAD.insertarMonjeVisitaAnimal(p_afViajar.getMonjeid(), idAnimal);
            }

            //generamos un mensaje de notificacion de recepcion de solicitud para la abadía destino
            oMensajesAD = new adMensajes(con);
            oMensajesAD.crearMensaje(p_afViajar.getAbadiaid_destino(), -1, -1, p_oUsuario.getIdDeIdioma(), 0, p_oResource.getMessage("mensajes.solicitud.copia.recibida", p_afViajar.getAbadia_origen(), p_oInfoViajeCopia.getNombreLibro()));
            oMensajesAD.crearMensaje(p_afViajar.getAbadiaid_origen(), -1, -1, p_oUsuario.getIdDeIdioma(), 0, p_oResource.getMessage("mensajes.solicitud.copia.enviada", p_afViajar.getAbadia_destino(), p_oInfoViajeCopia.getNombreLibro()));

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public Object gestionVisita(int p_iMonjeId, String p_szParameter, String p_szAccion, Abadia p_oAbadia, Usuario p_oUsuario,
                                MessageResources p_oResource, ActionMessages p_amMensajes) throws AbadiaException {
        String sTrace = this.getClass() + ".gestionVisita()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adMonje oMonjeAD;
        adViajar oViajarAD;
        adAbadia oAbadiaAD;
        adMensajes oMensajesAD;

        Monje oMonje;
        ArrayList<Mensajes> alMensajes = new ArrayList<Mensajes>();
        Confirmacion oConfirmacion;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oMonjeAD = new adMonje(con);
            oViajarAD = new adViajar(con);
            oAbadiaAD = new adAbadia(con);
            oMensajesAD = new adMensajes(con);

            oMonje = oMonjeAD.recuperarMonje(p_iMonjeId, p_oAbadia.getIdDeAbadia(), p_oResource, p_oUsuario.getIdDeIdioma());

            if (!oViajarAD.chkMonjeSePuedeVisualizar(p_oAbadia, oMonje)) {
                throw new ApplicationException(sTrace, null, log);
                //return mapping.findForward("failure");
            }
            if (!oViajarAD.puedeForzarVuelta(p_oAbadia.getIdDeAbadia(), oMonje.getIdMonje())) {
                p_amMensajes.add("msg", new ActionMessage("viajar.marchar.abbatia.conclave"));
                throw new ApplicationException(sTrace, null, log);
            } else {
                //meter control de validación con pantalla intermedia
                //solicitamos confirmacion.
                if (p_szParameter.equals("inicio")) {
                    oConfirmacion = new Confirmacion();
                    //cuidado, si decides arar el campo, perderás la cosecha actual, estás seguro....
                    //mostrar dialogo estándar con opciones si o no.
                    //accion si
                    oConfirmacion.setAccionSi("/visitaConfirmar.do?clave=" + oMonje.getIdMonje() + "&action=" + p_szAccion);
                    //accion no
                    oConfirmacion.setAccionNo("/listarMonjes.do");

                    //titulo del diálogo
                    if (p_szAccion.equals("expulsar")) {
                        oConfirmacion.setTitulo(p_oResource.getMessage("mensajes.aviso.expulsar.monje.titulo"));
                        oConfirmacion.setTextoConfirmacion(p_oResource.getMessage("mensajes.aviso.expulsar.monje.visita.texto", oMonje.getNombre()));
                    } else {
                        oConfirmacion.setTitulo(p_oResource.getMessage("mensajes.aviso.abandonar.monje.titulo"));
                        oConfirmacion.setTextoConfirmacion(p_oResource.getMessage("mensajes.aviso.abandonar.monje.visita.texto", oMonje.getNombre()));
                    }

                    return oConfirmacion;
                    //request.setAttribute("DatosConfirmacion", oConfirmacion);

                    //return mapping.findForward("confirmar");
                } else if (p_szParameter.equals("confirmar")) {
                    oViajarAD.forzarVuelta(p_oAbadia, oMonje, p_szAccion, p_oResource);
                    if (p_szAccion.equals("expulsar")) {
                        //generar mensajes para la abadía que expulsa y la abadía del monje expulsado.
                        //has expulsado a {0} , forzando su vuelta a la abadía {1}, en breve iniciará su viaje
                        //{0} ha sido expulsado de la abadía {1} y se prepara para su regreso
                        /*
                        mensajes.viaje.expulsar.origen=Has expulsado a {0} , forzando su vuelta a la abadía {1}, en breve iniciará su viaje
                        mensajes.viaje.expulsar.destino={0} ha sido expulsado de la abadía {1} y se prepara para su regreso
                        */
                        alMensajes.add(new Mensajes(p_oAbadia.getIdDeAbadia(), -1, p_oResource.getMessage("mensajes.viaje.expulsar.origen", oMonje.getNombre(), oAbadiaAD.getNomAbadia(oMonje.getIdAbadia())), 1));
                        alMensajes.add(new Mensajes(oMonje.getIdAbadia(), -1, p_oResource.getMessage("mensajes.viaje.expulsar.destino", oMonje.getNombre(), p_oAbadia.getNombre()), 1));
                        p_amMensajes.add("msg", new ActionMessage("viajar.expulsion.abbatia"));
                    } else {
                        //generar mensajes para la abadía que expulsa y la abadía del monje expulsado.
                        //{0} de la abadía {1} ha decicido abandonar tu abadía, en breve iniciará su viaje de vuelta
                        //{0} inicia su viaje de regreso a casa desde la abadía {1}
                        /*
                        mensajes.viaje.marcharse.origen={0} de la abadía {1} ha decicido abandonar tu abadía, en breve iniciará su viaje de vuelta
                        mensajes.viaje.marcharse.destino={0} inicia su viaje de regreso a casa desde la abadía {1}
                        */
                        alMensajes.add(new Mensajes(p_oAbadia.getIdDeAbadia(), -1, p_oResource.getMessage("mensajes.viaje.marcharse.origen", oMonje.getNombre(), oAbadiaAD.getNomAbadia(oMonje.getIdAbadia())), 1));
                        alMensajes.add(new Mensajes(oMonje.getIdAbadia(), -1, p_oResource.getMessage("mensajes.viaje.marcharse.destino", oMonje.getNombre(), p_oAbadia.getNombre()), 1));
                        p_amMensajes.add("msg", new ActionMessage("viajar.marchar.abbatia"));
                    }
                    oMensajesAD.crearMensajes(alMensajes);
                }
                return null;
            }
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }


    }
}