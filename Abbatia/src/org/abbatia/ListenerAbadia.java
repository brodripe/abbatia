package org.abbatia;

import org.abbatia.filter.ResponseCache;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpSessionEvent;


public class ListenerAbadia implements javax.servlet.http.HttpSessionListener {
    private static Logger log = Logger.getLogger(ListenerAbadia.class.getName());
    private static int contador = 0;

    public void sessionCreated(HttpSessionEvent event) {
        log.debug("ListenerAbadia. sessionCreated. Se ha creado una sesion");
        contador++;
        //Utilidades.incrementarContador();
    }

    public void sessionDestroyed(HttpSessionEvent event) {
        log.debug("ListenerAbadia. sessionCreated. Se ha eliminado una sesion");
        ResponseCache.removeInstanceFromPool(event.getSession().getId());
        //Utilidades.decrementarContador();
        if (contador > 0) contador--;
    }


    public static int getContador() {
        return contador;
    }
}
