package org.abbatia.adbean;

import org.abbatia.adbean.base.adbeans;
import org.abbatia.bean.Enfermedad;
import org.abbatia.bean.Monje;
import org.abbatia.core.CoreTiempo;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;

public class adEnfermedad extends adbeans {
    private static Logger log = Logger.getLogger(adEnfermedad.class.getName());

    /**
     * Instancia la clase sin obtener una nueva conexión
     *
     * @param con
     * @throws org.abbatia.exception.base.AbadiaException
     *
     */
    public adEnfermedad(Connection con) throws AbadiaException {
        super(con);
    }


    /**
     * Recupera una hashtable con todas las enfermedades
     *
     * @param iNivel
     * @return
     * @throws AbadiaException
     */
    public HashMap<Integer, HashMap<Object, Enfermedad>> recuperarTablaEnfermedades(int iNivel) throws AbadiaException {
        String sSQL = "Select et.tipo_enfermedadid, et.variante, et.falta_exceso, et.propiedadid_impacto, salud, literalid, duracion, et.probabilidad, et.nivel " +
                " from enfermedad_tipo et where nivel = ? " +
                " order by falta_exceso";

        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap<Object, Enfermedad> htExceso = new HashMap<Object, Enfermedad>();
        HashMap<Object, Enfermedad> htDefecto = new HashMap<Object, Enfermedad>();
        HashMap<Integer, HashMap<Object, Enfermedad>> htEnfermedades = new HashMap<Integer, HashMap<Object, Enfermedad>>();

        Enfermedad enfermedad;

        int falta_exceso;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, iNivel);

            rs = ps.executeQuery();

            while (rs.next()) {
                falta_exceso = rs.getInt("falta_exceso");
                enfermedad = new Enfermedad();
                enfermedad.setNivel(rs.getInt("nivel"));
                enfermedad.setTipoEnfermedad(rs.getInt("tipo_enfermedadid"));
                enfermedad.setPropiedadCausa(rs.getInt("propiedadid_impacto"));
                enfermedad.setVariante(rs.getInt("variante"));
                enfermedad.setSalud(rs.getDouble("salud"));
                enfermedad.setLiteralid(rs.getInt("literalid"));
                enfermedad.setDuracion(rs.getInt("duracion"));
                enfermedad.setProbabilidad(rs.getInt("probabilidad"));
                enfermedad.setPropiedadImpacto(recuperarPropiedadImpacto(enfermedad));

                //uno significa defecto
                if (falta_exceso == Constantes.ENFERMEDAD_DEFECTO) {
                    htExceso.put(rs.getInt("propiedadid_impacto"), enfermedad);
                } else //exceso
                {
                    htDefecto.put(rs.getInt("propiedadid_impacto"), enfermedad);
                }
            }

            htEnfermedades.put(1, htExceso);
            htEnfermedades.put(2, htDefecto);

