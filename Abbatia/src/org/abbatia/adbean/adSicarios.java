package org.abbatia.adbean;

import org.abbatia.adbean.base.adbeans;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.MonjeVisita;
import org.abbatia.bean.Sicarios;
import org.abbatia.bean.Usuario;
import org.abbatia.core.CoreTiempo;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.Utilidades;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class adSicarios extends adbeans {
    private static Logger log = Logger.getLogger(adSicarios.class.getName());

    /**
     * Instancia la clase sin obtener una nueva conexión
     *
     * @param con
     * @throws AbadiaException
     */
    public adSicarios(Connection con) throws AbadiaException {
        super(con);
    }


    // Recupera las reliquias de una abbatia
    public ArrayList<Sicarios> recuperarSicarios(Abadia abadia, Usuario usuario) throws AbadiaException {
        //Definición de cadena sql de consulta
        String sSQL = "Select * from sicario WHERE regionid=? and estado=0 order by nombre, apellido";
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
            ps.setInt(parNo, abadia.getIdDeRegion());
            //Lanzo la consulta y cargo el resultado en un resultset
            rs = ps.executeQuery();
            //si la consulta encuentra la recurso....
            ArrayList<Sicarios> listaSicarios = new ArrayList<Sicarios>();
            Sicarios sicario;
            while (rs.next()) {
                //iniciamos el volcado de datos sobre
                //el objeto recurso.
                sicario = new Sicarios();
                sicario.setIdSicario(rs.getInt("SicarioID"));
                sicario.setEstado(rs.getInt("estado"));
                sicario.setIdRegion(rs.getInt("regionid"));
                sicario.setNombre(rs.getString("nombre"));
                sicario.setApellido(rs.getString("apellido"));
                sicario.setAbadiaid_contrato(rs.getInt("abadiaid_contrato"));
                sicario.setAbadiaid_destino(rs.getInt("abadiaid_destino"));
                sicario.setMonjeid_destino(rs.getInt("monjeid_destino"));
                sicario.setFecha_contrato(rs.getString("Fecha_contrato"));
                sicario.setFecha_matanza(rs.getString("Fecha_matanza"));
                sicario.setFecha_redisponible(rs.getString("Fecha_redisponible"));
                sicario.setPapas_muertos(rs.getInt("papas_muertos"));
                sicario.setObispos_muertos(rs.getInt("obispos_muertos"));
                sicario.setCardenales_muertos(rs.getInt("cardenales_muertos"));
                sicario.setPrecio(rs.getInt("precio"));
                sicario.setMoral(rs.getInt("moral"));
                listaSicarios.add(sicario);
            }
            return listaSicarios;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adSicarios. recuperarSicarios. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    // Recupera las reliquias de una abbatia
    public boolean validarSicario(Abadia abadia, int sicarioid) throws AbadiaException {
        //Definición de cadena sql de consulta
        String sSQL = "Select * from sicario WHERE regionid=? and estado=0 and sicarioid=?";
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
            ps.setInt(parNo++, abadia.getIdDeRegion());
            ps.setInt(parNo, sicarioid);
            //Lanzo la consulta y cargo el resultado en un resultset
            rs = ps.executeQuery();
            //si la consulta encuentra la recurso....
            return rs.next();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adSicarios. validarSicario. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public Sicarios recuperarSicario(int sicarioid) throws AbadiaException {
        //Definición de cadena sql de consulta
        String sSQL = "Select * from sicario WHERE sicarioid=? ";
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
            ps.setInt(parNo, sicarioid);
            //Lanzo la consulta y cargo el resultado en un resultset
            rs = ps.executeQuery();
            //si la consulta encuentra la recurso....
            //ArrayList listaSicarios = new ArrayList();
            Sicarios sicario = null;
            if (rs.next()) {
                //iniciamos el volcado de datos sobre
                //el objeto recurso.
                sicario = new Sicarios();
                sicario.setIdSicario(rs.getInt("SicarioID"));
                sicario.setEstado(rs.getInt("estado"));
                sicario.setIdRegion(rs.getInt("regionid"));
                sicario.setNombre(rs.getString("nombre"));
                sicario.setApellido(rs.getString("apellido"));
                sicario.setAbadiaid_contrato(rs.getInt("abadiaid_contrato"));
                sicario.setAbadiaid_destino(rs.getInt("abadiaid_destino"));
                sicario.setMonjeid_destino(rs.getInt("monjeid_destino"));
                sicario.setFecha_contrato(rs.getString("Fecha_contrato"));
                sicario.setFecha_matanza(rs.getString("Fecha_matanza"));
                sicario.setFecha_redisponible(rs.getString("Fecha_redisponible"));
                sicario.setPapas_muertos(rs.getInt("papas_muertos"));
                sicario.setObispos_muertos(rs.getInt("obispos_muertos"));
                sicario.setCardenales_muertos(rs.getInt("cardenales_muertos"));
                sicario.setPrecio(rs.getInt("precio"));
                sicario.setMoral(rs.getInt("moral"));
            }
            return sicario;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adSicarios. recuperarSicario. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    // Devuelve el precio para matar a una eminencia
    public int recuperarPrecioSicario(Abadia abadia, int sicarioid, int monjeid) throws AbadiaException {
        adUtils utils;
        adHabilidades habilidadesAD;
        //Definición de cadena sql de consulta
        String sSQL = "Select * from sicario WHERE regionid=? order by nombre, apellido";


        habilidadesAD = new adHabilidades(con);
        int popular = (int) habilidadesAD.recuperarHabilidad(monjeid, Constantes.HABILIDAD_POPULARIDAD);

        utils = new adUtils(con);
        // Sicario
        int precio = utils.getSQL("SELECT precio FROM `sicario` WHERE sicarioid =" + sicarioid, 0);
        int moral = utils.getSQL("SELECT moral FROM `sicario` WHERE sicarioid =" + sicarioid, 0);
        // Info del Monje a matar
        int abadiaid = utils.getSQL("SELECT abadiaid FROM `monje` WHERE monjeid =" + monjeid, 0);
        int jerarquiaid = utils.getSQL("SELECT jerarquiaid FROM `monje` WHERE monjeid =" + monjeid, 0);
        int region_destino = utils.getSQL("SELECT a.regionid FROM `monje` m, abadia a where m.abadiaid = a.abadiaid and monjeid=" + monjeid, 1);
        // Distancias
        int dist_camino = utils.getSQL("SELECT distancia_campo FROM `region_distancia` where regionid_origen = " + abadia.getIdDeRegion() + " and regionid_destino = " + region_destino, 5);
        int dist_montan = utils.getSQL("SELECT distancia_montanyas FROM `region_distancia` where regionid_origen = " + abadia.getIdDeRegion() + " and regionid_destino = " + region_destino, 1);
        int dist_barco = utils.getSQL("SELECT distancia_mar FROM `region_distancia` where regionid_origen = " + abadia.getIdDeRegion() + " and regionid_destino = " + region_destino, 0);

        // Jerarquiaid y popular
        precio = precio + (jerarquiaid * 1000);
        precio = precio + (popular * 500);
        // Cuanto más lejos, mas caro
        precio = precio + ((dist_camino + dist_montan + dist_barco) * 100);
        return precio;
    }

    // Devuelve el precio para matar a una eminencia
    public void contratarSicario(Abadia abadia, int sicarioid, MonjeVisita monje, int precio) throws AbadiaException {
        //Definición de cadena sql de consulta
        String sSQL = "UPDATE sicario SET abadiaid_contrato=?, abadiaid_destino=?, monjeid_destino=?, fecha_contrato=?, " +
                "fecha_matanza=?, fecha_redisponible=?, estado=1 WHERE sicarioid=? ";


        adUtils utils = new adUtils(con);
        // Distancias
        int dist_camino = utils.getSQL("SELECT distancia_campo FROM `region_distancia` where regionid_origen = " + abadia.getIdDeRegion() + " and regionid_destino = " + monje.getIdRegion(), 5);
        int dist_montan = utils.getSQL("SELECT distancia_montanyas FROM `region_distancia` where regionid_origen = " + abadia.getIdDeRegion() + " and regionid_destino = " + monje.getIdRegion(), 1);
        int dist_barco = utils.getSQL("SELECT distancia_mar FROM `region_distancia` where regionid_origen = " + abadia.getIdDeRegion() + " and regionid_destino = " + monje.getIdRegion(), 0);

        int distancia = dist_camino + dist_montan + dist_barco + Utilidades.Random(0, 10);

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
            ps.setInt(parNo++, monje.getIdAbadia());
            ps.setInt(parNo++, monje.getIdMonje());
            ps.setString(parNo++, CoreTiempo.getTiempoAbadiaString());
            ps.setString(parNo++, CoreTiempo.getDiferenciaString(distancia));
            ps.setString(parNo++, CoreTiempo.getDiferenciaString((distancia * 2) + 1));
            ps.setInt(parNo, sicarioid);
            ps.execute();

        } catch (SQLException e) {
            throw new AbadiaSQLException("adSicarios. contratarSicario. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }


}
