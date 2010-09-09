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

<head>
    <title><bean:message key="mercado.compra.titulo"/></title>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <link REL="STYLESHEET" HREF="/theme/style-global.css" TYPE="text/css"/>
    <link REL="STYLESHEET" HREF="/theme/style-revised.css" TYPE="text/css"/>
    <script type="text/javascript" src="/theme/overlib/overlib.js"><!-- overLIB (c) Erik Bosrup --></script>
</head>

<html:html xhtml="http://www.w3.org/1999/xhtml">
    <body>
    <div id="container">
        <div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
        <div id="content">
            <display:table name="requestScope.AltasPendientes" uid="altas_pendientes" pagesize="30"
                           export="true" requestURI="/controlAltas.do"
                           sort="list" class="contenidoedificio alignCenter">
                <display:setProperty name="export.xsl.filename" value="altas_pendientes.xsl"/>
                <display:setProperty name="export.pdf.filename" value="altas_pendientes.pdf"/>
                <display:setProperty name="export.csv.filename" value="altas_pendientes.csv"/>
                <display:caption><bean:message key="usuario.altas.pendientes"/></display:caption>
                <display:column property="idDeUsuario" titleKey="usuario.id" sortable="true"/>
                <display:column property="nick" titleKey="usuario.nick"/>
                <display:column titleKey="usuario.nombre" property="nombre" sortable="true" class="textLeft"/>
                <display:column titleKey="usuario.apellido1" property="primerApellido" sortable="true"
                                class="textLeft"/>
                <display:column titleKey="usuario.apellido2" property="segundoApellido" sortable="true"
                                class="textLeft"/>
                <display:column titleKey="usuario.email" property="email" sortable="true" class="textLeft"/>
                <display:column titleKey="usuario.ip" property="ipActual" sortable="true"/>
                <display:column titleKey="usuario.fechaalta" property="fecha_alta" sortable="true"/>
                <display:column titleKey="usuario.edad" property="edadDesc" sortable="true" class="textLeft"/>
                <display:column titleKey="mensajes.inbox.table.seleccion">
                    <html:link action="/autorizarAlta" paramName="altas_pendientes" paramId="clave"
                               paramProperty="idDeUsuario" onclick="return confirmarAlta(${altas_pendientes.edad})"
                               onmouseover="return overlib('This is an ordinary popup.');">
                        <html:img border="0" page="/images/iconos/16/autorizar.jpg"
                                  altKey="tooltip.administracion.alta.ok"/>
                    </html:link>
                </display:column>
                <display:column titleKey="mensajes.inbox.table.seleccion">
                    <html:link action="/denegarAlta" paramName="altas_pendientes" paramId="clave"
                               paramProperty="idDeUsuario">
                        <html:img border="0" page="/images/iconos/16/cancelar.jpg"
                                  altKey="tooltip.administracion.alta.ko"/>
                    </html:link>
                </display:column>
            </display:table>
        </div>
    </div>
    </body>
</html:html>

<script type="text/javascript">
    var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
    document.write(unescape("%3Cscript src='" + gaJsHost +
                            "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
    function confirmarAlta(p_Edad)
    {
        if (p_Edad <= 1)
        {
            return confirm('Recuerda que la autorización de menores precisa de autorización paterna');
        } else return true;
    }

</script>
<script type="text/javascript">
    var pageTracker = _gat._getTracker("UA-302272-1");
    pageTracker._initData();
    pageTracker._trackPageview();
</script>
