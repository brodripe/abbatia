package org.abbatia.adbean;

import org.abbatia.actionform.MercadoVentaActForm;
import org.abbatia.adbean.base.adbeans;
import org.abbatia.bean.*;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.ProductoNoEncontradoException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.Utilidades;
import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;

import java.sql.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
// Proyecto

public class adMercadoVenta extends adbeans {
    private static Logger log = Logger.getLogger(adUtils.class.getName());

    /**
     * Instancia la clase sin obtener una nueva conexión
     *
     * @param con
     * @throws AbadiaException
     */
    public adMercadoVenta(Connection con) throws AbadiaException {
        super(con);
    }

    /* Devuelve una lista con todos los productos que tenemos en venta
    */

    public MercadoVenta getMercadoVenta(Abadia abadia, Usuario usuario, MessageResources resource) throws AbadiaException {
        MercadoVenta Contents = new MercadoVenta();
        ArrayList<Table> alTable;
        Table tabla;
        String sHTML = "";

        alTable = cargarArrayProductosVenta(abadia.getIdDeAbadia(), usuario.getIdDeIdioma(), 0);

        if (alTable.isEmpty()) {
            sHTML =
                    "<tr><td colspan=9><center>" + resource.getMessage("mercado.venta.vacio") + "</center><td></tr>";
            tabla = new Table(0, sHTML);
            alTable.add(tabla);
        }
        Contents.setLstVenta(alTable);

        Contents.setLstVenta2(cargarArrayProductosVenta(abadia.getIdDeAbadia(), usuario.getIdDeIdioma(), 1));

        return Contents;
    }

    /* Devuelve una lista con todos los productos que tenemos en la Abadia
    */

