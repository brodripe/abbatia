package org.abbatia.adbean;

import org.abbatia.adbean.base.adbeans;
import org.abbatia.bean.*;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.HTML;
import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class adJerarquiaEclesiastica extends adbeans {
    private static Logger log = Logger.getLogger(adJerarquiaEclesiastica.class.getName());

    /**
     * Instancia la clase sin obtener una nueva conexión
     *
     * @param con
     * @throws AbadiaException
     */
    public adJerarquiaEclesiastica(Connection con) throws AbadiaException {
        super(con);
    }

    //recupera el objeto JerarquiaEclesiastica cargado...

    public JerarquiaEclesiastica recuperarJerarquiaEclesiastica(int idDeJerarquiaTmp) throws SQLException {
        //Definición de cadena sql de consulta
        String sSQL = "Select * from jerarquia_eclesiastica where JERARQUIAID = ?";
        //creo un objeto de tipo JerarquiaEclesiastica
        JerarquiaEclesiastica jerarquiaEclesiastica = new JerarquiaEclesiastica();
        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = con.prepareStatement(sSQL);
        //asigno el parametro para el filtrado de la consulta
        //esta instruccion sustituye el simbolo ? por la clave.

        int parNo = 1;
        ps.setInt(parNo, idDeJerarquiaTmp);
        //Lanzo la consulta y cargo el resultado en un resultset
        ResultSet rs = ps.executeQuery();
        //si la consulta encuentra la jerarquiaEclesiastica....
        if (rs.next()) {
            //iniciamos el volcado de datos sobre
            //el objeto jerarquiaEclesiastica.
            jerarquiaEclesiastica.setIdDeJerarquia(rs.getInt("JERARQUIAID"));
            jerarquiaEclesiastica.setDescripcion(rs.getString("DESCRIPCION"));

            //devolvemos el objeto JerarquiaEclesiastica informado.
            return jerarquiaEclesiastica;
        }
        //si jerarquiaEclesiastica no se localiza, devolveremos null
        return null;
    }

    //dará de alta un objeto JerarquiaEclesiastica en la base de datos

    public void crearJerarquiaEclesiastica(JerarquiaEclesiastica jerarquiaEclesiastica) throws SQLException {
        String sSQL = "Insert Into `jerarquia_eclesiastica` ( `JERARQUIAID`,`DESCRIPCION`) Values ('?','?');";

        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = con.prepareStatement(sSQL);
        //asigno los valores
        int parNo = 0;
        ps.setInt(parNo++, jerarquiaEclesiastica.getIdDeJerarquia());
        ps.setString(parNo, jerarquiaEclesiastica.getDescripcion());
        // Ejecutarlo
        ps.execute();
    }


    //elimina un objeto JerarquiaEclesiastica de la base de datos
    //a partir de un objeto JerarquiaEclesiastica devuelve verdadero si no ha ocurrido un error

    public boolean eliminarJerarquiaEclesiastica(JerarquiaEclesiastica jerarquiaEclesiastica) throws SQLException {
        return eliminarJerarquiaEclesiastica(jerarquiaEclesiastica.getIdDeJerarquia());
    }

    //Elimina un objeto JerarquiaEclesiastica de la base de datos
    //a partir de la clave del JerarquiaEclesiastica

    public boolean eliminarJerarquiaEclesiastica(int idDeJerarquiaTmp) throws SQLException {
        String sSQL = "Delete From jerarquia_eclesiastica Where `JERARQUIAID` = ?";

        PreparedStatement ps = con.prepareStatement(sSQL);
        //asigno el parametro para el filtrado de la consulta
        //esta instruccion sustituye el simbolo ? por la clave del monje
        int parNo = 0;
        ps.setInt(parNo, idDeJerarquiaTmp);
        // Ejecutarlo
        return ps.execute();
    }

    // Recuperar jerarquia

    public String recuperarDescJerarquiaEclesiastica(int idDeJerarquiaTmp, int idiomaid) throws AbadiaException {
        //Definición de cadena sql de consulta
        String sSQL = "SELECT literal from jerarquia_eclesiastica je, literales l where je.jerarquiaid = ? " +
                " and l.literalid = je.literalid and l.idiomaid = ?";

        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            ps = con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave.

            int parNo = 1;
            ps.setInt(parNo++, idDeJerarquiaTmp);
            ps.setInt(parNo, idiomaid);
            //Lanzo la consulta y cargo el resultado en un resultset
            rs = ps.executeQuery();
            //si la consulta encuentra la jerarquiaEclesiastica....
            if (rs.next()) {
                //iniciamos el volcado de datos sobre
                //el objeto jerarquiaEclesiastica.
                return rs.getString("LITERAL");
            }
            return "";

        } catch (SQLException e) {
            throw new AbadiaSQLException("adJerarquiaEclesiastica. recuperarDescJerarquiaEclesiastica. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }


    //Actualiza el nivel de una habilidad determinada en
    //base de datos devuelve verdadero si lo ha hecho correctamente

    public boolean setJerarquiaid(long abadiaID, int monjeID, int jerarquiaid) throws AbadiaSQLException {
        String sSQL = "Update monje Set jerarquiaid = ? Where AbadiaID=? and MONJEID = ? ";

        // Preparar
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            // Asignar
            ps.setInt(1, jerarquiaid);
            ps.setLong(2, abadiaID);
            ps.setInt(3, monjeID);
            // Ejecutarlo
            return ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adJerarquiaEclesiastica. setjerarquiaid. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public boolean setJerarquiaid(int monjeID, int jerarquiaid) throws AbadiaSQLException {
        String sSQL = "Update monje Set jerarquiaid = ? Where MONJEID = ? ";

        // Preparar
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            // Asignar
            ps.setInt(1, jerarquiaid);
            ps.setInt(2, monjeID);
            // Ejecutarlo
            return ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adJerarquiaEclesiastica. setjerarquiaid. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }


    // Devuelve un id de monje por la jerarquia que queremos recuperar

    public int getMonjeIDporJerarquia(long idAbadia, int jerarquiaid) throws AbadiaSQLException {
        String sSQL = "Select MONJEID from monje m where m.ESTADO <> 1 AND m.jerarquiaid = ? AND m.ABADIAID = ? ";
        // Buscar monje
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, jerarquiaid);
            ps.setLong(2, idAbadia);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("MONJEID");
            } else return 0;


        } catch (SQLException e) {
            log.error("adMonje. getSiguiente. Error SQL", e);
            throw new AbadiaSQLException("adMonje. getSiguiente. Error SQL", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    // Devuelve un id de monje por la jerarquia que queremos recuperar

    public int getMonjeIDporJerarquia(int jerarquiaid) throws AbadiaSQLException {
        String sSQL = "Select MONJEID from monje m where m.ESTADO <> 1 AND m.jerarquiaid = ? ";
        // Buscar monje
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, jerarquiaid);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("MONJEID");
            } else return 0;


        } catch (SQLException e) {
            //log.error("adMonje. getSiguiente. Error SQL", e);
            throw new AbadiaSQLException("adMonje. getSiguiente. Error SQL", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }


    // Devuelve un id de monje por la jerarquia que queremos recuperar

    public ArrayList<Monje> getMonjesPorJerarquia(int jerarquiaid) throws AbadiaException {
        String sSQL = "Select monjeid from monje where estado <> 1 and jerarquiaid = ?";
        // Buscar monje
        adMonje admonje;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Monje> listaMonjes = new ArrayList<Monje>();
        Monje monje;
        try {
            ps = getConexion().prepareStatement(sSQL);
            ps.setInt(1, jerarquiaid);
            rs = ps.executeQuery();
            while (rs.next()) {
                admonje = new adMonje(con);
                monje = admonje.recuperarDatosMonje(rs.getInt("monjeid"));
                listaMonjes.add(monje);
            }
            return listaMonjes;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adJerarquiaEclesiastica. getMonjesPorJerarquia. Error SQL", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    // --

    public ArrayList<Table> recuperarEminencias(Usuario usuario, int opcion, int clave, MessageResources resource) throws
            AbadiaException {

        String sSQL = "Select l.literal, m.nombre, m.apellido1, m.monjeid, r.descripcion, a.nombre nomabadia " +
                "from monje m, abadia a, region r, jerarquia_eclesiastica je, literales l " +
                "where m.estado <> 1 and m.jerarquiaid >= 3 and m.abadiaid = a.abadiaid and a.regionid = r.regionid and je.jerarquiaid = m.jerarquiaid and l.literalid = je.literalid and l.idiomaid = ? " +
                "order by m.jerarquiaid desc, m.nombre, m.apellido1";

        ArrayList<Table> monjes = new ArrayList<Table>();
        int n = 0, monjeid = 0;
        HTML cHTML = new HTML();
        String sHTML, sOpcion = "", sParam = "", sOpcionHTML;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            Table monje;
            sHTML = cHTML.getTD_TitleTable(resource.getMessage("eminencias.jerarquia"), "", 0);
            sHTML += cHTML.getTD_TitleTable(resource.getMessage("eminencias.nombre"), "", 0);
            sHTML += cHTML.getTD_TitleTable(resource.getMessage("eminencias.nombre.abadia"), "", 0);
            sHTML += cHTML.getTD_TitleTable(resource.getMessage("eminencias.region"), "", 0);
            monje = new Table(0, "<tr>" + sHTML + "</tr>");
            monjes.add(monje);

            switch (opcion) {
                // Dar dinero
                case 0:
                    sOpcion = "dar_dinero.do";
                    sParam = "&monjeid_origen=" + clave;
                    monjeid = clave;
                    break;
                // Asesinar eminencia
                case 1:
                    sOpcion = "contratarSicario.do";
                    sParam = "&confirmar=1&sicarioid=" + clave;
                    monjeid = clave;
                    break;
            }

            ps = con.prepareStatement(sSQL);
            ps.setInt(1, usuario.getIdDeIdioma());
            rs = ps.executeQuery();
            while (rs.next()) {
                if (monjeid != rs.getInt("monjeid")) {
                    sOpcionHTML = "<a href=\"" + sOpcion + "?monjeid=" + rs.getInt("monjeid") + sParam + "\">";
                    sOpcionHTML += rs.getString("nombre") + " " + resource.getMessage("monjes.abadia.nomciudad") + " " + rs.getString("apellido1");
                    sOpcionHTML += "</a>";
                    sHTML = cHTML.getTD(HTML.ALIGN_NONE, null, rs.getString("literal"));
                    sHTML += cHTML.getTD(HTML.ALIGN_NONE, null, sOpcionHTML);
                    sHTML += cHTML.getTD(HTML.ALIGN_CENTER, null, rs.getString("nomabadia"));
                    sHTML += cHTML.getTD(HTML.ALIGN_CENTER, null, rs.getString("descripcion"));
                    monje = new Table(0, cHTML.getTR(n++, sHTML));
                    monjes.add(monje);
                }
            }
            return monjes;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adJerarquiaEclesiastica. recuperarEminencias. Error SQL", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public ArrayList<MonjeEminencia> recuperarEminencias(Usuario usuario, int opcion, int clave) throws
            AbadiaException {

        String sSQL = "Select l.literal, m.nombre, m.apellido1, m.monjeid, r.descripcion, a.nombre nomabadia " +
                "from monje m, abadia a, region r, jerarquia_eclesiastica je, literales l " +
                "where m.estado <> 1 and m.jerarquiaid >= 3 and m.abadiaid = a.abadiaid and a.regionid = r.regionid and je.jerarquiaid = m.jerarquiaid and l.literalid = je.literalid and l.idiomaid = ? " +
                "order by m.jerarquiaid desc, m.nombre, m.apellido1";

        ArrayList<MonjeEminencia> monjes = new ArrayList<MonjeEminencia>();
        MonjeEminencia monje;
        int monjeid = 0;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, usuario.getIdDeIdioma());
            rs = ps.executeQuery();
            while (rs.next()) {
                if (monjeid != rs.getInt("monjeid")) {
                    monje = new MonjeEminencia();
                    monje.setIdMonje(rs.getInt("monjeid"));
                    monje.setNombre(rs.getString("nombre"));
                    monje.setApellido1(rs.getString("apellido1"));
                    monje.setAbadia(rs.getString("nomabadia"));
                    monje.setRegion(rs.getString("descripcion"));
                    monje.setJerarquia(rs.getString("literal"));
                    monjes.add(monje);
                }
            }
            return monjes;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adJerarquiaEclesiastica. recuperarEminencias. Error SQL", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Devuelve un {@link boolean} notificando si existe en la abadia recibida
     * algún monje con la jerarquia recibida
     *
     * @param abadiaid    Identificador de abadia
     * @param jerarquiaid Identificador de jerarquia por la que preguntamos
     * @return
     * @throws AbadiaException Excepción base de abbatia
     */
    public boolean tieneEminencia(int abadiaid, int jerarquiaid) throws AbadiaException {
        String sSQL = "";

        if (jerarquiaid == Constantes.JERARQUIA_CARDENAL) {
            sSQL = "Select * from cardenales WHERE ABADIAID = ?";
        } else if (jerarquiaid == Constantes.JERARQUIA_OBISPO) {
            sSQL = "Select * from obispado WHERE ABADIAID = ? and monjeid <> -1";
        } else if (jerarquiaid == Constantes.JERARQUIA_PAPA) {
            sSQL = "Select * from papas WHERE ABADIAID = ? and estado = 0";
        }

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, abadiaid);
            rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            throw new AbadiaSQLException("adJerarquiaEclesiastica. tieneEminencia", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }


}
