package org.abbatia.process;

import org.abbatia.base.ProcesoBase;
import org.abbatia.bbean.UtilsBBean;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.process.bbean.ProcesosAlimentosBBean;
import org.abbatia.utils.Constantes;
import org.apache.log4j.Logger;

/**
 * Created by Benjamín Rodríguez.
 * User: Benjamin
 * Date: 23-feb-2006
 * Time: 23:31:50
 */
public class PRAlimentarMonjes extends ProcesoBase {
    private static Logger log = Logger.getLogger(PRAlimentarMonjes.class.getName());

    /**
     * método que implementará la funcionalidad que se debe ejecutar con cada proceso
     */
    public void run() throws AbadiaException {
        String szSQL = "UPDATE `monje_alimentacion` " +
                "set ha_comido_FamiliaID_1 = -1, ha_comido_FamiliaID_2 = -1, ha_comido_FamiliaID_3 = -1, " +
                "ha_comido_FamiliaID_4 = -1, ha_comido_FamiliaID_5 = -1 ";

        UtilsBBean oUtilsBBean = new UtilsBBean();
        oUtilsBBean.ejecutarSQL(szSQL);

        ProcesosAlimentosBBean pr = new ProcesosAlimentosBBean();
        pr.alimentar_monjes(Constantes.MONJE_VIVO);
        pr.alimentar_monjes(Constantes.MONJE_VISITA);
    }
}
