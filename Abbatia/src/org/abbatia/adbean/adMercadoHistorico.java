package org.abbatia.adbean;

import org.abbatia.adbean.base.adbeans;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.MercadoHistorico;
import org.abbatia.bean.Table;
import org.abbatia.bean.Usuario;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.Utilidades;
import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;

import java.sql.*;
import java.util.ArrayList;

public class adMercadoHistorico extends adbeans {
    private static Logger log = Logger.getLogger(adUtils.class.getName());

    /**
     * Instancia la clase sin obtener una nueva conexión
     *
     * @param con conexión
     * @throws AbadiaException Excepción general
     */
    public adMercadoHistorico(Connection con) throws AbadiaException {
        super(con);
    }


    public ArrayList<Table> getMercadoHistorico(Abadia abadiaDB, Usuario usuario, MessageResources resource, String sProductoID) throws AbadiaException {
        ArrayList<Table> Contents = new ArrayList<Table>();

        adUtils oUtilsAD;

        Table tabla;
        String sHTML;
        String sSQL;
        String sTXT;
        String Tipo, NombreAbadia;

        int ProductoID = Integer.parseInt(sProductoID);
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {

            // Queremos visualizar alimentación o por defecto
            sSQL = "SELECT m.*, mm.abadiaid as MOV_ABADIAID, mm.fecha as MOV_FECHA , mm.tipo as MOV_TIPO, mm.CTD as MOV_CTD, mm.Precio_Unidad as MOV_PRECIO " +
                    " FROM `mercados` m, `mercados_movima` mm " +
                    " WHERE m.productoid = mm.productoid AND ( m.abadiaid = ? OR mm.abadiaID = ? ) ";

            // Cargar los mensajes
            ps = con.prepareStatement(sSQL);

            int parNo = 1;
            if (ProductoID == 0) {
                ps.setLong(parNo++, abadiaDB.getIdDeAbadia());
                ps.setLong(parNo, abadiaDB.getIdDeAbadia());
            } else {
                ps.setLong(parNo, ProductoID);
            }

            oUtilsAD = new adUtils(con);
            rs = ps.executeQuery();
            while (rs.next()) {
                // Lo hemos vendido o comprado???
                if (rs.getLong("abadiaid") == abadiaDB.getIdDeAbadia()) {
                    if (rs.getInt("MOV_ABADIAID") != 0) {

                        NombreAbadia = oUtilsAD.getSQL(
                                "SELECT nombre FROM abadia WHERE abadiaid = " +
                                        rs.getString("MOV_ABADIAID"), "desconocido");
                    } else NombreAbadia = resource.getMessage("mercado.ciudad");
                    // Tipo de movimiento
                    if (rs.getInt("tipo") == 0) {
                        Tipo = "<font color=\"#008000\"><b>" + resource.getMessage("mercado.venta") + "</b></font>";
                    } else {
                        Tipo = "<font color=\"#800000\"><b>" + resource.getMessage("mercado.subasta") + "</b></font>";
                    }
                } else {
                    if (rs.getInt("ABADIAID") != 0) {
                        NombreAbadia = oUtilsAD.getSQL("SELECT nombre FROM abadia WHERE abadiaid = " + rs.getString("abadiaid"), "desconocido");
                    } else NombreAbadia = resource.getMessage("mercado.ciudad");
                    if (rs.getInt("tipo") == 0) {
                        Tipo = "<font color=\"#800000\"><b>" + resource.getMessage("mercado.compra") + "</b></font>";
                    } else {
                        Tipo = "<font color=\"#800000\"><b>" + resource.getMessage("mercado.puja") + "</b></font>";
                    }
                }

                sHTML = "<tr>";
                sHTML = sHTML + "<td align=\"center\">" + Tipo + "</td>";
                sHTML = sHTML + "<td align=\"center\">" + NombreAbadia + "</td>";
                sHTML = sHTML + "<td align=\"center\">" + Utilidades.formatStringFromDB(rs.getString("mov_fecha")) + "</td>";
                // Tipo
                sTXT = "Desconocido : ";
                // Alimentación
                if (rs.getString("mercancia").equals("A")) {
                    sTXT = oUtilsAD.getSQL("SELECT CONCAT(l1.literal, ' : ', l2.literal) " +
                            "FROM `mercados_alimentos` ma,  `alimentos_familia` af, `alimentos_tipo` at, literales l1, literales l2 " +
                            "where ma.alimentoid = at.alimentoid and at.familiaid = af.familiaid and " +
                            "l1.idiomaid=" + usuario.getIdDeIdioma() + " AND l2.idiomaid=" + usuario.getIdDeIdioma() + " AND l1.literalid = af.literalid AND l2.literalid=at.literalid AND " +
                            "ma.productoid = " + rs.getString("productoid"), "no se encuentra");
                    sTXT = "Alimentos : <b>" + sTXT + "</b>";
                }
                // Recursos
                if (rs.getString("mercancia").equals("R")) {
                    sTXT = oUtilsAD.getSQL("SELECT l1.literal " +
                            "FROM `mercados_recursos` mr,  `recurso_tipo` rt, literales l1 " +
                            "where mr.recursoid = rt.recursoid and " +
                            "l1.idiomaid=" + usuario.getIdDeIdioma() + " AND l1.literalid = rt.literalid AND " +
                            "mr.productoid = " + rs.getString("productoid"), "no se encuentra");
                    sTXT = "Recurso : <b>" + sTXT + "</b>";
                }
                // Animales
                if (rs.getString("mercancia").equals("N")) {
                    sTXT = oUtilsAD.getSQL("SELECT CONCAT(l1.literal, ' : ', l2.literal) " +
                            "FROM `mercados_animales` ma,  `animales_crecimiento` ac, animales_tipo as at, literales l1, literales l2  " +
                            "where ma.tipo_animalid = ac.tipo_animalid and ma.nivel = ac.nivel and ma.tipo_animalid = at.tipo_animalid and " +
                            "l1.idiomaid=" + usuario.getIdDeIdioma() + " AND l2.idiomaid=" + usuario.getIdDeIdioma() + " AND l1.literalid = at.literalid AND l2.literalid=ac.literalid AND " +
                            "ma.productoid = " + rs.getString("productoid"), "no se encuentra");
                    sTXT = "Animal : <b>" + sTXT + "</b>";
                }

                sHTML = sHTML + "<td>" + sTXT + "</td>";
                sHTML = sHTML + "<td align=\"right\">" + rs.getString("mov_ctd") + "</td>";
                sHTML = sHTML + "<td align=\"right\">" + Utilidades.redondear(rs.getDouble("mov_precio")) + "</td>";
                sHTML = sHTML + "<td align=\"right\">" + Utilidades.redondear(rs.getInt("mov_ctd") * rs.getDouble("mov_precio")) + "</td>";
                sHTML = sHTML + "</tr>";
                tabla = new Table(0, sHTML);
                Contents.add(tabla);
            }
            if (Contents.isEmpty()) {
                sHTML = "<tr><td colspan=7><center>" + resource.getMessage("mercado.historico.sinmovima") + "</center><td></tr>";
                tabla = new Table(0, sHTML);
                Contents.add(tabla);
            }

            return Contents;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMercadoHistorico. getMercadoHistorico. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public ArrayList<MercadoHistorico> getMercadoHistoricoCompras(Abadia abadiaDB, Usuario usuario, MessageResources resource, String sTipoProducto) throws AbadiaException {
        ArrayList<MercadoHistorico> alCompras = new ArrayList<MercadoHistorico>();
        MercadoHistorico mercado;
        String sSQL = null;
        String sTipo = "";

        ResultSet rs = null;
        CallableStatement cs = null;
        try {
            if (sTipoProducto.equals(Constantes.MERCANCIA_ALIMENTOS_STR)) {
                sSQL = "call recuperarHistoricoComprasAlimentos(?,?)";
                sTipo = resource.getMessage("formulario.alta.mercado.alimentos");
            } else if (sTipoProducto.equals(Constantes.MERCANCIA_ANIMALES_STR)) {
                sSQL = "call recuperarHistoricoComprasAnimales(?,?)";
                sTipo = resource.getMessage("formulario.alta.mercado.animales");
            } else if (sTipoProducto.equals(Constantes.MERCANCIA_RECURSOS_STR)) {
                sSQL = "call recuperarHistoricoComprasRecursos(?,?)";
                sTipo = resource.getMessage("formulario.alta.mercado.recursos");
            }

            // Cargar los mensajes
            cs = con.prepareCall(sSQL);

            int parNo = 1;
            cs.setLong(parNo++, abadiaDB.getIdDeAbadia());
            cs.setShort(parNo, usuario.getIdDeIdioma());
            cs.execute();

            rs = cs.getResultSet();

            while (rs.next()) {
                mercado = new MercadoHistorico();
                mercado.setAccion(resource.getMessage("mercado.compra"));
                mercado.setTipo(sTipo);
                mercado.setAbadia(rs.getString("NOMBRE_ABADIA"));
                mercado.setFecha(Utilidades.formatStringFromDB(rs.getString("MOV_FECHA")));
                mercado.setDescripcion(rs.getString("DESC_PRODUCTO"));
                mercado.setCantidadD(rs.getDouble("MOV_CTD"));
                mercado.setCantidad(Utilidades.redondear(mercado.getCantidadD()));
                mercado.setPrecioUnidadD(rs.getDouble("MOV_PRECIO"));
                mercado.setPrecioUnidad(Utilidades.redondear(mercado.getPrecioUnidadD()));
                mercado.setPrecioTotalD(rs.getDouble("MOV_CTD") * rs.getDouble("MOV_PRECIO"));
                mercado.setPrecioTotal(Utilidades.redondear(mercado.getPrecioTotalD()));

                alCompras.add(mercado);
            }
            return alCompras;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMercadoHistorico. getMercadoHistoricoComprasAlimentos. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(cs);
        }

    }

    public ArrayList<MercadoHistorico> getMercadoHistoricoComprasProducto(Abadia abadiaDB, Usuario usuario, MessageResources resource, String sTipoProducto, String sProducto) throws AbadiaException {
        ArrayList<MercadoHistorico> alCompras = new ArrayList<MercadoHistorico>();
        MercadoHistorico mercado;
        String sSQL = null;
        String sTipo = "";

        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            if (sTipoProducto.equals(Constantes.MERCANCIA_ALIMENTOS_STR)) {
                sSQL = "SELECT mm.fecha as MOV_FECHA , mm.tipo as MOV_TIPO, mm.CTD as MOV_CTD, mm.Precio_Unidad as MOV_PRECIO, a.nombre as NOMBRE_ABADIA, CONCAT(l1.literal, ' : ', l2.literal) as DESC_PRODUCTO " +
                        " FROM mercados m, mercados_movima mm, abadia a, mercados_alimentos ma, alimentos_familia af, alimentos_tipo at, literales l1, literales l2  " +
                        " WHERE m.productoid = mm.productoid and m.productoid = ? AND mm.abadiaid = ? and m.mercancia = '" + Constantes.MERCANCIA_ALIMENTOS_STR + "' and a.abadiaid = m.abadiaid and " +
                        " ma.alimentoid = at.alimentoid and at.familiaid = af.familiaid and " +
                        " l1.idiomaid = ? AND l2.idiomaid = ? AND l1.literalid = af.literalid AND l2.literalid=at.literalid AND " +
                        " ma.productoid = m.productoid order by mm.fecha desc ";
                sTipo = resource.getMessage("formulario.alta.mercado.alimentos");
            } else if (sTipoProducto.equals(Constantes.MERCANCIA_ANIMALES_STR)) {
                sSQL = "SELECT mm.fecha as MOV_FECHA , mm.tipo as MOV_TIPO, mm.CTD as MOV_CTD, mm.Precio_Unidad as MOV_PRECIO, a.nombre as NOMBRE_ABADIA, CONCAT(l1.literal, ' : ', l2.literal) as DESC_PRODUCTO " +
                        " FROM mercados m, mercados_movima mm, abadia a, mercados_animales ma, animales_crecimiento ac, animales_tipo at, literales l1, literales l2  " +
                        " WHERE ma.productoid = m.productoid and m.productoid = ? and ma.tipo_animalid = ac.tipo_animalid and ma.nivel = ac.nivel and ma.tipo_animalid = at.tipo_animalid and mm.abadiaid = ? and m.mercancia = '" + Constantes.MERCANCIA_ANIMALES_STR + "' and a.abadiaid = m.abadiaid and " +
                        " l1.idiomaid = ? AND l2.idiomaid = ? AND l1.literalid = at.literalid AND l2.literalid=ac.literalid AND " +
                        " mm.productoid = ma.productoid order by mm.fecha desc ";
                sTipo = resource.getMessage("formulario.alta.mercado.animales");
            } else if (sTipoProducto.equals(Constantes.MERCANCIA_RECURSOS_STR)) {
                sSQL = "SELECT mm.fecha as MOV_FECHA , mm.tipo as MOV_TIPO, mm.CTD as MOV_CTD, mm.Precio_Unidad as MOV_PRECIO, a.nombre as NOMBRE_ABADIA, l1.literal as DESC_PRODUCTO " +
                        " FROM mercados m, mercados_movima mm, abadia a, mercados_recursos mr, recurso_tipo rt, literales l1  " +
                        " WHERE m.productoid = mr.productoid and m.productoid = ? and rt.recursoid = mr.recursoid and mm.abadiaid = ? and a.abadiaid = m.abadiaid and m.mercancia = '" + Constantes.MERCANCIA_RECURSOS_STR + "' and " +
                        " rt.literalid = l1.literalid and l1.idiomaid = ? and l1.idiomaid = ? and mm.productoid = mr.productoid order by mm.fecha desc ";
                sTipo = resource.getMessage("formulario.alta.mercado.recursos");
            }

            // Cargar los mensajes
            ps = con.prepareStatement(sSQL);

            int parNo = 1;
            ps.setInt(parNo++, Integer.parseInt(sProducto));
            ps.setLong(parNo++, abadiaDB.getIdDeAbadia());
            ps.setShort(parNo++, usuario.getIdDeIdioma());
            ps.setShort(parNo, usuario.getIdDeIdioma());

            rs = ps.executeQuery();

            while (rs.next()) {
                mercado = new MercadoHistorico();
                mercado.setAccion(resource.getMessage("mercado.compra"));
                mercado.setTipo(sTipo);
                mercado.setAbadia(rs.getString("NOMBRE_ABADIA"));
                mercado.setFecha(Utilidades.formatStringFromDB(rs.getString("MOV_FECHA")));
                mercado.setDescripcion(rs.getString("DESC_PRODUCTO"));
                mercado.setCantidad(Utilidades.redondear(rs.getDouble("MOV_CTD")));
                mercado.setPrecioUnidad(Utilidades.redondear(rs.getDouble("MOV_PRECIO")));
                mercado.setPrecioTotal(Utilidades.redondear(rs.getDouble("MOV_CTD") * rs.getDouble("MOV_PRECIO")));

                alCompras.add(mercado);
            }
            return alCompras;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMercadoHistorico. getMercadoHistoricoComprasAlimentos. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public ArrayList<MercadoHistorico> getMercadoHistoricoVentas(Abadia abadiaDB, Usuario usuario, MessageResources resource, String sTipoProducto) throws AbadiaException {
        ArrayList<MercadoHistorico> alCompras = new ArrayList<MercadoHistorico>();
        MercadoHistorico mercado;
        String sSQL = null;
        String sTipo = "";

        ResultSet rs = null;
        CallableStatement cs = null;
        try {
            if (sTipoProducto.equals(Constantes.MERCANCIA_ALIMENTOS_STR)) {
                sSQL = "call recuperarHistoricoVentasAlimentos(?,?)";
                sTipo = resource.getMessage("formulario.alta.mercado.alimentos");

            } else if (sTipoProducto.equals(Constantes.MERCANCIA_ANIMALES_STR)) {
                sSQL = "call recuperarHistoricoVentasAnimales(?,?)";
                sTipo = resource.getMessage("formulario.alta.mercado.animales");

            } else if (sTipoProducto.equals(Constantes.MERCANCIA_RECURSOS_STR)) {
                sSQL = "call recuperarHistoricoVentasRecursos(?,?)";
                sTipo = resource.getMessage("formulario.alta.mercado.recursos");
            }

            // Cargar los mensajes
            cs = con.prepareCall(sSQL);

            int parNo = 1;
            cs.setLong(parNo++, abadiaDB.getIdDeAbadia());
            cs.setShort(parNo, usuario.getIdDeIdioma());
            //ps.setShort(parNo++, usuario.getIdDeIdioma());
            cs.execute();
            rs = cs.getResultSet();

            while (rs.next()) {
                mercado = new MercadoHistorico();
                mercado.setAccion(resource.getMessage("mercado.venta"));
                mercado.setTipo(sTipo);
                mercado.setAbadia(rs.getString("NOMBRE_ABADIA"));
                mercado.setFecha(Utilidades.formatStringFromDB(rs.getString("MOV_FECHA")));
                mercado.setDescripcion(rs.getString("DESC_PRODUCTO"));
                mercado.setCantidad(Utilidades.redondear(rs.getDouble("MOV_CTD")));
                mercado.setPrecioUnidad(Utilidades.redondear(rs.getDouble("MOV_PRECIO")));
                mercado.setPrecioTotal(Utilidades.redondear(rs.getDouble("MOV_CTD") * rs.getDouble("MOV_PRECIO")));

                alCompras.add(mercado);
            }
            return alCompras;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMercadoHistorico. getMercadoHistoricoVentas. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(cs);
        }

    }

    public ArrayList<MercadoHistorico> getMercadoHistoricoVentasProducto(Abadia abadiaDB, Usuario usuario, MessageResources resource, String sTipoProducto, String sProducto) throws AbadiaException {
        ArrayList<MercadoHistorico> alCompras = new ArrayList<MercadoHistorico>();
        MercadoHistorico mercado;
        String sSQL = null;
        String sTipo = "";

        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            if (sTipoProducto.equals(Constantes.MERCANCIA_ALIMENTOS_STR)) {
                sSQL = "SELECT mm.fecha as MOV_FECHA , mm.tipo as MOV_TIPO, mm.CTD as MOV_CTD, mm.Precio_Unidad as MOV_PRECIO, a.nombre as NOMBRE_ABADIA, CONCAT(l1.literal, ' : ', l2.literal) as DESC_PRODUCTO " +
                        " FROM mercados m, mercados_movima mm, abadia a, mercados_alimentos ma, alimentos_familia af, alimentos_tipo at, literales l1, literales l2  " +
                        " WHERE m.productoid = mm.productoid and m.productoid = ? AND m.abadiaid = ? and m.mercancia = '" + Constantes.MERCANCIA_ALIMENTOS_STR + "' and a.abadiaid = mm.abadiaid and " +
                        " ma.alimentoid = at.alimentoid and at.familiaid = af.familiaid and " +
                        " l1.idiomaid = ? AND l2.idiomaid = ? AND l1.literalid = af.literalid AND l2.literalid=at.literalid AND " +
                        " ma.productoid = m.productoid order by mm.fecha desc ";
                sTipo = resource.getMessage("formulario.alta.mercado.alimentos");

            } else if (sTipoProducto.equals(Constantes.MERCANCIA_ANIMALES_STR)) {
                sSQL = "SELECT mm.fecha as MOV_FECHA , mm.tipo as MOV_TIPO, mm.CTD as MOV_CTD, mm.Precio_Unidad as MOV_PRECIO, a.nombre as NOMBRE_ABADIA, CONCAT(l1.literal, ' : ', l2.literal) as DESC_PRODUCTO " +
                        " FROM mercados m, mercados_movima mm, abadia a, mercados_animales ma, animales_crecimiento ac, animales_tipo at, literales l1, literales l2  " +
                        " WHERE ma.productoid = m.productoid and m.productoid = ? and ma.tipo_animalid = ac.tipo_animalid and ma.nivel = ac.nivel and ma.tipo_animalid = at.tipo_animalid and m.abadiaid = ? and m.mercancia = '" + Constantes.MERCANCIA_ANIMALES_STR + "' and a.abadiaid = mm.abadiaid and " +
                        " l1.idiomaid = ? AND l2.idiomaid = ? AND l1.literalid = at.literalid AND l2.literalid=ac.literalid AND " +
                        " mm.productoid = ma.productoid order by mm.fecha desc ";
                sTipo = resource.getMessage("formulario.alta.mercado.animales");

            } else if (sTipoProducto.equals(Constantes.MERCANCIA_RECURSOS_STR)) {
                sSQL = "SELECT mm.fecha as MOV_FECHA , mm.tipo as MOV_TIPO, mm.CTD as MOV_CTD, mm.Precio_Unidad as MOV_PRECIO, a.nombre as NOMBRE_ABADIA, l1.literal as DESC_PRODUCTO " +
                        " FROM mercados m, mercados_movima mm, abadia a, mercados_recursos mr, recurso_tipo rt, literales l1  " +
                        " WHERE m.productoid = mr.productoid and m.productoid = ? and rt.recursoid = mr.recursoid and m.abadiaid = ? and a.abadiaid = mm.abadiaid and m.mercancia = '" + Constantes.MERCANCIA_RECURSOS_STR + "' and " +
                        " rt.literalid = l1.literalid and l1.idiomaid = ? and l1.idiomaid = ? and mm.productoid = mr.productoid order by mm.fecha desc ";
                sTipo = resource.getMessage("formulario.alta.mercado.recursos");
            }

            // Cargar los mensajes
            ps = con.prepareStatement(sSQL);

            int parNo = 1;
            ps.setInt(parNo++, Integer.parseInt(sProducto));
            ps.setLong(parNo++, abadiaDB.getIdDeAbadia());
            ps.setShort(parNo++, usuario.getIdDeIdioma());
            ps.setShort(parNo, usuario.getIdDeIdioma());

            rs = ps.executeQuery();

            while (rs.next()) {
                mercado = new MercadoHistorico();
                mercado.setAccion(resource.getMessage("mercado.venta"));
                mercado.setTipo(sTipo);
                mercado.setAbadia(rs.getString("NOMBRE_ABADIA"));
                mercado.setFecha(Utilidades.formatStringFromDB(rs.getString("MOV_FECHA")));
                mercado.setDescripcion(rs.getString("DESC_PRODUCTO"));
                mercado.setCantidad(Utilidades.redondear(rs.getDouble("MOV_CTD")));
                mercado.setPrecioUnidad(Utilidades.redondear(rs.getDouble("MOV_PRECIO")));
                mercado.setPrecioTotal(Utilidades.redondear(rs.getDouble("MOV_CTD") * rs.getDouble("MOV_PRECIO")));

                alCompras.add(mercado);
            }
            return alCompras;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMercadoHistorico. getMercadoHistoricoVentas. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

}
