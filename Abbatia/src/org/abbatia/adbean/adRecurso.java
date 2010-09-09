package org.abbatia.adbean;

import org.abbatia.adbean.base.adbeans;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Edificio;
import org.abbatia.bean.Recurso;
import org.abbatia.bean.Usuario;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.HTML;
import org.abbatia.utils.Utilidades;
import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class adRecurso extends adbeans {
    private static Logger log = Logger.getLogger(adRecurso.class.getName());

    /**
     * Instancia la clase sin obtener una nueva conexión
     *
     * @param con
     * @throws AbadiaException
     */
    public adRecurso(Connection con) throws AbadiaException {
        super(con);
    }


    /**
     * REcupera el valor de un recurso dado un idrecurso y una abadía
     *
     * @param idRecurso
     * @param idAbadia
     * @return
     * @throws AbadiaException
     */
    public double recuperarValorRecurso(int idRecurso, int idAbadia) throws AbadiaException {
        String sSQL = "Select cantidad from recurso where recursoid = " + idRecurso + " and abadiaid = " + idAbadia;
        adUtils utils = new adUtils(con);
        return utils.getSQL(sSQL, (double) 0);
    }

    //recupera el objeto Recurso cargado...

    public Recurso recuperarRecurso(int idDeRecursoTmp, long idDeAbadiaTmp) throws AbadiaException {
        //Definición de cadena sql de consulta
        String sSQL = "Select r.recursoid, r.abadiaid, r.cantidad, rt.descripcion " +
                " from recurso r, recurso_tipo rt " +
                " where r.recursoid = rt.recursoid and r.RECURSOID = ? and r.ABADIAID = ?";
        //creo un objeto de tipo Recurso
        Recurso recurso = new Recurso();
        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave.
            ps = con.prepareStatement(sSQL);
            int parNo = 1;
            ps.setInt(parNo++, idDeRecursoTmp);
            ps.setLong(parNo, idDeAbadiaTmp);
            //Lanzo la consulta y cargo el resultado en un resultset
            rs = ps.executeQuery();
            //si la consulta encuentra la recurso....
            if (rs.next()) {
                //iniciamos el volcado de datos sobre
                //el objeto recurso.
                recurso.setRecursoID(rs.getInt("RECURSOID"));
                recurso.setAbadiaID(rs.getInt("ABADIAID"));
                recurso.setCantidad(rs.getDouble("CANTIDAD"));
                recurso.setDescripcion(rs.getString("DESCRIPCION"));

                //devolvemos el objeto Recurso informado.
                return recurso;
            }
            return null;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adRecurso. recuperarRecurso. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public ArrayList<Recurso> recuperarRecursos(Edificio edificio, Usuario usuario, MessageResources resource) throws AbadiaException {
        //Definición de cadena sql de consulta
        String sSQL = "Select r.RECURSOID, l.LITERAL, r.CANTIDAD, r.ESTADO, rt.VOLUMEN " +
                "from recurso r, recurso_tipo rt, literales l " +
                "where rt.EDIFICIOID_ALMACEN = ? and l.LITERALID=rt.LITERALID AND r.abadiaid=? AND rt.RECURSOID=r.RECURSOID AND l.IDIOMAID=? and r.CANTIDAD>0";
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
            ps.setLong(parNo++, edificio.getIdDeTipoDeEdificio());
            ps.setLong(parNo++, edificio.getIdDeAbadia());
            ps.setShort(parNo, usuario.getIdDeIdioma());
            //Lanzo la consulta y cargo el resultado en un resultset
            rs = ps.executeQuery();
            //si la consulta encuentra la recurso....
            ArrayList<Recurso> listaRecursos = new ArrayList<Recurso>();
            Recurso recurso;
            while (rs.next()) {
                //iniciamos el volcado de datos sobre
                //el objeto recurso.
                recurso = new Recurso();
                recurso.setAbadiaID(edificio.getIdDeAbadia());
                recurso.setRecursoID(rs.getInt("RECURSOID"));
                recurso.setDescripcion(rs.getString("LITERAL"));
                recurso.setCantidad(rs.getDouble("CANTIDAD"));
                recurso.setVolumen(recurso.getCantidad() * rs.getDouble("VOLUMEN"));
                recurso.setEstado(rs.getInt("ESTADO"));
                recurso.setCantidadF(Utilidades.redondear(recurso.getCantidad()));
                actualizarBarrasHTML(recurso, resource);
                listaRecursos.add(recurso);
            }
            return listaRecursos;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adRecurso. recuperarRecursos. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }


    public HashMap<Integer, Double> recuperarRecursos(long idDeAbadia) throws AbadiaException {
        //Definición de cadena sql de consulta
        String sSQL = "Select * from recurso where ABADIAID = ?";
        //creo un objeto de tipo Recurso
        Recurso recurso = new Recurso();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //creo un objeto de tipo PreparedStatement sobre el que se
            //ejecutara la consulta
            ps = con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave.

            int parNo = 1;
            ps.setLong(parNo, idDeAbadia);
            //Lanzo la consulta y cargo el resultado en un resultset
            rs = ps.executeQuery();
            HashMap<Integer, Double> recursos = new HashMap<Integer, Double>();
            //si la consulta encuentra la recurso....
            while (rs.next()) {
                //iniciamos el volcado de datos sobre
                //el objeto recurso.
                recurso.setRecursoID(rs.getInt("RECURSOID"));
                recurso.setAbadiaID(rs.getInt("ABADIAID"));
                recurso.setCantidad(rs.getFloat("CANTIDAD"));
                recursos.put(rs.getInt("RECURSOID"), rs.getDouble("CANTIDAD"));
            }
            return recursos;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adRecurso. recuperarRecursos. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    //recupera el objeto Recurso cargado...

    public boolean existeRecurso(int idDeRecursoTmp, long idDeAbadiaTmp) throws AbadiaException {
        //Definición de cadena sql de consulta
        String sSQL = "Select r.recursoid from recurso r where r.RECURSOID = ? and r.ABADIAID = ?";
        //creo un objeto de tipo Recurso
        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave.
            ps = con.prepareStatement(sSQL);
            int parNo = 1;
            ps.setInt(parNo++, idDeRecursoTmp);
            ps.setLong(parNo, idDeAbadiaTmp);
            //Lanzo la consulta y cargo el resultado en un resultset
            rs = ps.executeQuery();
            //si la consulta encuentra la recurso....
            return rs.next();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adRecurso. existeRecurso. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    //recupera el objeto Recurso cargado...

    public double recuperarCantidadRecurso(int idRecurso, int idAbadia) throws AbadiaException {
        //Definición de cadena sql de consulta
        String sSQL = "Select r.cantidad from recurso r where r.RECURSOID = ? and r.ABADIAID = ?";

        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave.
            ps = con.prepareStatement(sSQL);
            int parNo = 1;
            ps.setInt(parNo++, idRecurso);
            ps.setInt(parNo, idAbadia);
            //Lanzo la consulta y cargo el resultado en un resultset
            rs = ps.executeQuery();
            //si la consulta encuentra la recurso....
            if (rs.next()) {
                return rs.getDouble("cantidad");
            } else return 0;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adRecurso. recuperarCantidadRecurso. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    // Crear los recursos iniciales de una abbatia

    public void crearRecursosIniciales(long abadiaID) throws AbadiaException {
        //adRecurso recursoAD = new adRecurso(con);
        crearRecurso(0, abadiaID, 10000);    // Monedas
        crearRecurso(1, abadiaID, 1000);    // agua
        crearRecurso(2, abadiaID, 1000);    // Madera
        crearRecurso(3, abadiaID, 1000);    // Piedra
        crearRecurso(4, abadiaID, 1000);    // Hierro
        // Guardia
        crearRecurso(10, abadiaID, 2);
        // Aldeanos
        crearRecurso(20, abadiaID, 52);
        // Subastas  ( dinero que debes )
        crearRecurso(1000, abadiaID, 0);
        //recursoAD.finalize();
    }

    //dará de alta un objeto Recurso en la base de datos

    public void crearRecurso(Recurso recurso) throws AbadiaException {
        String sSQL = "Insert Into `recurso` ( `RECURSOID`,`ABADIAID`, `CANTIDAD`) Values (?,?,?);";

        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno los valores
            int parNo = 1;
            ps.setInt(parNo++, recurso.getRecursoID());
            ps.setLong(parNo++, recurso.getAbadiaID());
            ps.setDouble(parNo, recurso.getCantidad());
            // Ejecutarlo
            ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adRecurso. crearRecurso. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public void crearRecurso(int idDeRecursoTmp, long idDeAbadiaTmp, float cantidad) throws AbadiaException {
        Recurso recurso = new Recurso();

        recurso.setRecursoID(idDeRecursoTmp);
        recurso.setAbadiaID(idDeAbadiaTmp);
        recurso.setCantidad(cantidad);
        if (!existeRecurso(idDeRecursoTmp, idDeAbadiaTmp)) {
            crearRecurso(recurso);
        } else {
            modificarRecurso(idDeRecursoTmp, idDeAbadiaTmp, cantidad);
        }
    }

    // Modificar el recurso

    public void modificarRecurso(int idDeRecursoTmp, long idDeAbadiaTmp, double cantidad) throws AbadiaException {
        String sSQL = "Update `recurso` Set CANTIDAD = ? Where RecursoID = ? AND AbadiaID = ? ;";

        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno los valores
            int parNo = 1;
            ps.setDouble(parNo++, cantidad);
            ps.setInt(parNo++, idDeRecursoTmp);
            ps.setLong(parNo, idDeAbadiaTmp);
            // Ejecutarlo
            ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adRecurso. modificarRecurso. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }


    }

    // Sumar el recurso

    public void sumarRecurso(int idDeRecursoTmp, long idDeAbadiaTmp, double cantidad) throws AbadiaException {
        String sSQL = "Update `recurso` Set CANTIDAD = CANTIDAD + (?) Where RecursoID = ? AND AbadiaID = ? ;";

        if (!existeRecurso(idDeRecursoTmp, idDeAbadiaTmp)) {
            crearRecurso(idDeRecursoTmp, idDeAbadiaTmp, 0);
        }
        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno los valores
            int parNo = 1;
            ps.setDouble(parNo++, cantidad);
            ps.setInt(parNo++, idDeRecursoTmp);
            ps.setLong(parNo, idDeAbadiaTmp);
            // Ejecutarlo
            ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adRecurso. sumarRecurso. SQLException ", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    // Restar el recurso

    public void restarRecurso(int idDeRecursoTmp, long idDeAbadiaTmp, double cantidad) throws AbadiaException {
        String sSQL = "Update `recurso` Set CANTIDAD = CANTIDAD - ? Where RecursoID = ? AND AbadiaID = ? ;";

        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno los valores
            int parNo = 1;
            ps.setDouble(parNo++, cantidad);
            ps.setInt(parNo++, idDeRecursoTmp);
            ps.setLong(parNo, idDeAbadiaTmp);
            // Ejecutarlo
            ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adRecurso. sumarRecurso. SQLException ", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    //elimina un objeto Recurso de la base de datos
    //a partir de un objeto Recurso devuelve verdadero si no ha ocurrido un error

    public boolean eliminarRecurso(Recurso recurso) throws AbadiaSQLException {
        return eliminarRecurso(recurso.getRecursoID(), recurso.getAbadiaID());
    }

    //Elimina un objeto Recurso de la base de datos
    //a partir de la clave del Recurso

    public boolean eliminarRecurso(int idDeRecursoTmp, long idDeAbadiaTmp) throws AbadiaSQLException {
        String sSQL = "Delete From recurso Where `RECURSOID` = ? AND `ABADIAID` = ?";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave del monje
            int parNo = 1;
            ps.setInt(parNo++, idDeRecursoTmp);
            ps.setLong(parNo, idDeAbadiaTmp);
            // Ejecutarlo
            return ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adRecurso. eliminarRecurso. SQLException ", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public boolean eliminarRecursosAbadia(long idDeAbadiaTmp) throws AbadiaSQLException {
        String sSQL = "Delete From recurso Where `ABADIAID` = ?";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            int parNo = 1;
            ps.setLong(parNo, idDeAbadiaTmp);
            // Ejecutarlo
            return ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adRecurso. eliminarRecursosAbadia. SQLException ", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }


    public ArrayList<Recurso> recuperarSemillas(Abadia abadia, Usuario usuario) throws AbadiaException {
        //Definición de cadena sql de consulta
        String sSQL = "Select r.RECURSOID, l.LITERAL, r.CANTIDAD " +
                " from recurso r, recurso_tipo rt, literales l " +
                " where r.ABADIAID = ? and r.RECURSOID in (select distinct recursoid_siembra from campo_alimento) and l.LITERALID=rt.LITERALID AND rt.RECURSOID=r.RECURSOID AND l.IDIOMAID=? and r.CANTIDAD>0 ";
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
            ArrayList<Recurso> listaRecursos = new ArrayList<Recurso>();
            Recurso recurso;
            while (rs.next()) {
                //iniciamos el volcado de datos sobre
                //el objeto recurso.
                recurso = new Recurso();
                recurso.setAbadiaID(abadia.getIdDeAbadia());
                recurso.setRecursoID(rs.getInt("RECURSOID"));
                recurso.setDescripcion(rs.getString("LITERAL"));
                recurso.setCantidad(rs.getDouble("CANTIDAD"));
                listaRecursos.add(recurso);
            }
            return listaRecursos;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adRecurso. recuperarSemillas. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public double recuperarVolumenRecurso(int idDeRecursoTmp) throws AbadiaException {
        //Definición de cadena sql de consulta
        String sSQL = "Select VOLUMEN from recurso_tipo rt where rt.recursoid = ?";
        //creo un objeto de tipo Recurso
        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave.
            ps = con.prepareStatement(sSQL);
            int parNo = 1;
            ps.setInt(parNo, idDeRecursoTmp);
            //Lanzo la consulta y cargo el resultado en un resultset
            rs = ps.executeQuery();
            //si la consulta encuentra la recurso....
            if (rs.next()) {
                //iniciamos el volcado de datos sobre
                //el objeto recurso.
                return rs.getDouble("VOLUMEN");
            } else return 0;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adRecurso. recuperarVolumenRecurso. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public String recuperarDescripcionRecurso(int idDeRecursoTmp, Usuario usuario) throws AbadiaException {
        //Definición de cadena sql de consulta
        String sSQL = "Select l.literal " +
                "from recurso_tipo rt, literales l " +
                "where rt.recursoid = ? and l.literalid = rt.literalid and l.idiomaid = ?";
        //creo un objeto de tipo Recurso
        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave.
            ps = con.prepareStatement(sSQL);
            int parNo = 1;
            ps.setInt(parNo++, idDeRecursoTmp);
            ps.setInt(parNo, usuario.getIdDeIdioma());
            //Lanzo la consulta y cargo el resultado en un resultset
            rs = ps.executeQuery();
            //si la consulta encuentra la recurso....
            if (rs.next()) {
                //iniciamos el volcado de datos sobre
                //el objeto recurso.
                return rs.getString("literal");
            } else return "";

        } catch (SQLException e) {
            throw new AbadiaSQLException("adRecurso. recuperarDescripcionRecurso. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public String recuperarDescripcionRecurso(int idDeRecursoTmp, int idIdioma) throws AbadiaException {
        //Definición de cadena sql de consulta
        String sSQL = "Select l.literal " +
                "from recurso_tipo rt, literales l " +
                "where rt.recursoid = ? and l.literalid = rt.literalid and l.idiomaid = ? ";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idDeRecursoTmp);
            ps.setInt(2, idIdioma);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("literal");
            } else return "";

        } catch (SQLException e) {
            throw new AbadiaSQLException("adRecurso. recuperarDescripcionRecurso. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public void decrementarEstadoRecursoPorEdificio(int idAbadia, int idTipoEdificio, int desgaste) throws AbadiaException {
        String sSQL = "update recurso, recurso_tipo set recurso.estado = recurso.estado - " + desgaste + " where  recurso_tipo.EDIFICIOID_ALMACEN = " + idTipoEdificio + " AND recurso.ABADIAID = " + idAbadia + " and recurso_tipo.recursoid = recurso.recursoid AND recurso_tipo.DETERIORABLE = 1";
        adUtils utils = null;

        try {
            utils = new adUtils(con);
            utils.execSQL(sSQL);
        } catch (AbadiaException e) {
            throw new AbadiaSQLException("adRecurso. decrementarEstadoRecursoPorEdificio. SQLException", e, log);
        } finally {
            //
        }

    }

    public void decrementarCantidadRecursoPorEdificio(int idAbadia, int idTipoEdificio, int cantidad) throws AbadiaException {
        String sSQL = "update recurso, recurso_tipo set recurso.cantidad = recurso.cantidad - round((recurso.cantidad / " + cantidad + ")) where  recurso_tipo.EDIFICIOID_ALMACEN = " + idTipoEdificio + " AND recurso.ABADIAID = " + idAbadia + " and recurso_tipo.recursoid = recurso.recursoid AND recurso_tipo.DETERIORABLE = 2";
        adUtils utils = null;

        try {
            utils = new adUtils(con);
            utils.execSQL(sSQL);
        } catch (AbadiaException e) {
            throw new AbadiaSQLException("adRecurso. decrementarEstadoRecursoPorEdificio. SQLException", e, log);
        } finally {
            //
        }

    }

    public void eliminarRecursosMantenimiento() throws AbadiaException {
        String sSQL = "Delete From recurso Where estado <= 0";
        adUtils utils = new adUtils(con);
        utils.execSQL(sSQL);
    }

    /* Barras de HTML
     */

    public void actualizarBarrasHTML(Recurso recurso, MessageResources resource) {
        String sHTML = "";
        sHTML = sHTML + HTML.smallBarra(recurso.getEstado() / 10, resource.getMessage("edificios.abadia.almacen.estado") + " " + recurso.getEstado() + " " + resource.getMessage("edificio.abadia.sacrificar.texto4") + " " + 100);
        recurso.setBarra_HTML(sHTML);
    }

    /**
     * Recupera el literal de la unidad de medida de un recurso determinado
     *
     * @param idRecurso
     * @param usuario
     * @return
     * @throws AbadiaSQLException
     */
    public String recuperarUnidadMedidaPorRecurso(int idRecurso, Usuario usuario) throws AbadiaSQLException {
        String sSQLLote = "SELECT um.unidad_desc FROM unidad_medida um, recurso_tipo rt WHERE rt.unidad_medida = um.unidad_medida and um.idiomaid=? and rt.recursoid=?";

        // Lote
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQLLote);
            int parNo = 1;
            ps.setInt(parNo++, usuario.getIdDeIdioma());
            ps.setInt(parNo, idRecurso);
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("unidad_desc");
            } else return "";

        } catch (SQLException e) {
            throw new AbadiaSQLException("adRecurso. recuperarUnidadMedidaPorAlimento. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }


}
