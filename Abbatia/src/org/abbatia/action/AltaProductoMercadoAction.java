package org.abbatia.action;

import org.abbatia.actionform.MercadoAdminForm;
import org.abbatia.bbean.MercadoBBean;
import org.abbatia.bean.Usuario;
import org.abbatia.utils.Constantes;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.StringTokenizer;

public class AltaProductoMercadoAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        MercadoAdminForm mercadoForm = (MercadoAdminForm) form;

        MercadoBBean oMercadoBBean;

        HashMap hmRequest;
        MercadoAdminForm mercado = null;
        ActionForward af = null;

        oMercadoBBean = new MercadoBBean();
        //en primer lugar, validaremos el usuario que está accediendo a la pantalla de alta de productos
        if (usuario.getAdministrador() == 1 && mercadoForm.getAccion().equals("inicio"))
        //1 - Entra un usuario administrador
        {
            hmRequest = oMercadoBBean.AltaProductoMercadoInicio(usuario);
            //recuperamos las listas de producto por mercancía.
            request.setAttribute("lista_alimentos", hmRequest.get("lista_alimentos"));
            request.setAttribute("lista_animales", hmRequest.get("lista_animales"));
            request.setAttribute("lista_recursos", hmRequest.get("lista_recursos"));
            request.setAttribute("lista_reliquias", hmRequest.get("lista_reliquias"));
            request.setAttribute("lista_libros", hmRequest.get("lista_libros"));
            request.setAttribute("datosMercadoAdmin", mercado);
            af = mapping.findForward("success");
        } else if (usuario.getAdministrador() == 1 && mercadoForm.getAccion().equals("alta"))
        //2 - Entra un cardenal. Miraremos si está informado el idProducto y si corresponde con el ámbito que puede administrar el cardenal
        {
            //gestionamos el alta del producto en el mercado
            //Se ha selecionado un alimento para dar de alta
            if (!mercadoForm.getClaveAlimento().equals("0")) {
                mercadoForm.setAlimentoId(Integer.valueOf(mercadoForm.getClaveAlimento()));
                mercadoForm.setMercancia(Constantes.MERCANCIA_ALIMENTOS_STR);
            } else
                //Se ha selecionado un animal para dar de alta
                if (!mercadoForm.getClaveAnimal().equals("0")) {
                    StringTokenizer stDatos = new StringTokenizer(mercadoForm.getClaveAnimal(), ";");
                    mercadoForm.setAnimalTipo(Integer.valueOf(stDatos.nextToken()));
                    mercadoForm.setAnimalNivel(Integer.valueOf(stDatos.nextToken()));
                    mercadoForm.setMercancia(Constantes.MERCANCIA_ANIMALES_STR);
                } else
                    //Se ha seleccionado un recurso para dar de alta
                    if (!mercadoForm.getClaveRecurso().equals("0")) {
                        mercadoForm.setRecursoId(Integer.valueOf(mercadoForm.getClaveRecurso()));
                        mercadoForm.setMercancia(Constantes.MERCANCIA_RECURSOS_STR);
                    }
            mercado = oMercadoBBean.AltaProductoMercadoConfirmar(mercadoForm, usuario);

            request.setAttribute("datosMercadoAdmin", mercado);
            af = mapping.findForward("alta");

        }

        return af;

    }
}