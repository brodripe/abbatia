package org.abbatia.adbean;

import org.abbatia.adbean.base.adbeans;
import org.abbatia.bean.Especializacion;
import org.abbatia.exception.base.AbadiaException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class adEspecializacion extends adbeans {
    /**
     * Instancia la clase sin obtener una nueva conexión
     *
     * @param con
     * @throws org.abbatia.exception.base.AbadiaException
     *
     */
    public adEspecializacion(Connection con) throws AbadiaException {
        super(con);
    }

    //recupera el objeto Especializacion cargado...
    public Especializacion recuperarEspecializacion(int idDeEspecializacionTmp) throws SQLException {
        //Definición de cadena sql de consulta
        String sSQL = "Select * from especializacion where ESPECIALIZACIONID = ?";
        //creo un objeto de tipo Especializacion
        Especializacion especializacion = new Especializacion();
        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = (PreparedStatement) con.prepareStatement(sSQL);
        //asigno el parametro para el filtrado de la consulta
        //esta instruccion sustituye el simbolo ? por la clave.

        int parNo = 0;
        ps.setInt(parNo++, idDeEspecializacionTmp);
        //Lanzo la consulta y cargo el resultado en un resultset
        ResultSet rs = (ResultSet) ps.executeQuery();
        //si la consulta encuentra la especializacion....
        if (rs.next()) {
            //iniciamos el volcado de datos sobre
            //el objeto especializacion.
            especializacion.setIdDeEspecializacion(rs.getInt("ESPECIALIZACIONID"));
            especializacion.setDescripcion(rs.getString("DESCRIPCION"));

            //devolvemos el objeto Especializacion informado.
            return especializacion;
        }
        //si especializacion no se localiza, devolveremos null
        return null;
    }

    //dará de alta un objeto Especializacion en la base de datos
    public void crearEspecializacion(Especializacion especializacion) throws SQLException {
        String sSQL = "Insert Into `especializacion` ( `ESPECIALIZACIONID`,`DESCRIPCION`) Values ('?','?');";

        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = (PreparedStatement) con.prepareStatement(sSQL);
        //asigno los valores
        int parNo = 0;
        ps.setLong(parNo++, especializacion.getIdDeEspecializacion());
        ps.setString(parNo++, especializacion.getDescripcion());
        // Ejecutarlo
        ps.execute();
    }


    //elimina un objeto Especializacion de la base de datos
    //a partir de un objeto Especializacion devuelve verdadero si no ha ocurrido un error
    public boolean eliminarEspecializacion(Especializacion especializacion) throws SQLException {
        return eliminarEspecializacion(especializacion.getIdDeEspecializacion());
    }

    //Elimina un objeto Especializacion de la base de datos
    //a partir de la clave del Especializacion
    public boolean eliminarEspecializacion(long idDeEspecializacionTmp) throws SQLException {
        String sSQL = "Delete From especializacion Where `ESPECIALIZACIONID` = ?";

        PreparedStatement ps = (PreparedStatement) con.prepareStatement(sSQL);
        //asigno el parametro para el filtrado de la consulta
        //esta instruccion sustituye el simbolo ? por la clave del monje
        int parNo = 0;
        ps.setLong(parNo++, idDeEspecializacionTmp);
        // Ejecutarlo
        return ps.execute();
    }
}
