package org.abbatia.adbean;

import org.abbatia.actionform.AbadiaActForm;
import org.abbatia.actionform.AbadiaPuntuacionForm;
import org.abbatia.actionform.BuscarAbadiasActForm;
import org.abbatia.adbean.base.adbeans;
import org.abbatia.bean.*;
import org.abbatia.core.CoreTiempo;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaNotFoundException;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.exception.base.SystemException;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.HTML;
import org.abbatia.utils.Utilidades;
import org.apache.commons.validator.GenericValidator;
import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;

public class adAbadia extends adbeans {
    private static Logger log = Logger.getLogger(adAbadia.class.getName());
/*
  public static int MONJES_TODOS = 0;
  public static int MONJES_VIVOS = 1;
  public static int MONJES_MUERTOS = 2;  // Velatorio
  public static int MONJES_CEMENTERIO = 3;
  public static int MONJES_ENFERMOS = 4;  // Enfermos
  public static int MONJES_VIAJANDO = 5;
  public static int MONJES_VISITA = 6;
  public static int MONJES_VISITA_MIABADIA = 7;  // Monjes que están de visita en mi abadia
  //public static int MONJES_VISITA_MIABADIA_ENF = 8;  // Monjes que están de visita en mi abadia y estan enfermos
  public static int MONJES_VISITA_ENFERMOS = 8;  // Monjes de visita que estan enfermos
  public static int MONJES_OSARIO = 9;
*/

    /**
     * Instancia la clase sin obtener una nueva conexión
     *
     * @param con
     * @throws AbadiaException
     */
    public adAbadia(Connection con) throws AbadiaException {
        super(con);
    }

    //recupera el objeto Abadia cargado...

