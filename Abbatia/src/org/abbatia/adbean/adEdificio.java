package org.abbatia.adbean;

import org.abbatia.actionform.AbadiaActForm;
import org.abbatia.actionform.DatosEdificioActForm;
import org.abbatia.actionform.DatosVentaAgrupadaActForm;
import org.abbatia.actionform.MercadoVentaActForm;
import org.abbatia.adbean.base.adbeans;
import org.abbatia.bean.*;
import org.abbatia.core.CoreTiempo;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.*;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.HTML;
import org.abbatia.utils.Utilidades;
import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static org.abbatia.utils.Constantes.*;

public class adEdificio extends adbeans {
    private static Logger log = Logger.getLogger(adEdificio.class.getName());


    /**
     * Instancia la clase sin obtener una nueva conexión
     *
     * @param con
     * @throws AbadiaException
     */
    public adEdificio(Connection con) throws AbadiaException {
        super(con);
    }

    //recupera el objeto Edificio cargado...

    public Edificio recuperarEdificio(int idDeEdificioTmp, Abadia abadia, Usuario usuario, boolean contenidos, MessageResources resource, String tab) throws AbadiaException {
        //Definición de cadena sql de consulta
        String sSQL = "Select e.EDIFICIOID, e.FECHACONSTRUCCION, e.ESTADO, e.NIVEL, e.ABADIAID, l.LITERAL as NOMBRE, e.TIPOEDIFICIOID, en.ALMACENAMIENTO, en.CAPACIDADVITAL, en.TIEMPO_CONSTRUCCION, et.GRAFICO_1, et.GRAFICO_2, e.MANTENIMIENTO " +
                "from edificio AS e, edificio_tipo et, edificio_nivel en, literales l " +
                "where en.NIVEL = e.NIVEL AND en.TIPOEDIFICIOID = e.TIPOEDIFICIOID AND e.TIPOEDIFICIOID = et.TIPOEDIFICIOID AND et.NOMBRE = l.LITERALID AND " +
                "e.EDIFICIOID = ? and e.ABADIAID=? and l.IDIOMAID = ? ";
        //creo un objeto de tipo Edificio
        Edificio edificio;
        Libro libro;

        adLibros libroAD;
        adAnimal animales;
        adAlimentoLotes alimentos;
        adCampo campo;
        adRecurso recursoAD;
        adReliquias reliquiasAD;
        adMolino molinoAD;
        adMercadoVenta mercadoAD;
        adAbadia abadiaAD;
        adMonje monjeAD;


        PreparedStatement ps = null;
        ResultSet rs = null;
        int almacenamiento_mas_libro = 0;
        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        try {
            ps = con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave.

            int parNo = 1;
            ps.setLong(parNo++, idDeEdificioTmp);
            ps.setLong(parNo++, abadia.getIdDeAbadia());
            ps.setInt(parNo, usuario.getIdDeIdioma());
            //Lanzo la consulta y cargo el resultado en un resultset
            rs = ps.executeQuery();
            //si la consulta encuentra la edificio....
            if (rs.next()) {
                libroAD = new adLibros(con);
                libro = libroAD.recuperaLibroTipo(300 + rs.getInt("TIPOEDIFICIOID"), rs.getInt("ABADIAID"));

                if (libro != null) {
                    almacenamiento_mas_libro = Utilidades.recuperarIncrementoEspacioEdificioPorLibro(rs.getInt("TIPOEDIFICIOID"), libro.getNivel(), rs.getInt("nivel"));
                }
                //iniciamos el volcado de datos sobre
                //el objeto edificio.
                edificio = new Edificio();
                edificio.setIdDeEdificio(rs.getInt("EDIFICIOID"));
                edificio.setFechaDeConstruccion(Utilidades.formatStringFromDB(rs.getDate("FECHACONSTRUCCION").toString()));
                edificio.setEstado(rs.getDouble("ESTADO"));
                edificio.setBarraEstado(HTML.smallBarra(HTML.getBarrasEstado((int) edificio.getEstado()), resource.getMessage("edificio.estado") + " " + Double.toString(edificio.getEstado())));
                edificio.setMantenimiento(rs.getInt("MANTENIMIENTO"));
                if (edificio.getMantenimiento() == EDIFICIO_MANTENIMIENTO_BASICO) {
                    edificio.setMantenimientoDesc(resource.getMessage("edificio.mantenimiento.tipo.basico"));
                } else if (edificio.getMantenimiento() == EDIFICIO_MANTENIMIENTO_INEXISTENTE) {
                    edificio.setMantenimientoDesc(resource.getMessage("edificio.mantenimiento.tipo.inexistente"));
                } else if (edificio.getMantenimiento() == EDIFICIO_MANTENIMIENTO_MODERADO) {
                    edificio.setMantenimientoDesc(resource.getMessage("edificio.mantenimiento.tipo.moderado"));
                } else if (edificio.getMantenimiento() == EDIFICIO_MANTENIMIENTO_INTENSIVO) {
                    edificio.setMantenimientoDesc(resource.getMessage("edificio.mantenimiento.tipo.completo"));
                }

                edificio.setNivel(rs.getInt("NIVEL"));
                edificio.setIdDeAbadia(rs.getInt("ABADIAID"));
                edificio.setNombre(rs.getString("NOMBRE"));
                edificio.setIdDeTipoDeEdificio(rs.getInt("TIPOEDIFICIOID"));
                // Nivel
                //edificio.setAlmacenamiento(rs.getInt("ALMACENAMIENTO"));
                edificio.setAlmacenamiento(rs.getInt("ALMACENAMIENTO"));
                edificio.setAlmacenamiento_plus(almacenamiento_mas_libro);
                //log.info("Busco el almacenamientoActual");
                edificio.setAlmacenamientoActual(recuperaAlmacenamientoActual(abadia.getIdDeAbadia(), edificio.getIdDeEdificio(), edificio.getIdDeTipoDeEdificio()));
                edificio.setAlmacenamientoActualStr(Utilidades.redondear(edificio.getAlmacenamientoActual()));
                //log.info("Almacenamiento actual en adedificio: " + edificio.getAlmacenamientoActual());
                edificio.setCapacidadVital(rs.getInt("CAPACIDADVITAL"));
                edificio.setTiempoConstruccion(rs.getInt("TIEMPO_CONSTRUCCION"));
                edificio.setGrafico_construccion(rs.getString("GRAFICO_1"));
                edificio.setGrafico_visualizacion(rs.getString("GRAFICO_2"));

                DatosConstruccion datos = getDatosConstruccion(edificio);
                edificio.setEnConstruccion(datos.getEnConstruccion());
                edificio.setFechaFinPrevista(datos.getFechaPrevistaFin());

                if (contenidos) {
                    // Recuperar los alimentos del edificio
                    List alContenido = null;
                    if (edificio.getIdDeTipoDeEdificio() == EDIFICIO_COCINA || edificio.getIdDeTipoDeEdificio() == EDIFICIO_GRANERO) {
                        alimentos = new adAlimentoLotes(con);
                        if (tab.equals("salar")) {
                            alContenido = alimentos.recuperarAlimentoSalables(edificio.getIdDeEdificio(), usuario);
                        } else {
                            alContenido = alimentos.recuperarAlimentoEdificioAgrupados(edificio.getIdDeEdificio(), usuario);
                        }
//            alimentos.finalize();
                    } else if (edificio.getIdDeTipoDeEdificio() == EDIFICIO_GRANJA || edificio.getIdDeTipoDeEdificio() == EDIFICIO_ESTABLO) {
                        animales = new adAnimal(con);
                        alContenido = animales.recuperarAnimalesEdificio(edificio.getIdDeEdificio(), usuario, resource);
                        //animales.finalize();
                    } else if (edificio.getIdDeTipoDeEdificio() == EDIFICIO_CAMPO) {
                        campo = new adCampo(con);
                        alContenido = campo.getCultivosAbadia(abadia, usuario);
                        //campo.finalize();
                    } else if ((edificio.getIdDeTipoDeEdificio() == EDIFICIO_ALMACEN) ||
                            (edificio.getIdDeTipoDeEdificio() == EDIFICIO_TALLER_ARTESANIA) ||
                            (edificio.getIdDeTipoDeEdificio() == EDIFICIO_TALLER_COSTURA)) {
                        recursoAD = new adRecurso(con);
                        alContenido = recursoAD.recuperarRecursos(edificio, usuario, resource);
                        //recursoAD.finalize();
                    } else if (edificio.getIdDeTipoDeEdificio() == EDIFICIO_BIBLIOTECA) {
                        libroAD = new adLibros(con);
                        alContenido = libroAD.recuperarLibrosPropios(abadia, usuario, resource);
                        //libroAD.finalize();
                    } else if (edificio.getIdDeTipoDeEdificio() == EDIFICIO_IGLESIA) {
                        reliquiasAD = new adReliquias(con);
                        alContenido = reliquiasAD.recuperarReliquias(abadia, usuario);
                        //reliquiasAD.finalize();
                    } else if (edificio.getIdDeTipoDeEdificio() == EDIFICIO_MOLINO) {
                        molinoAD = new adMolino(con);
                        alContenido = molinoAD.recuperaProductosMolino(abadia, usuario);
                        //molinoAD.finalize();
                    } else if (edificio.getIdDeTipoDeEdificio() == EDIFICIO_MERCADO) {
                        mercadoAD = new adMercadoVenta(con);
                        alContenido = mercadoAD.recuperarContenidoMercado(abadia.getIdDeAbadia(), usuario.getIdDeIdioma(), 0);
                        //mercadoAD.finalize();
                    } else if (edificio.getIdDeTipoDeEdificio() == EDIFICIO_CEMENTERIO) {
                        abadiaAD = new adAbadia(con);
                        if (tab.equals("velatorio") || (tab.equals("init"))) {
                            alContenido = abadiaAD.getMonjes(abadia.getIdDeAbadia(), Constantes.MONJES_MUERTOS, resource);
                        } else if (tab.equals("cementerio")) {
                            alContenido = abadiaAD.getMonjes(abadia.getIdDeAbadia(), Constantes.MONJES_CEMENTERIO, resource);
                        } else if (tab.equals("osario")) {
                            alContenido = abadiaAD.getMonjes(abadia.getIdDeAbadia(), Constantes.MONJES_OSARIO, resource);
                        } else {
                            alContenido = abadiaAD.getMonjes(abadia.getIdDeAbadia(), Constantes.MONJES_MUERTOS, resource);
                        }
                        //abadiaAD.finalize();
                    }
                    monjeAD = new adMonje(con);
                    edificio.setNumMonjesCementerio(monjeAD.getNumMonjesCementerio(abadia.getIdDeAbadia()));
                    edificio.setNumMonjesOsario(monjeAD.getNumMonjesOsario(abadia.getIdDeAbadia()));
                    edificio.setNumMonjesVelatorio(monjeAD.getNumMonjesVelatorio(abadia.getIdDeAbadia()));
                    edificio.setContenido(alContenido);
                }

                //devolvemos el objeto Edificio informado.
            } else
                throw new EdificioNotFoundException("adEdificio. recuperarEdificio. edificio no encontrado", null, log);//log.info("No se ha encontrado el edificio");
            return edificio;
        } catch (SQLException e) {
            //log.debug("LOGBRP - recuperarEdificio error SQL: " + e.getMessage());
            throw new AbadiaSQLException("adEdificio. recuperarEdificios " + e.getMessage(), e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public Edificio recuperarEdificio(int idEdificio, int idAbadia, int idIdioma) throws AbadiaException {
        //Definición de cadena sql de consulta
        String sSQL = "Select e.EDIFICIOID, e.FECHACONSTRUCCION, e.ESTADO, e.NIVEL, e.ABADIAID, l.LITERAL as NOMBRE, e.TIPOEDIFICIOID, en.ALMACENAMIENTO, en.CAPACIDADVITAL, en.TIEMPO_CONSTRUCCION, et.GRAFICO_1, et.GRAFICO_2 " +
                "from edificio AS e, edificio_tipo et, edificio_nivel en, literales l " +
                "where en.NIVEL = e.NIVEL AND en.TIPOEDIFICIOID = e.TIPOEDIFICIOID AND e.TIPOEDIFICIOID = et.TIPOEDIFICIOID AND et.NOMBRE = l.LITERALID AND " +
                "e.EDIFICIOID = ? and e.ABADIAID=? and l.IDIOMAID = ? ";
        //creo un objeto de tipo Edificio
        Edificio edificio = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        try {
            ps = con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave.

            int parNo = 1;
            ps.setInt(parNo++, idEdificio);
            ps.setInt(parNo++, idAbadia);
            ps.setInt(parNo, idIdioma);
            //Lanzo la consulta y cargo el resultado en un resultset
            rs = ps.executeQuery();
            //si la consulta encuentra la edificio....
            if (rs.next()) {
                //iniciamos el volcado de datos sobre
                edificio = new Edificio();
                //el objeto edificio.
                edificio.setIdDeEdificio(rs.getInt("EDIFICIOID"));
                edificio.setFechaDeConstruccion(Utilidades.formatStringFromDB(rs.getDate("FECHACONSTRUCCION").toString()));
                edificio.setEstado(rs.getInt("ESTADO"));
                edificio.setNivel(rs.getInt("NIVEL"));
                edificio.setIdDeAbadia(rs.getInt("ABADIAID"));
                edificio.setNombre(rs.getString("NOMBRE"));
                edificio.setIdDeTipoDeEdificio(rs.getInt("TIPOEDIFICIOID"));
                // Nivel
                edificio.setAlmacenamiento(rs.getInt("ALMACENAMIENTO"));
                edificio.setCapacidadVital(rs.getInt("CAPACIDADVITAL"));
                edificio.setTiempoConstruccion(rs.getInt("TIEMPO_CONSTRUCCION"));
                edificio.setGrafico_construccion(rs.getString("GRAFICO_1"));
                edificio.setGrafico_visualizacion(rs.getString("GRAFICO_2"));
            }

            return edificio;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adEdificio. recuperarEdificio " + e.getMessage(), e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            adbeans.cerrarStatement(ps);
        }
    }

    public Edificio recuperarEdificioPorProductoVenta(MercadoVentaActForm datosVenta, Abadia abadia, Usuario usuario) throws AbadiaException {
        adEdificio edificioAD = null;
        int idEdificio = 0;
        Edificio edificio;

        edificioAD = new adEdificio(con);
        if (datosVenta.getMercado().equals(MERCANCIA_ALIMENTOS_STR)) {
            idEdificio = edificioAD.recuperarIdEdificioPorLote(Integer.valueOf(datosVenta.getId()));
        } else if (datosVenta.getMercado().equals(MERCANCIA_ANIMALES_STR)) {
            idEdificio = edificioAD.recuperarIdEdificioPorTipoAnimal(datosVenta.getTipoAnimal(), abadia.getIdDeAbadia());
        } else if (datosVenta.getMercado().equals(MERCANCIA_RECURSOS_STR)) {
            idEdificio = edificioAD.recuperarIdEdificioPorRecurso(Integer.valueOf(datosVenta.getId()), abadia.getIdDeAbadia());
        }
        edificio = edificioAD.recuperarEdificio(idEdificio, abadia.getIdDeAbadia(), usuario.getIdDeIdioma());

        return edificio;

    }

    public Edificio recuperarEdificioPorProductoVentaAgrupada(DatosVentaAgrupadaActForm datosVenta, Abadia abadia, Usuario usuario) throws AbadiaException {
        adEdificio edificioAD = null;
        int idEdificio = 0;
        Edificio edificio;
        edificioAD = new adEdificio(con);
        if (datosVenta.getMercancia().equals(MERCANCIA_ALIMENTOS_STR)) {
            idEdificio = edificioAD.recuperarIdEdificioPorLote(datosVenta.getIdProducto());
        } else if (datosVenta.getMercancia().equals(MERCANCIA_RECURSOS_STR)) {
            idEdificio = edificioAD.recuperarIdEdificioPorRecurso(datosVenta.getIdProducto(), abadia.getIdDeAbadia());
        }
        edificio = edificioAD.recuperarEdificio(idEdificio, abadia.getIdDeAbadia(), usuario.getIdDeIdioma());

        return edificio;
    }

    //recupera el objeto Edificio cargado...

    public Edificio recuperarEdificioTipo(int idDeTipoEdificioTmp, Abadia abadia, Usuario usuario) throws AbadiaException {
        //Definición de cadena sql de consulta
        String sSQL = "SELECT e.FECHACONSTRUCCION, e.EDIFICIOID, e.ESTADO, e.NIVEL, e.ABADIAID, l.LITERAL as NOMBRE, e.TIPOEDIFICIOID, en.ALMACENAMIENTO, en.CAPACIDADVITAL, en.TIEMPO_CONSTRUCCION, et.GRAFICO_1 " +
                "from edificio e, edificio_tipo et, literales l, edificio_nivel en " +
                "WHERE e.TIPOEDIFICIOID = et.TIPOEDIFICIOID and en.TIPOEDIFICIOID = e.TIPOEDIFICIOID and en.NIVEL = e.NIVEL and et.NOMBRE=l.LITERALID and l.IDIOMAID = ? and " +
                "et.TIPOEDIFICIOID=? and e.ABADIAID=?";

        //String sSQL = "Select * from edificio where ABADIAID = ? AND TIPOEDIFICIOID = ?";
        //creo un objeto de tipo Edificio
        Edificio edificio = null;
        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave.

            int parNo = 1;
            ps.setInt(parNo++, usuario.getIdDeIdioma());
            ps.setInt(parNo++, idDeTipoEdificioTmp);
            ps.setInt(parNo, abadia.getIdDeAbadia());
            //Lanzo la consulta y cargo el resultado en un resultset
            rs = ps.executeQuery();
            //si la consulta encuentra la edificio....
            if (rs.next()) {
                //iniciamos el volcado de datos sobre
                //el objeto edificio.
                //edificio = recuperarEdificio( rs.getInt("EDIFICIOID"),  abbatia, usuario);
                edificio = new Edificio();
                edificio.setIdDeEdificio(rs.getInt("EDIFICIOID"));
                edificio.setFechaDeConstruccion(Utilidades.formatStringFromDB(rs.getDate("FECHACONSTRUCCION").toString()));
                edificio.setEstado(rs.getInt("ESTADO"));
                edificio.setNivel(rs.getInt("NIVEL"));
                edificio.setIdDeAbadia(rs.getInt("ABADIAID"));
                edificio.setNombre(rs.getString("NOMBRE"));
                edificio.setIdDeTipoDeEdificio(rs.getInt("TIPOEDIFICIOID"));
                edificio.setAlmacenamiento(rs.getInt("ALMACENAMIENTO"));
                edificio.setAlmacenamientoActual(recuperaAlmacenamientoActual(abadia.getIdDeAbadia(), edificio.getIdDeEdificio(), edificio.getIdDeTipoDeEdificio()));
                edificio.setCapacidadVital(rs.getInt("CAPACIDADVITAL"));
                edificio.setTiempoConstruccion(rs.getInt("TIEMPO_CONSTRUCCION"));
                edificio.setGrafico_visualizacion(rs.getString("GRAFICO_1"));

            }
            //devolvemos el objeto Edificio informado.
            return edificio;

        } catch (SQLException e) {
            throw new AbadiaSQLException("LOGBRP - No se pudo localizar el recuperarEdificios " + e.getMessage(), e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Recupera el identificador de edificio de un tipo determinado de una abadia determinada
     *
     * @param idTipoEdificio
     * @param idAbadia
     * @return
     * @throws AbadiaException
     */
    public int recuperarIdEdificioTipo(int idTipoEdificio, int idAbadia) throws AbadiaException {
        //Definición de cadena sql de consulta
        int idEdificio = 0;
        String sSQL = "SELECT edificioid " +
                "from edificio " +
                "WHERE tipoedificioid = " + idTipoEdificio + " and abadiaid = " + idAbadia;

        adUtils utils = new adUtils(con);
        idEdificio = utils.getSQL(sSQL, 0);

        return idEdificio;
    }


    /**
     * Recupera un objeto {@link Edificio} con los datos del edificio que tiene
     * asociado un edificio de la familia indicada
     *
     * @param abadia
     * @param idProducto
     * @param usuario
     * @return
     * @throws AbadiaException
     */
    public Edificio recuperarEdificioPorFamiliaAlimento(Abadia abadia, int idProducto, Usuario usuario) throws AbadiaException {
        //Definición de cadena sql de consulta

        String sSQL = "SELECT e.FECHACONSTRUCCION, e.EDIFICIOID, e.ESTADO, e.NIVEL, e.ABADIAID, l.LITERAL as NOMBRE, e.TIPOEDIFICIOID, en.ALMACENAMIENTO, en.CAPACIDADVITAL, en.TIEMPO_CONSTRUCCION, et.GRAFICO_1 " +
                "from edificio e, alimentos_familia af, alimentos_tipo at, edificio_tipo et, literales l, edificio_nivel en " +
                "WHERE e.TIPOEDIFICIOID = af.TIPOEDIFICIOID and en.TIPOEDIFICIOID = e.TIPOEDIFICIOID and en.NIVEL = e.NIVEL and af.familiaid = at.familiaid and e.TIPOEDIFICIOID = et.TIPOEDIFICIOID and et.NOMBRE=l.LITERALID and l.IDIOMAID = ? and " +
                "at.ALIMENTOID=? and e.ABADIAID=? and e.nivel > 0";

        //creo un objeto de tipo Edificio
        Edificio edificio = new Edificio();
        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = (PreparedStatement) con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave.
            int parNo = 1;
            ps.setInt(parNo++, usuario.getIdDeIdioma());
            ps.setLong(parNo++, idProducto);
            ps.setLong(parNo, abadia.getIdDeAbadia());

            //Lanzo la consulta y cargo el resultado en un resultset
            rs = (ResultSet) ps.executeQuery();
            //si la consulta encuentra la edificio....
            if (rs.next()) {
                edificio.setIdDeEdificio(rs.getInt("EDIFICIOID"));
                edificio.setFechaDeConstruccion(Utilidades.formatStringFromDB(rs.getDate("FECHACONSTRUCCION").toString()));
                edificio.setEstado(rs.getInt("ESTADO"));
                edificio.setNivel(rs.getInt("NIVEL"));
                edificio.setIdDeAbadia(rs.getInt("ABADIAID"));
                edificio.setNombre(rs.getString("NOMBRE"));
                edificio.setIdDeTipoDeEdificio(rs.getInt("TIPOEDIFICIOID"));
                edificio.setAlmacenamiento(rs.getInt("ALMACENAMIENTO"));
                edificio.setAlmacenamientoActual(recuperaAlmacenamientoActual(abadia.getIdDeAbadia(), edificio.getIdDeEdificio(), edificio.getIdDeTipoDeEdificio()));
                edificio.setCapacidadVital(rs.getInt("CAPACIDADVITAL"));
                edificio.setTiempoConstruccion(rs.getInt("TIEMPO_CONSTRUCCION"));
                edificio.setGrafico_visualizacion(rs.getString("GRAFICO_1"));
                //devolvemos el objeto Edificio informado.
                return edificio;
            }
            //si edificio no se localiza, devolveremos por defecto la cocina

            return recuperarEdificioTipo(EDIFICIO_COCINA, abadia, usuario);

        } catch (SQLException e) {
            log.error("adEdificio. recuperarEdificioPorFamiliaAlimento. SQLException ", e);
            throw new AbadiaSQLException("adEdificio. recuperarEdificioPorFamiliaAlimento. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Recupera el identificador de edificio de la abadia recibida donde se debe
     * almacenar un alimento determinado
     *
     * @param idAlimento
     * @param idAbadia
     * @return
     * @throws AbadiaException
     */
    public int recuperarIdEdificioPorFamiliaAlimento(int idAlimento, int idAbadia) throws AbadiaException {
        //Definición de cadena sql de consulta

        String sSQL = "SELECT e.FECHACONSTRUCCION, e.EDIFICIOID, e.ESTADO, e.NIVEL, e.ABADIAID, e.TIPOEDIFICIOID " +
                "from edificio e, alimentos_familia af, alimentos_tipo at " +
                "WHERE e.TIPOEDIFICIOID = af.TIPOEDIFICIOID and af.familiaid = at.familiaid and " +
                "at.ALIMENTOID=? and e.ABADIAID=? and e.nivel > 0";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = (PreparedStatement) con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave.
            int parNo = 1;
            ps.setLong(parNo++, idAlimento);
            ps.setInt(parNo, idAbadia);

            //Lanzo la consulta y cargo el resultado en un resultset
            rs = (ResultSet) ps.executeQuery();
            //si la consulta encuentra la edificio....
            if (rs.next()) {
                return rs.getInt("EDIFICIOID");
            }
            //si edificio no se localiza, devolveremos por defecto la cocina

            return 0;

        } catch (SQLException e) {
            log.error("adEdificio. recuperarIdEdificioPorFamiliaAlimento. SQLException ", e);
            throw new AbadiaSQLException("adEdificio. recuperarIdEdificioPorFamiliaAlimento. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Recupera un objeto {@link Edificio} con los datos del edificio asociado al recurso
     * recibido por parametro
     *
     * @param abadia
     * @param idRecurso
     * @param usuario
     * @return
     * @throws AbadiaException
     */
    public Edificio recuperarEdificioPorRecurso(Abadia abadia, int idRecurso, Usuario usuario) throws AbadiaException {

        String sSQL = "SELECT e.FECHACONSTRUCCION, e.EDIFICIOID, e.ESTADO, e.NIVEL, e.ABADIAID, l.LITERAL as NOMBRE, e.TIPOEDIFICIOID, en.ALMACENAMIENTO, en.CAPACIDADVITAL, en.TIEMPO_CONSTRUCCION " +
                "from edificio e, edificio_tipo et, literales l, recurso_tipo rt, edificio_nivel en " +
                "WHERE e.TIPOEDIFICIOID = et.TIPOEDIFICIOID and en.TIPOEDIFICIOID = e.TIPOEDIFICIOID and en.NIVEL = e.NIVEL and et.NOMBRE=l.LITERALID and l.IDIOMAID = ? and " +
                "e.TIPOEDIFICIOID = rt.EDIFICIOID_ALMACEN and rt.RECURSOID = ? and e.ABADIAID=?";

        //creo un objeto de tipo Edificio
        Edificio edificio = new Edificio();
        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = (PreparedStatement) con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave.
            int parNo = 1;
            ps.setInt(parNo++, usuario.getIdDeIdioma());
            ps.setLong(parNo++, idRecurso);
            ps.setLong(parNo, abadia.getIdDeAbadia());

            //Lanzo la consulta y cargo el resultado en un resultset
            rs = (ResultSet) ps.executeQuery();
            //si la consulta encuentra la edificio....
            if (rs.next()) {
                //iniciamos el volcado de datos sobre
                //el objeto edificio.
                edificio.setIdDeEdificio(rs.getInt("EDIFICIOID"));
                edificio.setFechaDeConstruccion(Utilidades.formatStringFromDB(rs.getDate("FECHACONSTRUCCION").toString()));
                edificio.setEstado(rs.getInt("ESTADO"));
                edificio.setNivel(rs.getInt("NIVEL"));
                edificio.setIdDeAbadia(rs.getInt("ABADIAID"));
                edificio.setNombre(rs.getString("NOMBRE"));
                edificio.setIdDeTipoDeEdificio(rs.getInt("TIPOEDIFICIOID"));
                edificio.setAlmacenamiento(rs.getInt("ALMACENAMIENTO"));
                edificio.setAlmacenamientoActual(recuperaAlmacenamientoActual(abadia.getIdDeAbadia(), edificio.getIdDeEdificio(), edificio.getIdDeTipoDeEdificio()));
                edificio.setCapacidadVital(rs.getInt("CAPACIDADVITAL"));
                edificio.setTiempoConstruccion(rs.getInt("TIEMPO_CONSTRUCCION"));

                //devolvemos el objeto Edificio informado.
                return edificio;
            }
            //si edificio no se localiza, devolveremos null
            return null;

        } catch (SQLException e) {
            log.error("adEdificio. recuperarEdificioPorRecurso. SQLException ", e);
            throw new AbadiaSQLException("adEdificio. recuperarEdificioPorRecurso. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }


    }


    /**
     * Recupera el id del edificio asociado al recurso
     * recibido por parametro
     *
     * @param idRecurso
     * @param idAbadia
     * @return
     * @throws AbadiaException
     */
    public int recuperarIdEdificioPorRecurso(int idRecurso, int idAbadia) throws AbadiaException {

        String sSQL = "SELECT e.EDIFICIOID " +
                "FROM edificio e, recurso_tipo rt " +
                "WHERE e.TIPOEDIFICIOID = rt.EDIFICIOID_ALMACEN and rt.RECURSOID = ? and e.ABADIAID=?";

        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave.
            int parNo = 1;
            ps.setInt(parNo++, idRecurso);
            ps.setInt(parNo, idAbadia);
            //Lanzo la consulta y cargo el resultado en un resultset
            rs = (ResultSet) ps.executeQuery();
            //si la consulta encuentra la edificio....
            if (rs.next()) {
                return rs.getInt("EDIFICIOID");
            } else {
                throw new EdificioNotFoundException("adEdificio. recuperarIdEdificioPorRecurso. edificio no encontrado", null, log);
            }

        } catch (SQLException e) {
            log.error("adEdificio. recuperarIdEdificioPorRecurso. SQLException ", e);
            throw new AbadiaSQLException("adEdificio. recuperarIdEdificioPorRecurso. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);

        }


    }

    public Edificio recuperarEdificioPorTipoAnimal(Abadia abadia, int idTipoAnimal, Usuario usuario) throws AbadiaException {

        String sSQL = "SELECT e.FECHACONSTRUCCION, e.EDIFICIOID, e.ESTADO, e.NIVEL, e.ABADIAID, l.LITERAL as NOMBRE, e.TIPOEDIFICIOID, en.ALMACENAMIENTO, en.CAPACIDADVITAL, en.TIEMPO_CONSTRUCCION, et.GRAFICO_1 " +
                "from edificio e, edificio_tipo et, literales l, animales_tipo at, edificio_nivel en " +
                "WHERE e.TIPOEDIFICIOID = et.TIPOEDIFICIOID and en.TIPOEDIFICIOID = e.TIPOEDIFICIOID and en.NIVEL = e.NIVEL and et.NOMBRE=l.LITERALID and l.IDIOMAID = ? and " +
                "e.TIPOEDIFICIOID = at.TIPO_EDIFICIOID and at.TIPO_ANIMALID = ? and e.ABADIAID=?";

        //creo un objeto de tipo Edificio
        Edificio edificio = new Edificio();
        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = (PreparedStatement) con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave.
            int parNo = 1;
            ps.setInt(parNo++, usuario.getIdDeIdioma());
            ps.setLong(parNo++, idTipoAnimal);
            ps.setLong(parNo++, abadia.getIdDeAbadia());

            //Lanzo la consulta y cargo el resultado en un resultset
            rs = (ResultSet) ps.executeQuery();
            //si la consulta encuentra la edificio....
            if (rs.next()) {
                //iniciamos el volcado de datos sobre
                //el objeto edificio.
                edificio.setIdDeEdificio(rs.getInt("EDIFICIOID"));
                edificio.setFechaDeConstruccion(Utilidades.formatStringFromDB(rs.getDate("FECHACONSTRUCCION").toString()));
                edificio.setEstado(rs.getInt("ESTADO"));
                edificio.setNivel(rs.getInt("NIVEL"));
                edificio.setIdDeAbadia(rs.getInt("ABADIAID"));
                edificio.setNombre(rs.getString("NOMBRE"));
                edificio.setIdDeTipoDeEdificio(rs.getInt("TIPOEDIFICIOID"));
                edificio.setAlmacenamiento(rs.getInt("ALMACENAMIENTO"));
                edificio.setAlmacenamientoActual(recuperaAlmacenamientoActual(abadia.getIdDeAbadia(), edificio.getIdDeEdificio(), edificio.getIdDeTipoDeEdificio()));
                edificio.setCapacidadVital(rs.getInt("CAPACIDADVITAL"));
                edificio.setTiempoConstruccion(rs.getInt("TIEMPO_CONSTRUCCION"));
                edificio.setGrafico_visualizacion(rs.getString("GRAFICO_1"));

                //devolvemos el objeto Edificio informado.
                return edificio;
            }
            //si edificio no se localiza, devolveremos null
            return null;

        } catch (SQLException e) {
            log.error("adEdificio. recuperarEdificioPorRecurso. SQLException ", e);
            throw new AbadiaSQLException("adEdificio. recuperarEdificioPorRecurso. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /**
     * Recupera el identificador de edificio de la abadia que se recibe por parámetro
     * al que está solicado un tipo de animal determinado
     *
     * @param idTipoAnimal
     * @param idAbadia
     * @return
     * @throws AbadiaException
     */
    public int recuperarIdEdificioPorTipoAnimal(int idTipoAnimal, int idAbadia) throws AbadiaException {

        String sSQL = "SELECT e.FECHACONSTRUCCION, e.EDIFICIOID, e.ESTADO, e.NIVEL, e.ABADIAID, e.TIPOEDIFICIOID " +
                "from edificio e, animales_tipo at " +
                "WHERE e.TIPOEDIFICIOID = at.TIPO_EDIFICIOID and at.TIPO_ANIMALID = ? and e.ABADIAID=?";

        //creo un objeto de tipo Edificio
        Edificio edificio = new Edificio();
        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = (PreparedStatement) con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave.
            int parNo = 1;
            ps.setInt(parNo++, idTipoAnimal);
            ps.setInt(parNo++, idAbadia);

            //Lanzo la consulta y cargo el resultado en un resultset
            rs = (ResultSet) ps.executeQuery();
            //si la consulta encuentra la edificio....
            if (rs.next()) {
                //iniciamos el volcado de datos sobre
                //el objeto edificio.
                return rs.getInt("EDIFICIOID");
            }
            //si edificio no se localiza, devolveremos null
            return 0;

        } catch (SQLException e) {
            log.error("adEdificio. recuperarIdEdificioPorTipoAnimal. SQLException ", e);
            throw new AbadiaSQLException("adEdificio. recuperarIdEdificioPorTipoAnimal. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }


    }

    /**
     * Recupera el identificador de edificio de la abadia que se recibe por parámetro
     * al que está solicado un lote determinado
     *
     * @param idLote
     * @return
     * @throws AbadiaException
     */
    public int recuperarIdEdificioPorLote(int idLote) throws AbadiaException {

        String sSQL = "Select a.EDIFICIOID From alimentos a Where a.loteid = ? ";

        //creo un objeto de tipo Edificio
        Edificio edificio = new Edificio();
        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = (PreparedStatement) con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave.
            int parNo = 1;
            ps.setInt(parNo++, idLote);

            //Lanzo la consulta y cargo el resultado en un resultset
            rs = (ResultSet) ps.executeQuery();
            //si la consulta encuentra la edificio....
            if (rs.next()) {
                //iniciamos el volcado de datos sobre
                //el objeto edificio.
                return rs.getInt("EDIFICIOID");
            }
            //si edificio no se localiza, devolveremos null
            return 0;

        } catch (SQLException e) {
            log.error("adEdificio. recuperarIdEdificioPorLote. SQLException ", e);
            throw new AbadiaSQLException("adEdificio. recuperarIdEdificioPorLote. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }


    }


    public boolean recursoParaAlmacen(int idRecurso) throws AbadiaException {
        String sSQL = "SELECT * from recurso_tipo rt WHERE rt.RECURSOID = ? and EDIFICIOID_ALMACEN=12";

        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = (PreparedStatement) con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave.
            int parNo = 1;
            ps.setLong(parNo, idRecurso);

            //Lanzo la consulta y cargo el resultado en un resultset
            rs = ps.executeQuery();
            //si la consulta encuentra la edificio....
            return rs.next();

        } catch (SQLException e) {
            log.error("adEdificio. recuperarEdificioPorRecurso. SQLException ", e);
            throw new AbadiaSQLException("adEdificio. recuperarEdificioPorRecurso. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }


    }


    //dará de alta un objeto Edificio en la base de datos

    public void crearEdificio(Edificio edificio) throws AbadiaException {
        String sSQL = "Insert Into edificio ( TIPOEDIFICIOID, ABADIAID, FECHACONSTRUCCION, ESTADO, NIVEL) Values (?, ?, ?, ?, ?)";

        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno los valores
            int parNo = 1;
            ps.setInt(parNo++, edificio.getIdDeTipoDeEdificio());
            ps.setInt(parNo++, edificio.getIdDeAbadia());
            ps.setString(parNo++, edificio.getFechaDeConstruccion());
            ps.setDouble(parNo++, edificio.getEstado());
            ps.setInt(parNo, edificio.getNivel());
            // Ejecutarlo
            ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adEdificio. crearEdificio. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }


    //elimina un objeto Edificio de la base de datos
    //a partir de un objeto Edificio devuelve verdadero si no ha ocurrido un error

    public boolean eliminarEdificio(Edificio edificio) throws SQLException {
        return eliminarEdificio(edificio.getIdDeEdificio());
    }

    //Elimina un objeto Edificio de la base de datos
    //a partir de la clave del Edificio

    public boolean eliminarEdificio(long idDeEdificioTmp) throws SQLException {
        String sSQL = "Delete From edificio Where `EDIFICIOID` = ?";

        PreparedStatement ps = (PreparedStatement) con.prepareStatement(sSQL);
        //asigno el parametro para el filtrado de la consulta
        //esta instruccion sustituye el simbolo ? por la clave del monje
        int parNo = 1;
        ps.setLong(parNo++, idDeEdificioTmp);
        // Ejecutarlo
        return ps.execute();
    }

    public static ArrayList<Edificio> crearEdificiosAbadia(AbadiaActForm abadia) throws AbadiaException {
        ArrayList<Edificio> edificios = new ArrayList<Edificio>();

        log.debug("Creacion de edificios para la abbatia: " + abadia.getNombreAbadia());
        log.debug("Se crea el Abadia. ");
        Edificio edificio = new Edificio();
        edificio.setIdDeTipoDeEdificio(EDIFICIO_ABADIA);
        edificio.setFechaDeConstruccion(CoreTiempo.getTiempoAbadiaString());
        edificio.setIdDeAbadia(abadia.getAbadia());
        edificio.setEstado(100);
        edificio.setNivel(1);
        edificios.add(edificio);

        log.debug("Se crea el Oratorio. ");
        edificio = new Edificio();
        edificio.setIdDeTipoDeEdificio(EDIFICIO_ORATORIO);
        edificio.setFechaDeConstruccion(CoreTiempo.getTiempoAbadiaString());
        edificio.setIdDeAbadia(abadia.getAbadia());
        edificio.setEstado(100);
        edificio.setNivel(1);
        edificios.add(edificio);

        log.debug("Se crea el Claustro. ");
        edificio = new Edificio();
        edificio.setIdDeTipoDeEdificio(EDIFICIO_CLAUSTRO);
        edificio.setFechaDeConstruccion(CoreTiempo.getTiempoAbadiaString());
        edificio.setIdDeAbadia(abadia.getAbadia());
        edificio.setEstado(100);
        edificio.setNivel(1);
        edificios.add(edificio);

        log.debug("Se crea el Dormitorio. ");
        edificio = new Edificio();
        edificio.setIdDeTipoDeEdificio(EDIFICIO_DORMITORIO);
        edificio.setFechaDeConstruccion(CoreTiempo.getTiempoAbadiaString());
        edificio.setIdDeAbadia(abadia.getAbadia());
        edificio.setEstado(100);
        edificio.setNivel(1);
        edificios.add(edificio);

        log.debug("Se crea el Comedor. ");
        edificio = new Edificio();
        edificio.setIdDeTipoDeEdificio(EDIFICIO_COMEDOR);
        edificio.setFechaDeConstruccion(CoreTiempo.getTiempoAbadiaString());
        edificio.setIdDeAbadia(abadia.getAbadia());
        edificio.setEstado(100);
        edificio.setNivel(1);
        edificios.add(edificio);

        log.debug("Se crea el Cocina. ");
        edificio = new Edificio();
        edificio.setIdDeTipoDeEdificio(EDIFICIO_COCINA);
        edificio.setFechaDeConstruccion(CoreTiempo.getTiempoAbadiaString());
        edificio.setIdDeAbadia(abadia.getAbadia());
        edificio.setEstado(100);
        edificio.setNivel(1);
        edificios.add(edificio);

        log.debug("Se crea la Iglesia. ");
        edificio = new Edificio();
        edificio.setIdDeTipoDeEdificio(EDIFICIO_IGLESIA);
        edificio.setFechaDeConstruccion(CoreTiempo.getTiempoAbadiaString());
        edificio.setIdDeAbadia(abadia.getAbadia());
        edificio.setEstado(100);
        edificio.setNivel(1);
        edificios.add(edificio);

        log.debug("Se crea la Mercado. ");
        edificio = new Edificio();
        edificio.setIdDeTipoDeEdificio(EDIFICIO_MERCADO);
        edificio.setFechaDeConstruccion(CoreTiempo.getTiempoAbadiaString());
        edificio.setIdDeAbadia(abadia.getAbadia());
        edificio.setEstado(100);
        edificio.setNivel(1);
        edificios.add(edificio);

        //en funci�n de la actividad principal de la abbatia, asignamos un adificio
        //adicional.


        if (abadia.getActividad() == ACTIVIDAD_AGRICULTURA) {
            log.debug("Se crea el Granero. ");
            edificio = new Edificio();
            edificio.setIdDeTipoDeEdificio(EDIFICIO_GRANERO);
            edificio.setFechaDeConstruccion(CoreTiempo.getTiempoAbadiaString());
            edificio.setIdDeAbadia(abadia.getAbadia());
            edificio.setEstado(100);
            edificio.setNivel(1);
            edificios.add(edificio);

            log.debug("Se crea el Almac�n. ");
            edificio = new Edificio();
            edificio.setIdDeTipoDeEdificio(EDIFICIO_ALMACEN);
            edificio.setFechaDeConstruccion(CoreTiempo.getTiempoAbadiaString());
            edificio.setIdDeAbadia(abadia.getAbadia());
            edificio.setEstado(100);
            edificio.setNivel(1);
            edificios.add(edificio);

            log.debug("Se crea el Campo. ");
            edificio = new Edificio();
            edificio.setIdDeTipoDeEdificio(EDIFICIO_CAMPO);
            edificio.setFechaDeConstruccion(CoreTiempo.getTiempoAbadiaString());
            edificio.setIdDeAbadia(abadia.getAbadia());
            edificio.setEstado(100);
            edificio.setNivel(1);
            edificios.add(edificio);

        } else if (abadia.getActividad() == ACTIVIDAD_ESCRIBAS) {
            log.debug("Se crea la Biblioteca. ");
            edificio = new Edificio();
            edificio.setIdDeTipoDeEdificio(EDIFICIO_BIBLIOTECA);
            edificio.setFechaDeConstruccion(CoreTiempo.getTiempoAbadiaString());
            edificio.setIdDeAbadia(abadia.getAbadia());
            edificio.setEstado(100);
            edificio.setNivel(1);
            edificios.add(edificio);

            log.debug("Se crea el taller de artesanía. ");
            edificio = new Edificio();
            edificio.setIdDeTipoDeEdificio(EDIFICIO_TALLER_ARTESANIA);
            edificio.setFechaDeConstruccion(CoreTiempo.getTiempoAbadiaString());
            edificio.setIdDeAbadia(abadia.getAbadia());
            edificio.setEstado(100);
            edificio.setNivel(1);
            edificios.add(edificio);

        } else if (abadia.getActividad() == ACTIVIDAD_GANADERIA) {
            log.debug("Se crea la granja. ");
            edificio = new Edificio();
            edificio.setIdDeTipoDeEdificio(EDIFICIO_GRANJA);
            edificio.setFechaDeConstruccion(CoreTiempo.getTiempoAbadiaString());
            edificio.setIdDeAbadia(abadia.getAbadia());
            edificio.setEstado(100);
            edificio.setNivel(2);
            edificios.add(edificio);
        }

        if (abadia.getActividad() != ACTIVIDAD_GANADERIA) {
            log.debug("Se crea la granja. ");
            edificio = new Edificio();
            edificio.setIdDeTipoDeEdificio(EDIFICIO_GRANJA);
            edificio.setFechaDeConstruccion(CoreTiempo.getTiempoAbadiaString());
            edificio.setIdDeAbadia(abadia.getAbadia());
            edificio.setEstado(100);
            edificio.setNivel(1);
            edificios.add(edificio);
        }
        return edificios;
        //crearEdificios(edificios);
    }

    /**
     * Recupera un {@link ArrayList} de objetos {@link Edificio} con los edificios
     * de la abadia en cuestión
     *
     * @param abadia
     * @param usuario
     * @return
     * @throws AbadiaException
     */
    public ArrayList<Edificio> recuperarListaEdificios(Abadia abadia, Usuario usuario, MessageResources resource) throws AbadiaException {

        String sSQLEdificios = "Select e.EDIFICIOID, t.GRAFICO_1, t.GRAFICO_2, l.LITERAL NOMBRE, t.MAP_X, t.MAP_Y, e.NIVEL, e.ESTADO, e.FECHACONSTRUCCION, n.ALMACENAMIENTO, n.CAPACIDADVITAL, e.TIPOEDIFICIOID, e.MANTENIMIENTO " +
                "from edificio as e, edificio_tipo as t, edificio_nivel as n, literales as l " +
                "where e.ABADIAID = ? AND t.TIPOEDIFICIOID = e.TIPOEDIFICIOID AND e.TIPOEDIFICIOID = n.TIPOEDIFICIOID AND e.NIVEL = n.NIVEL AND l.LITERALID = t.NOMBRE AND l.IDIOMAID = ? and t.TIPOEDIFICIOID <> " + Constantes.EDIFICIO_ABADIA + " order by t.TIPOEDIFICIOID ";

        adLibros libroAD;
        Libro libro;
        Edificio edificio;

        ArrayList<Edificio> al = new ArrayList<Edificio>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement(sSQLEdificios);

            int parNo = 1;
            ps.setLong(parNo++, abadia.getIdDeAbadia());
            ps.setInt(parNo, usuario.getIdDeIdioma());
            rs = ps.executeQuery();

            int almacenamiento_mas_libro = 0;
            //log.debug("LOGBRP - antes del next");
            while (rs.next()) {
                //iniciamos el volcado de datos sobre
                //el objeto edificio.
                edificio = new Edificio();
                edificio.setIdDeEdificio(rs.getInt("EDIFICIOID"));
                edificio.setIdDeTipoDeEdificio(rs.getInt("TIPOEDIFICIOID"));
                edificio.setFechaDeConstruccion(Utilidades.formatStringFromDB(rs.getString("FECHACONSTRUCCION")));
                edificio.setCapacidadVital(rs.getShort("CAPACIDADVITAL"));
                edificio.setAlmacenamiento(rs.getShort("ALMACENAMIENTO"));
                edificio.setAlmacenamientoActual(recuperaAlmacenamientoActual(abadia.getIdDeAbadia(), edificio.getIdDeEdificio(), edificio.getIdDeTipoDeEdificio()));
                edificio.setNombre(rs.getString("NOMBRE"));
                edificio.setMap_x(rs.getInt("MAP_X"));
                edificio.setMap_y(rs.getInt("MAP_Y"));
                edificio.setNivel(rs.getInt("NIVEL"));
                edificio.setEstado(rs.getDouble("ESTADO"));
                edificio.setBarraEstado(HTML.smallBarra(HTML.getBarrasEstado((int) edificio.getEstado()), resource.getMessage("edificio.estado") + " " + Double.toString(edificio.getEstado())));
                edificio.setMantenimiento(rs.getInt("MANTENIMIENTO"));
                if (edificio.getMantenimiento() == EDIFICIO_MANTENIMIENTO_BASICO) {
                    edificio.setMantenimientoDesc(resource.getMessage("edificio.mantenimiento.tipo.basico"));
                } else if (edificio.getMantenimiento() == EDIFICIO_MANTENIMIENTO_INEXISTENTE) {
                    edificio.setMantenimientoDesc(resource.getMessage("edificio.mantenimiento.tipo.inexistente"));
                } else if (edificio.getMantenimiento() == EDIFICIO_MANTENIMIENTO_MODERADO) {
                    edificio.setMantenimientoDesc(resource.getMessage("edificio.mantenimiento.tipo.moderado"));
                } else if (edificio.getMantenimiento() == EDIFICIO_MANTENIMIENTO_INTENSIVO) {
                    edificio.setMantenimientoDesc(resource.getMessage("edificio.mantenimiento.tipo.completo"));
                }

                //incluir gestión de libros para control de almacenamiento
                libroAD = new adLibros(con);
                libro = libroAD.recuperaLibroTipo(300 + rs.getInt("TIPOEDIFICIOID"), abadia.getIdDeAbadia());

                almacenamiento_mas_libro = 0;
                if (libro != null) {
                    almacenamiento_mas_libro = Utilidades.recuperarIncrementoEspacioEdificioPorLibro(rs.getInt("TIPOEDIFICIOID"), libro.getNivel(), rs.getInt("nivel"));
                }
                edificio.setAlmacenamiento_plus(almacenamiento_mas_libro);


                edificio.setGrafico_construccion(rs.getString("GRAFICO_1"));
                edificio.setGrafico_visualizacion(rs.getString("GRAFICO_2"));


                al.add(edificio);
            }
            return al;
        } catch (SQLException e) {
            throw new AbadiaSQLException("LOGBRP - recuperarListaEdificios error SQL: " + e.getMessage(), e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Devuelve un {@link ArrayList} con la lista de objetos {@link EdificioBase}
     * con los datos sobre ampliaciones y construcciones de edificios
     *
     * @param abadia
     * @param usuario
     * @return
     * @throws AbadiaException
     */
    public ArrayList<EdificioBase> recuperarListaEdificiosParaConstruir(Abadia abadia, Usuario usuario) throws AbadiaException {

        String sSQLEdificios = "SELECT et.TIPOEDIFICIOID, et.GRAFICO_1, et.GRAFICO_2, l.LITERAL as DESCRIPCION, l1.LITERAL as NOMBRE, en.TIEMPO_CONSTRUCCION, en.RECURSO_MONEDA, en.RECURSO_PIEDRA, en.RECURSO_MADERA, en.RECURSO_HIERRO " +
                "FROM  edificio_nivel en, literales l, edificio_tipo et, literales l1 " +
                "WHERE en.NIVEL = 1 AND en.TIPOEDIFICIOID = et.TIPOEDIFICIOID AND et.DESCRIPCION = l.LITERALID AND l.IDIOMAID = ? AND et.NOMBRE = l1.LITERALID and l1.IDIOMAID = ? AND et.TIPOEDIFICIOID <> " + Constantes.EDIFICIO_ABADIA;

        log.debug("adEdificio. recuperarListaEdificiosParaConstruir. creo la consulta: " + sSQLEdificios);
        ArrayList<Edificio> alEdificiosExistentes = getEdificiosAbadia(abadia);
        ArrayList<EdificioBase> al = new ArrayList<EdificioBase>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        EdificioBase edificio;
        Edificio edificioExistente;
        String desc = "";

        try {
            ps = con.prepareStatement(sSQLEdificios);
            ps.setShort(1, usuario.getIdDeIdioma());
            ps.setShort(2, usuario.getIdDeIdioma());
            rs = ps.executeQuery();
            while (rs.next()) {
                //iniciamos el volcado de datos sobre
                //el objeto edificio.
                edificio = new EdificioBase();
                edificio.setTipo_edificio(rs.getInt("TIPOEDIFICIOID"));
                desc = rs.getString("DESCRIPCION");
                if (desc.length() > 80) {
                    edificio.setDescripcion_edificio(desc.substring(0, 80) + "...");
                } else {
                    edificio.setDescripcion_edificio(desc);
                }
                edificio.setDescripcion_edificio_larga(rs.getString("DESCRIPCION"));
                edificio.setNombre_edificio(rs.getString("NOMBRE"));
                edificio.setCoste_hierro(Utilidades.redondear(rs.getDouble("RECURSO_HIERRO")));
                edificio.setCoste_madera(Utilidades.redondear(rs.getDouble("RECURSO_MADERA")));
                edificio.setCoste_piedra(Utilidades.redondear(rs.getDouble("RECURSO_PIEDRA")));
                edificio.setCoste_oro(Utilidades.redondear(rs.getDouble("RECURSO_MONEDA")));
                edificio.setDias_costruccion(rs.getInt("TIEMPO_CONSTRUCCION"));
                edificio.setGrafico_construccion(rs.getString("GRAFICO_1"));
                edificio.setGrafico_visualizacion(rs.getString("GRAFICO_2"));
                for (int iCount = 0; iCount < alEdificiosExistentes.size(); iCount++) {
                    edificioExistente = alEdificiosExistentes.get(iCount);
                    if (edificioExistente.getIdDeTipoDeEdificio() == edificio.getTipo_edificio()) {
                        edificio.setExiste((short) 1);
                        edificio.setEdificioid(edificioExistente.getIdDeEdificio());
                        if (getDatosSiguienteNivel(edificioExistente) != null)
                            edificio.setSiguienteNivel(edificioExistente.getNivel() + 1);
                        edificio.setNivel(edificioExistente.getNivel());
                        iCount = alEdificiosExistentes.size();
                    }
                }
                al.add(edificio);
            }
            return al;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adEdificio. recuperarListaEdificiosParaConstruir. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /*  Se consulta directamente a BD para averiguar si los datos han cambiado desde la �ltima vez
    */

    public double recuperaAlmacenamientoActual(int idAbadia, int idEdificio, int tipo) throws AbadiaException {

        adEdificio oEdificio;
        adMonje oMonjeAD;
        String sSQL = "";
        log.debug("adEdificio. recuperarAlmacenamientoActual. idEdificio: " + idEdificio + " tipo: " + tipo);
        if (tipo == EDIFICIO_COCINA || tipo == EDIFICIO_GRANERO) {
            sSQL = " SELECT Sum(volumen*cantidad) " +
                    "FROM `alimentos` a,  `alimentos_lote` al, `alimentos_tipo` at " +
                    "WHERE a.alimentoid = at.alimentoid and al.loteid = a.loteid and al.cantidad >0 and a.edificioID = ?";
        } else if (tipo == EDIFICIO_GRANJA || tipo == EDIFICIO_ESTABLO) {
            sSQL = " SELECT Sum(volumen) " +
                    "FROM animales a,  animales_crecimiento ac " +
                    "WHERE a.TIPO_ANIMALID = ac.TIPO_ANIMALID and a.NIVEL = ac.NIVEL and a.edificioID = ? and a.FECHA_FALLECIMIENTO is null and a.ESTADO = 0";
        } else if (tipo == EDIFICIO_CAMPO) {
            sSQL = "Select count(*) AS CONTADOR from campo c, edificio e where e.edificioID=? and e.abadiaid=c.abadiaid";
        } else if (tipo == EDIFICIO_DORMITORIO) {
            //Select Count(*) from monje where ESTADO=0 AND ABADIAID = ?
            sSQL = "SELECT (Select Count(*) from monje where abadiaid = e.abadiaid and estado = " + MONJE_VIVO + ") + (select count(*) from monje_visita mv, monje m where e.abadiaid = mv.abadiaid_destino and mv.monjeid = m.monjeid and m.estado= " + MONJE_VISITA + ") -" +
                    "(select count(*) from monje m, edificio ed where e.abadiaid = ed.abadiaid and m.abadiaid = e.abadiaid and m.jerarquiaid >= " + JERARQUIA_OBISPO + " and ed.tipoedificioid = " + EDIFICIO_HOSPEDERIA +
                    " and m.estado =" + MONJE_VIVO + ")  - (select count(*) from monje m, edificio ed, monje_visita mv  where mv.monjeid = m.monjeid and e.abadiaid = ed.abadiaid and mv.abadiaid_destino = e.abadiaid and m.jerarquiaid >= " + JERARQUIA_OBISPO +
                    " and ed.tipoedificioid =" + EDIFICIO_HOSPEDERIA + " and m.estado = " + MONJE_VISITA + ") num_monjes " +
                    " from edificio e where e.edificioid = ?";
        } else if (tipo == EDIFICIO_HOSPEDERIA) {
            sSQL = " Select (select count(*) from monje m, edificio ed where e.abadiaid = ed.abadiaid and m.estado = " + MONJE_VIVO + " and m.abadiaid = e.abadiaid and m.jerarquiaid >= " + JERARQUIA_OBISPO + " and ed.tipoedificioid = e.tipoedificioid ) + " +
                    " (select count(*) from monje m, edificio ed, monje_visita mv  where mv.monjeid = m.monjeid and e.abadiaid = ed.abadiaid and mv.abadiaid_destino = e.abadiaid and m.jerarquiaid >= " + JERARQUIA_OBISPO + " and m.estado = " + MONJE_VISITA + " and ed.tipoedificioid = e.tipoedificioid ) " +
                    "  from edificio e where e.edificioid = ? ";

        } else if (tipo == EDIFICIO_ALMACEN || tipo == EDIFICIO_TALLER_ARTESANIA || tipo == EDIFICIO_TALLER_COSTURA || tipo == EDIFICIO_BIBLIOTECA) {
            sSQL = "Select sum(volumen * cantidad) from recurso r, recurso_tipo rt, edificio e where rt.EDIFICIOID_ALMACEN = e.tipoedificioid and e.edificioID=? and e.abadiaid=r.abadiaid and r.RECURSOID=rt.RECURSOID";
/*
        }else if (tipo == Constantes.EDIFICIO_BIBLIOTECA)
        {
            sSQL = "Select count(*) AS CONTADOR from libro l, edificio e where e.edificioID=? and l.abadiaid=e.abadiaid and l.estado in (2, 5, 6, 7, 8)";
*/
        } else if (tipo == EDIFICIO_MERCADO) {
            sSQL = "call CargarVolumenVenta(?);";
        } else if (tipo == EDIFICIO_CEMENTERIO) {
            sSQL = "Select count(*) from edificio e, monje_cementerio mc where e.abadiaid = mc.abadiaid and mc.estado = " + MONJE_MUERTO + " and e.edificioid = ?";
        } else return 0;


        double dAlmacenamiento;
        int iMonjesEnfermos;
        PreparedStatement ps = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            int parNo = 1;
            if (tipo == EDIFICIO_MERCADO) {
                cs = con.prepareCall(sSQL);
                cs.setLong(parNo, idAbadia);
                cs.execute();
                rs = cs.executeQuery();
            } else {
                ps = con.prepareStatement(sSQL);
                ps.setLong(parNo, idEdificio);
                rs = ps.executeQuery();
            }
            if (rs.next()) {
                //iniciamos el volcado de datos sobre
                //el objeto edificio.
                dAlmacenamiento = rs.getDouble(1);
            } else {
                return (0);
            }
            //si se trata del dormitorio, buscaremos posibles monjes enfermos para
            //descontarlos del almacenamiento actual...
            if (tipo == EDIFICIO_DORMITORIO) {
                oEdificio = new adEdificio(con);
                if (oEdificio.existeEdificioTipo(idAbadia, EDIFICIO_ENFERMERIA)) {
                    oMonjeAD = new adMonje(con);
                    iMonjesEnfermos = oMonjeAD.getNumMonjesEnfermos(idAbadia);
                    dAlmacenamiento = dAlmacenamiento - iMonjesEnfermos;
                }
            }


            return dAlmacenamiento;

        } catch (SQLException e) {
            //log.error("adEdificio. recuperaAlmacenamientoActual error SQL ", e);
            throw new AbadiaSQLException("adEdificio. recuperaAlmacenamientoActual error SQL ", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
            DBMSUtils.cerrarObjetoSQL(cs);
        }

    }

    public DatosNivel getDatosSiguienteNivel(Edificio edificio) throws AbadiaException {
        //Definición de cadena sql de consulta
        String sSQL = "Select * from edificio_nivel where NIVEL = ? AND TIPOEDIFICIOID = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        DatosNivel datos;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave.
            int parNo = 1;
            ps.setLong(parNo++, edificio.getNivel() + 1);
            ps.setLong(parNo, edificio.getIdDeTipoDeEdificio());
            //Lanzo la consulta y cargo el resultado en un resultset
            rs = ps.executeQuery();
            //si la consulta encuentra la edificio....
            datos = new DatosNivel();
            if (rs.next()) {
                //iniciamos el volcado de datos sobre
                //el objeto edificio.
                datos.setAlmacenamiento(rs.getInt("ALMACENAMIENTO"));
                datos.setCapacidad(rs.getInt("CAPACIDADVITAL"));
                datos.setHierro(rs.getInt("RECURSO_HIERRO"));
                datos.setNivel(rs.getInt("NIVEL"));
                datos.setOro(rs.getInt("RECURSO_MONEDA"));
                datos.setMadera(rs.getInt("RECURSO_MADERA"));
                datos.setPiedra(rs.getInt("RECURSO_PIEDRA"));
                datos.setTiempo(rs.getInt("TIEMPO_CONSTRUCCION"));
                //devolvemos el objeto Edificio informado.
                log.debug("Fin de getDatosSiguienteNivel");
                return datos;
            }
            return null;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adEdificio. getDatosSiguienteNivel", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }


    }

    public void subirNivel(Abadia abadia, Edificio edificio, DatosNivel datosNivel) throws AbadiaException {

        adLibros librotipoAD;
        adRecurso recursosAD;
        Libro libro;

        librotipoAD = new adLibros(con);
        libro = librotipoAD.recuperaLibroTipo(LIBRO_TIPO_CONTRUCCION, abadia.getIdDeAbadia());

        if (datosNivel.getNivel() >= 3) {
            if (libro != null) {
                if (datosNivel.getNivel() == 3 && libro.getNivel() >= 1) {

                } else if (datosNivel.getNivel() > 3 && libro.getNivel() > 1) {

                } else {
                    throw new LibroConstruccionInsuficiente("No dispones del libro para poder construir a este nivel", log);
                }
            } else {
                throw new LibroConstruccionInsuficiente("No dispones del libro para poder construir a este nivel", log);
            }

        }
        //validamos si la abbatia dispone de oro suficiente
        recursosAD = new adRecurso(con);
        HashMap recursos = recursosAD.recuperarRecursos(abadia.getIdDeAbadia());

        if (recursos.get(RECURSOS_ORO) == null) {
            throw new RecursosInsuficientesException("No se dispone de oro suficiente", log);
        }
        if (datosNivel.getOro() > (Double) recursos.get(RECURSOS_ORO)) {
            throw new RecursosInsuficientesException("No se dispone de oro suficiente", log);
        }
        //validamos si la abbatia dispone de madera suficiente
        if (recursos.get(RECURSOS_MADERA) == null) {
            throw new RecursosInsuficientesException("No se dispone de madera suficiente", log);
        }
        if (datosNivel.getMadera() > (Double) recursos.get(RECURSOS_MADERA)) {
            throw new RecursosInsuficientesException("No se dispone de madera suficiente", log);
        }
        //validamos si la abbatia dispone de hierro suficiente
        if (recursos.get(RECURSOS_HIERRO) == null) {
            throw new RecursosInsuficientesException("No se dispone de hierro suficiente", log);
        }
        if (datosNivel.getHierro() > (Double) recursos.get(RECURSOS_HIERRO)) {
            throw new RecursosInsuficientesException("No se dispone de hierro suficiente", log);
        }

        //validamos si la abbatia dispone de piedra suficiente
        if (recursos.get(RECURSOS_PIEDRA) == null) {
            throw new RecursosInsuficientesException("No se dispone de piedra suficiente", log);
        }
        if (datosNivel.getPiedra() > (Double) recursos.get(RECURSOS_PIEDRA)) {
            throw new RecursosInsuficientesException("No se dispone de piedra suficiente", log);
        }

        PreparedStatement ps = null;
        try {
            //Insertamos un registro en la tabla de edificio_contrucci�n
            //para programar la construccion del edificio.
            String sSQL = "INSERT INTO edificio_construccion (ABADIAID, TIPOEDIFICIOID, NIVEL, FECHA_CONSTRUCCION, FECHA_FINALIZACION) VALUES(?,?,?,?,?)";
            ps = con.prepareStatement(sSQL);

            ps.setLong(1, abadia.getIdDeAbadia());
            ps.setInt(2, edificio.getIdDeTipoDeEdificio());
            ps.setInt(3, datosNivel.getNivel());
            //get tiempo de la abbatia
            ps.setString(4, CoreTiempo.getTiempoAbadiaString());
            //get tiempo de la abbatia mas tiempo de contruccion
            ps.setString(5, CoreTiempo.getDiferenciaString(datosNivel.getTiempo()));
            ps.execute();

            //Restamos los recursos de la abbatia.
            recursosAD = new adRecurso(con);
            recursosAD.restarRecurso(RECURSOS_ORO, abadia.getIdDeAbadia(), datosNivel.getOro());
            recursosAD.restarRecurso(RECURSOS_MADERA, abadia.getIdDeAbadia(), datosNivel.getMadera());
            recursosAD.restarRecurso(RECURSOS_HIERRO, abadia.getIdDeAbadia(), datosNivel.getHierro());
            recursosAD.restarRecurso(RECURSOS_PIEDRA, abadia.getIdDeAbadia(), datosNivel.getPiedra());

        } catch (SQLException e) {
            throw new AbadiaSQLException("adEdificio. SubirNivel.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /**
     * Baja un punto el nivel del edificio y restablece un tercio de la piedra y el hierro
     *
     * @param Edificioid
     * @param Abadiaid
     * @param Piedra
     * @param Hierro
     * @throws AbadiaException
     */
    public void bajarNivel(int Edificioid, int Abadiaid, int Piedra, int Hierro) throws AbadiaException {
        adRecurso recursoAD = null;
        adUtils utils = null;
        try {
            //solo recuperamos una cuarta parte del hierro.
            Hierro = Hierro / 3;
            //para calcular las 3 cuartas partes de la madera.
            int aux_Piedra = Piedra / 3;
            Piedra = Piedra - aux_Piedra;
            // le restamos uno al nivel del edificio.
            utils = new adUtils(con);
            utils.execSQL("Update edificio set nivel = nivel -1 where edificioid =" + Edificioid);

            if (Piedra > 0 && Hierro > 0) {
                recursoAD = new adRecurso(con);
                recursoAD.sumarRecurso(RECURSOS_HIERRO, Abadiaid, Hierro);
                recursoAD.sumarRecurso(RECURSOS_PIEDRA, Abadiaid, Piedra);
            }

        } catch (AbadiaException e) {
            throw new AbadiaSQLException("adEdificio. BajarNivel.", e, log);
        }
    }

    /**
     * sube un punto el nivel del edificio
     *
     * @param Edificioid
     * @throws AbadiaException
     */
    public void subirNivel(int Edificioid) throws AbadiaException {
        adUtils utils;
        try {
            utils = new adUtils(con);
            utils.execSQL("Update edificio set nivel = nivel +1 where edificioid =" + Edificioid);

        } catch (AbadiaException e) {
            throw new AbadiaSQLException("adEdificio. subirNivel.", e, log);
        }
    }


    public void subirNivel_Obispado(Abadia abadia, Abadia obispado, Edificio edificio, DatosNivel datosNivel) throws AbadiaException {

        if (datosNivel.getNivel() > 2) {
            throw new NivelEdificioObispoIncorrecto("adEdificio. subirNivel_Obispado. El obispo no puede subir el nivel de los edificios por encima de 2", null, log);
        }
        adComisiones ComisionesAD;
        int opt = 1;

        // Total coste del edificio
        double total = getCosteTotalCostruccion(edificio, datosNivel.getNivel());
        // El oro que disponemos
        ComisionesAD = new adComisiones(con);
        double oro = ComisionesAD.recaudacion(obispado.getIdDeRegion());
        if (total > oro) {
            throw new RecursosInsuficientesException("No se dispone de oro suficiente", log);
        }
        if (datosNivel.getNivel() > 1) opt = 2; // Ampliaci�n
        // Restar de la recuadaci�n
        ComisionesAD = new adComisiones(con);
        ComisionesAD.actualizaRecaudacion(obispado.getIdDeRegion(), -total);
        ComisionesAD.addMovima(obispado.getIdDeRegion(), JERARQUIA_OBISPO, obispado.getIdDeAbadia(), abadia.getIdDeAbadia(), opt, total, edificio.getIdDeTipoDeEdificio(), -1);

        PreparedStatement ps = null;
        try {
            //Insertamos un registro en la tabla de edificio_contrucci�n
            //para programar la construccion del edificio.
            String sSQL = "INSERT INTO edificio_construccion (ABADIAID, TIPOEDIFICIOID, NIVEL, FECHA_CONSTRUCCION, FECHA_FINALIZACION) VALUES(?,?,?,?,?)";
            ps = con.prepareStatement(sSQL);
            ps.setLong(1, abadia.getIdDeAbadia());
            ps.setInt(2, edificio.getIdDeTipoDeEdificio());
            ps.setInt(3, datosNivel.getNivel());
            //get tiempo de la abbatia
            ps.setString(4, CoreTiempo.getTiempoAbadiaString());
            //get tiempo de la abbatia mas tiempo de contruccion
            ps.setString(5, CoreTiempo.getDiferenciaString(datosNivel.getTiempo()));
            ps.execute();

            //Restamos los recursos de la abbatia.
        } catch (SQLException e) {
            throw new AbadiaSQLException("adEdificio. subirNivel_Obispado.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /**
     * Recupera un objeto {@link DatosConstruccion} con las datos correspondientes a la
     * construccion de un edificio
     *
     * @param edificio
     * @return
     * @throws AbadiaSQLException
     */
    public DatosConstruccion getDatosConstruccion(Edificio edificio) throws AbadiaSQLException {
        String sSQL = "Select * from edificio_construccion where ABADIAID = ? AND TIPOEDIFICIOID = ? AND NIVEL = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        DatosConstruccion datos;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setLong(1, edificio.getIdDeAbadia());
            ps.setInt(2, edificio.getIdDeTipoDeEdificio());
            ps.setInt(3, edificio.getNivel() + 1);
            rs = ps.executeQuery();
            datos = new DatosConstruccion();
            if (rs.next()) {
                datos.setEnConstruccion("S");
                datos.setFechaPrevistaFin(Utilidades.formatStringFromDB(rs.getString("FECHA_FINALIZACION")));
            } else {
                datos.setEnConstruccion("N");
                datos.setFechaPrevistaFin("");
            }
            return datos;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adEdificio. getDatosConstruccion.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public ArrayList<Edificio> getEdificiosAbadia(Abadia abadia) throws AbadiaException {

        String sSQLEdificios = "Select e.EDIFICIOID, e.NIVEL, e.ESTADO, e.FECHACONSTRUCCION, e.TIPOEDIFICIOID from edificio as e where e.ABADIAID = ? ";

        ArrayList<Edificio> al = new ArrayList<Edificio>();

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQLEdificios);
            int parNo = 1;
            ps.setLong(parNo, abadia.getIdDeAbadia());
            rs = ps.executeQuery();
            Edificio edificio;
            while (rs.next()) {
                //iniciamos el volcado de datos sobre
                //el objeto edificio.
                edificio = new Edificio();
                edificio.setIdDeEdificio(rs.getInt("EDIFICIOID"));
                edificio.setIdDeTipoDeEdificio(rs.getInt("TIPOEDIFICIOID"));
                edificio.setFechaDeConstruccion(Utilidades.formatStringFromDB(rs.getString("FECHACONSTRUCCION")));
                edificio.setNivel(rs.getInt("NIVEL"));
                edificio.setEstado(rs.getInt("ESTADO"));

                al.add(edificio);
            }
            return al;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adEdificio. getEdificiosAbadia. SQLException ", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public DatosEdificioActForm getDatosEdificioTipo(Edificio edificio, Usuario usuario) throws AbadiaException {

        String sSQL = "SELECT et.TIPOEDIFICIOID, et.GRAFICO_1, et.GRAFICO_2, l.LITERAL as DESCRIPCION, l1.LITERAL as NOMBRE, en.TIEMPO_CONSTRUCCION, en.RECURSO_MONEDA, en.RECURSO_PIEDRA, en.RECURSO_MADERA, en.RECURSO_HIERRO " +
                "FROM  edificio_nivel en, literales l, edificio_tipo et, literales l1 " +
                "WHERE en.NIVEL = 1 AND en.TIPOEDIFICIOID = et.TIPOEDIFICIOID AND et.DESCRIPCION = l.LITERALID AND l.IDIOMAID = ? AND et.NOMBRE = l1.LITERALID and l1.IDIOMAID = ? and et.TIPOEDIFICIOID = ?";


        PreparedStatement ps = null;
        ResultSet rs = null;
        DatosEdificioActForm datos;
        try {
            ps = con.prepareStatement(sSQL);
            int parNo = 1;
            ps.setShort(parNo++, usuario.getIdDeIdioma());
            ps.setShort(parNo++, usuario.getIdDeIdioma());
            ps.setInt(parNo, edificio.getIdDeTipoDeEdificio());
            rs = ps.executeQuery();

            datos = new DatosEdificioActForm();
            if (rs.next()) {
                datos.setTipo_edificio(rs.getInt("TIPOEDIFICIOID"));
                datos.setDescripcion_edificio(rs.getString("DESCRIPCION"));
                datos.setNombre_edificio(rs.getString("NOMBRE"));
                datos.setCoste_hierro(Utilidades.redondear(rs.getDouble("RECURSO_HIERRO")));
                datos.setCoste_madera(Utilidades.redondear(rs.getDouble("RECURSO_MADERA")));
                datos.setCoste_piedra(Utilidades.redondear(rs.getDouble("RECURSO_PIEDRA")));
                datos.setCoste_oro(Utilidades.redondear(rs.getDouble("RECURSO_MONEDA")));
                datos.setDias_costruccion(rs.getInt("TIEMPO_CONSTRUCCION"));
                return datos;
            }
            return null;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adEdificio. getDatosEdificioTipo. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    // devuelve el coste de mercado de to do el material

    public double getCosteTotalCostruccion(Edificio edificio, int nivel) throws AbadiaException {
        String sSQL = "SELECT en.RECURSO_MONEDA, en.RECURSO_PIEDRA, en.RECURSO_MADERA, en.RECURSO_HIERRO " +
                "FROM  edificio_nivel en " +
                "WHERE en.nivel = ? and en.tipoedificioid = ?";


        adUtils utils;
        utils = new adUtils(con);
        double hierro = utils.getSQL("SELECT precio_actual " +
                "FROM mercados m , mercados_recursos mr " +
                "WHERE m.productoid = mr.productoid and m.mercancia = 'R' and m.abadiaid = 0 and recursoid = 4", 5);
        double piedra = utils.getSQL("SELECT precio_actual " +
                "FROM mercados m , mercados_recursos mr " +
                "WHERE m.productoid = mr.productoid and m.mercancia = 'R' and m.abadiaid = 0 and recursoid = 3", 5);
        double madera = utils.getSQL("SELECT precio_actual " +
                "FROM mercados m , mercados_recursos mr " +
                "WHERE m.productoid = mr.productoid and m.mercancia = 'R' and m.abadiaid = 0 and recursoid = 2", 5);
        double total = 0;

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, nivel);
            ps.setInt(2, edificio.getIdDeTipoDeEdificio());
            rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getDouble("RECURSO_MONEDA") +
                        (rs.getDouble("RECURSO_HIERRO") * hierro) +
                        (rs.getDouble("RECURSO_PIEDRA") * piedra) +
                        (rs.getDouble("RECURSO_MADERA") * madera);
            }
            return total;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adEdificio. getCosteTotalCostruccion. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }


    }

    public boolean eliminarEdificiosAbadia(long idAbadia) throws AbadiaSQLException {
        String sSQL = "Delete From edificio Where `ABADIAID` = ?";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave del monje
            int parNo = 1;
            ps.setLong(parNo, idAbadia);
            // Ejecutarlo
            return ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adEdificio. eliminarEdificiosAbadia. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public boolean eliminarEdificiosAbadiaConstruccion(long idAbadia) throws AbadiaSQLException {
        String sSQL = "Delete From edificio_construccion Where `ABADIAID` = ?";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave del monje
            int parNo = 1;
            ps.setLong(parNo, idAbadia);
            // Ejecutarlo
            return ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adEdificio. eliminarEdificiosAbadiaConstruccion. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public String recuperarNombrePorEdificioId(int idEdificio, int idIdioma) throws AbadiaException {
        //Definición de cadena sql de consulta
        String sSQL = "Select l.LITERAL as NOMBRE " +
                "from edificio_tipo et, edificio e, literales l " +
                "where et.nombre = l.LITERALID and l.IDIOMAID= ? and et.TIPOEDIFICIOID = e.tipoedificioid and e.edificioid = ?";
        //creo un objeto de tipo Edificio
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);

            int parNo = 1;
            ps.setInt(parNo++, idIdioma);
            ps.setLong(parNo, idEdificio);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("NOMBRE");
            } else return "";
        } catch (SQLException e) {
            throw new AbadiaSQLException("LOGBRP - No se pudo localizar el recuperarEdificios " + e.getMessage(), e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public String recuperarNombreEdificio(int idEdificio, int idIdioma) throws AbadiaException {
        adUtils utils = null;
        //Definición de cadena sql de consulta
        String sSQL = "Select l.LITERAL as NOMBRE " +
                "from edificio_tipo et, edificio e, literales l " +
                "where et.nombre = l.LITERALID and l.IDIOMAID = " + idIdioma + " and et.TIPOEDIFICIOID = e.tipoedificioid and e.edificioid = " + idEdificio;

        String sNombre = "";
        try {
            utils = new adUtils(con);
            sNombre = utils.getSQL(sSQL, "NOMBRE");
            return sNombre;

        } catch (AbadiaException e) {
            throw new AbadiaSQLException("adEdificio. recuperarNombreEdificio.", e, log);
        }
    }

    /**
     * Elimina posibles registros de la tabla edificio_construccion de un edificio determinado
     *
     * @param idAbadia
     * @param idTipoEdificio
     * @throws AbadiaException
     */
    public void eliminarEdificioConstruccion(int idAbadia, int idTipoEdificio) throws AbadiaException {
        adUtils utils = null;
        //Definición de cadena sql de consulta
        String sSQL = "delete from edificio_construccion where abadiaid = " + idAbadia + " and tipoedificioid = " + idTipoEdificio;

        try {
            utils = new adUtils(con);
            utils.execSQL(sSQL);
        } catch (AbadiaException e) {
            throw new AbadiaSQLException("adEdificio. eliminarEdificioConstruccion.", e, log);
        }
    }

    public String recuperarDescripcionEdificioTipo(long idDeTipoEdificio, Usuario usuario) throws AbadiaException {
        //Definición de cadena sql de consulta
        String sSQL = "Select l.LITERAL as NOMBRE " +
                "from edificio_tipo et, literales l " +
                "where et.DESCRIPCION = l.LITERALID and l.IDIOMAID= ? and et.TIPOEDIFICIOID = ?";
        //creo un objeto de tipo Edificio
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);

            int parNo = 1;
            ps.setInt(parNo++, usuario.getIdDeIdioma());
            ps.setLong(parNo, idDeTipoEdificio);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("NOMBRE");
            }
        } catch (SQLException e) {
            throw new AbadiaSQLException("LOGBRP - No se pudo localizar el recuperarEdificios " + e.getMessage(), e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
        //si edificio no se localiza, devolveremos null
        return null;
    }

    public String recuperarNombreEdificioTipo(long idDeTipoEdificio, Usuario usuario) throws AbadiaException {
        //Definición de cadena sql de consulta
        String sSQL = "Select l.LITERAL as NOMBRE " +
                "from edificio_tipo et, literales l " +
                "where et.NOMBRE = l.LITERALID and l.IDIOMAID= ? and et.TIPOEDIFICIOID = ?";
        //creo un objeto de tipo Edificio
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);

            int parNo = 1;
            ps.setInt(parNo++, usuario.getIdDeIdioma());
            ps.setLong(parNo, idDeTipoEdificio);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("NOMBRE");
            }
        } catch (SQLException e) {
            throw new AbadiaSQLException("LOGBRP - No se pudo localizar el recuperarEdificios " + e.getMessage(), e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
        //si edificio no se localiza, devolveremos null
        return null;
    }


    public ArrayList<Notificacion> recuperarLinksEdificios(Abadia abadia, Usuario usuario) throws AbadiaSQLException {
        Notificacion nota;
        String sSQLEdificios = "Select e.EDIFICIOID, l.LITERAL as NOMBRE, concat(et.GRAFICO_2,'_',e.NIVEL,'.gif') GRAFICO  " +
                "from edificio as e, edificio_tipo et, literales l " +
                "where e.ABADIAID = ? AND e.nivel>0 AND e.TIPOEDIFICIOID = et.TIPOEDIFICIOID and e.TIPOEDIFICIOID in(4,5,7,8,22,12,25,9) and l.LITERALID = et.NOMBRE AND l.IDIOMAID = ?";

        ArrayList<Notificacion> al = new ArrayList<Notificacion>();

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQLEdificios);
            int parNo = 1;
            ps.setLong(parNo++, abadia.getIdDeAbadia());
            ps.setInt(parNo, usuario.getIdDeIdioma());
            rs = ps.executeQuery();
            while (rs.next()) {
                nota = new Notificacion("mostrarEdificio.do?clave=" + rs.getInt("EDIFICIOID"), rs.getString("NOMBRE"), rs.getString("GRAFICO"));
                al.add(nota);
            }
            return al;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adEdificio. recuperarLinksEdificios. SQLException ", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /*  Recupera todos los monjes que estan en el edificio en ese momento
    */

    public ArrayList<Monje> recuperarMonjes(long idAbadia, int edificioid, MessageResources resource) throws AbadiaException {
        Monje monje;
        String sSQL = "Select m.*, mc.salud " +
                "from monje m, monje_alimentacion mc, actividad ma " +
                "WHERE m.estado=0 AND mc.MonjeID = m.monjeid AND m.MONJEID = ma.MONJEID AND ma.PeriodoID=? AND ma.actividadid=? AND m.ABADIAID=? " +
                " order by nombre, apellido1";
        String sSQLDormir = "Select * " +
                "from monje m, monje_alimentacion mc WHERE m.monjeid = mc.monjeid AND m.estado=0 AND m.ABADIAID=? " +
                " order by nombre, apellido1";

        int periodoid;

        periodoid = CoreTiempo.getPeriodoActual();

        adUtils utils = new adUtils(con);
        int actividadid = utils.getSQL("SELECT actividad_tipo FROM edificio_tipo where tipoedificioid=" + edificioid, 0);

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            if ((periodoid == PERIODO_NADA) && (edificioid == EDIFICIO_DORMITORIO)) {   // Durmiendo en el dormitorio!
                ps = con.prepareStatement(sSQLDormir);
                ps.setLong(1, idAbadia);
            } else {
                ps = con.prepareStatement(sSQL);
                ps.setInt(1, periodoid);
                ps.setInt(2, actividadid);
                ps.setLong(3, idAbadia);
            }
            rs = ps.executeQuery();
            ArrayList<Monje> monjes = new ArrayList<Monje>();
            while (rs.next()) {
                monje = new Monje();
                monje.setIdMonje(rs.getInt("MONJEID"));
                monje.setIdAbadia(rs.getInt("ABADIAID"));
                monje.setIdDeEspecializacion(rs.getInt("ESPECIALIZACIONID"));
                monje.setIdDeJerarquia(rs.getInt("JERARQUIAID"));
                monje.setNombre(rs.getString("NOMBRE"));
                monje.setApellido1(rs.getString("APELLIDO1"));
                monje.setFechaDeEntradaEnAbadia(rs.getString("FECHA_ENTRADA"));
                monje.setFechaDeNacimiento(Utilidades.formatStringFromDB(rs.getString("FECHA_NACIMIENTO")));
                monje.setFechaDeFallecimiento(Utilidades.formatStringFromDB(rs.getString("FECHA_FALLECIMIENTO")));
                monje.setEdad(CoreTiempo.getEdad(rs.getString("FECHA_NACIMIENTO")));
                monje.setSalud(rs.getShort("SALUD"));
                monje.setBarra_salud(HTML.smallBarra(Math.round(monje.getSalud() / 10), resource.getMessage("monjes.abadia.salud") + Integer.toString(monje.getSalud())));
                //
                monjes.add(monje);
            }

            return monjes;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adAbadia. getMonjes. Error SQL", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Recupera el nivel de un edificio a partir de la abadia y el tipo de edificio
     *
     * @param idAbadia       Identificador de abadia
     * @param idTipoEdificio Identificador de tipo de edificio
     * @return int
     * @throws org.abbatia.exception.base.AbadiaException
     *          Excepción base de abbatia
     */
    public int recuperarNivelEdificio(int idAbadia, int idTipoEdificio) throws AbadiaException {
        //Definición de cadena sql de consulta
        String sSQL = "Select nivel from edificio where TIPOEDIFICIOID = ? and ABADIAID = ?";
        //creo un objeto de tipo Edificio
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            int parNo = 1;
            ps.setInt(parNo++, idTipoEdificio);
            ps.setLong(parNo, idAbadia);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("nivel");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            throw new AbadiaSQLException("adEdificio. recuperarNivelEdificio. SQLException " + e.getMessage(), e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public boolean existeEdificioTipo(int idAbadia, int idTipoEdificio) throws AbadiaException {
        String sSQL = "Select * from edificio where tipoedificioid = " + idTipoEdificio + " and abadiaid = " + idAbadia + " and nivel > 0 ";
        adUtils utils;
        boolean bReturn;

        utils = new adUtils(con);
        bReturn = utils.getSQL(sSQL, false);
        return bReturn;
    }

/*    public static boolean existeEdificioTipo(int idAbadia, int idTipoEdificio) throws AbadiaException
    {
        String sSQL = "Select * from edificio where tipoedificioid = " + idTipoEdificio + " and abadiaid = " + idAbadia + " and nivel > 0 ";
        adUtils utils = null;
        boolean bReturn;

        try
        {
            utils = new adUtils();
            bReturn = utils.getSQL(sSQL, false);
            return bReturn;

        }catch (AbadiaException e)
        {
            throw e;
        }finally
        {
            DBMSUtils.cerrarObjetoSQL(utils);
        }
    }*/

    /**
     * Devuelve información sobre la existencia o no del siguiente nivel de un tipo de edificio determinado...
     *
     * @param idTipoEdificio
     * @param sNivel
     * @return
     * @throws AbadiaException
     */
    public boolean existeSiguienteNivel(int idTipoEdificio, short sNivel) throws AbadiaException {
        String sSQL = "Select * from edificio_nivel where tipoedificioid = " + idTipoEdificio + " and nivel = " + (sNivel + 1);
        adUtils utils = null;
        boolean bReturn;

        try {
            utils = new adUtils(con);
            bReturn = utils.getSQL(sSQL, false);
            return bReturn;

        } catch (AbadiaException e) {
            throw e;
        }
    }

    /**
     * Crea un registro de edificio por cada iteración del {@link ArrayList}
     *
     * @param alEdificios
     * @throws AbadiaException
     */
    public void crearEdificios(ArrayList<Edificio> alEdificios) throws AbadiaException {
        Iterator<Edificio> edificios = alEdificios.iterator();
        Edificio edificio;
        adEdificio edificioAD;
        edificioAD = new adEdificio(con);
        while (edificios.hasNext()) {
            edificio = edificios.next();
            edificioAD.crearEdificio(edificio);
        }
    }

    /**
     * Recupera un ArrayList con los datos de mantenimiento
     * de los edificios de cada abadia.
     *
     * @return
     * @throws AbadiaException
     */
    public ArrayList<EdificioMantenimiento> recuperarAbadiasMantenimientoEdificios() throws AbadiaException {
        //recuperar abbatia, coste total de mantenimiento, oro de la abbatia, idioma
        EdificioMantenimiento abadia;
        ArrayList<EdificioMantenimiento> alAbadias = new ArrayList<EdificioMantenimiento>();
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            cs = con.prepareCall("call recuperarAbadiasMantenimientoEdificios();");
            cs.execute();
            rs = cs.getResultSet();
            while (rs.next()) {
                abadia = new EdificioMantenimiento();
                abadia.setIdAbadia(rs.getInt("ABADIAID"));
                abadia.setPrecioMantenimiento(rs.getDouble("PRECIO_TOTAL"));
                abadia.setOroTotal(rs.getDouble("CANTIDAD"));
                abadia.setIdIdioma(rs.getInt("IDIOMAID"));
                alAbadias.add(abadia);
            }
            return alAbadias;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adEdificio. recuperarAbadiasMantenimientoEdificios. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(cs);
        }

    }

    /**
     * Recupera una HashMap con tipo de edificio y nivel como key y los puntos a restar como contenido
     *
     * @return
     * @throws AbadiaException
     */
    public HashMap<String, Double> recuperarParametrosDesgaste() throws AbadiaException {
        String sSQL = "select concat(TIPOEDIFICIOID, NIVEL) as TIPO, DETERIORO " +
                " FROM edificio_mantenimiento " +
                " ORDER by TIPOEDIFICIOID, NIVEL";
        HashMap<String, Double> hmEdificioMantenimiento = new HashMap<String, Double>();

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                hmEdificioMantenimiento.put(rs.getString("TIPO"), rs.getDouble("DETERIORO"));
            }

            return hmEdificioMantenimiento;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adEdificio. recuperarParametrosDesgaste. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /**
     * Recupera una HashMap con tipo de edificio y nivel como key y los puntos a restar como contenido
     *
     * @return
     * @throws AbadiaException
     */
    public int recuperarCosteMantenimiento(String p_szTipoEdificioId, short p_sNivel) throws AbadiaException {
        String sSQL = "select PRECIO " +
                " FROM edificio_mantenimiento " +
                " WHERE TIPOEDIFICIOID = ? AND NIVEL=?";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement(sSQL);
            ps.setString(1, p_szTipoEdificioId);
            ps.setShort(2, p_sNivel);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("PRECIO");
            }
            return 0;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adEdificio. recuperarCosteMantenimiento. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }


    /**
     * Recupera un arraylist con los edificios susceptibles de sufrir deterioro.
     *
     * @return
     * @throws AbadiaException
     */
    public ArrayList<EdificioMantenimiento> recuperarEdificiosMantenimiento() throws AbadiaException {
        EdificioMantenimiento edificio;
        ArrayList<EdificioMantenimiento> alEdificios = new ArrayList<EdificioMantenimiento>();
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            cs = con.prepareCall("call recuperarEdificiosMantenimiento();");
            cs.execute();
            rs = cs.getResultSet();
            while (rs.next()) {
                edificio = new EdificioMantenimiento();
                edificio.setIdAbadia(rs.getInt("ABADIAID"));
                edificio.setIdEdificio(rs.getInt("EDIFICIOID"));
                edificio.setIdTipoEdificio(rs.getInt("TIPOEDIFICIOID"));
                edificio.setNivel(rs.getShort("NIVEL"));
                edificio.setEstado(rs.getInt("ESTADO"));
                edificio.setIdIdioma(rs.getInt("IDIOMAID"));
                alEdificios.add(edificio);
            }
            return alEdificios;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adEdificio. recuperarEdificiosMantenimiento. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(cs);
        }
    }

    /**
     * Reduce n puntos del estado del edificio debido al deterioro
     *
     * @param Edificioid
     * @param Desgaste
     * @throws AbadiaException
     */
    public void bajarEstadoEdificio(int Edificioid, double Desgaste) throws AbadiaException {
        adUtils utils = null;
        try {
            utils = new adUtils(con);
            utils.execSQL("Update edificio set estado = estado - " + Desgaste + " where edificioid =" + Edificioid);

        } catch (AbadiaException e) {
            throw new AbadiaSQLException("adEdificio. bajarEstadoEdificio.", e, log);
        }
    }

    /**
     * Suma n puntos del estado del edificio debido al mantenimiento
     *
     * @param Edificioid
     * @param Desgaste
     * @throws AbadiaException
     */
    public void subirEstadoEdificio(int Edificioid, double Desgaste) throws AbadiaException {
        adUtils utils = null;
        try {
            utils = new adUtils(con);
            utils.execSQL("Update edificio set estado = estado + " + Desgaste + " where edificioid =" + Edificioid);

        } catch (AbadiaException e) {
            throw new AbadiaSQLException("adEdificio. subirEstadoEdificio.", e, log);
        }
    }

    /**
     * Suma n puntos del estado del edificio debido al mantenimiento
     *
     * @param Edificioid
     * @param iEstado
     * @throws AbadiaException
     */
    public void asignarEstadoEdificio(int Edificioid, int iEstado) throws AbadiaException {
        adUtils utils = null;
        try {
            utils = new adUtils(con);
            utils.execSQL("Update edificio set estado = " + iEstado + " where edificioid =" + Edificioid);

        } catch (AbadiaException e) {
            throw new AbadiaSQLException("adEdificio. asignarEstadoEdificio.", e, log);
        }
    }

    /**
     * Recupera un arraylist con la información de los edificios para el proceso
     * de mantenimiento
     *
     * @return
     * @throws AbadiaException
     */
    public ArrayList<EdificioMantenimiento> recuperarParametrosMantenimiento() throws AbadiaException {
        String sSQL = "select e.ABADIAID, e.EDIFICIOID, e.TIPOEDIFICIOID, e.NIVEL, e.MANTENIMIENTO " +
                " FROM edificio e " +
                " WHERE e.MANTENIMIENTO > 0 and NIVEL > 0 " +
                " ORDER by TIPOEDIFICIOID, NIVEL";

        EdificioMantenimiento edificio;
        ArrayList<EdificioMantenimiento> alEdificios = new ArrayList<EdificioMantenimiento>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                edificio = new EdificioMantenimiento();
                edificio.setIdAbadia(rs.getInt("ABADIAID"));
                edificio.setIdEdificio(rs.getInt("EDIFICIOID"));
                edificio.setIdTipoEdificio(rs.getInt("TIPOEDIFICIOID"));
                edificio.setNivel(rs.getShort("NIVEL"));
                edificio.setMantenimiento(rs.getInt("MANTENIMIENTO"));
                alEdificios.add(edificio);
            }

            return alEdificios;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adEdificio. recuperarParametrosMantenimiento. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /**
     * Recupera un {@link ArrayList} de objetos {@link Edificio} con los edificios
     * de la abadia en cuestión
     *
     * @param abadia
     * @param usuario
     * @param resource
     * @return
     * @throws AbadiaException
     */
    public ArrayList<Edificio> recuperarListaEdificiosMantenimiento(Abadia abadia, Usuario usuario, MessageResources resource) throws AbadiaException {

        String sSQLEdificios = "Select e.EDIFICIOID, l.LITERAL NOMBRE, e.NIVEL, e.ESTADO, e.FECHACONSTRUCCION, e.TIPOEDIFICIOID, e.MANTENIMIENTO, m.PRECIO * e.MANTENIMIENTO AS PRECIO " +
                "from edificio as e, edificio_tipo as t, edificio_mantenimiento as m, literales as l " +
                "where e.ABADIAID = ? AND t.TIPOEDIFICIOID = e.TIPOEDIFICIOID AND e.TIPOEDIFICIOID = m.TIPOEDIFICIOID AND e.NIVEL = m.NIVEL AND l.LITERALID = t.NOMBRE AND l.IDIOMAID = ? order by t.TIPOEDIFICIOID ";

        ArrayList<Edificio> al = new ArrayList<Edificio>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQLEdificios);
            int parNo = 1;
            ps.setLong(parNo++, abadia.getIdDeAbadia());
            ps.setInt(parNo, usuario.getIdDeIdioma());
            rs = ps.executeQuery();
            Edificio edificio;

            while (rs.next()) {
                //iniciamos el volcado de datos sobre
                //el objeto edificio.
                edificio = new Edificio();
                edificio.setIdDeEdificio(rs.getInt("EDIFICIOID"));
                edificio.setIdDeTipoDeEdificio(rs.getInt("TIPOEDIFICIOID"));
                edificio.setFechaDeConstruccion(Utilidades.formatStringFromDB(rs.getString("FECHACONSTRUCCION")));
                edificio.setNombre(rs.getString("NOMBRE"));
                edificio.setNivel(rs.getInt("NIVEL"));
                edificio.setEstado(rs.getDouble("ESTADO"));
                edificio.setBarraEstado(HTML.smallBarra(HTML.getBarrasEstado((int) edificio.getEstado()), resource.getMessage("edificio.estado") + " " + Double.toString(edificio.getEstado())));
                edificio.setMantenimiento(rs.getInt("MANTENIMIENTO"));
                edificio.setCosteMantenimiento(Utilidades.redondear(rs.getDouble("PRECIO")));
                edificio.setCosteMantenimientoLong(rs.getLong("PRECIO"));
                edificio.setCosteMantenimientoD(rs.getDouble("PRECIO"));
                al.add(edificio);
            }
            return al;
        } catch (SQLException e) {
            throw new AbadiaSQLException("LOGBRP - recuperarListaEdificios error SQL: " + e.getMessage(), e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /**
     * Actualiza el nivel de mantenimiento de un edificio
     *
     * @param Edificioid
     * @param nivelMantenimiento
     * @throws AbadiaException
     */
    public void actualizarMantenimiento(int Edificioid, int nivelMantenimiento) throws AbadiaException {
        adUtils utils = null;
        try {
            utils = new adUtils(con);
            utils.execSQL("Update edificio set mantenimiento = " + nivelMantenimiento + " where edificioid =" + Edificioid);

        } catch (AbadiaException e) {
            throw new AbadiaSQLException("adEdificio. actualizarMantenimiento.", e, log);
        }
    }

    /**
     * pone a 100 el estado de los edificios que superan los 100 puntos de estado
     *
     * @throws AbadiaException
     */
    public void regularizarEstadoEdificios() throws AbadiaException {
        adUtils utils = null;
        try {
            utils = new adUtils(con);
            utils.execSQL("Update edificio set estado = 100 where estado > 100");

        } catch (AbadiaException e) {
            throw new AbadiaSQLException("adEdificio. regularizarEstadoEdificios.", e, log);
        }
    }

    /**
     * recupera un {@ArrayList} de objetos edificio con los datos de los edificios que
     * almacenan recursos por encima de su capacidad.
     *
     * @return
     * @throws AbadiaException
     */
    public ArrayList<EdificioSaturado> recuperarEdificiosSaturadosRecursos() throws AbadiaException {
        ArrayList<EdificioSaturado> alEdificios = new ArrayList<EdificioSaturado>();
        String sSQL = "call recuperarEdificiosSaturadosRecursos();";
        EdificioSaturado edificio;
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = con.prepareCall(sSQL);
            cs.execute();
            rs = cs.getResultSet();
            while (rs.next()) {
                edificio = new EdificioSaturado();
                edificio.setIdAbadia(rs.getInt("ABADIAID"));
                edificio.setIdEdificio(rs.getInt("EDIFICIOID"));
                edificio.setIdTipoEdificio(rs.getInt("TIPOEDIFICIOID"));
                edificio.setNivel(rs.getInt("NIVEL"));
                edificio.setIdIdioma(rs.getInt("IDIOMAID"));
                edificio.setAlmacenamientoMaximo(rs.getDouble("ALMACENAMIENTO"));
                edificio.setAlmacenamientoActual(rs.getDouble("CANTIDAD_TOTAL"));
                edificio.setNombre(rs.getString("LITERAL"));
                alEdificios.add(edificio);
            }
            return alEdificios;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adEdificio. recuperarEdificiosSaturadosRecursos. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(cs);
        }

    }


}

