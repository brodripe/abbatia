package org.abbatia.adbean;

import org.abbatia.actionform.ViajarActForm;
import org.abbatia.adbean.base.adbeans;
import org.abbatia.bean.*;
import org.abbatia.core.CoreTiempo;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.Utilidades;
import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class adViajar extends adbeans {
    private static Logger log = Logger.getLogger(adViajar.class.getName());


    public static int VIAJE_VISITA = 0;
    public static int VIAJE_ESPIA = 1;
    public static int VIAJE_CONCLAVE = 10;

    /**
     * Instancia la clase sin obtener una nueva conexión
     *
     * @param con
     * @throws AbadiaException
     */
    public adViajar(Connection con) throws AbadiaException {
        super(con);
    }

    /*  Función que informa la tabla de monje_viaje para los procesos de viaje
    */

    public void prepararViaje(Abadia abadia, ViajarActForm viajarFrm) throws AbadiaException {

        try {
            // Borrar de la tabla monje_viaje cualquier registro del monje para evitar problemas... por si aca ;)
            eliminarViajePorIdMonje(viajarFrm.getMonjeid());

            crearViajeSinFechas(viajarFrm.getMonjeid(), abadia.getIdDeAbadia(), viajarFrm.getAbadiaid_destino(), viajarFrm.getTipoViaje(), viajarFrm.getDias_pie(), viajarFrm.getDias_caballo(), viajarFrm.getCoste());

        } catch (AbadiaException e) {
            throw new AbadiaSQLException("adViajar. prepararViaje. ", e, log);
        }
    }

    /**
     * Da de alta un registro en la tabla monje_visita para iniciar un viaje.
     *
     * @param viajarFrm
     * @param abadia
     * @param fecha_partida_destino
     * @param fecha_llegada_destino
     * @param fecha_partida_origen
     * @throws AbadiaException
     */
    public void crearViaje(ViajarActForm viajarFrm, Abadia abadia, String fecha_partida_destino, String fecha_llegada_destino, String fecha_partida_origen) throws AbadiaException {
        crearViaje(viajarFrm.getMonjeid(), abadia.getIdDeAbadia(), viajarFrm.getAbadiaid_destino(), viajarFrm.getTipoViaje(), viajarFrm.getDias_pie(), viajarFrm.getDias_caballo(), fecha_partida_destino, fecha_llegada_destino, fecha_partida_origen, viajarFrm.getCoste());
    }

    /**
     * Gestiona la creacion de los viajes para los cardenales, son necesitad de autorización de las abadias receptoras
     *
     * @param abadiaid
     * @param monjeid
     * @param abadiaid_destino
     * @param tipo_viaje
     * @throws AbadiaException
     */
    public void prepararViaje(int abadiaid, int monjeid, int abadiaid_destino, int tipo_viaje) throws AbadiaException {
        adMonje monjeAD;
        adUtils utils;
        String fecha_llegada_destino;
        String fecha_partida_origen = null;
        String fecha_partida_destino;

        fecha_partida_destino = CoreTiempo.getTiempoAbadiaString();

        utils = new adUtils(con);
        int region_origen = utils.getSQL("Select regionid from abadia where abadiaid=" + abadiaid, 0);
        int region_destino = utils.getSQL("Select regionid from abadia where abadiaid=" + abadiaid_destino, 0);
        int dias = utils.getSQL("SELECT distancia_campo FROM `region_distancia` where regionid_origen = " + region_origen + " and regionid_destino = " + region_destino, 5);
        // Calcular los dias para llegar
        fecha_llegada_destino = CoreTiempo.getDiferenciaString(dias);
        // Marcar el monje con estado 3 para el viaje ( VIAJE )
        monjeAD = new adMonje(con);
        monjeAD.actualizarEstado(monjeid, Constantes.MONJE_VIAJE);

        // Borrar de la tabla monje_viaje cualquier registro del monje para evitar problemas... por si aca ;)
        eliminarViajePorIdMonje(monjeid);
        // Insertar los datos....
        crearViajeSinRetorno(monjeid, abadiaid, abadiaid_destino, tipo_viaje, dias, (dias / 2), fecha_partida_destino, fecha_llegada_destino, 0);
    }

    public void crearViajeSinRetorno(int monjeid, int abadiaid, int abadiaid_destino, int tipo_viaje, int dias_pie, int dias_caballo, String fecha_partida_destino, String fecha_llegada_destino, double coste) throws AbadiaException {
        adUtils utils = new adUtils(con);
        utils.execSQL("INSERT INTO `monje_visita` " +
                "(`MONJEID`, `ABADIAID`, `ABADIAID_DESTINO`, `RELIQUIAID`, `GUARDIAS`, `TIPO_VISITA`, DIAS_PIE, DIAS_CABALLO, " +
                "`FECHA_PARTIDA_DESTINO`, `FECHA_LLEGADA_DESTINO`, `FECHA_PARTIDA_ORIGEN`, `FECHA_LLEGADA_ORIGEN`, COSTE) " +
                "VALUES (" + monjeid + ", " + abadiaid + ", " + abadiaid_destino + ", " +
                "0, 0, " + tipo_viaje + ", " + dias_pie + "," + dias_caballo + "," +
                "'" + fecha_partida_destino + "', '" + fecha_llegada_destino + "', null, null, " + coste + " )");
    }

    public void crearViaje(int monjeid, int abadiaid, int abadiaid_destino, int tipo_viaje, int dias_pie, int dias_caballo, String fecha_partida_destino, String fecha_llegada_destino, String fecha_partida_origen, double coste) throws AbadiaException {
        adUtils utils = new adUtils(con);
        utils.execSQL("INSERT INTO `monje_visita` " +
                "(`MONJEID`, `ABADIAID`, `ABADIAID_DESTINO`, `RELIQUIAID`, `GUARDIAS`, `TIPO_VISITA`, DIAS_PIE, DIAS_CABALLO, " +
                "`FECHA_PARTIDA_DESTINO`, `FECHA_LLEGADA_DESTINO`, `FECHA_PARTIDA_ORIGEN`, `FECHA_LLEGADA_ORIGEN`, COSTE) " +
                "VALUES (" + monjeid + ", " + abadiaid + ", " + abadiaid_destino + ", " +
                "0, 0, " + tipo_viaje + ", " + dias_pie + "," + dias_caballo + "," +
                "'" + fecha_partida_destino + "', '" + fecha_llegada_destino + "', '" + fecha_partida_origen + "', null, " + coste + " )");
    }

    public void crearViajeSinFechas(int monjeid, int abadiaid, int abadiaid_destino, int tipo_viaje, int dias_pie, int dias_caballo, double coste) throws AbadiaException {
        adUtils utils = new adUtils(con);
        utils.execSQL("INSERT INTO `monje_visita` " +
                "(`MONJEID`, `ABADIAID`, `ABADIAID_DESTINO`, `RELIQUIAID`, `GUARDIAS`, `TIPO_VISITA`, DIAS_PIE, DIAS_CABALLO, " +
                "`FECHA_PARTIDA_DESTINO`, `FECHA_LLEGADA_DESTINO`, `FECHA_PARTIDA_ORIGEN`, `FECHA_LLEGADA_ORIGEN`, COSTE) " +
                "VALUES (" + monjeid + ", " + abadiaid + ", " + abadiaid_destino + ", " +
                "0, 0, " + tipo_viaje + ", " + dias_pie + "," + dias_caballo + ", null, null, null, null, " + coste + " )");
    }

    /*  Se procede a viajar
    */

    public void forzarVuelta(Abadia abadia, Monje monje, String accion, MessageResources resource) throws AbadiaException {
        forzarVuelta(abadia.getIdDeAbadia(), monje.getIdMonje(), accion, resource);
    }

    public void forzarVuelta(int abadiaID, int monjeID, String accion, MessageResources resource) throws AbadiaException {
        String msg1, literalDe;
        int idiomaId;
        int idLibroOrigen;
        int idLibroCopia;

        adLibros libroAD;
        adUtils utils;
        adMonje monjeAD;
        adHabilidades adhabil;
        adRecurso oRecursoAD;
        adAbadia oAbadiaAD;
        adMensajes oMensajesAD;

        ArrayList<Mensajes> alMensajes = new ArrayList<Mensajes>();
        String sNombreMonje = "";
        Libro libroCopia = null;
        Libro libro = null;

        try {
            //Si el monje no ha sido expulsado, y se ha ido por su voluntad, deberá pagar el % de lo que ha realizado sin llevarse el libro.
            //recuperamos el idLibro que hemos venido a copiar
            libroAD = new adLibros(con);
            idLibroOrigen = libroAD.recuperarLibroPorTareaMonje(monjeID);
            if (idLibroOrigen != 0) {
                //recuperamos la info del libro
                libro = libroAD.recuperarLibro(idLibroOrigen);
            }
            //recuperamos el idlibro de nuestra copia
            idLibroCopia = libroAD.recuperarLibroPorTareaMonjeCopia(monjeID);
            if (idLibroCopia != 0) {
                //recuperamos la informacion del libro que hemos venido a copiar
                libroCopia = libroAD.recuperarLibro(idLibroCopia);
            }

            if (accion.equals("expulsar")) {
                utils = new adUtils(con);
                idiomaId = utils.getIdiomaID(abadiaID);
//                literalDe = utils.getLiteral(5000, idiomaId);
                // Ha sido expulsado de la abadía %s por el Abad y esta regresando a la abadía
                msg1 = utils.getLiteral(12110, idiomaId);

                //hay que devolver la pasta del "fondo de garantias" si está copiando un libro
                if (libro != null) {
                    //recuperamos los datos del libro
                    oRecursoAD = new adRecurso(con);
                    oRecursoAD.sumarRecurso(Constantes.RECURSOS_ORO, libroCopia.getIdAbadia(), libro.getPrecioCopia());
                }
                //recuperamos el nombre de la abbatia
                oAbadiaAD = new adAbadia(con);
                msg1 = Utilidades.Format(msg1, oAbadiaAD.getNomAbadia(abadiaID));
                alMensajes.add(new Mensajes(abadiaID, monjeID, msg1, 0));

            } else {
                utils = new adUtils(con);
                int abadiaid_destino = utils.getSQL("SELECT abadiaid_destino FROM monje_visita WHERE monjeid=" + monjeID, 0);
                idiomaId = utils.getIdiomaID(abadiaid_destino);
                literalDe = utils.getLiteral(5000, idiomaId);

                if (accion.equals("enfadado")) {
                    // El %s %s se ha marchado enfadado... ha hecho que baje la Fé en tú abadía
                    utils = new adUtils(con);
                    msg1 = utils.getLiteral(12111, idiomaId);

                    monjeAD = new adMonje(con);
                    msg1 = Utilidades.Format(msg1, monjeAD.getDescJerarquiaMonje(monjeID, idiomaId));
                    msg1 = Utilidades.Format(msg1, monjeAD.getNomMonje(monjeID, literalDe));

                    alMensajes.add(new Mensajes(abadiaid_destino, -1, msg1, 0));
                    //mensajes.crearMensaje(abadiaid_destino, -1, -1, 0, msg1);
                    // Decrementar la fé
                    adhabil = new adHabilidades(con);
                    adhabil.decrementarHabilidad(abadiaid_destino, Constantes.HABILIDAD_FE, 25);
                } else {
                    // El %s %s ha decidido marcharse ya de tú abadía
                    utils = new adUtils(con);
                    msg1 = utils.getLiteral(12112, idiomaId);

                    monjeAD = new adMonje(con);
                    msg1 = Utilidades.Format(msg1, monjeAD.getDescJerarquiaMonje(monjeID, idiomaId));
                    sNombreMonje = monjeAD.getNomMonje(monjeID, literalDe);

                    msg1 = Utilidades.Format(msg1, sNombreMonje);

                    alMensajes.add(new Mensajes(abadiaid_destino, -1, msg1, 0));
                    //mensajes.crearMensaje(abadiaid_destino, -1, -1, 0, msg1);

                    //si el libro que estábamos copiando sigue existiendo
                    if (libro != null) {
                        //importe = ( Numero de paginas realizadas * 100) / numero maximo de paginas.
                        double importe = libroCopia.getNumPaginasCopiadas() * libro.getPrecioCopia() / libro.getNumPaginas();
                        //si de debe algo :-D
                        if (importe > 0) {
                            oRecursoAD = new adRecurso(con);
                            //Ingreso del importe a la abbatia destino.
                            oRecursoAD.sumarRecurso(Constantes.RECURSOS_ORO, libro.getIdAbadia_copia(), importe);
                            //Devuelvo a la abbatia origen el dinero que no se ha invertido en la copia
                            oRecursoAD.sumarRecurso(Constantes.RECURSOS_ORO, libro.getIdAbadia(), libro.getPrecioCopia() - importe);

                            //recuperamos el id del libro que se ha dejado a medias...
                            //idLibroCopia = adLibros.recuperarLibroPorTareaMonjeCopia(monjeID);
                            //actualizamos en el registro del libro copia el importe invertido en esa copia....
                            libroAD = new adLibros(con);
                            libroAD.actualizarPrecioCopia(idLibroCopia, importe);

//                          mensajes.copiar.regreso.origen=Has reclamado su retorno de la abbatia {1} abonando {2} monedas de oro por los gastos ocasionados
//                          mensajes.copiar.regreso.destino=El monje {0} de la abbatia {1} ha abandonado la copia de su libro para regresar con los suyos abonando a tus arcas {2} monedas de oro por los servicios prestados
                            //mensaje de aviso del ingreso a la abbatia destino
                            //resource.
                            oAbadiaAD = new adAbadia(con);
                            String sNombreAbadiaCopia = oAbadiaAD.getNomAbadia(libroCopia.getIdAbadia_copia());
                            String sMsg = resource.getMessage("mensajes.copiar.regreso.origen", sNombreAbadiaCopia, Utilidades.redondear(importe));
                            alMensajes.add(new Mensajes(libroCopia.getIdAbadia(), monjeID, sMsg, 0));

                            //mensaje de aviso del cargo a la abbatia origen
                            String sNombreAbadiaOrigen = oAbadiaAD.getNomAbadia(libroCopia.getIdAbadia());
                            sMsg = resource.getMessage("mensajes.copiar.regreso.destino", new String[]{sNombreMonje, sNombreAbadiaOrigen, Utilidades.redondear(importe)});
                            alMensajes.add(new Mensajes(libroCopia.getIdAbadia_copia(), -1, sMsg, 0));
                        }
                    }

                }
            }
            //Si el monje estaba copiando un libro, se tendrian que desvincular todas las tareas de copia del libro asociado al monje
            //además de las tareas de el monje.
            if (idLibroOrigen != 0) {
                libroAD = new adLibros(con);
                //eliminar los resgistros temporales de actividades del monje
                libroAD.eliminarTareasCopiaPorMonje(monjeID);

                //eliminar bloqueos sobre actividades de la abbatia destino relativas al libro
                //que se solicito copiar.
                monjeAD = new adMonje(con);
                monjeAD.desbloquarActividadesCopia(monjeID);

                //actualizar el estado del libro a INCOMPLETO
                libroAD.actualizarEstadoLibro(idLibroCopia, Constantes.ESTADO_LIBRO_INCOMPLETO);
            }
            oMensajesAD = new adMensajes(con);
            oMensajesAD.crearMensajes(alMensajes);

            // El proceso se encarga de la vuelta
            actualizarFechaPartidaOrigen(monjeID);

        } catch (AbadiaException e) {
            throw new AbadiaSQLException("adViajar. forzarVuelta. ", e, log);
        }
    }

    public void forzarVueltaProceso(MonjeProceso monje, int p_iCausaVuelta) throws AbadiaException {
        String msg1;
        int idLibroOrigen;
        int idLibroCopia;
        int idAbadiaVisita;
        adLibros libroAD;
        adUtils utils;
        adRecurso oRecursoAD;
        adAbadia oAbadiaAD;
        adMonje oMonjeAD;
        adMensajes oMensajesAD;

        ArrayList<Mensajes> alMensajes = new ArrayList<Mensajes>();
        Libro libroCopia = null;
        Libro libro = null;

        try {
            //Si el monje no ha sido expulsado, y se ha ido por su voluntad, deberá pagar el % de lo que ha realizado sin llevarse el libro.
            //recuperamos el idLibro que hemos venido a copiar
            libroAD = new adLibros(con);
            idLibroOrigen = libroAD.recuperarLibroPorTareaMonje(monje.getIdMonje());
            if (idLibroOrigen != 0) {
                //recuperamos la info del libro
                libro = libroAD.recuperarLibro(idLibroOrigen);
            }
            //recuperamos el idlibro de nuestra copia
            idLibroCopia = libroAD.recuperarLibroPorTareaMonjeCopia(monje.getIdMonje());
            if (idLibroCopia != 0) {
                //recuperamos la informacion del libro que hemos venido a copiar
                libroCopia = libroAD.recuperarLibro(idLibroCopia);
            }

            utils = new adUtils(con);
            // Ha avandonado la abadía %s, el libro que estaba copiando ha desaparecido
            msg1 = utils.getLiteral(p_iCausaVuelta, monje.getIdIdioma());

            //hay que devolver la pasta del "fondo de garantias" si está copiando un libro
            if (libro != null) {
                oRecursoAD = new adRecurso(con);
                //recuperamos los datos del libro
                oRecursoAD.sumarRecurso(Constantes.RECURSOS_ORO, libroCopia.getIdAbadia(), libro.getPrecioCopia());
            }
            //recuperamos el nombre de la abbatia
            oAbadiaAD = new adAbadia(con);
            if (p_iCausaVuelta == Constantes.FORZAR_RETORNO_LIBRO_DETERIORADO) {
                msg1 = Utilidades.Format(msg1, oAbadiaAD.getNomAbadia(libroCopia.getIdAbadia_copia()));
            }

            alMensajes.add(new Mensajes(monje.getIdAbadia(), monje.getIdMonje(), msg1, 0));

            //Si el monje estaba copiando un libro, se tendrian que desvincular todas las tareas de copia del libro asociado al monje
            //además de las tareas de el monje.
            if (idLibroOrigen != 0) {
                //eliminar los resgistros temporales de actividades del monje
                libroAD.eliminarTareasCopiaPorMonje(monje.getIdMonje());

                //eliminar bloqueos sobre actividades de la abbatia destino relativas al libro
                //que se solicito copiar.
                oMonjeAD = new adMonje(con);
                oMonjeAD.desbloquarActividadesCopia(monje.getIdMonje());

                //actualizar el estado del libro a INCOMPLETO
                libroAD.actualizarEstadoLibro(idLibroCopia, Constantes.ESTADO_LIBRO_INCOMPLETO);
            }
            oMensajesAD = new adMensajes(con);
            oMensajesAD.crearMensajes(alMensajes);

            // El proceso se encarga de la vuelta
            actualizarFechaPartidaOrigen(monje.getIdMonje());

        } catch (AbadiaException e) {
            throw new AbadiaSQLException("adViajar. forzarVueltaProceso. ", e, log);
        }
    }

    /**
     * Comprueba si se puede volver a la abadia, si es del tipo 10; conclave, pues va ser que no
     *
     * @param abadiaID la abadia de visita
     * @param monjeID  el monje que esta de visita
     * @return boolean devuelve si puede volver o no de la abadia destino
     * @throws AbadiaException Excepción base de abbatia
     */
    public boolean puedeForzarVuelta(int abadiaID, int monjeID) throws AbadiaException {
        adUtils utils;
        try {
            utils = new adUtils(con);
            int tipo_visita = utils.getSQL("SELECT tipo_visita FROM `monje_visita` WHERE monjeid = " + monjeID + " and abadiaid=" + abadiaID + " limit 1", 0);

            if (tipo_visita == 10)
                return false;
            else return true;

        } catch (AbadiaException e) {
            throw new AbadiaSQLException("adViajar. puedeForzarVuelta. ", e, log);
        }
    }

    /* Devuelve verdadero si la abbatia puede ver el monje... pa piratillas ( controla la orige y la destino )
    */

    public boolean chkMonjeSePuedeVisualizar(Abadia abadia, Monje monje) throws AbadiaException {
        boolean salir = false;
        adUtils utils = null;
        try {
            if (abadia.getIdDeAbadia() == monje.getIdAbadia())
                return true;
            utils = new adUtils(con);
            if (utils.getSQL("SELECT 1 FROM `monje_visita` WHERE abadiaid_destino = " + abadia.getIdDeAbadia() + " and monjeid = " + monje.getIdMonje(), 0) == 1) {
                salir = true;
            }

            return salir;

        } catch (AbadiaException e) {
            throw new AbadiaSQLException("adViajar. chkMonjeSePuedeVisualizar. ", e, log);
        } finally {
            //if (utils != null) utils.finalize();
        }

    }

    public ArrayList<Table> getMonjesLista(Abadia abadia, int idiomaid, MessageResources resource) throws AbadiaException {
        String sSQL = "SELECT l.literal, m.nombre, m.apellido1, m.monjeid " +
                " FROM monje m, `jerarquia_eclesiastica` je, literales l " +
                " where m.jerarquiaid = je.jerarquiaid and je.literalid = l.literalid and l.idiomaid = " + idiomaid +
                " and m.abadiaid=" + abadia.getIdDeAbadia() + " and (je.jerarquiaid > 2 or je.jerarquiaid = 1) and estado = 0  " +
                " order by nombre, apellido1";
        String Nom;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            ArrayList<Table> alTable = new ArrayList<Table>();
            Table tabla;
            while (rs.next()) {
                Nom = rs.getString("literal") + " - " + rs.getString("nombre") + " " + resource.getMessage("monjes.abadia.nomciudad") + " " + rs.getString("apellido1");
                tabla = new Table(rs.getInt("monjeid"), Nom);
                alTable.add(tabla);
            }
            return alTable;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adViajar. getMonjesLista. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }


    /*  Restricciones de la abbatia para viajar, el usuario tiene que tener más de 30 días de alta ( 1 año abbatia )
    */

    public boolean monjesExperiencia(Usuario usuario, Abadia abadia) throws AbadiaException {
        boolean salir = false;
        try {
            adUtils utils = new adUtils(con);
            if (utils.getSQL("SELECT 1 FROM `usuario` WHERE usuarioid = " + usuario.getIdDeUsuario() + " and fechaalta < Date_Sub(Now(), Interval 30 day) limit 1", 0) == 1) {
                salir = true;
            }
        } catch (AbadiaException e) {
            throw new AbadiaSQLException("adViajar. monjesExperiencia. ", e, log);
        }
        return salir;
    }

    /*  Restricciones de la abbatia para viajar, la abbatia solo puede tener un monje de viaje o visita
    */

    public boolean monjesViajando(Abadia abadia) throws AbadiaException {
        return monjesViajando(abadia.getIdDeAbadia());
    }

    /*  Restricciones de la abbatia para viajar, la abbatia solo puede tener un monje de viaje o visita
    */

    public boolean monjesViajando(int idAbadia) throws AbadiaException {
        boolean salir = false;
        adUtils utils;
        try {
            utils = new adUtils(con);
            if (utils.getSQL("SELECT 1 FROM `monje` m, `monje_visita` mj WHERE m.monjeid = mj.monjeid and m.estado <>0 and mj.abadiaid=" + idAbadia + " limit 1", 0) == 1) {
                salir = true;
            }
        } catch (AbadiaException e) {
            throw new AbadiaSQLException("adViajar. monjesViajando. ", e, log);
        }
        return salir;
    }


    /**
     * Determina si existe algún monje de visita en la abadía
     *
     * @param abadia
     * @return
     * @throws AbadiaException
     */
    public boolean monjesVisita(Abadia abadia) throws AbadiaException {
        return monjesVisita(abadia.getIdDeAbadia());
    }

    /**
     * Determina si existe algún monje de visita en la abadía
     *
     * @param idAbadia
     * @return
     * @throws AbadiaException
     */
    public boolean monjesVisita(int idAbadia) throws AbadiaException {
        boolean salir = false;
        adUtils utils;
        try {
            utils = new adUtils(con);
            if (utils.getSQL("SELECT 1 FROM `monje` m, `monje_visita` mj WHERE m.monjeid = mj.monjeid and m.estado <>0 and mj.abadiaid_destino=" + idAbadia + " limit 1", 0) == 1) {
                salir = true;
            }
        } catch (AbadiaException e) {
            throw new AbadiaSQLException("adViajar. monjesVisita. ", e, log);
        }
        return salir;
    }


    public void recuperarForm(Abadia abadia, ViajarActForm viajarFrm) throws AbadiaException {
        adUtils utils;
        try {
            // Abadia origen
            viajarFrm.setAbadiaid_origen(abadia.getIdDeAbadia());
            viajarFrm.setAbadia_origen(abadia.getNombre());

            utils = new adUtils(con);
            String str = utils.getSQL("Select descripcion from region where regionid=" + abadia.getIdDeRegion(), "");

            viajarFrm.setRegion_origen(str);
            // Abadia destino
            Abadia abadia_destino;

            adAbadia abadiaAD = new adAbadia(con);
            abadia_destino = abadiaAD.recuperarAbadia(viajarFrm.getAbadiaid_destino());

            viajarFrm.setAbadia_destino(abadia_destino.getNombre());

            utils = new adUtils(con);
            str = utils.getSQL("Select descripcion from region where regionid=" + abadia_destino.getIdDeRegion(), "");
            viajarFrm.setRegion_destino(str);
            // Distancias
            int dist_camino = utils.getSQL("SELECT distancia_campo FROM `region_distancia` where regionid_origen = " + abadia.getIdDeRegion() + " and regionid_destino = " + abadia_destino.getIdDeRegion(), 5);
            viajarFrm.setDias_camino_pie(Utilidades.redondear(dist_camino));
            viajarFrm.setDias_camino_caballo(Utilidades.redondear(dist_camino / 2));
            int dist_montan = utils.getSQL("SELECT distancia_montanyas FROM `region_distancia` where regionid_origen = " + abadia.getIdDeRegion() + " and regionid_destino = " + abadia_destino.getIdDeRegion(), 1);
            viajarFrm.setDias_montanya_pie(Utilidades.redondear(dist_montan));
            viajarFrm.setDias_montanya_caballo(Utilidades.redondear(dist_montan / 2));
            int dist_barco = utils.getSQL("SELECT distancia_mar FROM `region_distancia` where regionid_origen = " + abadia.getIdDeRegion() + " and regionid_destino = " + abadia_destino.getIdDeRegion(), 0);
            viajarFrm.setDias_barco(Utilidades.redondear(dist_barco));
            viajarFrm.setDias_caballo((dist_camino / 2) + (dist_montan / 2) + dist_barco);
            viajarFrm.setDias_pie(dist_camino + dist_montan + dist_barco);

            // Totales
            recuperarPrecio(viajarFrm);
            if (dist_camino == 12) {
                viajarFrm.setTotal(0);
            }


        } catch (AbadiaException e) {
            throw new AbadiaSQLException("adViajar. recuperarForm. ", e, log);
        }
    }

    public static void recuperarPrecio(ViajarActForm viajarFrm) throws AbadiaException {
        viajarFrm.setTotal(viajarFrm.getDias_pie() * 100);
        viajarFrm.setTotal_caballo((viajarFrm.getDias_caballo() * 100) + 500);
    }


    /**
     * Recupera el coste del viaje para devolverlo a la abadía
     * en caso de que se cancele el viaje.
     *
     * @param solicitud
     * @throws AbadiaException
     */
    public void devolverCosteViaje(Solicitud solicitud) throws AbadiaException {
        adRecurso recursoAD;
        double coste;
        String sSQL = "SELECT COSTE FROM monje_visita WHERE MONJEID = " + solicitud.getIdMonje();

        adUtils utils = new adUtils(con);
        coste = utils.getSQL(sSQL, 0);

        if (coste > 0) {
            recursoAD = new adRecurso(con);
            recursoAD.sumarRecurso(Constantes.RECURSOS_ORO, solicitud.getIdAbadia(), coste);
        }

    }

    /**
     * Elimina el registro de monje_viajes asocia a ese monje
     *
     * @param idMonje
     * @throws AbadiaException
     */
    public void eliminarViajePorIdMonje(int idMonje) throws AbadiaException {
        String sSQLMonje = "DELETE FROM monje_visita WHERE MONJEID = " + idMonje;
        String sSQLAnimal = "DELETE FROM monje_visita_animal where monjeid = " + idMonje;
        adUtils utils = null;
        try {
            utils = new adUtils(con);
            utils.execSQL(sSQLMonje);
            utils.execSQL(sSQLAnimal);
        } finally {
            //if (utils != null) utils.finalize();
        }
    }


    /**
     * Inserta un registro en la tabla de monje_visita_animal
     *
     * @param idMonje
     * @param idAnimal
     * @throws AbadiaException
     */
    public void insertarMonjeVisitaAnimal(int idMonje, int idAnimal) throws AbadiaException {
        String sSQL = "insert into monje_visita_animal (monjeid, animalid) values (" + idMonje + ", " + idAnimal + ")";

        adUtils utils = new adUtils(con);
        utils.execSQL(sSQL);
    }

    /**
     * Recupera un vector de identificadores de animales que acompañan al monje en su viaje
     *
     * @param idMonje
     * @return
     * @throws AbadiaException
     */
    public ArrayList<Integer> recuperarAnimalesVisita(int idMonje) throws AbadiaException {

        String sSQL = "select animalid from monje_visita_animal where monjeid = " + idMonje;
        adUtils utils = new adUtils(con);
        return utils.getSQLIntArray(sSQL);
    }


    /**
     * Actualiza a 1 el estado del viaje y las fechas de partida y llegada previstas
     * para activarlo en el siguiente proceso
     *
     * @param idMonje
     * @throws AbadiaException
     */
    public void iniciarViajeMonje(int idMonje) throws AbadiaException {
        String sSQLPie = "update monje_visita set fecha_partida_destino = '" + CoreTiempo.getTiempoAbadiaString() + "', fecha_llegada_destino = ADDDATE('" + CoreTiempo.getTiempoAbadiaString() + "', dias_pie)  where monjeid = " + idMonje;
        String sSQLCaballo = "update monje_visita set fecha_partida_destino = '" + CoreTiempo.getTiempoAbadiaString() + "', fecha_llegada_destino = ADDDATE('" + CoreTiempo.getTiempoAbadiaString() + "', dias_caballo)  where monjeid = " + idMonje;
        int idTipoAnimal;
        boolean hayCaballo = false;
        adAnimal oAnimalAD;
        adUtils utils;

        oAnimalAD = new adAnimal(con);
        ArrayList<Integer> idAnimales = recuperarAnimalesVisita(idMonje);

        for (int idAnimale : idAnimales) {
            idTipoAnimal = oAnimalAD.getTipoAnimal(idAnimale);
            if (idTipoAnimal == Constantes.ANIMALES_CABALLO || idTipoAnimal == Constantes.ANIMALES_YEGUA) {
                hayCaballo = true;
            }
        }

        utils = new adUtils(con);
        if (hayCaballo) {
            utils.execSQL(sSQLCaballo);
        } else {
            utils.execSQL(sSQLPie);
        }

    }

    /**
     * Actualiza la fecha de partida para origen (retorno) de un viaje
     *
     * @param idMonje
     * @throws AbadiaException
     */
    public void actualizarFechaPartidaOrigen(int idMonje) throws AbadiaException {
        // El proceso se encarga de la vuelta
        adUtils utils = new adUtils(con);
        utils.execSQL("UPDATE `monje_visita` SET `FECHA_PARTIDA_ORIGEN`='" + CoreTiempo.getTiempoAbadiaString() + "' WHERE `MONJEID`=" + idMonje);

    }


    /**
     * Actualiza el campo dummy del viaje con el identificador del libro copiado
     *
     * @param idMonje
     * @param valor
     * @throws AbadiaException
     */
    public void actualizarDummy(int idMonje, String valor) throws AbadiaException {
        adUtils utils = new adUtils(con);
        utils.execSQL("UPDATE `monje_visita` SET `DUMMY`= " + valor + " WHERE `MONJEID`=" + idMonje);
    }

}
