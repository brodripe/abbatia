package org.abbatia.process.adbean;

import org.abbatia.adbean.adLibros;
import org.abbatia.adbean.adRecurso;
import org.abbatia.adbean.adUtils;
import org.abbatia.adbean.base.adbeans;
import org.abbatia.bbean.singleton.CargasInicialesDietasBBean;
import org.abbatia.bean.*;
import org.abbatia.core.CoreTiempo;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.Utilidades;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * Created by IntelliJ IDEA.
 * User: Benjamin
 * Date: 20-sep-2004
 * Time: 22:04:44
 * To change this template use File | Settings | File Templates.
 */

public class adProcesos extends adbeans {
    private static Logger log = Logger.getLogger(adProcesos.class.getName());

    /**
     * Instancia la clase sin obtener una nueva conexión
     *
     * @param con
     * @throws AbadiaException
     */
    public adProcesos(Connection con) throws AbadiaException {
        super(con);
    }


    /* Añadir un mensajes de procesos
    */

    public void setActividadMens(int idmonje, int idactividad, int idperiodo, String mensaje, double cantidad) throws AbadiaException {

        // Comprobar si existe un usuario con el mismo nick
        String sSQL = "INSERT into actividad_mensajes  ( monjeid, actividadid, periodoid, fechaabadia, fechareal, mensaje, cantidad ) " +
                "VALUES ( ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = getConexion().prepareStatement(sSQL);
            ps.setInt(1, idmonje);
            ps.setInt(2, idactividad);
            ps.setInt(3, idperiodo);
            ps.setString(4, CoreTiempo.getTiempoAbadiaStringConHoras());
            ps.setString(5, CoreTiempo.getTiempoRealStringConHoras());
            ps.setString(6, mensaje);
            ps.setDouble(7, cantidad);
            // Ejecutarlo
            ps.execute();

        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. setActividadMens. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }


    /**
     * Enterrar monjes
     * BORRAR PROPIEDADES
     * BORRAR MONJE_ALIMENTACION
     * BORRAR ACTIVIDAD_MENSAJES
     * BORRAR MENSAJES_monje
     * BORRAR ACTIVIDAD de cada monje
     *
     * @throws AbadiaException
     */
    public void eliminarDatosMonjesMuertos() throws AbadiaException {
        CallableStatement cs = null;
        try {
            cs = getConexion().prepareCall("call eliminarDatosMonjesMuertos()");
            cs.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. eliminarDatosMonjesMuertos. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(cs);
        }

    }

    public void eliminarAnimalesMuertos() throws AbadiaException {
        //BORRAR ANIMALES
        //String sSQL = "Delete FROM animales Where FECHA_FALLECIMIENTO is not null and estado != 0";

        //PreparedStatement ps = null;
        CallableStatement cs = null;
        try {
            cs = getConexion().prepareCall("{call eliminarAnimalesMuertos()}");
            //BORRAR Animales
            //ps = (PreparedStatement) con.prepareStatement(sSQL);
            cs.execute();

        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. eliminarAnimalesMuertos. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(cs);
        }

    }

