package org.abbatia.adbean;

import org.abbatia.actionform.BuscarMercadoCompraForm;
import org.abbatia.adbean.base.adbeans;
import org.abbatia.bean.*;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.NoImplementadoException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.HTML;
import org.abbatia.utils.Utilidades;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.util.MessageResources;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class adMercadoCompra extends adbeans {
    private static Logger log = Logger.getLogger(adUtils.class.getName());

    /**
     * Instancia la clase sin obtener una nueva conexión
     *
     * @param con
     * @throws AbadiaException
     */
    public adMercadoCompra(Connection con) throws AbadiaException {
        super(con);
    }

    /*   Visualiza la pantalla de compras
    */

    /**
     * Recuperación de los datos del mercado, permitiendo el filtrado.
     *
     * @param abadia
     * @param usuario
     * @param datosFiltro
     * @param resource
     * @return
     * @throws AbadiaException
     */
    public MercadoCompra getMercadoCompra(MessageResources resource, Abadia abadia, Usuario usuario, BuscarMercadoCompraForm datosFiltro) throws AbadiaException {
        MercadoCompra Contents = new MercadoCompra();
        ArrayList<Mercado> alCompra = new ArrayList<Mercado>();
        String sSQL, sSQLCount, sSQLSelect, sSQLFrom, sSQLWhere;
        log.debug("adMercadoCompra. getMercadoCompra. sMercancia: " + datosFiltro.getMercancia());
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            sSQL = null;
            sSQLCount = null;
            sSQLSelect = "SELECT a.abadiaid, a.nombre, a.regionid, r.descripcion as descregion, m.productoid, m.tipo, m.fecha_inicial, m.fecha_final, m.ctd_inicial, m.ctd_actual, " +
                    "m.precio_inicial, m.precio_actual, um.unidad_desc, um.descripcion as udesc ";
            sSQLFrom = "FROM `abadia` as a,  `region` as r, `mercados` as m, `unidad_medida` as um, literales l1, literales l2  ";
            sSQLWhere = "WHERE a.abadiaid = m.abadiaid AND a.regionid = r.regionid AND um.idiomaID = " + usuario.getIdDeIdioma() + " AND l1.idiomaID=" +
                    usuario.getIdDeIdioma() + " AND l2.idiomaID=" + usuario.getIdDeIdioma() + "  AND m.estado = 0 AND m.abadiaid <> " + abadia.getIdDeAbadia();
            // ALIMENTACION
            if (datosFiltro.getMercancia() == Constantes.MERCANCIA_ALIMENTOS) {
                sSQL = sSQLSelect + ", l1.literal descripcion, l2.literal as familia, ma.fecha_caducidad   " +
                        sSQLFrom + ", `mercados_alimentos` as ma, `alimentos_tipo` as at, `alimentos_familia` as af " +
                        sSQLWhere + " AND m.productoid = ma.productoid AND  ma.alimentoid = at.alimentoid AND at.familiaid = af.familiaid " +
                        " AND l1.literalid=at.literalid AND l2.literalid = af.literalid AND " +
                        " um.unidad_medida = at.unidad_medida AND m.mercancia='A'";
                sSQLCount = "SELECT Count(*) " +
                        sSQLFrom + ", `mercados_alimentos` as ma, `alimentos_tipo` as at, `alimentos_familia` as af " +
                        sSQLWhere + " AND m.productoid = ma.productoid AND  ma.alimentoid = at.alimentoid AND at.familiaid = af.familiaid " +
                        "AND l1.literalid=at.literalid AND l2.literalid = af.literalid AND um.unidad_medida = at.unidad_medida AND m.mercancia='A'";
            }
            // ANIMALES
            else if (datosFiltro.getMercancia() == Constantes.MERCANCIA_ANIMALES) {
                sSQL = sSQLSelect + ", l1.literal descripcion, l2.literal familia, ma.nivel, ma.salud   " +
                        sSQLFrom + ", `mercados_animales` as ma, `animales_tipo` as at, animales_crecimiento as ac " +
                        sSQLWhere + " AND m.productoid = ma.productoid AND  ma.tipo_animalid = at.tipo_animalid and ma.tipo_animalid=ac.tipo_animalid and ma.nivel=ac.nivel AND um.unidad_medida = at.unidad_medida " +
                        " AND l1.literalid=ac.literalid AND l2.literalid = at.literalid  AND m.mercancia='N' ";
                sSQLCount = "SELECT Count(*) " +
                        sSQLFrom + ", `mercados_animales` as ma, `animales_tipo` as at, animales_crecimiento as ac " +
                        sSQLWhere + " AND m.productoid = ma.productoid AND  ma.tipo_animalid = at.tipo_animalid and ma.tipo_animalid=ac.tipo_animalid " +
                        " AND l1.literalid=ac.literalid AND l2.literalid = at.literalid and ma.nivel=ac.nivel AND um.unidad_medida = at.unidad_medida  AND m.mercancia='N'";
            }
            // LIBROS
            else if (datosFiltro.getMercancia() == Constantes.MERCANCIA_LIBROS) {
                sSQL = sSQLSelect + ", l1.literal descripcion, '' as familia   " +
                        sSQLFrom + ", `mercados_libros` as ml, `libro_tipo` as at " +
                        sSQLWhere + " AND m.productoid = ml.productoid AND  ml.tipo_libroid = at.tipo_libroid AND um.unidad_medida = at.unidad_medida " +
                        " AND l1.literalid = at.literalid AND l2.literalid = at.literalid";
                sSQLCount = "SELECT Count(*) " +
                        sSQLFrom + ", `mercados_libros` as ml, `libro_tipo` as at " +
                        sSQLWhere + " AND m.productoid = ml.productoid AND  ml.tipo_libroid = at.tipo_libroid AND um.unidad_medida = at.unidad_medida " +
                        "AND l1.literalid=at.literalid AND l2.literalid = at.literalid ";
            }
            // RELIQUIAS
            else if (datosFiltro.getMercancia() == Constantes.MERCANCIA_RELIQUIAS) {
                sSQL = sSQLSelect + ", l1.literal descripcion, '' as familia  " +
                        sSQLFrom + ", `mercados_reliquias` as mr, `reliquias_tipo` as at " +
                        sSQLWhere + " AND m.productoid = mr.productoid AND  mr.reliquiaid = at.reliquiaid AND um.unidad_medida = at.unidad_medida " +
                        " AND l1.literalid=at.literalid AND l2.literalid = at.literalid ";
                sSQLCount = "SELECT Count(*) " +
                        sSQLFrom + ", `mercados_reliquias` as mr, `reliquias_tipo` as at " +
                        sSQLWhere + " AND m.productoid = mr.productoid AND  mr.reliquiaid = at.reliquiaid AND um.unidad_medida = at.unidad_medida " +
                        "AND l1.literalid=at.literalid AND l2.literalid = at.literalid ";
            }
            // RECURSOS
            else if (datosFiltro.getMercancia() == Constantes.MERCANCIA_RECURSOS) {
                sSQL = sSQLSelect + ",  l1.literal descripcion, '' as familia  " +
                        sSQLFrom + ", `mercados_recursos` as mr, `recurso_tipo` as at " +
                        sSQLWhere + " AND m.productoid = mr.productoid AND  mr.recursoid = at.recursoid AND um.unidad_medida = at.unidad_medida  AND m.mercancia='R'" +
                        " AND l1.literalid=at.literalid AND l2.literalid = at.literalid ";
                sSQLCount = "SELECT Count(*) " +
                        sSQLFrom + ", `mercados_recursos` as mr, `recurso_tipo` as at " +
                        sSQLWhere + " AND m.productoid = mr.productoid AND  mr.recursoid = at.recursoid AND um.unidad_medida = at.unidad_medida  AND m.mercancia='R' " +
                        "AND l1.literalid=at.literalid AND l2.literalid = at.literalid ";
            } else {
                throw new NoImplementadoException("adMercadoCompra.getMercadoCompraAgrupado. Esta mercancia no está implementada.", log);
            }

            if (sSQL != null) {
                log.debug("adMercadoCompra. sql a ejecutar: " + sSQL);
                sSQLWhere = "";
                // Mercado
                if (datosFiltro.getMercado() == Constantes.MERCADO_CIUDAD)
                    sSQLWhere = sSQLWhere + " AND a.abadiaid = 0";
                if (datosFiltro.getMercado() == Constantes.MERCADO_REGIONAL)
                    sSQLWhere = sSQLWhere + " AND r.regionid = " + abadia.getIdDeRegion();
                if (datosFiltro.getMercado() == Constantes.MERCADO_REGIONES)
                    sSQLWhere = sSQLWhere + " AND a.abadiaid <> 0";

                // Tipo (de momento no se utiliza....
                //if ((datosFiltro.getTipo() == 0)) sSQL = sSQL + " AND a.abadiaid = 0";

                // Filtros
                if (datosFiltro.getFiltro() == Constantes.FILTRO_PRECIO_MENOR) {
                    try {
                        Double.parseDouble(datosFiltro.getFiltrocontents());
                        sSQLWhere = sSQLWhere + " AND m.precio_actual <= " + datosFiltro.getFiltrocontents();
                    } catch (NumberFormatException nfe) { /* Ignoramos el filtro */ }
                }
                if (datosFiltro.getFiltro() == Constantes.FILTRO_PRECIO_MAYOR) {
                    try {
                        Double.parseDouble(datosFiltro.getFiltrocontents());
                        sSQLWhere = sSQLWhere + " AND m.precio_actual >= " + datosFiltro.getFiltrocontents();
                    } catch (NumberFormatException nfe) { /* Ignoramos el filtro */ }
                }
                if (datosFiltro.getFiltro() == Constantes.FILTRO_NOMBRE_ABADIA)
                    sSQLWhere = sSQLWhere + " AND a.nombre like '%" + datosFiltro.getFiltrocontents() + "%'";
                if (datosFiltro.getFiltro() == Constantes.FILTRO_NOMBRE_PRODUCTO)
                    sSQLWhere = sSQLWhere + " AND l1.literal like '%" + datosFiltro.getFiltrocontents() + "%'";
                if (datosFiltro.getFiltro() == Constantes.FILTRO_NOMBRE_REGION)
                    sSQLWhere = sSQLWhere + " AND r.descripcion like '%" + datosFiltro.getFiltrocontents() + "%'";

                // Ordern
                //if (sFiltro.compareTo("nombreproducto") == 0) sSQL = sSQL + " ORDER BY xxx";
                sSQL = sSQL + sSQLWhere;
                switch (datosFiltro.getOrdenid()) {
                    case 2:
                        if (datosFiltro.getOrden() == 1)
                            sSQL = sSQL + " ORDER BY ctd_actual ";
                        else sSQL = sSQL + " ORDER BY ctd_actual desc ";
                        break;
                    case 3:
                        if (datosFiltro.getOrden() == 1)
                            sSQL = sSQL + " ORDER BY precio_actual ";
                        else sSQL = sSQL + " ORDER BY precio_actual desc ";
                        break;
                    default:
                        sSQL = sSQL + " ORDER BY a.abadiaid, familia ";
                        break;
                }

                sSQL = sSQL + " LIMIT " + (datosFiltro.getPagina() * Constantes.REGISTROS_PAGINA) + "," + Constantes.REGISTROS_PAGINA;

                sSQLCount = sSQLCount + sSQLWhere;
                ps = getConexion().prepareStatement(sSQLCount);
                rs = ps.executeQuery();
                if (rs.next())
                    Contents.setCount(rs.getInt(1));

                Contents.setPage_actual(datosFiltro.getPagina());
                Contents.setPages(Contents.getCount() / Constantes.REGISTROS_PAGINA);
                //Contents.setNavigate( (new HTML()).getNavigateBar("mercado_compra_detalle.do", "action=inicio&filtro="+datosFiltro.getFiltro()+"&filtrocontents="+datosFiltro.getFiltrocontents(), Contents.getPage_actual(), Contents.getPages()) );
                Contents.setNavigate(HTML.getNavigateBarJavaScript("saltarPagina", Contents.getPage_actual(), Contents.getPages()));
                // Cargar los mensajes
                log.debug("adMercadoCompra. getMercadoCompra. SQL: " + sSQL);
                ps = con.prepareStatement(sSQL);
                rs = ps.executeQuery();

                Mercado mercado;

                while (rs.next()) {
                    mercado = new Mercado();
                    mercado.setMercancia("" + datosFiltro.getMercancia());
                    // Tratamiento para el tipo de venta
                    mercado.setTipo(rs.getInt("tipo"));
                    if (mercado.getTipo() == 0) {
                        mercado.setTipo_desc(resource.getMessage("mercado.venta"));
                    } else {
                        mercado.setTipo_desc(resource.getMessage("mercado.subasta"));
                    }
                    // Tratamiento especial para la ciudad
                    if (rs.getInt("abadiaid") == 0) {
                        mercado.setRegion("");
                        mercado.setAbadia(resource.getMessage("mercado.ciudad"));
                        mercado.setFecha_inicial("Sin inicio");
                        mercado.setFecha_final("No caduca");
                        mercado.setFecha_caduca("No caduca");
                    } else {
                        mercado.setRegion(rs.getString("descregion"));
                        mercado.setAbadia(rs.getString("Nombre"));
                        mercado.setFecha_inicial(Utilidades.formatStringFromDB(rs.getString("fecha_inicial")));
                        mercado.setFecha_final(Utilidades.formatStringFromDB(rs.getString("fecha_final")));
                        if (datosFiltro.getMercancia() == Constantes.MERCANCIA_ALIMENTOS) {
                            mercado.setFecha_caduca(Utilidades.formatStringFromDB(rs.getString("FECHA_CADUCIDAD")));
                        }
                    }

                    // Descripcion
                    mercado.setFamilia(rs.getString("familia"));
                    mercado.setDescripcion(rs.getString("descripcion"));

                    // Cantidad disponible
                    mercado.setCtd_actual(rs.getInt("ctd_actual"));
                    mercado.setUnidad_desc(rs.getString("unidad_desc"));
                    mercado.setPrecio_actual(rs.getDouble("precio_actual"));
                    mercado.setPrecio(Utilidades.redondear(mercado.getPrecio_actual()));
                    mercado.setIdProducto(rs.getInt("productoid"));

                    // Tratamiento especial por tipo de mercancia
                    if (datosFiltro.getMercancia() == Constantes.MERCANCIA_ANIMALES) {
                        mercado.setIdAnimalNivel(rs.getInt("nivel"));
                        mercado.setAnimalSalud(rs.getInt("salud"));
                        mercado.setBarra_HTML(HTML.smallBarra(mercado.getAnimalSalud() / 10, resource.getMessage("monjes.abadia.salud") + " " + mercado.getAnimalSalud() + " " + resource.getMessage("edificio.abadia.sacrificar.texto4") + " " + 100));
                    }

                    alCompra.add(mercado);
                }
            }

            Contents.setLstCompra(alCompra);
            return Contents;

        }
        catch (SQLException e) {
            throw new AbadiaSQLException("adMercadoCompra. getMercadoCompra. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }


    /**
     * Recuperación de los datos del mercado de forma agrupada.
     *
     * @param usuario
     * @param actionForm
     * @return
     * @throws AbadiaException
     */
    public MercadoCompra getMercadoCompraAgrupado(Usuario usuario, ActionForm actionForm) throws AbadiaException {
        MercadoCompra Contents = new MercadoCompra();
        ArrayList<MercadoAgrupado> alTable = new ArrayList<MercadoAgrupado>();
        String sSQL;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int mercancia = 0;
        int familiaAlimento = 0;
        String sSQLFilter = "";

        // Recuperación de los datos del filtro
        try {
            mercancia = (Integer) PropertyUtils.getSimpleProperty(actionForm, "mercancia");
            familiaAlimento = (Integer) PropertyUtils.getSimpleProperty(actionForm, "familiaAlimento");
            if (familiaAlimento != 0) {
                sSQLFilter = " AND af.familiaid = " + familiaAlimento;
            }


        } catch (Exception e) {
            throw new AbadiaSQLException("adMercadoCompra.getMercadoCompraAgrupado. Exception recuperando la mercancia.", e, log);
        }

        log.debug("adMercadoCompra. getMercadoCompraAgrupado. sMercancia: " + mercancia);

        try {
            // ALIMENTACION
            if (mercancia == Constantes.MERCANCIA_ALIMENTOS) {
                sSQL = "SELECT ma.alimentoid ID,  sum(m.ctd_actual) CTD, count(*) ventas, min(precio_actual) min_precio, max(precio_actual) max_precio, " +
                        "       max(unidad_desc) unidad_desc , max(l1.literal) familia, max(l2.literal) descripcion, '' nivel " +
                        " FROM mercados as m, unidad_medida as um, literales l1, literales l2, mercados_alimentos as ma, alimentos_tipo as at, alimentos_familia as af " +
                        " WHERE um.idiomaID = " + usuario.getIdDeIdioma() + " AND l1.idiomaID=" + usuario.getIdDeIdioma() + " AND l2.idiomaID=" + usuario.getIdDeIdioma() + " AND m.estado = 0 " +
                        "      AND m.productoid = ma.productoid AND  ma.alimentoid = at.alimentoid AND at.familiaid = af.familiaid AND um.unidad_medida = at.unidad_medida AND m.mercancia='A' " +
                        "      AND l1.literalid=af.literalid AND l2.literalid = at.literalid " + sSQLFilter +
                        " GROUP BY  ma.alimentoid " +
                        " ORDER BY familia, descripcion ";
            }
            // ANIMALES
            else if (mercancia == Constantes.MERCANCIA_ANIMALES) {
                sSQL = "SELECT ma.tipo_animalid id, ma.nivel, sum(m.ctd_actual) CTD, count(*) ventas, min(precio_actual) min_precio, " +
                        "      max(precio_actual) max_precio, max(unidad_desc) unidad_desc, l1.literal familia, l2.literal descripcion" +
                        " FROM `mercados` as m, `unidad_medida` as um, literales l1, `mercados_animales` as ma, `animales_tipo` as at, literales l2, `animales_crecimiento` as ac " +
                        " WHERE um.idiomaID = " + usuario.getIdDeIdioma() + " AND l1.idiomaID=" + usuario.getIdDeIdioma() + " AND m.estado = 0 AND m.productoid = ma.productoid " +
                        "      AND ma.tipo_animalid = at.tipo_animalid AND um.unidad_medida = at.unidad_medida AND m.mercancia='N' " +
                        "      AND l1.literalid = at.literalid  AND l2.literalid = ac.literalid AND l2.idiomaID=" + usuario.getIdDeIdioma() + " AND ac.tipo_animalid=ma.tipo_animalid AND ma.nivel=ac.nivel " +
                        " GROUP BY ma.tipo_animalid,ma.nivel " +
                        " ORDER BY familia,nivel,descripcion";
            }
            // RECURSOS
            else if (mercancia == Constantes.MERCANCIA_RECURSOS) {
                sSQL = "SELECT mr.recursoid ID, sum(m.ctd_actual) CTD, count(*) ventas, min(precio_actual) min_precio, " +
                        "       max(precio_actual) max_precio, max(unidad_desc) unidad_desc, '' familia, max(l1.literal) descripcion, '' nivel " +
                        "FROM mercados as m, unidad_medida as um, literales l1, mercados_recursos as mr, recurso_tipo as rt " +
                        "WHERE um.idiomaID = " + usuario.getIdDeIdioma() + " AND l1.idiomaID=" + usuario.getIdDeIdioma() + " AND m.estado = 0 AND m.productoid = mr.productoid " +
                        "AND mr.recursoid = rt.recursoid AND um.unidad_medida = rt.unidad_medida AND m.mercancia='R' AND l1.literalid = rt.literalid " +
                        "GROUP BY  mr.recursoid " +
                        "ORDER BY descripcion";
            }
            // LIBROS
            else if (mercancia == Constantes.MERCANCIA_LIBROS) {
                // TODO Query libros
                sSQL = "SELECT ma.alimentoid ID,  sum(m.ctd_actual) CTD, count(*) ventas, min(precio_actual) min_precio, max(precio_actual) max_precio, " +
                        "       max(unidad_desc) unidad_desc , max(l1.literal) familia, max(l2.literal) descripcion, '' nivel " +
                        "FROM mercados as m, unidad_medida as um, literales l1, literales l2, mercados_alimentos as ma, alimentos_tipo as at, alimentos_familia as af " +
                        "WHERE um.idiomaID = " + usuario.getIdDeIdioma() + " AND l1.idiomaID=" + usuario.getIdDeIdioma() + " AND l2.idiomaID=" + usuario.getIdDeIdioma() + " AND m.estado = 0 " +
                        "      AND m.productoid = ma.productoid AND  ma.alimentoid = at.alimentoid AND at.familiaid = af.familiaid AND um.unidad_medida = at.unidad_medida AND m.mercancia='A' " +
                        "      AND l1.literalid=af.literalid AND l2.literalid = at.literalid " +
                        "GROUP BY  ma.alimentoid " +
                        "ORDER BY familia, descripcion ";
            }
            // RELIQUIAS
            else if (mercancia == Constantes.MERCANCIA_RELIQUIAS) {
                // TODO Query reliquias
                sSQL = "SELECT ma.alimentoid ID,  sum(m.ctd_actual) CTD, count(*) ventas, min(precio_actual) min_precio, max(precio_actual) max_precio, " +
                        "       max(unidad_desc) unidad_desc , max(l1.literal) familia, max(l2.literal) descripcion, '' nivel " +
                        "FROM mercados as m, unidad_medida as um, literales l1, literales l2, mercados_alimentos as ma, alimentos_tipo as at, alimentos_familia as af " +
                        "WHERE um.idiomaID = " + usuario.getIdDeIdioma() + " AND l1.idiomaID=" + usuario.getIdDeIdioma() + " AND l2.idiomaID=" + usuario.getIdDeIdioma() + " AND m.estado = 0 " +
                        "      AND m.productoid = ma.productoid AND  ma.alimentoid = at.alimentoid AND at.familiaid = af.familiaid AND um.unidad_medida = at.unidad_medida AND m.mercancia='A' " +
                        "      AND l1.literalid=af.literalid AND l2.literalid = at.literalid " +
                        "GROUP BY  ma.alimentoid " +
                        "ORDER BY familia, descripcion ";
            } else {
                throw new NoImplementadoException("adMercadoCompra.getMercadoCompraAgrupado. Esta mercancia no está implementada.", log);
            }

            if (sSQL != null) {
                // Cargar los mensajes
                log.debug("adMercadoCompra. getMercadoCompraAgrupado. SQL: " + sSQL);
                ps = con.prepareStatement(sSQL);
                rs = ps.executeQuery();

                MercadoAgrupado mercado = null;

                while (rs.next()) {
                    mercado = new MercadoAgrupado();
                    mercado.setFamilia(rs.getString("familia"));
                    mercado.setDescripcion(rs.getString("descripcion"));
                    mercado.setPrecioMaximo(Utilidades.redondear(rs.getDouble("max_precio")));
                    mercado.setPrecioMinimo(Utilidades.redondear(rs.getDouble("min_precio")));
                    mercado.setVentas(rs.getString("ventas"));
                    mercado.setCantidad(rs.getString("ctd"));
                    mercado.setUnidadDescripcion(rs.getString("unidad_desc"));
                    mercado.setMercancia(mercancia);
                    mercado.setNivel(rs.getString("nivel"));

                    alTable.add(mercado);
                }
            }
            Contents.setLstCompra(alTable);
            return Contents;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adMercadoCompra. getMercadoCompraAgrupado. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /* Gestión de compra
    */

    //recupera el precio de un producto en la abbatia 0 (mercado)

    public double getPrecioMercado(Mercado datos) throws AbadiaException {
        String sSQLAlimentos = "SELECT m.precio_actual FROM  mercados m, mercados_alimentos ma WHERE m.productoid = ma.productoid and  ma.alimentoid=? and m.abadiaid=0";
        String sSQLAnimales = "SELECT m.precio_actual FROM  mercados m, mercados_animales ma WHERE m.productoid = ma.productoid and  ma.NIVEL=? and ma.TIPO_ANIMALID = ? and m.abadiaid=0";
        String sSQLRecursos = "SELECT m.precio_actual FROM  mercados m, mercados_recursos ma WHERE m.productoid = ma.productoid and  ma.recursoid=? and m.abadiaid=0";
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            if (datos.getMercancia().equals("A")) {
                ps = con.prepareStatement(sSQLAlimentos);
                ps.setInt(1, datos.getIdAlimento());
                rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getDouble("precio_actual");
                } else return 10000;
            } else if (datos.getMercancia().equals("N")) {
                ps = con.prepareStatement(sSQLAnimales);
                ps.setInt(1, datos.getIdAnimalNivel());
                ps.setInt(2, datos.getIdAnimalTipo());
                rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getDouble("precio_actual");
                } else return 10000;
            } else if (datos.getMercancia().equals("R")) {
                ps = con.prepareStatement(sSQLRecursos);
                ps.setInt(1, datos.getIdRecurso());
                rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getDouble("precio_actual");
                } else return 10000;
            } else return 100000;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMercadoCompra. getPrecioMercado. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }


}
