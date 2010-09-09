package org.abbatia.adbean;

import org.abbatia.adbean.base.adbeans;
import org.abbatia.bean.Table;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaNamingException;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.apache.commons.validator.GenericValidator;
import org.apache.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;


public class adLiterales extends adbeans {
    private static Logger log = Logger.getLogger(adLiterales.class.getName());

    /**
     * Instancia la clase sin obtener una nueva conexión
     *
     * @param con
     * @throws AbadiaException
     */
    public adLiterales(Connection con) throws AbadiaException {
        super(con);
    }


    // Devuelve los literales delimitado por página
    public ArrayList<Table> getLiterales(int idioma1, int idioma2, int pagina, String nuevos) throws AbadiaException {
        String sSQL = "SELECT * " +
                "FROM `literales` WHERE idiomaid = ? ORDER by literalid  ";

        String sHTML = "", sIdioma2;
        int n = 1;
        ArrayList<Table> literales = new ArrayList<Table>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            Table literal;
            adUtils utils;

            if (nuevos.equals("S")) {
                sSQL = "SELECT l1.* " +
                        " FROM `literales` l1, literales l2 " +
                        " where l1.literalid = l2.literalid and l1.idiomaid=? and Left(l2.literal,1) = '*' and l2.idiomaid =  " + idioma2 +
                        " ORDER by literalid ";
            }
            sSQL = sSQL + " LIMIT " + (pagina * Constantes.REGISTROS_PAGINA) + "," + Constantes.REGISTROS_PAGINA;

            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idioma1);
            rs = ps.executeQuery();
            while (rs.next()) {
                utils = new adUtils(con);
                sIdioma2 = utils.getSQL("SELECT literal FROM `literales` WHERE literalid = " + rs.getInt("literalid") + " AND idiomaid = " + idioma2, "");
                if (n % 2 == 0)
                    sHTML = "<tr>";
                else sHTML = "<tr bgcolor=\"#EDE0C5\">";
                sHTML = sHTML + "<td>" + rs.getString("literalid") + "</td>";
                sHTML = sHTML + "<td>" + rs.getString("literal") + "</td>";
                sHTML = sHTML + "<td><input type=\"text\" name=\"T_" + n + "\" size=\"50\" value=\"" + sIdioma2 + "\" onChange=\"this.form.C_" + n + ".checked = true;\" >";
                sHTML = sHTML + "<input type=\"hidden\" name=\"L_" + n + "\" value=\"" + rs.getInt("literalid") + "\"></td>";
                sHTML = sHTML + "<td><center><input type=\"checkbox\" name=\"C_" + n + "\" value=\"ON\"></center></td>";
                sHTML = sHTML + "</tr>";
                literal = new Table(0, sHTML);
                literales.add(literal);
                n++;
            }
            return literales;
            // Ejecutarlo
        } catch (SQLException e) {
            throw new AbadiaSQLException("adLiterales. getLiterales", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    // Devuelve el total de registros de limitadores
    public int getTotalRecords(int idioma1, int idioma2, String nuevos) throws AbadiaException {
        String sSQL = "SELECT count(*) " +
                "FROM `literales` WHERE idiomaid = ?";

        if (nuevos.equals("S")) {
            sSQL = "SELECT count(*) " +
                    " FROM `literales` l1, literales l2 " +
                    " where l1.literalid = l2.literalid and l1.idiomaid=? and Left(l2.literal,1) = '*' and l2.idiomaid =  " + idioma2;
        }

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idioma1);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
            // Ejecutarlo
        } catch (SQLException e) {
            throw new AbadiaSQLException("adLiterales. getTotalRecords", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    // Establece un literal
    public void setLiteral(int literalid, int idioma, String literal) throws AbadiaException {

        adUtils utils = new adUtils(con);
        if (existeLiteral(literalid, idioma)) {
            utils.execSQL("UPDATE literales SET literal = '" + literal + "' WHERE idiomaid = " + idioma + " and literalid = " + literalid);
        } else {
            utils.execSQL("INSERT INTO literales (literalid, idiomaid, literal) Values (" + literalid + "," + idioma + ",'" + literal + "')");
        }
    }

    // Recupera un literal
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
            DBMSUtils.cerrarObjetoSQL(ps);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }


    // Devuelve los literales delimitado por página
    public ArrayList<String> getLiteral(int LiteralID) throws AbadiaException {
        String sSQL = "SELECT literal FROM `literales` WHERE literalid =  ? order by idiomaid";

        ArrayList<String> literales = new ArrayList<String>();
        literales.add("**Error en getLiteral para idiomaid=0, no existe**"); // Rellenar el 0 para operar directamente con el idiomaid
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, LiteralID);
            rs = ps.executeQuery();
            while (rs.next()) {
                //literal = new String;
                literales.add(rs.getString("Literal"));
            }
            return literales;
            // Ejecutarlo
        } catch (SQLException e) {
            throw new AbadiaSQLException("adLiterales. getLiterales", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    // Devuelve verdadero o falos si existe el literal
    public boolean existeLiteral(int LiteralID, int IdiomaID) throws AbadiaException {
        String sSQL = "SELECT literal FROM `literales` WHERE literalid =  ? and idiomaid = ?";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, LiteralID);
            ps.setInt(2, IdiomaID);
            rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new AbadiaSQLException("ERROR: al ejecutar existeLiteral " + LiteralID + ":" + IdiomaID, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    // Devuelve el total de registros de limitadores
    public String getIdiomaDesc(int idioma1) throws AbadiaException {
        String sSQL = "SELECT descripcion " +
                "FROM `idioma` WHERE idiomaid = ?";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idioma1);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
            return "?";
            // Ejecutarlo
        } catch (SQLException e) {
            throw new AbadiaSQLException("adLiterales. getIdiomaDesc", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Recupera un literal de un hashMap de memoria
     *
     * @param idLiteral
     * @param idIdioma
     * @return
     * @throws AbadiaException
     */
    public String getLiteralStatic(int idLiteral, int idIdioma) throws AbadiaException {
        HashMap hmLiterales;
        Context initCtx;
        String sLiteral;
        adLiterales literalesAD;
        try {
            initCtx = new InitialContext();
            //recuperar hashMap del initialcontext con todos los literales
            hmLiterales = (HashMap) initCtx.lookup(Constantes.LITERALES_CARGADOS);
            initCtx.close();
            //recuperar de la hashMap el literal correspondiente para el idioma propuesto
            sLiteral = (String) hmLiterales.get(String.valueOf(idLiteral).concat(".").concat(String.valueOf(idIdioma)));
            //si el literal recuperado es null...
            if (GenericValidator.isBlankOrNull(sLiteral)) {
                //tratramos de recuperar el literal con el idioma por defecto
                sLiteral = (String) hmLiterales.get(String.valueOf(idLiteral).concat(".").concat(String.valueOf(Constantes.IDIOMA_DEFECTO)));
                //si tampoco localizamos el literal...
                if (GenericValidator.isBlankOrNull(sLiteral)) {
                    //invocamos al método clásico para forzar la recuperación del literal de base de datos..
                    literalesAD = new adLiterales(con);
                    sLiteral = literalesAD.getLiteral(idLiteral, idIdioma);
                }
            }

            return sLiteral;

        } catch (NamingException e) {
            throw new AbadiaNamingException("adLiterales. getLiteralStatic. NamingException", e, log);
        } catch (Exception e) {
            throw new AbadiaNamingException("adLiterales. getLiteralStatic. Exception", e, log);
        }
    }

    /**
     * Carga la tabla de literales completa en un HashMap y lo mete en el initialContext
     *
     * @throws AbadiaException
     */
    public void cargarLiterales() throws AbadiaException {
        String sSQL = "Select * from literales order by idiomaid";
        ResultSet rs = null;
        PreparedStatement ps = null;
        HashMap<String, String> hmLiterales = new HashMap<String, String>();
        try {
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                hmLiterales.put(String.valueOf(rs.getInt("LITERALID")).concat(".").concat(String.valueOf(rs.getInt("IDIOMAID"))), rs.getString("LITERAL"));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adLiterales. cargarLiterales. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

        try {
            Context initCtx = new InitialContext();
            initCtx.rebind(Constantes.LITERALES_CARGADOS, hmLiterales);
            initCtx.close();

        } catch (NamingException e) {
            throw new AbadiaNamingException("adLiterales: Error en JNDI " + e.getMessage(), e, log);
        }
    }

}
