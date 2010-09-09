package org.abbatia.adbean;

import org.abbatia.adbean.base.adbeans;
import org.abbatia.bean.Usuario;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.base.AbadiaException;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class adForum extends adbeans {
    private static Logger log = Logger.getLogger(adForum.class.getName());

    /**
     * Instancia la clase sin obtener una nueva conexión
     *
     * @param con
     * @throws AbadiaException
     */
    public adForum(Connection con) throws AbadiaException {
        super(con);
    }


    public void validar(Usuario user) throws AbadiaException {
        String sSQL = "SELECT username, user_password, user_email FROM `phpbb_users` Where user_id = ? LIMIT 1";
        String sSQLUpdate = "UPDATE `phpbb_users` SET username = ?, user_password = ?, user_email = ? Where user_id = ?";
        String sSQLInsert = "INSERT INTO `phpbb_users` (user_id, username, user_password, user_email, user_lang, user_regdate) VALUES (?, ?, ?, ?, 'spanish', UNIX_TIMESTAMP(now()))";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setLong(1, user.getIdDeUsuario());
            rs = ps.executeQuery();
            if (rs.next()) {
                // Comprobar que el nombre y la contraseña sean correctos
                if ((!rs.getString("username").equals(user.getNick())) || (!rs.getString("user_password").equals(md5(user.getContrasena()))) || (!rs.getString("user_email").equals(user.getEmail()))) {
                    ps = con.prepareStatement(sSQLUpdate);
                    ps.setString(1, user.getNick());
                    ps.setString(2, md5(user.getContrasena()));
                    ps.setString(3, user.getEmail());
                    ps.setLong(4, user.getIdDeUsuario());
                    ps.execute();
                }
            } else {
                ps = con.prepareStatement(sSQLInsert);
                ps.setLong(1, user.getIdDeUsuario());
                ps.setString(2, user.getNick());
                ps.setString(3, md5(user.getContrasena()));
                ps.setString(4, user.getEmail());
                ps.execute();
            }
        } catch (SQLException e) {
            throw new AbadiaSQLException("adForum. validar. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(rs);
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    // Eliminar el Usuario del foro por ID
    public boolean eliminar(long ID) throws AbadiaSQLException {
        String sSQL = "delete From `phpbb_users` Where user_id = ?";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setLong(1, ID);
            return ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adForum. eliminar. SQLException.", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }
    }

    public void bloquearUsuario(long idUsuario) throws AbadiaException {
        String sSQL = "Update phpbb_users set user_active = 0 WHERE user_id = ?";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sSQL);
            ps.setLong(1, idUsuario);
            ps.execute();
        } catch (SQLException e) {
            throw new AbadiaSQLException("adUsuario. bloquearUsuario. SQLException", e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(ps);
        }

    }

    // HEX
    public String hex(byte[] array) {
        StringBuffer sb = new StringBuffer();
        for (byte anArray : array) {
            sb.append(Integer.toHexString((anArray & 0xFF) | 0x100).toUpperCase().substring(1, 3));
        }
        return sb.toString();
    }

    // MD5
    public String md5(String message) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return hex(md.digest(message.getBytes("CP1252"))).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
        }
        catch (UnsupportedEncodingException e) {
        }
        return null;
    }
}
