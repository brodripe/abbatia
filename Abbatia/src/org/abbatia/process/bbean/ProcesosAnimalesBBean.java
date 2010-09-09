package org.abbatia.process.bbean;

import org.abbatia.adbean.adLiterales;
import org.abbatia.adbean.adMensajes;
import org.abbatia.adbean.adUtils;
import org.abbatia.bean.*;
import org.abbatia.core.CoreTiempo;
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
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;

public class ProcesosAnimalesBBean extends ProcesosUtils {

    private static Logger log = Logger.getLogger(ProcesosAnimalesBBean.class.getName());


    /* Comprueba los nieveles de los animales
    */
    public void nivel_animales() throws AbadiaException {

        String sTrace = this.getClass() + ".nivel_animales()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adProcesos oProcesosAD;
        adLiterales oLiteralesAD;
        adUtils oUtilsAD;
        adMensajes oMensajesAD;

        ArrayList<Mensajes> alMensajes = new ArrayList<Mensajes>();
        ArrayList<Animal> alAnimales;
        String msg, msg1 = "", msg2 = "";
        int last_abadiaid = 0, last_idiomaid = 0, tmp_idiomaid;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);
            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando Proceso nivel_animales", 0);
            alAnimales = oProcesosAD.recuperarAnimalesNivel();

            oLiteralesAD = new adLiterales(con);
            oUtilsAD = new adUtils(con);
            oMensajesAD = new adMensajes(con);

            Iterator<Animal> animales = alAnimales.iterator();
            Animal animal;

            while (animales.hasNext()) {
                animal = animales.next();
                // Cargar los alMensajes en el idioma correcto
                if (last_abadiaid != animal.getAbadiaid()) {
                    last_abadiaid = animal.getAbadiaid();

                    oUtilsAD = new adUtils(con);
                    tmp_idiomaid = oUtilsAD.getIdiomaID(last_abadiaid);
                    if (last_idiomaid != tmp_idiomaid) {
                        last_idiomaid = tmp_idiomaid;
                        msg1 = oLiteralesAD.getLiteralStatic(10030, last_idiomaid); // El animal %s ha pasado al nivel %d
                        msg2 = oLiteralesAD.getLiteralStatic(10031, last_idiomaid); // El animal %s se ha muerto de viejo
                    }

                }

                oUtilsAD.execSQL("UPDATE animales SET nivel = nivel + 1 WHERE nivel < 4 and animalID = " + animal.getAnimalid());
                if (animal.getNivel() + 1 < 5) {
                    // Por debajo del nivel 5?, pos enviar un mensaje
                    msg = Format(msg1, animal.getNombre());
                    msg = Format(msg, animal.getNivel() + 1);
                    alMensajes.add(new Mensajes(animal.getAbadiaid(), -1, msg, 0));
                } else
                    // Lo matamos???
                    if (Utilidades.Random(0, 100) < 25) { // 25% de probabilidades pa morir ;-) *** Habria que factorizarlo con el estado del animal
                        oUtilsAD.execSQL("UPDATE animales SET estado = 1, fecha_fallecimiento='" +
                                CoreTiempo.getTiempoAbadiaString() + "' WHERE animalid = " +
                                animal.getAnimalid());

                        msg = Format(msg2, animal.getNombre());
                        alMensajes.add(new Mensajes(animal.getAbadiaid(), -1, msg, 1));
                    }
            }

            oMensajesAD.crearMensajes(alMensajes);

            // Ahora para e mercado
            oProcesosAD.addLog(" Lanzando SUBProceso nivel_animales para mercados", 0);
            alAnimales = oProcesosAD.recuperarAnimalesMercadoNivel();

