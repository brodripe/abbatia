package org.abbatia.adbean;

import org.abbatia.adbean.base.adbeans;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.base.AbadiaException;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class adSalarios extends adbeans {
    private static Logger log = Logger.getLogger(adSalarios.class.getName());

    /**
     * Instancia la clase sin obtener una nueva conexión
     *
     * @param con
     * @throws AbadiaException
     */
    public adSalarios(Connection con) throws AbadiaException {
        super(con);
    }


    /**
     * Recuperar Salario a partir de salarioid
     *
     * @param idSalario Identificador de salario
     * @return double monedas del salario
     * @throws org.abbatia.exception.AbadiaSQLException
     *          Excepción base de abbatia
     */
    public double recuperarSalario(int idSalario) throws AbadiaException {
        String sSQL = "select monedas from salario where SALARIOID = ? ";
        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno los valores
            ps.setLong(1, idSalario);
            // Ejecutarlo
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("monedas");
            }
            return 0;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adHabilidades. recuperarSalario. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

}
