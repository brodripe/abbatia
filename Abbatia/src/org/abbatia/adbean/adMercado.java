package org.abbatia.adbean;

import org.abbatia.actionform.DatosVentaActForm;
import org.abbatia.actionform.MercadoCompraForm;
import org.abbatia.adbean.base.adbeans;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Mercado;
import org.abbatia.bean.Usuario;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.ProductoNoEncontradoException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.Utilidades;
import org.apache.log4j.Logger;

import java.sql.*;
// Proyecto


public class adMercado extends adbeans {
    private static Logger log = Logger.getLogger(adUtils.class.getName());

    /**
     * Instancia la clase sin obtener una nueva conexión
     *
     * @param con
     * @throws AbadiaException
     */
    public adMercado(Connection con) throws AbadiaException {
        super(con);
    }


    public Mercado recuperarMercado(int idDeProducto) throws AbadiaException {
        //Definición de cadena sql de consulta
        String sSQL = "Select * from mercados where PRODUCTOID = ?";
        String sSQLAlimen = "Select * from mercados_alimentos where PRODUCTOID = ?";
        String sSQLAnimales = "Select * from mercados_animales where PRODUCTOID = ?";
        String sSQLLibros = "Select * from mercados_libros where PRODUCTOID = ?";
        String sSQLRecurso = "Select * from mercados_recursos where PRODUCTOID = ?";
        String sSQLReliquias = "Select * from mercados_reliquias where PRODUCTOID = ?";
        //creo un objeto de tipo Recurso
        Mercado mercado = new Mercado();
        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = (PreparedStatement) getConexion().prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave.

            int parNo = 1;
            ps.setInt(parNo, idDeProducto);
            //Lanzo la consulta y cargo el resultado en un resultset
            rs = (ResultSet) ps.executeQuery();
            //si la consulta encuentra la recurso....
            if (rs.next()) {
                //iniciamos el volcado de datos sobre
                //el objeto mercado.
                mercado.setIdProducto(rs.getInt("PRODUCTOID"));
                mercado.setIdAbadia(rs.getInt("ABADIAID"));
                mercado.setEstado(rs.getInt("ESTADO"));
                mercado.setMercancia(rs.getString("MERCANCIA"));
                mercado.setTipo(rs.getInt("TIPO"));
                mercado.setFecha_inicial(rs.getDate("FECHA_INICIAL").toString());
                mercado.setFecha_final(rs.getDate("FECHA_FINAL").toString());
                mercado.setCtd_inicial(rs.getInt("CTD_INICIAL"));
                mercado.setCtd_actual(rs.getInt("CTD_ACTUAL"));
                mercado.setPrecio_inicial(rs.getFloat("PRECIO_INICIAL"));
                mercado.setPrecio_actual(rs.getFloat("PRECIO_ACTUAL"));
                rs.close();

                // Alimento
                if (mercado.getMercancia().equals(Constantes.MERCANCIA_ALIMENTOS_STR)) {
                    ps = con.prepareStatement(sSQLAlimen);
                    ps.setInt(1, idDeProducto);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        mercado.setIdAlimento(rs.getInt("ALIMENTOID"));
                        mercado.setFecha_caduca(rs.getString("FECHA_CADUCIDAD"));
                    } else return null;
                }

                // Animales
                if (mercado.getMercancia().equals(Constantes.MERCANCIA_ANIMALES_STR)) {
                    ps = con.prepareStatement(sSQLAnimales);
                    ps.setInt(1, idDeProducto);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        mercado.setIdAnimal(rs.getInt("TIPO_ANIMALID"));
                        mercado.setIdAnimalNivel(rs.getInt("NIVEL"));
                        mercado.setIdAnimalTipo(rs.getInt("TIPO_ANIMALID"));
                        mercado.setFecha_nacimiento(rs.getString("FECHA_NACIMIENTO"));
                        mercado.setAnimalSalud(rs.getInt("SALUD"));
                    } else return null;
                }

