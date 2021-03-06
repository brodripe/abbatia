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
    <title><bean:message key="registro.abadia.titulo"/></title>
</head>
<body>
<html:form action="/registroAbadia?accion=registro2">
    <div id="container">
        <div id="registroUsuario">
            <h4><bean:message key="registro.abadia.titulo"/></h4>

            <div class="floatLeft" style="width:30%">
                <img src="/images/registro.gif" alt="<bean:message key='mensajes.mail.asunto'/>">
            </div>
            <div class="floatRight" style="width:70%">
                <dl>
                    <dt><bean:message key="registro.abadia.nombre"/></dt>
                    <dd>
                        <html:text property="nombreAbadia" maxlength="15" name="AbadiaForm" size="15"/>
                    </dd>
                    <dt><bean:message key="registro.abadia.region"/></dt>
                    <dd>
                        <html:select name="AbadiaForm" property="region">
                            <html:options collection="regiones" property="id" labelProperty="description"/>
                        </html:select>
                    </dd>
                    <dt><bean:message key="registro.abadia.orden"/></dt>
                    <dd>
                        <html:select name="AbadiaForm" property="orden">
                            <html:options collection="ordenes" property="id" labelProperty="description"/>
                        </html:select>
                    </dd>
                    <dt><bean:message key="registro.abadia.actividad"/></dt>
                    <dd>
                        <html:select name="AbadiaForm" property="actividad">
                            <html:options collection="actividades" property="id" labelProperty="description"/>
                        </html:select>
                    </dd>
                </dl>
            </div>
            <div class="break"></div>
            <div class="alignCenter topLineBorder">
                <html:submit><bean:message key="registro.abadia.siguiente"/></html:submit>
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

</body>
</html>