            animales = alAnimales.iterator();
            while (animales.hasNext()) {
                animal = animales.next();
                // Nivel por debajo de 4 ?
                if (animal.getNivel() < 4) { // existe un nivel siguiente
                    oUtilsAD.execSQL("UPDATE mercados_animales SET nivel = nivel + 1 WHERE nivel < 4 and productoid = " + animal.getProductoid());
                } else {
                    // Lo matamos???
                    if (Utilidades.Random(0, 100) < 25) { // 25% de probabilidades pa morir ;-) *** Habria que factorizarlo con el estado del animal
                        oUtilsAD.execSQL("UPDATE mercados SET estado=1 WHERE abadiaid=" + animal.getAbadiaid() + " and productoid = " + animal.getProductoid());
                        msg = Format(msg2, animal.getNombre());
                        alMensajes.add(new Mensajes(animal.getAbadiaid(), -1, msg, 1));
                    }
                }
            }
            oProcesosAD.addLog("- Finalizando Proceso nivel_animales", 0);

        } catch (AbadiaException e) {
            throw new AbadiaSQLException("ERROR: nivel_animales = " + e, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    /* Comprueba los nieveles de los animales
    */
    public void follar(short aislado, Hashtable htStress) throws AbadiaException {

        String sTrace = this.getClass() + ".follar(" + aislado + ")";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adProcesos oProcesosAD;
        adLiterales oLiteralesAD;
        adUtils oUtilsAD;
        adMensajes oMensajesAD;

        HashMap<Integer, ArrayList<AnimalProceso>> hmHembras;

        ArrayList<Mensajes> mensajes = new ArrayList<Mensajes>();
        ArrayList<AnimalProceso> alHembras;
        ArrayList<AbadiaIdioma> alAbadias;
        Iterator<AnimalProceso> hembras;
        AnimalProceso animalHembra;
        AnimalProceso animalMacho;
        String msg, msg1 = "", msg2 = "";
        int last_idiomaid = 0, tmp_idiomaid;
        EdificioProceso edificio;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);
            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando Proceso follar " + aislado, 0);
            alAbadias = oProcesosAD.recuperarAbadiasAnimalesMachosIdioma(aislado);
            hmHembras = oProcesosAD.recuperarHembras(aislado);

            oLiteralesAD = new adLiterales(con);
            oUtilsAD = new adUtils(con);
            oMensajesAD = new adMensajes(con);

            Iterator<AbadiaIdioma> itAbadias = alAbadias.iterator();
            AbadiaIdioma oAbadiaIdioma;

            while (itAbadias.hasNext()) {
                oAbadiaIdioma = itAbadias.next();
                // Cargar los mensajes en el idioma correcto
                tmp_idiomaid = oAbadiaIdioma.getIdIdioma();
                if (last_idiomaid != tmp_idiomaid) {
                    last_idiomaid = tmp_idiomaid;
                    msg1 = oLiteralesAD.getLiteralStatic(10060, last_idiomaid); // La %s ha sido fecundada por %s
                    msg2 = oLiteralesAD.getLiteralStatic(10080, last_idiomaid); // Los animales no pueden aparearse en el edificio %s por falta de espacio
                }

                edificio = (EdificioProceso) htStress.get(new Integer(oAbadiaIdioma.getIdEdificio()));

                if (edificio.getExtresado() == 0) {
                    // Recuperar animales... hembras fertiles ;-)
                    //oProcesosAD = new adProcesos();
                    alHembras = hmHembras.get(Integer.valueOf(oAbadiaIdioma.getIdEdificio()));
                    //oProcesosAD.finalize();
                    if (alHembras != null) {
                        hembras = alHembras.iterator();

                        while (hembras.hasNext()) {
                            animalHembra = hembras.next();

                            animalMacho = oProcesosAD.recuperarMachos(oAbadiaIdioma.getIdEdificio(), animalHembra.getTipoAnimalIdMasc(), aislado);

                            if (animalMacho != null) {
                                // Empezar la insiminación virtual, la probabilidad va en funcion de la salud
                                if ((Utilidades.Random(1, 102 - animalMacho.getSalud()) == 1) & (Utilidades.Random(1, 102 - animalHembra.getSalud()) == 1)) {
                                    oUtilsAD.execSQL("UPDATE animales SET fecha_embarazo='" + animalHembra.getFecha_parir() +
                                            "' WHERE animalid = " + animalHembra.getIdAnimal());

                                    msg = Format(msg1, animalHembra.getDescripcion());
                                    msg = Format(msg, animalMacho.getDescripcion());
                                    mensajes.add(new Mensajes(oAbadiaIdioma.getIdAbadia(), -1, msg, 0));
                                }
                            }
                        }
                    }//if (alHembras != null)
                } else {
                    msg = Format(msg2, oAbadiaIdioma.getNombreEdificio());
                    mensajes.add(new Mensajes(oAbadiaIdioma.getIdAbadia(), -1, msg, 1));
                }
            }

            oMensajesAD.crearMensajes(mensajes);

            oProcesosAD.addLog("- Finalizando Proceso follar " + aislado, 0);

        } catch (Exception e) {
            throw new AbadiaSQLException("ERROR: follar = " + e, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    // Abrir todas las hembras que tiene la abbatia y que esten apunto de parir
    public void parir(Hashtable htStress) throws AbadiaException {

        String sTrace = this.getClass() + ".parir()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adProcesos oProcesosAD;
        adLiterales oLiteralesAD;
        adUtils oUtilsAD;
        adMensajes oMensajesAD;

        String msg, msg1 = "", msg2 = "", msg3 = "", msg4 = "", sSQL;
        int crias, tipo_animal, last_abadiaid = 0, last_edificioid = 0, last_idiomaid = 0, tmp_idiomaid;
        ArrayList<AnimalProceso> alAnimales;
        Iterator<AnimalProceso> itAnimales;


        ArrayList<Mensajes> mensajes = new ArrayList<Mensajes>();
        EdificioProceso edificio = null;


        AnimalProceso animal;

        Connection con = null;

        try {

            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_PROCESS);

            oProcesosAD = new adProcesos(con);
            oProcesosAD.addLog("+ Lanzando Proceso parir", 0);
            alAnimales = oProcesosAD.recuperarAnimalesParir();

            oUtilsAD = new adUtils(con);
            oLiteralesAD = new adLiterales(con);
            oMensajesAD = new adMensajes(con);

            itAnimales = alAnimales.iterator();
            while (itAnimales.hasNext()) {
                animal = itAnimales.next();
                // Cargar los mensajes en el idioma correcto
                if (last_abadiaid != animal.getIdAbadia()) {
                    last_abadiaid = animal.getIdAbadia();

                    tmp_idiomaid = oUtilsAD.getIdiomaID(last_abadiaid);

                    if (last_idiomaid != tmp_idiomaid) {
                        last_idiomaid = tmp_idiomaid;
                        msg1 = oLiteralesAD.getLiteralStatic(10061, last_idiomaid); // La %s se ha muerto al parir
                        msg2 = oLiteralesAD.getLiteralStatic(10062, last_idiomaid); // La %s ha parido pero la cria ha nacido muerta
                        msg3 = oLiteralesAD.getLiteralStatic(10063, last_idiomaid); // La %s ha tenido %d cria/s
                        msg4 = oLiteralesAD.getLiteralStatic(10064, last_idiomaid); // Ha nacido un nuevo animal
                    }
                }
                if (last_edificioid != animal.getIdEdificio()) {
                    last_edificioid = animal.getIdEdificio();
                    edificio = (EdificioProceso) htStress.get(new Integer(last_edificioid));
                }
                if (edificio == null) {
                    log.error("ProcesosAnimalesBBean. parir. No se encuentra el edificio: " + animal.getIdEdificio() + " al que pertenece el animal: " + animal.getIdAnimal() + " de la abbatia: " + animal.getIdAnimal());
                } else {
                    // aleatorio
                    if (animal.getSalud() > 70)
                        crias = Utilidades.Random(1, animal.getCriasMax());
                    else
                        crias = Utilidades.Random(0, animal.getCriasMax());   // alguna puede nacer muerta
                    if (animal.getSalud() < 30) crias = 0;
                    if ((animal.getSalud() < 20) || (edificio.getExtresado() == 1)) {
                        oUtilsAD.execSQL("UPDATE animales SET estado = 1, fecha_fallecimiento='" + CoreTiempo.getTiempoAbadiaString() + "' WHERE animalid = " + animal.getIdAnimal());
                        msg = Format(msg1, animal.getDescripcion());
                        mensajes.add(new Mensajes(animal.getIdAbadia(), -1, msg, 1));
                    } else {
                        if (crias == 0) {
                            msg = Format(msg2, animal.getDescripcion());
                            mensajes.add(new Mensajes(animal.getIdAbadia(), -1, msg, 1));
                            //setMensaje(rs.getInt("AbadiaID"), msg, 1);
                        } else {
                            msg = Format(msg3, animal.getDescripcion());
                            msg = Format(msg, crias);
                            mensajes.add(new Mensajes(animal.getIdAbadia(), -1, msg, 1));
                            //setMensaje( rs.getInt("AbadiaID"), msg, 1);

                            for (int n = 0; n <= crias; n++) {
                                // Probabilidad de un macho !!!
                                if (Utilidades.Random(0, 100) > 80)
                                    tipo_animal = animal.getTipoAnimalIdMasc();
                                else tipo_animal = animal.getTipoAnimalIdFem();

                                sSQL = "INSERT INTO `animales` (tipo_animalid, edificioid, nivel, estado, fecha_nacimiento, salud) ";
                                sSQL = sSQL + "values (" + tipo_animal + "," +
                                        animal.getIdEdificio() + ",1,0,'" +
                                        CoreTiempo.getTiempoAbadiaString() + "',100)";
                                oUtilsAD.execSQL(sSQL);
                                mensajes.add(new Mensajes(animal.getIdAbadia(), -1, msg4, 1));
                                //setMensaje( rs.getInt("AbadiaID"), msg4, 1);
                            }
                        }
                    }
                    oUtilsAD.execSQL("UPDATE animales SET fecha_embarazo=null, fecha_parido='" + CoreTiempo.getTiempoAbadiaString() + "' WHERE animalid = " + animal.getIdAnimal());
                }
            }

            oMensajesAD.crearMensajes(mensajes);

            oProcesosAD.addLog("- Finalizando Proceso parir", 0);

        } catch (Exception e) {
            throw new AbadiaSQLException("ERROR: parir = " + e, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }
}

