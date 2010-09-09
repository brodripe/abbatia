package org.abbatia.adbean;

import org.abbatia.adbean.base.adbeans;
import org.abbatia.bean.MercadoMovima;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.base.AbadiaException;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class adMercadoMovima extends adbeans {
    private static Logger log = Logger.getLogger(adUtils.class.getName());

    /**
     * Instancia la clase sin obtener una nueva conexión
     *
     * @param con Connection
     * @throws AbadiaException Excepción general
     */
    public adMercadoMovima(Connection con) throws AbadiaException {
        super(con);
    }


    public MercadoMovima crearMercadoMovima(int idProducto, long idAbadia, String Fecha, String Tipo, int Ctd, double precio) throws AbadiaException {
        MercadoMovima movimiento = new MercadoMovima();

        movimiento.setIdProducto(idProducto);
        movimiento.setIdAbadia(idAbadia);
        movimiento.setFecha(Fecha);
        movimiento.setTipo(Tipo);
        movimiento.setCantidad(Ctd);
        movimiento.setPrecio_unidad(precio);

        crearMercadoMovima(movimiento);

        return movimiento;
    }


    public void crearMercadoMovima(MercadoMovima movimiento) throws AbadiaException {
        String sSQL = "insert into mercados_movima (PRODUCTOID, ABADIAID, FECHA, TIPO, CTD, PRECIO_UNIDAD) VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno los valores
            int parNo = 1;
            ps.setInt(parNo++, movimiento.getIdProducto());
            ps.setLong(parNo++, movimiento.getIdAbadia());
            ps.setString(parNo++, movimiento.getFecha());
            ps.setString(parNo++, movimiento.getTipo());
            ps.setInt(parNo++, movimiento.getCantidad());
            ps.setDouble(parNo, movimiento.getPrecio_unidad());
            // Ejecutarlo
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMercadoMovima. crearMercadoMovima. SQLException ", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }
}
