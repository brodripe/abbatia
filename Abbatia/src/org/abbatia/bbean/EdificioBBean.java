package org.abbatia.bbean;

import org.abbatia.actionform.DatosEdificioActForm;
import org.abbatia.actionform.DatosElaboracionActForm;
import org.abbatia.actionform.FiltroLibrosActForm;
import org.abbatia.adbean.*;
import org.abbatia.bbean.singleton.CargasInicialesFiltroLibrosBBean;
import org.abbatia.bean.*;
import org.abbatia.core.CoreTiempo;
import org.abbatia.dbms.ConnectionFactory;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaSinEdificiosException;
import org.abbatia.exception.AbadiaSinObispoException;
import org.abbatia.exception.NoCoincideRegionException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.exception.base.SystemException;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.Utilidades;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.util.MessageResources;

import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Benjamín Rodríguez.
 * User: benjamin
 * Date: 25-may-2008
 * Time: 16:01:06
 */
public class EdificioBBean {

    private static Logger log = Logger.getLogger(EdificioBBean.class.getName());

    public Abadia subirNivelEdificioObispo(Abadia p_oAbadiaObispo, int p_iAbadiaId) throws AbadiaException {
        String sTrace = this.getClass() + ".subirNivelEdificioObispo(" + p_oAbadiaObispo.getIdDeAbadia() + ")";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adObispado oObispadoAD;
        adAbadia oAbadiaAD;

        Abadia oAbadia;
        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            // Recuperar la abbatia que tenemos que visualizar!!
            // Comprobar que realmente tenemos un obispo
            oObispadoAD = new adObispado(con);
            boolean obispado = oObispadoAD.abadiaconObispado(p_oAbadiaObispo);

            if (!obispado) {
                throw new AbadiaSinObispoException("La abadía no tiene obispo", log);
/*
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.general.sinacceso"));
            return mapping.findForward("error");
*/
            }
            oAbadiaAD = new adAbadia(con);
            oAbadia = oAbadiaAD.recuperarAbadia(p_iAbadiaId);

            // El obispo hace trampa!
            if (oAbadia.getIdDeRegion() != p_oAbadiaObispo.getIdDeRegion()) {
                throw new NoCoincideRegionException("La abadía del obispo no coincide con la de la abadía que se quiere construir", log);
//              errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.general.sinacceso"));
//              return mapping.findForward("error");
            }
            return oAbadia;
        } catch (AbadiaException e) {
            throw e;
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public HashMap<String, Object> subirNivelEdificio(int p_iEdificioId, Abadia p_oAbadia, Abadia p_oAbadiaObispo, Usuario oUsuario, MessageResources p_oResource) throws AbadiaException {
        String sTrace = this.getClass() + ".subirNivelEdificio()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adEdificio oEdificioAD;
        Edificio oEdificio;
        DatosNivel oDatosNivel;

        HashMap<String, Object> hmReturn = new HashMap<String, Object>();
        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);

            oEdificioAD = new adEdificio(con);
            oEdificio = oEdificioAD.recuperarEdificio(p_iEdificioId, p_oAbadia, oUsuario, false, p_oResource, null);
            oDatosNivel = oEdificioAD.getDatosSiguienteNivel(oEdificio);
            if (p_oAbadiaObispo != null) {
                hmReturn.put("TotalOro", Utilidades.redondear(oEdificioAD.getCosteTotalCostruccion(oEdificio, oDatosNivel.getNivel())));
                //request.setAttribute("TotalOro", Utilidades.redondear(oEdificioAD.getCosteTotalCostruccion( oEdificio, oDatosNivel.getNivel() )));
            }
            hmReturn.put("Edificio", oEdificio);
            hmReturn.put("DatosNivel", oDatosNivel);
//            request.setAttribute("Edificio", edificio);
//            request.setAttribute("DatosNivel", datosNivel);
            return hmReturn;
        } catch (AbadiaException e) {
            throw e;
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public Edificio subirNivelConfirmado(int p_iEdificioId, Abadia p_oAbadia, Abadia p_oAbadiaObispo, Usuario p_oUsuario, MessageResources p_oResource) throws AbadiaException {
        String sTrace = this.getClass() + ".subirNivelConfirmado()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adEdificio oEdificioAD;
        adMensajes oMensajesAD;
        Edificio oEdificio;
        DatosNivel oDatosNivel;
        Mensajes oMensaje;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS, Constantes.AUTOCOMIT_OF);
            oEdificioAD = new adEdificio(con);
            oEdificio = oEdificioAD.recuperarEdificio(p_iEdificioId, p_oAbadia, p_oUsuario, false, p_oResource, null);
            oDatosNivel = oEdificioAD.getDatosSiguienteNivel(oEdificio);
            if (p_oAbadiaObispo != null) {
                oEdificioAD.subirNivel_Obispado(p_oAbadia, p_oAbadiaObispo, oEdificio, oDatosNivel);
            } else {
                oEdificioAD.subirNivel(p_oAbadia, oEdificio, oDatosNivel);
            }

            oEdificio = oEdificioAD.recuperarEdificio(p_iEdificioId, p_oAbadia, p_oUsuario, false, p_oResource, null);
            oDatosNivel = oEdificioAD.getDatosSiguienteNivel(oEdificio);

            //genero un mensaje para el usuario
            oMensajesAD = new adMensajes(con);
            oMensaje = new Mensajes();
            oMensaje.setFechaAbadia(CoreTiempo.getTiempoAbadiaStringConHoras());
            oMensaje.setFechaReal(CoreTiempo.getTiempoRealStringConHoras());
            oMensaje.setIdDeAbadia(p_oAbadia.getIdDeAbadia());
            oMensaje.setIdDeMonje(-1);
            oMensaje.setIdDeRegion(p_oAbadia.getIdDeRegion());
            oMensaje.setMensaje("Se han iniciado las obras de ampliación de tu " +
                    " <a href='mostrarEdificio.do?clave=" +
                    oEdificio.getIdDeEdificio() + "'>" +
                    oEdificio.getNombre() + "</a> a nivel " +
                    oDatosNivel.getNivel());
            oMensajesAD.crearMensaje(oMensaje);

            ConnectionFactory.commitTransaction(con);
            return oEdificio;
        } catch (AbadiaException e) {
            ConnectionFactory.rollbackTransaction(con);
            throw e;
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }


    }

    public ArrayList<Notificacion> recuperarLinksEdificios(Abadia p_oAbadia, Usuario p_oUsuario) throws AbadiaException {
        String sTrace = this.getClass() + ".recuperarLinksEdificios()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adEdificio oEdificioAD;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);

            oEdificioAD = new adEdificio(con);
            return oEdificioAD.recuperarLinksEdificios(p_oAbadia, p_oUsuario);

        } catch (AbadiaException e) {
            throw e;
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public Abadia crearEdificioObispo(String p_sAbadiaIdObispado, Abadia p_oAbadiaObispado, InicioContents p_oInicioContents) throws AbadiaException {
        String sTrace = this.getClass() + ".crearEdificioObispo()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adObispado oObispadoAD;
        adAbadia oAbadiaAD;
        adInicioContents oInicioContentsAD;

        Abadia oAbadia;
        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS, Constantes.AUTOCOMIT_OF);
            // Comprobar que realmente tenemos un obispo
            if (p_sAbadiaIdObispado != null) {
                oObispadoAD = new adObispado(con);
                if (!oObispadoAD.abadiaconObispado(p_oAbadiaObispado)) {
                    throw new AbadiaSinObispoException("La Abadia " + p_oAbadiaObispado.getNombre() + " no dispone de obispo", log);
                    /*
                                  errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.general.sinacceso"));
                                  return mapping.findForward("error");
                    */
                }
                oAbadiaAD = new adAbadia(con);
                oAbadia = oAbadiaAD.recuperarAbadia(Integer.parseInt(p_sAbadiaIdObispado));

                // El obispo hace trampa!
                if (oAbadia.getIdDeRegion() != p_oAbadiaObispado.getIdDeRegion()) {
                    throw new NoCoincideRegionException("La región del obispo no coincide con la de abadía", log);
                    /*
                                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.general.sinacceso"));
                                    return mapping.findForward("error");
                    */
                }
                return oAbadia;
            } else {
                // Recursos de la abbatia
                oInicioContentsAD = new adInicioContents(con);
                oInicioContentsAD.getRecursos(p_oAbadiaObispado, p_oInicioContents);

                return p_oAbadiaObispado;
            }


        } catch (AbadiaException e) {
            throw e;
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public ArrayList<EdificioBase> recuperarListaEdificiosParaConstruir(Abadia p_oAbadia, Usuario p_oUsuario) throws AbadiaException {
        String sTrace = this.getClass() + ".recuperarListaEdificiosParaConstruir()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adEdificio oEdificioAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oEdificioAD = new adEdificio(con);
            return oEdificioAD.recuperarListaEdificiosParaConstruir(p_oAbadia, p_oUsuario);

        } catch (AbadiaException e) {
            throw e;
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public DatosEdificioActForm recuperarDatosEdificio(String p_sClave, Edificio p_oEdificio, Usuario p_oUsuario, boolean p_bParaOtraAbadia) throws AbadiaException {
        String sTrace = this.getClass() + ".recuperarDatosEdificio()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adEdificio oEdificioAD;

        DatosEdificioActForm afDatosEdificio;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oEdificioAD = new adEdificio(con);
            p_oEdificio.setIdDeTipoDeEdificio(Integer.parseInt(p_sClave));
            p_oEdificio.setNivel(1);
            afDatosEdificio = oEdificioAD.getDatosEdificioTipo(p_oEdificio, p_oUsuario);
            if (p_bParaOtraAbadia) {
                afDatosEdificio.setCoste_total(Utilidades.redondear(oEdificioAD.getCosteTotalCostruccion(p_oEdificio, 1)));
            }
            return afDatosEdificio;

        } catch (AbadiaException e) {
            throw e;
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public void crearEdificio(Edificio p_oEdificio, DatosEdificioActForm p_afDatosEdificio,
                              Abadia p_oAbadia, Abadia p_oAbadiaObispado, Usuario p_oUsuario,
                              MessageResources p_oResource) throws AbadiaException {
        String sTrace = this.getClass() + ".crearEdificio()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adEdificio oEdificioAD;
        adMensajes oMensajesAD;
        adCampo oCampoAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS, Constantes.AUTOCOMIT_OF);
            p_oEdificio.setIdDeTipoDeEdificio(p_afDatosEdificio.getTipo_edificio());
            p_oEdificio.setFechaDeConstruccion(CoreTiempo.getTiempoAbadiaString());
            p_oEdificio.setNivel(0);
            p_oEdificio.setEstado(100);
            p_oEdificio.setIdDeAbadia(p_oAbadia.getIdDeAbadia());
            //recuperamos los datos para nivel 1
            oEdificioAD = new adEdificio(con);
            DatosNivel datosNivel = oEdificioAD.getDatosSiguienteNivel(p_oEdificio);
            p_oEdificio.setNombre(oEdificioAD.recuperarNombreEdificioTipo(p_oEdificio.getIdDeTipoDeEdificio(), p_oUsuario));
            //registramos una subida de nivel para nivel 1.
            if (p_oAbadiaObispado != p_oAbadia)
                oEdificioAD.subirNivel_Obispado(p_oAbadia, p_oAbadiaObispado, p_oEdificio, datosNivel);
            else
                oEdificioAD.subirNivel(p_oAbadia, p_oEdificio, datosNivel);
            //creamos el edificio tipo de nivel 0
            oEdificioAD.crearEdificio(p_oEdificio);

            oMensajesAD = new adMensajes(con);
            Mensajes mensaje = new Mensajes();
            mensaje.setIdDeAbadia(p_oAbadia.getIdDeAbadia());
            mensaje.setMensaje(p_oResource.getMessage("mensajes.info.contruccionedificio", p_oEdificio.getNombre()));
            mensaje.setIdDeRegion(p_oAbadia.getIdDeRegion());
            mensaje.setFechaAbadia(CoreTiempo.getTiempoAbadiaStringConHoras());
            mensaje.setFechaReal(CoreTiempo.getTiempoRealStringConHoras());
            mensaje.setIdDeMonje(-1);
            oMensajesAD.crearMensaje(mensaje);
            //si el edificio es un campo de cultivo, crearemos ademas un campo.
            if (p_oEdificio.getIdDeTipoDeEdificio() == Constantes.EDIFICIO_CAMPO) {
                oCampoAD = new adCampo(con);
                oCampoAD.crearCampo(p_oAbadia);

                mensaje = new Mensajes();
                mensaje.setIdDeAbadia(p_oAbadia.getIdDeAbadia());
                mensaje.setMensaje(p_oResource.getMessage("mensajes.info.yatienescampo"));
                mensaje.setIdDeRegion(p_oAbadia.getIdDeRegion());
                mensaje.setFechaAbadia(CoreTiempo.getTiempoAbadiaStringConHoras());
                mensaje.setFechaReal(CoreTiempo.getTiempoRealStringConHoras());
                mensaje.setIdDeMonje(-1);
                oMensajesAD.crearMensaje(mensaje);
            }
            ConnectionFactory.commitTransaction(con);
        } catch (AbadiaException e) {
            ConnectionFactory.rollbackTransaction(con);
            throw new SystemException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public ArrayList<Edificio> recuperarListadoEdificoObispo(int p_iAbadiaIdAcceso, Abadia p_oAbadia, Usuario p_oUsuario, MessageResources p_oRecursos,
                                                             HashMap<String, String> p_hmRequest) throws AbadiaException {
        String sTrace = this.getClass() + ".recuperarListadoEdificoObispo()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adObispado oObispadoAD;
        adAbadia oAbadiaAD;
        adEdificio oEdificioAD;
        adUtils oUtilsAD;

        Abadia oAbadiaVisualiza;
        int iRegionId;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            // Comprobar que realmente tenemos un obispo
            oObispadoAD = new adObispado(con);
            boolean obispado = oObispadoAD.abadiaconObispado(p_oAbadia);
            if (!obispado) {
                throw new AbadiaSinObispoException(sTrace, log);
            }
            oAbadiaAD = new adAbadia(con);
            oAbadiaVisualiza = oAbadiaAD.recuperarAbadia(p_iAbadiaIdAcceso);

            // El tiempo hace trampa y es un obispo!!!!
            if (p_oAbadia.getIdDeRegion() != oAbadiaVisualiza.getIdDeRegion()) {
                throw new AbadiaSinObispoException(sTrace, log);
            }
            iRegionId = oAbadiaVisualiza.getIdDeRegion();
            p_hmRequest.put("Nombre", oAbadiaVisualiza.getNombre());
            p_hmRequest.put("Region", String.valueOf(iRegionId));

            oUtilsAD = new adUtils(con);
            if (Integer.parseInt(oUtilsAD.getPropriedad(100, iRegionId, "R", "0")) == 0)
                p_hmRequest.put("Nieve", "0");
            else p_hmRequest.put("Nieve", "1");

            oEdificioAD = new adEdificio(con);
            return oEdificioAD.recuperarListaEdificios(oAbadiaVisualiza, p_oUsuario, p_oRecursos);


        } catch (AbadiaException e) {
            throw new SystemException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public ArrayList<Edificio> recuperarListadoEdificios(Abadia p_oAbadia, Usuario p_oUsuario, MessageResources p_oRecursos,
                                                         HashMap<String, String> p_hmRequest) throws AbadiaException {
        String sTrace = this.getClass() + ".recuperarListadoEdificios()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adEdificio oEdificioAD;
        adUtils oUtilsAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oEdificioAD = new adEdificio(con);

            oUtilsAD = new adUtils(con);
            if (Integer.parseInt(oUtilsAD.getPropriedad(100, p_oAbadia.getIdDeRegion(), "R", "0")) == 0)
                p_hmRequest.put("Nieve", "0");
            else p_hmRequest.put("Nieve", "1");

            return oEdificioAD.recuperarListaEdificios(p_oAbadia, p_oUsuario, p_oRecursos);
        } catch (AbadiaException e) {
            throw new SystemException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public HashMap<String, Serializable> gestionMantenimiento(Abadia p_oAbadia, Usuario p_oUsuario, MessageResources p_oResource,
                                                              String p_szParametro, Map p_hmParams) throws AbadiaException {
        String sTrace = this.getClass() + ".gestionMantenimiento()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        ArrayList<Edificio> alEdificios;
        HashMap<String, Serializable> hmRequest = new HashMap<String, Serializable>();
        int iNivelMantenimiento;
        long lCosteMantenimiento = 0;

        adEdificio oEdificioAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);

            oEdificioAD = new adEdificio(con);
            alEdificios = oEdificioAD.recuperarListaEdificiosMantenimiento(p_oAbadia, p_oUsuario, p_oResource);

            //si no aparecen edificios en la lista
            if (alEdificios == null || alEdificios.isEmpty()) {
                //mostramos mensaje de error
                throw new AbadiaSinEdificiosException("No existen edificios para la abadía", log);
            }

            //Si llegamos desde el botón aceptar..
            if (p_szParametro != null && p_szParametro.equals("Aceptar")) {
                //debemos actualizar los niveles de mantenimiento
                //itEdificios = alEdificios.iterator();
                for (Edificio oEdificio : alEdificios) {
                    //oEdificio = (Edificio) itEdificios.next();
                    iNivelMantenimiento = Integer.valueOf(((String[]) p_hmParams.get(String.valueOf(oEdificio.getIdDeTipoDeEdificio())))[0]);
                    //si el nivel de mantenimiento ha cambiado...
                    if (iNivelMantenimiento != oEdificio.getMantenimiento()) {
                        oEdificioAD.actualizarMantenimiento(oEdificio.getIdDeEdificio(), iNivelMantenimiento);
                        oEdificio.setMantenimiento(iNivelMantenimiento);
                    }
                }
                //recuperamos la lista de edificios actual del usuario actualizado
                alEdificios = oEdificioAD.recuperarListaEdificiosMantenimiento(p_oAbadia, p_oUsuario, p_oResource);
            }
            //calculamos el coste total de mantenimiento
            for (Edificio oEdificio : alEdificios) {
                lCosteMantenimiento += oEdificio.getCosteMantenimientoLong();
            }
            //ponemos el coste total de mantenimiento en la request
            hmRequest.put("costeMantenimiento", Utilidades.redondear(lCosteMantenimiento));

            //cargamos el arraylist de edificios en la request.
            hmRequest.put("edificios", alEdificios);

            return hmRequest;
        } catch (Exception e) {
            throw new SystemException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public HashMap<String, Object> recuperarDetalleEdificio(int p_iClave,
                                                            boolean p_bMostrarConetenidos, Abadia p_oAbadia,
                                                            Usuario p_oUsuario, MessageResources p_oResource,
                                                            ActionForm p_af, int p_iDetalle, HashMap p_mParameterMap)
            throws AbadiaException {

        String sTrace = this.getClass() + ".recuperarDetalleEdificio()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adEdificio oEdificioAD;
        adAlimentos oAlimentosAD;
        adElaboracionAlimentos oElaboracionAD;
        adLibros oLibrosAD;
        adRecurso oRecursoAD;
        adUtils oUtilsAD;

        Edificio oEdificio;

        ArrayList<Monje> alMonjesEdificio;
        ArrayList<Libro> alLibros;
        ArrayList<Recurso> alRecursos;
        ArrayList<ConsumoAlimentosFamilia> alConsumos;

        String tab;
        if (p_mParameterMap == null || p_mParameterMap.get("Tab") == null) {
            tab = "init";
        } else {
            tab = (String) p_mParameterMap.get("Tab");
        }


        HashMap<String, Object> hmRequest = new HashMap<String, Object>();

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);

            oEdificioAD = new adEdificio(con);
            oEdificio = oEdificioAD.recuperarEdificio(p_iClave, p_oAbadia, p_oUsuario,
                    p_bMostrarConetenidos, p_oResource, p_mParameterMap);
            oLibrosAD = new adLibros(con);
            // Cargar los monjes del edificio
            if ((tab.equals("monjes")) || ((oEdificio.getIdDeTipoDeEdificio() == Constantes.EDIFICIO_ORATORIO))) {
                alMonjesEdificio = oEdificioAD.recuperarMonjes(p_oAbadia.getIdDeAbadia(), oEdificio.getIdDeTipoDeEdificio(), p_oResource);
                hmRequest.put("MonjesEdificio", alMonjesEdificio);
                //request.setAttribute("MonjesEdificio", alMonjesEdificio);
            } else if (oEdificio.getIdDeTipoDeEdificio() == Constantes.EDIFICIO_COMEDOR) {
                //Recuperar la listade todos los monjes de la abadía (los propios y los invitados) con sus dietas
/*
                oMonjeAD = new adMonje(con);
                alMonjesAbadia = oMonjeAD.recuperarMonjesPorAbadia(p_oAbadia.getIdDeAbadia(), p_oResource);
                //Recuperamos el total de familias consumidas por monje
                hmFamilias = adAlimentos.calcularConsumosFamilia(alMonjesAbadia);
*/
                //recuperar los recursos alimenticios disponibles asociados a los necesarios para los monjes
                //con los nombres de las familias de alimentos..
                oAlimentosAD = new adAlimentos(con);
                alConsumos = oAlimentosAD.recuperarConsumosAlimentosFamilia(p_oAbadia.getIdDeAbadia(), p_oUsuario.getIdDeIdioma());
                oUtilsAD = new adUtils(con);
                for (ConsumoAlimentosFamilia oConsumo : alConsumos) {
                    oConsumo.setDisponible(oUtilsAD.getSQL("SELECT Sum(al.CANTIDAD) FROM alimentos AS a , alimentos_lote AS al , edificio AS e WHERE a.LOTEID =  al.LOTEID AND a.EDIFICIOID =  e.EDIFICIOID AND e.ABADIAID =  " + oConsumo.getAbadiaId() + " AND a.ALIMENTOID = " + oConsumo.getAlimentoId(), (double) 0));
                    oConsumo.setDiferencia(oConsumo.getDisponible() - oConsumo.getRequerido());
                    oConsumo.setDiferenciaStr(Utilidades.redondear(oConsumo.getDiferencia()));
                    oConsumo.setDisponibleStr(Utilidades.redondear(oConsumo.getDisponible()));
                    oConsumo.setRequeridoStr(Utilidades.redondear(oConsumo.getRequerido()));
                }
/*                alFamilias = oAlimentosAD.recuperarCantidadFamilia(hmFamilias, p_oAbadia, p_oUsuario);*/
                hmRequest.put("consumos", alConsumos);
                //request.setAttribute("familias", alFamilias);
            } else
                // Elaboración de productos en la cocina
                if (tab.equals("elaboracion")) {
                    ArrayList<Table> productosElaborables = null;
                    ArrayList<datosElaboracion> productosElaborando = null;
                    oElaboracionAD = new adElaboracionAlimentos(con);
                    //recupero los productos que pueden ser producidos
                    if (oEdificio.getIdDeTipoDeEdificio() == Constantes.EDIFICIO_ALMACEN) {
                        productosElaborables = oElaboracionAD.getProductosElaborables(Constantes.ELABORACION_TIPO_RECURSO, oEdificio, p_oUsuario, p_oResource);
                        productosElaborando = oElaboracionAD.recuperaProductosElaboracion(Constantes.ELABORACION_TIPO_RECURSO, oEdificio.getIdDeEdificio(), p_oUsuario, p_oResource);
                    } else if (oEdificio.getIdDeTipoDeEdificio() == Constantes.EDIFICIO_COCINA) {
                        productosElaborables = oElaboracionAD.getProductosElaborables(Constantes.ELABORACION_TIPO_ALIMENTO, oEdificio, p_oUsuario, p_oResource);
                        productosElaborando = oElaboracionAD.recuperaProductosElaboracion(Constantes.ELABORACION_TIPO_ALIMENTO, oEdificio.getIdDeEdificio(), p_oUsuario, p_oResource);
                    } else if (oEdificio.getIdDeTipoDeEdificio() == Constantes.EDIFICIO_TALLER_ARTESANIA) {
                        productosElaborables = oElaboracionAD.getProductosElaborables(Constantes.ELABORACION_TIPO_ARTESANIA, oEdificio, p_oUsuario, p_oResource);
                        productosElaborando = oElaboracionAD.recuperaProductosElaboracion(Constantes.ELABORACION_TIPO_ARTESANIA, oEdificio.getIdDeEdificio(), p_oUsuario, p_oResource);
                    } else if (oEdificio.getIdDeTipoDeEdificio() == Constantes.EDIFICIO_TALLER_COSTURA) {
                        productosElaborables = oElaboracionAD.getProductosElaborables(Constantes.ELABORACION_TIPO_COSTURA, oEdificio, p_oUsuario, p_oResource);
                        productosElaborando = oElaboracionAD.recuperaProductosElaboracion(Constantes.ELABORACION_TIPO_COSTURA, oEdificio.getIdDeEdificio(), p_oUsuario, p_oResource);
                    }

                    hmRequest.put("elaborables", productosElaborables);
                    DatosElaboracionActForm datosElaboracion = new DatosElaboracionActForm();
                    oEdificio.setContenido(productosElaborando);
                    hmRequest.put("datosElaboracion", datosElaboracion);
                } else
                    //biblioteca - Libros region
                    if (tab.equals("region")) {
                        FiltroLibrosActForm filtro = (FiltroLibrosActForm) p_af;
                        //log.info("filtro. disponible:" + filtro.isDisponible());
                        alLibros = oLibrosAD.recuperarLibrosRegionFiltro(p_oAbadia, p_oUsuario, p_oResource, filtro);
                        //Recuperamos la lista de abadías con sus identificadores para cargarlos en un ArrayList a parte.
                        //hmFiltros = oLibrosAD.cargarFiltrosBiblioteca(alLibros, p_oResource);
                        hmRequest.put(Constantes.TABLA_ABADIAS, CargasInicialesFiltroLibrosBBean.getValueList("AbadiasConLibroPorRegion", p_oAbadia.getIdDeRegion()));
                        hmRequest.put(Constantes.TABLA_LIBROS, CargasInicialesFiltroLibrosBBean.getValueList("LibroActivos"));
                        hmRequest.put(Constantes.TABLA_IDIOMAS, CargasInicialesFiltroLibrosBBean.getValueList("IdiomasLibro"));
                        hmRequest.put(Constantes.TABLA_CATEGORIAS, CargasInicialesFiltroLibrosBBean.getValueList("CategoriasLibro"));
                        oEdificio.setContenido(oLibrosAD.aplicarFiltro(alLibros, filtro));
                        //oEdificio.setContenido(alLibros);
                        hmRequest.put("FiltroLibros", filtro);

                    } else if (tab.equals("general")) {
                        FiltroLibrosActForm filtro = (FiltroLibrosActForm) p_af;
                        //log.info("filtro. disponible:" + filtro.isDisponible());
                        alLibros = oLibrosAD.recuperarLibrosTodosFiltro(p_oUsuario, p_oResource, filtro);

                        //Recuperamos la lista de abadías con sus identificadores para cargarlos en un ArrayList a parte.
                        //hmFiltros = oLibrosAD.cargarFiltrosBiblioteca(alLibros, p_oResource);
                        hmRequest.put(Constantes.TABLA_ABADIAS, CargasInicialesFiltroLibrosBBean.getValueList("AbadiasConLibro"));
                        hmRequest.put(Constantes.TABLA_LIBROS, CargasInicialesFiltroLibrosBBean.getValueList("LibroActivos"));
                        hmRequest.put(Constantes.TABLA_IDIOMAS, CargasInicialesFiltroLibrosBBean.getValueList("IdiomasLibro"));
                        hmRequest.put(Constantes.TABLA_REGIONES, CargasInicialesFiltroLibrosBBean.getValueList("RegionesConLibro"));
                        hmRequest.put(Constantes.TABLA_CATEGORIAS, CargasInicialesFiltroLibrosBBean.getValueList("CategoriasLibro"));

                        oEdificio.setContenido(oLibrosAD.aplicarFiltro(alLibros, filtro));
                        //oEdificio.setContenido(alLibros);
                        hmRequest.put("FiltroLibros", filtro);
                    } else if (tab.equals("copias")) {
                        alLibros = oLibrosAD.recuperarLibrosCopiando(p_oAbadia, p_oUsuario, p_oResource);
                        oEdificio.setContenido(alLibros);
                    } else if (tab.equals("recursos")) {
                        //recuperar recursos de biblioteca....
                        //plumas, tinta y pergaminos
                        oRecursoAD = new adRecurso(con);
                        alRecursos = oRecursoAD.recuperarRecursos(oEdificio, p_oUsuario, p_oResource);
                        oEdificio.setContenido(alRecursos);
                    }


            DatosNivel nivel = oEdificioAD.getDatosSiguienteNivel(oEdificio);
            oEdificio.setDescripcion(oEdificioAD.recuperarDescripcionEdificioTipo(oEdificio.getIdDeTipoDeEdificio(), p_oUsuario));

            hmRequest.put("Edificio", oEdificio);
            hmRequest.put("DatosNivel", nivel);
            hmRequest.put("Detalle", p_iDetalle);
            hmRequest.put("Tab", tab);
            return hmRequest;

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public int recuperarCosteMantenimiento(String p_szTipoEdificioId, short p_sNivel) throws AbadiaException {
        String sTrace = this.getClass() + ".recuperarCosteMantenimiento(" + p_szTipoEdificioId + ", " + p_sNivel + ")";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adEdificio oEdificioAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oEdificioAD = new adEdificio(con);
            return oEdificioAD.recuperarCosteMantenimiento(p_szTipoEdificioId, p_sNivel);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }


    }
}
