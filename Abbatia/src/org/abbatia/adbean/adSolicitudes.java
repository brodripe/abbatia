package org.abbatia.adbean;

import org.abbatia.adbean.base.adbeans;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Solicitud;
import org.abbatia.bean.Usuario;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Utilidades;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Benjamin
 * Date: 14-mar-2005
 * Time: 0:18:04
 * To change this template use File | Settings | File Templates.
 */
public class adSolicitudes extends adbeans {
    private static Logger log = Logger.getLogger(adSolicitudes.class.getName());

    /**
     * Instancia la clase sin obtener una nueva conexión
     *
     * @param con
     * @throws AbadiaException
     */
    public adSolicitudes(Connection con) throws AbadiaException {
        super(con);
    }


    /**
     * Recupera un {@link ArrayList} de objetos {@link Solicitud} con las solicitudes que estan pendientes de aprobacion
     *
     * @param tipo
     * @return
     * @throws AbadiaException
     */
    public ArrayList<Solicitud> recuperarSolicitudes(short tipo) throws AbadiaException {
        String sSQL = " Select s.solicitudid, s.abadiaid, s.monjeid, s.fecha_creacion, st.votos, st.vigencia, st.defecto, st.literalid_si, st.literalid_no, u.idiomaid, st.accion, sum(if(sv.voto=0, 1, 0)) as pendiente, sum(if(sv.voto=1, 1, 0)) as si, sum(if(sv.voto=2, 1, 0)) as no, a.regionid " +
                " from solicitud s, solicitud_tipo st, solicitud_voto sv, abadia a, usuario u " +
                " where s.tiposolicitudid=st.tiposolicitudid and s.solicitudid=sv.solicitudid and s.estado=? and s.abadiaid=a.abadiaid and a.usuarioid=u.usuarioid" +
                " group by s.solicitudid, s.abadiaid, s.monjeid, s.fecha_creacion, st.votos, st.vigencia, st.defecto, st.literalid_si, st.literalid_no, u.idiomaid, st.accion  " +
                " order by u.idiomaid";

        ArrayList<Solicitud> alSolicitudes = new ArrayList<Solicitud>();
        Solicitud solicitud;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setShort(1, tipo);
            rs = ps.executeQuery();
            while (rs.next()) {
                solicitud = new Solicitud();
                solicitud.setIdSolicitud(rs.getInt("SOLICITUDID"));
                solicitud.setIdAbadia(rs.getInt("ABADIAID"));
                solicitud.setIdMonje(rs.getInt("MONJEID"));
                solicitud.setFechaCreacion(rs.getString("FECHA_CREACION"));
                solicitud.setVotosNecesarios(rs.getInt("VOTOS"));
                solicitud.setDiasVigencia(rs.getInt("VIGENCIA"));
                solicitud.setValorDefecto(rs.getShort("DEFECTO"));
                solicitud.setLiteralSi(rs.getInt("LITERALID_SI"));
                solicitud.setLiteralNo(rs.getInt("LITERALID_NO"));
                solicitud.setAccion(rs.getString("ACCION"));
                solicitud.setVotosSi(rs.getInt("SI"));
                solicitud.setVotosNo(rs.getInt("NO"));
                solicitud.setVotosPendiente(rs.getInt("PENDIENTE"));
                solicitud.setIdIdioma(rs.getInt("IDIOMAID"));
                solicitud.setTotalVotos(solicitud.getVotosNo() + solicitud.getVotosSi() + solicitud.getVotosPendiente());
                alSolicitudes.add(solicitud);
            }
            return alSolicitudes;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adSolicitudes. recuperarSolicitudes. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Actualiza el estado de una solicitud en base de datos
     *
     * @param estado
     * @param idSolicitud
     * @throws org.abbatia.exception.base.AbadiaException
     *
     */
    public void actualizarEstado(short estado, int idSolicitud) throws AbadiaException {
        adUtils utils = null;
        try {
            utils = new adUtils(con);
            utils.execSQL("update solicitud set estado = " + estado + " where solicitudid = " + idSolicitud);
        } finally {
            //if (utils != null) utils.finalize();
        }
    }

    /**
     * Da de alta una solicitud en la tabla solicitud y los registros
     * correspondientes en la tabla solicitud_voto (una por votante)
     *
     * @param solicitud
     * @throws AbadiaException
     */
    public void crearSolicitud(Solicitud solicitud) throws AbadiaException {

        String sSQL = "Insert Into solicitud ( ABADIAID , MONJEID, FECHA_CREACION, ESTADO, TIPOSOLICITUDID, TEXTO ) " +
                " Values (" + solicitud.getIdAbadia() + ", " + solicitud.getIdMonje() + ", '" + solicitud.getFechaCreacion() + "', " + solicitud.getEstado() + ", " + solicitud.getIdTipoSolicitud() + ", '" + solicitud.getTexto() + "')";

        String sSQLVotos = "Insert into solicitud_voto ( SOLICITUDID, ABADIAID, VOTO ) " +
                " Values ( ?, ?, 0) ";
        Statement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        int idSolicitud = 0;
        try {
            ps = con.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_UPDATABLE);
            ps.executeUpdate(sSQL, Statement.RETURN_GENERATED_KEYS);
            rs = ps.getGeneratedKeys();

            if (rs.next()) {
                idSolicitud = rs.getInt(1);
            } else
                throw new AbadiaSQLException("adSolicitudes. crearSolicitud. No se ha podido crear la solicitud", log);

            ps1 = con.prepareStatement(sSQLVotos);
            for (int iCount = 0; solicitud.getVotantes().length > iCount; iCount++) {
                ps1.setInt(1, idSolicitud);
                ps1.setInt(2, solicitud.getVotantes()[iCount]);
                ps1.execute();
            }

        } catch (SQLException e) {
            throw new AbadiaSQLException("adSolicitudes. crearSolicitud. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
            DBMSUtils.cerrarObjetoSQL(ps1);
        }

    }

    /**
     * Actualiza el voto de una abadia en una solicitud
     *
     * @param solicitudid
     * @param abadiaid
     * @param voto
     * @throws org.abbatia.exception.base.AbadiaException
     *
     */
    public void actualizarVoto(int solicitudid, int abadiaid, short voto) throws AbadiaException {
        adUtils utilsAD = new adUtils(con);
        utilsAD.execSQL("UPDATE solicitud_voto set voto = " + voto + " where solicitudid = " + solicitudid + " and abadiaid = " + abadiaid);
    }

    /**
     * Devuelve un {@link ArrayList} de objetos {@link Solicitud} asociados a esa abadia
     *
     * @param abadia
     * @param usuario
     * @return
     * @throws AbadiaException
     */
    public ArrayList<Solicitud> recuperarListaTerceros(Abadia abadia, Usuario usuario, short tipo) throws AbadiaException {
        String sSQL = " select s.solicitudid, s.abadiaid, s.monjeid, s.fecha_creacion, adddate(s.fecha_creacion, st.vigencia) as caduca, s.texto, s.estado, a.nombre, sv.voto, s.tiposolicitudid " +
                " from solicitud s, solicitud_tipo st, solicitud_voto sv, abadia a " +
                " where s.tiposolicitudid = st.tiposolicitudid and s.solicitudid = sv.solicitudid and sv.abadiaid = ? and s.tiposolicitudid = ? and a.abadiaid=s.abadiaid ";

        PreparedStatement ps = null;
        ResultSet rs = null;
        Solicitud solicitud;
        ArrayList<Solicitud> solicitudes = new ArrayList<Solicitud>();

        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, abadia.getIdDeAbadia());
            ps.setShort(2, tipo);
            rs = ps.executeQuery();

            while (rs.next()) {
                solicitud = new Solicitud();
                solicitud.setIdSolicitud(rs.getInt("SOLICITUDID"));
                solicitud.setIdAbadia(rs.getInt("ABADIAID"));
                solicitud.setIdMonje(rs.getInt("MONJEID"));
                solicitud.setFechaCreacion(Utilidades.formatStringFromDB(rs.getString("FECHA_CREACION")));
                solicitud.setFechaCaduca(Utilidades.formatStringFromDB(rs.getString("CADUCA")));
                solicitud.setEstado(rs.getShort("ESTADO"));
                solicitud.setTexto(rs.getString("TEXTO"));
                solicitud.setNombreAbadia(rs.getString("NOMBRE"));
                solicitud.setVoto(rs.getShort("VOTO"));
                solicitud.setIdTipoSolicitud(rs.getInt("TIPOSOLICITUDID"));
                solicitudes.add(solicitud);
            }
            return solicitudes;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adSolicitudes. recuperarListaTerceros. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /**
     * Devuelve un {@link ArrayList} de objetos {@link Solicitud} asociados a esa abadia
     *
     * @param abadia
     * @param usuario
     * @param tipo
     * @return
     * @throws AbadiaException
     */
    public ArrayList<Solicitud> recuperarListaPropias(Abadia abadia, Usuario usuario, short tipo) throws AbadiaException {
        String sSQL = " select s.solicitudid, s.abadiaid, s.monjeid, s.fecha_creacion, adddate(s.fecha_creacion, st.vigencia) as caduca, s.texto, s.estado, s.tiposolicitudid " +
                " from solicitud s, solicitud_tipo st " +
                " where s.tiposolicitudid = st.tiposolicitudid and s.abadiaid = ? and s.tiposolicitudid = ? ";

        PreparedStatement ps = null;
        ResultSet rs = null;
        Solicitud solicitud = null;
        ArrayList<Solicitud> solicitudes = new ArrayList<Solicitud>();

        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, abadia.getIdDeAbadia());
            ps.setShort(2, tipo);
            rs = ps.executeQuery();

            while (rs.next()) {
                solicitud = new Solicitud();
                solicitud.setIdSolicitud(rs.getInt("SOLICITUDID"));
                solicitud.setIdAbadia(rs.getInt("ABADIAID"));
                solicitud.setIdMonje(rs.getInt("MONJEID"));
                solicitud.setFechaCreacion(Utilidades.formatStringFromDB(rs.getString("FECHA_CREACION")));
                solicitud.setFechaCaduca(Utilidades.formatStringFromDB(rs.getString("CADUCA")));
                solicitud.setEstado(rs.getShort("ESTADO"));
                solicitud.setTexto(rs.getString("TEXTO"));
                solicitud.setIdTipoSolicitud(rs.getInt("TIPOSOLICITUDID"));
                solicitud.setNombreAbadia("");
                solicitudes.add(solicitud);
            }
            return solicitudes;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adSolicitudes. recuperarListaPropias. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /**
     * REcupera los datos de una solicitud
     *
     * @param idSolicitud
     * @return
     * @throws AbadiaException
     */
    public Solicitud recuperarSolicitud(int idSolicitud) throws AbadiaException {
        String sSQL = " Select s.solicitudid, s.abadiaid, s.monjeid, s.fecha_creacion, st.vigencia, st.defecto, st.accion, s.estado " +
                " from solicitud s, solicitud_tipo st " +
                " where s.tiposolicitudid=st.tiposolicitudid and s.solicitudid = ? ";

        Solicitud solicitud = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idSolicitud);
            rs = ps.executeQuery();
            if (rs.next()) {
                solicitud = new Solicitud();
                solicitud.setIdSolicitud(rs.getInt("SOLICITUDID"));
                solicitud.setIdAbadia(rs.getInt("ABADIAID"));
                solicitud.setIdMonje(rs.getInt("MONJEID"));
                solicitud.setFechaCreacion(rs.getString("FECHA_CREACION"));
                solicitud.setDiasVigencia(rs.getInt("VIGENCIA"));
                solicitud.setValorDefecto(rs.getShort("DEFECTO"));
                solicitud.setAccion(rs.getString("ACCION"));
                solicitud.setEstado(rs.getShort("ESTADO"));
            }
            return solicitud;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adSolicitudes. recuperarSolicitud. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /**
     * Elimina una solicitud y sus votos.
     *
     * @param abadia
     * @param solicitudid
     * @throws AbadiaException
     */
    public void cancelarSolicitud(Abadia abadia, int solicitudid) throws AbadiaException {
        adUtils utilsAD = new adUtils(con);
        //eliminamos los votos asociados a la solicitud
        utilsAD.execSQL("delete from solicitud_voto where solicitudid in (select solicitudid from solicitud where solicitudid = " + solicitudid + " and abadiaid = " + abadia.getIdDeAbadia() + ")");
        //eliminamos el registro de solicitud
        utilsAD.execSQL("delete from solicitud where solicitudid = " + solicitudid + " and abadiaid = " + abadia.getIdDeAbadia());
    }
}
