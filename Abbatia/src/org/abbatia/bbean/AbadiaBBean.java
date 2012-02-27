package org.abbatia.bbean;

import org.abbatia.actionform.*;
import org.abbatia.adbean.*;
import org.abbatia.bean.*;
import org.abbatia.core.CoreMail;
import org.abbatia.core.CoreMonje;
import org.abbatia.core.CoreTiempo;
import org.abbatia.dbms.ConnectionFactory;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaConVisitantesException;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.CorreoNoEnviadoException;
import org.abbatia.exception.RegionNoValidaException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.exception.base.SystemException;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.HTML;
import org.abbatia.utils.Utilidades;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;

import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Benjamín Rodríguez.
 * User: benjamin
 * Date: 24-may-2008
 * Time: 21:06:39
 */
public class AbadiaBBean {
    private static Logger log = Logger.getLogger(AbadiaBBean.class.getName());

    public InicioContents getRecursos(Abadia p_oAbadia) throws AbadiaException {
        String sTrace = this.getClass() + ".getRecursos(" + p_oAbadia.getIdDeAbadia() + ")";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        InicioContents Contents = new InicioContents();

        adRecurso oRecursoAD;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);

            oRecursoAD = new adRecurso(con);
            String monedas = Utilidades.redondear(oRecursoAD.recuperarCantidadRecurso(0, p_oAbadia.getIdDeAbadia()));
            String agua = Utilidades.redondear(oRecursoAD.recuperarCantidadRecurso(1, p_oAbadia.getIdDeAbadia()));
            String madera = Utilidades.redondear(oRecursoAD.recuperarCantidadRecurso(2, p_oAbadia.getIdDeAbadia()));
            String piedra = Utilidades.redondear(oRecursoAD.recuperarCantidadRecurso(3, p_oAbadia.getIdDeAbadia()));
            String hierro = Utilidades.redondear(oRecursoAD.recuperarCantidadRecurso(4, p_oAbadia.getIdDeAbadia()));

            Contents.setRecursoMonedas(monedas);
            Contents.setRecursoAgua(agua);
            Contents.setRecursoMadera(madera);
            Contents.setRecursoPiedra(piedra);
            Contents.setRecursoHierro(hierro);

