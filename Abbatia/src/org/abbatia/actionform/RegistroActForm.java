package org.abbatia.actionform;

import org.abbatia.exception.base.AbadiaException;
import org.abbatia.exception.pwdCortaException;
import org.abbatia.exception.pwdSinMayusculasException;
import org.abbatia.exception.pwdSinMinusculasException;
import org.abbatia.exception.pwdSinNumerosException;
import org.abbatia.utils.Utilidades;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import javax.servlet.http.HttpServletRequest;

public class RegistroActForm extends ActionForm {

    private int id;                // codigo de usuario
    private int idioma;
    private String nick = null;
    private String pwd = null;   // la contraseña no se guarda por seguridad, además que sólo se necesita para el alta y el check
    private String pwd2 = null;
    private boolean conectado = false;
    private String nombre = null;
    private String apellido1 = null;
    private String apellido2 = null;
    private String email = null;
    private String web = null;
    private String ICQ = null;
    private int pais;
    private int edad;
    private int sexo;
    private boolean aceptaNormas;

    public boolean isAceptaNormas() {
        return aceptaNormas;
    }

    public void setAceptaNormas(boolean aceptaNormas) {
        this.aceptaNormas = aceptaNormas;
    }

    public int getSexo() {
        return sexo;
    }

    public void setSexo(int sexo) {
        this.sexo = sexo;
    }


    public int getPais() {
        return pais;
    }

    public void setPais(int pais) {
        this.pais = pais;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getId() {
        return id;
    }


    public void setIdioma(int idioma) {
        this.idioma = idioma;
    }


    public int getIdioma() {
        return idioma;
    }


    public void setNick(String nick) {
        this.nick = nick;
    }


    public String getNick() {
        return nick;
    }


    public void setPwd(String pwd) {
        this.pwd = pwd;
    }


    public String getPwd() {
        return pwd;
    }


    public void setPwd2(String pwd2) {
        this.pwd2 = pwd2;
    }


    public String getPwd2() {
        return pwd2;
    }


    public void setConectado(boolean conectado) {
        this.conectado = conectado;
    }


    public boolean isConectado() {
        return conectado;
    }


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public String getNombre() {
        return nombre;
    }


    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }


    public String getApellido1() {
        return apellido1;
    }


    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }


    public String getApellido2() {
        return apellido2;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public String getEmail() {
        return email;
    }


    public void setWeb(String web) {
        this.web = web;
    }


    public String getWeb() {
        return web;
    }


    public void setICQ(String ICQ) {
        this.ICQ = ICQ;
    }


    public String getICQ() {
        return ICQ;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (GenericValidator.isBlankOrNull(nombre)) {
            errors.add("nombre", new ActionMessage("error.nombre.required"));
        }
        if (GenericValidator.isBlankOrNull(apellido1)) {
            errors.add("apellido1", new ActionMessage("error.apellido.required"));
        }
        if (GenericValidator.isBlankOrNull(nick)) {
            errors.add("nick", new ActionMessage("error.nick.required"));
        }
        if (!GenericValidator.isEmail(email)) {
            errors.add("email", new ActionMessage("error.email.required"));
        }
        if (!GenericValidator.isBlankOrNull(pwd) && !pwd.equals(pwd2)) {
            errors.add("pwd", new ActionMessage("error.pwd.notequal"));
        }

        //verificar si la contraseña es "potente"
        try {
            if (!GenericValidator.isBlankOrNull(pwd)) {
                Utilidades.check(pwd);
            }
        } catch (pwdSinNumerosException e) {
            errors.add("pwd", new ActionMessage("mensajes.pwd.pocosegura.sinnumeros"));

        } catch (pwdSinMayusculasException e) {
            errors.add("pwd", new ActionMessage("mensajes.pwd.pocosegura.sinmayusculas"));
        } catch (pwdSinMinusculasException e) {
            errors.add("pwd", new ActionMessage("mensajes.pwd.pocosegura.sinminusculas"));
        } catch (pwdCortaException e) {
            errors.add("pwd", new ActionMessage("mensajes.pwd.pocosegura.corta"));
        } catch (AbadiaException e) {
            errors.add("pwd", new ActionMessage("mensajes.pwd.pocosegura.corta"));
        }

        return errors;
    }

    /**
     * Reset all bean properties to their default state.  This method is
     * called before the properties are repopulated by the controller servlet.
     * <p>
     * The default implementation does nothing.  Subclasses should override
     * this method to reset all bean properties to default values.
     * </p>
     * <p>
     * This method is <strong>not</strong> the appropriate place to initialize form values
     * for an "update" type page (this should be done in a setup Action).  You mainly
     * need to worry about setting checkbox values to false; most of the time you
     * can leave this method unimplemented.
     * </p>
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        // TODO:  Override this org.apache.struts.action.ActionForm method
        this.nombre = "";
        this.apellido1 = "";
        this.apellido2 = "";
        this.email = "";
        this.nick = "";
        super.reset(mapping, request);
    }


}
