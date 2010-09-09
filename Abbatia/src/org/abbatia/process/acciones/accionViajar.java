package org.abbatia.process.acciones;

import org.abbatia.base.AccionesBase;
import org.abbatia.bean.Solicitud;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.process.bbean.ProcesosAccionesBBean;
import org.abbatia.utils.Constantes;

/**
 * Created by Benjamín Rodríguez.
 * User: Benjamin
 * Date: 12-mar-2005
 * Time: 21:51:02
 */
public class accionViajar extends AccionesBase {
    /**
     * Activa el viaje de un monje
     *
     * @param solicitud
     * @throws org.abbatia.exception.base.AbadiaException
     *
     */
    public void accionSi(Solicitud solicitud) throws AbadiaException {
        ProcesosAccionesBBean oAccionesBBean;
        oAccionesBBean = new ProcesosAccionesBBean();
        oAccionesBBean.accionViajarSi(solicitud);
    }

    /**
     * Elimina el registro de viaje de un monje
     *
     * @param solicitud
     * @throws org.abbatia.exception.base.AbadiaException
     *
     */
    public void accionNo(Solicitud solicitud) throws AbadiaException {
        ProcesosAccionesBBean oAccionesBBean;
        oAccionesBBean = new ProcesosAccionesBBean();
        oAccionesBBean.accionViajarNo(solicitud);
    }

    /**
     * Cuando se elimine una solicitud, debemos evaluar el estado y determinar
     * las operaciones asociadas: Eliminacion de registro de viaje, devolución de pasta
     * en caso de que el viaje esté en curso, eliminacion de bloqueos sobre actividades
     * si el viaje era par copiar un libro....
     *
     * @param solicitud
     * @throws org.abbatia.exception.base.AbadiaException
     *
     */
    public void accionEliminar(Solicitud solicitud) throws AbadiaException {
        //si la solicitud está pendiente...
        if (solicitud.getEstado() == Constantes.SOLICITUD_ESTADO_PENDIENTE) {
            ProcesosAccionesBBean oAccionesBBean;
            oAccionesBBean = new ProcesosAccionesBBean();
            oAccionesBBean.accionViajarEliminar(solicitud);
        }
    }
}
