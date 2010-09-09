package org.abbatia.adbean;

import org.abbatia.adbean.base.adbeans;
import org.abbatia.bean.*;
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
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: Benjamin
 * Date: 23-nov-2004
 * Time: 0:18:04
 * To change this template use File | Settings | File Templates.
 */
public class adElaboracion extends adbeans {
    private static Logger log = Logger.getLogger(adElaboracion.class.getName());

    /**
     * Instancia la clase sin obtener una nueva conexión
     *
     * @param con
     * @throws AbadiaException
     */
    public adElaboracion(Connection con) throws AbadiaException {
        super(con);
    }

    public ArrayList<Table> getProductosElaborables(String tipo, Edificio edificio, Usuario usuario, MessageResources resource) throws AbadiaException {
        String sSQLElaboracion = "";

        if (tipo.equals(Constantes.ELABORACION_TIPO_ALIMENTO)) {
            sSQLElaboracion = "Select ep.productoid, l.literal " +
                    "from elaboracion_parametros ep, literales l, edificio e, alimentos_tipo at " +
                    "where ep.tipo_producto = ? and e.edificioid = ? and e.tipoedificioid = ep.tipo_edificioid " +
                    "and ep.productoid = at.alimentoid and at.literalid = l.literalid and l.idiomaid = ? " +
                    "order by l.literal";
        } else {
            sSQLElaboracion = "Select ep.productoid, l.literal " +
                    "from elaboracion_parametros ep, literales l, edificio e, recurso_tipo rt " +
                    "where ep.tipo_producto = ? and e.edificioid = ? and e.tipoedificioid = ep.tipo_edificioid " +
                    "and ep.productoid = rt.recursoid and rt.literalid = l.literalid and l.idiomaid = ? " +
                    "order by l.literal";
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQLElaboracion);
            ps.setString(1, tipo);
            ps.setInt(2, edificio.getIdDeEdificio());
            ps.setInt(3, usuario.getIdDeIdioma());
            rs = ps.executeQuery();

            ArrayList<Table> tablas = new ArrayList<Table>();
            tablas.add(new Table(0, resource.getMessage("mensajes.listados.sinseleccion")));
            while (rs.next()) {
                tablas.add(new Table(rs.getInt("productoid"), rs.getString("literal")));
            }

            return tablas;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adElaboracion. getProductosElaborables. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public HashMap<Integer, ArrayList<requisitoElaboracion>> recuperarRequisitosGlobales() throws AbadiaException {
        String sSQL = "select er.productoid, er.requisitoid, er.cantidad, er.tipo_requisito " +
                "from elaboracion_requisitos er " +
                "order by er.productoid";

        PreparedStatement ps = null;
        ResultSet rs = null;
        requisitoElaboracion requisito;
        ArrayList<requisitoElaboracion> requisitos = new ArrayList<requisitoElaboracion>();
        HashMap<Integer, ArrayList<requisitoElaboracion>> requisitosHT = new HashMap<Integer, ArrayList<requisitoElaboracion>>();
        int last_productoid = 0;
        try {
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (last_productoid != rs.getInt("PRODUCTOID")) {
                    if (last_productoid != 0) {
                        requisitosHT.put(last_productoid, requisitos);
                        requisitos = new ArrayList<requisitoElaboracion>();
                    }
                    last_productoid = rs.getInt("PRODUCTOID");
                }
                requisito = new requisitoElaboracion();
                requisito.setIdProducto(rs.getInt("REQUISITOID"));
                requisito.setCantidadD(rs.getDouble("CANTIDAD"));
                requisito.setTipo(rs.getString("TIPO_REQUISITO"));
                requisitos.add(requisito);
            }
            requisitosHT.put(last_productoid, requisitos);

            return requisitosHT;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adElaborarAlimentos. recuperarRequisitosGlobales. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public void actualizarElaboracionAlimentos(ArrayList elaboracionesAL) throws AbadiaException {
        String sSQL = "update elaboracion_alimentos set elaborado=?, estado=?, fecha_inicio=?, fecha_fin=? where elaboracionid=?";
        Iterator elaboraciones = elaboracionesAL.iterator();
        datosElaboracion elaboracion;
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            while (elaboraciones.hasNext()) {
                elaboracion = (datosElaboracion) elaboraciones.next();
                ps.setDouble(1, elaboracion.getElaborado());
                ps.setInt(2, elaboracion.getEstado());
                ps.setString(3, elaboracion.getFecha_inicio());
                ps.setString(4, elaboracion.getFecha_fin());
                ps.setInt(5, elaboracion.getIdElaboracion());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new AbadiaSQLException("adElaborarAlimentos. actualizarElaboracionAlimentos. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public double recuperarCantidadProducto(requisitoElaboracion requisito, int idAbadia) throws AbadiaException {
        if (requisito.getTipo().equals(Constantes.MERCANCIA_ALIMENTOS_STR)) {
            adAlimentoLotes alimentoAD = new adAlimentoLotes(con);
            Alimento alimento = alimentoAD.recuperarCantidadAlimentoId(requisito.getIdProducto(), idAbadia);
            if (alimento != null) {
                return alimento.getCantidad();
            } else return 0;

        } else if (requisito.getTipo().equals(Constantes.MERCANCIA_RECURSOS_STR)) {
            adRecurso recursoAD = new adRecurso(con);
            Recurso recurso = recursoAD.recuperarRecurso(requisito.getIdProducto(), idAbadia);
            if (recurso != null) {
                return recurso.getCantidad();
            } else return 0;
        }
        return 0;
    }

    public void restarCantidadProducto(requisitoElaboracion requisito, int idAbadia, double ctd_dia) throws AbadiaException {
        if (requisito.getTipo().equals(Constantes.MERCANCIA_ALIMENTOS_STR)) {
            adAlimentoLotes alimentoAD = new adAlimentoLotes(con);
            alimentoAD.restarLotesPorAlimentoId(idAbadia, requisito.getIdProducto(), requisito.getCantidadD() * ctd_dia);
        } else if (requisito.getTipo().equals(Constantes.MERCANCIA_RECURSOS_STR)) {
            adRecurso recursoAD = new adRecurso(con);
            recursoAD.restarRecurso(requisito.getIdProducto(), idAbadia, requisito.getCantidadD() * ctd_dia);
        }
    }

    public void eliminarElaboracion(int idElaboracion) throws AbadiaException {
        adUtils utils = new adUtils(con);
        utils.execSQL("delete from elaboracion_alimentos where elaboracionid = " + idElaboracion);
    }


}