            return htEnfermedades;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adEnfermedades. recuperarTablaEnfermedades. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /**
     * Recupera una hashtable con todas las enfermedades indexadas por tipo enfermedad + nivel
     *
     * @return
     * @throws AbadiaException
     */
    public HashMap<String, Enfermedad> recuperarTablaEnfermedadesPorTipo() throws AbadiaException {
        String sSQL = "Select et.tipo_enfermedadid, et.variante, et.falta_exceso, et.propiedadid_impacto, salud, literalid, duracion, et.probabilidad, et.nivel, et.literalid_mejora, et.literalid_empeora " +
                " from enfermedad_tipo et " +
                " order by tipo_enfermedadid";

        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap<String, Enfermedad> htEnfermedades = new HashMap<String, Enfermedad>();

        Enfermedad enfermedad;

        try {
            ps = con.prepareStatement(sSQL);

            rs = ps.executeQuery();

            while (rs.next()) {

                enfermedad = new Enfermedad();
                enfermedad.setNivel(rs.getInt("nivel"));
                enfermedad.setTipoEnfermedad(rs.getInt("tipo_enfermedadid"));
                enfermedad.setPropiedadCausa(rs.getInt("propiedadid_impacto"));
                enfermedad.setVariante(rs.getInt("variante"));
                enfermedad.setSalud(rs.getDouble("salud"));
                enfermedad.setLiteralid(rs.getInt("literalid"));
                enfermedad.setDuracion(rs.getInt("duracion"));
                enfermedad.setProbabilidad(rs.getInt("probabilidad"));
                enfermedad.setFaltaExceso(rs.getShort("falta_exceso"));
                enfermedad.setLiteralidMejora(rs.getInt("literalid_mejora"));
                enfermedad.setLiteralidEmpeora(rs.getInt("literalid_empeora"));
                //informamos las propiedades impactadas por la enfermedad
                enfermedad.setPropiedadImpacto(recuperarPropiedadImpacto(enfermedad));

                //la clave de la hash es la concatenacion del tipo de enfermedad y el nivel.
                htEnfermedades.put(String.valueOf(enfermedad.getTipoEnfermedad()).concat(String.valueOf(enfermedad.getNivel())), enfermedad);
            }

            return htEnfermedades;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adEnfermedades. recuperarTablaEnfermedadesPorTipo. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /**
     * Inserta en la tabla monje_enfermedad el registro que indica que el monje ha enfermado
     *
     * @param idMonje
     * @param enfermedad
     * @throws AbadiaException
     */
    public void crearRegistroEnfermedad(int idMonje, Enfermedad enfermedad) throws AbadiaException {
        String sSQL = "insert into monje_enfermedad (monjeid, tipo_enfermedadid, nivel, variante, fecha_inicio, fecha_fin) " +
                " values (" + idMonje + ", " + enfermedad.getTipoEnfermedad() + ", " + enfermedad.getNivel() + ", " +
                enfermedad.getVariante() + ", '" + CoreTiempo.getTiempoAbadiaString() + "', '" + CoreTiempo.getDiferenciaString(enfermedad.getDuracion()) + "' )";

        adUtils utils = null;

        try {
            utils = new adUtils(con);
            utils.execSQL(sSQL);
        } catch (AbadiaException e) {
            throw e;
        }

    }

    /**
     * Recupera las propiedades impactadas por la enfermedad
     *
     * @param enfermedad
     * @return
     * @throws AbadiaException
     */
    public HashMap<Integer, Integer> recuperarPropiedadImpacto(Enfermedad enfermedad) throws AbadiaException {
        String sSQL = "Select propiedadid, porcentaje_resta " +
                " from enfermedad_propiedad " +
                " where tipo_enfermedadid = ? and nivel = ? and variante = ? ";
        HashMap<Integer, Integer> propiedad_enfermedad = new HashMap<Integer, Integer>();

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, enfermedad.getTipoEnfermedad());
            ps.setInt(2, enfermedad.getNivel());
            ps.setInt(3, enfermedad.getVariante());

            rs = ps.executeQuery();

            while (rs.next()) {
                propiedad_enfermedad.put(rs.getInt("propiedadid"), rs.getInt("porcentaje_resta"));
            }

            return propiedad_enfermedad;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adEnfermedades. recupearPropiedadImpacto. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Actualiza las habilidades del monje impactadas por la enfermedad
     *
     * @param idMonje
     * @param htImpacto
     * @throws AbadiaException
     */
    public void aplicarImpactoEnfermedadPropiedad(int idMonje, HashMap htImpacto) throws AbadiaException {
        Iterator eImpacto = htImpacto.keySet().iterator();

        String sSQL;
        adUtils utils;
        Integer iPropiedad;
        int idPorcentaje;
        try {
            utils = new adUtils(con);
            while (eImpacto.hasNext()) {
                iPropiedad = (Integer) eImpacto.next();
                idPorcentaje = (Integer) htImpacto.get(iPropiedad);
                sSQL = "update habilidad_monje set valor_actual = valor_actual - (valor_actual * " + idPorcentaje + ") / 100 where monjeid = " + idMonje + " and habilidadid = " + iPropiedad;
                utils.execSQL(sSQL);
            }

        } catch (AbadiaException e) {
            throw new AbadiaSQLException("adEnfermedad. aplicarImpactoEnfermedadPropiedad", e, log);
        }
    }

    /**
     * Restablece las habilidades del monje impactadas por la enfermedad
     *
     * @param idMonje
     * @param htImpacto
     * @throws AbadiaException
     */
    public void restablecerImpactoEnfermedadPropiedad(int idMonje, HashMap htImpacto) throws AbadiaException {
        Iterator eImpacto = htImpacto.keySet().iterator();
        String sSQL;
        adUtils utils = null;
        Integer iPropiedad;
        int idPorcentaje;
        try {
            utils = new adUtils(con);
            while (eImpacto.hasNext()) {
                iPropiedad = (Integer) eImpacto.next();
                idPorcentaje = (Integer) htImpacto.get(iPropiedad);

                //iAntes = utils.getSQL("Select valor_actual from habilidad_monje where monjeid = " + idMonje + " and habilidadid = " + iPropiedad.intValue() , 0);

                sSQL = "update habilidad_monje set valor_actual = valor_actual + (valor_actual * " + idPorcentaje + ") / 100 where monjeid = " + idMonje + " and habilidadid = " + iPropiedad;
                utils.execSQL(sSQL);

                //iDespues = utils.getSQL("Select valor_actual from habilidad_monje where monjeid = " + idMonje + " and habilidadid = " + iPropiedad.intValue() , 0);
            }

        } catch (AbadiaException e) {
            throw new AbadiaSQLException("adEnfermedad. restablecerImpactoEnfermedadPropiedad", e, log);
        }
    }

    /**
     * Recupera una cadena html con la imagen correspondiente al nivel de la enfermedad
     *
     * @param idMonje
     * @return
     * @throws AbadiaException
     */
    public String getHTMLEnfermedadPorMonje(int idMonje) throws AbadiaException {
        String sSQL = "select me.nivel, me.tipo_enfermedadid, l.literal " +
                " from monje_enfermedad me, monje m, abadia a, usuario u, enfermedad_tipo et, literales l " +
                " where me.monjeid = ? and me.monjeid = m.monjeid and m.abadiaid = a.abadiaid and a.usuarioid = u.usuarioid and u.idiomaid = l.idiomaid " +
                " and l.literalid = et.literalid and me.tipo_enfermedadid = et.tipo_enfermedadid and me.nivel = et.nivel and me.variante = et.variante ";

        PreparedStatement ps = null;
        ResultSet rs = null;
        String sRetorno = "";

        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idMonje);
            rs = ps.executeQuery();
            if (rs.next()) {
                sRetorno = "<img src='images/iconos/16/enfermo_" + rs.getInt("nivel") + ".gif' border='0' alt='" + rs.getString("literal") + "'>";
            }
            return sRetorno;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adEnfermedades. getHTMLEnfermedadPorMonje. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Recupera el nivel de enfermedad que tiene un monje y 0 si no tiene ninguna
     *
     * @param idMonje
     * @return
     * @throws AbadiaException
     */
    public int recuperarNivelEnfermedad(int idMonje) throws AbadiaException {
        String sSQL = "select nivel from monje_enfermedad where monjeid = " + idMonje;
        adUtils utils = null;
        int nivel = 0;

        try {
            utils = new adUtils(con);
            nivel = utils.getSQL(sSQL, nivel);

            return nivel;
        } catch (AbadiaException e) {
            throw e;
        }
    }

    public void actualizarEnfermedad(Monje monje, Enfermedad enfermedad) throws AbadiaException {
        String sSQL = "update monje_enfermedad set nivel = ?, fecha_inicio = ?, fecha_fin = ? where monjeid = ?";

        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, enfermedad.getNivel());
            ps.setString(2, CoreTiempo.getTiempoAbadiaString());
            ps.setString(3, CoreTiempo.getDiferenciaString(enfermedad.getDuracion()));
            ps.setInt(4, monje.getIdMonje());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adEnfermedades. actualizarEnfermedad. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public void eliminarEnfermedad(int idMonje) throws AbadiaException {
        String sSQL = "delete from monje_enfermedad where monjeid = " + idMonje;
        adUtils utils = null;
        try {
            utils = new adUtils(con);
            utils.execSQL(sSQL);
        } catch (AbadiaException e) {
            throw e;
        }
    }

    public void actualizarDatosEnfermedad(Monje monje, Enfermedad enfermedad) throws AbadiaException {
        String sSQL = "update monje_enfermedad set nivel = ?, fecha_inicio = ?, fecha_fin = ? where monjeid = ?";

        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, enfermedad.getNivel());
            ps.setString(2, enfermedad.getFechaInicio());
            ps.setString(3, enfermedad.getFechaFin());
            ps.setInt(4, monje.getIdMonje());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adEnfermedades. actualizarDatosEnfermedad. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }


}
