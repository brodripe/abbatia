package org.abbatia.bbean;

import org.abbatia.actionform.LibroDetalleActForm;
import org.abbatia.adbean.adEdificio;
import org.abbatia.adbean.adLibros;
import org.abbatia.adbean.adMonje;
import org.abbatia.adbean.adRecurso;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Edificio;
import org.abbatia.bean.Libro;
import org.abbatia.bean.Usuario;
import org.abbatia.dbms.ConnectionFactory;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.RestauracionLibroException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.HTML;
import org.abbatia.utils.Utilidades;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Benjamín Rodríguez.
 * User: benjamin
 * Date: 24-may-2008
 * Time: 21:06:39
 */
public class LibroBBean {
    private static Logger log = Logger.getLogger(LibroBBean.class.getName());

    public LibroDetalleActForm recuperarDetalleLibro(int p_iLibroId, Usuario p_oUsuario, MessageResources resource) throws AbadiaException {
        String sTrace = this.getClass() + ".recuperarDetalleLibro()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adLibros oLibroAD;
        LibroDetalleActForm afLibroDetalle = new LibroDetalleActForm();
        Libro oLibro;
        int iDeterioro;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oLibroAD = new adLibros(con);
            oLibro = oLibroAD.recuperarDetalleLibro(p_iLibroId, p_oUsuario.getIdDeIdioma());
            if (oLibro != null) {
                afLibroDetalle.setIdLibro(oLibro.getIdLibro());
                afLibroDetalle.setNombreLibro(oLibro.getNombreLibro());
                afLibroDetalle.setDescLibro(oLibro.getDescLibro());
                afLibroDetalle.setAbadiaid(oLibro.getIdAbadia());
                afLibroDetalle.setIdLibroTipo(oLibro.getIdLibroTipo());
                afLibroDetalle.setNivel(oLibro.getNivel());
                afLibroDetalle.setIdIdioma(oLibro.getIdIdioma());
                afLibroDetalle.setNumPaginas(oLibro.getNumPaginas());
                afLibroDetalle.setAbadiaDesc_copia(oLibro.getNombreAbadia_copia() + " (" + oLibro.getNombreRegion() + ")");
                afLibroDetalle.setIdioma_desc(oLibro.getIdioma_desc());
                afLibroDetalle.setDesgaste(oLibro.getDesgaste());
                afLibroDetalle.setGrafico(oLibro.getGrafico());
                afLibroDetalle.setPrecioMax(Utilidades.redondear(oLibro.getPrecioMax()));
                afLibroDetalle.setPrecioMin(Utilidades.redondear(oLibro.getPrecioMin()));
                afLibroDetalle.setPrecioCopia(oLibro.getPrecioCopia());
                afLibroDetalle.setPrecioCopiaS(Utilidades.redondear(oLibro.getPrecioCopia()));

                //calculamos el grado de deterioro del libro
                iDeterioro = (int) (afLibroDetalle.getDesgaste()) / 10;
                afLibroDetalle.setDeterioro(HTML.smallBarraRed(iDeterioro, resource.getMessage("edificio.abadia.biblioteca.altdeterioro", Utilidades.redondear(afLibroDetalle.getDesgaste()), String.valueOf(100))));
            }
            return afLibroDetalle;

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public LibroDetalleActForm recuperarDetalleLibroConCopias(String p_sLibroId, Usuario p_oUsuario, MessageResources resource) throws AbadiaException {
        String sTrace = this.getClass() + ".recuperarDetalleLibro()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adLibros oLibroAD;
        LibroDetalleActForm afLibroDetalle;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oLibroAD = new adLibros(con);
            afLibroDetalle = oLibroAD.recuperarDetalleLibro(Integer.parseInt(p_sLibroId), p_oUsuario.getIdDeIdioma(), resource);
            afLibroDetalle.setDetalles(oLibroAD.recuperarCopiasEnCurso(Integer.parseInt(p_sLibroId), p_oUsuario.getIdDeIdioma(), resource));
            return afLibroDetalle;

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public Edificio ActualizarPrecioCopia(Abadia p_oAbadia, Usuario p_oUsuario, LibroDetalleActForm afLibroDetalle) throws AbadiaException {
        String sTrace = this.getClass() + ".ActualizarPrecioCopia()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adLibros oLibroAD;
        adEdificio oEdificioAD;

        Edificio oEdificio;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oLibroAD = new adLibros(con);
            oLibroAD.actualizarPrecioCopia(afLibroDetalle.getIdLibro(), afLibroDetalle.getPrecioCopia());

            oEdificioAD = new adEdificio(con);
            oEdificio = oEdificioAD.recuperarEdificioTipo(Constantes.EDIFICIO_BIBLIOTECA, p_oAbadia, p_oUsuario);
            return oEdificio;

        } catch (AbadiaException e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public ActionForward cancelarRestauracion(int p_iLibroId, Usuario p_oUsuario, Abadia p_oAbadia, ActionMessages p_amMensajes, ActionMapping p_amMapping) throws AbadiaException {
        String sTrace = this.getClass() + ".cancelarRestauracion()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adLibros oLibroAD;
        adEdificio oEdificioAD;

        Libro oLibro;

        ActionForward af;

        Edificio oEdificio;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            //recuperamos el objeto libro
            oLibroAD = new adLibros(con);
            oLibro = oLibroAD.recuperarLibro(p_iLibroId, p_oUsuario.getIdDeIdioma());
            //verificamos que se trata de un libro de nuestra abbatia
            if (oLibro.getIdAbadia() == p_oAbadia.getIdDeAbadia()) {
                //verificamos si el estado del libro permite la cancelación de su restauración (En restauración = 8)
                if (oLibro.getEstado() == Constantes.ESTADO_LIBRO_RESTAURANDO) {
                    //verificamos el nivel de deterioro del libro
                    //si el desgaste supera los 50 puntos...
                    if (oLibro.getDesgaste() > 50) {
                        //marcamos el libro como Deteriorado
                        oLibroAD.actualizarEstadoLibro(oLibro.getIdLibro(), Constantes.ESTADO_LIBRO_DETERIORADO);
                    } else // si el desgaste el inferior o igual a 50..
                    {
                        //marcamos el libro como Completo
                        oLibroAD.actualizarEstadoLibro(oLibro.getIdLibro(), Constantes.ESTADO_LIBRO_COMPLETO);
                    }

                    oEdificioAD = new adEdificio(con);
                    oEdificio = oEdificioAD.recuperarEdificioTipo(Constantes.EDIFICIO_BIBLIOTECA, p_oAbadia, p_oUsuario);

                    //debería mostrar una página con el resumen.
                    Utilidades.eliminarRegistroContext(p_oUsuario.getNick());
                    af = new ActionForward("/mostrarEdificio.do?clave=" + oEdificio.getIdDeEdificio());
                } else {
                    p_amMensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.libro.abandonarrestauracion.estadoincorrecto", oLibro.getDescEstado()));
                    af = p_amMapping.findForward("mensajes");
                }
            } else {
                p_amMensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.libro.restaurar.abadiaincorrecta"));
                af = p_amMapping.findForward("mensajes");
            }
            return af;

        } catch (AbadiaException e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public ActionForward cancelarRestauraciones(Usuario p_oUsuario, Abadia p_oAbadia, ActionMessages p_amMensajes, ActionMapping p_amMapping, MessageResources p_oResource) throws AbadiaException {
        String sTrace = this.getClass() + ".cancelarRestauraciones()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adLibros oLibroAD;
        adEdificio oEdificioAD;

        List<Libro> alLibros;

        ActionForward af;

        Edificio oEdificio;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            //recuperamos el objeto libro
            oLibroAD = new adLibros(con);
            alLibros = oLibroAD.recuperarLibrosPropios(p_oAbadia, p_oUsuario, p_oResource);
            for (Libro oLibro : alLibros) {
                //verificamos si el estado del libro permite la cancelación de su restauración (En restauración = 8)
                if (oLibro.getEstado() == Constantes.ESTADO_LIBRO_RESTAURANDO) {
                    //verificamos el nivel de deterioro del libro
                    //si el desgaste supera los 50 puntos...
                    if (oLibro.getDesgaste() > 50) {
                        //marcamos el libro como Deteriorado
                        oLibroAD.actualizarEstadoLibro(oLibro.getIdLibro(), Constantes.ESTADO_LIBRO_DETERIORADO);
                    } else // si el desgaste el inferior o igual a 50..
                    {
                        //marcamos el libro como Completo
                        oLibroAD.actualizarEstadoLibro(oLibro.getIdLibro(), Constantes.ESTADO_LIBRO_COMPLETO);
                    }

                }
            }

            oEdificioAD = new adEdificio(con);
            oEdificio = oEdificioAD.recuperarEdificioTipo(Constantes.EDIFICIO_BIBLIOTECA, p_oAbadia, p_oUsuario);
            //debería mostrar una página con el resumen.
            Utilidades.eliminarRegistroContext(p_oUsuario.getNick());
            af = new ActionForward("/mostrarEdificio.do?clave=" + oEdificio.getIdDeEdificio());

            return af;

        } catch (AbadiaException e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public ActionForward eliminarLibro(int p_iLibroId, Abadia p_oAbadia, Usuario p_oUsuario, ActionMessages p_oMensajes, ActionMapping p_amActionMapping) throws AbadiaException {
        String sTrace = this.getClass() + ".eliminarLibro()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adLibros oLibroAD;
        adMonje oMonjeAD;
        adEdificio oEdificioAD;

        Libro oLibro;
        Edificio oEdificio;
        int iMonjeId;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS, Constantes.AUTOCOMIT_OF);
            //recuperamos el objeto libro
            oLibroAD = new adLibros(con);
            oLibro = oLibroAD.recuperarLibro(p_iLibroId, p_oUsuario.getIdDeIdioma());

            //verificamos que se trata de un libro de nuestra abbatia
            if (oLibro.getIdAbadia() == p_oAbadia.getIdDeAbadia()) {
                //verificamos si el estado del libro permite su eliminacion
                if (oLibro.getEstado() == Constantes.ESTADO_LIBRO_COPIANDOSE || oLibro.getEstado() == Constantes.ESTADO_LIBRO_PENDIENTE || oLibro.getEstado() == Constantes.ESTADO_LIBRO_INCOMPLETO) {
                    //recuperamos el id de monje que esta copiando el libro
                    iMonjeId = oLibroAD.recuperarMonjePorLibroid(oLibro.getIdLibro());
                    //libroAD.finalize();
                    //recuperamos un array con las tareas que el monje estaba dedicando a la copia de ese libro
                    ArrayList<Integer> tareas = oLibroAD.recupearTareasPorMonjeLibro(oLibro.getIdLibro(), iMonjeId);
                    //eliminamos registros de libro_tarea asociados a ese libro
                    oLibroAD.eliminarTareasPorMonjeLibro(iMonjeId, oLibro.getIdLibro());
                    //eliminamos registro libro
                    oLibroAD.eliminarLibro(oLibro.getIdLibro());
                    //desbloqueamos actividades de copia
                    oMonjeAD = new adMonje(con);
                    for (int tarea : tareas) {
                        oMonjeAD.actualizarActividad(iMonjeId, tarea, Constantes.TAREA_NINGUNA, 0);
                    }

                    oEdificioAD = new adEdificio(con);
                    oEdificio = oEdificioAD.recuperarEdificioTipo(Constantes.EDIFICIO_BIBLIOTECA, p_oAbadia, p_oUsuario);

                    //debería mostrar una página con el resumen.
                    Utilidades.eliminarRegistroContext(p_oUsuario.getNick());
                    ConnectionFactory.commitTransaction(con);
                    return new ActionForward("/mostrarEdificio.do?clave=" + oEdificio.getIdDeEdificio());
                } else {
                    p_oMensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.libro.eliminar.estadoincorrecto", oLibro.getDescEstado()));
                    //saveMessages(request.getSession(), mensajes);
                    return p_amActionMapping.findForward("mensajes");
                }
            } else {
                p_oMensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.libro.eliminar.abadiaincorrecta"));
                //saveMessages(request.getSession(), mensajes);
                return p_amActionMapping.findForward("mensajes");
            }

        } catch (AbadiaException e) {
            ConnectionFactory.rollbackTransaction(con);
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public ActionForward eliminarPeriodoCopia(int p_iLibroId, int p_iPeriodoId, Usuario p_oUsuario,
                                              Abadia p_oAbadia, ActionMessages p_oMensajes, ActionMapping p_amMapping) throws AbadiaException {
        String sTrace = this.getClass() + ".eliminarPeriodoCopia()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adLibros oLibroAD;
        adMonje oMonjeAD;

        Libro oLibro;
        int iMonjeId;
        ActionForward afReturn;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS, Constantes.AUTOCOMIT_OF);
            //recuperamos el objeto libro
            oLibroAD = new adLibros(con);
            oLibro = oLibroAD.recuperarLibro(p_iLibroId, p_oUsuario.getIdDeIdioma());

            //verificamos que se trata de un libro de nuestra abbatia
            if (oLibro.getIdAbadia() == p_oAbadia.getIdDeAbadia()) {
                //verificamos si el estado del libro permite su eliminacion
                if (oLibro.getEstado() == Constantes.ESTADO_LIBRO_COPIANDOSE || oLibro.getEstado() == Constantes.ESTADO_LIBRO_PENDIENTE) {
                    //libroAD = new adLibros();
                    //recuperamos el id de monje que esta copiando el libro
                    iMonjeId = oLibroAD.recuperarMonjePorLibroid(oLibro.getIdLibro());
                    //libroAD.finalize();
                    //eliminamos registros de libro_tarea asociados a ese libro
                    oLibroAD.eliminarTareaPorMonjeLibro(iMonjeId, oLibro.getIdLibro(), p_iPeriodoId);

                    //recuperamos un vector con las tareas que el monje estaba dedicando a la copia de ese libro
                    ArrayList<Integer> tareas = oLibroAD.recupearTareasPorMonjeLibro(oLibro.getIdLibro(), iMonjeId);

                    //si el libro está en estado pendiente, no tiene ninguna pagina copiada  y no tienen a ningun monje deberiamos borrarlo.
                    if ((oLibro.getEstado() == Constantes.ESTADO_LIBRO_PENDIENTE) && (tareas.size() == 0) && (oLibro.getNumPaginasCopiadas() == 0)) {
                        //eliminamos registro libro
                        oLibroAD.eliminarLibro(oLibro.getIdLibro());
                    }
                    //si el libro esta copiandose y le quitamos todos los monjes deberia quedar marcado como "incompleto"
                    if (((oLibro.getEstado() != Constantes.ESTADO_LIBRO_PENDIENTE) && (tareas.size() == 0)) ||
                            ((oLibro.getEstado() == Constantes.ESTADO_LIBRO_PENDIENTE) && (tareas.size() == 0) && (oLibro.getNumPaginasCopiadas() > 0))) {
                        oLibro.setEstado(Constantes.ESTADO_LIBRO_INCOMPLETO);
                        oLibroAD.actualizarLibro(oLibro);
                    }

                    //desbloqueamos actividad de copia
                    oMonjeAD = new adMonje(con);
                    oMonjeAD.actualizarActividad(iMonjeId, p_iPeriodoId, Constantes.TAREA_NINGUNA, 0);

                    //debería mostrar una página con el resumen.
                    Utilidades.eliminarRegistroContext(p_oUsuario.getNick());
                    afReturn = new ActionForward("/mostrarDetalleLibro.do?clave=" + oLibro.getIdLibro_origen());
                } else {
                    p_oMensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.libro.eliminar.estadoincorrecto", oLibro.getDescEstado()));
                    //saveMessages(request.getSession(), mensajes);
                    afReturn = p_amMapping.findForward("mensajes");
                }
            } else {
                p_oMensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.libro.eliminar.libroenotraabadia"));
                //saveMessages(request.getSession(), mensajes);
                afReturn = p_amMapping.findForward("mensajes");
            }
            ConnectionFactory.commitTransaction(con);
            return afReturn;


        } catch (AbadiaException e) {
            ConnectionFactory.rollbackTransaction(con);
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public ActionForward encuadernarLibro(int p_iLibroId, Abadia p_oAbadia, Usuario p_oUsuario,
                                          ActionMessages p_oMensajes, ActionMapping p_amMapping) throws AbadiaException {
        String sTrace = this.getClass() + ".encuadernarLibro()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adLibros oLibroAD;
        adRecurso oRecursoAD;
        adEdificio oEdificioAD;

        ActionForward afReturn;
        Libro oLibro;
        double dCantidadPiel;
        Edificio oEdificio;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS, Constantes.AUTOCOMIT_OF);
            //recuperamos el objeto libro
            oLibroAD = new adLibros(con);
            oLibro = oLibroAD.recuperarLibro(p_iLibroId, p_oUsuario.getIdDeIdioma());

            //verificamos que se trata de un libro de nuestra abbatia
            if (oLibro.getIdAbadia() == p_oAbadia.getIdDeAbadia()) {
                //verificamos si el estado del libro permite su encuadernación
                if (oLibro.getEstado() == Constantes.ESTADO_LIBRO_SIN_ENCUADERNAR) {
                    //recuperamos el recurso necesario para la encuadernacion
                    oRecursoAD = new adRecurso(con);
                    dCantidadPiel = oRecursoAD.recuperarValorRecurso(Constantes.RECURSOS_PIEL_VACUNA, p_oAbadia.getIdDeAbadia());

                    //recuperar cantidad de piel necesaria para encuadernar...
                    if (dCantidadPiel >= oLibro.getCtdPielEncuadernar()) {
                        //restamos el recurso
                        oRecursoAD.restarRecurso(Constantes.RECURSOS_PIEL_VACUNA, p_oAbadia.getIdDeAbadia(), oLibro.getCtdPielEncuadernar());

                        //actualizamos el estado del libro
                        oLibroAD.actualizarEstadoLibro(oLibro.getIdLibro(), Constantes.ESTADO_LIBRO_COMPLETO);

                        oEdificioAD = new adEdificio(con);
                        oEdificio = oEdificioAD.recuperarEdificioTipo(Constantes.EDIFICIO_BIBLIOTECA, p_oAbadia, p_oUsuario);

                        //debería mostrar una página con el resumen.
                        Utilidades.eliminarRegistroContext(p_oUsuario.getNick());
                        afReturn = new ActionForward("/mostrarEdificio.do?clave=" + oEdificio.getIdDeEdificio());

                    } else {
                        p_oMensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.libro.pielinsuficiente", Utilidades.redondear(oLibro.getCtdPielEncuadernar())));
                        //saveMessages(request.getSession(), mensajes);
                        afReturn = p_amMapping.findForward("mensajes");
                    }

                } else {
                    p_oMensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.libro.encuadernar.estadoincorrecto", oLibro.getDescEstado()));
                    //saveMessages(request.getSession(), mensajes);
                    afReturn = p_amMapping.findForward("mensajes");
                }
            } else {
                p_oMensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.libro.encuadernar.abadiaincorrecta"));
                //saveMessages(request.getSession(), mensajes);
                afReturn = p_amMapping.findForward("mensajes");
            }

            ConnectionFactory.commitTransaction(con);
            return afReturn;

        } catch (AbadiaException e) {
            ConnectionFactory.rollbackTransaction(con);
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public ArrayList propagarLibro(int p_iLibroId, Usuario p_oUsuario) throws AbadiaException {
        String sTrace = this.getClass() + ".propagarLibro()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adLibros oLibroAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oLibroAD = new adLibros(con);
            oLibroAD.generarLibroPorRegion(p_iLibroId);

            return oLibroAD.recuperarLibrosTipo(p_oUsuario.getIdDeIdioma());

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public ArrayList recuperarLibrosPropagar(Usuario p_oUsuario) throws AbadiaException {
        String sTrace = this.getClass() + ".recuperarLibrosPropagar()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adLibros oLibroAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oLibroAD = new adLibros(con);
            return oLibroAD.recuperarLibrosTipo(p_oUsuario.getIdDeIdioma());

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public ActionForward restaurarLibro(int p_iLibroId, Abadia p_oAbadia, Usuario p_oUsuario,
                                        ActionMessages p_amMensajes) throws AbadiaException {
        String sTrace = this.getClass() + ".restaurarLibro()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adLibros oLibroAD;
        adEdificio oEdificioAD;

        Libro oLibro;
        Edificio oEdificio;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oLibroAD = new adLibros(con);
            oLibro = oLibroAD.recuperarLibro(p_iLibroId, p_oUsuario.getIdDeIdioma());

            //verificamos que se trata de un libro de nuestra abbatia
            if (oLibro.getIdAbadia() == p_oAbadia.getIdDeAbadia()) {
                //verificamos si el estado del libro permite su restauración (completo = 2) or deteriorado = 7
                if (oLibro.getEstado() == Constantes.ESTADO_LIBRO_COMPLETO || oLibro.getEstado() == Constantes.ESTADO_LIBRO_DETERIORADO) {
                    //Verificamos si el libro se está copiando o tiene pendientes solicitudes de copia....
                    if (!oLibroAD.seEstaCopiando(oLibro.getIdLibro())) {
                        //si no se está copiando modificamos el estado a "en restauración"
                        oLibroAD.actualizarEstadoLibro(oLibro.getIdLibro(), Constantes.ESTADO_LIBRO_RESTAURANDO);
                        oEdificioAD = new adEdificio(con);
                        oEdificio = oEdificioAD.recuperarEdificioTipo(Constantes.EDIFICIO_BIBLIOTECA, p_oAbadia, p_oUsuario);

                        //debería mostrar una página con el resumen.
                        Utilidades.eliminarRegistroContext(p_oUsuario.getNick());
                        return new ActionForward("/mostrarEdificio.do?clave=" + oEdificio.getIdDeEdificio());

                    } else {
                        p_amMensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.libro.restaurar.copiando", oLibro.getDescEstado()));
                        throw new RestauracionLibroException(sTrace, log);
                    }

                } else {
                    p_amMensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.libro.restaurar.estadoincorrecto", oLibro.getDescEstado()));
                    throw new RestauracionLibroException(sTrace, log);
                }
            } else {
                p_amMensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.libro.restaurar.abadiaincorrecta"));
                throw new RestauracionLibroException(sTrace, log);
            }

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }


    }

    public ActionForward restaurarLibros(Abadia p_oAbadia, Usuario p_oUsuario, MessageResources p_oResource) throws AbadiaException {
        String sTrace = this.getClass() + ".restaurarLibros()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adLibros oLibroAD;
        adEdificio oEdificioAD;

        Edificio oEdificio;
        List<Libro> alLibros;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oLibroAD = new adLibros(con);
            alLibros = oLibroAD.recuperarLibrosPropios(p_oAbadia, p_oUsuario, p_oResource);
            for (Libro oLibro : alLibros) {
                //verificamos que se trata de un libro de nuestra abbatia
                if (oLibro.getIdAbadia() == p_oAbadia.getIdDeAbadia()) {
                    //verificamos si el estado del libro permite su restauración (completo = 2) or deteriorado = 7
                    if (oLibro.getEstado() == Constantes.ESTADO_LIBRO_COMPLETO || oLibro.getEstado() == Constantes.ESTADO_LIBRO_DETERIORADO) {
                        //Verificamos si el libro se está copiando o tiene pendientes solicitudes de copia....
                        if (!oLibroAD.seEstaCopiando(oLibro.getIdLibro())) {
                            //si no se está copiando modificamos el estado a "en restauración"
                            oLibroAD.actualizarEstadoLibro(oLibro.getIdLibro(), Constantes.ESTADO_LIBRO_RESTAURANDO);
                        }
                    }
                }
            }
            oEdificioAD = new adEdificio(con);
            oEdificio = oEdificioAD.recuperarEdificioTipo(Constantes.EDIFICIO_BIBLIOTECA, p_oAbadia, p_oUsuario);
            Utilidades.eliminarRegistroContext(p_oUsuario.getNick());
            return new ActionForward("/mostrarEdificio.do?clave=" + oEdificio.getIdDeEdificio());

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }


    }
}


