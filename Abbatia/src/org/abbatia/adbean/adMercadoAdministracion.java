package org.abbatia.adbean;

import org.abbatia.actionform.MercadoAdminForm;
import org.abbatia.bean.Mercado;
import org.abbatia.bean.Tablas;
import org.abbatia.bean.Usuario;
import org.abbatia.core.CoreTiempo;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
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
 * Date: 06-feb-2005
 * Time: 11:53:10
 * To change this template use File | Settings | File Templates.
 */
public class adMercadoAdministracion extends adMercado {
    private static Logger log = Logger.getLogger(adMercadoAdministracion.class.getName());

    /**
     * Instancia la clase sin obtener una nueva conexión
     *
     * @param con
     * @throws AbadiaException
     */
    public adMercadoAdministracion(Connection con) throws AbadiaException {
        super(con);
    }


    /**
     * Devuelve un {@link ArrayList} de objetos {@link org.abbatia.actionform.MercadoAdminForm}
     * con los datos de los productos que la ciudad tiene puestos a la venta y que
     * podran ser administrados por los cardenales
     * responsables del mercado y por los propios administradores del juego.
     * Recupera los datos de forma sensible a la mercancia (Alimentos, recursos..)
     *
     * @param usuario   Datos del usuario que lanza la consulta para trabajar con el idioma correcto
     * @param mercancia Mercancia que queremos recuperar ('A' -> Alimentos, 'N' -> Animales, 'R' -> Recursos..)
     * @return
     * @throws AbadiaException Excepción base de abbatia
     */
    public ArrayList<MercadoAdminForm> getMercadoAdmin(Usuario usuario, int mercancia) throws AbadiaException {
        String sSQL = "";
        if (mercancia == Constantes.MERCANCIA_ALIMENTOS) {
            sSQL = "SELECT m.productoid, m.mercancia, l.literal, mcv.precio_minimo, mcv.precio_maximo, mcv.precio_actual, mcv.ventas_dia, mcv.precio_minimo_cardenal, mcv.precio_maximo_cardenal, l2.literal familia, mcv.nr_ventas_dia_anterior, mcv.nr_ventas " +
                    "FROM mercados m, mercados_alimentos ma, alimentos_tipo at, mercados_ciudad_variaciones mcv, literales l, literales l2, alimentos_familia af " +
                    "WHERE m.productoid=ma.productoid and ma.alimentoid = at.alimentoid and mcv.productoid = m.productoid and l.literalid = at.literalid " +
                    "and l.idiomaid=" + usuario.getIdDeIdioma() + " and abadiaid=0 and at.familiaid = af.familiaid and l2.literalid = af.literalid and l2.idiomaid = " + usuario.getIdDeIdioma() + " order by familia";
        } else if (mercancia == Constantes.MERCANCIA_ANIMALES) {
            sSQL = "SELECT m.productoid, m.mercancia, l.literal, mcv.precio_minimo, mcv.precio_maximo, mcv.precio_actual, mcv.ventas_dia, mcv.precio_minimo_cardenal, mcv.precio_maximo_cardenal, ma.nivel, mcv.nr_ventas_dia_anterior, mcv.nr_ventas " +
                    "FROM mercados m, mercados_animales ma, animales_tipo at, mercados_ciudad_variaciones mcv, literales l " +
                    "WHERE m.productoid=ma.productoid and ma.tipo_animalid = at.tipo_animalid and mcv.productoid = m.productoid and l.literalid = at.literalid " +
                    "and l.idiomaid=" + usuario.getIdDeIdioma() + " and abadiaid=0 order by literal";
        } else if (mercancia == Constantes.MERCANCIA_RECURSOS) {
            sSQL = "SELECT m.productoid, m.mercancia, l.literal, mcv.precio_minimo, mcv.precio_maximo, mcv.precio_actual, mcv.ventas_dia, mcv.precio_minimo_cardenal, mcv.precio_maximo_cardenal, mcv.nr_ventas_dia_anterior, mcv.nr_ventas " +
                    "FROM mercados m, mercados_recursos ma, recurso_tipo at, mercados_ciudad_variaciones mcv, literales l " +
                    "WHERE m.productoid=ma.productoid and ma.recursoid = at.recursoid and mcv.productoid = m.productoid and l.literalid = at.literalid " +
                    "and l.idiomaid=" + usuario.getIdDeIdioma() + " and abadiaid=0 order by literal";
        }

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            MercadoAdminForm mercadoAdminForm = null;
            ArrayList<MercadoAdminForm> alMercadoAdmins = new ArrayList<MercadoAdminForm>();
            while (rs.next()) {
                mercadoAdminForm = new MercadoAdminForm();
                mercadoAdminForm.setProductoId(rs.getInt("PRODUCTOID"));
                mercadoAdminForm.setMercancia(rs.getString("MERCANCIA"));
                mercadoAdminForm.setDescripcion(rs.getString("LITERAL"));
                mercadoAdminForm.setPrecio(Utilidades.redondear(rs.getDouble("PRECIO_ACTUAL")));
                mercadoAdminForm.setPrecioMaximo(Utilidades.redondear(rs.getDouble("PRECIO_MAXIMO")));
                mercadoAdminForm.setPrecioMinimo(Utilidades.redondear(rs.getDouble("PRECIO_MINIMO")));
                mercadoAdminForm.setPrecioMaximoC(Utilidades.redondear(rs.getDouble("PRECIO_MAXIMO_CARDENAL")));
                mercadoAdminForm.setPrecioMinimoC(Utilidades.redondear(rs.getDouble("PRECIO_MINIMO_CARDENAL")));
                mercadoAdminForm.setVentasAyer(Utilidades.redondear(rs.getLong("NR_VENTAS_DIA_ANTERIOR")));
                mercadoAdminForm.setVentasHoy(Utilidades.redondear(rs.getLong("NR_VENTAS")));
                mercadoAdminForm.setNumeroVentas(rs.getInt("VENTAS_DIA"));
                if (mercancia == Constantes.MERCANCIA_ALIMENTOS)
                    mercadoAdminForm.setFamilia(rs.getString("FAMILIA"));
                if (mercancia == Constantes.MERCANCIA_ANIMALES)
                    mercadoAdminForm.setNivel(rs.getInt("NIVEL"));
                alMercadoAdmins.add(mercadoAdminForm);
            }
            return alMercadoAdmins;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMercadoAdministracion. getMercadoAdmin. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Devuelve un objeto {@link MercadoAdminForm} con las propiedades de un producto a la venta
     * en la ciudad a partir del identificador de producto
     *
     * @param usuario    Datos del usuario que lanza la consulta para trabajar con el idioma correcto
     * @param productoId Identificador de producto en el mercado
     * @return
     * @throws AbadiaException Excepción base de abbatia
     */
    public MercadoAdminForm getDatosProductoVenta(Usuario usuario, int productoId) throws AbadiaException {
        Mercado mercado = recuperarMercado(productoId);

        String sSQL = "";
        if (mercado.getMercancia().equals(Constantes.MERCANCIA_ALIMENTOS_STR)) {
            sSQL = "SELECT m.productoid, m.mercancia, l.literal, mcv.precio_minimo, mcv.precio_minimo_cardenal, mcv.precio_maximo, mcv.precio_maximo_cardenal, mcv.precio_actual, mcv.ventas_dia, m.ctd_actual, l2.literal familia, mcv.nr_ventas_dia_anterior, mcv.nr_ventas " +
                    "FROM mercados m, mercados_alimentos ma, alimentos_tipo at, mercados_ciudad_variaciones mcv, literales l, literales l2, alimentos_familia af " +
                    "WHERE m.productoid=ma.productoid and ma.alimentoid = at.alimentoid and mcv.productoid = m.productoid and l.literalid = at.literalid and at.familiaid = af.familiaid " +
                    "and l.idiomaid=" + usuario.getIdDeIdioma() + " and abadiaid=0 and m.productoid = ? and l2.literalid = af.literalid and l2.idiomaid = " + usuario.getIdDeIdioma();
        } else if (mercado.getMercancia().equals(Constantes.MERCANCIA_ANIMALES_STR)) {
            sSQL = "SELECT m.productoid, m.mercancia, l.literal, mcv.precio_minimo, mcv.precio_minimo_cardenal, mcv.precio_maximo, mcv.precio_maximo_cardenal, mcv.precio_actual, mcv.ventas_dia, m.ctd_actual, ma.nivel, mcv.nr_ventas_dia_anterior, mcv.nr_ventas " +
                    "FROM mercados m, mercados_animales ma, animales_tipo at, mercados_ciudad_variaciones mcv, literales l " +
                    "WHERE m.productoid=ma.productoid and ma.tipo_animalid = at.tipo_animalid and mcv.productoid = m.productoid and l.literalid = at.literalid " +
                    "and l.idiomaid=" + usuario.getIdDeIdioma() + " and abadiaid=0 and m.productoid = ?";
        } else if (mercado.getMercancia().equals(Constantes.MERCANCIA_RECURSOS_STR)) {
            sSQL = "SELECT m.productoid, m.mercancia, l.literal, mcv.precio_minimo, mcv.precio_minimo_cardenal, mcv.precio_maximo, mcv.precio_maximo_cardenal, mcv.precio_actual, mcv.ventas_dia, m.ctd_actual, mcv.nr_ventas_dia_anterior, mcv.nr_ventas " +
                    "FROM mercados m, mercados_recursos ma, recurso_tipo at, mercados_ciudad_variaciones mcv, literales l " +
                    "WHERE m.productoid=ma.productoid and ma.recursoid = at.recursoid and mcv.productoid = m.productoid and l.literalid = at.literalid " +
                    "and l.idiomaid=" + usuario.getIdDeIdioma() + " and abadiaid=0 and m.productoid = ?";
        }

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, productoId);
            rs = ps.executeQuery();
            MercadoAdminForm mercadoAdminForm = null;
            if (rs.next()) {
                mercadoAdminForm = new MercadoAdminForm();
                mercadoAdminForm.setProductoId(rs.getInt("PRODUCTOID"));
                mercadoAdminForm.setMercancia(rs.getString("MERCANCIA"));
                mercadoAdminForm.setDescripcion(rs.getString("LITERAL"));
                mercadoAdminForm.setPrecio(Utilidades.redondear(rs.getDouble("PRECIO_ACTUAL")));
                mercadoAdminForm.setPrecioMaximo(Utilidades.redondear(rs.getDouble("PRECIO_MAXIMO")));
                mercadoAdminForm.setPrecioMaximoC(Utilidades.redondear(rs.getDouble("PRECIO_MAXIMO_CARDENAL")));
                mercadoAdminForm.setPrecioMinimo(Utilidades.redondear(rs.getDouble("PRECIO_MINIMO")));
                mercadoAdminForm.setPrecioMinimoC(Utilidades.redondear(rs.getDouble("PRECIO_MINIMO_CARDENAL")));
                mercadoAdminForm.setNumeroVentas(rs.getInt("VENTAS_DIA"));
                mercadoAdminForm.setCantidad(rs.getInt("CTD_ACTUAL"));
                mercadoAdminForm.setVentasAyer(Utilidades.redondear(rs.getLong("nr_ventas_dia_anterior")));
                mercadoAdminForm.setVentasHoy(Utilidades.redondear(rs.getLong("mcv.nr_ventas")));

                if (mercado.getMercancia().equals(Constantes.MERCANCIA_ALIMENTOS_STR))
                    mercadoAdminForm.setFamilia(rs.getString("FAMILIA"));
                if (mercado.getMercancia().equals(Constantes.MERCANCIA_ANIMALES_STR))
                    mercadoAdminForm.setNivel(rs.getInt("NIVEL"));

            }
            return mercadoAdminForm;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMercadoAdministracion. getDatosProductoVenta. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Recibe un objeto {@link MercadoAdminForm} con los datos actualizados de un
     * registro de mercado
     *
     * @param mercadoForm Datos del usuario que lanza la consulta para trabajar con el idioma correcto
     * @param usuario     Usuario que está lanzando el proceso
     * @throws AbadiaException Excepción base de abbatia
     */
    public void actualizarMercadoAdmin(MercadoAdminForm mercadoForm, Usuario usuario) throws AbadiaException {

        String sSQLAdmin = "Update mercados_ciudad_variaciones SET PRECIO_MINIMO=?, PRECIO_MINIMO_CARDENAL=?, PRECIO_MAXIMO=?, PRECIO_MAXIMO_CARDENAL = ?, PRECIO_ACTUAL=?, VENTAS_DIA=? " +
                "WHERE PRODUCTOID=?";
        String sSQLCardenal = "UPDATE mercados_ciudad_variaciones SET PRECIO_MINIMO_CARDENAL=?, PRECIO_MAXIMO_CARDENAL=?, VENTAS_DIA=? " +
                "WHERE PRODUCTOID=?";

        PreparedStatement ps = null;

        try {
            //si el usuario es administardor
            if (usuario.getAdministrador() == 1) {
                ps = con.prepareStatement(sSQLAdmin);
                ps.setDouble(1, Utilidades.formatStringToDouble(mercadoForm.getPrecioMinimo()));
                ps.setDouble(2, Utilidades.formatStringToDouble(mercadoForm.getPrecioMinimo()));
                ps.setDouble(3, Utilidades.formatStringToDouble(mercadoForm.getPrecioMaximo()));
                ps.setDouble(4, Utilidades.formatStringToDouble(mercadoForm.getPrecioMaximo()));
                ps.setDouble(5, Utilidades.formatStringToDouble(mercadoForm.getPrecio()));
                ps.setInt(6, mercadoForm.getNumeroVentas());
                ps.setInt(7, mercadoForm.getProductoId());
                //Si el usuario es Cardenal
            } else {
                ps = con.prepareStatement(sSQLCardenal);
                ps.setDouble(1, Utilidades.formatStringToDouble(mercadoForm.getPrecioMinimoC()));
                ps.setDouble(2, Utilidades.formatStringToDouble(mercadoForm.getPrecioMaximoC()));
                ps.setInt(3, mercadoForm.getNumeroVentas());
                ps.setInt(4, mercadoForm.getProductoId());
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMercadoAdministracion. actualizarMercadoAdmin. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /**
     * Recibe un objeto {@link MercadoAdminForm} y utiliza toda la informacion
     * para insertar en las distintas tablas necesarias los datos de un nuevo producto a la venta
     *
     * @param mercadoForm Datos del usuario que lanza la consulta para trabajar con el idioma correcto
     * @return
     * @throws AbadiaException Excepción base de abbatia
     */
    public int addMercadoAdmin(MercadoAdminForm mercadoForm) throws AbadiaException {
        //En primer lugar damos de alta el producto en el mercado en función del
        //tipo de recurso
        Mercado mercado = new Mercado();
        mercado.setIdAbadia(0);
        mercado.setCtd_actual(5000);
        mercado.setCtd_inicial(5000);
        mercado.setEstado(0);
        mercado.setPrecio_actual(1);
        mercado.setPrecio_inicial(1);
        mercado.setFecha_inicial(CoreTiempo.getTiempoAbadiaString());
        mercado.setFecha_final(CoreTiempo.getTiempoAbadiaString());
        mercado.setFecha_caduca(CoreTiempo.getTiempoAbadiaString());
        mercado.setTipo(0);
        mercado.setMercancia(mercadoForm.getMercancia());
        if (mercadoForm.getMercancia().equals(Constantes.MERCANCIA_ALIMENTOS_STR)) {
            //valores para venta de alimentos
            mercado.setIdAlimento(mercadoForm.getAlimentoId());
        } else if (mercadoForm.getMercancia().equals(Constantes.MERCANCIA_ANIMALES_STR)) {
            //valores para la venta de animales
            mercado.setIdAnimalNivel(mercadoForm.getAnimalNivel());
            mercado.setIdAnimalTipo(mercadoForm.getAnimalTipo());
            mercado.setFecha_nacimiento(null);
            mercado.setAnimalSalud(100);
        } else if (mercadoForm.getMercancia().equals(Constantes.MERCANCIA_RECURSOS_STR)) {
            //valores para la venta de recursos
            mercado.setIdRecurso(mercadoForm.getRecursoId());
        } else if (mercadoForm.getMercancia().equals(Constantes.MERCANCIA_RELIQUIAS_STR)) {
            //valores para la venta de reliquias
            mercado.setIdReliquia(mercadoForm.getReliquiaId());
        } else if (mercadoForm.getMercancia().equals(Constantes.MERCANCIA_LIBROS_STR)) {
            //valores para la venta de libros
            mercado.setIdLibro(mercadoForm.getLibroId());
        }
        int idProducto = crearMercado(mercado);

        //falta crear los  datos de variaciones de mercado.
        String sSQL = "insert into mercados_ciudad_variaciones (PRODUCTOID, MERCANCIA, PRECIO_MINIMO, PRECIO_MAXIMO, PRECIO_ACTUAL, VENTAS_DIA, PRECIO_MINIMO_CARDENAL, PRECIO_MAXIMO_CARDENAL)" +
                "VALUES                                       (?,          ?,         ?,             ?,             ?,             ?,          ?,                      ?)";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idProducto);
            ps.setString(2, mercado.getMercancia());
            ps.setDouble(3, mercado.getPrecio_actual());
            ps.setDouble(4, mercado.getPrecio_actual());
            ps.setDouble(5, mercado.getPrecio_actual());
            ps.setInt(6, 10);
            ps.setDouble(7, mercado.getPrecio_actual());
            ps.setDouble(8, mercado.getPrecio_actual());
            ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMercadoAdministracion. addMercadoAdmin. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

        return idProducto;

    }

    /**
     * Devuelve un {@link ArrayList}  de objetos tabla con los productos que no están a la venta
     * en el mercado por tipo de mercancia.
     *
     * @param mercancia Identificador de mercancia: ('A' - > Animales, 'R' -> Recursos, 'N' -> Animales..)
     * @param usuario   Usuario de invoca el metodo para determinar el idioma de los literales
     * @return
     * @throws AbadiaException Excepción base de abbatia
     */
    public ArrayList<Tablas> getProductosSinVender(String mercancia, Usuario usuario) throws AbadiaException {
        String sSQL = "";
        ArrayList<Tablas> listaProductos = new ArrayList<Tablas>();
        Tablas tabla;
        if (mercancia.equals(Constantes.MERCANCIA_ALIMENTOS_STR)) {
            sSQL = "SELECT at.alimentoid as id, CONCAT(l2.literal, ' (', l.literal, ')') as descripcion " +
                    "FROM alimentos_tipo at, literales l, literales l2, alimentos_familia af " +
                    "WHERE at.literalid=l.literalid AND l.idiomaid=" + usuario.getIdDeIdioma() + " AND at.familiaid=af.familiaid " +
                    "AND af.literalid = l2.literalid AND l2.idiomaid=" + usuario.getIdDeIdioma() + " AND at.alimentoid not in " +
                    "(SELECT ma.alimentoid FROM mercados_alimentos ma, mercados m " +
                    " WHERE ma.productoid = m.productoid AND m.abadiaid=0) ";

        } else if (mercancia.equals(Constantes.MERCANCIA_ANIMALES_STR)) {
            sSQL = "SELECT concat(ac.tipo_animalid, ';', ac.nivel) as id, l.literal as descripcion " +
                    "FROM animales_crecimiento ac, literales l " +
                    "WHERE ac.literalid=l.literalid AND l.idiomaid = " + usuario.getIdDeIdioma() +
                    " AND concat(ac.tipo_animalid, ';', ac.nivel) not in " +
                    "(SELECT concat(ma.tipo_animalid, ';', ma.nivel) FROM mercados_animales ma, mercados m " +
                    "WHERE ma.productoid = m.productoid AND m.abadiaid=0)";

        } else if (mercancia.equals(Constantes.MERCANCIA_RECURSOS_STR)) {
            sSQL = "SELECT rt.recursoid as id, l.literal as descripcion " +
                    "FROM recurso_tipo rt, literales l " +
                    "WHERE rt.literalid=l.literalid AND l.idiomaid = " + usuario.getIdDeIdioma() +
                    " AND rt.recursoid not in " +
                    "(SELECT mr.recursoid FROM mercados_recursos mr, mercados m " +
                    "WHERE mr.productoid = m.productoid AND m.abadiaid=0)";

        } else if (mercancia.equals(Constantes.MERCANCIA_LIBROS_STR)) {
            tabla = new Tablas();
            tabla.setCodigo("0");
            tabla.setDescripcion("Ninguno");
            listaProductos.add(tabla);
            return listaProductos;

        } else if (mercancia.equals(Constantes.MERCANCIA_RELIQUIAS_STR)) {
            tabla = new Tablas();
            tabla.setCodigo("0");
            tabla.setDescripcion("Ninguno");
            listaProductos.add(tabla);
            return listaProductos;
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            tabla = new Tablas();
            tabla.setCodigo("0");
            tabla.setDescripcion("Ninguno");
            listaProductos.add(tabla);
            while (rs.next()) {
                tabla = new Tablas();
                tabla.setCodigo(rs.getString("ID"));
                tabla.setDescripcion(rs.getString("DESCRIPCION"));
                listaProductos.add(tabla);
            }
            return listaProductos;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMercadoAdministracion. getProductosSinVender. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

}
