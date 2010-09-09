package org.abbatia.action;

import org.abbatia.actionform.AdminActForm;
import org.abbatia.bbean.AdminBBean;
import org.abbatia.bbean.GestionDblClickBBean;
import org.abbatia.bbean.PlanificadorBBean;
import org.abbatia.bean.Abadia;
import org.abbatia.bean.Usuario;
import org.abbatia.exception.AbadiaNotFoundException;
import org.abbatia.exception.AbadiaSQLException;
import org.abbatia.exception.MonjeNoEncontradoException;
import org.abbatia.exception.UsuarioNoEncontradoException;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.utils.Constantes;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.validator.GenericValidator;
import org.apache.log4j.Logger;
import org.apache.struts.action.*;

import javax.naming.Context;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public class AdminAction extends Action {
    private static Logger log = Logger.getLogger(AdminAction.class.getName());

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        ActionMessages mensajes = new ActionMessages();

        Abadia abadiaOri = (Abadia) request.getSession().getAttribute(Constantes.ABADIA);
        Usuario usuario = (Usuario) request.getSession().getAttribute(Constantes.USER_KEY);

        AdminActForm admin;

        String sPwd;
        String sValidado;

        AdminBBean oAdminBBean;
        Context initCtx;
        Abadia oAbadia;
        HashMap hmRequest;

        sValidado = (String) request.getSession().getAttribute("validado");
        if (sValidado == null) {
            sValidado = "0";
        }
        try {
            oAdminBBean = new AdminBBean();
            if (mapping.getParameter().equals("inicio")) {
                GestionDblClickBBean.resetUserTime(usuario.getNick());
/*
                initCtx = new InitialContext();
                initCtx.rebind(usuario.getNick(), null);
                initCtx.close();
*/

                if (sValidado.equals("1")) {
                    request.getSession().setAttribute("validado", "1");
                    return mapping.findForward("validado");
                }
                if (PropertyUtils.getSimpleProperty(form, "pwd") == null) {
                    return mapping.findForward("validar");
                } else if (GenericValidator.isBlankOrNull((String) PropertyUtils.getSimpleProperty(form, "pwd"))) {
                    return mapping.findForward("validar");
                } else {
                    sPwd = (String) PropertyUtils.getSimpleProperty(form, "pwd");
                    if (sPwd.equals(Constantes.VARIOS_PWD_ADMIN)) {
                        request.getSession().setAttribute("validado", "1");
                        return mapping.findForward("validado");
                    } else {
                        return mapping.findForward("validar");
                    }
                }
            } else if (mapping.getParameter().equals("confirmado") || sValidado.equals("1")) {
                admin = (AdminActForm) form;
                sValidado = (String) request.getSession().getAttribute("validado");
                if (GenericValidator.isBlankOrNull(sValidado)) sValidado = "0";
                log.debug("AdminAction. administrador: " + usuario.getAdministrador());
                if (usuario.getAdministrador() != Constantes.USUARIO_ADMINISTRADOR || sValidado.equals("0")) {
                    oAdminBBean.accesoIlegal(abadiaOri, usuario);
                    request.getSession().invalidate();
                    mensajes.add("msg", new ActionMessage("mensajes.aviso.noeresadministrador"));
                    mensajes.add("msg", new ActionMessage("mensajes.aviso.usuariobloqueado"));
                    saveMessages(request.getSession(), mensajes);
                    return mapping.findForward("mensajes");
                }

                String sAbadia = admin.getNombreAbadia();
                log.debug("nombre de abadía: " + sAbadia);

                if (admin.getAccion() == null) {
                    request.setAttribute("planificador", PlanificadorBBean.getEstadoPlanificador());
/*
                    if (PlanificadorBBean.getEstadoPlanificador())
                    {
                        PlanificadorBBean.desactivarPlanificador();
                    }else
                    {
                        PlanificadorBBean.activarPlanificador();
                    }
*/
/*
                    initCtx = new InitialContext();
                    request.setAttribute("planificador", initCtx.lookup(Constantes.ESTADO_PLANIFICADOR));
                    initCtx.close();
*/

                    return mapping.findForward("success");
                } else if (admin.getAccion().equals("restaurar")) {
                    oAbadia = oAdminBBean.restaurarAbadia(sAbadia);
                    mensajes.add("msg", new ActionMessage("mensajes.info.abadiarestablecida", oAbadia.getNombre()));
                    saveMessages(request.getSession(), mensajes);
                } else if (admin.getAccion().equals("suplantar")) {
                    hmRequest = oAdminBBean.suplantarAbadia(admin);
                    request.getSession().setAttribute(Constantes.ABADIA, hmRequest.get("abbatia"));
                    request.getSession().setAttribute(Constantes.USER_KEY, hmRequest.get("usuario"));
                    return mapping.findForward("inicio");
                } else if (admin.getAccion().equals("eliminar")) {
                    if (admin.isConfirmacion()) {
                        oAdminBBean.eliminarAbadia(sAbadia, mensajes);
                    } else {
                        mensajes.add("msg", new ActionMessage("mensajes.info.confirmacion"));
                    }

                    saveMessages(request.getSession(), mensajes);
                } else if (admin.getAccion().equals("habilidadesTodas")) {
                    if (admin.isConfirmacion()) {
                        oAdminBBean.restaurarHabilidades(mensajes);
                    } else {
                        mensajes.add("msg", new ActionMessage("mensajes.info.confirmacion"));
                    }
                    saveMessages(request.getSession(), mensajes);
                } else if (admin.getAccion().equals("habilidadesAbadia")) {
                    oAdminBBean.restaurarHabilidadesAbadia(admin, mensajes);
                    saveMessages(request.getSession(), mensajes);
                } else if (admin.getAccion().equals("arreglarMonje")) {
                    oAdminBBean.restaurarMonje(admin, mensajes);
                    saveMessages(request.getSession(), mensajes);
                } else if (admin.getAccion().equals("catastrofe")) {
                    oAdminBBean.lanzarCatastrofe(admin, mensajes);
                    saveMessages(request.getSession(), mensajes);
                } else if (admin.getAccion().equals("generar_libro")) {
                    oAdminBBean.generarLibro(admin);
                } else if (admin.getAccion().equals("recarga_singletons")) {
                    org.abbatia.bbean.singleton.CargaInicialActividadesBBean.reload();
                    org.abbatia.bbean.singleton.CargasInicialesFiltroLibrosBBean.reload();
                    org.abbatia.bbean.singleton.CargaInicialFamiliasBBean.reload();
                    org.abbatia.bbean.singleton.CargasInicialesDietasBBean.reload();
                }

            }
            return mapping.findForward("success");

        } catch (MonjeNoEncontradoException e) {
            mensajes.add("msg", new ActionMessage("error.general.monjenoencontrado"));
            saveMessages(request.getSession(), mensajes);
            log.error("AdminAction. MonjeNoEncontradoException", e);
            return mapping.findForward("success");
        } catch (AbadiaSQLException e) {
            mensajes.add("msg", new ActionMessage("registro.error.conexion"));
            saveMessages(request.getSession(), mensajes);
            log.error("AdminAction. AbadiaSQLException", e);
            return mapping.findForward("failure");
        } catch (AbadiaNotFoundException e) {
            mensajes.add("msg", new ActionMessage("error.general.abadianoencontrada"));
            saveMessages(request.getSession(), mensajes);
            log.error("AdminAction. AbadiaNotFoundException", e);
            return mapping.findForward("success");
        } catch (UsuarioNoEncontradoException e) {
            mensajes.add("msg", new ActionMessage("error.general.usuarionoencontrado"));
            saveMessages(request.getSession(), mensajes);
            log.error("AdminAction. UsuarioNoEncontradoException", e);

            return mapping.findForward("success");

        } catch (AbadiaException e) {
            mensajes.add("msg", new ActionMessage("registro.error.conexion"));
            saveMessages(request.getSession(), mensajes);
            log.error("AdminAction. AbadiaException", e);

            return mapping.findForward("success");
        }


    }
}
