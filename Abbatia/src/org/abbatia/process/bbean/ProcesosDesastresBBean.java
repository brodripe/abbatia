package org.abbatia.process.bbean;

import org.abbatia.adbean.adEdificio;
import org.abbatia.adbean.adLiterales;
import org.abbatia.adbean.adMensajes;
import org.abbatia.adbean.adUtils;
import org.abbatia.bbean.MonjeBBean;
import org.abbatia.bean.Edificio;
import org.abbatia.bean.Mensajes;
import org.abbatia.dbms.ConnectionFactory;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.process.adbean.adProcesos;
import org.abbatia.process.utils.ProcesosUtils;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.Utilidades;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.util.ArrayList;

public class ProcesosDesastresBBean extends ProcesosUtils {

    private static Logger log = Logger.getLogger(ProcesosDesastresBBean.class.getName());

    /* Función que busca una abbatia cualquiera o no... y realiza el desastre ;-)
    */
    public void realizar_desastre(int abadia_maligna, boolean fijo) throws AbadiaException {

        String sTrace = this.getClass() + ".realizar_desastre(" + abadia_maligna + ")";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);


        adUtils oUtilsAD;
        adProcesos oProcesosAD;

        // Incializar las variab.
        String hora;
        int abadiaID;
        // El proceso se le reasigna una hora completamente diferente para la próxima ejecución. Asi nunca se realizaran a la misma hora
        hora = Utilidades.completarLongitudEnteros(2, Utilidades.Random(0, 23)) + ":" + Utilidades.completarLongitudEnteros(2, Utilidades.Random(0, 59)) + ":00";

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);
            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando Proceso realizar_desastre", 0);

            // Recuperar los valores iniciales de la abbatia a realizar la catastrofe!!!
            oUtilsAD = new adUtils(con);
            if (abadia_maligna == -1) {
                abadiaID = oUtilsAD.getSQL("SELECT abadiaID FROM `usuario` u, abadia a where u.usuarioid = a.usuarioid AND u.abadia_congelada = 0 and a.posicion > 0 " +
                        "and a.posicion < (SELECT Round((Count(*)*25)/100) FROM abadia) order by rand() limit 1", -1);
            } else {
                abadiaID = abadia_maligna;
            }
            int regionID = oUtilsAD.getSQL("SELECT RegionID FROM `abadia` WHERE abadiaID = " + abadiaID, -1);
            int idiomaID = oUtilsAD.getIdiomaID(abadiaID);
            oUtilsAD.execSQL("UPDATE procesos SET hora='" + hora + "' WHERE procesoid=13");

            switch (Utilidades.Random(0, 2)) {
                // Monje con fe baja
                case 0:
                    realizar_desastre_monje_fe_baja(abadiaID, regionID, idiomaID, fijo);
                    break;
                // NEGATIVOS (Edificios, Animales, etc...)
                case 1:
                    realizar_desastre_aleatorio(abadiaID, idiomaID);
                    break;
                // NEGATIVOS (Clima)
                case 2:
                    realizar_desastre_clima(abadiaID, regionID, idiomaID, fijo);
                    break;
            }
            oProcesosAD.addLog("- Finalizando Proceso realizar_desastre para la abbatia " + abadiaID, 0);
        } catch (AbadiaException e) {
            throw new AbadiaSQLException("ERROR: realizar_desastre = " + e, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    /*  DESASTRE QUE AFECTA A UN MONJE CON LA FE BAJA
     .-NEGATIVOS (Producidos por fe baja)

     *	Fe por debajo de 10:
     .	El Monje X ha sido pasto de las llamas al ser sorprendido por los Inquisidores en una relación anti-natura con un joven aldeano.
     .	Una piedra que se ha desprendido fortuitamente se ha llevado a Monje X al reino de los cielos.
     .	La Santa Inquisición ha torturado y quemado en la hoguera al Monje X al sorprenderlo en una relación anti-natura con un animal.
     *	Fe por debajo de 15:
     .	Monje X ha colgado los hábitos para fugarse con una aldeana.
     *	Fe por debajo de 25:
     .	El Monje X ha sido expulsado del monasterio al encontrarse pergaminos pornográficos en su dormitorio.
     .	El Monje X ha desaparecido misteriosamente, todos los indicios apuntan a la presencia de El Maligno.
    */
    public void realizar_desastre_monje_fe_baja(int abadiaID, int regionID, int idiomaID, boolean fijo) throws AbadiaException {
        String sTrace = this.getClass() + ".realizar_desastre_monje_fe_baja(" + abadiaID + ", " + regionID + ", " + idiomaID + ")";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        MonjeBBean oMonjeBBean;

        adUtils oUtilsAD;
        adLiterales oLiteralesAD;
        adMensajes oMensajesAD;

        String msg;
        int literalID = 0, estado = 0;

        ArrayList<Mensajes> mensajes = new ArrayList<Mensajes>();


        Connection con = null;

        try {
            oMonjeBBean = new MonjeBBean();
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS, Constantes.AUTOCOMIT_OF);

            oLiteralesAD = new adLiterales(con);
            oMensajesAD = new adMensajes(con);
            // Recupera el monje con la fé más baja de la abbatia
            oUtilsAD = new adUtils(con);
            int monjeID = oUtilsAD.getSQL("SELECT monjeid FROM monje m, `propiedad_valor` pv " +
                    " where pv.propiedadid = 21 and pv.claveid = m.monjeid and pv.tipo = 'M' and m.estado = 0 and m.abadiaid = " + abadiaID +
                    " order by valor", -1);
            int fe = oUtilsAD.getSQL("SELECT valor FROM `propiedad_valor` pv where pv.propiedadid = 21 and pv.claveid = " + monjeID + " and pv.tipo = 'M' ", 0);

            //si el parametro fijo esta a true, fijamos la fe a 0 para garantizar que se produce la catastrofe
            if (fijo) fe = 0;
            // Si tiene la fe por debajo de 10...
            if (fe < 10) {
                literalID = Utilidades.Random(0, 2) + 11000;  // identificador del mensaje aleatorio
                estado = 1;
            } else {
                if (fe < 15) {
                    literalID = 11010;
                    estado = 2;
                } else {
                    if (fe < 45) {
                        literalID = Utilidades.Random(0, 1) + 11020;  // identificador del mensaje aleatorio
                        estado = 1;
                    }
                }
            }
            // Le ha ocurrido un desastre???
            if (estado != 0) {
                msg = oLiteralesAD.getLiteralStatic(literalID, idiomaID);

                if (estado == 1) {
                    oMonjeBBean.morirMonje(abadiaID, monjeID, literalID + 2100, con);     // 13100 codifica desastres mortuorias ;-)
                } else {
                    oUtilsAD.execSQL("UPDATE monje SET estado = " + estado + " WHERE monjeid = " + monjeID);
                }
                //utils.finalize();

                mensajes.add(new Mensajes(abadiaID, monjeID, msg, 1));

                log.info("Desastre abadiaID:" + abadiaID + " - " + msg);

                // Grabar los mensajes...
                oMensajesAD.crearMensajes(mensajes);
            }
            ConnectionFactory.commitTransaction(con);

        } catch (AbadiaException e) {
            ConnectionFactory.rollbackTransaction(con);
            throw new AbadiaSQLException("ERROR: realizar_desastre_monje_fe_baja = " + e, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }


    /*  DESASTRE QUE AFECTA ALEATORIAMENTE UNA PROPIEDAD DEL JUEGO
    */
    public void realizar_desastre_aleatorio(int abadiaID, int idiomaID) throws AbadiaException {
        String sTrace = this.getClass() + ".realizar_desastre_aleatorio(" + abadiaID + ", " + idiomaID + ")";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        try {
            switch (Utilidades.Random(0, 1)) {
                // Monje con fe baja
                case 0:
                    realizar_desastre_edificio(abadiaID, idiomaID);
                    break;
                // NEGATIVOS (Edificios, Animales, etc...)
                case 1:
                    realizar_desastre_animales(abadiaID, idiomaID);
                    break;
            }
            switch (Utilidades.Random(0, 2)) {
                case 0:
                    //utils = new adUtils(Constantes.DB_TIMEOUT_PROCESS, Constantes.DB_CONEXION_PROCESS);
                    //utils.finalize();
                    break;
            }
        } finally {
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

/*  DESASTRE QUE AFECTA A LOS EDIFICIOS
*/

    public void realizar_desastre_edificio(int abadiaID, int idiomaID) throws AbadiaException {
        String sTrace = this.getClass() + ".realizar_desastre_edificio(" + abadiaID + ", " + idiomaID + ")";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        String msg;
        int literalID, piedra, hierro;

        adUtils oUtilsAD;
        adLiterales oLiteralAD;
        adEdificio oEdificioAD;
        adMensajes oMensajeAD;

        Edificio oEdificio;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);

            oUtilsAD = new adUtils(con);
            int edificioID = oUtilsAD.getSQL("SELECT edificioid FROM edificio " +
                    " where abadiaid = " + abadiaID + " and tipoedificioid not in (select tipoedificioid from edificio_construccion where abadiaid = " + abadiaID + ") and nivel > 1 order by rand() limit 1", 0);
            //si no ha encontrado edificio, salimos..
            if (edificioID == 0) {
                return;
            }

            oEdificioAD = new adEdificio(con);
            oEdificio = oEdificioAD.recuperarEdificio(edificioID, abadiaID, idiomaID);

            //recuperamos el valor en piedra del edificio_tipo por nivel
            piedra = oUtilsAD.getSQL("SELECT recurso_piedra FROM edificio_nivel where tipoedificioid = " + oEdificio.getIdDeTipoDeEdificio() + " and nivel = " +
                    oEdificio.getNivel(), 0);
            //recuperamos el valor en hierro del edificio_tipo por nivel
            hierro = oUtilsAD.getSQL("SELECT recurso_piedra FROM edificio_nivel where tipoedificioid = " + oEdificio.getIdDeTipoDeEdificio() + " and nivel = " +
                    oEdificio.getIdDeTipoDeEdificio(), 0);

            literalID = Utilidades.Random(1, 5) + 11100;  // identificador del mensaje aleatorio

            oLiteralAD = new adLiterales(con);

            msg = oLiteralAD.getLiteralStatic(literalID, idiomaID);

            msg = oEdificio.getNombre() + ": " + msg;

            oEdificioAD.bajarNivel(edificioID, abadiaID, piedra, hierro);

            oMensajeAD = new adMensajes(con);
            oMensajeAD.crearMensaje(new Mensajes(abadiaID, -1, msg, 1));

        } catch (AbadiaException e) {
            throw new AbadiaSQLException("ERROR: realizar_desastre_edificio = " + e, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    /*
    DESASTRE QUE AFECTA A LOS ANIMALES
    */

    public void realizar_desastre_animales(int abadiaID, int idiomaID) throws AbadiaException {

        String sTrace = this.getClass() + ".realizar_desastre_animales(" + abadiaID + ", " + idiomaID + ")";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adUtils oUtilsAD;
        adLiterales oLiteralesAD;
        adMensajes oMensajeAD;

        int numAnimales = 0;
        int idAnimal;
        String msg;
        int literalID;

        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);

            oUtilsAD = new adUtils(con);
            numAnimales = oUtilsAD.getSQL("Select round((count(*)*15/100)) from animales where edificioid in (select edificioid from edificio where abadiaid = " + abadiaID + ")", 0);

            numAnimales = Utilidades.Random(1, numAnimales);

            for (int iCount = 0; iCount < numAnimales; iCount++) {
                idAnimal = oUtilsAD.getSQL("Select animalid from animales where edificioid in (select edificioid from edificio where abadiaid = " + abadiaID + ") order by rand() limit 1", 0);
                if (idAnimal != 0) {
                    oUtilsAD.execSQL("delete from animales where animalid = " + idAnimal);
                }
            }

            literalID = Utilidades.Random(1, 3) + 11200;  // identificador del mensaje aleatorio
            oLiteralesAD = new adLiterales(con);
            msg = oLiteralesAD.getLiteralStatic(literalID, idiomaID);
            msg = Utilidades.Format(msg, numAnimales);

            oMensajeAD = new adMensajes(con);
            oMensajeAD.crearMensaje(new Mensajes(abadiaID, -1, msg, 1));

        } catch (AbadiaException e) {
            throw new AbadiaSQLException("ERROR: realizar_desastre_animales = " + e, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    /*  DESASTRE QUE AFECTA EL CLIMA
    */
    public void realizar_desastre_clima(int abadiaID, int regionID, int idiomaID, boolean fijo) throws AbadiaException {
        String sTrace = this.getClass() + ".realizar_desastre_clima(" + abadiaID + ", " + regionID + ", " + idiomaID + ")";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adUtils oUtilsAD;
        adLiterales oLiteralesAD;
        adMensajes oMensajeAD;

        // Incializar las variab.
        String msg;
        int i1;
        ArrayList mensajes = new ArrayList();

        //1.	Un rayo ha alcanzado a Monje X. Que el señor en su infinita misericordia la acoja en su seno.
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);
            oUtilsAD = new adUtils(con);
            oLiteralesAD = new adLiterales(con);

            switch (Utilidades.Random(0, 2)) {
                // Si hay tormenta fuerte, entonces pulverizar un aldeano
                case 0:

                    i1 = oUtilsAD.getDBProp(1, "R", regionID, -1); // "CLIMA_REG"
                    if ((i1 == 21) || (i1 == 22) || (fijo)) {  //Tiempo = 10, 11  ( LLuvias )
                        msg = oLiteralesAD.getLiteralStatic(100, idiomaID);
                    }

                    break;
            }

            // Grabar los mensajes...
            oMensajeAD = new adMensajes(con);
            oMensajeAD.crearMensajes(mensajes);

        } catch (AbadiaException e) {
            throw new AbadiaSQLException("ERROR: realizar_desastre_clima = " + e, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }
}

/*
ABBATIA. Eventos especiales


.-NEGATIVOS (Aleatorios)

8.	Una bandada de cuervos a diezmado tu cosecha.
9.	Una plaga de langostas ha asolado tus campos de cultivo.
10.	Un incendio he reducido a cenizas a tu Edificio X.
11.	La carcoma ha dañado seriamente tu Edificio X.
12.	Una banda de ladrones ha robado oro de tu monasterio.
13.	Una banda de ladrones ha robado animales de tu granja.
14.	Una banda de ladrones ha robado comida de tu cocina.
15.	Los rebaños de un señor feudal han entrado en tus campos causando destrozos en tu cosecha.
16.	El granizo ha diezmado tu cosecha.
17.	Un toro ha embestido al monje x dejándolo muy malherido.
18.	La peste negra ha llegado a tu monasterio. Que el señor nos ayude.
19.	La viruela azota a algunos de tus monjes.
20.	La gripe causa estragos en tu monasterio. Que el señor se apiade de tus monjes.
21.	Al parecer algunos aldeanos han robado piedras de tus muros para construir sus casas.
22.	El señor feudal de tu comarca te ha requisado x monedas de oro para financiar su castillo.
23.	Las guerras entre señores feudales de tu región han devastado tus campos de cultivo.



.-POSITIVOS

24.	El hijo de un aldeano ha ingresado como novicio en tu monasterio.
25.	Un ejercito que se dirige a las cruzadas ha pernoctado en tu abadía y os ha entregado como agradecimiento una bolsa de x monedas de oro
26.	Unos peregrinos han visitado tu abadía dejando como donativo una bolsa de x monedas de oro.
27.	El milagro se ha producido en tu Abadía y se han multiplicado los panes y los peces.
28.	El Papa ha visitado tu abadía, subiendo la fe de todos tus monjes.
a.	Sube la fe a +25
29.	Bajo una losa de tu monasterio has encontrado un libro escondido.
30.	Una vieja aldeana que ha fallecido ha dejado como herencia a tu monasterio x animales.
31.	El hijo de un señor feudal está estudiando en tu monasterio. Como pago recibes x animales.

*/
