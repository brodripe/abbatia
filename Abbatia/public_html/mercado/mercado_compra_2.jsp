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

<html>
<head>
    <title>Pantalla mercado venta</title>
    <link REL="STYLESHEET" HREF="/theme/styles.css" TYPE="text/css"/>
</head>
<body topmargin="2" leftmargin="2" bgcolor="#E1C08B" text="#000000">
<center>
    <p>&nbsp;</p>
    <logic:iterate id="lista" name="MercadosListas" property="lstCompra">
        <bean:write property="description" name="lista" filter="false"/>
    </logic:iterate>
    <p align="center">
        <html:link action="/mercado_compra">Volver al mercado</html:link>
    </p>
</center>
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
