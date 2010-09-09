/**
 * Created by Benjamín Rodríguez.
 * User: benjamin
 * Date: 05-may-2009
 * Time: 1:20:16
 */
package org.abbatia.bbean;

import org.apache.log4j.Logger;

import java.util.HashMap;

public class GestionDblClickBBean {
    private static Logger log = Logger.getLogger(GestionDblClickBBean.class.getName());
    private static GestionDblClickBBean ourInstance = new GestionDblClickBBean();
    private HashMap<String, Long> hmUsuarios;

    public static GestionDblClickBBean getInstance() {
        return ourInstance;
    }

    private GestionDblClickBBean() {
        hmUsuarios = new HashMap<String, Long>();
    }

    public static Long getUserTime(String p_szUsuarioId) {
        if (getInstance().hmUsuarios.containsKey(p_szUsuarioId)) {
            return getInstance().hmUsuarios.get(p_szUsuarioId);
        } else {
            return (long) 0;
        }
    }

    public static void setUserTime(String p_szUsuarioId, long p_lTime) {
        getInstance().hmUsuarios.put(p_szUsuarioId, p_lTime);
    }

    public static void resetUserTime(String p_szUsuarioId) {
        getInstance().hmUsuarios.remove(p_szUsuarioId);
    }


    public static boolean evaluarDblClick(String p_szUsuarioId, String p_szDestino) {
        long lMilisegundos = System.currentTimeMillis();
        long lDiferencia;

        lDiferencia = (lMilisegundos - getUserTime(p_szUsuarioId));
        if (lDiferencia > 10000) {
            setUserTime(p_szUsuarioId, lMilisegundos);
            return true;
        } else {
            log.info("*********-" + p_szUsuarioId + " - Está haciendo multiclick sobre la acción: " + p_szDestino + ". Transcurridos " + lDiferencia + " Milisegundos ");
            resetUserTime(p_szUsuarioId);
            return false;
        }
    }
}
