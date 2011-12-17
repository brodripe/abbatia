package org.abbatia.process.bbean;

import com.sun.rowset.CachedRowSetImpl;
import org.abbatia.adbean.*;
import org.abbatia.bbean.SicarioBBean;
import org.abbatia.bean.*;
import org.abbatia.core.CoreMail;
import org.abbatia.core.CoreTiempo;
import org.abbatia.dbms.ConnectionFactory;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaDBConnectionException;
import org.abbatia.exception.AbadiaIOException;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.process.adbean.adProcesos;
import org.abbatia.process.utils.ProcesosUtils;
import org.abbatia.utils.AbadiaConfiguracion;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.Utilidades;
import org.apache.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProcesosVariosBBean extends ProcesosUtils {

    private static Logger log = Logger.getLogger(ProcesosVariosBBean.class.getName());

    public ProcesosVariosBBean() throws AbadiaDBConnectionException {
        super();
    }

/*   Proceso que lanza el clima
  */

    public void cambiar_clima() throws AbadiaException {
        String sTrace = this.getClass() + ".cambiar_clima()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adProcesos oProcesosAD;
        adClima oClimaAD;
        adUtils oUtilsAD;

        ArrayList<ClimaRegion> alClima;

        int temp, tiempo, nieve;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);
            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando Proceso cambiar_clima", 0);
            oUtilsAD = new adUtils(con);
            oClimaAD = new adClima(con);
            alClima = oClimaAD.recuperarTemperaturas();
            for (ClimaRegion oClimaRegion : alClima) {
                temp = Utilidades.Random(oClimaRegion.getTemperaturaMinima(), oClimaRegion.getTemperaturaMaxima());
                tiempo = Utilidades.Random(oClimaRegion.getTiempoMinimo(), oClimaRegion.getTiempoMaximo());
                // Ejecutar
                oUtilsAD.setPropriedad(1, oClimaRegion.getIdRegion(), "R", String.valueOf(tiempo));
                oUtilsAD.setPropriedad(3, oClimaRegion.getIdRegion(), "R", String.valueOf(temp));
                // Establecer la cantidad de nieve...
                nieve = Integer.parseInt(oUtilsAD.getPropriedad(100, oClimaRegion.getIdRegion(), "R", "0"));
                switch (tiempo) {
                    case 19: // Nieve esporádica
                        nieve = nieve + 1;
                        break;
                    case 20: // Nieve fuerte esporádica
                        nieve = nieve + 6;
                        break;
                    case 21: // Nieve
                        nieve = nieve + 4;
                        break;
                    case 22: // Nieve fuerte
                        nieve = nieve + 8;
                        break;
                    default:
                        if (nieve > 0) {
                            if (temp > 15) nieve = 0;
                            if ((temp >= 10) && (temp < 15)) nieve = nieve - 50;
                            if ((temp > 5) && (temp < 10)) nieve = nieve - 10;
                            if (temp <= 5) nieve--;
                        }
                        break;
                }
                if (nieve < 0) nieve = 0;
                oUtilsAD.setPropriedad(100, oClimaRegion.getIdRegion(), "R", String.valueOf(nieve));
            }
            oProcesosAD.addLog("- Finalizando Proceso cambiar_clima", 0);

        } catch (Exception e) {
            throw new AbadiaSQLException(sTrace + " ERROR: cambiar_clima", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    /**
     * Control de nivel de abadías a partir de la puntuacion
     *
     * @throws AbadiaException
     */
    public void gestionNivelAbadias() throws AbadiaException {
        String sTrace = this.getClass() + ".gestionNivelAbadias()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adProcesos oProcesosAD;
        adMonje oMonjeAD;
        adUtils oUtilsAD;

        List<Monje> lMonjes;

        //recuperar puntuación de todas las abadias por tramos
        //por cada nivel, recuperamos las abadías (edificios abadía=99) que cumplen con los criterios
        //de nivel de abadía
        //se recuperan las abadías que no tienen el nivel que les corresponde a partir de sus puntuaciones.
        String sSQL = "select ap.abadiaid, an.nivel " +
                " from abadia_puntuacion ap, abadia_nivel an, edificio e, propiedad_valor pv " +
                " where ap.total between an.puntuacion_min and an.puntuacion_max and e.tipoedificioid = 99 and  " +
                "   e.abadiaid = ap.abadiaid and e.nivel <> an.nivel and pv.PROPIEDADID = 99 and " +
                "   ap.FECHA_ABADIA = date(pv.valor) " +
                " order by ap.abadiaid ";
//        String sSQLUpdate = "UPDATE edificio set nivel = ? where abadiaid = ? and tipoedificioid = 99 ";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);
            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando SubProceso gestionNivelAbadias", 0);
            oUtilsAD = new adUtils(con);
            oMonjeAD = new adMonje(con);
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                oUtilsAD.execSQL("UPDATE edificio set nivel = " + rs.getInt("NIVEL") + " WHERE abadiaid = " + rs.getInt("ABADIAID") + " AND tipoedificioid = 99");
                //despues de actualizar el nivel de edificio, sería necesario forzar la actualizacion de las dietas de los monjes de esa abadía.
                lMonjes = oMonjeAD.recuperarMonjes(rs.getInt("ABADIAID"), Constantes.MONJES_VIVOS, null);
                for (Monje oMonje : lMonjes) {
                    oMonje.setNivelAbadia(rs.getShort("NIVEL"));
                    oMonjeAD.actualizarAlimentacionSmart(oMonje);
                    oMonjeAD.actualizarAlimentacion(oMonje);
                }
            }
            oProcesosAD.addLog("+ Finalizado SubProceso gestionNivelAbadias", 0);
        } catch (Exception e) {
            throw new AbadiaSQLException("ERROR: gestionNivelAbadias = " + e, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }
    /* Sicarios!!!
          estado = 0 esperando
          estado = 1 viajando
          estado = 2 de vuelta
    */


    public void sicarios() throws AbadiaException {

        String sTrace = this.getClass() + ".sicarios()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adProcesos oProcesosAD;
        adUtils oUtilsAD;
        adSicarios oSicarioAD;
        SicarioBBean oSicarioBBean;

        // Averiguar cuantos sicarios pueden haber por region
        String sSQL = "SELECT regionid, round(count(*) / 25) as cont FROM abadia group by regionid";
        String mSQL = "SELECT * FROM sicario WHERE fecha_matanza <= ? AND estado = 1";

        // 12010 a la 12014


        int sicarios, sicatual, sic, moral, precio;
        String nombre, apellido;
        PreparedStatement ps = null;
        ResultSet rs = null;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);

            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando Proceso sicarios", 0);
            oUtilsAD = new adUtils(con);
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();

            while (rs.next()) {
                sicarios = rs.getInt("cont");
                if (sicarios == 0) sicarios = 1;        // Como mínimo uno por región
                // Averiguar cuanto sicarios tenemos actualmente...
                sicatual = oUtilsAD.getSQL("SELECT Count(*) From sicario WHERE regionid=" + rs.getInt("regionid"), 0);
                // Si tenemos una falta de sicarios.... añadir más ;-)
                if (sicatual < sicarios) {
                    for (sic = sicatual; sic < sicarios; sic++) {
                        nombre = oUtilsAD.getSQL("SELECT Descripcion FROM `tablas` where codigo like 'NOM_%' order by rand() limit 1", "x");
                        apellido = oUtilsAD.getSQL("SELECT nombre FROM `region_ciudades` order by rand() limit 1", "x1");
                        moral = Utilidades.Random(10, 100);
                        precio = Utilidades.Random(5000, (moral * 500) + 1);
                        oUtilsAD.execSQL("INSERT INTO `sicario` " +
                                " (`REGIONID`, `NOMBRE`, `APELLIDO`,  `PRECIO`, `MORAL`) " +
                                "VALUES  (" + rs.getInt("regionid") + ", '" + nombre + "', '" + apellido + "', " + precio + ", " + moral + ")");
                    }
                }
            }
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
            /* Algún sicario ha llegado ha destino?
            */
            ps = con.prepareStatement(mSQL);
            ps.setString(1, CoreTiempo.getTiempoAbadiaString());
            rs = ps.executeQuery();
            oSicarioBBean = new SicarioBBean();
            while (rs.next()) {
                oSicarioBBean.asesinar(rs.getInt("sicarioid"));
            }

            // QUe vuelvan los que quedan vivos!
            oUtilsAD.execSQL("UPDATE sicario SET estado=0 WHERE fecha_redisponible <= '" + CoreTiempo.getTiempoAbadiaString() + "'");

            oProcesosAD.addLog("- Finalizando Proceso sicarios", 0);

        } catch (Exception e) {
            throw new AbadiaSQLException("ERROR: sicarios = " + e, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);

        }

    }

    /*  Si los monjes no caben al dormir... entonces se quejan de que no caben
    */

    public void monjes_apretados_aldormir() throws AbadiaException {
        String sTrace = this.getClass() + ".monjes_apretados_aldormir()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adProcesos oProcesosAD;
        adUtils oUtilsAD;
        adMonje oMonjeAD;
        adLibros oLibrotipoAD;
        adEdificio oEdificioAD;
        adLiterales oLiteralesAD;
        adMensajes oMensajesAD;


        String sSQL = "SELECT e.abadiaid, e.nivel, almacenamiento, ( Select Count(*) from monje where abadiaid = e.abadiaid and estado = " + Constantes.MONJE_VIVO + ") + (select count(*) from monje_visita mv, monje m where e.abadiaid = abadiaid_destino and estado = " + Constantes.MONJE_VISITA + " and m.monjeid = mv.monjeid)" +
                " - ( select count(*) from monje where abadiaid = e.abadiaid and estado = " + Constantes.MONJE_VIVO + " and jerarquiaid >= " + Constantes.JERARQUIA_OBISPO + ")" +
                " - (select count(*) from monje m, edificio ed, monje_visita mv  where mv.monjeid = m.monjeid and e.abadiaid = ed.abadiaid and mv.abadiaid_destino = e.abadiaid and m.jerarquiaid >= " + Constantes.JERARQUIA_OBISPO +
                " and ed.tipoedificioid =" + Constantes.EDIFICIO_HOSPEDERIA + " and m.estado = " + Constantes.MONJE_VISITA + ") total" +
                " FROM `edificio_nivel` en, edificio e " +
                " WHERE en.tipoedificioid = e.tipoedificioid and en.nivel = e.nivel and e.tipoedificioid = " + Constantes.EDIFICIO_DORMITORIO +
                " and en.almacenamiento < ( Select Count(*) from monje where abadiaid = e.abadiaid and estado = " + Constantes.MONJE_VIVO + ") + (select count(*) from monje_visita mv, monje m where e.abadiaid = abadiaid_destino and estado = " + Constantes.MONJE_VISITA + " and m.monjeid = mv.monjeid)" +
                " - ( select count(*) from monje where abadiaid = e.abadiaid and estado = " + Constantes.MONJE_VIVO + " and jerarquiaid >= " + Constantes.JERARQUIA_OBISPO + ")" +
                " - (select count(*) from monje m, edificio ed, monje_visita mv  where mv.monjeid = m.monjeid and e.abadiaid = ed.abadiaid and mv.abadiaid_destino = e.abadiaid and m.jerarquiaid >= " + Constantes.JERARQUIA_OBISPO +
                " and ed.tipoedificioid =" + Constantes.EDIFICIO_HOSPEDERIA + " and m.estado = " + Constantes.MONJE_VISITA + ") ";

        //String sSQLMonje = "Select abadiaid, m.monjeid, jerarquiaid from monje m, monje_alimentacion ma where m.monjeid = ma.monjeid and ma.salud <= 0 and m.estado <> 1";


        ArrayList<MonjeProceso> alMonjes;
        ArrayList<Mensajes> mensajes = new ArrayList<Mensajes>();
        String msg1;
        int almacenamiento_mas_libro = 0;
        int idAbadia;
        int iNivelEdificio;
        int iTotalMonjes;
        int iTotalEnfermos;
        int iTotalEnfermosVisita;
        int iPropiedad;
        int idIdioma;

        Libro libro;

        PreparedStatement ps = null;
        ResultSet rs = null;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);

            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando Proceso monjes_apretados_aldormir", 0);
            oEdificioAD = new adEdificio(con);
            oMonjeAD = new adMonje(con);
            oLibrotipoAD = new adLibros(con);
            oUtilsAD = new adUtils(con);
            oLiteralesAD = new adLiterales(con);

            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();

            while (rs.next()) {
                iTotalMonjes = rs.getInt("total");
                idAbadia = rs.getInt("abadiaid");
                iNivelEdificio = rs.getInt("nivel");

                //si la abbatia dispone de enfermeria...
                if (oEdificioAD.existeEdificioTipo(idAbadia, Constantes.EDIFICIO_ENFERMERIA)) {
                    //obtenemos los posibles monjes enfermos..
                    iTotalEnfermos = oMonjeAD.getNumMonjesEnfermos(idAbadia);
                    iTotalEnfermosVisita = oMonjeAD.getNumMonjesVisitaEnfermos(idAbadia);
                    //los restamos del total
                    iTotalMonjes = iTotalMonjes - (iTotalEnfermos + iTotalEnfermosVisita);
                }

                libro = oLibrotipoAD.recuperaLibroTipo(300 + Constantes.EDIFICIO_DORMITORIO, idAbadia);

                if (libro != null) {
                    almacenamiento_mas_libro = Utilidades.recuperarIncrementoEspacioEdificioPorLibro(Constantes.EDIFICIO_DORMITORIO, libro.getNivel(), iNivelEdificio);
                }

                // el libro de tipo 300, aumenta el nivel de espacio en el dormitorio.
                almacenamiento_mas_libro = rs.getInt("almacenamiento") + almacenamiento_mas_libro;


                if (almacenamiento_mas_libro < iTotalMonjes) {
                    //recuperamos los n monjes mas viejos de la abadía.
                    //donde n es el número excedente de monjes.
                    //no se deben recuperar las eminencias que estén en la hospederia
                    alMonjes = oMonjeAD.recuperarMonjesMayores(idAbadia, iTotalMonjes - almacenamiento_mas_libro);
                    for (MonjeProceso monje : alMonjes) {
                        //por cada monje, restamos n puntos de proteinas.
                        //random para determinar qué propiedad se le pone a 1

                        iPropiedad = Utilidades.Random(1, 4);
                        //si propiedad = 1, ponemos las vitaminas a 1
                        if (iPropiedad == 1) {
                            oMonjeAD.asignarValorPropiedad(monje.getIdMonje(), "VITAMINAS", 1);
                        } else if (iPropiedad == 2) {
                            oMonjeAD.asignarValorPropiedad(monje.getIdMonje(), "PROTEINAS", 1);
                        } else if (iPropiedad == 3) {
                            oMonjeAD.asignarValorPropiedad(monje.getIdMonje(), "LIPIDOS", 1);
                        } else if (iPropiedad == 4) {
                            oMonjeAD.asignarValorPropiedad(monje.getIdMonje(), "HIDRATOS_CARBONO", 1);
                        }
                    }

                    idIdioma = oUtilsAD.getIdiomaID(idAbadia);
                    msg1 = oLiteralesAD.getLiteralStatic(12150, idIdioma);

                    // Mensajes
                    mensajes.add(new Mensajes(idAbadia, -1, msg1, 1));
                }
            }
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
            // Pasar todos los mensajes almacenados a pantalla
            oMensajesAD = new adMensajes(con);
            oMensajesAD.crearMensajes(mensajes);

            oProcesosAD.addLog("- Finalizando Proceso monjes_apretados_aldormir", 0);

        } catch (Exception e) {
            throw new AbadiaSQLException("ERROR: monjes_apretados_aldormir. Exception. ", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    /**
     * Rutina que controla los viajes
     *
     * @throws org.abbatia.exception.base.AbadiaException
     *          excepción general
     */
    public void viajes() throws AbadiaException {
        String sTrace = this.getClass() + ".viajes()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adProcesos oProcesosAD;
        adUtils oUtilsAD;
        adMonje oMonjeAD;
        adLibros oLibroAD;
        adAbadia oAbadiaAD;
        adLiterales oLiteralesAD;
        adViajar oViajarAD;
        adEdificio oEdificioAD;
        adAnimal oAnimalAD;
        adMensajes oMensajesAD;


        String sSQL_llegada_destino = "select * from monje_visita mv, monje m\n" +
                "where mv.monjeid = m.monjeid and m.estado = " + Constantes.MONJE_VIAJE + " and fecha_llegada_destino <= ? " +
                "                           and fecha_llegada_origen is null ";
        String sSQL_partida_origen = "select * from monje_visita mv, monje m\n" +
                "where mv.monjeid = m.monjeid  and m.estado = " + Constantes.MONJE_VISITA + " and " +
                "fecha_partida_origen is not null and fecha_partida_origen <= ? and fecha_llegada_origen is null ";
        String sSQL_llegada_origen = "select * from monje_visita mv, monje m\n" +
                "where mv.monjeid = m.monjeid and m.estado = " + Constantes.MONJE_VIAJE + " and " +
                "fecha_llegada_origen is not null and fecha_llegada_origen <= ? ";
        String DEL_llegada_origen = "DELETE monje_visita FROM monje_visita, monje\n" +
                "                                        where monje_visita.monjeid = monje.monjeid and monje.estado = 0 and monje_visita.fecha_partida_destino is not null ";

        String msg1, msg2, literalDe;
        int idm, idm2;
        int idEstablo;
        boolean existeCaballo = false;
        String sSQLUpdate;

        ArrayList<Mensajes> mensajes = new ArrayList<Mensajes>();

        PreparedStatement ps = null;
        ResultSet rs = null;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);

            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando Proceso viajes", 0);
            oAbadiaAD = new adAbadia(con);
            oLiteralesAD = new adLiterales(con);
            oAnimalAD = new adAnimal(con);
            oUtilsAD = new adUtils(con);
            oLibroAD = new adLibros(con);
            oMonjeAD = new adMonje(con);
            oEdificioAD = new adEdificio(con);
            oViajarAD = new adViajar(con);
            // HEMOS LLEGADO A DESTINO?????
            ps = con.prepareStatement(sSQL_llegada_destino);
            ps.setString(1, CoreTiempo.getTiempoAbadiaString());
            rs = ps.executeQuery();
            while (rs.next()) {

                // Establecer el estado a 4 para indicar que el monje está de visita
                oUtilsAD.execSQL("UPDATE monje SET estado = " + Constantes.MONJE_VISITA + " WHERE monjeid = " + rs.getInt("monjeid"));
                // Notificar ambas abadias...
                // Ha llegado a la abadía %s de visita
                idm = oUtilsAD.getIdiomaID(rs.getInt("abadiaid"));
                idm2 = oUtilsAD.getIdiomaID(rs.getInt("abadiaid_destino"));

                msg1 = oLiteralesAD.getLiteralStatic(12100, idm);
                // El %s %s de la abbatia %s a llegado de visita
                msg2 = oLiteralesAD.getLiteralStatic(12101, idm2);
                literalDe = oLiteralesAD.getLiteralStatic(5000, idm2);

                //
                //abadiaAD = new adAbadia(Constantes.DB_CONEXION_PROCESS);
                msg1 = Utilidades.Format(msg1, oAbadiaAD.getNomAbadia(rs.getInt("abadiaid_destino")));
                //abadiaAD.finalize();

                msg2 = Utilidades.Format(msg2, oMonjeAD.getDescJerarquiaMonje(rs.getInt("monjeid"), idm2));

                msg2 = Utilidades.Format(msg2, oMonjeAD.getNomMonje(rs.getInt("monjeid"), literalDe));

                //abadiaAD = new adAbadia(Constantes.DB_CONEXION_PROCESS);
                msg2 = Utilidades.Format(msg2, oAbadiaAD.getNomAbadia(rs.getInt("abadiaid")));
                //abadiaAD.finalize();

                // Mensajes
                mensajes.add(new Mensajes(rs.getInt("abadiaid"), rs.getInt("monjeid"), msg1, 0));
                mensajes.add(new Mensajes(rs.getInt("abadiaid_destino"), -1, msg2, 0));
                //procesamos la actualizacion del estado y el idEdificio de los animales que viajan con el monje (si existen..)

                ArrayList<Integer> idAnimales = oViajarAD.recuperarAnimalesVisita(rs.getInt("monjeid"));
                //si existen animales...
                if (idAnimales.size() > 0) {
                    //recuperamos el id del edificio establo de la abbatia a la que llegamos
                    idEstablo = oEdificioAD.recuperarIdEdificioTipo(Constantes.EDIFICIO_ESTABLO, rs.getInt("abadiaid_destino"));
                    for (int idAnimal : idAnimales) {
                        //actualizamos el idedificio del registro animal y el estado....
                        oAnimalAD.actualizarEstadoEdificio(idAnimal, Constantes.ESTADO_ANIMAL_VIVO, idEstablo);
                    }
                }

                //miramos si el viaje tenía como objetivo la copia de un libro
                if (rs.getShort("TIPO_VISITA") == Constantes.VIAJE_TIPO_COPIA) {
                    //recuperamos el idLibro que hemos venido a copiar
                    int idLibro = oLibroAD.recuperarLibroPorTareaMonje(rs.getInt("MONJEID"));
                    //recuperamos la informacion del libro que hemos venido a copiar
                    Libro libro = oLibroAD.recuperarLibro(idLibro, idm);
                    //Si hay alguna copia del libro que quiero copiar perteneciente a mi abbatia en estado "incompleto"
                    //continuo con la copia de ese libro en lugar de crear uno nuevo...
                    int idLibroCopia = oLibroAD.existeCopiaIncompletaTipo(libro.getIdLibroTipo(), libro.getNivel(), rs.getInt("abadiaid"), rs.getInt("abadiaid_destino"));
                    //oLibroAD.finalize();

                    if (idLibroCopia == 0) {
                        //recuperamos el idioma de la region a la que pertenece la abbatia origen
                        int idIdiomaRegion = oAbadiaAD.recuperarIdiomaRegionAbadia(rs.getInt("abadiaid"));
                        //creamos la copia del libro
                        idLibroCopia = oLibroAD.crearRegistroCopiaProceso(libro, rs.getInt("abadiaid"), idIdiomaRegion);
                    } else {
                        oLibroAD.actualizarEstadoLibro(idLibroCopia, Constantes.ESTADO_LIBRO_COPIANDOSE);
                    }
                    //actualizamos el estado y el idlibro de la tabla libro/copia
                    oLibroAD.actualizarIdLibroCopiaTarea(idLibro, idLibroCopia, rs.getInt("MONJEID"));
                }
                // Hay Hospederia?
                // Decrementar la Fe a todos los monjes!
                /* adHabilidad adhabil = new adHabilidad();
               adhabil.incrementarHabilidad(abbatia.getIdDeAbadia(), Integer.parseInt(sClave), Constantes.HABILIDAD_FE, -15);
               adhabil.finalize(); */
                // Esta a tope?
                // Caballos!
            }

            // TENEMOS QUE MARCHAR A ORIGEN?????
            ps = con.prepareStatement(sSQL_partida_origen);
            ps.setString(1, CoreTiempo.getTiempoAbadiaString());
            rs = ps.executeQuery();
            while (rs.next()) {
                // Establecer el estado a 3 para indicar que el monje está de viaje
                oUtilsAD.execSQL("UPDATE monje SET estado = " + Constantes.MONJE_VIAJE + " WHERE monjeid = " + rs.getInt("monjeid"));
                idm2 = oUtilsAD.getIdiomaID(rs.getInt("abadiaid_destino"));
                // Notificar ambas abadias...
                // Incia el viaje de regreso a la abadía
                msg1 = oLiteralesAD.getLiteralStatic(12102, oUtilsAD.getIdiomaID(rs.getInt("abadiaid")));
                // El %s %s de la abadía %s inicia el viaje de regreso a su abadía
                msg2 = oLiteralesAD.getLiteralStatic(12103, idm2);
                literalDe = oLiteralesAD.getLiteralStatic(5000, idm2);

                //

                msg2 = Utilidades.Format(msg2, oMonjeAD.getDescJerarquiaMonje(rs.getInt("monjeid"), idm2));
                msg2 = Utilidades.Format(msg2, oMonjeAD.getNomMonje(rs.getInt("monjeid"), literalDe));
                msg2 = Utilidades.Format(msg2, oAbadiaAD.getNomAbadia(rs.getInt("abadiaid")));

                // Mensajes
                mensajes.add(new Mensajes(rs.getInt("abadiaid"), rs.getInt("monjeid"), msg1, 0));
                mensajes.add(new Mensajes(rs.getInt("abadiaid_destino"), -1, msg2, 0));
                // Caballos!

                //procesamos la actualizacion del estado y el idEdificio de los animales que viajan con el monje (si existen..)
                ArrayList<Integer> idAnimales = oViajarAD.recuperarAnimalesVisita(rs.getInt("monjeid"));

                //si existen animales...
                if (idAnimales.size() > 0) {
                    //recuperamos el id del edificio establo de la abbatia a la que llegamos
                    idEstablo = oEdificioAD.recuperarIdEdificioTipo(Constantes.EDIFICIO_ESTABLO, rs.getInt("abadiaid"));
                    for (int idAnimale : idAnimales) {
                        //verificamos por cada animal si sigue existiendo en la abbatia destino
                        if (oAnimalAD.existeAnimal(idAnimale)) {
                            existeCaballo = true;
                            //actualizamos el idedificio del registro animal y el estado....
                            oAnimalAD.actualizarEstadoEdificio(idAnimale, Constantes.ESTADO_ANIMAL_VIAJANDO, idEstablo);
                        }
                        //animalAD.finalize();
                    }
                }
                //verificamos si la abadía receptora sigue teniendo caballo para la vuelta
                if (existeCaballo) {
                    sSQLUpdate = "UPDATE monje_visita SET fecha_llegada_origen = '" + CoreTiempo.getDiferenciaString(rs.getInt("dias_caballo")) + "' WHERE monjeid = " + rs.getInt("monjeid");
                } else {
                    sSQLUpdate = "UPDATE monje_visita SET fecha_llegada_origen = '" + CoreTiempo.getDiferenciaString(rs.getInt("dias_pie")) + "' WHERE monjeid = " + rs.getInt("monjeid");
                }

                // Marcar la vuelta
                oUtilsAD.execSQL(sSQLUpdate);
            }

            // HEMOS VUELTO A ORIGEN?????
            ps = con.prepareStatement(sSQL_llegada_origen);
            ps.setString(1, CoreTiempo.getTiempoAbadiaString());
            rs = ps.executeQuery();
            //oUtilsAD = new adUtils(getConexion());
            while (rs.next()) {
                // Establecer el estado a 0 para indicar que el monje ha vuelto a casa
                oUtilsAD.execSQL("UPDATE monje SET estado = " + Constantes.MONJE_VIVO + " WHERE monjeid = " + rs.getInt("monjeid"));
                // Notificar abbatia
                // Ha llegado de su viaje
                msg1 = oLiteralesAD.getLiteralStatic(12104, oUtilsAD.getIdiomaID(rs.getInt("abadiaid")));

                //si el tipo de viaje es de copia...
                if (rs.getShort("TIPO_VISITA") == Constantes.VIAJE_TIPO_COPIA) {
                    //recuperamos del campo dummy el codigo del libro
                    String idLibro = rs.getString("DUMMY");
                    //si existe codigo de libro asociado....
                    if (idLibro != null && !idLibro.equals("")) {
                        //actualizamos el estado del libro
                        oLibroAD.actualizarEstadoLibro(Integer.valueOf(idLibro), Constantes.ESTADO_LIBRO_SIN_ENCUADERNAR);
                    }
                }

                //procesamos la actualizacion del estado y el idEdificio de los animales que viajan con el monje (si existen..)
                //viajarAD = new adViajar(getConexion());
                ArrayList<Integer> idAnimales = oViajarAD.recuperarAnimalesVisita(rs.getInt("monjeid"));
                //viajarAD.finalize();
                //si existen animales...
                if (idAnimales.size() > 0) {
                    for (int idAnimale : idAnimales) {
                        //actualizamos el idedificio del registro animal y el estado....
                        oAnimalAD.actualizarEstado(idAnimale, Constantes.ESTADO_ANIMAL_VIVO);
                    }
                }

                // Mensajes
                mensajes.add(new Mensajes(rs.getInt("abadiaid"), rs.getInt("monjeid"), msg1, 0));
            }

            // Borrar los registros que ya estan ok
            oUtilsAD.execSQL(DEL_llegada_origen);

            // Pasar todos los mensajes almacenados a pantalla
            oMensajesAD = new adMensajes(con);
            oMensajesAD.crearMensajes(mensajes);

            oProcesosAD.addLog("- Finalizando Proceso viajes", 0);

        } catch (Exception e) {
            throw new AbadiaSQLException("ERROR: viajes. Exception ", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    /* Bloquear los usuarios
    */

    public void borrarAbadiasAntiguas() throws AbadiaException {
        String sTrace = this.getClass() + ".borrarAbadiasAntiguas()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adProcesos oProcesosAD;
        adAbadia oAbadiaAD;
        adViajar oViajarAD;

        ArrayList<Integer> alAbadias = new ArrayList<Integer>();

        String sSQL = "select a.abadiaid from usuario u, abadia a " +
                "where u.usuarioid = a.usuarioid and u.abadia_congelada=0 and ultimaconexion is not null AND ultimaconexion < Date_Sub(Now(), Interval 20 day) and u.usuario_tipo = 0 and u.registrado = 0";

        PreparedStatement ps = null;
        ResultSet rs = null;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);

            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando Proceso borrar_abadiasAntiguas", 0);
            oViajarAD = new adViajar(con);
            oAbadiaAD = new adAbadia(con);

            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();

            while (rs.next()) {
                alAbadias.add(rs.getInt("abadiaid"));
            }
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);

            for (int idAbadia : alAbadias) {
                //solo eliminaremos la abadía si no tiene monjes de visita ni viajando
                //de este modo evitaremos los problemas de monjes huerfanos.
                if (!oViajarAD.monjesViajando(idAbadia) && !oViajarAD.monjesVisita(idAbadia)) {
                    oAbadiaAD.eliminarTodoAbadia(idAbadia);
                }
            }

            oProcesosAD.addLog("- Finalizando Proceso borrar_abadiasAntiguas", 0);

        } catch (Exception e) {
            throw new AbadiaSQLException("ProcesosVariosBBean. borrar_abadiasAntiguas. Exception", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public void borrarAbadiasMarcadas() throws AbadiaException {
        String sTrace = this.getClass() + ".borrarAbadiasMarcadas()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adProcesos oProcesosAD;
        adAbadia oAbadiaAD;

        ArrayList<Integer> alAbadias = new ArrayList<Integer>();
        String sSQL = "select a.abadiaid from abadia a " +
                "where a.FECHA_ELIMINACION < Date_Sub(Now(), Interval " + Constantes.VARIOS_DIAS_ELIMINACION + " day) ";

        PreparedStatement ps = null;
        ResultSet rs = null;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);

            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando Proceso borrar_abadiasMarcadas", 0);

            oAbadiaAD = new adAbadia(con);
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();

            while (rs.next()) {
                alAbadias.add(rs.getInt("abadiaid"));
            }
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);

            for (Integer iAbadiaId : alAbadias) {
                oAbadiaAD.eliminarTodoAbadia(iAbadiaId);
            }

            oProcesosAD.addLog("- Finalizando Proceso borrarAbadiasMarcadas", 0);

        } catch (Exception e) {
            throw new AbadiaSQLException("ProcesosVariosBBean. borrarAbadiasMarcadas. Exception", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    /**
     * Gestiona la eliminación de bloqueos al concluir el perido de sanción.
     *
     * @throws AbadiaException excepción general
     */
    public void gestionBloqueos() throws AbadiaException {
        String sTrace = this.getClass() + ".gestionBloqueos()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adProcesos oProcesosAD;
        adUtils oUtilsAD;

        String sSQL = "delete from usuario_bloqueo where DATE_ADD(fecha_bloqueo, INTERVAL dias_bloqueo DAY) <= curdate() and dias_bloqueo > 0 ";

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);

            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando Proceso gestionBloqueos", 0);
            oUtilsAD = new adUtils(con);
            oUtilsAD.execSQL(sSQL);
            oProcesosAD.addLog("- Finalizando Proceso gestionBloqueos", 0);
        } catch (Exception e) {
            throw new AbadiaSQLException("ProcesosVariosBBean. borrarAbadiasMarcadas. Exception", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    /* El pueblo tributa
    */

    public void tributos_pueblo() throws AbadiaException {

        String sTrace = this.getClass() + ".tributos_pueblo()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adProcesos oProcesosAD;
        adUtils oUtilsAD;
        adLibros oLibrosAD;
        adLiterales oLiteralesAD;
        adMensajes oMensajesAD;


        String sSQL = "SELECT a.abadiaid, count(*) monjes, max(r.cantidad) aldeanos, max(idiomaid) idiomaid " +
                "FROM `abadia` a, `usuario` u, monje m, recurso r " +
                "where a.usuarioid = u.usuarioid and u.abadia_congelada = 0  and m.abadiaid = a.abadiaid and m.estado = 0 and r.abadiaid = a.abadiaid and r.recursoid = 20 " +
                "group by a.abadiaid ";

        String sSQLTributos = "UPDATE recurso SET cantidad = cantidad + ? WHERE RecursoID = 0 AND abadiaID = ? ";


        Libro libro;
        ArrayList<Mensajes> mensajes = new ArrayList<Mensajes>();

        int min, max, val, last_idiomaid = 0;
        int cardenal_abadia = 0;
        int papa_abadia = 0;
        int obispo_abadia = 0;
        int abadia_ant = 0;
        String msg1 = "", msg2 = "";
        PreparedStatement ps = null;
        PreparedStatement psrec = null;
        ResultSet rs = null;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);

            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando Proceso tributos_pueblo", 0);
            oUtilsAD = new adUtils(con);
            oLibrosAD = new adLibros(con);
            oLiteralesAD = new adLiterales(con);

            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (abadia_ant != rs.getInt("abadiaid")) {
                    obispo_abadia = oUtilsAD.getSQL("SELECT monjeid FROM monje WHERE  " +
                            " jerarquiaid = " + Constantes.JERARQUIA_OBISPO + " AND estado=" + Constantes.MONJE_VIVO + " AND abadiaid = " + rs.getInt("abadiaid"), 0);
                    if (obispo_abadia == 0)  // Lo tenemos de visita en nuestra abadía?
                        obispo_abadia = oUtilsAD.getSQL("SELECT m.monjeid FROM monje m, monje_visita mv WHERE  " +
                                "m.jerarquiaid = " + Constantes.JERARQUIA_OBISPO + " AND m.estado=" + Constantes.MONJE_VISITA + " AND m.monjeid = mv.monjeid AND mv.abadiaid_destino = " + rs.getInt("abadiaid"), 0);

                    // Cardenal
                    cardenal_abadia = oUtilsAD.getSQL("SELECT monjeid FROM monje WHERE  " +
                            " jerarquiaid = " + Constantes.JERARQUIA_CARDENAL + " AND estado=" + Constantes.MONJE_VIVO + " AND abadiaid = " + rs.getInt("abadiaid"), 0);
                    if (cardenal_abadia == 0)    // Lo tenemos de visita en nuestra abadía?
                        cardenal_abadia = oUtilsAD.getSQL("SELECT m.monjeid FROM monje m, monje_visita mv WHERE  " +
                                "m.jerarquiaid = " + Constantes.JERARQUIA_CARDENAL + " AND m.estado=" + Constantes.MONJE_VISITA + " AND m.monjeid = mv.monjeid AND mv.abadiaid_destino = " + rs.getInt("abadiaid"), 0);

                    // Papa
                    papa_abadia = oUtilsAD.getSQL("SELECT monjeid FROM monje WHERE  " +
                            " jerarquiaid = " + Constantes.JERARQUIA_PAPA + " AND estado=" + Constantes.MONJE_VIVO + " AND abadiaid = " + rs.getInt("abadiaid"), 0);
                    if (papa_abadia == 0)    // Lo tenemos de visita en nuestra abadía?
                        papa_abadia = oUtilsAD.getSQL("SELECT m.monjeid FROM monje m, monje_visita mv WHERE  " +
                                "m.jerarquiaid = " + Constantes.JERARQUIA_PAPA + " AND m.estado=" + Constantes.MONJE_VISITA + " AND m.monjeid = mv.monjeid AND mv.abadiaid_destino = " + rs.getInt("abadiaid"), 0);
                    abadia_ant = rs.getInt("abadiaid");
                }


                min = rs.getInt("monjes");
                max = rs.getInt("monjes") * rs.getInt("aldeanos");

                if (obispo_abadia > 0) {
                    min = min + 1000;
                    max = max + 1000;
                }
                if (cardenal_abadia > 0) {
                    min = min + 2000;
                    max = max + 2000;
                }
                if (papa_abadia > 0) {
                    min = min + 3000;
                    max = max + 3000;
                }

                val = Utilidades.Random(min, max);

                //utilizar el carta caritatis para incrementar las donaciones del pueblo en los siguientes términos:
                //nivel 1 donación + 10%
                //nivel 2 donación + 20%
                //nivel 3 donación + 30%
                //nivel 4 donación + 40%
                libro = oLibrosAD.recuperaLibroTipo(Constantes.LIBRO_TIPO_CARTA_CARITATIS, rs.getInt("abadiaid"));
                if (libro != null) {
                    switch (libro.getNivel()) {
                        case 1:
                            val += val * 10 / 100;
                            break;
                        case 2:
                            val += val * 20 / 100;
                            break;
                        case 3:
                            val += val * 30 / 100;
                            break;
                        case 4:
                            val += val * 40 / 100;
                            break;
                    }
                }

                if (last_idiomaid != rs.getInt("idiomaid")) {
                    last_idiomaid = rs.getInt("idiomaid");
                    //oUtilsAD = new adUtils(Constantes.DB_CONEXION_PROCESS);
                    msg1 = oLiteralesAD.getLiteralStatic(10020, last_idiomaid);
                    msg2 = oLiteralesAD.getLiteralStatic(10021, last_idiomaid);
                    //oUtilsAD.finalize();
                }
                if (val == 0) {
                    mensajes.add(new Mensajes(rs.getInt("abadiaid"), -1, msg1, 0));
                } else {
                    psrec = con.prepareStatement(sSQLTributos);
                    psrec.setInt(1, val);
                    psrec.setInt(2, rs.getInt("abadiaid"));
                    psrec.execute();
                    mensajes.add(new Mensajes(rs.getInt("abadiaid"), -1, Format(msg2, val), 0));
                }
            }
            oMensajesAD = new adMensajes(con);
            oMensajesAD.crearMensajes(mensajes);

            oProcesosAD.addLog("- Finalizando Proceso tributos_pueblo", 0);

        } catch (Exception e) {
            throw new AbadiaSQLException("ERROR: tributos_pueblo = " + e, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(psrec);
            DBMSUtils.cerrarObjetoSQL(ps);
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    /* La suciedad aumenta en abadias grandes
    */

    public void suciedad() throws AbadiaException {
        String sTrace = this.getClass() + ".suciedad()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adProcesos oProcesosAD;
        adUtils oUtilsAD;

        String sSQL = "SELECT m.abadiaid, count(*) total, cantidad " +
                "FROM recurso r, abadia a, usuario u " +
                "WHERE estado = 0 and  u.usuarioid = a.usuarioid and u.abadia_congelada = 0 and r.abadiaid = a.abadiaid and r.recursoid =  " +
                Constantes.RECURSOS_SUCIEDAD + " group by m.abadiaid";

        String sSQLAumentar = "UPDATE recurso SET cantidad = ? WHERE RecursoID = " + Constantes.RECURSOS_SUCIEDAD +
                " AND abadiaID = ? ";

        int val = 0;
        int ratas = 0;
        int aldeanos = 0;
        int edificios = 0;
        int animales = 0;
        int monjes = 0;
        int monjes_visita = 0;

        int retrete = 0;
        int suciedad = 0;
        int abadia_ant = 0;

        PreparedStatement ps = null;
        PreparedStatement psrec = null;
        ResultSet rs = null;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);

            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando Proceso suciedad", 0);
            oUtilsAD = new adUtils(con);

            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (abadia_ant != rs.getInt("abadiaid")) {
                    ratas = oUtilsAD.getSQL(new StringBuilder().append("SELECT cantidad FROM recursos WHERE  ").append(" RecursoID = ").append(Constantes.RECURSOS_RATAS).append(" AND abadiaid = ").append(rs.getInt("abadiaid")).toString(), 0);
                    aldeanos = oUtilsAD.getSQL(new StringBuilder().append("SELECT cantidad FROM recursos WHERE  ").append(" RecursoID = ").append(Constantes.RECURSOS_ALDEANOS).append(" AND abadiaid = ").append(rs.getInt("abadiaid")).toString(), 0);
                    edificios = oUtilsAD.getSQL(new StringBuilder().append("SELECT sum(nivel) FROM edificios WHERE  ").append(" abadiaid = ").append(rs.getInt("abadiaid")).toString(), 0);
                    animales = oUtilsAD.getSQL(new StringBuilder().append("SELECT count(a.animalid) FROM animales a,edificio e WHERE  ").append("a.estado = 0  AND e.abadiaid = ").append(rs.getInt("abadiaid")).append(" AND e.tipoedificioid =").append(Constantes.EDIFICIO_GRANJA).append(" AND e.edificioid = a.edificioid").toString(), 0);
                    monjes_visita = oUtilsAD.getSQL(new StringBuilder().append("SELECT count(m.monjeid) FROM monje m, monje_visita mv WHERE  ").append(" mv.abadiaid_destino = ").append(rs.getInt("abadiaid")).append(" AND m.estado = ").append(Constantes.MONJE_VISITA).append(" AND mv.monjeid = m.monjeid").toString(), 0);
                    monjes = oUtilsAD.getSQL(new StringBuilder().append("SELECT count(monjeid) FROM monje WHERE  ").append(" abadiaid = ").append(rs.getInt("abadiaid")).append(" AND estado = ").append(Constantes.MONJE_VIVO).toString(), 0);
                    retrete = oUtilsAD.getSQL(new StringBuilder().append("SELECT nivel FROM edificio WHERE  ").append("tipoedificioid = ").append(Constantes.EDIFICIO_RETRETE).append(" AND abadiaid = ").append(rs.getInt("abadiaid")).toString(), 0);
                    abadia_ant = rs.getInt("abadiaid");
                }
                if (suciedad != 0) {
                    psrec = con.prepareStatement(sSQLAumentar);
                    psrec.setInt(1, suciedad);
                    psrec.setInt(2, rs.getInt("abadiaid"));
                    psrec.execute();
                }
            }
            oProcesosAD.addLog("- Finalizando Proceso suciedad", 0);

        } catch (Exception e) {
            throw new AbadiaSQLException("ERROR: suciedad = " + e, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(psrec);
            DBMSUtils.cerrarObjetoSQL(ps);
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    /* El pueblo crece/migra y demás
    */

    public void crecimiento_pueblo() throws AbadiaException {
        String sTrace = this.getClass() + ".crecimiento_pueblo()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adProcesos oProcesosAD;
        adUtils oUtilsAD;

        String sSQL = "SELECT m.abadiaid, count(*) total, max(cantidad) cantidad " +
                "FROM monje m, usuario u, abadia a, recurso r " +
                "WHERE m.estado = 0 and m.abadiaid = a.abadiaid and u.usuarioid = a.usuarioid and u.abadia_congelada = 0 and r.abadiaid = a.abadiaid and r.recursoid = 20 " +
                "group by m.abadiaid";

        String sSQLTributos = "UPDATE recurso SET cantidad = cantidad + ? WHERE RecursoID = 20 AND abadiaID = ? ";


        int val = 0;
        int migrar;
        int cardenal_abadia = 0;
        int obispo_abadia = 0;
        int papa_abadia = 0;
        int abadia_ant = 0;
        int valmax = 5;
        int edificios_aldeanos = 0;
        int carisma = 0;


        PreparedStatement ps = null;
        PreparedStatement psrec = null;
        ResultSet rs = null;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);
            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando Proceso crecimiento_pueblo", 0);
            oUtilsAD = new adUtils(con);

            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                valmax = 0;
                if (abadia_ant != rs.getInt("abadiaid")) {

                    cardenal_abadia = oUtilsAD.getSQL("SELECT monjeid FROM monje WHERE  " +
                            " jerarquiaid = " + Constantes.JERARQUIA_CARDENAL + " AND abadiaid = " +
                            rs.getInt("abadiaid"), 0);
                    if (cardenal_abadia == 0) {
                        cardenal_abadia = oUtilsAD.getSQL("SELECT m.monjeid FROM monje m, monje_visita mv WHERE  " +
                                " m.jerarquiaid = " + Constantes.JERARQUIA_CARDENAL + " AND mv.abadiaid_destino = " +
                                rs.getInt("abadiaid") + " AND mv.monjeid = m.monjeid AND m.estado = " + Constantes.MONJE_VISITA, 0);
                    }

                    papa_abadia = oUtilsAD.getSQL("SELECT monjeid FROM monje WHERE  " +
                            " jerarquiaid = " + Constantes.JERARQUIA_PAPA + " AND abadiaid = " +
                            rs.getInt("abadiaid"), 0);
                    if (papa_abadia == 0) {
                        papa_abadia = oUtilsAD.getSQL("SELECT m.monjeid FROM monje m, monje_visita mv WHERE  " +
                                " m.jerarquiaid = " + Constantes.JERARQUIA_PAPA + " AND mv.abadiaid_destino = " +
                                rs.getInt("abadiaid") + " AND mv.monjeid = m.monjeid AND m.estado = " + Constantes.MONJE_VISITA, 0);
                    }

                    obispo_abadia = oUtilsAD.getSQL("SELECT monjeid FROM monje WHERE  " +
                            " jerarquiaid = " + Constantes.JERARQUIA_OBISPO + " AND abadiaid = " +
                            rs.getInt("abadiaid"), 0);
                    if (obispo_abadia == 0) {
                        obispo_abadia = oUtilsAD.getSQL("SELECT m.monjeid FROM monje m, monje_visita mv WHERE  " +
                                " m.jerarquiaid = " + Constantes.JERARQUIA_OBISPO + " AND mv.abadiaid_destino = " +
                                rs.getInt("abadiaid") + " AND mv.monjeid = m.monjeid AND m.estado = " + Constantes.MONJE_VISITA, 0);
                    }
                    //sube la poblacion los edificios (Noviciado,escuela,hospederia,coro,muralla,enfermeria y Iglesia)

                    edificios_aldeanos = oUtilsAD.getSQL("SELECT sum(nivel) FROM edificio WHERE" +
                            " tipoedificioid in (" +
                            Constantes.EDIFICIO_NOVICIADO + "," + Constantes.EDIFICIO_ESCUELA + "," +
                            Constantes.EDIFICIO_HOSPEDERIA + "," + Constantes.EDIFICIO_CORO + "," +
                            Constantes.EDIFICIO_MUROS + "," + Constantes.EDIFICIO_ENFERMERIA + "," +
                            Constantes.EDIFICIO_IGLESIA + ") AND abadiaid =" +
                            rs.getInt("abadiaid"), 0);

                    carisma = oUtilsAD.getSQL("SELECT valor_actual from monje mj, habilidad_monje hm WHERE" +
                            " mj.abadiaid =" + rs.getInt("abadiaid") + " AND mj.monjeid = hm.monjeid" +
                            " AND hm.habilidadid = " + Constantes.HABILIDAD_CARISMA +
                            " order by jerarquiaid desc limit 1", 1);

                    abadia_ant = rs.getInt("abadiaid");
                }

                //50 aldeanos por nivel de edificios (edificios_aldeanos)
                if (edificios_aldeanos > 0) {
                    valmax = (edificios_aldeanos * 50) + valmax;
                }

                //si existe una eminencia en la abbatia, las migraciones son mas grandes.

                if (obispo_abadia > 0)
                    valmax = valmax + 300;

                if (cardenal_abadia > 0)
                    valmax = valmax + 500;

                if (papa_abadia > 0)
                    valmax = valmax + 800;

                // Comprobar los niveles
                val = (rs.getInt("total") * carisma) + 12 + valmax;
                migrar = 0;

                if (val > rs.getInt("cantidad")) // Incrementa
                {
                    if (val > 10) //No pueden aumentar mas de 10 en 10 la población.
                        val = 10 + (carisma / 10); //el carisma del abad, hace que vengan grupos mas amplios.

                    migrar = Utilidades.Random(0, val);
                } else if (val < rs.getInt("cantidad")) // Decrece
                    migrar = Utilidades.Random(0, 2) * -1;

                if (migrar != 0) {
                    psrec = con.prepareStatement(sSQLTributos);
                    psrec.setInt(1, migrar);
                    psrec.setInt(2, rs.getInt("abadiaid"));
                    psrec.execute();
                }
            }
            oProcesosAD.addLog("- Finalizando Proceso crecimiento_pueblo", 0);

        } catch (Exception e) {
            throw new AbadiaSQLException("ERROR: crecimiento_pueblo = " + e, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(psrec);
            DBMSUtils.cerrarObjetoSQL(ps);
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    /* Cálcula las puntuaciones del usuario
        oro + oro(por edificio nivel) + oro (por animales) + oro (por comida en cocina) + oro (por comida en granero) + 1000 * ( cantidad monjes salud > 30)
       oro (por piedra) + oro (por madera) + oro (por productos en el almacen).
    */

    public void puntuaciones_abadias() throws AbadiaException {

        String sTrace = this.getClass() + ".puntuaciones_abadias()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adUtils oUtilsAD;
        adAbadia oAbadiaAD;
        adProcesos oProcesosAD;

        HashMap<Integer, Double> hmAnimales;
        HashMap<Integer, Double> hmEdificios;
        HashMap<Integer, ArrayList<PuntuacionLibros>> hmLibros;
        HashMap<Integer, Double> hmMonjes;
        HashMap<Integer, Double> hmMonjesMuertos;
        HashMap<Integer, Double> hmMonjesHab;
        HashMap<Integer, Double> hmNumMonjes;
        HashMap<Integer, Double> hmNumSantos;
        HashMap<Integer, ArrayList<TableDouble>> hmActividad;

        String sSQLAnimales = "SELECT e.abadiaid, sum(puntuacion) from animales a, animales_crecimiento al, edificio e where a.tipo_animalid = al.tipo_animalid and a.nivel = al.nivel and a.edificioid = e.edificioid and a.estado = 0 group by abadiaid";
        String sSQLEdificios = "SELECT edificio.abadiaid, sum(puntuacion) from edificio_nivel, edificio where edificio_nivel.tipoedificioid = edificio.tipoedificioid and edificio_nivel.nivel = edificio.nivel group by abadiaid ";
        String sSQLLibrosID = "SELECT libroid  from libro where estado in (" + Constantes.ESTADO_LIBRO_COMPLETO + ", " + Constantes.ESTADO_LIBRO_RESTAURANDO + " )  group by abadiaid, tipo_libroid, nivel";
        String sSQLMonjes = "SELECT m.abadiaid, sum((puntuacion * salud) / 100) from monje m, monje_alimentacion ma, jerarquia_eclesiastica je where m.jerarquiaid = je.jerarquiaid and m.monjeid=ma.monjeid and m.estado <> 1 group by abadiaid";
        String sSQLMonjesMuertos = "SELECT m.abadiaid, sum(puntuacion) from monje_cementerio m, jerarquia_eclesiastica je where m.jerarquiaid = je.jerarquiaid and m.estado = " + Constantes.MONJE_MUERTO + " group by abadiaid";
        String sSQLMonjesHab = "SELECT m.abadiaid, sum(puntuacion*valor_actual) from monje m, habilidad_tipo ht, habilidad_monje hm where m.monjeid=hm.monjeid and hm.habilidadid = ht.habilidadid and m.estado <> 1 group by abadiaid ";
        String sSQLNMonjes = "SELECT abadiaid, count(*) from monje m WHERE m.estado <> 1 group by abadiaid ";
        //recuperar número de monjes
        String sSQLNSantos = "SELECT abadiaid, count(*) from monje where santo = 1 group by abadiaid ";
        String sSQLActividad = "SELECT a.abadiaid, am.actividadid, sum(am.cantidad) * ac.puntuacion from abadia a, actividad_mensajes am, monje m,actividad_tipo ac, propiedad_valor pv  where m.abadiaid = a.abadiaid and m.monjeid = am.monjeid and ac.actividadid = am.actividadid and date(am.fechaabadia) between ADDDATE( date(pv.valor), -10 ) AND date(pv.valor) and pv.propiedadid=99 and am.cantidad <> 0 and ac.puntuacion > 0 group by a.abadiaid, am.actividadid ";
        String sSQLLibros = "Select libro.ABADIAID, libro.TIPO_LIBROID, libro.nivel, count(libroid), libro_nivel.PUNTUACION From libro Inner Join libro_nivel ON libro.TIPO_LIBROID = libro_nivel.TIPO_LIBROID AND libro.NIVEL = libro_nivel.NIVEL where estado in (" + Constantes.ESTADO_LIBRO_COMPLETO + ", " + Constantes.ESTADO_LIBRO_RESTAURANDO + ") group by abadiaid, tipo_libroid, nivel";


        double animales = 0, edificios = 0, libros = 0, nmonjes = 0, monjes = 0, monjes_hab = 0, reliquias = 0, total = 0, nsantos = 0, monjesMuertos = 0, actividad = 0;

        int idAbadia = 0;
        ArrayList<AbadiaBasic> alAbadias = null;

        AbadiaPuntuacion abadiaPuntuacion = null;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);
            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando Proceso puntuaciones_usuario", 0);
            oAbadiaAD = new adAbadia(con);
            alAbadias = oAbadiaAD.recuperarAbadiasPuntuacion();

            //cargar datos generales de puntuación de todas las abadías.
            oUtilsAD = new adUtils(con);
            hmAnimales = oUtilsAD.generarHM(sSQLAnimales);
            hmActividad = oUtilsAD.generarHMActividad(sSQLActividad);
            ///hmAlimentos = oUtilsAD.generarHM(sSQLAlimentos);
            hmEdificios = oUtilsAD.generarHM(sSQLEdificios);
            //sListaLibrosID = oUtilsAD.generarIN(sSQLLibrosID);
            //sSQLLibros    = "SELECT abadiaid, sum(puntuacion) from libro_nivel lt, libro l where l.tipo_libroid = lt.tipo_libroid and l.nivel = lt.nivel and l.libroid in ( " + sListaLibrosID + ") group by abadiaid";
            hmLibros = oUtilsAD.generarHMLibros(sSQLLibros);
            hmMonjes = oUtilsAD.generarHM(sSQLMonjes);
            hmMonjesMuertos = oUtilsAD.generarHM(sSQLMonjesMuertos);
            hmMonjesHab = oUtilsAD.generarHM(sSQLMonjesHab);
            hmNumMonjes = oUtilsAD.generarHM(sSQLNMonjes);
            hmNumSantos = oUtilsAD.generarHM(sSQLNSantos);
            //hmRecursos = oUtilsAD.generarHM(sSQLRecursos);
            for (AbadiaBasic abadia : alAbadias) {
                abadiaPuntuacion = new AbadiaPuntuacion();
                idAbadia = abadia.getIdDeAbadia();
                abadiaPuntuacion.setIdAbadia(idAbadia);
                nmonjes = adUtils.recuperarValorHM(hmNumMonjes, idAbadia);
                if (nmonjes != 0) {
                    // Recuperar las puntuaciones de alimentos, edificios, libros
                    abadiaPuntuacion.setAnimales(adUtils.recuperarValorHM(hmAnimales, idAbadia));
                    //animales = oUtilsAD.getSQL(sSQLAnimales + idAbadia, 0);
                    //alimentos = adUtils.recuperarValorHM(hmAlimentos, idAbadia);
                    //alimentos = oUtilsAD.getSQL(sSQLAlimentos + idAbadia, 0);
                    abadiaPuntuacion.setEdificios(adUtils.recuperarValorHM(hmEdificios, idAbadia));
                    //edificios = oUtilsAD.getSQL(sSQLEdificios + idAbadia, 0);
                    abadiaPuntuacion.setMonjesNivel(adUtils.recuperarValorHM(hmMonjes, idAbadia));
                    //monjes = oUtilsAD.getSQL(sSQLMonjes + idAbadia, 0);
                    abadiaPuntuacion.setMonjesHabilidad(adUtils.recuperarValorHM(hmMonjesHab, idAbadia));
                    //monjes_hab = oUtilsAD.getSQL(sSQLMonjesHab + idAbadia, 0);
                    abadiaPuntuacion.setSantos(adUtils.recuperarValorHM(hmNumSantos, idAbadia) * 150000);
                    //nsantos = oUtilsAD.getSQL(sSQLNSantos + idAbadia, 0);
                    abadiaPuntuacion.setLibros(adUtils.recuperarValorHMLibros(hmLibros, idAbadia));
                    //libros = oUtilsAD.getSQL(sSQLLibros + idAbadia + " group by tipo_libroid, nivel)", 0);
                    abadiaPuntuacion.setMonjesMuertos(adUtils.recuperarValorHM(hmMonjesMuertos, idAbadia));
                    abadiaPuntuacion.setMonjesActividad(adUtils.recuperarValorHMActividad(hmActividad, idAbadia));
                    //recursos = adUtils.recuperarValorHM(hmRecursos, idAbadia);
                    //monjesMuertos = oUtilsAD.getSQL(sSQLMonjesMuertos + idAbadia, 0);
                    abadiaPuntuacion.setTotal(abadiaPuntuacion.getAnimales() + abadiaPuntuacion.getEdificios() + abadiaPuntuacion.getLibros() + abadiaPuntuacion.getMonjesNivel() + abadiaPuntuacion.getMonjesHabilidad() + abadiaPuntuacion.getSantos() - abadiaPuntuacion.getMonjesMuertos() + abadiaPuntuacion.getMonjesActividad());
                    //total = animales + edificios + libros + monjes + monjes_hab + reliquias + (nsantos*150000) - monjesMuertos + actividad ;
                } else
                    total = 0; //Si no tiene monjes, la puntuación es 0.rs.getFloat("total") / 2;   // Sino tiene monjes que lo divida por dos T_o_d_o !

                //Crear registro puntuación por abadía-fecha
                oAbadiaAD.crearRegistroPuntuacion(abadiaPuntuacion);
            }
            // Asignar las posiciones de la abbatia
            oAbadiaAD.reasignarPosicionesPuntuacion();

            oProcesosAD.addLog("- Finalizando Proceso puntuaciones_usuario", 0);
        } catch (Exception e) {
            throw new AbadiaSQLException(sTrace + " ERROR: puntuaciones_usuario", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }


    /*   Proceso obispos... muerte y resurección por voto ;-)
    */

    public void obispado() throws AbadiaException, AbadiaSQLException {
        String sTrace = this.getClass() + ".obispado()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adProcesos oProcesosAD;
        adUtils oUtilsAD;
        adJerarquiaEclesiastica oJerarquiaADjerarquia;
        adMensajes oMensajesAD;
        adRecurso oRecursoAD;
        adHabilidades oHabilidadesAD;

        // regiones sin obispo
        String sSQL = "SELECT o.regionid " +
                "FROM obispado o WHERE o.fecha_votacion is null AND o.monjeid=-1";
        // Regiones en medio de la votacion
        String sSQL1 = "SELECT o.regionid, to_days(fecha_votacion) fvoto, to_days(?) fecha " +
                "FROM obispado o WHERE o.fecha_votacion is not null AND o.monjeid=-1";
        String sSQL2 = "SELECT * FROM monje where jerarquiaid = 5 and estado <> 1";
        // Impuestos revolucionario ;-)
        String sSQLImp = "select ch.regionid, sum(importe) total, max(c.obispado) obispado, max(o.abadiaid) abadiaid, max(o.monjeid) monjeid from `comisiones_historico` ch, comisiones c, obispado o " +
                "where procesado = 0 and ch.regionid = c.regionid and o.regionid = c.regionid " +
                "group by ch.regionid ";


        int nrAbadias, abadiaid, monjeid, obispoid, pos;
        double total, obispado, cardenales, papa, regional;
        double total_cardenales = 0, total_papa = 0;
        String nombre, abadia, region;

        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);
            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando Proceso obispado", 0);
            oMensajesAD = new adMensajes(con);
            oRecursoAD = new adRecurso(con);
            oJerarquiaADjerarquia = new adJerarquiaEclesiastica(con);
            // Inicializar el proceso de obispado cuando se muere un obispo ( cuando se entierra )
            oUtilsAD = new adUtils(con);
            oUtilsAD.execSQL("UPDATE obispado o, monje_cementerio mc SET o.monjeid=-1, o.fecha_votacion=null " +
                    "WHERE o.monjeid = mc.monjeid and o.monjeid <> -1 ");
            oUtilsAD.execSQL("UPDATE obispado o, monje m SET o.monjeid=-1, o.fecha_votacion=null " +
                    "WHERE o.monjeid = m.monjeid and (m.jerarquiaid <> 3 or estado=1)");
            // Cardenales que han muerto o se entierran
            oUtilsAD.execSQL("UPDATE cardenales cc, monje_cementerio mc SET cc.monjeid=-1, cc.abadiaid=-1 " +
                    "WHERE cc.monjeid = mc.monjeid and cc.monjeid <> -1 ");
            oUtilsAD.execSQL("UPDATE cardenales cc, monje m SET cc.monjeid=-1, cc.abadiaid=-1 " +
                    "WHERE cc.monjeid = m.monjeid and (m.jerarquiaid <> 5 or m.estado=1)");

            //marco las abadías congelada como NO seleccionables al cargo de obispo ni cardenal.
            oUtilsAD.execSQL("update abadia a set a.FLAG_CARDENAL=1, a.FLAG_OBISPO=1 where a.USUARIOID in (select u.USUARIOID from usuario u where u.ABADIA_CONGELADA=1)");
            // Buscar los monjes muertos y crear candidatos y votadores.
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                // 10% de las mejores abadias se presentan como candidatos
                nrAbadias = oUtilsAD.getSQL("SELECT Round((Count(*)*20)/100) FROM abadia WHERE regionid = " + rs.getInt("regionid"), 10);
                // Insertar los usuario que pueden votar
                oUtilsAD.execSQL("DELETE FROM obispado_candidatos WHERE regionid = " + rs.getInt("regionid"));
                // brp => 16/12/2011 - filtramos de la query para que las abadías congeladas NO computen
                oUtilsAD.execSQL("INSERT INTO `obispado_candidatos` (regionid, abadiaid) " +
                        "(SELECT a.regionid, a.abadiaid FROM abadia a, usuario u, monje m, abadia_puntuacion ap " +
                        " Where a.abadiaid = m.abadiaid and a.abadiaid = ap.abadiaid and ap.clasificacion > 0 and m.jerarquiaid = " + Constantes.JERARQUIA_ABAD + " and a.flag_obispo = 0 and a.usuarioid = u.usuarioid and u.usuario_tipo <> 1 and regionid = " + rs.getInt("regionid") + " and ap.fecha_abadia = (Select date(valor) from propiedad_valor where propiedadid = 99) " +
                        " ORDER by ap.clasificacion LIMIT " + nrAbadias + ")");
                // 50% de las mejores abadias pueden votar
                nrAbadias = oUtilsAD.getSQL("SELECT Round((Count(*)*60)/100) FROM abadia WHERE regionid = " + rs.getInt("regionid"), 10);
                // Insertar los usuario que pueden votar
                oUtilsAD.execSQL("DELETE FROM obispado_voto WHERE regionid = " + rs.getInt("regionid"));
                oUtilsAD.execSQL("INSERT INTO `obispado_voto` (regionid, abadiaid) " +
                        "(SELECT a.regionid, a.abadiaid FROM abadia a, usuario u, abadia_puntuacion ap " +
                        " Where a.usuarioid = u.usuarioid and u.usuario_tipo<>1 and a.abadiaid = ap.abadiaid and ap.clasificacion > 0 and u.ABADIA_CONGELADA=0 and a.regionid = " + rs.getInt("regionid") + " and ap.fecha_abadia = (Select date(valor) from propiedad_valor where propiedadid = 99) " +
                        " ORDER by ap.clasificacion LIMIT " + nrAbadias + ")");
                // Insertar los usuario que pueden votar
                oUtilsAD.execSQL("UPDATE obispado SET fecha_votacion = '" + CoreTiempo.getTiempoAbadiaString() + "' WHERE regionid = " + rs.getInt("regionid"));
                // Mensajes!!
                region = oUtilsAD.getSQL("SELECT descripcion from region where regionid = " + rs.getInt("regionid"), "?");

                // Han empezado las elecciones en %s
                oMensajesAD.crearMensajesParaRegion(rs.getInt("regionid"), 0, 12003, region, null, null, null);
            }
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
            oHabilidadesAD = new adHabilidades(con);
            // Averiguar si los votos se han finalizado por votos completos o porque ha pasado más de un mes
            ps = con.prepareStatement(sSQL1);
            ps.setString(1, CoreTiempo.getTiempoAbadiaString());
            rs = ps.executeQuery();
            while (rs.next()) {
                nrAbadias = oUtilsAD.getSQL("SELECT count(*) FROM obispado_voto ov where abadiaid_voto = 0 and regionid = " + rs.getInt("regionid"), 0);
                // Se ha finalizado la elección! porque todos han votado o porque pasan 5 dias!!
                if ((nrAbadias == 0) || (rs.getInt("fvoto") + 30 <= rs.getInt("fecha"))) {
                    // Averiguar quien ha quedado primero
                    nrAbadias = oUtilsAD.getSQL("SELECT abadiaid_voto, count(*) votos FROM obispado_voto ov where abadiaid_voto <> 0 and regionid = " +
                            rs.getInt("regionid") +
                            " GROUP BY abadiaid_voto " +
                            " ORDER BY votos desc " +
                            "LIMIT 1 ", 0);
                    if (nrAbadias == 0) {  // No quedan abadias para el voto... regenerar la lista pa la próxima ;-)
                        oUtilsAD.execSQL("UPDATE obispado SET fecha_votacion=null " +
                                " WHERE regionid=" + rs.getInt("regionid"));
                    } else {
                        // Recuperar el monje que va a ser Obispo
                        monjeid = oUtilsAD.getSQL("SELECT * FROM monje WHERE jerarquiaid = " +
                                Constantes.JERARQUIA_ABAD +
                                " and estado=0 and abadiaid = " + nrAbadias +
                                " LIMIT 1", 0);
                        if (monjeid == 0) {
                            // El monje se ha muerto y ya no hay abad... eliminarlo del voto y esperar al próximo día abbatia
                            oUtilsAD.execSQL("DELETE FROM obispado_candidatos WHERE abadiaid=" + nrAbadias);
                        } else {
                            // Ya tenemos el monje!!!!
                            //recuperamos la abbatia a la que pertenece.
                            abadiaid = oUtilsAD.getSQL("SELECT abadiaid from monje where monjeid=" + monjeid, 0);
                            //Asignamos el monje al obispado
                            oUtilsAD.execSQL("UPDATE obispado SET monjeid=" + monjeid + ", abadiaid=" + abadiaid +
                                    ", fecha_votacion=null " +
                                    ", fecha_proclamacion='" + CoreTiempo.getTiempoAbadiaString() + "' " +
                                    " WHERE regionid=" + rs.getInt("regionid"));
                            //Actualizamos la jerarquía del monje
                            oUtilsAD.execSQL("UPDATE monje SET jerarquiaid=" +
                                    Constantes.JERARQUIA_OBISPO + " WHERE monjeid=" +
                                    monjeid);
                            //determinar a popularidad del obispo al ser elegido en base a la votación
                            //De momento le ponemos 99 al ser monje.
                            oHabilidadesAD.crearHabilidad(monjeid, Constantes.HABILIDAD_POPULARIDAD, 99);
                            nombre = oUtilsAD.getSQL("SELECT nombre from monje where monjeid = " + monjeid, "?");
                            abadia = oUtilsAD.getSQL("SELECT nombre from abadia where abadiaid = " + abadiaid, "?");
                            region = oUtilsAD.getSQL("SELECT descripcion from region where regionid = " + rs.getInt("regionid"), "?");
                            // El monje %s de la abbatia %s ha sido nombrado cardenal para la región de %s
                            oMensajesAD.crearMensajesParaTodos(0, 12000, nombre, abadia, region, null);
                        }
                    }
                }
            }
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);

            // Averiguar cuantos cardenales tenemos!!!
            int nrcardenal = oUtilsAD.getSQL("select count(*) from monje where estado <> 1 and jerarquiaid = " + Constantes.JERARQUIA_CARDENAL, 0);
            if (nrcardenal < 10) {
                for (int n = nrcardenal; n < 10; n++) {
                    obispoid = oUtilsAD.getSQL("select monjeid from monje m, abadia a, abadia_puntuacion ap where m.abadiaid = a.abadiaid and a.flag_cardenal = 0 and m.estado <> 1 and m.jerarquiaid = " + Constantes.JERARQUIA_OBISPO +
                            " and a.abadiaid = ap.abadiaid and ap.fecha_abadia = (Select date(valor) from propiedad_valor where propiedadid = 99) and ap.clasificacion > 0 and a.abadiaid not in ( select abadiaid from monje where jerarquiaid = " + Constantes.JERARQUIA_CARDENAL + ") \n" +
                            " order by ap.clasificacion limit 1 ", 0);
                    if (obispoid != 0) {

                        oJerarquiaADjerarquia.setJerarquiaid(obispoid, Constantes.JERARQUIA_CARDENAL);
                        // MENSAJE
                        nombre = oUtilsAD.getSQL("SELECT nombre from monje where monjeid = " + obispoid, "?");
                        abadia = oUtilsAD.getSQL("SELECT a.nombre from abadia a, monje m where a.abadiaid = m.abadiaid and m.monjeid = " + obispoid, "?");
                        // El monje %s de la abbatia %s ha sido nombrado cardenal
                        oMensajesAD.crearMensajesParaTodos(0, 12001, nombre, abadia, null, null);
                    }
                }
            }
            // Establecer las comisiones de los cardenales...
            ps = con.prepareStatement(sSQL2);
            rs = ps.executeQuery();
            while (rs.next()) {
                monjeid = oUtilsAD.getSQL("SELECT monjeid FROM cardenales WHERE monjeid=" + rs.getInt("monjeid"), 0);
                if (monjeid == 0) {
                    // No está!!!
                    pos = oUtilsAD.getSQL("SELECT cardenalid FROM cardenales WHERE monjeid=-1", 0);
                    if (pos != 0)
                        oUtilsAD.execSQL("UPDATE cardenales SET abadiaid=" + rs.getInt("abadiaid") + ",monjeid=" + rs.getInt("monjeid") + " WHERE cardenalid=" + pos);
                }
            }
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);

            // Cónclave
            // Si los cardenales estan de viaje, comprobar si han llegado
            pos = oUtilsAD.getSQL("SELECT estado  FROM conclave WHERE estado = 1", -1);
            if (pos == 1) {
                pos = oUtilsAD.getSQL("SELECT count(*) FROM cardenales c, cardenales_conclave cc, monje m " +
                        "where c.monjeid = m.monjeid and c.cardenalid = cc.cardenalid and estado = 3 ", 0);
                if (pos == 0)
                    oUtilsAD.execSQL("UPDATE conclave SET estado = 2, fumata = 0 WHERE estado = 1");
            } else     // Si ya ha habido el conclave y ha finalizado... ponerlo a 0... es pa que se vea la fumata blanca durante un día abbatia
                oUtilsAD.execSQL("UPDATE conclave SET estado = 0, fumata = 0 WHERE estado = 3");

            /*
         adPapa papaAD = new adPapa();
         int papaid = papaAD.getPapaID();
         boolean conclave = papaAD.existeConclave();
         papaAD.finalize();
         if ( ( papaid == 0 ) && (!conclave) ) {
             // Rellenar las tablas con los candidatos a papa
             oUtilsAD = new adUtils(Constantes.DB_TIMEOUT_PROCESS, Constantes.DB_CONEXION_PROCESS);
             oUtilsAD.execSQL("INSERT INTO `cardenales_voto` ( CardenalID ) (Select CardenalID from cardenales )");
             oUtilsAD.finalize();
             // Empezar el viaje
             ps = con.prepareStatement(sSQLCardenales);
             rs = (ResultSet) ps.executeQuery();
             while (rs.next())
             {
                 viaje = new adViajar();
                 viaje.prepararViaje( rs.getInt(""), rs.getInt(""), rs.getInt(""), 10);    //
                 viaje.finalize();
             }
             rs.close();
             ps.close();

             oJerarquiaADjerarquia = new adJerarquiaEclesiastica();
             oJerarquiaADjerarquia.setJerarquiaid(cardenalid, Constantes.JERARQUIA_PAPA);
             oJerarquiaADjerarquia.finalize();
             // MENSAJE
             oUtilsAD = new adUtils(Constantes.DB_TIMEOUT_PROCESS, Constantes.DB_CONEXION_PROCESS);
             nombre = oUtilsAD.getSQL("SELECT nombre from monje where monjeid = " + cardenalid, "?" );
             abbatia = oUtilsAD.getSQL("SELECT a.nombre from abbatia a, monje m where a.abadiaid = m.abadiaid and m.monjeid = " + cardenalid, "?" );
             oUtilsAD.finalize();
             oMensajesAD = new adMensajes();
             // El monje %s de la abbatia %s ha sido nombrado Papa
             oMensajesAD.crearMensajesParaTodos(0, 12002, nombre, abbatia, null, null);
             oMensajesAD.finalize();

         }   */

            // Recolectas
            float impuesto_papa = oUtilsAD.getSQL("SELECT impuesto_papa FROM `comisiones_papa` where abadiaid = -1", 10);
            float impuesto_cardenal = oUtilsAD.getSQL("SELECT impuesto_cardenales FROM `comisiones_papa` where abadiaid = -1", 10);
            int recaudado = 0;
            //
            ps = con.prepareStatement(sSQLImp);
            rs = ps.executeQuery();
            while (rs.next()) {
                total = rs.getDouble("total");
                regional = total;
                // Papa 10%
                papa = total * impuesto_papa / 100;
                total -= papa;
                // Cardenales 10%
                cardenales = total * impuesto_cardenal / 100;
                total -= cardenales;
                // Hay obispado???
                if (rs.getInt("monjeid") != -1) {  // Hay obispado???
                    // Descontar el obispado
                    obispado = total * rs.getDouble("obispado") / 100;
                    total -= obispado;
                } else obispado = 0;
                // Marcar como procesados
                oUtilsAD.execSQL("update `comisiones_historico` ch set procesado = 1 where regionid = " + rs.getInt("regionid"));
                oUtilsAD.execSQL("update `comisiones` set total = total + " + total + " where regionid = " + rs.getInt("regionid"));
                recaudado = oUtilsAD.getSQL("Select recaudado from comisiones_historico_regional where regionid = " + rs.getInt("regionid") + " and fecha = '" + CoreTiempo.getTiempoAbadiaString() + "'", 0);
                if (recaudado == 0) {
                    oUtilsAD.execSQL("insert into `comisiones_historico_regional` (regionid, fecha, recaudado, obispo, cardenales, papa) " +
                            "values ('" + rs.getInt("regionid") + "','" + CoreTiempo.getTiempoAbadiaString() + "'," + regional + "," + obispado + "," + cardenales + "," + papa + ")");
                }
                if (obispado != 0) {
                    oRecursoAD.sumarRecurso(0, rs.getLong("abadiaid"), obispado);
                }
                total_cardenales += cardenales;
                total_papa += papa;
            }
            // Actualizar los datos del papa y del cardenal
            oUtilsAD.execSQL("UPDATE `comisiones_cardenales` set total = total + " + (total_cardenales / 10));
            oUtilsAD.execSQL("UPDATE `comisiones_papa` set total = total + " + total_papa + " where abadiaid = -1");
            // Limpieza de datos
            oUtilsAD.execSQL("DELETE FROM comisiones_historico where procesado = 1 and fecha < Date_Sub('" + CoreTiempo.getTiempoAbadiaString() + "', Interval 30 day)");
            oUtilsAD.execSQL("DELETE FROM comisiones_historico_regional where fecha < Date_Sub('" + CoreTiempo.getTiempoAbadiaString() + "', Interval 90 day)");

            oProcesosAD.addLog("- Finalizando Proceso obispado", 0);

        } catch (Exception e) {
            throw new AbadiaSQLException(sTrace + " ERROR: obispado", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    /* El pueblo crece/migra y demás
    */

    public void limpieza_bd() throws AbadiaException {
        String sTrace = this.getClass() + ".limpieza_bd()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adProcesos oProcesosAD;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);
            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando Proceso limpieza_bd", 0);
            oProcesosAD.addLog("+ Lanzando SUBProceso regular_idiomas", 0);
            regular_idiomas();
            oProcesosAD.addLog("- Finalizando SUBProceso regular_idiomas", 0);
            oProcesosAD.addLog("+ Lanzando SUBProceso eliminar_monjes_muertos", 0);
            oProcesosAD.eliminarDatosMonjesMuertos();
            oProcesosAD.addLog("- Finalizando SUBProceso eliminar_monjes_muertos", 0);
            oProcesosAD.addLog("+ Lanzando SUBProceso eliminar_animales_muertos", 0);
            oProcesosAD.eliminarAnimalesMuertos();
            oProcesosAD.addLog("- Finalizando SUBProceso eliminar_animales_muertos", 0);
            oProcesosAD.addLog("+ Lanzando SUBProceso eliminar_mensajes_antiguos", 0);
            oProcesosAD.eliminarMensajesAntiguos(-30);
            oProcesosAD.addLog("- Finalizando SUBProceso eliminar_mensajes_antiguos", 0);
            oProcesosAD.addLog("+ Lanzando SUBProceso eliminar_posiciones_ranking", 0);
            oProcesosAD.eliminarRankingAntiguo(-10);
            oProcesosAD.addLog("- Finalizando SUBProceso eliminar_posiciones_ranking", 0);
            oProcesosAD.addLog("+ Lanzando SUBProceso eliminar_elaboraciones_finalizadas", 0);
            oProcesosAD.eliminarElaboracionesFinalizadas(-20);
            oProcesosAD.addLog("- Finalizado SUBProceso eliminar_elaboraciones_finalizadas", 0);
            oProcesosAD.addLog("- Finalizando Proceso limpieza_bd", 0);
        } catch (Exception e) {
            throw new AbadiaSQLException(sTrace + " ERROR: obispado", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }


    /* Gestión de idiomas... crea registros para todos los idiomas
    */

    public void regular_idiomas() throws AbadiaException {
        String sTrace = this.getClass() + ".regular_idiomas()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adUtils oUtilsAD;

        String sSQL = "SELECT * from literales where idiomaid = 1 ";

        int i;

        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);
            oUtilsAD = new adUtils(con);
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                for (i = 2; i < 9; i++) {
                    if (oUtilsAD.getSQL("SELECT literalid FROM literales WHERE Literalid=" +
                            rs.getInt("literalid") + " AND Idiomaid=" + i, -1) == -1) {
                        oUtilsAD.execSQL(
                                "INSERT INTO literales (literalid, idiomaid, literal) VALUES (" +
                                        rs.getInt("literalid") + "," + i + ",'*" +
                                        rs.getString("literal") + "')");
                    }
                }
            }
        } catch (Exception e) {
            throw new AbadiaSQLException(sTrace + "ERROR: regular_idiomas", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public void subirNivelMonje(int mes) throws AbadiaException {
        String sTrace = this.getClass() + ".subirNivelMonje()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        String sSQL = "UPDATE monje set jerarquiaid=1 where fecha_entrada < ? and jerarquiaid=0 ";
        String sFecha = CoreTiempo.getDiferenciaMesString(-mes);

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);
            ps = con.prepareStatement(sSQL);
            ps.setString(1, sFecha);
            ps.executeUpdate();

        } catch (Exception e) {
            throw new AbadiaSQLException("ProcesosVariosBBean. subirNivelMonje. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    /**
     * Resta a las abadias el salario correspondiente a los guardias contratados
     * Si no se dispone de los recursos suficientes para los sueldos, los guardias se piran
     * Se generaran mensajes para indicar lo que se paga
     *
     * @throws org.abbatia.exception.AbadiaSQLException
     *          Excepción base de abbatia
     */
    public void salarioGuardias() throws AbadiaException {
        String sTrace = this.getClass() + ".salarioGuardias()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adProcesos oProcesosAD;
        adSalarios oSalariosAD;
        adLiterales oLiteralesAD;
        adRecurso oRecursoAD;
        adMensajes oMensajesAD;

        ArrayList<SalarioGuardia> alSalarios = new ArrayList<SalarioGuardia>();
        double totalSalarios;
        int last_idioma = 0;
        int monjes_pagados = 0;
        int monjes_impagados = 0;
        String msg1 = "";
        String msg2 = "";
        String msgTemp = "";

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);
            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando Proceso Salario de Guardias", 0);
            oLiteralesAD = new adLiterales(con);
            oSalariosAD = new adSalarios(con);
            oRecursoAD = new adRecurso(con);
            oMensajesAD = new adMensajes(con);
            double salarioGuardia = oSalariosAD.recuperarSalario(Constantes.EMPLEADO_GUARDIA);

            alSalarios = oProcesosAD.recuperarSalariosGuardias();

            for (SalarioGuardia salario : alSalarios) {
                if (last_idioma != salario.getIdIdioma()) {
                    last_idioma = salario.getIdAbadia();
                    msg1 = oLiteralesAD.getLiteralStatic(12200, last_idioma);
                    msg2 = oLiteralesAD.getLiteralStatic(12201, last_idioma);
                }

                totalSalarios = salario.getNumGuardias() * salarioGuardia;

                //la abbatia dispone de pasta suficiente para pagar a sus guardias
                if (totalSalarios <= salario.getMonedas()) {
                    monjes_pagados = salario.getNumGuardias();
                    monjes_impagados = 0;
                } else {
                    //calculamos para cuantos monjes hay pasta.....
                    monjes_pagados = (int) (salario.getMonedas() / salarioGuardia);
                    monjes_impagados = salario.getNumGuardias() - monjes_pagados;
                    totalSalarios = monjes_pagados * salarioGuardia;
                }

                if (monjes_pagados > 0) {
                    //decrementamos el oro de la abbatia
                    oRecursoAD.restarRecurso(Constantes.RECURSOS_ORO, salario.getIdAbadia(), totalSalarios);
                    //generamos el mensaje correspondiente
                    msgTemp = Utilidades.Format(msg1, (int) totalSalarios);
                    msgTemp = Utilidades.Format(msgTemp, monjes_pagados);
                    oMensajesAD.crearMensaje(salario.getIdAbadia(), -1, -1, 1, msgTemp);
                }
                if (monjes_impagados > 0) {
                    //generamos mensaje de que se piran los guardias
                    msgTemp = Utilidades.Format(msg2, monjes_impagados);
                    oMensajesAD.crearMensaje(salario.getIdAbadia(), -1, -1, 1, msgTemp);
                    //decrementamos el número de monjes de la abbatia
                    oRecursoAD.restarRecurso(Constantes.RECURSOS_GUARDIA, salario.getIdAbadia(), monjes_impagados);
                }
            }

            oProcesosAD.addLog("- Finalizando Proceso Salario de Guardias", 0);
        } catch (Exception e) {
            throw new AbadiaSQLException(sTrace + "SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    /**
     * Devuelve a sus abadías aquellos monjes que están de visita en abadías que han sido eliminadas.
     *
     * @throws AbadiaException excepción general
     */
    public void regularizarMonjesHuerfanos() throws AbadiaException {
        String sTrace = this.getClass() + ".regularizarMonjesHuerfanos()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adProcesos oProcesosAD;
        adMonje oMonjeAD;
        adLibros oLibrosAD;
        adViajar oViajarAD;

        //Localizamos los monjes de visita en abadías que ya no existen
        String sSQL = "select MONJEID from monje_visita where abadiaid_destino not in (select abadiaid from abadia)";
        PreparedStatement ps = null;
        ResultSet rs = null;
        int idMonje;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);

            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando Proceso Regularización de Monjes huerfanos", 0);
            oMonjeAD = new adMonje(con);
            oLibrosAD = new adLibros(con);
            oViajarAD = new adViajar(con);

            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                idMonje = rs.getInt("MONJEID");
                //Devolver el estado del monje a 0
                oMonjeAD.actualizarEstado(idMonje, Constantes.MONJE_VIVO);
                //Eliminar tareas de copia (si existen)
                oLibrosAD.eliminarTareasCopiaPorMonje(idMonje);
                //eliminar registro de monje visita
                oViajarAD.eliminarViajePorIdMonje(idMonje);
                //desbloquear actividades copia
                oMonjeAD.desbloquarActividadesCopia(idMonje);
            }
            oProcesosAD.addLog("+ Finalizado Proceso Regularización de Monjes huerfanos", 0);

        } catch (Exception e) {
            throw new AbadiaSQLException("ProcesosVariosBBean. regularizarMonjesHuerfanos. Exception", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }


    }

    /**
     * Devuelve el estado 0 a los monjes que supestamente están viajando pero no tienen registro en la tabla monje_visita
     *
     * @throws AbadiaException
     */
    public void regularizarMonjesPerdidos() throws AbadiaException {
        String sTrace = this.getClass() + ".regularizarMonjesPerdidos()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adProcesos oProcesosAD;
        adMonje oMonjeAD;
        adLibros oLibrosAD;
        adViajar oViajarAD;

        //Localizamos los monjes de visita en abadías que ya no existen
        String sSQL = "select monjeid from monje where estado in (3,4) and monjeid not in (select monjeid from monje_visita)";
        PreparedStatement ps = null;
        ResultSet rs = null;
        int idMonje;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);
            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando Proceso Regularización de Monjes Perdidos", 0);
            oMonjeAD = new adMonje(con);
            oLibrosAD = new adLibros(con);
            oViajarAD = new adViajar(con);

            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                idMonje = rs.getInt("MONJEID");
                //Devolver el estado del monje a 0
                oMonjeAD.actualizarEstado(idMonje, Constantes.MONJE_VIVO);
                //Eliminar tareas de copia (si existen)
                oLibrosAD.eliminarTareasCopiaPorMonje(idMonje);
                //eliminar registro de monje visita
                oViajarAD.eliminarViajePorIdMonje(idMonje);
                //desbloquear actividades copia
                oMonjeAD.desbloquarActividadesCopia(idMonje);
            }
            oProcesosAD.addLog("+ Finalizado Proceso Regularización de Monjes Perdidos", 0);

        } catch (Exception e) {
            throw new AbadiaSQLException("ProcesosVariosBBean. regularizarMonjesPerdidos. Exception", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }


    }

    /**
     * Actualiza el estado de los libros que no tienen ningún monje copiando y están estado "en uso (copiandose)" al estado Incompleto
     *
     * @throws AbadiaException
     */
    public void desbloquearLibrosIncompletos() throws AbadiaException {
        String sTrace = this.getClass() + ".desbloquearLibrosIncompletos()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adProcesos oProcesosAD;
        adUtils oUtilsAD;

        String sSQL = "update libro set estado = 1 where estado = 3 and libroid not in (select LIBRO_COPIAID from libro_tarea)";

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);

            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando Proceso desbloqueo de libros incompletos", 0);

            oUtilsAD = new adUtils(con);
            oUtilsAD.execSQL(sSQL);

            oProcesosAD.addLog("+ Finalizado Proceso desbloqueo de libros incompletos", 0);
        } catch (Exception e) {
            throw new AbadiaSQLException("ProcesosVariosBBean. desbloquearLibrosIncompletos", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    /**
     * Desbloquea las tareas de copia de monjes que ya han completado su copia pero siguen con la tarea asignada y bloqueada
     * - Recuperar id's de libros y monjes implicados
     * - Eliminar registros libro_tarea para el par: libro-monje
     * - Actualizar estado de la actividad del monje, desbloquear la tarea...
     *
     * @throws AbadiaException
     */
    public void desbloquearTareasCopiaMonjes() throws AbadiaException {
        String sTrace = this.getClass() + ".desbloquearTareasCopiaMonjes()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adProcesos oProcesosAD;
        adLibros oLibrosAD;
        adMonje oMonjesAD;

        String sSQL = "SELECT l.LIBROID, lt.MONJEID, lt.PERIODOID " +
                " FROM libro AS l Inner Join libro_tarea AS lt ON l.LIBROID = lt.LIBRO_COPIAID " +
                " WHERE l.ESTADO IN  (" + Constantes.ESTADO_LIBRO_COMPLETO + ", " + Constantes.ESTADO_LIBRO_DETERIORADO + ", " + Constantes.ESTADO_LIBRO_RESTAURANDO + ")";

        CachedRowSetImpl crs = null;
        int idLibro;
        int idMonje;
        int idPeriodo;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);

            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando Proceso desbloqueo de monjes que han terminado sus copias", 0);
            oMonjesAD = new adMonje(con);
            oLibrosAD = new adLibros(con);

            crs = new CachedRowSetImpl();
            crs.setCommand(sSQL);
            crs.execute(con);
            while (crs.next()) {
                idLibro = crs.getInt("LIBROID");
                idMonje = crs.getInt("MONJEID");
                idPeriodo = crs.getInt("PERIODOID");
                oLibrosAD.eliminarTareasPorMonjeLibro(idMonje, idLibro);
                oMonjesAD.desbloquarActividadesCopiaPeriodo(idMonje, idPeriodo);
            }
            //todo borramos también las tareas de copia que estén vinculadas a la copia de libros que no existen


            oProcesosAD.addLog("+ Finalizado Proceso desbloqueo de monjes que han terminado sus copias", 0);

        } catch (Exception e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(crs);
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    /**
     * Desbloquea las tareas de copia de monjes que tienen su registro de libro_tarea asociado a un libro_copiaid que
     * no existe, si las tareas de copia están recien creadas tendrán como código de libro copia el identificador del
     * monje (versión antigua) o 0 con la versión actualizada.
     * Pra prevenir problemas, serán procesados sólo aquellos registros en los que el id del libro copia no
     * coincida con el id del monje y que además no sea = 0
     * - Recuperar id's de libros y monjes implicados
     * - Eliminar registros libro_tarea para el par: libro-monje
     * - Actualizar estado de la actividad del monje, desbloquear la tarea...
     *
     * @throws AbadiaException
     */
    public void desbloquearTareasCopiaMonjesSinLibro() throws AbadiaException {
        String sTrace = this.getClass() + ".desbloquearTareasCopiaMonjes2()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adProcesos oProcesosAD;
        adLibros oLibrosAD;
        adMonje oMonjesAD;

        String sSQL = "select LIBRO_COPIAID, MONJEID, PERIODOID " +
                " from libro_tarea " +
                " where libro_copiaid not in(select libroid from libro) and libro_copiaid <> monjeid and libro_copiaid <> 0";

        CachedRowSetImpl crs = null;
        int idLibro;
        int idMonje;
        int idPeriodo;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);

            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando Proceso desbloqueo de monjes que están trabajando en la copia de un libro que no existe", 0);
            oMonjesAD = new adMonje(con);
            oLibrosAD = new adLibros(con);

            crs = new CachedRowSetImpl();
            crs.setCommand(sSQL);
            crs.execute(con);
            while (crs.next()) {
                idLibro = crs.getInt("LIBRO_COPIAID");
                idMonje = crs.getInt("MONJEID");
                idPeriodo = crs.getInt("PERIODOID");
                oLibrosAD.eliminarTareasPorMonjeLibro(idMonje, idLibro);
                oMonjesAD.desbloquarActividadesCopiaPeriodo(idMonje, idPeriodo);
            }
            oProcesosAD.addLog("+ Finalizado Proceso desbloqueo de monjes que están trabajando en la copia de un libro que no existe", 0);

        } catch (Exception e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(crs);
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    /**
     * Genera un fichero de texto plano con los datos con los que se cargará el mapa flash
     *
     * @throws AbadiaException
     */
    public void generarDatosMapa() throws AbadiaException {
        String sTrace = this.getClass() + ".generarDatosMapa()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adRegion oRegionAD;
        adAbadia oAbadiaAD;
        adObispado oObispadoAD;

        ArrayList<Region> alRegiones;
        ArrayList<AbadiaBasic> alAbadias;

        FileWriter f = null;
        StringBuffer sfDatosRegiones = new StringBuffer();
        StringBuffer sfDatosAbadias = new StringBuffer();
        StringBuffer sfDatos = new StringBuffer();
        //AbadiaConfiguracion abadiaconf = new AbadiaConfiguracion();
        //abadiaconf.Init(null);

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);

            //recuperamos la lista de regiones activas con código y nombre + el nombre de la abadía que ostenta el cargo de obispo en la región
            oRegionAD = new adRegion(con);
            alRegiones = oRegionAD.recuperarDatosRegiones();

            oObispadoAD = new adObispado(con);
            oAbadiaAD = new adAbadia(con);
            //recuperamos la lista de las 10 mejores abadías por región
            //&a04=1&a05=1&a11=1&a27=1&a31=1&a36=1&a39=1&a43=1&a52=1&a56=1&
            for (Region region : alRegiones) {
                sfDatosRegiones.append("&a");
                sfDatosRegiones.append(Utilidades.completarLongitudEnteros(2, region.getIdDeRegion()));
                sfDatosRegiones.append("=0");

                //&t04=OBISPO: Abadia Mandres<br><br>1. Montealto - 15.302.900<br>2. Loarre - 14.813.900<br>3. Mandres -  14.517.500<br>4. Mendelen - 14.011.600<br>5. Sharra - 13.690.100<br>6. Tres_Picos - 13.561.000<br>7. Gschwind - 10.026.400<br>8. Araya - 9.349.090<br>9. Cogorzas SA - 6.725.980<br>10. LiveSion - 3.910.930
                sfDatosAbadias.append("&t");
                sfDatosAbadias.append(Utilidades.completarLongitudEnteros(2, region.getIdDeRegion()));
                sfDatosAbadias.append("=OBISPO: ");
                //añadimos el nombre de la abadía del obispo
                sfDatosAbadias.append(oObispadoAD.recuperarAbadiaObispo(region.getIdDeRegion()));
                sfDatosAbadias.append("<br>");

                alAbadias = oAbadiaAD.recuperarTopTenRegional(region.getIdDeRegion());

                for (AbadiaBasic abadia : alAbadias) {
                    sfDatosAbadias.append("<br>");
                    sfDatosAbadias.append(abadia.getPosicion());
                    sfDatosAbadias.append(". ");
                    sfDatosAbadias.append(abadia.getNombre());
                    sfDatosAbadias.append(" - ");
                    sfDatosAbadias.append(abadia.getPuntuacion());
                }
                sfDatosAbadias.append('\n');
            }

            //consolidamos el resultado en un fichero de texto plano
            sfDatos.append(sfDatosRegiones);
            sfDatosAbadias.append('\n');
            sfDatos.append(sfDatosAbadias);


            f = new FileWriter(AbadiaConfiguracion.getBasePath() + AbadiaConfiguracion.getPropiedad("ruta_datos_mapa") + Constantes.FICHERO_DATOS_MAPA, false);
            //f = new FileWriter(AbadiaConfiguracion.getBasePath() + "/" + Constantes.FICHERO_DATOS_MAPA, false);
            f.append(sfDatos);
            f.close();

        } catch (Exception e) {
            throw new AbadiaIOException(sTrace, e, log);
        } finally {
            try {
                f.close();
            } catch (IOException e) {
            }
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    /**
     * Genera un fichero de texto plano con los datos de comisiones de tránsito por región con los que se cargará el mapa flash
     *
     * @throws AbadiaException
     */
    public void generarDatosMapaComisiones() throws AbadiaException {
        String sTrace = this.getClass() + ".generarDatosMapaComisiones()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adRegion oRegionAD;

        ArrayList<Region> alRegiones;

        FileWriter f = null;
        StringBuffer sfDatosRegiones = new StringBuffer();
        //AbadiaConfiguracion abadiaconf = new AbadiaConfiguracion();
        //abadiaconf.Init(null);

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);

            //recuperamos la lista de regiones activas con código y nombre + el nombre de la abadía que ostenta el cargo de obispo en la región
            oRegionAD = new adRegion(con);
            alRegiones = oRegionAD.recuperarComisionTransitoRegiones();

            //recuperamos la lista de las 10 mejores abadías por región
            //&a04=1&a05=1&a11=1&a27=1&a31=1&a36=1&a39=1&a43=1&a52=1&a56=1&
            for (Region region : alRegiones) {
                sfDatosRegiones.append("&R");
                sfDatosRegiones.append(region.getIdDeRegion());
                sfDatosRegiones.append("=");
                sfDatosRegiones.append((int) region.getComisionTransito());
            }

            f = new FileWriter(AbadiaConfiguracion.getBasePath() + AbadiaConfiguracion.getPropiedad("ruta_datos_mapa") + Constantes.FICHERO_DATOS_MAPA_IT, false);
            //f = new FileWriter(AbadiaConfiguracion.getBasePath() + "/" + Constantes.FICHERO_DATOS_MAPA_IT, false);
            f.append(sfDatosRegiones);
            f.close();

        } catch (Exception e) {
            throw new AbadiaIOException(sTrace, e, log);
        } finally {
            try {
                f.close();
            } catch (IOException e) {
            }
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    /**
     * Recupera los usuarios que tienen abadías congeladas y no se han conectado en 'n' días (unos 2 meses)
     * Sobre los usuarios recuperados, se enviará un correo informando de que su abadía ha sido descongelada
     * para que tengan posibilidad de volver a congelarla, si no lo hacen, tras 20 días sin conectarse su abadía será eliminada.
     *
     * @throws org.abbatia.exception.base.AbadiaException
     *          excepción general
     */
    public void descongelarAbadias() throws AbadiaException {
        String sTrace = this.getClass() + ".descongelarAbadias()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adProcesos oProcesosAD;

        String sSQL = "SELECT usuario.USUARIOID, usuario.EMAIL, usuario.ABADIA_CONGELADA, usuario.ULTIMACONEXION " +
                " FROM usuario " +
                " WHERE usuario.ABADIA_CONGELADA =  '1' AND DATEDIFF(CURDATE(), usuario.ULTIMACONEXION)>100";

        CachedRowSetImpl crs = null;
        ArrayList<Email> alCorreo = new ArrayList<Email>();
        String asunto = "Abadia descongelada por inactividad";
        String body = "Tu abadía ha sido descongelada, esta operación se realiza como medida administrativa y de control, si deseas volver a congelarla, sólo necesitas volver a acceder para hacerlo.";

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS, Constantes.AUTOCOMIT_OF);

            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando SubProceso descongelarAbadias", 0);

            crs = new CachedRowSetImpl();
            crs.setCommand(sSQL);
            crs.execute(con);

            while (crs.next()) {
                //enviar correo de notificación de descongelación de abadía
                crs.updateShort("ABADIA_CONGELADA", (short) 0);
                crs.updateString("ULTIMACONEXION", CoreTiempo.getTiempoRealString());
                crs.updateRow();
                alCorreo.add(new Email(crs.getString("EMAIL"), asunto, body));
            }
            crs.acceptChanges(con);
            CoreMail.enviarCorreo(alCorreo);

            oProcesosAD.addLog("+ Finalizado SubProceso descongelarAbadias", 0);

        } catch (Exception e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(crs);
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    /**
     * Este proceso elimina de la base de datos las abadías que no tienen asociado ningún usuario
     * Estas abadías han quedado "huerfanas" por algún problema de procesos o por una eliminación indiscriminada de usuarios y genera
     * un descuadre entre número de abadías por región.
     * Se añade control para evitar la eliminación de la abadía 0 que representa la ciudad.
     *
     * @throws org.abbatia.exception.base.AbadiaException
     *
     */
    public void borrarAbadiasSinUsuario() throws AbadiaException {
        String sTrace = this.getClass() + ".borrarAbadiasSinUsuario()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adAbadia oAbadiaAD;
        adProcesos oProcesosAD;

        String sSQL = "select abadiaid from abadia where dummy = 0 and usuarioid not in (select usuarioid from usuario)";
        CachedRowSetImpl crs = null;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);

            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando SubProceso borrarAbadiasSinUsuario", 0);
            oAbadiaAD = new adAbadia(con);
            crs = new CachedRowSetImpl();
            crs.setCommand(sSQL);
            crs.execute(con);

            while (crs.next()) {
                oAbadiaAD.eliminarTodoAbadia(crs.getInt("abadiaid"));
            }

            oProcesosAD.addLog("+ Finalizado SubProceso borrarAbadiasSinUsuario", 0);

        } catch (Exception e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(crs);
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    /**
     * Este proceso fuerza la vuelta de los monjes de visita para las abadías que se han quedado sin monjes estado=0;
     *
     * @throws AbadiaException
     */
    public void forzarVueltaMonjesAbadiaVacia() throws AbadiaException {
        String sTrace = this.getClass() + ".forzarVueltaMonjesAbadiaVacia()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adProcesos oProcesosAD;
        adUtils oUtilsAD;
        adMonje oMonjeAD;
        adViajar oViajarAD;

        List<Integer> alAbadias;
        List<MonjeProceso> alMonjesViajando;


        String sSQL = "SELECT abadia.ABADIAID FROM abadia WHERE abadia.abadiaid not in (select abadiaid from monje where monje.ESTADO =  0)";
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);
            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando SubProceso forzarVueltaMonjesAbadiaVacia", 0);

            oUtilsAD = new adUtils(con);
            alAbadias = oUtilsAD.getSQLIntArray(sSQL);
            oMonjeAD = new adMonje(con);
            oViajarAD = new adViajar(con);
            for (int iAbadiaId : alAbadias) {
                alMonjesViajando = oMonjeAD.recuperarMonjesVisita(iAbadiaId);
                for (MonjeProceso oMonje : alMonjesViajando) {
                    log.info(sTrace + "forzando vuelta del monje: " + oMonje.getIdMonje() + " de la abadia: " + oMonje.getIdAbadia());
                    oViajarAD.forzarVueltaProceso(oMonje, Constantes.FORZAR_RETORNO_ABADIA_SIN_MONJES);
                }
            }
            oProcesosAD.addLog("+ Finalizado SubProceso forzarVueltaMonjesAbadiaVacia", 0);

        } catch (Exception e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

}

