package org.abbatia.action;

import org.abbatia.actionform.RegistroActForm;
import org.abbatia.bbean.UsuarioBBean;
import org.abbatia.bean.Usuario;
import org.abbatia.utils.Constantes;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public class GetDatosUsuarioAction extends Action {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        //este método obtendrá de la sesión el objeto usuario para cargarlo en el actionform correspondiente
        //y enviarlo para cargar un jsp con los datos de usuario para ofrecer la posibilidad de actualizarlos.
        //trato de obtener de la sesión el objeto usuario...
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);
        HashMap hmRequest;
        UsuarioBBean oUsuarioBBean;
        //si el usuario no existe en la sesión...
        if (usuario == null) {
            //llamamos a la pantalla de sesión caducada...
            return mapping.findForward("sesioncaducada");
        }
        RegistroActForm registro = new RegistroActForm();
        registro.setNombre(usuario.getNombre());
        registro.setApellido1(usuario.getPrimerApellido());
        registro.setApellido2(usuario.getSegundoApellido());
        registro.setEmail(usuario.getEmail());
        registro.setNick(usuario.getNick());
        registro.setPwd(usuario.getContrasena());
        registro.setPwd2(usuario.getContrasena());
        registro.setIdioma(usuario.getIdDeIdioma());
        registro.setEdad(usuario.getEdad());
        registro.setPais(usuario.getPais());
        registro.setSexo(usuario.getSexo());

        oUsuarioBBean = new UsuarioBBean();
        hmRequest = oUsuarioBBean.recuperarDatosUsuario();

        request.getSession().setAttribute("languages", hmRequest.get("languages"));
        request.getSession().setAttribute("paises", hmRequest.get("paises"));
        request.getSession().setAttribute("edades", hmRequest.get("edades"));
        request.getSession().setAttribute("sexo", hmRequest.get("sexo"));

        request.setAttribute("RegistroForm", registro);
        return mapping.findForward("success");
    }
}
