package org.abbatia.adbean;

import org.abbatia.bean.*;
import org.abbatia.core.CoreTiempo;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaProcesoException;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.CampoNoEncontradoException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.process.adbean.adProcesos;
import org.abbatia.process.utils.ProcesosUtils;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.HTML;
import org.abbatia.utils.Utilidades;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;

public class adCampo extends adEdificio {
    private static Logger log = Logger.getLogger(adCampo.class.getName());

    /**
     * Instancia la clase sin obtener una nueva conexión
     *
     * @param con
     * @throws AbadiaException
     */
    public adCampo(Connection con) throws AbadiaException {
        super(con);
    }


    public void crearCampo(Abadia abadia) throws AbadiaException {
        String sSQL = "Insert Into campo ( ABADIAID, FECHA_CREACION, ESTADO) Values (?, ?, 0)";

        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno los valores
            int parNo = 1;
            ps.setLong(parNo++, abadia.getIdDeAbadia());
            ps.setString(parNo, CoreTiempo.getTiempoAbadiaString());
            // Ejecutarlo
            ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adCampo. crearCampo. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public void setEstadoCampo(String idCultivo, int estado, Abadia abadia) throws AbadiaException {
        String sSQL = "update campo set estado=? where CAMPOID = ? and ABADIAID = ?";

        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno los valores
            int parNo = 1;
            ps.setInt(parNo++, estado);
            ps.setLong(parNo++, Integer.parseInt(idCultivo));
            ps.setLong(parNo, abadia.getIdDeAbadia());
            // Ejecutarlo
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new AbadiaSQLException("adCampo. setEstadoCampo. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public void sembrarCampo(int idCultivo, int idRecurso, int idAbadia, double aradoTotal) throws AbadiaException {
        String sSQL = "update campo set estado=?, alimentoid=?, fecha_siembra=?, siembra_total=?, fecha_cultivo_desde=?, fecha_cultivo_hasta=?, fecha_recogida_desde=?, fecha_recogida_hasta=?, cultiva_total=?, nivel_campo = ? " +
                " where CAMPOID = ? and ABADIAID = ? ";

        Cultivo cultivo = new Cultivo();
        cultivo.setIdRecurso(idRecurso);
        cultivo.setIdCultivo(idCultivo);
        cultivo.setArado_total(aradoTotal);
        cultivo = completarDatosCultivo(cultivo);
        if (cultivo == null) {
            throw new CampoNoEncontradoException("adCampo. sembrarCampo. No se ha encontrado el campo para sembrar", log);
        }
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno los valores
            int parNo = 1;
            ps.setInt(parNo++, Constantes.ESTADO_CULTIVO_CULTIVANDO);
            ps.setInt(parNo++, cultivo.getIdAlimento());
            ps.setString(parNo++, cultivo.getFechaSiembra());
            ps.setDouble(parNo++, cultivo.getSiembra_total());
            ps.setString(parNo++, cultivo.getFechaInicioCultivo());
            ps.setString(parNo++, cultivo.getFechaFinCultivo());
            ps.setString(parNo++, cultivo.getFechaInicioRecogida());
            ps.setString(parNo++, cultivo.getFechaFinRecogida());
            ps.setDouble(parNo++, cultivo.getSiembra_total());
            ps.setInt(parNo++, cultivo.getNivelCampo());
            ps.setInt(parNo++, idCultivo);
            ps.setInt(parNo, idAbadia);

            // Ejecutarlo
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new AbadiaSQLException("adCampo. sembrarCampo. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public void sembrarCampoNivel(Cultivo cultivo) throws AbadiaException {
        String sSQL = "update campo set estado=?, alimentoid=?, fecha_siembra=?, fecha_cultivo_desde=?, fecha_cultivo_hasta=?, fecha_recogida_desde=?, fecha_recogida_hasta=?, cultiva_total=?, nivel_campo=?, recogida_total=0 " +
                " where CAMPOID = ? and ABADIAID = ? ";

        cultivo = completarDatosCultivoNivel(cultivo);

        if (cultivo == null) {
            throw new CampoNoEncontradoException("adCampo. sembrarCampo. No se ha encontrado el campo para sembrar", log);
        }
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno los valores
            int parNo = 1;
            ps.setInt(parNo++, Constantes.ESTADO_CULTIVO_CULTIVANDO);
            ps.setInt(parNo++, cultivo.getIdAlimento());
            ps.setString(parNo++, cultivo.getFechaSiembra());
            ps.setString(parNo++, cultivo.getFechaInicioCultivo());
            ps.setString(parNo++, cultivo.getFechaFinCultivo());
            ps.setString(parNo++, cultivo.getFechaInicioRecogida());
            ps.setString(parNo++, cultivo.getFechaFinRecogida());
            ps.setDouble(parNo++, cultivo.getSiembra_total());
            ps.setInt(parNo++, cultivo.getNivelCampo());
            ps.setInt(parNo++, cultivo.getIdCultivo());
            ps.setInt(parNo, cultivo.getIdAbadia());

            // Ejecutarlo
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new AbadiaSQLException("adCampo. sembrarCampoNivel. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public ArrayList<Cultivo> getCultivosAbadia(Abadia abadia, Usuario usuario) throws AbadiaException {
        String sSQL = "Select l.LITERAL, c.* " +
                "from campo c, campo_estado ce, literales l where c.ABADIAID=? and c.ESTADO = ce.ESTADO AND ce.LITERALID = l.LITERALID and l.IDIOMAID=?";

        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setLong(1, abadia.getIdDeAbadia());
            ps.setLong(2, usuario.getIdDeIdioma());
            rs = ps.executeQuery();
            Cultivo cultivo;
            ArrayList<Cultivo> listaCampos = new ArrayList<Cultivo>();
            while (rs.next()) {
                cultivo = new Cultivo();
                cultivo.setIdEstado(rs.getInt("ESTADO"));
                cultivo.setSiembra_total(rs.getFloat("Siembra_total"));
                cultivo.setCultiva_total(rs.getFloat("Cultiva_total"));
                cultivo.setRecogida_total(rs.getFloat("Recogida_total"));
                cultivo.setArado_total(rs.getDouble("ARADO_TOTAL"));

                cultivo.setDescEstado(rs.getString("LITERAL"));
                cultivo.setFechaCreacion(Utilidades.formatStringFromDB(rs.getString("FECHA_CREACION")));
                cultivo.setIdCultivo(rs.getInt("CAMPOID"));
                //se recupera el número de monjes trabajando en el campo
                cultivo.setNumMonjes(getNumMonjesPorCampo(cultivo.getIdCultivo()));

                if (rs.getInt("ALIMENTOID") == 0) {
                    cultivo.setIdAlimento(0);
                    cultivo.setIdRecurso(0);
                    cultivo.setDescAlimento("-");
                } else {
                    cultivo.setIdAlimento(rs.getInt("ALIMENTOID"));
                    cultivo = recuperarRecursoID(cultivo);
                    cultivo.setDescAlimento(recuperarDescripcionCultivo(cultivo.getIdRecurso(), usuario) + " (" + rs.getInt("NIVEL_CAMPO") + ")");
                }


                if (rs.getString("FECHA_SIEMBRA") == null) {
                    cultivo.setFechaSiembra("-");
                } else cultivo.setFechaSiembra(Utilidades.formatStringFromDB(rs.getString("FECHA_SIEMBRA")));

                if (rs.getString("FECHA_CULTIVO_DESDE") == null) {
                    cultivo.setFechaInicioCultivo("-");
                } else
                    cultivo.setFechaInicioCultivo(Utilidades.formatStringFromDB(rs.getString("FECHA_CULTIVO_DESDE")));

                if (rs.getString("FECHA_CULTIVO_HASTA") == null) {
                    cultivo.setFechaFinCultivo("-");
                } else cultivo.setFechaFinCultivo(Utilidades.formatStringFromDB(rs.getString("FECHA_CULTIVO_HASTA")));

                if (rs.getString("FECHA_RECOGIDA_DESDE") == null) {
                    cultivo.setFechaInicioRecogida("-");
                } else
                    cultivo.setFechaInicioRecogida(Utilidades.formatStringFromDB(rs.getString("FECHA_RECOGIDA_DESDE")));

                if (rs.getString("FECHA_RECOGIDA_HASTA") == null) {
                    cultivo.setFechaFinRecogida("-");
                } else cultivo.setFechaFinRecogida(Utilidades.formatStringFromDB(rs.getString("FECHA_RECOGIDA_HASTA")));

                actualizarBarrasHTML(cultivo);

                listaCampos.add(cultivo);
            }
            return listaCampos;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adCampo. getCultivosAbadia. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /* Barras de HTML
    */

    public void actualizarBarrasHTML(Cultivo cultivo) {
        String sHTML = "";
        int n;

        /*
        0 - Inactivo
        1 - Arrando
        2 - Sembrar
        3 - Sembrando
        4 - Cultivando
        5 - Recogiendo
        6 - Fianlzado
        */
        if (cultivo.getIdEstado() == Constantes.ESTADO_CULTIVO_ARADO || cultivo.getIdEstado() == Constantes.ESTADO_CULTIVO_CULTIVANDO || cultivo.getIdEstado() == Constantes.ESTADO_CULTIVO_RECOGIENDO || cultivo.getIdEstado() == Constantes.ESTADO_CULTIVO_ARANDO) {
            n = (int) ((cultivo.getArado_total() * 10) / Constantes.CULTIVO_TOTAL_ARADO);
            sHTML = sHTML + HTML.smallBarra(n, "Arado: " + Utilidades.redondear(cultivo.getArado_total()) + " de " + " " + Utilidades.redondear(Constantes.CULTIVO_TOTAL_ARADO));
            sHTML = sHTML + "<br>";
        }
        if (cultivo.getIdEstado() == Constantes.ESTADO_CULTIVO_CULTIVANDO || cultivo.getIdEstado() == Constantes.ESTADO_CULTIVO_RECOGIENDO) {
            n = (int) Math.round((cultivo.getCultiva_total() * 10 / cultivo.getSiembra_total()));
            sHTML = sHTML + HTML.smallBarra(n, "Cultivo: " + Utilidades.redondear(cultivo.getCultiva_total()) + " de " + Utilidades.redondear(cultivo.getSiembra_total()));
            sHTML = sHTML + "<br>";
        }
        if (cultivo.getIdEstado() == Constantes.ESTADO_CULTIVO_RECOGIENDO) {
            n = (int) Math.round((cultivo.getRecogida_total() * 10 / cultivo.getCultiva_total()));
            sHTML = sHTML + HTML.smallBarra(n, "Recogida: " + Utilidades.redondear(cultivo.getRecogida_total()) + " de " + Utilidades.redondear(cultivo.getCultiva_total()));
        }
        if (sHTML.equals("")) sHTML = "&nbsp;";
        cultivo.setBarras_visualiza(sHTML);
    }

    public Cultivo completarDatosCultivo(Cultivo cultivo) throws AbadiaException {
        String sSQL = "Select * from campo_alimento ca where ca.RECURSOID_SIEMBRA=? and ca.NIVEL_ARBOL < 2";

        int porcentaje = 0;
        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setLong(1, cultivo.getIdRecurso());
            rs = ps.executeQuery();

            if (rs.next()) {
                //si no se ha alcanzado el total arado..
                if (cultivo.getArado_total() < Constantes.CULTIVO_TOTAL_ARADO) {
                    //calculamos el porcentaje de siembra proporcional.
                    porcentaje = (int) cultivo.getArado_total() * 100 / Constantes.CULTIVO_TOTAL_ARADO;
                    cultivo.setSiembra_total((int) (porcentaje * rs.getInt("TOTAL_SIEMBRA") / 100));
                } else {
                    cultivo.setSiembra_total(rs.getInt("TOTAL_SIEMBRA"));
                }
                cultivo.setFechaSiembra(CoreTiempo.getTiempoAbadiaString());
                GregorianCalendar cal = CoreTiempo.getTiempoAbadia();
                int dias = rs.getInt("DIAS_CULTIVO");
                cal.add(GregorianCalendar.DATE, dias);
                cultivo.setFechaInicioCultivo(CoreTiempo.getTiempoAbadiaString());
                cultivo.setFechaFinCultivo(CoreTiempo.getTiempoAbadiaString(cal));

                cultivo.setFechaInicioRecogida(CoreTiempo.getTiempoAbadiaString(cal));
                dias = rs.getInt("DIAS_RECOGIDA");
                cal.add(GregorianCalendar.DATE, dias);
                cultivo.setFechaFinRecogida(CoreTiempo.getTiempoAbadiaString(cal));
                cultivo.setIdAlimento(rs.getInt("ALIMENTOID"));
                cultivo.setNivelCampo(rs.getInt("NIVEL_ARBOL"));
                return cultivo;
            }
            return null;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adCampo. completarDatosCultivo. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public Cultivo completarDatosCultivoNivel(Cultivo cultivo) throws AbadiaException {
        String sSQL = "Select * from campo_alimento ca where ca.ALIMENTOID=? and ca.NIVEL_ARBOL=?";
        int porcentaje = 0;
        //Cultivo cultivo = null;
        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, cultivo.getIdAlimento());
            ps.setInt(2, cultivo.getNivelCampo());

            rs = ps.executeQuery();

            if (rs.next()) {
                //cultivo = new Cultivo();
                if (cultivo.getArado_total() < Constantes.CULTIVO_TOTAL_ARADO) {
                    //calculamos el porcentaje de siembra proporcional.
                    porcentaje = (int) cultivo.getArado_total() * 100 / Constantes.CULTIVO_TOTAL_ARADO;
                    cultivo.setSiembra_total((int) (porcentaje * rs.getInt("TOTAL_SIEMBRA") / 100));
                } else {
                    cultivo.setSiembra_total(rs.getInt("TOTAL_SIEMBRA"));
                }
                //cultivo.setSiembra_total(rs.getInt("TOTAL_SIEMBRA"));
                cultivo.setFechaSiembra(CoreTiempo.getTiempoAbadiaString());
                GregorianCalendar cal = CoreTiempo.getTiempoAbadia();
                int dias = rs.getInt("DIAS_CULTIVO");
                cal.add(GregorianCalendar.DATE, dias);
                cultivo.setFechaInicioCultivo(CoreTiempo.getTiempoAbadiaString());
                cultivo.setFechaFinCultivo(CoreTiempo.getTiempoAbadiaString(cal));
                cultivo.setFechaInicioRecogida(CoreTiempo.getTiempoAbadiaString(cal));
                dias = rs.getInt("DIAS_RECOGIDA");
                cal.add(GregorianCalendar.DATE, dias);
                cultivo.setFechaFinRecogida(CoreTiempo.getTiempoAbadiaString(cal));
                return cultivo;
            }
            return null;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adCampo. completarDatosCultivoNivel. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /*  Recupera el recursoID de un alimentoID
    */

    public Cultivo recuperarRecursoID(Cultivo cultivo) throws AbadiaException {
        String sSQL = "Select * from campo_alimento ca where ca.ALIMENTOID=?";

        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setLong(1, cultivo.getIdAlimento());
            rs = ps.executeQuery();

            if (rs.next()) {
                cultivo.setIdRecurso(rs.getInt("RECURSOID_SIEMBRA"));
                cultivo.setDias_cultiva(rs.getInt("DIAS_CULTIVO"));
                cultivo.setDias_recogida(rs.getInt("DIAS_RECOGIDA"));
                return cultivo;
            }
            return null;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adCampo. recuperarRecursoID. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }


    public String recuperarDescripcionCultivo(int idRecurso, Usuario usuario) throws AbadiaException {
        String sSQL = "Select l.LITERAL from recurso_tipo rt, literales l where rt.RECURSOID=? and l.literalid=rt.literalid and l.idiomaid=?";

        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idRecurso);
            ps.setInt(2, usuario.getIdDeIdioma());
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("LITERAL");
            }
            return null;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adCampo. recuperarDescripcionCultivo. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public int recuperaNumeroDeCampos(Abadia abadia) throws AbadiaException {
        String sSQL = "Select count(*) AS CONTADOR from campo where abadiaid=?";

        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setLong(1, abadia.getIdDeAbadia());
            rs = ps.executeQuery();

            if (rs.next()) {

                return rs.getInt("CONTADOR");
            }
            return 0;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adCampo. recuperaNumeroDeCampos. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    //permite volver a dejar el campo como arado si no se ha palantado nada y se está cultivando.

    public boolean cancelarCampo(int idCultivo, int idAbadia) throws AbadiaException {

        String sSQL = "SELECT c.*, ca.RECURSOID_SIEMBRA FROM campo c, campo_alimento ca WHERE c.CAMPOID = ? AND c.ABADIAID = ? AND c.SIEMBRA_TOTAL=0 AND c.ESTADO > 3 AND ca.ALIMENTOID = c.ALIMENTOID";

        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        adRecurso oRecursoAD;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno los valores
            int parNo = 1;
            ps.setLong(parNo++, idCultivo);
            ps.setLong(parNo, idAbadia);
            // Ejecutarlo
            rs = ps.executeQuery();
            if (rs.next()) {
                limpiarCampo(idCultivo, idAbadia, Constantes.ESTADO_CULTIVO_ARADO);
                oRecursoAD = new adRecurso(con);
                oRecursoAD.sumarRecurso(rs.getInt("RECURSOID_SIEMBRA"), idAbadia, 1);
                return true;
            }
            return false;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adCampo. cancelarCampo. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /**
     * Inicializa los parametros basicos de un cultivo
     *
     * @param idCultivo
     * @param idAbadia
     * @throws AbadiaException
     */
    public void limpiarCampo(int idCultivo, int idAbadia, int estado) throws AbadiaException {
        String sSQL = "update campo " +
                " set estado=?, alimentoid=0, FECHA_SIEMBRA=null, FECHA_CULTIVO_DESDE=null, FECHA_CULTIVO_HASTA=null, FECHA_RECOGIDA_DESDE=null, FECHA_RECOGIDA_HASTA=null, SIEMBRA_TOTAL=0, CULTIVA_TOTAL=0, RECOGIDA_TOTAL=0, ARADO_TOTAL=0, NIVEL_CAMPO = 0 " +
                " where CAMPOID = ? and ABADIAID = ?";

        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno los valores
            int parNo = 1;
            ps.setInt(parNo++, estado);
            ps.setInt(parNo++, idCultivo);
            ps.setInt(parNo, idAbadia);
            // Ejecutarlo
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new AbadiaSQLException("adCampo. limpiarCampo. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }


    /**
     * Crea un registro en la tabla campo_monje
     *
     * @param idCampo
     * @param idAbadia
     * @param idMonje
     * @throws AbadiaException
     */
    public void crearRegistroCampoMonje(int idCampo, int idAbadia, int idMonje) throws AbadiaException {
        String sSQLDelete = "DELETE FROM campo_monje where MONJEID = " + idMonje;
        String sSQL = "INSERT INTO campo_monje (ABADIAID, CAMPOID, MONJEID) VALUES (?,?,?)";


        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQLDelete);
            ps.execute();

            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idAbadia);
            ps.setInt(2, idCampo);
            ps.setInt(3, idMonje);

            ps.execute();

        } catch (SQLException e) {
            throw new AbadiaSQLException("adCampo. crearRegistroCampoMonje. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * elimina un registro en la tabla campo_monje
     *
     * @param idMonje
     * @throws AbadiaException
     */
    public void eliminarRegistroCampoMonje(int idMonje) throws AbadiaException {
        String sSQLDelete = "DELETE FROM campo_monje where MONJEID = " + idMonje;
        adUtils utils = null;

        try {
            utils = new adUtils(con);
            utils.execSQL(sSQLDelete);
        } catch (AbadiaException e) {
            log.error("adCampo.eliminarRegistroCampoMonje.", e);
            throw e;
        } finally {
            //
        }
    }

    /**
     * Recupera la lista de identificadores de monje que estan asignados al campo
     *
     * @param idCampo
     * @param idAbadia
     * @throws AbadiaException
     */
    public int[] recuperarIdMonjesPorCampo(int idCampo, int idAbadia) throws AbadiaException {
        String sSQL = "SELECT MONJEID FROM campo_monje WHERE CAMPOID = ? AND ABADIAID = ?";

        int[] valores = new int[100];
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idCampo);
            ps.setInt(2, idAbadia);

            rs = ps.executeQuery();
            int iCount;
            for (iCount = 0; rs.next(); iCount++) {
                valores[iCount] = rs.getInt(1);
            }

            int[] valoresR = new int[iCount];
            System.arraycopy(valores, 0, valoresR, 0, iCount);
            return valoresR;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adCampo. recuperarIdMonjesPorCampo. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Recupera un identificador indicando si el monje esta asignado en algun campo...
     *
     * @param idMonje
     * @throws AbadiaException
     */
    public boolean estaTrabajando(int idMonje) throws AbadiaException {
        String sSQL = "SELECT MONJEID FROM campo_monje WHERE MONJEID = ?";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idMonje);
            rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            throw new AbadiaSQLException("adCampo. estaTrabajando. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Recupera el numero de monjes asignado a un compo concreto
     *
     * @param idCampo
     * @throws AbadiaException
     */
    public int getNumMonjesPorCampo(int idCampo) throws AbadiaException {
        String sSQL = "SELECT Count(*) as numMonjes FROM campo_monje WHERE CAMPOID = ?";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idCampo);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("numMonjes");
            } else return 0;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adCampo. getNumMonjesPorCampo. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Proceso arar de los trabajos del monje
     *
     * @param idPeriodo
     * @param hmMonjes  Hashmap con todos los monjes agrupados por identificador de campo
     * @throws AbadiaException
     */
    public void arar(int idPeriodo, HashMap hmMonjes) throws AbadiaException {

        HashMap<Integer, HashMap<Object, ArrayList<Animal>>> hmAnimalesAbadia = null;
        HashMap<Object, ArrayList<Animal>> hmAnimales = null;
        ArrayList<Animal> alAnimales = null;

        //recuperar hashmap recursos (azadas y arados) por abbatia
        HashMap<Integer, HashMap<Object, Integer>> hmRecursosAbadia = null;
        HashMap<Object, Integer> hmRecursos = null;

        ArrayList<Cultivo> alCampos = null;
        Iterator<Cultivo> itCampos = null;

        ArrayList alMonjes = null;
        Iterator itMonjes = null;
        MonjeAgricultura monje = null;
        Animal animal = null;

        adProcesos procesosAD = null;
        adActividad actividadAD = null;
        adUtils utils = null;
        adCampo campoAD = null;
        adAnimal oAnimalAD;
        adMensajes oMensajesAD;

        String msg = "";
        String msg_monje = "";

        ArrayList alMensajes = new ArrayList();

        int idLastAbadia = 0;
        int idLastIdioma = 0;
        double dPuntosArar = 0;
        double dPuntosArarMonje = 0;
        int iNumArados = 0;
        int iNumAzadas = 0;
        Cultivo cultivo = null;
        boolean finArado = false;
        try {
            campoAD = new adCampo(con);
            //recuperar ArrayList con los datos de todos los campos es estado arando
            //que tengan asignados monjes.
            alCampos = campoAD.recuperarCamposPorAbadiaEstadoArar(Constantes.ESTADO_CULTIVO_ARANDO);
            //recuperar hashmap con los animales disponibles por abbatia
            hmAnimalesAbadia = campoAD.cargarTablaAnimalesArar();
            //recuperar hashmap con los recursos disponibles por abbatia
            hmRecursosAbadia = campoAD.cargarTablaRecursosArar();

            if (alCampos != null) {
                itCampos = alCampos.iterator();
                while (itCampos.hasNext()) {
                    //inicializo los punto de arado por cada campo.
                    dPuntosArar = 0;
                    finArado = false;
                    cultivo = itCampos.next();

                    if (idLastIdioma != cultivo.getIdIdioma()) {
                        idLastIdioma = cultivo.getIdIdioma();
                        utils = new adUtils(con);
                        msg = utils.getLiteral(10617, idLastIdioma); // Ha trabajado arando un campo
                    }

                    alMonjes = (ArrayList) hmMonjes.get(new Integer(cultivo.getIdCultivo()));
                    if (alMonjes != null) {
                        itMonjes = alMonjes.iterator();
                        while (itMonjes.hasNext() && !finArado) {
                            monje = (MonjeAgricultura) itMonjes.next();
                            //marcamos el monje como que ha trabajado.
                            if (!monje.isHaTrabajado()) {
                                monje.setHaTrabajado(true);
                                if (idLastAbadia != cultivo.getIdAbadia()) {
                                    idLastAbadia = cultivo.getIdAbadia();
                                    //recuperamos animales y recursos de la abbatia
                                    //cada elemento de la hash de animales contiene un arraylist por tipo de animal
                                    hmAnimales = hmAnimalesAbadia.get(new Integer(cultivo.getIdAbadia()));
                                    //cada elemento de la hash de recursos contiene el numero de items por tipo de recurso
                                    hmRecursos = hmRecursosAbadia.get(new Integer(cultivo.getIdAbadia()));
                                }

                                //determinar puntos por habilidades;

                                dPuntosArarMonje = (monje.getFuerza() * monje.getFe()) / 200;
                                if (hmRecursos != null) {
                                    iNumArados = hmRecursos.get(Constantes.RECURSOS_ARADO);
                                    iNumAzadas = hmRecursos.get(Constantes.RECURSOS_AZADA);
                                } else {
                                    iNumArados = 0;
                                    iNumAzadas = 0;
                                }
                                //si disponemos de arado...
                                if (iNumArados > 0) {
                                    //verificamos si hay buey o caballos
                                    if (hmAnimales != null) {
                                        alAnimales = hmAnimales.get(new Integer(Constantes.ANIMALES_CABALLO));
                                        if (alAnimales.isEmpty()) {
                                            alAnimales = hmAnimales.get(new Integer(Constantes.ANIMALES_YEGUA));
                                            if (alAnimales.isEmpty()) {
                                                alAnimales = hmAnimales.get(new Integer(Constantes.ANIMALES_BUEY));
                                            }//if (alAnimales.isEmpty())
                                        }//if (alAnimales.isEmpty())
                                        //si disponemos de algún animal para trabajar con el arado...
                                        if (!alAnimales.isEmpty()) {
                                            //recupero el primer animal de la lista;
                                            animal = alAnimales.get(0);
                                            //restamo 1 punto a la salud del animal en concepto de agotamiento
                                            oAnimalAD = new adAnimal(con);
                                            oAnimalAD.restarSalud(animal.getAnimalid(), (short) 1);
                                            //gestion de punto en función del animal
                                            if (animal.getTipoAnimalid() == Constantes.ANIMALES_CABALLO || animal.getTipoAnimalid() == Constantes.ANIMALES_YEGUA) {
                                                //multiplico por dos la puntuación inicial
                                                dPuntosArarMonje = dPuntosArarMonje * Constantes.CULTIVO_PUNTOS_ARADO_CABALLO;
                                            } else //si se trata de un buey...
                                            {
                                                //multiplico por 1,85 la puntuación inicial
                                                dPuntosArarMonje = dPuntosArarMonje * Constantes.CULTIVO_PUNTOS_ARADO_BUEY;
                                            }
                                            //resto el numero de arados.
                                            hmRecursos.put(Constantes.RECURSOS_ARADO, iNumArados - 1);
                                            //elimino el animal una vez utilizado por un monje / franja.
                                            alAnimales.remove(0);
                                        }//if (!alAnimales.isEmpty())
                                    }
                                } else //no se dispone de arados
                                {
                                    //si disponemos de azadas...
                                    if (iNumAzadas > 0) {
                                        //multiplico por 1,5 la puntuación inicial
                                        dPuntosArarMonje = dPuntosArarMonje * Constantes.CULTIVO_PUNTOS_ARADO_AZADA;
                                        //restmos el número de azadas disponibles
                                        hmRecursos.put(Constantes.RECURSOS_AZADA, iNumAzadas - 1);
                                    }
                                }//if (iNumArados > 0)

                                msg_monje = ProcesosUtils.Format(msg, dPuntosArarMonje);
                                procesosAD = new adProcesos(con);
                                procesosAD.setActividadMens(monje.getIdMonje(), Constantes.TAREA_AGRICULTURA, idPeriodo, msg_monje, dPuntosArarMonje);

                                actividadAD = new adActividad(con);
                                actividadAD.incrementaAcumulado(monje.getIdMonje(), idPeriodo, Constantes.TAREA_AGRICULTURA, dPuntosArarMonje);

                                //tras procesar cada monje, evaluamos si se ha finalizado el arado del campo...
                                if ((cultivo.getArado_total() + dPuntosArarMonje) >= Constantes.CULTIVO_TOTAL_ARADO) {
                                    //actualizar el estado del cultivo...
                                    campoAD = new adCampo(con);
                                    campoAD.actualizarEstadoCultivo(cultivo.getIdCultivo(), Constantes.ESTADO_CULTIVO_ARADO);
                                    //forzamos la salida del bucle de monjes.
                                    finArado = true;
                                }
                                //sumo lo acumulado del monje a lo acumulado al campo
                                dPuntosArar = dPuntosArar + dPuntosArarMonje;
                            }//if (!monje.isHaTrabajado())
                        }//while (itMonjes.hasNext())
                    }//if (alMonjes != null)
                    //si hay puntos a incrementar...
                    if (dPuntosArar > 0) {
                        //sumamos los puntos totales de arada al acumulado
                        cultivo.setArado_total(cultivo.getArado_total() + dPuntosArar);
                        campoAD = new adCampo(con);
                        campoAD.actualizarPuntosArado(cultivo);
                    }
                }//while (itCampos.hasNext())
            }//if (alCampos != null)
            //creamos los mensajes
            oMensajesAD = new adMensajes(con);
            oMensajesAD.crearMensajes(alMensajes);

        } catch (Exception e) {
            throw new AbadiaProcesoException("adCampo. arar", e, log);
        } finally {
            //
        }
    }

    /**
     * Proceso cultivar de los trabajos del monje
     *
     * @param idPeriodo
     * @param hmMonjes  Hashmap con todos los monjes agrupados por identificador de campo
     * @throws AbadiaException
     */
    public void cultivar(int idPeriodo, HashMap hmMonjes) throws AbadiaException {
        ArrayList<Cultivo> alCampos = null;
        Iterator<Cultivo> itCampos = null;

        ArrayList alMonjes = null;
        Iterator itMonjes = null;
        MonjeAgricultura monje = null;

        adProcesos procesosAD = null;
        adActividad actividadAD = null;
        adUtils utils = null;
        adCampo campoAD = null;
        adMensajes oMensajesAD;

        String msg1 = "";

        ArrayList alMensajes = new ArrayList();
        int idLastIdioma = 0;
        double dPuntosCultivo = 0;
        double dPuntosCultivoMonje = 0;
        Cultivo cultivo = null;
        try {
            campoAD = new adCampo(con);
            //recuperar ArrayList con los datos de todos los campos en estado cultivando
            //que tengan asignados monjes.
            alCampos = campoAD.recuperarCamposPorAbadiaEstado(Constantes.ESTADO_CULTIVO_CULTIVANDO);
            if (!alCampos.isEmpty()) {
                itCampos = alCampos.iterator();
                while (itCampos.hasNext()) {
                    //inicializo los punto de cultivo por cada campo.
                    dPuntosCultivo = 0;
                    cultivo = itCampos.next();
                    if (idLastIdioma != cultivo.getIdIdioma()) {
                        idLastIdioma = cultivo.getIdIdioma();
                        utils = new adUtils(con);
                        msg1 = utils.getLiteral(10016, idLastIdioma); // Has cultivado.
                    }

                    //para cada campo tengo que validar si se ha alcanzado la fecha fin de cultivo
                    //de ser asi, actulizaremos el estado y excluiremos el cultivo del proceso.
                    alMonjes = (ArrayList) hmMonjes.get(new Integer(cultivo.getIdCultivo()));
                    if (alMonjes != null) {
                        itMonjes = alMonjes.iterator();
                        while (itMonjes.hasNext()) {
                            monje = (MonjeAgricultura) itMonjes.next();
                            if (!monje.isHaTrabajado()) {
                                //marcamos el monje como que ha trabajado.
                                monje.setHaTrabajado(true);

                                //determinar puntos por habilidades;
                                //Calculamos los puntos  que aporta cada monje al cultivo...
                                dPuntosCultivoMonje = (monje.getDestreza() * monje.getFe()) / 100;

                                procesosAD = new adProcesos(con);
                                procesosAD.setActividadMens(monje.getIdMonje(), Constantes.TAREA_AGRICULTURA, idPeriodo, msg1, dPuntosCultivoMonje);

                                actividadAD = new adActividad(con);
                                actividadAD.incrementaAcumulado(monje.getIdMonje(), idPeriodo, Constantes.TAREA_AGRICULTURA, dPuntosCultivoMonje);

                                //sumo lo acumulado del monje a lo acumulado al campo
                                dPuntosCultivo = dPuntosCultivo + dPuntosCultivoMonje;
                            }//if (!monje.isHaTrabajado())
                        }//while (itMonjes.hasNext())
                    }//if (alMonjes != null)
                    //incrementar el acumulado diario
                    incrementarAcumuladoCultivo(cultivo.getIdCultivo(), dPuntosCultivo);
                }//while (itCampos.hasNext())
            }//if (!alCampos.isEmpty())
            //creamos los mensajes
            oMensajesAD = new adMensajes(con);
            oMensajesAD.crearMensajes(alMensajes);

        } catch (Exception e) {
            throw new AbadiaProcesoException("adCampo. cultivar", e, log);
        } finally {
            //
        }
    }

    /**
     * Proceso recoger de los trabajos del monje
     *
     * @param idPeriodo
     * @param hmMonjes  Hashmap con todos los monjes agrupados por identificador de campo
     * @throws AbadiaException
     */
    public void recoger(int idPeriodo, HashMap hmMonjes) throws AbadiaException {
        ArrayList<Cultivo> alCampos = null;
        Iterator<Cultivo> itCampos = null;

        ArrayList alMonjes = null;
        Iterator itMonjes = null;
        MonjeAgricultura monje = null;
        AlimentoLote alimentoLote = null;

        adProcesos procesosAD = null;
        adActividad actividadAD = null;
        adUtils utils = null;
        adCampo campoAD = null;
        adAlimentoLotes alimentoAD = null;

        String msg = "";
        String msg_monje = "";

        int idLastIdioma = 0;
        double dPuntosRecogida = 0;
        double dPuntosRecogidaMonje = 0;
        int idLote = 0;
        int idEdificio = 0;
        Cultivo cultivo = null;
        try {
            campoAD = new adCampo(con);
            //recuperar ArrayList con los datos de todos los campos en estado cultivando
            //que tengan asignados monjes.
            alCampos = campoAD.recuperarCamposPorAbadiaEstado(Constantes.ESTADO_CULTIVO_RECOGIENDO);
            if (alCampos != null) {
                itCampos = alCampos.iterator();
                while (itCampos.hasNext()) {
                    //inicializo los punto de Recogida por cada campo.
                    dPuntosRecogida = 0;
                    cultivo = itCampos.next();
                    if (idLastIdioma != cultivo.getIdIdioma()) {
                        idLastIdioma = cultivo.getIdIdioma();
                        utils = new adUtils(con);
                        msg = utils.getLiteral(10011, idLastIdioma); // Ha recogido %d %s de %s
                    }

                    //para cada campo tengo que validar si se ha alcanzado la fecha fin de cultivo
                    //de ser asi, actulizaremos el estado y excluiremos el cultivo del proceso.
                    alMonjes = (ArrayList) hmMonjes.get(new Integer(cultivo.getIdCultivo()));
                    if (alMonjes != null) {
                        itMonjes = alMonjes.iterator();
                        while (itMonjes.hasNext()) {
                            monje = (MonjeAgricultura) itMonjes.next();
                            if (!monje.isHaTrabajado()) {
                                //marcamos el monje como que ha trabajado.
                                monje.setHaTrabajado(true);

                                //determinar puntos por habilidades;
                                //Calculamos los puntos que tiene el monje para recoger...
                                //(((fuerza *50 /100) * (destreza * 50 /100) * fe) / 100) / 11

                                dPuntosRecogidaMonje = (((((monje.getFuerza() * 50) / 100) * ((monje.getDestreza() * 50) / 100) * monje.getFe())) / 100) / 11;

                                if (cultivo.getCultiva_total() > cultivo.getRecogida_total() + dPuntosRecogidaMonje + dPuntosRecogida) {
                                    msg_monje = ProcesosUtils.Format(msg, (int) Math.round(dPuntosRecogidaMonje));

                                    procesosAD = new adProcesos(con);
                                    procesosAD.setActividadMens(monje.getIdMonje(), Constantes.TAREA_AGRICULTURA, idPeriodo, msg_monje, dPuntosRecogidaMonje);

                                    actividadAD = new adActividad(con);
                                    actividadAD.incrementaAcumulado(monje.getIdMonje(), idPeriodo, Constantes.TAREA_AGRICULTURA, dPuntosRecogidaMonje);
                                }

                                //sumo lo acumulado del monje a lo acumulado al campo
                                dPuntosRecogida = dPuntosRecogida + dPuntosRecogidaMonje;
                            }
                        }//while (itMonjes.hasNext())
                    }//if (alMonjes != null)

                    //Si se ha recogido algo....
                    if (dPuntosRecogida > 0) {
                        if (cultivo.getCultiva_total() < (cultivo.getRecogida_total() + dPuntosRecogida)) {
                            dPuntosRecogida = cultivo.getCultiva_total() - cultivo.getRecogida_total();
                        }
                        procesosAD = new adProcesos(con);
                        idEdificio = procesosAD.recuperarIdEdificioPorFamiliaAlimento(cultivo.getIdAlimento(), cultivo.getIdAbadia());

                        utils = new adUtils(con);
                        idLote = utils.getSQL("SELECT a.loteid FROM alimentos a, alimentos_lote al where a.loteid=al.loteid and a.edificioid=" + idEdificio + " and alimentoid = " + cultivo.getIdAlimento() + " and fecha_entrada ='" + CoreTiempo.getTiempoAbadiaString() + "'  LIMIT 1", -1);

                        alimentoAD = new adAlimentoLotes(con);
                        if (idLote == -1) {
                            alimentoLote = new AlimentoLote();
                            alimentoLote.setCantidad(dPuntosRecogida);
                            alimentoLote.setIdAlimento(cultivo.getIdAlimento());
                            alimentoLote.setIdEdificio(idEdificio);
                            alimentoLote.setFechaEntrada(CoreTiempo.getTiempoAbadiaString());
                            alimentoLote.setEstado(0);
                            alimentoLote.setFechaCaducidad(CoreTiempo.getDiferenciaString(alimentoAD.recuperarCaducidad(alimentoLote.getIdAlimento())));
                            alimentoAD.crearAlimentoLote(alimentoLote);
                        } else {
                            alimentoAD.sumarEnLote(idLote, dPuntosRecogida);
                        }

                        //actualizo el total recogido.
                        cultivo.setRecogida_total(cultivo.getRecogida_total() + dPuntosRecogida);
                        campoAD = new adCampo(con);
                        campoAD.actualizarPuntosRecogida(cultivo);
                    }
                }//while (itCampos.hasNext())
            }//if (alCampos != null)
            //creamos los mensajes
            //adMensajes.crearMensajes(alMensajes);

        } catch (Exception e) {
            throw new AbadiaProcesoException("adCampo. recoger", e, log);
        } finally {

        }
    }

    /**
     * Actualiza el estado de un cultivo
     *
     * @param idCultivo
     * @param idEstado
     * @throws AbadiaException
     */
    public void actualizarEstadoCultivo(int idCultivo, short idEstado) throws AbadiaException {
        String sSQL = "UPDATE campo SET ESTADO = ? WHERE CAMPOID = ?";

        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setDouble(1, idEstado);
            ps.setInt(2, idCultivo);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adCampo. actualizarEstadoCultivo", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /**
     * Actualiza el nivel de un cultivo
     *
     * @param idCultivo
     * @param nivel
     * @throws AbadiaException
     */
    public void actualizarNivelCultivo(int idCultivo, int nivel) throws AbadiaException {
        String sSQL = "UPDATE campo SET NIVEL_CAMPO = ? WHERE CAMPOID = ?";

        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setDouble(1, nivel);
            ps.setInt(2, idCultivo);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adCampo. actualizarNivelCultivo", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }


    /**
     * Actualiza los punto de arado de un campo
     *
     * @param cultivo
     * @throws AbadiaException
     */
    public void actualizarPuntosArado(Cultivo cultivo) throws AbadiaException {
        String sSQL = "UPDATE campo SET ARADO_TOTAL = ? WHERE CAMPOID = ?";

        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setDouble(1, cultivo.getArado_total());
            ps.setInt(2, cultivo.getIdCultivo());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adCampo. actualizarPuntosArado", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /**
     * Actualiza los puntos de cultivo de un campo
     *
     * @param cultivo
     * @throws AbadiaException
     */
    public void actualizarPuntosCultivo(Cultivo cultivo) throws AbadiaException {
        String sSQL = "UPDATE campo SET CULTIVA_TOTAL = ? WHERE CAMPOID = ?";

        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setDouble(1, cultivo.getCultiva_total());
            ps.setInt(2, cultivo.getIdCultivo());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adCampo. actualizarPuntosCultivo", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /**
     * Actualiza los puntos de cultivo de un campo
     *
     * @param idCampo
     * @param iValor
     * @throws AbadiaException
     */
    public void restarPuntosCultivoStatic(int idCampo, int iValor) throws AbadiaException {
        String sSQL = "UPDATE campo SET CULTIVA_TOTAL = CULTIVA_TOTAL - " + iValor + " WHERE CAMPOID = " + idCampo;
        adUtils utils = null;
        try {
            utils = new adUtils(con);
            utils.execSQL(sSQL);
        } catch (AbadiaException e) {
            throw new AbadiaSQLException("adCampo. actualizarPuntosCultivoStatic", e, log);
        } finally {

        }

    }

    /**
     * Regulariza a 0 los puntos de cultivo en los casos en los que sea menor que 0
     *
     * @throws AbadiaException
     */
    public void resetPuntosCultivoCero() throws AbadiaException {
        String sSQL = "UPDATE campo SET CULTIVA_TOTAL = 0 WHERE CULTIVA_TOTAL < 0";
        adUtils utils;
        try {
            utils = new adUtils(con);
            utils.execSQL(sSQL);
        } catch (AbadiaException e) {
            throw new AbadiaSQLException("adCampo. resetPuntosCultivoCero", e, log);
        } finally {

        }

    }


    /**
     * Actualiza los puntos de recogida de un campo
     *
     * @param cultivo
     * @throws AbadiaException
     */
    public void actualizarPuntosRecogida(Cultivo cultivo) throws AbadiaException {
        String sSQL = "UPDATE campo SET RECOGIDA_TOTAL = ? WHERE CAMPOID = ?";

        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setDouble(1, cultivo.getRecogida_total());
            ps.setInt(2, cultivo.getIdCultivo());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adCampo. actualizarPuntosRecogida", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /**
     * Recupera un arraylist con los campos de cultivo que están en un estado determinado
     *
     * @param idEstado
     * @return
     * @throws AbadiaException
     */
    public ArrayList<Cultivo> recuperarCamposPorAbadiaEstadoArar(int idEstado) throws AbadiaException {
        String sSQL = "Select c.CAMPOID, c.ABADIAID, c.ARADO_TOTAL, c.ESTADO, c.SIEMBRA_TOTAL, c.RECOGIDA_TOTAL, c.CULTIVA_TOTAL, u.IDIOMAID " +
                " from campo c, usuario u, abadia a where c.ESTADO = ? and u.USUARIOID = a.USUARIOID and a.ABADIAID = c.ABADIAID ";

        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idEstado);
            rs = ps.executeQuery();
            Cultivo cultivo;
            ArrayList<Cultivo> listaCampos = new ArrayList<Cultivo>();
            while (rs.next()) {
                cultivo = new Cultivo();
                cultivo.setIdEstado(rs.getInt("ESTADO"));
                cultivo.setArado_total(rs.getFloat("ARADO_TOTAL"));
                cultivo.setSiembra_total(rs.getFloat("SIEMBRA_TOTAL"));
                cultivo.setCultiva_total(rs.getFloat("CULTIVA_TOTAL"));
                cultivo.setRecogida_total(rs.getFloat("RECOGIDA_TOTAL"));
                cultivo.setIdCultivo(rs.getInt("CAMPOID"));
                cultivo.setIdAbadia(rs.getInt("ABADIAID"));
                cultivo.setIdIdioma(rs.getInt("IDIOMAID"));

                listaCampos.add(cultivo);
            }
            return listaCampos;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adCampo. recuperarCamposPorAbadiaEstadoArar. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Recupera un arraylist con los campos de cultivo que están en un estado determinado
     *
     * @param idEstado
     * @return
     * @throws AbadiaException
     */
    public ArrayList<Cultivo> recuperarCamposPorAbadiaEstado(int idEstado) throws AbadiaException {
        String sSQL = "Select c.CAMPOID, c.ABADIAID, c.ARADO_TOTAL, c.ESTADO, c.SIEMBRA_TOTAL, c.RECOGIDA_TOTAL, c.CULTIVA_TOTAL, ca.PUNTOS_CULTIVO_DIA, ca.AGUA_DIA, ca.TEMP_MIN, ca.TEMP_MAX, u.IDIOMAID, c.ALIMENTOID, c.CULTIVA_DIA, a.REGIONID, l.LITERAL " +
                " from campo c, campo_alimento ca, usuario u, abadia a, literales l, alimentos_tipo at " +
                " where c.ESTADO = ? and c.ALIMENTOID = ca.ALIMENTOID and u.USUARIOID = a.USUARIOID and a.ABADIAID = c.ABADIAID and c.NIVEL_CAMPO = ca.NIVEL_ARBOL and at.ALIMENTOID = c.ALIMENTOID and l.LITERALID = at.LITERALID and l.IDIOMAID = u.IDIOMAID";

        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idEstado);
            rs = ps.executeQuery();
            Cultivo cultivo;
            ArrayList<Cultivo> listaCampos = new ArrayList<Cultivo>();
            while (rs.next()) {
                cultivo = new Cultivo();
                cultivo.setIdEstado(rs.getInt("ESTADO"));
                cultivo.setArado_total(rs.getFloat("ARADO_TOTAL"));
                cultivo.setSiembra_total(rs.getFloat("SIEMBRA_TOTAL"));
                cultivo.setCultiva_por_dia(rs.getFloat("PUNTOS_CULTIVO_DIA"));
                cultivo.setCultiva_acumulado_dia(rs.getDouble("CULTIVA_DIA"));
                cultivo.setCultiva_total(rs.getFloat("CULTIVA_TOTAL"));
                cultivo.setRecogida_total(rs.getFloat("RECOGIDA_TOTAL"));
                cultivo.setIdCultivo(rs.getInt("CAMPOID"));
                cultivo.setIdAbadia(rs.getInt("ABADIAID"));
                cultivo.setIdIdioma(rs.getInt("IDIOMAID"));
                cultivo.setIdAlimento(rs.getInt("ALIMENTOID"));
                cultivo.setDescAlimento(rs.getString("LITERAL"));
                cultivo.setAguaPorDia(rs.getInt("AGUA_DIA"));
                cultivo.setTempMinima(rs.getInt("TEMP_MIN"));
                cultivo.setTempMaxima(rs.getInt("TEMP_MAX"));
                cultivo.setIdRegion(rs.getInt("REGIONID"));

                listaCampos.add(cultivo);
            }
            return listaCampos;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adCampo. recuperarCamposPorAbadiaEstado. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Gestión de transición entre estados de campos de cultivo.
     *
     * @throws AbadiaException
     */
    public void transitarEstadosCultivo() throws AbadiaException {
        String sSQLCultivando = "update campo set estado =  ? where fecha_cultivo_hasta <= ? and estado = ?";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQLCultivando);
            ps.setShort(1, Constantes.ESTADO_CULTIVO_RECOGIENDO);
            ps.setString(2, CoreTiempo.getTiempoAbadiaString());
            ps.setShort(3, Constantes.ESTADO_CULTIVO_CULTIVANDO);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adCampo. transitarEstadosCultivo. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /**
     * Gestión de transición entre estados de campos de cultivo.
     *
     * @throws AbadiaException
     */
    public void transitarEstadosRecogida() throws AbadiaException {
        adCampo campoAD;
        ArrayList<Cultivo> alCampos;
        Iterator<Cultivo> itCampos;
        Cultivo cultivo;
        Cultivo cultivoSiguienteNivel;

        try {
            campoAD = new adCampo(con);
            alCampos = campoAD.recuperarCamposFinalizados();

            if (alCampos != null) {
                itCampos = alCampos.iterator();
                while (itCampos.hasNext()) {
                    cultivo = itCampos.next();
                    //si el nivel del campo es superior a 0
                    if (cultivo.getNivelCampo() > 0) {
                        //significa que se trata de un arbol e intentaremos encontrar
                        //si existe un nivel superior de campo
                        //recuperar campo de cultivo de nivel n+1
                        campoAD = new adCampo(con);
                        cultivoSiguienteNivel = campoAD.recuperarDatosCultivoNivel(cultivo.getIdAlimento(), cultivo.getNivelCampo() + 1);
                        if (cultivoSiguienteNivel != null) {
                            cultivo.setNivelCampo(cultivo.getNivelCampo() + 1);
                        }
                        campoAD.sembrarCampoNivel(cultivo);

                    } else {
                        //si es de nivel 0 se trata de un cultivo normal
                        //una planta por lo tanto volveremos a dejar el cultivo
                        //en estado inactivo.
                        campoAD = new adCampo(con);
                        campoAD.limpiarCampo(cultivo.getIdCultivo(), cultivo.getIdAbadia(), Constantes.ESTADO_CULTIVO_INACTIVO);
                    }
                }
            }

        } catch (AbadiaException e) {
            throw new AbadiaSQLException("adCampo. transitarEstadosRecogida", e, log);
        } finally {

        }

    }

    /**
     * Recupera un arraylist de objetos cultivo con los campos que han alcanzado el final de la recogida
     * y deben transitar a ararando o a cultivando.
     *
     * @return
     * @throws AbadiaException
     */
    public ArrayList<Cultivo> recuperarCamposFinalizados() throws AbadiaException {
        String sSQL = "Select c.CAMPOID, c.ABADIAID, c.ESTADO, c.NIVEL_CAMPO, c.ALIMENTOID, c.ARADO_TOTAL " +
                " from campo c where c.ESTADO = ? and c.FECHA_RECOGIDA_HASTA <= ?";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, Constantes.ESTADO_CULTIVO_RECOGIENDO);
            ps.setString(2, CoreTiempo.getTiempoAbadiaString());
            rs = ps.executeQuery();
            Cultivo cultivo;
            ArrayList<Cultivo> listaCampos = new ArrayList<Cultivo>();
            while (rs.next()) {
                cultivo = new Cultivo();
                cultivo.setIdEstado(rs.getInt("ESTADO"));
                cultivo.setIdCultivo(rs.getInt("CAMPOID"));
                cultivo.setIdAbadia(rs.getInt("ABADIAID"));
                cultivo.setNivelCampo(rs.getInt("NIVEL_CAMPO"));
                cultivo.setIdAlimento(rs.getInt("ALIMENTOID"));
                cultivo.setArado_total(rs.getInt("ARADO_TOTAL"));

                listaCampos.add(cultivo);
            }
            return listaCampos;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adCampo. recuperarCamposFinalizados. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /**
     * Recuperar una hashmap con los animales que se utilizan al arar.
     *
     * @return
     * @throws AbadiaException
     */
    public HashMap<Integer, HashMap<Object, ArrayList<Animal>>> cargarTablaAnimalesArar() throws AbadiaException {
        String sSQL = "SELECT cm.ABADIAID FROM campo_monje cm, campo c where cm.CAMPOID = c.CAMPOID AND c.ESTADO = " + Constantes.ESTADO_CULTIVO_ARANDO + " GROUP BY ABADIAID";
        HashMap<Object, ArrayList<Animal>> hmAnimales;
        HashMap<Integer, HashMap<Object, ArrayList<Animal>>> hmAnimalesAbadia = new HashMap<Integer, HashMap<Object, ArrayList<Animal>>>();

        adAnimal animalAD = null;
        int idAbadia;

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                idAbadia = rs.getInt("ABADIAID");
                hmAnimales = new HashMap<Object, ArrayList<Animal>>();
                animalAD = new adAnimal(con);
                hmAnimales.put(Constantes.ANIMALES_CABALLO, animalAD.recuperarAnimalesPorAbadiaTipo(idAbadia, Constantes.ANIMALES_CABALLO));
                hmAnimales.put(Constantes.ANIMALES_YEGUA, animalAD.recuperarAnimalesPorAbadiaTipo(idAbadia, Constantes.ANIMALES_YEGUA));
                hmAnimales.put(Constantes.ANIMALES_BUEY, animalAD.recuperarAnimalesPorAbadiaTipo(idAbadia, Constantes.ANIMALES_BUEY));
                hmAnimalesAbadia.put(idAbadia, hmAnimales);
            }
            return hmAnimalesAbadia;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adCampo. cargarTablaAnimalesArar. SQLException.", e, log);
        } catch (Exception e) {
            throw new AbadiaSQLException("adCampo. cargarTablaAnimalesArar. Exception.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Recuperar una hashmap con los recursos que se utilizan al arar.
     *
     * @return
     * @throws AbadiaException
     */
    public HashMap<Integer, HashMap<Object, Integer>> cargarTablaRecursosArar() throws AbadiaException {
        String sSQL = "SELECT cm.ABADIAID FROM campo_monje cm, campo c where cm.CAMPOID = c.CAMPOID AND c.ESTADO = " + Constantes.ESTADO_CULTIVO_ARANDO + " GROUP BY ABADIAID";
        HashMap<Object, Integer> hmRecursos = null;
        HashMap<Integer, HashMap<Object, Integer>> hmRecursosAbadia = new HashMap<Integer, HashMap<Object, Integer>>();

        int idAbadia;

        adRecurso oRecursoAD;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            oRecursoAD = new adRecurso(con);
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                idAbadia = rs.getInt("ABADIAID");
                hmRecursos = new HashMap<Object, Integer>();
                hmRecursos.put(Constantes.RECURSOS_AZADA, (int) oRecursoAD.recuperarValorRecurso(Constantes.RECURSOS_AZADA.intValue(), idAbadia));
                hmRecursos.put(Constantes.RECURSOS_ARADO, (int) oRecursoAD.recuperarValorRecurso(Constantes.RECURSOS_ARADO.intValue(), idAbadia));
                hmRecursosAbadia.put(idAbadia, hmRecursos);
            }
            return hmRecursosAbadia;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adCampo. cargarTablaRecursosArar. SQLException.", e, log);
        } catch (Exception e) {
            throw new AbadiaSQLException("adCampo. cargarTablaRecursosArar. Exception.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Recuperar una hashmap con los monjes asociados a cada campo
     *
     * @return
     * @throws AbadiaException
     */
    public HashMap<Integer, ArrayList<MonjeAgricultura>> cargarTablaMonjesPorCultivo(int idPeriodo) throws AbadiaException {
        String sSQL = "SELECT CAMPOID FROM campo_monje GROUP BY CAMPOID";
        HashMap<Integer, ArrayList<MonjeAgricultura>> hmMonjes = new HashMap<Integer, ArrayList<MonjeAgricultura>>();

        int idCampo;

        adMonje monjeAD;

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            monjeAD = new adMonje(con);
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                idCampo = rs.getInt("CAMPOID");
                hmMonjes.put(idCampo, monjeAD.recuperarMonjesActividadCampo(Constantes.TAREA_AGRICULTURA, idPeriodo, idCampo));
            }
            return hmMonjes;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adCampo. cargarTablaMonjesPorCultivo. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Elimina los registros huerfanos de la tabla campo_monje cuando a un monje se le cambia
     * la tarea de agricultura por cualquier otra.
     *
     * @throws AbadiaException
     */
    public void eliminarCampoMonjeHuerfanos() throws AbadiaException {
        String sSQL = "DELETE FROM campo_monje where monjeid not in (SELECT monjeid FROM actividad where actividadid = " + Constantes.TAREA_AGRICULTURA + ")";
        adUtils utils = null;

        try {
            utils = new adUtils(con);
            utils.execSQL(sSQL);

        } catch (AbadiaException e) {
            throw new AbadiaSQLException("adCampo. eliminarCampoMonjeHuerfanos. SQLException.", e, log);
        }

    }

    /**
     * Recupera un objeto Cultivo con los datos básicos de un cultivo.
     *
     * @param idAbadia
     * @param idCultivo
     * @return
     * @throws AbadiaException
     */
    public Cultivo recuperarDatosCultivo(int idAbadia, int idCultivo) throws AbadiaException {
        String sSQL = "Select CAMPOID, ESTADO, ARADO_TOTAL " +
                "from campo where ABADIAID = ? and CAMPOID = ?";

        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idAbadia);
            ps.setInt(2, idCultivo);
            rs = ps.executeQuery();
            Cultivo cultivo;
            if (rs.next()) {
                cultivo = new Cultivo();
                cultivo.setIdCultivo(rs.getInt("CAMPOID"));
                cultivo.setIdEstado(rs.getInt("ESTADO"));
                cultivo.setArado_total(rs.getDouble("ARADO_TOTAL"));
            } else {
                throw new CampoNoEncontradoException("adCampo. recuperarDatosCultivo", log);
            }

            return cultivo;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adCampo. recuperarDatosCultivo. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Recupera un objeto Cultivo con los datos del cultivo en el nivel solicitado
     *
     * @param idAlimento
     * @param nivel
     * @return
     * @throws AbadiaException
     */
    public Cultivo recuperarDatosCultivoNivel(int idAlimento, int nivel) throws AbadiaException {
        String sSQL = " Select * " +
                " from campo_alimento where ALIMENTOID = ? and NIVEL_ARBOL = ? ";

        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idAlimento);
            ps.setInt(2, nivel);
            rs = ps.executeQuery();
            Cultivo cultivo = null;
            if (rs.next()) {
                cultivo = new Cultivo();
                cultivo.setIdAlimento(rs.getInt("ALIMENTOID"));
                cultivo.setNivelCampo(rs.getInt("NIVEL_ARBOL"));
            }

            return cultivo;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adCampo. recuperarDatosCultivoNivel. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Incrementa el acumulado diario de puntos de cultivo de un campo.
     *
     * @param idCultivo
     * @param valor
     * @throws AbadiaException
     */
    public void incrementarAcumuladoCultivo(int idCultivo, double valor) throws AbadiaException {
        String sSQL = "update campo set cultiva_dia = cultiva_dia + " + valor + " where campoid = " + idCultivo;
        adUtils utils = null;

        try {
            utils = new adUtils(con);
            utils.execSQL(sSQL);
        } catch (AbadiaException e) {
            throw new AbadiaSQLException("adCampo. incrementarAcumuladoCultivo", e, log);
        } finally {

        }

    }


    /**
     * Recupera un arraylist con los datos de los campos que
     * no tienen asignado ningún monje.
     *
     * @param idEstado
     * @return
     * @throws AbadiaException
     */
    public ArrayList<Cultivo> recuperarCamposDesatendidos(int idEstado) throws AbadiaException {
        String sSQL = "Select c.CAMPOID, c.ABADIAID, c.ESTADO, c.NIVEL_CAMPO, c.ALIMENTOID, u.IDIOMAID " +
                " from campo c, abadia a, usuario u where c.ESTADO = ? and a.ABADIAID = c.ABADIAID and a.USUARIOID = u.USUARIOID " +
                " and c.campoid not in (select campoid from campo_monje) ORDER BY IDIOMAID";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idEstado);
            rs = ps.executeQuery();
            Cultivo cultivo;
            ArrayList<Cultivo> listaCampos = new ArrayList<Cultivo>();
            while (rs.next()) {
                cultivo = new Cultivo();
                cultivo.setIdEstado(rs.getInt("ESTADO"));
                cultivo.setIdCultivo(rs.getInt("CAMPOID"));
                cultivo.setIdAbadia(rs.getInt("ABADIAID"));
                cultivo.setNivelCampo(rs.getInt("NIVEL_CAMPO"));
                cultivo.setIdAlimento(rs.getInt("ALIMENTOID"));
                cultivo.setIdIdioma(rs.getInt("IDIOMAID"));

                listaCampos.add(cultivo);
            }
            return listaCampos;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adCampo. recuperarCamposDesatendidos. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }


}