    public Abadia recuperarAbadia(long idDeAbadiaTmp) throws AbadiaException {
        //Definición de cadena sql de consulta
        String sSQL = "Select * from abadia where ABADIAID = ? ";
        //creo un objeto de tipo Abadia
        Abadia abadia = new Abadia();
        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave.

            int parNo = 1;
            ps.setLong(parNo, idDeAbadiaTmp);
            //Lanzo la consulta y cargo el resultado en un resultset
            rs = ps.executeQuery();
            //si la consulta encuentra la abadia....
            if (rs.next()) {
                //iniciamos el volcado de datos sobre
                //el objeto abadia.
                abadia.setIdDeAbadia(rs.getInt("ABADIAID"));
                abadia.setNombre(rs.getString("NOMBRE"));
                abadia.setIdDeRegion(rs.getInt("REGIONID"));
                abadia.setFechaDeConstruccion(rs.getDate("FECHA_CONSTRUCCION").toString());
                abadia.setArquitecto(rs.getString("ARQUITECTO"));
                abadia.setCapacidad(rs.getShort("CAPACIDAD"));
                abadia.setNivelJerarquico(rs.getShort("NIVEL_JERARQUICO"));

                abadia.setPuntuacion(recuperarPuntuacion(abadia.getIdDeAbadia()));
                //devolvemos el objeto Abadia informado.
                return abadia;
            }
            //si abadia no se localiza, devolveremos null
            return null;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adAbadia. recuperarAbadia. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    //recupera el objeto Abadia cargado...

    public Abadia recuperarAbadia(String nombreAbadia) throws AbadiaException {
        //Definición de cadena sql de consulta

        String sSQL = "Select a.*, e.NIVEL AS NIVEL_ABADIA from abadia a, edificio e where a.NOMBRE = ? and a.ABADIAID = e.ABADIAID AND " +
                " e.TIPOEDIFICIOID = 99 ";
        //creo un objeto de tipo Abadia
        Abadia abadia = new Abadia();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //creo un objeto de tipo PreparedStatement sobre el que se
            //ejecutara la consulta
            ps = con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave.

            ps.setString(1, nombreAbadia);
            //Lanzo la consulta y cargo el resultado en un resultset
            rs = ps.executeQuery();
            //si la consulta encuentra la abadia....
            if (rs.next()) {
                //iniciamos el volcado de datos sobre
                //el objeto abadia.
                abadia.setIdDeAbadia(rs.getInt("ABADIAID"));
                abadia.setNombre(rs.getString("NOMBRE"));
                abadia.setFechaDeConstruccion(rs.getDate("FECHA_CONSTRUCCION").toString());
                abadia.setArquitecto(rs.getString("ARQUITECTO"));
                abadia.setIdDeOrden(rs.getShort("ORDENID"));
                abadia.setCapacidad(rs.getShort("CAPACIDAD"));
                abadia.setNivelJerarquico(rs.getShort("NIVEL_ABADIA"));
                abadia.setHistoria(rs.getString("HISTORIA"));
                abadia.setIdDeUsuario(rs.getInt("USUARIOID"));
                abadia.setIdDeRegion(rs.getInt("REGIONID"));

                abadia.setPuntuacion(recuperarPuntuacion(abadia.getIdDeAbadia()));

                //devolvemos el objeto Abadia informado.
                return abadia;
            } else {
                throw new AbadiaNotFoundException("adAbadia. recuperarAbadia. No se ha encontrado la abadia: " + nombreAbadia, log);
            }
        } catch (SQLException e) {
            throw new AbadiaSQLException("adAbadia. recuperarAbadia. SQLException ", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    //recibe un formulario con todos los datos de la nueva abadia
    //y se encarga de distribuir los datos entre las diferentes
    //tablas implicadas.

    public Abadia crearAbadia(AbadiaActForm abadia, Usuario usuario) throws AbadiaException {
        adRecurso recursoAD = null;
        adAnimal animalAD = null;
        adMonje monjeAD = null;
        adRegion oRegionAD;
        adEdificio oEdificioAD;
        adAbadia oAbadiaAD;

        String sSQL = "Insert Into abadia ( NOMBRE, FECHA_CONSTRUCCION, ARQUITECTO, CAPACIDAD, NIVEL_JERARQUICO, HISTORIA, ORDENID, REGIONID, USUARIOID) " +
                "Values ('" + Utilidades.normalizarTexto(abadia.getNombreAbadia()) + "', " +
                "'" + CoreTiempo.getTiempoAbadiaString() + "', " +
                "'" + abadia.getArquitecto() + "', " +
                " 10, 10, '" + abadia.getHistoria() + "', " + abadia.getOrden() + ", " + abadia.getRegion() + ", " + abadia.getUsuarioid() + ")";

        Statement ps = null;
        ResultSet rs = null;

        try {
            ps = con.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_UPDATABLE);
            ps.executeUpdate(sSQL, Statement.RETURN_GENERATED_KEYS);
            rs = ps.getGeneratedKeys();

            if (rs.next()) {
                abadia.setAbadia(rs.getInt(1));
            }
            rs.close();
            ps.close();

            Monje monje;
            ArrayList alMonjes = abadia.getMonjes();
            Iterator itMonjes = alMonjes.iterator();

            monjeAD = new adMonje(con);
            while (itMonjes.hasNext()) {
                monje = (Monje) itMonjes.next();
                monje.setIdAbadia(abadia.getAbadia());
                monje.setIdMonje(monjeAD.crearMonjeID(monje));
            }


            Abadia abadias = new Abadia();
            abadias.setPertenecia(alMonjes);
            abadias.setIdDeAbadia(abadia.getAbadia());
            abadias.setIdDeRegion(abadia.getRegion());
            abadias.setIdDeOrden(abadia.getOrden());
            abadias.setNombre(abadia.getNombreAbadia());
            abadias.setActividadPrincipal(abadia.getActividad());
            abadias.setFechaDeConstruccion(CoreTiempo.getTiempoAbadiaString());
            oRegionAD = new adRegion(con);
            abadias.setIdIdioma(oRegionAD.getIdiomaRegion(abadia.getRegion()));

            // Edificios
            oEdificioAD = new adEdificio(con);
            oEdificioAD.crearEdificios(adEdificio.crearEdificiosAbadia(abadia));

            // Animales
            animalAD = new adAnimal(con);
            animalAD.crearAnimalesAbadia(abadias, usuario);

            // Recursos
            recursoAD = new adRecurso(con);
            recursoAD.crearRecursosIniciales(abadia.getAbadia());

            // Crear registro inicial de puntuacion
            oAbadiaAD = new adAbadia(con);
            oAbadiaAD.crearRegistroPuntuacion(new AbadiaPuntuacion(abadias.getIdDeAbadia()));

            return abadias;

        } catch (SQLException e) {
            throw new AbadiaSQLException("Error creando el registro de Abadia", e, log);
        } catch (AbadiaException e) {
            throw new AbadiaSQLException("Error creando el registro de Abadia", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Devuelve el nombre de una abadia a partir de su codigo
     *
     * @param idAbadia
     * @return
     * @throws AbadiaException
     */
    public String getNomAbadia(int idAbadia) throws AbadiaException {
        String sSQL = "SELECT NOMBRE FROM abadia WHERE ABADIAID = " + idAbadia;
        adUtils utils;
        try {
            utils = new adUtils(con);
            return utils.getSQL(sSQL, "X");
        } catch (AbadiaException e) {
            throw e;
        }
    }

    /**
     * Devuelve el id de región de una abadia
     *
     * @param idAbadia
     * @return
     * @throws AbadiaException
     */
    public int getRegionAbadia(int idAbadia) throws AbadiaException {
        String sSQL = "SELECT REGIONID FROM abadia WHERE ABADIAID = " + idAbadia;
        adUtils utils;
        try {
            utils = new adUtils(con);
            return utils.getSQL(sSQL, 0);
        } catch (AbadiaException e) {
            throw e;
        }
    }

    /**
     * Devuelve el id de idioma de la región en la que se encuentra la abadia
     *
     * @param idAbadia
     * @return
     * @throws AbadiaException
     */
    public int recuperarIdiomaRegionAbadia(int idAbadia) throws AbadiaException {
        String sSQL = "SELECT r.idiomaid FROM abadia a, region r WHERE a.regionid = r.regionid and abadiaid = " + idAbadia;
        adUtils utils = null;
        try {
            utils = new adUtils(con);
            return utils.getSQL(sSQL, 0);
        } catch (AbadiaException e) {
            throw e;
        }
    }

    /**
     * Se verifica si existe una abadia con ese nombre.
     *
     * @param nombre
     * @return
     * @throws AbadiaException
     */
    public boolean existeAbadia(String nombre) throws AbadiaException {
        adUtils utilsAD = null;
        int count = 0;
        try {
            utilsAD = new adUtils(con);
            count = utilsAD.getSQL("SELECT count(*) FROM abadia WHERE NOMBRE = '" + nombre + "'", 0);
            return count == 1;
        } catch (AbadiaException e) {
            throw e;
        }
    }

    /**
     * Invoca al procedure que gestiona la eliminación de todos los datos de la abadia.
     *
     * @param idDeAbadiaTmp
     * @throws Exception
     */
    public void eliminarTodoAbadia(int idDeAbadiaTmp) throws AbadiaException {
        CallableStatement cs = null;

        try {
            cs = con.prepareCall("call eliminarTodoAbadia(?)");
            cs.setInt(1, idDeAbadiaTmp);
            cs.execute();

        } catch (SQLException e) {
            throw new AbadiaSQLException("ERROR: eliminarTodoAbadia = " + e, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(cs);
        }

    }

    public ArrayList<Monje> getMonjes(int idAbadia, int tipo, MessageResources resource) throws AbadiaException {
        Monje monje;
        ArrayList<Monje> alMonjes;
        ArrayList<HabilidadMonje> alHabilidades;
        Iterator<Monje> monjes;
        Iterator<HabilidadMonje> habilidades;
        adUtils utils;
        adHabilidades habilidadesAD;
        adMonje monjeAD;

        try {

            habilidadesAD = new adHabilidades(con);
            utils = new adUtils(con);

            monjeAD = new adMonje(con);
            alMonjes = monjeAD.recuperarMonjes(idAbadia, tipo, resource);

            monjes = alMonjes.iterator();

            while (monjes.hasNext()) {
                monje = monjes.next();

                if (tipo == Constantes.MONJES_VIAJANDO) {
                    if (utils.getSQL("SELECT 1 FROM monje_visita where monjeid = " + monje.getIdMonje() + " and fecha_llegada_origen is not null", "0").equals("1")) {
                        monje.setFechallegada(Utilidades.formatStringFromDB(utils.getSQL("SELECT FECHA_LLEGADA_ORIGEN FROM monje_visita where monjeid =" + monje.getIdMonje(), "")));
                        monje.setAbadia(utils.getSQL("SELECT nombre FROM abadia WHERE abadiaid = " + monje.getIdAbadia(), ""));
                        monje.setRegion("");
                    } else {
                        monje.setAbadia(utils.getSQL("SELECT nombre FROM monje_visita, abadia " +
                                "where monjeid = " + monje.getIdMonje() + " and monje_visita.abadiaid_destino = abadia.abadiaid", ""));
                        monje.setRegion(utils.getSQL("SELECT region.descripcion " +
                                "FROM monje_visita, abadia, region " +
                                "where monjeid = " + monje.getIdMonje() + " and monje_visita.abadiaid_destino = abadia.abadiaid and abadia.regionid = region.regionid and region.idiomaid = 1", ""));
                        monje.setFechallegada(Utilidades.formatStringFromDB(utils.getSQL("SELECT FECHA_LLEGADA_DESTINO FROM monje_visita where monjeid =" + monje.getIdMonje(), "")));
                    }
                }
                if (tipo == Constantes.MONJES_VISITA || tipo == Constantes.MONJES_VISITA_ENFERMOS) {
                    monje.setAbadia(utils.getSQL("SELECT nombre FROM monje_visita, abadia " +
                            "where monjeid = " + monje.getIdMonje() + " and monje_visita.abadiaid_destino = abadia.abadiaid", ""));
                    monje.setRegion(utils.getSQL("SELECT region.descripcion " +
                            "FROM monje_visita, abadia, region " +
                            "where monjeid = " + monje.getIdMonje() + " and monje_visita.abadiaid_destino = abadia.abadiaid and abadia.regionid = region.regionid and region.idiomaid = 1", ""));
                }
                if (tipo == Constantes.MONJES_VISITA_MIABADIA) //|| (tipo == MONJES_VISITA_MIABADIA_ENF))
                {
                    monje.setAbadia(utils.getSQL("SELECT abadia.nombre FROM monje, abadia " +
                            "where monjeid = " + monje.getIdMonje() + " and monje.abadiaid = abadia.abadiaid", ""));
                    monje.setRegion(utils.getSQL("SELECT region.descripcion " +
                            "FROM monje, abadia, region " +
                            "where monjeid = " + monje.getIdMonje() + " and monje.abadiaid = abadia.abadiaid and abadia.regionid = region.regionid and region.idiomaid = 1", ""));
                }
                // Buscar la alimentaci�n que tiene
                if ((tipo != Constantes.MONJES_MUERTOS) && (tipo != Constantes.MONJES_CEMENTERIO)) {
                    // Habilidades
                    alHabilidades = habilidadesAD.recuperarHabilidadesMonje(monje.getIdMonje());

                    habilidades = alHabilidades.iterator();
                    HabilidadMonje habilidad;
                    while (habilidades.hasNext()) {
                        habilidad = habilidades.next();
                        if (habilidad.getIdHabilidad() == Constantes.HABILIDAD_FE) {
                            monje.setFe((int) habilidad.getValorActual());
                        } else if (habilidad.getIdHabilidad() == Constantes.HABILIDAD_TALENTO) {
                            monje.setTalento((int) habilidad.getValorActual());
                        } else if (habilidad.getIdHabilidad() == Constantes.HABILIDAD_IDIOMA) {
                            monje.setIdioma((int) habilidad.getValorActual());
                        } else if (habilidad.getIdHabilidad() == Constantes.HABILIDAD_SABIDURIA) {
                            monje.setSabiduria((int) habilidad.getValorActual());
                        } else if (habilidad.getIdHabilidad() == Constantes.HABILIDAD_FUERZA) {
                            monje.setFuerza((int) habilidad.getValorActual());
                        } else if (habilidad.getIdHabilidad() == Constantes.HABILIDAD_CARISMA) {
                            monje.setCarisma((int) habilidad.getValorActual());
                        } else if (habilidad.getIdHabilidad() == Constantes.HABILIDAD_DESTREZA) {
                            monje.setDestreza((int) habilidad.getValorActual());
                        } else if (habilidad.getIdHabilidad() == Constantes.HABILIDAD_POPULARIDAD) {
                            monje.setPopularidad((int) habilidad.getValorActual());
                        }
                    }
                    // email.setAsunto(resource.getMessage("mensajes.mail.asunto"));
                    monje.setBarra_fe(HTML.smallBarra(Math.round((short) monje.getFe() / 10), resource.getMessage("monjes.abadia.fe") + Integer.toString(monje.getFe())));
                    monje.setBarra_carisma(HTML.smallBarra(Math.round((short) monje.getCarisma() / 10), resource.getMessage("monjes.abadia.carisma") + Integer.toString(monje.getCarisma())));
                    monje.setBarra_destreza(HTML.smallBarra(Math.round((short) monje.getDestreza() / 10), resource.getMessage("monjes.abadia.destreza") + Integer.toString(monje.getDestreza())));
                    monje.setBarra_fuerza(HTML.smallBarra(Math.round((short) monje.getFuerza() / 10), resource.getMessage("monjes.abadia.fuerza") + Integer.toString(monje.getFuerza())));
                    monje.setBarra_sabiduria(HTML.smallBarra(Math.round((short) monje.getSabiduria() / 10), resource.getMessage("monjes.abadia.sabiduria") + Integer.toString(monje.getSabiduria())));
                    monje.setBarra_talento(HTML.smallBarra(Math.round((short) monje.getTalento() / 10), resource.getMessage("monjes.abadia.talento") + Integer.toString(monje.getTalento())));
                    monje.setBarra_idioma(HTML.smallBarra(Math.round((short) monje.getIdioma() / 10), resource.getMessage("monjes.abadia.idioma") + Integer.toString(monje.getIdioma())));
                }
            }
            return alMonjes;

        } catch (AbadiaException e) {
            throw new AbadiaSQLException("adAbadia. getMonjes. Error SQL", e, log);
        }
    }


    public Abadia recuperarAbadiaDeUsuario(Usuario usuario, MessageResources resource) throws AbadiaException {
        //Definición de cadena sql de consulta
        String sSQL = "Select a.ABADIAID, a.NOMBRE, a.FECHA_CONSTRUCCION, a.ORDENID, a.ARQUITECTO, a.CAPACIDAD, a.NIVEL_JERARQUICO, a.HISTORIA, a.REGIONID, r.IDIOMAID, l.literal as DESC_IDIOMA, a.FECHA_ELIMINACION, e.NIVEL " +
                "from abadia a, region r, idioma i, literales l, edificio e " +
                " where a.USUARIOID = ? and r.REGIONID = a.REGIONID and r.idiomaid = i.idiomaid and i.literalid = l.literalid and l.idiomaid = " + usuario.getIdDeIdioma() + " and e.abadiaid = a.abadiaid and e.tipoedificioid = 99";
        //creo un objeto de tipo Abadia
        Abadia abadia = new Abadia();
        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        adAbadia oAbadiaAD;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave.

            int parNo = 1;
            ps.setLong(parNo, usuario.getIdDeUsuario());
            //Lanzo la consulta y cargo el resultado en un resultset
            rs = ps.executeQuery();
            //si la consulta encuentra la abadia....
            if (rs.next()) {
                //iniciamos el volcado de datos sobre
                //el objeto abadia.
                abadia.setIdDeAbadia(rs.getInt("ABADIAID"));
                abadia.setNombre(rs.getString("NOMBRE"));
                abadia.setFechaDeConstruccion(rs.getDate("FECHA_CONSTRUCCION").toString());
                abadia.setArquitecto(rs.getString("ARQUITECTO"));
                abadia.setCapacidad(rs.getShort("CAPACIDAD"));
                abadia.setNivelJerarquico(rs.getShort("NIVEL_JERARQUICO"));
                abadia.setHistoria(rs.getString("HISTORIA"));
                abadia.setIdDeRegion(rs.getInt("REGIONID"));
                abadia.setIdIdioma(rs.getInt("IDIOMAID"));
                abadia.setIdDeOrden(rs.getInt("ORDENID"));
                abadia.setDescIdioma(rs.getString("DESC_IDIOMA"));
                abadia.setNivelJerarquico(rs.getShort("NIVEL"));
                oAbadiaAD = new adAbadia(con);
                abadia.setPuntuacion(oAbadiaAD.recuperarPuntuacion(abadia.getIdDeAbadia()));
                abadia.setFechaEliminacion(rs.getString("FECHA_ELIMINACION"));
            } else
                throw new AbadiaNotFoundException("adAbadia. recuperarAbadiaDeUsuario. No se encuentra la abadia", usuario, log);

            return abadia;

        } catch (SQLException e) {
            throw new SystemException("adAbadia. recuperarAbadiaDeUsuario. error SQL", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * recupera un boolean indicando si ya existe una abadía para ese usuario.
     *
     * @param idUsuario
     * @return
     * @throws AbadiaException
     */
    public boolean existeAbadiaUsuario(long idUsuario) throws AbadiaException {
        String sSQL = "select count(*) from abadia where usuarioid = " + idUsuario;
        adUtils utils = null;
        int iReturn;
        try {
            utils = new adUtils(con);
            iReturn = utils.getSQL(sSQL, 0);
            return iReturn != 0;
        } catch (AbadiaSQLException e) {
            throw e;
        }
    }

    public ArrayList<Abadia> recuperarAbadias() throws AbadiaSQLException {
        //Definición de cadena sql de consulta
        String sSQL = "Select * from abadia";
        //creo un objeto de tipo Abadia
        Abadia abadia;
        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave.

            //Lanzo la consulta y cargo el resultado en un resultset
            rs = ps.executeQuery();
            //si la consulta encuentra la abadia....
            ArrayList<Abadia> abadias = new ArrayList<Abadia>();
            while (rs.next()) {
                //iniciamos el volcado de datos sobre
                //el objeto abadia.
                abadia = new Abadia();
                abadia.setIdDeAbadia(rs.getInt("ABADIAID"));
                abadia.setNombre(rs.getString("NOMBRE"));
                abadia.setFechaDeConstruccion(rs.getDate("FECHA_CONSTRUCCION").toString());
                abadia.setArquitecto(rs.getString("ARQUITECTO"));
                abadia.setCapacidad(rs.getShort("CAPACIDAD"));
                abadia.setNivelJerarquico(rs.getShort("NIVEL_JERARQUICO"));
                abadia.setHistoria(rs.getString("HISTORIA"));

                //devolvemos el objeto Abadia informado.
                abadias.add(abadia);
            }
            //si abadia no se localiza, devolveremos null
            return abadias;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adAbadia. recuperarAbadias. error SQL", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public ArrayList<DatosAbadia> buscarAbadias(BuscarAbadiasActForm datos) throws AbadiaException {
        String sSQL = "Select a.abadiaid as ABADIAID, a.nombre as NOMBRE_ABADIA, r.descripcion as NOMBRE_REGION, o.descripcion as NOMBRE_ORDEN " +
                "from abadia a, region r, orden_eclesiastica o, usuario u where a.regionid=r.regionid and a.ordenid=o.ordenid and a.usuarioid=u.usuarioid";
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<DatosAbadia> listaAbadias = new ArrayList<DatosAbadia>();

        try {
            if (datos.getOrden() > 0) {
                sSQL = sSQL + " and a.ordenid = " + datos.getOrden();
            }
            if (datos.getRegion() > 0) {
                sSQL = sSQL + " and a.regionid = " + datos.getRegion();
            }
            if (datos.isAdministradores()) {
                sSQL = sSQL + " and u.usuario_tipo = 1";
            }
            if (!GenericValidator.isBlankOrNull(datos.getNombre())) {
                sSQL = sSQL + " and a.nombre like '%" + datos.getNombre() + "%' ";
            }
            sSQL = sSQL + " order by NOMBRE_ABADIA";
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            DatosAbadia abadia;
            while (rs.next()) {
                abadia = new DatosAbadia();
                abadia.setIdAbadia(rs.getLong("ABADIAID"));
                abadia.setNombre_abadia(rs.getString("NOMBRE_ABADIA"));
                abadia.setNombre_orden(rs.getString("NOMBRE_ORDEN"));
                abadia.setNombre_region(rs.getString("NOMBRE_REGION"));
                listaAbadias.add(abadia);
            }
            return listaAbadias;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adAbadia. buscarAbadias. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public ArrayList<DatosAbadia> buscarAbadiasPorNombre(String p_szNombreAbadia) throws AbadiaException {
        String sSQL = "{call buscar_abadias(?)}";
        CallableStatement cs = null;
        ResultSet rs = null;
        ArrayList<DatosAbadia> listaAbadias = new ArrayList<DatosAbadia>();

        try {
            cs = con.prepareCall(sSQL);
            cs.setString(1, p_szNombreAbadia);
            cs.execute();
            rs = cs.getResultSet();
            DatosAbadia abadia;
            while (rs.next()) {
                abadia = new DatosAbadia();
                abadia.setIdAbadia(rs.getLong("ABADIAID"));
                abadia.setNombre_abadia(rs.getString("NOMBRE_ABADIA"));
                abadia.setNombre_region(rs.getString("NOMBRE_REGION"));
                listaAbadias.add(abadia);
            }
            return listaAbadias;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adAbadia. buscarAbadiasPorNombre. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(cs);
        }
    }

    public String getIDAbadiasAdministradores() throws AbadiaSQLException {
        String sSQL = "SELECT ABADIAID FROM abadia, usuario WHERE abadia.usuarioid=usuario.usuarioid and usuario.usuario_tipo=1";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave del monje
            rs = ps.executeQuery();
            String abadias = "";
            while (rs.next()) {
                abadias = abadias + rs.getInt("ABADIAID") + ";";
            }
            if (!abadias.equals("")) {
                abadias = abadias.substring(0, abadias.length() - 1);
            }

            return abadias;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adAbadia. getIDAbadiasAdministradores.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public ArrayList<Email> getMailAdministradores() throws AbadiaException {
        String sSQL = "SELECT EMAIL FROM usuario WHERE usuario.usuario_tipo=1";
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Email> listaCorreo = new ArrayList<Email>();
        try {
            ps = con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave del monje
            rs = ps.executeQuery();

            Email mail;
            while (rs.next()) {
                mail = new Email();
                mail.setTo(rs.getString("EMAIL"));
                listaCorreo.add(mail);
            }
            return listaCorreo;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adAbadia. getMailAdministradores.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /**
     * Devuelve el indicador que determina si la abadía quiere tener obispo
     *
     * @param idAbadia
     * @return
     * @throws AbadiaException
     */
    public boolean getFlagObispo(int idAbadia) throws AbadiaException {
        String sSQL = "Select FLAG_OBISPO from abadia where abadiaid = " + idAbadia;
        int flagObispo;
        adUtils utils = new adUtils(con);
        flagObispo = utils.getSQL(sSQL, 0);
        return flagObispo != 0;
    }

    /**
     * Devuelve el indicador que determina si la abadía quiere que su obispo sea Cardenal
     *
     * @param idAbadia
     * @return
     * @throws AbadiaException
     */
    public boolean getFlagCardenal(int idAbadia) throws AbadiaException {
        String sSQL = "Select FLAG_CARDENAL from abadia where abadiaid = " + idAbadia;
        int flagCardenal;
        adUtils utils = new adUtils(con);
        flagCardenal = utils.getSQL(sSQL, 0);
        return flagCardenal != 0;
    }

    /**
     * Actualiza el flag que determina si la abadia quiere ser obispo
     *
     * @param idAbadia
     * @param iValor
     * @throws AbadiaException
     */
    public void actualizarFlagObispo(int idAbadia, int iValor) throws AbadiaException {
        String sSQL = "update abadia set FLAG_OBISPO = " + iValor + " where abadiaid = " + idAbadia;
        adUtils utils = new adUtils(con);
        utils.execSQL(sSQL);
    }

    /**
     * Actualiza el flag que determina si la abadia quiere que su obispo sea cardenal
     *
     * @param idAbadia
     * @param iValor
     * @throws AbadiaException
     */
    public void actualizarFlagCardenal(int idAbadia, int iValor) throws AbadiaException {
        String sSQL = "update abadia set FLAG_CARDENAL = " + iValor + " where abadiaid = " + idAbadia;
        adUtils utils = new adUtils(con);
        utils.execSQL(sSQL);
    }

    /**
     * Actualiza la fecha de eliminación de la abadia
     *
     * @param idAbadia
     * @param sValor
     * @throws AbadiaException
     */
    public void actualizarFechaEliminacion(int idAbadia, String sValor) throws AbadiaException {
        String sSQL = "";
        adUtils utils;
        if (sValor == null) {
            sSQL = "update abadia set FECHA_ELIMINACION = null where abadiaid = " + idAbadia;
        } else {
            sSQL = "update abadia set FECHA_ELIMINACION = '" + sValor + "' where abadiaid = " + idAbadia;
        }
        try {
            utils = new adUtils(con);
            utils.execSQL(sSQL);
        } catch (AbadiaException e) {
            throw new AbadiaSQLException("adAbadia.actualizarFechaEliminacion", e, log);
        }
    }

    public ArrayList<AbadiaBasic> recuperarAbadiasPuntuacion() throws AbadiaException {
        String sSQL = "SELECT a.nombre, a.abadiaid " +
                "FROM `abadia` a, usuario u " +
                "where a.usuarioid = u.usuarioid and u.usuarioid not in (select usuarioid from usuario_bloqueo)";

        AbadiaBasic abadia;
        ArrayList<AbadiaBasic> alAbadias = new ArrayList<AbadiaBasic>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                abadia = new AbadiaBasic();
                abadia.setIdDeAbadia(rs.getInt("ABADIAID"));
                abadia.setNombre(rs.getString("NOMBRE"));
                alAbadias.add(abadia);
            }
            return alAbadias;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adAbadia. recuperarAbadiasPuntuacion.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public ArrayList<AbadiaBasic> recuperarTopTenRegional(int idRegion) throws AbadiaException {
        String sSQL = "SELECT a.nombre, a.abadiaid, apa.total, apa.clasificacion_regional " +
                " FROM `abadia` a, usuario u, abadia_puntuacion_actual apa " +
                " where a.regionid = ? and a.abadiaid = apa.abadiaid and a.usuarioid = u.usuarioid and u.usuario_tipo <> 1 and apa.clasificacion_regional > 0 " +
                " order by apa.clasificacion_regional limit 10";

        AbadiaBasic abadia;
        ArrayList<AbadiaBasic> alAbadias = new ArrayList<AbadiaBasic>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idRegion);
            rs = ps.executeQuery();
            while (rs.next()) {
                abadia = new AbadiaBasic();
                abadia.setIdDeAbadia(rs.getInt("ABADIAID"));
                abadia.setNombre(rs.getString("NOMBRE"));
                abadia.setPosicion(rs.getInt("CLASIFICACION_REGIONAL"));
                abadia.setPuntuacion(Utilidades.redondear(rs.getDouble("TOTAL")));
                alAbadias.add(abadia);
            }
            return alAbadias;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adAbadia. recuperarTopTenRegional.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public void reasignarPosicionesPuntuacion() throws AbadiaException {
        // Posiciones
        String sSQLPos1 = "SELECT ap.abadiaid FROM abadia a, usuario u, abadia_puntuacion ap, propiedad_valor pv where a.usuarioid = u.usuarioid and ap.abadiaid = a.abadiaid and pv.propiedadid = 99 and date(pv.valor) = ap.fecha_abadia and u.usuarioid not in (select usuarioid from usuario_bloqueo) and u.usuario_tipo <> 1 ORDER BY total desc";
        String sSQLPos2 = "SELECT a.abadiaid, a.regionid FROM abadia a, abadia_puntuacion ap, usuario u, propiedad_valor pv where a.usuarioid = u.usuarioid and a.abadiaid = ap.abadiaid and pv.propiedadid = 99 and date(pv.valor) = ap.fecha_abadia and u.usuarioid not in (select usuarioid from usuario_bloqueo) and u.usuario_tipo <> 1 ORDER BY a.regionid, ap.total desc";

        int posicion;
        int regionid = 0;
        PreparedStatement ps = null;
        ResultSet rs = null;
        adUtils utils;

        try {
            posicion = 1;
            ps = con.prepareStatement(sSQLPos1);
            rs = ps.executeQuery();
            while (rs.next()) {
                utils = new adUtils(con);
                utils.execSQL("UPDATE abadia_puntuacion SET clasificacion = " + (posicion++) + " WHERE abadiaid = " + rs.getInt("abadiaid") + " and FECHA_ABADIA = (Select date(valor) from propiedad_valor where propiedadid = 99)");
            }
            // Asignar las posiciones de la abadia por region
            posicion = 1;
            ps = con.prepareStatement(sSQLPos2);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (regionid != rs.getInt("regionid")) {
                    regionid = rs.getInt("regionid");
                    posicion = 1;
                }
                utils = new adUtils(con);
                utils.execSQL("UPDATE abadia_puntuacion SET clasificacion_regional = " + (posicion++) + " WHERE abadiaid = " + rs.getInt("abadiaid") + " and FECHA_ABADIA = (Select date(valor) from propiedad_valor where propiedadid = 99)");
            }
        } catch (SQLException e) {
            throw new AbadiaSQLException("adAbadia.reasignarPosicionesPuntuacion", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public void crearRegistroPuntuacion(AbadiaPuntuacion puntuacion) throws AbadiaException {
        adUtils utils;
        String sSQL = "insert into abadia_puntuacion (ABADIAID, FECHA_ABADIA, FECHA_REAL, MONJES_NIVEL, MONJES_HABILIDAD, MONJES_ACTIVIDAD, MONJES_MUERTOS, SANTOS, ANIMALES, EDIFICIOS, LIBROS, TOTAL) " +
                " values (" + puntuacion.getIdAbadia() + ", (SELECT date(VALOR) FROM propiedad_valor WHERE propiedadid = 99), now(), " + puntuacion.getMonjesNivel() + ", " + puntuacion.getMonjesHabilidad() + ", " + puntuacion.getMonjesActividad() + ", " + puntuacion.getMonjesMuertos() + ", " + puntuacion.getSantos() + ", " + puntuacion.getAnimales() + ", " + puntuacion.getEdificios() + ", " + puntuacion.getLibros() + "," + puntuacion.getTotal() + ")";
        try {
            utils = new adUtils(con);
            utils.execSQL(sSQL);
        } catch (AbadiaException e) {
            log.error("adAbadia. crearRegistroPuntuacion", e);
        }
    }

    public double recuperarPuntuacion(int idAbadia) throws AbadiaException {
        adUtils utils;
        String sSQL = "select TOTAL from abadia_puntuacion where abadiaid = " + idAbadia + " order by fecha_abadia desc limit 1";
        double puntuacion = 0;

        try {
            utils = new adUtils(con);
            return utils.getSQL(sSQL, puntuacion);
        } catch (AbadiaException e) {
            log.error("adAbadia.recuperarPuntuacion", e);
            throw e;
        }
    }

    public AbadiaPuntuacion recuperarPuntuacionCompleta(int idAbadia) throws AbadiaException {
        String sSQL = "select * from abadia_puntuacion where abadiaid = " + idAbadia + " order by fecha_abadia desc limit 1";
        AbadiaPuntuacion puntuacion = new AbadiaPuntuacion();
        puntuacion.setIdAbadia(idAbadia);
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            if (rs.next()) {
                puntuacion.setAnimales(rs.getDouble("ANIMALES"));
                puntuacion.setLibros(rs.getDouble("LIBROS"));
                puntuacion.setEdificios(rs.getDouble("EDIFICIOS"));
                puntuacion.setSantos(rs.getDouble("SANTOS"));
                puntuacion.setMonjesActividad(rs.getDouble("MONJES_ACTIVIDAD"));
                puntuacion.setMonjesHabilidad(rs.getDouble("MONJES_HABILIDAD"));
                puntuacion.setMonjesMuertos(rs.getDouble("MONJES_MUERTOS"));
                puntuacion.setMonjesNivel(rs.getDouble("MONJES_NIVEL"));
                puntuacion.setTotal(rs.getDouble("TOTAL"));
                puntuacion.setClasificacion(rs.getInt("CLASIFICACION"));
                puntuacion.setClasificacionRegional(rs.getInt("CLASIFICACION_REGIONAL"));
            }
            return puntuacion;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adAbadia.recuperarPuntuacionCompleta", log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public ArrayList<AbadiaPuntuacionForm> recuperarPuntuaciones(int idAbadia) throws AbadiaException {
        String sSQL = "select * from abadia_puntuacion where abadiaid = " + idAbadia + " order by fecha_abadia desc";
        AbadiaPuntuacionForm puntuacion;
        ArrayList<AbadiaPuntuacionForm> alPuntuaciones = new ArrayList<AbadiaPuntuacionForm>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                puntuacion = new AbadiaPuntuacionForm();
                puntuacion.setFechaAbadia(Utilidades.formatStringFromDB(rs.getString("FECHA_ABADIA")));
                puntuacion.setFechaReal(Utilidades.formatStringFromDB(rs.getString("FECHA_REAL")));
                puntuacion.setAnimales(Utilidades.redondear(rs.getDouble("ANIMALES")));
                puntuacion.setAnimalesL(rs.getDouble("ANIMALES"));
                puntuacion.setLibros(Utilidades.redondear(rs.getDouble("LIBROS")));
                puntuacion.setLibrosL(rs.getDouble("LIBROS"));
                puntuacion.setEdificios(Utilidades.redondear(rs.getDouble("EDIFICIOS")));
                puntuacion.setEdificiosL(rs.getDouble("EDIFICIOS"));
                puntuacion.setSantos(Utilidades.redondear(rs.getDouble("SANTOS")));
                puntuacion.setSantosL(rs.getDouble("SANTOS"));
                puntuacion.setMonjesActividad(Utilidades.redondear(rs.getDouble("MONJES_ACTIVIDAD")));
                puntuacion.setMonjesActividadL(rs.getDouble("MONJES_ACTIVIDAD"));
                puntuacion.setMonjesHabilidad(Utilidades.redondear(rs.getDouble("MONJES_HABILIDAD")));
                puntuacion.setMonjesHabilidadL(rs.getDouble("MONJES_HABILIDAD"));
                puntuacion.setMonjesMuertos(Utilidades.redondear(rs.getDouble("MONJES_MUERTOS")));
                puntuacion.setMonjesMuertosL(rs.getDouble("MONJES_MUERTOS"));
                puntuacion.setMonjesNivel(Utilidades.redondear(rs.getDouble("MONJES_NIVEL")));
                puntuacion.setMonjesNivelL(rs.getDouble("MONJES_NIVEL"));
                puntuacion.setTotal(Utilidades.redondear(rs.getDouble("TOTAL")));
                puntuacion.setTotalL(rs.getDouble("TOTAL"));
                puntuacion.setClasificacion(rs.getInt("CLASIFICACION"));
                puntuacion.setClasificacionRegional(rs.getInt("CLASIFICACION_REGIONAL"));
                alPuntuaciones.add(puntuacion);
            }
            return alPuntuaciones;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adAbadia.recuperarPuntuaciones", log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public ArrayList<EdificioNivel> recuperarAbadiasParaNivelEdificios() throws AbadiaException {
        String sSQL = "SELECT * FROM `edificio_construccion` WHERE FECHA_FINALIZACION <= ? ";
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<EdificioNivel> alAbadias = new ArrayList<EdificioNivel>();

        try {

            ps = con.prepareStatement(sSQL);
            ps.setString(1, CoreTiempo.getTiempoAbadiaString());
            rs = ps.executeQuery();   // animales que deberían de pasar de nivel
            while (rs.next()) {
                alAbadias.add(new EdificioNivel(rs.getInt("abadiaid"), rs.getInt("nivel"), rs.getInt("tipoedificioid")));
            }
            return alAbadias;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adAbadia.recuperarPuntuaciones", log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

}
