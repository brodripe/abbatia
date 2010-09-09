package org.abbatia.adbean;

import org.abbatia.adbean.base.adbeans;
import org.abbatia.bean.ClimaRegion;
import org.abbatia.bean.Region;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.base.AbadiaException;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class adRegion extends adbeans {
    private static Logger log = Logger.getLogger(adRegion.class.getName());

    /**
     * Instancia la clase sin obtener una nueva conexión
     *
     * @param con
     * @throws AbadiaException
     */
    public adRegion(Connection con) throws AbadiaException {
        super(con);
    }

    //recupera el objeto Region cargado...
    public Region recuperarRegion(int idDeRegionTmp) throws AbadiaSQLException {
        //Definición de cadena sql de consulta
        String sSQL = "Select * from region where REGIONID = ?";
        //creo un objeto de tipo Region
        Region region = new Region();
        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave.

            int parNo = 1;
            ps.setInt(parNo, idDeRegionTmp);
            //Lanzo la consulta y cargo el resultado en un resultset
            rs = ps.executeQuery();
            //si la consulta encuentra la region....
            if (rs.next()) {
                //iniciamos el volcado de datos sobre
                //el objeto region.
                region.setIdDeRegion(rs.getInt("REGIONID"));
                region.setDescripcion(rs.getString("DESCRIPCION"));

                //devolvemos el objeto Region informado.
                return region;
            }
            //si region no se localiza, devolveremos null
            return null;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adRegion. recuperarRegion. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    //recupera el objeto Region cargado...
    public String recuperarNombreRegion(int idDeRegionTmp) throws AbadiaException {
        //Definición de cadena sql de consulta
        String sSQL = "Select descripcion from region where REGIONID = " + idDeRegionTmp;
        adUtils utils;
        String sResult = "";
        try {
            utils = new adUtils(con);
            return utils.getSQL(sSQL, sResult);
        } catch (AbadiaException e) {
            throw new AbadiaSQLException("adRegion. recuperarRegion. SQLException.", e, log);
        }
    }

    //dará de alta un objeto Region en la base de datos
    public void crearRegion(Region region) throws AbadiaSQLException {
        String sSQL = "Insert Into `REGION` ( `REGIONID`,`DESCRIPCION`) Values ('?','?');";

        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno los valores
            int parNo = 0;
            ps.setInt(parNo++, region.getIdDeRegion());
            ps.setString(parNo, region.getDescripcion());
            // Ejecutarlo
            ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adRegion. crearRegion. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }


    //elimina un objeto Region de la base de datos
    //a partir de un objeto Region devuelve verdadero si no ha ocurrido un error
    public boolean eliminarRegion(Region region) throws AbadiaSQLException {
        return eliminarRegion(region.getIdDeRegion());
    }

    //Elimina un objeto Region de la base de datos
    //a partir de la clave del Region
    public boolean eliminarRegion(int idDeRegionTmp) throws AbadiaSQLException {
        String sSQL = "Delete From region Where `REGIONID` = ?";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            // Ejecutarlo
            return ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adRegion. eliminarRegion. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public int getIdiomaRegion(int idRegion) throws AbadiaException {
        adUtils util = new adUtils(con);
        return util.getSQL("select idiomaid from region where regionid = " + idRegion, 0);
    }

    public HashMap<Integer, ClimaRegion> recuperarDatosClima() throws AbadiaException {
        String sSQL = "SELECT pv_region.CLAVEID as region, pv_temp.VALOR as temp, pv_clima.VALOR as clima, pv_nieve.VALOR as nieve " +
                " FROM propiedad_valor pv_region, propiedad_valor pv_temp, propiedad_valor pv_clima, propiedad_valor pv_nieve " +
                " WHERE pv_temp.TIPO = 'R' and pv_temp.PROPIEDADID=3 " +
                "  and pv_clima.TIPO='R' and pv_clima.PROPIEDADID=1 " +
                "  and pv_nieve.TIPO='R' and pv_nieve.PROPIEDADID=100 " +
                "  and pv_region.TIPO='R' AND pv_region.claveid=pv_temp.claveid and pv_region.claveid=pv_clima.claveid and pv_region.claveid=pv_nieve.claveid " +
                " group by region";
        HashMap<Integer, ClimaRegion> hmClimas = new HashMap<Integer, ClimaRegion>();
        ClimaRegion climaRegion;

        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                climaRegion = new ClimaRegion();
                climaRegion.setIdRegion(rs.getInt("region"));
                climaRegion.setTemperatura(rs.getInt("temp"));
                climaRegion.setClima(rs.getInt("clima"));
                climaRegion.setNieve(rs.getInt("nieve"));
                hmClimas.put(climaRegion.getIdRegion(), climaRegion);
            }

            return hmClimas;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adRegion. recuperarDatosClima. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /**
     * Recupera un ArrayList con los datos de región: código, nombre, abadía obispo de
     * las regiones que tienen abadías
     *
     * @return
     * @throws AbadiaException
     */
    public ArrayList<Region> recuperarDatosRegiones() throws AbadiaException {
        String sSQL = "Select r.regionid, r.descripcion from region r , abadia a where r.regionid = a.regionid group by r.regionid, r.descripcion";
        ArrayList<Region> alRegiones = new ArrayList<Region>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                alRegiones.add(new Region(rs.getInt("REGIONID"), rs.getString("DESCRIPCION")));
            }
            return alRegiones;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adRegion. recuperarDatosRegiones. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Recupera un ArrayList con los datos de región: código, nombre e impuesto de tránsito
     *
     * @return
     * @throws AbadiaException
     */
    public ArrayList<Region> recuperarComisionTransitoRegiones() throws AbadiaException {
        String sSQL = "Select r.regionid, r.descripcion, c.transito from region r , abadia a, comisiones c where r.regionid = a.regionid and r.regionid = c.regionid group by r.regionid, r.descripcion";
        ArrayList<Region> alRegiones = new ArrayList<Region>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                alRegiones.add(new Region(rs.getInt("REGIONID"), rs.getString("DESCRIPCION"), rs.getDouble("TRANSITO")));
            }
            return alRegiones;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adRegion. recuperarComisionTransitoRegiones. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

}
