package org.abbatia.bbean;

import org.abbatia.actionform.*;
import org.abbatia.adbean.*;
import org.abbatia.bbean.singleton.CargaInicialFamiliasBBean;
import org.abbatia.bean.*;
import org.abbatia.core.CoreTiempo;
import org.abbatia.dbms.ConnectionFactory;
import org.abbatia.dbms.DBMSUtils;
import org.abbatia.exception.*;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.abbatia.utils.Utilidades;
import org.apache.commons.validator.GenericValidator;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;

import javax.naming.Context;
import java.io.Serializable;
import java.sql.Connection;
import java.util.*;

/**
 * Created by Benjamín Rodríguez.
 * User: benjamin
 * Date: 16-may-2008
 * Time: 20:48:53
 */
public class MercadoBBean {
    private static Logger log = Logger.getLogger(MercadoBBean.class.getName());

    public void inicioVenta(MercadoVentaActForm p_FormDatosVenta, Abadia p_Abadia, Usuario p_Usuario) throws AbadiaException {
        String sTrace = this.getClass() + ".inicioVenta()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        Connection con = null;
        adMercadoVenta oMercadoVenta;
        adEdificio oEdificio;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            //VErificamos si el usuario lleva menos de un mes abbatia jugando
            if (CoreTiempo.getDiferenciaDiasInt(p_Abadia.getFechaDeConstruccion(), CoreTiempo.getTiempoAbadiaString()) < 30) {
                p_FormDatosVenta.setUsuarioNuevo(true);
                p_FormDatosVenta.setVenderACiudad(true);
            } else {
                oEdificio = new adEdificio(con);
                if (!oEdificio.existeEdificioTipo(p_Abadia.getIdDeAbadia(), Constantes.EDIFICIO_MERCADO)) {
                    p_FormDatosVenta.setVenderACiudad(true);
                    p_FormDatosVenta.setExisteMercado(false);
                } else {
                    p_FormDatosVenta.setVenderACiudad(false);
                    p_FormDatosVenta.setExisteMercado(true);
                }
            }

            oMercadoVenta = new adMercadoVenta(con);
            oMercadoVenta.recuperarDatosProductoVenta(p_Abadia.getIdDeAbadia(), p_FormDatosVenta, p_Usuario);

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public Edificio ejecutarVenta(MercadoVentaActForm p_FormDatosVenta, Abadia p_Abadia, Usuario p_Usuario, MessageResources resource) throws AbadiaException {
        String sTrace = this.getClass() + ".ejecutarVenta()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        Connection con = null;
        adMercadoVenta oMercadoVenta;
        adMercado oMercado;
        adEdificio oEdificio;
        Edificio edificioMercado = null;
        Edificio edificio;
        double dVolumen;
        double dCantidad;
        EspacioInsuficienteException exEspacioInsuficiente;


        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS, Constantes.AUTOCOMIT_OF);
            //VErificamos si el usuario lleva menos de un mes abbatia jugando
            if (CoreTiempo.getDiferenciaDiasInt(p_Abadia.getFechaDeConstruccion(), CoreTiempo.getTiempoAbadiaString()) < 30) {
                p_FormDatosVenta.setVenderACiudad(true);
            } else {
                oEdificio = new adEdificio(con);
                //verificamos si el usuario dispone de mercado
                if (!oEdificio.existeEdificioTipo(p_Abadia.getIdDeAbadia(), Constantes.EDIFICIO_MERCADO)) {
                    p_FormDatosVenta.setVenderACiudad(true);
                }
            }

            //volvemos a recuperar los precios de venta del mercado en prevision de reescritura del formulario
            oMercadoVenta = new adMercadoVenta(con);
            double precioMercado = oMercadoVenta.getPrecioMercado(p_FormDatosVenta);
            p_FormDatosVenta.setPrecioVentaCiudad(Utilidades.redondear(precioMercado / 4));
            //si está marcado el flag de vender a la ciudad..
            if (p_FormDatosVenta.isVenderACiudad() || (CoreTiempo.getDiferenciaDiasInt(p_Abadia.getFechaDeConstruccion(), CoreTiempo.getTiempoAbadiaString()) < 30)) {
                p_FormDatosVenta.setPrecio(p_FormDatosVenta.getPrecioVentaCiudad());

            } else // lo ponemos a la venta en el mercado
            {
                //Recuperar volumen disponible del edificio mercado
                oEdificio = new adEdificio(con);
                edificioMercado = oEdificio.recuperarEdificioTipo(Constantes.EDIFICIO_MERCADO, p_Abadia, p_Usuario);
            }
            //si el precio fijado por el usuario supera el precio de mercado..
            if (Utilidades.formatStringToDouble(p_FormDatosVenta.getPrecio()) > precioMercado) {
                //fijamos el precio de venta al del mercado
                p_FormDatosVenta.setPrecio(Utilidades.redondear(precioMercado));
            }
            //recuperamos el Edificio al que tenemos que ir al finalizar la compra
            oEdificio = new adEdificio(con);
            edificio = oEdificio.recuperarEdificioPorProductoVenta(p_FormDatosVenta, p_Abadia, p_Usuario);

            //todo determinar volumento total de productos que se pretende vender
            //si no se dispone de mercado...
            if (edificioMercado == null) {
                //fijamos el precio de venta al de mercado para vender a la ciudad
                p_FormDatosVenta.setPrecio(p_FormDatosVenta.getPrecioVentaCiudad());
            } else {
                oMercado = new adMercado(con);
                dCantidad = Utilidades.formatStringToDouble(p_FormDatosVenta.getCantidad());
                dVolumen = oMercado.recuperarVolumenElemento(p_FormDatosVenta.getMercado(), p_FormDatosVenta.getId(), p_FormDatosVenta.getTipoAnimal(), p_FormDatosVenta.getNivelAnimal());
                //si el almacenamiento actual + el volumen del producto * la cantidad supera la capacidad de almacenamiento del edificio...
                if (edificioMercado.getAlmacenamiento() < (edificioMercado.getAlmacenamientoActual() + (dVolumen * dCantidad))) {
                    exEspacioInsuficiente = new EspacioInsuficienteException("Espacio insuficiente en el edificio", log);
                    exEspacioInsuficiente.setdCantidad(dCantidad);
                    exEspacioInsuficiente.setdVolumen(dVolumen);
                    exEspacioInsuficiente.setoEdificio(edificio);
                    exEspacioInsuficiente.setoEdificioMercado(edificioMercado);
                    throw exEspacioInsuficiente;
                }
            }

            try {
                getProcesoVenderProducto(p_Abadia, p_Usuario, p_FormDatosVenta, resource, con);
                return edificio;
            } catch (CompraCiudadException e) {
                e.setOEdificio(edificio);
                throw e;
            }

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public ActionMessage procesarCompra(MessageResources resource, Abadia abadia, Usuario usuario, MercadoCompraForm datosCompra) throws AbadiaException {
        String sTrace = this.getClass() + ".procesarCompra()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        //ActionMessage
        ActionMessage actMensaje;
        //conexión
        Connection con = null;

        // adBeans
        adLibros oLibroAD;
        adAlimentoLotes oAlimentosAD;
        adEdificio oEdificiosAD;
        adAnimal oAnimalAD;
        adRecurso oRecursoAD;
        adUtils oUtilsAD;
        adMensajes oMensajesAD;
        adMercadoCompra oMercadoCompraAD;
        adComisiones oComisionesAD;
        adMercado oMercadoAD;
        adUsuario oUsuariosAD;
        adAbadia oAbadiaAD;
        adMercadoMovima oMercadoMovimaAD;

        Libro libro;
        int regionOrigen;
        ArrayList<Mensajes> alMensajesComision = new ArrayList<Mensajes>();
        ArrayList<ImpuestoRegion> alImpuestoRegion = null;
        Iterator<ImpuestoRegion> itImpuestoRegion;
        // Beans
        Mercado mercancia;
        Mensajes msg;
        String Lugar, descEdificio = "", descMercancia = "";
        Edificio edif = null;
        double precioMercado;
        MercadoCompraForm datosCompraOri;
        ImpuestoRegion impuestoRegion;
        double comisionTransitoTotal = 0;
        double comisionTransito;
        int almacenamiento = 0;
        int nDias;
        int LoteID;
        Vector<String> vIPs;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS, Constantes.AUTOCOMIT_OF);

            // Recuperamos los datos de la mercancia
            oMercadoAD = new adMercado(con);
            mercancia = oMercadoAD.recuperarMercado(datosCompra.getProductoID());
            datosCompraOri = oMercadoAD.getDatosCompra(usuario, datosCompra.getProductoID());

            //una vez recuperados los datos de la mercancía, podemos determinar la ip desde la que se conecta el usuario
            //que ha puesto el producto a la venta y contrastarla con la ip del usuario que está realizando la compra
            //en caso de ser iguales no permitiremos la compra...

            //recuperamos un vector con todas las ips desde las que se ha conectado el usuario que puso el producto a la venta.
            oUsuariosAD = new adUsuario(con);
            vIPs = oUsuariosAD.recuperarIPsUsuarioPorAbadia(mercancia.getIdAbadia());
            //si la ip actual del usuario coincide con alguna de las ip's con las que se ha logado
            //el usuario que puso la mercancia a la venta..
            if (vIPs.contains(usuario.getIpActual())) {
                //no permitimos la venta
                ConnectionFactory.rollbackTransaction(con);
                throw new CompraNoPermitidaException("adMercadoCompra. procesarCompra. La abbatia: " + abadia.getNombre() + " Está tratando de comprar un producto que se puso a la venta con su misma IP.", log);
            }

            double total = mercancia.getPrecio_actual() * datosCompra.getCantidad();
            double precioSinComisiones = total;
            /****************************************/
            //Inicio calculo de comisiones de transito...
            /****************************************/
            //recuperamos los datos originales del producto a comprar

//      mercado.finalize();
            //si la compra no se hace en la ciudad
            if (datosCompraOri.getAbadiaid() != 0) {
                oAbadiaAD = new adAbadia(con);
                //recuperamos la región de la que procede el producto a comprar
                regionOrigen = oAbadiaAD.getRegionAbadia(datosCompraOri.getAbadiaid());
                //si la regiones son diferentes... y no se trata de la ciudad
                if (regionOrigen != abadia.getIdDeRegion()) {
                    //obtenemos la lista de impuestos de transito por regiones de paso.
                    oComisionesAD = new adComisiones(con);
                    alImpuestoRegion = oComisionesAD.getComisionesTransito(abadia.getIdDeRegion(), regionOrigen);
                    //creo un iterator a partir del arraylist para el bucle
                    itImpuestoRegion = alImpuestoRegion.iterator();
                    while (itImpuestoRegion.hasNext()) {
                        impuestoRegion = itImpuestoRegion.next();
                        comisionTransitoTotal += (total * impuestoRegion.getValorImpuesto()) / 100;
                        //valorComision = (total*comision)/100;
                    }
                    total += comisionTransitoTotal;
                }
            }
            /****************************************/
            //Fin calculo de comisiones de transito...
            /****************************************/

            oMercadoCompraAD = new adMercadoCompra(con);
            precioMercado = oMercadoCompraAD.getPrecioMercado(mercancia);
            //si el precio de compra supera el valor de mercado de ese producto...
            //no permitiremos la compra.
            if (precioMercado < mercancia.getPrecio_actual() && mercancia.getIdAbadia() != 0) {
                ConnectionFactory.rollbackTransaction(con);
                throw new MoreExpensiveException("El precio de compra del producto supera su valor de mercado, puede conseguir este mismo producto en el mercado por " + Utilidades.redondear(precioMercado) + " monedas", log);
            }
            // Es la cantidad correcta????
            if (mercancia.getCtd_actual() >= datosCompra.getCantidad()) {
                // Comprueba que tenga dinero :-D
                oRecursoAD = new adRecurso(con);
                double dinero = oRecursoAD.recuperarValorRecurso(0, abadia.getIdDeAbadia());
                if (dinero >= total) {
                    // Tenemos dinero!!!

                    // ************************************************************
                    // Alimentos?
                    // ************************************************************

                    if (mercancia.getMercancia().equals(Constantes.MERCANCIA_ALIMENTOS_STR)) {
                        // Tenemos un edificio conveniente para almacenar el producto???
                        oEdificiosAD = new adEdificio(con);
                        edif = oEdificiosAD.recuperarEdificioPorFamiliaAlimento(abadia, mercancia.getIdAlimento(), usuario);
                        if (edif != null) {
                            log.debug("adMercadoCompra. getProcesoCompra2. edificio: " + edif.getNombre());
                            // Calcular la caducidad
                            // Cabe el material que copramos en el edificio????
                            int espacio = (int) edif.getAlmacenamientoActual();

                            oAlimentosAD = new adAlimentoLotes(con);
                            double volumen = oAlimentosAD.recuperarVolumen(mercancia.getIdAlimento());

                            oLibroAD = new adLibros(con);
                            libro = oLibroAD.recuperaLibroTipo(300 + edif.getIdDeTipoDeEdificio(), edif.getIdDeAbadia());

                            if (libro != null) {
                                almacenamiento = Utilidades.recuperarIncrementoEspacioEdificioPorLibro(edif.getIdDeTipoDeEdificio(), libro.getNivel(), edif.getNivel());
/*                                almacenamiento = (libro.getNivel() * edif.getNivel()) * 10;*/
                            }
                            almacenamiento = edif.getAlmacenamiento() + almacenamiento;


                            if (((datosCompra.getCantidad() * volumen) + espacio) < almacenamiento) {
                                AlimentoLote alimento = new AlimentoLote();
                                alimento.setIdAlimento(mercancia.getIdAlimento());
                                alimento.setIdEdificio(edif.getIdDeEdificio());
                                alimento.setFechaEntrada(CoreTiempo.getTiempoAbadiaString());
                                alimento.setCantidad(datosCompra.getCantidad());

                                oAlimentosAD = new adAlimentoLotes(con);
                                //si el producto se compra el la ciudad, calculamos la caducidad a partir del idalimento
                                if (mercancia.getIdAbadia() == 0) {
                                    nDias = oAlimentosAD.recuperarCaducidad(mercancia.getIdAlimento());
                                    alimento.setFechaCaducidad(CoreTiempo.sumarDias(CoreTiempo.getTiempoAbadiaString(), nDias));
                                } else {
                                    //ahora la fecha de caducidad se informa a partir de la fecha de caducidad de la mercancia...
                                    alimento.setFechaCaducidad(mercancia.getFecha_caduca());
                                }
                                LoteID = oAlimentosAD.crearAlimentoLote(alimento);
                                alimento = oAlimentosAD.recuperarAlimentoLote(LoteID);
                                // Descripción
                                descMercancia = alimento.getDescripcion();
                                descEdificio = edif.getNombre();

                            } else {
                                if (edif.getIdDeTipoDeEdificio() == Constantes.EDIFICIO_COCINA) {
                                    ConnectionFactory.rollbackTransaction(con);
                                    throw new EspacioInsuficienteEnCocina("La abbatia: " + abadia.getNombre() + " No dispone de espacio suficiente en la cocina.", log);
                                } else {
                                    ConnectionFactory.rollbackTransaction(con);
                                    throw new EspacioInsuficienteEnGranero("La abbatia: " + abadia.getNombre() + " No dispone de espacio suficiente en el granero.", log);
                                }
                                //sError =  "<p><b>NO SE PUEDE REALIZAR LA COMPRA... Ya no cabe en el edificio: " + edif.getDescripcion() + " </b></p>";
                            }
                        }
                    }
                    // ************************************************************
                    // Animales?
                    // ************************************************************
                    if (mercancia.getMercancia().equals(Constantes.MERCANCIA_ANIMALES_STR)) {
                        // Tenemos un edificio conveniente para almacenar el producto???
                        oEdificiosAD = new adEdificio(con);
                        edif = oEdificiosAD.recuperarEdificioPorTipoAnimal(abadia, mercancia.getIdAnimalTipo(), usuario);
                        if (edif == null) {
                            ConnectionFactory.rollbackTransaction(con);
                            throw new NoExisteEstabloException("Abadia: " + abadia.getIdDeAbadia() + "No dispone de Establo", log);
                        } else {
                            // Cabe el material que copramos en el edificio????
                            int espacio = (int) edif.getAlmacenamientoActual();

                            oAnimalAD = new adAnimal(con);
                            double volumen = oAnimalAD.recuperarVolumenAnimal(mercancia.getIdAnimalTipo(), mercancia.getIdAnimalNivel());

                            oLibroAD = new adLibros(con);
                            libro = oLibroAD.recuperaLibroTipo(300 + edif.getIdDeTipoDeEdificio(), edif.getIdDeAbadia());

                            if (libro != null) {
                                almacenamiento = Utilidades.recuperarIncrementoEspacioEdificioPorLibro(edif.getIdDeTipoDeEdificio(), libro.getNivel(), edif.getNivel());
/*                                almacenamiento = (libro.getNivel() * edif.getNivel()) * 10;*/
                            }

                            almacenamiento = edif.getAlmacenamiento() + almacenamiento;
                            if (((datosCompra.getCantidad() * volumen) + espacio) < almacenamiento) {
                                Animal animal = new Animal();
                                animal.setTipoAnimalid(mercancia.getIdAnimal());
                                animal.setEdificioid(edif.getIdDeEdificio());
                                //en el caso de que la compra se realice en el mercado, la fecha de nacimiento
                                //deberá recalcularse en funcion del nivel del animal..
                                animal.setNivel((short) mercancia.getIdAnimalNivel());
                                //si se trata de una compra en la abbatia
                                //la fecha de nacimiento del animal se calculara en función del tipo y el nivel.
                                if (mercancia.getIdAbadia() == 0) {
                                    //para que tenga una la edad de el nivel -1 (sino envejecen enseguida).
                                    int nivel_animal = animal.getNivel();
                                    if (nivel_animal > 1) {
                                        nivel_animal = nivel_animal - 1;
                                    }
                                    int iDiasVida = oAnimalAD.recuperarDiasVida(animal.getTipoAnimalid(), nivel_animal);

                                    GregorianCalendar tiempoAbadia = CoreTiempo.getTiempoAbadia();
                                    tiempoAbadia.add(GregorianCalendar.MONTH, -iDiasVida);
                                    animal.setFecha_nacimiento(Utilidades.formatStringFromDB(CoreTiempo.getTiempoAbadiaString(tiempoAbadia)));
                                } else {
                                    animal.setFecha_nacimiento(Utilidades.formatStringFromDB(mercancia.getFecha_nacimiento()));
                                }

                                animal.setSalud(mercancia.getAnimalSalud());
                                animal.setEstado(0);
                                animal.setCantidad(datosCompra.getCantidad());

                                int animalid = oAnimalAD.crearAnimal(animal, datosCompra.getCantidad());

                                animal = oAnimalAD.recuperarAnimal(animalid);

                                // Descripción
                                descMercancia = animal.getNombre();
                                descEdificio = edif.getNombre();
                            } else {
                                ConnectionFactory.rollbackTransaction(con);
                                throw new EspacioInsuficienteEnGranja("La abbatia: " + abadia.getNombre() + " No dispone de espacio suficiente en la granja.", log);
                            }
                        }
                    }

                    // ************************************************************
                    // Recursos?
                    // ************************************************************
                    if (mercancia.getMercancia().equals(Constantes.MERCANCIA_RECURSOS_STR)) {
                        // Modificar los recursos de la abbatia
                        //primero verificamos si el recurso deberia ir al almacen....
                        oEdificiosAD = new adEdificio(con);
                        boolean bParaAlmacen = oEdificiosAD.recursoParaAlmacen(mercancia.getIdRecurso());

                        oRecursoAD = new adRecurso(con);
                        double volumen = oRecursoAD.recuperarVolumenRecurso(mercancia.getIdRecurso());
                        //log.info("Cantidad: " + datosCompra.getCantidad());
                        //log.info("Volumen: " + volumen);
                        if (bParaAlmacen) {
                            oEdificiosAD = new adEdificio(con);
                            edif = oEdificiosAD.recuperarEdificioPorRecurso(abadia, mercancia.getIdRecurso(), usuario);

                            if (edif == null) {
                                ConnectionFactory.rollbackTransaction(con);
                                throw new NoExisteAlmacenException("No dispones de almacen para almacenar el producto", log);
                            }

                            oLibroAD = new adLibros(con);
                            libro = oLibroAD.recuperaLibroTipo(300 + edif.getIdDeTipoDeEdificio(), edif.getIdDeAbadia());

                            almacenamiento = edif.getAlmacenamiento();

                            if (libro != null) {
                                almacenamiento = Utilidades.recuperarIncrementoEspacioEdificioPorLibro(edif.getIdDeTipoDeEdificio(), libro.getNivel(), edif.getNivel());
/*                                almacenamiento = (libro.getNivel() * edif.getNivel()) * 15;*/
                                almacenamiento = edif.getAlmacenamiento() + almacenamiento;
                            }

                            if ((volumen * datosCompra.getCantidad() + edif.getAlmacenamientoActual()) > almacenamiento) {
                                ConnectionFactory.rollbackTransaction(con);
                                throw new EspacioInsuficienteEnAlmacen("No dispones de espacio suficiente en el almacen", log);
                            }
                        }

                        oRecursoAD = new adRecurso(con);
                        double recur = oRecursoAD.recuperarCantidadRecurso(mercancia.getIdRecurso(), abadia.getIdDeAbadia());
                        if (oRecursoAD.existeRecurso(mercancia.getIdRecurso(), abadia.getIdDeAbadia())) {
                            oRecursoAD.modificarRecurso(mercancia.getIdRecurso(), abadia.getIdDeAbadia(), recur + datosCompra.getCantidad());
                        } else {
                            oRecursoAD.crearRecurso(mercancia.getIdRecurso(), abadia.getIdDeAbadia(), datosCompra.getCantidad());
                        }
                        // Descripciones
                        descMercancia = oRecursoAD.recuperarDescripcionRecurso(mercancia.getIdRecurso(), usuario);

                        if (edif == null) {
                            descEdificio = "Abadia";
                        } else {
                            descEdificio = edif.getNombre();
                        }
                    }

                    // *************************************************************************
                    // SE HA PROCESADO CON EXITO???, PUES GESTIONAR LOS RECURSOS, MENSAJES Y LA MERCANCIA
                    // *************************************************************************
//          if (sError == null)
//          {
                    oMercadoMovimaAD = new adMercadoMovima(con);
                    oMercadoMovimaAD.crearMercadoMovima(mercancia.getIdProducto(),
                            abadia.getIdDeAbadia(),
                            CoreTiempo.getTiempoAbadiaString(), "C",
                            datosCompra.getCantidad(), mercancia.getPrecio_actual());

                    // Modificar los recursos de la abbatia
                    oRecursoAD = new adRecurso(con);
                    oRecursoAD.restarRecurso(0, abadia.getIdDeAbadia(), total);

                    // Mensajes para el que vende
                    msg = new Mensajes();
                    msg.setFechaAbadia(CoreTiempo.getTiempoAbadiaStringConHoras());
                    msg.setFechaReal(CoreTiempo.getTiempoRealStringConHoras());
                    msg.setIdDeMonje(-1);
                    msg.setIdDeRegion(-1);

                    // Modificar los recursos de la abbatia que ha vendido el producto,
                    // La abbatia 0 es la ciudad. no se tiene que rebajar el stock
                    if (mercancia.getIdAbadia() != 0) {
                        //calculamos las comisiones
                        String diferencia = CoreTiempo.getDiferenciaDias(mercancia.getFecha_inicial(), mercancia.getFecha_final());
                        String comisionTipo;
                        if (diferencia.equals("5")) {
                            comisionTipo = Constantes.COMISION_TIPO_VENTA5;
                        } else if (diferencia.equals("10")) {
                            comisionTipo = Constantes.COMISION_TIPO_VENTA10;
                        } else if (diferencia.equals("15")) {
                            comisionTipo = Constantes.COMISION_TIPO_VENTA15;
                        } else comisionTipo = Constantes.COMISION_TIPO_VENTA5;

                        double comision = 0;
                        oComisionesAD = new adComisiones(con);
                        //si la abbatia tiene menos de un mes de antiguedad, no paga comisiones
                        int libre = CoreTiempo.getDiferenciaDiasInt(abadia.getFechaDeConstruccion(), CoreTiempo.getTiempoAbadiaString());
                        if (libre > 30) {
                            comision = oComisionesAD.getComision(mercancia.getIdAbadia(), comisionTipo);
                        }
                        double valorComision = 0;
                        if (comision > 0) {
                            valorComision = (precioSinComisiones * comision) / 100;
                            oComisionesAD.sumarImporteComision(mercancia.getIdAbadia(), CoreTiempo.getTiempoAbadiaString(), valorComision);
                        }
                        double valorVenta = precioSinComisiones - valorComision;
                        oRecursoAD = new adRecurso(con);
                        oRecursoAD.sumarRecurso(0, mercancia.getIdAbadia(), valorVenta);

                        //ingresamos las comisiones de transito a los obispados implicados...
                        if (comisionTransitoTotal > 0) {
                            itImpuestoRegion = alImpuestoRegion.iterator();
                            while (itImpuestoRegion.hasNext()) {
                                impuestoRegion = itImpuestoRegion.next();
                                comisionTransito = precioSinComisiones * impuestoRegion.getValorImpuesto() / 100;
                                if (comisionTransito > 0) {
                                    //sumamos los importes de comision de transito, el que paga es el que compra....
                                    oComisionesAD.sumarImporteComisionTransito(abadia.getIdDeAbadia(), impuestoRegion.getIdRegion(), CoreTiempo.getTiempoAbadiaString(), comisionTransito);
                                    //creamos mensaje para el que compra.
                                    alMensajesComision.add(new Mensajes(abadia.getIdDeAbadia(), -1, resource.getMessage("mensaje.info.comision.transito", Utilidades.redondear(comisionTransito), impuestoRegion.getNombreRegion()), 0));
                                }
                            }
                            oMensajesAD = new adMensajes(con);
                            oMensajesAD.crearMensajes(alMensajesComision);
                        }

                        // Disminuir el estock de la mercancia
                        oMercadoAD = new adMercado(con);
                        oMercadoAD.actualizarCtdMercado(mercancia.getIdProducto(), mercancia.getCtd_actual() - datosCompra.getCantidad());

                        oUtilsAD = new adUtils(con);
                        Lugar = resource.getMessage("mercado.historico.abbatia") + " ( " + oUtilsAD.getSQL("Select nombre from abadia where abadiaid = " + mercancia.getIdAbadia(), "xxx") + " )";

                        msg.setIdDeAbadia(mercancia.getIdAbadia());

                        if (mercancia.getMercancia().equals(Constantes.MERCANCIA_ANIMALES_STR)) {
                            msg.setMensaje(resource.getMessage("mensajes.info.vendidoanimal", String.valueOf(datosCompra.getCantidad()), descMercancia, abadia.getNombre(), Utilidades.redondear(valorVenta)));
                        } else {
                            msg.setMensaje(resource.getMessage("mensajes.info.vendidoresto", String.valueOf(datosCompra.getCantidad()), descMercancia, abadia.getNombre(), Utilidades.redondear(valorVenta)));
                        }

                        oMensajesAD = new adMensajes(con);
                        oMensajesAD.crearMensaje(msg);
                        if (valorComision > 0) {
                            msg.setMensaje(resource.getMessage("mensajes.info.comision", Utilidades.redondear(valorComision)));
                            oMensajesAD.crearMensaje(msg);
                        }
                        //**************************************

                    } else {
                        Lugar = "Ciudad";
                    }

                    //Cambio muy importante, se guarda información en la tabla mercados_ciudad_variaciones también en las compras entre usuarios (antes solo aplicaba a las compras a la ciudad)
                    // Modificar las estadísticas de ventas de producto para así ir incrementando o decrementar los precios de la ciudad, el proceso de regulación de precios establecerá el nuevo precio del producto
                    oUtilsAD = new adUtils(con);
                    //TODO llegados a este punto, para los casos en los que la venta se hace entre jugadores y no comprando directamente al mercado, debemos conseguir que el productoid se corresponda
                    //TODO con el productoid con el que la ciudad está vendiendo ese mismo producto. De ese modo se computará la compra/venta para compras realizadas entre jugadores. ya que el control
                    //TODO de número de ventas que hace fluctuar el mercado se registra en la tabla mercados_ciudad_variaciones pero sólo hay registros que actualizar si el productoid corresponde con el
                    //TODO del mercado.

                    /*
                    En este punto, deberíamos poder recuperar el identificador del producto a la venta en el mercado por parte de la ciudad para utilizarlo en lugar del identificar de producto
                    original
                    String sSQLAlimentos = "SELECT m.productoid FROM  mercados m, mercados_alimentos ma WHERE m.productoid = ma.productoid and  ma.alimentoid=? and m.abadiaid=0";
                    String sSQLAnimales = "SELECT m.productoid FROM  mercados m, mercados_animales ma WHERE m.productoid = ma.productoid and  ma.NIVEL=? and ma.TIPO_ANIMALID = ? and m.abadiaid=0";
                    String sSQLRecursos = "SELECT m.productoid FROM  mercados m, mercados_recursos ma WHERE m.productoid = ma.productoid and  ma.recursoid=? and m.abadiaid=0";
                    */
                    int iProductoId;
                    if (mercancia.getIdAbadia() != 0) {
                        iProductoId = oMercadoCompraAD.getProductoIdMercado(mercancia);
                    } else
                        iProductoId = mercancia.getIdProducto();

                    oUtilsAD.execSQL("UPDATE `mercados_ciudad_variaciones` SET nr_ventas = nr_ventas + " + datosCompra.getCantidad() + " WHERE productoid = " +
                            iProductoId + " and mercancia = '" +
                            mercancia.getMercancia() + "'");
                    //Fin cambio importante.

                    // Mensajes para el que compra
                    msg.setIdDeAbadia(abadia.getIdDeAbadia());

                    if (mercancia.getMercancia().equals(Constantes.MERCANCIA_ANIMALES_STR)) {
                        msg.setMensaje(resource.getMessage("mensajes.info.compraanimal", String.valueOf(datosCompra.getCantidad()), descMercancia, Lugar, Utilidades.redondear(total)));
                    } else {
                        msg.setMensaje(resource.getMessage("mensajes.info.compraresto", String.valueOf(datosCompra.getCantidad()), descMercancia, Lugar, Utilidades.redondear(total)));
                    }
                    oMensajesAD = new adMensajes(con);
                    oMensajesAD.crearMensaje(msg);

                    actMensaje = new ActionMessage("mensajes.aviso.comprarealizada", descEdificio);
                } else {
                    // Sin dinero
                    ConnectionFactory.rollbackTransaction(con);
                    throw new OroInsuficienteException("No dispones de oro suficiente", log);
                }
            } else {
                ConnectionFactory.rollbackTransaction(con);
                throw new CantidadErroneaException("Cantidad erronea para la compra", log);
            }

            ConnectionFactory.commitTransaction(con);
            return actMensaje;

        } catch (AbadiaSQLException e) {
            ConnectionFactory.rollbackTransaction(con);
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public void gestionaVenta(Mercado mercancia, Abadia abadia, int cantidad, double precio, String descMercancia, MessageResources resource, Connection con) throws AbadiaException {

        String sTrace = this.getClass() + ".gestionaVenta()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adComisiones oComisionesAD;
        adMercadoMovima oMercadoMovimaAD;
        adRecurso oRecursoAD;
        adMensajes oMensajesAD;
        // Movimiento de compra de la mercancia

        try {

            //con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS, Constantes.AUTOCOMIT_OF);

            oMercadoMovimaAD = new adMercadoMovima(con);
            oMercadoMovimaAD.crearMercadoMovima(mercancia.getIdProducto(),
                    abadia.getIdDeAbadia(), CoreTiempo.getTiempoAbadiaString(), "C",
                    cantidad, mercancia.getPrecio_actual());

            //calculamos las comisiones
            String diferencia = CoreTiempo.getDiferenciaDias(mercancia.getFecha_inicial(), mercancia.getFecha_final());
            String comisionTipo;
            if (diferencia.equals("5")) {
                comisionTipo = Constantes.COMISION_TIPO_VENTA5;
            } else if (diferencia.equals("10")) {
                comisionTipo = Constantes.COMISION_TIPO_VENTA10;
            } else if (diferencia.equals("15")) {
                comisionTipo = Constantes.COMISION_TIPO_VENTA15;
            } else comisionTipo = Constantes.COMISION_TIPO_VENTA5;

            oComisionesAD = new adComisiones(con);
            double comision = 0;
            int libre = CoreTiempo.getDiferenciaDiasInt(abadia.getFechaDeConstruccion(), CoreTiempo.getTiempoAbadiaString());
            if (libre > 30) {
                comision = oComisionesAD.getComision(abadia.getIdDeRegion(), comisionTipo);
            }
            double valorTotal = cantidad * precio;
            double valorComision = 0;
            if (comision > 0) {
                valorComision = (valorTotal * comision) / 100;
                oComisionesAD.sumarImporteComision(abadia.getIdDeRegion(), abadia.getIdDeAbadia(), CoreTiempo.getTiempoAbadiaString(), valorComision);
            }
            double valorVenta = valorTotal - valorComision;

            //double valorComision = ((Utilidades.formatStringToDouble(datosVenta.getPrecio())* datosVenta.getCantidad())*comision)/100;

            // Modificar los recursos de la abbatia
            oRecursoAD = new adRecurso(con);
            oRecursoAD.sumarRecurso(0, abadia.getIdDeAbadia(), valorVenta);

            Mensajes msg = new Mensajes();

            // Mensajes para el que vende
            msg.setFechaAbadia(CoreTiempo.getTiempoAbadiaStringConHoras());
            msg.setFechaReal(CoreTiempo.getTiempoRealStringConHoras());
            msg.setIdDeMonje(-1);
            msg.setIdDeRegion(abadia.getIdDeRegion());

            msg.setIdDeAbadia(mercancia.getIdAbadia());
            if (mercancia.getMercancia().equals("N")) {
                msg.setMensaje(resource.getMessage("mensajes.info.ventaanimalciudad", String.valueOf(cantidad), descMercancia, Utilidades.redondear(valorVenta)));
            } else {
                msg.setMensaje(resource.getMessage("mensajes.info.ventarestociudad", String.valueOf(cantidad), descMercancia, Utilidades.redondear(valorVenta)));
            }

            oMensajesAD = new adMensajes(con);
            oMensajesAD.crearMensaje(msg);
            if (valorComision > 0) {
                msg.setMensaje(resource.getMessage("mensajes.info.comision", Utilidades.redondear(valorComision)));
                oMensajesAD.crearMensaje(msg);
            }
            //commit de la operación
            //ConnectionFactory.commitTransaction(con);
        } catch (AbadiaException e) {
            //Rollback de la operación
            //ConnectionFactory.rollbackTransaction(con);
            throw e;
        } finally {
            //DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public void gestionaVentaCiudad(MercadoVentaActForm mercancia, Abadia abadia, Usuario usuario, MessageResources resource, Connection con) throws AbadiaException {
        String sTrace = this.getClass() + ".gestionaVenta()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adComisiones oComisionesAD;
        adMercadoMovima oMercadoMovimaAD;
        adMensajes oMensajesAD;
        adRecurso oRecursoAD;
        adMercado oMercadoAD;

        String comisionTipo;
        String diferencia;
        double comision = 0;

        //Connection con = null;

        try {
            //con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS, Constantes.AUTOCOMIT_OF);
            // Movimiento de compra de la mercancia
            double precio = Utilidades.formatStringToDouble(mercancia.getPrecio());
            int cantidad = Integer.parseInt(mercancia.getCantidad());
            oMercadoMovimaAD = new adMercadoMovima(con);
            oMercadoMovimaAD.crearMercadoMovima(Integer.parseInt(mercancia.getId()), abadia.getIdDeAbadia(), CoreTiempo.getTiempoAbadiaString(), "C",
                    cantidad, precio);

            //calculamos las comisiones
            diferencia = String.valueOf(mercancia.getDuracionMercado());

            if (diferencia.equals("5")) {
                comisionTipo = Constantes.COMISION_TIPO_VENTA5;
            } else if (diferencia.equals("10")) {
                comisionTipo = Constantes.COMISION_TIPO_VENTA10;
            } else if (diferencia.equals("15")) {
                comisionTipo = Constantes.COMISION_TIPO_VENTA15;
            } else comisionTipo = Constantes.COMISION_TIPO_VENTA5;

            oComisionesAD = new adComisiones(con);
            //si la abbatia tiene menos de un mes de antiguedad, no paga comisiones.
            int libre = CoreTiempo.getDiferenciaDiasInt(abadia.getFechaDeConstruccion(), CoreTiempo.getTiempoAbadiaString());
            if (libre > 30) {
                comision = oComisionesAD.getComision(abadia.getIdDeRegion(), comisionTipo);
            }
            double valorTotal = cantidad * precio;
            double valorComision = 0;
            if (comision > 0) {
                valorComision = (valorTotal * comision) / 100;
                oComisionesAD.sumarImporteComision(abadia.getIdDeRegion(), abadia.getIdDeAbadia(), CoreTiempo.getTiempoAbadiaString(), valorComision);
            }
            double valorVenta = valorTotal - valorComision;
            // Modificar los recursos de la abbatia
            oRecursoAD = new adRecurso(con);
            oRecursoAD.sumarRecurso(0, abadia.getIdDeAbadia(), valorVenta);

            Mensajes msg = new Mensajes();
            // Mensajes para el que vende
            msg.setFechaAbadia(CoreTiempo.getTiempoAbadiaStringConHoras());
            msg.setFechaReal(CoreTiempo.getTiempoRealStringConHoras());
            msg.setIdDeMonje(-1);
            msg.setIdDeRegion(abadia.getIdDeRegion());
            msg.setIdDeAbadia(abadia.getIdDeAbadia());
            if (mercancia.getMercado().equals(Constantes.MERCANCIA_ANIMALES_STR)) {
                msg.setMensaje(resource.getMessage("mensajes.info.ventaanimalciudad", String.valueOf(cantidad), mercancia.getDescripcionProducto(), Utilidades.redondear(valorVenta)));
            } else {
                msg.setMensaje(resource.getMessage("mensajes.info.ventarestociudad", String.valueOf(cantidad), mercancia.getDescripcionProducto(), Utilidades.redondear(valorVenta)));
            }
            oMensajesAD = new adMensajes(con);
            oMensajesAD.crearMensaje(msg);
            if (valorComision > 0) {
                msg.setMensaje(resource.getMessage("mensajes.info.comision", Utilidades.redondear(valorComision)));
                oMensajesAD.crearMensaje(msg);
            }

            Mercado mercado = new Mercado();
            mercado.setIdProducto(Integer.parseInt(mercancia.getId()));
            mercado.setMercancia(mercancia.getMercado());
            oMercadoAD = new adMercado(con);
            oMercadoAD.eliminarMercado(mercado);

        } catch (AbadiaException e) {
            //Rollback de la operación
            //ConnectionFactory.rollbackTransaction(con);
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            //DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public MercadoVenta getProcesoVenderProducto(Abadia abadia, Usuario usuario, MercadoVentaActForm datosVenta, MessageResources resource, Connection con) throws AbadiaException {

        String sTrace = this.getClass() + ".getProcesoVenderProducto()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        MercadoVenta Contents = new MercadoVenta();
        adAlimentoLotes oAlimentoLotesAD;
        adAnimal oAnimalesAD;
        adMercado oMercadoAD;
        adRecurso oRecursoAD;
        adMensajes oMensajesAD;

        ArrayList alTable = new ArrayList();
        double precio = Utilidades.formatStringToDouble(datosVenta.getPrecio());
        double precioVentaCiudad = Utilidades.formatStringToDouble(datosVenta.getPrecioVentaCiudad());
        int ctd = (int) Utilidades.formatStringToDouble(datosVenta.getCantidad());
        int nDias = datosVenta.getDias();
        double total = precio * ctd;

        Mercado mercancia = new Mercado();

        // ************************************************************
        // Alimentos?
        // ************************************************************
        if (datosVenta.getMercado().equals(Constantes.MERCANCIA_ALIMENTOS_STR)) {
            int lid = Integer.parseInt(datosVenta.getId());
            AlimentoLote alimento;
            oAlimentoLotesAD = new adAlimentoLotes(con);
            alimento = oAlimentoLotesAD.recuperarAlimentoLote(lid);

            // Comprobar que podemos vender la cantidad
            if (alimento == null || alimento.getCantidad() < ctd) {
                throw new CantidadInsuficienteException("( " + usuario.getNick() + " ) - " + "No dispones de tanta cantidad para poner a la venta.", log);
            } else {
                //si la resta del lote deja el total = 0 o menos...
                if (alimento.getCantidad() - ctd <= 0) {
                    //eliminamos el lote directamente...
                    oAlimentoLotesAD.eliminarAlimentoLote(alimento.getIdLote());
                } else {
                    //de lo contrario, restamos la cantidad del lote
                    oAlimentoLotesAD.ModificarCantidad(alimento.getIdLote(), alimento.getCantidad() - ctd);
                }

                //si el precio de venta esta 3 veces por debajo del precio de mercado
                // Calcular el final
                // Llenar datos de la nueva mercancia
                mercancia.setIdAbadia(abadia.getIdDeAbadia());
                mercancia.setIdAlimento(alimento.getIdAlimento());
                mercancia.setEstado(0);
                mercancia.setFecha_inicial(CoreTiempo.getTiempoAbadiaString());
                mercancia.setFecha_final(CoreTiempo.getDiferenciaString(nDias));
                mercancia.setPrecio_inicial(precio);
                mercancia.setFecha_caduca(alimento.getFechaCaducidad());
                mercancia.setCtd_inicial(ctd);
                mercancia.setMercancia(datosVenta.getMercado());
                mercancia.setTipo(0); // Venta directa

                if (precio <= precioVentaCiudad) {
                    //en este punto también deberiamos crear un registro de mercado, pero cerrado...
                    //  mercados = new adMercado();
                    //  mercados.crearMercado(mercancia);
                    //  mercados.finalize();
                    gestionaVenta(mercancia, abadia, mercancia.getCtd_inicial(), mercancia.getPrecio_inicial(), alimento.getDescripcion(), resource, con);

                    throw new CompraCiudadException("La ciudad ha ejercido su derecho a compra...", log);
                } else {
                    // Crear los datos
                    log.debug("adMercadoVenta. getProcesoVenderProducto.6");
                    oMercadoAD = new adMercado(con);
                    oMercadoAD.crearMercado(mercancia);

                    Mensajes msg = new Mensajes();

                    msg.setFechaAbadia(CoreTiempo.getTiempoAbadiaStringConHoras());
                    msg.setFechaReal(CoreTiempo.getTiempoRealStringConHoras());
                    msg.setIdDeAbadia(abadia.getIdDeAbadia());
                    msg.setIdDeMonje(-1);
                    msg.setIdDeRegion(abadia.getIdDeRegion());
                    msg.setMensaje(resource.getMessage("mensajes.info.ventaresto", datosVenta.getCantidad(), alimento.getDescripcion(), Utilidades.redondear(total)));

                    oMensajesAD = new adMensajes(con);
                    oMensajesAD.crearMensaje(msg);
                }
            }
            // Restar del mercado
            //mercado.actualizarCtdMercado( pid, ctd);
            // ************************************************************
            // Recursos
            // ************************************************************
        } else if (datosVenta.getMercado().equals(Constantes.MERCANCIA_RECURSOS_STR)) {
            int lid = Integer.parseInt(datosVenta.getId());
            oRecursoAD = new adRecurso(con);
            Recurso recurso = oRecursoAD.recuperarRecurso(lid, abadia.getIdDeAbadia());

            // Comprobar que podemos vender la cantidad
            if (recurso.getCantidad() < ctd) {
                throw new CantidadInsuficienteException("( " + usuario.getNick() + " ) - " + "No dispones de tanta cantidad para poner a la venta.", log);
            } else {
                // Modificar el lote
                oRecursoAD = new adRecurso(con);
                oRecursoAD.restarRecurso(lid, abadia.getIdDeAbadia(), ctd);

                //si el precio de venta esta 3 veces por debajo del precio de mercado

                // Llenar datos de la nueva mercancia
                mercancia.setIdAbadia(abadia.getIdDeAbadia());
                //mercancia.setIdAlimento(recurso.getRecursoID());
                mercancia.setIdRecurso(recurso.getRecursoID());
                mercancia.setEstado(0);
                mercancia.setFecha_inicial(CoreTiempo.getTiempoAbadiaStringConHoras());
                mercancia.setFecha_final(CoreTiempo.getDiferenciaString(nDias));
                mercancia.setPrecio_inicial(precio);
                mercancia.setCtd_inicial(ctd);
                mercancia.setMercancia(datosVenta.getMercado());
                mercancia.setTipo(0); // Venta directa

                if (precio <= precioVentaCiudad) {
                    //en este punto también deberiamos crear un registro de mercado, pero cerrado...
                    //  mercados = new adMercado();
                    //  mercados.crearMercado(mercancia);
                    //  mercados.finalize();

                    gestionaVenta(mercancia, abadia, mercancia.getCtd_inicial(), mercancia.getPrecio_inicial(), recurso.getDescripcion(), resource, con);
                    ConnectionFactory.commitTransaction(con);
                    throw new CompraCiudadException("La ciudad ha ejercido su derecho a compra...", log);
                } else {
                    // Crear los datos
                    oMercadoAD = new adMercado(con);
                    oMercadoAD.crearMercado(mercancia);
                    // Mensajes
                    Mensajes msg = new Mensajes();
                    msg.setFechaAbadia(CoreTiempo.getTiempoAbadiaStringConHoras());
                    msg.setFechaReal(CoreTiempo.getTiempoRealStringConHoras());
                    msg.setIdDeAbadia(abadia.getIdDeAbadia());
                    msg.setIdDeMonje(-1);
                    msg.setIdDeRegion(abadia.getIdDeRegion());
                    msg.setMensaje(resource.getMessage("mensajes.info.ventaresto", datosVenta.getCantidad(), recurso.getDescripcion(), Utilidades.redondear(total)));
                    oMensajesAD = new adMensajes(con);
                    oMensajesAD.crearMensaje(msg);
                }
                // Restar del mercado
                //mercado.actualizarCtdMercado( pid, ctd);
            }
            // ************************************************************
            // Animales?
            // ************************************************************
        } else if (datosVenta.getMercado().equals(Constantes.MERCANCIA_ANIMALES_STR)) {
            oAnimalesAD = new adAnimal(con);
            int cantidad = oAnimalesAD.recuperarCantidad(abadia.getIdDeAbadia(), datosVenta.getNivelAnimal(), datosVenta.getTipoAnimal(), datosVenta.getFechaAnimal());
            //si no hay ningun animal...
            if (cantidad < 1) {
                throw new CantidadInsuficienteException("( " + usuario.getNick() + " ) - " + "No dispones de tanta cantidad para poner a la venta.", log);
            }

            Animal animal = oAnimalesAD.recuperarAnimalTipo(datosVenta.getTipoAnimal(), datosVenta.getNivelAnimal());
            int salud = oAnimalesAD.recuperarSalud(abadia.getIdDeAbadia(), datosVenta.getNivelAnimal(), datosVenta.getTipoAnimal(), datosVenta.getFechaAnimal());
            if (salud == 0) salud = 50;
            animal.setSalud(salud);

            // Comprobar que podemos vender la cantidad
            if (cantidad < ctd) {
                throw new CantidadInsuficienteException("( " + usuario.getNick() + " ) - " + "No dispones de tanta cantidad para poner a la venta.", log);
            } else {
                oAnimalesAD.venderAnimalTipo(abadia.getIdDeAbadia(), datosVenta.getNivelAnimal(), datosVenta.getTipoAnimal(), datosVenta.getFechaAnimal(), ctd);

                // Llenar datos de la nueva mercancia
                mercancia.setIdAbadia(abadia.getIdDeAbadia());
                mercancia.setIdAnimal(animal.getTipoAnimalid());
                mercancia.setIdAnimalTipo(animal.getTipoAnimalid());
                mercancia.setIdAnimalNivel(animal.getNivel());
                mercancia.setAnimalSalud(animal.getSalud());
                mercancia.setFecha_nacimiento(datosVenta.getFechaAnimal());
                mercancia.setEstado(0);
                mercancia.setFecha_inicial(CoreTiempo.getTiempoAbadiaString());
                mercancia.setFecha_final(CoreTiempo.getDiferenciaString(nDias));
                mercancia.setPrecio_inicial(precio);
                mercancia.setCtd_inicial(ctd);
                mercancia.setMercancia(datosVenta.getMercado());
                mercancia.setTipo(0); // Venta directa

                //si el precio de venta esta 3 veces por debajo del precio de mercado
                if (precio <= precioVentaCiudad) {
                    //en este punto también deberiamos crear un registro de mercado, pero cerrado...
                    //  mercados = new adMercado();
                    //  mercados.crearMercado(mercancia);
                    //  mercados.finalize();

                    gestionaVenta(mercancia, abadia, mercancia.getCtd_inicial(), mercancia.getPrecio_inicial(), animal.getNombre(), resource, con);

                    throw new CompraCiudadException("La ciudad ha ejercido su derecho a compra...", log);
                } else {
                    // Crear los datos
                    oMercadoAD = new adMercado(con);
                    oMercadoAD.crearMercado(mercancia);

                    // Mensajes
                    Mensajes msg = new Mensajes();

                    msg.setFechaAbadia(CoreTiempo.getTiempoAbadiaStringConHoras());
                    msg.setFechaReal(CoreTiempo.getTiempoRealStringConHoras());
                    msg.setIdDeAbadia(abadia.getIdDeAbadia());
                    msg.setIdDeMonje(-1);
                    msg.setIdDeRegion(abadia.getIdDeRegion());
                    msg.setMensaje(resource.getMessage("mensajes.info.ventaanimal", datosVenta.getCantidad(), animal.getNombre(), Utilidades.redondear(total)));

                    oMensajesAD = new adMensajes(con);
                    oMensajesAD.crearMensaje(msg);
                }
                // Restar del mercado
                //mercado.actualizarCtdMercado( pid, ctd);
            }
        }
        Contents.setLstVenta(alTable);
        return Contents;
    }

    public boolean venderAlimentosAgrupados(Abadia abadia, Usuario usuario, MercadoVentaActForm datosVenta, MessageResources resource, int[] lotes) throws AbadiaException {
        String sTrace = this.getClass() + ".venderAlimentosAgrupados()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        MercadoVenta Contents = new MercadoVenta();

        adAlimentoLotes alimentosLoteAD;
        adMensajes oMensajesAD;
        adMercado oMercadoAD;

        AlimentoLote alimento;
        Mercado mercancia;

        Mensajes msg;

        Connection con = null;

        ArrayList alTable = new ArrayList();
        double precio = Utilidades.formatStringToDouble(datosVenta.getPrecio());
        double precioVentaCiudad = Utilidades.formatStringToDouble(datosVenta.getPrecioVentaCiudad());
        int nDias = datosVenta.getDias();
        double total = precio * datosVenta.getCantidadDouble();

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS, Constantes.AUTOCOMIT_OF);
            // Eliminar los lotes
            alimentosLoteAD = new adAlimentoLotes(con);
            alimento = alimentosLoteAD.recuperarAlimentoLote(lotes[0]);
            if (alimento == null) {
                ConnectionFactory.rollbackTransaction(con);
                throw new ProductoNoEncontradoException(usuario.getNick() + " - adMercadoVenta. venderAlimentosAgrupados. No se han encontrado los lotes: " + lotes, null, log);
            }
            for (int lote : lotes) {
                alimentosLoteAD.eliminarAlimentoLote(lote);
            }
            // Calcular el final
            // Llenar datos de la nueva mercancia
            mercancia = new Mercado();
            mercancia.setIdAbadia(abadia.getIdDeAbadia());
            mercancia.setIdAlimento(alimento.getIdAlimento());
            mercancia.setEstado(0);
            mercancia.setFecha_inicial(CoreTiempo.getTiempoAbadiaString());
            mercancia.setFecha_final(CoreTiempo.getDiferenciaString(nDias));
            mercancia.setPrecio_inicial(precio);
            mercancia.setFecha_caduca(alimento.getFechaCaducidad());
            mercancia.setCtd_inicial((int) datosVenta.getCantidadDouble());
            mercancia.setMercancia(datosVenta.getMercado());
            mercancia.setTipo(0); // Venta directa

            if (precio <= precioVentaCiudad) {
                //en este punto también deberiamos crear un registro de mercado, pero cerrado...
                //  mercados = new adMercado();
                //  mercados.crearMercado(mercancia);
                //  mercados.finalize();
                gestionaVenta(mercancia, abadia, mercancia.getCtd_inicial(), mercancia.getPrecio_inicial(), alimento.getDescripcion(), resource, con);
                ConnectionFactory.commitTransaction(con);
                return true;
                //throw new CompraCiudadException("La ciudad ha ejercido su derecho a compra...", log);
            } else {
                // Crear los datos
                oMercadoAD = new adMercado(con);
                oMercadoAD.crearMercado(mercancia);

                msg = new Mensajes();
                msg.setFechaAbadia(CoreTiempo.getTiempoAbadiaStringConHoras());
                msg.setFechaReal(CoreTiempo.getTiempoRealStringConHoras());
                msg.setIdDeAbadia(abadia.getIdDeAbadia());
                msg.setIdDeMonje(-1);
                msg.setIdDeRegion(abadia.getIdDeRegion());
                msg.setMensaje(resource.getMessage("mensajes.info.ventaresto", Utilidades.redondear(datosVenta.getCantidadDouble()), alimento.getDescripcion(), Utilidades.redondear(total)));

                oMensajesAD = new adMensajes(con);
                oMensajesAD.crearMensaje(msg);
            }

            Contents.setLstVenta(alTable);
            ConnectionFactory.commitTransaction(con);
            return false;
        } catch (AbadiaException e) {
            //Rollback de la operación
            ConnectionFactory.rollbackTransaction(con);
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    /* Modificar el precio
     */

    public void getProcesoModificarProducto(Abadia abadia, Usuario usuario, DatosVentaActForm datosVenta, MessageResources resource) throws AbadiaException {
        String sTrace = this.getClass() + ".getProcesoModificarProducto()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        double precio = Utilidades.formatStringToDouble(datosVenta.getPrecio());
        int lPid = datosVenta.getProductoID();

        adMercado oMercadoAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS, Constantes.AUTOCOMIT_OF);
            oMercadoAD = new adMercado(con);
            oMercadoAD.actualizarPrecioMercado(lPid, precio);
            DatosVentaActForm datos = oMercadoAD.getDatosVenta(lPid, abadia);

            MercadoVentaActForm mercadoVentaForm = new MercadoVentaActForm();

            mercadoVentaForm.setId(String.valueOf(datosVenta.getProductoID()));
            mercadoVentaForm.setMercado(datos.getMercancia());
            mercadoVentaForm.setPrecio(datos.getPrecio());
            mercadoVentaForm.setCantidad(String.valueOf(datos.getCantidad()));
            mercadoVentaForm.setDescripcionProducto(datos.getDescripcionProducto());
            mercadoVentaForm.setDescripcionFamilia(datos.getDescripcionFamilia());
            mercadoVentaForm.setDuracionMercado(Integer.parseInt(CoreTiempo.getDiferenciaDias(datos.getFechaInicio(), datos.getFechaFin())));

            double precioVentaCiudad = Utilidades.formatStringToDouble(datosVenta.getPrecioVentaCiudad());

            if (precioVentaCiudad >= precio) {
                gestionaVentaCiudad(mercadoVentaForm, abadia, usuario, resource, con);
            }
            ConnectionFactory.commitTransaction(con);
        } catch (AbadiaException e) {
            //Rollback de la operación
            ConnectionFactory.rollbackTransaction(con);
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    /* Cancelar producto de la venta
    */

    public void getProcesoCancelarProducto(Abadia abadia, DatosVentaActForm datos, Usuario usuario, Connection con) throws AbadiaException {
        String sTrace = this.getClass() + ".getProcesoCancelarProducto()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adMercado oMercadosAD;
        adAlimentoLotes oAlimentoLotesAD;
        adRecurso oRecursosAD;
        adAnimal oAnimalesAD;
        adEdificio oEdificiosAD;

        Edificio edificio;
        Mercado mercancia;

        //Connection con = null;

        try {
            //con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS, Constantes.AUTOCOMIT_OF);
            oMercadosAD = new adMercado(con);
            mercancia = oMercadosAD.recuperarMercado(datos.getProductoID());

            if (mercancia == null) {
                throw new ProductoNoEncontradoException("adMercadoVenta. getProcesoCancelarProducto. No se encuentra el producto: " + datos.getProductoID(), null, log);
            }

            // Alimentos
            if (mercancia.getMercancia().equals(Constantes.MERCANCIA_ALIMENTOS_STR)) {
                AlimentoLote alimento = new AlimentoLote();

                oEdificiosAD = new adEdificio(con);
                edificio = oEdificiosAD.recuperarEdificioPorFamiliaAlimento(abadia, mercancia.getIdAlimento(), usuario);
                alimento.setIdEdificio(edificio.getIdDeEdificio());

                oMercadosAD.restarMercado(mercancia, datos.getCantidad());

                alimento.setFechaCaducidad(mercancia.getFecha_caduca());
                alimento.setFechaEntrada(mercancia.getFecha_inicial());
                alimento.setEstado(mercancia.getEstado());
                alimento.setCantidad(datos.getCantidad());
                alimento.setIdAlimento(mercancia.getIdAlimento());

                oAlimentoLotesAD = new adAlimentoLotes(con);
                oAlimentoLotesAD.crearAlimentoLote(alimento);
            } else if (mercancia.getMercancia().equals(Constantes.MERCANCIA_RECURSOS_STR)) {
                //elimino el producto del menrcado
                oMercadosAD.restarMercado(mercancia, datos.getCantidad());
                //incremento el recurso local

                oRecursosAD = new adRecurso(con);
                oRecursosAD.sumarRecurso(mercancia.getIdRecurso(), abadia.getIdDeAbadia(), datos.getCantidad());

            } else if (mercancia.getMercancia().equals(Constantes.MERCANCIA_ANIMALES_STR)) {

                Animal animal;

                oEdificiosAD = new adEdificio(con);
                edificio = oEdificiosAD.recuperarEdificioPorTipoAnimal(abadia, mercancia.getIdAnimalTipo(), usuario);

                oAnimalesAD = new adAnimal(con);
                animal = oAnimalesAD.recuperarAnimalTipo(mercancia.getIdAnimalTipo(), mercancia.getIdAnimalNivel());

                animal.setTipoAnimalid(mercancia.getIdAnimal());
                animal.setEdificioid(edificio.getIdDeEdificio());
                animal.setFecha_nacimiento(Utilidades.formatStringFromDB(mercancia.getFecha_nacimiento()));
                animal.setNivel((short) mercancia.getIdAnimalNivel());
                animal.setSalud(mercancia.getAnimalSalud());
                animal.setEstado(0);

                oMercadosAD.restarMercado(mercancia, datos.getCantidad());

                for (int iCount = 0; iCount < datos.getCantidad(); iCount++) {
                    oAnimalesAD.crearAnimal(animal);
                }
            }
            //ConnectionFactory.commitTransaction(con);
        } catch (AbadiaException e) {
            //Rollback de la operación
            //ConnectionFactory.rollbackTransaction(con);
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            //DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }


    public DatosVentaActForm mostrarDatosCancelarVenta(String p_sProductoId, Abadia p_oAbadia) throws AbadiaException {
        String sTrace = this.getClass() + ".mostrarDatosCancelarVenta()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adMercado oMercadoAD;
        adComisiones oComisionesAD;
        DatosVentaActForm afDatosVenta;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);

            oMercadoAD = new adMercado(con);
            afDatosVenta = oMercadoAD.getDatosVenta(Integer.parseInt(p_sProductoId), p_oAbadia);

            oComisionesAD = new adComisiones(con);
            afDatosVenta.setComision(oComisionesAD.getComision(p_oAbadia.getIdDeRegion(), Constantes.COMISION_TIPO_CANCELACION));

            return afDatosVenta;
        } catch (NumberFormatException e) {
            throw new ValidacionIncorrectaException(sTrace, e, log);
        } catch (AbadiaException e) {
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public HashMap<String, Serializable> cancelarVenta(DatosVentaActForm p_afDatosVenta, Abadia p_oAbadia, Usuario p_oUsuario, MessageResources p_oResource, ActionMessages p_amMensajes) throws AbadiaException {
        String sTrace = this.getClass() + ".cancelarVenta()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adMercado oMercadoAD;
        adEdificio oEdificioAD;
        adAlimentoLotes oAlimentoLotesAD;
        adAnimal oAnimalAD;
        adLibros oLibrosAD;
        adComisiones oComisionesAD;
        adRecurso oRecursoAD;

        Edificio edificio = null;
        DatosVentaActForm afDatosVenta;
        Mercado oMercancia;
        Libro oLibro;
        ArrayList<Notificacion> notas = new ArrayList<Notificacion>();
        HashMap<String, Serializable> hmRequest = new HashMap<String, Serializable>();
        int iAlmacenamiento = 0;
        double dValorComision, dValorOro;


        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS, Constantes.AUTOCOMIT_OF);

            double volumen = 0;
            oMercadoAD = new adMercado(con);
            oMercancia = oMercadoAD.recuperarMercado(p_afDatosVenta.getProductoID());
            afDatosVenta = oMercadoAD.getDatosVenta(p_afDatosVenta.getProductoID(), p_oAbadia);

            afDatosVenta.setCantidad(p_afDatosVenta.getCantidad());
            //control por si no encuentra el producto entre los que tiene a la venta.
            if (oMercancia == null) {
                notas.add(new Notificacion("javascript:history.back(1);", p_oResource.getMessage("mensajes.mercado.volver.cancelacion"), "mercado_historico.gif", (short) 1));
                hmRequest.put("notificacion", notas);
                p_amMensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.productonoencontrado"));
                ConnectionFactory.commitTransaction(con);
                return hmRequest;
            }
            if (afDatosVenta.getCantidad() > oMercancia.getCtd_actual()) {

                notas.add(new Notificacion("javascript:history.back(1);", p_oResource.getMessage("mensajes.mercado.volver.cancelacion"), "mercado_historico.gif", (short) 1));
                hmRequest.put("notificacion", notas);
                p_amMensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.productosinsuficientes"));
                ConnectionFactory.commitTransaction(con);
                return hmRequest;
            }
            if (afDatosVenta.getCantidad() < 0) {
                //si el importe es menor que cero (negativo)
                notas.add(new Notificacion("javascript:history.back(1);", p_oResource.getMessage("mensajes.mercado.volver.cancelacion"), "mercado_historico.gif", (short) 1));
                hmRequest.put("notificacion", notas);
                p_amMensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.cantidadnegativa"));
                ConnectionFactory.commitTransaction(con);
                return hmRequest;
            }

            if (afDatosVenta.getMercancia().equals(Constantes.MERCANCIA_ALIMENTOS_STR)) {
                oEdificioAD = new adEdificio(con);
                edificio = oEdificioAD.recuperarEdificioPorFamiliaAlimento(p_oAbadia, oMercancia.getIdAlimento(), p_oUsuario);
                oAlimentoLotesAD = new adAlimentoLotes(con);
                volumen = oAlimentoLotesAD.recuperarVolumen(oMercancia.getIdAlimento());
            } else if (afDatosVenta.getMercancia().equals(Constantes.MERCANCIA_ANIMALES_STR)) {
                oEdificioAD = new adEdificio(con);
                edificio = oEdificioAD.recuperarEdificioPorTipoAnimal(p_oAbadia, oMercancia.getIdAnimalTipo(), p_oUsuario);

                oAnimalAD = new adAnimal(con);
                volumen = oAnimalAD.recuperarVolumenAnimal(oMercancia.getIdAnimalTipo(), oMercancia.getIdAnimalNivel());
            }
            if (!afDatosVenta.getMercancia().equals(Constantes.MERCANCIA_RECURSOS_STR)) {
                oLibrosAD = new adLibros(con);
                oLibro = oLibrosAD.recuperaLibroTipo(300 + edificio.getIdDeTipoDeEdificio(), edificio.getIdDeAbadia());
                if (oLibro != null) {
                    iAlmacenamiento = Utilidades.recuperarIncrementoEspacioEdificioPorLibro(edificio.getIdDeTipoDeEdificio(), oLibro.getNivel(), edificio.getNivel());
/*                    iAlmacenamiento = (oLibro.getNivel() * edificio.getNivel()) * 10;*/
                }
                edificio.setAlmacenamiento(edificio.getAlmacenamiento() + iAlmacenamiento);
                if (edificio.getAlmacenamientoActual() >= edificio.getAlmacenamiento()) {
                    //el edificio ya esta por encima de sus posibilidades
                    notas.add(new Notificacion("javascript:history.back(1);", p_oResource.getMessage("mensajes.mercado.volver.cancelacion"), "mercado_historico.gif", (short) 1));
                    hmRequest.put("notificacion", notas);
                    p_amMensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.edificiolleno"));
                    ConnectionFactory.commitTransaction(con);
                    return hmRequest;
                } else {
                    double disponible = edificio.getAlmacenamiento() - edificio.getAlmacenamientoActual();
                    //si no cabe en el edificio...
                    if ((disponible - (afDatosVenta.getCantidad() * volumen)) < 0) {
                        notas.add(new Notificacion("javascript:history.back(1);", p_oResource.getMessage("mensajes.mercado.volver.cancelacion"), "mercado_historico.gif", (short) 1));
                        hmRequest.put("notificacion", notas);
                        p_amMensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.espacioinsuficiente"));
                        return hmRequest;
                    }
                }
            }
            //calculamos comisiones sobre la cancelacion
            //si la abbatia lleva menos de un mes de juego no paga comisiones.
            int comision = 0;
            int libre = CoreTiempo.getDiferenciaDiasInt(p_oAbadia.getFechaDeConstruccion(), CoreTiempo.getTiempoAbadiaString());
            if (libre > 30) {
                oComisionesAD = new adComisiones(con);
                comision = oComisionesAD.getComision(p_oAbadia.getIdDeRegion(), Constantes.COMISION_TIPO_CANCELACION);
            }
            if (comision > 0) {
                dValorComision = (Utilidades.formatStringToDouble(afDatosVenta.getPrecio()) * afDatosVenta.getCantidad() * comision) / 100;
                oRecursoAD = new adRecurso(con);
                //restamos el oro correspondiente a la comisión
                dValorOro = oRecursoAD.recuperarValorRecurso(Constantes.RECURSOS_ORO, p_oAbadia.getIdDeAbadia());
                if (dValorComision >= dValorOro) dValorComision = dValorOro;
                oRecursoAD.restarRecurso(Constantes.RECURSOS_ORO, p_oAbadia.getIdDeAbadia(), dValorComision);
                oComisionesAD = new adComisiones(con);
                oComisionesAD.sumarImporteComision(p_oAbadia.getIdDeRegion(), p_oAbadia.getIdDeAbadia(), CoreTiempo.getTiempoAbadiaString(), dValorComision);
            }

            getProcesoCancelarProducto(p_oAbadia, afDatosVenta, p_oUsuario, con);
            //mostrar mensaje con los datos de la cancelación
            oEdificioAD = new adEdificio(con);
            edificio = oEdificioAD.recuperarEdificioTipo(Constantes.EDIFICIO_MERCADO, p_oAbadia, p_oUsuario);

            //verificamos si la descripción está informada..
            if (afDatosVenta.getDescripcionProducto() == null) {
                afDatosVenta.setDescripcionProducto(oMercadoAD.getDescripcionProducto(afDatosVenta.getProductoID(), afDatosVenta.getMercancia()));
            }
            hmRequest.put("location", "/mostrarEdificio.do?clave=" + edificio.getIdDeEdificio());
            notas.add(new Notificacion("/cerrarPopUp.do", p_oResource.getMessage("mensajes.mercado.volver"), (short) 1));
            hmRequest.put("notificacion", notas);
            p_amMensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.cancelarventa", Utilidades.redondear(afDatosVenta.getCantidad()), afDatosVenta.getDescripcionProducto()));
            ConnectionFactory.commitTransaction(con);
            return hmRequest;

        } catch (AbadiaException e) {
            ConnectionFactory.rollbackTransaction(con);
            throw new AbadiaSQLException(sTrace, e, log);
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public HashMap<String, Serializable> inicioCompra(MercadoCompraForm p_afDatosCompra, String p_sProductoId, Abadia p_Abadia, Usuario p_Usuario, ActionMessages p_oMensajes) throws AbadiaException {
        String sTrace = this.getClass() + ".inicioCompra()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);


        adMercado oMercado;
        adComisiones oComisionesAD;
        adAbadia oAbadiaAD;
        adRecurso oRecursoAD;

        MercadoCompraForm afDatosCompra;
        HashMap<String, Serializable> hmRequest = new HashMap<String, Serializable>();
        int iRegionOrigen;
        ArrayList<ImpuestoRegion> alImpuestoRegion = new ArrayList<ImpuestoRegion>();

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            //VErificamos si el usuario lleva menos de un mes abbatia jugando
            oMercado = new adMercado(con);
            afDatosCompra = oMercado.getDatosCompra(p_Usuario, Integer.parseInt(p_sProductoId));

            /*
                datosVenta.setCantidadDisp(rs.getInt("CTD_ACTUAL"));
                datosVenta.setCantidad(rs.getInt("CTD_ACTUAL"));
                datosVenta.setPrecio(Utilidades.redondear(rs.getDouble("PRECIO_ACTUAL")));
                datosVenta.setPrecioTotal(Utilidades.redondear(rs.getDouble("PRECIO_ACTUAL") * rs.getInt("CTD_ACTUAL")));
                datosVenta.setMercanciaDesc(rs.getString("MERCANCIA"));
                datosVenta.setProductoID(idProducto);
                datosVenta.setAbadiaid(rs.getInt("ABADIAID"));
               productoCompra.setDescripcionFamilia(rs.getString("descfam"));
                productoCompra.setDescripcionProducto(rs.getString("descpro"));
                productoCompra.setDescripcionUnidad(rs.getString("descuni"));
                if (productoCompra.getMercanciaDesc().equals(Constantes.MERCANCIA_ALIMENTOS_STR)) {
                    productoCompra.setFechaCaducidad(rs.getString("fecha_caducidad"));
                }
             */
            p_afDatosCompra.setCantidadDisp(afDatosCompra.getCantidadDisp());
            p_afDatosCompra.setCantidad(afDatosCompra.getCantidad());
            p_afDatosCompra.setPrecio(afDatosCompra.getPrecio());
            p_afDatosCompra.setPrecioTotal(afDatosCompra.getPrecioTotal());
            p_afDatosCompra.setMercanciaDesc(afDatosCompra.getMercanciaDesc());
            p_afDatosCompra.setProductoID(afDatosCompra.getProductoID());
            p_afDatosCompra.setAbadiaid(afDatosCompra.getAbadiaid());
            p_afDatosCompra.setDescripcionFamilia(afDatosCompra.getDescripcionFamilia());
            p_afDatosCompra.setDescripcionProducto(afDatosCompra.getDescripcionProducto());
            p_afDatosCompra.setDescripcionUnidad(afDatosCompra.getDescripcionUnidad());
            p_afDatosCompra.setFechaCaducidad(afDatosCompra.getFechaCaducidad());

            if (p_afDatosCompra.getAbadiaid() != 0) {
                if (!GenericValidator.isBlankOrNull(p_afDatosCompra.getFechaCaducidad())) {
                    //si se trata de un alimento y ya está caducado...
                    if (Utilidades.compararStrDate(Utilidades.formatStringFromDB(CoreTiempo.getTiempoAbadiaString()), Utilidades.formatStringFromDB(p_afDatosCompra.getFechaCaducidad())).equals(Constantes.FECHA_MENOR)) {
                        //no dejamos comprar..
                        //notas.add(new Notificacion("mercado_compra.do", resource.getMessage("mensajes.mercado.volver")));
                        //request.getSession().setAttribute("notificacion", notas);
                        p_oMensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.productocaducado", Utilidades.formatStringFromDB(p_afDatosCompra.getFechaCaducidad())));
                        //saveMessages(request.getSession(), p_oMensajes);
                        //return mapping.findForward("mensajes");
                        return hmRequest;
                    }
                }
                oAbadiaAD = new adAbadia(con);
                iRegionOrigen = oAbadiaAD.getRegionAbadia(p_afDatosCompra.getAbadiaid());
                oComisionesAD = new adComisiones(con);
                if (iRegionOrigen != p_Abadia.getIdDeRegion()) {
                    alImpuestoRegion = oComisionesAD.getComisionesTransito(p_Abadia.getIdDeRegion(), iRegionOrigen);
                }
            }

            p_afDatosCompra.setImpuestoRegion(alImpuestoRegion);
            oRecursoAD = new adRecurso(con);
            hmRequest.put("monedas", Utilidades.redondear(oRecursoAD.recuperarValorRecurso(0, p_Abadia.getIdDeAbadia())));
            //hmRequest.put("DatosCompra", p_afDatosCompra);
            //request.getSession().setAttribute("MercadoCompraForm", p_afDatosCompra); // <--- nuevo???
            return hmRequest;
            //recurso.finalize();
            //return mapping.findForward("success");
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public HashMap<String, Serializable> getMercadoCompraAgrupado(Usuario p_oUsuario, ActionForm p_af) throws AbadiaException {
        String sTrace = this.getClass() + ".getMercadoCompraAgrupado()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adUtils oUtilsAD;
        adMercadoCompra oMercadoCompraAD;

        MercadoCompra oMercadoCompra;
        HashMap<String, Serializable> hmRequest;
        hmRequest = new HashMap<String, Serializable>();

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oUtilsAD = new adUtils(con);
            hmRequest.put("mercancia_tipo", oUtilsAD.getClaveValor(Constantes.TABLA_MERCANCIA));
            hmRequest.put("alimento_familia", (Serializable) CargaInicialFamiliasBBean.recuperarFamiliasPorIdioma(p_oUsuario.getIdDeIdioma()));
            // Recuperar la lista del mercado
            oMercadoCompraAD = new adMercadoCompra(con);
            oMercadoCompra = oMercadoCompraAD.getMercadoCompraAgrupado(p_oUsuario, p_af);
            hmRequest.put("MercadosListas", oMercadoCompra);
            return hmRequest;
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public HashMap<String, Serializable> inicioMercadoCompra(Abadia p_oAbadia, Usuario p_oUsuario, BuscarMercadoCompraForm p_afDatosFiltro, MessageResources p_oResource) throws AbadiaException {
        String sTrace = this.getClass() + ".inicioMercadoCompra()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adUtils oUtilsAD;
        adMercadoCompra oMercadoCompraAD;

        MercadoCompra oMercadoCompra;
        HashMap<String, Serializable> hmRequest = new HashMap<String, Serializable>();


        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);

            // Información de las combos de la página
            oUtilsAD = new adUtils(con);
            hmRequest.put("mercancia_tipo", oUtilsAD.getClaveValor(Constantes.TABLA_MERCANCIA));
            hmRequest.put("mercado", oUtilsAD.getClaveValor(Constantes.TABLA_MERCADO));
            hmRequest.put("compra_tipo", oUtilsAD.getClaveValor(Constantes.TABLA_TIPO_VENTA));
            hmRequest.put("filtro", oUtilsAD.getClaveValor(Constantes.TABLA_FILTRO));

            // Recuperar la lista del mercado
            oMercadoCompraAD = new adMercadoCompra(con);
            oMercadoCompra = oMercadoCompraAD.getMercadoCompra(p_oResource, p_oAbadia, p_oUsuario, p_afDatosFiltro);
            hmRequest.put("MercadosListas", oMercadoCompra);
            return hmRequest;

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public HashMap<String, Serializable> cargarHistoricoMercado(Abadia p_oAbadia, Usuario p_oUsuario, String p_szMercancia, MessageResources p_oResource) throws AbadiaException {
        String sTrace = this.getClass() + ".cargarHistoricoMercado()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adUtils oUtilsAD;
        adMercadoHistorico oMercadoHistoricoAD;

        HashMap<String, Serializable> hmRequest = new HashMap<String, Serializable>();


        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);

            // Información de las combos de la página

            oMercadoHistoricoAD = new adMercadoHistorico(con);
            hmRequest.put("compras", oMercadoHistoricoAD.getMercadoHistoricoCompras(p_oAbadia, p_oUsuario, p_oResource, p_szMercancia));
            hmRequest.put("ventas", oMercadoHistoricoAD.getMercadoHistoricoVentas(p_oAbadia, p_oUsuario, p_oResource, p_szMercancia));

            oUtilsAD = new adUtils(con);
            hmRequest.put("mercancia_tipo", oUtilsAD.getClaveValor(Constantes.TABLA_MERCANCIA));

            return hmRequest;

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public HashMap<String, Serializable> cargarHistoricoMercadoProducto(Abadia p_oAbadia, Usuario p_oUsuario, String p_szProducto, MessageResources p_oResource) throws AbadiaException {
        String sTrace = this.getClass() + ".cargarHistoricoMercado()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adUtils oUtilsAD;
        adMercadoHistorico oMercadoHistoricoAD;
        adMercado oMercadoAD;

        HashMap<String, Serializable> hmRequest = new HashMap<String, Serializable>();
        String sMercancia;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);

            oMercadoAD = new adMercado(con);
            sMercancia = oMercadoAD.recuperarMercanciaProducto(Integer.parseInt(p_szProducto));

            oMercadoHistoricoAD = new adMercadoHistorico(con);
            hmRequest.put("compras", oMercadoHistoricoAD.getMercadoHistoricoComprasProducto(p_oAbadia, p_oUsuario, p_oResource, sMercancia, p_szProducto));
            hmRequest.put("ventas", oMercadoHistoricoAD.getMercadoHistoricoVentasProducto(p_oAbadia, p_oUsuario, p_oResource, sMercancia, p_szProducto));

            oUtilsAD = new adUtils(con);
            hmRequest.put("mercancia_tipo", oUtilsAD.getClaveValor(Constantes.TABLA_MERCANCIA));

            return hmRequest;

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public MercadoVenta mercadoVentaInicio(Abadia p_oAbadia, Usuario p_oUsuario, MessageResources p_oResource) throws AbadiaException {
        String sTrace = this.getClass() + ".mercadoVentaInicio()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adMercadoVenta oMercadoVentaAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);

            oMercadoVentaAD = new adMercadoVenta(con);
            return oMercadoVentaAD.getMercadoVenta(p_oAbadia, p_oUsuario, p_oResource);

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public MercadoVenta recuperarProductos(int p_iAbadiaId, int p_iMercancia, Usuario p_oUsuario) throws AbadiaException {
        String sTrace = this.getClass() + ".recuperarProductos()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adMercadoVenta oMercadoVentaAD;
        adUtils oUtilsAD;

        MercadoVenta oMercadoVenta;
        ArrayList<Table> alMercancia;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);

            oUtilsAD = new adUtils(con);
            alMercancia = oUtilsAD.getClaveValor("MERCANCIA");

            oMercadoVentaAD = new adMercadoVenta(con);
            oMercadoVenta = oMercadoVentaAD.getProductos(p_iAbadiaId, p_iMercancia, p_oUsuario);
            oMercadoVenta.setMercanciaTipo(alMercancia);
            return oMercadoVenta;

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public MercadoAdminForm getDatosProductoVenta(Usuario p_oUsuario, int p_iProductoId) throws AbadiaException {
        String sTrace = this.getClass() + ".getDatosProductoVenta()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adMercadoAdministracion oMercadoAdministracionAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oMercadoAdministracionAD = new adMercadoAdministracion(con);
            return oMercadoAdministracionAD.getDatosProductoVenta(p_oUsuario, p_iProductoId);

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public MercadoAdminForm inicioMercadoAdmin(Abadia p_oAbadia, Usuario p_oUsuario, int p_iProductoId) throws AbadiaException {
        String sTrace = this.getClass() + ".inicioMercadoAdmin()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adJerarquiaEclesiastica oJerarquiaEclesiasticaAD;
        adCardenal oCardenalAD;
        adMercadoAdministracion oMercadoAdministracionAD;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);

            //Verificamos si la abbatia con la que se está entrando dispone de algun cardenal
            oJerarquiaEclesiasticaAD = new adJerarquiaEclesiastica(con);
            if (oJerarquiaEclesiasticaAD.tieneEminencia(p_oAbadia.getIdDeAbadia(), Constantes.JERARQUIA_CARDENAL)) {
                //Verificamos si entre las funciones del cardenal esta la de administrar el mercado
                oCardenalAD = new adCardenal(con);

                if (oCardenalAD.getFuncionCardenal(p_oAbadia) == Constantes.FUNCION_CARDENAL_MERCADO) {
                    //recuperamos los datos de venta del producto
                    oMercadoAdministracionAD = new adMercadoAdministracion(con);
                    return oMercadoAdministracionAD.getDatosProductoVenta(p_oUsuario, p_iProductoId);
                } else {
                    throw new CardenalSinFuncionException("Cardenal sin funciones", log);
                    //mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.info.cardenalsinfunciones"));
                    //saveMessages(request.getSession(), mensajes);
                    //af = mapping.findForward("mensajes");
                }
            } else {
                throw new NoEsCardenalException("No eres cardenal", log);
                //mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.info.debessercardenal"));
                //saveMessages(request.getSession(), mensajes);
                //af = mapping.findForward("mensajes");
            }

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public String actualizarMercadoAdmin(MercadoAdminForm p_afMercadoAdmin, MercadoAdminForm p_afMercadoAdminLocal,
                                         Usuario p_oUsuario, Abadia p_oAbadia, ActionMessages p_amMensages) throws AbadiaException {
        String sTrace = this.getClass() + ".actualizarMercadoAdmin()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adMercadoAdministracion oMercadoAdministracionAD;
        adJerarquiaEclesiastica oJerarquiaEclesiasticaAD;
        adCardenal oCarcenalAD;

        Context initCtx = null;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);

            if (p_oUsuario.getAdministrador() == 1) {
                oMercadoAdministracionAD = new adMercadoAdministracion(con);
                oMercadoAdministracionAD.actualizarMercadoAdmin(p_afMercadoAdmin, p_oUsuario);

                GestionDblClickBBean.resetUserTime(p_oUsuario.getNick());
/*
                try {
                    initCtx = new InitialContext();
                    initCtx.rebind(p_oUsuario.getNick(), null);
                    initCtx.close();
                } catch (NamingException e) {
                    throw new AbadiaNamingException("naming exception", e, log);  //To change body of catch statement use File | Settings | File Templates.
                }
*/

                return "actualizado";
            } else {
                //Verificamos si la abbatia con la que se está entrando dispone de algun cardenal
                oJerarquiaEclesiasticaAD = new adJerarquiaEclesiastica(con);

                if (oJerarquiaEclesiasticaAD.tieneEminencia(p_oAbadia.getIdDeAbadia(), Constantes.JERARQUIA_CARDENAL)) {

                    //Verificamos si entre las funciones del cardenal esta la de administrar el mercado
                    oCarcenalAD = new adCardenal(con);

                    if (oCarcenalAD.getFuncionCardenal(p_oAbadia) == Constantes.FUNCION_CARDENAL_MERCADO) {
                        //recuperamos los datos de venta del producto
                        oMercadoAdministracionAD = new adMercadoAdministracion(con);
                        p_afMercadoAdminLocal = oMercadoAdministracionAD.getDatosProductoVenta(p_oUsuario, p_afMercadoAdmin.getProductoId());
                        //validamos los valores recibidos del formulario
                        //el valor máximo establecido no puede superar el fijado por los administradores
                        if (Utilidades.formatStringToDouble(p_afMercadoAdmin.getPrecioMaximoC()) > Utilidades.formatStringToDouble(p_afMercadoAdminLocal.getPrecioMaximo())) {
                            p_amMensages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.preciomaximomuyalto"));
                        }
                        //el valor mínimo establecido no puede ser menor que el fijado por los administradores
                        if (Utilidades.formatStringToDouble(p_afMercadoAdmin.getPrecioMinimoC()) < Utilidades.formatStringToDouble(p_afMercadoAdminLocal.getPrecioMinimo())) {
                            p_amMensages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.aviso.preciominimomuybajo"));
                        }
                        //sii no se han registrado mensajes...
                        if (p_amMensages.isEmpty()) {
                            oMercadoAdministracionAD.actualizarMercadoAdmin(p_afMercadoAdmin, p_oUsuario);
                            return "actualizado";
                        } else {
                            //cargo en el fomulario de mercancia las actualizaciones del usuario y volvemos al formulario
                            //mostrando los mensajes de error
                            p_afMercadoAdminLocal.setPrecioMaximoC(p_afMercadoAdmin.getPrecioMaximoC());
                            p_afMercadoAdminLocal.setPrecioMinimoC(p_afMercadoAdmin.getPrecioMinimoC());
                            p_afMercadoAdminLocal.setCantidad(p_afMercadoAdmin.getCantidad());
                            return "success";
                        }
                    } else {
                        throw new CardenalSinFuncionException(sTrace + " Cardenal sin funciones", log);
                    }
                } else {
                    throw new NoEsCardenalException(sTrace + " No eres cardenal", log);
                }
            }

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }
    }

    public DatosVentaActForm modificarVentaInicio(int p_iProductoId, Abadia p_oAbadia) throws AbadiaException {
        String sTrace = this.getClass() + ".modificarVentaInicio()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adMercado oMercadoAD;
        adMercadoVenta oMercadoVentaAD;

        DatosVentaActForm afDatosVenta;
        double dPrecioMercado;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oMercadoAD = new adMercado(con);
            afDatosVenta = oMercadoAD.getDatosVenta(p_iProductoId, p_oAbadia);

            oMercadoVentaAD = new adMercadoVenta(con);
            dPrecioMercado = oMercadoVentaAD.getPrecioMercadoPorProducto(afDatosVenta.getProductoID(), afDatosVenta.getMercancia());

            afDatosVenta.setVenderACiudad(false);
            afDatosVenta.setPrecioVentaCiudad(Utilidades.redondear(dPrecioMercado / 4));

            return afDatosVenta;

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public Edificio modificarVenta(DatosVentaActForm p_afDatosVenta, Abadia p_oAbadia, Usuario p_oUsuario, MessageResources p_oResource) throws AbadiaException {
        String sTrace = this.getClass() + ".modificarVenta()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adEdificio oEdificioAD;
        adMercadoVenta oMercadoVentaAD;

        double dPrecioMercado;
        Edificio oEdificio;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oMercadoVentaAD = new adMercadoVenta(con);
            dPrecioMercado = oMercadoVentaAD.getPrecioMercadoPorProducto(p_afDatosVenta.getProductoID(), p_afDatosVenta.getMercancia());
            p_afDatosVenta.setPrecioVentaCiudad(Utilidades.redondear(dPrecioMercado / 4));

            if (p_afDatosVenta.isVenderACiudad()) {
                p_afDatosVenta.setPrecio(p_afDatosVenta.getPrecioVentaCiudad());
            }
            //si el precio fijado por el usuario supera el precio de mercado..
            if (Utilidades.formatStringToDouble(p_afDatosVenta.getPrecio()) > dPrecioMercado) {
                //fijamos el precio de venta al del mercado
                p_afDatosVenta.setPrecio(Utilidades.redondear(dPrecioMercado));
            }

            oEdificioAD = new adEdificio(con);
            oEdificio = oEdificioAD.recuperarEdificioTipo(Constantes.EDIFICIO_MERCADO, p_oAbadia, p_oUsuario);
            DBMSUtils.cerrarObjetoSQL(con);

            getProcesoModificarProducto(p_oAbadia, p_oUsuario, p_afDatosVenta, p_oResource);

            return oEdificio;
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public Alimento ventaAgrupadaInicio(DatosVentaAgrupadaActForm p_afDatosVenta, Abadia p_oAbadia, Usuario p_oUsuario) throws AbadiaException {
        String sTrace = this.getClass() + ".ventaAgrupadaInicio()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adEdificio oEdificioAD;
        adAlimentoLotes oAlimentoLotesAD;
        Edificio oEdificio;
        Alimento oAlimento;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oEdificioAD = new adEdificio(con);
            oEdificio = oEdificioAD.recuperarEdificioPorFamiliaAlimento(p_oAbadia, p_afDatosVenta.getIdProducto(), p_oUsuario);

            oAlimentoLotesAD = new adAlimentoLotes(con);
            oAlimento = oAlimentoLotesAD.recuperarDatosAlimento(p_afDatosVenta.getIdProducto(), p_oUsuario);
            p_afDatosVenta.setListaProductos(oAlimentoLotesAD.recuperarAlimentosPorID(p_afDatosVenta.getIdProducto(), oEdificio.getIdDeEdificio()));
            if (CoreTiempo.getDiferenciaDiasInt(p_oAbadia.getFechaDeConstruccion(), CoreTiempo.getTiempoAbadiaString()) < 30) {
                p_afDatosVenta.setUsuarioNuevo(true);
                p_afDatosVenta.setVenderACiudad(true);
            } else {
                //verificamos si el usuario dispone de mercado
                if (!oEdificioAD.existeEdificioTipo(p_oAbadia.getIdDeAbadia(), Constantes.EDIFICIO_MERCADO)) {
                    p_afDatosVenta.setVenderACiudad(true);
                    p_afDatosVenta.setExisteMercado(false);
                } else {
                    p_afDatosVenta.setVenderACiudad(false);
                    p_afDatosVenta.setExisteMercado(true);
                }
            }

            return oAlimento;

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public Edificio ventaAgrupadaConfirmacion(DatosVentaAgrupadaActForm p_afDatosVenta, Abadia p_oAbadia,
                                              Usuario p_oUsuario, MessageResources p_oResource, ActionMessages p_amMensajes,
                                              ArrayList<Notificacion> p_alNotas) throws AbadiaException {
        String sTrace = this.getClass() + ".ventaAgrupadaConfirmacion()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adEdificio oEdificioAD;
        adMercadoVenta oMercadoVentaAD;
        adMercado oMercadoAD;

        Edificio oEdificio;
        MercadoVentaActForm afMercadoVenta;
        double dVolumen;
        boolean bCompraMercado = false;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            oEdificioAD = new adEdificio(con);
            //VErificamos si el usuario lleva menos de un mes abbatia jugando
            if (CoreTiempo.getDiferenciaDiasInt(p_oAbadia.getFechaDeConstruccion(), CoreTiempo.getTiempoAbadiaString()) < 30) {
                p_afDatosVenta.setVenderACiudad(true);
            } else {
                //verificamos si el usuario dispone de mercado
                if (!oEdificioAD.existeEdificioTipo(p_oAbadia.getIdDeAbadia(), Constantes.EDIFICIO_MERCADO)) {
                    p_afDatosVenta.setVenderACiudad(true);
                }
            }

            String lotes;

            afMercadoVenta = new MercadoVentaActForm();
            afMercadoVenta.setMercado(Constantes.MERCANCIA_ALIMENTOS_STR);
            afMercadoVenta.setDias(5);

            lotes = String.valueOf(p_afDatosVenta.getSeleccion()[0]);
            for (int iCount = 1; p_afDatosVenta.getSeleccion().length > iCount; iCount++) {
                lotes = lotes + "," + p_afDatosVenta.getSeleccion()[iCount];
            }

            oMercadoVentaAD = new adMercadoVenta(con);
            afMercadoVenta.setCantidadDouble(oMercadoVentaAD.recuperarCantidadPorProductoAlimento(p_oAbadia.getIdDeAbadia(), lotes, p_oUsuario));
            afMercadoVenta.setId(String.valueOf(p_afDatosVenta.getSeleccion()[0]));
            double precio = oMercadoVentaAD.getPrecioMercado(afMercadoVenta);

            afMercadoVenta.setPrecioVentaCiudad(Utilidades.redondear(precio / 4));
            if (p_afDatosVenta.isVenderACiudad()) {
                afMercadoVenta.setPrecio(afMercadoVenta.getPrecioVentaCiudad());
            } else {
                //si el precio del producto supera el precio de mercado...
                if (Utilidades.formatStringToDouble(p_afDatosVenta.getPrecio()) > precio) {
                    afMercadoVenta.setPrecio(Utilidades.redondear(precio));
                } else {
                    afMercadoVenta.setPrecio(p_afDatosVenta.getPrecio());
                }
            }

            //Recuperar volumen disponible del edificio mercado
            oEdificio = oEdificioAD.recuperarEdificioTipo(Constantes.EDIFICIO_MERCADO, p_oAbadia, p_oUsuario);
            //determinar volumento total de productos que se pretende vender
            //si no se dispone de mercado...
            if (oEdificio == null) {
                //fijamos el precio de venta al de mercado para vender a la ciudad
                afMercadoVenta.setPrecio(p_afDatosVenta.getPrecioVentaCiudad());
            } else {
                oMercadoAD = new adMercado(con);
                dVolumen = oMercadoAD.recuperarVolumenProducto(Constantes.MERCANCIA_ALIMENTOS_STR, String.valueOf(p_afDatosVenta.getSeleccion()[0]), 0, 0);
                //si el almacenamiento actual + el volumen del producto * la cantidad supera la capacidad de almacenamiento del edificio...
                if (oEdificio.getAlmacenamiento() < (oEdificio.getAlmacenamientoActual() + (dVolumen * afMercadoVenta.getCantidadDouble()))) {
                    if (!p_afDatosVenta.isVenderACiudad()) {
                        p_amMensajes.add("msg", new ActionMessage("mensajes.aviso.sinespacio", oEdificio.getNombre()));
                        throw new EspacioInsuficienteEnCocina(sTrace, log);
                    }
                }
            }
            bCompraMercado = venderAlimentosAgrupados(p_oAbadia, p_oUsuario, afMercadoVenta, p_oResource, p_afDatosVenta.getSeleccion());

            //recuperamos el Edificio al que tenemos que ir al finalizar la compra
            oEdificio = oEdificioAD.recuperarEdificioPorFamiliaAlimento(p_oAbadia, p_afDatosVenta.getIdProducto(), p_oUsuario);

            //request.getSession().setAttribute("location", "/mostrarEdificio.do?clave=" + oEdificio.getIdDeEdificio());
            p_alNotas.add(new Notificacion("/cerrarPopUp.do", p_oResource.getMessage("mensajes.link.volver.edificio", oEdificio.getNombre()), oEdificio.getGrafico_visualizacion() + "_" + oEdificio.getNivel() + ".gif", (short) 1));
            //notas = edificioAD.recuperarLinksEdificios(abbatia, usuario);
            if (bCompraMercado) {
                p_amMensajes.add("msg", new ActionMessage("mensajes.info.comprapreciominimo"));
            } else {
                p_amMensajes.add("msg", new ActionMessage("mensajes.info.productoalaventa"));
            }

            return oEdificio;

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

    public HashMap<String, ArrayList<Tablas>> AltaProductoMercadoInicio(Usuario p_oUsuario) throws AbadiaException {
        String sTrace = this.getClass() + ".AltaProductoMercadoInicio()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adMercadoAdministracion oMercadoAD;

        HashMap<String, ArrayList<Tablas>> hmRequest = new HashMap<String, ArrayList<Tablas>>();
        ArrayList<Tablas> listaMercado;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);
            //recuperamos las listas de producto por mercancía.
            oMercadoAD = new adMercadoAdministracion(con);
            listaMercado = oMercadoAD.getProductosSinVender(Constantes.MERCANCIA_ALIMENTOS_STR, p_oUsuario);
            hmRequest.put("lista_alimentos", new ArrayList<Tablas>(listaMercado));
            listaMercado = oMercadoAD.getProductosSinVender(Constantes.MERCANCIA_ANIMALES_STR, p_oUsuario);
            hmRequest.put("lista_animales", new ArrayList<Tablas>(listaMercado));
            listaMercado = oMercadoAD.getProductosSinVender(Constantes.MERCANCIA_RECURSOS_STR, p_oUsuario);
            hmRequest.put("lista_recursos", new ArrayList<Tablas>(listaMercado));
            listaMercado = oMercadoAD.getProductosSinVender(Constantes.MERCANCIA_RELIQUIAS_STR, p_oUsuario);
            hmRequest.put("lista_reliquias", new ArrayList<Tablas>(listaMercado));
            listaMercado = oMercadoAD.getProductosSinVender(Constantes.MERCANCIA_LIBROS_STR, p_oUsuario);
            hmRequest.put("lista_libros", new ArrayList<Tablas>(listaMercado));

            return hmRequest;
        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }


    public MercadoAdminForm AltaProductoMercadoConfirmar(MercadoAdminForm mercadoForm, Usuario p_oUsuario) throws AbadiaException {
        String sTrace = this.getClass() + ".AltaProductoMercadoConfirmar()";
        String msgLog = "Entrando en metodo: " + sTrace;
        log.info(msgLog);

        adMercadoAdministracion oMercadoAD;

        HashMap<String, ArrayList<Tablas>> hmRequest = new HashMap<String, ArrayList<Tablas>>();
        ArrayList<Tablas> listaMercado;

        Connection con = null;

        try {
            con = ConnectionFactory.getConnection(Constantes.DB_CONEXION_ABADIAS);

            oMercadoAD = new adMercadoAdministracion(con);
            int idProducto = oMercadoAD.addMercadoAdmin(mercadoForm);
            //recuperamos los datos de venta del producto
            return oMercadoAD.getDatosProductoVenta(p_oUsuario, idProducto);

        } finally {
            DBMSUtils.cerrarObjetoSQL(con);
            msgLog = "Saliendo de metodo: " + sTrace;
            log.info(msgLog);
        }

    }

}
