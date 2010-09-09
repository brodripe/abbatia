package org.abbatia.adbean;

import org.abbatia.adbean.base.adbeans;
import org.abbatia.bean.*;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.Utilidades;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.sql.*;
import java.util.*;

public class adUtils extends adbeans {
    private static Logger log = Logger.getLogger(adUtils.class.getName());

    /**
     * Instancia la clase sin obtener una nueva conexión
     *
     * @param con
     * @throws AbadiaException
     */
    public adUtils(Connection con) throws AbadiaException {
        super(con);
        //super(con);
    }

    // Recoger una tabla de datos y rellenarlo de table

    public ArrayList<Table> getTable(String sTableName) throws AbadiaException {
        return getTable(sTableName, "");
    }

    // Recoger una tabla de datos, ordenado por... y rellenarlo de table

    public ArrayList<Table> getTable(String sTableName, String sOrderBy) throws AbadiaException {
        String sSQL = "Select * from " + sTableName;
        if ((sOrderBy != null) && (sOrderBy.compareTo("") != 0)) {
            sSQL = sSQL + " ORDER BY " + sOrderBy;
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            ArrayList<Table> alTable = new ArrayList<Table>();
            Table tabla = null;
            while (rs.next()) {
                tabla = new Table(rs.getInt(1), rs.getString(2));
                alTable.add(tabla);
            }
            return alTable;
        } catch (SQLException e) {
            log.error("adUtils. getTable. error SQL", e);
            throw new AbadiaSQLException("adUtils. getTable. error SQL", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    // Recoger una tabla de datos, ordenado por... y rellenarlo de table

    public ArrayList<Table> getTable2(String sTableName) throws AbadiaException {
        String sSQL = "Select * from " + sTableName;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            ArrayList<Table> alTable = new ArrayList<Table>();
            Table tabla = new Table(0, "Todas");
            alTable.add(tabla);
            while (rs.next()) {
                tabla = new Table(rs.getInt(1), rs.getString(2));
                alTable.add(tabla);
            }
            return alTable;
        } catch (SQLException e) {
            log.error("adUtils. getTable. error SQL", e);
            throw new AbadiaSQLException("adUtils. getTable. error SQL", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public ArrayList<Table> getClaveValor(String sTableName) throws AbadiaSQLException {
        String sSQL = "Select SUBCODIGO, DESCRIPCION from tablas WHERE CODIGO = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setString(1, sTableName);
            rs = ps.executeQuery();
            ArrayList<Table> alTable = new ArrayList<Table>();
            Table tabla;
            while (rs.next()) {
                tabla = new Table(rs.getInt("SUBCODIGO"), rs.getString("DESCRIPCION"));
                alTable.add(tabla);
            }
            return alTable;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adUtils. getClaveValor. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }


    /*
          Metodo que devuelve un valor de la Tabla directamente sin crear clases ni na!
          JOHN 05-05-2004
    */

    public String getTablaValor(String Codigo, String SubCodigo, String sValorDefecto) throws AbadiaException {
        String sSQL = "Select Valor from tablas where Codigo = ? and SubCodigo = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setString(1, Codigo);
            ps.setString(2, SubCodigo);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            } else {
                return sValorDefecto;
            }
        } catch (SQLException e) {
            throw new AbadiaSQLException("adUtils. getTablaValor. SQLException ", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /*
          Metodo que devuelve una descripción de la Tabla directamente sin crear clases ni na!
          JOHN 05-05-2004
    */

    public String getTablaDescripcion(String Codigo, String SubCodigo, String sValorDefecto) throws AbadiaSQLException {
        String sSQL = "Select Descripcion from tablas where Codigo = ? and SubCodigo = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setString(1, Codigo);
            ps.setString(2, SubCodigo);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            } else {
                return sValorDefecto;
            }
        } catch (SQLException e) {
            throw new AbadiaSQLException("adUtils. getTablaDescripcion. SQLException ", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /*
          Metodo que devuelve un SQL directamente sin crear clases ni na!
          devuelve un string o un entero ( está sobrecargado )
          JOHN 05-05-2004
    */

    public String getSQL(String sSQL, String sValorDefecto) throws AbadiaException {

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            } else {
                return sValorDefecto;
            }
        } catch (SQLException e) {
            throw new AbadiaSQLException("adUtils. getSQL. SQLException: " + sSQL, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public ArrayList<Integer> getSQLIntArray(String sSQL) throws AbadiaException {

        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Integer> alLista = new ArrayList<Integer>();
        try {
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                alLista.add(rs.getInt(1));
            }
            return alLista;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adUtils. getSQLIntArray. SQLException: " + sSQL, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }


    public int getSQL(String sSQL, int sValorDefecto) throws AbadiaSQLException {

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            if (rs.next()) {
                return (int) rs.getLong(1);
            } else {
                return sValorDefecto;
            }
        } catch (SQLException e) {
            throw new AbadiaSQLException("adUtils. getSQL. SQLException: " + sSQL, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public boolean getSQL(String sSQL, boolean bValorDefecto) throws AbadiaSQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adUtils. getSQL. SQLException: " + sSQL, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

/*
    public int[] getSQL(String sSQL) throws AbadiaSQLException {

        PreparedStatement ps = null;
        ResultSet rs = null;
        int[] valores = new int[500];
        try {
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            int iCount;
            for (iCount = 0; rs.next(); iCount++) {
                valores[iCount] = rs.getInt(1);
            }

            int[] valoresR = new int[iCount];

            System.arraycopy(valores, 0, valoresR, 0, iCount);
            return valoresR;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adUtils. int[] getSQL. SQLException: " + sSQL, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }
*/

    public Vector<String> getSQLVector(String sSQL) throws AbadiaSQLException {

        PreparedStatement ps = null;
        ResultSet rs = null;
        Vector<String> v = new Vector<String>();
        try {
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();

            while (rs.next()) {
                v.add(rs.getString(1));
            }

            return v;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adUtils. getSQLVector. SQLException: " + sSQL, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public float getSQL(String sSQL, float sValorDefecto) throws AbadiaException {

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getFloat(1);
            } else {
                return sValorDefecto;
            }
        } catch (SQLException e) {
            throw new AbadiaSQLException("adUtils. getSQL. SQLException: " + sSQL, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public double getSQL(String sSQL, double sValorDefecto) throws AbadiaException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            } else {
                return sValorDefecto;
            }
        } catch (SQLException e) {
            throw new AbadiaSQLException("adUtils. getSQL. SQLException: " + sSQL, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /*
          Ejecuta un SQL directamente sin crear clases ni na! ( para UPDATE o INSERT puntuales )
          devuelve verdadero si lo ha realizado correctamente o false si ha fallado
          JOHN 05-05-2004
    */

    public boolean execSQL(String sSQL) throws AbadiaException {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            return ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("Error al ejecutar execSQL - " + sSQL, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public boolean execProcedure(String sSQL) throws AbadiaException {
        CallableStatement cs = null;
        try {
            cs = con.prepareCall(sSQL);
            return cs.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("Error al ejecutar execSQL - " + sSQL, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(cs);
        }

    }

    public List<Table> execProcedureList(String sSQL, boolean p_bZeroValue) throws AbadiaException {
        CallableStatement cs = null;
        ResultSet rs = null;
        ArrayList<Table> alTabla = new ArrayList<Table>();
        if (p_bZeroValue) alTabla.add(new Table(0, "Todos"));
        try {
            cs = con.prepareCall(sSQL);
            cs.execute();
            rs = cs.getResultSet();
            while (rs.next()) {
                alTabla.add(new Table(rs.getInt(1), rs.getString(2)));
            }
            return alTabla;
        } catch (SQLException e) {
            throw new AbadiaSQLException("Error al ejecutar execSQL - " + sSQL, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(cs);
        }

    }

    public List<Table> execSQLList(String sSQL) throws AbadiaException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Table> alTabla = new ArrayList<Table>();
        try {
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                alTabla.add(new Table(rs.getInt(1), rs.getString(2)));
            }
            return alTabla;
        } catch (SQLException e) {
            throw new AbadiaSQLException("Error al ejecutar execSQL - " + sSQL, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /**
     * Recuperar un arraylist de integers a partir del sql recibido por parámetro
     *
     * @param sSQL
     * @return
     * @throws AbadiaException
     */
    public List<Integer> execSQLListInt(String sSQL) throws AbadiaException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Integer> alTabla = new ArrayList<Integer>();
        try {
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                alTabla.add(rs.getInt(1));
            }
            return alTabla;
        } catch (SQLException e) {
            throw new AbadiaSQLException("Error al ejecutar execSQL - " + sSQL, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /*
          Función que devuelve la propiedad, se sobrecarga el nombre de la propiedad por si lo sabes o no
          JOHN 05-05-2004
    */

    public String getPropriedad(int PropiedadID, int ClaveID, String tipo, String defecto) throws AbadiaException {
        String sSQL = "SELECT Valor FROM propiedad_valor WHERE PropiedadID = ? AND ClaveID = ? and Tipo = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, PropiedadID);
            ps.setInt(2, ClaveID);
            ps.setString(3, tipo);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            } else {
                // Buscar el valor por defecto si lo tiene ( -1 )
                if (defecto.equals("")) {
                    ps.setInt(1, PropiedadID);
                    ps.setInt(2, -1);
                    ps.setString(3, tipo);
                    rs = ps.executeQuery();
                    if (rs.next())
                        return rs.getString(1);
                }
                return defecto;
            }
        } catch (SQLException e) {
            throw new AbadiaSQLException("Se ha producido un error buscando la Propiedad.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public String getPropriedad(int PropiedadID, int ClaveID, String tipo) {
        try {
            return getPropriedad(PropiedadID, ClaveID, tipo, "");
        } catch (AbadiaException e) {
        }
        return tipo;
    }

    public String getPropriedad(String Propiedad, int ClaveID, String tipo) throws AbadiaException {
        String sSQL = "SELECT PropiedadID FROM propiedad WHERE Nombre = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sSQL);
            ps.setString(1, Propiedad);
            ResultSet rs = ps.executeQuery();
            boolean eof = rs.next();
            if (eof) {
                return getPropriedad(rs.getInt(1), ClaveID, tipo);
            } else {
                return "";
            }
        } catch (SQLException e) {
            throw new AbadiaSQLException("Se ha producido un error buscando el nombre de la Propiedad.", e, log);
        }
    }

    public void setPropriedad(int PropiedadID, int ClaveID, String tipo, String valor) throws AbadiaException {
        String uSQL = "UPDATE propiedad_valor Set valor=? WHERE PropiedadID = ? AND ClaveID = ? and Tipo = ?";
        String iSQL = "INSERT INTO `propiedad_valor` (`PROPIEDADID`, `CLAVEID`, `TIPO`, `VALOR`) " +
                "VALUES (?, ?, ?, ?)";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {

            String wtd = getPropriedad(PropiedadID, ClaveID, tipo, "null");
            if (wtd.equals("null")) {
                ps = con.prepareStatement(iSQL);
                ps.setInt(1, PropiedadID);
                ps.setInt(2, ClaveID);
                ps.setString(3, tipo);
                ps.setString(4, valor);
            } else {
                ps = con.prepareStatement(uSQL);
                ps.setString(1, valor);
                ps.setInt(2, PropiedadID);
                ps.setInt(3, ClaveID);
                ps.setString(4, tipo);
            }
            ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("Se ha producido un error setPropriedad:", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    // Recoger los nombres por region

    public ArrayList<Table> getNombresPorRegion(long region) throws AbadiaException {
        String sSQL = "Select SUBCODIGO, tablas.DESCRIPCION from tablas, region WHERE (CONCAT('NOM_', region.NOM) = tablas.CODIGO) AND region.REGIONID = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setLong(1, region);
            rs = ps.executeQuery();
            ArrayList<Table> alTable = new ArrayList<Table>();
            Table tabla;
            while (rs.next()) {
                tabla = new Table(rs.getInt("SUBCODIGO"), rs.getString("DESCRIPCION"));
                alTable.add(tabla);
            }
            return alTable;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adUtils. getNombresPorRegion. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    // Recoger los nombres por region

    public ArrayList<Table> getCiudadesPorRegion(long region) throws AbadiaException {
        String sSQL = "SELECT * FROM region_ciudades WHERE REGIONID = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setLong(1, region);
            rs = ps.executeQuery();
            ArrayList<Table> alTable = new ArrayList<Table>();
            Table tabla;
            while (rs.next()) {
                tabla = new Table(rs.getInt("REGIONID"), rs.getString("NOMBRE"));
                alTable.add(tabla);
            }
            return alTable;
        } catch (SQLException e) {
            throw new AbadiaSQLException("Se ha producido un error buscando los nombre de ciudad para la región.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public ArrayList<Table> getTableRegionMenosUso() throws AbadiaException {
        String sSQL = "SELECT a.regionid, count(*) contar, max(descripcion) descripcion " +
                " FROM abadia a, region r, abadia_puntuacion ap " +
                " WHERE a.regionid = r.regionid and a.dummy = 0 and a.abadiaid = ap.abadiaid and ap.total > 0 " +
                " GROUP by a.regionid " +
                " ORDER by contar " +
                " LIMIT 10 ";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            ArrayList<Table> alTable = new ArrayList<Table>();
            Table tabla;
            while (rs.next()) {
                tabla = new Table(rs.getInt("REGIONID"), rs.getString("DESCRIPCION"));
                alTable.add(tabla);
            }
            return alTable;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adUtils. getTableRegion. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public ArrayList<Table> getTableRegion() throws AbadiaException {
        String sSQL = "Select REGIONID, DESCRIPCION from region";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            ArrayList<Table> alTable = new ArrayList<Table>();
            Table tabla;
            while (rs.next()) {
                tabla = new Table(rs.getInt("REGIONID"), rs.getString("DESCRIPCION"));
                alTable.add(tabla);
            }
            return alTable;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adUtils. getTableRegion. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public ArrayList<Table> getTableRegion2() throws AbadiaException {
        String sSQL = "Select REGIONID, DESCRIPCION from region where regionid<>0 order by descripcion";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            ArrayList<Table> alTable = new ArrayList<Table>();
            Table tabla = new Table(0, "Todas");
            alTable.add(tabla);
            while (rs.next()) {
                tabla = new Table(rs.getInt("REGIONID"), rs.getString("DESCRIPCION"));
                alTable.add(tabla);
            }
            return alTable;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adUtils. getTableRegion. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    // ___________________________________________________________________________
    //   Pagina de Inicio Index_main.do ( JLO )
    //     Recupera los valores de todos los objetos de la página de inicio
    // ___________________________________________________________________________

    public InicioMain getInicioMain(String Param) throws AbadiaException {
        InicioMain Inicio = new InicioMain();

        // Cargar las últimas conexiones
        String sSQLConex = "Select a.nombre, u.ultimaconexion, u.registrado, u.usuario_tipo, u.abadia_congelada from abadia as a, usuario as u WHERE  a.usuarioid = u.usuarioid ORDER BY u.ULTIMACONEXION desc LIMIT 5";
        String sSQLAbadias = "SELECT a.nombre, u.registrado, u.usuario_tipo, u.abadia_congelada from abadia as a, usuario as u, abadia_puntuacion_ultima ap WHERE u.usuarioid = a.usuarioid and u.usuario_tipo <> 1 and u.usuarioid not in (select usuarioid from usuario_bloqueo) and a.abadiaid = ap.abadiaid order by ap.total desc LIMIT 5";
        String sSQLMensajes = "SELECT m.fechareal, m.mensaje, m.monjeid, a.nombre, m.abadiaid  FROM mensajes m, abadia a WHERE m.abadiaid = a.abadiaid and idiomaid = 1 " +
                " union" +
                " SELECT m.fechareal, m.mensaje, m.monjeid, '' as nombre, m.abadiaid  FROM mensajes m WHERE m.abadiaid = -1 AND m.monjeid=-1 and m.idiomaid=1 " +
                " ORDER BY fechareal DESC";
        if (Param.equals("mensaje")) {
            sSQLMensajes = sSQLMensajes + " LIMIT 50";
        } else {
            sSQLMensajes = sSQLMensajes + " LIMIT 5";
        }
        // varios
        String sHTML;
        String strTimeDB, strDateDB, strDateDBold = "";
        int iSeparator;
        PreparedStatement ps = null;
        ResultSet rs = null;
        adAbadia oAbadiaAD;

        try {
            ps = con.prepareStatement(sSQLConex);
            rs = ps.executeQuery();

            ArrayList<Serializable> alTable = new ArrayList<Serializable>();
            Table tabla;
            while (rs.next()) {
                strDateDB = Utilidades.formatStringFromDB(rs.getString("ULTIMACONEXION"));
                iSeparator = strDateDB.indexOf(" ");
                if (iSeparator == -1) {
                    strTimeDB = "00:00";
                } else {
                    strTimeDB = strDateDB.substring(strDateDB.indexOf(" "));
                    strDateDB = strDateDB.substring(0, iSeparator);
                }

                sHTML = "";
                if (strDateDB.compareTo(strDateDBold) != 0) {
                    if (!alTable.isEmpty()) {
                        sHTML = sHTML + "</dl></div>";
                    }
                    sHTML = sHTML + "<h5>" + strDateDB + "</h5>";
                    strDateDBold = strDateDB;
                    sHTML = sHTML + "<div id='ultima_conexion'><dl>";

                }
                sHTML = sHTML + " <dt>" + strTimeDB + "</dt>";
                sHTML = sHTML + "<dd>" + rs.getString("nombre");
                if (rs.getInt("registrado") == 1) sHTML = sHTML + "&nbsp;<img src='images/iconos/16/registrado.gif'>";
                if (rs.getInt("usuario_tipo") == 1) sHTML = sHTML + "&nbsp;<img src='images/iconos/16/gm.gif'>";
                if (rs.getInt("usuario_tipo") == 3)
                    sHTML = sHTML + "&nbsp;<img src='images/iconos/16/colaboradorplus.gif'>";
                if (rs.getInt("usuario_tipo") == 2)
                    sHTML = sHTML + "&nbsp;<img src='images/iconos/16/colaborador.gif'>";
                sHTML = sHTML + "</dd>";
                tabla = new Table(0, sHTML);
                alTable.add(tabla);
            }
            tabla = new Table(0, "</dl></div>");
            alTable.add(tabla);
            Inicio.setUltimasConexiones(alTable);

            // Recuperar mejores abadias
            int n = 1;
            ps = con.prepareStatement(sSQLAbadias);
            rs = ps.executeQuery();
            alTable = new ArrayList<Serializable>();
            //tabla = null;
            while (rs.next()) {
                sHTML = "";//"<font color=\"#555555\">" + n + ".</font>&nbsp;";
                //if (rs.getInt("abadia_congelada") == 1) sHTML = sHTML + "<font color=\"#00CCFF\">";
                sHTML = sHTML + rs.getString("nombre");
                //if (rs.getInt("abadia_congelada") == 1) sHTML = sHTML + "</font>";
                if (rs.getInt("registrado") == 1) sHTML = sHTML + "&nbsp;<img src=\"images/iconos/16/registrado.gif\">";
                if (rs.getInt("usuario_tipo") == 1) sHTML = sHTML + "&nbsp;<img src=\"images/iconos/16/gm.gif\">";
                if (rs.getInt("usuario_tipo") == 2)
                    sHTML = sHTML + "&nbsp;<img src=\"images/iconos/16/colaborador.gif\">";
                tabla = new Table(n, sHTML);
                alTable.add(tabla);
                n++;
            }
            Inicio.setUltimosMejoresAbadias(alTable);

            // Recuperar últimos acontecimientos
            ps = con.prepareStatement(sSQLMensajes);
            rs = ps.executeQuery();
            alTable = new ArrayList<Serializable>();
            int santo;
            Mensaje objMsg;

            while (rs.next()) {
                objMsg = new Mensaje();
                objMsg.setFechaReal(Utilidades.formatStringFromDB(rs.getString("fechareal")));
                objMsg.setIdAbadia(rs.getInt("abadiaid"));
                objMsg.setIdDeMonje(rs.getInt("monjeid"));
                sHTML = "";
                if (objMsg.getIdAbadia() != -1) {
                    oAbadiaAD = new adAbadia(con);
                    objMsg.setNombreAbadia(oAbadiaAD.getNomAbadia(objMsg.getIdAbadia()));
                } else {
                    objMsg.setNombreAbadia(" ");
                }
                if (objMsg.getIdDeMonje() != -1) {
                    santo = getSQL("SELECT Santo FROM monje where monjeid = " + objMsg.getIdDeMonje(), 0);
                    if (santo == 1) sHTML = sHTML + "<font color=\"#FFFFFF\"><b>";
                    sHTML = sHTML + getSQL("SELECT nombre FROM monje where monjeid = " + objMsg.getIdDeMonje(), "");
                    if (santo == 1) sHTML = sHTML + "</b></font>";
                    sHTML = sHTML + " : ";
                }
                sHTML = sHTML + rs.getString("mensaje");
                objMsg.setMensaje(sHTML);
                //tabla = new Table(n, sHTML );
                alTable.add(objMsg);
            }
            //Utils.finalize();

            Inicio.setUltimosAcontecimientos(alTable);

            return Inicio;
        }
        catch (SQLException e) {
            throw new AbadiaSQLException("adUtils. getInicioMain. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

// ___________________________________________________________________________
    //   Pagina de Inicio Index_main.do ( JLO )
    //     Recupera los valores de todos los objetos de la página de inicio
    // ___________________________________________________________________________

    public InicioMain getTopUsers(int pagina, int region) throws AbadiaException {
        InicioMain Inicio = new InicioMain();

        // Cargar las últimas conexiones
        String sSQL, sSQLCount = "";
        if (region == -1) {
            sSQL = "SELECT a.nombre, apu.total, a.regionid, r.descripcion, u.registrado, u.usuario_tipo, u.abadia_congelada from abadia a, usuario u, region r, abadia_puntuacion_ultima apu " +
                    " WHERE r.regionid = a.regionid and u.usuarioid = a.usuarioid and a.abadiaid = apu.abadiaid and u.usuario_tipo <> 1 and u.usuarioid not in (select usuarioid from usuario_bloqueo)  order by apu.total desc ";
            sSQLCount = "SELECT Count(*) from abadia a, usuario u, region r " +
                    " WHERE r.regionid = a.regionid and u.usuarioid = a.usuarioid and u.usuario_tipo <> 1 and u.usuarioid not in (select usuarioid from usuario_bloqueo) ";
        } else {
            if (region == 0) {
                sSQL = "SELECT r.regionid, r.descripcion nombre, sum(apu.total) puntua from abadia a, usuario u , region r, abadia_puntuacion_ultima apu " +
                        "WHERE a.regionid = r.regionid and u.usuarioid = a.usuarioid and a.abadiaid = apu.abadiaid and u.usuario_tipo <> 1 and u.usuarioid not in (select usuarioid from usuario_bloqueo) " +
                        "group by r.regionid, r.descripcion " +
                        "order by puntua desc ";
            } else {
                sSQL = "SELECT a.nombre, apu.total, u.registrado, u.usuario_tipo, u.abadia_congelada from abadia a, usuario u, abadia_puntuacion_ultima apu " +
                        " WHERE a.regionid = " + region +
                        " and u.usuarioid = a.usuarioid and a.abadiaid = apu.abadiaid and u.usuario_tipo <> 1 and u.usuarioid not in (select usuarioid from usuario_bloqueo) " +
                        " order by apu.total desc  ";
                sSQLCount = "SELECT Count(*) from abadia a, usuario u " +
                        " WHERE a.regionid = " + region +
                        " and u.usuarioid = a.usuarioid and u.usuario_tipo <> 1 and u.usuarioid not in (select usuarioid from usuario_bloqueo) ";
            }
        }
        String sHTML;
        int n = 1;
        // Top users
        PreparedStatement ps = null;
        ResultSet rs = null;
        Table tabla;
        try {
            sSQL = sSQL + " LIMIT " + (pagina * 100) + ",100";

            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            ArrayList<Table> alTable = new ArrayList<Table>();

            if (region > 0) {
                sHTML = "<tr><td colspan=3 align=\"center\"><b><a href=\"index_main.do?param=topusers&region=0\">";
                sHTML = sHTML + getSQL("Select descripcion from region where regionid=" + region, "");
                sHTML = sHTML + "</b></a></td></tr>";
                tabla = new Table(n, sHTML);
                alTable.add(tabla);
            }

            while (rs.next()) {
                sHTML = "<tr><td>" + Math.round(n + (pagina * 100)) + ".</td><td>";
                if (region != 0) {
                    if (rs.getInt("abadia_congelada") == 1) sHTML = sHTML + "<font color=\"#00CCFF\">";
                    sHTML = sHTML + rs.getString("nombre");
                    if (rs.getInt("registrado") == 1)
                        sHTML = sHTML + "&nbsp;<img src=\"images/iconos/16/registrado.gif\">";
                    if (rs.getInt("usuario_tipo") == 2)
                        sHTML = sHTML + "&nbsp;<img src=\"images/iconos/16/colaborador.gif\">";
                    if (rs.getInt("usuario_tipo") == 3)
                        sHTML = sHTML + "&nbsp;<img src=\"images/iconos/16/colaboradorplus.gif\">";
                    if (rs.getInt("abadia_congelada") == 1) sHTML = sHTML + "</font>";
                } else
                    sHTML = sHTML + rs.getString("nombre");
                sHTML = sHTML + "</td>";
                switch (region) {
                    case -1:
                        sHTML = sHTML + "<td align=\"right\">" + Utilidades.redondear(rs.getDouble("total")) + "</td>";
                        sHTML = sHTML + "<td align=\"center\"><a href=\"index_main.do?param=topusers&region=" + rs.getInt("regionid") + "\">" + rs.getString("descripcion") + "</a></td>";
                        break;
                    case 0:
                        sHTML = sHTML + "<td align=\"right\">" + Utilidades.redondear(rs.getDouble("puntua")) + "</td>";
                        break;
                    default:
                        sHTML = sHTML + "<td align=\"right\">" + Utilidades.redondear(rs.getDouble("total")) + "</td>";
                        break;
                }
                sHTML = sHTML + "</tr>";
                tabla = new Table(n, sHTML);
                alTable.add(tabla);
                n++;
            }
            Inicio.setUltimosMejoresAbadias(alTable);
            if (region == 0) {
                Inicio.setCountMejoresAbadias(50);
            } else {
                Inicio.setCountMejoresAbadias(getSQL(sSQLCount, 0));
            }
            return Inicio;
        }
        catch (SQLException e) {
            throw new AbadiaSQLException("Se ha producido un error getTopUsers conexiones.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public void restablecerHabilidades(long idAbadia) throws AbadiaException {
        adMonje monjeAD = new adMonje(con);
        ArrayList<Monje> listaMonjes = monjeAD.getDatosMonjesPorAbadiaH(idAbadia, Constantes.MONJES_TODOS);
        Iterator<Monje> monjes = listaMonjes.iterator();
        adHabilidades habilidadesAD;
        Monje monje;
        while (monjes.hasNext()) {
            habilidadesAD = new adHabilidades(con);
            monje = monjes.next();
            if (monje.getFe() == 1000) {
                habilidadesAD.crearHabilidad(Constantes.HABILIDAD_FE, monje.getIdDeMonje(), 100);
            } else if (monje.getFe() != 100) {
                habilidadesAD.actualizarHabilidad(Constantes.HABILIDAD_FE, monje.getIdDeMonje(), 100);
            }

            if (monje.getIdioma() == 1000) {
                habilidadesAD.crearHabilidad(Constantes.HABILIDAD_IDIOMA, monje.getIdDeMonje(), 30);
            } else if (monje.getIdioma() != 30) {
                habilidadesAD.actualizarHabilidad(Constantes.HABILIDAD_IDIOMA, monje.getIdDeMonje(), 30);
            }

            if (monje.getTalento() == 1000) {
                habilidadesAD.crearHabilidad(Constantes.HABILIDAD_TALENTO, monje.getIdDeMonje(), 30);
            } else if (monje.getTalento() != 30) {
                habilidadesAD.actualizarHabilidad(Constantes.HABILIDAD_TALENTO, monje.getIdDeMonje(), 30);
            }

            if (monje.getSabiduria() == 1000) {
                habilidadesAD.crearHabilidad(Constantes.HABILIDAD_SABIDURIA, monje.getIdDeMonje(), 30);
            } else if (monje.getSabiduria() != 30) {
                habilidadesAD.actualizarHabilidad(Constantes.HABILIDAD_SABIDURIA, monje.getIdDeMonje(), 30);
            }

            if (monje.getFuerza() == 1000) {
                habilidadesAD.crearHabilidad(Constantes.HABILIDAD_FUERZA, monje.getIdDeMonje(), 30);
            } else if (monje.getFuerza() != 30) {
                habilidadesAD.actualizarHabilidad(Constantes.HABILIDAD_FUERZA, monje.getIdDeMonje(), 30);
            }

            if (monje.getCarisma() == 1000) {
                habilidadesAD.crearHabilidad(Constantes.HABILIDAD_CARISMA, monje.getIdDeMonje(), 30);
            } else if (monje.getCarisma() != 30) {
                habilidadesAD.actualizarHabilidad(Constantes.HABILIDAD_CARISMA, monje.getIdDeMonje(), 30);
            }

            if (monje.getDestreza() == 1000) {
                habilidadesAD.crearHabilidad(Constantes.HABILIDAD_DESTREZA, monje.getIdDeMonje(), 30);
            } else if (monje.getDestreza() != 30) {
                habilidadesAD.actualizarHabilidad(Constantes.HABILIDAD_DESTREZA, monje.getIdDeMonje(), 30);
            }

        }

    }

    public int getIdiomaID(int idabadia) throws AbadiaException {
        String sSQL = "SELECT u.IdiomaID FROM `abadia` a, `usuario` u WHERE a.AbadiaID = ? AND a.UsuarioID = u.UsuarioID";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            int parNo = 1;
            ps = con.prepareStatement(sSQL);
            ps.setInt(parNo, idabadia);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            } else return 1;
        } catch (SQLException e) {
            throw new AbadiaSQLException("ERROR: al ejecutar getIdiomaID abbatia:" + idabadia, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /**
     * Recupera un literal para un idioma determinado
     *
     * @param LiteralID
     * @param IdiomaID
     * @return
     * @throws AbadiaException
     */
    public String getLiteral(int LiteralID, int IdiomaID) throws AbadiaException {
        String sSQL = "SELECT literal FROM `literales` WHERE literalid =  ? and idiomaid = ?";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, LiteralID);
            ps.setInt(2, IdiomaID);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            } else {
                ps.close();
                ps = con.prepareStatement(sSQL);
                ps.setInt(1, LiteralID);
                ps.setInt(2, 1);
                rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
            return "Sin Literal=" + LiteralID + ":" + IdiomaID;
        } catch (SQLException e) {
            throw new AbadiaSQLException("ERROR: al ejecutar getLiteral " + LiteralID + ":" + IdiomaID, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public String getLiteral(int[] LiteralID, int IdiomaID) throws AbadiaException {
        String sSQL = "SELECT literal FROM `literales` WHERE literalid =  ? and idiomaid = ?";

        int idLiteral = LiteralID[Utilidades.Random(0, LiteralID.length - 1)];

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idLiteral);
            ps.setInt(2, IdiomaID);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            } else {
                ps.close();
                ps = con.prepareStatement(sSQL);
                ps.setInt(1, idLiteral);
                ps.setInt(2, 1);
                rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
            return "Sin Literal=" + LiteralID + ":" + IdiomaID;
        } catch (SQLException e) {
            throw new AbadiaSQLException("ERROR: al ejecutar getLiteral " + LiteralID + ":" + IdiomaID, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    // Ejecuta directamente una SQL y devuelve un autoincrementado

    public int execInsSQL(String sSQL) throws AbadiaSQLException {
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = con.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
                    java.sql.ResultSet.CONCUR_UPDATABLE);

            stmt.executeUpdate(sSQL, Statement.RETURN_GENERATED_KEYS);
            rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return -1;
            }

        } catch (SQLException e) {
            throw new AbadiaSQLException("ERROR execInsSQL = " + e, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(stmt);
        }
    }

    // Devolver una propiedad

    public String getDBProp(int propiedadID, String tipo, int clave, String defecto) throws AbadiaException {
        try {
            adUtils utils = new adUtils(con);
            String r = utils.getSQL("SELECT valor FROM `propiedad_valor` pv WHERE pv.propiedadid = " + propiedadID +
                    " AND pv.claveid = " + clave + " AND pv.tipo = '" + tipo + "'", defecto);
            return r;
        } catch (AbadiaSQLException ex) {
            return defecto;
        }
    }

    // Devolver una propiedad

    public String getPropiedad(int propiedadID, String tipo, int clave, String defecto) throws AbadiaException {
        try {
            adUtils utils = new adUtils(con);
            String r = utils.getSQL("SELECT valor FROM `propiedad_valor` pv WHERE pv.propiedadid = " + propiedadID +
                    " AND pv.claveid = " + clave + " AND pv.tipo = '" + tipo + "'", defecto);
            return r;
        }
        catch (AbadiaSQLException ex) {
            return defecto;
        }
    }

    public int getDBProp(int propiedadID, String tipo, int clave, int defecto) throws AbadiaException {
        try {
            adUtils utils = new adUtils(con);
            int r = utils.getSQL("SELECT valor FROM `propiedad_valor` pv WHERE pv.propiedadid = " + propiedadID +
                    " AND pv.claveid = " + clave + " AND pv.tipo = '" + tipo + "'", defecto);
            return r;
        }
        catch (AbadiaSQLException ex) {
            return defecto;
        }
    }

    /**
     * Devuelve una tabla de idiomas a partir de un flag
     * 0 - No seleccionables en info de usuario
     * 1 - Seleccionables en info de usuario
     *
     * @param flag
     * @return
     * @throws AbadiaSQLException
     */
    public ArrayList<Table> getTablaIdiomas(short flag) throws AbadiaSQLException {
        String sSQL = "Select idiomaid, descripcion from idioma where asignable = ? ";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setShort(1, flag);
            rs = ps.executeQuery();
            ArrayList<Table> alTable = new ArrayList<Table>();
            Table tabla;
            while (rs.next()) {
                tabla = new Table(rs.getInt(1), rs.getString(2));
                alTable.add(tabla);
            }
            return alTable;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adUtils. getTablaIdiomas. SQLException ", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * REcupera el valor de una propiedad de la tabla propiedad_valor
     *
     * @param idPropiedad
     * @param idClave
     * @param sTipo
     * @return
     * @throws AbadiaException
     */
    public String getPropidadValor(int idPropiedad, int idClave, String sTipo) throws AbadiaException {
        String sSQL = "select valor from propiedad_valor where propiedadid = " + idPropiedad + " and claveid = " + idClave + " and tipo = '" + sTipo + "'";
        adUtils utils = new adUtils(con);
        return utils.getSQL(sSQL, "0");
    }

    /**
     * REcupera la lista de excepciones maximas permitidas
     *
     * @return Hashtable
     * @throws AbadiaException
     */
    public HashMap<String, ExcepcionMax> getTablaExcepciones() throws AbadiaException {
        String sSQL = "Select nombre, max, accion from excepcion_max ";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            HashMap<String, ExcepcionMax> htTable = new HashMap<String, ExcepcionMax>();
            ExcepcionMax excepcion;
            while (rs.next()) {
                excepcion = new ExcepcionMax();
                excepcion.setNombreExcepcion(rs.getString("NOMBRE"));
                excepcion.setMax(rs.getInt("MAX"));
                excepcion.setAccion(rs.getInt("ACCION"));

                htTable.put(excepcion.getNombreExcepcion(), excepcion);
            }
            return htTable;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adUtils. getTablaExcepciones. SQLException ", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public HashMap<Integer, Double> generarHM(String sSQL) throws AbadiaException {
        HashMap<Integer, Double> hmResultado = new HashMap<Integer, Double>();
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                hmResultado.put(rs.getInt(1), rs.getDouble(2));
            }
            return hmResultado;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adUtils. generarHM. SQLException ", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public HashMap<Integer, ArrayList<TableDouble>> generarHMActividad(String sSQL) throws AbadiaException {
        HashMap<Integer, ArrayList<TableDouble>> hmResultado = new HashMap<Integer, ArrayList<TableDouble>>();
        ResultSet rs = null;
        PreparedStatement ps = null;
        int idAbadia = 0;
        int idAbadiaTmp = 0;
        ArrayList<TableDouble> alActividades = new ArrayList<TableDouble>();
        try {
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                idAbadia = rs.getInt(1);
                if (idAbadia != idAbadiaTmp) {
                    if (idAbadiaTmp != 0) {
                        hmResultado.put(idAbadiaTmp, alActividades);
                    }
                    alActividades = new ArrayList<TableDouble>();
                    idAbadiaTmp = idAbadia;
                }
                alActividades.add(new TableDouble(rs.getInt(2), rs.getDouble(3)));
            }
            //nos aseguramos de cargar los datos de la última abadía procesada....
            hmResultado.put(idAbadiaTmp, alActividades);
            return hmResultado;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adUtils. generarHMActividad. SQLException ", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public HashMap<Integer, ArrayList<PuntuacionLibros>> generarHMLibros(String sSQL) throws AbadiaException {
        HashMap<Integer, ArrayList<PuntuacionLibros>> hmResultado = new HashMap<Integer, ArrayList<PuntuacionLibros>>();
        ResultSet rs = null;
        PreparedStatement ps = null;
        int idAbadia = 0;
        int idAbadiaTmp = 0;
        ArrayList<PuntuacionLibros> alLibros = new ArrayList<PuntuacionLibros>();
        try {
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                idAbadia = rs.getInt(1);
                if (idAbadia != idAbadiaTmp) {
                    if (idAbadiaTmp != 0) {
                        hmResultado.put(idAbadiaTmp, alLibros);
                    }
                    alLibros = new ArrayList<PuntuacionLibros>();
                    idAbadiaTmp = idAbadia;
                }
                alLibros.add(new PuntuacionLibros(rs.getShort(2), rs.getShort(3), rs.getInt(4), rs.getDouble(5)));
            }
            //nos aseguramos de cargar los datos de la última abadía procesada....
            hmResultado.put(idAbadiaTmp, alLibros);
            return hmResultado;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adUtils. generarHMLibros. SQLException ", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public static float recuperarValorHM(HashMap hm, int idClave) throws AbadiaException {
        Double dValor;
        float fValor;

        dValor = (Double) hm.get(Integer.valueOf(idClave));
        if (dValor == null) {
            fValor = 0;
        } else {
            fValor = dValor.floatValue();
        }
        return fValor;
    }

    public static double recuperarValorHMActividad(HashMap hm, int idClave) throws AbadiaException {
        double dValor = 0;
        ArrayList alActividades;
        Iterator itActividades;
        TableDouble tabla;

        alActividades = (ArrayList) hm.get(Integer.valueOf(idClave));
        if (alActividades == null) {
            return 0;
        }
        itActividades = alActividades.iterator();

        while (itActividades.hasNext()) {
            tabla = (TableDouble) itActividades.next();
            dValor += tabla.getParam2();
        }

        return dValor;
    }

    public static double recuperarValorHMLibros(HashMap hm, int idClave) throws AbadiaException {
        double dValor = 0;
        ArrayList alLibros;
        Iterator itLibros;
        PuntuacionLibros registroLibro;
        int iCantidad;
        double dPuntuacion;

        alLibros = (ArrayList) hm.get(Integer.valueOf(idClave));
        if (alLibros == null) {
            return 0;
        }
        itLibros = alLibros.iterator();

        while (itLibros.hasNext()) {
            registroLibro = (PuntuacionLibros) itLibros.next();
            iCantidad = registroLibro.getCantidad();
            dPuntuacion = registroLibro.getPuntuacion();
            for (int iCount = 0; iCount < iCantidad; iCount++) {
                dValor += dPuntuacion;
                dPuntuacion = dPuntuacion * 40 / 100;
            }
        }

        return dValor;
    }

    public String generarIN(String sSQL) throws AbadiaException {
        ResultSet rs = null;
        PreparedStatement ps = null;
        StringBuffer sfResult = new StringBuffer();
        try {
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (sfResult.length() == 0) {
                    sfResult.append(rs.getInt(1));
                } else {
                    sfResult.append(",").append(rs.getInt(1));
                }
            }
            return sfResult.toString();

        } catch (SQLException e) {
            throw new AbadiaSQLException("adUtils. generarIN. SQLException ", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }
}
