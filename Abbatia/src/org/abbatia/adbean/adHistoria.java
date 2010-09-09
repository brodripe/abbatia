package org.abbatia.adbean;

import org.abbatia.adbean.base.adbeans;
import org.abbatia.core.CoreTiempo;
import org.abbatia.exception.base.AbadiaException;
import org.apache.log4j.Logger;

import java.sql.Connection;

public class adHistoria extends adbeans {
    private static Logger log = Logger.getLogger(adHistoria.class.getName());

    /**
     * Instancia la clase sin obtener una nueva conexión
     *
     * @param con
     * @throws AbadiaException
     */
    public adHistoria(Connection con) throws AbadiaException {
        super(con);
    }


    public void anyadirHistoria(long AbadiaID, int MonjeID, int JerarquiaID, int LiteralID) throws AbadiaException {
        // Mensajes de muertes de monjes!
        if ((LiteralID >= 13500) && (LiteralID < 14000)) {
            // Averiguar el literal de la jerarquia
            int lit_jerarquia = 230 + JerarquiaID;
            adUtils utils = new adUtils(con);
            String nom_monje = utils.getSQL("Select nombre from monje where MONJEID = " + MonjeID, "nombre");
            String ape_monje = utils.getSQL("Select apellido1 from monje where MONJEID = " + MonjeID, "nombre");
            utils.execSQL("INSERT INTO `historia`\n" +
                    " (`FECHA`, `LITERALID`, `PARAM_LITERALID1`, `PARAM2`, `PARAM3` )\n" +
                    "VALUES ('" + CoreTiempo.getTiempoAbadiaStringConHoras() + "', " + LiteralID + ", " + lit_jerarquia + ", '" + nom_monje + "', '" + ape_monje + "' )");
        }
    }

}
