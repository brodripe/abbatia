package org.abbatia.adbean;

import org.abbatia.actionform.MonjeActividadActForm;
import org.abbatia.actionform.MostrarMonjeActForm;
import org.abbatia.adbean.base.adbeans;
import org.abbatia.bbean.singleton.CargasInicialesDietasBBean;
import org.abbatia.bean.*;
import org.abbatia.core.CoreTiempo;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaDBConnectionException;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.ErrorGeneralException;
import org.abbatia.exception.MonjeNoEncontradoException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.HTML;
import org.abbatia.utils.Utilidades;
import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;

public class adMonje extends adbeans {
    private static Logger log = Logger.getLogger(adMonje.class.getName());
/*
  public static int MONJES_TODOS = 0;
  public static int MONJES_VIVOS = 1;
  public static int MONJES_MUERTOS = 2;  // Velatorio
  public static int MONJES_CEMENTERIO = 3;
  public static int MONJES_ENFERMOS = 4;  // Enfermos
  public static int MONJES_VIAJANDO = 5;
  public static int MONJES_VISITA = 6;
  public static int MONJES_VISITA_MIABADIA = 7;  // Monjes que están de visita en mi abbatia
  //public static int MONJES_VISITA_MIABADIA_ENF = 8;  // Monjes que están de visita en mi abbatia y estan enfermos
  public static int MONJES_VISITA_ENFERMOS = 8;  // Monjes de visita que estan enfermos
  public static int MONJES_OSARIO = 9;
*/


    /**
     * Instancia la clase sin obtener una nueva conexión
     *
     * @param con Conexión
     * @throws AbadiaException Excepción general
     */
    public adMonje(Connection con) throws AbadiaException {
        super(con);
    }

    //Devuelve el objeto Monje con los datos
    //cargados de la base de datos

