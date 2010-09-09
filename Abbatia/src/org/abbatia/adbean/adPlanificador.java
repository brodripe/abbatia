package org.abbatia.adbean;

import org.abbatia.adbean.base.adbeans;
import org.abbatia.bean.Table;
import org.abbatia.bean.datosProceso;
import org.abbatia.core.CoreTiempo;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Utilidades;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class adPlanificador extends adbeans {
    private static Logger log = Logger.getLogger(adPlanificador.class.getName());

    /**
     * Instancia la clase sin obtener una nueva conexión
     *
     * @param con
     * @throws AbadiaException
     */
    public adPlanificador(Connection con) throws AbadiaException {
        super(con);
    }

    public int recuperarProcesoStatus() throws AbadiaException {
        //Definición de cadena sql de consulta
        String sSQL = "SELECT activo FROM procesos_status WHERE procesoid=0";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);

            //Lanzo la consulta y cargo el resultado en un resultset
            rs = ps.executeQuery();
            //si la consulta encuentra la actividad....
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adPlanificador. recuperarProcesoStatus. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public void bloquearProcesosStatus() throws AbadiaException {
        //Definición de cadena sql de consulta
        String sSQL = "update procesos_status set activo=0 WHERE procesoid=0";

        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);

            //Lanzo la consulta y cargo el resultado en un resultset
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new AbadiaSQLException("adPlanificador. bloquearProcesosStatus. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }


    //recupera el objeto Actividad cargado...
    public datosProceso recuperarProcesoPendiente() throws AbadiaException {
        //Definición de cadena sql de consulta
        String sSQL = "SELECT DESCRIPCION, PROCESOID, FRECUENCIA, HORA, PROCESO, CLASE, PARAMETROS FROM procesos WHERE FECHARUN <= ? AND ACTIVO<>'N' order by FECHARUN";
        String sFechaAbadia = CoreTiempo.getTiempoAbadiaStringConHoras();
        //log.info("adPlanificador. recuperarProcesoPendiente. fechaAbadia: " + sFechaAbadia);

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);

            int parNo = 1;
            ps.setString(parNo, sFechaAbadia);
            //Lanzo la consulta y cargo el resultado en un resultset
            rs = ps.executeQuery();
            //si la consulta encuentra la actividad....
            datosProceso datos = null;
            if (rs.next()) {
                datos = new datosProceso();
                datos.setName(rs.getString("DESCRIPCION"));
                datos.setIdProceso(rs.getInt("PROCESOID"));
                datos.setIntervalo(rs.getInt("FRECUENCIA"));
                datos.setHora(rs.getString("HORA"));
                datos.setProceso(rs.getString("PROCESO"));
                datos.setClase(rs.getString("CLASE"));
                datos.setParametro(rs.getString("PARAMETROS"));
            }
            return datos;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adPlanificador. recuperarProcesosPendientes. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public datosProceso recuperarProceso(int idProceso) throws AbadiaException {
        //Definición de cadena sql de consulta
        String sSQL = "SELECT DESCRIPCION, PROCESOID, FRECUENCIA, HORA, PROCESO, CLASE, PARAMETROS FROM procesos WHERE PROCESOID = ?";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);

            int parNo = 1;
            ps.setInt(parNo, idProceso);
            //Lanzo la consulta y cargo el resultado en un resultset
            rs = ps.executeQuery();
            //si la consulta encuentra la actividad....
            datosProceso datos = null;
            if (rs.next()) {
                datos = new datosProceso();
                datos.setName(rs.getString("DESCRIPCION"));
                datos.setIdProceso(rs.getInt("PROCESOID"));
                datos.setIntervalo(rs.getInt("FRECUENCIA"));
                datos.setHora(rs.getString("HORA"));
                datos.setProceso(rs.getString("PROCESO"));
                datos.setClase(rs.getString("CLASE"));
                datos.setParametro(rs.getString("PARAMETROS"));

            }
            return datos;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adPlanificador. recuperarProceso. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }


    /* Devuelve los procesos por orden de ejecución
    */
    public ArrayList<Table> getProcesosEjecutar(long nivel) throws AbadiaException {
        String sSQL = "SELECT * " +
                "FROM `procesos` order by activo, fecharun ";

        String sHTML = "";
        int n = 1;
        ArrayList<Table> procesos = new ArrayList<Table>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            Table proceso;
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("fecharealend") == null) {
                    sHTML = "<tr bgcolor=\"#00E000\">";
                } else {
                    if (n % 2 == 0)
                        sHTML = "<tr>";
                    else sHTML = "<tr bgcolor=\"#EDE0C5\">";
                }
                sHTML = sHTML + "<td>" + rs.getString("procesoid") + "</td>";
                sHTML = sHTML + "<td>" + rs.getString("activo") + "</td>";
                sHTML = sHTML + "<td>" + rs.getString("descripcion") + "</td>";
                sHTML = sHTML + "<td>" + Utilidades.formatStringFromDB(rs.getString("fecharun")) + "</td>";
                sHTML = sHTML + "<td>" + Utilidades.formatStringFromDB(rs.getString("fecharealstart")) + "</td>";
                sHTML = sHTML + "<td>" + Utilidades.formatStringFromDB(rs.getString("fecharealend")) + "</td>";
                if (nivel == 1) {
                    sHTML = sHTML + "<td><a href=\"EjecutarProceso.do?clave=" +
                            rs.getString("procesoid") + "\">Ejecutar</a></td>";
                } else {
                    sHTML = sHTML + "<td>&nbsp;</td>";
                }
                sHTML = sHTML + "</tr>";
                proceso = new Table(0, sHTML);
                procesos.add(proceso);
                n++;
            }
            return procesos;
            // Ejecutarlo
        } catch (SQLException e) {
            //log.error("adPlanificador. getProcesosEjecutar", e);
            throw new AbadiaSQLException("adPlanificador. getProcesosEjecutar", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }


    /* Devuelve los procesos por orden de ejecución
    */
    public ArrayList<Table> getProcesosLogs() throws AbadiaException {
        String sSQL = "SELECT * FROM `procesos_mensajes` order by id desc LIMIT 100 ";

        String sHTML = "", desc;
        int n = 1;
        ArrayList<Table> procesos = new ArrayList<Table>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            Table proceso = null;
            ps = (PreparedStatement) con.prepareStatement(sSQL);
            rs = (ResultSet) ps.executeQuery();
            while (rs.next()) {
                if (n % 2 == 0)
                    sHTML = "<tr>";
                else sHTML = "<tr bgcolor=\"#EDE0C5\">";
                sHTML = sHTML + "<td>" + rs.getString("fecha_real") + "</td>";
                //sHTML = sHTML + "<td>" + rs.getString("tipo") + "</td>";
                desc = "";
                switch (rs.getInt("Tipo")) {
                    case 1:
                        desc = " bgColor=\"#990000\" ";
                        break;
                    case 2:
                        desc = " bgColor=\"#009900\" ";
                        break;
                    case 3:
                        desc = " bgColor=\"#000099\" ";
                        break;
                }
                sHTML = sHTML + "<td" + desc + ">" + rs.getString("descripcion") + "</td>";
                sHTML = sHTML + "</tr>";
                proceso = new Table(0, sHTML);
                procesos.add(proceso);
                n++;
            }
            return procesos;
            // Ejecutarlo
        } catch (SQLException e) {
            log.error("adPlanificador. getProcesosEjecutar", e);
            throw new AbadiaSQLException("adPlanificador. getProcesosEjecutar", e, log);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
            }
        }

    }


}

