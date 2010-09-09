package org.abbatia.adbean;

import org.abbatia.adbean.base.adbeans;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Candidato;
import org.abbatia.bean.Votacion;
import org.abbatia.core.CoreTiempo;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.Utilidades;
import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Benjamín Rodríguez.
 * User: Benjamin
 * Date: 25-oct-2004
 * Time: 12:23:06
 */
public class adElecciones extends adbeans {
    private static Logger log = Logger.getLogger(adElecciones.class.getName());

    /**
     * Instancia la clase sin obtener una nueva conexión
     *
     * @param con
     * @throws AbadiaException
     */
    public adElecciones(Connection con) throws AbadiaException {
        super(con);
    }

    //devuelve una lista de claves de abadías candidatas a la eleccion de obispo
    //separadas por coma para incorporar en un select in
    public String getAbadiasCandidatasObispo(int idRegion) throws AbadiaException {
        StringBuffer candidatas = new StringBuffer();
        String sSQL = "select abadiaid from obispado_candidatos where regionid = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idRegion);
            rs = ps.executeQuery();
            while (rs.next()) {
                candidatas.append(rs.getString("abadiaid").concat(","));
            }

            if (candidatas.length() > 0) {
                return candidatas.substring(0, candidatas.length() - 1);
            } else return "";


        } catch (SQLException e) {
            throw new AbadiaSQLException("adElecciones. getObisposCandidatos. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public ArrayList<Candidato> getObisposCandidatos(MessageResources msg, Abadia abadia) throws AbadiaException {
        String sSQL = "select m.monjeid, m.nombre as NOMBRE_MONJE, m.apellido1, m.abadiaid AS ID_ABADIA, a.nombre as NOMBRE_ABADIA, o.descripcion orden, o.ordenid, a.regionid, oc.descripcion " +
                " from monje m, abadia a, orden_eclesiastica o, obispado_candidatos oc " +
                " where m.abadiaid=a.abadiaid and a.ordenid=o.ordenid and m.abadiaid = oc.abadiaid and oc.regionid=? and m.jerarquiaid = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Candidato> candidatos = new ArrayList<Candidato>();
        try {
            ps = con.prepareStatement(sSQL);
            //ps.setString(1, candidatas);
            ps.setInt(1, abadia.getIdDeRegion());
            ps.setInt(2, Constantes.JERARQUIA_ABAD);
            rs = ps.executeQuery();
            Candidato candidato;
            while (rs.next()) {
                candidato = new Candidato();
                candidato.setIdAbadia(rs.getInt("ID_ABADIA"));
                candidato.setIdMonje(rs.getInt("MONJEID"));
                candidato.setIdOrden(rs.getInt("ORDENID"));
                candidato.setNombreAbadia(rs.getString("NOMBRE_ABADIA"));
                candidato.setNombreMonje(rs.getString("NOMBRE_MONJE") + " " + msg.getMessage("monjes.abadia.nomciudad") + " " + rs.getString("APELLIDO1"));
                candidato.setNombreOrden(rs.getString("ORDEN"));
                candidato.setDescripcion(rs.getString("DESCRIPCION"));
                candidato.setIdRegion(rs.getInt("REGIONID"));
                candidato.setVotos(getVotosAbadiaObispo(candidato.getIdRegion(), candidato.getIdAbadia()));
                if (rs.getInt("ID_ABADIA") == abadia.getIdDeAbadia()) {
                    candidato.setVotable(0);
                } else candidato.setVotable(1);

                candidatos.add(candidato);
            }

            return candidatos;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adElecciones. getObisposCandidatos. SQLException: " + sSQL, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public ArrayList<Candidato> getCardenalesCandidatos(MessageResources msg) throws AbadiaException {
        String sSQL = "select m.monjeid, m.nombre as NOMBRE_MONJE, m.apellido1, m.abadiaid AS ID_ABADIA, a.nombre as NOMBRE_ABADIA, o.descripcion orden, o.ordenid, a.regionid " +
                " from monje m, abadia a, orden_eclesiastica o, cardenales c " +
                " where m.abadiaid=a.abadiaid and a.ordenid=o.ordenid and m.jerarquiaid = ? and m.monjeid=c.monjeid ";
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Candidato> candidatos = new ArrayList<Candidato>();
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, Constantes.JERARQUIA_CARDENAL);
            rs = ps.executeQuery();
            Candidato candidato;
            while (rs.next()) {
                candidato = new Candidato();
                candidato.setIdAbadia(rs.getInt("ID_ABADIA"));
                candidato.setIdMonje(rs.getInt("MONJEID"));
                candidato.setIdOrden(rs.getInt("ORDENID"));
                candidato.setNombreAbadia(rs.getString("NOMBRE_ABADIA"));
                candidato.setNombreMonje(rs.getString("NOMBRE_MONJE") + " " + msg.getMessage("monjes.abadia.nomciudad") + " " + rs.getString("APELLIDO1"));
                candidato.setNombreOrden(rs.getString("ORDEN"));

                candidatos.add(candidato);
            }

            return candidatos;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adElecciones. getObisposCandidatos. SQLException: " + sSQL, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }


    // Función que devuelve si se está produciendo una votación en la región
    public boolean puedoVotarObispo(int idRegion, int idAbadia) throws AbadiaException {
        String sSQL = "select * from obispado_voto where regionid = ? and abadiaid = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idRegion);
            ps.setInt(2, idAbadia);
            rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            throw new AbadiaSQLException("adElecciones. puedoVotarObispo. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    // Función que devuelve si se está produciendo una votación en la región, y los dias que quedan para que finalicen
    public int hayElecciones(int idRegion) throws AbadiaException {
        String sSQL = "select (to_days(fecha_votacion)+30)-to_days(?)  from obispado where regionid = ? and fecha_votacion is not null";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setString(1, CoreTiempo.getTiempoAbadiaString());
            ps.setInt(2, idRegion);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            } else return -1;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adElecciones. hayElecciones. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    // Función que devuelve si se está produciendo una votación en la región, y los dias que quedan para que finalicen
    public Votacion getDatosVotacion(int idRegion) throws AbadiaException {
        String sSQL = "select o.fecha_votacion as FECHA_INICIO, DATE_ADD(o.fecha_votacion, interval 30 day) as FECHA_FIN, r.descripcion, r.regionid " +
                " from obispado o, region r " +
                " where o.regionid = ? and o.fecha_votacion is not null and o.regionid=r.regionid";
        PreparedStatement ps = null;
        ResultSet rs = null;
        Votacion datosVotacion = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idRegion);
            rs = ps.executeQuery();
            if (rs.next()) {
                datosVotacion = new Votacion();
                datosVotacion.setIdRegion(rs.getInt("REGIONID"));
                datosVotacion.setNombreRegion(rs.getString("DESCRIPCION"));
                datosVotacion.setFechaInicio(Utilidades.formatStringFromDB(rs.getString("FECHA_INICIO")));
                datosVotacion.setFechaFin(Utilidades.formatStringFromDB(rs.getString("FECHA_FIN")));
                datosVotacion.setTotalVotantes(getNumVotantesPorRegion(idRegion));
                datosVotacion.setPendientesVoto(getNumVotosPendientes(idRegion));
            }

            return datosVotacion;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adElecciones. getDatosVotacion. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public void descCandidato(Abadia abadia, String desc) throws AbadiaException {
        String sSQL = "update obispado_candidatos set descripcion = ? WHERE ABADIAID = ? AND REGIONID = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement(sSQL);
            ps.setString(1, desc);
            ps.setLong(2, abadia.getIdDeAbadia());
            ps.setInt(3, abadia.getIdDeRegion());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new AbadiaSQLException("adElecciones. descCandidato. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }


    public void votarOvispo(int idRegion, int idAbadia, int voto) throws AbadiaException {
        String sSQL = "update obispado_voto set ABADIAID_VOTO = ? where REGIONID = ? and ABADIAID = ? ";
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, voto);
            ps.setInt(2, idRegion);
            ps.setInt(3, idAbadia);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new AbadiaSQLException("adElecciones. votarOvispo. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public int getVotosAbadiaObispo(int idRegion, int idAbadia) throws AbadiaException {
        String sSQL = "select count(*) as VOTOS from obispado_voto where regionid = ? and abadiaid_voto = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idRegion);
            ps.setInt(2, idAbadia);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("VOTOS");
            } else return 0;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adElecciones. getVotosAbadiaObispo. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public int getNumVotantesPorRegion(int idRegion) throws AbadiaException {
        String sSQL = "select count(*) as VOTANTES from obispado_voto where regionid = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idRegion);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("VOTANTES");
            } else return 0;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adElecciones. getNumVotantesPorRegion. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public int getNumVotosPendientes(int idRegion) throws AbadiaException {
        String sSQL = "select count(*) as VOTANTES from obispado_voto where regionid = ? and abadiaid_voto=0";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idRegion);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("VOTANTES");
            } else return 0;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adElecciones. getNumVotosPendientes. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /*  Estado del conclave
        0-Sin conclave
        1-monjes viajando
        2-celebrando conclave
        3-conclave finalizado
    */
    public void votarPapa(int idAbadia, int voto, MessageResources resource) throws AbadiaException {
        String sSQL = "UPDATE cardenales_conclave cc, cardenales c " +
                "                SET cc.VOTO_CARDENALID=?, cc.VOTO_FECHA = Now()  " +
                "                WHERE cc.CARDENALID=c.CARDENALID AND cc.VOTO_CARDENALID = 0  AND c.ABADIAID = ? ";
        String sSQLVotos = "SELECT voto_cardenalid, count(*) contador " +
                " FROM `cardenales_conclave`" +
                " where voto_cardenalid <>0" +
                " group by voto_cardenalid" +
                " order by contador desc Limit 1";
        String sSQLCardenales = "SELECT count(*) FROM cardenales_conclave cc, cardenales c, monje m " +
                " WHERE cc.CARDENALID=c.CARDENALID AND c.MONJEID = m.MONJEID AND ( m.ESTADO = 4 OR m.ESTADO = 0 ) ";
        String sSQLCardenalesVotados = "SELECT Count(*) FROM cardenales_conclave WHERE voto_cardenalid <> 0";
        String sSQLCardenalesVolver = "SELECT * from monje_visita mv, cardenales c " +
                " where mv.monjeid = c.monjeid";

        PreparedStatement ps = null;
        ResultSet rs = null;
        adUtils utils;
        adMensajes mensajes;
        adViajar viaje;
        adJerarquiaEclesiastica jerarquia;

        int candidatos = 0, nvotos = 0;
        boolean volver = false;

        try {
            // Votar por el monje
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, voto);
            ps.setInt(2, idAbadia);
            ps.executeUpdate();
            ps.close();

            // Número de candidatos
            ps = con.prepareStatement(sSQLCardenales);
            rs = ps.executeQuery();
            if (rs.next())
                candidatos = rs.getInt(1);
            rs.close();
            ps.close();

            // Número de votos realizados
            ps = con.prepareStatement(sSQLCardenalesVotados);
            rs = ps.executeQuery();
            if (rs.next())
                nvotos = rs.getInt(1);
            rs.close();
            ps.close();

            // Averiguar los votos que tiene el cardenal más votado
            ps = con.prepareStatement(sSQLVotos);
            rs = ps.executeQuery();
            if (rs.next()) {
                int por_candidatos = (candidatos * 70) / 100;
                if (rs.getInt("Contador") >= por_candidatos) {
                    // Tenemos monje... fumata blanca!!!!!
                    jerarquia = new adJerarquiaEclesiastica(con);
                    jerarquia.setJerarquiaid(rs.getInt("voto_cardenalid"), Constantes.JERARQUIA_PAPA);
                    // Cambiar estados
                    utils = new adUtils(con);
                    utils.execSQL("UPDATE `conclave` SET estado = 3, fumata = 1, monjeid_papa_nuevo = " + rs.getInt("voto_cardenalid") + " WHERE estado = 2");
                    // MENSAJE
                    String nombre = utils.getSQL("SELECT nombre from monje where monjeid = " + rs.getInt("voto_cardenalid"), "?");
                    String abadia = utils.getSQL("SELECT a.nombre from abadia a, monje m where a.abadiaid = m.abadiaid and m.monjeid = " + rs.getInt("voto_cardenalid"), "?");

                    mensajes = new adMensajes(con);
                    // El monje %s de la abbatia %s ha sido nombrado Papa
                    mensajes.crearMensajesParaTodos(0, 12002, nombre, abadia, null, null);
                    // Marcar la vuelta de los monjes
                    volver = true;
                } else {
                    // Hemos pasado el porcentaje y no tenemos candidato... fumata negra
                    if (nvotos >= por_candidatos) {
                        utils = new adUtils(con);
                        // Marcar como nulo
                        utils.execSQL("UPDATE `conclave` SET Fumata = 2, votos_nulos = votos_nulos + 1 WHERE estado = 2");
                        // Restablecer los votos y enviar un mensaje para decir que se vuelve a proceder la votacion
                        utils.execSQL("UPDATE `cardenales_conclave` SET voto_cardenalid = 0");
                        // Mensaje
                        mensajes = new adMensajes(con);
                        // No ha habido consenso entre los cardenales, se reanudan las votaciones.
                        mensajes.crearMensajesParaTodos(0, 12004, null, null, null, null);
                    }
                }
            }
            rs.close();
            ps.close();

            // Tienen que volver los monjes????
            if (volver) {
                viaje = new adViajar(con);
                ps = con.prepareStatement(sSQLCardenalesVolver);
                rs = ps.executeQuery();
                while (rs.next()) {
                    viaje.forzarVuelta(rs.getInt("abadiaid"), rs.getInt("monjeid"), "", resource);
                }
            }
            rs.close();
            ps.close();

        } catch (SQLException e) {
            throw new AbadiaSQLException("adElecciones. votarPapa. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    // Función que devuelve si se está produciendo una votación en la región
    public boolean puedoVotarCardenal(int idAbadia) throws AbadiaException {
        String sSQL = "SELECT cc.VOTO_CARDENALID FROM cardenales_conclave cc, cardenales c " +
                " WHERE cc.CARDENALID=c.CARDENALID AND cc.VOTO_CARDENALID = 0 AND c.ABADIAID =  ? ";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idAbadia);
            rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            throw new AbadiaSQLException("adElecciones. puedoVotarCardenal. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

}
