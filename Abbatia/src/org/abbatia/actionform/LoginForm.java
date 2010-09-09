package org.abbatia.actionform;

import org.abbatia.utils.Utilidades;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import javax.servlet.http.HttpServletRequest;

public class LoginForm extends ActionForm {
    private String usuario = null;
    private String pwd = null;

    public LoginForm() {
    }

    /**
     * Validate the properties that have been set for this HTTP request,
     * and return an <code>ActionErrors</code> object that encapsulates any
     * validation errors that have been found.  If no errors are found,
     * return <code>null</code> or an <code>ActionErrors</code> object with
     * no recorded error messages.
     * <p/>
     * The default ipmlementation performs no validation and returns
     * <code>null</code>.  Subclasses must override this method to provide
     * any validation they wish to perform.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public ActionErrors validate(ActionMapping mapping,
                                 HttpServletRequest request) {

        ActionErrors errors = new ActionErrors();
        if (GenericValidator.isBlankOrNull(usuario)) {
            errors.add("usuario", new ActionMessage("error.username.required"));
        } else {
            usuario = Utilidades.normalizarTexto(usuario);
        }
        if (GenericValidator.isBlankOrNull(pwd)) {
            errors.add("password", new ActionMessage("error.password.required"));
        }
        return errors;
    }


    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }


    public String getUsuario() {
        return usuario;
    }


    public void setPwd(String pwd) {
        this.pwd = pwd;
    }


    public String getPwd() {
        return pwd;
    }
}
