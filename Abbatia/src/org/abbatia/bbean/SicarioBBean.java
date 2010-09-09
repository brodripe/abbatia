package org.abbatia.bbean;

import org.abbatia.adbean.*;
import org.abbatia.bean.*;
import org.abbatia.core.CoreTiempo;
import org.abbatia.dbms.ConnectionFactory;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.MonjeNoEncontradoException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.Utilidades;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;

import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Benjamín Rodríguez.
 * User: benjamin
 * Date: 24-may-2008
 * Time: 21:06:39
 */
public class SicarioBBean {
    private static Logger log = Logger.getLogger(SicarioBBean.class.getName());


    public ArrayList<Sicarios> recuperarSicarios(Abadia p_oAbadia, Usuario p_oUsuario) throws AbadiaException {
        String sTrace = this.getClass() + ".recuperarSicarios()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adSicarios oSicariosAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oSicariosAD = new adSicarios(con);
            return oSicariosAD.recuperarSicarios(p_oAbadia, p_oUsuario);

        } catch (AbadiaException e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public HashMap<String, Serializable> contratarSicario_1(int p_iSicarioId, int p_iMonjeId, Abadia p_oAbadia, MessageResources p_oResources, ActionMessages p_amMensajes) throws AbadiaException {
        String sTrace = this.getClass() + ".contratarSicario_1()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adSicarios oSicariosAD;
        adInicioContents oInicioContents;
        adUtils oUtilsAD;
        adMonje oMonjeAD;

        InicioContents oContents;
        HashMap<String, Serializable> hmRequest = new HashMap<String, Serializable>();
        MonjeVisita oMonjeVisita;
        String sNombreSicario;
        int iDistCamino, iDistMont, iDistBarco;
        int iPrecio;
        boolean bResult;
        ArrayList<Notificacion> alNotas = new ArrayList<Notificacion>();


        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);

            oContents = new InicioContents();
            oInicioContents = new adInicioContents(con);
            oInicioContents.getRecursos(p_oAbadia, oContents);
            hmRequest.put("DatosContents", oContents);
            //request.setAttribute("DatosContents", datos);
            //
            oUtilsAD = new adUtils(con);
            sNombreSicario = oUtilsAD.getSQL("SELECT Concat(NOMBRE, ' ', APELLIDO) FROM sicario where sicarioid=" + p_iSicarioId, "xx");
            //Debemos buscar la abadía en la que se encuentra la eminencia por si está de viaje
            oMonjeAD = new adMonje(con);
            oMonjeVisita = oMonjeAD.recuperarLocalizacionMonje(p_iMonjeId, p_oResources);

            //
            // Distancias
            iDistCamino = oUtilsAD.getSQL("SELECT distancia_campo FROM `region_distancia` where regionid_origen = " + p_oAbadia.getIdDeRegion() + " and regionid_destino = " + oMonjeVisita.getIdRegion(), 5);
            iDistMont = oUtilsAD.getSQL("SELECT distancia_montanyas FROM `region_distancia` where regionid_origen = " + p_oAbadia.getIdDeRegion() + " and regionid_destino = " + oMonjeVisita.getIdRegion(), 1);
            iDistBarco = oUtilsAD.getSQL("SELECT distancia_mar FROM `region_distancia` where regionid_origen = " + p_oAbadia.getIdDeRegion() + " and regionid_destino = " + oMonjeVisita.getIdRegion(), 0);

            if (oMonjeVisita.getIdAbadia() == p_oAbadia.getIdDeAbadia()) {
                alNotas.add(new Notificacion("poblacion.do", "Volver a la población"));
                hmRequest.put("notificacion", alNotas);
                //request.getSession().setAttribute("notificacion", alNotas);
                p_amMensajes.add("msg", new ActionMessage("sicario.confirmar.mismaabadia"));
                //saveMessages(request.getSession(), mensajes);
                //  return mapping.findForward("mensajes");
                return hmRequest;
            }
            // Precio
            oSicariosAD = new adSicarios(con);
            iPrecio = oSicariosAD.recuperarPrecioSicario(p_oAbadia, p_iSicarioId, p_iMonjeId);
            bResult = oSicariosAD.validarSicario(p_oAbadia, p_iSicarioId);

            if (!bResult) {
                p_amMensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.tramposo"));
                //saveMessages(request.getSession(), mensajes);
                //return mapping.findForward("failure");
                return hmRequest;
            }

            hmRequest.put("Nom_Sicario", sNombreSicario);
            hmRequest.put("Nom_Monje", oMonjeVisita.getNombreMonje());
            hmRequest.put("Nom_Abadia", oMonjeVisita.getNombreAbadia());
            hmRequest.put("Nom_Region", oMonjeVisita.getNombreRegion());
            hmRequest.put("Dias", String.valueOf(iDistCamino + iDistMont + iDistBarco + 2));
            hmRequest.put("Precio", Utilidades.redondear(iPrecio));
            //
            hmRequest.put("monjeid", String.valueOf(p_iMonjeId));
            hmRequest.put("sicarioid", String.valueOf(p_iSicarioId));

            return hmRequest;

        } catch (AbadiaException e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public HashMap<String, ArrayList<Notificacion>> contratarSicario_2(Abadia p_oAbadia, int p_iSicarioId, int p_iMonjeId, ActionMessages p_amMensajes, MessageResources p_oResources) throws AbadiaException {
        String sTrace = this.getClass() + ".contratarSicario_2()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adRecurso oRecursoAD;
        adSicarios oSicariosAD;
        adMonje oMonjeAD;

        Recurso oRecurso;
        int iPrecio;
        boolean bResult;
        MonjeVisita oMonjeVisita;
        ArrayList<Notificacion> alNotas = new ArrayList<Notificacion>();

        HashMap<String, ArrayList<Notificacion>> hmRequest = new HashMap<String, ArrayList<Notificacion>>();

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS, Constantes.AUTOCOMIT_OF);

            oRecursoAD = new adRecurso(con);
            oRecurso = oRecursoAD.recuperarRecurso(0, p_oAbadia.getIdDeAbadia());

            oSicariosAD = new adSicarios(con);
            iPrecio = oSicariosAD.recuperarPrecioSicario(p_oAbadia, p_iSicarioId, p_iMonjeId);
            bResult = oSicariosAD.validarSicario(p_oAbadia, p_iSicarioId);

            if (!bResult) {
                p_amMensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.tramposo"));
                //saveMessages(request.getSession(), mensajes);
                //return mapping.findForward("failure");
                return hmRequest;
            }
            // Comprobar que tiene monjes en la abbatia
            oMonjeAD = new adMonje(con);
            if (oMonjeAD.getNumMonjes(p_oAbadia.getIdDeAbadia()) <= 0) {
                p_amMensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.nomonjes"));
                //saveMessages(request.getSession(), mensajes);
                //return mapping.findForward("failure");
                return hmRequest;
            }
            oMonjeVisita = oMonjeAD.recuperarLocalizacionMonje(p_iMonjeId, p_oResources);
            // Tenemos dinero?
            if (iPrecio > oRecurso.getCantidad()) {
                alNotas.add(new Notificacion("poblacion.do", "Volver a la población"));
                hmRequest.put("notificacion", alNotas);
                //request.getSession().setAttribute("notificacion", alNotas);
                p_amMensajes.add("msg", new ActionMessage("sicario.confirmar.sindinero"));
                //saveMessages(request.getSession(), mensajes);
                //return mapping.findForward("mensajes");
                return hmRequest;
            }
            // Tenemos dinero... a contratarlo
            oRecursoAD.restarRecurso(0, p_oAbadia.getIdDeAbadia(), iPrecio);        // Restar las pelas
            // Contratar sicario
            oSicariosAD.contratarSicario(p_oAbadia, p_iSicarioId, oMonjeVisita, iPrecio);
            // Mensaje
            alNotas.add(new Notificacion("poblacion.do", "Volver a la población"));
            hmRequest.put("notificacion", alNotas);
            //request.getSession().setAttribute("notificacion", alNotas);
            p_amMensajes.add("msg", new ActionMessage("sicario.confirmar.contratado"));
            //saveMessages(request.getSession(), mensajes);
            //return mapping.findForward("mensajes");
            ConnectionFactory.commitTransaction(con);
            return hmRequest;

        } catch (AbadiaException e) {
            ConnectionFactory.rollbackTransaction(con);
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public void asesinar(int sicarioid) throws AbadiaException {
        String sTrace = this.getClass() + ".asesinar()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        Sicarios sicario = null;
        Monje monje;
        boolean pillan = false;
        boolean confiesa = false;
        boolean matar_sicario = false;
        boolean entrado = false;
        boolean muerto = false;
        String nom_abadia_destino;
        String nom_abadia_contrato;
        int guardias;
        int nmonjes;
        int nEminencias;
        int niv_muralla;
        long idAbadia;
        int guardiasNecesarios;

        adMonje oMonjeAD;
        adUtils oUtilsAD;
        adSicarios oSicariosAD;
        adMensajes oMensajesAD;

        MonjeBBean oMonjeBBean;

        Connection con = null;

        try {
            oMonjeBBean = new MonjeBBean();
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS, Constantes.AUTOCOMIT_OF);

            oSicariosAD = new adSicarios(con);
            sicario = oSicariosAD.recuperarSicario(sicarioid);

            // Info del Monje a matar
            oMonjeAD = new adMonje(con);
            monje = oMonjeAD.recuperarDatosMonje(sicario.getMonjeid_destino());

            int popularidad = monje.getPopularidad();

            oUtilsAD = new adUtils(con);
            //String nom_sicario = oUtilsAD.getSQL("SELECT Concat(NOMBRE, ' ', APELLIDO) FROM sicario where sicarioid=" + sicarioid, "xx");
            nom_abadia_contrato = oUtilsAD.getSQL("SELECT nombre FROM abadia WHERE abadiaid =" + sicario.getAbadiaid_contrato(), "");
            //String nom_monje_aux = monje.getNombre();//oUtilsAD.getSQL("SELECT Nombre FROM monje WHERE estado=0 AND monjeid="+sicario.getMonjeid_destino(), "xx");
            //int jerarquiaid = monje.getIdDeJerarquia(); //oUtilsAD.getSQL("SELECT jerarquiaid FROM monje WHERE estado=0 AND monjeid="+sicario.getMonjeid_destino(), 0);
            String nom_jerarquia = oUtilsAD.getSQL("SELECT l.literal FROM `literales` l,`jerarquia_eclesiastica` je WHERE je.jerarquiaid = " + monje.getIdDeJerarquia() + " and " +
                    "l.literalid = je.literalid and l.idiomaid = 1", "");

            String nom_monje = nom_jerarquia + " " + monje.getNombre() + " ";


            nom_monje = nom_monje + " " + monje.getApellido1();
            //verificar si la eminencia está en su abadía o en otra.
            if (monje.getEstado() == Constantes.MONJE_VISITA) {
                //si está de visita, buscamos la abadía en la que se encuentra..
                idAbadia = oUtilsAD.getSQL("SELECT ABADIAID_DESTINO from monje_visita where monjeid = " + sicario.getMonjeid_destino(), 0);
            } else {
                idAbadia = monje.getIdAbadia();
            }
            // Info de la abbatia
            nom_abadia_destino = oUtilsAD.getSQL("SELECT a.nombre FROM abadia a where a.abadiaid = " + idAbadia, "xx");
            guardias = oUtilsAD.getSQL("SELECT cantidad FROM `recurso` WHERE recursoid = " + Constantes.RECURSOS_GUARDIA + " AND abadiaid = " + idAbadia, 0);
            nmonjes = oUtilsAD.getSQL("SELECT count(*) FROM monje WHERE abadiaid = " + idAbadia + " and jerarquiaid < 3", 0);

            //número de eminencias.
            nEminencias = oUtilsAD.getSQL("SELECT count(*) from monje WHERE abadiaid = " + idAbadia + " and jerarquiaid > 2", 0);
            niv_muralla = oUtilsAD.getSQL("SELECT nivel FROM `edificio` WHERE tipoedificioid = 0 AND abadiaid = " + idAbadia, 0);
            //String nom_abadia   = oUtilsAD.getSQL("SELECT nombre FROM abadia WHERE abadiaid = "+sicario.getAbadiaid_destino(), "");

            int literalid = 0;

            /*
                Gestionar las visitas de las eminencias para que el sicario lo busque controlando las fechas.

                monje_visita
            */

            // EL monje sigue vivo?
            if (!monje.getNombre().equals("xx")) {
                // La proporción de guardias es de 2 guardias por monje para alcanzar el 33% de protección máxima
                //int prop_guardias = ((guardias / nmonjes) * 33) / 2;
                int prop_guardias;
                guardiasNecesarios = nmonjes * 3 + nEminencias * 50;
                if (guardias >= guardiasNecesarios) {
                    prop_guardias = 15;
                } else {
                    prop_guardias = Math.round((15 * guardias / guardiasNecesarios) + Float.valueOf("0.5"));
                }

/*
                if (guardias > nmonjes * 2) {
                    guardias = nmonjes * 2;
                }
*/


                // El nivel máximo de la muralla es de 4 y se puede conseguir un 33% máximo de proteccion
                //int prop_nmuralla = ((niv_muralla * 33) / 4);
                int prop_nmuralla = 0;
                switch (niv_muralla) {
                    case 0:
                        prop_nmuralla = 0;
                        break;
                    case 1:
                        prop_nmuralla = 5;
                        break;
                    case 2:
                        prop_nmuralla = 7;
                        break;
                    case 3:
                        prop_nmuralla = 10;
                        break;
                    case 4:
                        prop_nmuralla = 15;
                        break;
                }

                // Popularidad, cuanto más mejora para protejerse
                //int prop_popular = ((popularidad * 33) / 100);
                int prop_popular = Math.round(((popularidad * 70) / 99) + Float.valueOf("0.5"));

                int proteccion = prop_guardias + prop_nmuralla + prop_popular;
                //le restamos a la protección la experiencia del sicario
                proteccion = proteccion - (sicario.getObispos_muertos() + sicario.getPapas_muertos() + sicario.getCardenales_muertos());
                //si la protección no alcanza los 5... lo forzamos para darle alguna posibilidad a la eminencia.
                if (proteccion < 5)
                    proteccion = 5;
                int entrar = Utilidades.Random(0, 100);
                if (entrar > proteccion) {            // Ha entrado y ha llegado hasta la eminencia!
                    entrado = true;
                    muerto = true;
                    // Lo va a matar?
/*
                    int matar = Utilidades.Random(0, 100);
                    if (matar < sicario.getMoral()) { // Lo ha matado!!!
                        muerto = true;
                    }
*/
                }

                // Dependiendo de la guardia, lo pueden pillar o no
                int nguardias = ((guardias / nmonjes) * 100) / 2;
                //tiene un 20 por ciento de posibilidades de que no lo pillen si existe el maximo de guardias.
                int pillar = Utilidades.Random(0, 120);
                if (nguardias >= pillar) {            // Lo pillan
                    pillan = true;
                    int confesar = Utilidades.Random(0, 120);
                    if (sicario.getMoral() < confesar) {
                        confiesa = true;
                    }
                }

                // Mensajes
                oMensajesAD = new adMensajes(con);
                // Lo ha matado?
                if (muerto) {

                    oMensajesAD.crearMensajesParaTodos(0, 12030, nom_monje, nom_abadia_destino, null, null);  // Un sicario ha matado a %s
                    oMonjeBBean.morirMonje(sicario.getAbadiaid_destino(), sicario.getMonjeid_destino(), 13003, con);  // Fué asesinado por un Sicario desalmado.
                }
                // Lo pillan pero no ha entrado
                if ((pillan) && (!entrado)) {
                    if (confiesa) {
                        oMensajesAD.crearMensajesParaTodos(0, 12021, sicario.getNombre(), nom_abadia_destino, nom_abadia_contrato, nom_monje);  // Han encontrado el sicario %s merodeando la abbatia %s y ha confesado que le contrato la abbatia %s para matar a %
                    } else {
                        oMensajesAD.crearMensajesParaTodos(0, 12020, nom_abadia_destino, null, null, null);  // Merodeando la abbatia %s han encontrado un sicario sospechoso, pero se le ha dejado libre porque no ha confesado nada.
                    }
                }
                // Lo pillan y ha entrado dentro
                if ((pillan) && (entrado)) {
                    // Lo ha matado?
                    if (muerto) {
                        if (confiesa) {
                            oMensajesAD.crearMensajesParaTodos(0, 12023, sicario.getNombre(), nom_abadia_destino, nom_abadia_contrato, nom_monje);  // Han encontrado el sicario %s merodeando la abbatia %s y ha confesado que le contrato la abbatia %s para matar a %
                            matar_sicario = true;
                        } else {
                            oMensajesAD.crearMensajesParaTodos(0, 12024, nom_abadia_destino, nom_monje, null, null);  // En la abbatia %s han encontrado el sicario que ha matado a %s, le han torturado pero no ha confesado quien lo ha contratado.
                            matar_sicario = true;
                        }
                    } else {
                        if (confiesa) {
                            oMensajesAD.crearMensajesParaTodos(0, 12023, sicario.getNombre(), nom_abadia_destino, nom_abadia_contrato, nom_monje);  // Han encontrado el sicario %s dentro de la abbatia %s y ha confesado que le contrato la abbatia %s para matar a %
                        } else {
                            oMensajesAD.crearMensajesParaTodos(0, 12022, nom_abadia_destino, null, null, null); // Dentro de la abbatia %s han encontrado un sicario sospechoso, le han torturado pero no ha confesado el porque habia entrado.
                        }
                    }
                }

                // Abadia que contrato el sicario
                if (confiesa) {
                    int monje_contrato = oUtilsAD.getSQL("SELECT m.monjeid FROM monje m, habilidad_monje mh where m.monjeid = mh.monjeid and mh.habilidadid = " + Constantes.HABILIDAD_CARISMA + " and m.abadiaid = " + sicario.getAbadiaid_contrato() + " order by mh.valor_actual desc limit 1", 0);
                    String nom_monje_contrato = oUtilsAD.getSQL("SELECT Concat(NOMBRE, ' ', APELLIDO1) FROM monje where monjeid=" + monje_contrato, "xx");
                    oUtilsAD.execSQL("UPDATE monje SET estado=1, fecha_fallecimiento='" + CoreTiempo.getTiempoAbadiaString() + "' WHERE monjeid = " + monje_contrato);

                    if (muerto) {
                        oMensajesAD.crearMensajesParaTodos(0, 12026, nom_monje_contrato, nom_abadia_contrato, nom_monje, null);  // La Santa Inquisición ha quemado a %s de la abbatia %s por contratar un sicario y dar muerte a %s
                        literalid = 13005;
                    } else {
                        oMensajesAD.crearMensajesParaTodos(0, 12025, nom_monje_contrato, nom_abadia_contrato, null, null); // La Santa Inquisición ha quemado a %s de la abbatia %s por contratar un sicario
                        literalid = 13004;
                    }

                    oMonjeBBean.morirMonje(sicario.getAbadiaid_contrato(), monje_contrato, literalid, con);  // Fué asesinado por un Sicario desalmado.
                }
                //mensajes.finalize();
            }

            // Sicario
            if (matar_sicario) {
                oUtilsAD.execSQL("DELETE FROM sicario WHERE sicarioid=" + sicario.getIdSicario());
            } else {
                if ((pillan) || (muerto)) {
                    String sSQL = "UPDATE sicario SET estado=2 ";
                    int moral = 5;
                    int precio = 100;
                    if (muerto) {
                        String campo = "OBISPOS_MUERTOS";
                        switch (monje.getIdDeJerarquia()) {
                            case 6:
                                campo = "PAPAS_MUERTOS";
                                moral = 20;
                                precio = 5000;
                                break;
                            case 5:
                                campo = "CARDENALES_MUERTOS";
                                moral = 10;
                                precio = 1000;
                                break;
                            default:
                                break;
                        }
                        sSQL = sSQL + ", " + campo + "=" + campo + "+1 ";
                    }

                    if (pillan) {
                        moral = sicario.getMoral() - moral;
                        precio = sicario.getPrecio() - precio;
                    } else {
                        moral = sicario.getMoral() + moral;
                        precio = sicario.getPrecio() + precio;
                    }
                    if (moral < 0) moral = 0;
                    if (precio < 0) precio = 0;
                    sSQL = sSQL + ", moral=" + moral + ", precio=" + precio + " WHERE sicarioid=" + sicario.getIdSicario();
                    oUtilsAD.execSQL(sSQL);
                }
            }
            ConnectionFactory.commitTransaction(con);

        } catch (MonjeNoEncontradoException e) {
            //no se encuentra el monje que hay que matar... no pasa nada.
            if (sicario != null)
                msgLog = sTrace + " No se ha encontrado el monje al que hay que asesinar: " + sicario.getMonjeid_destino();
            else
                msgLog = sTrace + " No se ha encontrado el monje al que hay que asesinar: xx";
            log.info(msgLog);
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

}
