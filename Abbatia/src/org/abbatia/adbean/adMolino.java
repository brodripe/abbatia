package org.abbatia.adbean;

import org.abbatia.bean.Abadia;
import org.abbatia.bean.ProductoMolino;
import org.abbatia.bean.Usuario;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.HTML;
import org.abbatia.utils.Utilidades;
import org.abbatia.dbms.DBMSUtils;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Benjamin
 * Date: 04-nov-2004
 * Time: 22:25:18
 * To change this template use File | Settings | File Templates.
 */
public class adMolino extends adEdificio {
    private static Logger log = Logger.getLogger(adMolino.class.getName());

    /**
     * Instancia la clase sin obtener una nueva conexión
     *
     * @param con
     * @throws AbadiaException
     */
    public adMolino(Connection con) throws AbadiaException {
        super(con);
    }


    public ArrayList recuperaProductosMolino(Abadia abadia, Usuario usuario) throws AbadiaException {
        String sSQL = "Select mp.productoid_entrada, mp.recursoid_salida, mp.ctd_actual, mp.ctd_inicial, mp.fecha_inicial, l1.literal as producto_desc, l2.literal as recurso_desc " +
                "from molino_produccion mp, literales l1, literales l2, recurso_tipo rt, alimentos_tipo at " +
                "where abadiaid = ? and mp.productoid_entrada = at.alimentoid and mp.recursoid_salida = rt.recursoid " +
                " and at.literalid = l1.literalid and rt.literalid=l2.literalid and l1.idiomaid = ? and l2.idiomaid = ? ";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = (PreparedStatement) con.prepareStatement(sSQL);
            ps.setLong(1, abadia.getIdDeAbadia());
            ps.setInt(2, usuario.getIdDeIdioma());
            ps.setInt(3, usuario.getIdDeIdioma());
            rs = (ResultSet) ps.executeQuery();
            ProductoMolino producto = null;
            ArrayList productos = new ArrayList();
            while (rs.next()) {
                producto = new ProductoMolino();
                producto.setAbadiaid((int) abadia.getIdDeAbadia());
                producto.setCantidad_act(rs.getInt("CTD_ACTUAL"));
                producto.setCantidad_ini(rs.getInt("CTD_INICIAL"));
                producto.setFecha_inicio(Utilidades.formatStringFromDB(rs.getString("FECHA_INICIAL")));
                producto.setProductoid_desc(rs.getString("PRODUCTO_DESC"));
                producto.setProductoid_entrada(rs.getInt("PRODUCTOID_ENTRADA"));
                producto.setRecursoid_desc(rs.getString("RECURSO_DESC"));
                producto.setRecursoid_salida(rs.getInt("RECURSOID_SALIDA"));
                this.actualizarBarrasHTML(producto);
                productos.add(producto);
            }
            return productos;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adMolino. recuperaProductosMolino. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public void insertarProductoMolino(ProductoMolino datosProducto) throws AbadiaException {
        String sSQL = "insert into molino_produccion (abadiaid, productoid_entrada, recursoid_salida, ctd_inicial, ctd_actual, fecha_inicial) " +
                "values (?,?,?,?,?,?)";

        PreparedStatement ps = null;

        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, datosProducto.getAbadiaid());
            ps.setInt(2, datosProducto.getProductoid_entrada());
            ps.setInt(3, datosProducto.getRecursoid_salida());
            ps.setInt(4, datosProducto.getCantidad_ini());
            ps.setInt(5, datosProducto.getCantidad_act());
            ps.setString(6, datosProducto.getFecha_inicio());
            ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMolino. insertarProductoMolino. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public void actualizarProductoMolino(ProductoMolino datosProducto) throws AbadiaException {
        String sSQL = "update molino_produccion set ctd_inicial = ctd_inicial + ?, ctd_actual = ctd_actual + ? where abadiaid = ? and productoid_entrada = ? ";

        PreparedStatement ps = null;

        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, datosProducto.getCantidad_ini());
            ps.setInt(2, datosProducto.getCantidad_act());
            ps.setInt(3, datosProducto.getAbadiaid());
            ps.setInt(4, datosProducto.getProductoid_entrada());
            ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMolino. actualizarProductoMolino. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public boolean existeProductoMolino(int p_iAbadiaId, int p_iProductoId) throws AbadiaException {
        String sSQL = "select * from molino_produccion where abadiaid = ? and productoid_entrada = ? ";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, p_iAbadiaId);
            ps.setInt(2, p_iProductoId);
            rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMolino. existeProductoMolino. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public int recuperarRecursoPorProducto(Abadia abadia, int idProducto) throws AbadiaException {
        String sSQL = "select mt.recursoid from molino_tipo mt, edificio e where e.abadiaid = ? and e.tipoedificioid = ? and productoid = ? ";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, (int) abadia.getIdDeAbadia());
            ps.setInt(2, Constantes.EDIFICIO_MOLINO);
            ps.setInt(3, idProducto);
            rs = (ResultSet) ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("RECURSOID");
            } else return 0;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMolino. recuperarRecursoPorProducto. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /* Barras de HTML
     */
    public void actualizarBarrasHTML(ProductoMolino producto) {
        String sHTML = "";
        double n;
        int dif;

        HTML cHTML = new HTML();
        dif = producto.getCantidad_ini() - producto.getCantidad_act();
        //calculo el porcentaje que representa lo molido respecto del total
        n = (double) ((double) dif / (double) producto.getCantidad_ini()) * 100;
        //divido por 10 para obtener un valor de 0 a 10
        n = Math.round(n / 10);
        sHTML = sHTML + cHTML.smallBarra((int) n, "Molido: " + Math.round(dif) + " de " + producto.getCantidad_ini());
        producto.setBarra_HTML(sHTML);
    }

}
