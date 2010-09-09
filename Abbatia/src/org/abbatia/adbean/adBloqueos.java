package org.abbatia.adbean;

import org.abbatia.actionform.PeticionBloqueoActForm;
import org.abbatia.adbean.base.adbeans;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.PeticionBloqueo;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Utilidades;
import org.apache.log4j.Logger;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Benjamín Rodríguez.
 * User: Benjamin
 * Date: 14-mar-2005
 * Time: 0:18:04
 */
public class adBloqueos extends adbeans {
    private static Logger log = Logger.getLogger(adBloqueos.class.getName());

    /**
     * Instancia la clase sin obtener una nueva conexión
     *
     * @param con
     * @throws org.abbatia.exception.base.AbadiaException
     *
     */
    public adBloqueos(Connection con) throws AbadiaException {
        super(con);
    }


    /**
     * Recupera un {@link java.util.ArrayList} de objetos {@link org.abbatia.bean.Solicitud} con las solicitudes que estan pendientes de aprobacion
     *
     * @param p_sEstado
     * @return ArrayList
     * @throws org.abbatia.exception.base.AbadiaException
     *
     */
    public ArrayList<PeticionBloqueo> recuperarPeticionesBloqueo(short p_sEstado) throws AbadiaException {
        String sSQL = "{call recuperarPeticionesBloqueo(?)} ";

        ArrayList<PeticionBloqueo> alPeticionesBloqueo = new ArrayList<PeticionBloqueo>();
        PeticionBloqueo peticionBloqueo = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            cs = con.prepareCall(sSQL);
            cs.setShort(1, p_sEstado);
            cs.execute();
            rs = cs.getResultSet();
            while (rs.next()) {
                peticionBloqueo = new PeticionBloqueo();
                peticionBloqueo.setPeticionId(rs.getInt("PETICIONID"));
                peticionBloqueo.setNombreUsuario(rs.getString("NICK"));
                peticionBloqueo.setNombreAbadia(rs.getString("NOMBRE"));
                peticionBloqueo.setNombreRegion(rs.getString("DESCRIPCION"));
                peticionBloqueo.setFechaCreacion(Utilidades.formatStringFromDB(rs.getString("FECHA_CREACION")));
                peticionBloqueo.setDiasBloqueo(rs.getInt("DIAS_BLOQUEO"));
                peticionBloqueo.setMotivo(rs.getString("MOTIVO"));
                peticionBloqueo.setEstado(rs.getString("DESCRIPCION_ESTADO"));
                peticionBloqueo.setFechaCierre(Utilidades.formatStringFromDB(rs.getString("FECHA_CIERRE")));
                alPeticionesBloqueo.add(peticionBloqueo);
            }
            return alPeticionesBloqueo;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adBloqueos. recuperarPeticionesBloqueo. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(cs);
        }
    }

    /**
     * Actualiza el estado de una solicitud en base de datos
     *
     * @param estado
     * @param p_iPeticionId
     * @throws org.abbatia.exception.base.AbadiaException
     *
     */
    public void actualizarEstado(short estado, int p_iPeticionId) throws AbadiaException {
        adUtils utils = null;
        try {
            utils = new adUtils(con);
            utils.execSQL("update peticion_bloqueo set estado = " + estado + " where peticionid = " + p_iPeticionId);
        } finally {
            //
        }
    }

    /**
     * Da de alta una petición de bloqueo
     *
     * @param p_afPeticionBloqueo
     * @throws org.abbatia.exception.base.AbadiaException
     *
     */
    public void crearPeticionBloqueo(PeticionBloqueoActForm p_afPeticionBloqueo) throws AbadiaException {

        String sSQL = "Insert Into peticion_bloqueo ( USUARIOID , ABADIAID, DIAS_BLOQUEO, MOTIVO, FECHA_CREACION) " +
                " Values (" + p_afPeticionBloqueo.getUsuarioId() + ", " + p_afPeticionBloqueo.getAbadiaId() + ", " + p_afPeticionBloqueo.getNumDias() + ", '" + p_afPeticionBloqueo.getMotivo() + "', now())";

        String sSQLInsert;
        adUtils utilsAD = null;
        try {
            utilsAD = new adUtils(con);
            int iPeticionId = utilsAD.execInsSQL(sSQL);
            sSQLInsert = "Insert into peticion_bloqueo_voto (PETICIONID, USUARIOID, FECHA_CREACION, VOTO) " +
                    " Values (" + iPeticionId + ", " + p_afPeticionBloqueo.getUsuarioId() + ", now(), 1) ";
            utilsAD.execSQL(sSQLInsert);
        } finally {
            //
        }

    }

    /**
     * Actualiza el voto de una abadia en una solicitud
     *
     * @param p_iPeticionId
     * @param p_iUsuarioId
     * @param p_sVoto
     * @throws org.abbatia.exception.base.AbadiaException
     *
     */
    public void actualizarVoto(int p_iPeticionId, long p_iUsuarioId, short p_sVoto) throws AbadiaException {
        adUtils utilsAD = new adUtils(con);
        if (utilsAD.getSQL("Select count(*) from peticion_bloqueo_voto where peticionid = " + p_iPeticionId + " and usuarioid = " + p_iUsuarioId, 0) == 0) {
            utilsAD.execInsSQL("Insert into peticion_bloqueo_voto (PETICIONID, USUARIOID, FECHA_CREACION, VOTO) values (" + p_iPeticionId + ", " + p_iUsuarioId + ", now(), " + p_sVoto + ")");
        } else {
            utilsAD.execSQL("UPDATE peticion_bloqueo_voto set voto = " + p_sVoto + " where PETICIONID = " + p_iPeticionId + " and USUARIOID = " + p_iUsuarioId);
        }
    }

    /**
     * Elimina una solicitud y sus votos.
     *
     * @param abadia
     * @param solicitudid
     * @throws org.abbatia.exception.base.AbadiaException
     *
     */
    public void cancelarSolicitud(Abadia abadia, int solicitudid) throws AbadiaException {
        adUtils utilsAD = new adUtils(con);
        //eliminamos los votos asociados a la solicitud
        utilsAD.execSQL("delete from solicitud_voto where solicitudid in (select solicitudid from solicitud where solicitudid = " + solicitudid + " and abadiaid = " + abadia.getIdDeAbadia() + ")");
        //eliminamos el registro de solicitud
        utilsAD.execSQL("delete from solicitud where solicitudid = " + solicitudid + " and abadiaid = " + abadia.getIdDeAbadia());
    }

    /**
     * Invoca al proceure que gestiona el estado de la petición
     *
     * @param p_iPeticionId
     * @return ArrayList
     * @throws org.abbatia.exception.base.AbadiaException
     *
     */
    public void gestionarPeticionBloqueo(int p_iPeticionId) throws AbadiaException {
        String sSQL = "{call gestionPeticionBloqueo(?)} ";

        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            cs = con.prepareCall(sSQL);
            cs.setInt(1, p_iPeticionId);
            cs.execute();

        } catch (SQLException e) {
            throw new AbadiaSQLException("adBloqueos. gestionarPeticionBloqueo. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(cs);
        }
    }

}