    public MercadoVenta getProductos(long idDeAbadiaTmp, int iMercancia, Usuario usuario) throws AbadiaSQLException {
        MercadoVenta Contents = new MercadoVenta();
        Table tabla;
        ArrayList<Table> alTable = new ArrayList<Table>();
        String sHTML = "";
        String sSQL = "";
        String sTXT = "";
        String sMerc = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            log.debug("adMercadoVenta. getProductos. sMercancia: " + iMercancia);
            // Queremos visualizar alimentación o por defecto
            if (iMercancia == Constantes.MERCANCIA_ALIMENTOS) {
                sSQL = "SELECT *, af.descripcion as descfam, l.literal as descedi, at.descripcion as descali, al.estado as estadolote " +
                        "FROM `alimentos` a, `edificio` e, `edificio_tipo` et, `alimentos_lote` al, `alimentos_tipo` at, `alimentos_familia` af, literales l " +
                        "Where a.edificioid = e.edificioid and e.tipoedificioid = et.tipoedificioid AND  a.loteid = al.loteid AND  a.alimentoid = at.alimentoid AND af.familiaid=at.familiaid " +
                        "AND e.abadiaid = ? AND et.NOMBRE = l.LITERALID AND l.IDIOMAID = " + usuario.getIdDeIdioma() + " order by fecha_caducidad";
                sMerc = Constantes.MERCANCIA_ALIMENTOS_STR;
            } else if (iMercancia == Constantes.MERCANCIA_ANIMALES) {
                sSQL = "SELECT count(*) as cantidad, ac.descripcion as descali, l.literal as descedi, at.descripcion as descfam, " +
                        "a.salud as estadolote, at.dias_vida as fecha_caducidad, a.fecha_nacimiento as fecha_entrada, " +
                        "CONCAT(a.NIVEL, ';',a.TIPO_ANIMALID, ';', a.FECHA_NACIMIENTO) as loteid " +
                        "FROM animales a, `edificio` e, `edificio_tipo` et, animales_crecimiento ac, animales_tipo at, literales l " +
                        "Where a.edificioid = e.edificioid and e.tipoedificioid = et.tipoedificioid AND a.TIPO_ANIMALID = ac.TIPO_ANIMALID " +
                        "AND a.TIPO_ANIMALID = at.TIPO_ANIMALID " +
                        "AND a.NIVEL = ac.NIVEL and a.FECHA_EMBARAZO=null AND a.ESTADO=0 AND a.FECHA_FALLECIMIENTO=null " +
                        "AND e.abadiaid = ? AND et.NOMBRE = l.LITERALID AND l.IDIOMAID = " + usuario.getIdDeIdioma() + " GROUP BY descfam, descedi, descali, estadolote, fecha_caducidad, fecha_entrada, loteid";

                sMerc = Constantes.MERCANCIA_ANIMALES_STR;
            } else if (iMercancia == Constantes.MERCANCIA_RECURSOS) {
                sSQL = "SELECT rt.descripcion as descfam, '-' as descedi, rt.descripcion as descali, 100 as estadolote, r.cantidad as cantidad, r.recursoid as loteid " +
                        "FROM recurso r, recurso_tipo as rt " +
                        "Where r.recursoid = rt.recursoid and r.recursoid in (1,2,3,4) AND r.abadiaid = ?";
                sMerc = Constantes.MERCANCIA_RECURSOS_STR;
            } else if (iMercancia == Constantes.MERCANCIA_LIBROS) {
                sSQL = "SELECT rt.descripcion as descfam, '-' as descedi, rt.descripcion as descali, 100 as estadolote, r.cantidad as cantidad, r.recursoid as loteid " +
                        "FROM recurso r, recurso_tipo as rt " +
                        "Where r.recursoid = rt.recursoid and r.recursoid in (1,2,3,4) AND r.abadiaid = ?";
                sMerc = Constantes.MERCANCIA_RECURSOS_STR;
            } else if (iMercancia == Constantes.MERCANCIA_RELIQUIAS) {
                sSQL = "SELECT rt.descripcion as descfam, '-' as descedi, rt.descripcion as descali, 100 as estadolote, r.cantidad as cantidad, r.recursoid as loteid " +
                        "FROM recurso r, recurso_tipo as rt " +
                        "Where r.recursoid = rt.recursoid and r.recursoid in (1,2,3,4) AND r.abadiaid = ?";
                sMerc = Constantes.MERCANCIA_RECURSOS_STR;
            }

            log.debug("adMercadoVenta. getProductos. SQL: " + sSQL);
            ps = con.prepareStatement(sSQL);

            int parNo = 1;
            ps.setLong(parNo, idDeAbadiaTmp);
            rs = ps.executeQuery();

            while (rs.next()) {
                sHTML = "<tr>";
                sHTML = sHTML + "<td align=\"center\">" + rs.getString("descedi") +
                        "</td>";
                sTXT = rs.getString("descfam") + " : <b>" + rs.getString("descali") +
                        "</b>";
                sHTML = sHTML + "<td>" + sTXT + "</td>";
                if (sMerc.equals("N")) {
                    sHTML = sHTML + "<td align=\"center\">" + Utilidades.formatStringFromDB(rs.getString("fecha_entrada")) + "</td>";
                    sHTML = sHTML + "<td align=\"center\">" + rs.getString("fecha_caducidad") + "</td>";
                } else if (sMerc.equals("A")) {
                    sHTML = sHTML + "<td align=\"center\">" + Utilidades.formatStringFromDB(rs.getString("fecha_entrada")) + "</td>";
                    sHTML = sHTML + "<td align=\"center\">" + Utilidades.formatStringFromDB(rs.getString("fecha_caducidad")) + "</td>";
                } else if (sMerc.equals("R")) {
                    sHTML = sHTML + "<td align=\"center\"> - </td>";
                    sHTML = sHTML + "<td align=\"center\"> - </td>";
                }

                sHTML = sHTML + "<td align=\"center\">" + Utilidades.redondear(rs.getDouble("cantidad")) +
                        "</td>";
                sHTML = sHTML + "<td align=\"center\">" + rs.getString("estadolote") +
                        "</td>";
                sHTML = sHTML +
                        "<td align=\"center\"> " + //<input type=\"button\" value=\"Vender\" " +
                        //"onClick=\"self.location.href='mercado_vender.do?lid=" + rs.getString("loteid") + "&merc=" + sMerc + "'\" ></td>"; //'" ></td></tr>
                        " <a href=\"javascript:vender(" + rs.getString("loteid") + ",\'" + sMerc + "\');\" > " +
                        " <img src=\"images/iconos/16/vender.jpg\" border=\"0\" alt=\"Vender Producto\"></a></td>";

                sHTML = sHTML + "</tr>";
                tabla = new Table(0, sHTML);
                alTable.add(tabla);
            }
            if (alTable.isEmpty()) {
                sHTML = "<tr><td colspan=7><center>No tienes ningún producto para vender</center><td></tr>";
                tabla = new Table(0, sHTML);
                alTable.add(tabla);
            }
            Contents.setLstVenta(alTable);
            return Contents;
        }
        catch (SQLException e) {
            throw new AbadiaSQLException("adMercadoVenta. getProductos. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }


    }


    public ArrayList<Table> cargarArrayProductosVenta(int idDeAbadiaTmp, int idioma, int estado) throws AbadiaException {
        String sHTML = "";
        String sTXT = "";
        String sDESC = "";
        Table tabla;
        ArrayList<Table> alTable = new ArrayList<Table>();

        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            cs = con.prepareCall("call cargarProductosVenta(?,?,?)");
            int parNo = 1;
            cs.setInt(parNo++, idDeAbadiaTmp);
            cs.setInt(parNo++, estado);
            cs.setInt(parNo, idioma);
            cs.execute();
            rs = cs.getResultSet();

            while (rs.next()) {
                sHTML = "<tr>";
                if (rs.getInt("estado") == 0) {
                    sTXT = "<img src=\"images/iconos/16/abierto.gif\" border=\"0\" alt=\"Abierto\">";
                } else {
                    sTXT = "<img src=\"images/iconos/16/cerrado.gif\" border=\"0\" alt=\"Cerrado\">";
                }
                sHTML = sHTML + "<td align=\"center\">" + sTXT + "</td>";
                if (rs.getInt("tipo") == 0) {
                    sTXT = "Venta";
                } else {
                    sTXT = "Subasta";
                }
                sHTML = sHTML + "<td align=\"center\">" + sTXT + "</td>";
                sDESC = rs.getString("familia") + " : <b>" + rs.getString("descripcion") + "</b>";
                sHTML = sHTML + "<td>" + sDESC + "</td>";
                sHTML = sHTML + "<td align=\"center\">" + Utilidades.formatStringFromDB(rs.getString("fecha_inicial")) + "</td>";
                sHTML = sHTML + "<td align=\"center\">" + Utilidades.formatStringFromDB(rs.getString("fecha_final")) + "</td>";
                // Cantidad
                sTXT = rs.getString("ctd_actual");
                if (rs.getInt("ctd_inicial") != rs.getInt("ctd_actual")) {
                    sTXT = sTXT + " ( <font color=\"#888888\">" + rs.getString("ctd_inicial") + "</font> ) ";
                }
                sTXT = sTXT + " " + rs.getString("unidad_desc");
                sHTML = sHTML + "<td align=\"right\">" + sTXT + "</td>";
                // Precio
                sTXT = Utilidades.redondear(rs.getDouble("precio_actual"));
                if (rs.getDouble("precio_inicial") != rs.getDouble("precio_actual")) {
                    sTXT = sTXT + " ( <font color=\"#888888\">" + Utilidades.redondear(rs.getDouble("precio_inicial")) + "</font> ) ";
                }
                sHTML = sHTML + "<td align=\"right\">" + sTXT + "</td>";
                // Total
                sHTML = sHTML + "<td align=\"right\">" + Utilidades.redondear((double) (rs.getFloat("precio_actual") * rs.getInt("ctd_actual"))) + "</td>";

                sHTML = sHTML + "<td align=\"center\">";
                if (rs.getInt("estado") == 0) {
                    sHTML = sHTML + "<a href='javascript:cancelarVenta(" + rs.getString("productoid") + ");'><img src=\"images/iconos/16/cancelar.jpg\" border=\"0\" alt=\"Cancelar Venta\"></a>";
                    sHTML = sHTML + "<a href='javascript:modificarVenta(" + rs.getString("productoid") + ");'><img src=\"images/iconos/16/modificar.gif\" border=\"0\" alt=\"Modificar Venta\"></a>";
                }
                sHTML = sHTML + "<a href=\"mostrarHistoricoProducto.do?idproducto=" + rs.getString("productoid") + "\"><img src=\"images/iconos/16/historico.gif\" border=\"0\" alt=\"Historico Ventas\"></a>";
                sHTML = sHTML + "</td></font></tr>";
                tabla = new Table(0, sHTML);
                alTable.add(tabla);
            }

            return alTable;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMercadoVenta. cargarArrayProductosVenta. SQLException ", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(cs);
        }
    }

