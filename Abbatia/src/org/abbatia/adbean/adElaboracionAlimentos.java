package org.abbatia.adbean;

import org.abbatia.bean.*;
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

/**
 * Created by IntelliJ IDEA.
 * User: Benjamin
 * Date: 04-nov-2004
 * Time: 22:25:18
 * To change this template use File | Settings | File Templates.
 */
public class adElaboracionAlimentos extends adElaboracion {
    private static Logger log = Logger.getLogger(adElaboracionAlimentos.class.getName());

    /**
     * Instancia la clase sin obtener una nueva conexión
     *
     * @param con
     * @throws AbadiaException
     */
    public adElaboracionAlimentos(Connection con) throws AbadiaException {
        super(con);
    }

    public ArrayList<datosElaboracion> recuperaProductosElaboracion(String tipo, int idEdificio, Usuario usuario, MessageResources resource) throws AbadiaException {

        String sSQLElaboracion = "";

        if (tipo.equals(Constantes.ELABORACION_TIPO_ALIMENTO)) {
            sSQLElaboracion = "SELECT ea.elaboracionid, ea.productoid, ea.edificioid, l.literal as desc_alimento, ea.cantidad, ea.elaborado, ea.estado, l2.literal as desc_estado, ea.fecha_inicio, ep.tiempo_total " +
                    "FROM elaboracion_alimentos ea, alimentos_tipo at, literales l, literales l2, elaboracion_estado ee, elaboracion_parametros ep " +
                    "WHERE ea.productoid = at.alimentoid and at.literalid = l.literalid and l.idiomaid = ? and ea.estado = ee.estado and " +
                    "ee.literalid = l2.literalid and l2.idiomaid = ? and ea.edificioid = ? and ea.productoid = ep.productoid " +
                    "order by desc_alimento";
        } else {
            sSQLElaboracion = "SELECT ea.elaboracionid, ea.productoid, ea.edificioid, l.literal as desc_alimento, ea.cantidad, ea.elaborado, ea.estado, l2.literal as desc_estado, ea.fecha_inicio, ep.tiempo_total " +
                    "FROM elaboracion_alimentos ea, recurso_tipo rt, literales l, literales l2, elaboracion_estado ee, elaboracion_parametros ep " +
                    "WHERE ea.productoid = rt.recursoid and rt.literalid = l.literalid and l.idiomaid = ? and ea.estado = ee.estado and " +
                    "ee.literalid = l2.literalid and l2.idiomaid = ? and ea.edificioid = ? and ea.productoid = ep.productoid " +
                    "order by desc_alimento";
        }

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement(sSQLElaboracion);
            ps.setInt(1, usuario.getIdDeIdioma());
            ps.setInt(2, usuario.getIdDeIdioma());
            ps.setInt(3, idEdificio);
            rs = ps.executeQuery();
            datosElaboracion elaboracion;
            ArrayList<datosElaboracion> productos = new ArrayList<datosElaboracion>();
            while (rs.next()) {
                elaboracion = new datosElaboracion();
                elaboracion.setIdElaboracion(rs.getInt("elaboracionid"));
                elaboracion.setIdProducto(rs.getInt("productoid"));
                elaboracion.setDescProducto(rs.getString("desc_alimento"));
                elaboracion.setEstado(rs.getInt("estado"));
                if (elaboracion.getEstado() == Constantes.ESTADO_ELABORACION_REPOSANDO) {
                    elaboracion.setDescEstado(rs.getString("desc_estado") + " (" + rs.getInt("tiempo_total") + " " + resource.getMessage("edificios.abadia.dias") + ")");
                } else {
                    elaboracion.setDescEstado(rs.getString("desc_estado"));
                }
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


    public int recuperarIdElaboracion(int idEdificio, int idProducto, String estado) throws AbadiaException {
        String sSQL = "select elaboracionid from elaboracion_alimentos where productoid = " + idProducto + " and estado in (" + estado + ") and edificioid = " + idEdificio;
        adUtils utils = null;
        int idElaboracion = 0;

        try {
            utils = new adUtils(con);
            idElaboracion = utils.getSQL(sSQL, 0);
            return idElaboracion;
        } finally {
            //if (utils!= null) utils.finalize();
        }
    }

    public void sumarCantidadElaboracion(int idElaboracion, int cantidad) throws AbadiaException {
        String sSQL = "update elaboracion_alimentos set cantidad = cantidad + " + cantidad + " where elaboracionid = " + idElaboracion;
        adUtils utils = null;

        try {
            utils = new adUtils(con);
            utils.execSQL(sSQL);
        } finally {
            //if (utils!= null) utils.finalize();
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

    /**
     * Devuelve el identificador de edificio asociado a la elaboración de un producto determinado
     *
     * @param idProducto Identificador del producto que se va a elaborar
     * @param abadia     objeto {@link Abadia} correspondiente al usuario
     * @throws org.abbatia.exception.AbadiaSQLException
     *          Excepción base de abbatia
     */

    public int recuperarEdificioIdElaboracion(int idProducto, Abadia abadia) throws AbadiaException {
        String sSQL = "Select e.edificioid from edificio e, elaboracion_parametros ep " +
                "where e.tipoedificioid = ep.tipo_edificioid and e.abadiaid = " + abadia.getIdDeAbadia() + " and ep.productoid = " + idProducto;
        adUtils utils = null;

        try {
            utils = new adUtils(con);
            return utils.getSQL(sSQL, 0);
        } catch (Exception e) {
            throw new AbadiaSQLException("adElaboracionAlimentos. recuperarEdificioIdElaboracion. SQLException", e, log);
        } finally {
            //if (utils!=null) utils.finalize();
        }
    }

    /**
     * Devuelve el identificador de edificio asociado a la elaboracion
     *
     * @param idElaboracion Identificador de la elaboracion
     * @param abadia        objeto {@link Abadia} correspondiente al usuario
     * @return
     * @throws org.abbatia.exception.AbadiaSQLException
     *          Excepción base de abbatia
     */
    public int recuperarEdificioIdPorElaboracionId(int idElaboracion, Abadia abadia) throws AbadiaException {
        String sSQL = "Select e.edificioid from elaboracion_alimentos ea, edificio e " +
                "where ea.elaboracionid = ? and e.abadiaid = ? and e.edificioid = ea.edificioid ";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idElaboracion);
            ps.setInt(2, abadia.getIdDeAbadia());
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("edificioid");
            }
            return 0;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adElaboracionAlimentos. recuperarEdificioIdPorElaboracionId. SQLException", e, log);
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

        dif = producto.getCantidad_ini() - producto.getCantidad_act();
        //calculo el porcentaje que representa lo molido respecto del total
        n = (double) ((double) dif / (double) producto.getCantidad_ini()) * 100;
        //divido por 10 para obtener un valor de 0 a 10
        n = Math.round(n / 10);
        sHTML = sHTML + HTML.smallBarra((int) n, "Molido: " + Math.round(dif) + " de " + producto.getCantidad_ini());
        producto.setBarra_HTML(sHTML);
    }

}
