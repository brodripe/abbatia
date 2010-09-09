package org.abbatia.adbean;

import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class adPapa extends adMonje {
    private static Logger log = Logger.getLogger(adPapa.class.getName());
    public static int MONJES_TODOS = 0;
    public static int MONJES_VIVOS = 1;
    public static int MONJES_MUERTOS = 2;

    /**
     * Instancia la clase sin obtener una nueva conexión
     *
     * @param con
     * @throws AbadiaException
     */
    public adPapa(Connection con) throws AbadiaException {
        super(con);
    }


    /**
     * Devuelve un MonejeID del papa actual
     *
     * @return int devuelve el código del monjeID del papa, 0 si no existe papa
     * @throws org.abbatia.exception.base.AbadiaException
     *          Excepción base de abbatia
     */
    public int getPapaID() throws AbadiaException {

        String sSQL = "select monjeid from monje where estado <> 1 and jerarquiaid = " + Constantes.JERARQUIA_PAPA;

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            if (rs.next())
                return rs.getInt(1);
            else return 0;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adPapa. getPapaID", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Devuelve un boolean para saber si se esta celebrando el conclave
     *
     * @return int devuelve el estado si hay conclave en el mundo abbatia, sino devuelve 0
     * @throws org.abbatia.exception.base.AbadiaException
     *          Excepción base de abbatia
     */
    public int estadoConclave() throws AbadiaException {

        String sSQL = "SELECT estado " +
                " FROM `conclave` where estado <> 0 ";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            } else return 0;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adPapa. estadoConclave", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public int getFumataPapa(int estado) throws AbadiaException {

        String sSQL = "SELECT fumata " +
                " FROM `conclave` where estado = ? ";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, estado);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            } else return 0;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adPapa. estadoConclave", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Devuelve un boolean para saber si se esta celebrando el conclave
     * <p/>
     * 1 - gestión de mercado
     *
     * @param AbadiaID
     * @param MonjeID
     * @throws org.abbatia.exception.base.AbadiaException
     *          Excepción base de abbatia
     */
    public void organizarConclave(int AbadiaID, int MonjeID) throws AbadiaException {

        String sSQLCardenales = "SELECT c.* FROM cardenales c, monje m where c.monjeid=m.monjeid ";

        adUtils oUtilsAD;
        adViajar oViajarAD;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // Rellenar las tablas con los candidatos a papa
            oUtilsAD = new adUtils(con);
            oUtilsAD.execSQL("DELETE FROM `cardenales_conclave` ");
            oUtilsAD.execSQL("INSERT INTO `cardenales_conclave` ( CardenalID ) (Select CardenalID from cardenales )");
            oUtilsAD.execSQL("INSERT INTO `conclave` " +
                    "( `ESTADO`, `FECHA_COMIENZO`, `FECHA_VOTO`, `FECHA_FIN`, `VOTOS_NULOS`, `ABADIAID_PAPA_MUERTO`, `MONJEID_PAPA_MUERTO`, `ABADIAID_PAPA_NUEVO`, `MONJEID_PAPA_NUEVO`) " +
                    "VALUES (1, Now(), null, null, " + AbadiaID + ", " + MonjeID + ", 0, 0, 0) ");

            // Empezar el viaje
            ps = con.prepareStatement(sSQLCardenales);
            rs = ps.executeQuery();
            oViajarAD = new adViajar(con);
            while (rs.next()) {
                oViajarAD.prepararViaje(rs.getInt("abadiaid"), rs.getInt("monjeid"), AbadiaID, 10);    //
            }

        } catch (SQLException e) {
            throw new AbadiaSQLException("adPapa. organizarConclave", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

}
