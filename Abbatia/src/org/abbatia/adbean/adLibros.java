package org.abbatia.adbean;


import org.abbatia.actionform.FiltroLibrosActForm;
import org.abbatia.actionform.LibroDetalleActForm;
import org.abbatia.adbean.base.adbeans;
import org.abbatia.bean.*;
import org.abbatia.core.CoreTiempo;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.LibroNoEncontradoException;
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

public class adLibros extends adbeans {
    private static Logger log = Logger.getLogger(adLibros.class.getName());

    /**
     * Instancia la clase sin obtener una nueva conexión
     *
     * @param con
     * @throws AbadiaException
     */
    public adLibros(Connection con) throws AbadiaException {
        super(con);
    }


    /**
     * Recupera un {@link ArrayList} de objetos {@link Libro} con los libros de la biblioteca de la abadía
     *
     * @param abadia
     * @param usuario
     * @return
     * @throws AbadiaException
     */
    public ArrayList<Libro> recuperarLibrosPropios(Abadia abadia, Usuario usuario, MessageResources resource) throws AbadiaException {
        adMonje monjeAD;
        adAbadia oAbadiaAD;

        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            //creo un objeto de tipo PreparedStatement sobre el que se
            //ejecutara la consulta
            cs = con.prepareCall("call recuperarLibrosPropios(?,?)");
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave.

            int parNo = 1;
            cs.setShort(parNo++, usuario.getIdDeIdioma());
            cs.setLong(parNo, abadia.getIdDeAbadia());
            cs.execute();
            //Lanzo la consulta y cargo el resultado en un resultset
            rs = cs.getResultSet();
            //si la consulta encuentra la recurso....
            ArrayList<Libro> Libros = new ArrayList<Libro>();
            Libro libro;
            int idMonje;
            int iProgreso;
            int iDeterioro;

            while (rs.next()) {
                //iniciamos el volcado de datos sobre
                //el objeto recurso.
                libro = new Libro();
                libro.setIdLibro(rs.getInt("LIBROID"));
                libro.setIdLibro_origen(rs.getInt("LIBROID_ORIGEN"));
                libro.setIdLibroTipo(rs.getInt("TIPO_LIBROID"));
                libro.setNombreLibro(rs.getString("NOMBRE_LIBRO"));
                libro.setDescLibro(rs.getString("DESCRIPCION_LIBRO"));
                libro.setNombreCategoria(rs.getString("CATEGORIA_LIBRO"));
                libro.setFecha_adquisicion(Utilidades.formatStringFromDB(rs.getString("FECHA_ADQUISICION")));
                libro.setFecha_creacion(Utilidades.formatStringFromDB(rs.getString("FECHA_CREACION")));
                libro.setCopiable(rs.getInt("COPIABLE"));
                libro.setDesgaste(rs.getDouble("DESGASTE"));
                libro.setIdAbadia(rs.getInt("ABADIAID"));
                libro.setIdAbadia_copia(rs.getInt("ABADIAID_COPIA"));
                libro.setEstado(rs.getShort("ESTADO"));
                libro.setDescEstado(rs.getString("DESCRIPCION_ESTADO"));
                libro.setNivel(rs.getShort("NIVEL"));
                libro.setIdIdioma(rs.getInt("IDIOMAID"));
                libro.setIdioma_desc(rs.getString("DESCRIPCION_IDIOMA"));
                libro.setIdIdioma_origen(rs.getInt("IDIOMAID_ORIGEN"));
                libro.setNumPaginas(rs.getInt("PAGINAS"));
                libro.setNumPaginasCopiadas(rs.getDouble("PAGINAS_COPIADAS"));
                libro.setPrecioCopia(rs.getDouble("PRECIO_COPIA"));
                libro.setPrecioCopiaS(Utilidades.redondear(libro.getPrecioCopia()));
                //si el libro me pertenece..
                if (abadia.getIdDeAbadia() == libro.getIdAbadia()) {
                    libro.setEsCopiaLocal(1);
                } else libro.setEsCopiaLocal(0);

                if (libro.getEstado() == Constantes.ESTADO_LIBRO_COPIANDOSE || libro.getEstado() == Constantes.ESTADO_LIBRO_PENDIENTE || libro.getEstado() == Constantes.ESTADO_LIBRO_INCOMPLETO) {
                    idMonje = recuperarMonjePorLibroid(libro.getIdLibro());

                    monjeAD = new adMonje(con);
                    libro.setNombreMonje(monjeAD.getNomMonje(idMonje, resource.getMessage("monjes.abadia.de")));

                    oAbadiaAD = new adAbadia(con);
                    libro.setNombreAbadia(oAbadiaAD.getNomAbadia(libro.getIdAbadia()));

                } else if (libro.getEstado() == Constantes.ESTADO_LIBRO_COMPLETO || libro.getEstado() == Constantes.ESTADO_LIBRO_DETERIORADO) {
                    libro.setOcupacion(getFranjasOcupadas(libro.getIdLibro()) + "/" + "4");
                }

                //calculamos el grado de avance de la copia
                iProgreso = (int) (libro.getNumPaginasCopiadas() * 100 / libro.getNumPaginas()) / 10;
                libro.setProgreso(HTML.smallBarra(iProgreso, resource.getMessage("edificio.abadia.biblioteca.altprogreso", Utilidades.redondear(libro.getNumPaginasCopiadas()), String.valueOf(libro.getNumPaginas()))));

                //calculamos el grado de deterioro del libro
                iDeterioro = (int) (libro.getDesgaste()) / 10;
                libro.setDeterioro(HTML.smallBarraRed(iDeterioro, resource.getMessage("edificio.abadia.biblioteca.altdeterioro", Utilidades.redondear(libro.getDesgaste()), String.valueOf(100))));

                Libros.add(libro);
            }
            return Libros;

        } catch (SQLException e) {
            //log.error("adLibros. recuperarLibrosPropios. SQLException ", e);
            throw new AbadiaSQLException("adLibros. recuperarLibrosPropios. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(cs);
        }
    }

