package org.abbatia.base;

import org.abbatia.bean.Solicitud;
import org.abbatia.exception.base.AbadiaException;

/**
 * todas las acciones asociadas a las solicitudes deberan definirse hereando de esta clase
 */
public abstract class AccionesBase {
    /**
     * método que implementará la funcionalidad que se debe ejecutar en caso de ser aprobada
     * la solicitud
     *
     * @param solicitud
     */
    public abstract void accionSi(Solicitud solicitud) throws AbadiaException;

    /**
     * método que implementará la funcionalidad que se debe ejecutar en caso de NO se aprobada
     * la solicitud
     *
     * @param solicitud
     */
    public abstract void accionNo(Solicitud solicitud) throws AbadiaException;

    /**
     * método que implementará la funcionalidad que debe ejecutarse en caso de eliminar la solicitud
     *
     * @param solicitud
     * @throws AbadiaException
     */
    public abstract void accionEliminar(Solicitud solicitud) throws AbadiaException;

}
