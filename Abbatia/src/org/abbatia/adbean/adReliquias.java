package org.abbatia.adbean;

import org.abbatia.adbean.base.adbeans;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Reliquias;
import org.abbatia.bean.Usuario;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.base.AbadiaException;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class adReliquias extends adbeans {
    private static Logger log = Logger.getLogger(adReliquias.class.getName());

    /**
     * Instancia la clase sin obtener una nueva conexión
     *
     * @param con
     * @throws AbadiaException
     */
    public adReliquias(Connection con) throws AbadiaException {
        super(con);
    }


    // Recupera las reliquias de una abbatia
    public ArrayList<Reliquias> recuperarReliquias(Abadia abadia, Usuario usuario) throws AbadiaException {
        //Definición de cadena sql de consulta
        String sSQL = "Select r.*, l.LITERAL from reliquias r, reliquias_tipo rt, literales l where r.ABADIAID = ? and l.LITERALID=rt.LITERALID AND rt.RELIQUIAID=r.RELIQUIAID AND l.IDIOMAID=?";
        //creo un objeto de tipo Recurso

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //creo un objeto de tipo PreparedStatement sobre el que se
            //ejecutara la consulta
            ps = con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave.

            int parNo = 1;
            ps.setLong(parNo++, abadia.getIdDeAbadia());
            ps.setShort(parNo, usuario.getIdDeIdioma());
            //Lanzo la consulta y cargo el resultado en un resultset
            rs = ps.executeQuery();
            //si la consulta encuentra la recurso....
            ArrayList<Reliquias> listaReliquias = new ArrayList<Reliquias>();
            Reliquias reliquia = null;
            while (rs.next()) {
                //iniciamos el volcado de datos sobre
                //el objeto recurso.
                reliquia = new Reliquias();
                reliquia.setIdAbadia(abadia.getIdDeAbadia());
                reliquia.setIdReliquia(rs.getInt("RECURSOID"));
                reliquia.setNombre(rs.getString("LITERAL"));
                reliquia.setFecha_adquisicion(rs.getString("FECHA_ADQUISICION"));
                reliquia.setFecha_creacion(rs.getString("FECHA_CREACION"));
                listaReliquias.add(reliquia);
            }
            return listaReliquias;

        } catch (SQLException e) {
            //log.error("adReliquias. recuperarReliquias. ", e);
            throw new AbadiaSQLException("adReliquias. recuperarReliquias. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

}
