<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<logic:notPresent name="usuario" scope="session">
    <logic:forward name="error"/>
</logic:notPresent>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
    <link rel="stylesheet" href="/theme/style-global.css" type="text/css"/>
    <link rel="stylesheet" href="/theme/style-revised.css" type="text/css"/>
    <title><bean:message key="eminencias.titulo"/> <br> ${requestScope.Titulo}</title>
</head>

<body>
<div id="container">
    <div id="content">
        <display:table name="requestScope.Contents" uid="lista_eminencias"
                       pagesize="30" class="contenidoedificio alignCenter"
                       requestURI="/buscar_eminencias.do">
            <display:caption><bean:message key="eminencias.titulo"/></display:caption>
            <display:column property="jerarquia" titleKey="eminencias.jerarquia" class="textLeft"/>
            <display:column titleKey="eminencias.nombre" class="textLeft">
                <c:set var="url"
                       value="/contratarSicario.do?confirmar=1&monjeid=${lista_eminencias.idMonje}&sicarioid=${requestScope.SicarioId}"/>
                <a href="${url}">${lista_eminencias.nombre} ${lista_eminencias.apellido1}</a>
            </display:column>
            <display:column property="abadia" titleKey="eminencias.nombre.abadia" class="textLeft"/>
            <display:column property="region" titleKey="eminencias.region" class="textLeft"/>
        </display:table>
        <!--publicidad inicio-->
        <jsp:include page="/varios/publicidad.jsp"/>
        <!--publicidad fin-->
    </div>
</div>


<!--Inicio Script para google-analytics-->

<script type="/javascript">
    var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
    document.write(unescape("%3Cscript src='" + gaJsHost +
                            "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
    var pageTracker = _gat._getTracker("UA-302272-1");
    pageTracker._initData();
    pageTracker._trackPageview();
</script>
<!--Fin Script para google-analytics-->

</body>
<!-- Mostrar la abadia congelada -->
<jsp:include page="/abadias/congelado.jsp"/>
</html>

