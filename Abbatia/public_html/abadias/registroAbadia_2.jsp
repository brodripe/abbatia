<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
    <link rel="stylesheet" href="/theme/style-global.css" type="text/css"/>
    <link rel="stylesheet" href="/theme/style-revised.css" type="text/css"/>
    <title><bean:message key="registro.abadia.titulo"/></title>
</head>
<body>
<div id="container">
    <html:form action="/registroAbadia?accion=registro3">
        <html:hidden property="nombreAbadia"/>
        <display:table name="requestScope.AbadiaForm.monjes" uid="monjes_registro"
                       pagesize="30" class="contenidoedificio alignCenter">
            <display:caption><bean:message key="registro.abadia.tabla.cabecera"/></display:caption>
            <display:column property="idDeMonje" titleKey="registro.abadia.tabla.id"/>
            <display:column property="nombre" titleKey="registro.abadia.tabla.nombre" class="textLeft"/>
            <display:column property="primerApellido" titleKey="registro.abadia.tabla.ciudad" class="textLeft"/>
            <display:column property="jerarquia" titleKey="registro.abadia.tabla.jerarquia" class="textLeft"/>
            <display:column property="fechaDeNacimiento" titleKey="registro.abadia.tabla.fechanacimiento"/>
            <display:column property="fechaDeEntradaEnAbadia" titleKey="registro.abadia.tabla.fechaentrada"/>
        </display:table>
        <div class="alignCenter">
            <html:submit>
                <bean:message key="registro.abadia.aceptar"/>
            </html:submit>
        </div>
    </html:form>
    <!--publicidad inicio-->
    <jsp:include page="/varios/publicidad.jsp"/>
    <!--publicidad fin-->
</div>

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
