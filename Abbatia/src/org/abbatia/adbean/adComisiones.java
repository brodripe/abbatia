package org.abbatia.adbean;

import org.abbatia.actionform.BuscarAbadiaActForm;
import org.abbatia.adbean.base.adbeans;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Comisiones;
import org.abbatia.bean.ImpuestoRegion;
import org.abbatia.bean.Table;
import org.abbatia.core.CoreTiempo;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.CalculoComisionTransitoException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.HTML;
import org.abbatia.utils.Utilidades;
import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Benjamin
 * Date: 23-oct-2004
 * Time: 11:50:31
 * To change this template use File | Settings | File Templates.
 */
public class adComisiones extends adbeans {
    private static Logger log = Logger.getLogger(adComisiones.class.getName());

    /**
     * Instancia la clase sin obtener una nueva conexión
     *
     * @param con
     * @throws AbadiaException
     */
    public adComisiones(Connection con) throws AbadiaException {
        super(con);
    }


    public int getComision(int idRegion, String tipo) throws AbadiaSQLException {
        String sSQL = "Select " + tipo + " from comisiones where REGIONID = ?";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idRegion);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(tipo);
            } else return 0;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adComisiones. getComision. SQLException: " + sSQL, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public int getComision(long idAbadia, String tipo) throws AbadiaSQLException {
        String sSQL = "Select " + tipo + " from comisiones c, abadia a where c.REGIONID = a.REGIONID and a.ABADIAID = ?";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setLong(1, idAbadia);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(tipo);
            } else return 0;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adComisiones. getComision. SQLException: " + sSQL, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }


    public Comisiones getComision(int idRegion) throws AbadiaSQLException {
        String sSQL = "Select * from comisiones where REGIONID = ?";

        Comisiones comision = new Comisiones();

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idRegion);
            rs = ps.executeQuery();
            if (rs.next()) {
                comision.setRegionid(idRegion);
                comision.setTotal(rs.getFloat("total"));
                comision.setCancelacion(rs.getInt("cancelacion"));
                comision.setObispado(rs.getInt("obispado"));
                comision.setVenta10(rs.getInt("venta10"));
                comision.setVenta15(rs.getInt("venta15"));
                comision.setVenta5(rs.getInt("venta5"));
                comision.setTransito(rs.getInt("transito"));
            }
            return comision;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adComisiones. getComision. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public Comisiones getComisionPapado() throws AbadiaSQLException {
        String sSQL = "Select * from comisiones_papa where abadiaid = -1";

        Comisiones comision = new Comisiones();

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            if (rs.next()) {
                comision.setTotal(rs.getFloat("total"));
                comision.setCardenales(rs.getInt("impuesto_cardenales"));
                comision.setPapado(rs.getInt("impuesto_papa"));
            }
            return comision;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adComisiones. getComisionPapado. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }


    public void setComision(int idRegion, String tipo, int valor) throws AbadiaSQLException {
        String sSQL = "Select " + tipo + " from comisiones where REGIONID = ?";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idRegion);
            rs = ps.executeQuery();
            if (rs.next()) {
                actComision(idRegion, tipo, valor);
            } else crearComision(idRegion, tipo, valor);

        } catch (SQLException e) {
            throw new AbadiaSQLException("adComisiones. setComision. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public void setComision(Comisiones comision) throws AbadiaSQLException {
        String sSQL = "UPDATE comisiones set cancelacion=?, obispado=?, venta5=?, venta10=?, venta15=?, transito=?  where REGIONID = ?";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, comision.getCancelacion());
            ps.setInt(2, comision.getObispado());
            ps.setInt(3, comision.getVenta5());
            ps.setInt(4, comision.getVenta10());
            ps.setInt(5, comision.getVenta15());
            ps.setInt(6, comision.getTransito());
            ps.setInt(7, comision.getRegionid());
            ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adComisiones. setComision. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public void setComisionPapado(Comisiones comision) throws AbadiaSQLException {
        String sSQL = "UPDATE comisiones_papa set impuesto_papa=?, impuesto_cardenales=? where abadiaid = -1";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, comision.getPapado());
            ps.setInt(2, comision.getCardenales());
            ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adComisiones. setComision. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }


    public void crearComision(int idRegion, String tipo, int valor) throws AbadiaSQLException {
        String sSQL = "insert into comisiones (REGIONID, " + tipo + ") values (?, ?)";

        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idRegion);
            ps.setInt(2, valor);
            ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adComisiones. crearComision. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public void actComision(int idRegion, String tipo, int valor) throws AbadiaSQLException {
        String sSQL = "update comision set " + tipo + " = ? where RETIONID = ? ";

        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, valor);
            ps.setInt(2, idRegion);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adComisiones. actComision. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public void sumarImporteComision(int idRegion, int idAbadia, String fecha, double importe) throws AbadiaException {
        String sSQL = "select * from comisiones_historico where REGIONID=? and ABADIAID=? and FECHA=?";
        String sSQLU = "update comisiones_historico set IMPORTE = IMPORTE + ? where REGIONID = ? and ABADIAID=? and FECHA=? ";
        String sSQLI = "insert into comisiones_historico (REGIONID, ABADIAID, FECHA, IMPORTE, PROCESADO) values (?,?,?,?,0) ";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = getConexion().prepareStatement(sSQL);
            ps.setInt(1, idRegion);
            ps.setInt(2, idAbadia);
            ps.setString(3, Utilidades.formatStringToDB(fecha));
            rs = ps.executeQuery();
            if (rs.next()) {
                ps = getConexion().prepareStatement(sSQLU);
                ps.setDouble(1, importe);
                ps.setInt(2, idRegion);
                ps.setInt(3, idAbadia);
                ps.setString(4, Utilidades.formatStringToDB(fecha));
                ps.executeUpdate();
            } else {
                ps = getConexion().prepareStatement(sSQLI);
                ps.setInt(1, idRegion);
                ps.setInt(2, idAbadia);
                ps.setString(3, Utilidades.formatStringToDB(fecha));
                ps.setDouble(4, importe);
                ps.execute();
            }

        } catch (SQLException e) {
            throw new AbadiaSQLException("adComisiones. sumarImporteComision. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public void sumarImporteComision(int idAbadia, String fecha, double importe) throws AbadiaException {
        String sSQL = "select * from comisiones_historico where REGIONID=? and ABADIAID=? and FECHA=?";
        String sSQLU = "update comisiones_historico set IMPORTE = IMPORTE + ? where REGIONID = ? and ABADIAID=? and FECHA=? ";
        String sSQLI = "insert into comisiones_historico (REGIONID, ABADIAID, FECHA, IMPORTE, PROCESADO) values (?,?,?,?,0) ";
        adUtils utils = new adUtils(con);
        int idRegion = utils.getSQL("select regionid from abadia where abadiaid = " + idAbadia, 0);
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = getConexion().prepareStatement(sSQL);
            ps.setInt(1, idRegion);
            ps.setInt(2, idAbadia);
            ps.setString(3, Utilidades.formatStringToDB(fecha));
            rs = ps.executeQuery();
            if (rs.next()) {
                ps = getConexion().prepareStatement(sSQLU);
                ps.setDouble(1, importe);
                ps.setInt(2, idRegion);
                ps.setInt(3, idAbadia);
                ps.setString(4, Utilidades.formatStringToDB(fecha));
                ps.executeUpdate();
            } else {
                ps = getConexion().prepareStatement(sSQLI);
                ps.setInt(1, idRegion);
                ps.setInt(2, idAbadia);
                ps.setString(3, Utilidades.formatStringToDB(fecha));
                ps.setDouble(4, importe);
                ps.execute();
            }

        } catch (SQLException e) {
            throw new AbadiaSQLException("adComisiones. sumarImporteComision. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Gestiona el pago de comisión de tránsito
     *
     * @param idAbadia
     * @param idRegion
     * @param fecha
     * @param importe
     * @throws AbadiaException
     */
    public void sumarImporteComisionTransito(int idAbadia, int idRegion, String fecha, double importe) throws AbadiaException {
        String sSQL = "select * from comisiones_historico where REGIONID=? and ABADIAID=? and FECHA=?";
        String sSQLU = "update comisiones_historico set IMPORTE = IMPORTE + ? where REGIONID = ? and ABADIAID=? and FECHA=? ";
        String sSQLI = "insert into comisiones_historico (REGIONID, ABADIAID, FECHA, IMPORTE, PROCESADO) values (?,?,?,?,0) ";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idRegion);
            ps.setInt(2, idAbadia);
            ps.setString(3, Utilidades.formatStringToDB(fecha));
            rs = ps.executeQuery();
            if (rs.next()) {
                ps = con.prepareStatement(sSQLU);
                ps.setDouble(1, importe);
                ps.setInt(2, idRegion);
                ps.setInt(3, idAbadia);
                ps.setString(4, Utilidades.formatStringToDB(fecha));
                ps.executeUpdate();
            } else {
                ps = con.prepareStatement(sSQLI);
                ps.setInt(1, idRegion);
                ps.setInt(2, idAbadia);
                ps.setString(3, Utilidades.formatStringToDB(fecha));
                ps.setDouble(4, importe);
                ps.execute();
            }

        } catch (SQLException e) {
            throw new AbadiaSQLException("adComisiones. sumarImporteComisionTransito. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    // devuelve verdadero o false si existe obispo
    public double recaudacion(int regionid) throws AbadiaException {
        String sSQL = "Select total FROM comisiones WHERE regionid = ? ";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, regionid);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
            return 0;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adComisiones. recaudacion. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    // devuelve verdadero o false si existe obispo
    public void actualizaRecaudacion(int regionid, double valor) throws AbadiaException {
        String sSQL = "UPDATE comisiones SET total = total + (?) WHERE regionid = ? ";

        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setDouble(1, valor);
            ps.setInt(2, regionid);
            ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adComisiones. actualzaRecaudacion. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    // añadir un movimiento en la tabla de acciones de comision
    /*   Tipos:
       1- Construcción de un edificio a otra abbatia
    */
    public void addMovima(int regionid, int jerarquiaid, long de_abadiaid, long a_abadiaid, int tipo, double importe, int param1, int param2) throws AbadiaException {
        String sSQL = "INSERT INTO `comisiones_movima` " +
                "(`regionid`, fecha, `jerarquiaid`, `de_abadiaid`, `a_abadiaid`, `tipo`, `importe`, `param1`, `param2`) " +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) ";

        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            int Param = 1;
            ps.setInt(Param++, regionid);
            ps.setString(Param++, CoreTiempo.getTiempoAbadiaStringConHoras());
            ps.setInt(Param++, jerarquiaid);
            ps.setLong(Param++, de_abadiaid);
            ps.setLong(Param++, a_abadiaid);
            ps.setInt(Param++, tipo);
            ps.setDouble(Param++, importe);
            ps.setInt(Param++, param1);
            ps.setInt(Param, param2);
            ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adComisiones. addMovima. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /*
    Tipos de mensaje:
      obispado.mostrar.comisiones.tipo1=Se ha costeado toda la construcción de: %s
      obispado.mostrar.comisiones.tipo2=Se ha costeado la ampliacion de: %s
      obispado.mostrar.comisiones.tipo3=La recogida de impuestos de la región asciende a %s
    */
    public ArrayList<Table> recuperarMovima(MessageResources resource, int regionid, int idiomaid, BuscarAbadiaActForm datosFiltro) throws AbadiaException {
        String sSQL = "SELECT cm.*, a1.nombre de_nombre, a2.nombre a_nombre " +
                "FROM `comisiones_movima` cm, abadia a1, abadia a2 " +
                "where cm.de_abadiaid = a1.abadiaid and cm.a_abadiaid = a2.abadiaid and cm.regionid = ? ";

        ArrayList<Table> Contents = new ArrayList<Table>();
        HTML cHTML = new HTML();
        PreparedStatement ps = null;
        ResultSet rs = null;
        adUtils utils;
        Table tabla;

        String sHTML, sDE, sTIPO;

        try {
            // Contar registros para la páginacion
            utils = new adUtils(con);
            datosFiltro.setTotal(utils.getSQL("Select Count(*) from comisiones_movima a where a.regionid=" + regionid, 0));

            // Título
            sHTML = "<tr>";
            sHTML = sHTML + cHTML.getTD_TitleTable(resource.getMessage("obispado.mostrar.comisiones.fecha"), "", 0);
            sHTML = sHTML + cHTML.getTD_TitleTable(resource.getMessage("obispado.mostrar.comisiones.origen"), "", 0);
            sHTML = sHTML + cHTML.getTD_TitleTable(resource.getMessage("obispado.mostrar.comisiones.destino"), "", 0);
            sHTML = sHTML + cHTML.getTD_TitleTable(resource.getMessage("obispado.mostrar.comisiones.descripcion"), "", 0);
            sHTML = sHTML + cHTML.getTD_TitleTable(resource.getMessage("obispado.mostrar.comisiones.importe"), "", 0);
            sHTML = sHTML + "</tr>";
            tabla = new Table(0, sHTML);
            Contents.add(tabla);

            // Como ordenamos
            switch (datosFiltro.getOrdenid()) {
                default:
                    sSQL = sSQL + " ORDER BY fecha ";
                    break;
            }
            if (datosFiltro.getOrden() != 2) sSQL = sSQL + " desc ";

            sSQL = sSQL + " LIMIT " + (datosFiltro.getPagina() * Constantes.REGISTROS_PAGINA) + "," + Constantes.REGISTROS_PAGINA;

            ps = con.prepareStatement(sSQL);
            ps.setInt(1, regionid);
            rs = ps.executeQuery();
            //DatosAbadia abbatia = null;
            while (rs.next()) {
                // Quien ha realizado la acción
                switch (rs.getInt("jerarquiaid")) {
                    case 3:
                        sDE = resource.getMessage("obispado.mostrar.comisiones.obispode") + " ";
                        break;
                    default:
                        sDE = "";
                        break;
                }
                // Descripción según el tipo... no se guarda en BD porque deberiamos de guardarlo en multiidioma
                switch (rs.getInt("tipo")) {
                    // Se ha costeado toda la construcción de: %s
                    case 1:
                        sTIPO = resource.getMessage("obispado.mostrar.comisiones.tipo1");
                        utils = new adUtils(con);
                        sTIPO = Utilidades.Format(sTIPO, utils.getSQL("select l.literal from edificio_tipo et, literales l " +
                                "where et.nombre = l.literalid and idiomaid = " + idiomaid + " and tipoedificioid = " + rs.getString("param1"), "error"));
                        break;
                        // Se ha costeado la ampliacion de: %s
                    case 2:
                        sTIPO = resource.getMessage("obispado.mostrar.comisiones.tipo2");
                        utils = new adUtils(con);
                        sTIPO = Utilidades.Format(sTIPO, utils.getSQL("select l.literal from edificio_tipo et, literales l " +
                                "where et.nombre = l.literalid and idiomaid = " + idiomaid + " and tipoedificioid = " + rs.getString("param1"), "error"));
                        break;
                        // La recogida de impuestos de la región asciende a %s
                    case 3:
                        sTIPO = resource.getMessage("obispado.mostrar.comisiones.tipo3");
                        sTIPO = Utilidades.Format(sTIPO, Utilidades.redondear(rs.getDouble("param1")));
                        break;
                    default:
                        sTIPO = "";
                        break;
                }
                sHTML = "<tr>";
                sHTML = sHTML + cHTML.getTD(HTML.ALIGN_NONE, null, rs.getString("Fecha"));
                sHTML = sHTML + cHTML.getTD(HTML.ALIGN_NONE, null, sDE + rs.getString("de_nombre"));
                sHTML = sHTML + cHTML.getTD(HTML.ALIGN_NONE, null, rs.getString("a_nombre"));
                sHTML = sHTML + cHTML.getTD(HTML.ALIGN_NONE, null, sTIPO);
                sHTML = sHTML + cHTML.getTD(HTML.ALIGN_RIGHT, null, Utilidades.redondear(rs.getDouble("importe")));
                sHTML = sHTML + "</tr>";
                tabla = new Table(0, sHTML);
                Contents.add(tabla);
            }
            return Contents;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adComision. recuperarMovima. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public ArrayList<Table> recuperarContribuciones(Abadia abadia, int pagina, int orden, int ordenid, Point total, MessageResources resource, String Param) throws AbadiaException {
        //String sSQL = "Select a.nombre, ch.fecha, ch.importe FROM comisiones_historico ch, abbatia a WHERE ch.regionid = ? and ch.abadiaid=a.abadiaid ";
        String sSQL = "SELECT  a.nombre, ch.fecha, ch.importe ";
        String sSQLFrom = "FROM comisiones_historico ch, abadia a WHERE ch.regionid = " + abadia.getIdDeRegion() + " and ch.abadiaid=a.abadiaid and ch.procesado=1 ";

        switch (ordenid) {
            case 2:
                sSQL = sSQL + sSQLFrom + " ORDER BY ch.fecha ";
                break;
            default:
                sSQL = sSQL + sSQLFrom + " ORDER BY a.nombre ";
                break;
        }
        if (orden == 2) sSQL = sSQL + " desc";

        adUtils utils = new adUtils(con);
        total.x = utils.getSQL("Select count(*) " + sSQLFrom, 0);

        String sHTML = "";
        int n = 1;
        HTML cHTML = new HTML();
        ArrayList<Table> recaudaciones = new ArrayList<Table>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            Table recaudacion;

            sSQL = sSQL + " LIMIT " + (pagina * Constantes.REGISTROS_PAGINA) + "," + Constantes.REGISTROS_PAGINA;

            sHTML = cHTML.getTD_TitleTable(resource.getMessage("obispado.impuestos.historia.abbatia"), Param, 1);
            sHTML += cHTML.getTD_TitleTable(resource.getMessage("obispado.impuestos.historia.fechapago"), Param, 2);
            sHTML += cHTML.getTD_TitleTable(resource.getMessage("obispado.impuestos.historia.importe"), "", 0);
            recaudacion = new Table(0, "<tr>" + sHTML + "</tr>");
            recaudaciones.add(recaudacion);

            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                sHTML = cHTML.getTD(HTML.ALIGN_NONE, null, rs.getString("nombre"));
                sHTML += cHTML.getTD(HTML.ALIGN_CENTER, null, rs.getString("fecha"));
                sHTML += cHTML.getTD(HTML.ALIGN_RIGHT, null, Utilidades.redondear(rs.getDouble("importe")));
                recaudacion = new Table(0, cHTML.getTR(n++, sHTML));
                recaudaciones.add(recaudacion);
            }
            return recaudaciones;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adComisiones. recuperarContribuciones. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public ArrayList<Table> recuperarContribucionesRegion(Abadia abadia, int regionid, int pagina, int orden, int ordenid, Point total, MessageResources resource, String Param) throws AbadiaException {
        //String sSQL = "Select a.nombre, ch.fecha, ch.importe FROM comisiones_historico ch, abadia a WHERE ch.regionid = ? and ch.abadiaid=a.abadiaid ";
        String sSQL = "SELECT ch.regionid, ch.abadiaid, ch.fecha, sum(importe) importe, max(descripcion) region, max(nombre) nombre ";
        String sSQLFrom = "FROM `comisiones_historico` ch, region r, abadia a " +
                "WHERE ch.regionid = r.regionid and a.abadiaid = ch.abadiaid and procesado = 1 ";
        if (regionid != 0) sSQLFrom += " and ch.regionid = " + regionid;

        adUtils utils = new adUtils(con);
        total.x = utils.getSQL("Select count(*) " + sSQLFrom, 0);

        sSQLFrom += " GROUP BY ch.regionid, ch.abadiaid, ch.fecha ";

        switch (ordenid) {
            case 3:
                sSQL += sSQLFrom + " ORDER BY region ";
                if (orden == 2) sSQL += " desc";
                sSQL += ", ch.fecha desc";
                break;
            case 2:
                sSQL += sSQLFrom + " ORDER BY ch.fecha ";
                if (orden == 2) sSQL += " desc";
                sSQL += ", a.nombre";
                break;
            default:
                sSQL += sSQLFrom + " ORDER BY a.nombre ";
                if (orden == 2) sSQL += " desc";
                sSQL += ", ch.fecha desc";
                break;
        }

        String sHTML = "";
        int n = 1;
        HTML cHTML = new HTML();
        ArrayList<Table> recaudaciones = new ArrayList<Table>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            Table recaudacion;

            sSQL = sSQL + " LIMIT " + (pagina * Constantes.REGISTROS_PAGINA) + "," + Constantes.REGISTROS_PAGINA;

            sHTML = cHTML.getTD_TitleTable(resource.getMessage("obispado.impuestos.historia.fechapago"), Param, 2);
            if (regionid == 0)
                sHTML += cHTML.getTD_TitleTable(resource.getMessage("buscar.abbatia.nombreregion"), Param, 3);
            sHTML += cHTML.getTD_TitleTable(resource.getMessage("obispado.impuestos.historia.abbatia"), Param, 1);
            sHTML += cHTML.getTD_TitleTable(resource.getMessage("obispado.impuestos.historia.importe"), Param, 0);
            recaudacion = new Table(0, "<tr>" + sHTML + "</tr>");
            recaudaciones.add(recaudacion);

            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                sHTML = cHTML.getTD(HTML.ALIGN_NONE, null, rs.getString("fecha"));
                if (regionid == 0) sHTML += cHTML.getTD(HTML.ALIGN_CENTER, null, rs.getString("region"));
                sHTML += cHTML.getTD(HTML.ALIGN_CENTER, null, rs.getString("nombre"));
                sHTML += cHTML.getTD(HTML.ALIGN_RIGHT, null, Utilidades.redondear(rs.getDouble("importe")));
                recaudacion = new Table(0, cHTML.getTR(n++, sHTML));
                recaudaciones.add(recaudacion);
            }
            return recaudaciones;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adComisiones. recuperarContribuciones. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public ArrayList<Table> recuperarContribucionesComision(Abadia abadia, int regionid, int pagina, int orden, int ordenid, Point total, MessageResources resource, String Param) throws AbadiaException {
        String sSQL, sSQLFrom;
        if (regionid == -1) {
            sSQL = "SELECT ch.fecha, sum(recaudado) recaudado, sum(obispo) obispo, sum(cardenales) cardenales, sum(papa) papa ";
            sSQLFrom = " FROM `comisiones_historico_regional` ch";
            sSQLFrom += " GROUP BY ch.fecha";
        } else {
            sSQL = "SELECT ch.*, r.descripcion ";
            sSQLFrom = "FROM `comisiones_historico_regional` ch, region r WHERE ch.regionid = r.regionid ";
            if (regionid != 0) sSQLFrom += "and ch.regionid = " + regionid;
        }
        switch (ordenid) {
            case 2:
                sSQL += sSQLFrom + " ORDER BY ch.fecha ";
                if (orden == 2) sSQL += " desc";
                break;
            default:
                sSQL += sSQLFrom + " ORDER BY r.descripcion ";
                if (orden == 2) sSQL += " desc";
                break;
        }

        adUtils utils = new adUtils(con);
        total.x = utils.getSQL("Select count(*) " + sSQLFrom, 0);

        String sHTML = "";
        int n = 1;
        HTML cHTML = new HTML();
        ArrayList<Table> recaudaciones = new ArrayList<Table>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            Table recaudacion;

            sSQL = sSQL + " LIMIT " + (pagina * Constantes.REGISTROS_PAGINA) + "," + Constantes.REGISTROS_PAGINA;

            sHTML = cHTML.getTD_TitleTable(resource.getMessage("obispado.mostrar.comisiones.fecha"), Param, 2);
            if (regionid == 0)
                sHTML += cHTML.getTD_TitleTable(resource.getMessage("buscar.abbatia.nombreregion"), Param, 1);
            sHTML += cHTML.getTD_TitleTable(resource.getMessage("diplomacia.jerarquia.comision.recaudado"), "", 0);
            sHTML += cHTML.getTD_TitleTable(resource.getMessage("diplomacia.jerarquia.comision.obispado"), "", 0);
            sHTML += cHTML.getTD_TitleTable(resource.getMessage("diplomacia.jerarquia.comision.cardenales"), "", 0);
            sHTML += cHTML.getTD_TitleTable(resource.getMessage("diplomacia.jerarquia.comision.papado"), "", 0);
            recaudacion = new Table(0, "<tr>" + sHTML + "</tr>");
            recaudaciones.add(recaudacion);

            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                sHTML = cHTML.getTD(HTML.ALIGN_NONE, null, rs.getString("fecha"));
                if (regionid == 0) sHTML += cHTML.getTD(HTML.ALIGN_CENTER, null, rs.getString("descripcion"));
                sHTML += cHTML.getTD(HTML.ALIGN_RIGHT, null, Utilidades.redondear(rs.getDouble("recaudado")));
                sHTML += cHTML.getTD(HTML.ALIGN_RIGHT, null, Utilidades.redondear(rs.getDouble("obispo")));
                sHTML += cHTML.getTD(HTML.ALIGN_RIGHT, null, Utilidades.redondear(rs.getDouble("cardenales")));
                sHTML += cHTML.getTD(HTML.ALIGN_RIGHT, null, Utilidades.redondear(rs.getDouble("papa")));
                recaudacion = new Table(0, cHTML.getTR(n++, sHTML));
                recaudaciones.add(recaudacion);
            }
            return recaudaciones;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adComisiones. recuperarContribuciones. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Devuelve un {@link ArrayList} de objetos {@link ImpuestoRegion} con los impuestos que tienen establecidos
     * los obispos de las regiones de transito.
     *
     * @param idRegionOri
     * @param idRegionDest
     * @return
     * @throws AbadiaException
     */
    public ArrayList<ImpuestoRegion> getComisionesTransito(int idRegionOri, int idRegionDest) throws AbadiaException {
        /*
        X = region origen
        Y = region destino.
        dias_totales  = Select region_distancia from regionid_origen = x and regionid_destino = Y

        while (region_destino != Y);
        Region a pasar = Select region_destino from region_distancia where regionid_origen = X and
                                          region_destino in (select region_origen from region_distancia where distancia_campo < dias_totales and regionid_destino =Y)
                                          order by region_destino asc limit 1;

        dias_totales = dias_totales - 12
        X = region a pasar
        */
        String sSQL = "Select rd.regionid_destino, r.descripcion, rc.transito,rd.distancia_campo " +
                " from region_distancia rd, region r, comisiones rc " +
                " where rd.regionid_origen = ? and rd.regionid_destino = r.regionid and r.regionid = rc.regionid and rd.distancia_campo > 12 and" +
                " rd.regionid_destino in (select regionid_origen from region_distancia where distancia_campo < ? and regionid_destino = ?) " +
                " order by rd.distancia_campo asc, rc.transito asc limit 1 ";
        ArrayList<ImpuestoRegion> alImpuestoRegion = new ArrayList<ImpuestoRegion>();
        ImpuestoRegion impuestoRegion;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int diasTotales;

        try {
            ps = con.prepareStatement("Select distancia_campo from region_distancia where regionid_origen = " + idRegionOri + " and regionid_destino = " + idRegionDest);
            rs = ps.executeQuery();
            if (rs.next()) {
                diasTotales = rs.getInt("distancia_campo");
            } else return null;
            rs.close();
            ps.close();

            while (idRegionOri != idRegionDest) {
                impuestoRegion = new ImpuestoRegion();
                ps = con.prepareStatement(sSQL);
                ps.setInt(1, idRegionOri);
                ps.setInt(2, diasTotales);
                ps.setInt(3, idRegionDest);

                rs = ps.executeQuery();
                if (rs.next()) {
                    impuestoRegion.setIdRegion(rs.getInt("regionid_destino"));
                    impuestoRegion.setNombreRegion(rs.getString("descripcion"));
                    impuestoRegion.setValorImpuesto(rs.getInt("transito"));
                    alImpuestoRegion.add(impuestoRegion);
                    idRegionOri = impuestoRegion.getIdRegion();
                    ps = con.prepareStatement("Select distancia_campo from region_distancia where regionid_origen = " + idRegionOri + " and regionid_destino = " + idRegionDest);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        diasTotales = rs.getInt("distancia_campo");
                    } else return null;
                    rs.close();
                    ps.close();

                } else
                    throw new CalculoComisionTransitoException("No se encuentra ruta entre región: " + idRegionOri + " y " + idRegionDest + " para " + diasTotales + " dias", log);
            }

            return alImpuestoRegion;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adComisiones. getComisionesTransito. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }
}
