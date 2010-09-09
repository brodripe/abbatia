package org.abbatia.adbean;

import org.abbatia.bean.*;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.HTML;
import org.abbatia.utils.Utilidades;
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
public class adElaboracionRecursos extends adElaboracion {
    private static Logger log = Logger.getLogger(adElaboracionRecursos.class.getName());

    /**
     * Instancia la clase sin obtener una nueva conexión
     *
     * @param con
     * @throws AbadiaException
     */
    public adElaboracionRecursos(Connection con) throws AbadiaException {
        super(con);
    }


    public ArrayList<datosElaboracion> recuperaProductosElaboracion(int idEdificio, Usuario usuario) throws AbadiaException {
        String sSQL = "SELECT ea.elaboracionid, ea.productoid, ea.edificioid, l.literal as desc_alimento, ea.cantidad, ea.elaborado, ea.estado, l2.literal as desc_estado, ea.fecha_inicio " +
                "FROM elaboracion_alimentos ea, recursos_tipo rt, literales l, literales l2, elaboracion_estado ee " +
                "WHERE ea.productoid = rt.recursoid and rt.literalid = l.literalid and l.idiomaid = ? and ea.estado = ee.estado and " +
                "ee.literalid = l2.literalid and l2.idiomaid = ? and ea.edificioid = ?";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, usuario.getIdDeIdioma());
            ps.setInt(2, usuario.getIdDeIdioma());
            ps.setInt(3, idEdificio);
            rs = ps.executeQuery();
            datosElaboracion elaboracion = null;
            ArrayList<datosElaboracion> productos = new ArrayList<datosElaboracion>();
            while (rs.next()) {
                elaboracion = new datosElaboracion();
                elaboracion.setIdElaboracion(rs.getInt("elaboracionid"));
                elaboracion.setIdProducto(rs.getInt("productoid"));
                elaboracion.setDescProducto(rs.getString("desc_alimento"));
                elaboracion.setEstado(rs.getInt("estado"));
                elaboracion.setDescEstado(rs.getString("desc_estado"));
                elaboracion.setIdEdificio(rs.getInt("edificioid"));
                elaboracion.setCantidad(rs.getInt("cantidad"));
                elaboracion.setElaborado(rs.getInt("elaborado"));
                elaboracion.setElaboradoS(Utilidades.redondear(rs.getDouble("elaborado")));
                if (rs.getString("fecha_inicio") == null) {
                    elaboracion.setFecha_inicio("-");
                } else {
                    elaboracion.setFecha_inicio(Utilidades.formatStringFromDB(rs.getString("fecha_inicio")));
                }
                productos.add(elaboracion);
            }
            return productos;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adElaboracionAlimentos. recuperaProductosElaboracion. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public void insertarProductoElaboracion(datosElaboracion datos) throws AbadiaException {
        String sSQL = "insert into elaboracion_alimentos (productoid, edificioid, cantidad, elaborado, estado, fecha_inicio) " +
                "values (?,?,?,?,?,?)";

        PreparedStatement ps = null;

        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, datos.getIdProducto());
            ps.setInt(2, datos.getIdEdificio());
            ps.setInt(3, datos.getCantidad());
            ps.setInt(4, 0);
            ps.setInt(5, Constantes.ESTADO_ELABORACION_PENDIENTE);
            ps.setString(6, null);
            ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adElaboracionAlimentos. insertarProductoElaboracion. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public ArrayList<requisitoElaboracion> recuperarRequisitosPorProducto(Usuario usuario, int idProducto) throws AbadiaException {
        String sSQL = "select er.tipo_requisito, er.requisitoid, er.cantidad " +
                "from elaboracion_requisitos er " +
                "where er.productoid = ? ";

        PreparedStatement ps = null;
        ResultSet rs = null;
        requisitoElaboracion requisito;
        adAlimentos alimentoAD;
        adRecurso recursoAD;
        ArrayList<requisitoElaboracion> requisitos = new ArrayList<requisitoElaboracion>();
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idProducto);
            //ps.setInt(2, usuario.getIdDeIdioma());
            rs = ps.executeQuery();
            while (rs.next()) {
                requisito = new requisitoElaboracion();
                //si es un alimento
                if (rs.getString("tipo_requisito").equals(Constantes.MERCANCIA_ALIMENTOS_STR)) {
                    alimentoAD = new adAlimentos(con);
                    requisito.setProducto(alimentoAD.recuperarDescripcionAlimento(rs.getInt("requisitoid"), usuario));
                    //si es un recurso
                } else {
                    recursoAD = new adRecurso(con);
                    requisito.setProducto(recursoAD.recuperarDescripcionRecurso(rs.getInt("requisitoid"), usuario));
                }
                requisito.setCantidad(Utilidades.redondear(rs.getDouble("CANTIDAD")));
                requisitos.add(requisito);
            }
            return requisitos;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adElaborarAlimentos. recuperarRequisitosPorProducto. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }


    public int recuperarEdificioIdElaboracion(int idProducto, Abadia abadia) throws AbadiaException {
        String sSQL = "Select e.edificioid from edificio e, elaboracion_parametros ep " +
                "where e.tipoedificioid = ep.tipo_edificioid and e.abadiaid = ? and ep.productoid = ?";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, abadia.getIdDeAbadia());
            ps.setInt(2, idProducto);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("edificioid");
            }
            return 0;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adElaboracionAlimentos. recuperarEdificioIdElaboracion. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public void actualizarEstado(Abadia abadia, int idElaboracion, int estado) throws AbadiaException {
        String sSQL = "update elaboracion_alimentos set estado = ? where elaboracionid = ? and " +
                "edificioid in (select edificioid from edificio where abadiaid = ?)";

        PreparedStatement ps = null;

        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, estado);
            ps.setInt(2, idElaboracion);
            ps.setLong(3, abadia.getIdDeAbadia());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adElaboracionAlimentos. actualizarEstado. SQLException", e, log);
        } finally {
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
        n = ((double) dif / (double) producto.getCantidad_ini()) * 100;
        //divido por 10 para obtener un valor de 0 a 10
        n = Math.round(n / 10);
        sHTML = sHTML + HTML.smallBarra((int) n, "Molido: " + Math.round(dif) + " de " + producto.getCantidad_ini());
        producto.setBarra_HTML(sHTML);
    }

}