    public Monje recuperarMonje(long MonjeID, long idAbadia, MessageResources resource, int idiomaid) throws AbadiaException {
        adEnfermedad enfermedadAD;

        Monje monje = new Monje();
        String sSQL = "Select * from monje m, monje_alimentacion ma where m.MONJEID = ma.MONJEID AND m.MONJEID = ?";
        String sSQLMens = "SELECT * FROM mensajes WHERE monjeid = ? ORDER BY fechaabadia DESC LIMIT 20";
        String sSQLActi = "SELECT * from actividad where MONJEID = ? ";

        String sHTML;
        // Buscar monje
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setLong(1, MonjeID);
            rs = ps.executeQuery();
            if (rs.next()) {
                monje.setIdMonje(rs.getInt("MONJEID"));
                monje.setIdAbadia(rs.getInt("ABADIAID"));
                monje.setIdDeEspecializacion(rs.getInt("ESPECIALIZACIONID"));
                monje.setIdDeJerarquia(rs.getInt("JERARQUIAID"));
                monje.setEstado(rs.getInt("ESTADO"));
                monje.setNombre(rs.getString("NOMBRE"));
                monje.setApellido1(rs.getString("APELLIDO1"));
                monje.setFechaDeEntradaEnAbadia(Utilidades.formatStringFromDB(rs.getString("FECHA_ENTRADA")));
                monje.setFechaDeNacimiento(Utilidades.formatStringFromDB(rs.getString("FECHA_NACIMIENTO")));
                monje.setFechaDeFallecimiento(Utilidades.formatStringFromDB(rs.getString("FECHA_FALLECIMIENTO")));
                monje.setEdad(CoreTiempo.getEdad(rs.getString("FECHA_NACIMIENTO")));
                monje.setSiguienteID(getSiguiente(MonjeID, idAbadia));
                monje.setAnteriorID(getAnterior(MonjeID, idAbadia));
                // Buscar la alimentación que tiene
                monje.setComeFamiliaID1(rs.getShort("COMER_FAMILIAID_1"));
                monje.setComeFamiliaID2(rs.getShort("COMER_FAMILIAID_2"));
                monje.setComeFamiliaID3(rs.getShort("COMER_FAMILIAID_3"));
                monje.setComeFamiliaID4(rs.getShort("COMER_FAMILIAID_4"));
                monje.setComeFamiliaID5(rs.getShort("COMER_FAMILIAID_5"));
                monje.setHa_comidoFamiliaID1(rs.getShort("HA_COMIDO_FAMILIAID_1"));
                monje.setHa_comidoFamiliaID2(rs.getShort("HA_COMIDO_FAMILIAID_2"));
                monje.setHa_comidoFamiliaID3(rs.getShort("HA_COMIDO_FAMILIAID_3"));
                monje.setHa_comidoFamiliaID4(rs.getShort("HA_COMIDO_FAMILIAID_4"));
                monje.setHa_comidoFamiliaID5(rs.getShort("HA_COMIDO_FAMILIAID_5"));
                monje.setProteinas(rs.getShort("PROTEINAS"));
                monje.setLipidos(rs.getShort("LIPIDOS"));
                monje.setHidratosCarbono(rs.getShort("HIDRATOS_CARBONO"));
                monje.setVitaminas(rs.getShort("VITAMINAS"));
                monje.setFechaDeUltimaComida(rs.getString("ULTIMA_COMIDA"));
                monje.setSalud(rs.getShort("SALUD"));
                rs.close();
                ps.close();
                // Actividades
                ps = con.prepareStatement(sSQLActi);
                ps.setLong(1, MonjeID);
                rs = ps.executeQuery();
                while (rs.next()) {
                    if (rs.getInt("periodoid") == Constantes.PERIODO_MAITINES) {
                        monje.setActMaitines(rs.getShort("actividadid"));
                        monje.setActMaitines_completado(rs.getShort("realizado"));
                        monje.setActMaitines_bloqueado(rs.getShort("bloqueada"));
                        if (rs.getShort("bloqueada") == 1)
                            monje.setActMaitines_link(recuperarLinkTarea(monje.getIdMonje(), idiomaid, rs.getInt("actividadid"), rs.getInt("periodoid")));
                    }
                    if (rs.getInt("periodoid") == Constantes.PERIODO_LAUDES) {
                        monje.setActLaudes(rs.getShort("actividadid"));
                        monje.setActLaudes_completado(rs.getShort("realizado"));
                        monje.setActLaudes_bloqueado(rs.getShort("bloqueada"));
                        if (rs.getShort("bloqueada") == 1)
                            monje.setActLaudes_link(recuperarLinkTarea(monje.getIdMonje(), idiomaid, rs.getInt("actividadid"), rs.getInt("periodoid")));

                    }
                    if (rs.getInt("periodoid") == Constantes.PERIODO_PRIMA) {
                        monje.setActPrima(rs.getShort("actividadid"));
                        monje.setActPrima_completado(rs.getShort("realizado"));
                        monje.setActPrima_bloqueado(rs.getShort("bloqueada"));
                        if (rs.getShort("bloqueada") == 1)
                            monje.setActPrima_link(recuperarLinkTarea(monje.getIdMonje(), idiomaid, rs.getInt("actividadid"), rs.getInt("periodoid")));

                    }
                    if (rs.getInt("periodoid") == Constantes.PERIODO_TERCIA) {
                        monje.setActTercia(rs.getShort("actividadid"));
                        monje.setActTercia_completado(rs.getShort("realizado"));
                        monje.setActTercia_bloqueado(rs.getShort("bloqueada"));
                        if (rs.getShort("bloqueada") == 1)
                            monje.setActTercia_link(recuperarLinkTarea(monje.getIdMonje(), idiomaid, rs.getInt("actividadid"), rs.getInt("periodoid")));

                    }
                    if (rs.getInt("periodoid") == Constantes.PERIODO_SEXTA) {
                        monje.setActSexta(rs.getShort("actividadid"));
                        monje.setActSexta_completado(rs.getShort("realizado"));
                        monje.setActSexta_bloqueado(rs.getShort("bloqueada"));
                        if (rs.getShort("bloqueada") == 1)
                            monje.setActSexta_link(recuperarLinkTarea(monje.getIdMonje(), idiomaid, rs.getInt("actividadid"), rs.getInt("periodoid")));

                    }
                    if (rs.getInt("periodoid") == Constantes.PERIODO_NONA) {
                        monje.setActNona(rs.getShort("actividadid"));
                        monje.setActNona_completado(rs.getShort("realizado"));
                        monje.setActNona_bloqueado(rs.getShort("bloqueada"));
                        if (rs.getShort("bloqueada") == 1)
                            monje.setActNona_link(recuperarLinkTarea(monje.getIdMonje(), idiomaid, rs.getInt("actividadid"), rs.getInt("periodoid")));

                    }
                    if (rs.getInt("periodoid") == Constantes.PERIODO_VISPERAS) {
                        monje.setActVispera(rs.getShort("actividadid"));
                        monje.setActVispera_completado(rs.getShort("realizado"));
                        monje.setActVispera_bloqueado(rs.getShort("bloqueada"));
                        if (rs.getShort("bloqueada") == 1)
                            monje.setActVispera_link(recuperarLinkTarea(monje.getIdMonje(), idiomaid, rs.getInt("actividadid"), rs.getInt("periodoid")));

                    }
                    if (rs.getInt("periodoid") == Constantes.PERIODO_ANGELUS) {
                        monje.setActAngelus(rs.getShort("actividadid"));
                        monje.setActAngelus_completado(rs.getShort("realizado"));
                        monje.setActAngelus_bloqueado(rs.getShort("bloqueada"));
                        if (rs.getShort("bloqueada") == 1)
                            monje.setActAngelus_link(recuperarLinkTarea(monje.getIdMonje(), idiomaid, rs.getInt("actividadid"), rs.getInt("periodoid")));
                    }
                }
                rs.close();
                ps.close();
                // Jerarquia
                monje.setJerarquia(getDescJerarquiaMonje(monje.getIdMonje(), idiomaid));

                // Mensajes
                ArrayList<Table> alTable = new ArrayList<Table>();
                Table tabla = null;
                ps = con.prepareStatement(sSQLMens);
                ps.setLong(1, MonjeID);
                rs = ps.executeQuery();
                while (rs.next()) {
                    sHTML = " ( <font color=\"#666666\">" + Utilidades.formatStringFromDB(rs.getString("FECHAABADIA")) +
                            "</font> )&nbsp;";
                    if (rs.getInt("Tipo") == 1) {
                        sHTML = sHTML + "<font color=\"#800000\">" + rs.getString("Mensaje") + "</font>";
                    } else
                        sHTML = sHTML + rs.getString("Mensaje");
                    tabla = new Table(0, sHTML);
                    alTable.add(tabla);
                }
                if (alTable.isEmpty()) {
                    sHTML = " <center>" + resource.getMessage("monjes.abadia.sinmensajes") + "</center>";
                    tabla = new Table(0, sHTML);
                    alTable.add(tabla);
                }
                monje.setMensajesMonje(alTable);
                //asingamos la imagen de la enfermedad si procede...
                enfermedadAD = new adEnfermedad(con);
                monje.setHTMLEnfermedad(enfermedadAD.getHTMLEnfermedadPorMonje((int) MonjeID));
                monje.setNivelEnfermedad(enfermedadAD.recuperarNivelEnfermedad((int) MonjeID));

                return monje;
            } else throw new MonjeNoEncontradoException("adMonje. recuperarMonje. No se encuentra el monje.", log);

        } catch (SQLException e) {
            //log.error("adMonje. recuperarMonje. Error SQL", e);
            throw new AbadiaSQLException("adMonje. recuperarMonje. Error SQL", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public void recuperarMonjeActividad(long MonjeID, long idAbadia, MonjeActividadActForm p_oMonje, int idiomaid) throws AbadiaException {
        //MonjeActividadActForm monje = new MonjeActividadActForm();
        String sSQL = "Select * from monje m, monje_alimentacion ma where m.MONJEID = ma.MONJEID AND m.MONJEID = ?";
        String sSQLJerar = "SELECT literal from monje m, jerarquia_eclesiastica je, literales l where m.jerarquiaid = je.jerarquiaid " +
                " and l.literalid = je.literalid and l.idiomaid = " + idiomaid + " and m.monjeid = " + MonjeID;

        // Buscar monje
        PreparedStatement ps = null;
        ResultSet rs = null;
        adUtils utils;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setLong(1, MonjeID);
            rs = ps.executeQuery();
            if (rs.next()) {
                log.debug("Se devuelve el id de monje: " + rs.getLong("MONJEID"));
                p_oMonje.setIdDeMonje(rs.getLong("MONJEID"));
                p_oMonje.setIdDeAbadia(rs.getInt("ABADIAID"));
                p_oMonje.setNombre(rs.getString("NOMBRE"));
                p_oMonje.setPrimerApellido(rs.getString("APELLIDO1"));
                p_oMonje.setFechaDeEntradaEnAbadia(Utilidades.formatStringFromDB(rs.getString("FECHA_ENTRADA")));
                p_oMonje.setFechaDeNacimiento(Utilidades.formatStringFromDB(rs.getString("FECHA_NACIMIENTO")));
                p_oMonje.setEdad(CoreTiempo.getEdad(rs.getString("FECHA_NACIMIENTO")));
                p_oMonje.setSiguienteID(getSiguiente(MonjeID, idAbadia));
                p_oMonje.setAnteriorID(getAnterior(MonjeID, idAbadia));
                // Buscar la alimentación que tiene
                // Jerarquia
                utils = new adUtils(con);
                p_oMonje.setJerarquia(utils.getSQL(sSQLJerar, "*Novicio*"));
            } else
                throw new MonjeNoEncontradoException("adMonje. recuperarMonjeActividad. No se encuentra el monje.", log);

        } catch (SQLException e) {
            throw new AbadiaSQLException("adMonje. recuperarMonjeActividad. Error SQL", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    // Devuelve el nombre del monje

    public String getNomMonje(int monjeid, String literalDe) throws AbadiaException {
        String sSQL = "Select nombre, apellido1 from monje where MONJEID = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, monjeid);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString(1) + " " + literalDe + " " + rs.getString(2);
            } else {
                return " - ";
            }
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMonje. getNomMonje. Error SQL", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    // Devuelve id de la abbatia del monje

    public int getAbadiaID(int monjeid) throws AbadiaException {
        String sSQL = "Select abadiaid from monje where MONJEID = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return 0;
            }

        } catch (SQLException e) {
            throw new AbadiaSQLException("adMonje. getAbadiaID. Error SQL", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Devuelve la descripcion de la jerarquía eclesiastica de un monje
     *
     * @param idMonje
     * @param idIdioma
     * @return
     * @throws AbadiaException
     */
    public String getDescJerarquiaMonje(int idMonje, int idIdioma) throws AbadiaException {
        String sSQL = "SELECT literal from monje m, jerarquia_eclesiastica je, literales l where m.jerarquiaid = je.jerarquiaid " +
                " and l.literalid = je.literalid and l.idiomaid = " + idIdioma + " and m.monjeid = " + idMonje;

        adUtils utils = new adUtils(con);
        return utils.getSQL(sSQL, "X");

    }

    // Devuelve el núm de monjes que hay vivos en una abbatia

    public int getNumMonjes(int idAbadia) throws AbadiaSQLException {
        String sSQL = "Select Count(*) from monje where ESTADO<>1 AND ABADIAID = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            // Asignar
            int parNo = 1;
            ps.setLong(parNo, idAbadia);
            // Ejecutarlo
            rs = ps.executeQuery();
            //Si ya existe el registro....
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return (int) 0;
            }
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMonje. getNumMonjes. Error SQL", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    // Devuelve el núm de monjes que hay vivos en una abbatia

    public int getNumMonjesSinJerarquia(int idAbadia) throws AbadiaSQLException {
        String sSQL = "Select (Select Count(*) from monje m where m.estado =" + Constantes.MONJE_VIVO + " AND m.abadiaid = a.abadiaid ) - " +
                " (Select Count(*) from monje m, edificio e where m.estado = " + Constantes.MONJE_VIVO + " AND m.abadiaid = a.abadiaid " +
                " AND e.abadiaid = a.abadiaid AND jerarquiaid >= " + Constantes.JERARQUIA_OBISPO +
                " AND e.tipoedificioid = " + Constantes.EDIFICIO_HOSPEDERIA + ") total " +
                " from abadia a WHERE a.ABADIAID = ? ";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            // Asignar
            int parNo = 1;
            ps.setLong(parNo, idAbadia);
            // Ejecutarlo
            rs = ps.executeQuery();
            //Si ya existe el registro....
            if (rs.next()) {
                log.debug("getNumMonjes - " + rs.getInt(1));
                return rs.getInt(1);
            } else {
                return 0;
            }
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMonje. getNumMonjes. Error SQL", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Devuelve el numero de monjes en un estado determinado de una ab
     *
     * @param idAbadia
     * @param estado
     * @return
     * @throws AbadiaException
     */
    public int getNumMonjes(int idAbadia, int estado) throws AbadiaException {
        adUtils utilsAD = new adUtils(con);
        return utilsAD.getSQL("Select Count(*) from monje where ESTADO=" + estado + " AND ABADIAID = " + idAbadia, 0);
    }

    /**
     * Devuelve el numero de monjes en un estado determinado de una ab
     *
     * @param idAbadia
     * @return
     * @throws AbadiaException
     */
    public int getNumMonjesSanos(int idAbadia) throws AbadiaException {
        adUtils utilsAD = new adUtils(con);
        return utilsAD.getSQL("Select Count(*) from monje where ESTADO=" + Constantes.MONJE_VIVO + " AND ABADIAID = " + idAbadia + " and monjeid not in (select monjeid from monje_enfermedad where nivel = 3) ", 0);
    }

    /**
     * Devuelve el numero de monjes en un estado determinado de una ab
     *
     * @param idAbadia
     * @return
     * @throws AbadiaException
     */
    public int getNumMonjesEnfermos(int idAbadia) throws AbadiaException {
        adUtils utilsAD = new adUtils(con);
        return utilsAD.getSQL("Select Count(*) from monje where ESTADO=" + Constantes.MONJE_VIVO + " AND ABADIAID = " + idAbadia + " and monjeid in (select monjeid from monje_enfermedad where nivel = 3) ", 0);
    }

    /**
     * Devuelve el numero de monjes en un estado determinado de una ab
     *
     * @param idAbadia
     * @return
     * @throws AbadiaException
     */
    public int getNumMonjesVisitaSanos(int idAbadia) throws AbadiaException {
        adUtils utilsAD = new adUtils(con);
        return utilsAD.getSQL("Select Count(*) from monje_visita mv, monje m where m.monjeid = mv.monjeid and m.estado = " + Constantes.MONJE_VISITA + " and mv.abadiaid_destino = " + idAbadia + " and m.monjeid not in (select monjeid from monje_enfermedad where nivel = 3) ", 0);
    }

    /**
     * Devuelve el numero de monjes en un estado determinado de una ab
     *
     * @param idAbadia
     * @return
     * @throws AbadiaException
     */
    public int getNumMonjesVisitaEnfermos(int idAbadia) throws AbadiaException {

        adUtils utilsAD = new adUtils(con);
        return utilsAD.getSQL("Select Count(*) from monje_visita mv, monje m where m.monjeid = mv.monjeid and m.estado = " + Constantes.MONJE_VISITA + " and mv.abadiaid_destino = " + idAbadia + " and m.monjeid in (select monjeid from monje_enfermedad where nivel = 3) ", 0);
    }

    /**
     * Resta n puntos de salud a todos los monjes de una abadia determinada
     *
     * @param idAbadia
     * @param salud
     * @throws AbadiaException
     */
    public void restarSaludMonjesPorAbadia(int idAbadia, double salud) throws AbadiaException {
        adUtils utils;

        try {
            utils = new adUtils(con);
            utils.execSQL("UPDATE `monje` m, monje_alimentacion ma " +
                    " set salud = salud - " + salud +
                    " where m.monjeid = ma.monjeid and m.abadiaid = " + idAbadia);
        } catch (AbadiaException e) {
            throw e;
        }
    }

    /**
     * Recupera los n monjes mas viejos de una abadia
     *
     * @param idAbadia
     * @param iNumMonjes
     * @return
     * @throws AbadiaException
     */
    public ArrayList<MonjeProceso> recuperarMonjesMayores(int idAbadia, int iNumMonjes) throws AbadiaException {
        String sSQL = "";
        adEdificio oEdificioAD;
        oEdificioAD = new adEdificio(con);
        //consulta para verificar si la abadía dispone de hospederia.
        if (oEdificioAD.existeEdificioTipo(idAbadia, Constantes.EDIFICIO_HOSPEDERIA)) {
            //si despone de hospederia los monjes con jerarquia >= 3 no ocupan plaza en el dormitorio
            sSQL = "Select * from monje where abadiaid = ? and estado = 0 and JERARQUIAID < 3 order by fecha_nacimiento limit ? ";
        } else {
            //si no dispone de hospedería, no se distingue entre eminencias y novicios...
            sSQL = "Select * from monje where abadiaid = ? and estado = 0 order by fecha_nacimiento limit ?";
        }

        ArrayList<MonjeProceso> alMonjes = new ArrayList<MonjeProceso>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        MonjeProceso monje;

        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idAbadia);
            ps.setInt(2, iNumMonjes);
            rs = ps.executeQuery();
            while (rs.next()) {
                monje = new MonjeProceso();
                monje.setIdMonje(rs.getInt("monjeid"));
                alMonjes.add(monje);
            }

            return alMonjes;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adMonje. recuperarMonjesMayores", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    //dará de alta un monje en la base de datos

    /**
     * Gestiona el alta de un nuevo monje, con todas sus habilidades y actividades iniciales
     * a partir de un objeto {@link Monje}
     * - Insert en la tabla de monjes
     * - Insert en la tabla de monjes alimentación
     * - Invocaciones a los métodos de creación de activicades y habilidades
     *
     * @param monje
     * @return
     * @throws AbadiaException
     */
    public int crearMonjeID(Monje monje) throws AbadiaException {
        adHabilidades habilidadesAD;
        adMonje monjeAD;
        String sSQL = "Insert Into monje ( ABADIAID , JERARQUIAID , ESPECIALIZACIONID , NOMBRE , APELLIDO1 ,APELLIDO2 ,FECHA_NACIMIENTO , FECHA_ENTRADA ) " +
                "Values (" + monje.getIdAbadia() + ", " + monje.getIdDeJerarquia() + ", " + monje.getIdDeEspecializacion() + ", '" + monje.getNombre() + "', '" + monje.getApellido1() + "', '" + monje.getApellido2() + "', '" + Utilidades.formatStringToDB(monje.getFechaDeNacimiento()) + "', '" + Utilidades.formatStringToDB(monje.getFechaDeEntradaEnAbadia()) + "')";

        String sSQLAlim = "INSERT INTO `monje_alimentacion` " +
                "(`MONJEID`, `COMER_FAMILIAID_1`, `COMER_FAMILIAID_2`, `COMER_FAMILIAID_3`, `COMER_FAMILIAID_4`, `COMER_FAMILIAID_5`, `PROTEINAS`, `LIPIDOS`, `HIDRATOS_CARBONO`, `VITAMINAS`, `ULTIMA_COMIDA`, `SALUD`) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, '1100-01-01', ?)";

        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        Statement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        try {
            ps = con.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_UPDATABLE);
            ps.executeUpdate(sSQL, Statement.RETURN_GENERATED_KEYS);
            rs = ps.getGeneratedKeys();

            if (rs.next()) {
                monje.setIdMonje(rs.getInt(1));
            }
            ps1 = con.prepareStatement(sSQLAlim);
            ps1.setLong(1, monje.getIdMonje());
            ps1.setShort(2, monje.getComeFamiliaID1());
            ps1.setShort(3, monje.getComeFamiliaID2());
            ps1.setShort(4, monje.getComeFamiliaID3());
            ps1.setShort(5, monje.getComeFamiliaID4());
            ps1.setShort(6, monje.getComeFamiliaID5());
            ps1.setShort(7, monje.getProteinas());
            ps1.setShort(8, monje.getLipidos());
            ps1.setShort(9, monje.getHidratosCarbono());
            ps1.setShort(10, monje.getVitaminas());
            ps1.setShort(11, monje.getSalud());
            ps1.execute();

            habilidadesAD = new adHabilidades(con);
            habilidadesAD.crearHabilidadesIniciales(monje);

            monjeAD = new adMonje(con);
            monjeAD.actualizarActividades(monje);

            return monje.getIdMonje();

        } catch (SQLException e) {
            throw new AbadiaSQLException("adMonje. crearMonjeID. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
            DBMSUtils.cerrarObjetoSQL(ps1);
        }

    }

    //elimina un monje de la base de datos
    //a partir de un objeto monje devuelve verdadero si no ha ocurrido un error

    public boolean eliminarMonje(Monje monje) throws AbadiaSQLException {
        return eliminarMonje(monje.getIdMonje());
    }

    //Elimina un monje de la base de datos
    //a partir de la clave del monje

    public boolean eliminarMonje(long MonjeID) throws AbadiaSQLException {
        String sSQL = "Delete From `monje` Where `MONJEID` = ?";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave del monje
            ps.setLong(1, MonjeID);
            // Ejecutarlo
            return ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMonje. eliminarMonje. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }


    /*   Recupera el nombre de la abbatia y la region
    */

    public void recuperarDescAbadiaRegion(Monje monje) throws AbadiaException {
        String sSQLS = "SELECT a.nombre, r.descripcion FROM `monje` m, abadia a, region r " +
                "where m.abadiaid = a.abadiaid and r.regionid = a.regionid and m.monjeid = ?";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQLS);
            ps.setLong(1, monje.getIdMonje());
            rs = ps.executeQuery();
            if (rs.next()) {
                monje.setAbadia(rs.getString("nombre"));
                monje.setRegion(rs.getString("descripcion"));
            }
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMonje. recuperarDescAbadiaRegion. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /**
     * Actualiza los datos de alimentación de un monje
     *
     * @param monje
     * @return boolean
     * @throws AbadiaException
     */
    public boolean actualizarAlimentacion(Monje monje) throws AbadiaException {
        String sSQL = "Update monje_alimentacion Set COMER_FAMILIAID_1 = ?, COMER_FAMILIAID_2 = ?, COMER_FAMILIAID_3 = ?, " +
                "COMER_FAMILIAID_4 = ?, COMER_FAMILIAID_5 = ? Where MONJEID = ?";

        // Preparar
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            // Asignar
            int parNo = 1;
            ps.setShort(parNo++, monje.getComeFamiliaID1());
            ps.setShort(parNo++, monje.getComeFamiliaID2());
            ps.setShort(parNo++, monje.getComeFamiliaID3());
            ps.setShort(parNo++, monje.getComeFamiliaID4());
            ps.setShort(parNo++, monje.getComeFamiliaID5());
            ps.setLong(parNo, monje.getIdMonje());
            // Ejecutarlo
            return ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMonje. actualizarAlimentacion. Error SQL", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /**
     * Actualiza los datos de alimentación de un monje
     *
     * @param monje
     * @throws AbadiaException
     */
    public void actualizarAlimentacionSmart(Monje monje) throws AbadiaException {

        Integer iFamilia;

        try {

            iFamilia = CargasInicialesDietasBBean.getFamiliaAlimentosActividad(monje.getNivelAbadia(),
                    monje.getIdDeJerarquia(), monje.getActMaitines());
            monje.setComeFamiliaID1(iFamilia.shortValue());

            iFamilia = CargasInicialesDietasBBean.getFamiliaAlimentosActividad(monje.getNivelAbadia(),
                    monje.getIdDeJerarquia(), monje.getActPrima());
            monje.setComeFamiliaID2(iFamilia.shortValue());

            iFamilia = CargasInicialesDietasBBean.getFamiliaAlimentosActividad(monje.getNivelAbadia(),
                    monje.getIdDeJerarquia(), monje.getActTercia());
            monje.setComeFamiliaID3(iFamilia.shortValue());

            iFamilia = CargasInicialesDietasBBean.getFamiliaAlimentosActividad(monje.getNivelAbadia(),
                    monje.getIdDeJerarquia(), monje.getActNona());
            monje.setComeFamiliaID4(iFamilia.shortValue());

            iFamilia = CargasInicialesDietasBBean.getFamiliaAlimentosActividad(monje.getNivelAbadia(),
                    monje.getIdDeJerarquia(), monje.getActVispera());
            monje.setComeFamiliaID5(iFamilia.shortValue());

        } catch (Exception e) {
            throw new AbadiaSQLException("adMonje. actualizarAlimentacionSmart. Error SQL", e, log);
        }
    }

    public void actualizarActividades(Monje monje) throws AbadiaException {
        try {
            actualizarActividad(monje.getIdMonje(), Constantes.PERIODO_MAITINES, monje.getActMaitines(), 0);
            actualizarActividad(monje.getIdMonje(), Constantes.PERIODO_LAUDES, monje.getActLaudes(), 0);

            if (monje.getActPrima_bloqueado() == 0)
                actualizarActividad(monje.getIdMonje(), Constantes.PERIODO_PRIMA, monje.getActPrima(), 0);

            if (monje.getActTercia_bloqueado() == 0)
                actualizarActividad(monje.getIdMonje(), Constantes.PERIODO_TERCIA, monje.getActTercia(), 0);

            actualizarActividad(monje.getIdMonje(), Constantes.PERIODO_SEXTA, monje.getActSexta(), 0);
            if (monje.getActNona_bloqueado() == 0)
                actualizarActividad(monje.getIdMonje(), Constantes.PERIODO_NONA, monje.getActNona(), 0);

            if (monje.getActVispera_bloqueado() == 0)
                actualizarActividad(monje.getIdMonje(), Constantes.PERIODO_VISPERAS, monje.getActVispera(), 0);

            actualizarActividad(monje.getIdMonje(), Constantes.PERIODO_ANGELUS, monje.getActAngelus(), 0);

        } catch (AbadiaSQLException e) {
            throw new AbadiaSQLException("adMonje. actualizarActividades. Error SQL", e, log);
        }
    }

    /**
     * desbloquea las tareas de copia de un monje determinado
     *
     * @param idMonje Identificador de monje
     * @throws AbadiaException Excepción general
     */
    public void desbloquarActividadesCopia(int idMonje) throws AbadiaException {
        String sSQL = "update actividad set actividadid = 1, bloqueada = 0 where monjeid = " + idMonje + " and bloqueada = 1 and actividadid = " + Constantes.TAREA_COPIAR;
        adUtils utils = new adUtils(con);
        utils.execSQL(sSQL);
    }

    /**
     * desbloquea las tareas de copia de un monje determinado en un periodo determinado
     *
     * @param idMonje   Identificador de monje
     * @param idPeriodo Identificador de periodo
     * @throws AbadiaException Excepción general
     */
    public void desbloquarActividadesCopiaPeriodo(int idMonje, int idPeriodo) throws AbadiaException {
        String sSQL = "update actividad set actividadid = 1, bloqueada = 0 where monjeid = " + idMonje + " and bloqueada = 1 and actividadid = " + Constantes.TAREA_COPIAR + " and periodoid = " + idPeriodo;
        adUtils utils = new adUtils(con);
        utils.execSQL(sSQL);
    }

    public void actualizarActividad(long idMonje, int idPeriodo, int idActividad, int bloqueado) throws AbadiaException {
        String sSQL = "Select * from actividad where MONJEID = ? AND PERIODOID = ?";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            // Asignar
            int parNo = 1;
            ps.setLong(parNo++, idMonje);
            ps.setInt(parNo, idPeriodo);
            // Ejecutarlo
            rs = ps.executeQuery();
            //Si ya existe el registro....
            if (rs.next()) {
                //actualizamos registro con la nueva actividad
                sSQL = "Update actividad Set ACTIVIDADID = ? , BLOQUEADA = ?, ACUMULADO_DIA = 0, ACUMULADO_TOTAL = 0 Where MONJEID = ? AND PERIODOID = ? ";
                ps = con.prepareStatement(sSQL);
                parNo = 1;
                ps.setInt(parNo++, idActividad);
                ps.setInt(parNo++, bloqueado);
                ps.setLong(parNo++, idMonje);
                ps.setInt(parNo, idPeriodo);
            } else {
                //creamos registro con la nueva actividad
                sSQL = "Insert into actividad (MONJEID, PERIODOID, ACTIVIDADID, NIVEL, BLOQUEADA) values (?, ?, ?, 10, ?)";
                ps = con.prepareStatement(sSQL);
                parNo = 1;
                ps.setLong(parNo++, idMonje);
                ps.setLong(parNo++, idPeriodo);
                ps.setInt(parNo++, idActividad);
                ps.setInt(parNo, bloqueado);
            }
            ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMonje. actualizarActividad. Error SQL", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /**
     * Actualiza las barras html de un monje
     *
     * @param monje
     * @param resource
     * @throws AbadiaDBConnectionException
     */
    public void actualizarBarrasHTML(Monje monje, MessageResources resource) throws AbadiaException {
        HTML shtml = new HTML();
        // Lipidos...
        monje.setBarra_lipidos(HTML.smallBarra(HTML.getBarras_Value((int) monje.getLipidos()), resource.getMessage("monjes.abadia.grasas") + " " + Integer.toString(monje.getLipidos())));
        monje.setBarra_proteinas(HTML.smallBarra(HTML.getBarras_Value((int) monje.getProteinas()), resource.getMessage("monjes.abadia.proteinas") + " " + Integer.toString(monje.getProteinas())));
        monje.setBarra_hidratosCarbono(HTML.smallBarra(HTML.getBarras_Value((int) monje.getHidratosCarbono()), resource.getMessage("monjes.abadia.carbono") + " " + Integer.toString(monje.getHidratosCarbono())));
        monje.setBarra_vitaminas(HTML.smallBarra(HTML.getBarras_Value((int) monje.getVitaminas()), resource.getMessage("monjes.abadia.vitaminas") + " " + Integer.toString(monje.getVitaminas())));
        // Salud
        int n = (int) Math.round((short) monje.getSalud() / 10);
        monje.setBarra_salud(shtml.Barra(n, Integer.toString(monje.getSalud())));
        // Habilidades
        ArrayList alHabilidades;

        adHabilidades habilidadesAD = new adHabilidades(con);
        alHabilidades = habilidadesAD.recuperarHabilidadesMonje(monje.getIdMonje());

        Iterator habilidades = alHabilidades.iterator();
        HabilidadMonje habilidad;
        while (habilidades.hasNext()) {
            habilidad = (HabilidadMonje) habilidades.next();
            n = (int) Math.round((short) habilidad.getValorActual() / 10);
            if (habilidad.getIdHabilidad() == Constantes.HABILIDAD_FE) {
                monje.setBarra_fe(shtml.Barra(n, resource.getMessage("monjes.abadia.fe") + (int) habilidad.getValorActual()));
            } else if (habilidad.getIdHabilidad() == Constantes.HABILIDAD_TALENTO) {
                monje.setBarra_talento(shtml.Barra(n, resource.getMessage("monjes.abadia.talento") + (int) habilidad.getValorActual()));
            } else if (habilidad.getIdHabilidad() == Constantes.HABILIDAD_IDIOMA) {
                monje.setBarra_idioma(shtml.Barra(n, resource.getMessage("monjes.abadia.idioma") + (int) habilidad.getValorActual()));
            } else if (habilidad.getIdHabilidad() == Constantes.HABILIDAD_SABIDURIA) {
                monje.setBarra_sabiduria(shtml.Barra(n, resource.getMessage("monjes.abadia.sabiduria") + (int) habilidad.getValorActual()));
            } else if (habilidad.getIdHabilidad() == Constantes.HABILIDAD_FUERZA) {
                monje.setBarra_fuerza(shtml.Barra(n, resource.getMessage("monjes.abadia.fuerza") + (int) habilidad.getValorActual()));
            } else if (habilidad.getIdHabilidad() == Constantes.HABILIDAD_CARISMA) {
                monje.setBarra_carisma(shtml.Barra(n, resource.getMessage("monjes.abadia.carisma") + (int) habilidad.getValorActual()));
            } else if (habilidad.getIdHabilidad() == Constantes.HABILIDAD_DESTREZA) {
                monje.setBarra_destreza(shtml.Barra(n, resource.getMessage("monjes.abadia.destreza") + (int) habilidad.getValorActual()));
            } else if (habilidad.getIdHabilidad() == Constantes.HABILIDAD_POPULARIDAD) {
                monje.setBarra_popularidad(shtml.Barra(n, resource.getMessage("monjes.abadia.popularidad") + (int) habilidad.getValorActual()));
            }
        }

    }


    /* Barras de HTML
    */

    public static void actualizarMiniBarrasHTML(Monje monje, MessageResources resource) throws AbadiaException {
        try {
            monje.setBarra_salud(HTML.smallBarra(Math.round((short) monje.getSalud() /
                    10), resource.getMessage("monjes.abadia.salud") + " " + Integer.toString(monje.getSalud())));
            monje.setBarra_fe(HTML.smallBarra(Math.round((short) monje.getFe() /
                    10), resource.getMessage("monjes.abadia.fe") + " " + Integer.toString(monje.getFe())));
            monje.setBarra_carisma(HTML.smallBarra(Math.round((short) monje.
                    getCarisma() / 10), resource.getMessage("monjes.abadia.carisma") + " " + Integer.toString(monje.getCarisma())));
            monje.setBarra_destreza(HTML.smallBarra(Math.round((short) monje.
                    getDestreza() / 10), resource.getMessage("monjes.abadia.destreza") + " " + Integer.toString(monje.getDestreza())));
            monje.setBarra_fuerza(HTML.smallBarra(Math.round((short) monje.
                    getFuerza() / 10), resource.getMessage("monjes.abadia.fuerza") + " " + Integer.toString(monje.getFuerza())));
            monje.setBarra_sabiduria(HTML.smallBarra(Math.round((short) monje.
                    getSabiduria() / 10), resource.getMessage("monjes.abadia.sabiduria") + " " + Integer.toString(monje.getSabiduria())));
            monje.setBarra_talento(HTML.smallBarra(Math.round((short) monje.
                    getTalento() / 10), resource.getMessage("monjes.abadia.talento") + " " + Integer.toString(monje.getTalento())));
            monje.setBarra_idioma(HTML.smallBarra(Math.round((short) monje.
                    getIdioma() / 10), resource.getMessage("monjes.abadia.idioma") + " " + Integer.toString(monje.getIdioma())));
            monje.setBarra_popularidad(HTML.smallBarra(Math.round((short) monje.
                    getPopularidad() / 10), resource.getMessage("monjes.abadia.popularidad") + " " + Integer.toString(monje.getPopularidad())));
        } catch (Exception e) {
            throw new ErrorGeneralException("adMonje. actualizarMiniBarrasHTML. Exception", e, log);
        }
    }


    public void restaurarMonjes(long abadiaid) throws AbadiaException {
        String sSQL = "update monje set ESTADO = 0, FECHA_FALLECIMIENTO is null where ABADIAID = ?";
        String sSQL2 = "UPDATE monje_alimentacion, monje SET proteinas = 30, lipidos = 30, hidratos_carbono = 30, vitaminas = 30, salud = 100 WHERE monje_alimentacion.MONJEID = monje.MONJEID AND monje.ABADIAID = ?";
        log.debug("adMonje. restaurarMonjes. " + sSQL2);
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setLong(1, abadiaid);
            ps.execute();
            ps.close();

            ps = con.prepareStatement(sSQL2);
            ps.setLong(1, abadiaid);
            ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMonje. restaurarMonjes. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public int getSiguiente(long idMonje, long idAbadia) throws AbadiaException {
        String sSQL = "Select * from monje m where m.ESTADO<>1 AND m.MONJEID > ? AND m.ABADIAID = ? order by MONJEID";
        // Buscar monje
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setLong(1, idMonje);
            ps.setLong(2, idAbadia);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("MONJEID");
            } else return 0;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMonje. getSiguiente. Error SQL", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public int getAnterior(long idMonje, long idAbadia) throws AbadiaException {
        String sSQL = "Select MONJEID from monje m where m.ESTADO<>1 AND m.MONJEID < ? AND m.ABADIAID = ? order by MONJEID desc";
        // Buscar monje
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setLong(1, idMonje);
            ps.setLong(2, idAbadia);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("MONJEID");
            } else return 0;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMonje. getSiguiente. Error SQL", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    //Elimina un monje de la base de datos
    //a partir de la clave del monje

    public void eliminarMonjesAbadia(long idAbadia) throws AbadiaSQLException {
        //actualizo a 0 el identificador de monje obispo.

        String sSQL = "Delete From `monje` Where `ABADIAID` = ?";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave del monje
            ps.setLong(1, idAbadia);
            // Ejecutarlo
            ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMonje. eliminarMonjesAbadia. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public void eliminarMonjesAbadiaCementerio(long idAbadia) throws AbadiaSQLException {
        //actualizo a 0 el identificador de monje obispo.
        String sSQL = "Delete From monje_cementerio Where ABADIAID = ?";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave del monje
            ps.setLong(1, idAbadia);
            // Ejecutarlo
            ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMonje. eliminarMonjesAbadiaCementerio. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public void eliminarMonjesAbadiaAlimentacion(long idAbadia) throws AbadiaSQLException {
        String sSQL = "Delete From monje_alimentacion Where MONJEID IN (select monjeid from monje where ABADIAID = ?)";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave del monje
            ps.setLong(1, idAbadia);
            // Ejecutarlo
            ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMonje. eliminarMonjesAbadiaAlimentacion. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /**
     * Elimina las habilidades de todos los monjes de una abadia
     *
     * @param idAbadia
     * @throws AbadiaSQLException
     */
    public void eliminarPropiedadesMonjesAbadia(long idAbadia) throws AbadiaSQLException {
        String sSQL = "Delete from habilidad_monje where monjeid in (select monjeid from monje where abadiaid = ?)";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave del monje
            ps.setLong(1, idAbadia);
            // Ejecutarlo
            ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMonje. eliminarPropiedadesMonjesAbadia. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Devuelve un {@link ArrayList} con los datos de todos los monjes de una abadia
     *
     * @param idAbadia identificador de habilidad
     * @param tipo     identificador del monje
     * @return
     * @throws org.abbatia.exception.AbadiaSQLException
     *          Excepción base de abbatia
     */
    public ArrayList<Monje> getDatosMonjesPorAbadiaH(long idAbadia, int tipo) throws AbadiaException {
        Monje monje;
        String sSQL = "Select * from monje m, monje_alimentacion ma where m.MONJEID = ma.MONJEID AND m.ABADIAID = ?";
        // Buscar monje
        if (tipo == Constantes.MONJE_VIVO) sSQL = sSQL + " and fecha_fallecimiento is null";
        if (tipo == Constantes.MONJE_MUERTO) sSQL = sSQL + " and fecha_fallecimiento is not null";
        sSQL = sSQL + " order by nombre, apellido1";

        PreparedStatement ps = null;
        ResultSet rs = null;
        adHabilidades habilidadesAD;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setLong(1, idAbadia);
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
                // Buscar la alimentación que tiene
                monje.setComeFamiliaID1(rs.getShort("COMER_FAMILIAID_1"));
                monje.setComeFamiliaID2(rs.getShort("COMER_FAMILIAID_2"));
                monje.setComeFamiliaID3(rs.getShort("COMER_FAMILIAID_3"));
                monje.setComeFamiliaID4(rs.getShort("COMER_FAMILIAID_4"));
                monje.setComeFamiliaID5(rs.getShort("COMER_FAMILIAID_5"));
                monje.setProteinas(rs.getShort("PROTEINAS"));
                monje.setLipidos(rs.getShort("LIPIDOS"));
                monje.setHidratosCarbono(rs.getShort("HIDRATOS_CARBONO"));
                monje.setVitaminas(rs.getShort("VITAMINAS"));
                monje.setFechaDeUltimaComida(rs.getString("ULTIMA_COMIDA"));
                monje.setSalud(rs.getShort("SALUD"));

                // Habilidades
                ArrayList alHabilidades;
                habilidadesAD = new adHabilidades(con);
                alHabilidades = habilidadesAD.recuperarHabilidadesMonje(monje.getIdMonje());

                Iterator habilidades = alHabilidades.iterator();
                HabilidadMonje habilidad;
                while (habilidades.hasNext()) {
                    habilidad = (HabilidadMonje) habilidades.next();
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
     * Recupera toda la informacion relativa a un monje {@link Monje}
     *
     * @param idMonje Identificador del monje
     * @return Monje
     * @throws org.abbatia.exception.AbadiaSQLException
     *          Excepción base de abbatia
     */
    public Monje recuperarDatosMonje(int idMonje) throws AbadiaException {
        Monje monje = new Monje();
        String sSQL = "Select m.MONJEID, m.ABADIAID, m.ESPECIALIZACIONID, m.JERARQUIAID, m.ESTADO, m.NOMBRE, m.APELLIDO1, m.FECHA_ENTRADA, m.FECHA_NACIMIENTO, m.FECHA_FALLECIMIENTO, ma.SALUD, m.SANTO, a.REGIONID " +
                " from monje m, monje_alimentacion ma, abadia a " +
                " where m.MONJEID = ma.MONJEID AND m.MONJEID = ? and m.abadiaid = a.abadiaid";
        // Buscar monje
        PreparedStatement ps = null;
        ResultSet rs = null;
        adHabilidades oHabilidadesAD;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setLong(1, idMonje);
            rs = ps.executeQuery();
            if (rs.next()) {
                monje.setIdMonje(rs.getInt("MONJEID"));
                monje.setIdAbadia(rs.getInt("ABADIAID"));
                monje.setIdRegion(rs.getInt("REGIONID"));
                monje.setIdDeEspecializacion(rs.getInt("ESPECIALIZACIONID"));
                monje.setIdDeJerarquia(rs.getInt("JERARQUIAID"));
                monje.setEstado(rs.getInt("ESTADO"));
                monje.setNombre(rs.getString("NOMBRE"));
                monje.setApellido1(rs.getString("APELLIDO1"));
                monje.setFechaDeEntradaEnAbadia(Utilidades.formatStringFromDB(rs.getString("FECHA_ENTRADA")));
                monje.setFechaDeNacimiento(Utilidades.formatStringFromDB(rs.getString("FECHA_NACIMIENTO")));
                monje.setFechaDeFallecimiento(Utilidades.formatStringFromDB(rs.getString("FECHA_FALLECIMIENTO")));
                monje.setEdad(CoreTiempo.getEdad(rs.getString("FECHA_NACIMIENTO")));
                monje.setSalud(rs.getShort("SALUD"));

                // Habilidades
                ArrayList alHabilidades;
                oHabilidadesAD = new adHabilidades(con);
                alHabilidades = oHabilidadesAD.recuperarHabilidadesMonje(idMonje);

                Iterator habilidades = alHabilidades.iterator();
                HabilidadMonje habilidad;
                while (habilidades.hasNext()) {
                    habilidad = (HabilidadMonje) habilidades.next();
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

                return monje;
            } else {
                throw new MonjeNoEncontradoException("No se encontro el monje: " + idMonje, log);
            }
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMonje. recuperarDatosMonje. Error SQL", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Recupera toda la informacion relativa a un la locacización de un monje {@link MonjeVisita}
     *
     * @param idMonje  Identificador del monje
     * @param resource Objeto de recursos multiidioma
     * @return MonjeVisita
     * @throws AbadiaSQLException Excepción base de abbatia
     */
    public MonjeVisita recuperarLocalizacionMonje(int idMonje, MessageResources resource) throws AbadiaException {
        MonjeVisita monjeVisita;
        Monje monje;
        adAbadia oAbadiaAD;
        adRegion oRegionAD;

        //recuperamos los datos generales del monje
        monje = recuperarDatosMonje(idMonje);
        //si el monje está de viaje hacia otra abadía o está de visita
        if (monje.getEstado() == Constantes.MONJE_VIAJE || monje.getEstado() == Constantes.MONJE_VISITA) {
            monjeVisita = recuperarDatosViaje(idMonje);
            //si no retorna nada significa que no existe registro de viaje..
            if (monjeVisita == null) {
                monjeVisita = new MonjeVisita();
                monjeVisita.setIdMonje(monje.getIdMonje());
                monjeVisita.setIdAbadia(monje.getIdAbadia());
                monjeVisita.setIdRegion(monje.getIdRegion());
            }
        } else {
            //si el monje está en su abbatia, lo datos son los suyos propios
            monjeVisita = new MonjeVisita();
            monjeVisita.setIdMonje(monje.getIdMonje());
            monjeVisita.setIdAbadia(monje.getIdAbadia());
            monjeVisita.setIdRegion(monje.getIdRegion());
        }

        oAbadiaAD = new adAbadia(con);
        monjeVisita.setNombreAbadia(oAbadiaAD.getNomAbadia(monjeVisita.getIdAbadia()));
        monjeVisita.setNombreMonje(monje.getNombre() + " " + resource.getMessage("monjes.abadia.nomciudad") + " " + monje.getApellido1());
        oRegionAD = new adRegion(con);
        monjeVisita.setNombreRegion(oRegionAD.recuperarNombreRegion(monjeVisita.getIdRegion()));

        return monjeVisita;
    }

    public MonjeVisita recuperarDatosViaje(int idMonje) throws AbadiaException {
        String sSQLViaje = "Select * from monje_visita where monjeid = ? ";
        MonjeVisita monje = null;
        ResultSet rs = null;
        PreparedStatement ps = null;

        adAbadia oAbadiaAD;

        try {
            ps = con.prepareStatement(sSQLViaje);
            ps.setInt(1, idMonje);
            rs = ps.executeQuery();
            if (rs.next()) {
                monje = new MonjeVisita();
                monje.setIdMonje(idMonje);
                //si la fecha partida origen no está informada significa que está de viaje hacia una abadía o que ya ha llegado a la abadía destino
                if (rs.getString("FECHA_PARTIDA_ORIGEN") == null) {
                    monje.setIdAbadia(rs.getInt("ABADIAID_DESTINO"));
                } else {
                    monje.setIdAbadia(rs.getInt("ABADIAID"));
                }
                oAbadiaAD = new adAbadia(con);
                monje.setIdRegion(oAbadiaAD.getRegionAbadia(monje.getIdAbadia()));
            }
            return monje;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adMonje. recuperarLocalizacionMonje. SQLException ", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public void restaurarMonje_alimentacion(Monje monje) throws AbadiaException {
        String sSQLS = "select * from monje_alimentacion where monjeid=?";
        String sSQLI = "insert into monje_alimentacion (monjeid, proteinas, lipidos, hidratos_carbono, vitaminas, salud ) values(?, 30, 30, 30, 30, 100)";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQLS);
            ps.setLong(1, monje.getIdMonje());
            rs = ps.executeQuery();
            if (!rs.next()) {
                ps = con.prepareStatement(sSQLI);
                ps.setLong(1, monje.getIdMonje());
                ps.execute();
            }
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMonje. restaurarMonje_alimentacion. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public void incrementarSalud(int idMonje, double dSalud) throws AbadiaException {
        String sSQL = "UPDATE monje_alimentacion set salud = salud + " + dSalud + " where monjeid = " + idMonje;
        adUtils utils = new adUtils(con);
        utils.execSQL(sSQL);
    }

    public void actualizarPropiedades(Monje monje) throws AbadiaException {
        String sSQL = "UPDATE monje_alimentacion set salud = " + monje.getSalud() + ", " +
                " proteinas = " + monje.getProteinas() + ", " +
                " lipidos = " + monje.getLipidos() + ", " +
                " hidratos_carbono = " + monje.getHidratosCarbono() + ", " +
                " vitaminas = " + monje.getVitaminas() +
                " where monjeid = " + monje.getIdMonje();
        adUtils utils = new adUtils(con);
        utils.execSQL(sSQL);
    }


    public ArrayList<datosMonjeActividad> recuperarActividadMonje(long MonjeID) throws AbadiaException {
        String sSQL = "Select am.actividadid, at.descripcion, am.fechaabadia, am.periodoid, am.mensaje from actividad_mensajes am, actividad_tipo at where am.MONJEID = ? and am.FECHAABADIA > ? and at.ACTIVIDADID=am.ACTIVIDADID";
        // Buscar monje
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setLong(1, MonjeID);
            ps.setString(2, CoreTiempo.getDiferenciaString(-7));
            rs = ps.executeQuery();

            ArrayList<datosMonjeActividad> listaActividad = new ArrayList<datosMonjeActividad>();
            datosMonjeActividad datos;
            while (rs.next()) {
                datos = new datosMonjeActividad();
                datos.setPeriodo(rs.getShort("periodoid"));
                datos.setTareaid(rs.getShort("actividadid"));
                datos.setMensaje(rs.getString("mensaje"));
                datos.setTarea(rs.getString("descripcion"));
                datos.setFecha(Utilidades.formatStringFromDB(rs.getString("fechaabadia")));
                listaActividad.add(datos);
            }
            return listaActividad;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adMonje. recuperarMonje. Error SQL", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public ArrayList<Monje> recuperarMonjesMuertos() throws AbadiaException {

        String sSQL = "Select * from monje m where m.FECHA_FALLECIMIENTO != null";
        // Buscar monje
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Monje> listaMonjes = new ArrayList<Monje>();
        Monje monje;
        try {
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                monje = new Monje();
                monje.setIdMonje(rs.getInt("MONJEID"));
                monje.setIdAbadia(rs.getInt("ABADIAID"));
                monje.setIdDeEspecializacion(rs.getInt("ESPECIALIZACIONID"));
                monje.setIdDeJerarquia(rs.getInt("JERARQUIAID"));
                monje.setEstado(rs.getInt("ESTADO"));
                monje.setNombre(rs.getString("NOMBRE"));
                monje.setApellido1(rs.getString("APELLIDO1"));
                monje.setFechaDeEntradaEnAbadia(Utilidades.formatStringFromDB(rs.getString("FECHA_ENTRADA")));
                monje.setFechaDeNacimiento(Utilidades.formatStringFromDB(rs.getString("FECHA_NACIMIENTO")));
                monje.setFechaDeFallecimiento(Utilidades.formatStringFromDB(rs.getString("FECHA_FALLECIMIENTO")));
                monje.setEdad(CoreTiempo.getEdad(rs.getString("FECHA_NACIMIENTO")));
                listaMonjes.add(monje);
            }
            return listaMonjes;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMonje. recuperarMonjesMuertos. Error SQL", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Recupera un ArrayList con los monejs que llevan n días muertos
     *
     * @param nDias
     * @return
     * @throws AbadiaException
     */
    public ArrayList<MonjeBase> recuperarMonjesParaOsario(int nDias) throws AbadiaException {

        String sSQL = "Select m.MONJEID, m.ABADIAID, m.JERARQUIAID, m.ESTADO, m.NOMBRE, m.APELLIDO1, u.IDIOMAID " +
                " from monje_cementerio m, abadia a, usuario u " +
                " where m.ABADIAID = a.ABADIAID and a.USUARIOID = u.USUARIOID and u.ABADIA_CONGELADA = 0 and ADDDATE(m.FECHA_FALLECIMIENTO, " + nDias + ") < DATE('" + CoreTiempo.getTiempoAbadiaString() + "') and m.estado = " + Constantes.MONJE_MUERTO + "" +
                " order by u.IDIOMAID ";
        // Buscar monje
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<MonjeBase> listaMonjes = new ArrayList<MonjeBase>();
        MonjeBase monje;
        try {
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                monje = new MonjeBase();
                monje.setIdMonje(rs.getInt("MONJEID"));
                monje.setIdAbadia(rs.getInt("ABADIAID"));
                monje.setIdDeJerarquia(rs.getInt("JERARQUIAID"));
                monje.setEstado(rs.getInt("ESTADO"));
                monje.setNombre(rs.getString("NOMBRE"));
                monje.setApellido1(rs.getString("APELLIDO1"));
                monje.setIdIdioma(rs.getInt("IDIOMAID"));
                listaMonjes.add(monje);
            }
            return listaMonjes;
        } catch (SQLException e) {
            log.error("adMonje. recuperarMonjesParaOsario. Error SQL", e);
            throw new AbadiaSQLException("adMonje. recuperarMonjesParaOsario. Error SQL", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Actualiza el estado de un monje a partir del idMonje y la abadia
     *
     * @param idMonje
     * @param idEstado
     * @throws AbadiaException
     */
    public void actualizarEstado(int idMonje, int idEstado) throws AbadiaException {
        String sSQL = "Update monje Set estado = " + idEstado + " Where MONJEID = " + idMonje;
        adUtils utils = new adUtils(con);
        utils.execSQL(sSQL);
    }


    /**
     * Devuelve un {@link ArrayList} con los datos de todos los monjes de una abadia que cumplen con los requisitos
     * para copiar un libro
     *
     * @param idAbadia identificador de habilidad
     * @param resource
     * @return
     * @throws org.abbatia.exception.AbadiaSQLException
     *          Excepción base de abbatia
     */
    public ArrayList<Monje> getMonjesParaCopiar(int idAbadia, MessageResources resource) throws AbadiaException {
        Monje monje;
        //incluir los filtros establecidos para los monjes que pueden copiar libros.
        //no deberían salir los monjes que ya tienen taréas de copia.

        String sSQL = "Select m.monjeid, m.jerarquiaid, m.nombre, m.apellido1, round(mh_fe.valor_actual) as fe, round(mh_des.valor_actual) as destreza, round(mh_tal.valor_actual) as talento, round(mh_idi.valor_actual) as idioma " +
                " from monje m, habilidad_monje mh_fe, habilidad_monje mh_des, habilidad_monje mh_tal, habilidad_monje mh_idi " +
                " where m.MONJEID = mh_fe.MONJEID and mh_fe.habilidadid = " + Constantes.HABILIDAD_FE + " and " +
                " m.MONJEID = mh_des.MONJEID and mh_des.habilidadid = " + Constantes.HABILIDAD_DESTREZA + " and " +
                " m.MONJEID = mh_tal.MONJEID and mh_tal.habilidadid = " + Constantes.HABILIDAD_TALENTO + " and " +
                " m.MONJEID = mh_idi.MONJEID and mh_idi.habilidadid = " + Constantes.HABILIDAD_IDIOMA + " and " +
                " m.ABADIAID = ? and jerarquiaid > 0 and mh_des.valor_actual > 30 and mh_tal.valor_actual > 30 and (mh_des.valor_actual + mh_tal.valor_actual > 80) and m.estado = 0 and m.jerarquiaid = " + Constantes.JERARQUIA_MONJE +
                " and m.monjeid not in (select monjeid from libro_tarea) group by monjeid ";


        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setLong(1, idAbadia);
            rs = ps.executeQuery();
            ArrayList<Monje> monjes = new ArrayList<Monje>();
            while (rs.next()) {
                monje = new Monje();
                monje.setIdMonje(rs.getInt("MONJEID"));
                monje.setIdAbadia(idAbadia);
                monje.setIdDeJerarquia(rs.getInt("JERARQUIAID"));
                monje.setNombre(rs.getString("NOMBRE"));
                monje.setApellido1(rs.getString("APELLIDO1"));

                // Habilidades
                monje.setFe(rs.getInt("FE"));
                monje.setDestreza(rs.getInt("DESTREZA"));
                monje.setTalento(rs.getInt("TALENTO"));
                monje.setIdioma(rs.getInt("IDIOMA"));

                actualizarMiniBarrasHTML(monje, resource);

                monjes.add(monje);
            }

            return monjes;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adAbadia. getMonjesParaCopiar. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Devuelve un boleano indicando si el monje tiene el periodo bloqueado copiando
     * true - periodo/monje - bloqueado
     * false - periodo/monje - no bloqueado
     *
     * @param idMonje   Identificador de monje
     * @param idPeriodo Identificador de periodo
     * @return
     * @throws AbadiaException Excepción general
     */
    public boolean getPeriodoMonjeBloqueado(int idMonje, int idPeriodo) throws AbadiaException {
        adUtils utilsAD = new adUtils(con);
        int ocupadas = utilsAD.getSQL("Select count(*) from libro_tarea where monjeid = " + idMonje + " and periodoid = " + idPeriodo, 0);

        return ocupadas == 1;
    }

    public String recuperarLinkTarea(int idMonje, int idIdioma, int idActividad, int idPeriodo) throws AbadiaException {
        String sTexto = "";
        String sGerundio = "";
        adLibros oLibroAD;
        adActividad oActividadAD;

        oActividadAD = new adActividad(con);
        //recuperamos el gerundio de la actividad
        sGerundio = oActividadAD.getGerundioActividad(idActividad, idIdioma);

        if (idActividad == Constantes.TAREA_COPIAR) {
            oLibroAD = new adLibros(con);
            int idLibro = oLibroAD.recupearLibroPorTareaMonje(idMonje, idPeriodo);
            Libro libro = oLibroAD.recuperarLibro(idLibro, idIdioma);
            sTexto = "<a href='mostrarDetalleLibro.do?clave=" + idLibro + "'>" + sGerundio + ": " + libro.getNombreLibro() + "</a>";
        }

        return sTexto;
    }

    public static void cargarDatosMonje(Monje monje, MostrarMonjeActForm monjeActForm) throws AbadiaException {
        monjeActForm.setIdMonje(monje.getIdMonje());
        monjeActForm.setNombre(monje.getNombre());
        monjeActForm.setEdad(monje.getEdad());
        monjeActForm.setComeFamiliaID1(monje.getComeFamiliaID1());
        monjeActForm.setComeFamiliaID2(monje.getComeFamiliaID2());
        monjeActForm.setComeFamiliaID3(monje.getComeFamiliaID3());
        monjeActForm.setComeFamiliaID4(monje.getComeFamiliaID4());
        monjeActForm.setComeFamiliaID5(monje.getComeFamiliaID5());

        monjeActForm.setActMaitines(monje.getActMaitines());
        monjeActForm.setActMaitines_bloqueado(monje.getActMaitines_bloqueado());
        monjeActForm.setActMaitines_link(monje.getActMaitines_link());

        monjeActForm.setActLaudes(monje.getActLaudes());
        monjeActForm.setActLaudes_bloqueado(monje.getActLaudes_bloqueado());
        monjeActForm.setActLaudes_link(monje.getActLaudes_link());

        monjeActForm.setActPrima(monje.getActPrima());
        monjeActForm.setActPrima_bloqueado(monje.getActPrima_bloqueado());
        monjeActForm.setActPrima_link(monje.getActPrima_link());

        monjeActForm.setActTercia(monje.getActTercia());
        monjeActForm.setActTercia_bloqueado(monje.getActTercia_bloqueado());
        monjeActForm.setActTercia_link(monje.getActTercia_link());

        monjeActForm.setActSexta(monje.getActSexta());
        monjeActForm.setActSexta_bloqueado(monje.getActSexta_bloqueado());
        monjeActForm.setActSexta_link(monje.getActSexta_link());

        monjeActForm.setActNona(monje.getActNona());
        monjeActForm.setActNona_bloqueado(monje.getActNona_bloqueado());
        monjeActForm.setActNona_link(monje.getActNona_link());

        monjeActForm.setActVispera(monje.getActVispera());
        monjeActForm.setActVispera_bloqueado(monje.getActVispera_bloqueado());
        monjeActForm.setActVispera_link(monje.getActVispera_link());

        monjeActForm.setActAngelus(monje.getActAngelus());
        monjeActForm.setActAngelus_bloqueado(monje.getActAngelus_bloqueado());
        monjeActForm.setActAngelus_link(monje.getActAngelus_link());

    }

    public static void cargarDatosMonjeTareaBloqueos(Monje monje, MostrarMonjeActForm monjeActForm) throws AbadiaException {
        //monjeActForm.setActMaitines(monje.getActMaitines());
        monjeActForm.setActMaitines_bloqueado(monje.getActMaitines_bloqueado());
        monjeActForm.setActMaitines_link(monje.getActMaitines_link());

        //monjeActForm.setActLaudes( monje.getActLaudes() );
        monjeActForm.setActLaudes_bloqueado(monje.getActLaudes_bloqueado());
        monjeActForm.setActLaudes_link(monje.getActLaudes_link());

        //monjeActForm.setActPrima( monje.getActPrima() );
        monjeActForm.setActPrima_bloqueado(monje.getActPrima_bloqueado());
        monjeActForm.setActPrima_link(monje.getActPrima_link());

        //monjeActForm.setActTercia( monje.getActTercia() );
        monjeActForm.setActTercia_bloqueado(monje.getActTercia_bloqueado());
        monjeActForm.setActTercia_link(monje.getActTercia_link());

        //monjeActForm.setActSexta( monje.getActSexta() );
        monjeActForm.setActSexta_bloqueado(monje.getActSexta_bloqueado());
        monjeActForm.setActSexta_link(monje.getActSexta_link());

        //monjeActForm.setActNona( monje.getActNona() );
        monjeActForm.setActNona_bloqueado(monje.getActNona_bloqueado());
        monjeActForm.setActNona_link(monje.getActNona_link());

        //monjeActForm.setActVispera( monje.getActVispera() );
        monjeActForm.setActVispera_bloqueado(monje.getActVispera_bloqueado());
        monjeActForm.setActVispera_link(monje.getActVispera_link());

        //monjeActForm.setActAngelus( monje.getActAngelus() );
        monjeActForm.setActAngelus_bloqueado(monje.getActAngelus_bloqueado());
        monjeActForm.setActAngelus_link(monje.getActAngelus_link());

    }

    public ArrayList<Monje> recuperarMonjes(int idAbadia, int tipo, MessageResources resource) throws AbadiaException {
        Monje monje;
        adEnfermedad oEnfermedadAD;
        ArrayList<Monje> alMonjes = new ArrayList<Monje>();

        String sSQL = "Select * from monje m, monje_alimentacion ma where m.MONJEID = ma.MONJEID AND m.ABADIAID = " + idAbadia;

        if (tipo == Constantes.MONJES_VIVOS)
            sSQL = sSQL + " and fecha_fallecimiento is null and estado = " + Constantes.MONJE_VIVO + " and m.monjeid not in (select monjeid from monje_enfermedad where nivel = 3) ";
        if (tipo == Constantes.MONJES_MUERTOS) sSQL = sSQL + " and fecha_fallecimiento is not null";
        if (tipo == Constantes.MONJES_CEMENTERIO)
            sSQL = "Select * from monje_cementerio m WHERE ABADIAID = " + idAbadia + " and estado = " + Constantes.MONJE_MUERTO;
        if (tipo == Constantes.MONJES_OSARIO)
            sSQL = "Select * from monje_cementerio m WHERE ABADIAID = " + idAbadia + " and estado = " + Constantes.MONJE_OSARIO;
        if (tipo == Constantes.MONJES_ENFERMOS)
            sSQL = sSQL + " and fecha_fallecimiento is null and estado = " + Constantes.MONJE_VIVO + " and m.monjeid in (select monjeid from monje_enfermedad where nivel = 3) ";
        if (tipo == Constantes.MONJES_VIAJANDO)
            sSQL = sSQL + " and fecha_fallecimiento is null and estado = " + Constantes.MONJE_VIAJE;
        if (tipo == Constantes.MONJES_VISITA)
            sSQL = sSQL + " and fecha_fallecimiento is null and estado = " + Constantes.MONJE_VISITA + " and m.monjeid not in (select monjeid from monje_enfermedad where nivel = 3) ";
        if (tipo == Constantes.MONJES_VISITA_ENFERMOS)
            sSQL = sSQL + " and fecha_fallecimiento is null and estado = " + Constantes.MONJE_VISITA + " and m.monjeid in (select monjeid from monje_enfermedad where nivel = 3) ";
        if (tipo == Constantes.MONJES_VISITA_MIABADIA)
            sSQL = " SELECT * FROM `monje_visita` mv, monje m, monje_alimentacion ma " +
                    " where  mv.abadiaid_destino = " + idAbadia + " and m.MONJEID = ma.MONJEID and m.monjeid = mv.monjeid " +
                    " and fecha_fallecimiento is null and m.estado = " + Constantes.MONJE_VISITA + " and m.monjeid not in (select monjeid from monje_enfermedad where nivel = 3) ";

        sSQL = sSQL + " ORDER BY m.Jerarquiaid desc, m.nombre, m.apellido1 ";

        Statement ps = null;
        ResultSet rs = null;
        try {
            ps = con.createStatement();//prepareStatement(sSQL);
            rs = ps.executeQuery(sSQL);
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

                oEnfermedadAD = new adEnfermedad(con);
                monje.setHTMLEnfermedad(oEnfermedadAD.getHTMLEnfermedadPorMonje(monje.getIdMonje()));

                if ((tipo != Constantes.MONJES_CEMENTERIO) && (tipo != Constantes.MONJES_OSARIO)) {
                    monje.setSalud((short) rs.getInt("SALUD"));
                    if (resource != null) {
                        monje.setBarra_salud(HTML.smallBarra(Math.round((short) monje.getSalud() / 10), resource.getMessage("monjes.abadia.salud") + Integer.toString(monje.getSalud())));
                    }
                }
                if ((tipo != Constantes.MONJES_MUERTOS) && (tipo != Constantes.MONJES_CEMENTERIO) && (tipo != Constantes.MONJES_OSARIO)) {
                    monje.setComeFamiliaID1(rs.getShort("COMER_FAMILIAID_1"));
                    monje.setComeFamiliaID2(rs.getShort("COMER_FAMILIAID_2"));
                    monje.setComeFamiliaID3(rs.getShort("COMER_FAMILIAID_3"));
                    monje.setComeFamiliaID4(rs.getShort("COMER_FAMILIAID_4"));
                    monje.setComeFamiliaID5(rs.getShort("COMER_FAMILIAID_5"));
                    monje.setProteinas(rs.getShort("PROTEINAS"));
                    monje.setLipidos(rs.getShort("LIPIDOS"));
                    monje.setHidratosCarbono(rs.getShort("HIDRATOS_CARBONO"));
                    monje.setVitaminas(rs.getShort("VITAMINAS"));
                    monje.setFechaDeUltimaComida(rs.getString("ULTIMA_COMIDA"));
                }
                alMonjes.add(monje);
            }
            return alMonjes;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adMonje. recuperarMonjes. Error SQL", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /**
     * @deprecated
     */
    public void evaluarDieta(MostrarMonjeActForm monje, Monje monje_bdd, MessageResources resource) throws AbadiaException {
        String sSQL_Alimentos = "select proteinas pro, lipidos lip, hidratos_carbono hid, vitaminas vit " +
                " from alimentos_tipo " +
                " where alimentoid in (select alimentoid from alimentos_tipo where familiaid = ?) " +
                " group by familiaid ";

        String sSQL_Tareas = "select consumo_proteinas pro, consumo_lipidos lip, consumo_hidratos hid, consumo_vitaminas vit " +
                " from actividad_tipo " +
                " where actividadid = ?";

        PreparedStatement ps = null;
        ResultSet rs = null;
        int[] familias = new int[5];
        int[] tareas = new int[8];
        double proteinas = 0, lipidos = 0, hidratos = 0, vitaminas = 0;
        String html_sube = "<img src='images/iconos/16/sube.gif' alt='" + resource.getMessage("monjes.abadia.dieta.evaluar.sube") + "' >";
        String html_baja = "<img src='images/iconos/16/baja.gif' alt='" + resource.getMessage("monjes.abadia.dieta.evaluar.baja") + "' >";
        String html_sube_mucho = "<img src='images/iconos/16/sube_mucho.gif' alt='" + resource.getMessage("monjes.abadia.dieta.evaluar.subemucho") + "' >";
        String html_baja_mucho = "<img src='images/iconos/16/baja_mucho.gif' alt='" + resource.getMessage("monjes.abadia.dieta.evaluar.bajamucho") + "' >";
        String html_igual = "<img src='images/iconos/16/igual.gif' alt='" + resource.getMessage("monjes.abadia.dieta.evaluar.igual") + "' >";
        String html_duda = "<img src='images/iconos/16/duda.gif' alt='" + resource.getMessage("monjes.abadia.dieta.evaluar.igual") + "' >";

        try {
            familias[0] = monje.getComeFamiliaID1();
            familias[1] = monje.getComeFamiliaID2();
            familias[2] = monje.getComeFamiliaID3();
            familias[3] = monje.getComeFamiliaID4();
            familias[4] = monje.getComeFamiliaID5();

            ps = con.prepareStatement(sSQL_Alimentos);

            for (int familia : familias) {
                ps.setInt(1, familia);
                rs = ps.executeQuery();
                if (rs.next()) {
                    proteinas += rs.getDouble("pro");
                    lipidos += rs.getDouble("lip");
                    hidratos += rs.getDouble("hid");
                    vitaminas += rs.getDouble("vit");
                }
            }
            ps.close();
            rs.close();


            tareas[0] = Constantes.TAREA_REZAR;
            tareas[1] = Constantes.TAREA_REZAR;
            tareas[2] = Constantes.TAREA_REZAR;
            tareas[3] = monje.getActNona();
            tareas[4] = monje.getActPrima();
            tareas[5] = Constantes.TAREA_COMER;
            tareas[6] = monje.getActTercia();
            tareas[7] = monje.getActVispera();


            ps = getConexion().prepareStatement(sSQL_Tareas);

            for (int tarea : tareas) {
                ps.setInt(1, tarea);
                rs = ps.executeQuery();
                if (rs.next()) {
                    proteinas -= rs.getDouble("pro");
                    lipidos -= rs.getDouble("lip");
                    hidratos -= rs.getDouble("hid");
                    vitaminas -= rs.getDouble("vit");
                }
            }

            rs.close();
            ps.close();

            //proteinas
            if (proteinas > 0 && proteinas <= 5) {
                monje_bdd.setImgProteinas_eval(html_sube);
            } else if (proteinas > 5) {
                monje_bdd.setImgProteinas_eval(html_sube_mucho);
            } else if (proteinas < 0 && proteinas >= -5) {
                monje_bdd.setImgProteinas_eval(html_baja);
            } else if (proteinas < -5) {
                monje_bdd.setImgProteinas_eval(html_baja_mucho);
            } else {
                monje_bdd.setImgProteinas_eval(html_igual);
            }
            //lipidos
            if (lipidos > 0 && lipidos <= 5) {
                monje_bdd.setImgLipidos_eval(html_sube);
            } else if (lipidos > 5) {
                monje_bdd.setImgLipidos_eval(html_sube_mucho);
            } else if (lipidos < 0 && lipidos >= -5) {
                monje_bdd.setImgLipidos_eval(html_baja);
            } else if (lipidos < -5) {
                monje_bdd.setImgLipidos_eval(html_baja_mucho);
            } else {
                monje_bdd.setImgLipidos_eval(html_igual);
            }

            //hidratos
            if (hidratos > 0 && hidratos <= 5) {
                monje_bdd.setImgHidratos_eval(html_sube);
            } else if (hidratos > 5) {
                monje_bdd.setImgHidratos_eval(html_sube_mucho);
            } else if (hidratos < 0 && hidratos >= -5) {
                monje_bdd.setImgHidratos_eval(html_baja);
            } else if (hidratos < -5) {
                monje_bdd.setImgHidratos_eval(html_baja_mucho);
            } else {
                monje_bdd.setImgHidratos_eval(html_igual);
            }

            //vitaminas
            if (vitaminas > 0 && vitaminas <= 5) {
                monje_bdd.setImgVitaminas_eval(html_sube);
            } else if (vitaminas > 5) {
                monje_bdd.setImgVitaminas_eval(html_sube_mucho);
            } else if (vitaminas < 0 && vitaminas >= -5) {
                monje_bdd.setImgVitaminas_eval(html_baja);
            } else if (vitaminas < -5) {
                monje_bdd.setImgVitaminas_eval(html_baja_mucho);
            } else {
                monje_bdd.setImgVitaminas_eval(html_igual);
            }

            if (monje_bdd.getProteinas() + proteinas < Constantes.VARIOS_MINIMO_ALIMENTACION ||
                    monje_bdd.getHidratosCarbono() + hidratos < Constantes.VARIOS_MINIMO_ALIMENTACION ||
                    monje_bdd.getLipidos() + lipidos < Constantes.VARIOS_MINIMO_ALIMENTACION ||
                    monje_bdd.getVitaminas() + vitaminas < Constantes.VARIOS_MINIMO_ALIMENTACION) {
                monje_bdd.setImgSalud_eval(html_baja);
            } else if (monje_bdd.getProteinas() + proteinas > Constantes.VARIOS_MAXIMO_ALIMENTACION ||
                    monje_bdd.getHidratosCarbono() + hidratos > Constantes.VARIOS_MAXIMO_ALIMENTACION ||
                    monje_bdd.getLipidos() + lipidos > Constantes.VARIOS_MAXIMO_ALIMENTACION ||
                    monje_bdd.getVitaminas() + vitaminas > Constantes.VARIOS_MAXIMO_ALIMENTACION) {
                monje_bdd.setImgSalud_eval(html_duda);
            } else if (monje_bdd.getSalud() < 100) {
                monje_bdd.setImgSalud_eval(html_sube);
            } else {
                monje_bdd.setImgSalud_eval(html_igual);
            }

        } catch (SQLException e) {
            throw new AbadiaSQLException("adMonje. evaluarDieta. Error SQL", e, log);
        } catch (Exception e) {
            throw new AbadiaSQLException("adMonje. evaluarDieta. ", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /**
     * Resta de la salud del monje el valor recibido como parametro
     *
     * @param idMonje
     * @param valor
     * @throws AbadiaException
     */
    public void restarSalud(int idMonje, double valor) throws AbadiaException {
        String sSQL = "update monje_alimentacion set salud = salud - " + valor + " where monjeid = " + idMonje;
        adUtils utils = null;
        try {
            utils = new adUtils(con);
            utils.execSQL(sSQL);
        } catch (AbadiaException e) {
            throw e;
        }
    }

    /**
     * Resta de un monje el valor de una de las propiedades
     *
     * @param idMonje
     * @param sPropiedad
     * @param dValor
     * @throws AbadiaException
     */
    public void restarPropiedad(int idMonje, String sPropiedad, double dValor) throws AbadiaException {
        String sSQL = "update monje_alimentacion set " + sPropiedad + " = " + sPropiedad + " - " + dValor + " where monjeid = " + idMonje;
        adUtils utils = null;
        try {
            utils = new adUtils(con);
            utils.execSQL(sSQL);
        } catch (AbadiaException e) {
            throw e;
        }
    }

    /**
     * Asigna un valor a una propiedad de un monje
     *
     * @param idMonje
     * @param sPropiedad
     * @param dValor
     * @throws AbadiaException
     */
    public void asignarValorPropiedad(int idMonje, String sPropiedad, double dValor) throws AbadiaException {
        String sSQL = "update monje_alimentacion set " + sPropiedad + " = " + dValor + " where monjeid = " + idMonje;
        adUtils utils = null;
        try {
            utils = new adUtils(con);
            utils.execSQL(sSQL);
        } catch (AbadiaException e) {
            throw e;
        }
    }

    /**
     * Recupera un arraylist con los límites de salud por edades de los monjes
     *
     * @return
     * @throws AbadiaException
     */
    public ArrayList<MonjeSalud> recuperarTablaEdades() throws AbadiaException {
        String sSQL = "select * from monje_edad";

        ArrayList<MonjeSalud> alListaMonjes = new ArrayList<MonjeSalud>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        MonjeSalud monje;
        try {
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                monje = new MonjeSalud();
                monje.setEdadMin(rs.getInt("edad_min"));
                monje.setEdadMax(rs.getInt("edad_max"));
                monje.setSalud(rs.getInt("salud"));
                alListaMonjes.add(monje);
            }
            return alListaMonjes;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMonje. recuperarTablaEdades. Error SQL", e, log);
        } catch (Exception e) {
            throw new AbadiaSQLException("adMonje. recuperarTablaEdades. ", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }


    }

    public ArrayList<MonjeInicio> recuperarMonjesViajando(Usuario usuario, Abadia abadia) throws AbadiaException {
        String sSQL = "SELECT m.MONJEID, m.NOMBRE, l.LITERAL, a.NOMBRE AS NOMBRE_ABADIA, mv.FECHA_LLEGADA_DESTINO, mv.FECHA_LLEGADA_ORIGEN " +
                " from monje m, literales l, jerarquia_eclesiastica j, monje_visita mv, abadia a " +
                " where mv.abadiaid_destino = a.abadiaid and m.monjeid = mv.monjeid and mv.abadiaid = ? and m.estado = " + Constantes.MONJE_VIAJE + " and m.jerarquiaid = j.jerarquiaid and j.literalid=l.literalid and l.idiomaid = " + usuario.getIdDeIdioma() +
                " order by m.jerarquiaid desc";

        PreparedStatement ps = null;
        ResultSet rs = null;
        MonjeInicio monje;
        ArrayList<MonjeInicio> alMonjes = new ArrayList<MonjeInicio>();
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, abadia.getIdDeAbadia());
            rs = ps.executeQuery();
            while (rs.next()) {
                monje = new MonjeInicio();
                monje.setIdMonje(rs.getInt("MONJEID"));
                monje.setNombreMonje(rs.getString("NOMBRE"));
                monje.setNombreAbadia(rs.getString("NOMBRE_ABADIA"));
                monje.setFechaLlegadaDestino(rs.getString("FECHA_LLEGADA_DESTINO"));
                monje.setFechaLlegadaOrigen(rs.getString("FECHA_LLEGADA_ORIGEN"));
                monje.setLiteral(rs.getString("LITERAL"));
                alMonjes.add(monje);
            }
            return alMonjes;

        } catch (Exception e) {
            throw new AbadiaSQLException("adMonje. recuperarMonjesViajando. ", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public ArrayList<MonjeInicio> recuperarMonjesInvitados(Usuario usuario, Abadia abadia) throws AbadiaException {
        return recuperarMonjesInvitados(usuario.getIdDeIdioma(), abadia.getIdDeAbadia());
    }

    public ArrayList<MonjeInicio> recuperarMonjesInvitados(int p_iIdiomaId, int p_iAbadiaId) throws AbadiaException {
        String sSQL = "SELECT m.MONJEID, m.NOMBRE, m.ABADIAID, l.LITERAL, a.NOMBRE AS NOMBRE_ABADIA " +
                " from monje m, literales l, jerarquia_eclesiastica j, monje_visita mv, abadia a " +
                " where mv.abadiaid = a.abadiaid and m.monjeid = mv.monjeid and mv.abadiaID_destino = ? " +
                " and m.estado = " + Constantes.MONJE_VISITA + " and m.jerarquiaid = j.jerarquiaid and " +
                " j.literalid=l.literalid and l.idiomaid = " + p_iIdiomaId + " order by a.NOMBRE ";

        PreparedStatement ps = null;
        ResultSet rs = null;
        MonjeInicio monje;
        ArrayList<MonjeInicio> alMonjes = new ArrayList<MonjeInicio>();
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, p_iAbadiaId);
            rs = ps.executeQuery();
            while (rs.next()) {
                monje = new MonjeInicio();
                monje.setIdMonje(rs.getInt("MONJEID"));
                monje.setIdAbadia(rs.getInt("ABADIAID"));
                monje.setNombreMonje(rs.getString("NOMBRE"));
                monje.setNombreAbadia(rs.getString("NOMBRE_ABADIA"));
                monje.setLiteral(rs.getString("LITERAL"));
                alMonjes.add(monje);
            }
            return alMonjes;

        } catch (Exception e) {
            throw new AbadiaSQLException("adMonje. recuperarMonjesInvitados. ", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public ArrayList<MonjeInicio> recuperarMonjesInicio(Usuario usuario, Abadia abadia) throws AbadiaException {
        String sSQL = "SELECT m.MONJEID, m.NOMBRE, l.LITERAL " +
                " from monje m, literales l, jerarquia_eclesiastica j " +
                " where m.estado = " + Constantes.MONJE_VIVO + " AND m.abadiaID = ? and m.jerarquiaid = j.jerarquiaid and j.literalid=l.literalid and l.idiomaid = " + usuario.getIdDeIdioma() +
                " order by m.jerarquiaid desc";

        PreparedStatement ps = null;
        ResultSet rs = null;
        MonjeInicio monje;
        ArrayList<MonjeInicio> alMonjes = new ArrayList<MonjeInicio>();
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, abadia.getIdDeAbadia());
            rs = ps.executeQuery();
            while (rs.next()) {
                monje = new MonjeInicio();
                monje.setIdMonje(rs.getInt("MONJEID"));
                monje.setNombreMonje(rs.getString("NOMBRE"));
                monje.setLiteral(rs.getString("LITERAL"));
                alMonjes.add(monje);
            }
            return alMonjes;

        } catch (Exception e) {
            throw new AbadiaSQLException("adMonje. recuperarMonjesInicio. ", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public ArrayList<MonjeInicio> recuperarMonjesVisita(Usuario usuario, Abadia abadia) throws AbadiaException {
        return recuperarMonjesVisita(usuario.getIdDeIdioma(), abadia.getIdDeAbadia());
    }

    public ArrayList<MonjeInicio> recuperarMonjesVisita(int p_iIdiomaId, int p_iAbadiaId) throws AbadiaException {
        String sSQL = "SELECT m.MONJEID, m.NOMBRE, m.ABADIAID, l.LITERAL, a.NOMBRE AS NOMBRE_ABADIA " +
                " from monje m, literales l, jerarquia_eclesiastica j, monje_visita mv, abadia a " +
                " where mv.abadiaid_destino = a.abadiaid and m.monjeid = mv.monjeid and mv.abadiaid = ? and m.estado = " + Constantes.MONJE_VISITA + " and m.jerarquiaid = j.jerarquiaid and j.literalid=l.literalid and l.idiomaid = " + p_iIdiomaId +
                " order by m.jerarquiaid desc";

        PreparedStatement ps = null;
        ResultSet rs = null;
        MonjeInicio monje;
        ArrayList<MonjeInicio> alMonjes = new ArrayList<MonjeInicio>();
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, p_iAbadiaId);
            rs = ps.executeQuery();
            while (rs.next()) {
                monje = new MonjeInicio();
                monje.setIdMonje(rs.getInt("MONJEID"));
                monje.setIdAbadia(rs.getInt("ABADIAID"));
                monje.setNombreMonje(rs.getString("NOMBRE"));
                monje.setNombreAbadia(rs.getString("NOMBRE_ABADIA"));
                monje.setLiteral(rs.getString("LITERAL"));
                alMonjes.add(monje);
            }
            return alMonjes;

        } catch (Exception e) {
            throw new AbadiaSQLException("adMonje. recuperarMonjesVisita. ", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public ArrayList<MonjeProceso> recuperarMonjesVisita(int p_iAbadiaId) throws AbadiaException {
        /*String sSQL_ori = "SELECT m.MONJEID, m.NOMBRE, l.LITERAL, a.NOMBRE AS NOMBRE_ABADIA " +
                " from monje m, literales l, jerarquia_eclesiastica j, monje_visita mv, abadia a " +
                " where mv.abadiaid_destino = a.abadiaid and m.monjeid = mv.monjeid and mv.abadiaid = ? and m.estado = " + Constantes.MONJE_VISITA + " and m.jerarquiaid = j.jerarquiaid and j.literalid=l.literalid and l.idiomaid = 1 " +
                " order by m.jerarquiaid desc";*/

        String sSQL = "SELECT monje.MONJEID, abadia.ABADIAID, usuario.IDIOMAID " +
                " FROM abadia Inner Join usuario ON abadia.USUARIOID = usuario.USUARIOID Inner Join monje ON monje.ABADIAID = abadia.ABADIAID " +
                " WHERE abadia.ABADIAID = ? AND monje.ESTADO = " + Constantes.MONJE_VISITA;

        PreparedStatement ps = null;
        ResultSet rs = null;
        MonjeProceso monje;
        ArrayList<MonjeProceso> alMonjes = new ArrayList<MonjeProceso>();
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, p_iAbadiaId);
            rs = ps.executeQuery();
            while (rs.next()) {
                monje = new MonjeProceso();
                monje.setIdMonje(rs.getInt("MONJEID"));
                monje.setIdIdioma(rs.getInt("IDIOMAID"));
                monje.setIdAbadia(rs.getInt("ABADIAID"));
                alMonjes.add(monje);
            }
            return alMonjes;

        } catch (Exception e) {
            throw new AbadiaSQLException("adMonje. recuperarMonjesVisita. ", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /**
     * Recupera un arraylist con los monjes que tienen asiganada una actividad determinada
     * en una abadia concreta.
     *
     * @param idActividad
     * @param idAbadia
     * @param resource
     * @return
     * @throws AbadiaException
     */
    public ArrayList<Monje> recuperarMonjesActividadAbadia(int idActividad, int idAbadia, MessageResources resource) throws AbadiaException {
        String sSQL = "call recuperarMonjesActividadAbadia(?,?)";
        CallableStatement cs = null;
        ResultSet rs = null;
        ArrayList<Monje> alMonjes = new ArrayList<Monje>();

        try {
            cs = con.prepareCall(sSQL);
            cs.setInt(1, idActividad);
            cs.setInt(2, idAbadia);
            cs.execute();
            rs = cs.getResultSet();

            Monje monje;
            while (rs.next()) {
                monje = new Monje();
                monje.setIdMonje(rs.getInt("MONJEID"));
                monje.setIdAbadia(rs.getInt("CODIGO_ABADIA"));
                monje.setNombre(rs.getString("NOMBRE"));
                monje.setApellido1(rs.getString("APELLIDO1"));

                monje.setFe(rs.getInt("FE"));
                monje.setDestreza(rs.getInt("DESTREZA"));
                monje.setFuerza(rs.getInt("FUERZA"));

                actualizarMiniBarrasHTML(monje, resource);

                alMonjes.add(monje);
            }

            return alMonjes;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adMonje. recuperarMonjesActividadAbadia. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(cs);
        }
    }

    /**
     * Recupera un arraylist con los monjes que tienen asiganada una actividad determinada
     * en una abadia concreta.
     *
     * @param idActividad
     * @param idPeriodo
     * @param idCampo
     * @return
     * @throws AbadiaException
     */
    public ArrayList<MonjeAgricultura> recuperarMonjesActividadCampo(int idActividad, int idPeriodo, int idCampo) throws AbadiaException {
        String sSQL = "call recuperarMonjesActividadPeriodoCampo(?,?,?)";
        CallableStatement cs = null;
        ResultSet rs = null;
        ArrayList<MonjeAgricultura> alMonjes = new ArrayList<MonjeAgricultura>();

        try {
            cs = con.prepareCall(sSQL);
            cs.setInt(1, idActividad);
            cs.setInt(2, idPeriodo);
            cs.setInt(3, idCampo);
            cs.execute();
            rs = cs.getResultSet();

            MonjeAgricultura monje = null;
            while (rs.next()) {
                monje = new MonjeAgricultura();
                monje.setIdMonje(rs.getInt("MONJEID"));
                monje.setIdAbadia(rs.getInt("idAbadia"));
                monje.setIdCampo(rs.getInt("CAMPOID"));
                monje.setIdIdioma(rs.getInt("IDIOMAID"));

                monje.setFe(rs.getInt("FE"));
                monje.setDestreza(rs.getInt("DESTREZA"));
                monje.setFuerza(rs.getInt("FUERZA"));

                alMonjes.add(monje);
            }

            return alMonjes;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. recuperarMonjesActividadCampo. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(cs);
        }
    }

    /**
     * Recupera un arraylist con los monjes que no tienen resgistro en monje_alimentacion
     * en una abadia concreta.
     *
     * @return
     * @throws AbadiaException
     */
    public ArrayList<Monje> recuperarMonjesDefectuosos() throws AbadiaException {
        String sSQL = "select monjeid from monje where monjeid not in(select monjeid from monje_alimentacion)";
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Monje> alMonjes = new ArrayList<Monje>();

        try {
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();

            Monje monje;
            while (rs.next()) {
                monje = new Monje(rs.getInt("MONJEID"));
                alMonjes.add(monje);
            }

            return alMonjes;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. recuperarMonjesDefectuosos. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Verifica si el tiempo transcurrido desde la última expulsión autoriza una nueva
     *
     * @param idAbadia
     * @return
     * @throws AbadiaException
     */
    public boolean periodoExpulsionValido(int idAbadia) throws AbadiaException {
        String sSQL = "Select datediff('" + CoreTiempo.getTiempoAbadiaString() + "',fecha_expulsion)from monje_expulsado where fecha_expulsion = (select max(fecha_expulsion) from monje_expulsado where abadiaid = " + idAbadia + ") and abadiaid = " + idAbadia + " limit 1";
        adUtils oUtilsAD;
        int iReturn;
        try {
            oUtilsAD = new adUtils(con);
            iReturn = oUtilsAD.getSQL(sSQL, 100);

            return iReturn > 24;

        } catch (AbadiaException e) {
            log.error("adMonje.periodoExpulsionValido.", e);
            throw e;
        }
    }

    /**
     * Recupera los puntos que aporta un monje al cálculo general de la puntuación
     *
     * @param idMonje
     * @return
     * @throws AbadiaException
     */
    public int calcularPuntosMonje(int idMonje) throws AbadiaException {
        adUtils oUtilsAD = null;
        String sSQL = "SELECT sum(puntuacion*valor_actual) from monje m, habilidad_tipo ht, habilidad_monje hm where m.monjeid=hm.monjeid and hm.habilidadid = ht.habilidadid and m.estado <> 1 and m.monjeid = " + idMonje;
        int iPuntos;
        try {
            oUtilsAD = new adUtils(con);
            iPuntos = oUtilsAD.getSQL(sSQL, 0);
            return iPuntos;
        } catch (AbadiaException e) {
            log.error("adMonje.calcularPuntosMonje.", e);
            throw e;
        }
    }

    /**
     * BORRAR PROPIEDADES
     * BORRAR MONJE_ALIMENTACION
     * BORRAR ACTIVIDAD_MENSAJES
     * BORRAR MENSAJES_monje
     * BORRAR ACTIVIDAD de cada monje
     *
     * @param idMonje
     * @throws AbadiaException
     */
    public void eliminarDatosMonje(int idMonje) throws AbadiaException {
        CallableStatement cs = null;
        try {
            cs = con.prepareCall("call eliminarDatosMonje(" + idMonje + ")");
            cs.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMonje.eliminarDatosMonje. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(cs);
        }

    }


    /**
     * recupera los datos del último monje expulsado a partir de una abadía.
     *
     * @param idAbadia
     * @return
     * @throws AbadiaException
     */
    public Monje recuperarUltimoMonjeExpulsado(int idAbadia) throws AbadiaException {
        String sSQL = "SELECT MONJEID, NOMBRE, APELLIDO1  " +
                " from monje_expulsado " +
                " where abadiaid = ? " +
                " order by fecha_expulsion desc" +
                " limit 1";

        PreparedStatement ps = null;
        ResultSet rs = null;
        Monje monje;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idAbadia);
            rs = ps.executeQuery();
            if (rs.next()) {
                monje = new Monje();
                monje.setIdMonje(rs.getInt("MONJEID"));
                monje.setNombre(rs.getString("NOMBRE"));
                monje.setApellido1(rs.getString("APELLIDO1"));
                return monje;
            } else {
                throw new MonjeNoEncontradoException("adMonje. recuperarUltimoMonjeExpulsado. No se encuentra el monje", log);
            }

        } catch (Exception e) {
            throw new AbadiaSQLException("adMonje. recuperarUltimoMonjeExpulsado. ", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /**
     * recupera los datos del último monje expulsado a partir de una abadía.
     *
     * @param idAbadia
     * @return
     * @throws AbadiaException
     */
    public Monje recuperarUltimoNovicioMuerto(int idAbadia) throws AbadiaException {
        String sSQL = "SELECT MONJEID, NOMBRE, APELLIDO1, FECHA_FALLECIMIENTO  " +
                " from monje_cementerio " +
                " where abadiaid = ? and jerarquiaid = ? " +
                " order by fecha_fallecimiento desc " +
                " limit 1";

        PreparedStatement ps = null;
        ResultSet rs = null;
        Monje monje;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idAbadia);
            ps.setInt(2, Constantes.JERARQUIA_NOVICIO);
            rs = ps.executeQuery();
            if (rs.next()) {
                monje = new Monje();
                monje.setIdMonje(rs.getInt("MONJEID"));
                monje.setNombre(rs.getString("NOMBRE"));
                monje.setApellido1(rs.getString("APELLIDO1"));
                monje.setFechaDeFallecimiento(rs.getString("FECHA_FALLECIMIENTO"));
                return monje;
            } else {
                return null;
            }


        } catch (Exception e) {
            throw new AbadiaSQLException("adMonje. recuperarUltimoMonjeExpulsado. ", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public boolean existeMonje(int idAbadia, int idMonje) throws AbadiaException {
        String sSQL = "select monjeid from monje where monjeid = " + idMonje + " and abadiaid = " + idAbadia;
        adUtils utils;
        boolean result;
        try {
            utils = new adUtils(con);
            result = utils.getSQL(sSQL, false);
            return result;
        } catch (AbadiaException e) {
            throw e;
        }
    }

    public int getNumMonjesVelatorio(int idAbadia) throws AbadiaException {
        String sSQL = "Select count(*) from monje where estado = " + Constantes.MONJE_MUERTO + " and abadiaid = " + idAbadia;
        adUtils utils;
        int iCount = 0;
        try {
            utils = new adUtils(con);
            iCount = utils.getSQL(sSQL, 0);
            return iCount;
        } catch (AbadiaException e) {
            log.error("adAbadia.getNumMonjesVelatorio", e);
            throw e;
        }
    }

    public int getNumMonjesCementerio(int idAbadia) throws AbadiaException {
        String sSQL = "Select count(*) from monje_cementerio where estado = " + Constantes.MONJE_MUERTO + " and abadiaid = " + idAbadia;
        adUtils utils;
        int iCount;
        try {
            utils = new adUtils(con);
            iCount = utils.getSQL(sSQL, 0);
            return iCount;
        } catch (AbadiaException e) {
            log.error("adAbadia.getNumMonjesCementerio", e);
            throw e;
        }
    }

    public int getNumMonjesOsario(int idAbadia) throws AbadiaException {
        String sSQL = "Select count(*) from monje_cementerio where estado = " + Constantes.MONJE_OSARIO + " and abadiaid = " + idAbadia;
        adUtils utils;
        int iCount;
        try {
            utils = new adUtils(con);
            iCount = utils.getSQL(sSQL, 0);
            return iCount;
        } catch (AbadiaException e) {
            log.error("adAbadia.getNumMonjesOsario", e);
            throw e;
        }
    }

    /**
     * Recupera un arraylist de monjes que tienen alguna de sus taréas sin asignar.
     *
     * @return
     * @throws AbadiaException
     */
    public ArrayList<MonjeBase> recuperarMonjesOciosos() throws AbadiaException {
        String sSQL = "call recuperarMonjesOciosos();";

        ArrayList<MonjeBase> alMonjes = new ArrayList<MonjeBase>();
        MonjeBase datos;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            cs = con.prepareCall(sSQL);
            rs = cs.executeQuery();

            while (rs.next()) {
                datos = new MonjeBase();
                datos.setIdMonje(rs.getInt("MONJEID"));
                datos.setIdAbadia(rs.getInt("ABADIAID"));
                datos.setIdIdioma(rs.getInt("IDIOMAID"));
                datos.setNombre(rs.getString("NOMBRE"));
                datos.setApellido1(rs.getString("APELLIDO1"));
                alMonjes.add(datos);
            }
            return alMonjes;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adMonje. recuperarMonjesOciosos. Error SQL", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(cs);
        }
    }

    public ArrayList<Monje> recuperarMonjesPorAbadia(int idAbadia, MessageResources resource) throws AbadiaException {
        String sTrace = this.getClass() + ".recuperarMonjesPorAbadia()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        ArrayList<Monje> alMonjesVivos;
        ArrayList<Monje> alMonjesVisita;
        ArrayList<Monje> alMonjes;
        Iterator<Monje> itMonjesVisita;

        try {
            alMonjesVivos = recuperarMonjes(idAbadia, Constantes.MONJES_VIVOS, resource);
            alMonjesVisita = recuperarMonjes(idAbadia, Constantes.MONJES_VISITA_MIABADIA, resource);

            alMonjes = alMonjesVivos;
            itMonjesVisita = alMonjesVisita.iterator();
            while (itMonjesVisita.hasNext()) {
                alMonjes.add(itMonjesVisita.next());
            }

            return alMonjes;

        } finally {
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public void restarSaludPorEdad(MonjeSalud p_oMonje) throws AbadiaException {
        String sSQL = "call modificarSaludMonjesPorEdad(?,?,?,?)";

        CallableStatement cs = null;
        int anoAbadia;

        try {
            anoAbadia = CoreTiempo.getAnoEnCurso();

            cs = con.prepareCall(sSQL);

            cs.setInt(1, p_oMonje.getSalud());
            cs.setInt(2, p_oMonje.getEdadMin());
            cs.setInt(3, p_oMonje.getEdadMax());
            cs.setInt(4, anoAbadia);
            cs.execute();

        } catch (SQLException e) {
            //log.error("adMonje. recuperarMonje. Error SQL", e);
            throw new AbadiaSQLException("adMonje. restarSaludPorEdad. Error SQL", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(cs);
        }

    }

}
