<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page pageEncoding="ISO-8859-1" contentType="text/html; charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>

<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<html:html xhtml="http://www.w3.org/1999/xhtml">
    <head>
        <title><bean:message key="usuario.condiciones.validacion.title"/></title>
        <link REL="STYLESHEET" HREF="/theme/style-global.css" TYPE="text/css"/>
        <link REL="STYLESHEET" HREF="/theme/style-revised.css" TYPE="text/css"/>
    </head>
    <body>
    <div id="container">
        <div id="detalleCompra">
            <html:form action="/AceptarNormas">
                <div class="alignCenter downSpace">
                    <h4><bean:message key="usuario.condiciones.validacion.title"/></h4>
                    <html:checkbox name="AceptacionNormas" property="estadoCheck" titleKey="usuario.acepto.normas">
                        <bean:message key="usuario.acepto.normas"/>
                    </html:checkbox>
                    <a href="/condiciones">
                        <bean:message key="usuario.condiciones.link"/>
                    </a>
                </div>
                <div class="floatLeft topLineBorder downSpace topSpace">
                    <bean:message key="usuario.condiciones.textolargo"/>
                </div>
                <div class="break"></div>
                <div class="alignCenter topLineBorder downSpace topSpace">
                    <html:submit>
                        <bean:message key="mensajes.confirmacion.boton.aceptar"/>
                    </html:submit>
                </div>
            </html:form>
        </div>
    </div>
    </body>

    <%--Modulo de estadisticas de google--%>
    <script type="text/javascript">
        var _gaq = _gaq || [];
        _gaq.push(['_setAccount', 'UA-302272-1']);
        _gaq.push(['_setDomainName', 'abbatia.net']);
        _gaq.push(['_trackPageview']);
        (function() {
            var ga = document.createElement('script');
            ga.type = 'text/javascript';
            ga.async = true;
            ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
            var s = document.getElementsByTagName('script')[0];
            s.parentNode.insertBefore(ga, s);
        })();
    </script>
    <!--Fin Script para google-analytics-->

    <%--</body>--%>
</html:html>
