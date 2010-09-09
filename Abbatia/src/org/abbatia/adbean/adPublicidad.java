package org.abbatia.adbean;

import org.abbatia.adbean.base.adbeans;
import org.abbatia.bean.Publicidad;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.base.AbadiaException;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

public class adPublicidad extends adbeans {
    private static Logger log = Logger.getLogger(adPublicidad.class.getName());

    /**
     * Instancia la clase sin obtener una nueva conexión
     *
     * @param con
     * @throws org.abbatia.exception.base.AbadiaException
     *
     */
    public adPublicidad(Connection con) throws AbadiaException {
        super(con);
    }


    /* recuperar la publicidad según el tipo y lo que esté activado
    */
    public Publicidad recuperar(int ID) throws AbadiaException {
        Publicidad pub = new Publicidad();
        String sSQL = "SELECT * FROM `publicidad` where id = ?";
        String sSQLUpdate = "UPDATE `publicidad` SET stats_clicks=stats_clicks+1 where id = ?";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, ID);
            rs = ps.executeQuery();
            if (rs.next()) {
                pub.setId(rs.getInt("id"));
                pub.setImagen(rs.getString("imagen"));
                pub.setUrl(rs.getString("url"));
                pub.setHint(rs.getString("hint"));
            }
            rs.close();
            ps = con.prepareStatement(sSQLUpdate);
            ps.setInt(1, pub.getId());
            ps.execute();
            return pub;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adPublicidad. recuperar. Error SQL", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /* visualizar la publicidad según el tipo y lo que esté activado
    */
    public Publicidad visualizar(int Tipo) throws AbadiaException {
        Publicidad pub = new Publicidad();
        String sSQL = "SELECT * FROM `publicidad` where tipo = ? and activo = 1 and (pais='XX' or pais=?) order by (visualizar_total / stats_visto) desc";
        String sSQLUpdate = "UPDATE `publicidad` SET stats_visto=stats_visto+1 where id = ?";

        Locale lcl = Locale.getDefault();

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, Tipo);
            ps.setString(2, lcl.getCountry());
            rs = ps.executeQuery();
            if (rs.next()) {
                pub.setId(rs.getInt("id"));
                pub.setImagen(rs.getString("imagen"));
                pub.setUrl(rs.getString("url"));
                pub.setHint(rs.getString("hint"));
            }
            rs.close();
            ps = con.prepareStatement(sSQLUpdate);
            ps.setInt(1, pub.getId());
            ps.execute();
            return pub;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adPublicidad. visualizar. Error SQL", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

}
