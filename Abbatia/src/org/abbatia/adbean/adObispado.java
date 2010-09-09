package org.abbatia.adbean;


import org.abbatia.actionform.BuscarAbadiaActForm;
import org.abbatia.adbean.base.adbeans;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Table;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.HTML;
import org.abbatia.utils.Utilidades;
import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class adObispado extends adbeans {
    private static Logger log = Logger.getLogger(adObispado.class.getName());

    /**
     * Instancia la clase sin obtener una nueva conexión
     *
     * @param con
     * @throws AbadiaException
     */
    public adObispado(Connection con) throws AbadiaException {
        super(con);
    }


    public ArrayList<Table> recuperarAbadiasRegion(MessageResources resource, int regionid, BuscarAbadiaActForm datosFiltro) throws AbadiaException {
        String sSQL = "Select a.abadiaid, a.nombre as NOMBRE_ABADIA, o.descripcion as NOMBRE_ORDEN, apu.total " +
                "from abadia a, orden_eclesiastica o, abadia_puntuacion_ultima apu WHERE o.ordenid = a.ordenid " +
                " and a.regionid = ? and a.abadiaid = apu.abadiaid";

        ArrayList<Table> Contents = new ArrayList<Table>();
        HTML cHTML = new HTML();
        PreparedStatement ps = null;
        ResultSet rs = null;
        adUtils utils;
        Table tabla;
        String sHTML, sOpcion, filtro = "", sURL;

        if (datosFiltro.getSearch() != null) {
            if (!datosFiltro.getSearch().equals("")) {
                filtro = " AND a.nombre like '%" + datosFiltro.getSearch() + "%' ";
                sSQL = sSQL + filtro;
            }
        }

        // Contar registros para la páginacion
        utils = new adUtils(con);
        datosFiltro.setTotal(utils.getSQL("Select Count(*) from abadia a where a.regionid=" + regionid + " " + filtro, 0));

        // Título
        sHTML = "<tr>";
        sHTML = sHTML + cHTML.getTD_TitleTable("Abadia", "obispadoOpcionesAbadia.do?opcion=" + datosFiltro.getOpcion(), 1);
        sHTML = sHTML + cHTML.getTD_TitleTable("Orden", "obispadoOpcionesAbadia.do?opcion=" + datosFiltro.getOpcion(), 2);
        sHTML = sHTML + cHTML.getTD_TitleTable("Puntuacion", "obispadoOpcionesAbadia.do?opcion=" + datosFiltro.getOpcion(), 3);
        sHTML = sHTML + "</tr>";
        tabla = new Table(0, sHTML);
        Contents.add(tabla);

        // Como ordenamos
        switch (datosFiltro.getOrdenid()) {
            case 2:
                sSQL = sSQL + " ORDER BY NOMBRE_ORDEN ";
                break;
            case 3:
                sSQL = sSQL + " ORDER BY TOTAL ";
                break;
            default:
                sSQL = sSQL + " ORDER BY NOMBRE_ABADIA ";
                break;
        }
        if (datosFiltro.getOrden() == 2) sSQL = sSQL + " desc ";

        // URL destinataria
        switch (datosFiltro.getOpcion()) {
            case 2:
                sURL = "viajar.do?abadiaid=";
                break;
            case 1:
                sURL = "listarEdificios.do?abadiaid_obispado=";
                break;
            default:
                sURL = "crear_edificio.do?abadiaid_obispado=";
                break;
        }

        sSQL = sSQL + " LIMIT " + (datosFiltro.getPagina() * Constantes.REGISTROS_PAGINA) + "," + Constantes.REGISTROS_PAGINA;

        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, regionid);
            rs = ps.executeQuery();

            while (rs.next()) {
                sOpcion = "<a href=\"" + sURL + rs.getString("ABADIAID") + //+ "&sum="+Utilidades.doMD5("ABBA"+String.valueOf(rs.getString("ABADIAID"))) +
                        "\">" + rs.getString("NOMBRE_ABADIA") + "</a>";
                sHTML = "<tr>";
                sHTML = sHTML + cHTML.getTD(HTML.ALIGN_NONE, null, sOpcion);
                sHTML = sHTML + cHTML.getTD(HTML.ALIGN_NONE, null, rs.getString("NOMBRE_ORDEN"));
                sHTML = sHTML + cHTML.getTD(HTML.ALIGN_RIGHT, null, Utilidades.redondear(rs.getDouble("TOTAL")));
                sHTML = sHTML + "</tr>";
                tabla = new Table(0, sHTML);
                Contents.add(tabla);
            }
            return Contents;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adObispado. buscarAbadias. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    // devuelve verdadero o false si existe obispo
    public boolean abadiaconObispado(Abadia abadia) throws AbadiaException {
        String sSQL = "Select m.monjeid " +
                "FROM abadia a, monje m WHERE a.abadiaid = m.abadiaid AND m.jerarquiaid = ? " +
                " and a.abadiaid = ? ";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, Constantes.JERARQUIA_OBISPO);
            ps.setLong(2, abadia.getIdDeAbadia());
            rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adObispado. abadiaconObispado. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Devuelve el identificador de un monje que ucupe el cargo recibido
     *
     * @param abadia
     * @param cargo
     * @return
     * @throws AbadiaException
     */
    public int recuperaCargoAbadia(Abadia abadia, int cargo) throws AbadiaException {
        String sSQL = "Select m.monjeid " +
                "FROM abadia a, monje m WHERE a.abadiaid = m.abadiaid AND m.jerarquiaid = ? " +
                " and a.abadiaid = ? ";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, cargo);
            ps.setLong(2, abadia.getIdDeAbadia());
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("MONJEID");
            } else return 0;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adObispado. recuperaCargoAbadia. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }


    /**
     * REsetea los datos del obispado asociados a una abadia en concreto.
     *
     * @param idAbadia
     * @throws AbadiaException
     */
    public void resetObispadoAbadia(int idAbadia) throws AbadiaException {
        String sSQL = "update obispado set monjeid = -1, abadiaid = -1, fecha_proclamacion = null, fecha_votacion = null where abadiaid = " + idAbadia;
        adUtils utils = new adUtils(con);
        utils.execSQL(sSQL);
    }

    public String recuperarAbadiaObispo(int idRegion) throws AbadiaException {
        String sSQL = "select a.nombre from abadia a, obispado o where o.abadiaid = a.abadiaid and o.regionid = " + idRegion;
        String nombreAbadia;
        adUtils utilsAD;
        try {
            utilsAD = new adUtils(con);
            nombreAbadia = utilsAD.getSQL(sSQL, "xx");
            return nombreAbadia;
        } catch (AbadiaException e) {
            throw e;
        } finally {
            //if (utilsAD != null) utilsAD.finalize();
        }
    }

}
