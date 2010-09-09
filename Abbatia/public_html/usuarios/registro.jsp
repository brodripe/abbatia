<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
    <link rel="stylesheet" href="/theme/style-global.css" type="text/css"/>
    <link rel="stylesheet" href="/theme/style-revised.css" type="text/css"/>
    <title><bean:message key="index.header"/></title>
    <script type="text/javascript" src="/theme/validator.js"></script>
    <html:javascript formName="RegistroForm" dynamicJavascript="true" staticJavascript="false"/>
    <script type="text/javascript">
        function activarSubmit(valorCheck)
        {
            if (valorCheck.value)
            {
                document.getElementById('Registrar').disabled = false;
            } else
            {
                document.getElementById('Registrar').disabled = true;
            }
        }
    </script>
</head>
<body>
<html:form action="/registrar?accion=registrar" onsubmit="return validateRegistroForm(this);">
    <div id="container">
        <div id="registroUsuario">
            <h4><bean:message key="registro.abadia.titulo"/></h4>

            <div class="floatLeft image" style="width:30%">
                    <%--<img src="/images/registro.gif" alt="<bean:message key='mensajes.mail.asunto'/>">--%>
                    <%--                <html:img src="/images/registro.gif" altKey="mensajes.mail.asunto"/>--%>
            </div>
            <div class="floatRight" style="width:70%">
                    <%--<dl>--%>
                    <%-- <label for="usuario"><bean:message key="login.usuario"/></label><html:text property="usuario"
                                                                                               styleId="usuario"/>--%>
                <div class="row">
                    <label for="nombre"><bean:message key="usuario.nombre"/></label>
                    <html:text styleClass="field" property="nombre" altKey="usuario.nombrealt" maxlength="10"
                               name="RegistroForm" size="15" styleId="nombre"/>
                </div>
                    <%--
                                        <dt><label for="nombre"><bean:message key="usuario.nombre"/></label></dt>
                                        <dd>
                                            <html:text property="nombre" altKey="usuario.nombrealt" maxlength="10"
                                                       name="RegistroForm" size="15" styleId="nombre"/>
                                        </dd>
                    --%>
                <div class="row">
                    <label for="apellido1"><bean:message key="usuario.apellido1"/></label>
                    <html:text styleClass="field" maxlength="15" property="apellido1" altKey="usuario.apellido1alt"
                               name="RegistroForm" size="20" styleId="apellido1"/>
                </div>
                    <%--
                                        <dt><label for="apellido1"><bean:message key="usuario.apellido1"/></label></dt>
                                        <dd>
                                            <html:text maxlength="15" property="apellido1" altKey="usuario.apellido1alt"
                                                       name="RegistroForm" size="20" styleId="apellido1"/>
                                        </dd>
                    --%>
                <div class="row">
                    <label for="apellido2"><bean:message key="usuario.apellido2"/></label>
                    <html:text styleClass="field" maxlength="15" property="apellido2" altKey="usuario.apellido2alt"
                               name="RegistroForm" size="20" styleId="apellido2"/>

                </div>
                    <%--
                                        <dt><label for="apellido2"><bean:message key="usuario.apellido2"/></label></dt>
                                        <dd>
                                            <html:text maxlength="15" property="apellido2" altKey="usuario.apellido2alt"
                                                       name="RegistroForm" size="20" styleId="apellido2"/>
                                        </dd>
                    --%>
                <div class="row">
                    <label for="edad"><bean:message key="usuario.edad"/></label>
                    <html:select styleClass="field" name="RegistroForm" property="edad">
                        <html:options collection="edades" property="id" labelProperty="description"/>
                    </html:select>
                </div>
                    <%--
                                        <dt><label for="edad"><bean:message key="usuario.edad"/></label></dt>
                                        <dd>
                                            <html:select name="RegistroForm" property="edad">
                                                <html:options collection="edades" property="id" labelProperty="description"/>
                                            </html:select>
                                        </dd>
                    --%>
                <div class="row">
                    <label for="sexo"><bean:message key="usuario.sexo"/></label>
                    <html:select styleClass="field" name="RegistroForm" property="sexo">
                        <html:options collection="sexo" property="id" labelProperty="description"/>
                    </html:select>
                </div>
                    <%--
                                        <dt><label for="sexo"><bean:message key="usuario.sexo"/></label></dt>
                                        <dd>
                                            <html:select name="RegistroForm" property="sexo">
                                                <html:options collection="sexo" property="id" labelProperty="description"/>
                                            </html:select>
                                        </dd>
                    --%>
                <div class="row">
                    <label for="pais"><bean:message key="usuario.pais"/></label>
                    <html:select styleClass="field" name="RegistroForm" property="pais" value="194">
                        <html:options collection="paises" property="id" labelProperty="description"/>
                    </html:select>
                </div>
                    <%--
                                        <dt><label for="pais"><bean:message key="usuario.pais"/></label></dt>
                                        <dd>
                                            <html:select name="RegistroForm" property="pais" value="194">
                                                <html:options collection="paises" property="id" labelProperty="description"/>
                                            </html:select>
                                        </dd>
                    --%>
                <div class="row">
                    <label for="nick"><bean:message key="usuario.alias"/></label>
                    <html:text styleClass="field" maxlength="10" property="nick" alt="Alias para el juego"
                               name="RegistroForm"
                               size="10" styleId="nick"/>
                </div>
                    <%--
                                        <dt><label for="nick"><bean:message key="usuario.alias"/></label></dt>
                                        <dd>
                                            <html:text maxlength="10" property="nick" alt="Alias para el juego" name="RegistroForm"
                                                       size="10" styleId="nick"/>
                                        </dd>
                    --%>
                <div class="row">
                    <label for="email"><bean:message key="usuario.email"/></label>
                    <html:text styleClass="field" property="email" alt="Correo Electrónico" maxlength="40"
                               name="RegistroForm"
                               size="40" styleId="email"/>
                </div>
                    <%--
                                        <dt><label for="email"><bean:message key="usuario.email"/></label></dt>
                                        <dd>
                                            <html:text property="email" alt="Correo Electrónico" maxlength="40" name="RegistroForm"
                                                       size="40" styleId="email"/>
                                        </dd>
                    --%>
                <div class="row">
                    <label for="idioma"><bean:message key="usuario.idioma"/></label>
                    <html:select styleClass="field" name="RegistroForm" property="idioma">
                        <html:options collection="languages" property="id" labelProperty="description"/>
                    </html:select>
                </div>
                    <%--
                                        <dt><label for="idioma"><bean:message key="usuario.idioma"/></label></dt>
                                        <dd>
                                            <html:select name="RegistroForm" property="idioma">
                                                <html:options collection="languages" property="id" labelProperty="description"/>
                                            </html:select>
                                        </dd>
                    --%>
                    <%--</dl>--%>
                <div class="break"></div>
                <div class="alignCenter topSpace">
                    <html:checkbox name="RegistroForm" property="aceptaNormas" titleKey="usuario.acepto.normas"
                                   onchange="activarSubmit(this)">
                        <bean:message key="usuario.acepto.normas"/>
                    </html:checkbox>
                    <a href="/condiciones">
                        <bean:message key="usuario.condiciones.link"/>
                    </a>
                </div>
            </div>
            <div class="break"></div>
            <div class="alignCenter topLineBorder">
                <html:submit styleId="Registrar" disabled="true"><bean:message key="usuario.registrar"/></html:submit>
            </div>

        </div>
        <div class="break"></div>
        <div class="alignCenter">
            <html:errors/>
        </div>

        <!--publicidad inicio-->
        <jsp:include page="/varios/publicidad.jsp"/>
        <!--publicidad fin-->
    </div>
</html:form>

<!--Inicio Script para google-analytics-->
<script type="text/javascript">
    var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
    document.write(unescape("%3Cscript src='" + gaJsHost +
                            "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
</script>
<script type="text/javascript">
    var pageTracker = _gat._getTracker("UA-302272-1");
    pageTracker._initData();
    pageTracker._trackPageview();
</script>
<!--Fin Script para google-analytics-->

</body>
</html>
