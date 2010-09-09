package org.abbatia.adbean;

import org.abbatia.actionform.RecursosPorFamilia;
import org.abbatia.adbean.base.adbeans;
import org.abbatia.bean.*;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Utilidades;
import org.abbatia.utils.Constantes;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class adAlimentos extends adbeans {
    private static Logger log = Logger.getLogger(adAlimentos.class.getName());

    /**
     * Instancia la clase sin obtener una nueva conexión
     *
     * @param con
     * @throws AbadiaException
     */
    public adAlimentos(Connection con) throws AbadiaException {
        super(con);
    }


    // Devuelve una lista de alimentos
    public ArrayList<Table> getListaTipos(int idiomaid) throws AbadiaException {
        String sSQL = "SELECT af.familiaid, l.literal FROM alimentos_familia af, literales l where af.literalid = l.literalid AND l.idiomaid = ? order by literal";
        ArrayList<Table> Actividades = new ArrayList<Table>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idiomaid);
            rs = ps.executeQuery();
            Table actividad;
            while (rs.next()) {
                actividad = new Table(rs.getInt("FAMILIAID"), rs.getString("LITERAL"));
                Actividades.add(actividad);
            }
            return Actividades;
            // Ejecutarlo
        } catch (SQLException e) {
            throw new AbadiaSQLException("adAlimentos. getListaTipos", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public ArrayList<AlimentoLote> recuperarAlimentosPorFamilia(Abadia abadia, Usuario usuario, int familia) throws AbadiaException {
        String sSQL = "SELECT a.alimentoid, a.loteid, al.cantidad, l.literal, al.fecha_caducidad, al.estado " +
                "from alimentos a, alimentos_lote al, literales l, edificio e, alimentos_tipo at " +
                "where l.literalid = at.literalid and l.idiomaid = ? and a.alimentoid = at.alimentoid and at.familiaid = ? " +
                "and e.abadiaid = ? and a.edificioid = e.edificioid and al.loteid=a.loteid";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, usuario.getIdDeIdioma());
            ps.setInt(2, familia);
            ps.setLong(3, abadia.getIdDeAbadia());

            rs = ps.executeQuery();

            ArrayList<AlimentoLote> listaAlimentos = new ArrayList<AlimentoLote>();
            AlimentoLote alimento;

            while (rs.next()) {
                alimento = new AlimentoLote();
                alimento.setCantidad(rs.getInt("CANTIDAD"));
                alimento.setIdLote(rs.getInt("LOTEID"));
                alimento.setDescripcion(rs.getString("LITERAL"));
                alimento.setIdAlimento(rs.getInt("ALIMENTOID"));
                alimento.setFechaCaducidad(Utilidades.formatStringFromDB(rs.getString("FECHA_CADUCIDAD")));
                alimento.setEstado(rs.getInt("ESTADO"));
                listaAlimentos.add(alimento);
            }
            return listaAlimentos;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adAlimentos. recuperarAlimentosPorFamilia", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public ArrayList<AlimentoLote> recuperarAlimentosParaMolino(Abadia abadia, Usuario usuario) throws AbadiaException {
        String sSQL = "SELECT a.alimentoid, a.loteid, al.cantidad, l.literal, al.fecha_caducidad, al.estado " +
                " from alimentos a, alimentos_lote al, literales l, edificio e, alimentos_tipo at, molino_tipo mt, edificio e_molino " +
                " where l.literalid = at.literalid and l.idiomaid = ? and a.alimentoid = at.alimentoid and at.alimentoid = mt.productoid " +
                " and e.abadiaid = ? and a.edificioid = e.edificioid  and al.loteid=a.loteid and e_molino.abadiaid = e.abadiaid and e_molino.TIPOEDIFICIOID = " + Constantes.EDIFICIO_MOLINO + " and e_molino.nivel = mt.nivel";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, usuario.getIdDeIdioma());
            ps.setLong(2, abadia.getIdDeAbadia());

            rs = ps.executeQuery();

            ArrayList<AlimentoLote> listaAlimentos = new ArrayList<AlimentoLote>();
            AlimentoLote alimento;

            while (rs.next()) {
                alimento = new AlimentoLote();
                alimento.setCantidad(rs.getInt("CANTIDAD"));
                alimento.setIdLote(rs.getInt("LOTEID"));
                alimento.setDescripcion(rs.getString("LITERAL"));
                alimento.setIdAlimento(rs.getInt("ALIMENTOID"));
                alimento.setFechaCaducidad(Utilidades.formatStringFromDB(rs.getString("FECHA_CADUCIDAD")));
                alimento.setEstado(rs.getInt("ESTADO"));
                listaAlimentos.add(alimento);
            }
            return listaAlimentos;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adAlimentos. recuperarAlimentosPorFamilia", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public String recuperarDescripcionAlimento(int idAlimento, Usuario usuario) throws AbadiaSQLException {
        String sSQLLote = "Select l.literal FROM alimentos_tipo at, literales l where at.alimentoid = ? and at.literalid = l.literalid and l.idiomaid = " + usuario.getIdDeIdioma();

        // Lote
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQLLote);
            int parNo = 1;
            ps.setInt(parNo, idAlimento);
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("literal");
            } else return "";

        } catch (SQLException e) {
            throw new AbadiaSQLException("adAlimentosLotes. recuperarDescripcionAlimento. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public String recuperarDescripcionAlimento(int idAlimento, int idIdioma) throws AbadiaSQLException {
        String sSQL = "Select l.literal FROM alimentos_tipo at, literales l where at.alimentoid = " + idAlimento + " and at.literalid = l.literalid and l.idiomaid = " + idIdioma;

        adUtils utils = null;
        String sDescripcion = "";

        try {
            utils = new adUtils(con);
            sDescripcion = utils.getSQL(sSQL, "nada");
            return sDescripcion;

        } catch (AbadiaException e) {
            throw new AbadiaSQLException("adAlimentosLotes. recuperarDescripcionAlimento. SQLException", e, log);
        } finally {
            //
        }
    }

    public String recuperarUnidadMedidaPorAlimento(int idAlimento, Usuario usuario) throws AbadiaSQLException {
        String sSQLLote = "SELECT um.unidad_desc FROM unidad_medida um, alimentos_tipo at WHERE at.unidad_medida = um.unidad_medida and um.idiomaid=1 and at.alimentoid=?";

        // Lote
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQLLote);
            int parNo = 1;
            ps.setInt(parNo, idAlimento);
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("unidad_desc");
            } else return "";

        } catch (SQLException e) {
            throw new AbadiaSQLException("adAlimentosLotes. recuperarUnidadMedidaPorAlimento. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * recupera la cantidad de sal necesaria para salar la cantidad
     *
     * @param idAlimento
     * @param cantidad
     * @return
     * @throws AbadiaException
     */
    public double recuperarCantidadSalNecesaria(int idAlimento, double cantidad) throws AbadiaException {
        String sSQL = "Select cantidad from alimentos_sal where alimentoid = " + idAlimento;
        adUtils utils;
        double result;

        try {
            utils = new adUtils(con);
            result = utils.getSQL(sSQL, (double) 0);
            return (result * cantidad);
        } finally {
            //
        }

    }

    /**
     * recupera la cantidad de sal necesaria para salar la cantidad
     *
     * @param idAlimento
     * @return
     * @throws AbadiaException
     */
    public int recuperarDiasCaducidadProductoSalado(int idAlimento) throws AbadiaException {
        String sSQL = "Select dias from alimentos_sal where alimentoid = " + idAlimento;
        adUtils utils;
        int result;

        utils = new adUtils(con);
        result = utils.getSQL(sSQL, 0);
        return result;

    }

    /**
     * Carga un ArrayList con los totales de consumo de alimentos (por familia) asociados a los monjes.
     *
     * @param alMonjes
     * @return
     * @throws AbadiaException
     */
    public static HashMap<Integer, Integer> calcularConsumosFamilia(ArrayList alMonjes) throws AbadiaException {
        Monje monje;
        HashMap<Integer, Integer> hmFamilias = new HashMap<Integer, Integer>();
        Iterator itMonjes = alMonjes.iterator();
        Integer iValor = 0;
        while (itMonjes.hasNext()) {
            monje = (Monje) itMonjes.next();
            iValor = hmFamilias.get(Integer.valueOf(monje.getComeFamiliaID1()));
            if (iValor == null) {
                hmFamilias.put((int) monje.getComeFamiliaID1(), 1);
            } else {
                hmFamilias.put((int) monje.getComeFamiliaID1(), iValor + 1);
            }
            iValor = hmFamilias.get(Integer.valueOf(monje.getComeFamiliaID2()));
            if (iValor == null) {
                hmFamilias.put((int) monje.getComeFamiliaID2(), 1);
            } else {
                hmFamilias.put((int) monje.getComeFamiliaID2(), iValor + 1);
            }
            iValor = hmFamilias.get(Integer.valueOf(monje.getComeFamiliaID3()));
            if (iValor == null) {
                hmFamilias.put((int) monje.getComeFamiliaID3(), 1);
            } else {
                hmFamilias.put((int) monje.getComeFamiliaID3(), iValor + 1);
            }
            iValor = hmFamilias.get(Integer.valueOf(monje.getComeFamiliaID4()));
            if (iValor == null) {
                hmFamilias.put((int) monje.getComeFamiliaID4(), 1);
            } else {
                hmFamilias.put((int) monje.getComeFamiliaID4(), iValor + 1);
            }
            iValor = hmFamilias.get(Integer.valueOf(monje.getComeFamiliaID5()));
            if (iValor == null) {
                hmFamilias.put((int) monje.getComeFamiliaID5(), 1);
            } else {
                hmFamilias.put((int) monje.getComeFamiliaID5(), iValor + 1);
            }

        }
        return hmFamilias;

    }

    /**
     * recupera un ArraryList con los datos por familias de los alimentos indicando las cantidades disponibles
     * comparada con las necesarias para la alimentación de los monjes.
     *
     * @param hmFamilias
     * @param abadia
     * @param usuario
     * @return
     * @throws AbadiaException
     */
    public ArrayList<RecursosPorFamilia> recuperarCantidadFamilia(HashMap hmFamilias, Abadia abadia, Usuario usuario) throws AbadiaException {
        String sSQL = "Select alimentos_familia.FAMILIAID, Sum(alimentos_lote.CANTIDAD) TOTAL, literales.LITERAL, alimentos_tipo.CONSUMO_MONJE " +
                "From alimentos, alimentos_lote, alimentos_familia, alimentos_tipo, literales, edificio " +
                "Where alimentos_tipo.FAMILIAID = alimentos_familia.FAMILIAID AND " +
                "alimentos.ALIMENTOID = alimentos_tipo.ALIMENTOID AND " +
                "alimentos.LOTEID = alimentos_lote.LOTEID AND " +
                "alimentos_familia.LITERALID = literales.LITERALID AND " +
                "alimentos.EDIFICIOID = edificio.EDIFICIOID AND " +
                "edificio.ABADIAID = ? AND " +
                "literales.IDIOMAID = ? " +
                "Group By alimentos_familia.FAMILIAID";

        PreparedStatement ps = null;
        ResultSet rs = null;
        int idFamilia;
        Integer iCantidadNecesaria;
        double dNecesario;
        RecursosPorFamilia recurso;
        ArrayList<RecursosPorFamilia> alRecursosFamilia = new ArrayList<RecursosPorFamilia>();

        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, abadia.getIdDeAbadia());
            ps.setInt(2, usuario.getIdDeIdioma());
            rs = ps.executeQuery();
            while (rs.next()) {
                recurso = new RecursosPorFamilia();
                idFamilia = rs.getInt("FAMILIAID");
                iCantidadNecesaria = (Integer) hmFamilias.get(Integer.valueOf(idFamilia));
                if (iCantidadNecesaria == null) {
                    dNecesario = 0;
                } else {
                    dNecesario = iCantidadNecesaria * rs.getDouble("CONSUMO_MONJE");
                    hmFamilias.remove(idFamilia);
                }
                recurso.setIdFamilia(idFamilia);
                recurso.setDescripcion(rs.getString("LITERAL"));
                recurso.setCantidadDisponible(rs.getDouble("TOTAL"));
                recurso.setCantidadDisponibleString(Utilidades.redondear(recurso.getCantidadDisponible()));
                recurso.setCantidadNecesaria(dNecesario);
                recurso.setCantidadNecesariaString(Utilidades.redondear(dNecesario));
                recurso.setDeficid(Utilidades.redondear(recurso.getCantidadDisponible() - recurso.getCantidadNecesaria()));
                alRecursosFamilia.add(recurso);

            }
        } catch (SQLException e) {
            throw new AbadiaSQLException("adAlimentosLotes. recuperarCantidadFamilia. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

        //procesamos las familias que no han sido procesadas por no disponibles.
        sSQL = "Select af.FAMILIAID, Avg(at.CONSUMO_MONJE) as CONSUMO, l.LITERAL " +
                "From alimentos_familia AS af, alimentos_tipo AS at, literales AS l " +
                "Where af.FAMILIAID = at.FAMILIAID AND af.LITERALID = l.LITERALID AND l.IDIOMAID = ? AND af.FAMILIAID = ? " +
                "Group By af.FAMILIAID, l.LITERAL";
        try {
            Iterator itFamilias = hmFamilias.keySet().iterator();

            ps = con.prepareStatement(sSQL);

            while (itFamilias.hasNext()) {
                recurso = new RecursosPorFamilia();
                idFamilia = (Integer) itFamilias.next();
                //Las familias que continúan en la hash son aquellas de las que la abadía no dispone...
                //de modo que debemos recuperar la cantidad necesaria junto con el nombre de cada una.
                ps.setInt(1, usuario.getIdDeIdioma());
                ps.setInt(2, idFamilia);
                rs = ps.executeQuery();
                if (rs.next()) {
                    recurso.setCantidadDisponible(0);
                    recurso.setCantidadDisponibleString(Utilidades.redondear(recurso.getCantidadDisponible()));
                    iCantidadNecesaria = (Integer) hmFamilias.get(Integer.valueOf(idFamilia));
                    if (iCantidadNecesaria == null) {
                        dNecesario = 0;
                    } else {
                        dNecesario = iCantidadNecesaria * rs.getDouble("CONSUMO");
                    }
                    recurso.setCantidadNecesaria(dNecesario);
                    recurso.setCantidadNecesariaString(Utilidades.redondear(dNecesario));
                    recurso.setDescripcion(rs.getString("LITERAL"));
                    recurso.setDeficid(Utilidades.redondear(recurso.getCantidadDisponible() - recurso.getCantidadNecesaria()));
                    alRecursosFamilia.add(recurso);
                }
            }
            return alRecursosFamilia;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adAlimentosLotes. recuperarCantidadFamilia. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * recupera un ArraryList con los datos por familias de los alimentos indicando las cantidades disponibles
     * comparada con las necesarias para la alimentación de los monjes.
     *
     * @param hmFamilias
     * @param abadia
     * @param usuario
     * @return
     * @throws AbadiaException
     */
    public ArrayList<RecursosPorFamilia> recuperarCantidadFamiliaAnimales(HashMap<Integer, Integer> hmFamilias, Abadia abadia, Usuario usuario) throws AbadiaException {
        String sSQL = "Select alimentos_familia.FAMILIAID, Sum(alimentos_lote.CANTIDAD) TOTAL, literales.LITERAL, alimentos_tipo.CONSUMO_MONJE " +
                "From alimentos, alimentos_lote, alimentos_familia, alimentos_tipo, literales, edificio, animales_crecimiento " +
                "Where alimentos_tipo.FAMILIAID = alimentos_familia.FAMILIAID AND " +
                "alimentos.ALIMENTOID = alimentos_tipo.ALIMENTOID AND " +
                "alimentos.LOTEID = alimentos_lote.LOTEID AND " +
                "alimentos_familia.LITERALID = literales.LITERALID AND " +
                "alimentos.EDIFICIOID = edificio.EDIFICIOID AND " +
                "edificio.ABADIAID = ? AND " +
                "literales.IDIOMAID = ? " +
                "Group By alimentos_familia.FAMILIAID";

        PreparedStatement ps = null;
        ResultSet rs = null;
        int idFamilia;
        Integer iCantidadNecesaria;
        double dNecesario;
        RecursosPorFamilia recurso;
        ArrayList<RecursosPorFamilia> alRecursosFamilia = new ArrayList<RecursosPorFamilia>();

        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, abadia.getIdDeAbadia());
            ps.setInt(2, usuario.getIdDeIdioma());
            rs = ps.executeQuery();
            while (rs.next()) {
                recurso = new RecursosPorFamilia();
                idFamilia = rs.getInt("FAMILIAID");
                iCantidadNecesaria = hmFamilias.get(Integer.valueOf(idFamilia));
                if (iCantidadNecesaria == null) {
                    dNecesario = 0;
                } else {
                    dNecesario = iCantidadNecesaria * rs.getDouble("CONSUMO_MONJE");
                    hmFamilias.remove(idFamilia);
                }
                recurso.setIdFamilia(idFamilia);
                recurso.setDescripcion(rs.getString("LITERAL"));
                recurso.setCantidadDisponible(rs.getDouble("TOTAL"));
                recurso.setCantidadDisponibleString(Utilidades.redondear(recurso.getCantidadDisponible()));
                recurso.setCantidadNecesaria(dNecesario);
                recurso.setCantidadNecesariaString(Utilidades.redondear(dNecesario));
                recurso.setDeficid(Utilidades.redondear(recurso.getCantidadDisponible() - recurso.getCantidadNecesaria()));
                alRecursosFamilia.add(recurso);

            }
        } catch (SQLException e) {
            throw new AbadiaSQLException("adAlimentosLotes. recuperarCantidadFamilia. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

        //procesamos las familias que no han sido procesadas por no disponibles.
        sSQL = "Select af.FAMILIAID, Avg(at.CONSUMO_MONJE) as CONSUMO, l.LITERAL " +
                "From alimentos_familia AS af, alimentos_tipo AS at, literales AS l " +
                "Where af.FAMILIAID = at.FAMILIAID AND af.LITERALID = l.LITERALID AND l.IDIOMAID = ? AND af.FAMILIAID = ? " +
                "Group By af.FAMILIAID, l.LITERAL";
        try {
            Iterator<Integer> itFamilias = hmFamilias.keySet().iterator();

            ps = con.prepareStatement(sSQL);

            while (itFamilias.hasNext()) {
                recurso = new RecursosPorFamilia();
                idFamilia = itFamilias.next();
                //Las familias que continúan en la hash son aquellas de las que la abadía no dispone...
                //de modo que debemos recuperar la cantidad necesaria junto con el nombre de cada una.
                ps.setInt(1, usuario.getIdDeIdioma());
                ps.setInt(2, idFamilia);
                rs = ps.executeQuery();
                if (rs.next()) {
                    recurso.setCantidadDisponible(0);
                    recurso.setCantidadDisponibleString(Utilidades.redondear(recurso.getCantidadDisponible()));
                    iCantidadNecesaria = hmFamilias.get(Integer.valueOf(idFamilia));
                    if (iCantidadNecesaria == null) {
                        dNecesario = 0;
                    } else {
                        dNecesario = iCantidadNecesaria * rs.getDouble("CONSUMO");
                    }
                    recurso.setCantidadNecesaria(dNecesario);
                    recurso.setCantidadNecesariaString(Utilidades.redondear(dNecesario));
                    recurso.setDescripcion(rs.getString("LITERAL"));
                    recurso.setDeficid(Utilidades.redondear(recurso.getCantidadDisponible() - recurso.getCantidadNecesaria()));
                    alRecursosFamilia.add(recurso);
                }
            }
            return alRecursosFamilia;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adAlimentosLotes. recuperarCantidadFamilia. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public ArrayList<ConsumoAlimentosFamilia> recuperarConsumosAlimentosFamilia(int p_iAbadiaId, int p_iIdiomaId)
            throws AbadiaException {
        String sSQL = "call recuperarConsumosAlimentoFamilia(?,?)";
        CallableStatement cs = null;
        ResultSet rs = null;

        ConsumoAlimentosFamilia oConsumos;
        ArrayList<ConsumoAlimentosFamilia> alConsumos = new ArrayList<ConsumoAlimentosFamilia>();

        try {
            cs = con.prepareCall(sSQL);
            cs.setInt(1, p_iAbadiaId);
            cs.setInt(2, p_iIdiomaId);
            cs.execute();
            rs = cs.getResultSet();
            while (rs.next()) {
                oConsumos = new ConsumoAlimentosFamilia();
                oConsumos.setAbadiaId(rs.getInt("ABADIAID"));
                oConsumos.setFamiliaId(rs.getInt("FAMILIAID"));
                oConsumos.setAlimentoId(rs.getInt("ALIMENTOID"));
                oConsumos.setAlimentoDesc(rs.getString("LITERAL_ALIMENTO"));
                oConsumos.setFamiliaDesc(rs.getString("LITERAL_FAMILIA"));
                oConsumos.setRequerido(rs.getDouble("REQUISITOS"));
                alConsumos.add(oConsumos);
            }
            return alConsumos;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adAlimentos.recuperarConsumosAlimentosFamilia()", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(cs);
        }

    }
}
