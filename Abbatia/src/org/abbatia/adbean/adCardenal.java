package org.abbatia.adbean;

import org.abbatia.bean.Abadia;
import org.abbatia.bean.Monje;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.base.AbadiaException;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class adCardenal extends adMonje {
    private static Logger log = Logger.getLogger(adCardenal.class.getName());
    public static int MONJES_TODOS = 0;
    public static int MONJES_VIVOS = 1;
    public static int MONJES_MUERTOS = 2;

    /**
     * Instancia la clase sin obtener una nueva conexión
     *
     * @param con
     * @throws org.abbatia.exception.base.AbadiaException
     *
     */
    public adCardenal(Connection con) throws AbadiaException {
        super(con);
    }


    /**
     * Devuelve un int con el identificador de función que corresponde
     * al cardenal que exista en la abadia recibida por parámetro
     *
     * @param abadia objeto Abadia a evaluar
     * @return int devuelve el identificador de función del cardenal
     *         1 - gestión de mercado
     * @throws AbadiaException Excepción base de abbatia
     */
    public int getFuncionCardenal(Abadia abadia) throws AbadiaException {

        String sSQL = "Select FUNCION from cardenales WHERE ABADIAID = ?";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, abadia.getIdDeAbadia());
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("FUNCION");
            } else return 0;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adCardenal. getFuncionCardenal", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Devuelve un int con el identificador de función que corresponde
     * al cardenal con el identificador de monje recibida
     *
     * @param monje objeto Abadia a evaluar
     * @return int devuelve el identificador de función del cardenal
     *         1 - gestión de mercado
     * @throws AbadiaException Excepción base de abbatia
     */
    public int getFuncionCardenal(Monje monje) throws AbadiaException {

        String sSQL = "Select FUNCION from cardenales WHERE ABADIAID = ? and MONJEID = ?";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, monje.getIdAbadia());
            ps.setInt(2, monje.getIdMonje());
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("FUNCION");
            } else return 0;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adCardenal. getFuncionCardenal", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * REsetea los datos del cardenalato asociados a una abadia en concreto.
     *
     * @param idAbadia
     * @throws AbadiaException
     */
    public void resetCardenalAbadia(int idAbadia) throws AbadiaException {
        String sSQL = "update cardenales set monjeid = -1, abadiaid = -1 where abadiaid = " + idAbadia;
        adUtils utils = new adUtils(con);
        utils.execSQL(sSQL);
    }

}
