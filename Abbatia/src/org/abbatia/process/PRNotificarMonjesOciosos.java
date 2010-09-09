package org.abbatia.process;

import org.abbatia.base.ProcesoBase;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.process.bbean.ProcesosMonjesBBean;

/**
 * Created by Benjamín Rodríguez.
 * User: Benjamin
 * Date: 23-feb-2006
 * Time: 23:31:50
 */
public class PRNotificarMonjesOciosos extends ProcesoBase {
    /**
     * método que implementará la funcionalidad que se debe ejecutar con cada proceso
     */
    public void run() throws AbadiaException {
        ProcesosMonjesBBean oMonjesBBean;
        oMonjesBBean = new ProcesosMonjesBBean();
        oMonjesBBean.notificarMonjesOciosos();
    }

}
