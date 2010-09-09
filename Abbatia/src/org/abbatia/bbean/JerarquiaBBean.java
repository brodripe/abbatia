package org.abbatia.bbean;

import org.abbatia.actionform.*;
import org.abbatia.adbean.*;
import org.abbatia.bean.*;
import org.abbatia.core.CoreTiempo;
import org.abbatia.dbms.ConnectionFactory;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.Utilidades;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;

import java.awt.*;
import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Benjamín Rodríguez.
 * User: benjamin
 * Date: 24-may-2008
 * Time: 21:06:39
 */
public class JerarquiaBBean {
    private static Logger log = Logger.getLogger(JerarquiaBBean.class.getName());

    public void buscarEminencias(Map<String, Serializable> p_mRequest, MessageResources p_oResources, Usuario p_oUsuario) throws AbadiaException {
        String sTrace = this.getClass() + ".buscarEminencias()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adJerarquiaEclesiastica oJerarquiaAD;
        ArrayList<MonjeEminencia> alTable;

        //HashMap<String, Serializable> hmRequest = new HashMap<String, Serializable>();

        String sTitulo = "";

        int iOpcion = 0;
        int iMonjeId = 0;
        int iClave = 0;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            // opciones por parametro
            if (p_mRequest.get("opcion") != null)
                iOpcion = Integer.parseInt((String) p_mRequest.get("opcion"));
            if (p_mRequest.get("monjeid") != null)
                iMonjeId = Integer.parseInt((String) p_mRequest.get("monjeid"));
            if (p_mRequest.get("clave") != null)
                iClave = Integer.parseInt((String) p_mRequest.get("clave"));

            // Opciones
            switch (iOpcion) {
                case 0:
                    sTitulo = p_oResources.getMessage("diplomacia.jerarquia.prestardinero.para");
                    iClave = iMonjeId;
                    break;
                case 1:
                    sTitulo = p_oResources.getMessage("sicario.busca.asesinar");
                    break;
            }

            // mostrar las abadias de la region
            oJerarquiaAD = new adJerarquiaEclesiastica(con);
            alTable = oJerarquiaAD.recuperarEminencias(p_oUsuario, iOpcion, iClave);

            p_mRequest.put("Titulo", sTitulo);
            p_mRequest.put("Contents", alTable);

            //return hmRequest;
        } catch (AbadiaException e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }


    }

    public void recuperarImpuestosCardenal(ObispadoImpuestosActForm p_afObispadoImpuestos, int p_iTab, int p_iPagina, int p_iOrden, int p_iOrdenId, Point p_pTotal, MessageResources p_oResource, Abadia p_oAbadia) throws AbadiaException {
        String sTrace = this.getClass() + ".recuperarImpuestosCardenal()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adComisiones oComisionAD;

        Comisiones oComisiones;
        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);


            oComisionAD = new adComisiones(con);
            oComisiones = oComisionAD.getComision(p_oAbadia.getIdDeRegion());
            switch (p_iTab) {
                // Recaudaciones por día
                case 0:
                    p_afObispadoImpuestos.setContribuciones(oComisionAD.recuperarContribucionesComision(p_oAbadia, -1, p_iPagina, p_iOrden, p_iOrdenId, p_pTotal, p_oResource, "cardenalesImpuestos.do?tab=0"));
                    break;
                // Pago de comisiones
                case 1:
                    p_afObispadoImpuestos.setContribuciones(oComisionAD.recuperarContribucionesComision(p_oAbadia, 0, p_iPagina, p_iOrden, p_iOrdenId, p_pTotal, p_oResource, "cardenalesImpuestos.do?tab=1"));
                    break;
            }
            p_afObispadoImpuestos.setTotal(Utilidades.redondear(oComisiones.getTotal()));
        } catch (AbadiaException e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public void cambiarEstadoSeleccionable(DatosDiplomaciaForm p_afDiplomaciaForm, Abadia p_oAbadia) throws AbadiaException {
        String sTrace = this.getClass() + ".cambiarEstadoSeleccionable()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adAbadia oAbadiaAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oAbadiaAD = new adAbadia(con);

            if (p_afDiplomaciaForm.isFlagObispo())
                oAbadiaAD.actualizarFlagObispo(p_oAbadia.getIdDeAbadia(), 1);
            else
                oAbadiaAD.actualizarFlagObispo(p_oAbadia.getIdDeAbadia(), 0);

            if (p_afDiplomaciaForm.isFlagCardenal())
                oAbadiaAD.actualizarFlagCardenal(p_oAbadia.getIdDeAbadia(), 1);
            else
                oAbadiaAD.actualizarFlagCardenal(p_oAbadia.getIdDeAbadia(), 0);

        } catch (AbadiaException e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public void dimitirCarcenal(Abadia p_oAbadia, Usuario p_oUsuario, MessageResources p_oResource, ActionMessages p_oMensajes) throws AbadiaException {
        String sTrace = this.getClass() + ".dimitirCarcenal()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adObispado oObispadoAD;
        adMonje oMonjeAD;
        adJerarquiaEclesiastica oJerarquiaAD;
        adCardenal oCardenalAD;
        adMensajes oMensajesAD;

        int idCardenal;
        Monje oMonje;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS, Constantes.AUTOCOMIT_OF);
            oObispadoAD = new adObispado(con);
            idCardenal = oObispadoAD.recuperaCargoAbadia(p_oAbadia, Constantes.JERARQUIA_CARDENAL);

            if (idCardenal > 0) {

                oMonjeAD = new adMonje(con);
                oMonje = oMonjeAD.recuperarMonje(idCardenal, p_oAbadia.getIdDeAbadia(), p_oResource, p_oUsuario.getIdDeIdioma());

                oJerarquiaAD = new adJerarquiaEclesiastica(con);
                oJerarquiaAD.setJerarquiaid(idCardenal, Constantes.JERARQUIA_MONJE);

                oCardenalAD = new adCardenal(con);
                oCardenalAD.resetCardenalAbadia(p_oAbadia.getIdDeAbadia());

                oMensajesAD = new adMensajes(con);
                oMensajesAD.crearMensajesParaTodos(-1, 12014, oMonje.getNombre(), null, null, null);

                p_oMensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.cardenal.hasdimitido"));
            } else {
                p_oMensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.cardenal.noexiste"));
            }
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

    public void dimitirObispo(Abadia p_oAbadia, Usuario p_oUsuario, MessageResources p_oResource, ActionMessages p_oMensajes) throws AbadiaException {
        String sTrace = this.getClass() + ".dimitirObispo()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adObispado oObispadoAD;
        adMonje oMonjeAD;
        adJerarquiaEclesiastica oJerarquiaAD;
        adMensajes oMensajesAD;
        adRegion oRegionAD;

        int idObispo;
        Monje oMonje;
        Region oRegion;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS, Constantes.AUTOCOMIT_OF);
            oObispadoAD = new adObispado(con);
            idObispo = oObispadoAD.recuperaCargoAbadia(p_oAbadia, Constantes.JERARQUIA_OBISPO);
            if (idObispo > 0) {
                oMonjeAD = new adMonje(con);
                oMonje = oMonjeAD.recuperarMonje(idObispo, p_oAbadia.getIdDeAbadia(), p_oResource, p_oUsuario.getIdDeIdioma());

                oRegionAD = new adRegion(con);
                oRegion = oRegionAD.recuperarRegion(p_oAbadia.getIdDeRegion());

                oJerarquiaAD = new adJerarquiaEclesiastica(con);
                oJerarquiaAD.setJerarquiaid(idObispo, Constantes.JERARQUIA_MONJE);

                oObispadoAD = new adObispado(con);
                oObispadoAD.resetObispadoAbadia(p_oAbadia.getIdDeAbadia());

                oMensajesAD = new adMensajes(con);
                oMensajesAD.crearMensajesParaRegion(p_oAbadia.getIdDeRegion(), -1, 12015, oMonje.getNombre(), oRegion.getDescripcion(), null, null);
                oMensajesAD.crearMensajesParaRegion(p_oAbadia.getIdDeRegion(), -1, 12016, oRegion.getDescripcion(), null, null, null);

                p_oMensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.obispo.hasdimitido"));
            } else {
                p_oMensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.obispo.noexiste"));
            }
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

    public ArrayList<Table> recuperarComisiones(Abadia p_oAbadia, Usuario p_oUsuario, BuscarAbadiaActForm p_afDatosFiltro, MessageResources p_oResource) throws AbadiaException {

        String sTrace = this.getClass() + ".recuperarComisiones()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adComisiones oComisionesAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oComisionesAD = new adComisiones(con);
            return oComisionesAD.recuperarMovima(p_oResource, p_oAbadia.getIdDeRegion(), p_oUsuario.getIdDeIdioma(), p_afDatosFiltro);

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public HashMap<String, Serializable> recuperarJerarquiaTab0(DatosDiplomaciaForm p_afDiplomaciaForm, Abadia p_oAbadia, MessageResources p_oResource) throws AbadiaException {
        String sTrace = this.getClass() + ".recuperarJerarquiaTab0()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adJerarquiaEclesiastica oJerarquiaAD;
        adMonje oMonjeAD;
        adElecciones oEleccionesAD;
        adAbadia oAbadiaAD;
        adCardenal oCardenalAD;
        adPapa oPapaAD;

        HashMap<String, Serializable> hmReturn = new HashMap<String, Serializable>();

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oJerarquiaAD = new adJerarquiaEclesiastica(con);
            int abadid = oJerarquiaAD.getMonjeIDporJerarquia(p_oAbadia.getIdDeAbadia(), Constantes.JERARQUIA_ABAD);
            int cardenalid = oJerarquiaAD.getMonjeIDporJerarquia(p_oAbadia.getIdDeAbadia(), Constantes.JERARQUIA_CARDENAL);
            int obispoid = oJerarquiaAD.getMonjeIDporJerarquia(p_oAbadia.getIdDeAbadia(), Constantes.JERARQUIA_OBISPO);
            int papaid = oJerarquiaAD.getMonjeIDporJerarquia(p_oAbadia.getIdDeAbadia(), Constantes.JERARQUIA_PAPA);

            oMonjeAD = new adMonje(con);
            if (abadid != 0) {
                Monje abad = oMonjeAD.recuperarDatosMonje(abadid);
                adMonje.actualizarMiniBarrasHTML(abad, p_oResource);
                hmReturn.put("Abad", abad);
                // Elecciones regionales pa el obispo
                oEleccionesAD = new adElecciones(con);
                hmReturn.put("Elecciones", Integer.toString(oEleccionesAD.hayElecciones(p_oAbadia.getIdDeRegion())));

                //flag para ver si el usuario quiere que su abad sea cardenal
                oAbadiaAD = new adAbadia(con);
                p_afDiplomaciaForm.setFlagObispo(oAbadiaAD.getFlagObispo(p_oAbadia.getIdDeAbadia()));
                p_afDiplomaciaForm.setFlagCardenal(oAbadiaAD.getFlagCardenal(p_oAbadia.getIdDeAbadia()));

                hmReturn.put("DiplomaciaForm", p_afDiplomaciaForm);

            }
            if (cardenalid != 0) {
                Monje cardenal = oMonjeAD.recuperarDatosMonje(cardenalid);

                adMonje.actualizarMiniBarrasHTML(cardenal, p_oResource);
                // Función del cardenal
                oCardenalAD = new adCardenal(con);
                int funcion_cardenal = oCardenalAD.getFuncionCardenal(cardenal);

                // Se está celebrando el cónclave
                oPapaAD = new adPapa(con);
                String Conclave = Integer.toString(oPapaAD.estadoConclave());

                hmReturn.put("Cardenal", cardenal);
                hmReturn.put("Conclave", Conclave);
                hmReturn.put("Funcion_Cardenal", Integer.toString(funcion_cardenal));
            }
            if (obispoid != 0) {
                Monje obispo = oMonjeAD.recuperarDatosMonje(obispoid);

                adMonje.actualizarMiniBarrasHTML(obispo, p_oResource);
                hmReturn.put("Obispo", obispo);
            }
            if (papaid != 0) {
                Monje papa = oMonjeAD.recuperarDatosMonje(papaid);

                adMonje.actualizarMiniBarrasHTML(papa, p_oResource);
                hmReturn.put("Papa", papa);
            }

            return hmReturn;

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public HashMap<String, Serializable> recuperarJerarquiaTab1(Abadia p_oAbadia, MessageResources p_oResource) throws AbadiaException {
        String sTrace = this.getClass() + ".recuperarJerarquiaTab1()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adJerarquiaEclesiastica oJerarquiaAD;
        adMonje oMonjeAD;
        adUtils oUtilsAD;
        adComisiones oComisionesAD;


        HashMap<String, Serializable> hmReturn = new HashMap<String, Serializable>();

        String havotadoPapa = "-1", havotadoObispo = "-1";
        Monje oMonje;
        ArrayList alMonjes;
        Iterator itMonjes;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);

            oJerarquiaAD = new adJerarquiaEclesiastica(con);
            int papaid = oJerarquiaAD.getMonjeIDporJerarquia(Constantes.JERARQUIA_PAPA);

            oMonjeAD = new adMonje(con);
            oUtilsAD = new adUtils(con);

            if (papaid != 0) {
                Monje papa = oMonjeAD.recuperarDatosMonje(papaid);
                oMonjeAD.recuperarDescAbadiaRegion(papa);           // <-- recupera la descripción

                adMonje.actualizarMiniBarrasHTML(papa, p_oResource);  // <-- recupera las barras

                hmReturn.put("Papa", papa);
                if (papa.getIdAbadia() != p_oAbadia.getIdDeAbadia()) {

                    havotadoPapa = oUtilsAD.getSQL("SELECT abadiaid FROM `monje_popularidad_votos` where abadiaid=" + p_oAbadia.getIdDeAbadia() +
                            " and monjeid = " + papa.getIdMonje() + " and fecha='" + CoreTiempo.getTiempoRealString() + "'", "0");
                }
                hmReturn.put("havotadoPapa", havotadoPapa);
            }

            /// Cardenales
            oJerarquiaAD = new adJerarquiaEclesiastica(con);
            alMonjes = oJerarquiaAD.getMonjesPorJerarquia(Constantes.JERARQUIA_CARDENAL);

            itMonjes = alMonjes.iterator();
            while (itMonjes.hasNext()) {
                oMonje = (Monje) itMonjes.next();
                adMonje.actualizarMiniBarrasHTML(oMonje, p_oResource);  // <-- recupera las barras
                oMonjeAD.recuperarDescAbadiaRegion(oMonje);           // <-- recupera la descripción
            }
            hmReturn.put("Cardenales", alMonjes);

            // EL Obispo de tu region
            int obispospo = oUtilsAD.getSQL("SELECT monjeid FROM monje m, abadia a WHERE m.abadiaid = a.abadiaid and m.jerarquiaid=3 and a.regionid = " + p_oAbadia.getIdDeRegion(), 0);

            if (obispospo != 0) {
                Monje obispoRegion = oMonjeAD.recuperarDatosMonje(obispospo);
                oMonjeAD.recuperarDescAbadiaRegion(obispoRegion);           // <-- recupera la descripción

                adMonje.actualizarMiniBarrasHTML(obispoRegion, p_oResource);  // <-- recupera las barras
                hmReturn.put("ObispoRegion", obispoRegion);
                if (obispoRegion.getIdAbadia() != p_oAbadia.getIdDeAbadia()) {
                    havotadoObispo = oUtilsAD.getSQL(
                            "SELECT abadiaid FROM `monje_popularidad_votos` where abadiaid=" +
                                    p_oAbadia.getIdDeAbadia() +
                                    " and monjeid = " + obispoRegion.getIdMonje() +
                                    " and fecha='" + CoreTiempo.getTiempoRealString() + "'", "0");
                }
                hmReturn.put("havotadoObispo", havotadoObispo);
                // Impuestos
                oComisionesAD = new adComisiones(con);
                Comisiones comision = oComisionesAD.getComision(p_oAbadia.getIdDeRegion());
                hmReturn.put("ComisionRegion", comision);

            }

            return hmReturn;

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public HashMap<String, Serializable> recuperarJerarquiaTab2(MessageResources p_oResource) throws AbadiaException {
        String sTrace = this.getClass() + ".recuperarJerarquiaTab2()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adJerarquiaEclesiastica oJerarquiaAD;
        adMonje oMonjeAD;


        HashMap<String, Serializable> hmReturn = new HashMap<String, Serializable>();

        Monje oMonje;
        ArrayList alMonjes;
        Iterator itMonjes;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);

            // EL PAPA
            oJerarquiaAD = new adJerarquiaEclesiastica(con);
            int papaid = oJerarquiaAD.getMonjeIDporJerarquia(Constantes.JERARQUIA_PAPA);

            oMonjeAD = new adMonje(con);
            if (papaid != 0) {
                Monje papa = oMonjeAD.recuperarDatosMonje(papaid);
                adMonje.actualizarMiniBarrasHTML(papa, p_oResource);  // <-- recupera las barras
                oMonjeAD.recuperarDescAbadiaRegion(papa);           // <-- recupera la descripción
                hmReturn.put("Papa", papa);
            }

            /// Cardenales
            alMonjes = oJerarquiaAD.getMonjesPorJerarquia(Constantes.JERARQUIA_CARDENAL);

            itMonjes = alMonjes.iterator();
            while (itMonjes.hasNext()) {
                oMonje = (Monje) itMonjes.next();
                adMonje.actualizarMiniBarrasHTML(oMonje, p_oResource);  // <-- recupera las barras
                oMonjeAD.recuperarDescAbadiaRegion(oMonje);           // <-- recupera la descripción
            }

            hmReturn.put("Cardenales", alMonjes);

            /// Obispos
            alMonjes = oJerarquiaAD.getMonjesPorJerarquia(Constantes.JERARQUIA_OBISPO);
            itMonjes = alMonjes.iterator();

            while (itMonjes.hasNext()) {
                oMonje = (Monje) itMonjes.next();
                adMonje.actualizarMiniBarrasHTML(oMonje, p_oResource);  // <-- recupera las barras
                oMonjeAD.recuperarDescAbadiaRegion(oMonje);           // <-- recupera la descripción
            }

            hmReturn.put("Obispos", alMonjes);
            return hmReturn;

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public void nombrarCargo(int p_iClave, Abadia p_oAbadia, int p_iJerarquia) throws AbadiaException {
        String sTrace = this.getClass() + ".nombrarCargo()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);


        adJerarquiaEclesiastica oJerarquiaAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oJerarquiaAD = new adJerarquiaEclesiastica(con);
            oJerarquiaAD.setJerarquiaid(p_oAbadia.getIdDeAbadia(), p_iClave, p_iJerarquia);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public void revocarAbad(int p_iClave, Abadia p_oAbadia) throws AbadiaException {
        String sTrace = this.getClass() + ".revocarAbad()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);


        adJerarquiaEclesiastica oJerarquiaAD;
        adHabilidades oHabilidadesAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS, Constantes.AUTOCOMIT_OF);
            oJerarquiaAD = new adJerarquiaEclesiastica(con);
            oJerarquiaAD.setJerarquiaid(p_oAbadia.getIdDeAbadia(), p_iClave, Constantes.JERARQUIA_MONJE);
            // Decrementar la Fe a todos los monjes!
            oHabilidadesAD = new adHabilidades(con);
            //quitamos 20 puntos de fe a todos los monjes de la abadía...
            oHabilidadesAD.decrementarHabilidadesAbadia(p_oAbadia.getIdDeAbadia(), Constantes.HABILIDAD_FE, 20);
            //restamos 2 puntos de carisma al abad expulsado..
            oHabilidadesAD.decrementarHabilidad(p_iClave, Constantes.HABILIDAD_CARISMA, 2);
            ConnectionFactory.commitTransaction(con);
        } finally {
            ConnectionFactory.rollbackTransaction(con);
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public Comisiones gestionImpuestosObispado(ObispadoImpuestosActForm p_afImpuesto, int p_iTab, Abadia p_oAbadia,
                                               int p_iPagina, int p_iOrden, int p_iOrdenId, Point p_pTotal,
                                               MessageResources p_oResource) throws AbadiaException {
        String sTrace = this.getClass() + ".gestionImpuestosObispado()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adComisiones oComisionesAD;

        Comisiones oComision;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oComisionesAD = new adComisiones(con);

            if (p_afImpuesto.getModificar() == 1) {
                Comisiones coms = new Comisiones();
                coms.setRegionid(p_oAbadia.getIdDeRegion());
                if (p_afImpuesto.getCancelacion() > 50) p_afImpuesto.setCancelacion(50);
                coms.setCancelacion(p_afImpuesto.getCancelacion());
                if (p_afImpuesto.getObispado() > 50) p_afImpuesto.setObispado(50);
                coms.setObispado(p_afImpuesto.getObispado());
                if (p_afImpuesto.getVenta5() > 50) p_afImpuesto.setVenta5(50);
                coms.setVenta5(p_afImpuesto.getVenta5());
                if (p_afImpuesto.getVenta10() > 50) p_afImpuesto.setVenta10(50);
                coms.setVenta10(p_afImpuesto.getVenta10());
                if (p_afImpuesto.getVenta15() > 50) p_afImpuesto.setVenta15(50);
                coms.setVenta15(p_afImpuesto.getVenta15());
                if (p_afImpuesto.getTransito() > 50) p_afImpuesto.setTransito(50);
                coms.setTransito(p_afImpuesto.getTransito());

                oComisionesAD.setComision(coms);
            }

            oComision = oComisionesAD.getComision(p_oAbadia.getIdDeRegion());
            switch (p_iTab) {
                // Recaudaciones por día
                case 0:
                    p_afImpuesto.setContribuciones(oComisionesAD.recuperarContribucionesRegion(p_oAbadia, p_oAbadia.getIdDeRegion(), p_iPagina, p_iOrden, p_iOrdenId, p_pTotal, p_oResource, "obispadoImpuestos.do?tab=0"));
                    break;
                // Recaudaciones abadias
                case 1:
                    p_afImpuesto.setContribuciones(oComisionesAD.recuperarContribuciones(p_oAbadia, p_iPagina, p_iOrden, p_iOrdenId, p_pTotal, p_oResource, "obispadoImpuestos.do?tab=1"));
                    break;
                // Pago de comisiones
                case 2:
                    p_afImpuesto.setContribuciones(oComisionesAD.recuperarContribucionesComision(p_oAbadia, p_oAbadia.getIdDeRegion(), p_iPagina, p_iOrden, p_iOrdenId, p_pTotal, p_oResource, "obispadoImpuestos.do?tab=2"));
                    break;
            }

            return oComision;

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }


    }

    public Comisiones gestionImpuestosPapado(ObispadoImpuestosActForm p_afImpuesto, int p_iTab, Abadia p_oAbadia,
                                             int p_iPagina, int p_iOrden, int p_iOrdenId, Point p_pTotal,
                                             MessageResources p_oResource) throws AbadiaException {
        String sTrace = this.getClass() + ".gestionImpuestosPapado()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adComisiones oComisionesAD;

        Comisiones oComision;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oComisionesAD = new adComisiones(con);
            if (p_afImpuesto.getModificar() == 1)      // Modificar los datos
            {
                Comisiones coms = new Comisiones();
                coms.setPapado(p_afImpuesto.getPapado());
                coms.setCardenales(p_afImpuesto.getCardenales());
                oComisionesAD.setComisionPapado(coms);
            }

            oComision = oComisionesAD.getComisionPapado();
            switch (p_iTab) {
                // Recaudaciones por día
                case 0:
                    p_afImpuesto.setContribuciones(oComisionesAD.recuperarContribucionesComision(p_oAbadia, -1, p_iPagina, p_iOrden, p_iOrdenId, p_pTotal, p_oResource, "obispadoImpuestos.do?tab=0"));
                    break;
                // Pago de comisiones
                case 1:
                    p_afImpuesto.setContribuciones(oComisionesAD.recuperarContribucionesComision(p_oAbadia, 0, p_iPagina, p_iOrden, p_iOrdenId, p_pTotal, p_oResource, "obispadoImpuestos.do?tab=1"));
                    break;
            }
            return oComision;

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public void votarObispo(String p_szSeleccion, VotacionActForm p_afDatosVotacion, Abadia p_oAbadia, MessageResources p_oResource) throws AbadiaException {
        String sTrace = this.getClass() + ".votarObispo()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adElecciones oEleccionesAD;
        Votacion oVotacion;

        ArrayList alCandidatos;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oEleccionesAD = new adElecciones(con);
            if (p_szSeleccion != null) {
                if (p_szSeleccion.equals("-1")) {
                    oEleccionesAD.descCandidato(p_oAbadia, p_afDatosVotacion.getDescripcion());
                } else if (!p_szSeleccion.equals(String.valueOf(p_oAbadia.getIdDeAbadia()))) {
                    oEleccionesAD.votarOvispo(p_oAbadia.getIdDeRegion(), p_oAbadia.getIdDeAbadia(), Integer.parseInt(p_szSeleccion));
                }
            }
            alCandidatos = oEleccionesAD.getObisposCandidatos(p_oResource, p_oAbadia);
            oVotacion = oEleccionesAD.getDatosVotacion(p_oAbadia.getIdDeRegion());

            p_afDatosVotacion.setIdRegion(oVotacion.getIdRegion());
            p_afDatosVotacion.setNombreRegion(oVotacion.getNombreRegion());
            p_afDatosVotacion.setFechaInicio(oVotacion.getFechaInicio());
            p_afDatosVotacion.setFechaFin(oVotacion.getFechaFin());
            p_afDatosVotacion.setTotalVotantes(oVotacion.getTotalVotantes());
            p_afDatosVotacion.setPendientesVoto(oVotacion.getPendientesVoto());

            if (oEleccionesAD.puedoVotarObispo(p_oAbadia.getIdDeRegion(), p_oAbadia.getIdDeAbadia())) {
                p_afDatosVotacion.setPuedeVotar(1);
            } else
                p_afDatosVotacion.setPuedeVotar(0);

            p_afDatosVotacion.setCandidatos(alCandidatos);

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public void votarPapa(String p_szSeleccion, VotacionPapaActForm p_afDatosVotacion, Abadia p_oAbadia, MessageResources p_oResource) throws AbadiaException {
        String sTrace = this.getClass() + ".votarPapa()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adElecciones oEleccionesAD;

        ArrayList alCandidatos;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS, Constantes.AUTOCOMIT_OF);
            oEleccionesAD = new adElecciones(con);
            if (p_szSeleccion != null) {
                oEleccionesAD.votarPapa(p_oAbadia.getIdDeAbadia(), Integer.parseInt(p_szSeleccion), p_oResource);
            }

            alCandidatos = oEleccionesAD.getCardenalesCandidatos(p_oResource);
            if (oEleccionesAD.puedoVotarCardenal(p_oAbadia.getIdDeAbadia()))
                p_afDatosVotacion.setPuedeVotar(1);
            else p_afDatosVotacion.setPuedeVotar(0);

            //
            p_afDatosVotacion.setCandidatos(alCandidatos);

            ConnectionFactory.commitTransaction(con);

        } catch (AbadiaException e) {
            ConnectionFactory.rollbackTransaction(con);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

}
