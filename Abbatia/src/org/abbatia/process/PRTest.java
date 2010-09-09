package org.abbatia.process;

import org.abbatia.base.ProcesoBase;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.process.bbean.ProcesosAlimentosBBean;
import org.abbatia.process.bbean.ProcesosVariosBBean;
import org.abbatia.utils.Constantes;

/**
 * Created by Benjamín Rodríguez.
 * User: Benjamin
 * Date: 23-feb-2006
 * Time: 23:31:50
 */
public class PRTest extends ProcesoBase {
    /**
     * método que implementará la funcionalidad que se debe ejecutar con cada proceso
     */
    public void run() throws AbadiaException {
        ProcesosVariosBBean oProcesosBBean;
        oProcesosBBean = new ProcesosVariosBBean();
        oProcesosBBean.gestionNivelAbadias();
        ProcesosAlimentosBBean pr = new ProcesosAlimentosBBean();
        pr.alimentar_monjes(Constantes.MONJE_VIVO);
    }

}
