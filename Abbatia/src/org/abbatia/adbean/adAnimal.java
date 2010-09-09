package org.abbatia.adbean;

import org.abbatia.actionform.DatosSacrificioActForm;
import org.abbatia.adbean.base.adbeans;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Animal;
import org.abbatia.bean.Edificio;
import org.abbatia.bean.Usuario;
import org.abbatia.core.CoreTiempo;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.AnimalNoEncontradoException;
import org.abbatia.exception.CrearAnimalException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.HTML;
import org.abbatia.utils.Utilidades;
import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;

import java.sql.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class adAnimal extends adbeans {
    private static Logger log = Logger.getLogger(adAnimal.class.getName());

    /**
     * Instancia la clase sin obtener una nueva conexi髇
     *
     * @param con
     * @throws AbadiaException
     */
    public adAnimal(Connection con) throws AbadiaException {
        super(con);
    }


    //Inserta un registro de animal en base de datos.
    public int crearAnimal(Animal animal) throws AbadiaException {
        String sSQL = "INSERT INTO animales (TIPO_ANIMALID, EDIFICIOID, NIVEL, ESTADO, FECHA_NACIMIENTO, SALUD) " +
                "VALUES (" + animal.getTipoAnimalid() + ", " +
                animal.getEdificioid() + ", " +
                animal.getNivel() + ", " +
                animal.getEstado() + ", '" +
                Utilidades.formatStringToDB(animal.getFecha_nacimiento()) + "', " +
                animal.getSalud() + ")";


        ResultSet rs = null;
        Statement stmt = null;
        try {

            stmt = con.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_UPDATABLE);
            stmt.executeUpdate(sSQL, Statement.RETURN_GENERATED_KEYS);
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            } else return 0;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adAnimal. crearAnimal. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(stmt);
        }
    }

    //elimina el registro de un animal
    public void eliminarVentaAnimal(int idAnimal) throws AbadiaSQLException {

        String sSQLLote = "Delete From mercados Where productoid = ?";
        String sSQLAlim = "Delete From mercados_animales Where productoid = ?";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQLLote);
            int parNo = 1;
            ps.setLong(parNo, idAnimal);
            ps.execute();

            // Alimento
            ps = con.prepareStatement(sSQLAlim);
            parNo = 1;
            ps.setLong(parNo, idAnimal);
            ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adAnimal. eliminarVentaAnimal. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    //recupera los datos de un animal a partir
    //de su identificador
    public Animal recuperarAnimalTipo(int iTipo, int iNivel) throws AbadiaSQLException {
        String sSQL = "SELECT * from animales_crecimiento as ac where ac.tipo_animalid=? and ac.nivel=? ";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, iTipo);
            ps.setInt(2, iNivel);
            rs = ps.executeQuery();
            Animal animal = new Animal();
            if (rs.next()) {
                animal.setNivel((short) iNivel);
                animal.setTipoAnimalid(iTipo);
                animal.setNombre(rs.getString("DESCRIPCION"));
                return animal;
            }
            return null;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adAnimal. recuperarAnimalTipo. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    //recupera los datos de un animal a partir
    //de su identificador
    public boolean existeAnimalTipo(int Abadiaid, int Tipo) throws AbadiaException {
        String sSQL = "SELECT 1 FROM `animales` a, edificio e where e.edificioid = a.edificioid and tipo_animalid = " + Tipo + " and abadiaid = " + Abadiaid;

        adUtils utils = new adUtils(con);
        int result = utils.getSQL(sSQL, 0);
        if (result == 0) return false;
        else return true;
    }

    /**
     * Devuelve un boleano indicando si en la abadia existe un animal del tipo establecido de nivel mayor que 2
     * que este vivo y no este embarazado...
     *
     * @param Abadiaid
     * @param Tipo
     * @return
     * @throws AbadiaException
     */
    public boolean existeAnimalTipoAdultoLibre(int Abadiaid, int Tipo) throws AbadiaException {
        String sSQL = "SELECT 1 FROM `animales` a, edificio e where e.edificioid = a.edificioid and a.tipo_animalid = " + Tipo + " and e.abadiaid = " + Abadiaid + " and a.estado = " + Constantes.ESTADO_ANIMAL_VIVO + " and a.fecha_embarazo is null and a.nivel > 2 ";

        adUtils utils = new adUtils(con);
        int result = utils.getSQL(sSQL, 0);
        if (result == 0) return false;
        else return true;
    }

    /**
     * Devuelve el identificador de animal del tipo establecido de nivel mayor que 2
     * que este vivo y no este embarazado...
     *
     * @param Abadiaid
     * @param Tipo
     * @return
     * @throws org.abbatia.exception.base.AbadiaException
     *
     */
    public int getIdAnimalTipoAdultoLibre(int Abadiaid, int Tipo) throws AbadiaException {
        String sSQL = "SELECT a.animalid FROM `animales` a, edificio e where e.edificioid = a.edificioid and a.tipo_animalid = " + Tipo + " and e.abadiaid = " + Abadiaid + " and a.estado = " + Constantes.ESTADO_ANIMAL_VIVO + " and a.fecha_embarazo is null and a.nivel > 2 order by a.fecha_nacimiento ";

        adUtils utils = new adUtils(con);
        return utils.getSQL(sSQL, 0);
    }

    /**
     * Devuelve el identificador de tipo de animal
     *
     * @param idAnimal
     * @return
     * @throws AbadiaException
     */
    public int getTipoAnimal(int idAnimal) throws AbadiaException {
        String sSQL = "SELECT tipo_animalid FROM animales where animalid = " + idAnimal;

        adUtils utils = new adUtils(con);
        return utils.getSQL(sSQL, 0);
    }

    //recupera los datos de un animal a partir
    //de su identificador
    public Animal recuperarAnimal(int idAnimal) throws AbadiaException {
        String sSQL = "SELECT * from animales as a, animales_crecimiento as ac where a.tipo_animalid=ac.tipo_animalid and a.nivel=ac.nivel and a.animalid=?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idAnimal);

            rs = ps.executeQuery();
            Animal animal = new Animal();
            if (rs.next()) {
                animal.setAnimalid(idAnimal);
                animal.setNivel(rs.getShort("NIVEL"));
                animal.setTipoAnimalid(rs.getInt("TIPO_ANIMALID"));
                animal.setNombre(rs.getString("DESCRIPCION"));

                return animal;
            }
            return null;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adAnimal. recuperarAnimal. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /**
     * Recupera un animal a partir de su c骴igo y el de su abad韆
     *
     * @param idAnimal
     * @param idAbadia
     * @return
     * @throws AbadiaException
     */
    public Animal recuperarAnimalAbadia(int idAnimal, int idAbadia) throws AbadiaException {
        String sSQL = "SELECT a.NIVEL, a.TIPO_ANIMALID, a.TRABAJA, a.EDIFICIOID " +
                " from animales a, edificio e " +
                " where e.edificioid = a.edificioid and a.animalid=? and e.abadiaid = ?";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idAnimal);
            ps.setInt(2, idAbadia);

            rs = ps.executeQuery();
            Animal animal = new Animal();
            if (rs.next()) {
                animal.setAnimalid(idAnimal);
                animal.setNivel(rs.getShort("NIVEL"));
                animal.setTipoAnimalid(rs.getInt("TIPO_ANIMALID"));
                animal.setTrabaja(rs.getInt("TRABAJA"));
                animal.setEdificioid(rs.getInt("EDIFICIOID"));

                return animal;
            } else throw new AnimalNoEncontradoException("adAnimal. recuperarAnimalAbadia. Animal no encontrado", log);

        } catch (SQLException e) {
            throw new AbadiaSQLException("adAnimal. recuperarAnimal. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public boolean existeAnimal(int idAnimal) throws AbadiaException {
        String sSQL = "Select 1 from animales where animalid = " + idAnimal;
        adUtils utils = new adUtils(con);
        int result = utils.getSQL(sSQL, 0);
        return result != 0;
    }

    public Edificio recuperarEdificioAnimal(int idAnimal) throws AbadiaSQLException {

        String sSQL = "SELECT e.ABADIAID, e.EDIFICIOID from animales as a, edificio as e where e.edificioid=a.edificioid and a.animalid=? AND a.ESTADO=0 and a.fecha_fallecimiento is null";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idAnimal);
            rs = ps.executeQuery();
            Edificio edificio;
            if (rs.next()) {
                edificio = new Edificio();
                edificio.setIdDeAbadia(rs.getInt("ABADIAID"));
                edificio.setIdDeEdificio(rs.getInt("EDIFICIOID"));
                return edificio;
            } else return null;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adAnimal. recuperarEdificioAnimal. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }


    public ArrayList<Animal> recuperarAnimalesEdificio(long idDeEdificio, Usuario usuario, MessageResources resource) throws AbadiaException {
        //Definici贸n de cadena sql de consulta
        String sSQL = "SELECT count(*) as CANTIDAD, a.FECHA_NACIMIENTO, AVG(a.SALUD) as SALUD, a.NIVEL, l1.literal DESCRIPCION, l2.literal desctipo, a.EDIFICIOID, a.TIPO_ANIMALID, aa.MAX, aa.MIN, a.FECHA_EMBARAZO, a.AISLADO, a.ANIMALID, at.TRABAJA as PUEDE_TRABAJAR, a.TRABAJA " +
                "FROM animales as a, animales_crecimiento as ac, animales_alimentos aa, animales_tipo at, literales l1, literales l2 " +
                "WHERE a.TIPO_ANIMALID = ac.TIPO_ANIMALID AND a.NIVEL = ac.NIVEL AND a.TIPO_ANIMALID=aa.TIPO_ANIMALID AND a.NIVEL = aa.NIVEL AND aa.TIPO = 'M' AND a.EDIFICIOID = ? AND a.ESTADO=0 AND at.TIPO_ANIMALID = a.TIPO_ANIMALID " +
                "  AND  l1.idiomaid = ? AND l2.idiomaid = ? AND l1.literalid = ac.literalid AND l2.literalid = at.literalid " +
                "GROUP BY a.FECHA_NACIMIENTO, a.NIVEL,  l1.literal, a.EDIFICIOID, a.TIPO_ANIMALID, aa.MAX, aa.MIN, a.FECHA_EMBARAZO, a.AISLADO, a.TRABAJA " +
                "ORDER BY a.FECHA_EMBARAZO, descTipo, a.FECHA_NACIMIENTO ";

        Animal animal;
        ArrayList<Animal> animales = new ArrayList<Animal>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);

            int parNo = 1;
            ps.setLong(parNo++, idDeEdificio);
            ps.setInt(parNo++, usuario.getIdDeIdioma());
            ps.setInt(parNo, usuario.getIdDeIdioma());
            //Lanzo la consulta y cargo el resultado en un resultset
            rs = ps.executeQuery();
            //si la consulta encuentra la edificio....
            while (rs.next()) {
                animal = new Animal();
                animal.setCantidad(rs.getInt("CANTIDAD"));
                animal.setFecha_nacimiento(Utilidades.formatStringFromDB(rs.getString("FECHA_NACIMIENTO")));
                animal.setAnimalid(rs.getInt("ANIMALID"));
                animal.setEdificioid(rs.getInt("EDIFICIOID"));
                animal.setTipoAnimalid(rs.getInt("TIPO_ANIMALID"));
                animal.setNivel(rs.getShort("NIVEL"));
                animal.setNombre(rs.getString("DESCRIPCION"));
                animal.setDescTipo(rs.getString("DESCTIPO"));
                animal.setSalud(rs.getInt("SALUD"));
                animal.setCarneMax(rs.getInt("MAX"));
                animal.setCarneMin(rs.getInt("MIN"));
                animal.setClave(rs.getString("NIVEL") + ";" + rs.getString("TIPO_ANIMALID") + ";" + rs.getString("FECHA_NACIMIENTO"));
                animal.setAislado(rs.getShort("AISLADO"));
                animal.setPuedeTrabajar(rs.getShort("PUEDE_TRABAJAR"));
                animal.setTrabaja(rs.getShort("TRABAJA"));
                this.actualizarBarrasHTML(animal, resource);

                animal.setFecha_embarazo(Utilidades.formatStringFromDB(rs.getString("FECHA_EMBARAZO")));

                animales.add(animal);
            }
            return animales;
        } catch (SQLException e) {
            //log.debug("adAnimal. recuperarAnimalesEdificio error SQL: " + e.getMessage());
            throw new AbadiaSQLException("adAnimal. recuperarAnimalesEdificio error SQL ", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /**
     * recupera los animales de un tipo determinado para una abadia determinada
     *
     * @param idAbadia
     * @return ArrayList
     */
    public ArrayList<Animal> recuperarAnimalesPorAbadiaTipo(int idAbadia, int idTipoAnimal) throws AbadiaException {
        String sSQL = "SELECT a.ANIMALID, a.SALUD, a.TIPO_ANIMALID " +
                " FROM animales a, edificio e " +
                " WHERE a.TIPO_ANIMALID = ? AND e.ABADIAID = ? AND a.EDIFICIOID = e.EDIFICIOID" +
                " AND a.TRABAJA = 1" +
                " ORDER BY a.SALUD desc ";

        Animal animal;
        ArrayList<Animal> alAnimales = new ArrayList<Animal>();

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, idTipoAnimal);
            ps.setInt(2, idAbadia);
            rs = ps.executeQuery();
            while (rs.next()) {
                animal = new Animal();
                animal.setAnimalid(rs.getInt("ANIMALID"));
                animal.setTipoAnimalid(rs.getInt("TIPO_ANIMALID"));
                animal.setSalud(rs.getInt("SALUD"));
                alAnimales.add(animal);
            }
            return alAnimales;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adAnimal. recuperarAnimalesPorAbadiaTipo", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public void crearAnimalesAbadia(Abadia abadia, Usuario usuario) throws AbadiaException {
        adEdificio edificioAD = new adEdificio(con);
        Edificio edificio = edificioAD.recuperarEdificioTipo(Constantes.EDIFICIO_GRANJA, abadia, usuario);

        Animal animal = new Animal();

        if (abadia.getActividadPrincipal() == Constantes.ACTIVIDAD_GANADERIA) {
            //creamos vacas
            animal.setEdificioid(edificio.getIdDeEdificio());
            animal.setSalud(100);
            animal.setNivel((short) 3);
            animal.setTipoAnimalid(Constantes.ANIMALES_VACA);
            animal.setEstado(0);
            animal.setFecha_nacimiento(CoreTiempo.getDiferenciaMesString(-recuperarDiasVida(Constantes.ANIMALES_VACA, (short) animal.getNivel() - 1)));
            crearAnimal(animal, 4);
            //creamos toro
            animal = new Animal();
            animal.setEdificioid(edificio.getIdDeEdificio());
            animal.setSalud(100);
            animal.setNivel((short) 3);
            animal.setTipoAnimalid(Constantes.ANIMALES_TORO);
            animal.setEstado(0);
            animal.setFecha_nacimiento(CoreTiempo.getDiferenciaMesString(-recuperarDiasVida(Constantes.ANIMALES_TORO, (short) animal.getNivel() - 1)));
            crearAnimal(animal, 1);
            //creamos terneras
            animal = new Animal();
            animal.setEdificioid(edificio.getIdDeEdificio());
            animal.setSalud(100);
            animal.setNivel((short) 1);
            animal.setTipoAnimalid(Constantes.ANIMALES_VACA);
            animal.setEstado(0);
            animal.setFecha_nacimiento(CoreTiempo.getDiferenciaMesString(-recuperarDiasVida(Constantes.ANIMALES_VACA, (short) animal.getNivel() - 1)));
            crearAnimal(animal, 2);
            //creamos Conejas
            animal = new Animal();
            animal.setEdificioid(edificio.getIdDeEdificio());
            animal.setSalud(100);
            animal.setNivel((short) 3);
            animal.setTipoAnimalid(Constantes.ANIMALES_CONEJA);
            animal.setEstado(0);
            animal.setFecha_nacimiento(CoreTiempo.getDiferenciaMesString(-recuperarDiasVida(Constantes.ANIMALES_CONEJA, (short) animal.getNivel() - 1)));
            crearAnimal(animal, 6);

            //creamos Conejos
            animal = new Animal();
            animal.setEdificioid(edificio.getIdDeEdificio());
            animal.setSalud(100);
            animal.setNivel((short) 3);
            animal.setTipoAnimalid(Constantes.ANIMALES_CONEJO);
            animal.setEstado(0);
            animal.setFecha_nacimiento(CoreTiempo.getDiferenciaMesString(-recuperarDiasVida(Constantes.ANIMALES_CONEJO, (short) animal.getNivel() - 1)));
            crearAnimal(animal, 1);

            //creamos Conejitas
            animal = new Animal();
            animal.setEdificioid(edificio.getIdDeEdificio());
            animal.setSalud(100);
            animal.setNivel((short) 1);
            animal.setTipoAnimalid(Constantes.ANIMALES_CONEJA);
            animal.setEstado(0);
            animal.setFecha_nacimiento(CoreTiempo.getDiferenciaMesString(-recuperarDiasVida(Constantes.ANIMALES_CONEJA, (short) animal.getNivel() - 1)));
            crearAnimal(animal, 2);
            //creamos Conejitos
            animal = new Animal();
            animal.setEdificioid(edificio.getIdDeEdificio());
            animal.setSalud(100);
            animal.setNivel((short) 1);
            animal.setTipoAnimalid(Constantes.ANIMALES_CONEJO);
            animal.setEstado(0);
            animal.setFecha_nacimiento(CoreTiempo.getDiferenciaMesString(-recuperarDiasVida(Constantes.ANIMALES_CONEJO, (short) animal.getNivel() - 1)));
            crearAnimal(animal, 2);

            //creamos Gallinas
            animal = new Animal();
            animal.setEdificioid(edificio.getIdDeEdificio());
            animal.setSalud(100);
            animal.setNivel((short) 3);
            animal.setTipoAnimalid(Constantes.ANIMALES_GALLINA);
            animal.setEstado(0);
            animal.setFecha_nacimiento(CoreTiempo.getDiferenciaMesString(-recuperarDiasVida(Constantes.ANIMALES_GALLINA, (short) animal.getNivel() - 1)));
            crearAnimal(animal, 6);

            //creamos Gallos
            animal = new Animal();
            animal.setEdificioid(edificio.getIdDeEdificio());
            animal.setSalud(100);
            animal.setNivel((short) 3);
            animal.setTipoAnimalid(Constantes.ANIMALES_GALLO);
            animal.setEstado(0);
            animal.setFecha_nacimiento(CoreTiempo.getDiferenciaMesString(-recuperarDiasVida(Constantes.ANIMALES_GALLO, (short) animal.getNivel() - 1)));
            crearAnimal(animal, 1);

            //creamos Pollitos macho
            animal = new Animal();
            animal.setEdificioid(edificio.getIdDeEdificio());
            animal.setSalud(100);
            animal.setNivel((short) 1);
            animal.setTipoAnimalid(Constantes.ANIMALES_GALLO);
            animal.setEstado(0);
            animal.setFecha_nacimiento(CoreTiempo.getDiferenciaMesString(-recuperarDiasVida(Constantes.ANIMALES_GALLO, (short) animal.getNivel() - 1)));
            crearAnimal(animal, 2);

            //creamos Pollitos hembra
            animal = new Animal();
            animal.setEdificioid(edificio.getIdDeEdificio());
            animal.setSalud(100);
            animal.setNivel((short) 1);
            animal.setTipoAnimalid(Constantes.ANIMALES_GALLINA);
            animal.setEstado(0);
            animal.setFecha_nacimiento(CoreTiempo.getDiferenciaMesString(-recuperarDiasVida(Constantes.ANIMALES_GALLINA, (short) animal.getNivel() - 1)));
            crearAnimal(animal, 2);

            //creamos OVEJAS
            animal = new Animal();
            animal.setEdificioid(edificio.getIdDeEdificio());
            animal.setSalud(100);
            animal.setNivel((short) 3);
            animal.setTipoAnimalid(Constantes.ANIMALES_OVEJA);
            animal.setEstado(0);
            animal.setFecha_nacimiento(CoreTiempo.getDiferenciaMesString(-recuperarDiasVida(Constantes.ANIMALES_OVEJA, (short) animal.getNivel() - 1)));
            crearAnimal(animal, 4);

            //creamos OVEJAS-MACHO
            animal = new Animal();
            animal.setEdificioid(edificio.getIdDeEdificio());
            animal.setSalud(100);
            animal.setNivel((short) 3);
            animal.setTipoAnimalid(Constantes.ANIMALES_OVEJA_MACHO);
            animal.setEstado(0);
            animal.setFecha_nacimiento(CoreTiempo.getDiferenciaMesString(-recuperarDiasVida(Constantes.ANIMALES_OVEJA_MACHO, (short) animal.getNivel() - 1)));
            crearAnimal(animal, 1);

            //creamos OVEJAS-MACHO CRIAS
            animal = new Animal();
            animal.setEdificioid(edificio.getIdDeEdificio());
            animal.setSalud(100);
            animal.setNivel((short) 1);
            animal.setTipoAnimalid(Constantes.ANIMALES_OVEJA_MACHO);
            animal.setEstado(0);
            animal.setFecha_nacimiento(CoreTiempo.getDiferenciaMesString(-recuperarDiasVida(Constantes.ANIMALES_OVEJA_MACHO, (short) animal.getNivel() - 1)));
            crearAnimal(animal, 1);

            //creamos OVEJAS-MACHO CRIAS
            animal = new Animal();
            animal.setEdificioid(edificio.getIdDeEdificio());
            animal.setSalud(100);
            animal.setNivel((short) 1);
            animal.setTipoAnimalid(Constantes.ANIMALES_OVEJA);
            animal.setEstado(0);
            animal.setFecha_nacimiento(CoreTiempo.getDiferenciaMesString(-recuperarDiasVida(Constantes.ANIMALES_OVEJA, (short) animal.getNivel() - 1)));
            crearAnimal(animal, 1);

        } else //para abadias NO ganaderas...
        {
            //creamos vacas
            animal.setEdificioid(edificio.getIdDeEdificio());
            animal.setSalud(100);
            animal.setNivel((short) 3);
            animal.setTipoAnimalid(Constantes.ANIMALES_VACA);
            animal.setEstado(0);
            animal.setFecha_nacimiento(CoreTiempo.getDiferenciaMesString(-recuperarDiasVida(Constantes.ANIMALES_VACA, (short) animal.getNivel() - 1)));
            crearAnimal(animal, 2);

            //creamos terneras
            animal = new Animal();
            animal.setEdificioid(edificio.getIdDeEdificio());
            animal.setSalud(100);
            animal.setNivel((short) 1);
            animal.setTipoAnimalid(Constantes.ANIMALES_VACA);
            animal.setEstado(0);
            animal.setFecha_nacimiento(CoreTiempo.getDiferenciaMesString(-recuperarDiasVida(Constantes.ANIMALES_VACA, (short) animal.getNivel() - 1)));
            crearAnimal(animal, 1);

            //creamos Conejas
            animal = new Animal();
            animal.setEdificioid(edificio.getIdDeEdificio());
            animal.setSalud(100);
            animal.setNivel((short) 3);
            animal.setTipoAnimalid(Constantes.ANIMALES_CONEJA);
            animal.setEstado(0);
            animal.setFecha_nacimiento(CoreTiempo.getDiferenciaMesString(-recuperarDiasVida(Constantes.ANIMALES_CONEJA, (short) animal.getNivel() - 1)));
            crearAnimal(animal, 3);

            //creamos Conejitas
            animal = new Animal();
            animal.setEdificioid(edificio.getIdDeEdificio());
            animal.setSalud(100);
            animal.setNivel((short) 1);
            animal.setTipoAnimalid(Constantes.ANIMALES_CONEJA);
            animal.setEstado(0);
            animal.setFecha_nacimiento(CoreTiempo.getDiferenciaMesString(-recuperarDiasVida(Constantes.ANIMALES_CONEJA, (short) animal.getNivel() - 1)));
            crearAnimal(animal, 2);

            //creamos Gallinas
            animal = new Animal();
            animal.setEdificioid(edificio.getIdDeEdificio());
            animal.setSalud(100);
            animal.setNivel((short) 3);
            animal.setTipoAnimalid(Constantes.ANIMALES_GALLINA);
            animal.setEstado(0);
            animal.setFecha_nacimiento(CoreTiempo.getDiferenciaMesString(-recuperarDiasVida(Constantes.ANIMALES_GALLINA, (short) animal.getNivel() - 1)));
            crearAnimal(animal, 4);

            //creamos Pollitos hembra
            animal = new Animal();
            animal.setEdificioid(edificio.getIdDeEdificio());
            animal.setSalud(100);
            animal.setNivel((short) 1);
            animal.setTipoAnimalid(Constantes.ANIMALES_GALLINA);
            animal.setEstado(0);
            animal.setFecha_nacimiento(CoreTiempo.getDiferenciaMesString(-recuperarDiasVida(Constantes.ANIMALES_GALLINA, (short) animal.getNivel() - 1)));
            crearAnimal(animal, 1);

            //creamos Pollitos macho
            animal = new Animal();
            animal.setEdificioid(edificio.getIdDeEdificio());
            animal.setSalud(100);
            animal.setNivel((short) 1);
            animal.setTipoAnimalid(Constantes.ANIMALES_GALLO);
            animal.setEstado(0);
            animal.setFecha_nacimiento(CoreTiempo.getDiferenciaMesString(-recuperarDiasVida(Constantes.ANIMALES_GALLO, (short) animal.getNivel() - 1)));
            crearAnimal(animal, 1);

            //creamos OVEJAS
            animal = new Animal();
            animal.setEdificioid(edificio.getIdDeEdificio());
            animal.setSalud(100);
            animal.setNivel((short) 3);
            animal.setTipoAnimalid(Constantes.ANIMALES_OVEJA);
            animal.setEstado(0);
            animal.setFecha_nacimiento(CoreTiempo.getDiferenciaMesString(-recuperarDiasVida(Constantes.ANIMALES_OVEJA, (short) animal.getNivel() - 1)));
            crearAnimal(animal, 2);

            //creamos OVEJAS
            animal = new Animal();
            animal.setEdificioid(edificio.getIdDeEdificio());
            animal.setSalud(100);
            animal.setNivel((short) 1);
            animal.setTipoAnimalid(Constantes.ANIMALES_OVEJA_MACHO);
            animal.setEstado(0);
            animal.setFecha_nacimiento(CoreTiempo.getDiferenciaMesString(-recuperarDiasVida(Constantes.ANIMALES_OVEJA_MACHO, (short) animal.getNivel() - 1)));
            crearAnimal(animal, 1);

        }
    }

    //Inserta n registros de un mismo animal.
    public int crearAnimal(Animal animal, int numero) throws AbadiaException {
        int idAnimal = 0;
        adAnimal animalAD;
        try {
            animalAD = new adAnimal(con);
            for (int iCount = 0; iCount < numero; iCount++) {
                idAnimal = animalAD.crearAnimal(animal);
            }
            return idAnimal;
        } catch (Exception e) {
            throw new CrearAnimalException("adAnimal. crearAnimal.", e, log);
        }
    }

    public int recuperarCantidad(long abadia, int nivel, int tipo, String sFecha) throws AbadiaException {

        String sSQL = "SELECT count(*) as CANTIDAD " +
                " from animales as a, edificio as e " +
                " where e.abadiaid=? and e.edificioid=a.edificioid and a.nivel=? and a.fecha_nacimiento=? and a.tipo_animalid=? AND a.ESTADO=0 AND a.FECHA_FALLECIMIENTO is null and a.FECHA_EMBARAZO is null ";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setLong(1, abadia);
            ps.setInt(2, nivel);
            ps.setString(3, sFecha);
            ps.setInt(4, tipo);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("CANTIDAD");
            } else return 0;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adAnimal. recuperCantidad. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public int recuperarSalud(long abadia, int nivel, int tipo, String sFecha) throws AbadiaException {

        String sSQL = "SELECT avg(a.SALUD) as SALUD from animales as a, edificio as e where e.abadiaid=? and e.edificioid=a.edificioid and a.nivel=? and a.fecha_nacimiento=? and a.tipo_animalid=? AND a.ESTADO=0 AND a.FECHA_FALLECIMIENTO is null ";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setLong(1, abadia);
            ps.setInt(2, nivel);
            ps.setString(3, sFecha);
            ps.setInt(4, tipo);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("SALUD");
            } else return 0;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adAnimal. recuperCantidad. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public void venderAnimalTipo(long abadia, int nivel, int tipo, String sFecha, int iCantidad) throws AbadiaException {
        String sSQL = "SELECT a.ANIMALID from animales as a, edificio as e where e.abadiaid=? and e.edificioid=a.edificioid and a.nivel=? and a.fecha_nacimiento=? and a.tipo_animalid=? and a.fecha_embarazo is null AND a.ESTADO=0 and a.FECHA_FALLECIMIENTO is null";
        String sSQL2 = "DELETE from animales where animalid=?";
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        int idAnimal = 0;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setLong(1, abadia);
            ps.setInt(2, nivel);
            ps.setString(3, sFecha);
            ps.setInt(4, tipo);
            rs = ps.executeQuery();
            int iCount = 0;
            while (rs.next()) {
                idAnimal = rs.getInt("ANIMALID");
                if (iCount < iCantidad) {
                    ps2 = con.prepareStatement(sSQL2);
                    ps2.setInt(1, idAnimal);
                    ps2.execute();
                    ps2 = null;
                    iCount++;
                }
            }

        } catch (SQLException e) {
            throw new AbadiaSQLException("adAnimal. venderAnimalTipo. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public double recuperarVolumenAnimal(int tipo, int nivel) throws AbadiaException {
        String sSQL = "SELECT VOLUMEN from animales_crecimiento where TIPO_ANIMALID=? AND NIVEL=?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setLong(1, tipo);
            ps.setInt(2, nivel);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("VOLUMEN");
            } else return 0;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adAnimal. recuperarVolumenAnimal. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public int recuperarDiasVida(int tipo, int nivel) throws AbadiaException {
        try {
            adUtils utilsAD = new adUtils(con);
            return utilsAD.getSQL("SELECT MES_CAMBIO_NIVEL AS DIAS from animales_crecimiento where TIPO_ANIMALID=" + tipo + " AND NIVEL=" + nivel, 0);
        } catch (AbadiaSQLException e) {
            throw new AbadiaSQLException("adAnimal. recuperarDiasVida. SQLException.", e, log);
        }
    }


    public DatosSacrificioActForm getProduccionPotencialAlimentos(DatosSacrificioActForm datos, Usuario usuario) throws AbadiaException {
        StringTokenizer st = new StringTokenizer(datos.getId(), ";");
        String sNivel = st.nextToken();
        String Tipo = st.nextToken();
        String sFecha = st.nextToken();

        String sSQL = "SELECT aa.MAX, aa.MIN, aa.ALIMENTOID, at.DESCRIPCION, um.DESCRIPCION as DESCUM, l.literal as DESCANI " +
                "FROM animales_alimentos aa, alimentos_tipo at, unidad_medida um, animales_crecimiento ac, literales l " +
                "WHERE aa.alimentoid = at.alimentoid and um.UNIDAD_MEDIDA = at.UNIDAD_MEDIDA and " +
                "aa.TIPO_ANIMALID=? AND aa.NIVEL=? and aa.tipo=? AND ac.TIPO_ANIMALID=aa.TIPO_ANIMALID and ac.NIVEL=aa.NIVEL and " +
                "l.literalid = ac.literalid and l.idiomaid = ? and um.idiomaid=l.idiomaid";
        PreparedStatement ps = null;

        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, Integer.parseInt(Tipo));
            ps.setInt(2, Integer.parseInt(sNivel));
            ps.setString(3, "M");
            ps.setInt(4, usuario.getIdDeIdioma());
            rs = ps.executeQuery();
            if (rs.next()) {
                datos.setAnimal_tipo(Integer.parseInt(Tipo));
                datos.setAnimal_nivel(Integer.parseInt(sNivel));
                datos.setAnimal_fechanacimiento(sFecha);
                datos.setAlimento_desc(rs.getString("DESCRIPCION"));
                datos.setAnimal_desc(rs.getString("DESCANI"));
                datos.setAlimento_id(rs.getInt("ALIMENTOID"));
                datos.setAlimento_max(rs.getDouble("MAX"));
                datos.setAlimento_min(rs.getDouble("MIN"));
                datos.setUnidad_alimento(rs.getString("DESCUM"));
                datos.setCantidad(1);
                datos.setPrecio(Utilidades.redondear(getPrecioMercadoCarne(rs.getInt("ALIMENTOID"))));
                return datos;
            } else return null;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adAnimal. getProduccionPotencial. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public DatosSacrificioActForm getProduccionPotencialRecursos(DatosSacrificioActForm datos, Usuario usuario) throws AbadiaException {
        StringTokenizer st = new StringTokenizer(datos.getId(), ";");
        String sNivel = st.nextToken();
        String Tipo = st.nextToken();

        String sSQL = "SELECT ar.MAX, ar.MIN, ar.RECURSOID, um.DESCRIPCION as DESCUM, l.literal as DESCRIPCION " +
                "FROM animales_recursos ar, recurso_tipo rt, unidad_medida um, literales l " +
                "WHERE ar.recursoid = rt.recursoid and um.UNIDAD_MEDIDA = rt.UNIDAD_MEDIDA and " +
                "ar.TIPO_ANIMALID=? AND ar.NIVEL=? and um.idiomaid = 1 and rt.literalid = l.literalid and l.idiomaid = ? ";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, Integer.parseInt(Tipo));
            ps.setInt(2, Integer.parseInt(sNivel));
            ps.setInt(3, usuario.getIdDeIdioma());
            rs = ps.executeQuery();
            if (rs.next()) {
                datos.setRecurso_desc(rs.getString("DESCRIPCION"));
                datos.setRecurso_id(rs.getInt("RECURSOID"));
                datos.setRecurso_max(rs.getDouble("MAX"));
                datos.setRecurso_min(rs.getDouble("MIN"));
                datos.setUnidad_recurso(rs.getString("DESCUM"));
                return datos;
            } else return null;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adAnimal. getProduccionPotencialRecursos. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }


    public void eliminarAnimalTipo(DatosSacrificioActForm datos, long idAbadia) throws AbadiaSQLException {
        String sSQL = "SELECT a.ANIMALID from animales as a, edificio as e where e.abadiaid=? and e.edificioid=a.edificioid and a.nivel=? and a.fecha_nacimiento=? and a.tipo_animalid=? and a.fecha_embarazo is null AND a.ESTADO=0 AND a.FECHA_FALLECIMIENTO is null";
        String sSQL2 = "DELETE from animales where animalid=? ";
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setLong(1, idAbadia);
            ps.setInt(2, datos.getAnimal_nivel());
            ps.setString(3, datos.getAnimal_fechanacimiento());
            ps.setInt(4, datos.getAnimal_tipo());
            rs = ps.executeQuery();
            if (rs.next()) {
                ps2 = con.prepareStatement(sSQL2);
                ps2.setInt(1, rs.getInt("ANIMALID"));
                ps2.execute();
                ps2 = null;
            }

        } catch (SQLException e) {
            throw new AbadiaSQLException("adAnimal. venderAnimalTipo. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public void eliminarAnimalesAbadia(long idAbadia) throws AbadiaSQLException {
        String sSQL = "DELETE from animales where EDIFICIOID IN (SELECT EDIFICIOID FROM edificio where ABADIAID=?)";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setLong(1, idAbadia);
            ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adAnimal. eliminarAnimalesAbadia. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public double getPrecioMercadoCarne(int idAlimento) throws AbadiaException {
        String sSQLAlimentos = "SELECT m.precio_actual, m.abadiaid FROM  mercados m, mercados_alimentos ma WHERE m.productoid = ma.productoid and ma.alimentoid=? and m.abadiaid=0 ";

        ResultSet rs = null;
        PreparedStatement ps = null;
        try {

            ps = con.prepareStatement(sSQLAlimentos);

            ps.setInt(1, idAlimento);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("precio_actual");
            } else return 1;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adAnimal. getPrecioMercadoCarne. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public int recuperarDiasEmbarazo(Animal animal) throws AbadiaException {
        //Definici贸n de cadena sql de consulta
        String sSQL = "SELECT TIEMPO_DIAS_EMBARAZO FROM animales_crias as ac WHERE ac.TIPO_ANIMALID_FEMENINO = ? ";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);

            int parNo = 1;
            ps.setLong(parNo, animal.getTipoAnimalid());
            //Lanzo la consulta y cargo el resultado en un resultset
            rs = ps.executeQuery();
            //si la consulta encuentra la edificio....
            if (rs.next()) {
                return rs.getInt("TIEMPO_DIAS_EMBARAZO");
            }
            return 0;
        } catch (SQLException e) {
            throw new AbadiaSQLException("adAnimal. recuperarDiasEmbarazo. error SQL ", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public void aislarAnimal(int idAnimal) throws AbadiaException {
        //Definici贸n de cadena sql de consulta
        String sSQL = "UPDATE animales set AISLADO=1 where animalid=?";

        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);

            int parNo = 1;
            ps.setLong(parNo, idAnimal);
            //Lanzo la consulta y cargo el resultado en un resultset
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adAnimal. aislarAnimal. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /**
     * Actualiza el estado de un animal.
     *
     * @param idAnimal
     * @param estado
     * @throws AbadiaException
     */
    public void actualizarEstado(int idAnimal, short estado) throws AbadiaException {
        adUtils utils = null;
        String sSQL = "UPDATE animales set estado = " + estado + " where animalid = " + idAnimal;
        utils = new adUtils(con);
        utils.execSQL(sSQL);
    }

    /**
     * Actualiza el estado de un animal.
     *
     * @param idAnimal
     * @param valor
     * @throws AbadiaException
     */
    public void restarSalud(int idAnimal, short valor) throws AbadiaException {
        adUtils utils = null;
        String sSQL = "UPDATE animales set salud = salud - " + valor + " where animalid = " + idAnimal;
        utils = new adUtils(con);
        utils.execSQL(sSQL);
    }

    /**
     * Actualiza el estado de un animal y el idedificio en el que se encuentra.
     *
     * @param idAnimal
     * @param estado
     * @param idEdificio
     * @throws AbadiaException
     */
    public void actualizarEstadoEdificio(int idAnimal, short estado, int idEdificio) throws AbadiaException {
        String sSQL = "UPDATE animales set estado = " + estado + ", edificioid = " + idEdificio + " where animalid = " + idAnimal;

        adUtils utils = new adUtils(con);
        utils.execSQL(sSQL);
    }


    public void devolverAnimal(int idAnimal) throws AbadiaException {
        //Definici贸n de cadena sql de consulta
        String sSQL = "UPDATE animales set AISLADO=0 where animalid=?";

        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);

            int parNo = 1;
            ps.setLong(parNo, idAnimal);
            //Lanzo la consulta y cargo el resultado en un resultset
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adAnimal. devolverAnimal. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public void caparToro(int idAnimal) throws AbadiaException {
        //Definici贸n de cadena sql de consulta
        String sSQL = "UPDATE animales set TIPO_ANIMALID=1 where animalid=?";

        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);

            int parNo = 1;
            ps.setLong(parNo, idAnimal);
            //Lanzo la consulta y cargo el resultado en un resultset
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adAnimal. caparToro. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /* Barras de HTML
     */
    public void actualizarBarrasHTML(Animal animal, MessageResources resource) {
        String sHTML = "";
        sHTML = sHTML + HTML.smallBarra(animal.getSalud() / 10, resource.getMessage("monjes.abadia.salud") + " " + animal.getSalud() + " " + resource.getMessage("edificio.abadia.sacrificar.texto4") + " " + 100);
        animal.setBarra_HTML(sHTML);
    }


    /**
     * Actualiza el par醡etro trabaja de un animal
     *
     * @param idAnimal
     * @param trabaja  // 1->si, 2-> no
     * @throws AbadiaException
     */
    public void actualizarTrabaja(int idAnimal, short trabaja) throws AbadiaException {
        adUtils utils = null;
        String sSQL = "UPDATE animales set trabaja = " + trabaja + " where animalid = " + idAnimal;
        utils = new adUtils(con);
        utils.execSQL(sSQL);
    }


}

