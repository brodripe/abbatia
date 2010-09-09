package org.abbatia.adbean;

import org.abbatia.adbean.base.adbeans;
import org.abbatia.bean.Alimento;
import org.abbatia.bean.AlimentoLote;
import org.abbatia.bean.LoteCantidad;
import org.abbatia.bean.Usuario;
import org.abbatia.core.CoreTiempo;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.CantidadInsuficienteException;
import org.abbatia.exception.ProductoNoEncontradoException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.Utilidades;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;


public class adAlimentoLotes extends adbeans {
    private static Logger log = Logger.getLogger(adAlimentoLotes.class.getName());

    /**
     * Instancia la clase sin obtener una nueva conexión
     *
     * @param con
     * @throws AbadiaException
     */
    public adAlimentoLotes(Connection con) throws AbadiaException {
        super(con);
    }


    //recupera el objeto Edificio cargado...
    public AlimentoLote recuperarAlimentoLote(long idDeAlimentoLote) throws AbadiaException {
        //Definición de cadena sql de consulta
        String sSQL = "SELECT *, af.descripcion as descfam, at.saladoid " +
                "FROM `alimentos` as a, `alimentos_familia` as af, `alimentos_lote` as al, `alimentos_tipo` as at " +
                "WHERE a.alimentoid = at.alimentoid AND al.loteid = a.loteid AND at.familiaid = af.familiaid " +
                "AND a.LoteID = ? ";

        //creo un objeto de tipo Edificio
        AlimentoLote alimento = new AlimentoLote();
        //creo un objeto de tipo PreparedStatement sobre el que se
        //ejecutara la consulta
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            //asigno el parametro para el filtrado de la consulta
            //esta instruccion sustituye el simbolo ? por la clave.

            int parNo = 1;
            ps.setLong(parNo, idDeAlimentoLote);
            //Lanzo la consulta y cargo el resultado en un resultset
            rs = ps.executeQuery();
            //si la consulta encuentra la edificio....
            if (rs.next()) {
                //iniciamos el volcado de datos sobre
                //el objeto edificio.

                alimento.setIdLote(rs.getInt("LOTEID"));
                alimento.setIdAlimento(rs.getInt("ALIMENTOID"));
                alimento.setIdEdificio(rs.getInt("EDIFICIOID"));
                alimento.setIdAlimentoSalado(rs.getInt("SALADOID"));
                alimento.setIdFamilia(rs.getInt("FAMILIAID"));
                alimento.setDiasVidas(rs.getInt("DIAS_VIDAS"));
                alimento.setEstado(rs.getInt("ESTADO"));
                alimento.setConsumo_monje(rs.getFloat("CONSUMO_MONJE"));
                alimento.setVolumen_unidad(rs.getFloat("VOLUMEN"));
                alimento.setCantidad(rs.getInt("CANTIDAD"));
                alimento.setCantidadS(Utilidades.redondear(rs.getInt("CANTIDAD")));
                alimento.setDescripcion(rs.getString("DESCRIPCION"));
                alimento.setFamiliaDescripcion(rs.getString("descfam"));
                alimento.setUnidad_medida(rs.getInt("UNIDAD_MEDIDA"));
                alimento.setHidratos_carbono(rs.getInt("HIDRATOS_CARBONO"));
                alimento.setLipidos(rs.getInt("LIPIDOS"));
                alimento.setProteinas(rs.getInt("PROTEINAS"));
                alimento.setFechaEntrada(rs.getString("FECHA_ENTRADA"));
                alimento.setFechaCaducidad(rs.getString("FECHA_CADUCIDAD"));

                return alimento;
            }
            return null;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adAlimentoLotes. recuperarAlimentoLote. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public ArrayList<LoteCantidad> recuperarLotesPorAbadiaFamilia(int idAbadia, int idFamilia) throws AbadiaException {
        String sSQLAlimen = "SELECT al.loteid, al.cantidad  " +
                "FROM `edificio` e, `alimentos` a, `alimentos_lote` al, `alimentos_tipo` at " +
                "where e.abadiaid = ? and a.edificioid = e.edificioid and al.loteid = a.loteid and at.alimentoid = a.alimentoid " +
                "and cantidad <> 0 and at.familiaid =? " +
                "order by fecha_caducidad, cantidad ";

        ArrayList<LoteCantidad> alLotes = new ArrayList<LoteCantidad>();
        LoteCantidad lote;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQLAlimen);
            ps.setInt(1, idAbadia);
            ps.setInt(2, idFamilia);
            rs = ps.executeQuery();
            while (rs.next()) {
                lote = new LoteCantidad(rs.getInt("loteid"), rs.getDouble("cantidad"));
                alLotes.add(lote);
            }
            return alLotes;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adAlimentoLotes. recuperarLotesPorAbadiaFamilia. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public ArrayList<AlimentoLote> recuperarAlimentoEdificio(long idDeEdificio) throws AbadiaException {

        String sSQL = "SELECT *, at.descripcion as descali, af.descripcion as descfam " +
                "FROM `alimentos` as a, `alimentos_familia` as af, `alimentos_lote` as al, `alimentos_tipo` as at " +
                "WHERE a.alimentoid = at.alimentoid AND al.loteid = a.loteid AND at.familiaid = af.familiaid " +
                "AND a.EdificioID = ? order by FECHA_CADUCIDAD";

        AlimentoLote alimento;
        ArrayList<AlimentoLote> al = new ArrayList<AlimentoLote>();

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);

            int parNo = 1;
            ps.setLong(parNo, idDeEdificio);
            //Lanzo la consulta y cargo el resultado en un resultset
            rs = ps.executeQuery();
            //si la consulta encuentra la edificio....
            while (rs.next()) {
                alimento = new AlimentoLote();

                alimento.setIdLote(rs.getInt("LOTEID"));
                alimento.setIdAlimento(rs.getInt("ALIMENTOID"));
                alimento.setIdEdificio(rs.getInt("EDIFICIOID"));
                alimento.setIdFamilia(rs.getInt("FAMILIAID"));
                alimento.setDiasVidas(rs.getInt("DIAS_VIDAS"));
                alimento.setEstado(rs.getInt("ESTADO"));
                alimento.setConsumo_monje(rs.getFloat("CONSUMO_MONJE"));
                alimento.setVolumen_unidad(rs.getFloat("VOLUMEN"));
                alimento.setCantidad(rs.getInt("CANTIDAD"));
                alimento.setCantidadS(Utilidades.redondear(rs.getInt("CANTIDAD")));
                alimento.setDescripcion(rs.getString("descali"));
                alimento.setFamiliaDescripcion(rs.getString("descfam"));
                alimento.setUnidad_medida(rs.getInt("UNIDAD_MEDIDA"));
                alimento.setHidratos_carbono(rs.getInt("HIDRATOS_CARBONO"));
                alimento.setLipidos(rs.getInt("LIPIDOS"));
                alimento.setProteinas(rs.getInt("PROTEINAS"));
                alimento.setFechaEntrada(Utilidades.formatStringFromDB(rs.getString("FECHA_ENTRADA")));
                alimento.setFechaCaducidad(Utilidades.formatStringFromDB(rs.getString("FECHA_CADUCIDAD")));

                al.add(alimento);
            }
            return al;
            //si la abbatia no se localiza, devolveremos una excepci�n
        } catch (SQLException e) {
            throw new AbadiaSQLException("adAlimentoLotes. recuperarAlimentoEdificio. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public ArrayList<AlimentoLote> recuperarAlimentosPorID(long idAlimento, long idEdificio) throws AbadiaException {

        String sSQL = "SELECT *, at.descripcion as descali, af.descripcion as descfam, at.saladoid " +
                "FROM `alimentos` as a, `alimentos_familia` as af, `alimentos_lote` as al, `alimentos_tipo` as at " +
                "WHERE a.alimentoid = at.alimentoid AND al.loteid = a.loteid AND at.familiaid = af.familiaid " +
                "AND a.EdificioID = ? and a.alimentoid = ? order by FECHA_CADUCIDAD";

        AlimentoLote alimento;
        ArrayList<AlimentoLote> al = new ArrayList<AlimentoLote>();

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);

            int parNo = 1;
            ps.setLong(parNo++, idEdificio);
            ps.setLong(parNo, idAlimento);
            //Lanzo la consulta y cargo el resultado en un resultset
            rs = ps.executeQuery();
            //si la consulta encuentra la edificio....
            while (rs.next()) {
                alimento = new AlimentoLote();

                alimento.setIdLote(rs.getInt("LOTEID"));
                alimento.setIdAlimento(rs.getInt("ALIMENTOID"));
                alimento.setIdAlimentoSalado(rs.getInt("SALADOID"));
                alimento.setIdEdificio(rs.getInt("EDIFICIOID"));
                alimento.setIdFamilia(rs.getInt("FAMILIAID"));
                alimento.setDiasVidas(rs.getInt("DIAS_VIDAS"));
                alimento.setEstado(rs.getInt("ESTADO"));
                alimento.setConsumo_monje(rs.getFloat("CONSUMO_MONJE"));
                alimento.setVolumen_unidad(rs.getFloat("VOLUMEN"));
                alimento.setCantidad(rs.getDouble("CANTIDAD"));
                alimento.setCantidadS(Utilidades.redondear(rs.getDouble("CANTIDAD")));
                alimento.setDescripcion(rs.getString("descali"));
                alimento.setFamiliaDescripcion(rs.getString("descfam"));
                alimento.setUnidad_medida(rs.getInt("UNIDAD_MEDIDA"));
                alimento.setHidratos_carbono(rs.getInt("HIDRATOS_CARBONO"));
                alimento.setLipidos(rs.getInt("LIPIDOS"));
                alimento.setProteinas(rs.getInt("PROTEINAS"));
                alimento.setFechaEntrada(Utilidades.formatStringFromDB(rs.getString("FECHA_ENTRADA")));
                alimento.setFechaCaducidad(Utilidades.formatStringFromDB(rs.getString("FECHA_CADUCIDAD")));

                al.add(alimento);
            }
            return al;
            //si la abbatia no se localiza, devolveremos una excepci�n
        } catch (SQLException e) {
            throw new AbadiaSQLException("adAlimentoLotes. recuperarAlimentosPorID. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public ArrayList<AlimentoLote> recuperarAlimentosPorIDAbadia(long idAlimento, int idAbadia) throws AbadiaException {

        String sSQL = "SELECT *, at.descripcion as descali, af.descripcion as descfam " +
                "FROM `alimentos` as a, `alimentos_familia` as af, `alimentos_lote` as al, `alimentos_tipo` as at, edificio as e " +
                "WHERE a.alimentoid = at.alimentoid AND al.loteid = a.loteid AND at.familiaid = af.familiaid " +
                "AND a.EdificioID = e.edificioid and a.alimentoid = ? and e.abadiaid = ? " +
                "order by FECHA_CADUCIDAD";

        AlimentoLote alimento;
        ArrayList<AlimentoLote> al = new ArrayList<AlimentoLote>();

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);

            int parNo = 1;
            ps.setLong(parNo++, idAlimento);
            ps.setLong(parNo, idAbadia);
            //Lanzo la consulta y cargo el resultado en un resultset
            rs = ps.executeQuery();
            //si la consulta encuentra la edificio....
            while (rs.next()) {
                alimento = new AlimentoLote();

                alimento.setIdLote(rs.getInt("LOTEID"));
                alimento.setIdAlimento(rs.getInt("ALIMENTOID"));
                alimento.setIdEdificio(rs.getInt("EDIFICIOID"));
                alimento.setIdFamilia(rs.getInt("FAMILIAID"));
                alimento.setDiasVidas(rs.getInt("DIAS_VIDAS"));
                alimento.setEstado(rs.getInt("ESTADO"));
                alimento.setConsumo_monje(rs.getFloat("CONSUMO_MONJE"));
                alimento.setVolumen_unidad(rs.getFloat("VOLUMEN"));
                alimento.setCantidad(rs.getDouble("CANTIDAD"));
                alimento.setCantidadS(Utilidades.redondear(rs.getDouble("CANTIDAD")));
                alimento.setDescripcion(rs.getString("descali"));
                alimento.setFamiliaDescripcion(rs.getString("descfam"));
                alimento.setUnidad_medida(rs.getInt("UNIDAD_MEDIDA"));
                alimento.setHidratos_carbono(rs.getInt("HIDRATOS_CARBONO"));
                alimento.setLipidos(rs.getInt("LIPIDOS"));
                alimento.setProteinas(rs.getInt("PROTEINAS"));
                alimento.setFechaEntrada(Utilidades.formatStringFromDB(rs.getString("FECHA_ENTRADA")));
                alimento.setFechaCaducidad(Utilidades.formatStringFromDB(rs.getString("FECHA_CADUCIDAD")));

                al.add(alimento);
            }
            return al;
            //si la abbatia no se localiza, devolveremos una excepci�n
        } catch (SQLException e) {
            throw new AbadiaSQLException("adAlimentoLotes. recuperarAlimentoEdificio. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }


    public ArrayList<AlimentoLote> recuperarAlimentoEdificioAgrupados(long idDeEdificio, Usuario usuario) throws AbadiaException {
        String sSQL = "SELECT sum(al.cantidad) as CANTIDAD, a.ALIMENTOID, l1.literal as descali, l2.literal as descfam, min(fecha_caducidad) as caduca_min, max(fecha_caducidad) as caduca_max, at.saladoid " +
                "FROM `alimentos` as a, `alimentos_familia` as af, `alimentos_lote` as al, `alimentos_tipo` as at, literales l1, literales l2 " +
                "WHERE a.alimentoid = at.alimentoid AND al.loteid = a.loteid AND at.familiaid = af.familiaid " +
                "AND l1.idiomaid = ? AND l2.idiomaid = ? AND l1.literalid = at.literalid AND l2.literalid = af.literalid " +
                "AND a.EdificioID = ? " +
                "group by a.ALIMENTOID, l1.literal, l2.literal, at.saladoid " +
                "order by FECHA_CADUCIDAD";

        AlimentoLote alimento = null;
        ArrayList<AlimentoLote> al = new ArrayList<AlimentoLote>();

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);

            int parNo = 1;
            ps.setInt(parNo++, usuario.getIdDeIdioma());
            ps.setInt(parNo++, usuario.getIdDeIdioma());
            ps.setLong(parNo, idDeEdificio);
            //Lanzo la consulta y cargo el resultado en un resultset
            rs = ps.executeQuery();
            //si la consulta encuentra la edificio....
            while (rs.next()) {
                alimento = new AlimentoLote();

                alimento.setIdAlimento(rs.getInt("ALIMENTOID"));
                alimento.setIdAlimentoSalado(rs.getInt("SALADOID"));
                alimento.setCantidad(rs.getInt("CANTIDAD"));
                alimento.setCantidadS(Utilidades.redondear(rs.getInt("CANTIDAD")));
                alimento.setDescripcion(rs.getString("descali"));
                alimento.setFamiliaDescripcion(rs.getString("descfam"));
                alimento.setFechaCaducidad_desde(Utilidades.formatStringFromDB(rs.getString("caduca_min")));
                alimento.setFechaCaducidad_hasta(Utilidades.formatStringFromDB(rs.getString("caduca_max")));

                al.add(alimento);
            }
            return al;
            //si la abbatia no se localiza, devolveremos una excepci�n
        } catch (SQLException e) {
            throw new AbadiaSQLException("adAlimentoLotes. recuperarAlimentoEdificioAgrupados. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public ArrayList<AlimentoLote> recuperarAlimentoSalables(long idDeEdificio, Usuario usuario) throws AbadiaException {

        String sSQL = "SELECT al.loteid, al.cantidad as CANTIDAD, a.ALIMENTOID, l1.literal as descali, l2.literal as descfam, fecha_caducidad, at.saladoid " +
                "FROM `alimentos` as a, `alimentos_familia` as af, `alimentos_lote` as al, `alimentos_tipo` as at, literales l1, literales l2 " +
                "WHERE a.alimentoid = at.alimentoid AND al.loteid = a.loteid AND at.familiaid = af.familiaid " +
                "AND l1.idiomaid = ? AND l2.idiomaid = ? AND l1.literalid = at.literalid AND l2.literalid = af.literalid " +
                "AND a.EdificioID = ? " +
                "order by descali, descfam, FECHA_CADUCIDAD";

        AlimentoLote alimento = null;
        ArrayList<AlimentoLote> al = new ArrayList<AlimentoLote>();

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);

            int parNo = 1;
            ps.setInt(parNo++, usuario.getIdDeIdioma());
            ps.setInt(parNo++, usuario.getIdDeIdioma());
            ps.setLong(parNo, idDeEdificio);
            //Lanzo la consulta y cargo el resultado en un resultset
            rs = (ResultSet) ps.executeQuery();
            //si la consulta encuentra la edificio....
            while (rs.next()) {
                alimento = new AlimentoLote();

                alimento.setIdLote(rs.getInt("LOTEID"));
                alimento.setIdAlimento(rs.getInt("ALIMENTOID"));
                alimento.setIdAlimentoSalado(rs.getInt("SALADOID"));
                alimento.setCantidad(rs.getInt("CANTIDAD"));
                alimento.setCantidadS(Utilidades.redondear(rs.getInt("CANTIDAD")));
                alimento.setDescripcion(rs.getString("descali"));
                alimento.setFamiliaDescripcion(rs.getString("descfam"));
                alimento.setFechaCaducidad(Utilidades.formatStringFromDB(rs.getString("fecha_caducidad")));

                al.add(alimento);
            }
            return al;
            //si la abbatia no se localiza, devolveremos una excepci�n
        } catch (SQLException e) {
            throw new AbadiaSQLException("adAlimentoLotes. recuperarAlimentoSalables. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }


    public int crearAlimentoLote(AlimentoLote alimentolote) throws AbadiaException {
        String sSQLLote = "Insert Into alimentos_lote ( FECHA_ENTRADA, FECHA_CADUCIDAD, ESTADO, CANTIDAD) Values (";
        String sSQLAlim = "Insert Into alimentos ( ALIMENTOID, EDIFICIOID, LOTEID) Values (?, ?, ?)";

        int LoteID = -1;
        Statement stmt = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            stmt = con.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
                    java.sql.ResultSet.CONCUR_UPDATABLE);

            sSQLLote = sSQLLote + "'" + alimentolote.getFechaEntrada() + "', ";
            sSQLLote = sSQLLote + "'" + alimentolote.getFechaCaducidad() + "', ";
            sSQLLote = sSQLLote + "'" + alimentolote.getEstado() + "', ";
            sSQLLote = sSQLLote + "'" + alimentolote.getCantidad() + "' ) ";

            stmt.executeUpdate(sSQLLote, Statement.RETURN_GENERATED_KEYS);
            rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                LoteID = rs.getInt(1);
                alimentolote.setIdLote(LoteID);
            } else {
                return -1;
            }

            rs.close();


            ps = con.prepareStatement(sSQLAlim);
            //asigno los valores
            int parNo = 1;
            ps.setInt(parNo++, alimentolote.getIdAlimento());
            ps.setInt(parNo++, alimentolote.getIdEdificio());
            ps.setInt(parNo, alimentolote.getIdLote());
            // Ejecutarlo
            ps.execute();
            return LoteID;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adAlimentoLotes. crearAlimentoLote. SQLException. ", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
            DBMSUtils.cerrarObjetoSQL(stmt);
        }
    }

