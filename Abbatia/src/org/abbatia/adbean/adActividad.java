package org.abbatia.adbean;

import org.abbatia.adbean.base.adbeans;
import org.abbatia.bean.Actividad;
import org.abbatia.bean.Table;
import org.abbatia.core.CoreTiempo;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class adActividad extends adbeans {
    private static Logger log = Logger.getLogger(adActividad.class.getName());

    /**
     * Instancia la clase sin obtener una nueva conexión
     *
     * @param con
     * @throws AbadiaException
     */
    public adActividad(Connection con) throws AbadiaException {
        super(con);
    }


    /**
     * Crea una nueva entrada de actividad para un monje en un periodo determinado
     *
     * @param idMonje
     * @param idPeriodo
     * @param nivel
     * @throws AbadiaSQLException
     */
    public void crearActividad(long idMonje, int idPeriodo, int nivel) throws AbadiaSQLException {
        String sSQL = "Insert into actividad (MONJEID, PERIODOID, ACTIVIDADID, NIVEL) values (?, ?, ?, 10)";
        PreparedStatement ps = null;
        try {
            //creo un objeto de tipo PreparedStatement sobre el que se
            //ejecutara la consulta
            ps = con.prepareStatement(sSQL);
            //asigno los valores
            int parNo = 0;
            ps.setLong(parNo++, idMonje);
            ps.setInt(parNo++, idPeriodo);
            ps.setInt(parNo, nivel);
            // Ejecutarlo
            ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adActividad. crearActividad. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }


    //
    //elimina un objeto Actividad de la base de datos
    //a partir de un objeto Actividad devuelve verdadero si no ha ocurrido un error
    public boolean eliminarActividad(Actividad actividad) throws AbadiaSQLException {
        return eliminarActividad(actividad.getIdDeActividad());
    }

    //Elimina un objeto Actividad de la base de datos
    //a partir de la clave del Actividad
    public boolean eliminarActividad(long idDeActividadTmp) throws AbadiaSQLException {
        String sSQL = "Delete From actividad Where `ACTIVIDADID` = ?";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave del monje
            int parNo = 1;
            ps.setLong(parNo, idDeActividadTmp);
            // Ejecutarlo
            return ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adActividad. crearActividad. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public ArrayList<Table> getListaTipos(int idiomaid, int idJerarquia) throws AbadiaException {
        String sSQL = "SELECT at.actividadid, l.literal FROM actividad_tipo at, literales l, actividad_tipo_asignable ata " +
                "WHERE at.literalid = l.literalid AND l.idiomaid = ? and at.actividadid = ata.actividadid and ata.jerarquiaid = ? " +
                " ORDER BY literal";

        ArrayList<Table> Actividades = new ArrayList<Table>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idiomaid);
            ps.setInt(2, idJerarquia);
            rs = ps.executeQuery();
            Table actividad;
            while (rs.next()) {
                actividad = new Table(rs.getInt("ACTIVIDADID"), rs.getString("LITERAL"));
                Actividades.add(actividad);
            }
            return Actividades;
            // Ejecutarlo
        } catch (SQLException e) {
            //log.error("adActividad. getListaTipos", e);
            throw new AbadiaSQLException("adActividad. getListaTipos", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public void eliminarActividadMonjesAbadia(long idAbadia) throws AbadiaSQLException {
        String sSQL = "Delete From actividad Where MONJEID IN (select MONJEID from monje where ABADIAID=?)";
        //String sSQL = "Delete actividad.* From actividad a, monje m Where a.MONJEID = m.MONJEID AND m.ABADIAID=?";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave del monje
            int parNo = 1;
            ps.setLong(parNo, idAbadia);
            // Ejecutarlo
            ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adActividad. eliminarActividadMonjesAbadia. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /* Devuelve las actividades agrupadas por abbatia, actividad, monje
    */
    public ArrayList<Table> getActividadesAgrupados(long idabadia, short idiomaid) throws AbadiaException {
        String sSQL = "SELECT a.actividadid, m.monjeid, a.periodoid, max(l.literal) literal, max(ap.descripcion) descripcion, max(nombre) nombre " +
                "FROM `actividad` a, actividad_periodo ap, monje m, actividad_tipo at, literales l " +
                "where a.monjeid = m.monjeid and m.estado = 0 and at.asignable=1 and ap.periodoid = a.periodoid and m.abadiaid = ? " +
                "  and at.actividadid= a.actividadid and l.literalid = at.literalid and l.idiomaid = ? " +
                "group by a.actividadid, m.monjeid, a.periodoid " +
                "order by literal, nombre, monjeid, periodoid ";

        adUtils utils;

        String sHTML = "", desc = "";
        int lastactividad = -1, lastmonjeid = -1, tareas = 0, monjes = 0;
        ArrayList<Table> Actividades = new ArrayList<Table>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // literales
            adLiterales literal = new adLiterales(con);
            String msg1 = literal.getLiteral(550, idiomaid);   // Tareas con
            String msg2 = literal.getLiteral(551, idiomaid);   // Tarea con
            String msg3 = literal.getLiteral(552, idiomaid);   // monjes
            String msg4 = literal.getLiteral(553, idiomaid);   // monje

            ps = con.prepareStatement(sSQL);
            ps.setLong(1, idabadia);
            ps.setShort(2, idiomaid);
            rs = ps.executeQuery();
            Table actividad;
            sHTML = "<table border=\"0\" width=\"90%\">";
            actividad = new Table(0, sHTML);
            Actividades.add(actividad);
            while (rs.next()) {
                sHTML = "";
                // Cambio de actividad
                if (lastactividad != rs.getInt("actividadid")) {
                    if (lastmonjeid != -1) sHTML = sHTML + desc + " )";
                    if (lastactividad != -1) sHTML = sHTML + "</td></tr>";
                    sHTML = sHTML + "<tr><td colspan=2>";
                    if (lastactividad != -1) sHTML = sHTML + "<br/><br/>";
                    lastactividad = rs.getInt("actividadid");
                    sHTML = sHTML + "&nbsp;<b><u>" + rs.getString("literal") + "</u></b> ";
                    utils = new adUtils(con);
                    tareas = utils.getSQL("SELECT count(a.monjeid) FROM `actividad` a, monje m where a.monjeid = m.monjeid and m.estado = 0 and m.abadiaid = " + idabadia + " and actividadid = " + lastactividad, 0);
                    monjes = utils.getSQL("SELECT count(DISTINCT a.monjeid)  FROM `actividad` a, monje m where a.monjeid = m.monjeid and m.estado = 0 and m.abadiaid = " + idabadia + " and actividadid = " + lastactividad, 0);
                    sHTML = sHTML + tareas + " ";
                    if (tareas == 1) sHTML = sHTML + msg2;
                    else sHTML = sHTML + msg1;
                    sHTML = sHTML + " " + monjes + " ";
                    if (monjes == 1) sHTML = sHTML + msg4;
                    else sHTML = sHTML + msg3;
                    sHTML = sHTML + "</td></tr><td width=\"80\">";
                    switch (lastactividad) {
                        case 0:
                            sHTML = sHTML + "<img src=\"images/iconos/monje_dormir.gif\">";
                            break;
                        case 1:
                            sHTML = sHTML + "<img src=\"images/iconos/monje_rezar.gif\">";
                            break;
                        case 2:
                            sHTML = sHTML + "<img src=\"images/iconos/monje_labrador.gif\">";
                            break;
                        case 3:
                            sHTML = sHTML + "<img src=\"images/iconos/monje_cortandolena.gif\">";
                            break;
                        case 4:
                            sHTML = sHTML + "<img src=\"images/iconos/monje_pescando.gif\">";
                            break;
                        case 5:
                            sHTML = sHTML + "";
                            break;
                        case 6:
                            sHTML = sHTML + "<img src=\"images/iconos/monje_curar.gif\">";
                            break;
                        case 7:
                            sHTML = sHTML + "";
                            break;
                        case 8:
                            sHTML = sHTML + "<img src=\"images/iconos/monje_copiando.gif\">";
                            break;
                        case 9:
                            sHTML = sHTML + "<img src=\"images/iconos/monje_ensenar.gif\">";
                            break;
                        case 10:
                            sHTML = sHTML + "<img src=\"images/iconos/monje_abad.gif\">";
                            break;
                        case 11:
                            sHTML = sHTML + "<img src=\"images/iconos/monje_ganadero.gif\">";
                            break;
                        case 12:
                            sHTML = sHTML + "<img src=\"images/iconos/monje_comiendo.gif\">";
                            break;
                        case 13:
                            sHTML = sHTML + "<img src=\"images/iconos/monje_molino.gif\">";
                            break;
                        case 14:
                            sHTML = sHTML + "<img src=\"images/iconos/monje_elaborar_comida.gif\">";
                            break;
                        case 15:
                            sHTML = sHTML + "<img src=\"images/iconos/monje_elaborar_recurso.gif\">";
                            break;
                        case 16:
                            sHTML = sHTML + "<img src=\"images/iconos/monje_elaborar_costura.gif\">";
                            break;
                        case 17:
                            sHTML = sHTML + "<img src=\"images/iconos/monje_elaborar_artesania.gif\">";
                            break;
                        case 18:
                            sHTML = sHTML + "<img src=\"images/iconos/monje_aprender.gif\">";
                            break;
                        case 19:
                            sHTML = sHTML + "<img src=\"images/iconos/monje_restaurar.gif\">";
                            break;

                    }
                    sHTML = sHTML + "</td><td valign=\"top\">";
                    lastmonjeid = -1;
                }
                // Cambio de monje
                if (lastmonjeid != rs.getInt("monjeid")) {
                    if (lastmonjeid == -1)
                        sHTML = sHTML + "<p>";
                    else
                        sHTML = sHTML + desc + " )<br>";
                    sHTML = sHTML + "<a href=\"mostrarMonje.do?clave=" + rs.getInt("monjeid") + "\">" + rs.getString("nombre") + "</a> (";
                    lastmonjeid = rs.getInt("monjeid");
                    desc = "";
                }
                if (!desc.equals("")) desc = desc + ",";
                desc = desc + " " + rs.getString("descripcion");
                actividad = new Table(0, sHTML);
                Actividades.add(actividad);
            }
            // último elemento
            if (lastmonjeid != -1) {
                sHTML = sHTML + desc + " )</td></tr></table>";
                actividad = new Table(0, sHTML);
                Actividades.add(actividad);
            }
            return Actividades;
            // Ejecutarlo
        } catch (SQLException e) {
            throw new AbadiaSQLException("adActividad. getActividadesAgrupados", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }


    /* Devuelve las actividades agrupadas por abbatia, actividad, monje
    */
    public ArrayList<Table> getActividadesMonjesAhoraMismo(long idabadia, short idiomaid, MessageResources resource) throws AbadiaException {
        String sSQL = "SELECT a.actividadid, m.monjeid, l.literal, nombre, apellido1  " +
                "FROM `actividad` a, monje m, actividad_tipo at, literales l " +
                "where a.monjeid = m.monjeid and  ( m.estado = 0 or m.estado = 2)  and a.periodoid = ? and m.abadiaid = ? " +
                "  and at.actividadid= a.actividadid and l.literalid = at.literalid_gerundio and l.idiomaid = ? " +
                "order by literal, nombre, apellido1 ";

        String sSQLDormir = "SELECT 0 as actividadid, m.monjeid, l.literal, nombre, apellido1 " +
                "FROM monje m, actividad_tipo at, literales l " +
                "where m.monjeid and  ( m.estado = 0 or m.estado = 2)  and m.abadiaid = ? " +
                "  and at.actividadid= 0 and l.literalid = at.literalid_gerundio and l.idiomaid = ? " +
                "order by literal, nombre, apellido1 ";

        String sSQLViajes = "SELECT m.monjeid, nombre, apellido1, literal, estado\n" +
                "            FROM monje m, literales l\n" +
                "            where (m.estado = 3  or m.estado = 4) and m.abadiaid=? and l.idiomaid = ? and l.literalid = 1097 + m.estado\n" +
                "            order by nombre, apellido1";

        adUtils utils;

        String sHTML = "";
        int lastactividad = -1, idperiodo, monjes = 0;
        ArrayList<Table> Actividades = new ArrayList<Table>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // literales
            adLiterales literal = new adLiterales(con);
            String msg1 = literal.getLiteral(552, idiomaid);   // monjes
            String msg2 = literal.getLiteral(553, idiomaid);   // monje

            idperiodo = CoreTiempo.getPeriodoActual();

            if (idperiodo == Constantes.PERIODO_NADA) {
                ps = con.prepareStatement(sSQLDormir);
                ps.setLong(1, idabadia);
                ps.setShort(2, idiomaid);
            } else {
                ps = con.prepareStatement(sSQL);
                ps.setInt(1, idperiodo);
                ps.setLong(2, idabadia);
                ps.setShort(3, idiomaid);
            }
            rs = ps.executeQuery();
            Table actividad;
            sHTML = "<table border=\"0\" width=\"90%\">";
            actividad = new Table(0, sHTML);
            Actividades.add(actividad);
            while (rs.next()) {
                sHTML = "";
                // Cambio de actividad
                if (lastactividad != rs.getInt("actividadid")) {
                    if (lastactividad != -1) sHTML = sHTML + "</td></tr>";
                    sHTML = sHTML + "<tr><td colspan=2>";
                    if (lastactividad != -1) sHTML = sHTML + "<br/><br/>";
                    lastactividad = rs.getInt("actividadid");
                    sHTML = sHTML + "&nbsp;<b><u>" + rs.getString("literal") + "</u></b> ";
                    utils = new adUtils(con);
                    if (idperiodo == Constantes.PERIODO_NADA) {
                        monjes = utils.getSQL("SELECT count(m.monjeid) FROM monje m where  ( m.estado = 0 or m.estado = 2)  and m.abadiaid = " + idabadia, 0);
                    } else {
                        monjes = utils.getSQL("SELECT count(DISTINCT a.monjeid)  FROM `actividad` a, monje m where a.monjeid = m.monjeid and a.periodoid=" + idperiodo + " and ( m.estado = 0 or m.estado = 2) and m.abadiaid = " + idabadia + " and actividadid = " + lastactividad, 0);
                    }
                    sHTML = sHTML + monjes + " ";
                    if (monjes == 1) sHTML = sHTML + msg2;
                    else sHTML = sHTML + msg1;
                    sHTML = sHTML + "</td></tr><td width=\"80\">";
                    switch (lastactividad) {
                        case 0:
                            sHTML = sHTML + "<img src=\"images/iconos/monje_durmiendo.gif\">";
                            break;
                        case 1:
                            sHTML = sHTML + "<img src=\"images/iconos/monje_rezar.gif\">";
                            break;
                        case 2:
                            sHTML = sHTML + "<img src=\"images/iconos/monje_labrador.gif\">";
                            break;
                        case 3:
                            sHTML = sHTML + "<img src=\"images/iconos/monje_cortandolena.gif\">";
                            break;
                        case 4:
                            sHTML = sHTML + "<img src=\"images/iconos/monje_pescando.gif\">";
                            break;
                        case 5:
                            sHTML = sHTML + "";
                            break;
                        case 6:
                            sHTML = sHTML + "<img src=\"images/iconos/monje_curar.gif\">";
                            break;
                        case 7:
                            sHTML = sHTML + "";
                            break;
                        case 8:
                            sHTML = sHTML + "";
                            break;
                        case 9:
                            sHTML = sHTML + "<img src=\"images/iconos/monje_ensenar.gif\">";
                            break;
                        case 10:
                            sHTML = sHTML + "";
                            break;
                        case 11:
                            sHTML = sHTML + "<img src=\"images/iconos/monje_ganadero.gif\">";
                            break;
                        case 12:
                            sHTML = sHTML + "<img src=\"images/iconos/monje_comiendo.gif\">";
                            break;
                        case 13:
                            sHTML = sHTML + "<img src=\"images/iconos/monje_molino.gif\">";
                            break;
                        case 14:
                            sHTML = sHTML + "<img src=\"images/iconos/monje_elaborar_comida.gif\">";
                            break;
                        case 15:
                            sHTML = sHTML + "<img src=\"images/iconos/monje_elaborar_recurso.gif\">";
                            break;
                        case 16:
                            sHTML = sHTML + "<img src=\"images/iconos/monje_elaborar_costura.gif\">";
                            break;
                        case 17:
                            sHTML = sHTML + "<img src=\"images/iconos/monje_elaborar_artesania.gif\">";
                            break;
                        case 18:
                            sHTML = sHTML + "<img src=\"images/iconos/monje_aprender.gif\">";
                            break;
                        case 19:
                            sHTML = sHTML + "<img src=\"images/iconos/monje_restaurar.gif\">";
                            break;

                    }
                    sHTML = sHTML + "</td><td valign=\"top\" align=\"left\">";
                }
                sHTML = sHTML + "<a href=\"mostrarMonje.do?clave=" + rs.getInt("monjeid") + "\">" + rs.getString("nombre") + " "
                        + resource.getMessage("monjes.abadia.nomciudad") + " "
                        + rs.getString("apellido1") + "</a><br>";

                actividad = new Table(0, sHTML);
                Actividades.add(actividad);
            }
            if (lastactividad != -1) {
                actividad = new Table(0, " </td></tr>");
                Actividades.add(actividad);
                lastactividad = -1;
            }

            ps = con.prepareStatement(sSQLViajes);
            ps.setLong(1, idabadia);
            ps.setShort(2, idiomaid);
            rs = ps.executeQuery();
            while (rs.next()) {
                sHTML = "";
                // Cambio de actividad
                if (lastactividad != rs.getInt("estado")) {
                    if (lastactividad != -1) sHTML = sHTML + "</td></tr>";
                    sHTML = sHTML + "<tr><td colspan=2>";
                    if (lastactividad != -1) sHTML = sHTML + "<br/><br/>";
                    lastactividad = rs.getInt("estado");
                    sHTML = sHTML + "&nbsp;<b><u>" + rs.getString("literal") + "</u></b> ";
                    monjes = 1;
                    sHTML = sHTML + monjes + " ";
                    if (monjes == 1) sHTML = sHTML + msg2;
                    else sHTML = sHTML + msg1;
                    sHTML = sHTML + "</td></tr><td width=\"80\">";
                    switch (lastactividad) {
                        case 3:
                            sHTML = sHTML + "<img src=\"images/iconos/monje_viajando.gif\">";
                            break;
                        case 4:
                            sHTML = sHTML + "<img src=\"images/iconos/monje_viajando.gif\">";
                            break;
                    }
                    sHTML = sHTML + "</td><td valign=\"top\" align=\"left\">";
                }
                sHTML = sHTML + "<a href=\"mostrarMonje.do?clave=" + rs.getInt("monjeid") + "\">" + rs.getString("nombre") + " "
                        + resource.getMessage("monjes.abadia.nomciudad") + " "
                        + rs.getString("apellido1") + "</a><br>";

                actividad = new Table(0, sHTML);
                Actividades.add(actividad);
            }
            if (lastactividad != -1) {
                actividad = new Table(0, " </td></tr>");
                Actividades.add(actividad);
                lastactividad = -1;
            }
            actividad = new Table(0, " </table>");
            Actividades.add(actividad);

            return Actividades;
            // Ejecutarlo
        } catch (SQLException e) {
            throw new AbadiaSQLException("adActividad. getActividadesAgrupados", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /**
     * Inicializa los acumulados diarios e incrementa el acumulado total de los monjes
     * para todas las actividades
     *
     * @throws AbadiaException
     */
    public void resetActividades() throws AbadiaException {
        String sSQL = "UPDATE actividad a set a.acumulado_total = a.acumulado_total + a.acumulado_dia, a.acumulado_dia = 0, a.realizado = 0";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            // Ejecutarlo
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adActividad. resetActividades. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Incrementa el acumulado_dia de un monje para un actividad-periodo
     * determinado
     *
     * @param idMonje
     * @param idPeriodo
     * @param idActividad
     * @param cantidad
     * @throws AbadiaException
     */
    public void incrementaAcumulado(int idMonje, int idPeriodo, int idActividad, double cantidad) throws AbadiaException {
        String sSQL = "UPDATE actividad SET acumulado_dia = acumulado_dia + ? WHERE monjeid = ? AND periodoid = ? AND actividadid = ?";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            // Ejecutarlo
            ps.setDouble(1, cantidad);
            ps.setInt(2, idMonje);
            ps.setInt(3, idPeriodo);
            ps.setInt(4, idActividad);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adActividad. incrementaAcumulado. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /**
     * Devuelve la descripcion del gerundio de una determinada actividad a partir de un
     * idioma determinado.
     *
     * @param idActividad
     * @param idIdioma
     * @return
     * @throws AbadiaException
     */
    public String getGerundioActividad(int idActividad, int idIdioma) throws AbadiaException {
        adUtils utils = new adUtils(con);
        return utils.getSQL("Select l.literal from literales l, actividad_tipo at where at.literalid_gerundio = l.literalid and l.idiomaid = " + idIdioma + " and at.actividadid = " + idActividad, "");
    }
}

