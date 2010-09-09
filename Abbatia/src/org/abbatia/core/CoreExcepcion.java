package org.abbatia.core;

import org.abbatia.bean.ExcepcionCount;
import org.abbatia.bean.ExcepcionMax;
import org.abbatia.exception.AbadiaNamingException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.apache.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * Created by Benjamín Rodríguez.
 * User: Benjamin
 * Date: 04-sep-2005
 * Time: 2:08:24
 */
public class CoreExcepcion {
    private static Logger log = Logger.getLogger(CoreExcepcion.class.getName());

    public static int controlExcepciones(String nombreExcepcion, HttpSession sesion) throws AbadiaException {
        HashMap<String, ExcepcionCount> htExcepcionesUsuario = null;
        ExcepcionCount contadorExcepciones = null;
        ExcepcionMax maxExcepciones = null;
        try {
            Context initCtx = new InitialContext();
            Object objExcepciones = initCtx.lookup(Constantes.EXCEPCIONES_CONTROLADAS);
            //si el objeto no esta informado devolvemos 0
            if (objExcepciones == null) return 0;

            //recuperamos la hash con los datos de las excepciones controladas.
            HashMap<String, ExcepcionMax> htExcepciones = (HashMap<String, ExcepcionMax>) objExcepciones;

            Object objExcepcionesUsuario = sesion.getAttribute(Constantes.EXCEPCIONES_CONTROLADAS);
            //si se trata de la primera excepcion que capturamos para ese usuario...
            if (objExcepcionesUsuario == null) {
                //creo un nuevo objeto de excepcion
                contadorExcepciones = new ExcepcionCount(nombreExcepcion, 0);
                //creo un nuevo objeto Hashtable
                htExcepcionesUsuario = new HashMap<String, ExcepcionCount>();
            } else {
                //recupero el objeto hashtable de la sesión
                htExcepcionesUsuario = (HashMap<String, ExcepcionCount>) objExcepcionesUsuario;
                //recupero de la sesión la hash de excepciones de usuario
                Object objExcepcion = htExcepcionesUsuario.get(nombreExcepcion);
                //si la excepción no esta inventariada en la sesión...
                if (objExcepcion == null) {
                    //creo un nuevo objeto de excepcion
                    contadorExcepciones = new ExcepcionCount(nombreExcepcion, 0);
                } else contadorExcepciones = (ExcepcionCount) objExcepcion;
            }

            //Incremento en 1 el contador para esa excepcion
            contadorExcepciones.setCount(contadorExcepciones.getCount() + 1);

            Object objMaxExcepciones = htExcepciones.get(nombreExcepcion);
            //si la excepción tratada no esta cargada en la hash inicial...
            if (objMaxExcepciones == null) {
                //devolvemos 0 para que no compute
                return 0;
            }

            //Añado a la hash la referencia al objeto excepción-contador inicializado
            htExcepcionesUsuario.put(nombreExcepcion, contadorExcepciones);
            //cargo la hash en sesión
            sesion.setAttribute(Constantes.EXCEPCIONES_CONTROLADAS, htExcepcionesUsuario);


            maxExcepciones = (ExcepcionMax) objMaxExcepciones;

            //si se ha superado el maximo número de excepciones permitidas...
            if (contadorExcepciones.getCount() >= maxExcepciones.getMax()) {
                return maxExcepciones.getAccion();
            } else {
                return 0;
            }


        } catch (NamingException e) {
            throw new AbadiaNamingException("CoreExcepcion. controExcepciones. NamingException", e, log);
        }

    }
}
