package org.abbatia.action;

import org.abbatia.actionform.DatosEdificioActForm;
import org.abbatia.bbean.EdificioBBean;
import org.abbatia.bbean.GestionDblClickBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Edificio;
import org.abbatia.bean.InicioContents;
import org.abbatia.bean.Usuario;
import org.abbatia.exception.AbadiaSinObispoException;
import org.abbatia.exception.NoCoincideRegionException;
import org.abbatia.exception.RecursosInsuficientesException;
import org.abbatia.utils.Constantes;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

public class CrearEdificioAction extends Action {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse httpServletResponse) throws Exception {

        //  Dado que en este punto, el usuario debe estar registrado...
        //  nos limitaremos a recoger de la sesión el objeto abbatia

        ActionMessages mensajes = new ActionMessages();
        Usuario oUsuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        Abadia oAbadia = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        MessageResources resource = getResources(request);

        DatosEdificioActForm formulario = (DatosEdificioActForm) actionForm;
        String clave = request.getParameter("clave");

        Edificio edificio = new Edificio();
        InitialContext initCtx;

        EdificioBBean oEdificioBBean;
        Abadia oAbadiaReceptora;
        boolean bParaOtraAbadia = false;
        ArrayList edificios;

        InicioContents datos = new InicioContents();

        String sAbadiaIdObispado = request.getParameter("abadiaid_obispado");
        // Mostrar los edificios de otro usuario si eres obispo
        try {
            oEdificioBBean = new EdificioBBean();
            // Recuperar la abbatia que tenemos que visualizar!!
            oAbadiaReceptora = oEdificioBBean.crearEdificioObispo(sAbadiaIdObispado, oAbadia, datos);
            //si la creación de edificio es para otra abadía...
            if (oAbadiaReceptora != oAbadia) {
                bParaOtraAbadia = true;
                request.setAttribute("Nombre", oAbadiaReceptora.getNombre());
                request.setAttribute("Abadiaid_Obispado", sAbadiaIdObispado);
            } else {
                request.setAttribute("DatosContents", datos);
            }

            // Acciones
            if (formulario.getAccion() == null) {
                if (clave == null) {
                    if (oAbadiaReceptora != oAbadia) {
                        edificios = oEdificioBBean.recuperarListaEdificiosParaConstruir(oAbadiaReceptora, oUsuario);
                    } else {
                        edificios = oEdificioBBean.recuperarListaEdificiosParaConstruir(oAbadia, oUsuario);
                    }

                    request.setAttribute("Edificios", edificios);
                    return mapping.findForward("lista");
                } else {
                    edificio = new Edificio();
                    request.setAttribute("DatosEdificioForm", oEdificioBBean.recuperarDatosEdificio(clave, edificio, oUsuario, bParaOtraAbadia));
                    return mapping.findForward("confirmar");
                }
            } else {
                //aceptar confirmacion de construcción.
                oEdificioBBean.crearEdificio(edificio, formulario, oAbadiaReceptora, oAbadia, oUsuario, resource);
                GestionDblClickBBean.resetUserTime(oUsuario.getNick());

/*
                initCtx = new InitialContext();
                initCtx.rebind(oUsuario.getNick(), null);
                initCtx.close();
*/

                return mapping.findForward("success");
            }
        } catch (AbadiaSinObispoException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.sinacceso"));
            saveMessages(request, mensajes);
            return mapping.findForward("mensajes");
        } catch (NoCoincideRegionException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.sinacceso"));
            saveMessages(request, mensajes);
            return mapping.findForward("mensajes");
        } catch (RecursosInsuficientesException ri) {
            mensajes.add("recursos", new ActionMessage("error.edificio.sinrecursos"));
            saveMessages(request.getSession(), mensajes);
            request.setAttribute("DatosEdificioForm", formulario);
            return (new ActionForward(mapping.getInput()));
        }
    }
}