                // Libro
                if (mercado.getMercancia().equals(Constantes.MERCANCIA_LIBROS_STR)) {
                    ps = con.prepareStatement(sSQLLibros);
                    ps.setInt(1, idDeProducto);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        mercado.setIdLibro(rs.getInt("TIPO_LIBROID"));
                    } else return null;
                }

                // Recursos
                if (mercado.getMercancia().equals(Constantes.MERCANCIA_RECURSOS_STR)) {
                    ps = getConexion().prepareStatement(sSQLRecurso);
                    ps.setInt(1, idDeProducto);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        mercado.setIdRecurso(rs.getInt("RECURSOID"));
                    } else return null;
                }

                // Reliquias
                if (mercado.getMercancia().equals(Constantes.MERCANCIA_RELIQUIAS_STR)) {
                    ps = con.prepareStatement(sSQLReliquias);
                    ps.setInt(1, idDeProducto);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        mercado.setIdReliquia(rs.getInt("TIPO_RELIQUIAID"));
                    } else return null;
                }

                //devolvemos el objeto Recurso informado.
                return mercado;
            }
            //si recurso no se localiza, devolveremos null
            return null;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMercado. recuperarMercado. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public int recuperarCtdMercado(int idDeProducto) throws AbadiaException {
        //Definición de cadena sql de consulta
        String sSQL = "Select * from mercados where PRODUCTOID = ?";
        //creo un objeto de tipo Recurso
        Mercado mercado = new Mercado();
        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave.

            int parNo = 1;
            ps.setInt(parNo, idDeProducto);
            //Lanzo la consulta y cargo el resultado en un resultset
            rs = ps.executeQuery();
            //si la consulta encuentra la recurso....
            if (rs.next()) {
                //iniciamos el volcado de datos sobre
                //el objeto mercado.
                return rs.getInt("CTD_ACTUAL");

            }
            //si recurso no se localiza, devolveremos null
            return 0;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMercado. recuperarCtdMercado. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }


    //dará de alta un objeto Mercado en la base de datos
    public int crearMercado(Mercado mercado) throws AbadiaException {
        String sSQL = "UPDATE `mercados` SET `ABADIAID` = ?,`ESTADO` = ?, `MERCANCIA` = ?, `TIPO` = ?, `FECHA_INICIAL` = ?, " +
                "`FECHA_FINAL` = ?, `CTD_INICIAL` = ?, `CTD_ACTUAL` = ?, `PRECIO_INICIAL` = ?, `PRECIO_ACTUAL` = ? " +
                " WHERE `PRODUCTOID` = ?";
        String sSQLAlim = "Insert Into mercados_alimentos ( PRODUCTOID, ALIMENTOID, ESTADO, FECHA_CADUCIDAD) Values (?, ?, ?, ?)";
        String sSQLRecu = "Insert Into mercados_recursos ( PRODUCTOID, RECURSOID) Values (?, ?)";
        String sSQLAnim = "Insert Into mercados_animales ( PRODUCTOID, TIPO_ANIMALID, NIVEL, FECHA_NACIMIENTO, SALUD) Values (?, ?, ?, ?, ?)";
        String sSQLLibr = "Insert Into mercados_libros ( PRODUCTOID, TIPO_LIBROID) Values (?, ?)";
        String sSQLReli = "Insert Into mercados_reliquias ( PRODUCTOID, TIPO_RELIQUIAID) Values (?, ?)";

        PreparedStatement ps = null;
        try {
            int Pid = nuevoProductoID();

            mercado.setIdProducto(Pid);

            ps = con.prepareStatement(sSQL);
            //asigno los valores
            int parNo = 1;
            ps.setLong(parNo++, mercado.getIdAbadia());
            ps.setInt(parNo++, mercado.getEstado());
            ps.setString(parNo++, mercado.getMercancia());
            ps.setInt(parNo++, mercado.getTipo());
            ps.setString(parNo++, mercado.getFecha_inicial());
            ps.setString(parNo++, mercado.getFecha_final());
            ps.setInt(parNo++, mercado.getCtd_inicial());
            ps.setInt(parNo++, mercado.getCtd_inicial());
            ps.setDouble(parNo++, mercado.getPrecio_inicial());
            ps.setDouble(parNo++, mercado.getPrecio_inicial());
            ps.setInt(parNo, Pid);
            // Ejecutarlo
            ps.execute();

            // Es un alimento???
            if (mercado.getMercancia().equals(Constantes.MERCANCIA_ALIMENTOS_STR)) {
                // Crear el v�nculo del mercado con el alimento tipo
                ps = con.prepareStatement(sSQLAlim);
                //asigno los valores
                parNo = 1;
                ps.setInt(parNo++, Pid);
                ps.setInt(parNo++, mercado.getIdAlimento());
                ps.setInt(parNo++, mercado.getEstado());
                ps.setString(parNo, mercado.getFecha_caduca());
                // Ejecutarlo
                ps.execute();
            }
            // Es un recurso???
            if (mercado.getMercancia().equals(Constantes.MERCANCIA_RECURSOS_STR)) {
                // Crear el v�nculo del mercado con el alimento tipo
                ps = con.prepareStatement(sSQLRecu);
                //asigno los valores
                parNo = 1;
                ps.setInt(parNo++, Pid);
                ps.setInt(parNo, mercado.getIdRecurso());
                // Ejecutarlo
                ps.execute();
            }

            // Es un Animal???
            if (mercado.getMercancia().equals(Constantes.MERCANCIA_ANIMALES_STR)) {
                // Crear el v�nculo del mercado con el alimento tipo
                ps = con.prepareStatement(sSQLAnim);
                //asigno los valores
                parNo = 1;
                ps.setInt(parNo++, Pid);
                ps.setInt(parNo++, mercado.getIdAnimalTipo());
                ps.setInt(parNo++, mercado.getIdAnimalNivel());
                ps.setString(parNo++, mercado.getFecha_nacimiento());
                ps.setInt(parNo, mercado.getAnimalSalud());
                // Ejecutarlo
                ps.execute();
            }

            // Es un Libro???
            if (mercado.getMercancia().equals(Constantes.MERCANCIA_LIBROS_STR)) {
                // Crear el v�nculo del mercado con el alimento tipo
                ps = con.prepareStatement(sSQLLibr);
                parNo = 1;
                ps.setInt(parNo++, Pid);
                ps.setInt(parNo, mercado.getIdLibro());
                ps.execute();
            }

            // Es una Reliquia???
            if (mercado.getMercancia().equals(Constantes.MERCANCIA_RELIQUIAS_STR)) {
                // Crear el v�nculo del mercado con el alimento tipo
                ps = con.prepareStatement(sSQLReli);
                parNo = 1;
                ps.setInt(parNo++, Pid);
                ps.setInt(parNo, mercado.getIdReliquia());
                ps.execute();
            }
            return Pid;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adMercado. crearMercado. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }


    /// Cojer el ID del producto autoicrementado
    public int nuevoProductoID() throws SQLException, AbadiaException {
        String sSQL = "Insert Into `mercados` (abadiaid, estado) values( 0,0)";

        Statement stmt = con.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
                java.sql.ResultSet.CONCUR_UPDATABLE);
        int Pid = -1;
        stmt.executeUpdate(sSQL, Statement.RETURN_GENERATED_KEYS);
        ResultSet rs = stmt.getGeneratedKeys();

        if (rs.next()) {
            Pid = rs.getInt(1);
        }

        rs.close();
        return Pid;
    }

    // Actualizar Cantidad
    public void actualizarCtdMercado(int idProducto, int ctd) throws AbadiaException {
        PreparedStatement ps = null;
        try {
            String sSQL = "Update mercados set CTD_ACTUAL = ? WHERE PRODUCTOID= ?";
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, ctd);
            ps.setLong(2, idProducto);
            ps.execute();
            if (ctd == 0) {
                sSQL = "Update mercados set ESTADO = 1 WHERE PRODUCTOID= ?";
                ps = con.prepareStatement(sSQL);
                ps.setLong(1, idProducto);
                ps.execute();
            }
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMercado. actualizarCtdMercado. error SQL", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    // Actualizar El precio
    public void actualizarPrecioMercado(int idProducto, double precio) throws AbadiaException {
        PreparedStatement ps = null;
        try {
            String sSQL = "Update mercados set PRECIO_ACTUAL = ? WHERE PRODUCTOID= ?";
            ps = con.prepareStatement(sSQL);
            ps.setDouble(1, precio);
            ps.setLong(2, idProducto);
            ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMercado. actualizarPrecioMercado. error SQL", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }


    public void eliminarMercado(Mercado mercado) throws AbadiaException {
        eliminarMercado(mercado.getIdProducto());
        eliminarMercadoTipo(mercado);
    }

    public void restarMercado(Mercado mercado, int cantidad) throws AbadiaException {
        String sSQL = "UPDATE mercados set CTD_ACTUAL = CTD_ACTUAL - ? WHERE PRODUCTOID = ? ";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave del monje
            int parNo = 1;
            ps.setInt(parNo++, cantidad);
            ps.setInt(parNo, mercado.getIdProducto());
            // Ejecutarlo
            ps.execute();

            int resto = recuperarCtdMercado(mercado.getIdProducto());
            if (resto < 1) {
                eliminarMercado(mercado.getIdProducto());
                eliminarMercadoTipo(mercado);
            }
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMercado. restarMercado. error SQL", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }


    //Elimina un objeto Recurso de la base de datos
    //a partir de la clave del Recurso
    public void eliminarMercado(int idDeProducto) throws AbadiaException {
        String sSQL = "Delete From mercados Where `PRODUCTOID` = ?";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave del monje
            int parNo = 1;
            ps.setInt(parNo, idDeProducto);
            // Ejecutarlo
            ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMercado. eliminarMercado. error SQL", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Elimina de las tablas mercados y mercados_alimentos los productos con el idDeProducto Recibido
     *
     * @param idDeProducto Identificador de producto
     * @throws AbadiaException Excepción general
     */
    public void eliminarAlimentoMercado(int idDeProducto) throws AbadiaException {
        String sSQLa = "Delete From mercados Where PRODUCTOID = " + idDeProducto;
        String sSQLb = "Delete from mercados_alimentos where productoid = " + idDeProducto;
        adUtils oUtilsAD;

        try {
            oUtilsAD = new adUtils(con);
            oUtilsAD.execSQL(sSQLa);
            oUtilsAD.execSQL(sSQLb);
        } catch (AbadiaException e) {
            throw new AbadiaSQLException("adMercado. eliminarAlimentoMercado. error SQL", e, log);
        }
    }

    public void eliminarMercadoTipo(Mercado mercado) throws AbadiaException {
        String sSQLAlimen = "delete from mercados_alimentos where PRODUCTOID = ?";
        String sSQLAnimales = "delete from mercados_animales where PRODUCTOID = ?";
        String sSQLLibros = "delete from mercados_libros where PRODUCTOID = ?";
        String sSQLRecurso = "delete from mercados_recursos where PRODUCTOID = ?";
        String sSQLReliquias = "delete from mercados_reliquias where PRODUCTOID = ?";
        PreparedStatement ps = null;
        try {
            if (mercado.getMercancia().equals("A")) {
                ps = con.prepareStatement(sSQLAlimen);
            } else if (mercado.getMercancia().equals("N")) {
                ps = con.prepareStatement(sSQLAnimales);
            } else if (mercado.getMercancia().equals(Constantes.MERCANCIA_LIBROS_STR)) {
                ps = con.prepareStatement(sSQLLibros);
            } else if (mercado.getMercancia().equals(Constantes.MERCANCIA_RELIQUIAS_STR)) {
                ps = con.prepareStatement(sSQLReliquias);
            } else if (mercado.getMercancia().equals("R")) {
                ps = con.prepareStatement(sSQLRecurso);
            }
            if (ps != null) {
                ps.setInt(1, mercado.getIdProducto());
                ps.execute();
            }

        } catch (SQLException e) {
            throw new AbadiaSQLException("adMercado. eliminarMercadoTipo. error SQL", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }


    }

    public DatosVentaActForm getDatosVenta(int idProducto, Abadia abadia) throws AbadiaException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        DatosVentaActForm datosVenta = new DatosVentaActForm();
        try {
            String sSQL = "Select * from mercados where PRODUCTOID = ? AND ABADIAID = ?";
            ps = con.prepareStatement(sSQL);
            ps.setLong(1, idProducto);
            ps.setLong(2, abadia.getIdDeAbadia());
            rs = ps.executeQuery();
            if (rs.next()) {
                log.debug("adMercado. getDatosVenta. encontrado ");
                datosVenta.setCantidad(rs.getInt("CTD_ACTUAL"));
                datosVenta.setPrecio(Utilidades.redondear(rs.getDouble("PRECIO_ACTUAL")));
                datosVenta.setMercancia(rs.getString("MERCANCIA"));
                datosVenta.setFechaInicio(rs.getString("FECHA_INICIAL"));
                datosVenta.setFechaFin(rs.getString("FECHA_FINAL"));
                datosVenta.setProductoID(idProducto);
                return getDescripcionProductoVenta(datosVenta);
            } else {
                throw new ProductoNoEncontradoException("adMercado. getDatosVenta. No se ha encotrado el producto a la venta: " + idProducto, null, log);
            }
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMercado. getDatosVenta. error SQL", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public MercadoCompraForm getDatosCompra(Usuario usuario, int idProducto) throws AbadiaException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        MercadoCompraForm datosVenta = new MercadoCompraForm();
        try {
            String sSQL = "Select * from mercados where PRODUCTOID = ? ";
            ps = con.prepareStatement(sSQL);
            ps.setLong(1, idProducto);
            rs = ps.executeQuery();
            if (rs.next()) {
                log.debug("adMercado. getDatosVenta. encontrado ");
                datosVenta.setCantidadDisp(rs.getInt("CTD_ACTUAL"));
                datosVenta.setCantidad(rs.getInt("CTD_ACTUAL"));
                datosVenta.setPrecio(Utilidades.redondear(rs.getDouble("PRECIO_ACTUAL")));
                datosVenta.setPrecioTotal(Utilidades.redondear(rs.getDouble("PRECIO_ACTUAL") * rs.getInt("CTD_ACTUAL")));
                datosVenta.setMercanciaDesc(rs.getString("MERCANCIA"));
                datosVenta.setProductoID(idProducto);
                datosVenta.setAbadiaid(rs.getInt("ABADIAID"));
                return getDescripcionProductoCompra(datosVenta, usuario);
            } else {
                throw new ProductoNoEncontradoException("adMercado. getDatosVenta. No se ha encotrado el producto a la venta: " + idProducto, null, log);
            }
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMercado. getDatosVenta. error SQL", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }


    public DatosVentaActForm getDescripcionProductoVenta(DatosVentaActForm productoVenta) throws AbadiaException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sSQL = "";
        try {
            if (productoVenta.getMercancia().equals(Constantes.MERCANCIA_ALIMENTOS_STR)) {
                sSQL = "SELECT af.descripcion as descfam, at.descripcion as descali " +
                        "FROM  alimentos_tipo at, alimentos_familia af, mercados m, mercados_alimentos ma " +
                        "WHERE af.familiaid=at.familiaid AND ma.productoid = m.productoid and ma.alimentoid =  at.alimentoid and m.productoid = ?";

            } else if (productoVenta.getMercancia().equals(Constantes.MERCANCIA_RECURSOS_STR)) {
                sSQL = "SELECT at.descripcion as descfam, at.descripcion as descali " +
                        "FROM  recurso_tipo at, mercados m, mercados_recursos ma " +
                        "WHERE ma.productoid = m.productoid and ma.recursoid = at.recursoid and m.productoid = ?";

            } else if (productoVenta.getMercancia().equals(Constantes.MERCANCIA_ANIMALES_STR)) {
                sSQL = "SELECT at.descripcion as descfam, ac.descripcion as descali " +
                        "FROM  animales_crecimiento ac, animales_tipo at, mercados m, mercados_animales ma " +
                        "WHERE m.productoid = ma.productoid AND ma.nivel = ac.NIVEL AND ma.TIPO_ANIMALID = ac.TIPO_ANIMALID AND at.TIPO_ANIMALID = ma.TIPO_ANIMALID AND m.productoid = ?";

            }
            ps = con.prepareStatement(sSQL);
            ps.setLong(1, productoVenta.getProductoID());
            rs = ps.executeQuery();
            if (rs.next()) {
                log.debug("adMercado. getDescripcionProductoVenta. encontrado ");
                productoVenta.setDescripcionFamilia(rs.getString("descfam"));
                productoVenta.setDescripcionProducto(rs.getString("descali"));
            } else {
                log.debug("adMercado. getDescripcionProductoVenta. No encontrado ");
            }
            return productoVenta;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMercado. getDescripcionProductoVenta. error SQL", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public String getDescripcionProducto(int idProducto, String sTipoMercancia) throws AbadiaException {
        String sSQL = "";
        String sDescripcion = "";
        adUtils utilsAD = null;
        try {
            if (sTipoMercancia.equals(Constantes.MERCANCIA_ALIMENTOS_STR)) {
                sSQL = "SELECT af.descripcion as descfam, at.descripcion as descali " +
                        "FROM  alimentos_tipo at, alimentos_familia af, mercados m, mercados_alimentos ma " +
                        "WHERE af.familiaid=at.familiaid AND ma.productoid = m.productoid and ma.alimentoid =  at.alimentoid and m.productoid = " + idProducto;

            } else if (sTipoMercancia.equals(Constantes.MERCANCIA_RECURSOS_STR)) {
                sSQL = "SELECT at.descripcion as descfam, at.descripcion as descali " +
                        "FROM  recurso_tipo at, mercados m, mercados_recursos ma " +
                        "WHERE ma.productoid = m.productoid and ma.recursoid = at.recursoid and m.productoid = " + idProducto;

            } else if (sTipoMercancia.equals(Constantes.MERCANCIA_ANIMALES_STR)) {
                sSQL = "SELECT at.descripcion as descfam, ac.descripcion as descali " +
                        "FROM  animales_crecimiento ac, animales_tipo at, mercados m, mercados_animales ma " +
                        "WHERE m.productoid = ma.productoid AND ma.nivel = ac.NIVEL AND ma.TIPO_ANIMALID = ac.TIPO_ANIMALID AND at.TIPO_ANIMALID = ma.TIPO_ANIMALID AND m.productoid = " + idProducto;

            }

            utilsAD = new adUtils(con);
            sDescripcion = utilsAD.getSQL(sSQL, "Null");
            return sDescripcion;

        } catch (AbadiaException e) {
            throw new AbadiaSQLException("adMercado. getDescripcionProducto. error SQL", e, log);
        } finally {
            //if (utilsAD != null) utilsAD.finalize();
        }

    }

    public MercadoCompraForm getDescripcionProductoCompra(MercadoCompraForm productoCompra, Usuario usuario) throws AbadiaException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sSQL = "";
        try {
            if (productoCompra.getMercanciaDesc().equals(Constantes.MERCANCIA_ALIMENTOS_STR)) {
                sSQL = "SELECT l1.literal as descfam, l2.literal as descpro, um.descripcion as descuni, ma.fecha_caducidad " +
                        "FROM  alimentos_tipo at, alimentos_familia af, mercados m, mercados_alimentos ma, unidad_medida as um, literales l1, literales l2 " +
                        "WHERE af.familiaid=at.familiaid AND ma.productoid = m.productoid and ma.alimentoid = at.alimentoid AND " +
                        "l1.idiomaid=" + usuario.getIdDeIdioma() + " AND l2.idiomaid=" + usuario.getIdDeIdioma() + " AND l1.literalid =af.literalid AND l2.literalid=at.literalid AND " +
                        "um.unidad_medida = at.unidad_medida and um.idiomaid=? and m.productoid = ?";

            } else if (productoCompra.getMercanciaDesc().equals(Constantes.MERCANCIA_RECURSOS_STR)) {
                sSQL = "SELECT '' as descfam, l1.literal as descpro, um.descripcion as descuni " +
                        "FROM  recurso_tipo at, mercados m, mercados_recursos ma, unidad_medida as um, literales l1 " +
                        "WHERE ma.productoid = m.productoid and ma.recursoid = at.recursoid AND " +
                        "l1.idiomaid=" + usuario.getIdDeIdioma() + " AND l1.literalid =at.literalid AND " +
                        "um.unidad_medida = at.unidad_medida and um.idiomaid=? and m.productoid = ?";

            } else if (productoCompra.getMercanciaDesc().equals(Constantes.MERCANCIA_ANIMALES_STR)) {
                sSQL = "SELECT l1.literal as descfam, l2.literal as descpro, um.descripcion as descuni " +
                        "FROM  animales_crecimiento ac, animales_tipo at, mercados m, mercados_animales ma, unidad_medida as um, literales l1, literales l2  " +
                        "WHERE m.productoid = ma.productoid AND ma.nivel = ac.NIVEL AND ma.TIPO_ANIMALID = ac.TIPO_ANIMALID AND at.TIPO_ANIMALID = ma.TIPO_ANIMALID AND " +
                        "l1.idiomaid=" + usuario.getIdDeIdioma() + " AND l2.idiomaid=" + usuario.getIdDeIdioma() + " AND l1.literalid =at.literalid AND l2.literalid=ac.literalid AND " +
                        "um.unidad_medida = at.unidad_medida and um.idiomaid=? AND m.productoid = ?";

            } else if (productoCompra.getMercanciaDesc().equals(Constantes.MERCANCIA_LIBROS_STR)) {
                sSQL = "SELECT '' as descfam, l.literal as descpro, um.descripcion as descuni " +
                        "FROM  libro_tipo at, literales l, mercados m, mercados_libros ma, unidad_medida as um " +
                        "WHERE ma.productoid = m.productoid and ma.tipo_libroid = at.tipo_libroid AND " +
                        "l.literalid = at.literalid AND l.idiomaid = " + usuario.getIdDeIdioma() + " AND " +
                        "um.unidad_medida = at.unidad_medida and um.idiomaid=? and m.productoid = ?";

            } else if (productoCompra.getMercanciaDesc().equals(Constantes.MERCANCIA_RELIQUIAS_STR)) {
                sSQL = "SELECT '' as descfam, l.literal as descpro, um.descripcion as descuni " +
                        "FROM  reliquias_tipo at, literales l, mercados m, mercados_reliquias ma, unidad_medida as um " +
                        "WHERE ma.productoid = m.productoid and ma.tipo_reliquiaid = at.tipo_reliquiaid AND " +
                        "l.literalid = at.literalid AND l.idiomaid = " + usuario.getIdDeIdioma() + " AND " +
                        "um.unidad_medida = at.unidad_medida and um.idiomaid=? and m.productoid = ?";
            }
            ps = con.prepareStatement(sSQL);
            ps.setLong(1, usuario.getIdDeIdioma());
            ps.setLong(2, productoCompra.getProductoID());
            rs = ps.executeQuery();
            if (rs.next()) {
                productoCompra.setDescripcionFamilia(rs.getString("descfam"));
                productoCompra.setDescripcionProducto(rs.getString("descpro"));
                productoCompra.setDescripcionUnidad(rs.getString("descuni"));
                if (productoCompra.getMercanciaDesc().equals(Constantes.MERCANCIA_ALIMENTOS_STR)) {
                    productoCompra.setFechaCaducidad(rs.getString("fecha_caducidad"));
                }

            } else {
                log.debug("adMercado. getDescripcionProductoCompra. No encontrado ");
            }
            return productoCompra;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMercado. getDescripcionProductoCompra. error SQL", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public void eliminarMercadosAbadia(long idAbadia) throws AbadiaException {

        String sSQLAlimen = "delete from mercados_alimentos where PRODUCTOID in (select PRODUCTOID from mercados where ABADIAID = ?)";
        //String sSQLAlimen = "delete mercados_alimentos.* from mercados_alimentos ma, mercados m where ma.PRODUCTOID = m.PRODUCTOID and m.ABADIAID = ?";
        String sSQLAnimales = "delete from mercados_animales where PRODUCTOID in (select PRODUCTOID from mercados where ABADIAID = ?)";
        //String sSQLAnimales = "delete mercados_animales.* from mercados_animales ma, mercados m where ma.PRODUCTOID = m.PRODUCTOID and m.ABADIAID = ?";
        String sSQLLibros = "delete from mercados_libros where PRODUCTOID in (select PRODUCTOID from mercados where ABADIAID = ?)";
        //String sSQLLibros = "delete mercados_libros.* from mercados_libros ml, mercados m where ml.PRODUCTOID = m.PRODUCTOID and m.ABADIAID = ?";
        String sSQLRecurso = "delete from mercados_recursos where PRODUCTOID in (select PRODUCTOID from mercados where ABADIAID = ?)";
        //String sSQLRecurso = "delete mercados_recursos.* from mercados_recursos mr, mercados m where mr.PRODUCTOID = m.PRODUCTOID and m.ABADIAID = ?";
        String sSQLReliquias = "delete from mercados_reliquias where PRODUCTOID in (select PRODUCTOID from mercados where ABADIAID = ?)";
        //String sSQLReliquias = "delete mercados_reliquias.* from mercados_reliquias mr, mercados m where mr.PRODUCTOID = m.PRODUCTOID and m.ABADIAID = ?";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQLAlimen);
            ps.setLong(1, idAbadia);
            ps.execute();

            ps = con.prepareStatement(sSQLAnimales);
            ps.setLong(1, idAbadia);
            ps.execute();

            ps = con.prepareStatement(sSQLLibros);
            ps.setLong(1, idAbadia);
            ps.execute();

            ps = con.prepareStatement(sSQLReliquias);
            ps.setLong(1, idAbadia);
            ps.execute();

            ps = con.prepareStatement(sSQLRecurso);
            ps.setLong(1, idAbadia);
            ps.execute();

        } catch (SQLException e) {
            throw new AbadiaSQLException("adMercado. eliminarMercadosAbadia. error SQL", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }


    }

    public void eliminarMercadoAbadia(long idAbadia) throws AbadiaException {
        String sSQL = "DELETE FROM mercados where ABADIAID = ?";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setLong(1, idAbadia);
            ps.execute();

        } catch (SQLException e) {
            throw new AbadiaSQLException("adMercado. eliminarMercadosAbadia. error SQL", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public void eliminarMercadoMovimaAbadia(long idAbadia) throws AbadiaException {
        String sSQL = "DELETE FROM mercados_movima where ABADIAID = ?";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setLong(1, idAbadia);
            ps.execute();

        } catch (SQLException e) {
            throw new AbadiaSQLException("adMercado. eliminarMercadoMovimaAbadia. error SQL", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Recupera la mercancia a la que pertenece el producto recibido
     *
     * @param idProducto
     * @return
     * @throws AbadiaException
     */
    public String recuperarMercanciaProducto(int idProducto) throws AbadiaException {
        //Definición de cadena sql de consulta
        String sSQL = "Select mercancia from mercados where PRODUCTOID = " + idProducto;
        adUtils utils = new adUtils(con);
        String sMercancia = utils.getSQL(sSQL, Constantes.MERCANCIA_ALIMENTOS_STR);
        return sMercancia;
    }

    public double recuperarVolumenProducto(String tipo, String idProducto, int iTipoAnimal, int iNivelAnimal) throws AbadiaException {
        String sSQLAlimentos = "SELECT at.volumen " +
                " FROM alimentos_tipo as at, alimentos_familia as af, alimentos_lote as al, alimentos a " +
                " WHERE a.loteid = al.loteid AND  a.alimentoid = at.alimentoid AND at.familiaid = af.familiaid " +
                " AND al.loteid = " + idProducto;
        String sSQLRecursos = "SELECT rt.volumen FROM recurso_tipo as rt WHERE rt.recursoid = " + idProducto;
        String sSQLAnimales = "SELECT ac.volumen FROM animales_crecimiento as ac WHERE ac.nivel = " + iNivelAnimal + " AND ac.tipo_animalid = " + iTipoAnimal;

        double dResult = 0;
        adUtils utils = null;
        try {
            utils = new adUtils(con);
            if (tipo.equalsIgnoreCase(Constantes.MERCANCIA_ALIMENTOS_STR)) {
                dResult = utils.getSQL(sSQLAlimentos, dResult);
            } else if (tipo.equalsIgnoreCase(Constantes.MERCANCIA_ANIMALES_STR)) {
                dResult = utils.getSQL(sSQLAnimales, dResult);
            } else if (tipo.equalsIgnoreCase(Constantes.MERCANCIA_RECURSOS_STR)) {
                dResult = utils.getSQL(sSQLRecursos, dResult);
            } else dResult = 0;

            return dResult;
        } catch (AbadiaSQLException e) {
            log.error("adMercado. recuperarVolumenProducto. AbadiaSQLException", e);
            throw e;
        } finally {
            //if (utils!=null) utils.finalize();
        }
    }

    public double recuperarVolumenElemento(String tipo, String idProducto, int iTipoAnimal, int iNivelAnimal) throws AbadiaException {
        String sSQLAlimentos = "SELECT at.volumen " +
                " FROM alimentos_tipo as at, alimentos_familia as af, alimentos_lote as al, alimentos a " +
                " WHERE a.loteid = al.loteid AND  a.alimentoid = at.alimentoid AND at.familiaid = af.familiaid " +
                " AND al.loteid = " + idProducto;
        String sSQLRecursos = "SELECT rt.volumen FROM recurso_tipo as rt WHERE rt.recursoid = " + idProducto;
        String sSQLAnimales = "SELECT ac.volumen FROM animales_crecimiento as ac WHERE ac.nivel = " + iNivelAnimal + " AND ac.tipo_animalid = " + iTipoAnimal;

        double dResult = 0;
        adUtils utils;
        try {
            utils = new adUtils(con);
            if (tipo.equalsIgnoreCase(Constantes.MERCANCIA_ALIMENTOS_STR)) {
                dResult = utils.getSQL(sSQLAlimentos, dResult);
            } else if (tipo.equalsIgnoreCase(Constantes.MERCANCIA_ANIMALES_STR)) {
                dResult = utils.getSQL(sSQLAnimales, dResult);
            } else if (tipo.equalsIgnoreCase(Constantes.MERCANCIA_RECURSOS_STR)) {
                dResult = utils.getSQL(sSQLRecursos, dResult);
            } else dResult = 0;

            return dResult;
        } catch (AbadiaSQLException e) {
            log.error("adMercado. recuperarVolumenProducto. AbadiaSQLException", e);
            throw e;
        }
    }


}