            return Contents;
        } catch (AbadiaException e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public HashMap<String, Serializable> buscarAbadia(BuscarAbadiaActForm p_afDatosFiltro,
                                                      Abadia p_oAbadia, Usuario p_oUsuario,
                                                      MessageResources p_oResource) throws AbadiaException {
        String sTrace = this.getClass() + ".buscarAbadia()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adUtils oUtilsAD;
        adObispado oObispadoAD;

        HashMap<String, Serializable> hmRequest = new HashMap<String, Serializable>();

        String sSQLRegion = "";
        int nrAbadias;
        int iAbadiaId;

        String sTmp;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            // opciones por parametro
            /*
            if (p_mapRequest.get("accion") != null)
                p_afDatosFiltro.setOpcion(Integer.parseInt(p_mapRequest.get("accion")));
            if (p_mapRequest.get("orden") != null)
                p_afDatosFiltro.setOrden(Integer.parseInt(p_mapRequest.get("orden")));
            if (p_mapRequest.get("ordenid") != null)
                p_afDatosFiltro.setOrdenid(Integer.parseInt(p_mapRequest.get("ordenid")));
            if (p_mapRequest.get("pagina") != null)
                p_afDatosFiltro.setPagina(Integer.parseInt(p_mapRequest.get("pagina")));
            if (p_mapRequest.get("region") != null)
                p_afDatosFiltro.setRegion(Integer.parseInt(p_mapRequest.get("region")));
            */
            oUtilsAD = new adUtils(con);
            // Cargar regiones
            if (p_afDatosFiltro.getRegion() != -1) {
//                oUtilsAD = new adUtils(con);
                hmRequest.put("regiones", oUtilsAD.getTableRegion2());
                if (p_afDatosFiltro.getRegion() == 0) p_afDatosFiltro.setRegion(p_oAbadia.getIdDeRegion());
            } else
                sSQLRegion = " regionid=" + p_oAbadia.getIdDeRegion() + " and ";

            if (p_afDatosFiltro.getBusqueda() == 1) {
                nrAbadias = oUtilsAD.getSQL("Select Count(*) from abadia, usuario where abadia.usuarioid=usuario.usuarioid and usuario.abadia_congelada=0 and " + sSQLRegion + " abadia.nombre like '%" + p_afDatosFiltro.getSearch() + "%'", 0);
                // Directamente a la opción
                if (nrAbadias == 1) {
                    iAbadiaId = oUtilsAD.getSQL("Select abadia.abadiaid from abadia, usuario where abadia.usuarioid=usuario.usuarioid and usuario.abadia_congelada=0 and " + sSQLRegion + " abadia.nombre like '%" + p_afDatosFiltro.getSearch() + "%'", 0);
                    Utilidades.eliminarRegistroContext(p_oUsuario.getNick());
                    switch (p_afDatosFiltro.getOpcion()) {
                        case 2:  // viajar a una abbatia
                            hmRequest.put("forward", new ActionForward("/viajar.do?abadiaid=" + iAbadiaId));
                            break;
                        case 1:  // ver los edificios de un opispado
                            hmRequest.put("forward", new ActionForward("/listarEdificios.do?abadiaid_obispado=" + iAbadiaId));
                            break;
                        default:  // Construir edificios para un obispado
                            hmRequest.put("forward", new ActionForward("/crear_edificio.do?abadiaid_obispado=" + iAbadiaId));
                            break;
                    }
                    return hmRequest;
                }
            }
            // mostrar las abadias de la region
            oObispadoAD = new adObispado(con);
            if (p_afDatosFiltro.getRegion() == -1) {
                p_afDatosFiltro.setContents(oObispadoAD.recuperarAbadiasRegion(p_oResource, p_oAbadia.getIdDeRegion(), p_afDatosFiltro));
            } else {
                p_afDatosFiltro.setContents(oObispadoAD.recuperarAbadiasRegion(p_oResource, p_afDatosFiltro.getRegion(), p_afDatosFiltro));
            }
            HTML cHTML = new HTML();
            String navega = cHTML.getNavigateBar("buscar_abadia.do", "opcion=" + p_afDatosFiltro.getOpcion() + "&regionid=" + p_afDatosFiltro.getRegion() + "&orden=" + p_afDatosFiltro.getOrden() + "&ordenid=" + p_afDatosFiltro.getOrdenid(),
                    p_afDatosFiltro.getPagina(), p_afDatosFiltro.getTotal() / Constantes.REGISTROS_PAGINA);
            hmRequest.put("Navega", navega);
            return hmRequest;
        } catch (Exception e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public HashMap<String, Serializable> buscarAbadias(BuscarAbadiasActForm p_afBuscarAbadias, Abadia p_oAbadia,
                                                       ActionMessages p_amMensajes) throws AbadiaException {
        String sTrace = this.getClass() + ".buscarAbadias()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adUtils oUtilsAD;
        adAbadia oAbadiaAD;
        adCorreo oCorreoAD;

        HashMap<String, Serializable> hmRequest = new HashMap<String, Serializable>();

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oUtilsAD = new adUtils(con);
            hmRequest.put("regiones", oUtilsAD.getTableRegion2());
            hmRequest.put("ordenes", oUtilsAD.getTable2(Constantes.TABLA_ORDEN));


            if (p_afBuscarAbadias.getAccion().equals("inicio")) {
                return hmRequest;
            }

            if (p_afBuscarAbadias.getAccion().equals("buscar")) {
                //localizar las abadias que cumplan con el filtrado.
                oAbadiaAD = new adAbadia(con);
                ArrayList<DatosAbadia> abadias = oAbadiaAD.buscarAbadias(p_afBuscarAbadias);
                p_afBuscarAbadias.setListado(abadias);
                p_afBuscarAbadias.setAccion(null);
                return hmRequest;
            } else if (p_afBuscarAbadias.getAccion().equals("aceptar")) {
                MensajeActForm correo = new MensajeActForm();
                correo.setIdAbadiaOrigen(p_oAbadia.getIdDeAbadia());
                String idAbadias = "";
                if (p_afBuscarAbadias.getSeleccion() == null) {
                    //request.setAttribute("BuscarAbadiaForm", p_afBuscarAbadias);
                    hmRequest.put("forward", "mostrar");
                    return hmRequest;
                }

                for (int iCount = 0; p_afBuscarAbadias.getSeleccion().length > iCount; iCount++) {
                    idAbadias = idAbadias + p_afBuscarAbadias.getSeleccion()[iCount] + ";";
                }
                oCorreoAD = new adCorreo(con);
                idAbadias = idAbadias.substring(0, idAbadias.length() - 1);
                log.debug("BuscarAbadiaAction. destinatarios clave: " + idAbadias);
                correo.setDestinatarioid(idAbadias);
                correo.setDestinatario(oCorreoAD.recuperarNombresSeleccion(p_afBuscarAbadias));
                correo.setAccion(null);
                hmRequest.put("MensajeForm", correo);

                p_afBuscarAbadias = new BuscarAbadiasActForm();
                //request.setAttribute("BuscarAbadiaForm", p_afBuscarAbadias);
                hmRequest.put("forward", "aceptar");
                return hmRequest;
            } else if (p_afBuscarAbadias.getAccion().equals("cancelar")) {
                hmRequest.put("forward", "cancelar");
                return hmRequest;
            }
            return hmRequest;
        } catch (AbadiaException e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public void cancelarEliminacionAbadia(int p_iAbadiaId) throws AbadiaException {
        String sTrace = this.getClass() + ".cancelarEliminacionAbadia()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adAbadia oAbadiaAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oAbadiaAD = new adAbadia(con);
            oAbadiaAD.actualizarFechaEliminacion(p_iAbadiaId, null);

        } catch (AbadiaException e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public void congelarAbadia(int p_iOpcion, long p_lUsuarioId, int p_iAbadiaId) throws AbadiaException {
        String sTrace = this.getClass() + ".congelarAbadia()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adUtils oUtilsAD;
        adMonje oMonjeAD;
        adViajar oViajarAD;
        List<MonjeInicio> listaMonjesInvitados = new ArrayList<MonjeInicio>();
        List<MonjeInicio> listaMonjesVisita = new ArrayList<MonjeInicio>();

        String sSQL;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oUtilsAD = new adUtils(con);
            oUtilsAD.execSQL("UPDATE usuario set abadia_congelada=" + p_iOpcion + " where usuarioid=" + p_lUsuarioId);
            // Stats
            if (oUtilsAD.getSQL("SELECT usuarioid from abadia_congelada where usuarioid=" + p_lUsuarioId, 0) == 0) {
                sSQL = "INSERT INTO abadia_congelada (abadiaid, usuarioid, cantidad,";
                if (p_iOpcion == 0) {
                    sSQL += "fecha_desbloqueo";
                } else {
                    sSQL += "fecha_bloqueo";
                }
                sSQL += ") Values (" + p_iAbadiaId + "," + p_lUsuarioId + ",1, Now() )";
            } else {
                sSQL = "UPDATE abadia_congelada set cantidad = cantidad +1, ";
                if (p_iOpcion == 0) {
                    sSQL += "fecha_desbloqueo";
                } else {
                    sSQL += "fecha_bloqueo";
                }
                sSQL += "= Now() ";
                sSQL += "WHERE usuarioid=" + p_lUsuarioId;
            }
            oUtilsAD.execSQL(sSQL);
            //si se congela la abadía...
            if (p_iOpcion == 1) {
                //proceso para expulsar a los monges de visita y los de viaje.
                //1 - Recuperar la lista de monjes que visitan la abadia
                oMonjeAD = new adMonje(con);
                oViajarAD = new adViajar(con);
                listaMonjesInvitados = oMonjeAD.recuperarMonjesInvitados(1, p_iAbadiaId);
                //2 - Recuperar la lista de monjes que están de viaje
                listaMonjesVisita = oMonjeAD.recuperarMonjesVisita(1, p_iAbadiaId);
                for (MonjeInicio monjeInicio : listaMonjesInvitados) {
                    oViajarAD.forzarVueltaProceso(monjeInicio, Constantes.FORZAR_RETORNO_ABADIA_CONGELADA_PROPIA);
                }
                for (MonjeInicio monjeInicio : listaMonjesVisita) {
                    oViajarAD.forzarVueltaProceso(monjeInicio, Constantes.FORZAR_RETORNO_ABADIA_CONGELADA_AJENA);
                }
            }

        } catch (AbadiaException e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public ArrayList<DatosAbadia> buscarAbadiasPorNombre(PeticionBloqueoActForm p_afFormBloqueo) throws AbadiaException {
        String sTrace = this.getClass() + ".buscarAbadiasPorNombre()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adAbadia oAbadiaAD;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oAbadiaAD = new adAbadia(con);
            return oAbadiaAD.buscarAbadiasPorNombre(p_afFormBloqueo.getNombreAbadia());

        } catch (AbadiaException e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public String recuperarNombreAbadia(int p_iAbadiaId) throws AbadiaException {
        String sTrace = this.getClass() + ".recuperarNombreAbadia(" + p_iAbadiaId + ")";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adAbadia oAbadiaAD;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oAbadiaAD = new adAbadia(con);
            return oAbadiaAD.getNomAbadia(p_iAbadiaId);

        } catch (AbadiaException e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public void marcarAbadiaParaEliminacion(Abadia p_oAbadia) throws AbadiaException {
        String sTrace = this.getClass() + ".marcarAbadiaParaEliminacion(" + p_oAbadia.getIdDeAbadia() + ")";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adAbadia oAbadiaAD;
        adViajar oViajarAD;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);

            oViajarAD = new adViajar(con);
            //antes de eliminar la abadía, deben ser expulsados todos los monjes de visita y devueltos a casa los que están por ahí...
            //consultamos si existen monjes que visiten la abadía..
            if (oViajarAD.monjesViajando(p_oAbadia) || oViajarAD.monjesVisita(p_oAbadia)) {
                throw new AbadiaConVisitantesException("No se puede eliminar la abbatia mientras tenga visitantes", log);
            }
            oAbadiaAD = new adAbadia(con);
            //marco la abbatia como eliminada
            oAbadiaAD.actualizarFechaEliminacion(p_oAbadia.getIdDeAbadia(), CoreTiempo.getTiempoRealString());

        } catch (AbadiaException e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public HashMap<String, Object> cargaInicialMain(String p_szBorr, String p_szMens, Abadia p_oAbadia, Usuario p_oUsuario, MessageResources p_oResource) throws AbadiaException {
        String sTrace = this.getClass() + ".cargaInicialMain()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adMensajes oMensajesAD;
        adInicioContents oInicioContentsAD;
        adElecciones oEleccionesAD;
        adPapa oPapaAD;

        InicioContents oInicioContents;
        HashMap<String, Object> hmRequest = new HashMap<String, Object>();

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);

            oMensajesAD = new adMensajes(con);
            if (p_szBorr.compareTo("1") == 0)
                oMensajesAD.eliminarMensajesDeAbadia(p_oAbadia.getIdDeAbadia());   // Todos
            if (p_szBorr.compareTo("2") == 0)
                oMensajesAD.eliminarMensajesDeAbadia(p_oAbadia.getIdDeAbadia(), 0);   // Sólo los informativos
            if (p_szBorr.compareTo("3") == 0)
                oMensajesAD.eliminarMensajesDeAbadiaAntiguos(p_oAbadia.getIdDeAbadia());   // Los que tienen más de un mes

            oInicioContentsAD = new adInicioContents(con);
            oInicioContents = oInicioContentsAD.getInicioContents(p_oAbadia, p_szMens, p_oUsuario, p_oResource);

            oInicioContentsAD.getRecursos(p_oAbadia, oInicioContents);
            oInicioContentsAD.getContadores(p_oAbadia, oInicioContents);

            hmRequest.put("DatosContents", oInicioContents);

            // Elecciones regionales pa el obispo
            oEleccionesAD = new adElecciones(con);
            hmRequest.put("Elecciones", Integer.toString(oEleccionesAD.hayElecciones(p_oAbadia.getIdDeRegion())));

            // Cónclave
            oPapaAD = new adPapa(con);
            int Conclave = oPapaAD.estadoConclave();
            if (Conclave != 0) {
                int Fumata = oPapaAD.getFumataPapa(Conclave);
                hmRequest.put("Fumata", Integer.toString(Fumata));
            } else {
                hmRequest.remove("Fumata");
            }

            hmRequest.put("Conclave", Integer.toString(Conclave));

            return hmRequest;
        } catch (AbadiaException e) {
            throw new SystemException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }


    }

    public ArrayList<AbadiaPuntuacionForm> recuperarPuntuaciones(Abadia p_oAbadia) throws AbadiaException {
        String sTrace = this.getClass() + ".recuperarPuntuaciones()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);


        adAbadia oAbadiaAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oAbadiaAD = new adAbadia(con);
            return oAbadiaAD.recuperarPuntuaciones(p_oAbadia.getIdDeAbadia());

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }


    }

    public InicioContents gestionAldeanos(Abadia p_oAbadia) throws AbadiaException {
        String sTrace = this.getClass() + ".gestionAldeanos()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adUtils oUtilsAD;

        InicioContents oContents;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oContents = new InicioContents();
            oUtilsAD = new adUtils(con);
            oContents.setContadorAldeanos(oUtilsAD.getSQL("Select cantidad from recurso where recursoid = 20 and abadiaid = " + p_oAbadia.getIdDeAbadia(), "53?"));

            String sPuntos = oUtilsAD.getPropidadValor(Constantes.PROPIEDAD_TIPO_PARAMETROS, Constantes.PROPIEDAD_PUNTOS_SICARIO, "P");
            oContents.setPuntos(Double.parseDouble(sPuntos));
            if (p_oAbadia.getPuntuacion() >= oContents.getPuntos()) {
                oContents.setPuedeContratar("S");
            } else {
                oContents.setPuedeContratar("N");
            }
            return oContents;

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public Object registroAbadia2(AbadiaActForm p_afAbadia, Usuario p_oUsuario) throws AbadiaException {
        String sTrace = this.getClass() + ".registroAbadia2()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adUtils oUtilsAD;
        adAbadia oAbadiaAD;
        CoreMonje oCoreMonjeAD;

        Locale oLocale = null;
        HashMap<String, ArrayList<Table>> hmRequest;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oAbadiaAD = new adAbadia(con);
            if (oAbadiaAD.existeAbadiaUsuario(p_oUsuario.getIdDeUsuario())) {
                //si el usuario aun no tiene abbatia, continuamos con el registro.
                if (p_oUsuario.getIdDeIdioma() == Constantes.IDIOMA_CASTELLANO) {
                    oLocale = new Locale("es", "ES");
                } else if (p_oUsuario.getIdDeIdioma() == Constantes.IDIOMA_CATALAN) {
                    oLocale = new Locale("ca", "ES");
                } else if (p_oUsuario.getIdDeIdioma() == Constantes.IDIOMA_GALLEGO) {
                    oLocale = new Locale("gl", "ES");
                } else if (p_oUsuario.getIdDeIdioma() == Constantes.IDIOMA_EUSKERA) {
                    oLocale = new Locale("ek", "ES");
                } else if (p_oUsuario.getIdDeIdioma() == Constantes.IDIOMA_INGLES) {
                    oLocale = new Locale("en", "US");
                } else if (p_oUsuario.getIdDeIdioma() == Constantes.IDIOMA_FRANCES) {
                    oLocale = new Locale("fr", "FR");
                } else if (p_oUsuario.getIdDeIdioma() == Constantes.IDIOMA_ALEMAN) {
                    oLocale = new Locale("de", "DE");
                } else if (p_oUsuario.getIdDeIdioma() == Constantes.IDIOMA_ITALIANO) {
                    oLocale = new Locale("it", "IT");
                } else if (p_oUsuario.getIdDeIdioma() == Constantes.IDIOMA_PORTUGUES) {
                    oLocale = new Locale("pt", "PT");
                } else if (p_oUsuario.getIdDeIdioma() == Constantes.IDIOMA_LATIN) {
                    oLocale = new Locale("lt", "IT");
                }

                return oLocale;
            }

            p_afAbadia.setNombreAbadia(Utilidades.normalizarTexto(p_afAbadia.getNombreAbadia()));
            //request.getSession().setAttribute("nombre_abadia", p_afAbadia.getNombreAbadia());
            if (oAbadiaAD.existeAbadia(p_afAbadia.getNombreAbadia())) {
                hmRequest = new HashMap<String, ArrayList<Table>>();
                oUtilsAD = new adUtils(con);
                hmRequest.put("regiones", oUtilsAD.getTableRegionMenosUso());
                hmRequest.put("ordenes", oUtilsAD.getTable(Constantes.TABLA_ORDEN));
                hmRequest.put("actividades", oUtilsAD.getClaveValor(Constantes.TABLA_ACTIVIDAD));
                return hmRequest;
            }
            oCoreMonjeAD = new CoreMonje(con);
            p_afAbadia.setMonjes(oCoreMonjeAD.generarMonjesParaAbadia(p_afAbadia, p_oUsuario));

            return null;
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public Object registroAbadia3(AbadiaActForm p_afAbadia, Usuario p_oUsuario, MessageResources p_oResource) throws AbadiaException {
        String sTrace = this.getClass() + ".registroAbadia3()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adUtils oUtilsAD;
        adAbadia oAbadiaAD;
        adMensajes oMensajeAD;

        Abadia oAbadia;
        Locale oLocale = null;
        ArrayList<Table> alRegiones;
        boolean bRegionOK;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS, Constantes.AUTOCOMIT_OF);
            oAbadiaAD = new adAbadia(con);
            if (oAbadiaAD.existeAbadiaUsuario(p_oUsuario.getIdDeUsuario())) {
                //si el usuario aun no tiene abbatia, continuamos con el registro.
                if (p_oUsuario.getIdDeIdioma() == Constantes.IDIOMA_CASTELLANO) {
                    oLocale = new Locale("es", "ES");
                } else if (p_oUsuario.getIdDeIdioma() == Constantes.IDIOMA_CATALAN) {
                    oLocale = new Locale("ca", "ES");
                } else if (p_oUsuario.getIdDeIdioma() == Constantes.IDIOMA_GALLEGO) {
                    oLocale = new Locale("gl", "ES");
                } else if (p_oUsuario.getIdDeIdioma() == Constantes.IDIOMA_EUSKERA) {
                    oLocale = new Locale("ek", "ES");
                } else if (p_oUsuario.getIdDeIdioma() == Constantes.IDIOMA_INGLES) {
                    oLocale = new Locale("en", "US");
                } else if (p_oUsuario.getIdDeIdioma() == Constantes.IDIOMA_FRANCES) {
                    oLocale = new Locale("fr", "FR");
                } else if (p_oUsuario.getIdDeIdioma() == Constantes.IDIOMA_ALEMAN) {
                    oLocale = new Locale("de", "DE");
                } else if (p_oUsuario.getIdDeIdioma() == Constantes.IDIOMA_ITALIANO) {
                    oLocale = new Locale("it", "IT");
                } else if (p_oUsuario.getIdDeIdioma() == Constantes.IDIOMA_PORTUGUES) {
                    oLocale = new Locale("pt", "PT");
                } else if (p_oUsuario.getIdDeIdioma() == Constantes.IDIOMA_LATIN) {
                    oLocale = new Locale("lt", "IT");
                }
                return oLocale;
            }

            p_afAbadia.setUsuarioid(p_oUsuario.getIdDeUsuario());
            if (!oAbadiaAD.existeAbadia(p_afAbadia.getNombreAbadia())) {
                //volvemos a validar si la región seleccionada está en la lista de las posibles.
                oUtilsAD = new adUtils(con);
                alRegiones = oUtilsAD.getTableRegionMenosUso();
                bRegionOK = false;
                for (Table tTabla : alRegiones) {
                    if (tTabla.getId() == p_afAbadia.getRegion()) {
                        bRegionOK = true;
                        break;
                    }
                }

                if (!bRegionOK) {
                    throw new RegionNoValidaException(sTrace, log);
                }

                oAbadia = oAbadiaAD.crearAbadia(p_afAbadia, p_oUsuario);
                ArrayList listaCorreo = oAbadiaAD.getMailAdministradores();

                String asunto = "La abbatia " + oAbadia.getNombre() + " ha completado su registro.";
                try {
                    CoreMail.enviarNotificacionAlta(listaCorreo, asunto);
                } catch (java.lang.SecurityException e) {
                    log.error("RegistroAbadiaAction. SecurityException. No se ha podido enviar el correo a los administradores");
                } catch (CorreoNoEnviadoException e) {
                    log.error("RegistroAbadiaAction. CorreoNoEnviadoException. No se ha podido enviar el correo a los administradores");
                }

                Mensajes msg = new Mensajes();

                msg.setFechaAbadia(CoreTiempo.getTiempoAbadiaStringConHoras());
                msg.setFechaReal(CoreTiempo.getTiempoRealStringConHoras());
                msg.setIdDeAbadia(oAbadia.getIdDeAbadia());
                msg.setIdDeMonje(-1);
                msg.setIdDeRegion(oAbadia.getIdDeRegion());
                msg.setMensaje(p_oResource.getMessage("mensajes.info.bienvenida1"));
                oMensajeAD = new adMensajes(con);
                oMensajeAD.crearMensaje(msg);

                msg.setFechaAbadia(CoreTiempo.getTiempoAbadiaStringConHoras());
                msg.setFechaReal(CoreTiempo.getTiempoRealStringConHoras());
                msg.setIdDeAbadia(oAbadia.getIdDeAbadia());
                msg.setIdDeMonje(-1);
                msg.setIdDeRegion(oAbadia.getIdDeRegion());
                msg.setMensaje(p_oResource.getMessage("mensajes.info.bienvenida2"));
                oMensajeAD.crearMensaje(msg);

                ConnectionFactory.commitTransaction(con);
                return oAbadia;
            }
            return null;
        } catch (SystemException e) {
            ConnectionFactory.rollbackTransaction(con);
            throw e;
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

}

