package org.abbatia.adbean;

import org.abbatia.adbean.base.adbeans;
import org.abbatia.bean.OrdenEclesiastica;
import org.abbatia.exception.base.AbadiaException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class adOrdenEclesiastica extends adbeans {
    /**
     * Instancia la clase sin obtener una nueva conexión
     *
     * @param con
     * @throws org.abbatia.exception.base.AbadiaException
     *
     */
    public adOrdenEclesiastica(Connection con) throws AbadiaException {
        super(con);
    }

    //recupera el objeto OrdenEclesiastica cargado...
    public OrdenEclesiastica recuperarOrdenEclesiastica(int idDeOrdenTmp) throws SQLException {
        //Definición de cadena sql de consulta
        String sSQL = "Select * from orden_eclesiastica where ORDENID = ?";
        //creo un objeto de tipo OrdenEclesiastica
        OrdenEclesiastica ordenEclesiastica = new OrdenEclesiastica();
        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = (PreparedStatement) con.prepareStatement(sSQL);
        //asigno el parametro para el filtrado de la consulta
        //esta instruccion sustituye el simbolo ? por la clave.

        int parNo = 0;
        ps.setInt(parNo++, idDeOrdenTmp);
        //Lanzo la consulta y cargo el resultado en un resultset
        ResultSet rs = (ResultSet) ps.executeQuery();
        //si la consulta encuentra la ordenEclesiastica....
        if (rs.next()) {
            //iniciamos el volcado de datos sobre
            //el objeto ordenEclesiastica.
            ordenEclesiastica.setIdDeOrden(rs.getInt("ORDENID"));
            ordenEclesiastica.setDescripcion(rs.getString("DESCRIPCION"));

            //devolvemos el objeto OrdenEclesiastica informado.
            return ordenEclesiastica;
        }
        //si ordenEclesiastica no se localiza, devolveremos null
        return null;
    }

    //dará de alta un objeto OrdenEclesiastica en la base de datos
    public void crearOrdenEclesiastica(OrdenEclesiastica ordenEclesiastica) throws SQLException {
        String sSQL = "Insert Into `orden_eclesiastica` ( `ORDENID`,`DESCRIPCION`) Values ('?','?');";

        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = (PreparedStatement) con.prepareStatement(sSQL);
        //asigno los valores
        int parNo = 0;
        ps.setInt(parNo++, ordenEclesiastica.getIdDeOrden());
        ps.setString(parNo++, ordenEclesiastica.getDescripcion());
        // Ejecutarlo
        ps.execute();
    }


    //elimina un objeto OrdenEclesiastica de la base de datos
    //a partir de un objeto OrdenEclesiastica devuelve verdadero si no ha ocurrido un error
    public boolean eliminarOrdenEclesiastica(OrdenEclesiastica ordenEclesiastica) throws SQLException {
        return eliminarOrdenEclesiastica(ordenEclesiastica.getIdDeOrden());
    }

    //Elimina un objeto OrdenEclesiastica de la base de datos
    //a partir de la clave del OrdenEclesiastica
    public boolean eliminarOrdenEclesiastica(int idDeOrdenTmp) throws SQLException {
        String sSQL = "Delete From orden_eclesiastica Where `ORDENID` = ?";

        PreparedStatement ps = (PreparedStatement) con.prepareStatement(sSQL);
        //asigno el parametro para el filtrado de la consulta
        //esta instruccion sustituye el simbolo ? por la clave del monje
        int parNo = 0;
        ps.setInt(parNo++, idDeOrdenTmp);
        // Ejecutarlo
        return ps.execute();
    }
}
