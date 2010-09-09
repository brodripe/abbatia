package org.abbatia.bbean;

import org.abbatia.actionform.DatosSacrificioActForm;
import org.abbatia.actionform.MercadoVentaActForm;
import org.abbatia.adbean.*;
import org.abbatia.bean.*;
import org.abbatia.core.CoreTiempo;
import org.abbatia.dbms.ConnectionFactory;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.*;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.exception.base.SystemException;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.Utilidades;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * Created by Benjamín Rodríguez.
 * User: benjamin
 * Date: 24-may-2008
 * Time: 21:06:39
 */
public class AnimalBBean {
    private static Logger log = Logger.getLogger(MonjeBBean.class.getName());

    public int aislarAnimal(Usuario p_oUsuario, Abadia p_oAbadia, int p_iAnimalId) throws AbadiaException {
        String sTrace = this.getClass() + ".aislarAnimal(" + p_oUsuario.getNick() + "," + p_oAbadia.getIdDeAbadia() + "," + p_iAnimalId + ")";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adAnimal oAnimalAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);

            oAnimalAD = new adAnimal(con);
            Edificio edificio = oAnimalAD.recuperarEdificioAnimal(p_iAnimalId);

            if (edificio == null || edificio.getIdDeAbadia() != p_oAbadia.getIdDeAbadia()) {
                throw new EdificioNotFoundException(sTrace, log);
            }
            oAnimalAD.aislarAnimal(p_iAnimalId);
            Utilidades.eliminarRegistroContext(p_oUsuario.getNick());
            return edificio.getIdDeEdificio();

        } catch (AbadiaException e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public Animal asignarAnimalAgricultura(String p_sAnimalId, Abadia p_oAbadia) throws AbadiaException {
        String sTrace = this.getClass() + ".asignarAnimalAgricultura(" + p_sAnimalId + "," + p_oAbadia.getIdDeAbadia() + ")";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adAnimal oAnimalAD;

        Animal oAnimal;
        Connection con = null;

        int iAnimalId;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS, Constantes.AUTOCOMIT_OF);

            iAnimalId = Integer.parseInt(p_sAnimalId);
            oAnimalAD = new adAnimal(con);
            //recuperamos el animal para verificar que pertenece a la abadía en cuestión y es del tipo correcto
            oAnimal = oAnimalAD.recuperarAnimalAbadia(iAnimalId, p_oAbadia.getIdDeAbadia());

            //si el parámetro es si (1)
            if (oAnimal.getTrabaja() == 1) {
                //lo actualizamos a no (0)
                oAnimalAD.actualizarTrabaja(iAnimalId, (short) 0);
            } else //si no, (0)
            {
                //lo actualizamos a si (1)
                oAnimalAD.actualizarTrabaja(iAnimalId, (short) 1);
            }
            ConnectionFactory.commitTransaction(con);
            return oAnimal;
        } catch (AbadiaException e) {
            ConnectionFactory.rollbackTransaction(con);
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public ActionForward caparToro(int p_iAnimalId, Abadia p_oAbadia, Usuario p_oUsuario) throws AbadiaException {
        String sTrace = this.getClass() + ".caparToro()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adAnimal oAnimalAD;

        Edificio oEdificio;
        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oAnimalAD = new adAnimal(con);
            oEdificio = oAnimalAD.recuperarEdificioAnimal(p_iAnimalId);
            if (oEdificio.getIdDeAbadia() == p_oAbadia.getIdDeAbadia()) {
                oAnimalAD.caparToro(p_iAnimalId);
            }

            Utilidades.eliminarRegistroContext(p_oUsuario.getNick());
            return new ActionForward("/mostrarEdificio.do?clave=" + oEdificio.getIdDeEdificio());

        } catch (AbadiaException e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public ActionForward devolverAnimal(int p_iAnimnalId, Usuario p_oUsuario) throws AbadiaException {
        String sTrace = this.getClass() + ".devolverAnimal()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adAnimal oAnimalAD;

        Edificio oEdificio;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oAnimalAD = new adAnimal(con);
            oEdificio = oAnimalAD.recuperarEdificioAnimal(p_iAnimnalId);
            if (oEdificio == null || oEdificio.getIdDeAbadia() != oEdificio.getIdDeAbadia()) {
                throw new AnimalNoEncontradoException(sTrace + " No se ha encontrado el edificio", log);
            } else {
                oAnimalAD.devolverAnimal(p_iAnimnalId);
                Utilidades.eliminarRegistroContext(p_oUsuario.getNick());
                return new ActionForward("/mostrarEdificio.do?clave=" + oEdificio.getIdDeEdificio());
            }

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public void sacrificarAnimalInicio(DatosSacrificioActForm p_afDatos, Usuario p_oUsuario, Abadia p_oAbadia) throws AbadiaException {
        String sTrace = this.getClass() + ".sacrificarAnimalInicio()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adAnimal oAnimalAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);

            oAnimalAD = new adAnimal(con);
            oAnimalAD.getProduccionPotencialAlimentos(p_afDatos, p_oUsuario);
            oAnimalAD.getProduccionPotencialRecursos(p_afDatos, p_oUsuario);
            p_afDatos.setNumAnimales(oAnimalAD.recuperarCantidad(p_oAbadia.getIdDeAbadia(), p_afDatos.getAnimal_nivel(), p_afDatos.getAnimal_tipo(), p_afDatos.getAnimal_fechanacimiento()));

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public Edificio sacrificarAnimalConfirmacion(DatosSacrificioActForm p_afDatos, Usuario p_oUsuario, Abadia p_oAbadia,
                                                 MessageResources p_oResource, ActionMessages p_amMensajes,
                                                 ArrayList<Notificacion> p_alNotas) throws AbadiaException {
        String sTrace = this.getClass() + ".sacrificarAnimalConfirmacion()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adAnimal oAnimalAD;
        adEdificio oEdificioAD;

        String szPrecio;
        int iCantidadAnimales;
        Edificio oEdificio = null;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS, Constantes.AUTOCOMIT_OF);

            oAnimalAD = new adAnimal(con);

            szPrecio = p_afDatos.getPrecio();
            oAnimalAD.getProduccionPotencialAlimentos(p_afDatos, p_oUsuario);
            oAnimalAD.getProduccionPotencialRecursos(p_afDatos, p_oUsuario);
            //verificamos que el usuario dispone de la cantidad de animales que quiere sacrificar
            iCantidadAnimales = oAnimalAD.recuperarCantidad(p_oAbadia.getIdDeAbadia(), p_afDatos.getAnimal_nivel(), p_afDatos.getAnimal_tipo(), p_afDatos.getAnimal_fechanacimiento());
            //si el usuario ha establecido un precio de venta superior al del mercado...
            if (Utilidades.formatStringToDouble(szPrecio) > Utilidades.formatStringToDouble(p_afDatos.getPrecio())) {
                //utilizaremos el precio de mercado
            } else {
                //Utilizaremos el precio que ha puesto el usuario
                p_afDatos.setPrecio(szPrecio);
            }

            oEdificioAD = new adEdificio(con);
            oEdificio = oEdificioAD.recuperarEdificioPorTipoAnimal(p_oAbadia, p_afDatos.getAnimal_tipo(), p_oUsuario);

            //si el usuario no dispone de ningun animal de ese tipo....
            if (iCantidadAnimales < p_afDatos.getNumAnimales()) {
                p_amMensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.info.notienesanimales", new String[]{p_afDatos.getAnimal_desc(), String.valueOf(p_afDatos.getAnimal_nivel())}));
                p_alNotas.add(new Notificacion("javascript:window.parent.cClick();", p_oResource.getMessage("mensajes.link.volver.edificio", oEdificio.getNombre()), oEdificio.getGrafico_visualizacion() + "_" + oEdificio.getNivel() + ".gif", (short) 0));
                p_alNotas.add(new Notificacion("javascript:history.back(1);", p_oResource.getMessage("mensajes.link.volver"), (short) 1));
                throw new AnimalesInsuficientesException(sTrace, log);
                //request.getSession().setAttribute("notificacion", p_alNotas);
            } else {
                //eliminamos los animales sacrificados.
                for (int iCount = 0; iCount < p_afDatos.getNumAnimales(); iCount++) {
                    oAnimalAD.eliminarAnimalTipo(p_afDatos, p_oAbadia.getIdDeAbadia());
                }

                //generamos los alimento propios del sacrificio del animal/es
                sacrificarAnimalAlimento(p_afDatos, p_oAbadia, p_oUsuario, p_oResource, p_amMensajes, con);
                //si el sacrificio produce recursos..
                if (p_afDatos.getRecurso_id() > 0) {
                    //generamos los recursos propios del sacrificio del animal/es
                    sacrificarAnimalRecursos(p_afDatos, p_oAbadia, p_oUsuario, p_oResource, p_amMensajes, con);
                }

                p_alNotas.add(new Notificacion("/cerrarPopUp.do", p_oResource.getMessage("mensajes.link.volver.edificio", oEdificio.getNombre()), oEdificio.getGrafico_visualizacion() + "_" + oEdificio.getNivel() + ".gif", (short) 1));

                ConnectionFactory.commitTransaction(con);
                return oEdificio;
            }

        } catch (SystemException e) {
            ConnectionFactory.rollbackTransaction(con);
            throw e;
        } catch (CocinaLlenaException e) {
            throw new EspacioInsuficienteEnCocina(sTrace, oEdificio, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public void sacrificarAnimalAlimento(DatosSacrificioActForm datosSacrificio, Abadia abadia, Usuario usuario,
                                         MessageResources resource, ActionMessages mensajes, Connection p_cConexion)
            throws AbadiaException {
        String sTrace = this.getClass() + ".sacrificarAnimalAlimento()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        // adBeans
        adAlimentoLotes alimentos = null;
        adEdificio edificios = null;
        adMensajes msgAD = null;
        adMercadoVenta mercadoVentaAD = null;
        MercadoBBean oMercadoBBean;

        Mensajes msg = new Mensajes();
        Edificio edif;

        double dParaDonar = 0;
        double volumen = 0;
        double dParaCocina = 0;
        double dParaVenta = 0;
        double ctd = 0;
        int nDias = 0;
        int espacio = 0;
        int dias = 0;
        int LoteID = 0;

        try {
            oMercadoBBean = new MercadoBBean();
            // Recuperamos los datos de la mercancia
            // Es la cantidad correcta????

            // Tenemos un edificio conveniente para almacenar el producto???
            edificios = new adEdificio(p_cConexion);
            edif = edificios.recuperarEdificioTipo(Constantes.EDIFICIO_COCINA, abadia, usuario);

            if (edif == null) {
                throw new EdificioNotFoundException("No encuentro el edificio Cocina para albergar la mercancia", log);
            }
            // Calcular la caducidad
            nDias = 30;
            GregorianCalendar tiempoAbadia = CoreTiempo.getTiempoAbadia();
            tiempoAbadia.add(GregorianCalendar.HOUR, (nDias * 24));
            // Cabe el material que copramos en el edificio????
            espacio = (int) edif.getAlmacenamientoActual();
            alimentos = new adAlimentoLotes(p_cConexion);
            //recuperamos el espacio que ocupa una unidad de carne del animal en cuestión....
            volumen = alimentos.recuperarVolumen(datosSacrificio.getAlimento_id());
            ctd = Utilidades.ObtenerMedia(datosSacrificio.getAlimento_min(), datosSacrificio.getAlimento_max()) * datosSacrificio.getNumAnimales();

            //me da el número de unidades que cabe en la cocina
            //si el espacio ocupado excede la capacidad de almacenamiento del edificio
            //inicializamos el espacio con el almacenamiento total.
            if (espacio > edif.getAlmacenamiento()) espacio = edif.getAlmacenamiento();
            dParaCocina = (edif.getAlmacenamiento() - espacio) / volumen;
            log.debug("adAnimal. sacrificarAnimal. Unidades que caben: " + dParaCocina);
            dParaVenta = 0;
            //si la cantidad de alimento supera la capacidad de almacenamiento de la cocina...
            if (ctd > dParaCocina) {
                //obtenemos el excedente para venderlo..
                dParaVenta = ctd - dParaCocina;
                //dejamos el resto para meterlo en la cocina.
                //ctd = dParaCocina;
            }

            //Inicialmente, ponemos en la cocina la totalidad de alimentos
            //que ha generado el sacrificio

            AlimentoLote alimento = new AlimentoLote();
            alimento.setIdAlimento(datosSacrificio.getAlimento_id());
            alimento.setIdEdificio(edif.getIdDeEdificio());
            alimento.setFechaEntrada(CoreTiempo.getTiempoAbadiaString());
            //alimento.setFechaCaducidad(CoreTiempo.getTiempoAbadiaString(tiempoAbadia));
            alimento.setCantidad(ctd);

            alimentos = new adAlimentoLotes(p_cConexion);
            //recuperamos el número de dias para que caduque el alimento
            dias = alimentos.recuperarCaducidad(alimento.getIdAlimento());
            //asignamos la fecha de caducidad sumando a la fecha abbatia actual los dias devueltos
            alimento.setFechaCaducidad(CoreTiempo.getDiferenciaString(dias));
            LoteID = alimentos.crearAlimentoLote(alimento);
            alimento = alimentos.recuperarAlimentoLote(LoteID);
            //si hay excedente....

            if (dParaVenta > 0) {
                //si existe cantidad para poner a la venta, verificamos la capacidad del mercado
                edificios = new adEdificio(p_cConexion);
                edif = edificios.recuperarEdificioTipo(Constantes.EDIFICIO_MERCADO, abadia, usuario);

                espacio = (int) edif.getAlmacenamientoActual();
                //si el espacio ocupado excede de la capacidad del edificio...
                if (espacio + (dParaVenta * volumen) > edif.getAlmacenamiento()) {
                    //inicializamos la capacidad del edificio..
                    if (espacio > edif.getAlmacenamiento()) {
                        espacio = edif.getAlmacenamiento();
                    }
                    dParaDonar = dParaVenta;
                    //calculamos la cantidad que cave en el mercado
                    dParaVenta = (edif.getAlmacenamiento() - espacio) / volumen;
                    //si queda espacio para vender
                    if (dParaVenta > 0) {
                        //calculamos cuanto queda para donar
                        dParaDonar = dParaDonar - dParaVenta;
                    }
                }
                if (dParaVenta > 0) {
                    //lo ponemos a la venta
                    mercadoVentaAD = new adMercadoVenta(p_cConexion);
                    MercadoVentaActForm datosVenta = new MercadoVentaActForm();
                    datosVenta.setId(String.valueOf(alimento.getIdLote()));
                    datosVenta.setMercado(Constantes.MERCANCIA_ALIMENTOS_STR);
                    datosVenta.setDias(5);
                    datosVenta.setTipoVenta(0);
                    mercadoVentaAD.recuperarDatosProductoVenta(abadia.getIdDeAbadia(), datosVenta, usuario);
                    datosVenta.setCantidad(Utilidades.redondear(dParaVenta));
                    datosVenta.setPrecio(datosSacrificio.getPrecio());
                    try {
                        oMercadoBBean.getProcesoVenderProducto(abadia, usuario, datosVenta, resource, p_cConexion);
                    } catch (CompraCiudadException e) {
                    }

                }
            }

            // Descripción
            //String descMercancia = alimento.getDescripcion();
            //String descEdificio  = edif.getDescripcion();
            // *************************************************************************
            // SE HA PROCESADO CON EXITO???, PUES GESTIONAR LOS RECURSOS, MENSAJES Y LA MERCANCIA
            // *************************************************************************
            // Mensajes para el usuario
            msg.setIdDeAbadia(abadia.getIdDeAbadia());
            msg.setFechaAbadia(CoreTiempo.getTiempoAbadiaStringConHoras());
            msg.setFechaReal(CoreTiempo.getTiempoRealStringConHoras());
            msg.setIdDeMonje(-1);
            msg.setIdDeRegion(abadia.getIdDeRegion());

            String claves[] = {String.valueOf(datosSacrificio.getNumAnimales()), datosSacrificio.getAnimal_desc(), Utilidades.redondear(ctd), datosSacrificio.getUnidad_alimento(), datosSacrificio.getAlimento_desc()};

            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.info.sacroficiocompleto", claves));

            msg.setMensaje(resource.getMessage("mensajes.info.sacroficiocompleto", claves));

            if (dParaVenta > 0) {
                claves = new String[]{Utilidades.redondear(dParaVenta), datosSacrificio.getUnidad_alimento(), Utilidades.redondear(Utilidades.formatStringToDouble(datosSacrificio.getPrecio()) * dParaVenta)};
                mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.info.puestoalaventa", claves));
                msg.setMensaje(msg.getMensaje() + " " + resource.getMessage("mensajes.info.puestoalaventa", claves)); //" y has puesto a la venta un excedente de " +
            }
            if (dParaDonar > 0) {
                claves = new String[]{Utilidades.redondear(dParaDonar), datosSacrificio.getUnidad_alimento(), datosSacrificio.getAlimento_desc()};
                mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.info.donado", claves));
                msg.setMensaje(msg.getMensaje() + " " + resource.getMessage("mensajes.info.donado", claves)); //" y al no disponer de espacio suficiente en tu mercado, has donado a los pobres {0} {1}
            }
            msgAD = new adMensajes(p_cConexion);
            msgAD.crearMensaje(msg);
            // FInalizar


        } finally {
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    /**
     * GEstiona la generacion de los recursos producidos por el sacrificio de un animal
     *
     * @param datosSacrificio
     * @param abadia
     * @param usuario
     * @param resource
     * @param mensajes
     * @param p_cConexion
     * @throws AbadiaException
     */
    public void sacrificarAnimalRecursos(DatosSacrificioActForm datosSacrificio, Abadia abadia, Usuario usuario, MessageResources resource, ActionMessages mensajes, Connection p_cConexion) throws AbadiaException {
        String sTrace = this.getClass() + ".sacrificarAnimalRecursos()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        // adBeans
        adEdificio edificios;
        Mensajes msg = new Mensajes();
        Recurso recurso;
        adRecurso recursoAD;

        try {
            // Recuperamos los datos de la mercancia
            // Es la cantidad correcta????
            Edificio edif;
            // Tenemos un edificio conveniente para almacenar el producto???
            edificios = new adEdificio(p_cConexion);
            edif = edificios.recuperarEdificioTipo(Constantes.EDIFICIO_ALMACEN, abadia, usuario);

            //si no se encuentra el edificio Sotano/almacen, simplemente no producimos recursos...
            if (edif != null) {
                double ctd = Utilidades.ObtenerMedia(datosSacrificio.getRecurso_min(), datosSacrificio.getRecurso_max()) * datosSacrificio.getNumAnimales();
                if (ctd > 0) {
                    recurso = new Recurso();
                    recurso.setAbadiaID(abadia.getIdDeAbadia());
                    recurso.setRecursoID(datosSacrificio.getRecurso_id());
                    recurso.setCantidad(ctd);

                    //creamos o incrementamos el recurso producido del sacrificio
                    recursoAD = new adRecurso(p_cConexion);
                    if (recursoAD.existeRecurso(recurso.getRecursoID(), abadia.getIdDeAbadia())) {
                        recursoAD.sumarRecurso(recurso.getRecursoID(), recurso.getAbadiaID(), recurso.getCantidad());
                    } else {
                        recursoAD.crearRecurso(recurso);
                    }

                    // Descripción

                    // *************************************************************************
                    // SE HA PROCESADO CON EXITO???, PUES GESTIONAR LOS RECURSOS, MENSAJES Y LA MERCANCIA
                    // *************************************************************************
                    // Mensajes para el usuario
                    msg.setIdDeAbadia(abadia.getIdDeAbadia());
                    msg.setFechaAbadia(CoreTiempo.getTiempoAbadiaStringConHoras());
                    msg.setFechaReal(CoreTiempo.getTiempoRealStringConHoras());
                    msg.setIdDeMonje(-1);
                    msg.setIdDeRegion(abadia.getIdDeRegion());
                    //mensajes.info.sacroficiorecursos=Con el sacrificio, has obtenido tambien {0} {1} de {2}
                    String claves[] = {String.valueOf(Utilidades.redondear(ctd)), datosSacrificio.getUnidad_recurso(), datosSacrificio.getRecurso_desc()};

                    mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.info.sacroficiorecursos", claves));

                    msg.setMensaje(resource.getMessage("mensajes.info.sacroficiorecursos", claves));

                    adMensajes msgAD = new adMensajes(p_cConexion);
                    msgAD.crearMensaje(msg);
                }
            }

        } finally {
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

}