    //Modificar cantidad alimento lote
    public void ModificarCantidad(long idDeLoteTmp, double cantidad) throws AbadiaException {
        String sSQLLote = "UPDATE alimentos_lote SET Cantidad = ? Where `LOTEID` = ?";

        // Lote
        PreparedStatement ps = null;
        AlimentoLote alimento;
        try {
            ps = con.prepareStatement(sSQLLote);
            int parNo = 1;
            ps.setDouble(parNo++, cantidad);
            ps.setLong(parNo, idDeLoteTmp);
            ps.execute();

            // Comprobar si la cantidad est� a 0, si lo est�, borrarlo
            alimento = recuperarAlimentoLote(idDeLoteTmp);
            if (alimento != null) {
                if (alimento.getCantidad() <= 0) eliminarAlimentoLote(idDeLoteTmp);
            }
        } catch (SQLException e) {
            throw new AbadiaSQLException("adAlimentosLotes. ModificarCantidad. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    //Modificar cantidad alimento lote
    public void modificarCantidadLote(long idDeLoteTmp, double cantidad) throws AbadiaException {
        String sSQLLote = "UPDATE alimentos_lote SET Cantidad = " + cantidad + " Where `LOTEID` = " + idDeLoteTmp;
        adUtils utils = new adUtils(con);
        utils.execSQL(sSQLLote);
    }

    /**
     * Modificar tipo de alimento de un lote
     *
     * @param idDeLoteTmp
     * @param idTipoAlimento
     * @throws AbadiaException
     */
    public void modificarTipoAlimentoLote(long idDeLoteTmp, int idTipoAlimento) throws AbadiaException {
        String sSQLLote = "UPDATE alimentos SET alimentoid = " + idTipoAlimento + " Where `LOTEID` = " + idDeLoteTmp;
        adUtils utils = new adUtils(con);
        utils.execSQL(sSQLLote);
    }

    /**
     * Modificar tipo de alimento de un lote
     *
     * @param idDeLoteTmp
     * @param fechaCaducidad
     * @throws AbadiaException
     */
    public void modificarCaducidadLote(long idDeLoteTmp, String fechaCaducidad) throws AbadiaException {
        String sSQLLote = "UPDATE alimentos_lote SET fecha_caducidad = '" + fechaCaducidad + "' Where `LOTEID` = " + idDeLoteTmp;
        adUtils utils = new adUtils(con);
        utils.execSQL(sSQLLote);
    }


    //Sumar cantidad alimento lote
    public void sumarEnLote(long idDeLoteTmp, double cantidad) throws AbadiaSQLException {
        String sSQLLote = "UPDATE alimentos_lote SET Cantidad = Cantidad + ? Where `LOTEID` = ?";

        // Lote
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQLLote);
            int parNo = 1;
            ps.setDouble(parNo++, cantidad);
            ps.setLong(parNo, idDeLoteTmp);
            ps.execute();

        } catch (SQLException e) {
            throw new AbadiaSQLException("adAlimentosLotes. sumarEnLote. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    //Restar cantidad alimento lote
    public void restarEnLote(long idDeLoteTmp, double cantidad) throws AbadiaException {
        String sSQLLote = "UPDATE alimentos_lote SET Cantidad = Cantidad - ? Where `LOTEID` = ?";

        // Lote
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQLLote);
            int parNo = 1;
            ps.setDouble(parNo++, cantidad);
            ps.setLong(parNo, idDeLoteTmp);
            ps.execute();

            // Comprobar si la cantidad est� a 0, si lo est�, borrarlo
            AlimentoLote alimento;
            alimento = recuperarAlimentoLote(idDeLoteTmp);
            if (alimento != null) {
                if (alimento.getCantidad() <= 0) eliminarAlimentoLote(idDeLoteTmp);
            }

        } catch (SQLException e) {
            throw new AbadiaSQLException("adAlimentosLotes. restarEnLote. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }


    //elimina un objeto Alimento Lote
    public boolean eliminarAlimentoLote(AlimentoLote alimentolote) throws AbadiaException {
        return eliminarAlimentoLote(alimentolote.getIdLote());
    }

    //Elimina un alimento lote
    public boolean eliminarAlimentoLote(long idDeLoteTmp) throws AbadiaException {
        String sSQLLote = "Delete From alimentos_lote Where `LOTEID` = ?";
        String sSQLAlim = "Delete From alimentos Where `LOTEID` = ?";

        PreparedStatement ps = null;
        try {
            // Lote
            ps = con.prepareStatement(sSQLLote);
            int parNo = 1;
            ps.setLong(parNo, idDeLoteTmp);
            ps.execute();

            // Alimento
            ps = con.prepareStatement(sSQLAlim);
            parNo = 1;
            ps.setLong(parNo, idDeLoteTmp);
            return ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adAlimentosLotes. eliminarAlimentoLote. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    //Elimina un alimento lote
    public void eliminarAlimentoLoteS(long idDeLoteTmp) throws AbadiaException {
        String sSQLLote = "Delete From alimentos_lote Where `LOTEID` = " + idDeLoteTmp;
        String sSQLAlim = "Delete From alimentos Where `LOTEID` = " + idDeLoteTmp;
        adUtils utils = new adUtils(con);
        utils.execSQL(sSQLLote);
        utils.execSQL(sSQLAlim);
    }

    //Modificar cantidad alimento lote
    public int recuperarCaducidad(int idAlimento) throws AbadiaException {
        String sSQLLote = "Select af.DIAS_VIDAS FROM alimentos_familia af, alimentos_tipo at where af.familiaid= at.familiaid and at.alimentoid = ?";

        int iDias = 0;
        // Lote
        adAlimentos oAlimentosAD;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            oAlimentosAD = new adAlimentos(con);
            iDias = oAlimentosAD.recuperarDiasCaducidadProductoSalado(idAlimento);
            ps = con.prepareStatement(sSQLLote);
            int parNo = 1;
            ps.setInt(parNo, idAlimento);
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("DIAS_VIDAS") + iDias;
            } else return 100;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adAlimentosLotes. ModificarCantidad. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public Alimento recuperarDatosAlimento(int idAlimento, Usuario usuario) throws AbadiaException {
        String sSQLLote = "Select at.DESCRIPCION as descali, af.DESCRIPCION as descfam  " +
                "FROM alimentos_tipo at, alimentos_familia af " +
                "where alimentoid = ? AND at.familiaid = af.familiaid";

        adMercadoVenta mercado;

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQLLote);
            int parNo = 1;
            ps.setInt(parNo, idAlimento);
            rs = ps.executeQuery();

            if (rs.next()) {
                Alimento alimento = new Alimento();
                alimento.setIdAlimento(idAlimento);
                alimento.setDescAli(rs.getString("descali"));
                alimento.setDescFam(rs.getString("descfam"));
                mercado = new adMercadoVenta(con);
                alimento.setPrecioMercado(Utilidades.redondear(mercado.getPrecioMercadoPorAlimento(idAlimento)));
                return alimento;
            } else
                throw new ProductoNoEncontradoException(usuario.getNick() + " - adAlimentosLotes. recuperarDatosAlimento. No se ha encontrado el idAlimento: " + idAlimento, null, log);

        } catch (SQLException e) {
            throw new AbadiaSQLException("adAlimentosLotes. recuperarDatosAlimento. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }


    public double recuperarVolumen(int idAlimento) throws AbadiaSQLException {
        String sSQLLote = "Select volumen From alimentos_tipo where alimentoid = ?";

        // Lote
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQLLote);
            int parNo = 1;
            ps.setInt(parNo, idAlimento);
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble("volumen");
            } else return 100;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adAlimentosLotes. recuperarVolumen. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public void eliminarAlimentosAbadia(long idAbadia) throws AbadiaException {
        String sSQL = "Delete From alimentos where edificioid in (select edificioid from edificio where ABADIAID = ?)";

        PreparedStatement ps = null;
        try {
            // Lote
            ps = con.prepareStatement(sSQL);
            int parNo = 1;
            ps.setLong(parNo, idAbadia);
            ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adAlimentosLotes. eliminarAlimentosAbadia. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public void eliminarAlimentosLotesAbadia(long idAbadia) throws AbadiaException {
        String sSQL = "Delete From alimentos_lote Where LOTEID in (select LOTEID from alimentos, edificio WHERE edificio.ABADIAID = ? and alimentos.edificioid = edificio.edificioid)";

        PreparedStatement ps = null;
        try {
            // Lote
            ps = con.prepareStatement(sSQL);
            int parNo = 1;
            ps.setLong(parNo, idAbadia);
            ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adAlimentosLotes. eliminarAlimentosLotesAbadia. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public Alimento recuperarDatosAlimentoLote(int idAlimento, int idAbadia, int idIdioma) throws AbadiaException {
        String sSQLLote = "SELECT sum(al.cantidad) as cantidad, l.literal " +
                "FROM alimentos a, alimentos_lote al, edificio e, literales l, alimentos_tipo at " +
                "WHERE al.loteid = a.loteid and a.alimentoid = ? and e.edificioid = a.edificioid and e.abadiaid=? and at.literalid = l.literalid " +
                "and a.alimentoid = at.alimentoid and l.idiomaid = ? " +
                "group by l.literalid";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQLLote);
            int parNo = 1;
            ps.setInt(parNo++, idAlimento);
            ps.setInt(parNo++, idAbadia);
            ps.setInt(parNo, idIdioma);
            rs = ps.executeQuery();

            if (rs.next()) {
                Alimento alimento = new Alimento();
                alimento.setIdAlimento(idAlimento);
                alimento.setCantidad(rs.getDouble("cantidad"));
                alimento.setDescAli(rs.getString("literalid"));
                return alimento;
            } else return null;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adAlimentosLotes. recuperarDatosAlimento. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public Alimento recuperarCantidadAlimentoId(int idAlimento, int idAbadia) throws AbadiaException {
        String sSQLLote = "SELECT sum(al.cantidad) as cantidad " +
                "FROM alimentos a, alimentos_lote al, edificio e " +
                "WHERE al.loteid = a.loteid and a.alimentoid = ? and e.edificioid = a.edificioid and e.abadiaid=? ";


        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQLLote);
            int parNo = 1;
            ps.setInt(parNo++, idAlimento);
            ps.setInt(parNo, idAbadia);
            rs = ps.executeQuery();

            if (rs.next()) {
                Alimento alimento = new Alimento();
                alimento.setIdAlimento(idAlimento);
                alimento.setCantidad(rs.getDouble("cantidad"));
                return alimento;
            } else return null;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adAlimentosLotes. recuperarCantidadAlimentoId. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public void restarLotesPorAlimentoId(int idAbadia, int idAlimento, double cantidad) throws AbadiaException {
        ArrayList<AlimentoLote> alimentosAL = this.recuperarAlimentosPorIDAbadia(idAlimento, idAbadia);
        Iterator<AlimentoLote> alimentos = alimentosAL.iterator();
        AlimentoLote alimento;
        boolean restaCompleta = false;
        while (alimentos.hasNext() && !restaCompleta) {
            alimento = alimentos.next();
            restarEnLote(alimento.getIdLote(), cantidad);
            //si la cantidad del lote superaba la cantidad necesaria..
            if (alimento.getCantidad() >= cantidad) {
                restaCompleta = true;
            } else {
                cantidad = cantidad - alimento.getCantidad();
                restaCompleta = false;
            }
        }
    }

    /**
     * Sala una cantidad determinada de alimentos
     * * Verifica la disponibilidad de sal
     * * Si se desea salar la totalidad del lote, simplemente se actualiza el tipo de alimento del lote y su fecha de caducidad
     * de lo contrario..
     * * Crea un nuevo lote con la cantidad de alimento salado
     * * Decrementa la cantidad a salar del lote original
     *
     * @param alimentoLote
     * @param cantidad
     * @param idAbadia
     * @throws AbadiaException
     */
    public void salarAlimento(AlimentoLote alimentoLote, double cantidad, int idAbadia) throws AbadiaException {
        adAlimentoLotes oAlimentoLotesAD;
        adRecurso oRecursoAD;
        adAlimentos oAlimentosAD;
        String sFechaActualizada = "";
        //recuperamos la sal disponible en la abadía
        oRecursoAD = new adRecurso(con);
        double dSalTotal = oRecursoAD.recuperarValorRecurso(Constantes.RECURSOS_SAL, idAbadia);
        //recuperamos la cantidad de sal necesaria para el alimento
        oAlimentosAD = new adAlimentos(con);
        double dSalNecesaria = oAlimentosAD.recuperarCantidadSalNecesaria(alimentoLote.getIdAlimentoSalado(), cantidad);
        //recuperamo el número de días en los que la fecha de caducidad se incrementará
        int iDias = oAlimentosAD.recuperarDiasCaducidadProductoSalado(alimentoLote.getIdAlimentoSalado());
        //si no se dispone de la sal necesaria..
        if (dSalTotal < dSalNecesaria) {
            throw new CantidadInsuficienteException("adAlimentoLotes. salarAlimento. No se dispone de la sal necesaria", log);
        }
        if (alimentoLote.getCantidad() < cantidad) {
            throw new CantidadInsuficienteException("adAlimentoLotes. salarAlimento. No dispones de tanta cantidad de alimento", log);
        }

        sFechaActualizada = CoreTiempo.sumarDias(Utilidades.formatStringToDB(alimentoLote.getFechaCaducidad()), iDias);
        oAlimentoLotesAD = new adAlimentoLotes(con);
        //si la cantidad de alimento a salar coincide con el total de lote...
        if (alimentoLote.getCantidad() == cantidad) {
            oAlimentoLotesAD.modificarCaducidadLote(alimentoLote.getIdLote(), sFechaActualizada);
            oAlimentoLotesAD.modificarTipoAlimentoLote(alimentoLote.getIdLote(), alimentoLote.getIdAlimentoSalado());
        } else //la cantidad no es la misma
        {
            //restamos la cantidad del lote original
            oAlimentoLotesAD.modificarCantidadLote(alimentoLote.getIdLote(), alimentoLote.getCantidad() - cantidad);
            //creamos un nuevo lote con la cantidad salada, el nuevo idalimento y la nueva fecha de caducidad
            alimentoLote.setFechaCaducidad(sFechaActualizada);
            alimentoLote.setIdAlimento(alimentoLote.getIdAlimentoSalado());
            alimentoLote.setCantidad(cantidad);
            oAlimentoLotesAD.crearAlimentoLote(alimentoLote);
        }

        //restar resurso sal
        oRecursoAD.restarRecurso(Constantes.RECURSOS_SAL, idAbadia, dSalNecesaria);
    }

}
