package org.abbatia.action;

import org.abbatia.actionform.RegistroActForm;
import org.abbatia.bbean.UsuarioBBean;
import org.abbatia.bean.Email;
import org.abbatia.bean.Usuario;
import org.abbatia.core.CoreMail;
import org.abbatia.exception.CorreoNoEnviadoException;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EnviarPasswordAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionMessages mensajes = new ActionMessages();

        RegistroActForm datos = (RegistroActForm) form;

        UsuarioBBean oUsuarioBBean;

        Usuario oUsuario;

        try {
            if (GenericValidator.isBlankOrNull(datos.getNick())) {
                return mapping.findForward("success");
            } else {
                oUsuarioBBean = new UsuarioBBean();
                oUsuario = oUsuarioBBean.recuperarUsuarioPorNick(datos.getNick());

                if (oUsuario == null) {
                    mensajes.add("msg", new ActionMessage("mensajes.info.noexistenick", datos.getNick()));
                } else {
                    Email mail = new Email();
                    mail.setAsunto("Password Abbatia");
                    mail.setTo(oUsuario.getEmail());
                    mail.setMsg(oUsuario.getContrasena());
                    CoreMail.enviarEmail(mail);
                    mensajes.add("msg", new ActionMessage("mensajes.info.passwordenviado"));
                }
                saveMessages(request.getSession(), mensajes);
                return mapping.findForward("mensajes");
            }

        } catch (java.lang.SecurityException adbce) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.info.correonoenviado"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");
        } catch (CorreoNoEnviadoException e) {
            mensajes.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("mensajes.info.correonoenviado"));
            saveMessages(request.getSession(), mensajes);
            return mapping.findForward("mensajes");
        }
    }
}