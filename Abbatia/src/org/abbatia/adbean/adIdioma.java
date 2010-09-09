package org.abbatia.adbean;

import org.abbatia.adbean.base.adbeans;
import org.abbatia.bean.Idioma;
import org.abbatia.exception.base.AbadiaException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class adIdioma extends adbeans {
    /**
     * Instancia la clase sin obtener una nueva conexión
     *
     * @param con
     * @throws org.abbatia.exception.base.AbadiaException
     *
     */
    public adIdioma(Connection con) throws AbadiaException {
        super(con);
    }

    //recupera el objeto Idioma cargado...
    public Idioma recuperarIdioma(int idDeIdiomaTmp) throws SQLException {
        //Definición de cadena sql de consulta
        String sSQL = "Select * from idioma where IDIOMAID = ?";
        //creo un objeto de tipo Idioma
        Idioma idioma = new Idioma();
        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = con.prepareStatement(sSQL);
        //asigno el parametro para el filtrado de la consulta
        //esta instruccion sustituye el simbolo ? por la clave.

        int parNo = 1;
        ps.setInt(parNo, idDeIdiomaTmp);
        //Lanzo la consulta y cargo el resultado en un resultset
        ResultSet rs = ps.executeQuery();
        //si la consulta encuentra la idioma....
        if (rs.next()) {
            //iniciamos el volcado de datos sobre
            //el objeto idioma.
            idioma.setIdDeIdioma(rs.getInt("IDIOMAID"));
            idioma.setDescripcion(rs.getString("DESCRIPCION"));

            //devolvemos el objeto Idioma informado.
            return idioma;
        }
        //si idioma no se localiza, devolveremos null
        return null;
    }

    //dará de alta un objeto Idioma en la base de datos
    public void crearIdioma(Idioma idioma) throws SQLException {
        String sSQL = "Insert Into `idioma` ( `IDIOMAID`,`DESCRIPCION`) Values ('?','?');";

        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = con.prepareStatement(sSQL);
        //asigno los valores
        int parNo = 1;
        ps.setInt(parNo++, idioma.getIdDeIdioma());
        ps.setString(parNo, idioma.getDescripcion());
        // Ejecutarlo
        ps.execute();
    }


    //elimina un objeto Idioma de la base de datos
    //a partir de un objeto Idioma devuelve verdadero si no ha ocurrido un error
    public boolean eliminarIdioma(Idioma idioma) throws SQLException {
        return eliminarIdioma(idioma.getIdDeIdioma());
    }

    //Elimina un objeto Idioma de la base de datos
    //a partir de la clave del Idioma
    public boolean eliminarIdioma(int idDeIdiomaTmp) throws SQLException {
        String sSQL = "Delete From idioma Where `IDIOMAID` = ?";

        PreparedStatement ps = con.prepareStatement(sSQL);
        //asigno el parametro para el filtrado de la consulta
        //esta instruccion sustituye el simbolo ? por la clave del monje
        int parNo = 1;
        ps.setInt(parNo, idDeIdiomaTmp);
        // Ejecutarlo
        return ps.execute();
    }
}
