package org.abbatia.adbean;

import org.abbatia.adbean.base.adbeans;
import org.abbatia.bean.ClimaLluvia;
import org.abbatia.bean.ClimaRegion;
import org.abbatia.core.CoreTiempo;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.base.AbadiaException;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class adClima extends adbeans {
    private static Logger log = Logger.getLogger(adClima.class.getName());

    /**
     * Instancia la clase sin obtener una nueva conexión
     *
     * @param con
     * @throws org.abbatia.exception.base.AbadiaException
     *
     */
    public adClima(Connection con) throws AbadiaException {
        super(con);
    }

    public HashMap<Integer, ClimaLluvia> recuperarLluviasPorClima() throws AbadiaException {
        //Definición de cadena sql de consulta
        String sSQL = "select * from clima_lluvia";
        HashMap<Integer, ClimaLluvia> hmPrecipitaciones = new HashMap<Integer, ClimaLluvia>();

        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        ClimaLluvia lluvia = null;
        try {
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                lluvia = new ClimaLluvia();
                lluvia.setIdClima(rs.getInt("CLIMAID"));
                lluvia.setMinLlubia(rs.getInt("LITROS_MIN"));
                lluvia.setMaxLlubia(rs.getInt("LITROS_MAX"));
                hmPrecipitaciones.put(lluvia.getIdClima(), lluvia);
            }
            return hmPrecipitaciones;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adClima. recuperarLluviasPorClima. SQLException", e, adClima.log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public ArrayList<ClimaRegion> recuperarTemperaturas() throws AbadiaException {
        String sTrace = this.getClass() + ".cambiar_clima()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        String sSQL = "SELECT * FROM `region` r, region_clima rc where (r.regionid = rc.regionid) and rc.mes = ?";


        GregorianCalendar gc = CoreTiempo.getTiempoAbadia();

        ArrayList<ClimaRegion> alTemperaturas = new ArrayList<ClimaRegion>();


        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, gc.get(GregorianCalendar.MONTH) + 1);
            rs = ps.executeQuery();
            while (rs.next()) {
                alTemperaturas.add(new ClimaRegion(rs.getInt("regionid"), rs.getInt("temp_min"), rs.getInt("temp_max"), rs.getInt("tiempo_max"), rs.getInt("tiempo_min")));
            }
            return alTemperaturas;
        } catch (SQLException e) {
            throw new AbadiaSQLException("ERROR: cambiar_clima = " + e, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }


}
