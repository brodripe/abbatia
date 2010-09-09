package org.abbatia.adbean;

import org.abbatia.actionform.RegistroActForm;
import org.abbatia.adbean.base.adbeans;
import org.abbatia.bean.Table;
import org.abbatia.bean.Usuario;
import org.abbatia.core.CoreTiempo;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.*;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.HTML;
import org.abbatia.utils.Utilidades;
import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

public class adUsuario extends adbeans {
    private static Logger log = Logger.getLogger(adUsuario.class.getName());

    /**
     * Instancia la clase sin obtener una nueva conexión
     *
     * @param con
     * @throws AbadiaException
     */
    public adUsuario(Connection con) throws AbadiaException {
        super(con);
    }


    /*
     Función para registrar un usuario en el sistema, comprueba que el nick no exista,
     que el email no esté duplicado, lo da de alta y le envia un mensaje de bienvenida
     Códigos de error:
        0 > Usuario registrado y devuelve el ID del usuario
        0 - Error inesperado
       -1 - Nick existente
       -2 - Email duplicado
       -3 - Error en el Insert

    */
    public Usuario registrar(RegistroActForm usuario) throws AbadiaException {
        // Comprobar si existe un usuario con el mismo nick
        String sSQL = "Select NICK From usuario Where NICK = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setString(1, usuario.getNick());
            rs = ps.executeQuery();
            if (rs.next()) {
                throw new YaExisteAliasException("El alias ya existe: " + usuario.getNick(), null, log);
            }
            // Comprobar si existe un usuario con el mismo email
            sSQL = "Select NICK From usuario Where EMAIL = ?";
            ps = con.prepareStatement(sSQL);
            ps.setString(1, usuario.getEmail());
            rs = ps.executeQuery();
            if (rs.next()) {
                throw new YaExisteMailException("El correo pertenece a otro usuario: " + usuario.getEmail(), null, log);
            }

            // Añadir el usuario en la tabla
            sSQL = "INSERT INTO usuario ( IDIOMAID , NICK , CONTRASENA , NOMBRE , APELLIDO1 , APELLIDO2 , EMAIL , CONECTADO , WEB , ICQ, FECHAALTA, ULTIMACONEXION, PAIS, EDAD, SEXO, USUARIO_TIPO, ACEPTA_NORMAS ) " +
                    "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, usuario.getIdioma());
            ps.setString(2, usuario.getNick());
            ps.setString(3, usuario.getPwd());
            ps.setString(4, usuario.getNombre());
            ps.setString(5, usuario.getApellido1());
            ps.setString(6, usuario.getApellido2());
            ps.setString(7, usuario.getEmail());
            ps.setInt(8, 1);
            ps.setString(9, usuario.getWeb());
            ps.setString(10, usuario.getICQ());
            ps.setString(11, CoreTiempo.getTiempoRealStringConHoras());
            ps.setString(12, CoreTiempo.getTiempoRealStringConHoras());
            ps.setInt(13, usuario.getPais());
            ps.setInt(14, usuario.getEdad());
            ps.setInt(15, usuario.getSexo());
            ps.setInt(16, 99);
            ps.setBoolean(17, usuario.isAceptaNormas());
            // Ejecutarlo
            ps.execute();
            return validar(usuario.getNick(), usuario.getPwd());
        } catch (SQLException e) {
            throw new AbadiaSQLException("adUsuario. registrar. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public Usuario actualizar(RegistroActForm usuario, long idUsuario) throws AbadiaException {
        // Comprobar si existe un usuario con el mismo nick
        String sSQL;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // Añadir el usuario en la tabla
            sSQL = "Update usuario set IDIOMAID = ?, CONTRASENA=? , NOMBRE=? , APELLIDO1=? , APELLIDO2=? ,EDAD=?, PAIS=?, SEXO=? " +
                    "WHERE USUARIOID= ?";
            ps = con.prepareStatement(sSQL);
            ps.setInt(1, usuario.getIdioma());
            ps.setString(2, usuario.getPwd());
            ps.setString(3, usuario.getNombre());
            ps.setString(4, usuario.getApellido1());
            ps.setString(5, usuario.getApellido2());
            ps.setInt(6, usuario.getEdad());
            ps.setInt(7, usuario.getPais());
            ps.setInt(8, usuario.getSexo());
            ps.setLong(9, idUsuario);
            // Ejecutarlo
            ps.execute();
            return validar(usuario.getNick(), usuario.getPwd());
        } catch (SQLException e) {
            throw new AbadiaSQLException("adUsuario. actualizar. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /*
        Rutina para validar el usuario y devolver el ID,
        0 para no encontrado o contraseña incorrecta ( No se especifica para que el usuario no sepa cual es el error, si el nick o el pass )
    */
    public Usuario validar(String Nick, String Contrasena) throws AbadiaException {
        // Comprobar si existe un usuario con el mismo nick
        String sSQL = "Select * From usuario Where NICK = ? LIMIT 1";

        Usuario usuario;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setString(1, Nick);
            rs = ps.executeQuery();
            if (rs.next()) {
                usuario = new Usuario();
                usuario.setIdDeUsuario(rs.getInt("USUARIOID")); // El nick ya existe
                usuario.setNombre(rs.getString("NOMBRE"));
                usuario.setPrimerApellido(rs.getString("APELLIDO1"));
                usuario.setSegundoApellido(rs.getString("APELLIDO2"));
                usuario.setNick(rs.getString("NICK"));
                usuario.setEmail(rs.getString("EMAIL"));
                usuario.setIdDeIdioma(rs.getShort("IDIOMAID"));
                usuario.setContrasena(rs.getString("CONTRASENA"));
                usuario.setAdministrador(rs.getLong("USUARIO_TIPO"));
                usuario.setRegistrado(rs.getLong("REGISTRADO"));
                usuario.setBloqueado(getBloqueado(usuario.getIdDeUsuario()));
                usuario.setCongelado(rs.getShort("Abadia_congelada"));
                usuario.setPais(rs.getInt("PAIS"));
                usuario.setEdad(rs.getInt("EDAD"));
                usuario.setSexo(rs.getInt("SEXO"));
                usuario.setFecha_alta(rs.getString("FECHAALTA"));
                usuario.setReintentos(rs.getInt("REINTENTOS"));
                usuario.setAceptaNormas(rs.getBoolean("ACEPTA_NORMAS"));
                //return usuario;
            } else {
                throw new ValidacionIncorrectaException("El usuario no existe", null, log);
            }
            //una vez recuperado el usuario, verificamos la contraseña
            if (!usuario.getContrasena().equalsIgnoreCase(Contrasena)) {
                throw new ValidacionIncorrectaException("Usuario y/o contraseña inválidos", null, log);
            }

            return usuario;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adUsuario. validar. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    // Devuelve la clase Usuario rellenada pasando un ID
    public Usuario recuperarUsuario(long ID) throws AbadiaException {
        String sSQL = "Select * From usuario Where USUARIOID = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setLong(1, ID);
            rs = ps.executeQuery();
            if (rs.next()) {
                // añadir la clase
                Usuario usuario = new Usuario();
                usuario.setIdDeUsuario(rs.getInt("USUARIOID")); // El nick ya existe
                usuario.setNombre(rs.getString("NOMBRE"));
                usuario.setPrimerApellido(rs.getString("APELLIDO1"));
                usuario.setSegundoApellido(rs.getString("APELLIDO2"));
                usuario.setNick(rs.getString("NICK"));
                usuario.setEmail(rs.getString("EMAIL"));
                usuario.setIdDeIdioma(rs.getShort("IDIOMAID"));
                usuario.setContrasena(rs.getString("CONTRASENA"));
                usuario.setAdministrador(rs.getLong("USUARIO_TIPO"));
                usuario.setRegistrado(rs.getLong("REGISTRADO"));
                usuario.setBloqueado(getBloqueado(usuario.getIdDeUsuario()));
                usuario.setCongelado(rs.getShort("Abadia_congelada"));
                usuario.setPais(rs.getInt("PAIS"));
                usuario.setEdad(rs.getInt("EDAD"));
                usuario.setFecha_alta(rs.getString("FECHAALTA"));
                usuario.setAceptaNormas(rs.getBoolean("ACEPTA_NORMAS"));
                // añadirlo a la session???
                return usuario;
            } else {
                throw new UsuarioNoEncontradoException("No se ha encontrado el usuario", null, log);
            }
        } catch (SQLException e) {
            throw new AbadiaSQLException("adUsuario. recuperarUsuario. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    // Devuelve la clase Usuario rellenada pasando un ID
    public String recuperarUsuarioAbadia(String Nick) throws AbadiaException {
        String sSQL = "Select a.nombre From usuario u, abadia a Where u.usuarioid = a.usuarioid and u.nick = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setString(1, Nick);
            rs = ps.executeQuery();
            if (rs.next()) {
                // añadir la clase
                return rs.getString("NOMBRE");
            } else return "";

        } catch (SQLException e) {
            throw new AbadiaSQLException("adUsuario. recuperarUsuarioAbadia. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    //Recupera los datos de un usuario a partir de su nick
    public Usuario recuperarUsuarioPorNick(String Nick) throws AbadiaException {
        String sSQL = "Select contrasena, email, idiomaid From usuario Where nick = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setString(1, Nick);
            rs = ps.executeQuery();

            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setContrasena(rs.getString("CONTRASENA"));
                usuario.setEmail(rs.getString("EMAIL"));
                usuario.setIdDeIdioma(rs.getShort("IDIOMAID"));
                // añadir la clase
                return usuario;
            } else return null;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adUsuario. recuperarUsuarioPorNick. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }


    // Eliminar el Usuario por ID
    public boolean eliminar(long ID) throws AbadiaSQLException {
        String sSQL = "delete From usuario Where USUARIOID = ?";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setLong(1, ID);
            return ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adUsuario. eliminar. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    // Borrar el usuario cuando se pasa el usuario ;-)
    public boolean eliminar(Usuario usr) throws AbadiaSQLException {
        return eliminar(usr.getIdDeUsuario());
    }

    public void actualizarFechaConexion(long idUsuario) throws AbadiaException {
        log.debug("adUsuario. actualizarFechaConexion. idUsuario: " + idUsuario);
        String sSQL = "Update usuario set ULTIMACONEXION = ? WHERE USUARIOID= ?";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setString(1, CoreTiempo.getTiempoRealStringConHoras());
            ps.setLong(2, idUsuario);
            ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adUsuario. actualizarFechaConexion. error SQL", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /**
     * Inserta un registro en la tabla de usuario_bloqueo indicando la fecha de bloqueo y el numero de días que ha de durar.
     *
     * @param idUsuario
     * @param dias
     * @throws AbadiaException
     */
    public void bloquearUsuario(long idUsuario, int dias, String motivo) throws AbadiaException {
        adUtils utilsAD;
        try {
            String sSQL = "insert into usuario_bloqueo (USUARIOID, FECHA_BLOQUEO, DIAS_BLOQUEO, MOTIVO) values(" + idUsuario + ", curdate(), " + dias + ", '" + motivo + "')";
            utilsAD = new adUtils(con);
            utilsAD.execSQL(sSQL);
        } catch (AbadiaSQLException e) {
            throw new AbadiaSQLException("adUsuario. bloquearUsuario. SQLException", e, log);
        }
    }

    /**
     * Inserta un registro en la tabla de usuario_bloqueo indicando la fecha de bloqueo y el numero de días que ha de durar.
     *
     * @param nick
     * @param dias
     * @param motivo
     * @throws AbadiaException
     */
    public void bloquearUsuario(String nick, int dias, String motivo) throws AbadiaException {
        adUtils utilsAD = null;
        try {
            int idUsuario = 0;
            String sSQLNick = "select usuarioid from usuario where nick = '" + nick + "'";
            utilsAD = new adUtils(con);
            idUsuario = utilsAD.getSQL(sSQLNick, 0);
            if (idUsuario > 0) {
                bloquearUsuario(idUsuario, dias, motivo);
            }
        } catch (AbadiaSQLException e) {
            throw new AbadiaSQLException("adUsuario. bloquearUsuario. SQLException", e, log);
        }
    }

    /**
     * Elimina el registro en la tabla de usuario_bloqueo del usuario recibido
     *
     * @param idUsuario
     * @throws AbadiaException
     */
    public void desBloquearUsuario(int idUsuario) throws AbadiaException {
        adUtils utilsAD;
        try {
            String sSQL = "delete from usuario_bloqueo where usuarioid = " + idUsuario;
            utilsAD = new adUtils(con);
            utilsAD.execSQL(sSQL);
        } catch (AbadiaSQLException e) {
            throw new AbadiaSQLException("adUsuario. desBloquearUsuario. SQLException", e, log);
        }

    }

    /**
     * Actualiza el tipo de usuario
     *
     * @param idUsuario
     * @param tipo
     * @throws AbadiaException
     */
    public void actualizarTipoUsuario(long idUsuario, short tipo) throws AbadiaException {
        adUtils utilsAD;
        try {
            String sSQL = "update usuario set USUARIO_TIPO = " + tipo + " where usuarioid = " + idUsuario;
            utilsAD = new adUtils(con);
            utilsAD.execSQL(sSQL);
        } catch (AbadiaSQLException e) {
            throw new AbadiaSQLException("adUsuario. actualizarTipoUsuario. SQLException", e, log);
        }

    }

    /**
     * Actualiza el tipo de usuario
     *
     * @param idUsuario
     * @param p_bEstado
     * @throws AbadiaException
     */
    public void actualizarAceptacionNormas(long idUsuario, boolean p_bEstado) throws AbadiaException {
        adUtils utilsAD;
        try {
            String sSQL = "update usuario set ACEPTA_NORMAS = " + p_bEstado + " where usuarioid = " + idUsuario;
            utilsAD = new adUtils(con);
            utilsAD.execSQL(sSQL);
        } catch (AbadiaSQLException e) {
            throw new AbadiaSQLException("adUsuario. actualizarAceptacionNormas. SQLException", e, log);
        }

    }


    /**
     * elimina registro de usuario
     *
     * @param idUsuario
     * @throws AbadiaException
     */
    public void eliminarUsuario(long idUsuario) throws AbadiaException {
        adUtils utilsAD;
        try {
            String sSQL = "delete From usuario Where USUARIOID = " + idUsuario;
            utilsAD = new adUtils(con);
            utilsAD.execSQL(sSQL);
        } catch (AbadiaSQLException e) {
            throw new AbadiaSQLException("adUsuario. eliminarUsuario. SQLException", e, log);
        }
    }

    // Actualizar dirección IP
    public void actualizarIPUsuario(long idUsuario, String IP) throws AbadiaException {
        log.debug("adUsuario. actualizarFechaConexion. idUsuario: " + idUsuario);
        String sSQL = "Update usuario set IP_ULTIMA = ? WHERE USUARIOID= ?";
        PreparedStatement ps = null;

        try {
            ps = con.prepareStatement(sSQL);
            ps.setString(1, IP);
            ps.setLong(2, idUsuario);
            ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adUsuario. actualizarIPUsuario. error SQL", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    public ArrayList<Usuario> recuperarUsuariosBloqueados() throws AbadiaException {
        String sSQL = "select u.usuarioid as USUARIO_ID, u.nick, a.nombre as NOMBRE_ABADIA, ub.fecha_bloqueo, ub.dias_bloqueo, ub.motivo " +
                " from usuario u, abadia a, usuario_bloqueo ub " +
                " where u.usuarioid = ub.usuarioid and u.usuarioid = a.usuarioid";

        ResultSet rs = null;
        PreparedStatement ps = null;
        Usuario usuario;
        ArrayList<Usuario> alBloqueados = new ArrayList<Usuario>();
        try {
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                usuario = new Usuario();
                usuario.setIdDeUsuario(rs.getInt("USUARIO_ID"));
                usuario.setNick(rs.getString("nick"));
                usuario.setFecha_bloqueo(Utilidades.formatStringFromDB(rs.getString("fecha_bloqueo")));
                usuario.setNombre(rs.getString("NOMBRE_ABADIA"));
                usuario.setDias_bloqueo(rs.getInt("dias_bloqueo"));
                usuario.setMotivo(rs.getString("MOTIVO"));
                alBloqueados.add(usuario);
            }
            return alBloqueados;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adUsuario. recuperarUsuariosBloqueados. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * recupera los usuarios que han completado el registro y están esperando confirmación
     *
     * @return
     * @throws AbadiaException
     */
    public ArrayList<Usuario> recuperarUsuariosAltasPendientes() throws AbadiaException {
        String sSQL = "Select u.USUARIOID, u.NICK, u.NOMBRE, u.APELLIDO1, u.APELLIDO2, u.EMAIL, u.IP_ALTA, u.FECHAALTA, u.EDAD " +
                " From usuario AS u " +
                " Where u.USUARIO_TIPO = " + Constantes.USUARIO_PENDIENTE_ALTA;

        ResultSet rs = null;
        PreparedStatement ps = null;
        Usuario usuario;
        ArrayList<Usuario> alBloqueados = new ArrayList<Usuario>();
        try {
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                usuario = new Usuario();
                usuario.setIdDeUsuario(rs.getInt("USUARIOID"));
                usuario.setNick(rs.getString("NICK"));
                usuario.setNombre(rs.getString("NOMBRE"));
                usuario.setEdad(rs.getInt("EDAD"));
                usuario.setPrimerApellido(rs.getString("APELLIDO1"));
                usuario.setSegundoApellido(rs.getString("APELLIDO2"));
                usuario.setEmail(rs.getString("EMAIL"));
                usuario.setIpActual(rs.getString("IP_ALTA"));
                usuario.setFecha_alta(Utilidades.formatStringFromDB(rs.getString("FECHAALTA")));
                alBloqueados.add(usuario);
            }
            return alBloqueados;

        } catch (SQLException e) {
            throw new AbadiaSQLException("adUsuario. recuperarUsuariosAltasPendientes. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }


    }

    // Devuelve número de usuarios registrados o activos
    public int getUsuariosRegistrados(int tipo) throws AbadiaException {
        String sSQL = "";

        if (tipo == 0) {
            sSQL = "SELECT Count(*) FROM usuario";
        } else if (tipo == 1) {
            sSQL = "SELECT count(*) FROM usuario u, abadia a where u.usuarioid = a.usuarioid";
        } else if (tipo == 2) {
            sSQL = "SELECT count(*) FROM usuario u where u.usuario_tipo = 99";
        }
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
            throw new AbadiaSQLException("adUsuario. getUsuariosRegistrados. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    /**
     * Devuelve si el usuario está bloqueado o no.
     *
     * @param idUsuario
     * @return 0 - No bloqueado, 1 - Bloqueado
     * @throws AbadiaException
     */
    public int getBloqueado(long idUsuario) throws AbadiaException {
        String sSQL = "select * from usuario_bloqueo where usuarioid = " + idUsuario;

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            if (rs.next()) {
                return 1;
            } else {
                return 0;
            }
        } catch (SQLException e) {
            throw new AbadiaSQLException("adUsuario. getBloqueado. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Devuelve si el usuario está bloqueado o no.
     *
     * @param nick
     * @return 0 - No bloqueado, 1 - Bloqueado
     * @throws AbadiaException
     */
    public int getBloqueado(String nick) throws AbadiaException {
        String sSQL = "select * from usuario_bloqueo where usuarioid in (select usuarioid from usuario where nick = ? )";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setString(1, nick);
            rs = ps.executeQuery();
            if (rs.next()) {
                return 1;
            } else {
                return 0;
            }
        } catch (SQLException e) {
            throw new AbadiaSQLException("adUsuario. getBloqueado. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    /**
     * Recupera el numero de reintentos de acceso realizados por un usuario.
     *
     * @param nick
     * @return
     * @throws AbadiaException
     */
    public int recuperarReintentos(String nick) throws AbadiaException {
        String sSQL = "Select REINTENTOS from usuario where nick = '" + nick + "'";
        adUtils utils = new adUtils(con);
        int reintentos = utils.getSQL(sSQL, 0);
        return reintentos;
    }

    /**
     * Incrementa en 1 el contador de reintentos de login incorrectos.
     *
     * @param nick
     * @throws AbadiaException
     */
    public void incrementarReintentos(String nick) throws AbadiaException {
        String sSQL = "update usuario set REINTENTOS = REINTENTOS + 1 where nick = '" + nick + "'";
        adUtils utils = new adUtils(con);
        utils.execSQL(sSQL);
    }

    /**
     * Inicializa el contador de reintentos de acceso.
     *
     * @param idUsuario
     * @throws AbadiaException
     */
    public void resetearReintentos(long idUsuario) throws AbadiaException {
        String sSQL = "update usuario set REINTENTOS = 0 where usuarioid = " + idUsuario;
        adUtils utils = new adUtils(con);
        utils.execSQL(sSQL);
    }

    // Alta dirección IP
    public void altaIPUsuario(long idUsuario, String IP) throws AbadiaException {
        log.debug("adUsuario. actualizarFechaConexion. idUsuario: " + idUsuario);
        String sSQL = "Update usuario set IP_ALTA = ? WHERE USUARIOID= ?";
        PreparedStatement ps = null;

        try {
            ps = con.prepareStatement(sSQL);
            ps.setString(1, IP);
            ps.setLong(2, idUsuario);
            ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adUsuario. altaIPUsuario. error SQL", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    // Devuelve los tramposos
    public ArrayList<Table> recuperarSupporters(int pagina, Point total, MessageResources resource) throws AbadiaException {
        String sSQL = "SELECT  * ";
        String sSQLFrom =
                "FROM `usuario` u, usuario_supporters us " +
                        "where u.usuarioid = us.usuarioid and registrado = 1 ";
        sSQL = sSQL + sSQLFrom + "ORDER BY u.nombre, u.apellido1 desc ";

        adUtils utils = new adUtils(con);
        total.x = utils.getSQL("Select count(*) " + sSQLFrom, 0);

        String sHTML = "";
        int n = 1;
        HTML cHTML = new HTML();
        ArrayList<Table> supporters = new ArrayList<Table>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            Table supporter;

            sSQL = sSQL + " LIMIT " + (pagina * Constantes.REGISTROS_PAGINA) + "," + Constantes.REGISTROS_PAGINA;

            sHTML = cHTML.getTD_TitleTable(resource.getMessage("supporters.nombre"), "", 0);
            sHTML += cHTML.getTD_TitleTable(resource.getMessage("supporters.donacion"), "", 0);
            sHTML += cHTML.getTD_TitleTable(resource.getMessage("supporters.ubicacion"), "", 0);
            supporter = new Table(0, "<tr>" + sHTML + "</tr>");
            supporters.add(supporter);

            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                sHTML = cHTML.getTD(HTML.ALIGN_NONE, null, rs.getString("nombre") + " " + rs.getString("apellido1"));
                sHTML += cHTML.getTD(HTML.ALIGN_RIGHT, null, rs.getString("cantidad") + " &euro;");
                sHTML += cHTML.getTD(HTML.ALIGN_NONE, null, rs.getString("ubicacion"));
                supporter = new Table(0, cHTML.getTR(n++, sHTML));
                supporters.add(supporter);
            }
            return supporters;
            // Ejecutarlo
        } catch (SQLException e) {
            throw new AbadiaSQLException("adUsuario. recuperarSupporters", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    // Devuelve los tramposos
    public ArrayList<Table> recuperarPosiblesTramposos(int pagina, Point total) throws AbadiaException {
        String sSQL = "SELECT  u.usuarioid uid, u.nick unick, u.ip_ultima uip, u.ultimaconexion uconx,  a.abadiaid aid, a.nombre anomb, " +
                "u1.usuarioid uid1, u1.nick unick1, u1.ip_ultima uip1, u1.ultimaconexion uconx1,  a1.abadiaid aid1, a1.nombre anomb1 ";
        String sSQLFrom =
                "FROM `usuario` u, usuario u1, abadia a, abadia a1 " +
                        "where u.ip_ultima = u1.ip_ultima and u.usuario_tipo = 0 and u1.usuario_tipo = 0 and u.usuarioid <> u1.usuarioid and u.ip_ultima <> '192.168.0.2' and u1.ip_ultima <> '192.168.0.2'  and u.ip_ultima <> '' and u1.ip_ultima <> '' " +
                        "and a.usuarioid = u.usuarioid and a1.usuarioid = u1.usuarioid and u.usuarioid not in (select usuarioid from usuario_bloqueo) and u1.bloqueado not in (select usuarioid from usuario_bloqueo) ";
        sSQL = sSQL + sSQLFrom + "ORDER BY u.ultimaconexion desc ";

        adUtils utils = new adUtils(con);
        total.x = utils.getSQL("Select count(*) " + sSQLFrom, 0);

        String sHTML = "";
        int n = 1;
        HTML cHTML = new HTML();
        ArrayList<Table> tramposos = new ArrayList<Table>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            Table tramposo;

            sSQL = sSQL + " LIMIT " + (pagina * Constantes.REGISTROS_PAGINA) + "," + Constantes.REGISTROS_PAGINA;

            sHTML = cHTML.getTD_TitleTable("Usuario 1", "", 0);
            sHTML = sHTML + cHTML.getTD_TitleTable("IP 1", "", 0);
            sHTML = sHTML + cHTML.getTD_TitleTable("U.Conexión 1", "", 0);
            sHTML = sHTML + cHTML.getTD_TitleTable("Abadia 1", "", 0);
            //2
            sHTML = sHTML + cHTML.getTD_TitleTable("Usuario 2", "", 0);
            sHTML = sHTML + cHTML.getTD_TitleTable("IP 2", "", 0);
            sHTML = sHTML + cHTML.getTD_TitleTable("U.Conexión 2", "", 0);
            sHTML = sHTML + cHTML.getTD_TitleTable("Abadia 2", "", 0);
            tramposo = new Table(0, "<tr>" + sHTML + "</tr>");
            tramposos.add(tramposo);

            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                sHTML = cHTML.getTD(HTML.ALIGN_NONE, null, rs.getString("unick") + "<font size=\"1\"> (" + rs.getString("uid") + ")</font>");
                sHTML = sHTML + cHTML.getTD(HTML.ALIGN_NONE, null, rs.getString("uip"));
                sHTML = sHTML + cHTML.getTD(HTML.ALIGN_NONE, null, rs.getString("uconx"));
                sHTML = sHTML + cHTML.getTD(HTML.ALIGN_NONE, null, rs.getString("anomb") + "<font size=\"1\"> (" + rs.getString("aid") + ")</font>");
                //2
                sHTML = sHTML + cHTML.getTD(HTML.ALIGN_NONE, null, rs.getString("unick1") + "<font size=\"1\"> (" + rs.getString("uid1") + ")</font>");
                sHTML = sHTML + cHTML.getTD(HTML.ALIGN_NONE, null, rs.getString("uip1"));
                sHTML = sHTML + cHTML.getTD(HTML.ALIGN_NONE, null, rs.getString("uconx1"));
                sHTML = sHTML + cHTML.getTD(HTML.ALIGN_NONE, null, rs.getString("anomb1") + "<font size=\"1\"> (" + rs.getString("aid1") + ")</font>");
                tramposo = new Table(0, cHTML.getTR(n++, sHTML));
                tramposos.add(tramposo);
            }
            return tramposos;
            // Ejecutarlo
        } catch (SQLException e) {
            throw new AbadiaSQLException("adUsuario. recuperarPosiblesTramposos", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public void addRegistroIP(String nick, String ip, int logado) throws AbadiaException {
        String sSQL = "insert into usuario_ip (nick, fecha_acceso, ip, logado) values ('" + nick + "', sysdate(), '" + ip + "', " + logado + ")";
        adUtils utils;
        try {
            utils = new adUtils(con);
            utils.execSQL(sSQL);
        } catch (AbadiaException e) {
            log.error("adUsuario. addRegistroIP", e);
            throw e;
        }
    }

    public Vector<String> recuperarIPsUsuarioPorAbadia(int idAbadia) throws AbadiaException {
        String sSQL = "Select distinct ui.IP from usuario_ip ui, usuario u, abadia a where ui.nick = u.nick and u.usuarioid = a.usuarioid and a.abadiaid = " + idAbadia + " and ui.logado = 1";
        Vector<String> vIPs;
        adUtils utils = new adUtils(con);
        vIPs = utils.getSQLVector(sSQL);
        return vIPs;
    }
}