    public void eliminarMensajesAntiguos(int iDiasAbbatia) throws AbadiaException {
        //BORRAR ANIMALES
        String sSQL = "DELETE FROM mensajes WHERE fechaabadia < ?";

        PreparedStatement ps = null;
        try {
            //BORRAR Animales
            ps = (PreparedStatement) con.prepareStatement(sSQL);
            String fechaAbbatia = CoreTiempo.getDiferenciaStringConHoras(iDiasAbbatia);
            ps.setString(1, fechaAbbatia);
            ps.execute();

        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. eliminarMensajesAntiguos. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public void eliminarRankingAntiguo(int iDias) throws AbadiaException {
        String sSQL = "DELETE FROM abadia_puntuacion WHERE fecha_real < date_add(now(), interval ? day)";

        PreparedStatement ps = null;
        try {
            //BORRAR Animales
            ps = (PreparedStatement) con.prepareStatement(sSQL);
            ps.setInt(1, iDias);
            ps.execute();

        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. eliminarRankingAntiguo. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    //se eliminan los registros de elaboracion que estén finaliados hace mas de x dias abbatia

    public void eliminarElaboracionesFinalizadas(int iDiasAbbatia) throws AbadiaException {
        //BORRAR ANIMALES
        String sSQL = "DELETE FROM elaboracion_alimentos WHERE fecha_fin < ? and estado = " + Constantes.ESTADO_ELABORACION_FINALIZADO;

        PreparedStatement ps = null;
        try {
            //BORRAR Animales
            ps = getConexion().prepareStatement(sSQL);
            String fechaAbbatia = CoreTiempo.getDiferenciaStringConHoras(iDiasAbbatia);
            ps.setString(1, fechaAbbatia);
            ps.execute();

        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. eliminarElaboracionesFinalizadas. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    // dejar sólo 7 mensajes en base de datos

    public void eliminarMensajesActividadesAntiguos(int iDiasAbbatia) throws AbadiaException {
        //BORRAR ANIMALES
        String sSQL = "DELETE FROM actividad_mensajes WHERE fechaabadia < ?";

        PreparedStatement ps = null;
        try {
            //BORRAR Animales
            ps = getConexion().prepareStatement(sSQL);
            String fechaAbbatia = CoreTiempo.getDiferenciaStringConHoras(iDiasAbbatia);
            ps.setString(1, fechaAbbatia);
            ps.execute();

        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. eliminarMensajesAntiguos. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    // Recupera los animales de una abbatia que tienen que cambiar de nivel

    public ArrayList<Animal> recuperarAnimalesNivel() throws AbadiaException {
        String sSQL = "SELECT e.abadiaid, a.animalid, a.tipo_animalid, a.nivel, fecha_nacimiento, descripcion, TO_DAYS(fecha_nacimiento) + (mes_cambio_nivel * 30) cambio, To_Days( ? ) dias " +
                "FROM `edificio` e, `animales` a, `animales_crecimiento` ac " +
                "where e.edificioid = a.edificioid and a.tipo_animalid = ac.tipo_animalid and a.nivel = ac.nivel and a.estado=0 " +
                "and TO_DAYS(fecha_nacimiento) + (mes_cambio_nivel * 30) <= To_Days( ? )";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = getConexion().prepareStatement(sSQL);
            ps.setString(1, CoreTiempo.getTiempoAbadiaString());
            ps.setString(2, CoreTiempo.getTiempoAbadiaString());
            rs = ps.executeQuery(); // animales que deberían de pasar de nivel
            Animal animal = null;
            ArrayList<Animal> animales = new ArrayList<Animal>();
            while (rs.next()) {
                animal = new Animal();
                animal.setAbadiaid(rs.getInt("abadiaid"));
                animal.setTipoAnimalid(rs.getInt("tipo_animalid"));
                animal.setNivel(rs.getShort("nivel"));
                animal.setAnimalid(rs.getInt("animalid"));
                animal.setNombre(rs.getString("Descripcion"));
                animales.add(animal);
            }
            return animales;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. recuperarAnimalesNivel. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    // Recupera los animales del Mercado que tienen que cambiar de nivel

    public ArrayList<Animal> recuperarAnimalesMercadoNivel() throws AbadiaException {
        /*
        String sSQL = "SELECT m.abadiaid,  m.productoid,  a.tipo_animalid, a.nivel, a.fecha_nacimiento, descripcion, TO_DAYS(fecha_nacimiento) + (mes_cambio_nivel * 30) cambio, To_Days( ? ) dias " +
        "FROM `mercados` m, `mercados_animales` a, `animales_crecimiento` ac " +
        "where m.mercancia='N' and m.productoid = a.productoid and a.tipo_animalid = ac.tipo_animalid and a.nivel = ac.nivel and m.estado = 0 and m.abadiaid <> 0 " +
        "and TO_DAYS(fecha_nacimiento) + (mes_cambio_nivel * 30) <= To_Days( ? )";
        */
        //PreparedStatement ps = null;
        ResultSet rs = null;
        CallableStatement cs = null;
        try {
            cs = getConexion().prepareCall("{call recuperarAnimalesMercadoNivel(?)}");
            cs.setString(1, CoreTiempo.getTiempoAbadiaString());
            //ps = getConexion().prepareStatement(sSQL);
            //ps.setString(1, CoreTiempo.getTiempoAbadiaString());
            //ps.setString(2, CoreTiempo.getTiempoAbadiaString());
            cs.execute(); // animales que deberían de pasar de nivel
            rs = cs.getResultSet();
            Animal animal = null;
            ArrayList<Animal> animales = new ArrayList<Animal>();
            while (rs.next()) {
                animal = new Animal();
                animal.setAbadiaid(rs.getInt("abadiaid"));
                animal.setProductoid(rs.getInt("productoid"));
                animal.setTipoAnimalid(rs.getInt("tipo_animalid"));
                animal.setNivel(rs.getShort("nivel"));
                animal.setNombre(rs.getString("Descripcion"));
                animales.add(animal);
            }
            return animales;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. recuperarAnimalesMercadoNivel. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(cs);
        }

    }

    public int recuperarIdEdificioPorFamiliaAlimento(int idProducto, int idAbadia) throws AbadiaException {
        //Definición de cadena sql de consulta

        String sSQL = "SELECT e.* from edificio e, alimentos_familia af, alimentos_tipo at " +
                "WHERE e.TIPOEDIFICIOID = af.TIPOEDIFICIOID and af.familiaid = at.familiaid and " +
                "at.ALIMENTOID=? and e.ABADIAID=? and e.nivel > 0";


        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = getConexion().prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave.
            int parNo = 1;
            ps.setLong(parNo++, idProducto);
            ps.setInt(parNo++, idAbadia);
            //Lanzo la consulta y cargo el resultado en un resultset
            rs = ps.executeQuery();
            //si la consulta encuentra la edificio....
            if (rs.next()) {
                return rs.getInt("EDIFICIOID");
            }
            //si no existe el edificio correspondiente, devolvemo el id de la cocina.
            return recuperarIdEdificioTipo(Constantes.EDIFICIO_COCINA, idAbadia);
        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. recuperarIdEdificioPorFamiliaAlimento. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public int recuperarIdEdificioTipo(long idDeTipoEdificioTmp, int idAbadia) throws AbadiaException {
        //Definición de cadena sql de consulta
        String sSQL = "Select * from edificio where ABADIAID = ? AND TIPOEDIFICIOID = ?";
        //creo un objeto de tipo Edificio
        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = getConexion().prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave.

            int parNo = 1;
            ps.setLong(parNo++, idAbadia);
            ps.setLong(parNo++, idDeTipoEdificioTmp);
            //Lanzo la consulta y cargo el resultado en un resultset
            rs = (ResultSet) ps.executeQuery();
            //si la consulta encuentra la edificio....
            if (rs.next()) {
                return rs.getInt("EDIFICIOID");
            } else return 0;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. recuperarIdEdificioTipo. SQLException ", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
        //si edificio no se localiza, devolveremos null
    }

    /**
     * REcupera un arraylist con los monjes que estan en su abadia para alimentacion
     *
     * @return
     * @throws AbadiaException
     */
    public ArrayList<Monje> getMonjesPrAlimentacion() throws AbadiaException {

        String sSQLMonjes = "SELECT ma.*, m.*, a.abadiaid, u.idiomaid, e.nivel as NIVEL_ABADIA, " +
                " A_MAITINES.ACTIVIDADID AS ACTIVIDAD_MAITINES, " +
                " A_LAUDES.ACTIVIDADID AS ACTIVIDAD_LAUDES, " +
                " A_PRIMA.ACTIVIDADID AS ACTIVIDAD_PRIMA, " +
                " A_TERCIA.ACTIVIDADID AS ACTIVIDAD_TERCIA, " +
                " A_SEXTA.ACTIVIDADID AS ACTIVIDAD_SEXTA, " +
                " A_NONA.ACTIVIDADID AS ACTIVIDAD_NONA, " +
                " A_VISPERAS.ACTIVIDADID AS ACTIVIDAD_VISPERAS, " +
                " A_ANGELUS.ACTIVIDADID AS ACTIVIDAD_ANGELUS  " +
                " FROM monje_alimentacion ma, monje m, abadia a, usuario u, edificio e, " +
                "   actividad AS A_MAITINES, actividad AS A_LAUDES, actividad AS A_PRIMA, " +
                "   actividad AS A_TERCIA, actividad AS A_SEXTA, actividad AS A_NONA, " +
                "   actividad AS A_VISPERAS, actividad AS A_ANGELUS " +
                " WHERE m.estado = 0 and u.abadia_congelada=0 and ma.monjeid = m.monjeid and " +
                "      m.abadiaid = a.abadiaid and a.usuarioid = u.usuarioid  and " +
                "      e.abadiaid = a.abadiaid and e.tipoedificioid = 99 and m.monjeid not in (select monjeid from monje_enfermedad where nivel = 3) " +
                "      and m.monjeid = A_MAITINES.monjeid and " +
                "      m.monjeid = A_LAUDES.monjeid and " +
                "      m.monjeid = A_PRIMA.monjeid and " +
                "      m.monjeid = A_TERCIA.monjeid and " +
                "      m.monjeid = A_SEXTA.monjeid and " +
                "      m.monjeid = A_NONA.monjeid and " +
                "      m.monjeid = A_VISPERAS.monjeid and " +
                "      m.monjeid = A_ANGELUS.monjeid and " +
                "      A_MAITINES.periodoid = 1 and " +
                "      A_LAUDES.periodoid = 2 and " +
                "      A_PRIMA.periodoid = 3 and " +
                "      A_TERCIA.periodoid = 4 and " +
                "      A_SEXTA.periodoid = 5 and " +
                "      A_NONA.periodoid = 6 and " +
                "      A_VISPERAS.periodoid = 7 and " +
                "      A_ANGELUS.periodoid = 8      " +
                " ORDER BY u.idiomaid, a.abadiaid";


/*
        String sSQLMonjes = "SELECT ma.*, m.*, a.abadiaid, u.idiomaid, e.nivel as NIVEL_ABADIA " +
                "FROM `monje_alimentacion` ma, `monje` m, `abadia` a, `usuario` u, edificio e " +
                "WHERE m.estado = " + Constantes.MONJE_VIVO + " and u.abadia_congelada=0 and ma.monjeid = m.monjeid and " +
                "   m.abadiaid = a.abadiaid and a.usuarioid = u.usuarioid  and " +
                "   e.abadiaid = a.abadiaid and e.tipoedificioid = 99 and m.monjeid not in (select monjeid from monje_enfermedad where nivel = 3) " +
                "ORDER BY u.idiomaid, a.abadiaid";
*/

        ArrayList<Monje> listaMonjes = new ArrayList<Monje>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // Monjes en las abadias
            ps = con.prepareStatement(sSQLMonjes);
            rs = ps.executeQuery();
            Monje monje = null;
            while (rs.next()) {
                monje = new Monje();
                monje.setIdDeMonje(rs.getInt("MONJEID"));
                monje.setIdDeAbadia(rs.getInt("ABADIAID"));
                monje.setIdDeEspecializacion(rs.getInt("ESPECIALIZACIONID"));
                monje.setIdDeJerarquia(rs.getInt("JERARQUIAID"));
                monje.setNombre(rs.getString("NOMBRE"));
                monje.setPrimerApellido(rs.getString("APELLIDO1"));
                monje.setFechaDeEntradaEnAbadia(rs.getString("FECHA_ENTRADA"));
                monje.setFechaDeNacimiento(Utilidades.formatStringFromDB(rs.getString("FECHA_NACIMIENTO")));
                monje.setFechaDeFallecimiento(Utilidades.formatStringFromDB(rs.getString("FECHA_FALLECIMIENTO")));
                monje.setEdad(CoreTiempo.getEdad(rs.getString("FECHA_NACIMIENTO")));
                monje.setEstado(rs.getShort("ESTADO"));
                monje.setNivelAbadia(rs.getShort("NIVEL_ABADIA"));
                // Buscar la alimentaci?n que tiene
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
                // Proteinas...
                monje.setProteinas(rs.getShort("PROTEINAS"));
                monje.setLipidos(rs.getShort("LIPIDOS"));
                monje.setHidratosCarbono(rs.getShort("HIDRATOS_CARBONO"));
                monje.setVitaminas(rs.getShort("VITAMINAS"));
                monje.setAnt_proteinas(rs.getShort("ANT_PROTEINAS"));
                monje.setAnt_lipidos(rs.getShort("ANT_LIPIDOS"));
                monje.setAnt_hidratosCarbono(rs.getShort("ANT_HIDRATOS_CARBONO"));
                monje.setAnt_vitaminas(rs.getShort("ANT_VITAMINAS"));
                monje.setFechaDeUltimaComida(rs.getString("ULTIMA_COMIDA"));
                monje.setSalud(rs.getShort("SALUD"));
                monje.setActMaitines(rs.getShort("ACTIVIDAD_MAITINES"));
                monje.setActLaudes(rs.getShort("ACTIVIDAD_LAUDES"));
                monje.setActPrima(rs.getShort("ACTIVIDAD_PRIMA"));
                monje.setActTercia(rs.getShort("ACTIVIDAD_TERCIA"));
                monje.setActSexta(rs.getShort("ACTIVIDAD_SEXTA"));
                monje.setActNona(rs.getShort("ACTIVIDAD_NONA"));
                monje.setActVispera(rs.getShort("ACTIVIDAD_VISPERAS"));
                monje.setActAngelus(rs.getShort("ACTIVIDAD_ANGELUS"));
                listaMonjes.add(monje);
            }

            return listaMonjes;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adAbadia. getMonjesPrAlimentacion. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Devuelve un arraylist con los datos de los monjes que estan de visita para procesar su alimentacion
     *
     * @return
     * @throws AbadiaException
     */
    public ArrayList<Monje> getMonjesPrAlimentacionVisita() throws AbadiaException {

        // Monjes de visita
        String sSQLMonjesVisita = "SELECT ma.*, m.*, a.abadiaid, u.idiomaid, mv.abadiaid_destino, e.nivel as NIVEL_ABADIA, " +
                "                 A_MAITINES.ACTIVIDADID AS ACTIVIDAD_MAITINES, " +
                "                 A_LAUDES.ACTIVIDADID AS ACTIVIDAD_LAUDES, " +
                "                 A_PRIMA.ACTIVIDADID AS ACTIVIDAD_PRIMA, " +
                "                 A_TERCIA.ACTIVIDADID AS ACTIVIDAD_TERCIA, " +
                "                 A_SEXTA.ACTIVIDADID AS ACTIVIDAD_SEXTA, " +
                "                 A_NONA.ACTIVIDADID AS ACTIVIDAD_NONA, " +
                "                 A_VISPERAS.ACTIVIDADID AS ACTIVIDAD_VISPERAS, " +
                "                 A_ANGELUS.ACTIVIDADID AS ACTIVIDAD_ANGELUS   " +
                "            FROM `monje_alimentacion` ma, `monje` m, monje_visita mv, `abadia` a, `usuario` u, edificio e," +
                "                  actividad AS A_MAITINES, actividad AS A_LAUDES, actividad AS A_PRIMA, actividad AS A_TERCIA, " +
                "                  actividad AS A_SEXTA, actividad AS A_NONA, actividad AS A_VISPERAS, actividad AS A_ANGELUS  " +
                "            WHERE m.estado = " + Constantes.MONJE_VISITA + " and u.abadia_congelada=0 and " +
                "            mv.monjeid = m.monjeid and ma.monjeid = m.monjeid and mv.abadiaid_destino = a.abadiaid and " +
                "            a.usuarioid = u.usuarioid  and a.abadiaid = e.abadiaid and e.tipoedificioid=99 and " +
                "            m.monjeid not in (select monjeid from monje_enfermedad where nivel = 3) " +
                "            and m.monjeid = A_MAITINES.monjeid and " +
                "            m.monjeid = A_LAUDES.monjeid and " +
                "            m.monjeid = A_PRIMA.monjeid and " +
                "            m.monjeid = A_TERCIA.monjeid and " +
                "            m.monjeid = A_SEXTA.monjeid and " +
                "            m.monjeid = A_NONA.monjeid and " +
                "            m.monjeid = A_VISPERAS.monjeid and " +
                "            m.monjeid = A_ANGELUS.monjeid and " +
                "            A_MAITINES.periodoid = 1 and " +
                "            A_LAUDES.periodoid = 2 and " +
                "            A_PRIMA.periodoid = 3 and " +
                "            A_TERCIA.periodoid = 4 and " +
                "            A_SEXTA.periodoid = 5 and " +
                "            A_NONA.periodoid = 6 and " +
                "            A_VISPERAS.periodoid = 7 and " +
                "            A_ANGELUS.periodoid = 8      " +
                "            ORDER BY u.idiomaid, a.abadiaid ";

        ArrayList<Monje> listaMonjes = new ArrayList<Monje>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // Monjes en las abadias que estan de visita
            ps = con.prepareStatement(sSQLMonjesVisita);
            rs = ps.executeQuery();
            Monje monje;
            while (rs.next()) {
                monje = new Monje();
                monje.setIdDeMonje(rs.getInt("MONJEID"));
                monje.setIdDeAbadia(rs.getInt("ABADIAID_DESTINO"));
                monje.setIdDeEspecializacion(rs.getInt("ESPECIALIZACIONID"));
                monje.setIdDeJerarquia(rs.getInt("JERARQUIAID"));
                monje.setNombre(rs.getString("NOMBRE"));
                monje.setPrimerApellido(rs.getString("APELLIDO1"));
                monje.setFechaDeEntradaEnAbadia(rs.getString("FECHA_ENTRADA"));
                monje.setFechaDeNacimiento(Utilidades.formatStringFromDB(rs.getString("FECHA_NACIMIENTO")));
                monje.setFechaDeFallecimiento(Utilidades.formatStringFromDB(rs.getString("FECHA_FALLECIMIENTO")));
                monje.setEdad(CoreTiempo.getEdad(rs.getString("FECHA_NACIMIENTO")));
                monje.setEstado(rs.getShort("ESTADO"));
                monje.setNivelAbadia(rs.getShort("NIVEL_ABADIA"));
                monje.setIdDeAbadia_Visita(rs.getInt("abadiaid_destino"));
                // Buscar la alimentaci?n que tiene
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
                // Proteinas...
                monje.setProteinas(rs.getShort("PROTEINAS"));
                monje.setLipidos(rs.getShort("LIPIDOS"));
                monje.setHidratosCarbono(rs.getShort("HIDRATOS_CARBONO"));
                monje.setVitaminas(rs.getShort("VITAMINAS"));
                monje.setAnt_proteinas(rs.getShort("ANT_PROTEINAS"));
                monje.setAnt_lipidos(rs.getShort("ANT_LIPIDOS"));
                monje.setAnt_hidratosCarbono(rs.getShort("ANT_HIDRATOS_CARBONO"));
                monje.setAnt_vitaminas(rs.getShort("ANT_VITAMINAS"));
                monje.setFechaDeUltimaComida(rs.getString("ULTIMA_COMIDA"));
                monje.setSalud(rs.getShort("SALUD"));
                monje.setActMaitines(rs.getShort("ACTIVIDAD_MAITINES"));
                monje.setActLaudes(rs.getShort("ACTIVIDAD_LAUDES"));
                monje.setActPrima(rs.getShort("ACTIVIDAD_PRIMA"));
                monje.setActTercia(rs.getShort("ACTIVIDAD_TERCIA"));
                monje.setActSexta(rs.getShort("ACTIVIDAD_SEXTA"));
                monje.setActNona(rs.getShort("ACTIVIDAD_NONA"));
                monje.setActVispera(rs.getShort("ACTIVIDAD_VISPERAS"));
                monje.setActAngelus(rs.getShort("ACTIVIDAD_ANGELUS"));
                listaMonjes.add(monje);
            }

            return listaMonjes;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adAbadia. getMonjesPrAlimentacionVisita. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    // Se recupera una lista de abadias ordenada por idioma para procesar en follamiento animales

    public ArrayList<AbadiaIdioma> recuperarAbadiasAnimalesMachosIdioma(short aislado) throws AbadiaException {
        // Abadias que tienen animales vivos y que sean machos.... suino no se puede fecundar
        String sSQL = "SELECT u.idiomaid, e.abadiaid, e.edificioid, l.literal " +
                " FROM `abadia` a, `usuario` u, edificio e , animales an, animales_tipo at, edificio_tipo et, literales l " +
                " where a.usuarioid = u.usuarioid and " +
                " l.idiomaid = u.idiomaid and " +
                " l.literalid = et.nombre and " +
                " e.tipoedificioid = et.tipoedificioid and " +
                " u.abadia_congelada = 0 and " +
                " e.edificioid = an.edificioid and " +
                " e.abadiaid = a.abadiaid and " +
                " an.aislado = " + aislado + " and " +
                " an.estado = 0 and an.nivel between 3 and 4 and an.tipo_animalid = at.tipo_animalid and at.hembra = 0 " +
                " group by u.idiomaid, e.abadiaid, e.edificioid ";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            AbadiaIdioma abadia = null;
            ArrayList<AbadiaIdioma> abadias = new ArrayList<AbadiaIdioma>();
            while (rs.next()) {
                abadia = new AbadiaIdioma();
                abadia.setIdAbadia(rs.getInt("ABADIAID"));
                abadia.setIdIdioma(rs.getInt("IDIOMAID"));
                abadia.setIdEdificio(rs.getInt("EDIFICIOID"));
                abadia.setNombreEdificio(rs.getString("LITERAL"));
                abadias.add(abadia);
            }
            return abadias;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. recuperarAbadiasAnimalesMachosIdioma. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public ArrayList<AnimalProceso> recuperarHembras(int idEdificio, short aislado) throws AbadiaException {
        // Abadias que tienen animales vivos
        String sSQL = "SELECT AddDate(?, interval tiempo_dias_embarazo DAY) as embarazo, e.abadiaid, a.animalid, ac.tipo_animalid_masculino, at.descripcion, a.salud " +
                "FROM `animales_crias` ac, `animales` a, `animales_tipo` at, `edificio` e " +
                "WHERE e.edificioid = a.edificioid and at.tipo_animalid = a.tipo_animalid and " +
                "( a.tipo_animalid = ac.tipo_animalid_femenino and a.nivel >= ac.nivel_min_femenino) and a.estado=0 and a.fecha_embarazo is null and a.salud > 40 and " +
                "((fecha_parido is null) or (To_Days(fecha_parido)+Tiempo_Dias_crias < To_Days(?))) " +
                "and e.edificioid = ? and aislado = ? ";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {

            ps = (PreparedStatement) getConexion().prepareStatement(sSQL);
            ps.setString(1, CoreTiempo.getTiempoAbadiaString());
            ps.setString(2, CoreTiempo.getTiempoAbadiaString());
            ps.setInt(3, idEdificio);
            ps.setShort(4, aislado);
            rs = (ResultSet) ps.executeQuery();
            AnimalProceso animal = null;
            ArrayList<AnimalProceso> animales = new ArrayList<AnimalProceso>();
            while (rs.next()) {
                animal = new AnimalProceso();
                animal.setIdAnimal(rs.getInt("animalid"));
                animal.setTipoAnimalIdMasc(rs.getInt("tipo_animalid_masculino"));
                animal.setDescripcion(rs.getString("descripcion"));
                animal.setFecha_parir(rs.getString("embarazo"));
                animal.setSalud(rs.getInt("salud"));
                animales.add(animal);
            }
            return animales;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. recuperarHembras. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public HashMap<Integer, ArrayList<AnimalProceso>> recuperarHembras(short aislado) throws AbadiaException {
        // Abadias que tienen animales vivos
        String sSQL = "SELECT AddDate(?, interval tiempo_dias_embarazo DAY) as embarazo, e.abadiaid, a.animalid, ac.tipo_animalid_masculino, at.descripcion, a.salud, e.edificioid " +
                "FROM `animales_crias` ac, `animales` a, `animales_tipo` at, `edificio` e " +
                "WHERE e.edificioid = a.edificioid and at.tipo_animalid = a.tipo_animalid and " +
                "( a.tipo_animalid = ac.tipo_animalid_femenino and a.nivel >= ac.nivel_min_femenino) and a.estado=0 and a.fecha_embarazo is null and a.salud > 40 and " +
                "((fecha_parido is null) or (To_Days(fecha_parido)+Tiempo_Dias_crias < To_Days(?))) " +
                " and aislado = ? " +
                "ORDER BY a.edificioid";
        HashMap<Integer, ArrayList<AnimalProceso>> hmHembras = new HashMap<Integer, ArrayList<AnimalProceso>>();
        AnimalProceso animal = null;
        ArrayList<AnimalProceso> alAnimales = new ArrayList<AnimalProceso>();

        int idEdificio = 0;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = (PreparedStatement) getConexion().prepareStatement(sSQL);
            ps.setString(1, CoreTiempo.getTiempoAbadiaString());
            ps.setString(2, CoreTiempo.getTiempoAbadiaString());
            ps.setShort(3, aislado);
            rs = (ResultSet) ps.executeQuery();
            while (rs.next()) {
                animal = new AnimalProceso();
                animal.setIdAnimal(rs.getInt("animalid"));
                animal.setIdEdificio(rs.getInt("edificioid"));
                animal.setTipoAnimalIdMasc(rs.getInt("tipo_animalid_masculino"));
                animal.setDescripcion(rs.getString("descripcion"));
                animal.setFecha_parir(rs.getString("embarazo"));
                animal.setSalud(rs.getInt("salud"));
                //si hay cambio de edificio
                if (animal.getIdEdificio() != idEdificio) {
                    //si no es la primera pasada
                    if (idEdificio != 0) {
                        //cargamos el arraylist en la hash
                        hmHembras.put(idEdificio, alAnimales);
                        //inicializamos el ArrayList
                        alAnimales = new ArrayList<AnimalProceso>();
                    } else {
                        //si es la primera pasada añadimos el animal al ArrayLIst
                        alAnimales.add(animal);
                    }
                    //actualizamos el idEdificio en curso
                    idEdificio = animal.getIdEdificio();
                } else //mientras el edificio sea igual
                {
                    //añadimos el animal al ArrayList
                    alAnimales.add(animal);
                }
            }
            if (!alAnimales.isEmpty()) {
                //cargamos el arraylist en la hash
                hmHembras.put(idEdificio, alAnimales);
            }
            return hmHembras;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. recuperarHembras. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /**
     * Devuelve un arrayList con los monjes que tienen numeros para enfermar
     *
     * @return
     * @throws AbadiaException
     */
    public ArrayList<Monje> recuperarMonjesParaEnfermar() throws AbadiaException {
        CallableStatement cs = null;
        ResultSet rs = null;
        ArrayList<Monje> alMonjes = new ArrayList<Monje>();
        try {
            cs = getConexion().prepareCall("call recuperarMonjesParaEnfermar()");
            cs.execute();
            Monje monje = null;
            rs = cs.getResultSet();

            while (rs.next()) {
                monje = new Monje();
                monje.setSalud(rs.getShort("salud"));
                monje.setIdDeMonje(rs.getInt("monjeid"));
                monje.setIdDeAbadia(rs.getInt("abadiaid"));
                monje.setLipidos(rs.getShort("lipidos"));
                monje.setProteinas(rs.getShort("Proteinas"));
                monje.setHidratosCarbono(rs.getShort("hidratos_carbono"));
                monje.setVitaminas(rs.getShort("vitaminas"));
                monje.setIdioma(rs.getInt("idiomaid"));
                alMonjes.add(monje);
            }
            return alMonjes;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. recuperarMonjesParaEnfermar. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(cs);
        }

    }

    /**
     * Devuelve un arrayList con los monjes que estan enfermos y la enfermedad ha llegado a su fin
     * en el nivel en que se encuentra.
     *
     * @return
     * @throws AbadiaException
     */
    public ArrayList<Monje> recuperarMonjesEnfermos(String sFechaFiltro) throws AbadiaException {
        CallableStatement cs = null;
        ResultSet rs = null;
        ArrayList<Monje> alMonjes = new ArrayList<Monje>();
        String sProcedure = "";
        try {
            if (sFechaFiltro == null) {
                sProcedure = "call recuperarMonjesConEnfermedad()";
            } else {
                sProcedure = "call recuperarMonjesEnfermos(?)";
            }

            cs = getConexion().prepareCall(sProcedure);
            if (sFechaFiltro != null) {
                cs.setString(1, sFechaFiltro);
            }
            cs.execute();
            Monje monje = null;
            Enfermedad enfermedad = null;
            rs = cs.getResultSet();

            while (rs.next()) {
                monje = new Monje();
                monje.setSalud(rs.getShort("salud"));
                monje.setIdDeMonje(rs.getInt("monjeid"));
                monje.setIdDeAbadia(rs.getInt("abadiaid"));
                monje.setLipidos(rs.getShort("lipidos"));
                monje.setProteinas(rs.getShort("Proteinas"));
                monje.setHidratosCarbono(rs.getShort("hidratos_carbono"));
                monje.setVitaminas(rs.getShort("vitaminas"));
                monje.setIdioma(rs.getInt("idiomaid"));

                enfermedad = new Enfermedad();
                enfermedad.setTipoEnfermedad(rs.getInt("TIPO_ENFERMEDADID"));
                enfermedad.setNivel(rs.getShort("NIVEL"));
                enfermedad.setVariante(rs.getShort("VARIANTE"));
                enfermedad.setFechaInicio(rs.getString("FECHA_INICIO"));
                enfermedad.setFechaFin(rs.getString("FECHA_FIN"));

                //cargamos el objeto enfermedad al del monje.
                monje.setEnfermedad(enfermedad);

                alMonjes.add(monje);
            }
            return alMonjes;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. recuperarMonjesParaEnfermos. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(cs);
        }

    }

    /**
     * Devuelve una HashMap con los monjes que estan enfermos indexada por abadia
     * en el nivel en que se encuentra.
     *
     * @return
     * @throws AbadiaException
     */
    public HashMap<Integer, ArrayList<Monje>> recuperarMonjesEnfermosPorAbadia() throws AbadiaException {
        CallableStatement cs = null;
        ResultSet rs = null;
        ArrayList<Monje> alMonjes = new ArrayList<Monje>();
        String sProcedure = "";
        int iLastAbadia = 0;
        HashMap<Integer, ArrayList<Monje>> hmMonjes = new HashMap<Integer, ArrayList<Monje>>();
        try {
            sProcedure = "call recuperarMonjesConEnfermedad()";
            cs = getConexion().prepareCall(sProcedure);
            cs.execute();
            Monje monje = null;
            Enfermedad enfermedad = null;
            rs = cs.getResultSet();

            while (rs.next()) {
                monje = new Monje();
                monje.setSalud(rs.getShort("salud"));
                monje.setIdDeMonje(rs.getInt("monjeid"));
                monje.setIdDeAbadia(rs.getInt("abadiaid"));
                monje.setLipidos(rs.getShort("lipidos"));
                monje.setProteinas(rs.getShort("Proteinas"));
                monje.setHidratosCarbono(rs.getShort("hidratos_carbono"));
                monje.setVitaminas(rs.getShort("vitaminas"));
                monje.setIdioma(rs.getInt("idiomaid"));

                enfermedad = new Enfermedad();
                enfermedad.setTipoEnfermedad(rs.getInt("TIPO_ENFERMEDADID"));
                enfermedad.setNivel(rs.getShort("NIVEL"));
                enfermedad.setVariante(rs.getShort("VARIANTE"));
                enfermedad.setFechaInicio(rs.getString("FECHA_INICIO"));
                enfermedad.setFechaFin(rs.getString("FECHA_FIN"));

                //cargamos el objeto enfermedad al del monje.
                monje.setEnfermedad(enfermedad);

                if (iLastAbadia != monje.getIdDeAbadia()) {
                    //no incorporamos el ultimo monje pues es de otra abbatia.
                } else {
                    alMonjes.add(monje);
                }

                //si es la primera pasada...
                if (iLastAbadia == 0) {
                    //inicializo el iLastAbadia con el valor de la abadía del monje en proceso
                    iLastAbadia = monje.getIdDeAbadia();
                }
                //cada vez que se cambia de abbatia...
                if (iLastAbadia != monje.getIdDeAbadia()) {
                    //inserto arraylist de monjes por idabadia
                    hmMonjes.put(iLastAbadia, new ArrayList<Monje>(alMonjes));
                    //igualo el iLastAbadia con el idabadia en proceso
                    iLastAbadia = monje.getIdDeAbadia();
                    //limpio el arraylist
                    alMonjes.clear();
                    //una vez limpio incorporo el monje de la otra abbatia.
                    alMonjes.add(monje);
                }


            }
            //inserto el registro correspondiente a la última abbatia procesada....
            if (hmMonjes.get(iLastAbadia) == null) {
                hmMonjes.put(iLastAbadia, new ArrayList<Monje>(alMonjes));
            }

            return hmMonjes;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. recuperarMonjesParaEnfermos. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(cs);
        }

    }

    public AnimalProceso recuperarMachos(int idEdificio, int tipo_animal, short aislado) throws AbadiaException {
        // Abadias que tienen animales vivos
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            cs = getConexion().prepareCall("call recuperarMachos(?,?,?)");
            cs.setInt(1, idEdificio);
            cs.setInt(2, tipo_animal);
            cs.setInt(3, aislado);
            cs.execute();
            AnimalProceso animal = null;
            rs = cs.getResultSet();
            if (rs.next()) {
                animal = new AnimalProceso();
                animal.setSalud(rs.getInt("salud"));
                animal.setDescripcion(rs.getString("descripcion"));
                return animal;
            }
            return null;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. recuperarMachos. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(cs);
        }

    }

    public ArrayList<AnimalProceso> recuperarAnimalesProduccionAlimentos() throws AbadiaException {

        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            cs = getConexion().prepareCall("call recuperarAnimalesProduccionAlimentos();");
            cs.execute();
            rs = cs.getResultSet();
            ArrayList<AnimalProceso> animales = new ArrayList<AnimalProceso>();
            AnimalProceso animal;
            while (rs.next()) {
                animal = new AnimalProceso();
                animal.setIdAnimal(rs.getInt("animalid"));
                animal.setIdEdificio(rs.getInt("edificioid"));
                animal.setIdAlimento(rs.getInt("alimentoid"));
                animal.setSalud(rs.getInt("salud"));
                animal.setMin(rs.getInt("min"));
                animal.setMax(rs.getInt("max"));
                animal.setRecoge_monje(rs.getInt("recoge_monje"));
                animales.add(animal);
            }
            return animales;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. recuperarAnimalesProduccionAlimentos. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(cs);
        }

    }

    public ArrayList<AnimalProceso> recuperarAnimalesProduccionRecursos() throws AbadiaException {

        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            cs = getConexion().prepareCall("call recuperarAnimalesProduccionRecursos();");
            cs.execute();
            rs = cs.getResultSet();
            ArrayList<AnimalProceso> animales = new ArrayList<AnimalProceso>();
            AnimalProceso animal = null;
            while (rs.next()) {
                animal = new AnimalProceso();
                animal.setIdAnimal(rs.getInt("animalid"));
                animal.setIdEdificio(rs.getInt("edificioid"));
                animal.setIdRecurso(rs.getInt("recursoid"));
                animal.setSalud(rs.getInt("salud"));
                animal.setMin(rs.getInt("min"));
                animal.setMax(rs.getInt("max"));
                animal.setRecoge_monje(rs.getInt("recoge_monje"));
                animales.add(animal);
            }
            return animales;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. recuperarAnimalesProduccionRecursos. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(cs);
        }

    }


    public ArrayList<AnimalProceso> recuperarAnimalesParir() throws AbadiaException {

        // Abadias que tienen animales vivos
        /*
        String sSQL = "SELECT ab.abadiaid, a.*, ac.*, at.descripcion " +
        "FROM `animales_crias` ac, `animales` a, `animales_tipo` at, `edificio` e, `abbatia` ab " +
        "where ab.abadiaid = e.abadiaid and e.edificioid = a.edificioid and at.tipo_animalid = a.tipo_animalid and " +
        "( a.tipo_animalid = ac.tipo_animalid_femenino and a.nivel >= ac.nivel_min_femenino) and a.fecha_fallecimiento is null and a.fecha_embarazo is not null and " +
        "(To_Days(a.fecha_embarazo) <To_Days(?)) ";
        */
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            cs = getConexion().prepareCall("call recuperarAnimalesParir(?)");
            cs.setString(1, CoreTiempo.getTiempoAbadiaString());
            cs.execute();
            rs = cs.getResultSet();

            ArrayList<AnimalProceso> animales = new ArrayList<AnimalProceso>();
            AnimalProceso animal = null;
            while (rs.next()) {
                animal = new AnimalProceso();
                animal.setIdAbadia(rs.getInt("abadiaid"));
                animal.setIdAnimal(rs.getInt("animalid"));
                animal.setSalud(rs.getInt("salud"));
                animal.setCriasMax(rs.getInt("Crias_max"));
                animal.setTipoAnimalIdMasc(rs.getInt("tipo_animalid_masculino"));
                animal.setTipoAnimalIdFem(rs.getInt("tipo_animalid_femenino"));
                animal.setDescripcion(rs.getString("descripcion"));
                animal.setIdEdificio(rs.getInt("edificioid"));
                animales.add(animal);
            }
            return animales;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. recuperarAnimalesParir. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(cs);
        }

    }

    public ArrayList<MonjeProceso> recuperarMonjesActividadPeriodo(int periodo, int actividad) throws AbadiaException {

        String sSQL = "call recuperarMonjesActividadPeriodo(?,?)";

        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            cs = getConexion().prepareCall(sSQL);
            cs.setInt(1, actividad);
            cs.setInt(2, periodo);
            cs.execute();
            rs = cs.getResultSet();

            ArrayList<MonjeProceso> monjes = new ArrayList<MonjeProceso>();
            MonjeProceso monje = null;
            while (rs.next()) {
                monje = new MonjeProceso();
                monje.setIdMonje(rs.getInt("monjeid"));
                monje.setIdAbadia(rs.getInt("idAbadia"));
                monje.setIdActividad(rs.getInt("actividadid"));
                monje.setIdIdioma(rs.getInt("idiomaid"));
                monjes.add(monje);
            }

            return monjes;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. recuperarMonjesActividadPeriodo. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(cs);
        }
    }

    public ArrayList<MonjeProceso> recuperarMonjesActividadRegion(int periodo, int actividad, int idRegion) throws AbadiaException {

        String sSQL = "call recuperarMonjesActividadRegion(?,?,?)";

        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            cs = getConexion().prepareCall(sSQL);
            cs.setInt(1, actividad);
            cs.setInt(2, periodo);
            cs.setInt(3, idRegion);
            cs.execute();
            rs = cs.getResultSet();

            ArrayList<MonjeProceso> monjes = new ArrayList<MonjeProceso>();
            MonjeProceso monje = null;
            while (rs.next()) {
                monje = new MonjeProceso();
                monje.setIdMonje(rs.getInt("monjeid"));
                monje.setIdAbadia(rs.getInt("idAbadia"));
                monje.setIdActividad(rs.getInt("actividadid"));
                monje.setIdIdioma(rs.getInt("idiomaid"));
                monjes.add(monje);
            }
            return monjes;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. recuperarMonjesActividadRegion. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(cs);
        }
    }

    public ArrayList<MonjeEnfermo> recuperarMonjesAprendiendo(int abadia, int periodo, int nivel_esc) throws AbadiaException {
        int num_aprender = 0;
        // Abadias que tienen animales vivos
        String sSQL = "SELECT m.monjeid, m.abadiaid,u.idiomaid, m.estado " +
                "FROM `monje` m, abadia ab, usuario u, actividad a " +
                "WHERE ab.abadiaid = m.abadiaid and ab.usuarioid = u.usuarioid and m.estado = " + Constantes.MONJE_VIVO +
                " AND u.abadia_congelada = 0 AND ab.abadiaid =  ? AND a.monjeid = m.monjeid AND a.actividadid = " + Constantes.TAREA_APRENDER +
                " AND a.periodoid = ? " +
                "ORDER BY m.fecha_nacimiento asc limit ?";
        // Abadias con monjes de visita
        String sSQLV = "SELECT m.monjeid, ab.abadiaid, u.idiomaid, m.estado " +
                "FROM `monje` m,  abadia ab, usuario u, monje_visita mv, actividad a " +
                "WHERE m.monjeid = mv.monjeid and mv.abadiaid_destino = ? AND ab.abadiaid = mv.abadiaid_destino AND " +
                "ab.usuarioid = u.usuarioid and m.estado = " + Constantes.MONJE_VISITA +
                " AND a.monjeid = m.monjeid AND a.actividadid = " + Constantes.TAREA_APRENDER +
                " AND a.periodoid = ? " +
                " AND u.abadia_congelada = 0 " +
                "ORDER BY m.fecha_nacimiento asc limit ? ";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            if (nivel_esc == 0)
                num_aprender = 1;

            if (nivel_esc == 1)
                num_aprender = 2;

            if (nivel_esc == 2)
                num_aprender = 4;

            if (nivel_esc == 3)
                num_aprender = 8;

            if (nivel_esc == 4)
                num_aprender = 16;

            ps = getConexion().prepareStatement(sSQL);
            ps.setInt(1, abadia);
            ps.setInt(2, periodo);
            ps.setInt(3, num_aprender); //dependiendo del nivel de la escuela se enseñaran a uno o mas alumnos por franja horaria
            rs = ps.executeQuery();
            ArrayList<MonjeEnfermo> monjes = new ArrayList<MonjeEnfermo>();
            MonjeEnfermo monje = null;
            while (rs.next()) {
                monje = new MonjeEnfermo();
                monje.setIdMonje(rs.getInt("monjeid"));
                monje.setIdAbadia(rs.getInt("abadiaid"));
                monje.setIdIdioma(rs.getInt("idiomaid"));
                monje.setIdEstado(rs.getInt("estado"));
                monjes.add(monje);
            }
            // Monjes que trabajan en otras abadias!
            ps = getConexion().prepareStatement(sSQLV);
            ps.setInt(1, abadia);
            ps.setInt(2, periodo);
            ps.setInt(3, num_aprender);//dependiendo del nivel de la escuela se enseñaran a uno o mas alumnos por franja horaria
            rs = ps.executeQuery();
            while (rs.next()) {
                monje = new MonjeEnfermo();
                monje.setIdMonje(rs.getInt("monjeid"));
                monje.setIdAbadia(rs.getInt("abadiaid"));
                monjes.add(monje);
            }
            return monjes;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. recuperarMonjesAprendiendo. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public ArrayList<MonjeProceso> recuperarMonjesActividad(int periodo, int actividad, int abadiaid) throws AbadiaException {

        // Abadias que tienen animales vivos
        String sSQL = "SELECT m.monjeid, m.abadiaid, a.actividadid, u.idiomaid " +
                "FROM `monje` m, `actividad` a, abadia ab, usuario u " +
                "WHERE m.monjeid = a.monjeid and ab.abadiaid = m.abadiaid and ab.usuarioid = u.usuarioid and m.estado = 0 AND " +
                " a.actividadid = ? AND periodoid = ? AND m.abadiaid=? AND u.abadia_congelada = 0 " +
                "ORDER BY u.idiomaid, m.abadiaid";
        // Abadias con monjes de visita
        String sSQLV = "SELECT m.monjeid, ab.abadiaid, a.actividadid, u.idiomaid " +
                "FROM `monje` m, `actividad` a, abadia ab, usuario u, monje_visita mv " +
                "WHERE m.monjeid = mv.monjeid and m.monjeid = a.monjeid and ab.abadiaid = mv.abadiaid_destino AND ab.usuarioid = u.usuarioid AND m.estado = 4 AND " +
                " a.actividadid = ? AND periodoid = ? AND mv.abadiaid_destino=? AND u.abadia_congelada = 0 " +
                "ORDER BY u.idiomaid, m.abadiaid ";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, actividad);
            ps.setInt(2, periodo);
            ps.setInt(3, abadiaid);
            rs = (ResultSet) ps.executeQuery();
            ArrayList<MonjeProceso> monjes = new ArrayList<MonjeProceso>();
            MonjeProceso monje = null;
            while (rs.next()) {
                monje = new MonjeProceso();
                monje.setIdMonje(rs.getInt("monjeid"));
                monje.setIdAbadia(rs.getInt("abadiaid"));
                monje.setIdActividad(rs.getInt("actividadid"));
                monje.setIdIdioma(rs.getInt("idiomaid"));
                monjes.add(monje);
            }
            // Monjes que trabajan en otras abadias!
            ps = (PreparedStatement) con.prepareStatement(sSQLV);
            ps.setInt(1, actividad);
            ps.setInt(2, periodo);
            ps.setInt(3, abadiaid);
            rs = ps.executeQuery();
            while (rs.next()) {
                monje = new MonjeProceso();
                monje.setIdMonje(rs.getInt("monjeid"));
                monje.setIdAbadia(rs.getInt("abadiaid"));
                monje.setIdActividad(rs.getInt("actividadid"));
                monje.setIdIdioma(rs.getInt("idiomaid"));
                monjes.add(monje);
            }
            return monjes;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. recuperarMonjesActividadPeriodo. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }


    public ArrayList<AlimentoProceso> recuperarAlimentosCaduca() throws AbadiaException {

        // devuelve los alimentos que van a caducar.
        String sSQL = "SELECT ab.abadiaid, lcase(at.descripcion) descrip, a.alimentoid, a.loteid, al.cantidad, lcase(un.descripcion) medida, u.idiomaid " +
                "FROM `alimentos` a, `alimentos_lote` al, `alimentos_tipo` at, edificio e, abadia ab, unidad_medida un, usuario u " +
                "Where a.loteid = al.loteid and at.alimentoid = a.alimentoid and a.edificioid = e.edificioid and e.abadiaid = ab.abadiaid and " +
                "un.unidad_medida = at.unidad_medida and un.idiomaid = u.idiomaid and u.usuarioid = ab.usuarioid and " +
                "al.fecha_caducidad <= ? and al.cantidad <> 0 " +
                "order by u.idiomaid, ab.abadiaid";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setString(1, CoreTiempo.getTiempoAbadiaString());
            rs = ps.executeQuery();
            ArrayList<AlimentoProceso> alimentos = new ArrayList<AlimentoProceso>();
            AlimentoProceso alimento;
            while (rs.next()) {
                alimento = new AlimentoProceso();
                alimento.setIdAbadia(rs.getInt("abadiaid"));
                alimento.setIdLote(rs.getInt("loteid"));
                alimento.setIdAalimento(rs.getInt("alimentoid"));
                alimento.setDescripcion(rs.getString("descrip"));
                alimento.setMedida(rs.getString("medida"));
                alimento.setCantidad(rs.getDouble("cantidad"));
                alimento.setIdidioma(rs.getInt("idiomaid"));
                alimentos.add(alimento);
            }
            return alimentos;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. recuperarAlimentosCaduca. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }


    public ArrayList<AlimentoProceso> recuperarAlimentosCaducaMercado() throws AbadiaException {

        // devuelve los alimentos que van a caducar.
        String sSQL = "select ab.abadiaid, lcase(at.descripcion) descrip, m.ctd_actual, lcase(un.descripcion) medida, ma.productoid, u.idiomaid  " +
                "from  `mercados` m, `mercados_alimentos` ma, abadia ab, unidad_medida un, usuario u, alimentos_tipo at " +
                "where u.usuarioid = ab.usuarioid and ab.abadiaid = m.abadiaid and ma.productoid = m.productoid and at.alimentoid = ma.alimentoid and  " +
                "un.unidad_medida = at.unidad_medida and un.idiomaid = u.idiomaid and m.estado = 0 and m.abadiaid <> 0 and " +
                "ma.fecha_caducidad < ? " +
                "order by u.idiomaid, ab.abadiaid";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setString(1, CoreTiempo.getTiempoAbadiaString());
            rs = ps.executeQuery();
            ArrayList<AlimentoProceso> alimentos = new ArrayList<AlimentoProceso>();
            AlimentoProceso alimento = null;
            while (rs.next()) {
                alimento = new AlimentoProceso();
                alimento.setIdAbadia(rs.getInt("abadiaid"));
                alimento.setIdLote(rs.getInt("productoid"));
                //alimento.setIdAalimento(rs.getInt("alimentoid"));
                alimento.setDescripcion(rs.getString("descrip"));
                alimento.setMedida(rs.getString("medida"));
                alimento.setCantidad(rs.getDouble("ctd_actual"));
                alimento.setIdidioma(rs.getInt("idiomaid"));
                alimentos.add(alimento);
            }
            return alimentos;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. recuperarAlimentosCaducaMercado. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public ArrayList<AnimalProceso> recuperarAnimalesAlimentacion(int idRegion) throws AbadiaException {

        // Abadias que tienen animales para alimentacion
        String sSQL = "SELECT e.abadiaid, e.edificioid, u.idiomaid, ac.consumo_familiaid, ac.consumo, an.salud, ac.descripcion, an.animalid " +
                "FROM `animales_crecimiento` ac, `animales` an, `abadia` a, `edificio` e, usuario u " +
                "WHERE ac.tipo_animalid = an.tipo_animalid and ac.nivel = an.nivel and an.edificioid = e.edificioid and a.abadiaid = e.abadiaid and consumo <> 0 " +
                "and an.estado = 0 and a.abadiaid <> 0 and u.usuarioid = a.usuarioid and u.abadia_congelada = 0 and a.regionid = ? " +
                "order by e.abadiaid, e.edificioid, consumo_familiaid ";
        /*
        String sSQLStress = "SELECT e.edificioid, sum(volumen) > max(en.almacenamiento) stress " +
        "FROM `animales_crecimiento` ac, `animales` a, `edificio` e, `edificio_nivel` en " +
        "where e.edificioid = a.edificioid and ac.tipo_animalid = a.tipo_animalid and ac.nivel = a.nivel and a.estado = 0 and " +
        "e.tipoedificioid = en.tipoedificioid and e.nivel = en.nivel " +
        "group by e.edificioid ";
        */
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idRegion);
            rs = ps.executeQuery();
            ArrayList<AnimalProceso> animales = new ArrayList<AnimalProceso>();
            AnimalProceso animal = null;
            while (rs.next()) {
                animal = new AnimalProceso();
                animal.setIdAbadia(rs.getInt("abadiaid"));
                animal.setIdEdificio(rs.getInt("edificioid"));
                animal.setIdAnimal(rs.getInt("animalid"));
                animal.setSalud(rs.getInt("salud"));
                animal.setIdIdioma(rs.getInt("idiomaid"));
                animal.setDescripcion(rs.getString("descripcion"));
                animal.setConsumoFamilia(rs.getInt("consumo_familiaid"));
                animal.setConsumo(rs.getDouble("consumo"));
                animales.add(animal);
            }
            return animales;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. recuperarAnimalesAlimentacion. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public ArrayList<AnimalProceso> recuperarAnimalesAlimentacion() throws AbadiaException {

        // Abadias que tienen animales para alimentacion
        String sSQL = "SELECT e.abadiaid, e.edificioid, u.idiomaid, ac.consumo_familiaid, ac.consumo, an.salud, ac.descripcion, an.animalid " +
                "FROM `animales_crecimiento` ac, `animales` an, `abadia` a, `edificio` e, usuario u " +
                "WHERE ac.tipo_animalid = an.tipo_animalid and ac.nivel = an.nivel and an.edificioid = e.edificioid and a.abadiaid = e.abadiaid and consumo <> 0 " +
                "and an.estado = 0 and a.abadiaid <> 0 and u.usuarioid = a.usuarioid and u.abadia_congelada = 0 " +
                "order by e.abadiaid, e.edificioid, consumo_familiaid ";
        /*
        String sSQLStress = "SELECT e.edificioid, sum(volumen) > max(en.almacenamiento) stress " +
        "FROM `animales_crecimiento` ac, `animales` a, `edificio` e, `edificio_nivel` en " +
        "where e.edificioid = a.edificioid and ac.tipo_animalid = a.tipo_animalid and ac.nivel = a.nivel and a.estado = 0 and " +
        "e.tipoedificioid = en.tipoedificioid and e.nivel = en.nivel " +
        "group by e.edificioid ";
        */
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            //ps.setInt(1, idRegion);
            rs = ps.executeQuery();
            ArrayList<AnimalProceso> animales = new ArrayList<AnimalProceso>();
            //ArrayList animales2 = new ArrayList();
            //Hashtable animales = new Hashtable();
            AnimalProceso animal = null;
            while (rs.next()) {
                animal = new AnimalProceso();
                animal.setIdAbadia(rs.getInt("abadiaid"));
                animal.setIdEdificio(rs.getInt("edificioid"));
                animal.setIdAnimal(rs.getInt("animalid"));
                animal.setSalud(rs.getInt("salud"));
                animal.setIdIdioma(rs.getInt("idiomaid"));
                animal.setDescripcion(rs.getString("descripcion"));
                animal.setConsumoFamilia(rs.getInt("consumo_familiaid"));
                animal.setConsumo(rs.getDouble("consumo"));
                animales.add(animal);
            }
            return animales;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. recuperarAnimalesAlimentacion. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public int recuperarNumMonjesTarea(int idAbadia, int idTarea) throws AbadiaException {

        // Abadias que tienen animales para alimentacion
        String sSQL = "SELECT count(periodoid) " +
                "FROM `actividad` a, `monje` m " +
                "where a.monjeid = m.monjeid and abadiaid = ? and actividadid = ?";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idAbadia);
            ps.setInt(2, idTarea);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. recuperarNumMonjesTarea. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public Hashtable<Integer, Integer> recuperarNumMonjesTareaPorAbadia(int idTarea) throws AbadiaException {

        // Abadias que tienen animales para alimentacion
        String sSQL = "SELECT m.abadiaid, count(a.periodoid) contador " +
                "FROM `actividad` a, `monje` m " +
                "where a.monjeid = m.monjeid and m.estado = " + Constantes.MONJE_VIVO + " and actividadid = ? " +
                "group by abadiaid";

        Hashtable<Integer, Integer> htNumMonjes = new Hashtable<Integer, Integer>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idTarea);
            rs = ps.executeQuery();
            while (rs.next()) {
                htNumMonjes.put(rs.getInt("abadiaid"), rs.getInt("contador"));
            }
            return htNumMonjes;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. recuperarNumMonjesTareaPorAbadia. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /**
     * Suma a los monjes ya existentes en una abadía / actividad los que están de visita...
     *
     * @param idTarea
     * @param htNumMonjes
     * @throws AbadiaException
     */
    public void sumarNumMonjesTareaPorAbadiaVisita(int idTarea, Hashtable<Integer, Integer> htNumMonjes) throws AbadiaException {

        String sSQLVisita = "select mv.ABADIAID_DESTINO as abbatia, count(a.periodoid) contador " +
                " from actividad a, monje_visita mv, monje m " +
                " where m.estado = " + Constantes.MONJE_VISITA + " and m.monjeid = mv.monjeid and m.monjeid = a.monjeid and a.actividadid = ?" +
                " group by m.abadiaid ";

        PreparedStatement ps = null;
        ResultSet rs = null;
        Integer iNumMonjesPropios;
        int numMonjesVisita = 0;
        int numMonjesPropios = 0;
        int idAbadia = 0;
        try {
            ps = con.prepareStatement(sSQLVisita);
            ps.setInt(1, idTarea);
            rs = ps.executeQuery();
            while (rs.next()) {
                idAbadia = rs.getInt("abbatia");
                numMonjesVisita = rs.getInt("contador");
                iNumMonjesPropios = htNumMonjes.get(Integer.valueOf(idAbadia));
                //si no hay monjes trabajando de la misma abadía...
                if (iNumMonjesPropios == null) {
                    numMonjesPropios = 0;
                } else {
                    numMonjesPropios = iNumMonjesPropios;
                }
                htNumMonjes.put(idAbadia, numMonjesVisita + numMonjesPropios);
            }


        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. sumarNumMonjesTareaPorAbadiaVisita. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /* Alimetar el animal, devuelve el entero de la alimentación que ha tomado

    public static int alimentar_animal(int idAbadia, int idFamilia, double consumo) throws AbadiaException
    {
        adAlimentoLotes alimentoAD = null;
        adUtils utils = null;

        ArrayList<LoteCantidad> alLotes = null;
        Iterator<LoteCantidad> itLotes = null;
        LoteCantidad lote = null;
        int result = -1;

        try
        {
            alimentoAD = new adAlimentoLotes(Constantes.DB_CONEXION_PROCESS);
            alLotes = alimentoAD.recuperarLotesPorAbadiaFamilia(idAbadia, idFamilia);
            alimentoAD.finalize();

            itLotes = alLotes.iterator();
            double ctd, ctd1;
            ctd = consumo;

            while(itLotes.hasNext() && ctd > 0)
            { //199 -7 = 198 < 0
                lote = itLotes.next();
                if ((lote.getCantidad() - ctd) < 0)
                {
                    ctd1 = lote.getCantidad();
                }else
                {
                    ctd1 = ctd;
                }
                ctd = ctd - ctd1;
                utils = new adUtils(Constantes.DB_CONEXION_PROCESS);
                utils.execSQL("UPDATE alimentos_lote SET cantidad = cantidad - " + ctd1 +
                        " WHERE loteid = " + lote.getIdLote());
                utils.finalize();
                // result = 5;
            }
            if (ctd <= 0 ) result = 5;

            return result;

        }catch(AbadiaException e)
        {
            throw new AbadiaSQLException( "ERROR: alimentar_animal = "+e, e, log);
            //addLog( "ERROR: alimentar_animal = "+e, 1 );
        }finally
        {
            if (alimentoAD != null) alimentoAD.finalize();
            if (utils != null) utils.finalize();
        }

    }
    */
    /* Alimetar el animal, devuelve el entero de la alimentación que ha tomado
    */

    public int alimentar_animal(int idabadia, int idfamilia, double consumo) throws AbadiaException {
        String sSQLAlimen = "call alimentar_animal(?,?,?,?);";

        CallableStatement cs = null;
        try {
            cs = getConexion().prepareCall(sSQLAlimen);
            cs.setInt(1, idabadia);
            cs.setInt(2, idfamilia);
            cs.setDouble(3, consumo);
            cs.registerOutParameter(4, Types.INTEGER);
            cs.execute();
            return cs.getInt(4);
        } catch (SQLException e) {
            throw new AbadiaSQLException("ERROR: alimentar_animal = " + e, e, log);
            //addLog( "ERROR: alimentar_animal = "+e, 1 );
        } finally {
            DBMSUtils.cerrarObjetoSQL(cs);
        }

    }


    public void addLog(String mensaje, int tipo) throws AbadiaException {

        String sSQL = "INSERT INTO procesos_mensajes (fecha_real, fecha_abbatia, tipo, descripcion) VALUES (?,?,?,?)";
        log.info("Procesos Planificador: " + mensaje);

        PreparedStatement ps = null;
        try {
            ps = getConexion().prepareStatement(sSQL);
            int parNo = 1;
            ps.setString(parNo++, CoreTiempo.getTiempoRealStringConHoras());
            ps.setString(parNo++, CoreTiempo.getTiempoAbadiaStringConHoras());
            ps.setInt(parNo++, tipo);
            ps.setString(parNo++, mensaje);
            // Ejecutarlo
            ps.execute();
            //ps.close();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. addLog. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /* Alimenta el monje según la familia
    0 - No ha comido na!
    1 - Ha comido pero no de lo que queria
    2 - Ha comido todo
    3 - Ha comido algo pero le falta
    -1 - Sin alimentos para toda la abbatia
    */

    public int alimentarMonjeFamilia(Monje monje, HashMap<Short, ArrayList<AlimentoFamiliaProceso>> p_hmAlimentosFamilia, ArrayList<AlimentoFamiliaProceso> p_alAlimentosAbadia) throws AbadiaException {
        int result = 0;
/*        String sSQL = "UPDATE alimentos_lote SET cantidad = cantidad - ? WHERE loteid = ? ";
        String sSQLAlimFam = new StringBuilder().append("SELECT al.loteid, consumo_monje, proteinas, lipidos, hidratos_carbono, vitaminas  ").
                append("FROM `edificio` e, `alimentos` a, `alimentos_lote` al, `alimentos_tipo` at ").
                append("where e.abadiaid = ? and a.edificioid = e.edificioid and al.loteid = a.loteid and at.alimentoid = a.alimentoid ").
                append("and cantidad <> 0 and consumo_monje <> 0 and at.familiaid = ? ").append("order by fecha_caducidad ").toString();
        String sSQLAlimenta = new StringBuilder().append("SELECT al.loteid, at.familiaid, consumo_monje, proteinas, lipidos, hidratos_carbono, vitaminas  ").
                append("FROM `edificio` e, `alimentos` a, `alimentos_lote` al, `alimentos_tipo` at ").
                append("where e.abadiaid = ? and a.edificioid = e.edificioid and al.loteid = a.loteid and at.alimentoid = a.alimentoid ").
                append("and cantidad <> 0 and consumo_monje <> 0 ").append("order by fecha_caducidad ").toString();
                */
        short idfamilia = 0, hacomido_familia = 0;
        short idActividad = 0;
        boolean cocina_vacia = false;
        ArrayList<AlimentoFamiliaProceso> alAlimentosFamilia;
        ArrayList<Table> alAlimentosFamiliaJerarquia;


        adUtils oUtilsAD;
        //AlimentoFamiliaProceso oAlimentoFamilia;

        //PreparedStatement ps = null;
        //ResultSet rs = null;
        //try {
        oUtilsAD = new adUtils(con);
        for (int alimenta = 1; alimenta < 6; alimenta++) {
            switch (alimenta) {
                case 1:
                    //idfamilia = monje.getComeFamiliaID1();
                    idActividad = monje.getActMaitines();
                    break;
                case 2:
                    //idfamilia = monje.getComeFamiliaID2();
                    idActividad = monje.getActPrima();
                    break;
                case 3:
                    //idfamilia = monje.getComeFamiliaID3();
                    idActividad = monje.getActTercia();
                    break;
                case 4:
                    //idfamilia = monje.getComeFamiliaID4();
                    idActividad = monje.getActNona();
                    break;
                case 5:
                    //idfamilia = monje.getComeFamiliaID5();
                    idActividad = monje.getActVispera();
                    break;
            }
            //recuperamos la familia que corresponde al monje en función del nivel de abadia, jerarquía del monje y
            //actividad. En lugar de recuperar los valores que tiene el monje.
            idfamilia = CargasInicialesDietasBBean.getFamiliaAlimentosActividad(monje.getNivelAbadia(),
                    monje.getIdDeJerarquia(), idActividad).shortValue();

            if (idActividad == 0) idActividad = 1;
/*
            if (monje.getIdDeMonje() == 281491) {
                log.debug("test");
            }
*/

            // Por el momento no ha comido
            hacomido_familia = -1;
            if ((idfamilia != 0) && (!cocina_vacia)) {
                alAlimentosFamilia = p_hmAlimentosFamilia.get(idfamilia);
                if (alAlimentosFamilia != null) {
                    //debemos establecer un subfiltro para que sólo procese los lotes correspondientes a los alimentos
                    //de esa familia que corresponden a la jerarquía del monje
                    alAlimentosFamiliaJerarquia = (ArrayList<Table>)
                            CargasInicialesDietasBBean.getAlimentosFamiliaJerarquiaNivelActividad(monje.getNivelAbadia(),
                                    idfamilia, monje.getIdDeJerarquia(), idActividad);
                    //por cada uno de los alimentos que le corresponden al monje de esa familia por jerarquía...
/*
                    if (alAlimentosFamiliaJerarquia == null) {
                        log.debug("test");
                    }
*/
                    for (Table oAlimento : alAlimentosFamiliaJerarquia) {
                        //si no ha completado la alimentación..
                        if (hacomido_familia != 0) {
                            //recorremos todos los alimentos disponibles de esa familia
                            for (AlimentoFamiliaProceso oAlimentoFamilia : alAlimentosFamilia) {
                                //si el alimento disponible coincide con el requerido...
                                if (oAlimentoFamilia.getAlimentoId() == oAlimento.getId()) {
                                    //if (oAlimentoFamilia.getCantidadLote() >= oAlimentoFamilia.getConsumoMonje()) {
                                    //Hacemos que los consumos se puedan hacer siempre
                                    if (oAlimentoFamilia.getCantidadLote() > 0) {
                                        monje.setProteinas((short) (monje.getProteinas() + oAlimentoFamilia.getProteinas()));
                                        monje.setLipidos((short) (monje.getLipidos() + oAlimentoFamilia.getLipidos()));
                                        monje.setHidratosCarbono((short) (monje.getHidratosCarbono() + oAlimentoFamilia.getHidratosCarbono()));
                                        monje.setVitaminas((short) (monje.getVitaminas() + oAlimentoFamilia.getVitaminas()));
                                        //actualizamos la cantidad disponible del lote restando lo cosumido por el monje.
                                        oAlimentoFamilia.setCantidadLote(oAlimentoFamilia.getCantidadLote() - oAlimentoFamilia.getConsumoMonje());
                                        //actualizamos la cantidad sobre base de datos
                                        oUtilsAD.execSQL("UPDATE alimentos_lote SET cantidad = cantidad - " + oAlimentoFamilia.getConsumoMonje() + " WHERE loteid = " + oAlimentoFamilia.getLoteId());
                                        //                           loteid = oAlimentoFamilia.getLoteId();
                                        //                           ctd = oAlimentoFamilia.getConsumoMonje();
                                        hacomido_familia = 0;   // Ha comido lo asignado
                                        if (result == 0) result = 2; // ha comido todo
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                //si no ha comido lo que le tocaba...
                if (hacomido_familia == -1) {
                    for (AlimentoFamiliaProceso oAlimentoFamilia : p_alAlimentosAbadia) {
                        //if (oAlimentoFamilia.getCantidadLote() >= oAlimentoFamilia.getConsumoMonje()) {
                        //Hacemos que los consumos se puedan hacer siempre
                        if (oAlimentoFamilia.getCantidadLote() > 0) {
                            monje.setProteinas((short) (monje.getProteinas() + oAlimentoFamilia.getProteinas()));
                            monje.setLipidos((short) (monje.getLipidos() + oAlimentoFamilia.getLipidos()));
                            monje.setHidratosCarbono((short) (monje.getHidratosCarbono() + oAlimentoFamilia.getHidratosCarbono()));
                            monje.setVitaminas((short) (monje.getVitaminas() + oAlimentoFamilia.getVitaminas()));
                            oAlimentoFamilia.setCantidadLote(oAlimentoFamilia.getCantidadLote() - oAlimentoFamilia.getConsumoMonje());
                            oUtilsAD.execSQL("UPDATE alimentos_lote SET cantidad = " + oAlimentoFamilia.getCantidadLote() + " WHERE loteid = " + oAlimentoFamilia.getLoteId());
                            //loteid = rs.getInt("loteid");
                            //ctd = rs.getFloat("consumo_monje");
                            hacomido_familia = oAlimentoFamilia.getFamiliaId();
                            result = 3; // ha comido.. aunque no le gustase ;-)
                            break;
                        }
                    }
                    //si tampoco ha comido una alternativa...
                    if (hacomido_familia == -1) {
                        cocina_vacia = true;
                        if (result != 0) result = 1;
                        else result = -1;
                    }
                }

                // Marcar lo que ha comido
                switch (alimenta) {
                    case 1:
                        monje.setHa_comidoFamiliaID1(hacomido_familia);
                        break;
                    case 2:
                        monje.setHa_comidoFamiliaID2(hacomido_familia);
                        break;
                    case 3:
                        monje.setHa_comidoFamiliaID3(hacomido_familia);
                        break;
                    case 4:
                        monje.setHa_comidoFamiliaID4(hacomido_familia);
                        break;
                    case 5:
                        monje.setHa_comidoFamiliaID5(hacomido_familia);
                        break;
                }
            }
        }
        return result;
        //} finally {
        //    DBMSUtils.cerrarObjetoSQL(rs);
        //    DBMSUtils.cerrarObjetoSQL(ps);
        //}
    }

    public int procesoMolinoPorMonje(int idAbadia, int nivelMolino, int p_iPotencialProduccion) throws AbadiaException {
        String sSQL = "SELECT mp.CTD_ACTUAL, mp.RECURSOID_SALIDA, mt.CTD_PROCESA_DIA, mp.PRODUCTOID_ENTRADA " +
                "FROM molino_produccion mp, molino_tipo mt " +
                "WHERE ABADIAID = ? and CTD_ACTUAL > 0 and mt.NIVEL=? and mp.PRODUCTOID_ENTRADA = mt.PRODUCTOID";

        PreparedStatement ps = null;
        ResultSet rs = null;
        adUtils utils;
        adRecurso recursoAD;
        int capacidadProceso = 0;
        int produccionPotencial = p_iPotencialProduccion;

        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idAbadia);
            ps.setInt(2, nivelMolino);
            rs = ps.executeQuery();
            while (rs.next()) {
                //miramos si la cantidad_actual para moler en el molino es mayor que la capacidad del molino
                if (rs.getInt("CTD_ACTUAL") < rs.getInt("CTD_PROCESA_DIA")) {
                    capacidadProceso = rs.getInt("CTD_ACTUAL");
                } else capacidadProceso = rs.getInt("CTD_PROCESA_DIA");
                //si lo que se va a procesar, excede de lo que le queda producir al monje, se iguala
                //el resto de la producción potencial con lo que va a producir
                if (capacidadProceso > produccionPotencial) {
                    capacidadProceso = produccionPotencial;
                }
                //restamos la cantidad actual
                utils = new adUtils(con);
                utils.execSQL("UPDATE molino_produccion set CTD_ACTUAL = CTD_ACTUAL - " + capacidadProceso + " where ABADIAID = " + idAbadia + " and PRODUCTOID_ENTRADA = " + rs.getInt("PRODUCTOID_ENTRADA"));

                //sumamos los recursos de harina
                recursoAD = new adRecurso(con);
                recursoAD.sumarRecurso(rs.getInt("RECURSOID_SALIDA"), (long) idAbadia, (double) capacidadProceso);
                //restamos lo producido del potencial de producción
                produccionPotencial -= capacidadProceso;
                //si se alcanza el potencial de producción, salimos del bucle
                if (produccionPotencial <= 0) {
                    break;
                }
            }
            //devuelvo lo que ha producido el monje
            return p_iPotencialProduccion - produccionPotencial;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. procesoMolinoPorMonje. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public ArrayList<ProductoIdioma> recuperarVentasParaCaducar() throws AbadiaException {
        String sSQL = "SELECT m.PRODUCTOID, m.ABADIAID, m.MERCANCIA, m.CTD_ACTUAL, u.IDIOMAID " +
                " FROM mercados m, usuario u, abadia a " +
                " WHERE m.CTD_ACTUAL > 0 and m.ABADIAID <> 0 and m.FECHA_FINAL < ? and m.ABADIAID = a.ABADIAID and a.USUARIOID = u.USUARIOID " +
                " order by IDIOMAID ";

        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<ProductoIdioma> alProductos = new ArrayList<ProductoIdioma>();

        try {
            ps = con.prepareStatement(sSQL);
            ps.setString(1, CoreTiempo.getTiempoAbadiaString());
            rs = ps.executeQuery();
            while (rs.next()) {
                alProductos.add(new ProductoIdioma(rs.getInt("ABADIAID"), rs.getInt("PRODUCTOID"), rs.getInt("IDIOMAID")));
            }
            return alProductos;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. recuperarVentasParaCaducar. SQLException", e, log);

        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public ArrayList<ProductoIdioma> recuperarVentasParaCaducarEnVenta() throws AbadiaException {
        String sSQL = "SELECT m.PRODUCTOID, m.ABADIAID, m.MERCANCIA, m.CTD_ACTUAL, u.IDIOMAID " +
                " FROM mercados m, usuario u, abadia a, mercados_alimentos ma " +
                " WHERE m.CTD_ACTUAL > 0 and m.ABADIAID <> 0 and ma.FECHA_CADUCIDAD < ? and m.ABADIAID = a.ABADIAID and a.USUARIOID = u.USUARIOID and m.productoid = ma.productoid " +
                " order by IDIOMAID ";

        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<ProductoIdioma> alProductos = new ArrayList<ProductoIdioma>();

        try {
            ps = con.prepareStatement(sSQL);
            ps.setString(1, CoreTiempo.getTiempoAbadiaString());
            rs = ps.executeQuery();
            while (rs.next()) {
                alProductos.add(new ProductoIdioma(rs.getInt("ABADIAID"), rs.getInt("PRODUCTOID"), rs.getInt("IDIOMAID")));
            }
            return alProductos;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. recuperarVentasParaCaducarEnVenta. SQLException", e, log);

        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public ArrayList<datosElaboracion> recuperarElaboracionPorTipo(short tipo) throws AbadiaException {
        String sSQL = "";

        if (tipo == Constantes.TAREA_ELEBORAR_ALIMENTO) {
            sSQL = "SELECT ea.elaboracionid, ea.productoid, ea.cantidad, ea.elaborado, ea.estado, ea.fecha_inicio, ep.ctd_procesa_dia, ep.tiempo_total, ep.ctd_total, ep.tipo_producto, ea.edificioid, e.abadiaid " +
                    " FROM elaboracion_alimentos ea, alimentos_tipo at, elaboracion_parametros ep, edificio e " +
                    " WHERE ea.productoid = at.alimentoid and ea.edificioid = e.edificioid and " +
                    " ea.productoid = ep.productoid and ea.estado = " + Constantes.ESTADO_ELABORACION_REPOSANDO + " and " +
                    " DATEDIFF('" + CoreTiempo.getTiempoAbadiaString() + "', ea.fecha_inicio) >= ep.tiempo_total ";
        } else {
            sSQL = "SELECT ea.elaboracionid, ea.productoid, ea.cantidad, ea.elaborado, ea.estado, ea.fecha_inicio, ep.ctd_procesa_dia, ep.tiempo_total, ep.ctd_total, ep.tipo_producto, ea.edificioid, e.abadiaid " +
                    " FROM elaboracion_alimentos ea, recurso_tipo rt, elaboracion_parametros ep, edificio e" +
                    " WHERE ea.productoid = rt.recursoid and ea.edificioid = e.edificioid and " +
                    " ea.productoid = ep.productoid and ea.estado = " + Constantes.ESTADO_ELABORACION_REPOSANDO + " and " +
                    " DATEDIFF('" + CoreTiempo.getTiempoAbadiaString() + "', ea.fecha_inicio) >= ep.tiempo_total ";
        }

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
//            ps.setString(1, CoreTiempo.getTiempoAbadiaString());
            rs = ps.executeQuery();
            ArrayList<datosElaboracion> listaElaboracion = new ArrayList<datosElaboracion>();
            datosElaboracion elaboracion = null;
            while (rs.next()) {
                elaboracion = new datosElaboracion();
                elaboracion.setIdElaboracion(rs.getInt("elaboracionid"));
                elaboracion.setIdEdificio(rs.getInt("edificioid"));
                elaboracion.setIdAbadia(rs.getInt("abadiaid"));
                elaboracion.setEstado(rs.getInt("estado"));
                elaboracion.setCantidad(rs.getInt("cantidad"));
                elaboracion.setCtd_dia(rs.getInt("ctd_procesa_dia"));
                elaboracion.setFecha_inicio(rs.getString("fecha_inicio"));
                elaboracion.setElaborado(rs.getInt("elaborado"));
                elaboracion.setIdProducto(rs.getInt("productoid"));
                elaboracion.setDias_total(rs.getInt("tiempo_total"));
                elaboracion.setCtd_total(rs.getInt("ctd_total"));
                elaboracion.setTipoProducto(rs.getString("tipo_producto"));

                listaElaboracion.add(elaboracion);
            }
            return listaElaboracion;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. recuperarElaboracionPorTipo. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }


    public ArrayList<datosElaboracion> recuperarElaboracionPorEdificio(int idEdificio, int idIdioma, short tipo) throws AbadiaException {
        String sSQL = "";

        if (tipo == Constantes.TAREA_ELEBORAR_ALIMENTO) {
            sSQL = "SELECT ea.elaboracionid, ea.productoid, ea.cantidad, ea.elaborado, ea.estado, ea.fecha_inicio, l.literal, ep.ctd_procesa_dia, ep.tiempo_total, ep.ctd_total, ep.tipo_producto " +
                    "FROM elaboracion_alimentos ea, alimentos_tipo at, literales l, elaboracion_parametros ep " +
                    "WHERE ea.productoid = at.alimentoid and at.literalid = l.literalid and l.idiomaid = ? and ea.productoid = ep.productoid and ea.edificioid = ? and ea.estado not in(?, ?, ?) " +
                    "GROUP BY productoid";
        } else {
            sSQL = "SELECT ea.elaboracionid, ea.productoid, ea.cantidad, ea.elaborado, ea.estado, ea.fecha_inicio, l.literal, ep.ctd_procesa_dia, ep.tiempo_total, ep.ctd_total, ep.tipo_producto " +
                    "FROM elaboracion_alimentos ea, recurso_tipo rt, literales l, elaboracion_parametros ep " +
                    "WHERE ea.productoid = rt.recursoid and rt.literalid = l.literalid and l.idiomaid = ? and ea.productoid = ep.productoid and ea.edificioid = ? and ea.estado not in(?, ?, ?) ";
        }

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = getConexion().prepareStatement(sSQL);
            ps.setInt(1, idIdioma);
            ps.setInt(2, idEdificio);
            ps.setInt(3, Constantes.ESTADO_ELABORACION_FINALIZADO);
            ps.setInt(4, Constantes.ESTADO_ELABORACION_PAUSADO);
            ps.setInt(5, Constantes.ESTADO_ELABORACION_REPOSANDO);
            rs = (ResultSet) ps.executeQuery();
            ArrayList<datosElaboracion> listaElaboracion = new ArrayList<datosElaboracion>();
            datosElaboracion elaboracion = null;
            while (rs.next()) {
                elaboracion = new datosElaboracion();
                elaboracion.setIdElaboracion(rs.getInt("elaboracionid"));
                elaboracion.setIdEdificio(idEdificio);
                elaboracion.setEstado(rs.getInt("estado"));
                elaboracion.setCantidad(rs.getInt("cantidad"));
                elaboracion.setCtd_dia(rs.getInt("ctd_procesa_dia"));
                elaboracion.setFecha_inicio(rs.getString("fecha_inicio"));
                elaboracion.setElaborado(rs.getInt("elaborado"));
                elaboracion.setIdProducto(rs.getInt("productoid"));
                elaboracion.setDias_total(rs.getInt("tiempo_total"));
                elaboracion.setCtd_total(rs.getInt("ctd_total"));
                elaboracion.setTipoProducto(rs.getString("tipo_producto"));
                elaboracion.setDescProducto(rs.getString("literal"));
                listaElaboracion.add(elaboracion);
            }
            return listaElaboracion;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. recuperarElaboracionPorEdificio. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public void generarProduccionGanaderiaAlimentos(Hashtable<Integer, AnimalProduccion> htProduccion) throws AbadiaException {
        String sSQL = "insert into animales_produccion_alimento (edificioid, alimentoid, cantidad, recoge_monje) values (?,?,?,?)";
        Enumeration<AnimalProduccion> enProduccion = htProduccion.elements();
        AnimalProduccion produccion = null;

        PreparedStatement ps = null;
        try {
            ps = getConexion().prepareStatement(sSQL);
            while (enProduccion.hasMoreElements()) {
                produccion = enProduccion.nextElement();
                ps.setInt(1, produccion.getIdEdificio());
                ps.setInt(2, produccion.getIdAlimento());
                ps.setDouble(3, produccion.getCantidad());
                ps.setInt(4, produccion.getRecoge_monje());
                ps.execute();
            }
        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. generarProduccionGanaderia. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public void generarProduccionGanaderiaRecursos(Hashtable<Integer, AnimalProduccion> htProduccion) throws AbadiaException {
        String sSQL = "insert into animales_produccion_recurso (edificioid, recursoid, cantidad, recoge_monje) values (?,?,?,?)";
        Enumeration<AnimalProduccion> enProduccion = htProduccion.elements();
        AnimalProduccion produccion;

        PreparedStatement ps = null;
        try {
            ps = getConexion().prepareStatement(sSQL);
            while (enProduccion.hasMoreElements()) {
                produccion = enProduccion.nextElement();
                ps.setInt(1, produccion.getIdEdificio());
                ps.setInt(2, produccion.getIdRecurso());
                ps.setDouble(3, produccion.getCantidad());
                ps.setInt(4, produccion.getRecoge_monje());
                ps.execute();
            }
        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. generarProduccionGanaderiaRecursos. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public Hashtable<Integer, Hashtable<Object, ProduccionPorAbadia>> cargarProduccionGanaderiaPorAbadiaAlimentos() throws AbadiaException {
        int last_abadia = 0;
        Hashtable<Integer, Hashtable<Object, ProduccionPorAbadia>> htProduccion = new Hashtable<Integer, Hashtable<Object, ProduccionPorAbadia>>();
        Hashtable<Object, ProduccionPorAbadia> alProduccion = null;
        ProduccionPorAbadia produccion = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            cs = getConexion().prepareCall("call cargarProduccionGanaderiaPorAbadiaAlimentos()");
            cs.execute();
            rs = cs.getResultSet();
            while (rs.next()) {
                if (last_abadia != rs.getInt("abadiaid")) {
                    if (last_abadia != 0) {
                        //
                        htProduccion.put(last_abadia, alProduccion);
                    }
                    alProduccion = new Hashtable<Object, ProduccionPorAbadia>();
                    last_abadia = rs.getInt("abadiaid");
                }
                produccion = new ProduccionPorAbadia();
                produccion.setIdAbadia(rs.getInt("abadiaid"));
                produccion.setIdEdificio(rs.getInt("edificioid"));
                produccion.setIdEdificioOri(rs.getInt("edificioid_ori"));
                produccion.setIdAlimento(rs.getInt("alimentoid"));
                produccion.setDescripcion(rs.getString("descAli"));
                produccion.setUnidad_medida(rs.getString("descUni"));
                produccion.setCantidad(rs.getInt("cantidad"));
                produccion.setRecoge_monje(rs.getInt("recoge_monje"));
                alProduccion.put(rs.getInt("alimentoid"), produccion);
            }
            htProduccion.put(last_abadia, alProduccion);
            return htProduccion;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. cargarProduccionGanaderiaPorAbadiaAlimentos. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(cs);
        }
    }

    public Hashtable<Integer, Hashtable<Object, ProduccionPorAbadia>> cargarProduccionGanaderiaPorAbadiaRecursos() throws AbadiaException {
        int last_abadia = 0;
        Hashtable<Integer, Hashtable<Object, ProduccionPorAbadia>> htProduccion = new Hashtable<Integer, Hashtable<Object, ProduccionPorAbadia>>();
        Hashtable<Object, ProduccionPorAbadia> alProduccion = null;
        ProduccionPorAbadia produccion = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            cs = getConexion().prepareCall("call cargarProduccionGanaderiaPorAbadiaRecursos()");
            cs.execute();
            rs = cs.getResultSet();
            while (rs.next()) {
                if (last_abadia != rs.getInt("abadiaid")) {
                    if (last_abadia != 0) {
                        //
                        htProduccion.put(last_abadia, alProduccion);
                    }
                    alProduccion = new Hashtable<Object, ProduccionPorAbadia>();
                    last_abadia = rs.getInt("abadiaid");
                }
                produccion = new ProduccionPorAbadia();
                produccion.setIdAbadia(rs.getInt("abadiaid"));
                produccion.setIdEdificio(rs.getInt("edificioid"));
                produccion.setIdEdificioOri(rs.getInt("edificioid_ori"));
                produccion.setIdRecurso(rs.getInt("recursoid"));
                produccion.setDescripcion(rs.getString("descRecurso"));
                produccion.setUnidad_medida(rs.getString("descUni"));
                produccion.setCantidad(rs.getDouble("cantidad"));
                produccion.setRecoge_monje(rs.getInt("recoge_monje"));
                alProduccion.put(rs.getInt("recursoid"), produccion);
            }
            htProduccion.put(last_abadia, alProduccion);
            return htProduccion;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. cargarProduccionGanaderiaPorAbadiaRecursos. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(cs);
        }
    }


    public void actualizarProduccionPorAbadiaAlimentos(Hashtable<Object, ProduccionPorAbadia> alProduccion) throws AbadiaException {
        String sSQL = "update animales_produccion_alimento set cantidad=? where edificioid=? and alimentoid = ?";
        Enumeration<ProduccionPorAbadia> itProduccion = alProduccion.elements();
        ProduccionPorAbadia produccion = null;
        PreparedStatement ps = null;
        try {
            ps = getConexion().prepareStatement(sSQL);
            while (itProduccion.hasMoreElements()) {
                produccion = itProduccion.nextElement();
                ps.setDouble(1, produccion.getCantidad());
                ps.setInt(2, produccion.getIdEdificioOri());
                ps.setInt(3, produccion.getIdAlimento());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. actualizarProduccionPorAbadiaAlimentos. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }


    }

    public void actualizarProduccionPorAbadiaRecursos(Hashtable<Object, ProduccionPorAbadia> alProduccion) throws AbadiaException {
        String sSQL = "update animales_produccion_recurso set cantidad=? where edificioid=? and recursoid = ?";
        Enumeration<ProduccionPorAbadia> itProduccion = alProduccion.elements();
        ProduccionPorAbadia produccion = null;
        PreparedStatement ps = null;
        try {
            ps = getConexion().prepareStatement(sSQL);
            while (itProduccion.hasMoreElements()) {
                produccion = itProduccion.nextElement();
                ps.setDouble(1, produccion.getCantidad());
                ps.setInt(2, produccion.getIdEdificioOri());
                ps.setInt(3, produccion.getIdRecurso());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. actualizarProduccionPorAbadiaRecursos. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }


    }

    /**
     * Recupera un {@link ArrayList} de objetos {@link SalarioGuardia} con los datos
     * de los guardias contratados por abadia y las monedas que dispone la abadia
     *
     * @return ArrayList
     * @throws org.abbatia.exception.AbadiaSQLException
     *          Excepción base de abbatia
     */
    public ArrayList<SalarioGuardia> recuperarSalariosGuardias() throws AbadiaException {
        ArrayList<SalarioGuardia> alSalarios = new ArrayList<SalarioGuardia>();
        SalarioGuardia salario = null;

        //recuperamos para cada abadía el número de guardias y la pasta disponible.
        String sSQL = "select a.abadiaid, r.cantidad as guardias, r2.cantidad as monedas, u.idiomaid " +
                " from abadia a, recurso r, recurso r2, usuario u " +
                " where a.abadiaid = r.abadiaid and r.recursoid = 10 and r.cantidad > 0 and a.abadiaid = r2.abadiaid and " +
                " r2.recursoid = 0 and a.usuarioid = u.usuarioid and u.usuarioid not in (select usuarioid from usuario_bloqueo) order by idiomaid";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = getConexion().prepareStatement(sSQL);
            rs = ps.executeQuery();
            //por cada abbatia...
            while (rs.next()) {
                salario = new SalarioGuardia();
                salario.setIdAbadia(rs.getInt("abadiaid"));
                salario.setMonedas(rs.getDouble("monedas"));
                salario.setNumGuardias(rs.getInt("guardias"));
                salario.setIdIdioma(rs.getInt("idiomaid"));
                alSalarios.add(salario);
            }
            return alSalarios;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. recuperarSalariosGuardias. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Devuelve una hashtable donde tendremos, por cada monje otra hashtable
     * con una entrada por cada actividad con la producción asociada
     *
     * @return Hashtable
     * @throws org.abbatia.exception.AbadiaSQLException
     *          Excepción base de abbatia
     */
    public Hashtable<Integer, Hashtable<Integer, Double>> recuperarActividadesProduccion() throws AbadiaException {
        int last_monje = 0;

        Hashtable<Integer, Hashtable<Integer, Double>> htMonje = new Hashtable<Integer, Hashtable<Integer, Double>>();
        Hashtable<Integer, Double> htActividad = new Hashtable<Integer, Double>();
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            cs = getConexion().prepareCall("call recuperarActividadesProduccion()");
            cs.execute();
            rs = cs.getResultSet();
            //por cada abbatia...
            while (rs.next()) {
                if (last_monje != rs.getInt("monjeid")) {
                    if (last_monje != 0) {
                        htMonje.put(last_monje, htActividad);
                    }
                    last_monje = rs.getInt("monjeid");
                    htActividad = new Hashtable<Integer, Double>();
                }
                htActividad.put(rs.getInt("actividadid"), rs.getDouble("valor"));
            }
            if (!htActividad.isEmpty()) htMonje.put(last_monje, htActividad);

            return htMonje;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. recuperarActividadesProduccion. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(cs);
        }
    }


    /**
     * Recupera una {@link Hashtable} de objetos {@link ImpactoPorHabilidad} con los datos
     * del impacto que cada actividad tiene en cada habilidad de los monjes
     *
     * @return Hashtable
     * @throws org.abbatia.exception.AbadiaSQLException
     *          Excepción base de abbatia
     */
    public Hashtable<Integer, Hashtable<Integer, Double>> recuperarRelacionActHab() throws AbadiaException {
        Hashtable<Integer, Hashtable<Integer, Double>> htRelacion = new Hashtable<Integer, Hashtable<Integer, Double>>();
        Hashtable<Integer, Double> htHabilidad = new Hashtable<Integer, Double>();
        int last_actividad = -1;

        //recuperamos para cada abadía el número de guardias y la pasta disponible.
        String sSQL = "select * from actividad_habilidad order by actividadid";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = getConexion().prepareStatement(sSQL);
            rs = ps.executeQuery();
            //por cada actividad...
            while (rs.next()) {
                if (last_actividad != rs.getInt("actividadid")) {
                    if (last_actividad != -1) {
                        htRelacion.put(last_actividad, htHabilidad);
                    }
                    last_actividad = rs.getInt("actividadid");
                    htHabilidad = new Hashtable<Integer, Double>();
                }
                htHabilidad.put(rs.getInt("habilidadid"), rs.getDouble("IMPACTO"));
            }

            if (!htHabilidad.isEmpty()) htRelacion.put(last_actividad, htHabilidad);

            return htRelacion;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. recuperarRelacionActHab. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Carga en una {@link Hashtable} las parejas de valores edificioid - estresado (0 - No, 1 - Si)
     *
     * @return
     * @throws AbadiaException
     */
    public Hashtable<Integer, EdificioProceso> cargarStressPorEdificio() throws AbadiaException {
        adLibros oLibrosAD;
        Libro libro;
        EdificioProceso edificio;
        int stress;
        Hashtable<Integer, EdificioProceso> htEdificios = new Hashtable<Integer, EdificioProceso>();

        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            oLibrosAD = new adLibros(con);
            cs = getConexion().prepareCall("call cargarStressPorEdificio()");
            cs.execute();
            rs = cs.getResultSet();
            while (rs.next()) {
                edificio = new EdificioProceso();
                stress = rs.getInt("STRESS");
                if (stress == 1) {

                    libro = oLibrosAD.recuperaLibroTipo(Constantes.LIBRO_TIPO_CAPACITATE + rs.getInt("tipoedificioid"), rs.getInt("abadiaid"));

                    if (libro != null) {
                        if ((rs.getInt("max_almacenamiento") + (libro.getNivel() * rs.getInt("nivel") * 10) >= rs.getInt("volumen_edificio"))) {
                            stress = 0;
                        }
                    }
                }
                edificio.setExtresado(stress);
                edificio.setAlmacenamiento(rs.getInt("max_almacenamiento"));
                edificio.setVolumen(rs.getInt("volumen_edificio"));

                htEdificios.put(rs.getInt("EDIFICIOID"), edificio);
            }
            return htEdificios;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. cargarStressPorEdificios. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(cs);
        }


    }

    /**
     * Carga en una {@link Hashtable} las parejas de valores edificioid - estresado (0 - No, 1 - Si)
     *
     * @return
     * @throws AbadiaException
     */
    public Hashtable<Integer, EdificioProceso> cargarStressPorEdificioRegion(int idRegion) throws AbadiaException {
        adLibros oLibrosAD = null;
        Libro libro = null;
        EdificioProceso edificio = null;
        int stress;
        Hashtable<Integer, EdificioProceso> htEdificios = new Hashtable<Integer, EdificioProceso>();
        //String sSQLStress = "";
        /*
        sSQLStress = "SELECT e.edificioid, e.tipoedificioid, e.abadiaid, e.nivel, sum(volumen) > max(en.almacenamiento) stress, sum(volumen) volumen_edificio, max(en.almacenamiento) max_almacenamiento  " +
        "FROM `animales_crecimiento` ac, `animales` a, `edificio` e, `edificio_nivel` en, abbatia ab " +
        "where e.edificioid = a.edificioid and ac.tipo_animalid = a.tipo_animalid and ac.nivel = a.nivel and a.estado = 0 and " +
        "e.tipoedificioid = en.tipoedificioid and e.nivel = en.nivel and e.abadiaid = ab.abadiaid and ab.regionid = ? " +
        "group by e.edificioid, e.tipoedificioid, e.abadiaid, e.nivel ";
        */
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            oLibrosAD = new adLibros(con);
            cs = getConexion().prepareCall("call cargarStressPorEdificioRegion(?)");
            cs.setInt(1, idRegion);
            cs.execute();
            rs = cs.getResultSet();
            while (rs.next()) {
                edificio = new EdificioProceso();
                stress = rs.getInt("STRESS");
                if (stress == 1) {

                    libro = oLibrosAD.recuperaLibroTipo(Constantes.LIBRO_TIPO_CAPACITATE + rs.getInt("tipoedificioid"), rs.getInt("abadiaid"));

                    if (libro != null) {
                        if ((rs.getInt("max_almacenamiento") + (libro.getNivel() * rs.getInt("nivel") * 10) >= rs.getInt("volumen_edificio"))) {
                            stress = 0;
                        }
                    }
                }
                edificio.setExtresado(stress);
                edificio.setAlmacenamiento(rs.getInt("max_almacenamiento"));
                edificio.setVolumen(rs.getInt("volumen_edificio"));

                htEdificios.put(rs.getInt("EDIFICIOID"), edificio);
            }
            return htEdificios;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. cargarStressPorEdificios. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(cs);
        }


    }

    public ArrayList<MonjeElaboracion> cargarMonjesElaboracion(int estado, short tipo_elaboracion, int periodo, int tipo_edificio) throws AbadiaException {
        String sSQL = "";
        ArrayList<MonjeElaboracion> alMonjes = new ArrayList<MonjeElaboracion>();
        MonjeElaboracion monjeElaboracion = null;

        if (estado == Constantes.MONJE_VIVO) {
            sSQL = "SELECT m.monjeid, u.idiomaid, e.nivel, ab.abadiaid, e.edificioid " +
                    "FROM `monje` m, `actividad` a, abadia ab, usuario u, edificio e " +
                    "WHERE m.monjeid = a.monjeid and ab.abadiaid = m.abadiaid and ab.usuarioid = u.usuarioid and " +
                    "m.estado = " + Constantes.MONJE_VIVO + " AND a.actividadid = " + tipo_elaboracion + " and periodoid = ? and u.abadia_congelada = 0 and e.abadiaid=ab.abadiaid and e.tipoedificioid = " + tipo_edificio +
                    " order BY ab.abadiaid, u.idiomaid";
        } else if (estado == Constantes.MONJE_VISITA) {
            sSQL = "SELECT m.monjeid, u.idiomaid, e.nivel, ab.abadiaid, e.edificioid " +
                    "FROM `monje` m, `actividad` a, abadia ab, usuario u, edificio e, monje_visita mv " +
                    "WHERE m.monjeid = a.monjeid and mv.monjeid = m.monjeid and ab.abadiaid = mv.abadiaid_destino and ab.usuarioid = u.usuarioid and " +
                    "m.estado = " + Constantes.MONJE_VISITA + " AND a.actividadid = " + tipo_elaboracion + " and periodoid = ? and u.abadia_congelada = 0 and e.abadiaid=ab.abadiaid and e.tipoedificioid = " + tipo_edificio +
                    " order BY ab.abadiaid, u.idiomaid";
        }

        ResultSet rs = null;
        PreparedStatement ps = null;

        try {
            ps = getConexion().prepareStatement(sSQL);
            ps.setInt(1, periodo);
            rs = ps.executeQuery();

            while (rs.next()) {
                monjeElaboracion = new MonjeElaboracion();
                monjeElaboracion.setIdMonje(rs.getInt("MONJEID"));
                monjeElaboracion.setIdIdioma(rs.getInt("IDIOMAID"));
                monjeElaboracion.setIdAbadia(rs.getInt("ABADIAID"));
                monjeElaboracion.setIdEdificio(rs.getInt("EDIFICIOID"));
                alMonjes.add(monjeElaboracion);
            }

            return alMonjes;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adProcesos. cargarMonjesElaboracion. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }


    }

    /*    Regulariza los precios de la ciudad, si hay tantas ventas como estipula ventas_dia, entonces que
    */

    public void regularizaPrecios() throws AbadiaException {
        String sSQL = "SELECT * FROM mercados_ciudad_variaciones";
        adUtils utils = null;

        PreparedStatement ps = null;
        ResultSet rs = null;
        float precio = 0;
        try {
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();   // animales que deberían de pasar de nivel
            while (rs.next()) {
                precio = rs.getFloat("precio_actual");
                if (rs.getFloat("nr_ventas") > rs.getFloat("ventas_dia")) {
                    precio = precio + (float) 0.1;
                    if (precio > rs.getFloat("precio_maximo_cardenal"))
                        precio = rs.getFloat("precio_maximo_cardenal");
                } else {
                    if (rs.getFloat("nr_ventas") < rs.getFloat("ventas_dia")) {
                        precio = precio - (float) 0.1;
                        if (precio < rs.getFloat("precio_minimo_cardenal"))
                            precio = rs.getFloat("precio_minimo_cardenal");
                    }
                }
                utils = new adUtils(con);
                utils.execSQL("UPDATE mercados_ciudad_variaciones SET nr_ventas_dia_anterior = nr_ventas, nr_ventas = 0, precio_actual='" + precio + "' " +
                        " WHERE productoid=" + rs.getInt("productoid") +
                        " AND mercancia='" + rs.getString("mercancia") + "'");
                utils.execSQL("UPDATE mercados SET precio_actual = '" + precio + "'" +
                        " WHERE productoid=" + rs.getInt("productoid") +
                        " AND abadiaid=0 AND mercancia='" + rs.getString("mercancia") + "'");
            }
        } catch (SQLException e) {
            throw new AbadiaSQLException("ERROR: regularizaPrecios = " + e, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public HashMap<Short, ArrayList<AlimentoFamiliaProceso>> recuperarAlimentosPorFamiliaAbadia(int p_iAbadiaId) throws AbadiaException {
        String sSQL = "SELECT al.loteid, al.cantidad, at.familiaid, a.alimentoid, consumo_monje, proteinas, lipidos, hidratos_carbono, vitaminas " +
                "FROM `edificio` e, `alimentos` a, `alimentos_lote` al, `alimentos_tipo` at " +
                "where e.abadiaid = ? and a.edificioid = e.edificioid and al.loteid = a.loteid and " +
                "at.alimentoid = a.alimentoid and cantidad <> 0 and consumo_monje <> 0 " +
                "order by at.familiaid, fecha_caducidad";


        AlimentoFamiliaProceso oAlimentoFamilia = null;
        HashMap<Short, ArrayList<AlimentoFamiliaProceso>> hmAlimentosFamilia = new HashMap<Short, ArrayList<AlimentoFamiliaProceso>>();
        ArrayList<AlimentoFamiliaProceso> alLotesPorFamilia = new ArrayList<AlimentoFamiliaProceso>();
        short iFamiliaId = -1;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, p_iAbadiaId);
            rs = ps.executeQuery();   // animales que deberían de pasar de nivel
            while (rs.next()) {
                oAlimentoFamilia = new AlimentoFamiliaProceso();
                oAlimentoFamilia.setLoteId(rs.getInt("loteid"));
                oAlimentoFamilia.setProteinas(rs.getInt("proteinas"));
                oAlimentoFamilia.setHidratosCarbono(rs.getInt("hidratos_carbono"));
                oAlimentoFamilia.setVitaminas(rs.getInt("vitaminas"));
                oAlimentoFamilia.setLipidos(rs.getInt("lipidos"));
                oAlimentoFamilia.setConsumoMonje(rs.getFloat("consumo_monje"));
                oAlimentoFamilia.setCantidadLote(rs.getFloat("cantidad"));
                oAlimentoFamilia.setFamiliaId(rs.getShort("familiaid"));
                oAlimentoFamilia.setAlimentoId(rs.getInt("alimentoid"));
                //si es la primera pasada, inicializamos la familia con la actual
                if (iFamiliaId == -1) iFamiliaId = rs.getShort("familiaid");
                //si se ha cambiado de familia...
                if (iFamiliaId != rs.getInt("familiaid")) {
                    //cargamos el arraylist sobre la hash de familia
                    hmAlimentosFamilia.put(iFamiliaId, alLotesPorFamilia);
                    //limpiamos el arraylist
                    alLotesPorFamilia = new ArrayList<AlimentoFamiliaProceso>();
                    iFamiliaId = rs.getShort("familiaid");
                }
                //cargamos el objeto en el arraylist
                alLotesPorFamilia.add(oAlimentoFamilia);

            }
            if (!alLotesPorFamilia.isEmpty()) {
                hmAlimentosFamilia.put(oAlimentoFamilia.getFamiliaId(), alLotesPorFamilia);
            }
            return hmAlimentosFamilia;

        } catch (SQLException e) {
            throw new AbadiaSQLException("ERROR: recuperarAlimentosPorFamiliaAbadia = " + e, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }


    }

    public ArrayList<AlimentoFamiliaProceso> recuperarAlimentosPorAbadia(int p_iAbadiaId) throws AbadiaException {
        String sSQL = "SELECT al.loteid, al.cantidad, at.familiaid, a.alimentoid, consumo_monje, proteinas, lipidos, hidratos_carbono, vitaminas " +
                "FROM `edificio` e, `alimentos` a, `alimentos_lote` al, `alimentos_tipo` at " +
                "where e.abadiaid = ? and a.edificioid = e.edificioid and al.loteid = a.loteid and " +
                "at.alimentoid = a.alimentoid and cantidad <> 0 and consumo_monje <> 0 " +
                "order by fecha_caducidad ";


        AlimentoFamiliaProceso oAlimentoFamilia;
        ArrayList<AlimentoFamiliaProceso> alLotesPorFamilia = new ArrayList<AlimentoFamiliaProceso>();
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, p_iAbadiaId);
            rs = ps.executeQuery();
            while (rs.next()) {
                oAlimentoFamilia = new AlimentoFamiliaProceso();
                oAlimentoFamilia.setLoteId(rs.getInt("loteid"));
                oAlimentoFamilia.setFamiliaId(rs.getShort("familiaid"));
                oAlimentoFamilia.setAlimentoId(rs.getInt("alimentoid"));
                oAlimentoFamilia.setProteinas(rs.getInt("proteinas"));
                oAlimentoFamilia.setHidratosCarbono(rs.getInt("hidratos_carbono"));
                oAlimentoFamilia.setVitaminas(rs.getInt("vitaminas"));
                oAlimentoFamilia.setLipidos(rs.getInt("lipidos"));
                oAlimentoFamilia.setConsumoMonje(rs.getFloat("consumo_monje"));
                oAlimentoFamilia.setCantidadLote(rs.getFloat("cantidad"));
                alLotesPorFamilia.add(oAlimentoFamilia);
            }
            return alLotesPorFamilia;

        } catch (SQLException e) {
            throw new AbadiaSQLException("ERROR: recuperarAlimentosPorAbadia = " + e, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }


    }

}

