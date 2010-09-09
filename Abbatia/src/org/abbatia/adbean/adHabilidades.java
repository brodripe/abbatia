package org.abbatia.adbean;

import org.abbatia.adbean.base.adbeans;
import org.abbatia.bean.HabilidadMonje;
import org.abbatia.bean.Monje;
import org.abbatia.bean.Usuario;
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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;

public class adHabilidades extends adbeans {
    private static Logger log = Logger.getLogger(adHabilidades.class.getName());

    /**
     * Instancia la clase sin obtener una nueva conexión
     *
     * @param con
     * @throws org.abbatia.exception.base.AbadiaException
     *
     */
    public adHabilidades(Connection con) throws AbadiaException {
        super(con);
    }


    /**
     * Incrementan o decrementan una habilidad en concreto para todos los monjes del JUEGO
     *
     * @param idHabilidad Identificador de habilidad
     * @param valor       valor de incremento
     * @throws org.abbatia.exception.AbadiaSQLException
     *          Excepción base de abbatia
     */
    public void incrementarHabilidad(int idHabilidad, double valor) throws AbadiaSQLException {
        String sSQL = "UPDATE habilidad_monje set valor_actual = valor_actual + (?) where habilidadid = ?";

        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave del monje
            int parNo = 1;
            ps.setDouble(parNo++, valor);
            ps.setInt(parNo, idHabilidad);
            // Ejecutarlo
            ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adHabilidades. incrementarHabilidad. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /**
     * Incrementan o decrementan una habilidad en concreto para todos los monjes del JUEGO
     *
     * @param idHabilidad Identificador de habilidad
     * @param valor       valor de incremento
     * @throws org.abbatia.exception.AbadiaSQLException
     *          Excepción base de abbatia
     */
    public void decrementarHabilidad(int idHabilidad, double valor) throws AbadiaSQLException {
        String sSQL = "UPDATE habilidad_monje set valor_actual = valor_actual - (?) where habilidadid = ?";

        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave del monje
            int parNo = 1;
            ps.setDouble(parNo++, valor);
            ps.setInt(parNo, idHabilidad);
            // Ejecutarlo
            ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adHabilidades. incrementarHabilidad. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /**
     * Incrementan o decrementan una habilidad en concreto
     *
     * @param idHabilidad Identificador de habilidad
     * @param valor       valor de incremento
     * @param idMonje
     * @return
     * @throws org.abbatia.exception.AbadiaSQLException
     *          Excepción base de abbatia
     */
    public boolean incrementarHabilidad(int idMonje, int idHabilidad, double valor) throws AbadiaException {
        String sSQL = "UPDATE `habilidad_monje` SET valor_actual = valor_actual + (?)  " +
                "where habilidadid = ? and monjeid = ? and valor_actual + (?) < 100 ";

        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave del monje

            int parNo = 1;
            ps.setDouble(parNo++, valor);
            ps.setInt(parNo++, idHabilidad);
            ps.setInt(parNo++, idMonje);
            ps.setDouble(parNo, valor);
            // Ejecutarlo
            return ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adHabilidades. incrementarHabilidad. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Incrementan o decrementan una habilidad en concreto
     *
     * @param idHabilidad Identificador de habilidad
     * @param valor       valor de incremento
     * @param idMonje
     * @return
     * @throws org.abbatia.exception.AbadiaSQLException
     *          Excepción base de abbatia
     */
    public boolean decrementarHabilidad(int idMonje, int idHabilidad, double valor) throws AbadiaException {
        String sSQL = "UPDATE `habilidad_monje` SET valor_actual = valor_actual - (?)  " +
                "where habilidadid = ? and monjeid = ? and valor_inicial <= valor_actual - (?) and valor_actual - (?) > 0 ";

        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave del monje

            int parNo = 1;
            ps.setDouble(parNo++, valor);
            ps.setInt(parNo++, idHabilidad);
            ps.setInt(parNo++, idMonje);
            ps.setDouble(parNo++, valor);
            ps.setDouble(parNo, valor);
            // Ejecutarlo
            return ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adHabilidades. decrementarHabilidad. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Incrementan o decrementan una habilidad en concreto
     *
     * @param idHabilidad Identificador de habilidad
     * @param valor       valor de incremento
     * @throws org.abbatia.exception.AbadiaSQLException
     *          Excepción base de abbatia
     */
    public boolean decrementarHabilidadesAbadia(int idAbadia, int idHabilidad, double valor) throws AbadiaException {
        String sSQL = "UPDATE `habilidad_monje` SET valor_actual = valor_actual - (?)  " +
                "where habilidadid = ? and monjeid in (select monjeid from monje where abadiaid = ? and estado = " + Constantes.MONJE_VIVO + ") and valor_actual - (?) > 0 ";

        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave del monje

            int parNo = 1;
            ps.setDouble(parNo++, valor);
            ps.setInt(parNo++, idHabilidad);
            ps.setInt(parNo++, idAbadia);
            ps.setDouble(parNo, valor);
            // Ejecutarlo
            return ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adHabilidades. decrementarHabilidadesAbadia. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Incrementan o decrementan una habilidad en concreto para todos los monjes de una region
     *
     * @param idRegion
     * @param idHabilidad Identificador de habilidad
     * @param valor       valor de incremento
     * @throws org.abbatia.exception.AbadiaSQLException
     *          Excepción base de abbatia
     */
    public boolean decrementarHabilidadesRegion(int idRegion, int idHabilidad, double valor) throws AbadiaException {
        String sSQL = "UPDATE `habilidad_monje` SET valor_actual = valor_actual - (?) " +
                " where habilidadid = ? and monjeid in (select m.monjeid from monje m, abadia a where m.abadiaid = a.abadiaid and a.regionid = ? ) and valor_actual - (?) > 0 ";

        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave del monje

            int parNo = 1;
            ps.setDouble(parNo++, valor);
            ps.setInt(parNo++, idHabilidad);
            ps.setInt(parNo++, idRegion);
            ps.setDouble(parNo, valor);
            // Ejecutarlo
            return ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adHabilidades. decrementarHabilidadesRegion. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Incrementa una habilidad verificando que se trate de una abadia en contreto
     *
     * @param idHabilidad Identificador de habilidad
     * @param valor       valor de incremento
     * @throws org.abbatia.exception.AbadiaSQLException
     *          Excepción base de abbatia
     */
    public boolean incrementarHabilidad(int idAbadia, int idMonje, int idHabilidad, double valor) throws AbadiaException {
        String sSQL = "UPDATE `habilidad_monje` SET valor_actual = valor_actual + (?)  " +
                "where habilidadid = ? and monjeid = ? and valor_inicial < valor_actual + (?) and valor_actual + (?) < 100 and " +
                " monjeid in (select monjeid from monje where abadiaid = ? and monjeid = ?) ";

        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave del monje
            int parNo = 1;
            ps.setDouble(parNo++, valor);
            ps.setLong(parNo++, idHabilidad);
            ps.setInt(parNo++, idMonje);
            ps.setDouble(parNo++, valor);
            ps.setDouble(parNo++, valor);
            ps.setInt(parNo++, idAbadia);
            ps.setInt(parNo, idMonje);
            // Ejecutarlo
            return ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adHabilidades. incrementarHabilidad. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Recupera un {@link java.util.ArrayList} de objetos {@link org.abbatia.bean.Habilidad} a partir de un identificador de monje
     *
     * @param idMonje Identificador de monje
     * @param usuario
     * @return
     * @throws org.abbatia.exception.base.AbadiaException
     *          Excepción base de abbatia
     */
    public ArrayList<HabilidadMonje> recuperarHabilidadesMonje(int idMonje, Usuario usuario) throws AbadiaException {
        //Definición de canena sql de consulta
        ArrayList<HabilidadMonje> listaHabilidades = new ArrayList<HabilidadMonje>();
        String sSQL = "Select h.MONJEID, h.HABILIADID, l.LITERAL, mh.VALOR_INICIAL, mh.MONJEID, mh.VALOR_ACTUAL " +
                "from habilidad_tipo ht, habilidad_monje mh where mh.MONJEID = ? AND ht.HABILIDADID = mh.HABILIADID and l.LITERALID = ht.LITERALID and l.IDIOMAID = " + usuario.getIdDeIdioma();
        HabilidadMonje habilidad = null;
        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave.
            ps.setInt(1, idMonje);
            //Lanzo la consulta y cargo el resultado en un resultset
            rs = ps.executeQuery();
            //si la consulta encuentra la abbatia....
            while (rs.next()) {
                //iniciamos el volcado de datos sobre
                //el objeto abbatia.
                habilidad = new HabilidadMonje();
                habilidad.setIdMonje(rs.getInt("MONJEID"));
                habilidad.setIdHabilidad(rs.getInt("HABILIDADID"));
                habilidad.setDescripcion(rs.getString("LITERAL"));
                habilidad.setValorInicial(rs.getDouble("VALOR_INICIAL"));
                habilidad.setValorActual(rs.getDouble("VALOR_ACTUAL"));

                listaHabilidades.add(habilidad);
            }
            //si la abbatia no se localiza, devolveremos null
            return listaHabilidades;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adMonje. recuperarHabilidades. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /**
     * Recupera un {@link java.util.ArrayList} de objetos {@link org.abbatia.bean.HabilidadMonje} a partir de un identificador de monje
     *
     * @param idMonje Identificador de monje
     * @return
     * @throws org.abbatia.exception.base.AbadiaException
     *          Excepción base de abbatia
     */
    public ArrayList<HabilidadMonje> recuperarHabilidadesMonje(int idMonje) throws AbadiaException {
        //Definición de canena sql de consulta
        ArrayList<HabilidadMonje> listaHabilidades = new ArrayList<HabilidadMonje>();
        String sSQL = "Select hm.MONJEID, hm.HABILIDADID, hm.VALOR_INICIAL, hm.MONJEID, hm.VALOR_ACTUAL " +
                "from habilidad_monje hm where hm.MONJEID = ? ";
        HabilidadMonje habilidad;
        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave.
            ps.setInt(1, idMonje);
            //Lanzo la consulta y cargo el resultado en un resultset
            rs = ps.executeQuery();
            //si la consulta encuentra la abbatia....
            while (rs.next()) {
                //iniciamos el volcado de datos sobre
                //el objeto abbatia.
                habilidad = new HabilidadMonje();
                habilidad.setIdMonje(rs.getInt("MONJEID"));
                habilidad.setIdHabilidad(rs.getInt("HABILIDADID"));
                habilidad.setValorInicial(rs.getDouble("VALOR_INICIAL"));
                habilidad.setValorActual(rs.getDouble("VALOR_ACTUAL"));

                listaHabilidades.add(habilidad);
            }
            //si la abbatia no se localiza, devolveremos null
            return listaHabilidades;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adHabilidades. recuperarHabilidades. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /**
     * Recupera un {@link java.util.HashMap} de objetos {@link org.abbatia.bean.HabilidadMonje} a partir de un identificador de monje
     *
     * @param idMonje Identificador de monje
     * @return
     * @throws org.abbatia.exception.base.AbadiaException
     *          Excepción base de abbatia
     */
    public HashMap<Integer, HabilidadMonje> recuperarHabilidadesMonjeHM(int idMonje) throws AbadiaException {
        //Definición de canena sql de consulta
        String sSQL = "Select hm.MONJEID, hm.HABILIDADID, hm.VALOR_INICIAL, hm.MONJEID, hm.VALOR_ACTUAL " +
                "from habilidad_monje hm where hm.MONJEID = ? ";
        HabilidadMonje habilidad = null;
        HashMap<Integer, HabilidadMonje> hmHabilidades = new HashMap<Integer, HabilidadMonje>();
        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave.
            ps.setInt(1, idMonje);
            //Lanzo la consulta y cargo el resultado en un resultset
            rs = ps.executeQuery();
            //si la consulta encuentra la abbatia....
            while (rs.next()) {
                //iniciamos el volcado de datos sobre
                //el objeto abbatia.
                habilidad = new HabilidadMonje();
                habilidad.setIdMonje(rs.getInt("MONJEID"));
                habilidad.setIdHabilidad(rs.getInt("HABILIDADID"));
                habilidad.setValorInicial(rs.getDouble("VALOR_INICIAL"));
                habilidad.setValorActual(rs.getDouble("VALOR_ACTUAL"));
                hmHabilidades.put(habilidad.getIdHabilidad(), habilidad);
            }
            //si la abbatia no se localiza, devolveremos null
            return hmHabilidades;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adHabilidades. recuperarHabilidadesHM. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /**
     * Actualiza el valor de una habilidad determinada
     *
     * @param idMonje     Identificador de monje
     * @param idHabilidad Identificador de habilidad
     * @param valor       Valor de la habilidad
     * @throws org.abbatia.exception.AbadiaSQLException
     *          Excepción base de abbatia
     */
    public boolean actualizarHabilidad(int idMonje, int idHabilidad, double valor) throws AbadiaException {
        String sSQL = "Update habilidad_monje Set VALOR_ACTUAL = ? Where HABILIDADID = ? AND MONJEID = ?";

        // Preparar
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            // Asignar
            ps.setDouble(1, valor);
            ps.setInt(2, idHabilidad);
            ps.setInt(3, idMonje);
            // Ejecutarlo
            return ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adHabilidades. actualizarHabilidad. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /**
     * crea una habilidad para un monje a partir de un objeto {@link HabilidadMonje}
     *
     * @param habilidad objeto para dar de alta
     * @throws org.abbatia.exception.AbadiaSQLException
     *          Excepción base de abbatia
     */
    public void crearHabilidad(HabilidadMonje habilidad) throws AbadiaException {
        String sSQL = "Insert Into habilidad_monje (MONJEID, HABILIDADID, VALOR_INICIAL, VALOR_ACTUAL) Values (? , ?, ?, ?)";
        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        try {
            if (!existeHabilidad(habilidad.getIdMonje(), habilidad.getIdHabilidad())) {
                ps = con.prepareStatement(sSQL);
                //asigno los valores

                ps.setInt(1, habilidad.getIdMonje());
                ps.setInt(2, habilidad.getIdHabilidad());
                ps.setDouble(3, habilidad.getValorInicial());
                ps.setDouble(4, habilidad.getValorActual());
                // Ejecutarlo
                ps.execute();
            }
        } catch (SQLException e) {
            throw new AbadiaSQLException("adHabilidades. crearHabilidad. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /**
     * crea una habilidad para un monje a partir de los valores a 'pelo'
     *
     * @param idMonje     identificador del monje
     * @param idHabilidad identificador de la habilidad
     * @param valor       valor inicial para la habilidad
     * @throws org.abbatia.exception.AbadiaSQLException
     *          Excepción base de abbatia
     */
    public void crearHabilidad(int idMonje, int idHabilidad, double valor) throws AbadiaException {
        HabilidadMonje habilidad = new HabilidadMonje();
        habilidad.setIdMonje(idMonje);
        habilidad.setIdHabilidad(idHabilidad);
        habilidad.setValorInicial(valor);
        habilidad.setValorActual(valor);
        crearHabilidad(habilidad);
    }

    /**
     * verifica si existe una habilidad determinada para un monje concreto
     *
     * @param idMonje     Identificador de monje
     * @param idHabilidad Identificador de habilidad
     * @throws org.abbatia.exception.AbadiaSQLException
     *          Excepción base de abbatia
     */
    public boolean existeHabilidad(int idMonje, int idHabilidad) throws AbadiaException {
        String sSQL = "select * from habilidad_monje where MONJEID = ? AND HABILIDADID = ?";
        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno los valores
            ps.setLong(1, idMonje);
            ps.setLong(2, idHabilidad);

            // Ejecutarlo
            rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adHabilidades. existeHabilidad. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * recupera la fe del papa
     *
     * @param idHabilidad Identificador de habilidad
     * @throws org.abbatia.exception.AbadiaSQLException
     *          Excepción base de abbatia
     */
    public double recuperaHabilidadPapa(int idHabilidad) throws AbadiaException {
        String sSQL = "select hm.valor_actual " +
                " from habilidad_monje hm, monje m " +
                " where hm.MONJEID = m.MONJEID AND m.jerarquiaid = " + Constantes.JERARQUIA_PAPA + " AND HABILIDADID = ? ";
        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno los valores
            ps.setLong(1, idHabilidad);

            // Ejecutarlo
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("valor_actual");
            }
            return 0;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adHabilidades. recuperaHabilidadPapa. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * recupera la media de fe de los cardenales
     *
     * @param idHabilidad Identificador de habilidad
     * @throws org.abbatia.exception.AbadiaSQLException
     *          Excepción base de abbatia
     */
    public double recuperaHabilidadCardenales(int idHabilidad) throws AbadiaException {
        String sSQL = "select avg(hm.valor_actual) as valor_fe " +
                " from habilidad_monje hm, monje m " +
                " where hm.MONJEID = m.MONJEID AND m.jerarquiaid = " + Constantes.JERARQUIA_CARDENAL + " AND HABILIDADID = ? ";
        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno los valores
            ps.setLong(1, idHabilidad);

            // Ejecutarlo
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("valor_fe");
            }
            return 0;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adHabilidades. recuperaHabilidadCardenales. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * recupera una habilidad del obispo de una región determinada
     *
     * @param idHabilidad Identificador de habilidad
     * @param idRegion    Identificador de la región
     * @throws org.abbatia.exception.AbadiaSQLException
     *          Excepción base de abbatia
     */
    public double recuperaHabilidadObispo(int idHabilidad, int idRegion) throws AbadiaException {
        String sSQL = "select hm.valor_actual " +
                " from habilidad_monje hm, monje m, abadia a " +
                " where hm.MONJEID = m.MONJEID AND m.jerarquiaid = " + Constantes.JERARQUIA_OBISPO + " AND HABILIDADID = ? and m.abadiaid = a.abadiaid and a.regionid = ?";
        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno los valores
            ps.setInt(1, idHabilidad);
            ps.setInt(2, idRegion);

            // Ejecutarlo
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("valor_actual");
            }
            return 0;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adHabilidades. recuperaHabilidadObispo. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * recupera una habilidad del abad de una abadia determinada
     *
     * @param idHabilidad Identificador de habilidad
     * @param idAbadia    Identificador de abadia
     * @throws org.abbatia.exception.AbadiaSQLException
     *          Excepción base de abbatia
     */
    public double recuperaHabilidadAbad(int idHabilidad, int idAbadia) throws AbadiaException {
        String sSQL = "select hm.valor_actual " +
                " from habilidad_monje hm, monje m " +
                " where hm.MONJEID = m.MONJEID AND m.jerarquiaid = " + Constantes.JERARQUIA_ABAD + " AND HABILIDADID = ? and m.abadiaid = ?";
        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno los valores
            ps.setInt(1, idHabilidad);
            ps.setInt(2, idAbadia);

            // Ejecutarlo
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("valor_actual");
            }
            return 0;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adHabilidades. recuperaHabilidadAbad. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * resetea los valores de una habilidad a 0 si están por debajo de 0 y a 100 si están por encima de 100
     *
     * @param idHabilidad Identificador de habilidad
     * @throws org.abbatia.exception.AbadiaSQLException
     *          Excepción base de abbatia
     */
    public void resetearHabilidad(int idHabilidad) throws AbadiaSQLException {
        String sSQL = "UPDATE habilidad_monje set valor_actual = 0 where habilidadid = ? and valor_actual < 0";
        String sSQL2 = "UPDATE habilidad_monje set valor_actual = 100 where habilidadid = ? and valor_actual > 100";

        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idHabilidad);
            // Ejecutarlo
            ps.execute();

            ps = con.prepareStatement(sSQL2);
            ps.setInt(1, idHabilidad);
            // Ejecutarlo
            ps.execute();


        } catch (SQLException e) {
            throw new AbadiaSQLException("adHabilidades. resetearHabilidad. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /**
     * crea las habilidades iniciales de un monje
     *
     * @param monje objeto {@link Monje}
     * @throws org.abbatia.exception.AbadiaSQLException
     *          Excepción base de abbatia
     */

    public void crearHabilidadesInicialesAdmin(Monje monje) throws AbadiaException {
        HabilidadMonje habilidad = new HabilidadMonje();
        habilidad.setIdMonje(monje.getIdMonje());
        habilidad.setIdHabilidad(Constantes.HABILIDAD_CARISMA);
        habilidad.setValorInicial(Utilidades.Random(1, 50));
        habilidad.setValorActual(habilidad.getValorInicial());
        crearHabilidad(habilidad);

        habilidad = new HabilidadMonje();
        habilidad.setIdMonje(monje.getIdMonje());
        habilidad.setIdHabilidad(Constantes.HABILIDAD_DESTREZA);
        habilidad.setValorInicial(Utilidades.Random(1, 50));
        habilidad.setValorActual(habilidad.getValorInicial());

        crearHabilidad(habilidad);

        habilidad = new HabilidadMonje();
        habilidad.setIdMonje(monje.getIdMonje());
        habilidad.setIdHabilidad(Constantes.HABILIDAD_FUERZA);
        habilidad.setValorInicial(Utilidades.Random(1, 50));
        habilidad.setValorActual(habilidad.getValorInicial());
        crearHabilidad(habilidad);

        habilidad = new HabilidadMonje();
        habilidad.setIdMonje(monje.getIdMonje());
        habilidad.setIdHabilidad(Constantes.HABILIDAD_IDIOMA);
        habilidad.setValorInicial(Utilidades.Random(1, 50));
        habilidad.setValorActual(habilidad.getValorInicial());
        crearHabilidad(habilidad);

        habilidad = new HabilidadMonje();
        habilidad.setIdMonje(monje.getIdDeMonje());
        habilidad.setIdHabilidad(Constantes.HABILIDAD_TALENTO);
        habilidad.setValorInicial(Utilidades.Random(1, 50));
        habilidad.setValorActual(habilidad.getValorInicial());
        crearHabilidad(habilidad);

        habilidad = new HabilidadMonje();
        habilidad.setIdMonje(monje.getIdDeMonje());
        habilidad.setIdHabilidad(Constantes.HABILIDAD_SABIDURIA);
        habilidad.setValorInicial(Utilidades.Random(1, 50));
        habilidad.setValorActual(habilidad.getValorInicial());
        crearHabilidad(habilidad);

        habilidad = new HabilidadMonje();
        habilidad.setIdMonje(monje.getIdDeMonje());
        habilidad.setIdHabilidad(Constantes.HABILIDAD_FE);
        habilidad.setValorInicial(Utilidades.Random(1, 50));
        habilidad.setValorActual(habilidad.getValorInicial());
        crearHabilidad(habilidad);
    }

    /**
     * Recupera una habilidad determinada a partir del idmonje y el adhabilidad
     *
     * @param idMonje
     * @param idHabilidad
     * @return
     * @throws AbadiaException
     */
    public double recuperarHabilidad(int idMonje, int idHabilidad) throws AbadiaException {
        String sSQL = "select valor_actual from habilidad_monje where habilidadid = ? AND monjeid = ?";
        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno los valores
            ps.setLong(1, idHabilidad);
            ps.setLong(2, idMonje);

            // Ejecutarlo
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("VALOR_ACTUAL");
            }
            return 0;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adHabilidades. recuperarHabilidad. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Recupera una habilidad determinada a partir del idmonje y el adhabilidad por consulta estática
     *
     * @param idMonje
     * @param idHabilidad
     * @return
     * @throws AbadiaException
     */
    public double recuperarHabilidadMonje(int idMonje, int idHabilidad) throws AbadiaException {
        int habilidad;
        String sSQL = "select valor_actual from habilidad_monje where habilidadid = " + idHabilidad + " AND monjeid = " + idMonje;
        adUtils utils = new adUtils(con);
        habilidad = utils.getSQL(sSQL, 0);
        return habilidad;
    }

    /**
     * Recupera una habilidad determinada a partir del idmonje y el adhabilidad
     *
     * @param idMonje
     * @param idHabilidad
     * @param defecto
     * @return
     * @throws AbadiaException
     */
    public double recuperarHabilidad(int idMonje, int idHabilidad, double defecto) throws AbadiaException {
        String sSQL = "select valor_actual from habilidad_monje where habilidadid = ? AND monjeid = ?";
        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno los valores
            ps.setLong(1, idHabilidad);
            ps.setLong(2, idMonje);

            // Ejecutarlo
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("VALOR_ACTUAL");
            }
            return defecto;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adHabilidades. recuperarHabilidad. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Recupera un vector con las habilidades utilizadas para la copia de libros
     * utilizando las posiciones del vector del siguiente modo:
     * 0 - Destreza
     * 1 - Idioma
     * 2 - Talento
     * 3 - Fe
     *
     * @param idMonje
     * @return
     * @throws AbadiaException
     */
    public int[] recuperarHabilidadesCopiar(int idMonje) throws AbadiaException {
        String sSQL = "Select round(mh_fe.valor_actual) as fe, round(mh_des.valor_actual) as destreza, round(mh_tal.valor_actual) as talento, round(mh_idi.valor_actual) as idioma " +
                " from monje m, habilidad_monje mh_fe, habilidad_monje mh_des, habilidad_monje mh_tal, habilidad_monje mh_idi " +
                " where m.MONJEID = mh_fe.MONJEID and mh_fe.habilidadid = " + Constantes.HABILIDAD_FE + " and " +
                " m.MONJEID = mh_des.MONJEID and mh_des.habilidadid = " + Constantes.HABILIDAD_DESTREZA + " and " +
                " m.MONJEID = mh_tal.MONJEID and mh_tal.habilidadid = " + Constantes.HABILIDAD_TALENTO + " and " +
                " m.MONJEID = mh_idi.MONJEID and mh_idi.habilidadid = " + Constantes.HABILIDAD_IDIOMA + " and " +
                " m.MONJEID = ?  ";

        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno los valores
            ps.setLong(1, idMonje);
            int[] habilidades = new int[4];
            // Ejecutarlo
            rs = ps.executeQuery();
            if (rs.next()) {
                habilidades[0] = rs.getInt("DESTREZA");
                habilidades[1] = rs.getInt("IDIOMA");
                habilidades[2] = rs.getInt("TALENTO");
                habilidades[3] = rs.getInt("FE");
            }
            return habilidades;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adHabilidades. recuperarHabilidadesCopiar. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Recupera un vector con las habilidades utilizadas para la restauración de libros
     * utilizando las posiciones del vector del siguiente modo:
     * 0 - Destreza
     * 1 - Idioma
     * 2 - Talento
     * 3 - Fe
     *
     * @param idMonje
     * @return
     * @throws AbadiaException
     */
    public double[] recuperarHabilidadesRestaurar(int idMonje) throws AbadiaException {
        String sSQL = "Select mh_fe.valor_actual as fe, mh_des.valor_actual as destreza, mh_tal.valor_actual as talento " +
                " from monje m, habilidad_monje mh_fe, habilidad_monje mh_des, habilidad_monje mh_tal " +
                " where m.MONJEID = mh_fe.MONJEID and mh_fe.habilidadid = " + Constantes.HABILIDAD_FE + " and " +
                " m.MONJEID = mh_des.MONJEID and mh_des.habilidadid = " + Constantes.HABILIDAD_DESTREZA + " and " +
                " m.MONJEID = mh_tal.MONJEID and mh_tal.habilidadid = " + Constantes.HABILIDAD_TALENTO + " and " +
                " m.MONJEID = ?  ";

        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno los valores
            ps.setLong(1, idMonje);
            double[] habilidades = new double[4];
            // Ejecutarlo
            rs = ps.executeQuery();
            if (rs.next()) {
                habilidades[0] = rs.getDouble("DESTREZA");
                habilidades[2] = rs.getDouble("TALENTO");
                habilidades[3] = rs.getDouble("FE");
            }
            return habilidades;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adHabilidades. recuperarHabilidadesRestaurar. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Crea las habilidades iniciales de un monje
     *
     * @param monje
     * @throws AbadiaSQLException
     */
    public void crearHabilidadesIniciales(Monje monje) throws AbadiaException {

        Hashtable habilidadesHT = monje.getHabilidadesDeLosMonjesHT();
        Enumeration habilidades = habilidadesHT.keys();
        String sClave = "";
        int iValor;
        HabilidadMonje habilidad = null;

        while (habilidades.hasMoreElements()) {
            habilidad = new HabilidadMonje();
            sClave = (String) habilidades.nextElement();
            iValor = Integer.valueOf((String) habilidadesHT.get(sClave));
            iValor = Utilidades.Random(20, iValor);
            habilidad.setIdMonje(monje.getIdDeMonje());
            habilidad.setIdHabilidad(getIDHabilidad(sClave));
            if (habilidad.getIdHabilidad() == Constantes.HABILIDAD_FE) {
                habilidad.setValorActual(50);
                habilidad.setValorInicial(0);
            } else {
                habilidad.setValorActual(iValor);
                habilidad.setValorInicial(iValor);
            }

            //habilidadesAD = new adHabilidades(con);
            if (!existeHabilidad(monje.getIdDeMonje(), habilidad.getIdHabilidad())) {
                crearHabilidad(habilidad);
            } else actualizarHabilidad(monje.getIdDeMonje(), habilidad.getIdHabilidad(), iValor);
            //habilidadesAD.finalize();
        }

    }

    public int getIDHabilidad(String sClave) {
        if (sClave.equalsIgnoreCase("idioma")) {
            return Constantes.HABILIDAD_IDIOMA;
        } else if (sClave.equalsIgnoreCase("talento")) {
            return Constantes.HABILIDAD_TALENTO;
        } else if (sClave.equalsIgnoreCase("sabiduria")) {
            return Constantes.HABILIDAD_SABIDURIA;
        } else if (sClave.equalsIgnoreCase("fe")) {
            return Constantes.HABILIDAD_FE;
        } else if (sClave.equalsIgnoreCase("fuerza")) {
            return Constantes.HABILIDAD_FUERZA;
        } else if (sClave.equalsIgnoreCase("carisma")) {
            return Constantes.HABILIDAD_CARISMA;
        } else //if(sClave.equalsIgnoreCase("destreza"))
        {
            return Constantes.HABILIDAD_DESTREZA;
        }
    }

}
