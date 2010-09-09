package org.abbatia.adbean;

import org.abbatia.adbean.base.adbeans;
import org.abbatia.exception.base.AbadiaException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @deprecated
 */
public class adHabilidad extends adbeans {
    /**
     * Instancia la clase sin obtener una nueva conexión
     *
     * @param con
     * @throws org.abbatia.exception.base.AbadiaException
     *
     */
    public adHabilidad(Connection con) throws AbadiaException {
        super(con);
    }

    /**
     * Incrementan o decrementan una habilidad en concreto para todos los monjes del JUEGO!!!
     *
     * @deprecated
     */
    public boolean incrementarHabilidad(int Habilidad, double valor) throws SQLException {
        String sSQL = "UPDATE monje m, `propiedad_valor` pv " +
                "SET valor = valor + (?)  " +
                "where m.monjeid = pv.claveid and pv.propiedadid = ? and pv.tipo = 'M' ";

        PreparedStatement ps = (PreparedStatement) con.prepareStatement(sSQL);
        //asigno el parametro para el filtrado de la consulta
        //esta instruccion sustituye el simbolo ? por la clave del monje
        int parNo = 1;
        ps.setDouble(parNo++, valor);
        ps.setInt(parNo++, Habilidad);
        // Ejecutarlo
        return ps.execute();
    }

    /**
     * Incrementan o decrementan una habilidad en concreto para todos los monjes de una abadia
     *
     * @deprecated
     */
    public boolean incrementarHabilidad(long idAbadia, int Habilidad, double valor) throws SQLException {
        String sSQL = "UPDATE monje m, `propiedad_valor` pv " +
                "SET valor = valor + (?)  " +
                "where m.abadiaid = ? and m.monjeid = pv.claveid and pv.propiedadid = ? and pv.tipo = 'M' ";

        PreparedStatement ps = (PreparedStatement) con.prepareStatement(sSQL);
        //asigno el parametro para el filtrado de la consulta
        //esta instruccion sustituye el simbolo ? por la clave del monje
        int parNo = 1;
        ps.setDouble(parNo++, valor);
        ps.setLong(parNo++, idAbadia);
        ps.setInt(parNo++, Habilidad);
        // Ejecutarlo
        return ps.execute();
    }

    /**
     * Incrementan o decrementan una habilidad en concreto
     *
     * @deprecated
     */
    public boolean incrementarHabilidad(long idAbadia, int idMonje, int Habilidad, double valor) throws SQLException {
        String sSQL = "UPDATE monje m, `propiedad_valor` pv " +
                "SET valor = valor + (?)  " +
                "where m.abadiaid = ? and m.monjeid = pv.claveid and m.monjeid=? and pv.propiedadid = ? and pv.tipo = 'M' ";

        PreparedStatement ps = (PreparedStatement) con.prepareStatement(sSQL);
        //asigno el parametro para el filtrado de la consulta
        //esta instruccion sustituye el simbolo ? por la clave del monje
        int parNo = 1;
        ps.setDouble(parNo++, valor);
        ps.setLong(parNo++, idAbadia);
        ps.setInt(parNo++, idMonje);
        ps.setInt(parNo++, Habilidad);
        // Ejecutarlo
        return ps.execute();
    }


}