    /**
     * Recupera un arraylist de objetos con los productos puestos a la venta en el mercado
     *
     * @param idDeAbadiaTmp
     * @param idioma
     * @param estado
     * @return
     * @throws AbadiaException
     */
    public ArrayList<Mercado> recuperarContenidoMercado(int idDeAbadiaTmp, int idioma, int estado) throws AbadiaException {
        Mercado mercancia;
        ArrayList<Mercado> alMercancias = new ArrayList<Mercado>();

        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            cs = con.prepareCall("call cargarProductosVenta(?,?,?)");
            int parNo = 1;
            cs.setInt(parNo++, idDeAbadiaTmp);
            cs.setInt(parNo++, estado);
            cs.setInt(parNo, idioma);
            cs.execute();
            rs = cs.getResultSet();

            while (rs.next()) {
                mercancia = new Mercado();
                mercancia.setIdProducto(rs.getInt("productoid"));
                mercancia.setEstado(rs.getInt("estado"));
                mercancia.setFamilia(rs.getString("familia"));
                mercancia.setDescripcion(rs.getString("descripcion"));
                mercancia.setFecha_inicial(Utilidades.formatStringFromDB(rs.getString("fecha_inicial")));
                mercancia.setFecha_final(Utilidades.formatStringFromDB(rs.getString("fecha_final")));
                mercancia.setCtd_inicial(rs.getInt("ctd_inicial"));
                mercancia.setCtd_actual(rs.getInt("ctd_actual"));
                mercancia.setVolumenD(rs.getDouble("espacio_ocupado"));
                mercancia.setVolumen(Utilidades.redondear(rs.getDouble("espacio_ocupado")));
                mercancia.setCantidad(Utilidades.redondear(mercancia.getCtd_actual()) + " (" + Utilidades.redondear(mercancia.getCtd_inicial()) + ")");

                mercancia.setUnidad_desc(rs.getString("unidad_desc"));
                mercancia.setPrecio_actual(rs.getDouble("precio_actual"));
                mercancia.setPrecio_inicial(rs.getDouble("precio_inicial"));
                mercancia.setPrecio(Utilidades.redondear(mercancia.getPrecio_actual()) + " (" + Utilidades.redondear(mercancia.getPrecio_inicial()) + ")");
                mercancia.setPrecioTotalD(mercancia.getCtd_actual() * mercancia.getPrecio_actual());
                mercancia.setPrecioTotal(Utilidades.redondear(mercancia.getPrecioTotalD()));

                alMercancias.add(mercancia);
            }

            return alMercancias;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMercadoVenta. recuperarContenidoMercado. SQLException ", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(cs);
        }
    }

    public MercadoVentaActForm recuperarDatosProductoVenta(long idAbadia, MercadoVentaActForm datosProducto, Usuario usuario) throws AbadiaException {

        String sSQL = "";
        ResultSet rs = null;
        PreparedStatement ps = null;
        double precio;

        try {
            //si se trata de un animal....
            if (datosProducto.getMercado().equals(Constantes.MERCANCIA_ANIMALES_STR)) {
                StringTokenizer st = new StringTokenizer(datosProducto.getId(), ";");
                datosProducto.setNivelAnimal(Integer.parseInt(st.nextToken()));
                datosProducto.setTipoAnimal(Integer.parseInt(st.nextToken()));
                datosProducto.setFechaAnimal(st.nextToken());

                sSQL = "SELECT count(*) AS cantidad, at.DESCRIPCION AS descfamilia, ac.DESCRIPCION AS descproducto " +
                        "FROM animales a, animales_crecimiento ac, animales_tipo at, edificio e " +
                        "WHERE a.nivel = ac.nivel AND a.tipo_animalid = at.tipo_animalid and at.tipo_animalid = ac.tipo_animalid and a.edificioid=e.edificioid and e.abadiaid=? and a.nivel = ? and a.tipo_animalid = ? and a.fecha_nacimiento = ? AND a.ESTADO=0 and a.fecha_embarazo is null and a.fecha_fallecimiento is null " +
                        "GROUP BY at.DESCRIPCION, ac.DESCRIPCION ";
                ps = con.prepareStatement(sSQL);
                ps.setLong(1, idAbadia);
                ps.setInt(2, datosProducto.getNivelAnimal());
                ps.setInt(3, datosProducto.getTipoAnimal());
                ps.setString(4, datosProducto.getFechaAnimal());
                rs = ps.executeQuery();
                //si se trata de alimentos...
            } else if (datosProducto.getMercado().equals(Constantes.MERCANCIA_ALIMENTOS_STR)) {
                sSQL = "SELECT cantidad, af.descripcion as descfamilia, at.descripcion as descproducto " +
                        "FROM `alimentos` a, `edificio` e, `edificio_tipo` et, `alimentos_lote` al, `alimentos_tipo` at, `alimentos_familia` af " +
                        "Where a.edificioid = e.edificioid and e.tipoedificioid = et.tipoedificioid AND a.loteid = al.loteid AND a.alimentoid = at.alimentoid AND af.familiaid=at.familiaid AND e.abadiaid = ? and al.loteid = ?";
                ps = con.prepareStatement(sSQL);
                ps.setLong(1, idAbadia);
                ps.setInt(2, Integer.parseInt(datosProducto.getId()));
                rs = ps.executeQuery();
            } else if (datosProducto.getMercado().equals(Constantes.MERCANCIA_RECURSOS_STR)) {
                sSQL = "SELECT a.cantidad, at.descripcion as descfamilia, at.descripcion as descproducto " +
                        "FROM `recurso` a, `recurso_tipo` at " +
                        "Where a.recursoid = at.recursoid and a.abadiaid = ? and a.recursoid = ?";
                ps = con.prepareStatement(sSQL);
                ps.setLong(1, idAbadia);
                ps.setInt(2, Integer.parseInt(datosProducto.getId()));
                rs = ps.executeQuery();
            }
            if (rs.next()) {
                datosProducto.setCantidad(Utilidades.redondear(rs.getDouble("cantidad")));
                datosProducto.setCantidadDisponible(Utilidades.redondear(rs.getInt("cantidad")));
                datosProducto.setCantidadDouble(rs.getDouble("cantidad"));
                datosProducto.setDescripcionProducto(rs.getString("descproducto"));
                datosProducto.setDescripcionFamilia(rs.getString("descfamilia"));
                precio = getPrecioMercado(datosProducto);
                datosProducto.setPrecio(Utilidades.redondear(precio));
                datosProducto.setPrecioVentaCiudad(Utilidades.redondear(precio / 4));
            } else {
                throw new ProductoNoEncontradoException(usuario.getNick() + " - adMercadoVenta. recuperarDatosProductoVenta. No se ha encontrado el producto", null, log);
            }

            return datosProducto;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMercadoVenta. recuperarDatosProductoVenta. SQLException ", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public double recuperarCantidadPorProductoAlimento(int idAbadia, String lotes, Usuario usuario) throws AbadiaException {
        String sSQL = "";
        sSQL = "SELECT sum(cantidad) as total " +
                "FROM `alimentos` a, `edificio` e, `edificio_tipo` et, `alimentos_lote` al, `alimentos_tipo` at, `alimentos_familia` af " +
                "Where a.edificioid = e.edificioid and e.tipoedificioid = et.tipoedificioid AND a.loteid = al.loteid AND " +
                "a.alimentoid = at.alimentoid AND af.familiaid=at.familiaid AND e.abadiaid = " + idAbadia + " and al.loteid in (" + lotes + ")";

        adUtils utils = new adUtils(con);
        double dTotal = utils.getSQL(sSQL, 0);

        if (dTotal == 0) {
            throw new ProductoNoEncontradoException(usuario.getNick() + " - adMercadoVenta. recuperarCantidadPorProductoAlimento. no se dispone de cantidad para vender para los lotes: " + lotes, null, log);
        }
        return dTotal;
    }

    //recupera el precio de un producto en la abbatia 0 (mercado)

    public double getPrecioMercado(MercadoVentaActForm datos) throws AbadiaException {
        String sSQLAlimentos = "SELECT m.precio_actual FROM mercados m, mercados_alimentos ma, alimentos a WHERE m.productoid = ma.productoid and ma.alimentoid=a.alimentoid and m.abadiaid=0 and a.loteid = ?";

        String sSQLRecursos = "SELECT m.precio_actual FROM  mercados m, mercados_recursos ma WHERE m.productoid = ma.productoid and ma.recursoid=? and m.abadiaid=0 ";

        String sSQLAnimales = "SELECT m.precio_actual FROM  mercados m, mercados_animales ma WHERE m.productoid = ma.productoid and  ma.NIVEL=? and ma.TIPO_ANIMALID = ? and m.abadiaid=0";

        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            if (datos.getMercado().equals(Constantes.MERCANCIA_ALIMENTOS_STR)) {
                ps = con.prepareStatement(sSQLAlimentos);

                ps.setInt(1, Integer.parseInt(datos.getId()));
                rs = ps.executeQuery();
                if (rs.next()) return rs.getDouble("precio_actual");
            } else if (datos.getMercado().equals(Constantes.MERCANCIA_ANIMALES_STR)) {
                ps = con.prepareStatement(sSQLAnimales);
                ps.setInt(1, datos.getNivelAnimal());
                ps.setInt(2, datos.getTipoAnimal());
                rs = ps.executeQuery();
                if (rs.next()) return rs.getDouble("precio_actual");
            } else if (datos.getMercado().equals(Constantes.MERCANCIA_RECURSOS_STR)) {
                ps = con.prepareStatement(sSQLRecursos);
                ps.setInt(1, Integer.parseInt(datos.getId()));
                rs = ps.executeQuery();
                if (rs.next()) return rs.getDouble("precio_actual");
            } else return 1;
            return 1;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMercadoCompra. getPrecioMercado. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public double getPrecioMercadoPorProducto(int productoID, String mercado) throws AbadiaException {

        String sSQLAlimentos = "SELECT m2.precio_actual FROM mercados_alimentos ma, mercados m2, mercados_alimentos ma2 " +
                "WHERE ma.productoid = ? and ma.alimentoid = ma2.alimentoid and m2.productoid = ma2.productoid and m2.abadiaid=0 ";

        String sSQLRecursos = "SELECT m2.precio_actual FROM mercados_recursos ma, mercados m2, mercados_recursos ma2 " +
                "WHERE ma.productoid = ? and ma.recursoid = ma2.recursoid and m2.productoid=ma2.productoid and m2.abadiaid=0";

        String sSQLAnimales = "SELECT m2.precio_actual FROM  mercados_animales ma, mercados m2, mercados_animales ma2 " +
                "WHERE ma.productoid = ? and ma.nivel = ma2.nivel and ma.tipo_animalid = ma2.tipo_animalid and m2.productoid=ma2.productoid and m2.abadiaid=0";

        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            if (mercado.equals(Constantes.MERCANCIA_ALIMENTOS_STR)) {
                ps = con.prepareStatement(sSQLAlimentos);

                ps.setInt(1, productoID);
                rs = ps.executeQuery();
                if (rs.next()) return rs.getDouble("precio_actual");
            } else if (mercado.equals(Constantes.MERCANCIA_ANIMALES_STR)) {
                ps = con.prepareStatement(sSQLAnimales);

                ps.setInt(1, productoID);
                rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getDouble("precio_actual");
                }
            } else if (mercado.equals(Constantes.MERCANCIA_RECURSOS_STR)) {
                ps = con.prepareStatement(sSQLRecursos);

                ps.setInt(1, productoID);
                rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getDouble("precio_actual");
                }
            } else return 1;
            return 1;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMercadoVenta. getPrecioMercadoPorProducto. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public double getPrecioMercadoPorAlimento(int idAlimento) throws AbadiaException {

        String sSQLAlimentos = "SELECT distinct m.precio_actual FROM mercados_alimentos ma, mercados m " +
                "WHERE ma.alimentoid = ? and ma.productoid=m.productoid and m.abadiaid=0 ";

        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQLAlimentos);

            ps.setInt(1, idAlimento);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("precio_actual");
            }
            return 1;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMercadoCompra. getPrecioMercadoPorAlimento. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }


}
