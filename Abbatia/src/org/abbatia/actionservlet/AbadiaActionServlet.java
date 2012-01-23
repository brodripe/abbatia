package org.abbatia.actionservlet;

import org.abbatia.bbean.UtilsBBean;
import org.abbatia.bbean.singleton.CargaInicialActividadesBBean;
import org.abbatia.bbean.singleton.CargaInicialFamiliasBBean;
import org.abbatia.bbean.singleton.CargasInicialesDietasBBean;
import org.abbatia.bbean.singleton.CargasInicialesFiltroLibrosBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Usuario;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.struts.action.ActionServlet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;
import java.util.Timer;


public class AbadiaActionServlet extends ActionServlet {
    private static Logger log = Logger.getLogger(AbadiaActionServlet.class.getName());
    private static Timer tempo = null;

    public void init() throws ServletException {
        //init = true;
        String prefix = getServletContext().getRealPath("/");
        UtilsBBean oUtilsBBean;
        log.debug("getRealPath: " + prefix);
        try {
            Context initCtx = new InitialContext();
            initCtx.rebind(Constantes.PATH_BASE, prefix);
//            initCtx.rebind(Constantes.ESTADO_PLANIFICADOR, "0");

            //se deberia cargar la tabla de excepciones con nombres de excepción y máximo de iteraciones permitidas.
            oUtilsBBean = new UtilsBBean();
            initCtx.rebind(Constantes.EXCEPCIONES_CONTROLADAS, oUtilsBBean.recuperarTablaExcepciones());
            initCtx.close();

            //cargamos tabla de literales en momoria
            oUtilsBBean.cargarTablaLiterales();


        } catch (NamingException ne) {
            log.error("AbadiaActionServlet. Init. NamingException.", ne);
        } catch (AbadiaException e) {
            log.error("AbadiaActionServlet. Init. AbadiaException.", e);
        }


        String file = getInitParameter("log4j-init-file");
        // if the log4j-init-file is not set, then no point in trying
        if (file != null) {
            PropertyConfigurator.configure(prefix + file);
        }
        Locale.setDefault(new Locale("es", "ES"));

        log.info("Inicio Carga Singletons");
        CargaInicialActividadesBBean.getInstance();
        CargaInicialFamiliasBBean.getInstance();
        CargasInicialesDietasBBean.getInstance();
        CargasInicialesFiltroLibrosBBean.getInstance();
        log.info("Fin Carga Singletons");


        super.init();

    }

    protected void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String sDestino = request.getRequestURI();
        Abadia abadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        //verificamos si el usuario trata de lanzar una invocación "a saco" de la misma página
        //presumiblemente para hacer trampas provocadas por el rendimiento del servidor
        if (!isValidFlow(usuario, abadia, sDestino)) {
            log.debug("Flujo no válido, direcciono a sesionCadudada.do");
            //flujoOK = false;
            response.sendRedirect("sesionCaducada.do");
        } else {

            if (usuario != null) {
                if (request.getParameter("pagesize") != null) {
                    request.getSession().setAttribute("pagesize", Integer.valueOf(request.getParameter("pagesize")));
                }
                log.debug("--> Entrando - " + usuario.getNick() + " -> " + sDestino);
                super.process(request, response);
                log.debug("--> Saliendo - " + usuario.getNick() + " -> " + sDestino);
            } else {
                super.process(request, response);
            }
        }
    }

    protected void finalize() throws Throwable {
        tempo.cancel();
        super.finalize();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void destroy() {

        tempo.cancel();
        super.destroy();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public boolean isValidFlow(Usuario usuario, Abadia abadia, String sDestino) {
        //transacciones que pueden hacerse sin abbatia
        if (usuario != null && abadia == null) {
            if (sDestino.endsWith("registroAbadia.do")) {
                return true;
            }
        }

        //transacciones que pueden hacerse sin usuario ni abbatia...
        if (usuario == null || abadia == null) {
            log.debug("El objeto Usuario no está cargado en la sesión. ");
            /*
               Se tiene que crear un archivo en XML o lo que sea con las páginas válidas
             sin control de session.
            */
            if (sDestino.endsWith("login.do")
                    || sDestino.endsWith("loginFrm.do")
                    || sDestino.endsWith("Publicidad.do")
                    || sDestino.endsWith("registro.do")
                    || sDestino.endsWith("index_main.do")
                    || sDestino.endsWith("registrar.do")
                    || sDestino.endsWith("supporters.do")
                    || sDestino.endsWith("desconectar.do")
                    || sDestino.endsWith("sesionCaducada.do")
                    || sDestino.endsWith("cabecera.do")
                    || sDestino.endsWith("idioma.do")
                    || sDestino.endsWith("enviar_password.do")
                    || sDestino.endsWith("inicio.do")
                    || sDestino.endsWith("MostrarMensaje.jsp")
                    || sDestino.endsWith("index_main.do")
                    || sDestino.endsWith("index_main_menu.do")
                    || sDestino.endsWith("proteccion_datos.do")
                    || sDestino.endsWith("equipo_desarrollo.do")
                    || sDestino.endsWith("ayudas.do")
                    || sDestino.endsWith("faq_normas.do")
                    || sDestino.endsWith("faq_mercados.do")
                    || sDestino.endsWith("faq_monjes.do")
                    || sDestino.endsWith("faq_animales.do")
                    || sDestino.endsWith("faq_edificios.do")
                    || sDestino.endsWith("condiciones")
                    || sDestino.endsWith("condiciones.do")
                    || sDestino.endsWith("/varios/publicidad.do")
                    || sDestino.endsWith("faq_consejos.do")
                    || sDestino.endsWith("index.do")) {
                return true;

            } else {
                return false;
            }
        }
        return true;
    }
}