    /**
     * Recupera un {@link ArrayList} de objetos {@link Libro} con los libros de las bibliotecas de la REgion
     *
     * @param abadia
     * @param usuario
     * @param resource
     * @return
     * @throws AbadiaException
     */
    public ArrayList<Libro> recuperarLibrosRegion(Abadia abadia, Usuario usuario, MessageResources resource) throws AbadiaException {
        adMonje monjeAD;

        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            //creo un objeto de tipo PreparedStatement sobre el que se
            //ejecutara la consulta
            cs = con.prepareCall("call recuperarLibrosRegion(?,?)");
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave.

            int parNo = 1;
            cs.setShort(parNo++, usuario.getIdDeIdioma());
            cs.setLong(parNo, abadia.getIdDeRegion());
            cs.execute();

            //Lanzo la consulta y cargo el resultado en un resultset
            rs = cs.getResultSet();
            //si la consulta encuentra la recurso....
            ArrayList<Libro> Libros = new ArrayList<Libro>();
            Libro libro;
            int idMonje;
            int iProgreso;
            int iDeterioro;

            while (rs.next()) {
                //iniciamos el volcado de datos sobre
                //el objeto recurso.
                libro = new Libro();
                libro.setIdLibro(rs.getInt("LIBROID"));
                libro.setIdLibro_origen(rs.getInt("LIBROID_ORIGEN"));
                libro.setIdLibroTipo(rs.getInt("TIPO_LIBROID"));
                libro.setNombreLibro(rs.getString("NOMBRE_LIBRO"));
                libro.setDescLibro(rs.getString("DESCRIPCION_LIBRO"));
                libro.setNombreCategoria(rs.getString("CATEGORIA_LIBRO"));
                libro.setFecha_adquisicion(Utilidades.formatStringFromDB(rs.getString("FECHA_ADQUISICION")));
                libro.setFecha_creacion(Utilidades.formatStringFromDB(rs.getString("FECHA_CREACION")));
                libro.setCopiable(rs.getInt("COPIABLE"));
                libro.setDesgaste(rs.getDouble("DESGASTE"));
                libro.setIdAbadia(rs.getInt("ABADIAID"));
                libro.setNombreAbadia(rs.getString("NOMBRE_ABADIA"));
                libro.setIdAbadia_copia(rs.getInt("ABADIAID_COPIA"));
                libro.setEstado(rs.getShort("ESTADO"));
                libro.setDescEstado(rs.getString("DESCRIPCION_ESTADO"));
                libro.setNivel(rs.getShort("NIVEL"));
                libro.setNombreLibroNivel(libro.getNombreLibro() + " (" + libro.getNivel() + ")");
                libro.setIdIdioma(rs.getInt("IDIOMAID"));
                libro.setIdioma_desc(rs.getString("DESCRIPCION_IDIOMA"));
                libro.setIdIdioma_origen(rs.getInt("IDIOMAID_ORIGEN"));
                libro.setNumPaginas(rs.getInt("PAGINAS"));
                libro.setNumPaginasCopiadas(rs.getDouble("PAGINAS_COPIADAS"));
                libro.setPrecioCopia(rs.getDouble("PRECIO_COPIA"));
                libro.setPrecioCopiaS(Utilidades.redondear(libro.getPrecioCopia()));

                if (libro.getEstado() == Constantes.ESTADO_LIBRO_COPIANDOSE || libro.getEstado() == Constantes.ESTADO_LIBRO_PENDIENTE) {
                    idMonje = recuperarMonjePorLibroid(libro.getIdLibro());

                    monjeAD = new adMonje(con);
                    libro.setNombreMonje(monjeAD.getNomMonje(idMonje, resource.getMessage("monjes.abadia.de")));

                } else if (libro.getEstado() == Constantes.ESTADO_LIBRO_COMPLETO || libro.getEstado() == Constantes.ESTADO_LIBRO_DETERIORADO) {
                    libro.setFranjasOcupadas(getFranjasOcupadas(libro.getIdLibro()));
                    libro.setOcupacion(libro.getFranjasOcupadas() + "/" + "4");
                }

                //calculamos el grado de avance de la copia
                iProgreso = (int) (libro.getNumPaginasCopiadas() * 100 / libro.getNumPaginas()) / 10;
                libro.setProgreso(HTML.smallBarra(iProgreso, resource.getMessage("edificio.abadia.biblioteca.altprogreso", Utilidades.redondear(libro.getNumPaginasCopiadas()), String.valueOf(libro.getNumPaginas()))));

                //calculamos el grado de deterioro del libro
                iDeterioro = (int) (libro.getDesgaste()) / 10;
                libro.setDeterioro(HTML.smallBarraRed(iDeterioro, resource.getMessage("edificio.abadia.biblioteca.altdeterioro", Utilidades.redondear(libro.getDesgaste()), String.valueOf(100))));

                Libros.add(libro);
            }
            return Libros;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adLibros. recuperarLibros. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(cs);
        }
    }

    /**
     * Recupera un {@link ArrayList} de objetos {@link Libro} con los libros de las bibliotecas de la REgion
     *
     * @param abadia
     * @param usuario
     * @param resource
     * @return
     * @throws AbadiaException
     */
    public ArrayList<Libro> recuperarLibrosRegionFiltro(Abadia abadia, Usuario usuario, MessageResources resource,
                                                        FiltroLibrosActForm p_afFiltro) throws AbadiaException {
        adMonje monjeAD;

        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            //creo un objeto de tipo PreparedStatement sobre el que se
            //ejecutara la consulta
            cs = con.prepareCall("call recuperarLibrosRegionFiltro(?,?,?,?,?,?)");
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave.

            int parNo = 1;
            cs.setShort(parNo++, usuario.getIdDeIdioma());
            cs.setInt(parNo++, abadia.getIdDeRegion());
            cs.setInt(parNo++, p_afFiltro.getAbadia());
            cs.setInt(parNo++, p_afFiltro.getLibro());
            cs.setInt(parNo++, p_afFiltro.getIdioma());
            cs.setInt(parNo, p_afFiltro.getCategoria());
            cs.execute();

            //Lanzo la consulta y cargo el resultado en un resultset
            rs = cs.getResultSet();
            //si la consulta encuentra la recurso....
            ArrayList<Libro> Libros = new ArrayList<Libro>();
            Libro libro;
            int idMonje;
            int iProgreso;
            int iDeterioro;

            while (rs.next()) {
                //iniciamos el volcado de datos sobre
                //el objeto recurso.
                libro = new Libro();
                libro.setIdLibro(rs.getInt("LIBROID"));
                libro.setIdLibro_origen(rs.getInt("LIBROID_ORIGEN"));
                libro.setIdLibroTipo(rs.getInt("TIPO_LIBROID"));
                libro.setNombreLibro(rs.getString("NOMBRE_LIBRO"));
                libro.setDescLibro(rs.getString("DESCRIPCION_LIBRO"));
                libro.setNombreCategoria(rs.getString("CATEGORIA_LIBRO"));
                libro.setFecha_adquisicion(Utilidades.formatStringFromDB(rs.getString("FECHA_ADQUISICION")));
                libro.setFecha_creacion(Utilidades.formatStringFromDB(rs.getString("FECHA_CREACION")));
                libro.setCopiable(rs.getInt("COPIABLE"));
                libro.setDesgaste(rs.getDouble("DESGASTE"));
                libro.setIdAbadia(rs.getInt("ABADIAID"));
                libro.setNombreAbadia(rs.getString("NOMBRE_ABADIA"));
                libro.setIdAbadia_copia(rs.getInt("ABADIAID_COPIA"));
                libro.setEstado(rs.getShort("ESTADO"));
                libro.setDescEstado(rs.getString("DESCRIPCION_ESTADO"));
                libro.setNivel(rs.getShort("NIVEL"));
                libro.setNombreLibroNivel(libro.getNombreLibro() + " (" + libro.getNivel() + ")");
                libro.setIdIdioma(rs.getInt("IDIOMAID"));
                libro.setIdioma_desc(rs.getString("DESCRIPCION_IDIOMA"));
                libro.setIdIdioma_origen(rs.getInt("IDIOMAID_ORIGEN"));
                libro.setNumPaginas(rs.getInt("PAGINAS"));
                libro.setNumPaginasCopiadas(rs.getDouble("PAGINAS_COPIADAS"));
                libro.setPrecioCopia(rs.getDouble("PRECIO_COPIA"));
                libro.setPrecioCopiaS(Utilidades.redondear(libro.getPrecioCopia()));

                if (libro.getEstado() == Constantes.ESTADO_LIBRO_COPIANDOSE || libro.getEstado() == Constantes.ESTADO_LIBRO_PENDIENTE) {
                    idMonje = recuperarMonjePorLibroid(libro.getIdLibro());

                    monjeAD = new adMonje(con);
                    libro.setNombreMonje(monjeAD.getNomMonje(idMonje, resource.getMessage("monjes.abadia.de")));

                } else if (libro.getEstado() == Constantes.ESTADO_LIBRO_COMPLETO || libro.getEstado() == Constantes.ESTADO_LIBRO_DETERIORADO) {
                    libro.setFranjasOcupadas(getFranjasOcupadas(libro.getIdLibro()));
                    libro.setOcupacion(libro.getFranjasOcupadas() + "/" + "4");
                }

                //calculamos el grado de avance de la copia
                iProgreso = (int) (libro.getNumPaginasCopiadas() * 100 / libro.getNumPaginas()) / 10;
                libro.setProgreso(HTML.smallBarra(iProgreso, resource.getMessage("edificio.abadia.biblioteca.altprogreso", Utilidades.redondear(libro.getNumPaginasCopiadas()), String.valueOf(libro.getNumPaginas()))));

                //calculamos el grado de deterioro del libro
                iDeterioro = (int) (libro.getDesgaste()) / 10;
                libro.setDeterioro(HTML.smallBarraRed(iDeterioro, resource.getMessage("edificio.abadia.biblioteca.altdeterioro", Utilidades.redondear(libro.getDesgaste()), String.valueOf(100))));

                Libros.add(libro);
            }
            return Libros;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adLibros. recuperarLibros. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(cs);
        }
    }

    /**
     * Recupera un {@link ArrayList} de objetos {@link Libro} con los libros de las bibliotecas de todas las regiones
     *
     * @param abadia
     * @param usuario
     * @param resource
     * @return
     * @throws AbadiaException
     */
    public ArrayList<Libro> recuperarLibrosTodos(Abadia abadia, Usuario usuario, MessageResources resource) throws AbadiaException {
        adMonje monjeAD;

        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            //creo un objeto de tipo PreparedStatement sobre el que se
            //ejecutara la consulta
            cs = con.prepareCall("call recuperarLibrosTodos(?)");

            int parNo = 1;
            cs.setShort(parNo, usuario.getIdDeIdioma());
            cs.execute();
            //Lanzo la consulta y cargo el resultado en un resultset
            rs = cs.getResultSet();
            //si la consulta encuentra la recurso....
            ArrayList<Libro> Libros = new ArrayList<Libro>();
            Libro libro;
            int idMonje;
            int iProgreso;
            int iDeterioro;

            while (rs.next()) {
                //iniciamos el volcado de datos sobre
                //el objeto recurso.
                libro = new Libro();
                libro.setIdLibro(rs.getInt("LIBROID"));
                libro.setIdLibro_origen(rs.getInt("LIBROID_ORIGEN"));
                libro.setIdLibroTipo(rs.getInt("TIPO_LIBROID"));
                libro.setNombreLibro(rs.getString("NOMBRE_LIBRO"));
                libro.setDescLibro(rs.getString("DESCRIPCION_LIBRO"));
                libro.setNombreCategoria(rs.getString("CATEGORIA_LIBRO"));
                libro.setFecha_adquisicion(Utilidades.formatStringFromDB(rs.getString("FECHA_ADQUISICION")));
                libro.setFecha_creacion(Utilidades.formatStringFromDB(rs.getString("FECHA_CREACION")));
                libro.setCopiable(rs.getInt("COPIABLE"));
                libro.setDesgaste(rs.getDouble("DESGASTE"));
                libro.setIdAbadia(rs.getInt("ABADIAID"));
                libro.setNombreAbadia(rs.getString("NOMBRE_ABADIA"));
                libro.setIdRegion(rs.getInt("REGIONID"));
                libro.setNombreRegion(rs.getString("NOMBRE_REGION"));
                libro.setIdAbadia_copia(rs.getInt("ABADIAID_COPIA"));
                libro.setEstado(rs.getShort("ESTADO"));
                libro.setDescEstado(rs.getString("DESCRIPCION_ESTADO"));
                libro.setNivel(rs.getShort("NIVEL"));
                libro.setNombreLibroNivel(libro.getNombreLibro() + " (" + libro.getNivel() + ")");
                libro.setIdIdioma(rs.getInt("IDIOMAID"));
                libro.setIdioma_desc(rs.getString("DESCRIPCION_IDIOMA"));
                libro.setIdIdioma_origen(rs.getInt("IDIOMAID_ORIGEN"));
                libro.setNumPaginas(rs.getInt("PAGINAS"));
                libro.setNumPaginasCopiadas(rs.getDouble("PAGINAS_COPIADAS"));
                libro.setPrecioCopia(rs.getDouble("PRECIO_COPIA"));

                libro.setPrecioCopiaS(Utilidades.redondear(libro.getPrecioCopia()));

                if (libro.getEstado() == Constantes.ESTADO_LIBRO_COPIANDOSE || libro.getEstado() == Constantes.ESTADO_LIBRO_PENDIENTE) {
                    idMonje = recuperarMonjePorLibroid(libro.getIdLibro());

                    monjeAD = new adMonje(con);
                    libro.setNombreMonje(monjeAD.getNomMonje(idMonje, resource.getMessage("monjes.abadia.de")));
                } else if (libro.getEstado() == Constantes.ESTADO_LIBRO_COMPLETO || libro.getEstado() == Constantes.ESTADO_LIBRO_DETERIORADO) {
                    libro.setFranjasOcupadas(getFranjasOcupadas(libro.getIdLibro()));

                    libro.setOcupacion(libro.getFranjasOcupadas() + "/" + "4");
                }

                //calculamos el grado de avance de la copia
                iProgreso = (int) (libro.getNumPaginasCopiadas() * 100 / libro.getNumPaginas()) / 10;
                libro.setProgreso(HTML.smallBarra(iProgreso, resource.getMessage("edificio.abadia.biblioteca.altprogreso", Utilidades.redondear(libro.getNumPaginasCopiadas()), String.valueOf(libro.getNumPaginas()))));

                //calculamos el grado de deterioro del libro
                iDeterioro = (int) (libro.getDesgaste()) / 10;
                libro.setDeterioro(HTML.smallBarraRed(iDeterioro, resource.getMessage("edificio.abadia.biblioteca.altdeterioro", Utilidades.redondear(libro.getDesgaste()), String.valueOf(100))));

                Libros.add(libro);
            }
            return Libros;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adLibros. recuperarLibros. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(cs);
        }
    }

    /**
     * Recupera un {@link ArrayList} de objetos {@link Libro} con los libros de las bibliotecas de todas las regiones
     *
     * @param usuario
     * @param resource
     * @return
     * @throws AbadiaException
     */
    public ArrayList<Libro> recuperarLibrosTodosFiltro(Usuario usuario, MessageResources resource,
                                                       FiltroLibrosActForm p_afFiltro) throws AbadiaException {
        adMonje monjeAD;

        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            //creo un objeto de tipo PreparedStatement sobre el que se
            //ejecutara la consulta
            cs = con.prepareCall("call recuperarLibrosTodosFiltro(?,?,?,?,?,?)");

            int parNo = 1;
            cs.setShort(parNo++, usuario.getIdDeIdioma());
            cs.setInt(parNo++, p_afFiltro.getRegion());
            cs.setInt(parNo++, p_afFiltro.getAbadia());
            cs.setInt(parNo++, p_afFiltro.getLibro());
            cs.setInt(parNo++, p_afFiltro.getIdioma());
            cs.setInt(parNo, p_afFiltro.getCategoria());
            cs.execute();
            //Lanzo la consulta y cargo el resultado en un resultset
            rs = cs.getResultSet();
            //si la consulta encuentra la recurso....
            ArrayList<Libro> Libros = new ArrayList<Libro>();
            Libro libro;
            int idMonje;
            int iProgreso;
            int iDeterioro;

            while (rs.next()) {
                //iniciamos el volcado de datos sobre
                //el objeto recurso.
                libro = new Libro();
                libro.setIdLibro(rs.getInt("LIBROID"));
                libro.setIdLibro_origen(rs.getInt("LIBROID_ORIGEN"));
                libro.setIdLibroTipo(rs.getInt("TIPO_LIBROID"));
                libro.setNombreLibro(rs.getString("NOMBRE_LIBRO"));
                libro.setDescLibro(rs.getString("DESCRIPCION_LIBRO"));
                libro.setNombreCategoria(rs.getString("CATEGORIA_LIBRO"));
                libro.setFecha_adquisicion(Utilidades.formatStringFromDB(rs.getString("FECHA_ADQUISICION")));
                libro.setFecha_creacion(Utilidades.formatStringFromDB(rs.getString("FECHA_CREACION")));
                libro.setCopiable(rs.getInt("COPIABLE"));
                libro.setDesgaste(rs.getDouble("DESGASTE"));
                libro.setIdAbadia(rs.getInt("ABADIAID"));
                libro.setNombreAbadia(rs.getString("NOMBRE_ABADIA"));
                libro.setIdRegion(rs.getInt("REGIONID"));
                libro.setNombreRegion(rs.getString("NOMBRE_REGION"));
                libro.setIdAbadia_copia(rs.getInt("ABADIAID_COPIA"));
                libro.setEstado(rs.getShort("ESTADO"));
                libro.setDescEstado(rs.getString("DESCRIPCION_ESTADO"));
                libro.setNivel(rs.getShort("NIVEL"));
                libro.setNombreLibroNivel(libro.getNombreLibro() + " (" + libro.getNivel() + ")");
                libro.setIdIdioma(rs.getInt("IDIOMAID"));
                libro.setIdioma_desc(rs.getString("DESCRIPCION_IDIOMA"));
                libro.setIdIdioma_origen(rs.getInt("IDIOMAID_ORIGEN"));
                libro.setNumPaginas(rs.getInt("PAGINAS"));
                libro.setNumPaginasCopiadas(rs.getDouble("PAGINAS_COPIADAS"));
                libro.setPrecioCopia(rs.getDouble("PRECIO_COPIA"));

                libro.setPrecioCopiaS(Utilidades.redondear(libro.getPrecioCopia()));

                if (libro.getEstado() == Constantes.ESTADO_LIBRO_COPIANDOSE || libro.getEstado() == Constantes.ESTADO_LIBRO_PENDIENTE) {
                    idMonje = recuperarMonjePorLibroid(libro.getIdLibro());

                    monjeAD = new adMonje(con);
                    libro.setNombreMonje(monjeAD.getNomMonje(idMonje, resource.getMessage("monjes.abadia.de")));
                } else if (libro.getEstado() == Constantes.ESTADO_LIBRO_COMPLETO || libro.getEstado() == Constantes.ESTADO_LIBRO_DETERIORADO) {
                    libro.setFranjasOcupadas(getFranjasOcupadas(libro.getIdLibro()));

                    libro.setOcupacion(libro.getFranjasOcupadas() + "/" + "4");
                }

                //calculamos el grado de avance de la copia
                iProgreso = (int) (libro.getNumPaginasCopiadas() * 100 / libro.getNumPaginas()) / 10;
                libro.setProgreso(HTML.smallBarra(iProgreso, resource.getMessage("edificio.abadia.biblioteca.altprogreso", Utilidades.redondear(libro.getNumPaginasCopiadas()), String.valueOf(libro.getNumPaginas()))));

                //calculamos el grado de deterioro del libro
                iDeterioro = (int) (libro.getDesgaste()) / 10;
                libro.setDeterioro(HTML.smallBarraRed(iDeterioro, resource.getMessage("edificio.abadia.biblioteca.altdeterioro", Utilidades.redondear(libro.getDesgaste()), String.valueOf(100))));

                Libros.add(libro);
            }
            return Libros;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adLibros. recuperarLibros. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(cs);
        }
    }

    /**
     * Recupera un {@link ArrayList} de objetos {@link LibroTipo} con los libros de las bibliotecas de todas las regiones
     *
     * @param p_idIdioma
     * @return
     * @throws AbadiaException
     */
    public ArrayList<LibroTipo> recuperarLibrosTipo(int p_idIdioma) throws AbadiaException {
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            //creo un objeto de tipo PreparedStatement sobre el que se
            //ejecutara la consulta
            cs = con.prepareCall("call recuperarLibrosTipo(?)");
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave.

            int parNo = 1;
            cs.setInt(parNo, p_idIdioma);
            cs.execute();
            //Lanzo la consulta y cargo el resultado en un resultset
            rs = cs.getResultSet();
            //si la consulta encuentra la recurso....
            ArrayList<LibroTipo> Libros = new ArrayList<LibroTipo>();
            LibroTipo libro;

            while (rs.next()) {
                //iniciamos el volcado de datos sobre
                //el objeto recurso.
                libro = new LibroTipo();
                libro.setIdLibroTipo(rs.getInt("TIPO_LIBROID"));
                libro.setNombre(rs.getString("NOMBRE"));
                libro.setDescripcion(rs.getString("DESCRIPCION"));

                Libros.add(libro);
            }
            return Libros;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adLibros. recuperarLibrosTipo. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(cs);
        }
    }

    /**
     * Recupera un {@link ArrayList} de objetos {@link Libro} con los libros de las bibliotecas de todas las regiones
     *
     * @param abadia
     * @param usuario
     * @param resource
     * @return
     * @throws AbadiaException
     */
    public ArrayList<Libro> recuperarLibrosCopiando(Abadia abadia, Usuario usuario, MessageResources resource) throws AbadiaException {
        adMonje monjeAD;

        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            //creo un objeto de tipo PreparedStatement sobre el que se
            //ejecutara la consulta
            cs = con.prepareCall("call recuperarLibrosCopiando(?,?)");
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave.

            int parNo = 1;
            cs.setShort(parNo++, usuario.getIdDeIdioma());
            cs.setLong(parNo, abadia.getIdDeAbadia());
            cs.execute();
            //Lanzo la consulta y cargo el resultado en un resultset
            rs = cs.getResultSet();
            //si la consulta encuentra la recurso....
            ArrayList<Libro> Libros = new ArrayList<Libro>();
            Libro libro;
            int idMonje;
            int iProgreso;
            int iDeterioro;

            while (rs.next()) {
                //iniciamos el volcado de datos sobre
                //el objeto recurso.
                libro = new Libro();
                libro.setIdLibro(rs.getInt("LIBROID"));
                libro.setIdLibro_origen(rs.getInt("LIBROID_ORIGEN"));
                libro.setIdLibroTipo(rs.getInt("TIPO_LIBROID"));
                libro.setNombreLibro(rs.getString("NOMBRE_LIBRO"));
                libro.setDescLibro(rs.getString("DESCRIPCION_LIBRO"));
                libro.setFecha_adquisicion(Utilidades.formatStringFromDB(rs.getString("FECHA_ADQUISICION")));
                libro.setFecha_creacion(Utilidades.formatStringFromDB(rs.getString("FECHA_CREACION")));
                libro.setCopiable(rs.getInt("COPIABLE"));
                libro.setDesgaste(rs.getDouble("DESGASTE"));
                libro.setIdAbadia(rs.getInt("ABADIAID"));
                libro.setNombreAbadia(rs.getString("NOMBRE_ABADIA"));
                libro.setIdRegion(rs.getInt("REGIONID"));
                libro.setNombreRegion(rs.getString("NOMBRE_REGION"));
                libro.setIdAbadia_copia(rs.getInt("ABADIAID_COPIA"));
                libro.setEstado(rs.getShort("ESTADO"));
                libro.setDescEstado(rs.getString("DESCRIPCION_ESTADO"));
                libro.setNivel(rs.getShort("NIVEL"));
                libro.setNombreLibroNivel(libro.getNombreLibro() + " (" + libro.getNivel() + ")");
                libro.setIdIdioma(rs.getInt("IDIOMAID"));
                libro.setIdioma_desc(rs.getString("DESCRIPCION_IDIOMA"));
                libro.setIdIdioma_origen(rs.getInt("IDIOMAID_ORIGEN"));
                libro.setNumPaginas(rs.getInt("PAGINAS"));
                libro.setNumPaginasCopiadas(rs.getDouble("PAGINAS_COPIADAS"));
                libro.setPrecioCopia(rs.getDouble("PRECIO_COPIA"));

                libro.setPrecioCopiaS(Utilidades.redondear(libro.getPrecioCopia()));

                if (libro.getEstado() == Constantes.ESTADO_LIBRO_COPIANDOSE || libro.getEstado() == Constantes.ESTADO_LIBRO_PENDIENTE) {
                    idMonje = recuperarMonjePorLibroid(libro.getIdLibro());

                    monjeAD = new adMonje(con);
                    libro.setNombreMonje(monjeAD.getNomMonje(idMonje, resource.getMessage("monjes.abadia.de")));
                }
                //calculamos el grado de avance de la copia
                iProgreso = (int) (libro.getNumPaginasCopiadas() * 100 / libro.getNumPaginas()) / 10;
                libro.setProgreso(HTML.smallBarra(iProgreso, resource.getMessage("edificio.abadia.biblioteca.altprogreso", Utilidades.redondear(libro.getNumPaginasCopiadas()), String.valueOf(libro.getNumPaginas()))));

                //calculamos el grado de deterioro del libro
                iDeterioro = (int) (libro.getDesgaste()) / 10;
                libro.setDeterioro(HTML.smallBarraRed(iDeterioro, resource.getMessage("edificio.abadia.biblioteca.altdeterioro", Utilidades.redondear(libro.getDesgaste()), String.valueOf(100))));

                Libros.add(libro);
            }
            return Libros;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adLibros. recuperarLibrosCopiando. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(cs);
        }
    }

    /**
     * Recupera los datos de un libro
     *
     * @param idLibro
     * @param idIdioma
     * @return
     * @throws AbadiaException
     */
    public Libro recuperarLibro(int idLibro, int idIdioma) throws AbadiaException {
        String sSQL = "Select l.libroid, li.literal as DESC_LIBRO, l.abadiaid, l.nivel, l.tipo_libroid, l.estado, l.idiomaid, l.libroid_origen, ln.precio_max, ln.precio_min, l.precio_copia, ln.CTD_PIEL_ENCUADERNAR, li2.literal as DESC_ESTADO, l.paginas_copiadas, l.DESGASTE " +
                " from libro l, libro_tipo lt, literales li, literales li2, libro_estado le, libro_nivel ln " +
                " where l.tipo_libroid = lt.tipo_libroid and lt.literalid = li.literalid and li.idiomaid = ? and l.estado = le.estado and le.literalid = li2.literalid and li2.idiomaid = li.idiomaid and l.libroid = ? and l.tipo_libroid = ln.tipo_libroid and l.nivel = ln.nivel ";
        Libro libro = new Libro();
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idIdioma);
            ps.setInt(2, idLibro);
            rs = ps.executeQuery();
            if (rs.next()) {
                libro.setIdLibro(rs.getInt("LIBROID"));
                libro.setNombreLibro(rs.getString("DESC_LIBRO"));
                libro.setDescEstado(rs.getString("DESC_ESTADO"));
                libro.setNivel(rs.getShort("NIVEL"));
                libro.setIdAbadia(rs.getInt("ABADIAID"));
                libro.setIdLibroTipo(rs.getInt("TIPO_LIBROID"));
                libro.setEstado(rs.getShort("ESTADO"));
                libro.setDesgaste(rs.getDouble("DESGASTE"));
                libro.setIdIdioma(rs.getInt("IDIOMAID"));
                libro.setIdLibro_origen(rs.getInt("LIBROID_ORIGEN"));
                libro.setPrecioMax(rs.getDouble("PRECIO_MAX"));
                libro.setPrecioMin(rs.getDouble("PRECIO_MIN"));
                libro.setPrecioCopiaS(Utilidades.redondear(rs.getDouble("PRECIO_COPIA")));
                libro.setPrecioCopia(rs.getDouble("PRECIO_COPIA"));
                libro.setCtdPielEncuadernar(rs.getDouble("CTD_PIEL_ENCUADERNAR"));
                libro.setNumPaginasCopiadas(rs.getDouble("PAGINAS_COPIADAS"));
            }
            return libro;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adLibros. recuperarLibro. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Verifica si el libro tiene un siguiente nivel
     *
     * @param idLibroTipo
     * @param nivel
     * @return
     * @throws AbadiaException
     */
    public boolean existeSiguienteNivelLibro(int idLibroTipo, int nivel) throws AbadiaException {
        String sSQL = "Select * " +
                " from libro_nivel ln " +
                " where ln.tipo_libroid = ? and nivel = ?";

        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idLibroTipo);
            ps.setInt(2, nivel + 1);
            rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            throw new AbadiaSQLException("adLibros. existeSiguienteNivelLibro. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Recupera los datos de un libro
     *
     * @param idLibro
     * @return
     * @throws AbadiaException
     */
    public Libro recuperarLibro(int idLibro) throws AbadiaException {
        String sSQL = "Select l.libroid, l.abadiaid, l.tipo_libroid, l.estado, l.idiomaid, l.libroid_origen, ln.precio_max, ln.precio_min, l.precio_copia, l.paginas_copiadas, ln.paginas, l.abadiaid_copia, l.DESGASTE " +
                " from libro l, libro_tipo lt, libro_nivel ln " +
                " where l.tipo_libroid = lt.tipo_libroid and l.libroid = ? and l.tipo_libroid = ln.tipo_libroid and l.nivel = ln.nivel ";
        Libro libro = new Libro();
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idLibro);
            rs = ps.executeQuery();
            if (rs.next()) {
                libro.setIdLibro(rs.getInt("LIBROID"));
                libro.setIdAbadia(rs.getInt("ABADIAID"));
                libro.setIdLibroTipo(rs.getInt("TIPO_LIBROID"));
                libro.setEstado(rs.getShort("ESTADO"));
                libro.setDesgaste(rs.getDouble("DESGASTE"));
                libro.setIdIdioma(rs.getInt("IDIOMAID"));
                libro.setIdLibro_origen(rs.getInt("LIBROID_ORIGEN"));
                libro.setPrecioMax(rs.getDouble("PRECIO_MAX"));
                libro.setPrecioMin(rs.getDouble("PRECIO_MIN"));
                libro.setPrecioCopiaS(Utilidades.redondear(rs.getDouble("PRECIO_COPIA")));
                libro.setPrecioCopia(rs.getDouble("PRECIO_COPIA"));
                libro.setNumPaginas(rs.getInt("PAGINAS"));
                libro.setNumPaginasCopiadas(rs.getDouble("PAGINAS_COPIADAS"));
                libro.setIdAbadia_copia(rs.getInt("ABADIAID_COPIA"));
            } else {
                throw new LibroNoEncontradoException("adLibros. recuperarLibro. LibroNoEncontradoException", log);
            }
            return libro;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adLibros. recuperarLibro. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Recupera el tipo de libro a partir del tipo de libro
     *
     * @param tipo_Libroid
     * @return
     * @throws AbadiaException
     */
    public Libro recuperaLibroTipo(int tipo_Libroid, int abadiaid) throws AbadiaException {
        String sSQL = "Select l.libroid, l.abadiaid, l.tipo_libroid, l.estado, l.idiomaid, l.libroid_origen, ln.precio_max, ln.precio_min, l.precio_copia, l.paginas_copiadas, ln.paginas, l.abadiaid_copia,l.nivel, l.DESGASTE " +
                " from libro l, libro_tipo lt, libro_nivel ln " +
                " where l.abadiaid = ? and l.tipo_libroid = ? and l.tipo_libroid = ln.tipo_libroid and l.nivel = ln.nivel and l.estado in (" + Constantes.ESTADO_LIBRO_COMPLETO + ", " + Constantes.ESTADO_LIBRO_RESTAURANDO + ") order by nivel desc limit 1 ";
        Libro libro = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, abadiaid);
            ps.setInt(2, tipo_Libroid);
            rs = ps.executeQuery();
            if (rs.next()) {
                libro = new Libro();
                libro.setIdLibro(rs.getInt("LIBROID"));
                libro.setIdAbadia(rs.getInt("ABADIAID"));
                libro.setIdLibroTipo(rs.getInt("TIPO_LIBROID"));
                libro.setEstado(rs.getShort("ESTADO"));
                libro.setIdIdioma(rs.getInt("IDIOMAID"));
                libro.setDesgaste(rs.getDouble("DESGASTE"));
                libro.setIdLibro_origen(rs.getInt("LIBROID_ORIGEN"));
                libro.setPrecioMax(rs.getDouble("PRECIO_MAX"));
                libro.setPrecioMin(rs.getDouble("PRECIO_MIN"));
                libro.setPrecioCopiaS(Utilidades.redondear(rs.getDouble("PRECIO_COPIA")));
                libro.setPrecioCopia(rs.getDouble("PRECIO_COPIA"));
                libro.setNumPaginas(rs.getInt("PAGINAS"));
                libro.setNumPaginasCopiadas(rs.getDouble("PAGINAS_COPIADAS"));
                libro.setIdAbadia_copia(rs.getInt("ABADIAID_COPIA"));
                libro.setNivel(rs.getShort("NIVEL"));
            }
            return libro;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adLibros. recuperaLibroTipo. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Recupera el tipo de libro a partir del tipo de libro
     *
     * @param tipo_Libroid
     * @return
     * @throws AbadiaException
     */
    public Libro recuperaLibroNivel(int tipo_Libroid) throws AbadiaException {
        String sSQL = "Select ln.tipo_libroid, ln.precio_max, ln.precio_min, ln.paginas " +
                " from libro_nivel ln " +
                " where ln.tipo_libroid =  ? and ln.nivel = 1 ";
        Libro libro = new Libro();
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, tipo_Libroid);
            rs = ps.executeQuery();
            if (rs.next()) {
                libro.setIdLibroTipo(rs.getInt("TIPO_LIBROID"));
                libro.setPrecioMax(rs.getDouble("PRECIO_MAX"));
                libro.setPrecioMin(rs.getDouble("PRECIO_MIN"));
                libro.setNumPaginas(rs.getInt("PAGINAS"));
            }
            return libro;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adLibros. recuperaLibroNivel. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Devuelve el número de franjas horarias disponibles para copiar un libro en concreto
     *
     * @param claveLibro
     * @return
     * @throws AbadiaException
     */
    public int getFranjasOcupadas(String claveLibro) throws AbadiaException {
        return getFranjasOcupadas(Integer.parseInt(claveLibro));
    }

    /**
     * Devuelve el número de franjas horarias disponibles para copiar un libro en concreto
     *
     * @param claveLibro
     * @return
     * @throws AbadiaException
     */
    public int getFranjasOcupadas(int claveLibro) throws AbadiaException {
        adUtils utilsAD = new adUtils(con);
        return utilsAD.getSQL("Select count(*) from libro_tarea where libro_origenid = " + claveLibro, 0);
    }

    /**
     * Devuelve un identificador de si la franja de copia esta ocupada para un libro determinado
     * 0 - periodo/libro - libre
     * 1 - periodo/libro - Ocupado
     *
     * @param idLibro
     * @param idPeriodo
     * @return
     * @throws AbadiaException
     */
    public int getPeriodoLibroOcupado(int idLibro, int idPeriodo) throws AbadiaException {
        adUtils utilsAD = new adUtils(con);
        return utilsAD.getSQL("Select count(*) from libro_tarea where libro_origenid = " + idLibro + " and periodoid = " + idPeriodo, 0);
    }

    public int crearRegistroCopia(Libro copia, Abadia abadia) throws AbadiaException {


        String sSQL = "INSERT INTO libro (TIPO_LIBROID, ABADIAID, ESTADO, IDIOMAID, NIVEL, LIBROID_ORIGEN, IDIOMAID_ORIGEN, ABADIAID_COPIA) " +
                "VALUES (" + copia.getIdLibroTipo() + ", " +
                abadia.getIdDeAbadia() + ", " + Constantes.ESTADO_LIBRO_PENDIENTE + ", " +
                abadia.getIdIdioma() + ", " + copia.getNivel() + ", " +
                copia.getIdLibro() + ", " +
                copia.getIdIdioma() + ", " +
                copia.getIdAbadia() + ")";

        ResultSet rs = null;
        Statement stmt = null;
        try {
            stmt = con.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_UPDATABLE);
            stmt.executeUpdate(sSQL, Statement.RETURN_GENERATED_KEYS);
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            } else return 0;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adLibro. crearRegistroCopia. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(stmt);
        }


    }

    public void crearRegistroLibro(Libro copia) throws AbadiaException {

        String sSQL = "INSERT INTO libro (TIPO_LIBROID, FECHA_CREACION, FECHA_ADQUISICION, ABADIAID, ESTADO, IDIOMAID, NIVEL, LIBROID_ORIGEN, IDIOMAID_ORIGEN, ABADIAID_COPIA, PRECIO_COPIA) " +
                "VALUES (" + copia.getIdLibroTipo() + ", '" +
                copia.getFecha_creacion() + "', '" +
                copia.getFecha_adquisicion() + "', " +
                copia.getIdAbadia() + ", " + Constantes.ESTADO_LIBRO_COMPLETO + ", " +
                copia.getIdIdioma() + ", 1, " +
                " 0, " +
                copia.getIdIdioma() + ", " +
                copia.getIdAbadia() + ", " +
                copia.getPrecioMin() + " )";

        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.execute(sSQL);

        } catch (SQLException e) {
            throw new AbadiaSQLException("adLibro. crearRegistroLibro. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }


    }


    /**
     * Crea un copia de libro desde prVarios a la llegada de un monje de viaje
     *
     * @param copia
     * @param idAbadia
     * @param idIdioma
     * @return
     * @throws AbadiaException
     */
    public int crearRegistroCopiaProceso(Libro copia, int idAbadia, int idIdioma) throws AbadiaException {

        String sSQL = "INSERT INTO libro (TIPO_LIBROID, ABADIAID, ESTADO, IDIOMAID, NIVEL, LIBROID_ORIGEN, IDIOMAID_ORIGEN, ABADIAID_COPIA) " +
                "VALUES (" + copia.getIdLibroTipo() + ", " +
                idAbadia + ", " + Constantes.ESTADO_LIBRO_PENDIENTE + ", " +
                idIdioma + ", " + copia.getNivel() + ", " +
                copia.getIdLibro() + ", " +
                copia.getIdIdioma() + ", " +
                copia.getIdAbadia() + ")";

        ResultSet rs = null;
        Statement stmt = null;
        try {
            stmt = con.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_UPDATABLE);
            stmt.executeUpdate(sSQL, Statement.RETURN_GENERATED_KEYS);
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            } else return 0;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adLibro. crearRegistroCopiaProceso. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(stmt);
        }


    }

    /**
     * Crea un registro en la tabla libro_tarea con la informacion sobre el libro
     * original, su copia, el periodo y el monje que ocupa el periodo.
     *
     * @param idLibro
     * @param idLibro_Origen
     * @param idMonje
     * @param idPeriodo
     * @param estado
     * @throws AbadiaException
     */
    public void crearRegistroLibroTarea(int idLibro, int idLibro_Origen, int idMonje, long idPeriodo, short estado) throws AbadiaException {
        String sSQL = "INSERT INTO libro_tarea (LIBRO_ORIGENID, LIBRO_COPIAID, PERIODOID, MONJEID, ESTADO) VALUES (?,?,?,?,?)";

        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idLibro_Origen);
            ps.setInt(2, idLibro);
            ps.setInt(3, (int) idPeriodo);
            ps.setInt(4, idMonje);
            ps.setInt(5, estado);

            ps.execute();

        } catch (SQLException e) {
            throw new AbadiaSQLException("adLibro. crearRegistroLibroTarea. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Devuelve el identificador de libro origen que esta copiando un monje determinado
     *
     * @param idMonje
     * @param idPeriodo
     * @return
     * @throws AbadiaException
     */
    public int recupearLibroPorTareaMonje(int idMonje, int idPeriodo) throws AbadiaException {
        adUtils utils = new adUtils(con);
        return utils.getSQL("Select LIBRO_ORIGENID from libro_tarea where monjeid = " + idMonje + " and periodoid = " + idPeriodo, 0);
    }

    /**
     * Devuelve el identificador de libro origen que esta asociado a la copia
     *
     * @param idMonje
     * @return
     * @throws AbadiaException
     */
    public int recuperarLibroPorTareaMonje(int idMonje) throws AbadiaException {
        adUtils utils = new adUtils(con);
        return utils.getSQL("Select LIBRO_ORIGENID from libro_tarea where monjeid = " + idMonje, 0);
    }

    /**
     * Devuelve el precio de copia asociado a un libro
     *
     * @param idLibro
     * @return
     * @throws AbadiaException
     */
    public double recuperarPrecioCopiaLibro(int idLibro) throws AbadiaException {
        double precio;
        adUtils utils = new adUtils(con);
        precio = utils.getSQL("Select PRECIO_COPIA from libro where libroid = " + idLibro, 0);

        return precio;
    }

    /**
     * Verifica si existe una copia de ese libro que esté incompleta
     *
     * @param idLibro
     * @return
     * @throws AbadiaException
     */
    public int existeCopiaIncompleta(int idLibro, int idAbadia) throws AbadiaException {
        int result;
        adUtils utils = new adUtils(con);
        result = utils.getSQL("Select LIBROID from libro where LIBROID_ORIGEN = " + idLibro + " and ABADIAID = " + idAbadia + " and ESTADO = " + Constantes.ESTADO_LIBRO_INCOMPLETO, 0);

        return result;
    }

    /**
     * Verifica si existe una copia de ese libro que esté incompleta
     *
     * @param idTipoLibro
     * @param idNivel
     * @param idAbadia
     * @return
     * @throws AbadiaException
     */
    public int existeCopiaIncompletaTipo(int idTipoLibro, int idNivel, int idAbadia) throws AbadiaException {
        int result;
        adUtils utils = new adUtils(con);
        result = utils.getSQL("Select LIBROID from libro where TIPO_LIBROID = " + idTipoLibro + " and nivel = " + idNivel + " and ABADIAID = " + idAbadia + " and ESTADO = " + Constantes.ESTADO_LIBRO_INCOMPLETO, 0);

        return result;
    }

    /**
     * Verifica si existe una copia de ese libro que esté incompleta en una abadia determinada
     *
     * @param idTipoLibro
     * @param idNivel
     * @param idAbadia
     * @param idAbadia_Copia
     * @return
     * @throws AbadiaException
     */
    public int existeCopiaIncompletaTipo(int idTipoLibro, int idNivel, int idAbadia, int idAbadia_Copia) throws AbadiaException {
        int result;
        adUtils utils = new adUtils(con);
        result = utils.getSQL("Select LIBROID from libro where TIPO_LIBROID = " + idTipoLibro + " and nivel = " + idNivel + " and ABADIAID = " + idAbadia + " and ABADIAID_COPIA = " + idAbadia_Copia + " and ESTADO = " + Constantes.ESTADO_LIBRO_INCOMPLETO, 0);

        return result;
    }


    /**
     * Devuelve el identificador de libro origen que esta asociado a la copia
     *
     * @param idMonje
     * @return
     * @throws AbadiaException
     */
    public int recuperarLibroPorTareaMonjeCopia(int idMonje) throws AbadiaException {
        int idLibro;
        adUtils utils = new adUtils(con);
        idLibro = utils.getSQL("Select LIBRO_COPIAID from libro_tarea where monjeid = " + idMonje, 0);

        return idLibro;
    }

    /**
     * Devuelve el id del monje que está copiando un libro determinado
     *
     * @param idLibro
     * @return
     * @throws AbadiaException
     */
    public int recuperarMonjePorLibroid(int idLibro) throws AbadiaException {
        adUtils utils = new adUtils(con);
        return utils.getSQL("Select monjeid from libro_tarea where LIBRO_COPIAID = " + idLibro, 0);
    }

    /**
     * Devuelve el id del monje que está copiando un libro determinado
     *
     * @param idLibro
     * @return
     * @throws AbadiaException
     */
    public boolean seEstaCopiando(int idLibro) throws AbadiaException {
        int cont;
        adUtils utils = new adUtils(con);
        cont = utils.getSQL("Select count(*) from libro_tarea where LIBRO_ORIGENID = " + idLibro, 0);

        return cont != 0;
    }

    public ArrayList<Integer> recupearTareasPorMonjeLibro(int idLibro, int idMonje) throws AbadiaException {
        adUtils utils = new adUtils(con);
        return utils.getSQLIntArray("select periodoid from libro_tarea where libro_copiaid = " + idLibro + " and monjeid = " + idMonje);
    }

    public ArrayList<Integer> recupearIDLibrosEnRestauracion(int idAbadia) throws AbadiaException {
        adUtils utils = new adUtils(con);
        return utils.getSQLIntArray("select LIBROID from libro where ABADIAID = " + idAbadia + " and ESTADO = " + Constantes.ESTADO_LIBRO_RESTAURANDO);
    }

    /**
     * Elimina los registros de libro_tarea de un monje determinado para un libro determinado
     *
     * @param idMonje
     * @param idLibro
     * @throws AbadiaException
     */
    public void eliminarTareasPorMonjeLibro(int idMonje, int idLibro) throws AbadiaException {
        adUtils utils = new adUtils(con);
        utils.execSQL("delete from libro_tarea where monjeid = " + idMonje + " and libro_copiaid = " + idLibro);
    }

    /**
     * Elimina los registros de libro_tarea de un monje determinado
     *
     * @param idMonje
     * @throws org.abbatia.exception.base.AbadiaException
     *
     */
    public void eliminarTareasCopiaPorMonje(int idMonje) throws AbadiaException {
        adUtils utils = new adUtils(con);
        utils.execSQL("delete from libro_tarea where monjeid = " + idMonje);
    }

    /**
     * Elimina los registros de libro_tarea de todos los monjes de una abadia
     *
     * @param idAbadia
     * @throws AbadiaException
     */
    public void eliminarTareasCopiaPorAbadia(int idAbadia) throws AbadiaException {
        adUtils utils = new adUtils(con);
        utils.execSQL("delete from libro_tarea where monjeid in (select monjeid from monje where abadiaid = " + idAbadia + ")");
    }

    public void eliminarTareaPorMonjeLibro(int idMonje, int idLibro, int idTarea) throws AbadiaException {
        adUtils utils = new adUtils(con);
        utils.execSQL("delete from libro_tarea where monjeid = " + idMonje + " and libro_copiaid = " + idLibro + " and periodoid = " + idTarea);
    }


    public void eliminarLibro(int idLibro) throws AbadiaException {
        adUtils utils = new adUtils(con);
        utils.execSQL("delete from libro where libroid = " + idLibro);
    }

    /**
     * Elimina todos los registros de libro de la abadía que se elimina
     *
     * @param idAbadia
     * @throws org.abbatia.exception.base.AbadiaException
     *
     */
    public void eliminarLibrosAbadia(int idAbadia) throws AbadiaException {
        adUtils utils = new adUtils(con);
        utils.execSQL("delete from libro where abadiaid = " + idAbadia);
    }

    /**
     * Recupera un {@link ArrayList} de objetos {@link Libro} para el proceso de copia
     *
     * @param idPeriodo
     * @return
     * @throws AbadiaException
     */
    public ArrayList<Libro> recuperarLibrosProceso(int idPeriodo) throws AbadiaException {
        //Definición de cadena sql de consulta
        String sSQL = "select l.paginas_copiadas,l.abadiaid, l.libroid, l.libroid_origen, l.idiomaid, l.idiomaid_origen,  ln.paginas, l.desgaste, lt.monjeid, l.fecha_creacion, l.abadiaid_copia, l.estado, l.nivel, u.idiomaid as idioma_usuario, l.tipo_libroid, r.idiomaid as idioma_region " +
                " from libro l, libro_nivel ln, libro_tarea lt, abadia a, usuario u, region r " +
                " where l.tipo_libroid = ln.tipo_libroid and l.nivel = ln.nivel and l.abadiaid_copia = a.abadiaid and a.usuarioid = u.usuarioid and a.regionid = r.regionid and " +
                " l.estado in (0,1,3) and lt.libro_copiaid = l.libroid and u.abadia_congelada=0 and lt.periodoid = ? order by l.abadiaid, u.idiomaid";
        //creo un objeto de tipo Recurso

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //creo un objeto de tipo PreparedStatement sobre el que se
            //ejecutara la consulta
            ps = con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave.

            int parNo = 1;
            ps.setInt(parNo, idPeriodo);

            //Lanzo la consulta y cargo el resultado en un resultset
            rs = ps.executeQuery();
            //si la consulta encuentra la recurso....
            ArrayList<Libro> Libros = new ArrayList<Libro>();
            Libro libro;
            while (rs.next()) {
                //iniciamos el volcado de datos sobre
                //el objeto recurso.
                libro = new Libro();
                libro.setIdAbadia(rs.getInt("ABADIAID"));
                libro.setIdLibro(rs.getInt("LIBROID"));
                libro.setIdLibro_origen(rs.getInt("LIBROID_ORIGEN"));
                libro.setFecha_creacion(rs.getString("FECHA_CREACION"));
                libro.setIdAbadia_copia(rs.getInt("ABADIAID_COPIA"));
                libro.setDesgaste(rs.getDouble("DESGASTE"));
                libro.setEstado(rs.getShort("ESTADO"));
                libro.setNivel(rs.getShort("NIVEL"));
                libro.setIdIdioma(rs.getInt("IDIOMAID"));
                libro.setIdIdioma_origen(rs.getInt("IDIOMAID_ORIGEN"));
                libro.setNumPaginas(rs.getInt("PAGINAS"));
                libro.setNumPaginasCopiadas(rs.getDouble("PAGINAS_COPIADAS"));
                libro.setIdIdioma_usuario(rs.getInt("IDIOMA_USUARIO"));
                libro.setIdMonje(rs.getInt("MONJEID"));
                libro.setIdLibroTipo(rs.getInt("TIPO_LIBROID"));
                Libros.add(libro);
            }
            return Libros;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adLibros. recuperarLibrosProceso. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Recupera un String con el nombre del libro
     *
     * @param idLibroTipo
     * @param idIdioma
     * @return
     * @throws AbadiaException
     */
    public String recuperarNombreLibro(int idLibroTipo, int idIdioma) throws AbadiaException {
        String nombreLibro = "";
        String sSQL = "Select l.literal from literales l, libro_tipo lt where lt.tipo_libroid = " + idLibroTipo + " and lt.literalid = l.literalid and l.idiomaid = " + idIdioma;
        adUtils utils = new adUtils(con);
        nombreLibro = utils.getSQL(sSQL, "");
        return nombreLibro;
    }

    /**
     * Actualiza el número de páginas copiadas de un libro
     *
     * @param idLibro
     * @param paginas
     * @throws AbadiaException
     */
    public void actualizarPaginasLibro(int idLibro, double paginas) throws AbadiaException {
        adUtils utils = new adUtils(con);
        utils.execSQL("update libro set paginas_copiadas = " + paginas + " + paginas_copiadas where libroid = " + idLibro);
    }

    /**
     * Actualiza el estado del libro
     *
     * @param idLibro
     * @param idEstado
     * @throws AbadiaException
     */
    public void actualizarEstadoLibro(int idLibro, short idEstado) throws AbadiaException {
        adUtils utils = new adUtils(con);
        utils.execSQL("update libro set estado = " + idEstado + " where libroid = " + idLibro);
    }

    /**
     * Actualiza el id de copia de un libro en la tabla libro_tarea
     *
     * @param idLibro
     * @param idLibroCopia
     * @param idMonje
     * @throws AbadiaException
     */
    public void actualizarIdLibroCopiaTarea(int idLibro, int idLibroCopia, int idMonje) throws AbadiaException {
        adUtils utils = new adUtils(con);
        utils.execSQL("update libro_tarea set libro_copiaid = " + idLibroCopia + " where libro_origenid = " + idLibro + " and monjeid = " + idMonje);
    }

    /**
     * Actualiza el precio de copia del un libro
     *
     * @param idLibro
     * @param precio
     * @throws AbadiaException
     */
    public void actualizarPrecioCopia(int idLibro, double precio) throws AbadiaException {
        adUtils utils = new adUtils(con);
        utils.execSQL("update libro set precio_copia = " + precio + " where libroid = " + idLibro);
    }

    /**
     * Actualiza el registro de la tabla libro a partir de un objeto {@link Libro}
     *
     * @param libro
     * @throws AbadiaException
     */
    public void actualizarLibro(Libro libro) throws AbadiaException {
        String sTrace = this.getClass() + ".actualizarLibro(" + libro + ")";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        String sSQL = "update libro set paginas_copiadas = ?, estado = ?, fecha_creacion = ?, precio_copia = ?  where libroid = ? ";

        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setDouble(1, libro.getNumPaginasCopiadas());
            ps.setShort(2, libro.getEstado());
            ps.setString(3, libro.getFecha_creacion());
            ps.setDouble(4, libro.getPrecioCopia());
            ps.setInt(5, libro.getIdLibro());
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error(libro.toString(), e);
            throw new AbadiaSQLException("adLibros. actualizarLibro. SQLException", e, log);
        } finally {
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Recupera el detalle de un libro
     *
     * @param idLibro
     * @param idIdioma
     * @return
     * @throws AbadiaException
     */
    public LibroDetalleActForm recuperarDetalleLibro(int idLibro, int idIdioma, MessageResources resource) throws AbadiaException {
        String sSQL = "Select l.libroid, li.literal as literal_nombre, l.abadiaid, l.tipo_libroid, l.estado, l.idiomaid, li3.literal as literal_idioma, l.desgaste, " +
                " ln.paginas, li2.literal as literal_desc, a.nombre as nombre_abadia, r.descripcion as nombre_region, l.desgaste, lt.grafico, l.nivel, ln.precio_max, ln.precio_min, l.precio_copia " +
                " from libro l, libro_tipo lt, literales li, libro_nivel ln, literales li2, abadia a, region r, literales li3, idioma i  " +
                " where l.tipo_libroid = lt.tipo_libroid and lt.literalid = li.literalid and " +
                " l.nivel = ln.nivel and l.tipo_libroid = ln.tipo_libroid and a.abadiaid = l.abadiaid and " +
                " a.regionid = r.regionid and i.idiomaid = l.idiomaid and li3.literalid = i.literalid and li3.idiomaid = ? and " +
                " li2.idiomaid = ? and lt.descripcionid = li2.literalid and " +
                " li.idiomaid = ? and l.libroid = ? ";
        LibroDetalleActForm libro = new LibroDetalleActForm();
        ResultSet rs = null;
        PreparedStatement ps = null;
        int iDeterioro;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idIdioma);
            ps.setInt(2, idIdioma);
            ps.setInt(3, idIdioma);
            ps.setInt(4, idLibro);
            rs = ps.executeQuery();
            if (rs.next()) {
                libro.setIdLibro(rs.getInt("LIBROID"));
                libro.setNombreLibro(rs.getString("LITERAL_NOMBRE"));
                libro.setDescLibro(rs.getString("LITERAL_DESC"));
                libro.setAbadiaid(rs.getInt("ABADIAID"));
                libro.setIdLibroTipo(rs.getInt("TIPO_LIBROID"));
                libro.setNivel(rs.getShort("NIVEL"));
                libro.setIdIdioma(rs.getInt("IDIOMAID"));
                libro.setNumPaginas(rs.getInt("PAGINAS"));
                libro.setAbadiaDesc_copia(rs.getString("NOMBRE_ABADIA") + " (" + rs.getString("NOMBRE_REGION") + ")");
                libro.setIdioma_desc(rs.getString("LITERAL_IDIOMA"));
                libro.setDesgaste(rs.getDouble("DESGASTE"));
                libro.setGrafico(rs.getString("GRAFICO"));
                libro.setPrecioMax(Utilidades.redondear(rs.getDouble("PRECIO_MAX")));
                libro.setPrecioMin(Utilidades.redondear(rs.getDouble("PRECIO_MIN")));
                libro.setPrecioCopia(rs.getDouble("PRECIO_COPIA"));
                libro.setPrecioCopiaS(Utilidades.redondear(rs.getDouble("PRECIO_COPIA")));

                //calculamos el grado de deterioro del libro
                iDeterioro = (int) (libro.getDesgaste()) / 10;
                libro.setDeterioro(HTML.smallBarraRed(iDeterioro, resource.getMessage("edificio.abadia.biblioteca.altdeterioro", Utilidades.redondear(libro.getDesgaste()), String.valueOf(100))));

            }
            return libro;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adLibros. recuperarDetalleLibro. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Recupera el detalle de un libro
     *
     * @param idLibro
     * @param idIdioma
     * @return
     * @throws AbadiaException
     */
    public Libro recuperarDetalleLibro(int idLibro, int idIdioma) throws AbadiaException {
        String sSQL = "Select l.libroid, li.literal as literal_nombre, l.abadiaid, l.tipo_libroid, l.estado, l.idiomaid, li3.literal as literal_idioma, l.desgaste, " +
                " ln.paginas, li2.literal as literal_desc, a.nombre as nombre_abadia, r.descripcion as nombre_region, l.desgaste, lt.grafico, l.nivel, ln.precio_max, ln.precio_min, l.precio_copia " +
                " from libro l, libro_tipo lt, literales li, libro_nivel ln, literales li2, abadia a, region r, literales li3, idioma i  " +
                " where l.tipo_libroid = lt.tipo_libroid and lt.literalid = li.literalid and " +
                " l.nivel = ln.nivel and l.tipo_libroid = ln.tipo_libroid and a.abadiaid = l.abadiaid and " +
                " a.regionid = r.regionid and i.idiomaid = l.idiomaid and li3.literalid = i.literalid and li3.idiomaid = ? and " +
                " li2.idiomaid = ? and lt.descripcionid = li2.literalid and " +
                " li.idiomaid = ? and l.libroid = ? ";
        Libro libro = new Libro();
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idIdioma);
            ps.setInt(2, idIdioma);
            ps.setInt(3, idIdioma);
            ps.setInt(4, idLibro);
            rs = ps.executeQuery();
            if (rs.next()) {
                libro.setIdLibro(rs.getInt("LIBROID"));
                libro.setNombreLibro(rs.getString("LITERAL_NOMBRE"));
                libro.setDescLibro(rs.getString("LITERAL_DESC"));
                libro.setIdAbadia(rs.getInt("ABADIAID"));
                libro.setIdLibroTipo(rs.getInt("TIPO_LIBROID"));
                libro.setNivel(rs.getShort("NIVEL"));
                libro.setIdIdioma(rs.getInt("IDIOMAID"));
                libro.setNumPaginas(rs.getInt("PAGINAS"));
                libro.setNombreAbadia_copia(rs.getString("NOMBRE_ABADIA"));
                libro.setNombreRegion(rs.getString("NOMBRE_REGION"));
                libro.setIdioma_desc(rs.getString("LITERAL_IDIOMA"));
                libro.setDesgaste(rs.getDouble("DESGASTE"));
                libro.setGrafico(rs.getString("GRAFICO"));
                libro.setPrecioMax(rs.getDouble("PRECIO_MAX"));
                libro.setPrecioMin(rs.getDouble("PRECIO_MIN"));
                libro.setPrecioCopia(rs.getDouble("PRECIO_COPIA"));
            }
            return libro;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adLibros. recuperarDetalleLibro. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public ArrayList<LibroCopia> recuperarCopiasEnCurso(int idLibro, int idIdioma, MessageResources resource) throws AbadiaException {
        ArrayList<LibroCopia> alCopias = new ArrayList<LibroCopia>();

        LibroCopia libro = null;

        //prima
        libro = recuperarCopiaPorPeriodo(Constantes.PERIODO_PRIMA, idLibro, idIdioma, resource);
        libro.setPeriodo_Desc(Constantes.PERIODO_PRIMA_DESC);
        alCopias.add(libro);

        //tercia
        libro = recuperarCopiaPorPeriodo(Constantes.PERIODO_TERCIA, idLibro, idIdioma, resource);
        libro.setPeriodo_Desc(Constantes.PERIODO_TERCIA_DESC);
        alCopias.add(libro);

        //nona
        libro = recuperarCopiaPorPeriodo(Constantes.PERIODO_NONA, idLibro, idIdioma, resource);
        libro.setPeriodo_Desc(Constantes.PERIODO_NONA_DESC);
        alCopias.add(libro);

        //visperas
        libro = recuperarCopiaPorPeriodo(Constantes.PERIODO_VISPERAS, idLibro, idIdioma, resource);
        libro.setPeriodo_Desc(Constantes.PERIODO_VISPERAS_DESC);
        alCopias.add(libro);

        return alCopias;
    }

    public LibroCopia recuperarCopiaPorPeriodo(int idPeriodo, int idLibro, int idIdioma, MessageResources resource) throws AbadiaException {
        //recuperamos las copias por cada franja horaria
        String sSQL = "SELECT m.nombre as nombre_monje, m.apellido1 as nombre_monje2, m.monjeid, a.nombre as nombre_abadia, l.paginas_copiadas, ln.paginas, li.literal, i.descripcion as idioma_desc, l.libroid, l.abadiaid_copia, l.abadiaid, l.desgaste  " +
                " FROM libro_tarea lt, monje m, libro_estado le, libro l, literales li, abadia a, libro_nivel ln, idioma i " +
                " WHERE lt.monjeid = m.monjeid and l.estado = le.estado and l.libroid = lt.libro_copiaid and " +
                " le.literalid = li.literalid and li.idiomaid = " + idIdioma + " and lt.libro_origenid = " + idLibro + " and m.abadiaid = a.abadiaid and l.tipo_libroid = ln.tipo_libroid and l.nivel = ln.nivel and lt.periodoid = ? and l.idiomaid = i.idiomaid";

        LibroCopia libro;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int iProgreso;
        int iDeterioro;

        try {
            ps = con.prepareStatement(sSQL);
            //prima
            libro = new LibroCopia();
            libro.setIdPeriodo(idPeriodo);
            ps.setInt(1, idPeriodo);
            rs = ps.executeQuery();
            if (rs.next()) {
                libro.setNombreMonje(rs.getString("NOMBRE_MONJE"));
                libro.setApellidoMonje(rs.getString("NOMBRE_MONJE2"));
                libro.setAbadia_Desc(rs.getString("NOMBRE_ABADIA"));
                libro.setDesgaste(rs.getDouble("DESGASTE"));
                libro.setNumPaginasCopiadas(rs.getInt("PAGINAS_COPIADAS"));
                libro.setNumPaginas(rs.getInt("PAGINAS"));
                libro.setDescEstado(rs.getString("LITERAL"));
                libro.setIdioma_desc(rs.getString("IDIOMA_DESC"));
                libro.setIdMonje(rs.getInt("MONJEID"));
                libro.setClaveLibroPeriodo(rs.getString("LIBROID") + ";" + idPeriodo);
                libro.setIdAbadia(rs.getInt("ABADIAID"));
                libro.setIdAbadiaCopia(rs.getInt("ABADIAID_COPIA"));
                if (libro.getIdAbadia() == libro.getIdAbadiaCopia()) libro.setEliminable(1);

                //calculamos el grado de avance de la copia
                iProgreso = (int) (libro.getNumPaginasCopiadas() * 100 / libro.getNumPaginas()) / 10;
                libro.setProgreso(HTML.smallBarra(iProgreso, resource.getMessage("edificio.abadia.biblioteca.altprogreso", Utilidades.redondear(libro.getNumPaginasCopiadas()), String.valueOf(libro.getNumPaginas()))));

                //calculamos el grado de deterioro del libro
                iDeterioro = (int) (libro.getDesgaste()) / 10;
                libro.setDeterioro(HTML.smallBarraRed(iDeterioro, resource.getMessage("edificio.abadia.biblioteca.altdeterioro", Utilidades.redondear(libro.getDesgaste()), String.valueOf(100))));

            }
            return libro;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adLibros. recuperarCopiaPorPeriodo. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public int recuperaridCopiaLibroPorMonje(int idLibro, int idMonje) throws AbadiaException {
        int idLibroCopia;
        String sSQL = "Select libro_copiaid from libro_tarea where monjeid = " + idMonje + " and libro_origenid = " + idLibro;
        adUtils utils = new adUtils(con);
        idLibroCopia = utils.getSQL(sSQL, 0);
        return idLibroCopia;
    }

    public short recuperarEstadoLibro(int idLibro) throws AbadiaException {
        short estadoLibro;
        String sSQL = "Select estado from libro where libroid = " + idLibro;
        adUtils utils = new adUtils(con);
        estadoLibro = (short) utils.getSQL(sSQL, Constantes.ESTADO_LIBRO_COMPLETO);
        return estadoLibro;
    }

    /**
     * Incrementa el deterioro de un libro en función de su tipo y nivel * el número de páginas copiadas.
     *
     * @param idLibro
     * @param deterioro
     * @return
     * @throws AbadiaException
     */
    public void incrementarDeterioroLibroPorCopia(int idLibro, double deterioro) throws AbadiaException {
        String sSQL = "update libro set desgaste = desgaste + " + deterioro + " where libroid = " + idLibro;
        adUtils utils = new adUtils(con);
        utils.execSQL(sSQL);
    }

    public void reducirDeterioroLibro(ArrayList<Integer> idLibro, double deterioro) throws AbadiaException {

        String sIDLibros = "";

        sIDLibros = String.valueOf(idLibro.get(0));
        for (Integer i : idLibro) {
            sIDLibros = sIDLibros.concat(",").concat(String.valueOf(i));
        }
/*
        for (int iCount = 1; iCount < idLibro.size(); iCount++) {
            sIDLibros = sIDLibros.concat(",").concat(String.valueOf(idLibro[iCount]));
        }
*/
        //fijo el número de decimales del double a 3 para evitar problemas de truncado.
        String sSQL = "update libro set desgaste = ROUND(desgaste - " + Utilidades.redondear(deterioro, 3) + ", 5) where libroid in (" + sIDLibros + ")";
        adUtils utils = new adUtils(con);
        utils.execSQL(sSQL);
    }

    public double recuperarDeterioroCorrespondientePorLibro(int idLibro) throws AbadiaException {
        double deterioro;
        String sSQL = "Select ln.desgaste from libro_nivel ln, libro l where l.libroid = " + idLibro + " and l.nivel = ln.nivel and l.tipo_libroid = ln.tipo_libroid";
        adUtils utils = new adUtils(con);
        deterioro = utils.getSQL(sSQL, (double) 0);
        return deterioro;
    }


    public void generarLibroPorRegion(int idTipoLibro) throws AbadiaException {
        adUtils utils;
        utils = new adUtils(con);
        ArrayList<Integer> alRegiones = utils.getSQLIntArray("select regionid from abadia where regionid not in (Select a.REGIONID From abadia a, libro l where a.ABADIAID = l.ABADIAID and l.estado = " + Constantes.ESTADO_LIBRO_COMPLETO + " and l.TIPO_LIBROID = " + idTipoLibro + ") group by regionid order by regionid ");

        String sSQL = "select a.abadiaid,r.idiomaid, a.regionid " +
                " from abadia a, edificio e, region r, abadia_puntuacion ap, usuario u " +
                " where a.abadiaid = e.abadiaid and e.tipoedificioid = 6 and r.regionid = a.regionid  and ap.abadiaid = a.abadiaid " +
                " and a.regionid = ? and ap.total > 7000000 and u.usuarioid = a.usuarioid and u.abadia_congelada = 0 " +
                " order by ap.fecha_abadia desc, rand() limit 1";

        adLibros libroAD = new adLibros(con);
        Libro libro = libroAD.recuperaLibroNivel(idTipoLibro);

        if (libro != null) {

            PreparedStatement ps = null;
            ResultSet rs = null;

            try {
                ps = con.prepareStatement(sSQL);

                for (int regione : alRegiones) {

                    ps.setInt(1, regione);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        libro.setIdIdioma(rs.getInt("idiomaid"));
                        libro.setIdAbadia(rs.getInt("abadiaid"));
                        libro.setFecha_creacion(CoreTiempo.getTiempoAbadiaString());
                        libro.setFecha_adquisicion(CoreTiempo.getTiempoAbadiaString());
                        libro.setIdLibroTipo(idTipoLibro);

                        crearRegistroLibro(libro);
                    }

                }

            } catch (SQLException e) {
                throw new AbadiaSQLException("adLibros. generarLibroPorRegion. SQLException", e, log);
            } catch (Exception e) {
                throw new AbadiaSQLException("adLibros. generarLibroPorRegion. Exception", e, log);
            } finally {
                DBMSUtils.cerrarObjetoSQL(rs);
                DBMSUtils.cerrarObjetoSQL(ps);
            }
        }
    }

    public HashMap<String, ArrayList> cargarFiltrosBiblioteca(ArrayList alLibros, MessageResources resource) throws AbadiaException {
        HashMap<String, ArrayList> hmFiltros = new HashMap<String, ArrayList>();

        HashMap<Integer, String> hmAbadias = new HashMap<Integer, String>();
        HashMap<Integer, String> hmLibros = new HashMap<Integer, String>();
        HashMap<Integer, String> hmIdiomas = new HashMap<Integer, String>();
        HashMap<Integer, String> hmRegiones = new HashMap<Integer, String>();

        Iterator itLibros = alLibros.iterator();
        Libro libro;
        while (itLibros.hasNext()) {
            libro = (Libro) itLibros.next();
            if (hmAbadias.get(new Integer(libro.getIdAbadia())) == null) {
                //hmAbadias.put(new Integer(libro.getIdAbadia()), new Table(libro.getIdAbadia(),libro.getNombreAbadia()));
                hmAbadias.put(libro.getIdAbadia(), libro.getNombreAbadia());
            }
            if (hmLibros.get(new Integer(libro.getIdLibroTipo())) == null) {
                hmLibros.put(libro.getIdLibroTipo(), libro.getNombreLibro());
            }
            if (hmIdiomas.get(new Integer(libro.getIdIdioma())) == null) {
                hmIdiomas.put(libro.getIdIdioma(), libro.getIdioma_desc());
            }
            if (hmRegiones.get(new Integer(libro.getIdRegion())) == null) {
                hmRegiones.put(libro.getIdRegion(), libro.getNombreRegion());
            }
        }

        hmFiltros.put(Constantes.TABLA_ABADIAS, Utilidades.ordenarContenidoHash2List(hmAbadias, new Table(0, resource.getMessage("principal.todas"))));
        hmFiltros.put(Constantes.TABLA_LIBROS, Utilidades.ordenarContenidoHash2List(hmLibros, new Table(0, resource.getMessage("principal.todos"))));
        hmFiltros.put(Constantes.TABLA_IDIOMAS, Utilidades.ordenarContenidoHash2List(hmIdiomas, new Table(0, resource.getMessage("principal.todos"))));
        hmFiltros.put(Constantes.TABLA_REGIONES, Utilidades.ordenarContenidoHash2List(hmRegiones, new Table(0, resource.getMessage("principal.todas"))));

        return hmFiltros;
    }

    public ArrayList<Libro> aplicarFiltro(ArrayList<Libro> alLibros, FiltroLibrosActForm filtro) throws AbadiaException {
        //Libro libro;
        ArrayList<Libro> alLibrosFiltrados = new ArrayList<Libro>();

        for (Libro libro : alLibros) {
            if (!filtro.isDisponible() || libro.getFranjasOcupadas() < 4) {
                alLibrosFiltrados.add(libro);
            }
        }
        return alLibrosFiltrados;
    }


    /**
     * Recupera un ArrayList de libros completos (estado = 2) con un desgaste superior al permitido y que
     * por lo tanto son susceptibles de cambiar de estado a Deteriorado (estado = 7)
     *
     * @return
     * @throws AbadiaException
     */
    public ArrayList<LibroProceso> recuperarLibrosCompletosDeteriorados() throws AbadiaException {
        String sSQL = "select l.abadiaid, u.idiomaid, l.tipo_libroid, l.nivel, li.literal, l.desgaste, l.estado " +
                " from libro l, abadia a, usuario u, libro_tipo lt, literales li " +
                " where l.desgaste > 50 and l.estado = " + Constantes.ESTADO_LIBRO_COMPLETO + " and l.tipo_libroid = lt.tipo_libroid and a.abadiaid = l.abadiaid and u.usuarioid = a.usuarioid and u.idiomaid = li.idiomaid and li.literalid = lt.literalid " +
                " order by u.idiomaid";
        ArrayList<LibroProceso> alLibros = new ArrayList<LibroProceso>();
        LibroProceso libro;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                libro = new LibroProceso();
                libro.setIdLibroTipo(rs.getInt("TIPO_LIBROID"));
                libro.setIdAbadia(rs.getInt("ABADIAID"));
                libro.setIdIdioma(rs.getInt("IDIOMAID"));
                libro.setEstado(rs.getShort("ESTADO"));
                libro.setNombre(rs.getString("LITERAL"));
                libro.setNivel(rs.getShort("NIVEL"));
                libro.setDeterioro(rs.getDouble("DESGASTE"));
                alLibros.add(libro);
            }
            return alLibros;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adLibros. recuperarLibrosCompletosDeteriorados. SQLException", e, log);
        } catch (Exception e) {
            throw new AbadiaSQLException("adLibros. recuperarLibrosCompletosDeteriorados. Exception", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /**
     * Recupera un @{link AraryList} con la lista de monjes que están copiando el libro especificado
     *
     * @param idLibro
     * @return
     * @throws AbadiaException
     */
    public ArrayList<MonjeProceso> recuperarMonjesCopistasPorIDLibro(int idLibro) throws AbadiaException {
        String sSQL = "Select m.MONJEID, m.ABADIAID, lt.LIBRO_COPIAID From libro_tarea AS lt Inner Join monje AS m ON lt.MONJEID = m.MONJEID Where lt.LIBRO_ORIGENID = " + idLibro + " group by m.MONJEID, m.ABADIAID, lt.LIBRO_COPIAID";
        ArrayList<MonjeProceso> alMonjes = new ArrayList<MonjeProceso>();
        MonjeProceso monje = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                monje = new MonjeProceso();
                monje.setIdAbadia(rs.getInt("ABADIAID"));
                monje.setIdMonje(rs.getInt("MONJEID"));
                monje.setIdLibroCopia(rs.getInt("LIBRO_COPIAID"));
                alMonjes.add(monje);
            }
            return alMonjes;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adLibros. recuperarMonjesCopìstasPorIDLibro. SQLException", e, log);
        } catch (Exception e) {
            throw new AbadiaSQLException("adLibros. recuperarMonjesCopìstasPorIDLibro. Exception", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /**
     * Recupera los libros que tienen un deterioro superior a deterioro sin tener en cuenta el estado
     *
     * @param deterioro
     * @return
     * @throws AbadiaException
     */
    public ArrayList<LibroProceso> recuperarLibrosPorDeterioroMayorOIgualQue(int deterioro) throws AbadiaException {
        String sSQL = "select l.abadiaid, u.idiomaid, l.tipo_libroid, l.nivel, li.literal, l.desgaste, l.estado, l.libroid " +
                " from libro l, abadia a, usuario u, libro_tipo lt, literales li " +
                " where l.desgaste >= " + deterioro + " and l.tipo_libroid = lt.tipo_libroid and a.abadiaid = l.abadiaid and u.usuarioid = a.usuarioid and u.idiomaid = li.idiomaid and li.literalid = lt.literalid " +
                " order by u.idiomaid";
        ArrayList<LibroProceso> alLibros = new ArrayList<LibroProceso>();
        LibroProceso libro;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                libro = new LibroProceso();
                libro.setIdLibro(rs.getInt("LIBROID"));
                libro.setIdLibroTipo(rs.getInt("TIPO_LIBROID"));
                libro.setIdAbadia(rs.getInt("ABADIAID"));
                libro.setIdIdioma(rs.getInt("IDIOMAID"));
                libro.setEstado(rs.getShort("ESTADO"));
                libro.setNombre(rs.getString("LITERAL"));
                libro.setNivel(rs.getShort("NIVEL"));
                libro.setDeterioro(rs.getDouble("DESGASTE"));
                alLibros.add(libro);
            }
            return alLibros;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adLibros. recuperarLibrosPorDeterioroMayorOIgualQue. SQLException", e, log);
        } catch (Exception e) {
            throw new AbadiaSQLException("adLibros. recuperarLibrosPorDeterioroMayorOIgualQue. Exception", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /**
     * Recupera un ArrayList de libros completos (estado = 2) a los que les fantan iDias para ser considerados
     * como deteriorados
     *
     * @return
     * @throws AbadiaException
     */
    public ArrayList<LibroProceso> recuperarLibrosPorDeterioro(int iDias, int iDias2, int iDias3) throws AbadiaException {
        String sSQL = "select l.abadiaid, u.idiomaid, round(l.desgaste) desgaste_total " +
                " from libro l, libro_nivel ln, abadia a, usuario u " +
                " where l.nivel = ln.nivel and l.TIPO_LIBROID = ln.TIPO_LIBROID and l.ESTADO = " + Constantes.ESTADO_LIBRO_COMPLETO + " and a.abadiaid = l.abadiaid and a.usuarioid = u.usuarioid " +
                " group by l.abadiaid, u.idiomaid " +
                " having desgaste_total = ? or desgaste_total = ? or desgaste_total = ? " +
                " order by u.idiomaid ";

        ArrayList<LibroProceso> alLibros = new ArrayList<LibroProceso>();
        LibroProceso libro;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, iDias);
            ps.setInt(2, iDias2);
            ps.setInt(3, iDias3);
            rs = ps.executeQuery();
            while (rs.next()) {
                libro = new LibroProceso();
                libro.setIdAbadia(rs.getInt("ABADIAID"));
                libro.setIdIdioma(rs.getInt("IDIOMAID"));
                alLibros.add(libro);
            }
            return alLibros;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adLibros. recuperarLibrosPorDeterioro. SQLException", e, log);
        } catch (Exception e) {
            throw new AbadiaSQLException("adLibros. recuperarLibrosPorDeterioro. Exception", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

}